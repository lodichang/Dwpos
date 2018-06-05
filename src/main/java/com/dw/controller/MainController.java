package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.component.SettleComponent;
import com.dw.component.UpdateTablePersonComponent;
import com.dw.dto.*;
import com.dw.entity.PosTable;
import com.dw.entity.PosTran;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
//import com.dw.print.PrintRxTx;
import com.dw.print.PrintRxTx;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosTableService;
import com.dw.service.PosTranService;
import com.dw.service.TopButtonService;
import com.dw.task.TimeTask;
import com.dw.util.*;
import com.dw.view.*;
import com.sun.javafx.robot.impl.FXRobotHelper;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@FXMLController
public class MainController implements Initializable {
    @FXML
    private BorderPane mainPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private VBox centerVBox;
    @FXML
    private AnchorPane tablesAnchorPane;
    @FXML
    private FlowPane topLeftFlowPane;
    @FXML
    private FlowPane topRightFlowPane;
    @FXML
    private FlowPane areasFlowPane;
    @FXML
    private FlowPane areaPageFlowPane;
    @FXML
    private ImageView headImageView;
    @FXML
    private DwLabel headLabel;
    @FXML
    private ImageView numberImageView;
    @FXML
    private DwLabel numberLabel;
    @FXML
    private ImageView storeImageView;
    @FXML
    private DwLabel storeLabel;
    @FXML
    private ImageView timeImageView;
    @FXML
    private DwLabel timeLabel;
    @Autowired
    private SplitTableView splitTableView;
    @Autowired
    private MainReportView mainReportView;

    private Map<String, FlowPane> tablesMap = new HashedMap();

    Map<String, List<PosTableDto>> groupbyMap = new HashedMap();

    private Boolean isRefreshingTable = true;

    //區域數組
    // private List<String> areas = AppUtils.isNotBlank(Main.posSetting.get("area"))?new ArrayList(Arrays.asList(Main.posSetting.get("area").split(","))):null;
    private List<String> areas;
    //當前所選區域
    // public String currentArea = AppUtils.isNotBlank(Main.posSetting.get("area"))?Main.posSetting.get("area").split(",")[0]:"";
    String currentArea;
    //當前所選區域的頁碼
    public Integer currentAreaPage = 1;
    //當前所選桌臺的頁碼
    public Integer currentTablesPage = 1;

    @Autowired
    private BillsView billsView;

    @Autowired
    private MainView mainView;

    @Autowired
    private ItemModifyView itemModifyView;

    @Autowired
    private PosTableService posTableService;
    @Autowired
    private TopButtonService topButtonService;
    @Autowired
    private OpenTableController openTableController;
    @Autowired
    private UpdateTablePersonComponent updateTablePersonComponent;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private NettyComponent nettyUtil;
    @Autowired
    private SettleComponent settleComponent;
    @Autowired
    private TableSettingView tableSettingView;
    private List<FlowPane> tableFlowPaneList = new LinkedList<>();

    private List<Stage> tableFlowPaneStageList = new LinkedList<>();

    private List<String> showAreaList = new LinkedList<String>();


