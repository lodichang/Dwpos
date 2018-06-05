package com.dw.controller;

import com.dw.Main;
import com.dw.component.CleanMachineComponent;
import com.dw.dto.*;
import com.dw.entity.PosTranHis;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.print.PrintStyleUtils;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lodi on 2018/2/24.
 */
@Getter
@Setter
@FXMLController
public class BillsController implements Initializable {

    @FXML
    private FlowPane billFlowPane;

    @FXML
    private TableView<BillDto> billsView;

    private TableColumn<BillDto, String> stationIdCol;

    private TableColumn<BillDto, String> periodCol;

    private TableColumn<BillDto, String> refNumCol;

    private TableColumn<BillDto, String> tableNumCol;

    private TableColumn<BillDto, String> tranTypeCol;

    private TableColumn<BillDto, String> inTimeCol;

    private TableColumn<BillDto, String> name1Col;

    private TableColumn<BillDto, String> personCol;

    private TableColumn<BillDto, String> billAmtCol;

    private TableColumn<BillDto, String> cashAmtCol;

    private TableColumn<BillDto, String> otherAmtCol;

    private TableColumn<BillDto, String> roundingCol;

    private TableColumn<BillDto, String> payDescCol;
    @Autowired
    private BillService billService;
    @Autowired
    private PayView payView;
    @Autowired
    private BillsView billView;
    @Autowired
    private ViewBillView viewBillView;
    @Autowired
    private MainView mainView;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosPayMentService posPayMentService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTableService posTableService;
    @Autowired
    private PosDayeLogService posDayeLogService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @FXML
    private FlowPane topBillFlowPane;
    @FXML
    private FlowPane buttomBillPane;

    @FXML
    private FlowPane leftButtomPane;

    @FXML
    private FlowPane centerButtomFlowPane;

    @FXML
    private FlowPane rightButtomPane;

    @Autowired
    private TakeOrderIndexView takeOrderIndexView;
    @Autowired
    private CleanMachineComponent cleanMachineComponent;
    @Autowired
    private ReportView reportView;

    private Stage billsStage;
    final Separator separator = new Separator();
    private ObservableList<BillDto> billDtos = FXCollections.observableArrayList();

