package com.dw.component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.controller.BillSettleController;
import com.dw.controller.CouponController;
import com.dw.controller.MainController;
import com.dw.controller.TakeOrderIndexController;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.mapper.MemPeriodMapper;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.BillSettleView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 *
 * Created by li.yongliang on 2018/5/10.

 */


@Component
public class SettleComponent {
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    MainController mainController;
    @Autowired
    private PosTimediscService posTimediscService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosLogHisService posLogHisService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private BillSettleView billSettleView;
    @Autowired
    private PosItemService posItemService;
    @Autowired
    private PosTableService posTableService;
    @Autowired
    private CouponController couponController;
    @Autowired
    private MemPeriodMapper memPeriodMapper;
    @Autowired
    private PosSettingService posSettingService;

    private boolean timecheck = false;
    private ObservableMap posItemDtoObservableMap = FXCollections.observableHashMap();
    public ObservableList<TableViewDto> foodTableViewDatas = FXCollections.observableArrayList();
    public BigDecimal orderAmt = BigDecimal.ZERO;

    public void createPosItemDtoMap() {
        List<PosItemDto> posItemDtoList = posItemService.getItems(Main.posOutlet, Main.posPeriodMap.get("price"));
        if (AppUtils.isBlank(posItemDtoObservableMap)) {
            posItemDtoObservableMap = FXCollections.observableMap(posItemDtoList.stream().collect(Collectors.toMap(PosItemDto::getItemCode, Function.identity(), (key1, key2) -> key2)));
        }
        foodTableViewDatas.removeAll(foodTableViewDatas);
        orderAmt = BigDecimal.ZERO;
    }

    public void getTableViewDatasFromTable(PosTran posTran, boolean isUpdateOrder) {
        List<PosOrder> posOrders = null;
        if (isUpdateOrder) {
            Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
            posOrderHisWrapper.eq("OUTLET", posTran.getOutlet());
            posOrderHisWrapper.eq("REF_NUM", posTran.getRefNum());
            posOrderHisWrapper.eq("SUB_REF", posTran.getSubRef());
            posOrderHisWrapper.eq("TYPE", posTran.getTranType());
            posOrderHisWrapper.gt("AMT", 0);
            posOrderHisWrapper.gt("QTY+CANCEL+FREE", 0);
            List<PosOrderHis> posOrderHiss = posOrderHisService.selectList(posOrderHisWrapper);
            posOrders = posOrderHiss.stream().filter(posOrderHis -> posOrderHis instanceof PosOrder).collect(Collectors.toList());
        } else {
            Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
            posOrderWrapper.eq("OUTLET", posTran.getOutlet());
            posOrderWrapper.eq("REF_NUM", posTran.getRefNum());
            posOrderWrapper.eq("SUB_REF", posTran.getSubRef());
            posOrderWrapper.eq("TYPE", posTran.getTranType());
            posOrderWrapper.gt("AMT", 0);
            posOrderWrapper.gt("QTY+CANCEL+FREE", 0);
            posOrders = posOrderService.selectList(posOrderWrapper);
        }


        posOrders.forEach(posOrder -> {
            TableViewDto tvd = new TableViewDto();

            tvd = new TableViewDto("", posOrder.getItemCode(), "", "", "",
                    0.00, posOrder.getAmt().doubleValue(), "", posOrder.getKicMess(), 0.00, "",
                    0.00, "", true, 0, posOrder.getService(), "", "", "", "", "", "", "", "", "", 0, 0, posOrder.getCartId(),false,null);
            tvd.setId(posOrder.getId());

            PosItemDto posItemDto = (PosItemDto) posItemDtoObservableMap.get(posOrder.getItemCode());
            posItemDto = AppUtils.isBlank(posItemDto) ? new PosItemDto() : posItemDto;
            PosOrderDiscDto posOrderDiscDto = new PosOrderDiscDto();
            posOrderDiscDto.setCostAmt(posOrder.getAmt());
            posOrderDiscDto.setCat(posItemDto.getCat());
            posOrderDiscDto.setScat(posItemDto.getScat());
            posOrderDiscDto.setDisc("");
            posOrderDiscDto.setService(posItemDto.getService());
            posOrderDiscDto.setBillDisc(posItemDto.getBillDisc());
            posOrderDiscDto.setPservice(posItemDto.getPriceService());
            posOrderDiscDto.setPbillDisc(posItemDto.getPriceBillDisc());
            posOrderDiscDto.setDscpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDctpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDorpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDoraDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDsrvDisc(BigDecimal.ZERO);
            posOrderDiscDto.setItemDisc(BigDecimal.ZERO);
            posOrderDiscDto.setCatDisc(BigDecimal.ZERO);
            posOrderDiscDto.setComb(posItemDto.getCombId());

            tvd.setPosOrderDiscDto(posOrderDiscDto);

            foodTableViewDatas.add(tvd);
        });
    }