    private Stage mainStage;
    boolean checkLeaveSeat = false;
    private boolean isUpdatingPerson = false;
    private boolean isSplitTable = false;
    private boolean isInvoice = false;
    private boolean isPrint = false;
    private DwButton updatePersonBtn;
    private DwButton splitTableBtn;
    private DwButton invoiceBtn;
    private DwButton printBtn;
    private List<DwButton> exclusionButton = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            mainStage = (Stage) mainPane.getScene().getWindow();
        });
        checkLeaveSeat = Boolean.parseBoolean(AppUtils.isNotBlank(Main.posSetting.get("checkLeaveSeat")) ? Main.posSetting.get("checkLeaveSeat") : "false");
        areas = AppUtils.isNotBlank(Main.posSetting.get("area")) ? new ArrayList(Arrays.asList(Main.posSetting.get("area").split(","))) : null;
        currentArea = AppUtils.isNotBlank(Main.posSetting.get("area")) ? Main.posSetting.get("area").split(",")[0] : "";
        // 需要先初始化 ，更改人數
        updatePersonBtn = new DwButton("update.table.person", FontSizeEnum.font20) {
            @Override
            public void exclusion() {
                isUpdatingPerson = false;
                // 改变本身颜色
                updatePersonBtn.getStyleClass().remove("yellow");
            }
        };

        // 需要先初始化 ，分臺
        splitTableBtn = new DwButton("global.splitTable", FontSizeEnum.font20) {
            @Override
            public void exclusion() {
                isSplitTable = false;
                // 改变本身颜色
                splitTableBtn.getStyleClass().remove("yellow");
            }
        };

        // 發票按鈕
        invoiceBtn = new DwButton("global.invoiceBtn", FontSizeEnum.font20) {
            @Override
            public void exclusion() {
                isInvoice = false;
                // 改变本身颜色
                invoiceBtn.getStyleClass().remove("yellow");
            }
        };
        //上菜纸
        printBtn = new DwButton("global.printBtn", FontSizeEnum.font20) {
            @Override
            public void exclusion() {
                isPrint = false;
                // 改变本身颜色
                printBtn.getStyleClass().remove("yellow");
            }
        };
        exclusionButton.add(updatePersonBtn);
        exclusionButton.add(splitTableBtn);
        exclusionButton.add(invoiceBtn);
        exclusionButton.add(printBtn);
        //主界面头部
        topComponent();
        //主界面右部分
        centerComponent();
    }


    //主页top顶部设置
    public void topComponent() {
        mainPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
        double topFlowPaneWidth = Main.primaryScreenBounds.getWidth();
        double topFlowPaneHeight = Main.primaryScreenBounds.getHeight() / 18 * 2;
        topFlowPane.setPrefHeight(topFlowPaneHeight);
        topFlowPane.setPrefWidth(topFlowPaneWidth);
        topFlowPane.getStyleClass().add("topFlowPane");

        //顶部左侧
        double topLeftFlowPaneWidth = topFlowPaneWidth / 3 * 2;
        double topLeftFlowPaneHeight = topFlowPaneHeight;

        topLeftFlowPane.setPrefWidth(topLeftFlowPaneWidth);
        topLeftFlowPane.setPrefHeight(topLeftFlowPaneHeight);
        topLeftFlowPane.getStyleClass().add("topLeftFlowPane");
        topLeftFlowPane.setVgap(5);
        topLeftFlowPane.setPadding(new Insets(5, 0, 0, 5));

        double imageLabelWidth = (topLeftFlowPaneWidth - 5) / 18;
        double imageLabelHeight = topLeftFlowPaneHeight / 3;

        //头像图片
        Image headImage = AppUtils.loadImage("user.png");

        headImageView.setFitWidth(imageLabelWidth - 5);
        headImageView.setFitHeight(imageLabelHeight - 5);
        headImageView.setImage(headImage);
        headImageView.setPreserveRatio(true);
        headImageView.getStyleClass().add("headImageView");

        headLabel.setPrefSize(imageLabelWidth * 2, imageLabelHeight);
        headLabel.setText(AppUtils.isBlank(Main.posStaff) ? Main.languageMap.get("main.unlogin") : Main.posStaff.getName1());
        headLabel.getStyleClass().add("label");
        headLabel.setFontSize(FontSizeEnum.font18);

     /*
        Image numberImage = AppUtils.loadImage("number.png");
        numberImageView.setFitWidth(imageLabelWidth);
        numberImageView.setFitHeight(imageLabelHeight);
        numberImageView.setImage(numberImage);

        numberLabel.setPrefSize(imageLabelWidth * 2.5, imageLabelHeight);
        numberLabel.setText(AppUtils.isBlank(Main.posStaff) ? "": Main.posStaff.getCode());
        numberLabel.getStyleClass().add("label");
        numberLabel.setFontSize(FontSizeEnum.font18);
*/
        Image storeImage = AppUtils.loadImage("store.png");
        storeImageView.setFitWidth(imageLabelWidth - 10);
        storeImageView.setFitHeight(imageLabelHeight - 5);
        storeImageView.setImage(storeImage);
        storeImageView.setPreserveRatio(true);

        storeLabel.setText(AppUtils.isBlank(Main.posOutlet) ? "" : Main.posOutlet);
        storeLabel.getStyleClass().add("label");
        storeLabel.setPrefSize(imageLabelWidth * 2.5, imageLabelHeight);
        storeLabel.setFontSize(FontSizeEnum.font18);

        Image timeImage = AppUtils.loadImage("time.png");

        timeImageView.setFitWidth(imageLabelWidth - 10);
        timeImageView.setFitHeight(imageLabelHeight - 10);
        timeImageView.setImage(timeImage);
        timeImageView.setPreserveRatio(true);

        timeLabel.setPrefSize(imageLabelWidth * 10, imageLabelHeight);
        timeLabel.getStyleClass().add("label");
        timeLabel.setFontSize(FontSizeEnum.font18);
        Main.timeLabel = timeLabel;
        TimeTask.changTime();

        double colorLabelWidth = (topLeftFlowPaneWidth - 25) / 6;
        double colorLabelHeight = topFlowPaneHeight / 3 * 1.5;

        for (TableStateEnum tableStateEnum : TableStateEnum.values()) {
            if (AppUtils.isNotBlank(tableStateEnum.getName())) {
                if (!checkLeaveSeat) {
                    if (AppUtils.isBlank(tableStateEnum.getValue()) || (AppUtils.isNotBlank(tableStateEnum.getValue()) && !tableStateEnum.getValue().equals("E"))) {
                        DwLabel colorLabel = new DwLabel(FontSizeEnum.font18);
                        colorLabel.initLable(colorLabelWidth, colorLabelHeight, tableStateEnum.getName(), tableStateEnum.getStyle());
                        colorLabel.getStyleClass().add(tableStateEnum.getStyle());
                        topLeftFlowPane.getChildren().add(colorLabel);
                    }
                } else {
                    DwLabel colorLabel = new DwLabel(FontSizeEnum.font18);
                    colorLabel.initLable(colorLabelWidth, colorLabelHeight, tableStateEnum.getName(), tableStateEnum.getStyle());
                    topLeftFlowPane.getChildren().add(colorLabel);
                }
            }
        }
        //顶部右侧
        double topRightFlowPaneWidth = topFlowPaneWidth / 3 - 1;
        double topRightFlowPaneHeight = topFlowPaneHeight;
        topRightFlowPane.setPrefWidth(topRightFlowPaneWidth);
        topRightFlowPane.setPrefHeight(topLeftFlowPaneHeight);
        topRightFlowPane.getStyleClass().add("topRightFlowPane");

        //區域部分
        double areasFlowPaneWidth = topRightFlowPaneWidth / 6 * 4;
        double areasFlowPaneHeight = topRightFlowPaneHeight;
        if (AppUtils.isNotBlank(areas)) {
            currentArea = areas.get(0);
            reloadAreas(areasFlowPane, 1);
        }
        areasFlowPane.setPrefWidth(areasFlowPaneWidth);
        areasFlowPane.setPrefHeight(areasFlowPaneHeight);
        areasFlowPane.setPadding(new Insets(5, 0, 0, 0));
        areasFlowPane.setHgap(5);


        double areaPageWidth = topRightFlowPaneWidth / 6 * 2 - 1;
        double areaPageHeight = topRightFlowPaneHeight;
        areaPageFlowPane.setPrefWidth(areaPageWidth);
        areaPageFlowPane.setPrefHeight(areaPageHeight);
        areaPageFlowPane.setPadding(new Insets(5, 0, 0, 0));
        areaPageFlowPane.setHgap(5);


        double areaPageButtonWidth = (areaPageWidth - 15) / 2;
        double areaPageButtonHeight = (areaPageHeight - 10);

        DwButton previousPageButton = new DwButton();
        previousPageButton.addExclusion(exclusionButton);
        previousPageButton.setPrefWidth(areaPageButtonWidth);
        previousPageButton.setPrefHeight(areaPageButtonHeight);
        previousPageButton.setGraphic(new ImageView(AppUtils.loadImage("leftArrow.png")));
        previousPageButton.getStyleClass().add("pageButton");
        previousPageButton.setOnAction(event -> {
            reloadAreas(areasFlowPane, currentAreaPage - 1);
        });
        areaPageFlowPane.getChildren().add(previousPageButton);
        DwButton nextPageButton = new DwButton();
        nextPageButton.addExclusion(exclusionButton);
        nextPageButton.setPrefWidth(areaPageButtonWidth);
        nextPageButton.setPrefHeight(areaPageButtonHeight);
        nextPageButton.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
        nextPageButton.getStyleClass().add("pageButton");
        nextPageButton.setOnAction(event -> {
            reloadAreas(areasFlowPane, currentAreaPage + 1);
        });
        areaPageFlowPane.getChildren().add(nextPageButton);
    }


    public void centerComponent() {
        try {

            centerVBox.setPrefSize(Main.primaryScreenBounds.getHeight() / 18 * 16, Main.primaryScreenBounds.getWidth());
            //主页中间区域body
            /*  FlowPane centerFlowPane = new FlowPane();*/
            tablesAnchorPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 18 * 14);
            tablesAnchorPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
            // centerVBox.getChildren().add(tablesAnchorPane);

            //主页中间区域底部
            double hgap = 5.00;
            //按鈕數量
            int btnSize = 11;
            //底部按鈕的寬度
            double subNodeWidth = Main.primaryScreenBounds.getWidth() / btnSize - hgap - btnSize / hgap;
            //底部按鈕的高度
            double subNodeHeight = Main.primaryScreenBounds.getHeight() / 9 - 10;
            FlowPane buttomFlowPane = new FlowPane();
            buttomFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), subNodeHeight);
            buttomFlowPane.setHgap(hgap);
            buttomFlowPane.getStyleClass().add("buttomBg");
            buttomFlowPane.setPadding(new Insets(5));
            //賬單
            DwButton button = new DwButton("main.bills", FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            button.setText(Main.languageMap.get("main.bills"));
            buttomFlowPane.getChildren().add(button);
            button.setOnAction(event -> {
                clearTables();
                BillsController billsController = (BillsController) billsView.getPresenter();
                billsController.loadBillData();
                Main.showInitialView(billsView.getClass());
            });
            //功能
            button = new DwButton("main.menus", FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            button.setText(Main.languageMap.get("fooddiscont"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(button);
            button.setOnAction(event -> {
                /*clearTables();
                Main.showInitialView(FunctionsView.class);*/
                Main.showInitialView(ItemModifyView.class);
                ItemModifyController itemModifyController = (ItemModifyController) itemModifyView.getPresenter();
                itemModifyController.initData();
                itemModifyView.showViewAndWait(Modality.APPLICATION_MODAL);
            });

            /*button = new DwButton(FontSizeEnum.font28);            button.addExclusion(updatePersonBtn);
            button.setText(Main.languageMap.get("main.refresh"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(button);
            button.setOnAction(event -> {
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("popups.title.confirmRefresh"), resultMap, mainStage);
                if (ResultEnum.YES.getValue().equals(result)) {
                    refreshTables(currentTablesPage, currentArea);
                }
            });*/

            //條碼
            button = new DwButton("main.barcodebill", FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            button.setText(Main.languageMap.get("main.barcodebill"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            button.setOnAction(event -> {
                scanCodeToPay("");
            });
            buttomFlowPane.getChildren().add(button);
            //打開錢箱
            button = new DwButton(FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            button.setText(Main.languageMap.get("global.openbox"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(button);
            button.setOnAction(event -> PrintRxTx.printRxTxInstance().printByCommand(Main.posSetting.get("openbox")));


            // 更改人數
            updatePersonBtn.setText(Main.languageMap.get("update.table.person"));
            updatePersonBtn.setPrefSize(subNodeWidth, subNodeHeight);
            updatePersonBtn.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(updatePersonBtn);
            updatePersonBtn.addExclusion(exclusionButton);
            updatePersonBtn.setOnAction(event -> {
                if (!isUpdatingPerson) {
                    isUpdatingPerson = true;
                    updatePersonBtn.getStyleClass().add("yellow");
                } else {
                    isUpdatingPerson = false;
                    updatePersonBtn.getStyleClass().remove("yellow");
                }
            });

            // 分臺
            splitTableBtn.setText(Main.languageMap.get("global.splitTable"));
            splitTableBtn.setPrefSize(subNodeWidth, subNodeHeight);
            splitTableBtn.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(splitTableBtn);
            splitTableBtn.addExclusion(exclusionButton);
            splitTableBtn.setOnAction(event -> {
                if (!isSplitTable) {
                    isSplitTable = true;
                    splitTableBtn.getStyleClass().add("yellow");
                } else {
                    isSplitTable = false;
                    splitTableBtn.getStyleClass().remove("yellow");
                }
            });

            // 發票
            invoiceBtn.setText(Main.languageMap.get("invoice"));
            invoiceBtn.setPrefSize(subNodeWidth, subNodeHeight);
            invoiceBtn.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(invoiceBtn);
            invoiceBtn.addExclusion(exclusionButton);
            invoiceBtn.setOnAction(event -> {
                if (!isInvoice) {
                    isInvoice = true;
                    invoiceBtn.getStyleClass().add("yellow");
                } else {
                    isInvoice = false;
                    invoiceBtn.getStyleClass().remove("yellow");
                }
            });

            //出菜紙
            printBtn.setText(Main.languageMap.get("tran.print"));
            printBtn.setPrefSize(subNodeWidth, subNodeHeight);
            printBtn.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(printBtn);
            printBtn.addExclusion(exclusionButton);
            printBtn.setOnAction(event -> {
                if (!isPrint) {
                    isPrint = true;
                    printBtn.getStyleClass().add("yellow");
                } else {
                    isPrint = false;
                    printBtn.getStyleClass().remove("yellow");
                }
            });
            //簽離
            DwButton signoffButton = new DwButton(FontSizeEnum.font20);
            signoffButton.initButton(subNodeWidth, subNodeHeight, Main.languageMap.get("bill.signoff"), "button");
            buttomFlowPane.getChildren().add(signoffButton);
            signoffButton.getStyleClass().add("menu-button");
            //由於LOGIN調用的主stage所以這裏需要特殊處理
            signoffButton.setOnAction(event -> {
                Main.showInitialView(LoginView.class);
            });
            //桌台設計
            DwButton tableSetButton = new DwButton(FontSizeEnum.font20);
            tableSetButton.initButton(subNodeWidth, subNodeHeight, Main.languageMap.get("table.design"), "button");
            buttomFlowPane.getChildren().add(tableSetButton);
            tableSetButton.getStyleClass().add("menu-button");
            tableSetButton.setOnAction(event -> {
                //啟動新的線程進行轉場，提高性能
                TableSettingsController tableSettingsController = (TableSettingsController) tableSettingView.getPresenter();
                Main.showInitialView(tableSettingView.getClass());
                ObservableList<Stage> stages = FXRobotHelper.getStages();
                tableSettingsController.setNewTableSettingStage(stages.get(0));
                tableSettingsController.refreshTables(1, tableSettingsController.getCurrentArea(), stages.get(0), false);
            });
            /*DwLabel printBtn = new DwLabel("tran.print");
            printBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.print"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("print.png")), ContentDisplay.TOP, Pos.CENTER);
            GridPane.setMargin(printBtn, new Insets(1, 0, 0, 0));
            printBtn.setOnMouseClicked(event -> printReceipt(posTran));*/
            /**
             * 報表功能未實現，先隱藏此按鈕
             */
            button = new DwButton("main.reports", FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            //button.setVisible(false);
            button.setText(Main.languageMap.get("main.reports"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            button.setOnMouseClicked(event -> {
                //MainReportController mainReportController = (MainReportController) mainReportView.getPresenter();
                Main.showInitialView(mainReportView.getClass());
            });
            buttomFlowPane.getChildren().add(button);
            /**
             * 鎖台功能未實現，先隱藏
             */
            /*button = new DwButton("global.locktable", FontSizeEnum.font20);
            button.addExclusion(exclusionButton);
            button.setVisible(false);
            button.setText(Main.languageMap.get("global.locktable"));
            button.setPrefSize(subNodeWidth, subNodeHeight);
            button.getStyleClass().add("menu-button");
            buttomFlowPane.getChildren().add(button);
            button.setOnAction(event -> {
            });*/


//            button = new DwButton(FontSizeEnum.font20);
//            button.addExclusion(exclusionButton);
//            button.setText(Main.languageMap.get("main.prepage"));
//            button.setPrefSize(subNodeWidth, subNodeHeight);
//            button.getStyleClass().add("page-button");
//            buttomFlowPane.getChildren().add(button);
//            button.setOnAction(event -> {
//                if (currentTablesPage > 1 && AppUtils.isNotBlank(currentArea)) {
//                    Platform.runLater(() -> {
//                        refreshTables(currentTablesPage - 1, currentArea);
//                    });
//                }
//            });
//            button = new DwButton(FontSizeEnum.font20);
//            button.addExclusion(exclusionButton);
//            button.setText(Main.languageMap.get("main.nextpage"));
//            button.setPrefSize(subNodeWidth, subNodeHeight);
//            button.getStyleClass().add("page-button");
//            buttomFlowPane.getChildren().add(button);
//            button.setOnAction(event -> {
//                Platform.runLater(() -> {
//                    refreshTables(currentTablesPage + 1, currentArea);
//                });
//            });
            centerVBox.getChildren().add(buttomFlowPane);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 刷新台号
     */

    public void refreshTables(Integer page, String area) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                //刷新桌台前设置isRefreshingTable为true
                isRefreshingTable = true;
                //清空tabaleMap,重新插入桌檯信息
                tablesMap.clear();
                List<PosTableDto> posTableDtoList = posTableService.getTablesByFloor(page, area, null, checkLeaveSeat);
                groupbyMap = posTableDtoList.stream().collect(Collectors.groupingBy(PosTableDto::getParentTableNum));
                Platform.runLater(() -> {
                    //double tableFlowPaneWidth = Main.primaryScreenBounds.getWidth() / 17;
                    double tableFlowPaneWidth = Main.primaryScreenBounds.getWidth() / 14;
                    double tableFlowPaneHeight = Main.primaryScreenBounds.getHeight() / 15;
                    if (AppUtils.isNotBlank(posTableDtoList)) {
                        currentTablesPage = page;
                        clearTables();
                    } else if (AppUtils.isBlank(posTableDtoList) && page > 1) {
                        return;
                    } else if (page == 1 && AppUtils.isBlank(posTableDtoList)) {
                        clearTables();
                    } else {
                        return;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    BigDecimal maxX = new BigDecimal(Main.primaryScreenBounds.getMaxX());
                    BigDecimal maxY = new BigDecimal(Main.primaryScreenBounds.getMaxY());
                    if (AppUtils.isNotBlank(groupbyMap)) {
                        for (Map.Entry<String, List<PosTableDto>> entry : groupbyMap.entrySet()) {
                            List<PosTableDto> tableNumDtoList = entry.getValue();
                            if (AppUtils.isNotBlank(tableNumDtoList)) {
                                if (tableNumDtoList.size() > 1) {
                                    int totalPerson = tableNumDtoList.stream().mapToInt(PosTableDto::getCurrentPerson).sum();
                                    //有搭檯需要顯示
                                    tableNumDtoList.forEach(posTableDto -> {
                                        if (posTableDto.getRemarks().equals("0")) {
                                            //顯示普通臺，如果是搭檯不顯示
                                            //顯示台號flowPane
                                            posTableDto.setTableState(TableStateEnum.SPLITTABLE.getValue());
                                            FlowPane tableFlowPane = new FlowPane();
                                            tableFlowPane.getStyleClass().remove(0, tableFlowPane.getStyleClass().size());
                                            tableFlowPane.setPrefWidth(tableFlowPaneWidth);
                                            tableFlowPane.setPrefHeight(tableFlowPaneHeight);
                                            tablesMap.put(posTableDto.getRoomNum(), tableFlowPane);
                                            splitTable(tableFlowPane, posTableDto, tableNumDtoList.size(), totalPerson);
                                            double x = posTableDto.getXRatio().multiply(maxX).doubleValue();
                                            double y = posTableDto.getYRatio().multiply(maxY).doubleValue() - Main.primaryScreenBounds.getHeight() / 18 * 2;
                                            tableFlowPane.setTranslateY(y);
                                            tableFlowPane.setTranslateX(x);
                                            tablesAnchorPane.getChildren().add(tableFlowPane);

                                        }
                                    });
                                } else {
                                    //沒有搭檯需要顯示。這個時候也要判斷桌臺類型是否搭檯，防止臺號只增加了搭檯沒有添加普通臺。
                                    PosTableDto ptd = tableNumDtoList.get(0);
                                    if (ptd.getRemarks().equals("0")) {
                                        //顯示普通臺，如果是搭檯不顯示
                                        //顯示台號flowPane
                                        FlowPane tableFlowPane = new FlowPane();
                                        tableFlowPane.setPrefWidth(tableFlowPaneWidth);
                                        tableFlowPane.setPrefHeight(tableFlowPaneHeight);
                                        tableFlowPane.getStyleClass().remove(0, tableFlowPane.getStyleClass().size());


                                        initSplitTableFlowPane(tableFlowPane, ptd);

                                        tableFlowPane.setOnMouseClicked(event -> {
                                            Platform.runLater(() -> {
                                                clickAction(tableFlowPane, ptd, SplitTableClickEnum.SINGLE.getValue());
                                            });

                                        });
                                        double x = ptd.getXRatio().multiply(maxX).doubleValue();
                                        double y = ptd.getYRatio().multiply(maxY).doubleValue() - Main.primaryScreenBounds.getHeight() / 18 * 2;
                                        tableFlowPane.setTranslateY(y);
                                        tableFlowPane.setTranslateX(x);
                                        tablesAnchorPane.getChildren().add(tableFlowPane);
                                        tablesMap.put(ptd.getRoomNum(), tableFlowPane);
                                    }
                                }
                            }
                        }
                    }
                    //刷新完之后
                    isRefreshingTable = false;
                });
                return null;
            }
        };
        new Thread(task).start();
    }


    /**
     * 刷新區域
     */
    public void reloadAreas(FlowPane parentFlowPane, Integer areaPage) {
        if (AppUtils.isNotBlank(areas) && areaPage > 0) {
            boolean hasNext = areas.size() - Main.showAreasPageSize * (areaPage - 1) > 0;
            boolean isMax = areas.size() - areaPage * Main.showAreasPageSize < 0;
            int startIndex;
            int endIndex;
            if (hasNext && !isMax) {
                startIndex = (areaPage - 1) * Main.showAreasPageSize;
                endIndex = (areaPage * Main.showAreasPageSize);
            } else if (hasNext && isMax) {
                startIndex = (areaPage - 1) * Main.showAreasPageSize;
                endIndex = areas.size();
            } else {
                return;
            }
            currentAreaPage = areaPage;
            parentFlowPane.getChildren().remove(0, parentFlowPane.getChildren().size());
            double areaButtonWidth = (Main.primaryScreenBounds.getWidth() / 3 / 6 * 4 - (Main.showAreasPageSize + 1) * 5) / Main.showAreasPageSize;
            double areaButtonHeight = (Main.primaryScreenBounds.getHeight() / 18 * 2 - 10);
            List<String> areaArrage = areas.subList(startIndex, endIndex);
            areaArrage.forEach((String area) -> {
                DwButton dwButton = new DwButton("optionArea-" + area, FontSizeEnum.font20);
                dwButton.setText(Main.posSetting.get("area_" + area));
                dwButton.setUserData("optionArea-" + area);
                dwButton.setBindData(area);
                dwButton.setPrefHeight(areaButtonHeight);
                dwButton.setPrefWidth(areaButtonWidth);
                dwButton.getStyleClass().add("areabutton");
                dwButton.addExclusion(exclusionButton);
                ButtonUtil.shadowButton(dwButton);
                parentFlowPane.getChildren().add(dwButton);
                dwButton.setOnAction(event -> {
                    parentFlowPane.getChildren().forEach((Node node) -> {
                        DwButton areaDwButton = (DwButton) node;
                        if (areaDwButton.getBindData().equals(dwButton.getBindData())) {
                            areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                            areaDwButton.getStyleClass().add("selectedButton");
                            currentArea = areaDwButton.getBindData().toString();
                        } else {
                            areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                            areaDwButton.getStyleClass().add("areabutton");
                        }
                    });
                    refreshTables(1, dwButton.getBindData().toString());
                });
            });
            parentFlowPane.getChildren().forEach((Node node) -> {
                DwButton areaDwButton = (DwButton) node;
                if (areaDwButton.getBindData().equals(currentArea)) {
                    areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                    areaDwButton.getStyleClass().add("selectedButton");
                } else {
                    areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                    areaDwButton.getStyleClass().add("areabutton");
                }
            });

        }

    }

    public void clearTables() {
        tablesAnchorPane.getChildren().clear();
        tableFlowPaneStageList.clear();
        tableFlowPaneList.clear();
    }

    /**
     * 初始化數據
     */
    public void iniData() {
        reloadAreas(areasFlowPane, 1);
        refreshTables(currentTablesPage, currentArea);
    }

    /**
     * 初始化數據
     */
    public void iniDataScanCode(String refNum, String subRef) {
        reloadAreas(areasFlowPane, 1);
        refreshTables(currentTablesPage, currentArea);
        scanCodeToPay(Main.posOutlet + refNum + subRef + TranTypeEnum.N.getValue());

    }

    /**
     * 掃碼結賬
     */
    public void scanCodeToPay(String code) {
        code = AppUtils.isNotBlank(code) ? code : ShowViewUtil.showKeywordToPay(Main.getStage(), "");
        if (AppUtils.isNotBlank(code) && !code.equals(ResultEnum.NO.getValue()) && code.length() >= 14) {
            //調付款結帳方法
            String outlet = code.substring(0, 6);
            String refNum = code.substring(6, 11);
            String subRef = code.substring(11, 13);
            String tranType = code.substring(13, 14);
            System.out.println("Main 調付款結帳方法");

            Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
            posTranWrapper.eq("OUTLET", outlet);
            posTranWrapper.eq("REF_NUM", refNum);
            posTranWrapper.eq("SUB_REF", subRef);
            posTranWrapper.eq("TRAN_TYPE", tranType);

            PosTran posTran = posTranService.selectOne(posTranWrapper);

            Wrapper<PosTable> posTableWrapper = new EntityWrapper<>();
            posTableWrapper.eq("ROOM_NUM", posTran.getTableNum());
            PosTable posTable = posTableService.selectOne(posTableWrapper);

            settleComponent.billSettle(InitDataTypeEnum.BARCODE.getValue(), posTran.getTableNum(), posTable.getRoomType(), posTran, null, false);
        }

    }

    /**
     * 有分臺的時候初始化桌臺信息
     */

    private void splitTable(FlowPane tableFlowPane, PosTableDto posTableDto, int size, int totalPerson) {
        tableFlowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(posTableDto.getTableState()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        //先把flowpane中的所有都清空
        tableFlowPane.getChildren().clear();
        //增加內容
        Double tableNoLabelWidth = tableFlowPane.getPrefWidth() - 2;
        Double tableNoLabelHeight = (tableFlowPane.getPrefHeight() - 2) / 5 * 3;
        tableFlowPane.setId(posTableDto.getRoomNum());


        //HBox tableNobox = new HBox();
        // tableNobox.setPrefSize(tableNoLabelWidth, tableNoLabelHeight);
        //顯示台號的Label
        DwLabel tableNoLabel = new DwLabel();
        tableNoLabel.setPrefWidth(tableNoLabelWidth * 0.6);
        tableNoLabel.setPrefHeight(tableNoLabelHeight);
        tableNoLabel.setText(posTableDto.getRoomNum());
        tableNoLabel.setFontSize(FontSizeEnum.font16);
        tableNoLabel.getStyleClass().remove(0, tableNoLabel.getStyleClass().size());
        tableNoLabel.getStyleClass().add("splitTableNo");

        //顯示台號的Label
        DwLabel splitTableNoLabel = new DwLabel();
        splitTableNoLabel.setPrefWidth(tableNoLabelWidth * 0.38);
        splitTableNoLabel.setPrefHeight(tableNoLabelHeight);
        splitTableNoLabel.setText("[" + size + "]");
        splitTableNoLabel.setFontSize(FontSizeEnum.font12);
        splitTableNoLabel.getStyleClass().remove(0, tableNoLabel.getStyleClass().size());
        splitTableNoLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDto.getTableState()));

        //tableNobox.getChildren().addAll(tableNoLabel, splitTableNoLabel);
        tableFlowPane.getChildren().add(tableNoLabel);
        tableFlowPane.getChildren().add(splitTableNoLabel);

        //HBox hbox = new HBox();
        // hbox.setPrefSize(tableFlowPane.getPrefWidth() - 2, (tableFlowPane.getPrefHeight() - 2) / 5);
        //分臺的label
        DwLabel splitTableLabel = new DwLabel();
        Double numberLabelWidth = (tableFlowPane.getPrefWidth() - 6) / 2;
        Double numberLabelHeight = (tableFlowPane.getPrefHeight() - 2) / 5;
        splitTableLabel.setPrefWidth(numberLabelWidth);
        splitTableLabel.setPrefHeight(numberLabelHeight);
        splitTableLabel.setText(Main.languageMap.get("global.splitTable"));
        splitTableLabel.setFontSize(FontSizeEnum.font12);
        splitTableLabel.getStyleClass().remove(0, numberLabel.getStyleClass().size());
        splitTableLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDto.getTableState()));
        tableFlowPane.getChildren().add(splitTableLabel);
        //顯示人數的Label
        DwLabel numberLabel = new DwLabel();
        numberLabel.setPrefWidth(numberLabelWidth);
        numberLabel.setPrefHeight(numberLabelHeight);
        if (posTableDto.getCurrentPerson() == null) {
            numberLabel.setText(0 + "/" + posTableDto.getMaxperson());
        } else {
            numberLabel.setText(totalPerson + "/" + posTableDto.getMaxperson());
        }
        numberLabel.setFontSize(FontSizeEnum.font14);
        numberLabel.getStyleClass().remove(0, numberLabel.getStyleClass().size());
        numberLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDto.getTableState()));
        //hbox.getChildren().addAll(splitTableLabel, numberLabel);
        tableFlowPane.getChildren().add(numberLabel);

        //顯示入座時間的Label
        DwLabel timeLabel = new DwLabel();
        Double timeLabelWidth = tableFlowPane.getPrefWidth() - 2;
        Double timeLabelHeight = (tableFlowPane.getPrefHeight() - 2) / 5;
        timeLabel.setPrefWidth(timeLabelWidth);
        timeLabel.setPrefHeight(timeLabelHeight);
        if (posTableDto.getInTime() != null) {
            timeLabel.setText(simpleDateFormat.format(posTableDto.getInTime()));
        }
        timeLabel.setFontSize(FontSizeEnum.font14);
        timeLabel.getStyleClass().remove(0, timeLabel.getStyleClass().size());
        timeLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDto.getTableState()));
        tableFlowPane.getChildren().add(timeLabel);

        tableFlowPane.setOnMouseClicked(event -> clickAction(tableFlowPane, posTableDto, SplitTableClickEnum.SPLITTABLE.getValue()));
    }


    /**
     * 沒有分臺的時候初始化桌臺信息
     **/
    public void initSplitTableFlowPane(FlowPane tableFlowPane, PosTableDto ptd) {
        tableFlowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(ptd.getTableState()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        //先把flowpane中的所有都清空
        tableFlowPane.getChildren().clear();
        double tableFlowPaneWidth = tableFlowPane.getPrefWidth();
        double tableFlowPaneHeight = tableFlowPane.getPrefHeight();
        //顯示台號的Label
        DwLabel tableNoLabel = new DwLabel();
        Double tableNoLabelWidth = tableFlowPaneWidth - 2;
        Double tableNoLabelHeight = (tableFlowPaneHeight) / 5 * 3;
        tableNoLabel.setPrefWidth(tableNoLabelWidth);
        tableNoLabel.setPrefHeight(tableNoLabelHeight);
        tableNoLabel.setText(ptd.getRoomNum());
        tableFlowPane.setId(ptd.getRoomNum());
        tableNoLabel.setFontSize(FontSizeEnum.font16);
        tableNoLabel.getStyleClass().remove(0, tableNoLabel.getStyleClass().size());
        tableNoLabel.getStyleClass().add(TableStateEnum.getStyleByValue(ptd.getTableState()));
        tableFlowPane.getChildren().add(tableNoLabel);

        //顯示人數的Label
        DwLabel numberLabel = new DwLabel(FontSizeEnum.font14);
        Double numberLabelWidth = tableFlowPaneWidth - 2;
        Double numberLabelHeight = (tableFlowPaneHeight) / 5;
        numberLabel.setPrefWidth(numberLabelWidth);
        numberLabel.setPrefHeight(numberLabelHeight);
        if (ptd.getCurrentPerson() == null) {
            numberLabel.setText(0 + "/" + ptd.getMaxperson());
        } else {
            numberLabel.setText(ptd.getCurrentPerson() + "/" + ptd.getMaxperson());
        }
        numberLabel.getStyleClass().remove(0, numberLabel.getStyleClass().size());
        numberLabel.getStyleClass().add(TableStateEnum.getStyleByValue(ptd.getTableState()));
        //numberLabel.setStyle("-fx-border-width:1px;-fx-border-color: #000000");
        tableFlowPane.getChildren().add(numberLabel);

        //顯示入座時間的Label
        DwLabel timeLabel = new DwLabel();
        Double timeLabelWidth = tableFlowPaneWidth - 2;
        Double timeLabelHeight = (tableFlowPaneHeight) / 5;
        timeLabel.setPrefWidth(timeLabelWidth);
        timeLabel.setPrefHeight(timeLabelHeight);
        if (ptd.getInTime() != null) {
            timeLabel.setText(simpleDateFormat.format(ptd.getInTime()));
        }
        timeLabel.setFontSize(FontSizeEnum.font14);
        timeLabel.getStyleClass().remove(0, timeLabel.getStyleClass().size());
        timeLabel.getStyleClass().add(TableStateEnum.getStyleByValue(ptd.getTableState()));
        tableFlowPane.getChildren().add(timeLabel);
    }

    public void updateTableRemark1(String tableNum, String disPlayType, int times) {
        List<PosTable> posTables = posTableService.getsetUpTableByTableNum(tableNum, disPlayType);
        if (AppUtils.isNotBlank(posTables)) {
            if (disPlayType.equals("0")) {
                for (int i = 0; i < (posTables.size() < times ? posTables.size() : times); i++) {
                    PosTable posTable = posTables.get(i);
                    posTable.setRemark1("1");
                    posTableService.updateById(posTable);
                }
            } else {
                posTables.forEach(posTable -> {
                    posTable.setRemark1("0");
                    posTableService.updateById(posTable);
                });
            }
        }
    }


    public void clickAction(FlowPane tableFlowPane, PosTableDto ptd, String clickType) {
        if (clickType.equals(SplitTableClickEnum.SINGLE.getValue())) {
            // 未開台
            if (isUpdatingPerson && AppUtils.isNotBlank(ptd.getTableState()) && (ptd.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue()) || ptd.getTableState().equals(TableStateEnum.OPENTABLEORDER.getValue()))) {
                updateTablePersonComponent.init(ptd);
            } else if (isSplitTable) {
                //弹出输入框，输入分台数量
                String result = ShowViewUtil.showNumbericKeyboard(Main.getStage(), Main.languageMap.get("splittable.pleaseenter"), "1", false);
                if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                    int times = Integer.parseInt(result.trim()); // 獲取修改人數
                    //把這個臺對應的第一個未搭檯的臺號改成可顯示，點擊的時候顯示這個臺和這個臺對應的所有可以顯示的搭檯
                    updateTableRemark1(ptd.getRoomNum(), "0", times);
                    List<PosTableDto> tableList = posTableService.getDisplayTableByTableNum(ptd.getRoomNum(), checkLeaveSeat);
                    //點此臺之前已經選中了分臺，則時候默認把本台和它的第一個搭檯添加到tableFlowPane中。
                    //得到這個臺對應的所有的搭檯，把第一個未顯示的搭檯添加到pane中
                    ptd.setTableState(TableStateEnum.SPLITTABLE.getValue());
                    splitTable(tableFlowPane, ptd, tableList.size(), 0);
                }
                isSplitTable = false;
                splitTableBtn.getStyleClass().remove("yellow");
                //  netty通知其他POS端刷新
                nettyUtil.sendMessage(NettyMessageTypeEnum.SPLITTABLE);
            } else if (isPrint) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue())) {
                    String tableType = posTableService.getTableTypeByTableNum(ptd.getRoomNum());
                    if (TableTypeEnum.CARRYOUT.getValue().equals(tableType) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(tableType)) {
                        PrintStyleUtils.printTakeOutTicket(posTranDtos.get(0));
                    } else {
                        PrintStyleUtils.printTicket(posTranDtos.get(0));
                    }
                }
                isPrint = false;
                printBtn.getStyleClass().remove("yellow");
            } else if (isInvoice) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue()) && !ptd.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue())) {
                    settleComponent.billSettle(InitDataTypeEnum.BARCODE.getValue(), ptd.getRoomNum(), ptd.getRoomType(), posTranDtos.get(0), null, false);
                    // PrintStyleUtils.printBillDetail(posTranDtos.get(0).getRefNum(), posTranDtos.get(0).getSubRef(), ptd.getRoomNum(), Main.posOutlet, false,LanguageEnum.ZH_HK);
                }
                isInvoice = false;
                invoiceBtn.getStyleClass().remove("yellow");
            } else {
                openTableController.init(ptd);
            }
            isUpdatingPerson = false;
            updatePersonBtn.getStyleClass().remove("yellow");
        } else if (clickType.equals(SplitTableClickEnum.SPLITTABLE.getValue())) {
            if (isSplitTable) {
                //弹出输入框，输入分台数量
                String result = ShowViewUtil.showNumbericKeyboard(Main.getStage(), Main.languageMap.get("splittable.pleaseenter"), "1", false);
                if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                    int times = Integer.parseInt(result.trim()); // 獲取修改人數
                    //把這個臺對應的第一個未搭檯的臺號改成可顯示，點擊的時候顯示這個臺和這個臺對應的所有可以顯示的搭檯
                    updateTableRemark1(ptd.getRoomNum(), "0", times);

                    List<PosTableDto> tableList = posTableService.getDisplayTableByTableNum(ptd.getRoomNum(), checkLeaveSeat);
                    //點此臺之前已經選中了分臺，則時候默認把本台和它的第一個搭檯添加到tableFlowPane中。
                    //得到這個臺對應的所有的搭檯，把第一個未顯示的搭檯添加到pane中
                    splitTable(tableFlowPane, ptd, tableList.size(), 0);
                }
                isSplitTable = false;
                splitTableBtn.getStyleClass().remove("yellow");
                //  netty通知其他POS端刷新
                nettyUtil.sendMessage(NettyMessageTypeEnum.SPLITTABLE);
            } else if (isPrint) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue())) {
                    String tableType = posTableService.getTableTypeByTableNum(ptd.getRoomNum());
                    if (TableTypeEnum.CARRYOUT.getValue().equals(tableType) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(tableType)) {
                        PrintStyleUtils.printTakeOutTicket(posTranDtos.get(0));
                    } else {
                        PrintStyleUtils.printTicket(posTranDtos.get(0));
                    }
                }
                isPrint = false;
                printBtn.getStyleClass().remove("yellow");
            } else if (isInvoice) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue()) && !ptd.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue())) {
                    settleComponent.billSettle(InitDataTypeEnum.BARCODE.getValue(), ptd.getRoomNum(), ptd.getRoomType(), posTranDtos.get(0), null, false);
                    PrintStyleUtils.printBillDetail(posTranDtos.get(0).getRefNum(), posTranDtos.get(0).getSubRef(), ptd.getRoomNum(), Main.posOutlet, false, LanguageEnum.ZH_HK);
                }
                isInvoice = false;
                invoiceBtn.getStyleClass().remove("yellow");
            } else {
                // 打開一個controller,裡邊顯示所有搭檯
                SplitTableController splitTableController = (SplitTableController) splitTableView.getPresenter();
                splitTableController.initData(ptd, checkLeaveSeat);
                splitTableView.getView().setTranslateX(tablesAnchorPane.getPrefWidth() * 0.1);
                splitTableView.getView().setTranslateY(tablesAnchorPane.getPrefHeight() * 0.1);
                tablesAnchorPane.getChildren().add(splitTableView.getView());
            }
            isUpdatingPerson = false;
            updatePersonBtn.getStyleClass().remove("yellow");
        } else if (clickType.equals(SplitTableClickEnum.SPLITSINGLETABLE.getValue())) {
            if (isUpdatingPerson && AppUtils.isNotBlank(ptd.getTableState()) && (ptd.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue()) || ptd.getTableState().equals(TableStateEnum.OPENTABLEORDER.getValue()))) {
                updateTablePersonComponent.init(ptd);
            } else if (isPrint) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue())) {
                    String tableType = posTableService.getTableTypeByTableNum(ptd.getRoomNum());
                    if (TableTypeEnum.CARRYOUT.getValue().equals(tableType) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(tableType)) {
                        PrintStyleUtils.printTakeOutTicket(posTranDtos.get(0));
                    } else {
                        PrintStyleUtils.printTicket(posTranDtos.get(0));
                    }
                }
            } else if (isInvoice) {
                List<PosTran> posTranDtos = posTranService.queryListByTable(ptd.getRoomNum(), TranTypeEnum.N.getValue());
                if (AppUtils.isNotBlank(posTranDtos) && AppUtils.isNotBlank(ptd.getTableState()) && !ptd.getTableState().equals(TableStateEnum.SPLITTABLE.getValue()) && !ptd.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue())) {
                    settleComponent.billSettle(InitDataTypeEnum.BARCODE.getValue(), ptd.getRoomNum(), ptd.getRoomType(), posTranDtos.get(0), null, false);
                    PrintStyleUtils.printBillDetail(posTranDtos.get(0).getRefNum(), posTranDtos.get(0).getSubRef(), ptd.getRoomNum(), Main.posOutlet, false, LanguageEnum.ZH_HK);
                }
                isInvoice = false;
                invoiceBtn.getStyleClass().remove("yellow");
            } else {
                openTableController.init(ptd);
                tablesAnchorPane.getChildren().remove(splitTableView.getView());
            }
        }
        //refreshTables(currentTablesPage, currentArea);
    }


    public void changStyle(Node nodeObject, String tableState, Boolean isSpiltTable) {
        if (nodeObject instanceof FlowPane) {
            FlowPane flowPane = (FlowPane) nodeObject;
            flowPane.getStyleClass().clear();
            flowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(tableState));
            if (flowPane.getChildren().size() > 0) {
                for (int i = 0; i < flowPane.getChildren().size(); i++) {
                    DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(i);
                    dwLabel.getStyleClass().clear();
                    if (isSpiltTable && i == 0 && TableStateEnum.HOLDON.getValue().equals(tableState)) {
                        dwLabel.getStyleClass().add("spiltHoldOnTableNo");
                    } else if (isSpiltTable && i == 0 && TableStateEnum.SPLITTABLE.getValue().equals(tableState)) {
                        dwLabel.getStyleClass().add("splitTableNo");
                    } else {
                        dwLabel.getStyleClass().add(TableStateEnum.getStyleByValue(tableState));
                    }

                }
            }
        }
    /*    } else if (nodeObject instanceof HBox) {
            HBox hbox = (HBox) nodeObject;
            if (hbox.getChildren().size() > 0) {
                for (Node node : hbox.getChildren()) {
                    changStyle(node, tableState);
                }
            }
        } else if (nodeObject instanceof DwLabel) {
            DwLabel dwLabel = (DwLabel) nodeObject;
            dwLabel.getStyleClass().clear();
            dwLabel.getStyleClass().add(TableStateEnum.getStyleByValue(tableState));
        }*/
    }


}