    private StringProperty count = new SimpleStringProperty("0.00");
    private StringProperty totalPrice = new SimpleStringProperty("0.00");
    private StringProperty cash = new SimpleStringProperty("0.00");
    private StringProperty otherPayment = new SimpleStringProperty("0.00");
    private StringProperty turnMoreAmt = new SimpleStringProperty("0.00");//已清機數據（這個數據相當於轉更數據）
    private StringProperty unTurnMoreAmt = new SimpleStringProperty("0.00");//未清機數據（這個數據相當於未轉更數據）
    private StringProperty everyOneAmt = new SimpleStringProperty("0.00");//人均
    private StringProperty unSettledAmt = new SimpleStringProperty("0.00");//未結賬賬單總額
    boolean checkLeaveSeat = false;
    private DwButton searchButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()->{
            billsStage = (Stage) billFlowPane.getScene().getWindow();
        });
        tableComponent();
        billComponent();
    }


    private void tableComponent() {
        billFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
        double billsViewWidth = Main.primaryScreenBounds.getWidth();
        double billsViewHeight = Main.primaryScreenBounds.getHeight() / 22 * 16;

        billsView.setPrefWidth(billsViewWidth);
        billsView.setPrefHeight(billsViewHeight);

        stationIdCol = new TableColumn<>();
        stationIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationId()));

        periodCol = new TableColumn<>();
        periodCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));

        refNumCol = new TableColumn<>();
        refNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRefNumShow()));

        tableNumCol = new TableColumn<>();
        tableNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableNum()));


        tranTypeCol = new TableColumn<>();
        tranTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTranType()));

        inTimeCol = new TableColumn<>();
        inTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.DateToString(cellData.getValue().getInTime(), "HH:mm")));

        name1Col = new TableColumn<>();
        name1Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName1()));

        personCol = new TableColumn<>();
        personCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerson().toString()));

        billAmtCol = new TableColumn<>();
        billAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillAmt().toString()));

        cashAmtCol = new TableColumn<>();
        cashAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCashAmt().toString()));

        otherAmtCol = new TableColumn<>();
        otherAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOtherAmt().toString()));

        roundingCol = new TableColumn<>();
        roundingCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRounding().toString()));

        payDescCol = new TableColumn<>();
        payDescCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayDesc()));

        billsView.getColumns().addAll(stationIdCol, periodCol, refNumCol, tableNumCol, tranTypeCol, inTimeCol, name1Col, personCol, billAmtCol, cashAmtCol, otherAmtCol, roundingCol, payDescCol);
        billsView.setPlaceholder(new DwLabel());

        double columWidth = (billsViewWidth - 20) / 18;
        //double columWidth = (billsViewWidth - 20) / 16;

        stationIdCol.setPrefWidth(columWidth * 1.4);
        periodCol.setPrefWidth(columWidth);
        periodCol.setText(Main.languageMap.get("global.period"));
        refNumCol.setPrefWidth(columWidth * 2);
        refNumCol.setText(Main.languageMap.get("global.refnum"));
        tableNumCol.setPrefWidth(columWidth);
        tableNumCol.setText(Main.languageMap.get("global.tablenum_short"));
        tranTypeCol.setPrefWidth(columWidth);
        tranTypeCol.setText(Main.languageMap.get("global.type"));
        inTimeCol.setPrefWidth(columWidth * 1.5);
        inTimeCol.setText(Main.languageMap.get("global.time"));
        name1Col.setPrefWidth(columWidth * 1.5);
        name1Col.setText(Main.languageMap.get("global.staff"));
        personCol.setPrefWidth(columWidth);
        personCol.setText(Main.languageMap.get("global.person_short"));
        billAmtCol.setPrefWidth(columWidth * 1.5);
        billAmtCol.setText(Main.languageMap.get("global.billamt"));
        cashAmtCol.setPrefWidth(columWidth * 1.5);
        cashAmtCol.setText(Main.languageMap.get("global.cash"));
        otherAmtCol.setPrefWidth(columWidth * 1.5);
        otherAmtCol.setText(Main.languageMap.get("global.other"));
        roundingCol.setPrefWidth(columWidth * 1.5);
        roundingCol.setText(Main.languageMap.get("global.rounding"));
        payDescCol.setPrefWidth(columWidth * 1.6);
        payDescCol.setText(Main.languageMap.get("global.payment_short"));
    }


    private void billComponent() {

        //Platform.runLater(() -> {


            billFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());

            double topBillFlowPaneWidth = Main.primaryScreenBounds.getWidth();
            double topBillFlowPaneHeight = Main.primaryScreenBounds.getHeight() / 22;
            topBillFlowPane.setPrefSize(topBillFlowPaneWidth, topBillFlowPaneHeight);
            topBillFlowPane.setPadding(new Insets(0,0,0,10));
            topBillFlowPane.setAlignment(Pos.TOP_LEFT);
            //topBillFlowPane.setHgap(10);
            //topBillFlowPane.setHgap(2);
            //topBillFlowPane.setHgap(1);

            double labelWidth = topBillFlowPaneWidth / 22;
            double labelHeight = topBillFlowPaneHeight;
            //單數
            DwLabel countLabel = new DwLabel(FontSizeEnum.font20);
            countLabel.setPrefSize(labelWidth, labelHeight);
            countLabel.setText(Main.languageMap.get("bill.top.total"));
            countLabel.getStyleClass().add("topLabel");
            topBillFlowPane.getChildren().add(countLabel);

            DwLabel countValue = new DwLabel(FontSizeEnum.font20);
            countValue.setPrefSize(labelWidth, labelHeight);
            countValue.getStyleClass().add("topValue");
            countValue.setText("0");
            countValue.textProperty().bind(count);
            countValue.setTextAlignment(TextAlignment.LEFT);
            topBillFlowPane.getChildren().add(countValue);
            //收入
            DwLabel totalPriceLabel = new DwLabel(FontSizeEnum.font20);
            totalPriceLabel.setPrefSize(labelWidth, labelHeight);
            totalPriceLabel.getStyleClass().add("topLabel");
            totalPriceLabel.setText(Main.languageMap.get("bill.top.totalAmount"));
            topBillFlowPane.getChildren().add(totalPriceLabel);

            DwLabel totalPriceValue = new DwLabel(FontSizeEnum.font20);
            totalPriceValue.setPrefSize(labelWidth * 3, labelHeight);
            totalPriceValue.getStyleClass().add("topValue");
            totalPriceValue.setText("0.00");
            totalPriceValue.setTextAlignment(TextAlignment.LEFT);
            totalPriceValue.textProperty().bind(totalPrice);
            topBillFlowPane.getChildren().add(totalPriceValue);
            //現金
            DwLabel cashLabel = new DwLabel(FontSizeEnum.font20);
            cashLabel.setPrefSize(labelWidth, labelHeight);
            cashLabel.getStyleClass().add("topLabel");
            cashLabel.setText(Main.languageMap.get("bill.top.totalCash"));
            topBillFlowPane.getChildren().add(cashLabel);

            DwLabel cashValue = new DwLabel(FontSizeEnum.font20);
            cashValue.setPrefSize(labelWidth * 3, labelHeight);
            cashValue.getStyleClass().add("topValue");
            cashValue.setText("0.00");
            cashValue.setTextAlignment(TextAlignment.LEFT);
            cashValue.textProperty().bind(cash);
            topBillFlowPane.getChildren().add(cashValue);
            //其他
            DwLabel otherPaymentLabel = new DwLabel(FontSizeEnum.font20);
            otherPaymentLabel.setPrefSize(labelWidth, labelHeight);
            otherPaymentLabel.getStyleClass().add("topLabel");
            otherPaymentLabel.setText(Main.languageMap.get("bill.top.totalOther"));
            topBillFlowPane.getChildren().add(otherPaymentLabel);

            DwLabel otherPaymentValue = new DwLabel(FontSizeEnum.font20);
            otherPaymentValue.setPrefSize(labelWidth * 2, labelHeight);
            otherPaymentValue.getStyleClass().add("topValue");
            otherPaymentValue.setText("0.00");
            otherPaymentValue.setTextAlignment(TextAlignment.LEFT);
            otherPaymentValue.textProperty().bind(otherPayment);
            topBillFlowPane.getChildren().add(otherPaymentValue);


            //人均
            DwLabel everyOneLabel = new DwLabel(FontSizeEnum.font20);
            everyOneLabel.setPrefSize(labelWidth, labelHeight);
            everyOneLabel.getStyleClass().add("topLabel");
            everyOneLabel.setText(Main.languageMap.get("bill.top.everyOne"));
            topBillFlowPane.getChildren().add(everyOneLabel);

            DwLabel everyOneValue = new DwLabel(FontSizeEnum.font20);
            everyOneValue.setPrefSize(labelWidth * 2, labelHeight);
            everyOneValue.getStyleClass().add("topValue");
            everyOneValue.setText("0.00");
            everyOneValue.setTextAlignment(TextAlignment.LEFT);
            everyOneValue.textProperty().bind(everyOneAmt);
            topBillFlowPane.getChildren().add(everyOneValue);

            //未結賬金額
            DwLabel unSettledLabel = new DwLabel(FontSizeEnum.font20);
            unSettledLabel.setPrefSize(labelWidth * 2.5, labelHeight);
            unSettledLabel.getStyleClass().add("topLabel");
            unSettledLabel.setText(Main.languageMap.get("bill.top.unsettle"));
            topBillFlowPane.getChildren().add(unSettledLabel);

            DwLabel unSettledValue = new DwLabel(FontSizeEnum.font20);
            unSettledValue.setPrefSize(labelWidth * 2, labelHeight);
            unSettledValue.getStyleClass().add("topValue");
            unSettledValue.setText("0.00");
            unSettledValue.setTextAlignment(TextAlignment.LEFT);
            unSettledValue.textProperty().bind(unSettledAmt);
            topBillFlowPane.getChildren().add(unSettledValue);


            //清机金额--轉更金額
            DwLabel turnMoreLabel = new DwLabel(FontSizeEnum.font20);
            turnMoreLabel.setPrefSize(labelWidth * 2, labelHeight);
            turnMoreLabel.getStyleClass().add("topLabel");
            turnMoreLabel.setText(Main.languageMap.get("bill.top.turnMore"));
            topBillFlowPane.getChildren().add(turnMoreLabel);

            DwLabel turnMoreValue = new DwLabel(FontSizeEnum.font20);
            turnMoreValue.setPrefSize(labelWidth * 2, labelHeight);
            turnMoreValue.getStyleClass().add("topValue");
            turnMoreValue.setText("0.00");
            turnMoreValue.setTextAlignment(TextAlignment.LEFT);
            turnMoreValue.textProperty().bind(turnMoreAmt);
            topBillFlowPane.getChildren().add(turnMoreValue);


            //未清机金额--未轉更金額
            DwLabel unTurnMoreLabel = new DwLabel(FontSizeEnum.font20);
            unTurnMoreLabel.setPrefSize(labelWidth * 2.5, labelHeight);
            unTurnMoreLabel.getStyleClass().add("topLabel");
            unTurnMoreLabel.setText(Main.languageMap.get("bill.top.unTurnMore"));
            topBillFlowPane.getChildren().add(unTurnMoreLabel);

            DwLabel unTurnMoreValue = new DwLabel(FontSizeEnum.font20);
            unTurnMoreValue.setPrefSize(labelWidth * 2, labelHeight);
            unTurnMoreValue.getStyleClass().add("topValue");
            unTurnMoreValue.setText("0.00");
            unTurnMoreValue.setTextAlignment(TextAlignment.LEFT);
            unTurnMoreValue.textProperty().bind(unTurnMoreAmt);
            topBillFlowPane.getChildren().add(unTurnMoreValue);

            /*************************buttomBillPane****************************************/
            double buttomBillPaneWidth = Main.primaryScreenBounds.getWidth();
            double buttomBillPaneHeight = Main.primaryScreenBounds.getHeight() / 22 * 4;
            buttomBillPane.setPrefSize(buttomBillPaneWidth, buttomBillPaneHeight);
            double buttongap = 5;
            //底部欄按鈕
            double centerBillFlowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.8;
            double outbuttonWidth = (Main.primaryScreenBounds.getWidth() - centerBillFlowPaneWidth - buttongap * 2) / 2;


            double buttonWidth = (centerBillFlowPaneWidth - 7 * buttongap) / 5;

            double buttonHeight = (buttomBillPaneHeight - buttongap) / 2;

            //FlowPane leftButtomPane = new FlowPane();
            leftButtomPane.setPrefSize(outbuttonWidth, buttomBillPaneHeight);
            //leftPane.setPadding(new Insets(0,0,0,buttongap));

            //付款按钮
            DwButton paymentButton = new DwButton("bill.pay", FontSizeEnum.font20, true);
            paymentButton.initButton(outbuttonWidth, buttomBillPaneHeight, Main.languageMap.get("bill.pay"), "paymentbutton");
            paymentButton.setOnAction(event -> {
                takeOrderIndexController.setIsUpdateOrder(false);
                BillDto billDto = billsView.getSelectionModel().getSelectedItem();
                if (AppUtils.isNotBlank(billDto) && billDto.getTranType().equals(TranTypeEnum.N.getValue()) && (billDto.getBillAmt().compareTo(BigDecimal.ZERO) > 0)) {
                    //轉場到payView
                    PayController payController = (PayController) payView.getPresenter();
                    billDto.setBillType(BillTypeEnum.ORDERHISBILL);
                    payController.initData(billDto);
                    Main.showInitialView(payView.getClass());

                } else {
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("popups.content.bill.needselect"), resultMap, billsStage);
                }
            });
            leftButtomPane.getChildren().add(paymentButton);

            //FlowPane centerBillFlowPane = new FlowPane();
            centerButtomFlowPane.setPrefSize(centerBillFlowPaneWidth, buttomBillPaneHeight);
            centerButtomFlowPane.setPadding(new Insets(0, 0, 0, buttongap));
            centerButtomFlowPane.setHgap(buttongap);
            centerButtomFlowPane.setVgap(buttongap);
            centerButtomFlowPane.getStyleClass().add("centerBillFlowPane");
            reloadCenterButtom(1);
            //FlowPane rightButtomPane = new FlowPane();
            rightButtomPane.setPrefSize(outbuttonWidth, buttomBillPaneHeight);
            //rightPane.setPadding(new Insets(0,0,0,buttongap));
            DwButton closeButton = new DwButton(FontSizeEnum.font20);
            closeButton.initButton(outbuttonWidth, buttomBillPaneHeight, Main.languageMap.get("global.close"), "closeButton");
            rightButtomPane.getChildren().add(closeButton);
            closeButton.setOnAction(event1 -> {
                MainController mainController = (MainController) mainView.getPresenter();
                mainController.iniData();
                Main.showInitialView(mainView.getClass());
            });
        //});

    }


    public void loadBillData() {
        checkLeaveSeat = Boolean.parseBoolean(AppUtils.isNotBlank(Main.posSetting.get("checkLeaveSeat")) ? Main.posSetting.get("checkLeaveSeat") : "false");
        Task task = new Task<Void>() {
            @Override
            public Void call() {

                List<BillDto> billDtoList = billService.getBillData(Main.posOutlet);
                billDtos = FXCollections.observableArrayList(billDtoList);
                //得到未結賬的點菜總額
                List<BillDto> unSettledBillDtoList = billService.getOrderBillData(Main.posOutlet);
                Platform.runLater(() -> {
                    billsView.setItems(billDtos);
                    if (AppUtils.isNotBlank(billDtos)) {
                        billsView.getSelectionModel().select(0);
                    }
                    Stream<BillDto> st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()));
                    count.set(String.valueOf(st.count()));
                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()));
                    totalPrice.set(String.valueOf(DecimalUtil.doubleToBigDecimalByPloy(st.collect(Collectors.summarizingDouble(BillDto::getBillAmtDoubleVale)).getSum())));
                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()));
                    cash.set(String.valueOf(DecimalUtil.doubleToBigDecimalByPloy(st.collect(Collectors.summarizingDouble(BillDto::getCashAmttDoubleVale)).getSum())));
                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()));
                    otherPayment.set(String.valueOf(DecimalUtil.doubleToBigDecimalByPloy(st.collect(Collectors.summarizingDouble(BillDto::getOtherAmttDoubleVale)).getSum())));

                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()) && AppUtils.isNotBlank(billDto.getPeriod()) && Integer.parseInt(billDto.getPeriod()) > 0);
                    turnMoreAmt.set(String.valueOf(DecimalUtil.doubleToBigDecimalByPloy(st.collect(Collectors.summarizingDouble(BillDto::getBillAmtDoubleVale)).getSum())));

                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()) && (AppUtils.isBlank(billDto.getPeriod()) || Integer.parseInt(billDto.getPeriod()) == 0));
                    unTurnMoreAmt.set(String.valueOf(DecimalUtil.doubleToBigDecimalByPloy(st.collect(Collectors.summarizingDouble(BillDto::getBillAmtDoubleVale)).getSum())));

                    st = billDtoList.stream().filter(billDto -> billDto.getTranType().equals(TranTypeEnum.N.getValue()));
                    double persons = st.collect(Collectors.summarizingDouble(BillDto::getPersonDoubleValue)).getSum();
                    everyOneAmt.set(DecimalUtil.divide(new BigDecimal(totalPrice.get()), new BigDecimal(persons).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : new BigDecimal(persons)).toString());

                    if(AppUtils.isNotBlank(unSettledBillDtoList)){
                        unSettledAmt.set(AppUtils.isNotBlank(unSettledBillDtoList.get(0).getOrderAmt())?String.valueOf(unSettledBillDtoList.get(0).getOrderAmt()):"0.00");
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }


    public void reloadCenterButtom(Integer page) {
        double buttongap = 5;
        double buttonWidth = (centerButtomFlowPane.getPrefWidth() - 7 * buttongap) / 5;
        double buttonHeight = (centerButtomFlowPane.getPrefHeight() - buttongap) / 2;

        if (page == 1) {
            centerButtomFlowPane.getChildren().clear();
            centerButtomFlowPane.getStyleClass().add("centerBillFlowPane");
            //寻找按钮
            searchButton = new DwButton("bill.search", FontSizeEnum.font20, true);
            searchButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.search"), "button");
            searchButton.setOnAction(event -> {
                List<Integer> indexs = AppUtils.isNotBlank(searchButton.getBindData()) ? (List<Integer>) searchButton.getBindData() : new ArrayList();
                //1.先查找
                if (indexs.size() == 0) {
                    String result = ShowViewUtil.showKeyword(billsStage, null);
                    if (AppUtils.isNotBlank(result)) {
                        //1.1.取得单号长度
                        int refNumLength = Integer.parseInt(Main.posSetting.get("refnum_length"));
                        for (int i = 0; i < billsView.getItems().size(); i++) {
                            if (result.contains(".") && billsView.getItems().get(i).getBillAmt().compareTo(new BigDecimal(result)) == 0) {
                                indexs.add(i);
                            } else if (result.length() < refNumLength && billsView.getItems().get(i).getTableNum().contains(result)) {
                                indexs.add(i);
                            } else if (result.length() >= refNumLength && billsView.getItems().get(i).getRefNumShow().contains(result)) {
                                indexs.add(i);
                            }
                        }
                        searchButton.setBindData(indexs);
                    }
                }
                //2.检索下一个
                if (indexs.size() > 0) {
                    billsView.getSelectionModel().select(indexs.get(0));
                    billsView.scrollTo(indexs.get(0) - 5);
                    indexs.remove(0);
                    searchButton.setText(indexs.size() > 0 ? Main.languageMap.get("bill.searchnext") : Main.languageMap.get("bill.search"));
                }
            });
            centerButtomFlowPane.getChildren().add(searchButton);
            //查單按鈕
            DwButton viewbillButton = new DwButton("bill.viewbill", FontSizeEnum.font20, true);
            viewbillButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.viewbill"), "button");
            viewbillButton.setOnAction(event -> {
                BillDto billDto = billsView.getSelectionModel().getSelectedItem();
                if (AppUtils.isNotBlank(billDto) && billDto.getTranType().equals(TranTypeEnum.N.getValue()) && (billDto.getBillAmt().compareTo(BigDecimal.ZERO) > 0)) {
                    //轉場到viewBillView
                    ViewBillController viewBillController = (ViewBillController) viewBillView.getPresenter();
                    viewBillController.initData(billDto);
                    Main.showInitialView(viewBillView.getClass());

                } else {
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("popups.content.bill.needselect"), resultMap, billsStage);
                }
            });
            centerButtomFlowPane.getChildren().add(viewbillButton);



           /*分單
            DwButton splitButton = new DwButton("bill.split", FontSizeEnum.font20, true);
            splitButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.split"), "button");
            centerButtomFlowPane.getChildren().add(splitButton);*/
            //打印賬單明細
            DwButton printOrderDetailButton = new DwButton("bill.printOrderDetail", FontSizeEnum.font20, false);
            printOrderDetailButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.printOrderDetail"), "button");
            printOrderDetailButton.setOnAction(event -> {
                BillDto billDto = billsView.getSelectionModel().getSelectedItem();
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(Main.languageMap.get("invoice.chinese"),LanguageEnum.ZH_HK.getMapFelid() + "");
                resultMap.put(Main.languageMap.get("invoice.english"),LanguageEnum.EN.getMapFelid() + "");
                resultMap.put(Main.languageMap.get("keyword.cancle"),"-1");
                String result = ShowViewUtil.showChoseView(resultMap,billsStage);
                if(LanguageEnum.EN.getMapFelid() == Integer.parseInt(result)){
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            PrintStyleUtils.printBillDetail(billDto.getRefNum(), billDto.getSubRef(), billDto.getTableNum(), Main.posOutlet, true,LanguageEnum.EN);
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
                else if(LanguageEnum.ZH_HK.getMapFelid() == Integer.parseInt(result)){
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            PrintStyleUtils.printBillDetail(billDto.getRefNum(), billDto.getSubRef(), billDto.getTableNum(), Main.posOutlet, true,LanguageEnum.ZH_HK);
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
            });
            centerButtomFlowPane.getChildren().add(printOrderDetailButton);

            //改單
            DwButton viesButton = new DwButton("bill.vies", FontSizeEnum.font20, true);
            viesButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.vies"), "button");
            centerButtomFlowPane.getChildren().add(viesButton);
            viesButton.setOnAction(event -> {
                BillDto billDto = billsView.getSelectionModel().getSelectedItem();
                if (AppUtils.isNotBlank(billDto)) {
                    TakeOrderIndexController takeOrderIndexController = (TakeOrderIndexController) takeOrderIndexView.getPresenter();
                    PosTranHis posTranHis = posTranHisService.getOnePosTranHis(Main.posOutlet, billDto.getRefNum(), billDto.getSubRef(), TranTypeEnum.N.getValue());
                    List<PosTableDto> posTableDtoList = posTableService.getTablesByFloor(null, null, billDto.getTableNum(), checkLeaveSeat);
                    if (AppUtils.isNotBlank(posTableDtoList)) {
                        takeOrderIndexController.initData(posTranHis, posTableDtoList.get(0), true, InitDataTypeEnum.SETTLE.getValue());
                        Main.showInitialView(takeOrderIndexView.getClass());
                    }
                }
            });

            //重印
            DwButton reprintButton = new DwButton("bill.reprint", FontSizeEnum.font20, true);
            reprintButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.reprint"), "button");
            centerButtomFlowPane.getChildren().add(reprintButton);
            reprintButton.setOnAction(event -> {
                final BigDecimal[] payAmt = {new BigDecimal(0.00)};
                final BigDecimal[] backAmt = {new BigDecimal(0.00)};
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SimpleDateFormat refNumSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                //付款成功后打印付款清单
                //加載付款記錄
                BillDto billDto = billsView.getSelectionModel().getSelectedItem();
                if (billDto != null) {
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            PrintStyleUtils.printPayBill(billDto);
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
            });

            //消單
            DwButton cancelButton = new DwButton("bill.cancel", FontSizeEnum.font20, true);
            cancelButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.cancel"), "button");
            centerButtomFlowPane.getChildren().add(cancelButton);

            //看報表
            DwButton reportButton = new DwButton("bill.report", FontSizeEnum.font20, true);
            reportButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.report"), "button");
            reportButton.setOnMouseClicked(event -> {

                //先整理出来需要的数据，然后跳转到报表界面
                ReportController reportController = (ReportController) reportView.getPresenter();
                reportController.initData("lookReport", Main.languageMap.get("bill.report"), 4);
                Main.showInitialView(reportView.getClass());

            });
            centerButtomFlowPane.getChildren().add(reportButton);

            //轉更按钮
            DwButton turnMoreButton = new DwButton("bill.turnmore", FontSizeEnum.font20, false);
            turnMoreButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.turnmore"), "button");
            turnMoreButton.setOnMouseClicked(event -> {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("report.confirm")+Main.languageMap.get("bill.turnmore")+"？", resultMap, null);
                if (result.equals(ResultEnum.YES.getValue())) {
                    if (cleanMachineComponent.canTurnMore() < 0) {
                        ReportController reportController = (ReportController) reportView.getPresenter();
                        reportController.initData("turnMore", Main.languageMap.get("bill.turnmore")+Main.languageMap.get("main.reports"), 4);
                        Main.showInitialView(reportView.getClass());
                        System.out.println("轉更操作");
                    }
                    /*int turnResult = cleanMachineComponent.turnMore();
                    resultMap.remove(ResultEnum.NO.getName());
                    if (turnResult == 3) {
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), "清機成功", resultMap, null);
                    }*/
                }
                loadBillData();
            });
            centerButtomFlowPane.getChildren().add(turnMoreButton);

            //清机按钮
            DwButton cleanMachineButton = new DwButton("bill.cleanMatchine", FontSizeEnum.font20, false);
            cleanMachineButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.cleanMatchine"), "button");
            cleanMachineButton.setOnMouseClicked(event -> {
                System.out.println("清機操作");
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("report.confirm")+Main.languageMap.get("bill.cleanMatchine")+"？"+Main.languageMap.get("report.clean.cannotbusiness"), resultMap, null);
                if (result.equals(ResultEnum.YES.getValue())) {
                    if (cleanMachineComponent.canClear()) {
                        ReportController reportController = (ReportController) reportView.getPresenter();
                        reportController.initData("clean", Main.languageMap.get("bill.cleanMatchine")+Main.languageMap.get("main.reports"), 4);
                        Main.showInitialView(reportView.getClass());
                    }
                    /*int cleanResult = cleanMachineComponent.cleanMachine();
                    resultMap.remove(ResultEnum.NO.getName());
                    if (cleanResult == 1) {
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), "日結成功", resultMap, null);
                    } else if (cleanResult == -2) {
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), "歷史數據失敗", resultMap, null);
                    } else if (cleanResult == -3) {
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), "日結失敗", resultMap, null);
                    }*/
                }
                loadBillData();
            });
            centerButtomFlowPane.getChildren().add(cleanMachineButton);

            DwButton otherButton = new DwButton(FontSizeEnum.font20);
            otherButton.initButton(buttonWidth, buttonHeight, "", "button");
            centerButtomFlowPane.getChildren().add(otherButton);
        } else {
            /*centerButtomFlowPane.getChildren().clear();
            centerButtomFlowPane.getStyleClass().add("centerBillFlowPan");

            DwButton roundingButton = new DwButton("bill.rounding", FontSizeEnum.font20, true);
            roundingButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.rounding"), "button");
            centerButtomFlowPane.getChildren().add(roundingButton);*/

            /*//分单按钮global.tran.split
            DwButton singleButton = new DwButton("bill.single", FontSizeEnum.font20, false);
            singleButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.single"), "button");
            centerButtomFlowPane.getChildren().add(singleButton);

            //雲單按钮
            DwButton cloudButton = new DwButton("bill.cloud", FontSizeEnum.font20, false);
            cloudButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.cloud"), "button");
            centerButtomFlowPane.getChildren().add(cloudButton);


            DwButton otherButton = new DwButton(FontSizeEnum.font20);
            otherButton.initButton(buttonWidth, buttonHeight, Main.languageMap.get("bill.other"), "button");
            centerButtomFlowPane.getChildren().add(otherButton);
            otherButton.setOnAction(event -> {
                reloadCenterButtom(1);
            });*/


        }


    }


}