    public void getTableViewData(ObservableList<TableViewDto> tableViewDtos) {

        tableViewDtos.forEach(tableViewDto -> {
            TableViewDto tvd = new TableViewDto();

            tvd = new TableViewDto(tableViewDto.getQty(), tableViewDto.getItemCode(), "", "", "",
                    0.00, tableViewDto.getAmt(), "", "", 0.00, "",
                    0.00, "", tableViewDto.isPrinter(), 0, tableViewDto.getService(), "", "", "", "", "", "", "", "", "", 0, 0, tableViewDto.getCartId(),false,tableViewDto.gettDate());
            tvd.setId(tableViewDto.getId());

            PosItemDto posItemDto = (PosItemDto) posItemDtoObservableMap.get(tableViewDto.getItemCode());
            posItemDto = AppUtils.isBlank(posItemDto) ? new PosItemDto() : posItemDto;
            PosOrderDiscDto posOrderDiscDto = new PosOrderDiscDto();
            posOrderDiscDto.setCostAmt(new BigDecimal(tableViewDto.getAmt()));
            posOrderDiscDto.setCat(posItemDto.getCat());
            posOrderDiscDto.setScat(posItemDto.getScat());
            posOrderDiscDto.setDisc("");
            posOrderDiscDto.setService(posItemDto.getService());
            posOrderDiscDto.setBillDisc(posItemDto.getBillDisc());
            posOrderDiscDto.setPservice(posItemDto.getPriceService());
            posOrderDiscDto.setPbillDisc(posItemDto.getPriceBillDisc());
            posOrderDiscDto.setDscpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDctpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDorpDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDoraDisc(BigDecimal.ZERO);
            posOrderDiscDto.setDsrvDisc(BigDecimal.ZERO);
            posOrderDiscDto.setItemDisc(BigDecimal.ZERO);
            posOrderDiscDto.setCatDisc(BigDecimal.ZERO);
            posOrderDiscDto.setComb(posItemDto.getCombId());

            tvd.setPosOrderDiscDto(posOrderDiscDto);

            foodTableViewDatas.add(tvd);
        });

    }


    public void getOrderAmt() {
        if (AppUtils.isNotBlank(foodTableViewDatas)) {
            orderAmt = new BigDecimal(foodTableViewDatas.stream().mapToDouble(w -> w.getAmt()).sum());
        }
    }


