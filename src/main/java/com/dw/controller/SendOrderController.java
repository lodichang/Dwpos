package com.dw.controller;

import com.ablegenius.netty.client.MessageNonAck;
import com.ablegenius.netty.common.Message;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.netty.NettyClient;
import com.dw.print.PrintRxTx;
import com.dw.print.PrintStyleUtils;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.MainView;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import net.sf.ehcache.search.parser.MCriteria;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.SpanShapeRenderer;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ablegenius.netty.common.NettyCommonProtocol.REQUEST;
import static com.dw.enums.HoldOnEnum.*;

@Component
public class SendOrderController {
    @Autowired
    private NettyClient nettyClient;
    @Value("${STATION_ID}")
    private String stationId;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosItemStkService posItemStkService;
    @Autowired
    private PosAttService posAttService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosSettingService posSettingService;
    @Autowired
    private PosPrinterTaskService printerTaskService;
    @Autowired
    private MemPeriodService memPeriodService;
    @Autowired
    private NettyComponent nettyComponent;
    @Autowired
    private PosTableService posTableService;

    /**
     * 送單方法
     * 1、先判斷foodTableView中有沒有記錄如果有記錄則執行送單流程
     * 2、判斷臺號是否存在，存在則繼續送單
     */
    public boolean sendOrders(PosTran posTran, List<SendOrderDto> sendOrderList, boolean isPrinter, PrinterTypeEnums printerTypeEnums, String formTableNum, String toTabelNum, String itemIdx, String logId, boolean isHistory)  {
        boolean sendOrderStatus = false;
        // 獲取桌台，用於判斷是否外賣
        PosTable table = posTableService.getTableByNum(posTran.getTableNum());
        //要不要打印前台小票
        boolean isPrintSendOrder = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat totalDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (AppUtils.isNotBlank(sendOrderList) && AppUtils.isNotBlank(posTran)) {
            if (AppUtils.isNotBlank(posTran) && AppUtils.isNotBlank(posTran.getTableNum())) {
                List<PosOrder> posOrderList = new ArrayList<>();
                List<PosTAtt> posTAttList = new ArrayList<>();
                List<PosItemStk> posItemStkList = new ArrayList<>();
                StringBuilder serialNumbers = new StringBuilder();
                AtomicReference<String> msg = new AtomicReference<>("");
                AtomicBoolean canSendOrder = new AtomicBoolean(true);
                AtomicInteger printPage = new AtomicInteger(1);
                Map<String, PosPrinterTask> printerTaskMap = new HashMap<>();// prn task
                Map<String, List<PosPrinterTaskDetail>> printerTaskDetailMap = new HashMap<>();// serialnumber list
                // List<PosPrinterTask> printerTaskList = new ArrayList<>();
                // List<PosPrinterTaskDetail> printerTaskDetailList = new ArrayList<>();
                //套餐编号
                String mealCode = null;
                //套餐下的菜品数
                Integer combos = 0;
                //套餐的Idex
                Long mealIdx = 000L;
                // PosOrder
                for (SendOrderDto sendOrderDto : sendOrderList) {
                    PosOrder order = null;
                    if (!isHistory) {
                        order = new PosOrder();
                        //如果是改单操作orderHis表
                    } else {
                        order = new PosOrderHis();
                    }
                    //如果是自由價，則保存輸入的菜品名稱
                    if (AppUtils.isNotBlank(sendOrderDto.getIopen()) && ItemIopenEnum.TRUE.getValue().equals(sendOrderDto.getIopen())) {
                        order.setReceiveItemName(sendOrderDto.getItemName());
                    }
                    order.setCartId(sendOrderDto.getCartId());
                    order.setId(sendOrderDto.getId());
                    order.setOutlet(Main.posSetting.get("outlet"));
                    order.setRefNum(posTran.getRefNum());
                    order.setSubRef(posTran.getSubRef());
                    order.setType(posTran.getTranType());
                    order.setTableNum(posTran.getTableNum());
                    order.setItemCode(sendOrderDto.getItemCode());
                    order.setItemIdx((AppUtils.isNotBlank(sendOrderDto.getItemIdx()) && Integer.parseInt(sendOrderDto.getItemIdx()) != 0) ? Long.parseLong(sendOrderDto.getItemIdx()) : Long.parseLong(posTranService.getOrderIndex()));
                    order.setServCost(sendOrderDto.getServAmt());
                    //便于下次点菜时能够自动比配该组别的菜，如果套餐内的菜品没有点菜，需要给ta字段设置组别
                    if (sendOrderDto.getItemCode().equals("0000")) {
                        order.setTa(sendOrderDto.getSgroup());
                    }
                    if(AppUtils.isNotBlank(sendOrderDto.getTDate()) && sendOrderDto.getTDate().contains(" ")){
                        try {
                            order.setTDate(dateFormat.parse(sendOrderDto.getTDate().split(" ")[0]));
                            order.setTTime(timeFormat.parse(sendOrderDto.getTDate().split(" ")[1]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    //主套餐
                    if (sendOrderDto.getIsMeal()) {
                        if (mealIdx != 000L && combos > 0) {
                            Integer finalCombos = combos;
                            Long finalMealIdx1 = mealIdx;
                            posOrderList.forEach((PosOrder posOrder) -> {
                                if (posOrder.getItemIdx().toString().equals(finalMealIdx1.toString())) {
                                    posOrder.setSetmeal(finalCombos.toString());
                                }
                            });
                            //重新清0
                            combos = 0;
                            mealCode = "";
                            mealIdx = 000L;
                        }
                        //如果为套餐时记录套餐的idx
                        mealIdx = order.getItemIdx();
                        //如果为套餐时初始化combos为0
                        combos = 0;
                    }
                    //单品时setMeal为0
                    if (!sendOrderDto.getIsMeal() && AppUtils.isBlank(sendOrderDto.getMealCode())) {
                        order.setSetmeal("0");
                        if (AppUtils.isNotBlank(mealCode) && mealIdx != 000L && combos > 0 && isPrinter) {
                            Integer finalCombos = combos;
                            Long finalMealIdx = mealIdx;
                            posOrderList.forEach((PosOrder posOrder) -> {
                                if (posOrder.getItemIdx().toString().equals(finalMealIdx.toString())) {
                                    posOrder.setSetmeal(finalCombos.toString());
                                }
                            });
                        }
                        combos = 0;
                        mealCode = "";
                        mealIdx = 000L;

                    }
                    //套餐详情时setMeal为-1
                    else if (AppUtils.isNotBlank(sendOrderDto.getMealCode()) && AppUtils.isBlank(sendOrderDto.getId())) {
                        order.setSetmeal("-1");
                        combos = combos + 1;
                        if (AppUtils.isNotBlank(mealCode) && mealIdx != 000L) {
                            order.setSetmealCode(mealCode);
                            order.setSepChar(mealIdx.toString());
                        }
                    }
                    //套餐內子商品沒有選擇菜品並且已經入庫
                    else if (AppUtils.isNotBlank(sendOrderDto.getMealCode()) && AppUtils.isNotBlank(sendOrderDto.getId())) {
                        combos = 0;
                        mealCode = "";
                        mealIdx = 000L;
                        PosOrder mealPosOrder = posOrderService.selectById(sendOrderDto.getId());
                        if (mealPosOrder != null) {
                            order.setId(mealPosOrder.getId());
                            order.setItemIdx(mealPosOrder.getItemIdx());
                        }
                        order.setTTime(new Date());
                        order.setTDate(new Date());
                    }
                    order.setService(sendOrderDto.getService());
                    //如果有ID,則證明是已經送過單的,無須設定時間和日期  wen.jing
                    if (AppUtils.isBlank(sendOrderDto.getId())) {
                        order.setTTime(new Date());
                        order.setTDate(new Date());
                    }
                    order.setStaff(Main.posStaff.getCode());
                    order.setPriceType(sendOrderDto.getSpeciPrintId());
                    order.setQty(sendOrderDto.getQty());
                    order.setAmt(sendOrderDto.getAmt());
                    order.setChgAmt(BigDecimal.ZERO);
                    order.setItemDisc(sendOrderDto.getItemDisc());
                    order.setCatDisc(sendOrderDto.getCatDisc());
                    order.setOrderDisc(BigDecimal.ZERO);
                    order.setTax(BigDecimal.ZERO);
                    order.setCancel(0);
                    order.setFree(0);
                    order.setCost(BigDecimal.ZERO);
                    order.setKicMess(sendOrderDto.getItemKicMsg());
                    order.setKconfirm(sendOrderDto.getOrgPrice() + "");
                    order.setChangeAmt(sendOrderDto.getAttAmt());
                    order.setUnitPrice(new BigDecimal(String.valueOf(sendOrderDto.getPrice())));
                    if (sendOrderDto.getIsMeal()) {
                        mealCode = sendOrderDto.getItemCode();
                    }

                    if (AppUtils.isNotBlank(sendOrderDto.getPrinterCode()) && sendOrderDto.isPrinter()) {
                        Map<String, String> prnMap = new HashMap<>();
                        Arrays.asList(sendOrderDto.getPrinterCode().split(",")).forEach(prn -> {
                            if (AppUtils.isNotBlank(prn)) {
                                prnMap.put(prn.trim(), prn.trim());
                            }
                        });
                        PosOrder finalOrder = order;
                        prnMap.forEach((k, v) -> {
                            if (AppUtils.isNotBlank(k)) {
                                HoldOnEnum holdOnEnum = HoldOnEnum.getEnumByValue(sendOrderDto.getHoldOn());
                                int attIndex = 1;
                                printPage.set(printPage.get() + 1);
                                if (AppUtils.isBlank(printerTaskMap.get(k))) {
                                    PosPrinterTask printerTask = new PosPrinterTask();
                                    printerTask.setVersion(new Date());
                                    printerTask.setOutlet(Main.posSetting.get("outlet"));
                                    printerTask.setStaffName(Main.posStaff.getName1());
                                    printerTask.setStaff(Main.posStaff.getCode());
                                    printerTask.setType(printerTypeEnums.getValue());
                                    printerTask.setPersons(posTran.getPerson());
                                    printerTask.setTableNum(posTran.getTableNum());
                                    printerTask.setRefNum(posTran.getRefNum());
                                    printerTask.setSubRef(posTran.getSubRef());
                                    printerTask.setPrinter(k.trim());
                                    printerTask.setStation(stationId);
                                    printerTask.setSendTime(new Date());
                                    printerTask.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                                    printerTask.setCurrentPage(printPage.get());
                                    printerTask.setTotalPage(sendOrderList.size());
                                    printerTask.setSerialNumber(getPrinterTaskNo());
                                    serialNumbers.append(";").append(printerTask.getSerialNumber());
                                    // 是否套餐
                                    if (AppUtils.isBlank(sendOrderDto.getMealCode()) && sendOrderDto.getIsMeal()) {
                                        printerTask.setSetmeal("TRUE");
                                    } else {
                                        printerTask.setSetmeal("FALSE");
                                    }
                                    if (table.getRoomType().equals(TableTypeEnum.CARRYOUT.getValue()) || table.getRoomType().equals(TableTypeEnum.CARRYOUTPLACE.getValue())) {
                                        printerTask.setTitle(TableTypeEnum.getNameByValue(table.getRoomType()));
                                    }
                                    printerTaskMap.put(k, printerTask);
                                }

                                if (AppUtils.isBlank(printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()))) {
                                    printerTaskDetailMap.put(printerTaskMap.get(k).getSerialNumber(), new ArrayList<>());
                                }
                                // 打印明細
                                PosPrinterTaskDetail taskItemDetail = new PosPrinterTaskDetail();
                                taskItemDetail.setVersion(new Date());
                                taskItemDetail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                                taskItemDetail.setQty(new BigDecimal(sendOrderDto.getQty()));
                                taskItemDetail.setSpec(String.valueOf(sendOrderDto.getQty()) + "x");
                                taskItemDetail.setAmt(sendOrderDto.getAmt());
                                taskItemDetail.setItemCode(sendOrderDto.getItemCode());
                                taskItemDetail.setItemName(sendOrderDto.getItemName());
                                taskItemDetail.setItemIdx(String.valueOf(finalOrder.getItemIdx()));
                                taskItemDetail.setSort(attIndex);
                                attIndex++;
                                taskItemDetail.setType(PrinterItemTypeEnum.ITEM.getValue());
                                taskItemDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                                taskItemDetail.setKicmType(holdOnEnum.getValue());
                                printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(taskItemDetail);


                                // 添加口味信息打印
                                String detailAttCode = "";
                                String detailAttGroupCode = "";
                                String detailActionCode = "";
                                if (AppUtils.isNotBlank(sendOrderDto.getAttCode()) && sendOrderDto.isPrinter()) {
                                    int attIdx = 0;
                                    for (String code : sendOrderDto.getAttCode()) {
                                        PosPrinterTaskDetail taskDetail = new PosPrinterTaskDetail();
                                        taskDetail.setVersion(new Date());
                                        taskDetail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                                        taskDetail.setQty(BigDecimal.ZERO);
                                        taskDetail.setSpec("**");
                                        if (AppUtils.isNotBlank(sendOrderDto.getAttPrices())) {
                                            taskDetail.setAmt(attIdx >= sendOrderDto.getAttPrices().length ? BigDecimal.ZERO : new BigDecimal(sendOrderDto.getAttPrices()[attIdx]));
                                        }
                                        taskDetail.setItemCode(sendOrderDto.getItemCode());
                                        taskDetail.setItemName(sendOrderDto.getAttName()[attIdx]);
                                        taskDetail.setItemIdx(String.valueOf(finalOrder.getItemIdx()));
                                        taskDetail.setSort(attIndex);
                                        attIndex++;
                                        taskDetail.setType(PrinterItemTypeEnum.ATT.getValue());
                                        taskDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                                        taskDetail.setAttCode(code);
                                        taskDetail.setAttGroupCode(sendOrderDto.getAttGroup()[attIdx]);
                                        taskDetail.setKicmType(holdOnEnum.getValue());
                                        if (AppUtils.isNotBlank(sendOrderDto.getActionCode()) && attIdx < sendOrderDto.getActionCode().length) {
                                            taskDetail.setAttActionCode(sendOrderDto.getActionCode()[attIdx]);
                                            detailActionCode += "," + taskDetail.getAttActionCode();
                                        } else {
                                            detailActionCode += ",";
                                        }
                                        printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(taskDetail);
                                        attIdx++;
                                        detailAttCode += "," + taskDetail.getAttCode();
                                        detailAttGroupCode += "," + taskDetail.getAttGroupCode();
                                    }
                                }
                                taskItemDetail.setAttCode(detailAttCode);
                                taskItemDetail.setAttGroupCode(detailAttGroupCode);
                                taskItemDetail.setAttActionCode(detailActionCode);

                                // 廚房信息
                                if (holdOnEnum.equals(HOLDON) || holdOnEnum.equals(SERVE)) {
                                    PosPrinterTaskDetail taskKicDetail = new PosPrinterTaskDetail();
                                    taskKicDetail.setVersion(new Date());
                                    taskKicDetail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                                    taskKicDetail.setQty(BigDecimal.ZERO);
                                    taskKicDetail.setSpec("**");
                                    taskKicDetail.setAmt(BigDecimal.ZERO);
                                    taskKicDetail.setItemCode(sendOrderDto.getItemCode());
                                    taskKicDetail.setItemName(holdOnEnum.getName());
                                    taskKicDetail.setItemIdx(String.valueOf(finalOrder.getItemIdx()));
                                    taskKicDetail.setKicmType(holdOnEnum.getValue());
                                    taskKicDetail.setSort(attIndex);
                                    attIndex++;
                                    taskKicDetail.setType(PrinterItemTypeEnum.KICM.getValue());
                                    taskKicDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                                    printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(taskKicDetail);
                                }
                                ////////////////
                            }
                        });
                    }
                    if (!isPrintSendOrder && sendOrderDto.isPrinter()) {
                        isPrintSendOrder = true;
                    }
                    posOrderList.add(order);
                    // 判断扣燉是否足够，注意半份
                    Wrapper<PosItemStk> wrapper = new EntityWrapper<>();
                    wrapper.eq("ITEM_CODE", sendOrderDto.getItemCode());
                    PosItemStk stk = posItemStkService.selectOne(wrapper);
                    if (AppUtils.isNotBlank(sendOrderDto.getAttCode()) && sendOrderDto.isPrinter()) {
                        int attIdx = 0;
                        for (String code : sendOrderDto.getAttCode()) {
                            if (AppUtils.isNotBlank(code)) {
                                PosTAtt posTAtt = null;
                                if (!isHistory) {
                                    posTAtt = new PosTAtt();
                                }
                                //如果isPrinter为false说明改单操作     pos_t_att_his表
                                else {
                                    posTAtt = new PosTAttHis();
                                }
                                posTAtt.setOutlet(Main.posSetting.get("outlet"));
                                posTAtt.setItemIdx(order.getItemIdx());
                                posTAtt.setAttIdx(attIdx + "");
                                posTAtt.setAttCode(code);
                                posTAtt.setAttGroup(sendOrderDto.getAttGroup()[attIdx]);
                                if (AppUtils.isNotBlank(sendOrderDto.getActionCode()) && attIdx < sendOrderDto.getActionCode().length) {
                                    posTAtt.setActionCode(sendOrderDto.getActionCode()[attIdx]);
                                }
                                if (AppUtils.isNotBlank(sendOrderDto.getAttPrices())) {
                                    posTAtt.setChangeAmt(attIdx >= sendOrderDto.getAttPrices().length ? BigDecimal.ZERO : new BigDecimal(sendOrderDto.getAttPrices()[attIdx]));
                                }
                                //posTAtt.setActionCode(sendOrderDto.getActionCode().length > 2 ? sendOrderDto.getActionCode()[attIdx] : null);

                                // 獲取扣燉數量，需要實時數
                                PosAtt posAtt = posAttService.queryByGroupAndAttCode(sendOrderDto.getAttGroup()[attIdx], code);
                                posTAttList.add(posTAtt);
                                if (stk != null) {
                                    if (posAtt.getCalType().equals(PosAttCalTypeEnum.SUBTRACT.getValue()) && new BigDecimal(order.getQty()).multiply(posAtt.getCalQty()).compareTo(new BigDecimal(stk.getStock())) > 0) {
                                        // 需要弹窗提示
                                        canSendOrder.set(false);
                                        msg.set(sendOrderDto.getItemName() + "扣燉數量不足，送單失敗");
                                        break;
                                    } else {
                                        if (posAtt.getCalType().equals(PosAttCalTypeEnum.SUBTRACT.getValue())) {
                                            stk.setStock((new BigDecimal(stk.getStock()).subtract(new BigDecimal(order.getQty()).multiply(posAtt.getCalQty()))).toString());
                                            posItemStkList.add(stk);
                                        } else {
                                            stk.setStock((new BigDecimal(stk.getStock()).subtract(new BigDecimal(order.getQty())).toString()));
                                            posItemStkList.add(stk);
                                        }
                                    }
                                }
                                attIdx++;
                            }
                        }
                    }
                    //如果要列印
                    else if (sendOrderDto.isPrinter()) {
                        if (stk != null) {
                            if (new BigDecimal(order.getQty()).compareTo(new BigDecimal(stk.getStock())) > 0) {
                                // 需要弹窗提示
                                canSendOrder.set(false);
                                msg.set(sendOrderDto.getItemName() + "扣燉數量不足，送單失敗");
                                break;
                            } else {
                                stk.setStock((new BigDecimal(stk.getStock()).subtract(new BigDecimal(order.getQty()))).toString());
                                posItemStkList.add(stk);
                            }
                        }
                    }
                }

                if (canSendOrder.get() && !isHistory) {
                    boolean status = posOrderService.sendOrder(posOrderList, posTAttList, posItemStkList);
                    //如果最后一条记录是套餐的组别
                    if (AppUtils.isNotBlank(mealCode) && mealIdx != 000L && combos > 0 && status) {
                        Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
                        posOrderWrapper.eq("ITEM_IDX", mealIdx);
                        PosOrder mealPosOrder = posOrderService.selectOne(posOrderWrapper);
                        if (mealPosOrder != null) {
                            // mealPosOrder.setItemIdx(mealIdx);
                            mealPosOrder.setSetmeal(combos.toString());
                            posOrderService.updateById(mealPosOrder);
                        }
                        combos = 0;
                        mealCode = null;
                        mealIdx = 000L;
                    }
                    final Date[] earlistDate = {null};
                    final String[] dateStr = {""};
                    if(AppUtils.isNotBlank(posOrderList)){
                        posOrderList.forEach((PosOrder posOrder) -> {
                            if (Integer.parseInt(posOrder.getKicMess()) == HoldOnEnum.HOLDON.getValue()) {
                                String orderDate = dateFormat.format(posOrder.getTDate());
                                String orderTime = timeFormat.format(posOrder.getTTime());
                                Date date = null;
                                try {
                                    date = totalDateTime.parse(orderDate + " " + orderTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (earlistDate[0] != null) {
                                    if (date.before(earlistDate[0])) {
                                        earlistDate[0] = date;
                                        dateStr[0] = orderDate + " " + orderTime;
                                    }
                                } else {
                                    earlistDate[0] = date;
                                    dateStr[0] = orderDate + " " + orderTime;
                                }
                            }
                        });
                        //如果存在最早的叫起时间，就发送netty消息去message更新叫起桌台
                        if (AppUtils.isNotBlank(earlistDate[0])) {
                            Map<String,String> tableMap = new HashedMap();
                            tableMap.put(posOrderList.get(0).getTableNum(),dateStr[0]);
                            nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON,tableMap);
                        }
                    }
                    //获取当前时段
                    Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
                    memPeriodWrapper.eq("ISVALID", "Y");
                    memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
                    MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);
                    //获取该账单桌台类型
                    String tableType = posTableService.getTableTypeByTableNum(posTran.getTableNum());
                    if (memPeriod != null && isPrintSendOrder) {
                        // 打印出菜纸
                        String sendToPrint = Main.posSetting.get("sendToPrint");
                        if (AppUtils.isNotBlank(sendToPrint)) {
                            boolean isInPeriod = false;//标识是否在配置的市别中
                            //需要判断当前市别是否出小票的情况
                            if (sendToPrint.contains(";")) {
                                String[] settings = sendToPrint.split(";");
                                for (int i = 1; i < settings.length; i++) {
                                    if (memPeriod.getCode().equals(settings[i])) {
                                        isInPeriod = true;
                                        break;
                                    }
                                }
                                if ("TRUE".equals(settings[0]) && !isInPeriod) {
                                    Task task = new Task<Void>() {
                                        @Override
                                        public Void call() {
                                            if(TableTypeEnum.CARRYOUT.getValue().equals(tableType) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(tableType)){
                                                PrintStyleUtils.printTakeOutTicket(posTran);
                                            }
                                            else{
                                                PrintStyleUtils.printTicket(posTran);
                                            }
                                            return null;
                                        }
                                    };
                                    new Thread(task).start();
                                }
                            }
                            //不需要判断当前市别且只需要判断是否为TRUE
                            else {
                                if ("TRUE".equals(sendToPrint)) {
                                    Task task = new Task<Void>() {
                                        @Override
                                        public Void call() {
                                            if(TableTypeEnum.CARRYOUT.getValue().equals(tableType) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(tableType)){
                                                PrintStyleUtils.printTakeOutTicket(posTran);
                                            }
                                            else{
                                                PrintStyleUtils.printTicket(posTran);
                                            }
                                            return null;
                                        }
                                    };
                                    new Thread(task).start();
                                }
                            }
                        }
                    }
                    if (!status) {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView("送單失敗", "請重試", resultMap, null);
                    } else {
                        if (isPrinter) {
                            try {
                                // 添加打印任務
                                List<PosPrinterTask> taskList = new ArrayList<>();
                                List<PosPrinterTaskDetail> printerTaskDetailList = new ArrayList<>();
                                printerTaskMap.forEach((k, v) -> taskList.add(v));
                                printerTaskDetailMap.forEach((k, v) -> printerTaskDetailList.addAll(v));
                                printerTaskService.printBatch(taskList, printerTaskDetailList);
                                printer(serialNumbers.toString(), posTran.getRefNum(), posTran.getSubRef(), printerTypeEnums);

                                //重新打一张收银总单
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        sendOrderStatus = true;
                        //  netty通知其他POS端刷新
                        nettyComponent.sendMessage(NettyMessageTypeEnum.SENDORDER);
                    }
                }
                //改单
                else if (canSendOrder.get() && isHistory) {
                    boolean status = posOrderHisService.sendOrder(posOrderList, posTAttList, posItemStkList);
                    //如果最后一条记录是套餐的组别
                    if (AppUtils.isNotBlank(mealCode) && mealIdx != 000L && combos > 0 && status) {
                        Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
                        posOrderHisWrapper.eq("ITEM_IDX", mealIdx);
                        PosOrderHis mealPosOrder = posOrderHisService.selectOne(posOrderHisWrapper);
                        if (mealPosOrder != null) {
                            // mealPosOrder.setItemIdx(mealIdx);
                            mealPosOrder.setSetmeal(combos.toString());
                            posOrderHisService.updateById(mealPosOrder);
                        }
                        combos = 0;
                        mealCode = null;
                        mealIdx = 000L;
                    }
                    sendOrderStatus = status;
                    if (sendOrderStatus) {
                        //  netty通知其他POS端刷新
                        nettyComponent.sendMessage(NettyMessageTypeEnum.SENDORDER);
                    }
                } else {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView("送單失敗", msg.get(), resultMap, null);
                }
            }
        } else if (isPrinter && posTran != null) { // 只打印
            List<PosPrinterTask> posPrinterTaskList = new ArrayList<>();
            List<PosPrinterTaskDetail> printerTaskDetailList = new ArrayList<>();
            StringBuilder serialNumbers = new StringBuilder();
            // 判斷是否全單轉台，全單轉台需要把所有已下菜品取出並出飛至相應各檔口
            String itemIdxs = null;
            if (AppUtils.isNotBlank(itemIdx)) {
                itemIdxs = itemIdx.replaceAll(";", ",");
                if (itemIdxs.startsWith(",")) {
                    itemIdxs = itemIdxs.substring(1);
                }
                if (itemIdxs.endsWith(",")) {
                    itemIdxs = itemIdxs.substring(0, itemIdxs.length() - 1);
                }
            }
            List<PosOrderPrinterDto> posOrderPrinterDtos = posOrderService.queryAllByRefNum(posTran.getRefNum(), posTran.getSubRef(), itemIdxs);
            Map<String, List<PosOrderPrinterDto>> posOrderPrinterMap = new HashMap<>();
            if (AppUtils.isNotBlank(posOrderPrinterDtos)) {
                posOrderPrinterMap = posOrderPrinterDtos.stream().collect(Collectors.groupingBy(PosOrderPrinterDto::getPrinter));
            }

            Map<String, List<PosOrderAttDto>> posOrderAttMap = new HashMap<>();
            List<PosOrderAttDto> orderAttDtos = null;
            if (AppUtils.isNotBlank(itemIdxs)) {
                orderAttDtos = posOrderService.queryOrderAttByItemIdx(itemIdxs);
            } else if (printerTypeEnums.equals(PrinterTypeEnums.F)) {
                orderAttDtos = posOrderService.queryOrderAttByRef(posTran.getRefNum(), posTran.getSubRef());
            }
            if (AppUtils.isNotBlank(orderAttDtos)) {
                posOrderAttMap = orderAttDtos.stream().collect(Collectors.groupingBy(PosOrderAttDto::getItemIdx));
            }

            Map<String, List<PosOrderAttDto>> finalPosOrderAttMap = posOrderAttMap;

            posOrderPrinterMap.forEach((k, v) -> {
                final AtomicInteger attIndex = new AtomicInteger(1);
                PosPrinterTask printerTask = new PosPrinterTask();
                printerTask.setVersion(new Date());
                printerTask.setOutlet(Main.posSetting.get("outlet"));
                printerTask.setStaffName(Main.posStaff.getName1());
                printerTask.setStaff(Main.posStaff.getCode());
                printerTask.setType(printerTypeEnums.getValue());
                printerTask.setPersons(posTran.getPerson());
                printerTask.setTableNum(formTableNum);
                printerTask.setDefTableNum(toTabelNum);
                printerTask.setRefNum(posTran.getRefNum());
                printerTask.setSubRef(posTran.getSubRef());
                printerTask.setPrinter(k);
                printerTask.setStation(stationId);
                printerTask.setSendTime(new Date());
                printerTask.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                printerTask.setCurrentPage(1);
                printerTask.setTotalPage(1);
                printerTask.setReason(logId);
                printerTask.setSerialNumber(getPrinterTaskNo());
                serialNumbers.append(";").append(printerTask.getSerialNumber());
                printerTask.setSetmeal("FALSE");
                if (table.getRoomType().equals(TableTypeEnum.CARRYOUT.getValue()) || table.getRoomType().equals(TableTypeEnum.CARRYOUTPLACE.getValue())) {
                    printerTask.setTitle(TableTypeEnum.getNameByValue(table.getRoomType()));
                }
                posPrinterTaskList.add(printerTask);
                v.forEach(dto -> {
                    HoldOnEnum holdOnEnum = HoldOnEnum.getEnumByValue(Integer.parseInt(dto.getKicMess()));
                    // 打印明細
                    PosPrinterTaskDetail taskItemDetail = new PosPrinterTaskDetail();
                    taskItemDetail.setVersion(new Date());
                    taskItemDetail.setSerialNumber(printerTask.getSerialNumber());
                    taskItemDetail.setQty(new BigDecimal(String.valueOf(dto.getQty())));
                    taskItemDetail.setSpec(String.valueOf(dto.getQty()) + "x");
                    taskItemDetail.setAmt(dto.getAmt());
                    taskItemDetail.setItemCode(dto.getItemCode());
                    taskItemDetail.setItemName(dto.getPrintDesc());
                    taskItemDetail.setItemIdx(String.valueOf(dto.getItemIdx()));
                    taskItemDetail.setSort(attIndex.get());
                    attIndex.set(attIndex.get() + 1);
                    taskItemDetail.setType(PrinterItemTypeEnum.ITEM.getValue());
                    taskItemDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                    taskItemDetail.setKicmType(holdOnEnum.getValue());
                    printerTaskDetailList.add(taskItemDetail);

                    // 口味
                    StringBuilder detailAttCode = new StringBuilder();
                    StringBuilder detailAttGroupCode = new StringBuilder();
                    StringBuilder detailActionCode = new StringBuilder();
                    if (AppUtils.isNotBlank(finalPosOrderAttMap.get(String.valueOf(dto.getItemIdx())))) {
                        finalPosOrderAttMap.get(String.valueOf(dto.getItemIdx())).forEach(att -> {
                            PosPrinterTaskDetail detail = new PosPrinterTaskDetail();
                            detail.setVersion(new Date());
                            detail.setSerialNumber(printerTask.getSerialNumber());
                            detail.setQty(BigDecimal.ZERO);
                            detail.setSpec("**");
                            detail.setAmt(att.getChangeAmt());
                            detail.setItemCode(dto.getItemCode());
                            detail.setItemName((AppUtils.isNotBlank(att.getActionName()) ? att.getActionName() : "") + att.getAttName());
                            detail.setItemIdx(String.valueOf(dto.getItemIdx()));
                            detail.setSort(attIndex.get());
                            attIndex.set(attIndex.get() + 1);
                            detail.setType(PrinterItemTypeEnum.ATT.getValue());
                            detail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                            detail.setAttCode(att.getAttCode());
                            detail.setAttGroupCode(att.getGroupCode());
                            detail.setAttActionCode(att.getActionCode());
                            detail.setKicmType(holdOnEnum.getValue());
                            detailActionCode.append(",").append(detail.getAttActionCode());
                            detailAttCode.append(",").append(detail.getAttCode());
                            detailAttGroupCode.append(",").append(detail.getAttGroupCode());
                            printerTaskDetailList.add(detail);
                        });
                        taskItemDetail.setAttCode(detailAttCode.toString());
                        taskItemDetail.setAttGroupCode(detailAttGroupCode.toString());
                        taskItemDetail.setAttActionCode(detailActionCode.toString());
                    }

                    // 廚房信息

                    if (holdOnEnum.equals(HOLDON)) {
                        PosPrinterTaskDetail taskDetail = new PosPrinterTaskDetail();
                        taskDetail.setVersion(new Date());
                        taskDetail.setSerialNumber(printerTask.getSerialNumber());
                        taskDetail.setQty(BigDecimal.ZERO);
                        taskDetail.setSpec("**");
                        taskDetail.setAmt(BigDecimal.ZERO);
                        taskDetail.setItemCode(dto.getItemCode());
                        taskDetail.setItemName(holdOnEnum.getName());
                        taskDetail.setItemIdx(String.valueOf(dto.getItemIdx()));
                        taskDetail.setKicmType(holdOnEnum.getValue());
                        taskDetail.setSort(attIndex.get());
                        attIndex.set(attIndex.get() + 1);
                        taskDetail.setType(PrinterItemTypeEnum.KICM.getValue());
                        taskDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                        printerTaskDetailList.add(taskDetail);
                    }
                });
            });

            // 如果是單品轉台，需要傳遞食品ITEM_IDX
            // 如果是追單，需要傳遞食品ITEM_IDX
            printerTaskService.printBatch(posPrinterTaskList, printerTaskDetailList);
            printer(serialNumbers.toString(), posTran.getRefNum(), posTran.getSubRef(), printerTypeEnums);
        }
        return sendOrderStatus;
    }

    /**
     * 後廚打印公共方法
     *
     * @param refNum 需要打印的訂單編號
     * @param subRef 需要打印的子訂單編號
     */
    public void printer(final String serialNumbers, final String refNum, final String subRef, PrinterTypeEnums printerTypeEnums) {
        // 异步打印，防止卡死
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Channel channel = nettyClient.getChannel();
                String _serialNumbers = subString(serialNumbers, ";");
                //String _itemIdxs = AppUtils.isNotBlank(itemIdxs) ? subString(itemIdxs, ";") : null;
                // 打印
                PrinterDto dto = new PrinterDto(NettyMessageTypeEnum.KITCHENPRINT);
                dto.setType(printerTypeEnums);
                dto.setStationId(stationId);
                dto.setRefNum(refNum);
                dto.setSubRef(subRef);
                //dto.setItemIdx(_itemIdxs);
                dto.setSerialNumbers(_serialNumbers);
                //dto.setFromTableNum(fromTabelNum);
                //dto.setToTableNum(toTabelNum);
                String text = JSONObject.toJSONString(dto);
                Message message = new Message();
                message.sign(REQUEST);
                message.setClientId(NettyClient.NETTY_CLIENT_ID);
                message.data(text);

                channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        System.out.println("發送後廚打印任務失敗:" + future.cause().getMessage());
                    } else {
                        System.out.println("發送後廚打印任務成功");
                    }
                });
                //防止对象处理发生异常的情况
                MessageNonAck msgNonAck = new MessageNonAck(message, channel);
                nettyClient.getClientConnector().addNeedAckMessageInfo(msgNonAck);
                return null;
            }

        };

        new Thread(task).start();

    }

    public String getPrinterTaskNo() {
        String tranOrderNo = null;
        try {

            // 查詢賬單編號
            PosSettingDto settingDto = posSettingService.queryPrinterTaskNo();
            tranOrderNo = AppUtils.autoGenericCode(settingDto.getPosValue(), Integer.parseInt(Main.posSetting.get("refnum_length")));
            // 更新賬單編號
            posSettingService.updateSetting(settingDto.getId(), String.valueOf(Integer.parseInt(settingDto.getPosValue()) + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tranOrderNo;
    }

    public String subString(String str, String split) {
        if (AppUtils.isNotBlank(str)) {
            if (str.startsWith(split) && str.endsWith(split)) {
                str = str.substring(1, str.length() - 1);
            } else if (str.startsWith(split)) {
                str = str.substring(1);
            } else if (str.endsWith(split)) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }
}
