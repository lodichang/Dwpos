package com.dw.netty;

import com.ablegenius.netty.client.ClientMessageHandler;
import com.ablegenius.netty.client.DefaultCommonClientConnector;
import com.ablegenius.netty.client.MessageNonAck;
import com.ablegenius.netty.common.Message;
import com.ablegenius.netty.common.NettyEventType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dw.Main;
import com.dw.controller.ItemModifyController;
import com.dw.controller.MainController;
import com.dw.controller.TableSettingsController;
import com.dw.controller.TakeOrderIndexController;
import com.dw.dto.NettyMessageDto;
import com.dw.dto.PosTableDto;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TableStateEnum;
import com.dw.extended.DwLabel;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosSettingService;
import com.dw.service.PosTranService;
import com.dw.util.AppUtils;
import com.dw.util.IDManager;
import com.dw.util.ShowViewUtil;
import com.sun.javafx.robot.impl.FXRobotHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ablegenius.netty.common.NettyCommonProtocol.REQUEST;

@Component
public class NettyClient {
    public static String NETTY_CLIENT_ID = "POS_STATION_";
    @Value("${STATION_ID}")
    private String stationId;
    @Value("${STATION_TYPE}")
    private String stationType;
    @Autowired
    private PosSettingService posSettingService;
    private Channel channel;
    private DefaultCommonClientConnector clientConnector;
    @Autowired
    private MainController mainController;
    @Autowired
    private TableSettingsController tableSettingsController;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private ItemModifyController itemModifyController;
    //超時圖標
    //private Image clockImage = AppUtils.loadImage("clock.png");

    private Map<String, FlowPane> timeOutMap = new HashedMap();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public NettyClient() {
        System.out.println("開始初始化Netty Client");
        new Thread(() -> {
            try {
                new NettyCheckTask().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("初始化Netty Client");
    }

    private class NettyCheckTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {
                try {
                    getChannel();
                } catch (Exception e) {
                    // 通知POS Netty連接失敗
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView("提示", "Netty連接服務器失敗,請重啟或聯絡技術人員", resultMap, (Stage) FXRobotHelper.getStages());
                    e.printStackTrace();
                }
            });
        }
    }

