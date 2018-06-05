package com.dw.controller;


import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.*;
import com.dw.enums.*;
import com.dw.exception.PayBillException;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.model.PayInfo;
import com.dw.print.PrintRxTx;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosPayMentService;
import com.dw.service.PosPayService;
import com.dw.util.*;
import com.dw.view.BillsView;
import com.dw.view.MainView;
import com.dw.view.PayView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sun.java2d.pipe.SpanShapeRenderer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 付款界面
 */

@FXMLController
public class PayController {
    @Value("${STATION_ID}")
    private String stationId;
    @FXML
    private FlowPane payFlowPane;

    @FXML
    private FlowPane topflowPane;

    @FXML
    private FlowPane topLeftflowPane;

    @FXML
    private FlowPane topRightflowPane;

    @FXML
    private HBox bottomPane;

    @FXML
    private FlowPane bottomLeftflowPane;

    @FXML
    private GridPane bottomCenterPane;

    @FXML
    private FlowPane bottomRightflowPane;

    @FXML
    private TableView<PosPayDto> paylist;

    @Autowired
    private TakeOrderIndexController takeOrderIndexController;

    @Autowired
    private PayView payView;

    private TableColumn<PosPayDto, String> rTimeCol;
    private TableColumn<PosPayDto, String> payDiscCol;
    private TableColumn<PosPayDto, String> amountCol;
    private TableColumn<PosPayDto, String> overAmtCol;
    private TableColumn<PosPayDto, String> payStaffCol;
    //付款方式列表
    private ObservableList<DwButton> payButtonList = FXCollections.observableArrayList();
    //接受外部參數
    private BillDto billDto;
    //付款信息
    private PayInfo payInfo = new PayInfo();
    private DwLabel refNumLab;
    private DwLabel refNum;
    private DwLabel tableNumLab;
    private DwLabel tableNum;
    private DwLabel orderAmtLab;
    private DwLabel orderAmt;
    private DwLabel servAmtLab;
    private DwLabel servAmt;
    private DwLabel billAmtLab;
    private DwLabel billAmt;
    private DwLabel otherAmtLab;
    private DwLabel otherAmt;
    private DwLabel orderDiscLab;
    private DwLabel orderDisc;
    private DwLabel roundingLab;
    private DwLabel rounding;
    private DwLabel owedLab;
    private DwLabel owed;
    private DwLabel tipsLab;
    private DwLabel tips;
    private DwLabel memNumLab;
    private DwLabel memNum;
    private ObservableList<PosPayMentDto> posPayMentDtos = FXCollections.observableArrayList();
    private ObservableList<PosPayDto> posPayDtos = FXCollections.observableArrayList();


    private double topflowPaneHieght = Main.primaryScreenBounds.getHeight() * 0.75;
    private double bottomPaneHieght = Main.primaryScreenBounds.getHeight() - topflowPaneHieght;
    /*1024*768上邊顯示不出來
    private double topleftflowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.2;
    private double toprightflowPaneWidth = Main.primaryScreenBounds.getWidth()- topleftflowPaneWidth;*/
    private double topleftflowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.3;
    private double toprightflowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.96 - topleftflowPaneWidth;
    private double bottomleftflowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.7;
    private double bottomcenterPaneWidth = Main.primaryScreenBounds.getWidth() * 0.2;
    private double bottomrightflowPaneWidth = Main.primaryScreenBounds.getWidth() - bottomleftflowPaneWidth - bottomcenterPaneWidth;
    private double gap = 5;

    //分頁控制
    private int pageSize = 0;
    private int pageShowcount = 10;
    private int starIndex = 0;
    private int endIndex = 0;
    private int page = 0;
    //刪除的付款方式，只保存有ID，即原有數據
    private List<PosPayDto> removeList = new ArrayList<>();
    //新增的付款方式
    private List<PosPayDto> newList = new ArrayList<>();

    private Stage payStage;
    @Autowired
    private PosPayMentService posPayMentService;
    @Autowired
    private PosPayService posPayService;
    @Autowired
    private BillsView billsView;
    @Autowired
    private MainView mainView;

    public static ListChangeListener payListlisten;

    @Autowired
    private NettyComponent nettyComponent;


