package com.dw.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.*;
import com.dw.entity.PosPayment;
import com.dw.entity.PosTran;
import com.dw.enums.*;
import com.dw.exception.PayBillException;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.model.PayInfo;
import com.dw.model.SettleInfo;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosPayMentService;
import com.dw.service.PosPayService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.CouponView;
import com.dw.view.MainView;
import com.dw.view.PayView;
import com.dw.view.TakeOrderIndexView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * Created by li.yongliang on 2018/4/25.
 */
@Setter
@Getter
@FXMLController
public class BillSettleController implements Initializable {
    @Value("${STATION_ID}")
    private String stationId;
    @FXML
    private FlowPane billSettleFlowPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private HBox topFlowPaneHbox;
    @FXML
    private Button closeButton;
    @FXML
    private FlowPane centerFlowPane;
    @FXML
    private VBox centerFlowPaneVbox;
    @FXML
    private FlowPane bottomFlowPane;
    @FXML
    private HBox bottomFlowPaneHbox;
    @Autowired
    private PosPayService posPayService;
    @Autowired
    private PosPayMentService posPayMentService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private MainController mainController;
    @Autowired
    private TakeOrderIndexView takeOrderIndexView;


    private Stage parentStage;
    private SettleInfo settleInfo = new SettleInfo();
    @Autowired
    private PayView payView;
    @Autowired
    private MainView mainView;
    @Autowired
    private CouponView couponView;
    @Autowired
    private NettyComponent nettyComponent;
    private boolean isUpdateOrder = false;
    private PosTran posTran;
    private String initDataType = "";
    private PosTableDto posTableDto;
    private Button discBtn = new DwButton(FontSizeEnum.font22);

    public void initData(PosTran posTran, PosStaffDto posStaffDto, boolean isUpdateOrder, String initDataType, PosTableDto posTableDto) {
        this.isUpdateOrder = isUpdateOrder;
        this.posTran = posTran;
        this.initDataType = initDataType;
        this.posTableDto = posTableDto;
        if(AppUtils.isNotBlank(initDataType) && initDataType.equals(InitDataTypeEnum.BARCODE.getValue())){
            discBtn.setVisible(true);
        }else{
            discBtn.setVisible(false);
        }
        settleInfo.setSettleInfo(posTran.getStationId(), posTran.getPeriod(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTranType(), posTran.getInTime(), posStaffDto.getName1(), posTran.getPerson(),
                new BigDecimal(0.00), new BigDecimal(0.00), posTran.getRounding().doubleValue(), "", posTran.getCatDisc(), posTran.getItemDisc(), isUpdateOrder ? BillTypeEnum.ORDERHISBILL : BillTypeEnum.ORDERBILL,
                posTran.getTableNum(), posTran.getOrderAmt().doubleValue(), posTran.getServAmt().doubleValue(), posTran.getOrderDisc().doubleValue(), posTran.getBillAmt().doubleValue(), Main.posStaff.getCode(), posTran.getInDate(), posTran.getInvoiceNumber());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> parentStage = (Stage) billSettleFlowPane.getScene().getWindow());
        show();
    }


    private void show() {
        double takeOrderIndexWidth = Main.primaryScreenBounds.getWidth();
        double takeOrderIndexHeight = Main.primaryScreenBounds.getHeight();
        billSettleFlowPane.setPrefWidth(takeOrderIndexWidth * 0.6);
        billSettleFlowPane.setPrefHeight(takeOrderIndexHeight * 0.6);
        billSettleFlowPane.setPadding(new Insets(10, 0, 10, 0));
        billSettleFlowPane.setAlignment(Pos.CENTER);

        topFlowPane.setPrefSize(billSettleFlowPane.getPrefWidth() * 0.99, billSettleFlowPane.getPrefHeight() * 0.2);
        topFlowPane.setAlignment(Pos.CENTER);
        topFlowPane.setStyle("-fx-background-color: #6E6E6E");
        initTopFlowPane();

        centerFlowPane.setPrefSize(billSettleFlowPane.getPrefWidth() * 0.99, billSettleFlowPane.getPrefHeight() * 0.6);
        centerFlowPane.setAlignment(Pos.TOP_CENTER);
        //centerFlowPane.setStyle("-fx-border-color: #5CAA88;-fx-border-width: 2;");
        initCenterFlowPane();


        bottomFlowPane.setPrefSize(billSettleFlowPane.getPrefWidth() * 0.99, billSettleFlowPane.getPrefHeight() * 0.2);
        bottomFlowPane.setAlignment(Pos.CENTER);
        //bottomFlowPane.setStyle("-fx-border-color: #5CAA88;-fx-border-width: 2;");
        initBottomFlowPane();

    }