    public Channel getChannel() {
        if (channel == null || !channel.isOpen() || clientConnector == null) {
            if (AppUtils.isNotBlank(stationType) && "CASHER".equals(stationType)) {
                NETTY_CLIENT_ID = "POS_STATION_" + stationType;
            } else {
                NETTY_CLIENT_ID = "POS_STATION_" + stationType + "_" + (AppUtils.isNotBlank(stationId) ? stationId : IDManager.generateID());
            }
            clientConnector = new DefaultCommonClientConnector(new BussinessHandler(NETTY_CLIENT_ID));
            String port = posSettingService.queryByKey("NETTY_SERVICE_PORT").getPosValue();
            String ip = posSettingService.queryByKey("SERVER_IP").getPosValue();
            channel = clientConnector.connect(Integer.parseInt(port), ip);
            Message message = new Message();
            message.setMsgType(NettyEventType.LOGIN);
            message.sign(REQUEST);
            message.setClientId(NETTY_CLIENT_ID);
            message.data("");
            channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    System.out.println("[NettyClient.getChannel]NETTY接入失敗 : " + future.cause().getMessage());
                } else {
                    System.out.println("[NettyClient.getChannel]NETTY接入成功");
                }
            });
            MessageNonAck msgNonAck = new MessageNonAck(message, channel);
            clientConnector.addNeedAckMessageInfo(msgNonAck);
        }
        return channel;
    }

    public DefaultCommonClientConnector getClientConnector() {
        return clientConnector;
    }

    private class BussinessHandler extends ClientMessageHandler {

        public BussinessHandler(String clientId) {
            super(clientId);
        }

        @Override
        public synchronized void receive(Message message) {
            try {
                NettyMessageDto msg = JSONObject.parseObject(message.data().toString(), NettyMessageDto.class);
                if (msg.getMsgType().equals(NettyMessageTypeEnum.CHANGEPERSON) && !mainController.getIsRefreshingTable()) {
                    // 改人數，刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.OPENTABLE) && !mainController.getIsRefreshingTable()) {
                    // 开台，刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.REMEDYORDER)) {
                    // 補單，刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.SPLITTABLE) && !mainController.getIsRefreshingTable()) {
                    // 分台，刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    if (AppUtils.isNotBlank(stationType) && "CASHER".equals(stationType)) {
                        // 打印上菜紙，根據台號查詢訂單
                        PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getRefNum()));
                        // 打印上菜紙，根據台號查詢訂單
                        PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getToRefNum()));
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.SENDORDER) && !mainController.getIsRefreshingTable()) {
                    // 送单，刷新桌
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    if (AppUtils.isNotBlank(stationType) && "CASHER".equals(stationType)) {
                        // 打印上菜紙，根據台號查詢訂單
                        System.out.println("**************" + message.data().toString());
                        PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getRefNum()));
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.SETTLE) && !mainController.getIsRefreshingTable()) {
                    // 结帐，刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.CHANGETABLE) && !mainController.getIsRefreshingTable()) { //轉臺 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    if (AppUtils.isNotBlank(stationType) && "CASHER".equals(stationType)) {
                        // 打印上菜紙，根據台號查詢訂單
                        PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getRefNum()));
                        // 打印上菜紙，根據台號查詢訂單
                        PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getToRefNum()));
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.ADDTABLE) && !mainController.getIsRefreshingTable()) { //加臺 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    tableSettingsController.refreshTables(tableSettingsController.getCurrentPage(), tableSettingsController.getCurrentArea(), tableSettingsController.getNewTableSettingStage(), false);
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.ADDTABLE) && !mainController.getIsRefreshingTable()) { //删除臺 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    tableSettingsController.refreshTables(tableSettingsController.getCurrentPage(), tableSettingsController.getCurrentArea(), tableSettingsController.getNewTableSettingStage(), false);
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.MOVETABLE) && !mainController.getIsRefreshingTable()) { //移动臺 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    tableSettingsController.refreshTables(tableSettingsController.getCurrentPage(), tableSettingsController.getCurrentArea(), tableSettingsController.getNewTableSettingStage(), false);
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.IMMEDIATELY) && !mainController.getIsRefreshingTable()) { //即起 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.HOLDON) && !mainController.getIsRefreshingTable()) { //叫起 ，刷新桌台
                    // 刷新桌台
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.CHECKHOLDON)) { //叫起闪烁 ，刷新桌台
                    Map<String, String> holdOnTableMap = JSONObject.parseObject(msg.getPosHoldOnTables(), Map.class);
                    if (mainController != null && !mainController.getIsRefreshingTable()) {
                        for (Map.Entry<String, String> entry : holdOnTableMap.entrySet()) {
                            if (entry.getKey().contains("-")) {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                //获取父台号的flowPane
                                String parentTableNum = entry.getKey().split("-")[0];
                                if (flowPane == null) {
                                    FlowPane parentFlowPane = mainController.getTablesMap().get(parentTableNum);
                                    if (parentFlowPane != null) {
                                        Platform.runLater(() ->
                                         mainController.changStyle(parentFlowPane, TableStateEnum.HOLDON.getValue(),true));
                                    }
                                } else {
                                    Platform.runLater(() -> mainController.changStyle(flowPane, TableStateEnum.HOLDON.getValue(),false));
                                }
                            } else {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                List<PosTableDto> posTableDtoList = mainController.getGroupbyMap().get(entry.getKey());
                                if (AppUtils.isNotBlank(flowPane)) {
                                    if(TableStateEnum.SPLITTABLE.getValue().equals(posTableDtoList.get(0).getTableState())){
                                        Platform.runLater(() -> mainController.changStyle(flowPane, TableStateEnum.HOLDON.getValue(),true));
                                    }
                                    else{
                                        Platform.runLater(() -> mainController.changStyle(flowPane, TableStateEnum.HOLDON.getValue(),false));
                                    }

                                }
                            }
                        }
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.UNCHECKHOLDON)) { //取消叫起闪烁 ，刷新桌台
                    Map<String, String> holdOnTableMap = JSONObject.parseObject(msg.getPosHoldOnTables(), Map.class);
                    //当前时间
                    Date nowDate = new Date();
                    if (mainController != null && !mainController.getIsRefreshingTable()) {
                        for (Map.Entry<String, String> entry : holdOnTableMap.entrySet()) {
                            //分台情况
                            if (entry.getKey().contains("-")) {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                //获取父台号的flowPane
                                String parentTableNum = entry.getKey().split("-")[0];
                                if (flowPane == null) {
                                    FlowPane parentFlowPane = mainController.getTablesMap().get(parentTableNum);
                                    if (parentFlowPane != null) {
                                        Platform.runLater(() -> mainController.changStyle(parentFlowPane, mainController.getGroupbyMap().get(parentTableNum).get(0).getTableState(),true));
                                        Date holdOnDtae = simpleDateFormat.parse(entry.getValue());
                                        Boolean flag = compareDate(nowDate,holdOnDtae,15);
                                        if(flag){
                                            DwLabel dwLabel = (DwLabel) parentFlowPane.getChildren().get(0);
                                            BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                    BackgroundSize.DEFAULT);
                                            Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                        }
                                    }
                                } else {
                                    List<PosTableDto> posTableDtos = mainController.getGroupbyMap().get(parentTableNum);
                                    posTableDtos.forEach((PosTableDto posTableDto) -> {
                                        if (entry.getKey().equals(posTableDto.getRoomNum())) {
                                            Platform.runLater(() -> mainController.changStyle(flowPane, posTableDto.getTableState(),false));
                                            Date holdOnDtae = null;
                                            try {
                                                holdOnDtae = simpleDateFormat.parse(entry.getValue());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            Boolean flag = compareDate(nowDate,holdOnDtae,15);
                                            if(flag){
                                                DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                                BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                        BackgroundSize.DEFAULT);
                                                Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                            }
                                        }
                                    });
                                }
                            } else {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                if (AppUtils.isNotBlank(flowPane)) {
                                    List<PosTableDto> posTableDtoList = mainController.getGroupbyMap().get(entry.getKey());
                                    if (AppUtils.isNotBlank(posTableDtoList)) {
                                        Date holdOnDtae = simpleDateFormat.parse(entry.getValue());
                                        Boolean flag = compareDate(nowDate,holdOnDtae,15);
                                        if(flag){
                                            DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                            BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                    BackgroundSize.DEFAULT);
                                            Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                        }
                                        if(TableStateEnum.SPLITTABLE.getValue().equals(posTableDtoList.get(0).getTableState())){
                                            Platform.runLater(() -> mainController.changStyle(flowPane, posTableDtoList.get(0).getTableState(),true));
                                        }
                                        else{
                                            Platform.runLater(() -> mainController.changStyle(flowPane, posTableDtoList.get(0).getTableState(),false));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.PRINTTICKET)) {
                    // 打印上菜紙，根據台號查詢訂單
                    PrintStyleUtils.printTicket(posTranService.queryByRefNumOnly(msg.getRefNum()));
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.SHOWTIMEOUT)) { //超時闪烁
                    List<String> timeOutTableNums = JSONObject.parseArray(msg.getPosHoldOnTables(), String.class);
                    //清空超时的map
                    timeOutMap.clear();
                    if (AppUtils.isNotBlank(timeOutTableNums)) {
                        if (mainController != null && !mainController.getIsRefreshingTable()) {
                            timeOutTableNums.forEach((String tableNum) -> {
                                //分台的情况
                                if (tableNum.contains("-")) {
                                    FlowPane flowPane = mainController.getTablesMap().get(tableNum);
                                    //获取父台号的flowPane
                                    String parentTableNum = tableNum.split("-")[0];
                                    if (flowPane == null) {
                                        FlowPane parentFlowPane = mainController.getTablesMap().get(parentTableNum);
                                        if (parentFlowPane != null) {
                                            DwLabel dwLabel = (DwLabel) parentFlowPane.getChildren().get(0);
                                            BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("clock.png", dwLabel.getPrefHeight() - 5, dwLabel.getPrefHeight() - 5),
                                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                    BackgroundSize.DEFAULT);
                                            Platform.runLater(() -> {
                                                dwLabel.setBackground(new Background(backgroundImage));
                                            });
                                            if (!timeOutMap.containsKey(parentTableNum)) {
                                                timeOutMap.put(parentTableNum, parentFlowPane);
                                            }
                                        }
                                    } else {
                                        DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                        BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("clock.png", dwLabel.getPrefHeight() - 5, dwLabel.getPrefHeight() - 5),
                                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                BackgroundSize.DEFAULT);
                                        Platform.runLater(() -> {
                                            dwLabel.setBackground(new Background(backgroundImage));
                                        });
                                        timeOutMap.put(tableNum, flowPane);
                                    }
                                } else {
                                    FlowPane flowPane = mainController.getTablesMap().get(tableNum);
                                    if (AppUtils.isNotBlank(flowPane)) {
                                        DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                        BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("clock.png", dwLabel.getPrefHeight() - 5, dwLabel.getPrefHeight() - 5),
                                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                BackgroundSize.DEFAULT);
                                        Platform.runLater(() -> {
                                            dwLabel.setBackground(new Background(backgroundImage));
                                        });
                                        timeOutMap.put(tableNum, flowPane);
                                    }
                                }
                            });
                        }
                    }
                } else if (msg.getMsgType().equals(NettyMessageTypeEnum.UNSHOWTIMEOUT)) {
                    //去掉超时的桌台图标
                    for (Map.Entry<String, FlowPane> entry : timeOutMap.entrySet()) {
                        DwLabel dwLabel = (DwLabel) entry.getValue().getChildren().get(0);
                        Platform.runLater(() -> {
                            dwLabel.setBackground(null);
                        });
                    }
                }
                else if (msg.getMsgType().equals(NettyMessageTypeEnum.HOLDONTIMEOUT)) { //叫起超时闪烁 ，刷新桌台
                    Map<String, String> holdOnTableMap = JSONObject.parseObject(msg.getPosHoldOnTables(), Map.class);
                    if (mainController != null && !mainController.getIsRefreshingTable()) {
                        for (Map.Entry<String, String> entry : holdOnTableMap.entrySet()) {
                            //分台情况
                            if (entry.getKey().contains("-")) {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                //获取父台号的flowPane
                                String parentTableNum = entry.getKey().split("-")[0];
                                if (flowPane == null) {
                                    FlowPane parentFlowPane = mainController.getTablesMap().get(parentTableNum);
                                    if (parentFlowPane != null) {
                                            DwLabel dwLabel = (DwLabel) parentFlowPane.getChildren().get(0);
                                            BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand-green.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                    BackgroundSize.DEFAULT);
                                            Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                    }
                                } else {
                                    List<PosTableDto> posTableDtos = mainController.getGroupbyMap().get(parentTableNum);
                                    posTableDtos.forEach((PosTableDto posTableDto) -> {
                                        if (entry.getKey().equals(posTableDto.getRoomNum())) {
                                                DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                                BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand-green.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                        BackgroundSize.DEFAULT);
                                                Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                        }
                                    });
                                }
                            } else {
                                FlowPane flowPane = mainController.getTablesMap().get(entry.getKey());
                                if (AppUtils.isNotBlank(flowPane)) {
                                            DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(0);
                                            BackgroundImage backgroundImage = new BackgroundImage(AppUtils.loadImage("hand-green.png", dwLabel.getPrefHeight(), dwLabel.getPrefHeight()),
                                                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                                    BackgroundSize.DEFAULT);
                                            Platform.runLater(() -> dwLabel.setBackground(new Background(backgroundImage)));
                                }
                            }
                        }
                    }
                }
                else if (msg.getMsgType().equals(NettyMessageTypeEnum.UPDATEITEMNAME)) {
                   //修改菜品價格，刷新緩存
                  if(takeOrderIndexController != null){
                      //刷新点菜界面的items
                      takeOrderIndexController.refreshItems();
                  }
                  if(itemModifyController != null){
                      itemModifyController.refreshItems();
                      itemModifyController.nextPageTopButton(itemModifyController.getCurrentTopButtonPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getCurrentItemPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getTopButtonDtos());
                  }
                }
                else if (msg.getMsgType().equals(NettyMessageTypeEnum.UPDATEITEMPRICE)) {
                    //修改菜品價格，刷新緩存
                    if(takeOrderIndexController != null){
                        //刷新点菜界面的items
                        takeOrderIndexController.refreshItems();
                    }
                    if(itemModifyController != null){
                        itemModifyController.refreshItems();
                        itemModifyController.nextPageTopButton(itemModifyController.getCurrentTopButtonPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getCurrentItemPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getTopButtonDtos());
                    }
                }


            } catch (
                    Exception e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 比较两个时间之间是否超过minute分钟
     * @param firstDate
     * @param secondDate
     * @param minute
     * @return
     */
    public boolean  compareDate(Date firstDate,Date secondDate,Integer minute){
        long between=(firstDate.getTime()-secondDate.getTime())/1000/60;//除以1000是为了转换成分钟
        return between > minute;
    }


}
