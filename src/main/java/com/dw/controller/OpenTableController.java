package com.dw.controller;

import com.ablegenius.netty.client.MessageNonAck;
import com.ablegenius.netty.common.Message;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.netty.NettyClient;
import com.dw.print.PrintRxTx;
import com.dw.service.*;
import com.dw.util.*;
import com.dw.view.MainView;
import com.dw.view.TakeOrderIndexView;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.concurrent.Task;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ablegenius.netty.common.NettyCommonProtocol.REQUEST;
import static com.dw.util.DateUtil.CM_DATE_FORMAT;

/**
 * Created by lodi on 2018/2/24.
 */
@Component
public class OpenTableController {
    @Value("${STATION_ID}")
    private String stationId;
    @Autowired
    private TakeOrderIndexView takeOrderIndexView;
    @Autowired
    private MainView mainView;
    private PosTableDto posTableDto;
    private int persons = 0;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private PosTableActionService posTableActionService;
    @Autowired
    private MainController mainController;
    @Autowired
    private NettyClient nettyClient;

    //默认显示开台时间
    private Boolean isShowOpenTableTime = false;

    public OpenTableController() {

    }

    /**
     * 執行開台操作
     */
    public void init(PosTableDto posTableDto) {
        this.posTableDto = posTableDto;
        PosTran tran = null;
        PosLogDto logDto = null;
        boolean checkLeaveSeat = Boolean.parseBoolean(AppUtils.isNotBlank(Main.posSetting.get("checkLeaveSeat")) ? Main.posSetting.get("checkLeaveSeat") : "false");
        if (checkLeaveSeat) {
            if (AppUtils.isNotBlank(posTableDto) && null != posTableDto.getTableState() && posTableDto.getTableState().equals(TableStateEnum.HASPAYED.getValue())) {
                //如果是已经拍脚未离座状态，则弹出框让选择。
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                resultMap.put(ResultEnum.REOPEN.getName(), ResultEnum.REOPEN.getValue());

                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("table.leaveseat"), resultMap, null);
                if (result.equals(ResultEnum.YES.getValue())) {
                    updateTranHisLeaveDate();
                    persons = 0;
                } else if (result.equals(ResultEnum.REOPEN.getValue())) {
                    updateTranHisLeaveDate();
                    tran = openTable(posTableDto.getRoomNum());
                }
                MainController mainController = (MainController) mainView.getPresenter();
                Main.showInitialView(mainView.getClass());
                mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
            }
        }
        if (!checkLeaveSeat || (AppUtils.isNotBlank(posTableDto) && (null == posTableDto.getTableState() || !posTableDto.getTableState().equals(TableStateEnum.HASPAYED.getValue())))) {
            if (AppUtils.isNotBlank(posTableDto)) {
                // 判断是否已经开台，TRAN表是否有该台的N记录
                List<PosTran> posTranDtos = posTranService.queryListByTable(posTableDto.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos)) {
                    tran = posTranDtos.get(0);
                    persons = tran.getPerson();
                } else {
                    persons = 0;
                    tran = openTable(posTableDto.getRoomNum());
                    if (isShowOpenTableTime) {
                        PosTran finalTran = tran;
                        Task task = new Task<Void>() {
                            @Override
                            public Void call() {
                                try {
                                    PosOrderDetailDto posOrderDetailDto = new PosOrderDetailDto();
                                    posOrderDetailDto.setTel(Main.posOutletDto.getTel());
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    posOrderDetailDto.setDateTime(dateFormat.format(finalTran.getInDate()));
                                    posOrderDetailDto.setSCode(Main.posStaff.getName1());
                                    String tranCode = finalTran.getOutlet() + finalTran.getRefNum() + finalTran.getSubRef() + finalTran.getTranType();
                                    posOrderDetailDto.setOrderCount("0");
                                    posOrderDetailDto.setOrderAmt("0.00");
                                    posOrderDetailDto.setBillAmt("0.00");
                                    posOrderDetailDto.setPerson(finalTran.getPerson().toString());
                                    posOrderDetailDto.setTranCode(tranCode);
                                    posOrderDetailDto.setTableNum(finalTran.getTableNum());
                                    posOrderDetailDto.setTableNo(finalTran.getTableNum());
                                    posOrderDetailDto.setServCost("0.00");
                                    posOrderDetailDto.setDiscAmt("-" + "0.00");
                                    PrintRxTx.printRxTxInstance().printAction(posOrderDetailDto);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        };
                        new Thread(task).start();
                    }
                    isShowOpenTableTime = false;
                }
            }
        }

        if (persons > 0) {
            PosTableAction posTableAction = isExistOperatorTable(posTableDto.getRoomNum());
            if(posTableAction != null){
                Map<String, String> map = new HashMap();
                map.put(Main.languageMap.get("global.confirm"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), "编号" + posTableAction.getOpStaff() + " " + posTableAction.getOpStaffName() + "正在操作該檯，請稍候再操作" , map, mainController.getMainStage());
                return;
            }
            TakeOrderIndexController takeOrderIndexController = (TakeOrderIndexController) takeOrderIndexView.getPresenter();
            takeOrderIndexController.initData(tran, posTableDto, false, InitDataTypeEnum.SETTLE.getValue());
            Main.showInitialView(takeOrderIndexView.getClass());
        }
    }