    private void initTopFlowPane() {
        topFlowPaneHbox.setSpacing(5);
        topFlowPaneHbox.setPrefSize(topFlowPane.getPrefWidth() * 0.8, topFlowPane.getPrefHeight() * 0.9);
        topFlowPaneHbox.setAlignment(Pos.CENTER);

        discBtn = new DwButton(FontSizeEnum.font22);
        discBtn.setPrefSize(topFlowPaneHbox.getPrefWidth() / 4, topFlowPaneHbox.getPrefHeight() * 0.8);
        discBtn.setText(Main.languageMap.get("tran.full.coupon"));
        discBtn.setVisible(false);
        discBtn.setOnMouseClicked(event -> {
            System.out.println("整單折扣！！！");
            TakeOrderIndexController takeOrderIndexController1 = (TakeOrderIndexController) takeOrderIndexView.getPresenter();
            takeOrderIndexController1.initData(posTran, posTableDto, false, InitDataTypeEnum.BARCODE.getValue());
            Main.showInitialView(takeOrderIndexView.getClass());

            parentStage.close();
        });


        Label label = new DwLabel(FontSizeEnum.font22);
        label.setPrefSize(topFlowPaneHbox.getPrefWidth() / 6, topFlowPaneHbox.getPrefHeight() * 0.8);
        label.setText(Main.languageMap.get("pay.tablenum") + ":");

        Label tableNumLb = new DwLabel(FontSizeEnum.font22);
        tableNumLb.setPrefSize(topFlowPaneHbox.getPrefWidth() / 6, topFlowPaneHbox.getPrefHeight() * 0.8);
        tableNumLb.textProperty().bind(settleInfo.tableNoProperty());
        tableNumLb.getStyleClass().add("label2");

        Label totalLb = new DwLabel(FontSizeEnum.font22);
        totalLb.setPrefSize(topFlowPaneHbox.getPrefWidth() / 6, topFlowPaneHbox.getPrefHeight() * 0.8);
        totalLb.setText(Main.languageMap.get("pay.billamt") + ":");
        totalLb.setPadding(new Insets(0, 0, 0, 20));

        Label amtLb = new DwLabel(FontSizeEnum.font22);
        amtLb.setPrefSize(topFlowPaneHbox.getPrefWidth() / 5, topFlowPaneHbox.getPrefHeight() * 0.8);
        amtLb.textProperty().bind(settleInfo.billAmtProperty().asString());
        amtLb.getStyleClass().add("label2");
        topFlowPaneHbox.getChildren().addAll(discBtn,label, tableNumLb, totalLb, amtLb);


    }