    /**
     * 重算折扣的方法
     */
    public void calculateDisTableView(String loadType, String refNum, String subRef, String tableNo, boolean isUpdateOrder, String inTime) {
        //查出所有的用券記錄(要有券的使用順序)
        String[] logType = {LogTypeEnum.UCOP.getValue(), LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()};
        List<PosLog> logList = null;
        if (isUpdateOrder) {
            Wrapper<PosLogHis> posLogHisWrapper = new EntityWrapper<>();
            posLogHisWrapper.eq("REF_NUM", refNum);
            posLogHisWrapper.eq("SUB_REF", subRef);
            posLogHisWrapper.eq("TABLE1", tableNo);
            posLogHisWrapper.eq("TYPE", TranTypeEnum.N.getValue());
            posLogHisWrapper.in("LOG_TYPE", logType);
            posLogHisWrapper.orderBy("VERSION", true);
            List<PosLogHis> logHisList = posLogHisService.selectList(posLogHisWrapper);
            logList = logHisList.stream().filter(posLogHis -> posLogHis instanceof PosLog).collect(Collectors.toList());
        } else {
            Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
            posLogWrapper.eq("REF_NUM", refNum);
            posLogWrapper.eq("SUB_REF", subRef);
            posLogWrapper.eq("TABLE1", tableNo);
            posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
            posLogWrapper.in("LOG_TYPE", logType);
            posLogWrapper.orderBy("VERSION", true);
            logList = posLogService.selectList(posLogWrapper);
        }


        if (AppUtils.isNotBlank(logList)) {
            takeOrderIndexController.setCoupon(true);
            for (PosLog log : logList) {
                if (log.getLogType().equals(LogTypeEnum.FULL.getValue()) || log.getLogType().equals(LogTypeEnum.SINGLE.getValue())) {
                    //使用折扣
                    List<PosLog> udisList = null;
                    if (isUpdateOrder) {
                        Wrapper<PosLogHis> logHisWrapper = new EntityWrapper<>();
                        logHisWrapper.eq("REMARK4", log.getId());
                        List<PosLogHis> udisHisList = posLogHisService.selectList(logHisWrapper);
                        udisList = udisHisList.stream().filter(posLogHis -> posLogHis instanceof PosLog).collect(Collectors.toList());
                    } else {
                        Wrapper<PosLog> logWrapper = new EntityWrapper<>();
                        logWrapper.eq("REMARK4", log.getId());
                        udisList = posLogService.selectList(logWrapper);
                    }
                    if (AppUtils.isNotBlank(udisList)) {
                        BigDecimal disAmt = BigDecimal.ZERO;
                        for (TableViewDto tableViewDto : foodTableViewDatas) {
                            PosOrderDiscDto orderDisc = tableViewDto.getPosOrderDiscDto();
                            for (PosLog dis : udisList) {
                                BigDecimal singleDiscAmt = BigDecimal.ZERO;
                                // 判斷是否單項折扣
                                if (log.getLogType().equals(LogTypeEnum.SINGLE.getValue())) {
                                    // 判斷是否針對此產品
                                    if (log.getRemark4().equals(tableViewDto.getCartId())) {
                                        switch (DiscTypeEnum.getByValue(dis.getLogType().trim())) {
                                            case DORP: //單折扣%
                                                // 判斷是否單項折扣
                                                if (couponController.verifyDiscBill(orderDisc.getBillDisc(), orderDisc.getPbillDisc())) { // 判斷是否可以折扣
                                                    BigDecimal disRate = new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01)).setScale(4, BigDecimal.ROUND_HALF_UP);
                                                    singleDiscAmt = orderDisc.getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                                                    tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), singleDiscAmt));
                                                    disAmt = disAmt.add(singleDiscAmt);
                                                }
                                                break;
                                            case DORA://單折扣$
                                                if (couponController.verifyDiscBill(orderDisc.getBillDisc(), orderDisc.getPbillDisc())) { // 判斷是否可以折扣
                                                    singleDiscAmt = new BigDecimal(dis.getRemark3());
                                                    tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), singleDiscAmt));
                                                    disAmt = disAmt.add(singleDiscAmt);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    switch (DiscTypeEnum.getByValue(dis.getLogType().trim())) {
                                        case DSCP: //大大类折扣
                                            if (couponController.verifyDisc(Integer.parseInt(log.getRemark2()), dis.getRemark2(), orderDisc.getScat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                                BigDecimal getDvalue = new BigDecimal(dis.getRemark3());
                                                BigDecimal multiply_getDvalue = getDvalue.multiply(new BigDecimal(0.01));
                                                BigDecimal discost = orderDisc.getCostAmt().multiply(multiply_getDvalue).setScale(4, BigDecimal.ROUND_HALF_UP);
                                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                                disAmt = disAmt.add(discost);
                                                singleDiscAmt = singleDiscAmt.add(discost);
                                            }
                                            break;
                                        case DCTP://大类折扣
                                            if (couponController.verifyDisc(Integer.parseInt(log.getRemark2()), dis.getRemark2(), orderDisc.getCat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            /*posOrderService.updateCateDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost);*/
                                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                                disAmt = disAmt.add(discost);
                                                singleDiscAmt = singleDiscAmt.add(discost);
                                            }
                                            break;
                                        case DORP: //單折扣%
                                            if (couponController.verifyDiscBill(AppUtils.isBlank(dis.getRemark2()) ? 1 : Integer.parseInt(log.getRemark2()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            /*posOrderService.updateItemDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost);*/
                                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                                disAmt = disAmt.add(discost);
                                                singleDiscAmt = singleDiscAmt.add(discost);
                                            }
                                            break;
                                        case DORA://單折扣$
                                            if (couponController.verifyDiscBill(AppUtils.isBlank(dis.getRemark2()) ? 1 : Integer.parseInt(dis.getRemark2()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                                // 把折扣金額換算成賬單的百分比保留四位小数，并四舍五入，然後再計算
                                                BigDecimal disRate = new BigDecimal(dis.getRemark3()).divide(orderAmt, 4, BigDecimal.ROUND_HALF_UP);
                                                BigDecimal discost = orderDisc.getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            /*posOrderService.updateItemDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost); //此金额最后还要与最折扣金额进行差异处理*/
                                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                                disAmt = disAmt.add(discost); //此金额最后还要与最折扣金额进行差异处理
                                                singleDiscAmt = singleDiscAmt.add(discost);
                                            }
                                            break;
                                        case COMB://組別折扣
                                            if (couponController.verifyDisc(Integer.parseInt(log.getRemark2()), dis.getRemark2(), orderDisc.getComb(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                                //posOrderService.updateCateDisc(orderDisc.getId(),discost);
                                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                                disAmt = disAmt.add(discost);
                                                singleDiscAmt = singleDiscAmt.add(discost);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                tableViewDto.getPosOrderDiscDto().setCostAmt(DecimalUtil.subtract(orderDisc.getCostAmt(), singleDiscAmt));
                            }
                        }
                        //更新log表的折扣金額
                        log.setAmt3(disAmt);
                        if (isUpdateOrder) {
                            posLogHisService.updateById(posLogService.convertToHis(log));
                        } else {
                            posLogService.updateById(log);
                        }
                    }
                } else {
                    //使用優惠券
                    if (AppUtils.isNotBlank(log.getRemark1())) {
                        if (log.getRemark1().substring(0, 2).equals("IQ")) {
                            //IQ券重算折扣金額
                            PosBindUseCoupon posBindUseCoupon = new PosBindUseCoupon();
                            posBindUseCoupon.setCardCoupon(log.getRemark1());
                            posBindUseCoupon.setCouponType(log.getRemark2());
                            posBindUseCoupon.setDiscAmt(log.getAmt1());
                            posBindUseCoupon.setFaceAmt(log.getAmt2());
                            posBindUseCoupon.setRealAmt(log.getAmt3());
                            posBindUseCoupon.setRemark3(log.getRemark3());
                            Map<String, Object> result = couponController.calculateIQCouponDiscAmountTableView(posBindUseCoupon, foodTableViewDatas);
                            if ((Integer) result.get("code") == 1) {
                                log.setAmt3((BigDecimal) result.get("disAmt"));
                                if (isUpdateOrder) {
                                    posLogHisService.updateById(posLogService.convertToHis(log));
                                } else {
                                    posLogService.updateById(log);
                                }
                            } else {
                                //todo 異常
                            }
                        } else {
                            //第三方券重算折扣金額
                            Map<String, Object> result = couponController.calculateCouponDiscAmountTableView(log, foodTableViewDatas);
                            if (AppUtils.isNotBlank(result) && (Integer) result.get("code") == 1) {
                                log.setAmt3((BigDecimal) result.get("disAmt"));
                                if (isUpdateOrder) {
                                    posLogHisService.updateById(posLogService.convertToHis(log));
                                } else {
                                    posLogService.updateById(log);
                                }
                            } else {
                                //todo 異常
                            }
                        }
                    }
                }
            }

        } else {
            takeOrderIndexController.setCoupon(false);
        }

        //判断桌台是否包房或者普通桌台，如果不是则不计算服务费
        Wrapper<PosTable> posTableWrapper = new EntityWrapper<>();
        posTableWrapper.eq("ROOM_NUM",tableNo);
        PosTable posTable = posTableService.selectOne(posTableWrapper);
        if(AppUtils.isNotBlank(posTable)){
            if(posTable.getRoomType().equals(TableTypeEnum.GENERAL.getValue()) || posTable.getRoomType().equals(TableTypeEnum.ROOM.getValue())) {
                //計算服務費，只放到內存中，不更新數據庫表
                boolean allDayServiceCharge = Boolean.parseBoolean(Main.posSetting.get("allDayServiceCharge"));
                if (!allDayServiceCharge) {
                    List<MemPeriod> memPeriodList = memPeriodMapper.getServicePeriodList(inTime);
                    if (AppUtils.isNotBlank(memPeriodList)) {
                        allDayServiceCharge = true;
                    }
                }
                if (allDayServiceCharge) {
                    try {
                        BigDecimal disAmt = BigDecimal.ZERO;
                        for (TableViewDto tableViewDto : foodTableViewDatas) {
                            PosOrderDiscDto orderDisc = tableViewDto.getPosOrderDiscDto();
                            if (Boolean.parseBoolean(Main.posSetting.get("service_type"))) {
                                //BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(Main.posPeriodMap.get("servPro")), orderDisc.getCostAmt()), new BigDecimal(100)).doubleValue());
                                BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(Main.posPeriodMap.get("servPro")), new BigDecimal(tableViewDto.getAmt())), new BigDecimal(100)).doubleValue());
                                tableViewDto.getPosOrderDiscDto().setServCost(servCost);
                            } else {
                                BigDecimal itemOrderAmt = DecimalUtil.subtract(DecimalUtil.subtract(new BigDecimal(tableViewDto.getAmt()), orderDisc.getItemDisc()), orderDisc.getCatDisc());
                                BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(Main.posPeriodMap.get("servPro")), itemOrderAmt), new BigDecimal(100)).doubleValue());
                                tableViewDto.getPosOrderDiscDto().setServCost(servCost);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        }

        if (loadType.equals("local")) {
            //刷新tableview數據
            couponController.initTableViewData();
        }
    }


    public int billSettle(String initDataType, String tableNum, String tableType, PosTran posTran, ObservableList<TableViewDto> tableViewDtos, boolean isUpdateOrder) {

        createPosItemDtoMap();
        if (initDataType.equals(InitDataTypeEnum.BARCODE.getValue())) {
            getTableViewDatasFromTable(posTran, isUpdateOrder);
        } else {
            getTableViewData(tableViewDtos);
        }
        getOrderAmt();
        int result = canSettle(tableType, posTran);
        if (result != SettleResultEnum.CAN_SETTLED.getValue()) {
            //如果不可以结账，返回检查结果
            return result;
        }
        calculateDisTableView("other", posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum(), isUpdateOrder, DateUtil.DateToString(posTran.getInTime(), "HH:mm"));

        final BigDecimal[] orderDisc = {BigDecimal.ZERO};
        BigDecimal service = BigDecimal.ZERO;
        if (isUpdateOrder) {
            foodTableViewDatas.forEach(tableViewDto -> {
                if (AppUtils.isNotBlank(tableViewDto.getId())) {
                    PosOrderHis posOrderHis = new PosOrderHis();
                    posOrderHis.setId(tableViewDto.getId());
                    posOrderHis.setOrderDisc(BigDecimal.ZERO);
                    posOrderHis.setItemDisc(tableViewDto.getPosOrderDiscDto().getItemDisc());
                    posOrderHis.setCatDisc(tableViewDto.getPosOrderDiscDto().getCatDisc());
                    posOrderHisService.updateById(posOrderHis);
                }
            });
            //4、統計賬單金額和各種折扣金額
            //4.1、提前结账折扣，判斷有沒有提前结账折扣,已經乘人數
            BigDecimal leaveDisc = posTimediscService.advanceCategoryLeaveDiscHis(Main.posOutlet, posTran);
            posTran.setRoomDisc(leaveDisc);
            //4.2、把order表chg_amt更新為0，然後把提前结账折扣拆到每個菜品的chg_amt字段
            posOrderHisService.splitDiscToOrder(leaveDisc, Main.posOutlet, posTran.getRefNum(), posTran.getSubRef(), TranTypeEnum.N.getValue());

            //4.3把每個商品的折扣都合到order表的order_disc字段

            Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
            posOrderHisWrapper.eq("OUTLET", Main.posOutlet);
            posOrderHisWrapper.eq("REF_NUM", posTran.getRefNum());
            posOrderHisWrapper.eq("SUB_REF", posTran.getSubRef());
            posOrderHisWrapper.eq("TYPE", posTran.getTranType());
            posOrderHisWrapper.gt("AMT", 0);
            posOrderHisWrapper.gt("QTY+CANCEL+FREE", 0);
            List<PosOrderHis> posOrderHisList = posOrderHisService.selectList(posOrderHisWrapper);
            posOrderHisList.forEach(po -> {
                BigDecimal otherDiscAmt = DecimalUtil.add(DecimalUtil.add(po.getChgAmt(), po.getItemDisc()), po.getCatDisc());
                BigDecimal singleOrderDisc = DecimalUtil.add(po.getOrderDisc(), otherDiscAmt);
                po.setOrderDisc(singleOrderDisc.compareTo(po.getAmt()) >= 0 ? po.getAmt() : singleOrderDisc);
                posOrderHisService.updateById(po);
                orderDisc[0] = DecimalUtil.add(orderDisc[0], po.getOrderDisc());
            });

            //4.4、把折扣總和和點菜總和放到tran表
            posTran.setOrderDisc(orderDisc[0]);
            posTran.setOrderAmt(orderAmt);

            if (tableType.equals(TableTypeEnum.ROOM.getValue()) || tableType.equals(TableTypeEnum.GENERAL.getValue())) {
                //5、根據賬單金額、折扣、服務費計算方式（折扣之前算還是折扣之算）、是否全天收取服務費、服務費費率，插入服務費費率到日誌表，計算服務費。通過賬單總金額，開台時間，服務費費率來計算賬單服務費。
                service = posLogHisService.insertLogCalculateService(Boolean.parseBoolean(Main.posSetting.get("service_type")), Boolean.parseBoolean(Main.posSetting.get("allDayServiceCharge")), Main.posPeriodMap.get("servPro"), posTran);
            }
        }
        else {
            foodTableViewDatas.forEach(tableViewDto -> {
                if (AppUtils.isNotBlank(tableViewDto.getId())) {
                    PosOrder posOrder = new PosOrder();
                    posOrder.setId(tableViewDto.getId());
                    posOrder.setOrderDisc(BigDecimal.ZERO);
                    posOrder.setItemDisc(tableViewDto.getPosOrderDiscDto().getItemDisc());
                    posOrder.setCatDisc(tableViewDto.getPosOrderDiscDto().getCatDisc());
                    posOrderService.updateById(posOrder);
                }
            });
            //4、統計賬單金額和各種折扣金額
            //4.1、提前结账折扣，判斷有沒有提前结账折扣,已經乘人數
            BigDecimal leaveDisc = posTimediscService.advanceCategoryLeaveDisc(Main.posOutlet, posTran);
            posTran.setRoomDisc(leaveDisc);

            //4.2、把order表chg_amt更新為0，然後把提前结账折扣拆到每個菜品的chg_amt字段
            posOrderService.splitDiscToOrder(leaveDisc, Main.posOutlet, posTran.getRefNum(), posTran.getSubRef(), TranTypeEnum.N.getValue());

            //4.3把每個商品的折扣都合到order表的order_disc字段

            Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
            posOrderWrapper.eq("OUTLET", Main.posOutlet);
            posOrderWrapper.eq("REF_NUM", posTran.getRefNum());
            posOrderWrapper.eq("SUB_REF", posTran.getSubRef());
            posOrderWrapper.eq("TYPE", posTran.getTranType());
            posOrderWrapper.gt("AMT", 0);
            posOrderWrapper.gt("QTY+CANCEL+FREE", 0);
            List<PosOrder> posOrders = posOrderService.selectList(posOrderWrapper);
            posOrders.forEach(po -> {
                BigDecimal otherDiscAmt = DecimalUtil.add(DecimalUtil.add(po.getChgAmt(), po.getItemDisc()), po.getCatDisc());
                BigDecimal singleOrderDisc = DecimalUtil.add(po.getOrderDisc(), otherDiscAmt);
                po.setOrderDisc(singleOrderDisc.compareTo(po.getAmt()) >= 0 ? po.getAmt() : singleOrderDisc);
                posOrderService.updateById(po);
                orderDisc[0] = DecimalUtil.add(orderDisc[0], po.getOrderDisc());
            });

            //4.4、把折扣總和和點菜總和放到tran表
            posTran.setOrderDisc(orderDisc[0]);
            posTran.setOrderAmt(orderAmt);

            if (tableType.equals(TableTypeEnum.ROOM.getValue()) || tableType.equals(TableTypeEnum.GENERAL.getValue())) {
                //5、根據賬單金額、折扣、服務費計算方式（折扣之前算還是折扣之算）、是否全天收取服務費、服務費費率，插入服務費費率到日誌表，計算服務費。通過賬單總金額，開台時間，服務費費率來計算賬單服務費。
                service = posLogService.insertLogCalculateService(Boolean.parseBoolean(Main.posSetting.get("service_type")), Boolean.parseBoolean(Main.posSetting.get("allDayServiceCharge")), Main.posPeriodMap.get("servPro"), posTran);
            }
        }
        posTran.setServAmt(service);
        //5.1、未去除找零差額的實收金額
        BigDecimal roundBillAmt = DecimalUtil.subtract(DecimalUtil.add(posTran.getOrderAmt(), posTran.getServAmt()), posTran.getOrderDisc());
        roundBillAmt = roundBillAmt.compareTo(BigDecimal.ZERO) > 0 ? roundBillAmt : BigDecimal.ZERO;
        //去除找零差額以後的實收金額
        BigDecimal billAmt = DecimalUtil.doubleToBigDecimalByPloy(roundBillAmt.doubleValue(), Integer.parseInt(Main.posSetting.get(PosSettingEnum.settledecimal_num.getValue())));
        posTran.setBillAmt(billAmt);
        posTran.setRounding(DecimalUtil.subtract(billAmt, roundBillAmt));
        //6、把數據更新到posTran表。
        if(isUpdateOrder){
            posTran.setSettled("");
        }
        else{
            posTran.setSettled("FALSE");
        }
        Date date = new Date();
        posTran.setBillDate(date);
        posTran.setBillTime(date);
        //设置发票编号
        if(AppUtils.isBlank(posTran.getInvoiceNumber())){
            posTran.setInvoiceNumber(getfullInvoiceNumber(posTranService.getInvoiceNumber()));
        }
        if (isUpdateOrder) {
            posTranHisService.updateById(posTranService.convertToHis(posTran));
        } else {
            posTranService.updateById(posTran);
        }

        PosTableDto posTableDto = new PosTableDto();
        posTableDto.setRoomNum(tableNum);
        posTableDto.setRoomType(tableType);
        BillSettleController billSettleController = (BillSettleController) billSettleView.getPresenter();
        billSettleController.initData(posTran, Main.posStaff, isUpdateOrder,initDataType,posTableDto);
        if (initDataType.equals(InitDataTypeEnum.BARCODE.getValue())) {
            billSettleView.showViewAndWait(mainController.getMainPane().getScene().getWindow(), Modality.APPLICATION_MODAL);
        } else {
            billSettleView.showViewAndWait(takeOrderIndexController.getParentFlowPane().getScene().getWindow(), Modality.APPLICATION_MODAL);
        }

        return 1;
    }


    private int canSettle(String tableType, PosTran posTran) {

        //1、先判斷有沒有未送單的菜品
        boolean[] sendOrderFlag = {false};
        boolean[] kicMessFlag = {false};
        foodTableViewDatas.forEach(tvd -> {
            if (!tvd.isPrinter()) {
                sendOrderFlag[0] = true;
            }
            if(tvd.getKicMsgCode().equals("1")){
                kicMessFlag[0] = true;
            }
        });
        if (sendOrderFlag[0]) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("settle.unsend"), resultMap, null);
            sendOrderFlag[0] = false;
            return SettleResultEnum.CANCEL_SETTLED.getValue();
        }
        //1.1判斷有沒有叫起的菜品
        if (kicMessFlag[0]) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("settle.unsend.kicmess"), resultMap, null);
            kicMessFlag[0] = false;
            return SettleResultEnum.CANCEL_SETTLED.getValue();
        }
        //2、判斷是不是包房
        if (timecheck == false && tableType.startsWith(TableTypeEnum.ROOM.getValue())) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
            String ret = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("settle.viproom.selectperiod"), resultMap, null);
            if (ret.equals(ResultEnum.NO.getValue())) {
                return SettleResultEnum.CANCEL_SETTLED.getValue();
            }
        }
        //3、檢查是否可以結賬
        Map<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
        int checkresult = SettleResultEnum.CHECK_ERROR.getValue();
        //如果没有点菜记录，提示无法结账
        if (AppUtils.isBlank(foodTableViewDatas) || foodTableViewDatas.size() <= 0) {
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("settle.noorders"), resultMap, null);
            checkresult = SettleResultEnum.HAS_NO_ORDER.getValue();
        } else if (orderAmt.compareTo(BigDecimal.ZERO) < 0) {
            //如果账单金额小于0，就不能结账settle.billamt.zero
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("settle.billamt.zero"), resultMap, null);
            checkresult = SettleResultEnum.AMT_LESS_ZERO.getValue();
        } else {
            //如果点菜记录中没有必选组别的菜
            int teaqty = 0;
            String catName = "";
            if (tableType.equals(TableTypeEnum.CARRYOUTPLACE.getValue()) || tableType.equals(TableTypeEnum.TEST.getValue())) {
                posTran.setPerson(1);
                posTran.setHeadCount("1");
                posTran.setSettled("FALSE");
                posTranService.updateById(posTran);
                return SettleResultEnum.CAN_SETTLED.getValue();
            } else {
                checkresult = SettleResultEnum.CAN_SETTLED.getValue();
            }

        }
        return checkresult;
    }

    //
    public static String getfullInvoiceNumber(String invoiceNumber) {
        int invoiceLength = Integer.parseInt(Main.posSetting.get("INVOICE_MAXLENGHT"));
        int differential = invoiceLength - invoiceNumber.length();
        StringBuffer fullInvoiceNumber = new StringBuffer("");
        if (differential > 0) {
            for (int i = 0; i < differential; i++) {
                fullInvoiceNumber.append("0");
            }
            return fullInvoiceNumber.toString() + invoiceNumber;
        } else {
            return invoiceNumber;
        }


    }


}