    /**
     * 普通开台和重新开台都需要调用的方法。
     */
    public PosTran openTable(String tableNum) {
        PosTran tran = new PosTran();
        PosLogDto logDto = new PosLogDto();
        String result = ShowViewUtil.showNumbericKeyboard(Main.getStage(), "請輸入入座人數", "4", true);
        if (!ResultEnum.NO.getValue().equals(result)) {
            if (AppUtils.isNotBlank(result)) {
                if (result.contains("showTime")) {
                    isShowOpenTableTime = true;
                    result = result.trim().substring(8);
                }
                if (Integer.parseInt(result.trim()) > 0) {
                    persons = Integer.parseInt(result.trim());
                    // 獲取單號
                    String orderNo = AppUtils.autoGenericCode(posTranService.getTranOrderNo(), Integer.parseInt(Main.posSetting.get("refnum_length")));
                    // 检查账单是否重复，如果重复重新获取
                    while (posTranService.queryByRefNum(orderNo, DateUtil.getCurrTimeYmd()) > 0) {
                        orderNo = AppUtils.autoGenericCode(posTranService.getTranOrderNo(), Integer.parseInt(Main.posSetting.get("refnum_length")));
                    }
                    try {

                        // 添加tran賬單

                        //tran.setRefNum(DateUtil.getCurrTimeYmd() + orderNo);
                        tran.setRefNum(orderNo);
                        tran.setSubRef("00");
                        tran.setStationId(stationId);
                        tran.setOutlet(Main.posSetting.get("outlet"));
                        tran.setTableNum(tableNum);
                        tran.setTranType(TranTypeEnum.N.getValue());
                        tran.setOpenStaff(Main.posStaff.getCode());
                        tran.setPerson(persons);
                        tran.setInDate(new Date());
                        tran.setInTime(new Date());
                        tran.setHeadCount(String.valueOf(persons));
                        tran.setId(IDManager.generateIDs());
                        tran.setOrderDisc(new BigDecimal("0.00"));
                        tran.setTIndex(Long.parseLong(orderNo));
                        tran.setLastUpdateNameId(Main.posStaff.getCode());
                        posTranService.insertPosTran(tran);

                        // 添加pos_log

                        logDto.setOutlet(Main.posSetting.get("outlet"));
                        logDto.setTDate(tran.getInDate());
                        logDto.setTTime(tran.getInTime());
                        logDto.setStaff(tran.getOpenStaff());
                        logDto.setLogType("STBL");
                        logDto.setType(TranTypeEnum.N.getValue());
                        logDto.setRefNum(tran.getRefNum());
                        logDto.setSubRef(tran.getSubRef());
                        logDto.setTable1(tran.getTableNum());
                        logDto.setId(IDManager.generateIDs());
                        posLogService.insertPosLog(logDto);

                        //開台上數雲端
                        apiOpenTable(tran, logDto);

                        //開臺成功後發送netty去通知其他機器刷新桌臺
                        //  netty通知其他POS端刷新
                        Task task = new Task<Void>() {
                            @Override
                            public Void call() {
                                Channel channel = nettyClient.getChannel();
                                NettyMessageDto msgDto = new NettyMessageDto(NettyMessageTypeEnum.OPENTABLE,null);
                                String text = JSONObject.toJSONString(msgDto);
                                Message message = new Message();
                                message.sign(REQUEST);
                                message.setClientId(NettyClient.NETTY_CLIENT_ID);
                                message.data(text);
                                channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {});
                                //防止对象处理发生异常的情况
                                MessageNonAck msgNonAck = new MessageNonAck(message, channel);
                                nettyClient.getClientConnector().addNeedAckMessageInfo(msgNonAck);
                                return null;
                            }
                        };
                        new Thread(task).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        }
        return tran;
    }

    private void apiOpenTable(PosTran tran, PosLogDto logDto) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    // 是否開台上數雲端
                    // IS_UPDATE_FOR_OPEN_TABLE=TRUE;
                    String uploadData = Main.posSetting.get("uploadData");
                    if (AppUtils.isNotBlank(uploadData) && uploadData.equals("TRUE")) {
                        //向云端插入一条记录，并返回跳转用的URL，分店将此URL打印成二维码
                        //請求链接返回url用来生成二维码,pos客户端直接访问云端接口，不再访问message进行跳转
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("channel", "HTML"));
                        params.add(new BasicNameValuePair("outline", Main.outline));
                        params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
                        params.add(new BasicNameValuePair("refNum", DateUtil.getCurrTimeYmd() + tran.getRefNum()));
                        params.add(new BasicNameValuePair("subRef", tran.getSubRef()));
                        params.add(new BasicNameValuePair("stationId", tran.getStationId()));
                        params.add(new BasicNameValuePair("tableNum", tran.getTableNum()));
                        params.add(new BasicNameValuePair("tranType", TranTypeEnum.N.getValue()));
                        params.add(new BasicNameValuePair("openStaff", Main.posStaff.getCode()));
                        params.add(new BasicNameValuePair("person", persons + ""));
                        params.add(new BasicNameValuePair("inDate", DateUtil.DateToString(tran.getInDate(), CM_DATE_FORMAT)));
                        params.add(new BasicNameValuePair("inTime", DateUtil.DateToString(tran.getInTime(), CM_DATE_FORMAT)));
                        params.add(new BasicNameValuePair("tIndex", tran.getTIndex() + ""));
                        params.add(new BasicNameValuePair("orders", ""));
                        String logs = "";
                        List<PosLogDto> logDtoList = new ArrayList<>();
                        if (AppUtils.isNotBlank(logDto)) {
                            net.sf.json.JSONObject logDtoJson = net.sf.json.JSONObject.fromObject(logDto);
                            logDtoJson.put("TDate", logDto.getTDate().getTime());
                            logDtoJson.put("TTime", logDto.getTTime().getTime());
                            logDtoJson.put("version", logDto.getVersion().getTime());
                            logs = "[" + logDtoJson.toString() + "]";
                        }
                        params.add(new BasicNameValuePair("logs", logs));
                        String url = Main.posSetting.get("apiUrl") + Main.posSetting.get("apiGetQRCodeUrl");
                        //发送http请求
                        String responseStr = HttpClientUtil.post(url, params);
                        JSONObject resultJson = JSONObject.parseObject(responseStr);
                        if (resultJson != null) {
                            if (AppUtils.isNotBlank(resultJson.get("code")) && (Integer) resultJson.get("code") == 1) {
                                System.out.println("上数云端成功");
                            } else {
                                System.out.println("上数云端失败" + resultJson.get("msg"));
                            }
                        } else {
                            System.out.println("上数云端失败，无法连接上数接口");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    /**
     * 更新tran表的leave_date,离座或者重新开台都需要这么操作
     */
    private void updateTranHisLeaveDate() {
        List<PosTranHis> posTranDtos = posTranHisService.queryListByTable(posTableDto.getRoomNum(), TranTypeEnum.N.getValue());
        if (AppUtils.isNotBlank(posTranDtos)) {
            PosTranHis posTranHis = posTranDtos.get(0);
            posTranHis.setLeaveDate(new Date());
            posTranHis.setLeaveTime(new Date());
            posTranHisService.updateById(posTranHis);
        }
    }

    /**
     * 判断某桌台是否正在被操作，如果不是就插入一条记录后返回null，否则就直接返回存在操作桌台的记录
     */
    public PosTableAction isExistOperatorTable(String tableNum) {
        Wrapper<PosTableAction> posTableActionWrapper = new EntityWrapper<>();
        posTableActionWrapper.eq("TABLE_NUM", tableNum);
        PosTableAction posTableAction = posTableActionService.selectOne(posTableActionWrapper);
        if (posTableAction != null) {
            return posTableAction;
        } else {
            PosTableAction newPosTableAction = new PosTableAction();
            newPosTableAction.setTableNum(tableNum);
            newPosTableAction.setOpStaff(Main.posStaff.getCode());
            newPosTableAction.setOpStaffName(Main.posStaff.getName1());
            newPosTableAction.setStationId(stationId);
            newPosTableAction.setTTime(new Date());
            posTableActionService.insert(newPosTableAction);
            return null;
        }
    }
}