    private void initCenterFlowPane() {
        centerFlowPaneVbox.setSpacing(5);
        centerFlowPaneVbox.setPrefSize(centerFlowPane.getPrefWidth() * 0.8, centerFlowPane.getPrefHeight() * 0.6);
        centerFlowPaneVbox.setAlignment(Pos.TOP_CENTER);

        HBox hBoxOrder = new HBox();
        hBoxOrder.setAlignment(Pos.CENTER);
        hBoxOrder.setPrefSize(centerFlowPaneVbox.getPrefWidth(), centerFlowPaneVbox.getPrefHeight() / 5);

        Label orderTextLb = new DwLabel(FontSizeEnum.font20);
        orderTextLb.setPrefSize(hBoxOrder.getPrefWidth() / 4, hBoxOrder.getPrefHeight());
        orderTextLb.setText(Main.languageMap.get("pays.orderAmt") + ":");

        Label orderAmtLb = new DwLabel(FontSizeEnum.font20);
        orderAmtLb.setPrefSize(hBoxOrder.getPrefWidth() / 4, hBoxOrder.getPrefHeight());
        orderAmtLb.textProperty().bind(settleInfo.orderAmtProperty().asString());

        hBoxOrder.getChildren().addAll(orderTextLb, orderAmtLb);

        HBox hBoxServ = new HBox();
        hBoxServ.setAlignment(Pos.CENTER);
        hBoxServ.setPrefSize(centerFlowPaneVbox.getPrefWidth(), centerFlowPaneVbox.getPrefHeight() / 5);

        Label servTextLb = new DwLabel(FontSizeEnum.font20);
        servTextLb.setPrefSize(hBoxServ.getPrefWidth() / 4, hBoxServ.getPrefHeight());
        servTextLb.setText(Main.languageMap.get("pays.servAmt") + ":");

        Label servAmtLb = new DwLabel(FontSizeEnum.font20);
        servAmtLb.setPrefSize(hBoxServ.getPrefWidth() / 4, hBoxServ.getPrefHeight());
        servAmtLb.textProperty().bind(settleInfo.servAmtProperty().asString());
        hBoxServ.getChildren().addAll(servTextLb, servAmtLb);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefSize(centerFlowPaneVbox.getPrefWidth(), centerFlowPaneVbox.getPrefHeight() / 5);

        Label discTextLb = new DwLabel(FontSizeEnum.font20);
        discTextLb.setPrefSize(hBox.getPrefWidth() / 4, hBox.getPrefHeight());
        discTextLb.setText(Main.languageMap.get("pay.orderdisc") + ":");

        Label orderDiscLb = new DwLabel(FontSizeEnum.font20);
        orderDiscLb.setPrefSize(hBox.getPrefWidth() / 4, hBox.getPrefHeight());
        orderDiscLb.textProperty().bind(settleInfo.orderDiscProperty().asString());
        hBox.getChildren().addAll(discTextLb, orderDiscLb);

        HBox hBoxRound = new HBox();
        hBoxRound.setAlignment(Pos.CENTER);
        hBoxRound.setPrefSize(centerFlowPaneVbox.getPrefWidth(), centerFlowPaneVbox.getPrefHeight() / 5);

        Label roundTextLb = new DwLabel(FontSizeEnum.font20);
        roundTextLb.setPrefSize(hBoxRound.getPrefWidth() / 4, hBoxRound.getPrefHeight());
        roundTextLb.setText(Main.languageMap.get("pay.rounding") + ":");

        Label roundingLb = new DwLabel(FontSizeEnum.font20);
        roundingLb.setPrefSize(hBoxRound.getPrefWidth() / 4, hBoxRound.getPrefHeight());
        roundingLb.textProperty().bind(settleInfo.roundingProperty().asString());
        hBoxRound.getChildren().addAll(roundTextLb, roundingLb);
        centerFlowPaneVbox.getChildren().addAll(hBoxOrder, hBoxServ, hBox, hBoxRound);
    }