    @FXML
    private void initialize() {
        payFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
        payFlowPane.setStyle("-fx-background-color: #34495E;");
        Platform.runLater(() -> {
            payStage = (Stage) payFlowPane.getScene().getWindow();
            tableComponent();
            bottomComponent();
            topComponent();
            addListener();
            bottomLeftflowPane.getChildren().clear();
            bottomLeftflowPane.getChildren().addAll(FXCollections.observableArrayList(payButtonList.subList(starIndex, endIndex)));
        });
    }

    public void tableComponent() {

        rTimeCol = new TableColumn<>();
        rTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.DateToString(cellData.getValue().getRTime(), "HH:mm")));

        payDiscCol = new TableColumn<>();
        payDiscCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayDisc()));

        amountCol = new TableColumn<>();
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount().toString()));

        overAmtCol = new TableColumn<>();
        overAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOverAmt().toString()));

        payStaffCol = new TableColumn<>();
        payStaffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayStaff().toString()));

        paylist.getColumns().addAll(rTimeCol, payDiscCol, amountCol, overAmtCol, payStaffCol);
        paylist.setPlaceholder(new DwLabel());

        double columWidth = (toprightflowPaneWidth - 40) / 5;
        rTimeCol.setPrefWidth(columWidth);
        rTimeCol.setText(Main.languageMap.get("pay.time"));

        payDiscCol.setPrefWidth(columWidth);
        payDiscCol.setText(Main.languageMap.get("pay.payment"));

        amountCol.setPrefWidth(columWidth);
        amountCol.setText(Main.languageMap.get("pay.billamt"));

        overAmtCol.setPrefWidth(columWidth);
        overAmtCol.setText(Main.languageMap.get("pay.overamt"));

        payStaffCol.setPrefWidth(columWidth);
        payStaffCol.setText(Main.languageMap.get("pay.paystaff"));

        paylist.focusModelProperty().addListener(observable -> {
            int rowIndex = paylist.getSelectionModel().getSelectedIndex();
            paylist.getSelectionModel().select(rowIndex);
        });


    }

    public void topComponent() {
        topflowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), topflowPaneHieght);
        topLeftflowPane.setPrefSize(topleftflowPaneWidth, topflowPaneHieght);
        infoPaneComponent();


        topRightflowPane.setPrefSize(toprightflowPaneWidth - 1, topflowPaneHieght);
        paylist.setPrefSize(toprightflowPaneWidth - 1, topflowPaneHieght);


    }

    public void bottomComponent() {

        bottomPane.setPrefSize(Main.primaryScreenBounds.getWidth(), bottomPaneHieght);
        bottomPane.setStyle("-fx-background-color: #707070;");

        bottomLeftflowPane.setPrefSize(bottomleftflowPaneWidth, bottomPaneHieght);
        bottomLeftflowPane.setHgap(gap);
        bottomLeftflowPane.setVgap(gap);
        bottomLeftflowPane.setPadding(new Insets(gap));


        bottomCenterPane.setPrefSize(bottomcenterPaneWidth, bottomPaneHieght);
        bottomCenterPane.setPadding(new Insets(gap));
        bottomCenterPane.setVgap(gap);
        bottomCenterPane.setHgap(gap);

        double bottomCenterButtonWidth = (bottomcenterPaneWidth - 3 * gap) / 2;
        double bottomCenterButtonHeight = (bottomPaneHieght - 3 * gap) / 2;


        DwButton rePrintButton = new DwButton(FontSizeEnum.font20);
        rePrintButton.initButton(bottomCenterButtonWidth, bottomCenterButtonHeight, Main.languageMap.get("pay.reprint"), "");
        bottomCenterPane.add(rePrintButton, 0, 0);

        DwButton integralButton = new DwButton(FontSizeEnum.font20);
        integralButton.initButton(bottomCenterButtonWidth, bottomCenterButtonHeight, Main.languageMap.get("pay.nextpage"), "");
        integralButton.setOnAction(event -> {

            //分頁算法
            starIndex = starIndex + pageShowcount;
            starIndex = pageSize >= starIndex ? starIndex : pageSize;
            endIndex = endIndex + starIndex;
            endIndex = pageSize >= endIndex ? endIndex : pageSize;
            page++;
            if (starIndex == endIndex) {
                page = 0;
                starIndex = 0;
                endIndex = pageSize >= pageShowcount ? pageShowcount : pageSize;
            }
            int finalStarIndex = starIndex;
            int finalEndIndex = endIndex;


            List<DwButton> button = payButtonList.subList(finalStarIndex, finalEndIndex);

            Platform.runLater(() -> bottomLeftflowPane.getChildren().setAll(FXCollections.observableArrayList(button)));


        });
        bottomCenterPane.add(integralButton, 0, 1);

        DwButton nextPageButton = new DwButton(FontSizeEnum.font20);
        nextPageButton.initButton(bottomCenterButtonWidth, bottomCenterButtonHeight, Main.languageMap.get("pay.integral"), "");
        bottomCenterPane.add(nextPageButton, 1, 0);

        //清除按鈕，綁定tableview事件
        DwButton clearButton = new DwButton(FontSizeEnum.font20);
        clearButton.initButton(bottomCenterButtonWidth, bottomCenterButtonHeight, Main.languageMap.get("pay.clear"), "");


        bottomCenterPane.add(clearButton, 1, 1);
        clearButton.setOnAction(event -> {
            //addListener();
            PosPayDto payDto = paylist.getSelectionModel().getSelectedItem();
            paylist.getItems().remove(payDto);

        });
        bottomRightflowPane.setPrefSize(bottomrightflowPaneWidth, bottomPaneHieght);
        bottomRightflowPane.setPadding(new Insets(gap));
        DwButton closeButton = new DwButton(FontSizeEnum.font22);

        closeButton.initButton(bottomrightflowPaneWidth - gap, bottomPaneHieght - gap, Main.languageMap.get("pay.close"), "closeButton");
        closeButton.setOnAction(event -> {
            //如果改单的话不能离开，必须付款完才能离开
            if (takeOrderIndexController.getIsUpdateOrder()) {
                return;
            }
            if (payInfo.getOwed() > 0) {
                //離開付款頁面時候的判斷
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("pay.popups.leave"), resultMap, payStage);
                if (ResultEnum.YES.getValue().equals(result)) {
                    switch (payInfo.getBillType()) {
                        //歷史改腳
                        case ORDERHISBILL:
                            BillsController billsController = (BillsController) billsView.getPresenter();
                            billsController.loadBillData();
                            Main.showInitialView(billsView.getClass());
                            break;
                        case ORDERBILL:
                            MainController mainController = (MainController) mainView.getPresenter();
                            mainController.iniData();
                            Main.showInitialView(mainView.getClass());
                            break;
                        default:
                            break;
                    }
                    //不拍脚直接离开时，删除桌台操作记录
                    takeOrderIndexController.deletePosTableAction(payInfo.getTableNum(), stationId);
                    //已結賬，刷新檯號
                    nettyComponent.sendMessage(NettyMessageTypeEnum.SETTLE);
                }
            } else {
                BillsController billsController = (BillsController) billsView.getPresenter();
                billsController.loadBillData();
                Main.showInitialView(billsView.getClass());
                //已結賬，刷新檯號
                nettyComponent.sendMessage(NettyMessageTypeEnum.SETTLE);
            }

        });
        bottomRightflowPane.getChildren().add(closeButton);
    }


    public void infoPaneComponent() {
        //  topLeftflowPane.getChildren().clear();
        double infoflowPaneHeight = topflowPaneHieght / 12;
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        refNumLab = new DwLabel(FontSizeEnum.font20);
        refNumLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.refnum"), "");
        refNum = new DwLabel(FontSizeEnum.font20);
        refNum.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        refNum.textProperty().bind(payInfo.refNumShowProperty());
        refNum.getStyleClass().add("label2");
        flowPane.getChildren().addAll(refNumLab, refNum);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        tableNumLab = new DwLabel(FontSizeEnum.font20);
        tableNumLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.tablenum"), "");
        tableNum = new DwLabel(FontSizeEnum.font20);
        tableNum.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        tableNum.textProperty().bind(payInfo.tableNumProperty());
        tableNum.getStyleClass().add("label2");
        flowPane.getChildren().addAll(tableNumLab, tableNum);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        orderAmtLab = new DwLabel(FontSizeEnum.font20);
        orderAmtLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pays.orderAmt"), "");
        orderAmt = new DwLabel(FontSizeEnum.font20);
        orderAmt.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        orderAmt.textProperty().bind(payInfo.orderAmtProperty().asString());
        orderAmt.getStyleClass().add("label2");
        flowPane.getChildren().addAll(orderAmtLab, orderAmt);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        servAmtLab = new DwLabel(FontSizeEnum.font20);
        servAmtLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pays.servAmt"), "");
        servAmt = new DwLabel(FontSizeEnum.font20);
        servAmt.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        servAmt.textProperty().bind(payInfo.servAmtProperty().asString());
        servAmt.getStyleClass().add("label2");
        flowPane.getChildren().addAll(servAmtLab, servAmt);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        orderDiscLab = new DwLabel(FontSizeEnum.font20);
        orderDiscLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.orderdisc"), "");
        orderDisc = new DwLabel(FontSizeEnum.font20);
        orderDisc.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        orderDisc.textProperty().bind(payInfo.orderDiscProperty().asString());
        orderDisc.getStyleClass().add("label2");
        flowPane.getChildren().addAll(orderDiscLab, orderDisc);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        roundingLab = new DwLabel(FontSizeEnum.font20);
        roundingLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.rounding"), "");
        rounding = new DwLabel(FontSizeEnum.font20);
        rounding.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        rounding.textProperty().bind(payInfo.roundingProperty().asString());
        rounding.getStyleClass().add("label2");
        flowPane.getChildren().addAll(roundingLab, rounding);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        billAmtLab = new DwLabel(FontSizeEnum.font20);
        billAmtLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.billamt"), "");
        billAmt = new DwLabel(FontSizeEnum.font20);
        billAmt.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        billAmt.textProperty().bind(payInfo.billAmtProperty().asString());
        billAmt.getStyleClass().add("label2");
        flowPane.getChildren().addAll(billAmtLab, billAmt);
        topLeftflowPane.getChildren().add(flowPane);

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        otherAmtLab = new DwLabel(FontSizeEnum.font20);
        otherAmtLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.otheramt"), "");
        otherAmt = new DwLabel(FontSizeEnum.font20);
        otherAmt.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        otherAmt.textProperty().bind(payInfo.otherAmtProperty().asString());
        otherAmt.getStyleClass().add("label2");
        flowPane.getChildren().addAll(otherAmtLab, otherAmt);
        topLeftflowPane.getChildren().add(flowPane);

        /*flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        orderDiscLab = new DwLabel(FontSizeEnum.font20);
        orderDiscLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.orderdisc"), "");
        orderDisc = new DwLabel(FontSizeEnum.font20);

        orderDisc.setPrefSize(topleftflowPaneWidth / 2, infoflowPaneHeight);
        orderDisc.textProperty().bind(payInfo.orderDiscProperty().asString());
        flowPane.getChildren().addAll(orderDiscLab, orderDisc);
        topLeftflowPane.getChildren().add(flowPane);*/

        /*flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        roundingLab = new DwLabel(FontSizeEnum.font20);
        roundingLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.rounding"), "");
        rounding = new DwLabel(FontSizeEnum.font20);

        rounding.setPrefSize(topleftflowPaneWidth / 2, infoflowPaneHeight);
        rounding.textProperty().bind(payInfo.roundingProperty().asString());
        flowPane.getChildren().addAll(roundingLab, rounding);
        topLeftflowPane.getChildren().add(flowPane);*/

        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        owedLab = new DwLabel(FontSizeEnum.font20);
        owedLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.owed"), "");
        owed = new DwLabel(FontSizeEnum.font20);
        owed.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        owed.textProperty().bind(payInfo.owedProperty().asString());
        owed.getStyleClass().add("label2");
        flowPane.getChildren().addAll(owedLab, owed);
        topLeftflowPane.getChildren().add(flowPane);


        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        tipsLab = new DwLabel(FontSizeEnum.font20);
        //Main.languageMap.get("pay.tips")
        tipsLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, "小費", "");
        tips = new DwLabel(FontSizeEnum.font20);
        tips.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        tips.textProperty().bind(payInfo.tipsProperty().asString());
        tips.getStyleClass().add("label2");
        flowPane.getChildren().addAll(tipsLab, tips);
        topLeftflowPane.getChildren().add(flowPane);


        flowPane = new FlowPane();
        flowPane.setPrefSize(topleftflowPaneWidth, infoflowPaneHeight);
        memNumLab = new DwLabel(FontSizeEnum.font20);
        memNumLab.initLable(topleftflowPaneWidth / 2, infoflowPaneHeight, Main.languageMap.get("pay.memNum"), "");
        memNum = new DwLabel(FontSizeEnum.font20);
        memNum.setPrefSize(topleftflowPaneWidth / 3, infoflowPaneHeight);
        memNum.textProperty().bind(payInfo.memNumProperty());
        memNum.getStyleClass().add("label2");
        flowPane.getChildren().addAll(memNumLab, memNum);
        topLeftflowPane.getChildren().add(flowPane);

    }

    public void initData(BillDto billDto) {
        //加載付款信息
        this.billDto = billDto;
        addListener();
        resetData();
        if (AppUtils.isNotBlank(billDto)) {
            if (!takeOrderIndexController.getIsUpdateOrder()) {
                payInfo.setPayInfo(billDto.getStationId(), billDto.getPeriod(), billDto.getRefNum(), billDto.getRefNumShow(),
                        billDto.getTableNum(), billDto.getTranType(), DateUtil.DateToString(billDto.getInTime(), "HH:mm"),
                        billDto.getName1(), billDto.getPerson(), billDto.getBillAmt(), billDto.getCashAmt(),
                        billDto.getOtherAmt(), billDto.getRounding(), billDto.getOrderDisc(), billDto.getSubRef(), billDto.getBillType(), billDto.getOwed(), billDto.getOrderAmt(), billDto.getServAmt(), billDto.getInvoiceNumber());
            } else {
                payInfo.setPayInfo(billDto.getStationId(), billDto.getPeriod(), billDto.getRefNum(), billDto.getRefNumShow(),
                        billDto.getTableNum(), billDto.getTranType(), DateUtil.DateToString(billDto.getInTime(), "HH:mm"),
                        billDto.getName1(), billDto.getPerson(), billDto.getBillAmt(), billDto.getCashAmt(),
                        billDto.getOtherAmt(), billDto.getRounding(), billDto.getOrderDisc(), billDto.getSubRef(), BillTypeEnum.ORDERHISBILL, billDto.getOwed(), billDto.getOrderAmt(), billDto.getServAmt(), billDto.getInvoiceNumber());
            }

            //加載付款方式列表
            try {
                //加載付款記錄
                List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, billDto.getRefNum(), billDto.getSubRef(), billDto.getTranType());
                posPayDtos = FXCollections.observableArrayList(posPayDtoList);
                //加載付款方式列表
                List<PosPayMentDto> posPayMentDtoList = posPayMentService.getPayMentList(Main.posOutlet);
                posPayMentDtos = FXCollections.observableArrayList(posPayMentDtoList);
                paylist.setItems(posPayDtos);
                if (AppUtils.isNotBlank(posPayDtos)) {
                    paylist.getSelectionModel().select(0);
                }
                posPayMentDtos.forEach(posPayMentDto -> {
                    generatePayMentList(posPayMentDto);
                });
                pageSize = payButtonList.size();
                starIndex = 0;
                endIndex = pageSize >= pageShowcount ? pageShowcount : pageSize;
                Platform.runLater(() -> {
                    //addListener();
                    bottomLeftflowPane.getChildren().clear();
                    bottomLeftflowPane.getChildren().addAll(FXCollections.observableArrayList(payButtonList.subList(starIndex, endIndex)));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 生成付款方式列表
     *
     * @param posPayMentDto
     */
    public void generatePayMentList(PosPayMentDto posPayMentDto) {
        DwButton dwButton = new DwButton(FontSizeEnum.font20);
        dwButton.setBindData(posPayMentDto);
        //80是滾動條的寬度，
        double buttonWidht = (bottomleftflowPaneWidth - 8 * gap) / 5;
        double buttonHeight = (bottomPaneHieght - 3 * gap) / 2;


        //有關名稱的要用下面算法來進行判斷哪個是默認語言
        String[] languages = new String[]{posPayMentDto.getDesc1(), posPayMentDto.getDesc2(), posPayMentDto.getDesc3(), posPayMentDto.getDesc4()};
        String languagedefault = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
        dwButton.initButton(buttonWidht, buttonHeight, languagedefault, "");
        //付款方式按鈕點擊事件
        dwButton.setOnAction(event -> {
            if (payInfo.getOwed() > 0) {
                String result = ShowViewUtil.showNumbericKeyboard(payStage, languagedefault + Main.languageMap.get("pay.numberkeyboard.title"), String.valueOf(payInfo.getOwed()), false);
                if (AppUtils.isNotBlank(result)) {
                    PosPayMentDto payMentData = (PosPayMentDto) dwButton.getBindData();
                    System.out.println("查詢結果：" + result);

                    //超付金額保留兩位小數並根據四捨五入的策略計算出金額
                    BigDecimal overAmt = DecimalUtil.doubleToBigDecimalByPloy(payInfo.getOwed() < Double.parseDouble(result) ? Double.parseDouble(result) - payInfo.getOwed() : 0.00);
                    BigDecimal amtount = DecimalUtil.doubleToBigDecimalByPloy(payInfo.getOwed() < Double.parseDouble(result) ? payInfo.getOwed() : Double.parseDouble(result));


                    PosPayDto payDto = null;
                    if (AppUtils.isNotBlank(payMentData)) {
                        String overType = payMentData.getOverType();

                        if (overType.equals(OverTypeEnum.C.getValue())) {
                            payDto = new PosPayDto(Main.posOutlet, stationId, payInfo.getRefNum(), payInfo.getSubRef(), TranTypeEnum.N.getValue(), payMentData.getCode()
                                    , new BigDecimal(payInfo.getBillAmt()), 1, amtount, new BigDecimal(result), overAmt, payInfo.getTableNum()
                                    , new Date(), new Date(), languagedefault, Main.posStaff.getName1(), Main.posStaff.getCode(), BigDecimal.ZERO);
                        } else if (overType.equals(OverTypeEnum.N.getValue())) {
                            if (payInfo.getOwed() < Double.parseDouble(result)) {
                                Map<String, String> resultMap = new LinkedHashMap<>();
                                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), languagedefault + Main.languageMap.get("pay.payment.cannotoveramt"), resultMap, null);
                            }
                        } else if (overType.equals(OverTypeEnum.O.getValue())) {
                            //o和C一樣的處理方式
                            payDto = new PosPayDto(Main.posOutlet, stationId, payInfo.getRefNum(), payInfo.getSubRef(), TranTypeEnum.N.getValue(), payMentData.getCode()
                                    , new BigDecimal(payInfo.getBillAmt()), 1, amtount, new BigDecimal(result), overAmt, payInfo.getTableNum()
                                    , new Date(), new Date(), languagedefault, Main.posStaff.getName1(), Main.posStaff.getCode(), BigDecimal.ZERO);
                        } else if (overType.equals(OverTypeEnum.T.getValue())) {
                            payDto = new PosPayDto(Main.posOutlet, stationId, payInfo.getRefNum(), payInfo.getSubRef(), TranTypeEnum.N.getValue(), payMentData.getCode()
                                    , new BigDecimal(payInfo.getBillAmt()), 1, amtount, new BigDecimal(result), BigDecimal.ZERO, payInfo.getTableNum()
                                    , new Date(), new Date(), languagedefault, Main.posStaff.getName1(), Main.posStaff.getCode(), overAmt);
                        }
                    }

                    if (payDto != null) {
                        paylist.getItems().add(payDto);
                    }
                }
            } else {
                //提示要刪除原有的付款信息
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("pay.popups.pleaseremovepaylog"), resultMap, payStage);
            }

        });

        payButtonList.add(dwButton);
    }

    /**
     * 重置自定義全局變量
     */
    public void resetData() {
        payButtonList.removeAll(payButtonList);
        newList.removeAll(newList);
        removeList.removeAll(removeList);
        posPayDtos.clear();
        paylist.getItems().remove(0, paylist.getItems().size());
    }

    public void addListener() {
        Platform.runLater(() -> {
            if (payListlisten != null) {
                paylist.getItems().removeListener(payListlisten);
            }
            payListlisten = new ListChangeListener<PosPayDto>() {
                @Override
                public void onChanged(Change<? extends PosPayDto> c) {
                    if (c.next()) {
                        if (c.getRemovedSize() > 0) {
                            removeList.addAll(c.getRemoved().stream().filter(posPayDtos -> AppUtils.isNotBlank(posPayDtos.getId())).collect(Collectors.toList()));
                            newList.removeAll(c.getRemoved());
                        } else if (c.getAddedSize() > 0) {
                            newList.addAll(c.getAddedSubList());
                        }

                        paylist.getSelectionModel().select(c.getList().size() - 1);

                        double payOtherAmt = c.getList().stream().filter(posPayDto -> !posPayDto.getPayType().equals("CASH")).collect(Collectors.summarizingDouble(PosPayDto::getAmtountDoubleVale)).getSum();

                        double PayEdAmt = c.getList().stream().collect(Collectors.summarizingDouble(PosPayDto::getAmtountDoubleVale)).getSum();
                        double tipsAmt = c.getList().stream().collect(Collectors.summarizingDouble(PosPayDto::getTipsDoubleVale)).getSum();
                        double owedAmt = payInfo.getBillAmt() - PayEdAmt;
                        payInfo.setOwed(owedAmt);
                        payInfo.setTips(tipsAmt);
                        payInfo.setOtherAmt(payOtherAmt);
                        System.out.println("總金額：" + PayEdAmt + "尚欠：" + owedAmt);
                        if (owedAmt == 0.00) {
                            //如果是正常下单结账成功的话，就直接删除桌台操作记录
                            if (!takeOrderIndexController.getIsUpdateOrder()) {
                                takeOrderIndexController.deletePosTableAction(payInfo.getTableNum(), stationId);
                                //结账完之后发送netty去刷新台号
                                nettyComponent.sendMessage(NettyMessageTypeEnum.CHARGE);
                            }
                            takeOrderIndexController.setIsUpdateOrder(false);
                            payInfoIntoTable();
                        }
                    }
                }
            };
            paylist.getItems().addListener(payListlisten);
        });

    }

    public void payInfoIntoTable() {
        try {
            if (posPayService.addPaybill(payInfo, newList, removeList)) {
                //打印付款清單
                PrintStyleUtils.printPayBill(billDto);
                //是否打印发票
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(Main.languageMap.get("invoice.chinese"), LanguageEnum.ZH_HK.getMapFelid() + "");
                resultMap.put(Main.languageMap.get("invoice.english"), LanguageEnum.EN.getMapFelid() + "");
                resultMap.put(Main.languageMap.get("keyword.cancle"), "-1");
                String result = ShowViewUtil.showChoseView(resultMap, payStage);
                if (LanguageEnum.EN.getMapFelid() == Integer.parseInt(result)) {
                    PrintStyleUtils.printBillDetail(billDto.getRefNum(), billDto.getSubRef(), billDto.getTableNum(), Main.posOutlet, true, LanguageEnum.EN);
                } else if (LanguageEnum.ZH_HK.getMapFelid() == Integer.parseInt(result)) {
                    PrintStyleUtils.printBillDetail(billDto.getRefNum(), billDto.getSubRef(), billDto.getTableNum(), Main.posOutlet, true, LanguageEnum.ZH_HK);
                }
                switch (payInfo.getBillType()) {
                    //歷史改腳
                    case ORDERHISBILL:
                        BillsController billsController = (BillsController) billsView.getPresenter();
                        billsController.loadBillData();
                        Main.showInitialView(billsView.getClass());
                        break;
                    case ORDERBILL:
                        MainController mainController = (MainController) mainView.getPresenter();
                        mainController.iniData();
                        Main.showInitialView(mainView.getClass());
                        break;
                    default:
                        break;
                }
            }
        } catch (PayBillException e) {
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), e.getMessage(), resultMap, payStage);
        }

    }


}