    private void initBottomFlowPane() {
        bottomFlowPaneHbox.setSpacing(20);
        bottomFlowPaneHbox.setPrefSize(bottomFlowPane.getPrefWidth(), bottomFlowPane.getPrefHeight() * 0.9);
        bottomFlowPaneHbox.setAlignment(Pos.CENTER);


        //String settledPayment = AppUtils.isBlank(Main.posSetting.get("SettledPayment"))?"CASH":Main.posSetting.get("SettledPayment");
        String settledPayment = AppUtils.isBlank(Main.posSetting.get("SettledPayment")) ? "" : Main.posSetting.get("SettledPayment");
        String settledPaymentNames = AppUtils.isBlank(Main.posSetting.get("SettledPaymentNames")) ? "" : Main.posSetting.get("SettledPaymentNames");

        String[] payments = settledPayment.split(",");
        String[] payNames = settledPaymentNames.split(",");
        for (int i = 0; i < payments.length; i++) {
            Wrapper<PosPayment> posPaymentWrapper = new EntityWrapper<>();
            posPaymentWrapper.eq("CODE", payments[i]);
            PosPayment posPayment = posPayMentService.selectOne(posPaymentWrapper);
            if (AppUtils.isNotBlank(posPayment)) {
                String[] languages = new String[]{posPayment.getDesc1(), posPayment.getDesc2(), posPayment.getDesc3(), posPayment.getDesc4()};
                String languagedefault = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                //现金脚
                Button paymentBtn = new DwButton(FontSizeEnum.font20);
                paymentBtn.setPrefSize(bottomFlowPane.getPrefWidth() / 4, bottomFlowPaneHbox.getPrefHeight() * 0.8);
                paymentBtn.setText(AppUtils.isNotBlank(payNames[i]) ? payNames[i] : languagedefault);
                paymentBtn.setAlignment(Pos.CENTER);
                paymentBtn.setOnMouseClicked(event -> {
                    PayInfo payInfo = new PayInfo();
                    payInfo.setPayInfo(stationId, "", settleInfo.getRefNum(), settleInfo.getRefNum() + "_" + settleInfo.getSubRef(),
                            settleInfo.getTableNo(), settleInfo.getTranType(), DateUtil.DateToString(settleInfo.getInTime(), "HH:mm"),
                            "", settleInfo.getPerson(), new BigDecimal(settleInfo.getBillAmt()), posPayment.getCode().equals("CASH") ? new BigDecimal(settleInfo.getBillAmt()) : BigDecimal.ZERO,
                            !posPayment.getCode().equals("CASH") ? new BigDecimal(settleInfo.getBillAmt()) : BigDecimal.ZERO, new BigDecimal(settleInfo.getRounding()), new BigDecimal(settleInfo.getOrderDisc()), settleInfo.getSubRef(), settleInfo.getBillType(), BigDecimal.ZERO, new BigDecimal(settleInfo.getOrderAmt()), new BigDecimal(settleInfo.getServAmt()), settleInfo.getInvoiceNumber());

                    List<PosPayDto> newList = new ArrayList<>();
                    List<PosPayDto> removeList = new ArrayList<>();
                    PosPayDto payDto = new PosPayDto(Main.posOutlet, stationId, settleInfo.getRefNum(), settleInfo.getSubRef(), TranTypeEnum.N.getValue(), posPayment.getCode()
                            , new BigDecimal(settleInfo.getBillAmt()), 1, new BigDecimal(settleInfo.getBillAmt()), new BigDecimal(settleInfo.getBillAmt()), BigDecimal.ZERO, payInfo.getTableNum()
                            , new Date(), new Date(), "", Main.posStaff.getName1(), Main.posStaff.getCode(), BigDecimal.ZERO);
                    newList.add(payDto);
                    if (BillTypeEnum.ORDERHISBILL == settleInfo.getBillType()) {
                        //加載付款記錄
                        List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, settleInfo.getRefNum(), settleInfo.getSubRef(), settleInfo.getTranType());
                        if (AppUtils.isNotBlank(posPayDtoList)) {
                            removeList.addAll(posPayDtoList);
                        }
                    }

                    try {
                        if (posPayService.addPaybill(payInfo, newList, removeList)) {
                            BillDto billDto = new BillDto();
                            billDto.setRefNum(payInfo.getRefNum());
                            billDto.setSubRef(payInfo.getSubRef());
                            billDto.setTranType(payInfo.getTranType());
                            billDto.setBillAmt(DecimalUtil.doubleToBigDecimalByPloy(payInfo.getBillAmt()));
                            billDto.setOrderDisc(DecimalUtil.doubleToBigDecimalByPloy(payInfo.getOrderDisc()));
                            billDto.setServAmt(DecimalUtil.doubleToBigDecimalByPloy(payInfo.getServAmt()));
                            billDto.setTableNum(payInfo.getTableNum());
                            billDto.setOrderAmt(DecimalUtil.doubleToBigDecimalByPloy(payInfo.getOrderAmt()));
                            billDto.setInvoiceNumber(payInfo.getInvoiceNumber());
                            PrintStyleUtils.printPayBill(billDto);

                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(Main.languageMap.get("invoice.chinese"), LanguageEnum.ZH_HK.getMapFelid() + "");
                            resultMap.put(Main.languageMap.get("invoice.english"), LanguageEnum.EN.getMapFelid() + "");
                            resultMap.put(Main.languageMap.get("keyword.cancle"), "-1");
                            String result = ShowViewUtil.showChoseView(resultMap, parentStage);
                            if (LanguageEnum.EN.getMapFelid() == Integer.parseInt(result)) {
                                PrintStyleUtils.printBillDetail(payInfo.getRefNum(), payInfo.getSubRef(), payInfo.getTableNum(), Main.posOutlet, true, LanguageEnum.EN);
                            } else if (LanguageEnum.ZH_HK.getMapFelid() == Integer.parseInt(result)) {
                                PrintStyleUtils.printBillDetail(payInfo.getRefNum(), payInfo.getSubRef(), payInfo.getTableNum(), Main.posOutlet, true, LanguageEnum.ZH_HK);
                            }
                            //关闭界面，并且跳转到主页
                            parentStage.close();
                            //正常下单拍脚的情况下直接删除桌台操作记录
                            if (BillTypeEnum.ORDERBILL.getValue().equals(settleInfo.getBillType().getValue())) {
                                takeOrderIndexController.deletePosTableAction(settleInfo.getTableNo(), stationId);
                            }
                            MainController mainController = (MainController) mainView.getPresenter();
                            Main.showInitialView(mainView.getClass());
                            mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                            // 拍脚，通知其他收银刷新桌台
                            nettyComponent.sendMessage(NettyMessageTypeEnum.CHARGE);
                        }
                    } catch (PayBillException e) {
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), e.getMessage(), resultMap, parentStage);
                    }
                });

                bottomFlowPaneHbox.getChildren().add(paymentBtn);
            }
        }


        //付款
        Button payBtn = new DwButton(FontSizeEnum.font20);
        payBtn.setPrefSize(bottomFlowPane.getPrefWidth() / 4, bottomFlowPaneHbox.getPrefHeight() * 0.8);
        payBtn.setText(Main.languageMap.get("bill.pay"));
        payBtn.setAlignment(Pos.CENTER);
        payBtn.setOnMouseClicked(event -> {
            if (new BigDecimal(settleInfo.getBillAmt()).compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                String ret = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("pay.billamt.zero"), resultMap, null);
                if (ret.equals(ResultEnum.YES.getValue())) {
                    BillDto billDto = new BillDto(settleInfo.getRefNum() + "_" + settleInfo.getSubRef(), settleInfo.getRefNum(), settleInfo.getSubRef(), settleInfo.getTableNo(), settleInfo.getTranType(), settleInfo.getInTime(),
                            settleInfo.getPerson(), new BigDecimal(settleInfo.getOrderAmt()), new BigDecimal(settleInfo.getBillAmt()), settleInfo.getCashAmt(), settleInfo.getOtherAmt(), new BigDecimal(settleInfo.getRounding()),
                            new BigDecimal(settleInfo.getOrderDisc()), settleInfo.getBillType(), new BigDecimal(settleInfo.getBillAmt()), settleInfo.getBillStaff(), DecimalUtil.doubleToBigDecimalByPloy(settleInfo.getServAmt()), settleInfo.getInDate(), settleInfo.getInvoiceNumber());

                    PayController payController = (PayController) payView.getPresenter();
                    payController.initData(billDto);
                    payController.payInfoIntoTable();
                    parentStage.close();
                    if (BillTypeEnum.ORDERBILL.getValue().equals(settleInfo.getBillType().getValue())) {
                        takeOrderIndexController.deletePosTableAction(settleInfo.getTableNo(), stationId);
                    }
                    MainController mainController = (MainController) mainView.getPresenter();
                    mainController.iniData();
                    Main.showInitialView(mainView.getClass());
                    // 拍脚，通知其他收银刷新桌台
                    nettyComponent.sendMessage(NettyMessageTypeEnum.CHARGE);
                }
            } else {
                BillDto billDto = new BillDto(settleInfo.getRefNum() + "_" + settleInfo.getSubRef(), settleInfo.getRefNum(), settleInfo.getSubRef(), settleInfo.getTableNo(), settleInfo.getTranType(), settleInfo.getInTime(),
                        settleInfo.getPerson(), new BigDecimal(settleInfo.getOrderAmt()), new BigDecimal(settleInfo.getBillAmt()), settleInfo.getCashAmt(), settleInfo.getOtherAmt(), new BigDecimal(settleInfo.getRounding()),
                        new BigDecimal(settleInfo.getOrderDisc()), settleInfo.getBillType(), new BigDecimal(settleInfo.getBillAmt()), settleInfo.getBillStaff(), DecimalUtil.doubleToBigDecimalByPloy(settleInfo.getServAmt()), settleInfo.getInDate(), settleInfo.getInvoiceNumber());
                posPayService.deleteOldPay(billDto.getRefNum(), billDto.getSubRef(), Main.posOutlet, billDto.getTranType());
                PayController payController = (PayController) payView.getPresenter();
                payController.initData(billDto);//todo
                Main.showInitialView(payView.getClass());
                parentStage.close();
            }
        });

        //关闭
        Button closeBtn = new DwButton(FontSizeEnum.font22);
        closeBtn.setPrefSize(bottomFlowPane.getPrefWidth() / 4, bottomFlowPaneHbox.getPrefHeight() * 0.8);
        closeBtn.setText(Main.languageMap.get("global.close"));
        closeBtn.setAlignment(Pos.CENTER);
        closeBtn.setOnMouseClicked(event -> {
            if (BillTypeEnum.ORDERBILL.getValue().equals(settleInfo.getBillType().getValue())) {
                takeOrderIndexController.deletePosTableAction(settleInfo.getTableNo(), stationId);
                //是否选择打印发票
                if(mainController.isInvoice()){
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(Main.languageMap.get("invoice.chinese"), LanguageEnum.ZH_HK.getMapFelid() + "");
                    resultMap.put(Main.languageMap.get("invoice.english"), LanguageEnum.EN.getMapFelid() + "");
                    resultMap.put(Main.languageMap.get("keyword.cancle"), "-1");
                    String result = ShowViewUtil.showChoseView(resultMap, parentStage);
                    if (LanguageEnum.EN.getMapFelid() == Integer.parseInt(result)) {
                        PrintStyleUtils.printBillDetail(settleInfo.getRefNum(), settleInfo.getSubRef(), settleInfo.getTableNo(), Main.posOutlet, false, LanguageEnum.EN);
                    } else if (LanguageEnum.ZH_HK.getMapFelid() == Integer.parseInt(result)) {
                        PrintStyleUtils.printBillDetail(settleInfo.getRefNum(), settleInfo.getSubRef(), settleInfo.getTableNo(), Main.posOutlet, false, LanguageEnum.ZH_HK);
                    }
                }
                //已結賬刷新檯號
                nettyComponent.sendMessage(NettyMessageTypeEnum.SETTLE);
            }
            parentStage.close();
        });
        closeBtn.getStyleClass().add("closeButton");

        bottomFlowPaneHbox.getChildren().addAll(payBtn, closeBtn);
    }
}
