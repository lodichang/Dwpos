package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.component.SettleComponent;
import com.dw.component.TakerOrderIndexComponemt;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwFoodChoosePane;
import com.dw.extended.DwLabel;
import com.dw.print.PrintStyleUtils;
import com.dw.service.*;
import com.dw.util.*;
import com.dw.view.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 桌台入單界面
 * Created by lodi on 2018/1/9.
 */
@Setter
@Getter
@FXMLController
public class TakeOrderIndexController {
    private Stage orderStage;
    @Value("${STATION_ID}")
    private String stationId;
    @Autowired
    private TranFunctionView tranFunctionView;
    @Autowired
    private BillSettleView billSettleView;
    @FXML
    private Label tableNoLabel;
    @FXML
    private Label totalNumberLabel;
    @FXML
    private SearchFoodController searchFoodController;

    @FXML
    private FlowPane parentFlowPane;
    @FXML
    private GridPane infoPane;
    @FXML
    private FlowPane bottomPane;

    @FXML
    private FlowPane leftFlowPane;
    @FXML
    private FlowPane middleFlowPane;
    @FXML
    private FlowPane addressInfoPane;
    @FXML
    private DwLabel addressInfoLable;
    @FXML
    private FlowPane rightPane;
    @FXML
    private FlowPane functionPane;
    @FXML
    private FlowPane midLeftPane;
    @FXML
    private FlowPane midLeftTopPane;
    @FXML
    private GridPane leftMidTopGridPane;
    @FXML
    private FlowPane midLeftBottomPane;
    @FXML
    private FlowPane midRightPane;
    @FXML
    private GridPane functionGridPane;
    @FXML
    private TableView<TableViewDto> foodTableView;

    private List<PosAttHead> posAttHeadDtoList;
    private Map<String, PosItem> mustAttItemMap = new HashMap<>();
    private Map<String, PosItemDto> mustAttMap = new HashMap<>();
    private List<PosAtt> itemAttList = new ArrayList<>();
    private ObservableList<TableViewDto> tableViewData = FXCollections.observableArrayList();
    private ObservableList<TopButtonDto> topButtonDtos = FXCollections.observableArrayList();
    private ObservableMap<String, PosComb> posCombObservableMap = FXCollections.observableHashMap();
    private ObservableMap<String, List<PosSetmealGroupDto>> setmealGroupDtoObservableMap = FXCollections.observableHashMap();
    //當前右邊菜單顯示的頁面
    private OrderRightPartEnum currentRightPage;


    // 每次添加菜品数量
    private int perAddItemCount = 1;
    private DwLabel perAddItemCountLabel;

    // 数量或口味动作面板显示按钮总数
    private int numberAndInfoPaneCount = 7;
    // 口味或动作每页显示数量
    private int numberAndInfoCount = 5;
    // 账单菜品及口味总金额
    private DwLabel moneyInfoLabel;
    private DwLabel moneyAmtInfoLabel;
    // 点菜总数量
    private DwLabel itemCountLabel;
    private DwLabel tableInfo;
    private DwLabel personInfo;
    private DwLabel sendOrder;
    private DwLabel backBtn;
    private GridPane numberAndInfoPane;
    // 當前台號對象
    private PosTran posTran;
    private PosTableDto posTableDto;
    private boolean timecheck = false;
    @Autowired
    private PosAttActionService posAttActionService;
    @Autowired
    private PosAttSettingService posAttSettingService;
    @Autowired
    private PosAttHeadService posAttHeadService;
    @Autowired
    private PosAttService posAttService;
    @Autowired
    private PosItemStkService posItemStkService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private MainView mainView;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosCategoryService posCategoryService;
    @Autowired
    private PosTAttService posTAttService;
    @Autowired
    private CouponView couponView;
    @Autowired
    private AttModifyView attModifyView;
    @Autowired
    private MemberView memberView;
    @Autowired
    private CancelItemView cancelItemView;
    @Autowired
    private PosItemService posItemService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private ItemChooseView itemChooseView;
    @Autowired
    private SearchKeyBoardView searchKeyBoardView;
    @Autowired
    private PosTimediscService posTimediscService;
    @Autowired
    private TopButtonService topButtonService;
    @Autowired
    private PosCombService posCombService;
    @Autowired
    private SendOrderController sendOrderController;
    @Autowired
    private PosSetmealService posSetmealService;
    @Autowired
    private TakerOrderIndexComponemt takerOrderIndexComponemt;
    @Autowired
    private SettleComponent settleComponent;
    @Autowired
    private PosTableActionService posTableActionService;
    @Autowired
    private PosOrderAddressService posOrderAddressService;
    @Autowired
    private NettyComponent nettyComponent;
    @Autowired
    private AddressView addressView;
    //绑定显示檯号属性-wen.jing
    private StringProperty tableNumProperty = new SimpleStringProperty("");
    //绑定显示人数属性-wen.jing
    private StringProperty personProperty = new SimpleStringProperty("");
    private double takeOrderIndexWidth = Main.primaryScreenBounds.getWidth();
    private double takeOrderIndexHeight = Main.primaryScreenBounds.getHeight();

    private double leftFlowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.47;
    private double rightFlowPaneWidth = takeOrderIndexWidth * 0.464;
    private double middleFlowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.06;

    private double tableViewHeight = Main.primaryScreenBounds.getHeight() * 0.6;
    private DoubleProperty tableViewHeightProp = new SimpleDoubleProperty(tableViewHeight);
    private double functionPaneHeight = Main.primaryScreenBounds.getHeight() * 0.1;
    private double addressInfoPaneHeight = Main.primaryScreenBounds.getHeight() * 0.05;
    private double bottomPaneHeight = Main.primaryScreenBounds.getHeight() * 0.296;

    //@Autowired
    //private CouponController couponController;
    @Autowired
    private MemPeriodService memPeriodService;

    //是否改單
    private Boolean isUpdateOrder;//= false;
    @Autowired
    private SpecifiedPrintView specifiedPrintView;
    private PosAtt clickPosAttDto;
    private PosAttActionDto clickActionDto;
    private PosAttSettingDto clickAttSettingDto;
    private boolean isCoupon = false;
    //是否顯示公共口味
    private boolean isPublicAtt = true;
    //默认的
    private final String comId = "1";
    private String initDataType = "";


    /**
     * 初始化方法
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            orderStage = (Stage) parentFlowPane.getScene().getWindow();
            addListener();
        });
        mainComponent();

    }

    @FXML
    public void close() {
        isCoupon = false;
        moneyInfoLabel.getStyleClass().remove("redText");
        moneyInfoLabel.getStyleClass().add("whiteText");
        Main.showInitialView(MainView.class);
    }
    @FXML
    public void closeByInitDataType(String refNum,String subRef){
        MainController mainController = (MainController) mainView.getPresenter();
        mainController.iniDataScanCode(refNum,subRef);
        Main.showInitialView(mainView.getClass());
    }

    /**
     * @Param posTran
     * @Param posTableDto
     * @Param isUpdateOrder 是否改單,true改單，false正常賬單
     */
    public void initData(PosTran posTran, PosTableDto posTableDto, Boolean isUpdateOrder,String initDataType) {
        this.posTran = posTran;
        this.posTableDto = posTableDto;
        this.personProperty.set(posTran.getPerson().toString());
        this.tableNumProperty.set(posTran.getTableNum());
        this.isUpdateOrder = isUpdateOrder;
        this.initDataType = initDataType;
        isCoupon = false;
        moneyInfoLabel.getStyleClass().remove("redText");
        moneyInfoLabel.getStyleClass().add("whiteText");
        tableViewData.clear();
        addressInfoLable.setText("");

        //初始化已选菜单信息
        if (isUpdateOrder) {
            getOrderHisList();
        } else {
            getOrderList();
        }
        // 查詢已點菜記錄
        Task task = new Task<Void>() {
            @Override
            public Void call() {
            try {
                Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
                memPeriodWrapper.eq("ISVALID", "Y");
                memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
                MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);

                //加载套餐组别
                List<PosSetmealGroupDto> posSetmealGroupDtoList = posSetmealService.getSetmealList();
                setmealGroupDtoObservableMap = FXCollections.observableMap(posSetmealGroupDtoList.stream().collect(Collectors.groupingBy(PosSetmealGroupDto::getCode)));

                List<TopButtonDto> topButtonDtoList;
                topButtonDtoList = topButtonService.getTopButtonList(Main.posOutlet, DateUtil.getWeek(DateUtil.getNowTime()), Main.posPeriodMap.get("price"), AppUtils.isNotBlank(memPeriod) ? memPeriod.getCode() : "ALL");
                topButtonDtos = FXCollections.observableArrayList(topButtonDtoList);
                posAttHeadDtoList = posAttHeadService.queryList();

                //初始化组合信息
                Wrapper<PosComb> posCombWrapper = new EntityWrapper<>();
                posCombWrapper.eq("STATUS", "1");
                List<PosComb> posCombs = posCombService.selectList(posCombWrapper);
                posCombObservableMap = FXCollections.observableMap(posCombs.stream().collect(Collectors.toMap(PosComb::getPosCombKey, Function.identity(), (key1, key2) -> key2)));


                Platform.runLater(() -> {
                /*//重算金额
                calculate();*/
                    openItemChooseView();
                    infoPaneInit(0);

                    //如果是扫码结账,并且点了整单折扣,那么进入到点菜界面以后要打开折扣界面
                    if(AppUtils.isNotBlank(initDataType) && initDataType.equals(InitDataTypeEnum.BARCODE.getValue())){
                        if (currentRightPage.equals(OrderRightPartEnum.COUPON)) {
                            openItemChooseView();
                        } else {
                            openCouponView(LogTypeEnum.FULL);
                        }
                    }
                });
                //檢查是否有外賣信息
                checkAddress();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
            }
        };
        new Thread(task).start();


    }


    public void mainComponent() {


        parentFlowPane.setPrefWidth(takeOrderIndexWidth);
        parentFlowPane.setPrefHeight(takeOrderIndexHeight);
        //构建点菜列表,口味,以及信息区域
        initLeftComponent();
        //构建中间功能按钮
        initMiddleComponent();
        rightPane.setPrefSize(rightFlowPaneWidth, takeOrderIndexHeight);
    }


    /**
     * 初始化左側
     */
    private void initLeftComponent() {
        leftFlowPane.setPrefSize(leftFlowPaneWidth, takeOrderIndexHeight);

        tableComponent();
        functionPaneComponent();
    }


    private void tableComponent() {
        foodTableView.setPlaceholder(new Label());
//        foodTableView.setPrefSize(leftFlowPane.getPrefWidth(), tableViewHeight);
        foodTableView.setPrefWidth(leftFlowPane.getPrefWidth());
        foodTableView.prefHeightProperty().bind(tableViewHeightProp);
        foodTableView.setBorder(Border.EMPTY);
        foodTableView.getStyleClass().add("noheader");
        foodTableView.setEditable(true);
        //foodTableView.getSelectionModel().select(0);
        foodTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        foodTableView.setRowFactory(new Callback<TableView<TableViewDto>, TableRow<TableViewDto>>() {
            @Override
            public TableRow<TableViewDto> call(TableView<TableViewDto> param) {
                return new TableRowControl();
            }
        });


        TableColumn<TableViewDto, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setMinWidth(foodTableView.getPrefWidth() * 0.1);
        qtyCol.setSortType(TableColumn.SortType.DESCENDING);
        qtyCol.setSortable(false);
        qtyCol.setCellFactory(new Callback<TableColumn<TableViewDto, String>, TableCell<TableViewDto, String>>() {
            @Override
            public TableCell<TableViewDto, String> call( // 单元格内容
                                                         TableColumn<TableViewDto, String> arg0) {
                return new TableCell<TableViewDto, String>() {
                    @Override
                    protected void updateItem(final String str, boolean arg1) {
                        super.updateItem(str, arg1);
                        if (arg1) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(new DwFoodChoosePane(TakeOrderIndexController.this, this.getTableView().getItems().get(this.getIndex()), "addItems", 0, qtyCol, this.getIndex()));
                        }
                    }
                };
            }
        });

        TableColumn<TableViewDto, String> itemCodeCol = new TableColumn<>("ItemCode");
        itemCodeCol.setMinWidth(foodTableView.getPrefWidth() * 0.55);
        itemCodeCol.setSortable(false);
        itemCodeCol.setCellFactory(new Callback<TableColumn<TableViewDto, String>, TableCell<TableViewDto, String>>() {
            @Override
            public TableCell<TableViewDto, String> call( // 单元格内容
                                                         TableColumn<TableViewDto, String> arg0) {
                return new TableCell<TableViewDto, String>() {
                    @Override
                    protected void updateItem(final String str, boolean arg1) {
                        super.updateItem(str, arg1);
                        if (arg1) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(new DwFoodChoosePane(TakeOrderIndexController.this, this.getTableView().getItems().get(this.getIndex()), "addItems", 1, itemCodeCol, this.getIndex()));
                        }
                    }
                };
            }
        });

        TableColumn<TableViewDto, String> amtCol = new TableColumn<>("amt");
        amtCol.setMinWidth(foodTableView.getPrefWidth() * 0.15);
        amtCol.setSortable(false);
        amtCol.setCellFactory(new Callback<TableColumn<TableViewDto, String>, TableCell<TableViewDto, String>>() {
            @Override
            public TableCell<TableViewDto, String> call(TableColumn<TableViewDto, String> arg0) {

                TableCell<TableViewDto, String> tableCell = new TableCell<TableViewDto, String>() {
                    @Override
                    protected void updateItem(final String str, boolean arg1) {
                        super.updateItem(str, arg1);
                        if (arg1) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(new DwFoodChoosePane(TakeOrderIndexController.this, this.getTableView().getItems().get(this.getIndex()), "addItems", 2, amtCol, this.getIndex()));
                        }
                    }
                };
                tableCell.setAlignment(Pos.BOTTOM_RIGHT);
                return tableCell;
            }
        });

        TableColumn<TableViewDto, String> actionCol = new TableColumn<>("Action");
        actionCol.setMinWidth(foodTableView.getPrefWidth() * 0.1);
        actionCol.setSortable(false);
        actionCol.setCellFactory(new Callback<TableColumn<TableViewDto, String>, TableCell<TableViewDto, String>>() {
            @Override
            public TableCell<TableViewDto, String> call( // 单元格内容
                                                         TableColumn<TableViewDto, String> arg0) {
                return new TableCell<TableViewDto, String>() {
                    @Override
                    protected void updateItem(final String str, boolean arg1) {
                        super.updateItem(str, arg1);
                        if (arg1) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(null);
                            //setGraphic(new DwFoodChoosePane(this.getTableView().getItems().get(this.getIndex())));
                            setGraphic(new DwFoodChoosePane(TakeOrderIndexController.this, this.getTableView().getItems().get(this.getIndex()), "addItems", 3, actionCol, this.getIndex()));
                        }
                    }
                };
            }
        });

        foodTableView.setItems(tableViewData);
        //取消表格默认显示内容
        DwLabel tableEmptyDesc = new DwLabel(FontSizeEnum.font18);
        tableEmptyDesc.setText("");
        foodTableView.setPlaceholder(tableEmptyDesc);
        foodTableView.getColumns().addAll(qtyCol, itemCodeCol, amtCol, actionCol);
    }


    private void functionPaneComponent() {
        //中间功能区域的布局
        addressInfoPane.setVisible(false);
        addressInfoLable.setVisible(false);
        addressInfoPane.setPrefSize(0, 0);
        functionPane.setPrefSize(leftFlowPane.getPrefWidth(), functionPaneHeight);
        midLeftPane.setPrefSize(functionPane.getPrefWidth() * 0.85, functionPane.getPrefHeight());
        midLeftTopPane.setPrefSize(midLeftPane.getPrefWidth(), midLeftPane.getPrefHeight() * 0.5);
        leftMidTopGridPane.setPrefSize(midLeftTopPane.getPrefWidth(), midLeftTopPane.getPrefHeight());
        if (tableInfo == null) {
            tableInfo = new DwLabel("ORDER_TABLE_INFO", FontSizeEnum.font16);
            leftMidTopGridPane.add(tableInfo, 0, 0);
        }
        tableInfo.textProperty().bind(tableNumProperty);
        tableInfo.setGraphic(new ImageView(AppUtils.loadImage("chair.png")));
        tableInfo.getStyleClass().add("whiteText");
        tableInfo.setAlignment(Pos.CENTER_LEFT);
        tableInfo.setPrefSize(leftMidTopGridPane.getPrefWidth() / 5 * 1.2, leftMidTopGridPane.getPrefHeight());

        if (itemCountLabel == null) {
            itemCountLabel = new DwLabel("ORDER_COUNT_INFO", FontSizeEnum.font16);
            itemCountLabel.setText("0");
            itemCountLabel.setGraphic(new ImageView(AppUtils.loadImage("item.png")));
            itemCountLabel.getStyleClass().add("whiteText");
            itemCountLabel.setAlignment(Pos.CENTER_LEFT);
            itemCountLabel.setPrefSize(leftMidTopGridPane.getPrefWidth() / 5 * 1.1, leftMidTopGridPane.getPrefHeight());
            leftMidTopGridPane.add(itemCountLabel, 1, 0);
        }

        if (moneyInfoLabel == null) {
            moneyInfoLabel = new DwLabel("ORDER_MONEY_INFO", FontSizeEnum.font16);
            moneyInfoLabel.setText("0.00");
            moneyInfoLabel.setGraphic(new ImageView(AppUtils.loadImage("itemMoney.png")));
            moneyInfoLabel.getStyleClass().add("whiteText");
            moneyInfoLabel.setAlignment(Pos.CENTER_LEFT);
            moneyInfoLabel.setPrefSize(leftMidTopGridPane.getPrefWidth() / 3 * 1.1, leftMidTopGridPane.getPrefHeight());
            moneyInfoLabel.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isCoupon) {
                    moneyInfoLabel.getStyleClass().remove("whiteText");
                    moneyInfoLabel.getStyleClass().add("redText");
                } else {
                    moneyInfoLabel.getStyleClass().remove("redText");
                    moneyInfoLabel.getStyleClass().add("whiteText");
                }
            });
            moneyInfoLabel.setOnMouseClicked(event -> {
                if (currentRightPage.equals(OrderRightPartEnum.COUPON)) {
                    openItemChooseView();
                } else {
                    openCouponView(LogTypeEnum.FULL);
                }
            });
            leftMidTopGridPane.add(moneyInfoLabel, 2, 0);
        }
        moneyAmtInfoLabel = new DwLabel();
        moneyAmtInfoLabel.setVisible(false);

        if (personInfo == null) {
            personInfo = new DwLabel("ORDER_PERSON_INFO", FontSizeEnum.font16);
            personInfo.setGraphic(new ImageView(AppUtils.loadImage("person.png")));
            personInfo.textProperty().bind(personProperty);
            personInfo.getStyleClass().add("whiteText");
            personInfo.setAlignment(Pos.CENTER_LEFT);
            personInfo.setPrefSize(leftMidTopGridPane.getPrefWidth() / 5.2, leftMidTopGridPane.getPrefHeight());
            personInfo.setOnMouseClicked(event -> {
                if (!isUpdateOrder) {
                    String result = ShowViewUtil.showNumbericKeyboard(orderStage, Main.languageMap.get("tran.input") + Main.languageMap.get("global.person"), String.valueOf(personInfo.getText()), false);
                    if (!ResultEnum.NO.getValue().equals(result)) {
                        if (AppUtils.isNotBlank(result) && Double.parseDouble(result.trim()) > 0) {
                            result = result.indexOf(".") < 0 ? result : result.substring(0, result.indexOf("."));
                            if (!result.equals(personInfo.getText())) {
                                personProperty.set(result);
                                posTranService.updatePersons(posTran, result);
                            }
                        }
                    }
                }
            });

            leftMidTopGridPane.add(personInfo, 3, 0);
        }

        if (perAddItemCountLabel == null) {
            perAddItemCountLabel = new DwLabel("ORDER_INPUT_INFO", FontSizeEnum.font16);
            perAddItemCountLabel.setText(Main.languageMap.get("tran.input"));
            perAddItemCountLabel.getStyleClass().add("whiteText");
            perAddItemCountLabel.setAlignment(Pos.CENTER_RIGHT);
            perAddItemCountLabel.setPrefSize(leftMidTopGridPane.getPrefWidth() / 5.2, leftMidTopGridPane.getPrefHeight());
            leftMidTopGridPane.add(perAddItemCountLabel, 4, 0);
        }

        midLeftBottomPane.setPrefSize(midLeftPane.getPrefWidth(), midLeftPane.getPrefHeight() * 0.5);
        // 數量等操作按鈕面板
        numberAndInfoPane = (GridPane) midLeftBottomPane.getChildren().get(0);
        numberAndInfoPane.setPrefSize(midLeftBottomPane.getPrefWidth(), midLeftBottomPane.getPrefHeight());
        showNumberAndActionBtn(false, null, 0);

        // 获取右边

        midRightPane.setPrefSize(functionPane.getPrefWidth() * 0.15 - 1, functionPane.getPrefHeight());
        //DwLabel sendOrder = new DwLabel("SEND_ORDER" , FontSizeEnum.font12);
        if (sendOrder == null) {
            sendOrder = new DwLabel(FontSizeEnum.font14);
            sendOrder.setGraphic(new ImageView(AppUtils.loadImage("send.png")));
            sendOrder.setContentDisplay(ContentDisplay.TOP);
            sendOrder.getStyleClass().add("sendOrder");
            sendOrder.setText(Main.languageMap.get("tran.send"));
            sendOrder.getStyleClass().add("whiteText");
            sendOrder.setAlignment(Pos.CENTER);
            sendOrder.setPrefSize(midRightPane.getPrefWidth(), midRightPane.getPrefHeight());
            sendOrder.setOnMouseClicked(event -> {
                sendOrders(true,true);
            });
            midRightPane.getChildren().add(sendOrder);
        }


        bottomPane.setPrefSize(leftFlowPane.getPrefWidth(), bottomPaneHeight);
        infoPane.setPrefSize(bottomPane.getPrefWidth(), bottomPane.getPrefHeight());

    }

    /**
     * 菜品信息
     *
     * @param page
     */
    public void infoPaneInit(int page) {
        infoPane.getStyleClass().remove("border-red");
        showNumberAndActionBtn(false, null, 0);
        backBtn.setGraphic(new ImageView(AppUtils.loadImage("back.png")));
        backBtn.setText("");
        infoPane.getChildren().remove(0, infoPane.getChildren().size());
        int perPageCount = 15;
        int attIndex = page * (perPageCount - 1);
        int pageCount = 0;
        String[] languages = null;
        int row = 0;
        int column = 0;

        if (AppUtils.isNotBlank(posAttHeadDtoList)) {
            pageCount = posAttHeadDtoList.size() / perPageCount + (posAttHeadDtoList.size() % perPageCount > 0 ? 1 : 0);
            for (; row < 3; row++) {
                for (; column < 5; column++) {
                    if (attIndex < posAttHeadDtoList.size() && attIndex < (perPageCount * (page + 1) - (1 + page))) {
                        PosAttHead attHeadDto = posAttHeadDtoList.get(attIndex);
                        languages = new String[]{attHeadDto.getDesc1(), attHeadDto.getDesc2(), attHeadDto.getDesc3(), attHeadDto.getDesc4()};
                        DwLabel btn = new DwLabel(FontSizeEnum.font14);
                        btn.getStyleClass().add("itemInfo");
                        btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                        btn.getStyleClass().add("whiteText");
                        btn.setAlignment(Pos.CENTER);
                        GridPane.setMargin(btn, new Insets(1, 0, 0, column == 0 ? 0 : 1));
                        btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
                        //點擊事件
                        if (AppUtils.isNotBlank(attHeadDto.getPosAttList())) {
                            btn.setOnMouseClicked(event -> showAtt(attHeadDto, null, null, 0));
                        }
                        infoPane.add(btn, column, row);
                        attIndex++;
                    } else {
                        DwLabel btn = new DwLabel(FontSizeEnum.font14);
                        btn.getStyleClass().add("itemInfo");
                        btn.setText("");
                        btn.getStyleClass().add("whiteText");
                        btn.setAlignment(Pos.CENTER);
                        GridPane.setMargin(btn, new Insets(1, 0, 0, column == 0 ? 0 : 1));
                        btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
                        infoPane.add(btn, column, row);
                    }
                }
                column = 0;
            }
        }

        if (posAttHeadDtoList.size() == (perPageCount * (page + 1) - page)) {
            PosAttHead attHeadDto = posAttHeadDtoList.get(attIndex);
            languages = new String[]{attHeadDto.getDesc1(), attHeadDto.getDesc2(), attHeadDto.getDesc3(), attHeadDto.getDesc4()};
            DwLabel btn = new DwLabel(FontSizeEnum.font12);
            btn.getStyleClass().add("itemInfo");
            btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
            btn.getStyleClass().add("whiteText");
            btn.setAlignment(Pos.CENTER);
            GridPane.setMargin(btn, new Insets(1, 0, 0, 1));
            btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
            infoPane.add(btn, 4, row - 1);
            attIndex++;
        } else if (posAttHeadDtoList.size() > (perPageCount * (page + 1)) || (page + 1) == pageCount) {
            // 下一页
            DwLabel btn = new DwLabel(FontSizeEnum.font12);
            btn.getStyleClass().add("itemInfo");
            btn.setText((page + 1) + "/" + pageCount);
            btn.getStyleClass().add("whiteText");
            btn.setAlignment(Pos.CENTER);
            GridPane.setMargin(btn, new Insets(1, 0, 0, 1));
            btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
            int finalPageCount = pageCount;
            btn.setOnMouseClicked(event -> infoPaneInit((page + 1) == finalPageCount ? 0 : page + 1));
            infoPane.add(btn, 4, row - 1);
        }
    }

    /**
     * 初始化中部按鈕
     */
    private void initMiddleComponent() {

        middleFlowPane.setPrefSize(middleFlowPaneWidth, takeOrderIndexHeight);
        functionGridPane.setPrefSize(middleFlowPane.getPrefWidth(), middleFlowPane.getPrefHeight());
        double funBtnHeight = middleFlowPane.getPrefHeight() / 14;
        //全選
        DwLabel checkAllBtn = new DwLabel("tran.uncheck");
        checkAllBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.check.or.uncheck"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("multi_select.png")), ContentDisplay.TOP, Pos.CENTER);
        checkAllBtn.setOnMouseClicked(event -> selectAllItemRows());
        GridPane.setMargin(checkAllBtn, new Insets(0, 0, 0, 0));
        functionGridPane.add(checkAllBtn, 0, 0);
        //刪除
        DwLabel deleteBtn = new DwLabel("tran.itemcancel");
        deleteBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.delete"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("delete.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(deleteBtn, new Insets(1, 0, 0, 0));
        deleteBtn.setOnMouseClicked(event -> {
            //改单时调用对应的删除方法
            if (isUpdateOrder) {
                deleteHaveSendItem();
            } else {
                deleteItem();
            }
        });

        functionGridPane.add(deleteBtn, 0, 1);
        //掛起
        DwLabel holdOnBtn = new DwLabel("tran.holdon");
        holdOnBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.hold.on"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("hand.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(holdOnBtn, new Insets(1, 0, 0, 0));
        holdOnBtn.setOnMouseClicked(event -> {
            //如果为改单，叫起不能使用
            if (isUpdateOrder) {
                return;
            }
            ObservableList<TableViewDto> selectedItems = foodTableView.getSelectionModel().getSelectedItems();
            ObservableList<Integer> selectedIndices = foodTableView.getSelectionModel().getSelectedIndices();
            //是否进行过即起操作
            if (selectedItems != null && selectedItems.size() > 0) {
                Boolean isServe = false;
                for (int i = 0; i < selectedItems.size(); i++) {
                    TableViewDto tvd = selectedItems.get(i);
                    if (tvd.isPrinter()) {
                        if (tvd.getHoldOn() == HoldOnEnum.HOLDON.getValue()) {
                            tvd.setHoldOn(HoldOnEnum.SERVE.getValue());
                            tvd.setIsPrinter(false);
//                        tableViewData.set(selectedIndices.get(i), tvd);
                            if (AppUtils.isNotBlank(tvd.getId())) {
                                PosOrder posOrder = new PosOrder();
                                posOrder.setId(tvd.getId());
                                posOrder.setKicMess("" + tvd.getHoldOn());
                                posOrderService.updateById(posOrder);
                                isServe = true;
                            }
                            sendOrderController.sendOrders(posTran, null, true, PrinterTypeEnums.H, tableNumProperty.get(), null, AppUtils.isNotBlank(tvd.getItemIdx()) ? "" + tvd.getItemIdx() : "" + ";", null, false);
                            tvd.setIsPrinter(true);
//                        tableViewData.set(selectedIndices.get(i), tvd);
                        }
                    } else {
                        if (tvd.getHoldOn() == HoldOnEnum.CANCELCALL.getValue()) {
                            tvd.setHoldOn(HoldOnEnum.HOLDON.getValue());
                        } else if (tvd.getHoldOn() == HoldOnEnum.HOLDON.getValue()) {
                            tvd.setHoldOn(HoldOnEnum.CANCELCALL.getValue());
                        }
//                    tableViewData.set(selectedIndices.get(i), tvd);
                    }
                }
                if (isServe) {
                    sendNettyMessageByHoldOn();
                }
            }
        });
        functionGridPane.add(holdOnBtn, 0, 2);
        //顧客點菜二維碼
        /*DwLabel qrcodeBtn = new DwLabel("tran.qrcode");
        qrcodeBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.qrcode"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("qrcode.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(qrcodeBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(qrcodeBtn, 0, 3); */

        //補單
        DwLabel remedyBtn = new DwLabel("tran.remedy");
        remedyBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.remedy"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("remedy.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(remedyBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(remedyBtn, 0, 3);
        remedyBtn.setOnMouseClicked(event -> {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
            String result = ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("tran.remedy.prompt"), resultMap, orderStage);
            if (ResultEnum.YES.getValue().equals(result)) {
                // 補單
                sendOrders(false,true);
                nettyComponent.sendMessage(NettyMessageTypeEnum.REMEDYORDER);
            } else {

            }

            /*int rowIndex = foodTableView.getSelectionModel().getSelectedIndex();
            if(rowIndex<0){
                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("tran.remedy.pleaseCheck"), resultMap, orderStage);
            }else {}*/
        });

        //優惠券,折扣
        DwLabel couponBtn = new DwLabel("tran.coupon");
        couponBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.coupon"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("coupon.png")), ContentDisplay.TOP, Pos.CENTER);
        couponBtn.setOnMouseClicked(event -> {
            if (currentRightPage.equals(OrderRightPartEnum.COUPON)) {
                openItemChooseView();
            } else {
                openCouponView(LogTypeEnum.FULL);
            }

        });
        GridPane.setMargin(couponBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(couponBtn, 0, 4);
        //項目折扣,折扣
        DwLabel singleCouponBtn = new DwLabel("tran.single.coupon");
        singleCouponBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.single.coupon"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("single_disc.png")), ContentDisplay.TOP, Pos.CENTER);
        singleCouponBtn.setOnMouseClicked(event -> {
            // 獲取選擇的記錄
            List<TableViewDto> selectedItemsList = foodTableView.getSelectionModel().getSelectedItems();
            if (selectedItemsList.size() == 0) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError5"), resultMap, orderStage);
                return;
            } else if (selectedItemsList.size() > 1) {
                // 只需檢測單品及主套餐，套餐明細過濾掉，不參與檢測
                List<TableViewDto> checkList = selectedItemsList.stream().filter(s -> AppUtils.isBlank(s.getMealCode())).collect(Collectors.toList()); // mealCode
                if (checkList.size() != 1) {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError6"), resultMap, orderStage);
                    return;
                }
            } else {
                if (AppUtils.isNotBlank(selectedItemsList.get(0).getMealCode())) {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError7"), resultMap, orderStage);
                    return;
                }
            }
            if (currentRightPage.equals(OrderRightPartEnum.COUPON)) {
                openItemChooseView();
            } else {
                openCouponView(LogTypeEnum.SINGLE);
            }

        });
        GridPane.setMargin(singleCouponBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(singleCouponBtn, 0, 5);
        //會員
        DwLabel discountBtn = new DwLabel("tran.member");
        discountBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.member"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("member.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(discountBtn, new Insets(1, 0, 0, 0));
        discountBtn.setOnMouseClicked(event -> {
            MemberController memberController = (MemberController) memberView.getPresenter();
            memberController.initData(posTran);
            memberView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);

        });
        functionGridPane.add(discountBtn, 0, 6);
        //結賬
        DwLabel settleBtn = new DwLabel("tran.settle");
        settleBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.settle"), "middleFunctionSettledBtn", new ImageView(AppUtils.loadImage("money.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(settleBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(settleBtn, 0, 7);
        settleBtn.setOnMouseClicked(event -> {
            //如果存在菜品叫起，不能结账
            for (TableViewDto t : tableViewData) {
                if (HoldOnEnum.HOLDON.getValue() == t.getHoldOn()) {
                    Map<String, String> map = new HashMap();
                    map.put(Main.languageMap.get("global.confirm"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("order.existHoldOn"), map, orderStage);
                    return;
                }
            }
            if (posTran != null && null != posTran.getSettled() && "FALSE".equals(posTran.getSettled())) {
                // 1.已经结帐未拍脚，2中已经算了账单金额，此处不用再算
                BillSettleController billSettleController = (BillSettleController) billSettleView.getPresenter();
                billSettleController.initData(posTran, Main.posStaff, isUpdateOrder, InitDataTypeEnum.SETTLE.getValue(),posTableDto);
                billSettleView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
            } else {
                // 送单
                sendOrders(true,false);
                // 2.算账单金额，结帐
                settleComponent.billSettle(InitDataTypeEnum.SETTLE.getValue(), posTableDto.getRoomNum(), posTableDto.getRoomType(), posTran, tableViewData, isUpdateOrder);
                //billSettle();
                nettyComponent.sendMessage(NettyMessageTypeEnum.SETTLE);
            }
        });
        //出菜紙
        DwLabel printBtn = new DwLabel("tran.print");
        printBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.print"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("print.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(printBtn, new Insets(1, 0, 0, 0));
        printBtn.setOnMouseClicked(event -> {
            if (TableTypeEnum.CARRYOUT.getValue().equals(posTableDto.getRoomType()) || TableTypeEnum.CARRYOUTPLACE.getValue().equals(posTableDto.getRoomType())) {
                PrintStyleUtils.printTakeOutTicket(posTran);
            } else {
                PrintStyleUtils.printTicket(posTran);
            }
        });

        functionGridPane.add(printBtn, 0, 8);
        //追單
        DwLabel urgeBtn = new DwLabel("tran.urge");
        urgeBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("global.tran.urge"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("urge.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(urgeBtn, new Insets(1, 0, 0, 0));
        urgeBtn.setOnMouseClicked(event -> {
            ObservableList<TableViewDto> tableViewDtos = foodTableView.getSelectionModel().getSelectedItems();
            this.takerOrderIndexComponemt.handleUrge(tableViewDtos, posTran, orderStage);
        });
        functionGridPane.add(urgeBtn, 0, 9);
        /*//單項列印
        DwLabel specifiedprintBtn = new DwLabel("tran.specifiedprint");
        specifiedprintBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("global.tran.specifiedprint"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("specified.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(specifiedprintBtn, new Insets(1, 0, 0, 0));
        specifiedprintBtn.setOnMouseClicked(event -> {

            specifiedPrintView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
        });
        functionGridPane.add(specifiedprintBtn, 0, 9);*/
        /*轉台*/
        DwLabel transferBtn = new DwLabel("tran.transfer");
        transferBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("global.tran.transfer"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("transfer.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(transferBtn, new Insets(1, 0, 0, 0));
        transferBtn.setOnMouseClicked(event -> {
            String tableNum = ShowViewUtil.showKeyword(Main.getStage(), "");
            if (AppUtils.isNotBlank(tableNum) && !tableNum.equals(ResultEnum.NO.getValue())) {
                this.takerOrderIndexComponemt.handleTransfer(posTran, tableNum, orderStage);
            }

        });
        functionGridPane.add(transferBtn, 0, 10);

        /*分台*/
        DwLabel splitBtn = new DwLabel("tran.split");
        splitBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("global.tran.split"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("call-split.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(splitBtn, new Insets(1, 0, 0, 0));
        splitBtn.setOnMouseClicked(event -> {
            ObservableList<TableViewDto> tableViewDtos = foodTableView.getSelectionModel().getSelectedItems();
            AtomicBoolean canSplit = new AtomicBoolean(true);
            tableViewDtos.forEach(dto -> {
                if (AppUtils.isNotBlank(dto.getMealCode())) {
                    canSplit.set(false);
                }
            });
            if (!canSplit.get()) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("tran.cant.split"), resultMap, orderStage);
            } else {
                String tableNum = ShowViewUtil.showKeyword(Main.getStage(), "");
                if (AppUtils.isNotBlank(tableNum) && !tableNum.equals(ResultEnum.NO.getValue())) {
                    this.takerOrderIndexComponemt.handleSplit(tableViewDtos, posTran, tableNum, orderStage);
                }
            }
        });
        functionGridPane.add(splitBtn, 0, 11);

        // 外賣地址
        DwLabel addressBtn = new DwLabel("tran.address");
        addressBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.address"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("address.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(addressBtn, new Insets(1, 0, 0, 0));
        addressBtn.setOnMouseClicked(event -> {
            AddressController addressController = (AddressController) addressView.getPresenter();
            addressController.initData(posTran);
            addressView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
        });
        functionGridPane.add(addressBtn, 0, 12);

        //功能表
        /*DwLabel functionBtn = new DwLabel("tran.function");
        functionBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("tran.function"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("function.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(functionBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(functionBtn, 0, 12);
        functionBtn.setOnMouseClicked(event -> {
            TranFunctionController tranFunctionController = (TranFunctionController) tranFunctionView.getPresenter();
            tranFunctionView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
        });*/

        //關閉
        DwLabel closeBtn = new DwLabel(FontSizeEnum.font12);
        closeBtn.initLable(functionGridPane.getPrefWidth(), funBtnHeight, Main.languageMap.get("global.close"), "middleFunctionBtn", new ImageView(AppUtils.loadImage("close.png")), ContentDisplay.TOP, Pos.CENTER);
        GridPane.setMargin(closeBtn, new Insets(1, 0, 0, 0));
        functionGridPane.add(closeBtn, 0, 13);
        closeBtn.setOnMouseClicked(event -> {
            if ("".equals(posTran.getSettled()) && isUpdateOrder) {
                return;
            } else {
                // 送单
                sendOrders(true,true);
                if (!isUpdateOrder) {
                    deletePosTableAction(posTableDto.getRoomNum(), stationId);
                }
                isCoupon = false;
                moneyInfoLabel.getStyleClass().remove("redText");
                moneyInfoLabel.getStyleClass().add("whiteText");
                MainController mainController = (MainController) mainView.getPresenter();
                Main.showInitialView(mainView.getClass());
                mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
            }
        });
    }

    /**
     * 檢查菜品必選口味
     *
     * @param posItemDto 待檢查菜品
     */

    public void checkMustAtt(PosItemDto posItemDto, int page) {

        // 需要必選口味
        if (AppUtils.isNotBlank(posItemDto) && posItemDto.getAttMin() > 0) {
            isPublicAtt = false;
            infoPane.getChildren().remove(0, infoPane.getChildren().size());
            // 把需要必選口味信息的菜品放到mustAttMap中
            // 送單時候，需要檢查mustAttMap中是否存在必選口味菜品，再檢測此菜品是否已經選擇口味，否則不能送單
            mustAttMap.put(posItemDto.getItemCode(), posItemDto);

            infoPane.getStyleClass().add("border-red");
            int perPageCount = 14;
            int total = posAttService.queryMustAttCountByItemCode(posItemDto.getItemCode());
            int pageCount = total / perPageCount + (total % perPageCount > 0 ? 1 : 0);
            String[] languages = null;
            int attIndex = 0;
            int row = 0;
            int _blankColumnStart = 0;
            boolean isContinue = true;
            // 查詢必選口味信息
            List<PosItemMustAttDto> mustAtt = posAttService.queryMustAttByItemCode(posItemDto.getItemCode(), page * perPageCount, perPageCount);
            if (AppUtils.isNotBlank(mustAtt)) {
                for (; row < 3; row++) {
                    if (isContinue) {
                        for (int column = 0; column < 5; ) {
                            if (attIndex < mustAtt.size()) {
                                PosItemMustAttDto attDto = mustAtt.get(attIndex);
                                languages = new String[]{attDto.getAttDesc1(), attDto.getAttDesc2(), attDto.getAttDesc3(), attDto.getAttDesc4()};
                                DwLabel btn = new DwLabel(FontSizeEnum.font14);
                                btn.getStyleClass().add("itemInfo");
                                btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                                btn.getStyleClass().add("whiteText");
                                btn.setAlignment(Pos.CENTER);
                                GridPane.setMargin(btn, new Insets(1, 0, 0, column == 0 ? 0 : 1));
                                btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
                                infoPane.add(btn, column, row);
                                PosAtt att = new PosAtt();
                                att.setAGroup(attDto.getGroupCode());
                                att.setAmtPro(attDto.getAttAmtPro());
                                att.setBillPrint(attDto.getAttBillPrint());
                                att.setCalAmount(attDto.getAttCalAmount());
                                att.setCalQty(attDto.getAttCalQty());
                                att.setCalType(attDto.getAttCalType());
                                att.setCanChange(attDto.getAttCanChange());
                                att.setCode(attDto.getAttCode());
                                att.setDesc1(attDto.getAttDesc1());
                                att.setDesc2(attDto.getAttDesc2());
                                att.setDesc3(attDto.getAttDesc3());
                                att.setDesc4(attDto.getAttDesc4());
                                att.setIngType(attDto.getAttIngType());
                                att.setPrintDesc(attDto.getAttPrintDesc());
                                att.setStkType(attDto.getAttStkType());
                                att.setRemark1(attDto.getAttRemark1());
                                att.setRemark2(attDto.getAttRemark2());
                                att.setRemark3(attDto.getAttRemark3());
                                btn.setOnMouseClicked(event -> showNumberAndActionBtn(true, att, 0));
                                attIndex++;
                                column++;
                                _blankColumnStart = column;
                            } else {
                                isContinue = false;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                row--;
                // 补空格
                if (attIndex < perPageCount) {
                    for (; row < 3; row++) {
                        for (; _blankColumnStart < 5; _blankColumnStart++) {
                            DwLabel btn = new DwLabel(FontSizeEnum.font14);
                            btn.getStyleClass().add("itemInfo");
                            btn.setText("");
                            btn.getStyleClass().add("whiteText");
                            btn.setAlignment(Pos.CENTER);
                            GridPane.setMargin(btn, new Insets(1, 0, 0, _blankColumnStart == 0 ? 0 : 1));
                            btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
                            infoPane.add(btn, _blankColumnStart, row);
                        }
                        _blankColumnStart = 0;
                    }
                }

                // 下一页
                DwLabel btn = new DwLabel(FontSizeEnum.font14);
                btn.getStyleClass().add("itemInfo");
                btn.setText((page + 1) + "/" + pageCount);
                btn.getStyleClass().add("whiteText");
                btn.setAlignment(Pos.CENTER);
                GridPane.setMargin(btn, new Insets(1, 0, 0, 1));
                btn.setPrefSize(infoPane.getPrefWidth() / 5, bottomPane.getPrefHeight() / 3);
                btn.setOnMouseClicked(event -> checkMustAtt(posItemDto, (page + 1) == pageCount ? 0 : (page + 1)));
                infoPane.add(btn, 4, 2);

                // 顯示返回按鈕
                backBtn.setGraphic(null);
                backBtn.setText(Main.languageMap.get("att.reback"));
                backBtn.getStyleClass().add("whiteText");
                backBtn.setOnMouseClicked(event -> infoPaneInit(0));
                numberAndInfoPane.getChildren().remove(backBtn);
                numberAndInfoPane.add(backBtn, 5, 1);
            }

        } else if (!isPublicAtt) {
            isPublicAtt = true;
            infoPaneInit(0);
        }
    }

    /**
     * 顯示子口味 mod
     *
     * @param attHeadDto
     * @param page
     */
    private void showAtt(PosAttHead attHeadDto, PosAttActionDto posAttActionDto, PosAttSettingDto attSettingDto, int page) {
        infoPane.getChildren().clear();

        int perPageCount = 15;
        int attIndex = page * (perPageCount - 1);
        int pageCount = 0;
        String[] languages = null;
        int row = 0;
        if (AppUtils.isNotBlank(attHeadDto.getPosAttList())) {
            // 顯示所有口味動作
            showNumberAndActionBtn(true, null, 0);

            pageCount = attHeadDto.getPosAttList().size() / perPageCount + (attHeadDto.getPosAttList().size() % perPageCount > 0 ? 1 : 0);
            for (; row < 3; row++) {
                for (int column = 0; column < 5; column++) {
                    if (attIndex < attHeadDto.getPosAttList().size() && attIndex < (perPageCount * (page + 1) - (1 + page))) {
                        PosAtt attDto = attHeadDto.getPosAttList().get(attIndex);
                        languages = new String[]{attDto.getDesc1(), attDto.getDesc2(), attDto.getDesc3(), attDto.getDesc4()};
                        DwButton btn = new DwButton(FontSizeEnum.font14);
                        btn.getStyleClass().add("itemInfo");
                        btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                        btn.getStyleClass().add("whiteText");
                        btn.setAlignment(Pos.CENTER);
                        GridPane.setMargin(btn, new Insets(1, 0, 1, column == 0 ? 0 : 1));
                        btn.setPrefSize((infoPane.getPrefWidth()) / 5, (bottomPane.getPrefHeight()) / 3);
                        infoPane.add(btn, column, row);
                        btn.setOnMouseClicked(event -> {
                            clickPosAttDto = attDto;
                            //showNumberAndActionBtn(true, itemCode, attDto, 0);
                            actionClick();
                        });
                        attIndex++;
                    } else {
                        DwButton btn = new DwButton(FontSizeEnum.font14);
                        btn.getStyleClass().add("itemInfo");
                        btn.setText("");
                        btn.getStyleClass().add("whiteText");
                        btn.setAlignment(Pos.CENTER);
                        GridPane.setMargin(btn, new Insets(1, 0, 1, column == 0 ? 0 : 1));
                        btn.setPrefSize((infoPane.getPrefWidth()) / 5, (bottomPane.getPrefHeight()) / 3);
                        infoPane.add(btn, column, row);
                    }
                }
            }
        }
        if (attHeadDto.getPosAttList().size() == (perPageCount * (page + 1) - page)) {
            PosAtt attDto = attHeadDto.getPosAttList().get(attIndex);
            languages = new String[]{attDto.getDesc1(), attDto.getDesc2(), attDto.getDesc3(), attDto.getDesc4()};
            DwButton btn = new DwButton(FontSizeEnum.font14);
            btn.getStyleClass().add("itemInfo");
            btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
            btn.getStyleClass().add("whiteText");
            btn.setAlignment(Pos.CENTER);
            GridPane.setMargin(btn, new Insets(1, 1, 1, 1));
            btn.setPrefSize((infoPane.getPrefWidth()) / 5, (bottomPane.getPrefHeight()) / 3);
            infoPane.add(btn, 4, row - 1);
            attIndex++;
        } else if (attHeadDto.getPosAttList().size() > (perPageCount * (page + 1)) || (page + 1) == pageCount) {
            // 下一页
            DwButton btn = new DwButton(FontSizeEnum.font14);
            btn.getStyleClass().add("itemInfo");
            btn.setText((page + 1) + "/" + pageCount);
            btn.getStyleClass().add("whiteText");
            btn.setAlignment(Pos.CENTER);
            GridPane.setMargin(btn, new Insets(1, 1, 1, 1));
            btn.setPrefSize((infoPane.getPrefWidth()) / 5, (bottomPane.getPrefHeight()) / 3);
            int finalPageCount = pageCount;
            btn.setOnMouseClicked(event -> showAtt(attHeadDto, posAttActionDto, attSettingDto, (page + 1) == finalPageCount ? 0 : page + 1));
            infoPane.add(btn, 4, row - 1);
        }

        backBtn.setGraphic(null);
        backBtn.setText(Main.languageMap.get("att.reback"));
        backBtn.getStyleClass().add("whiteText");
        backBtn.setOnMouseClicked(event -> infoPaneInit(0));
    }

    /**
     * 顯示口味動作及數量
     */
    private void showNumberAndActionBtn(boolean isShowAction, PosAtt posAttDto, int page) {
        numberAndInfoPane.getChildren().remove(0, numberAndInfoPane.getChildren().size());
        if (isShowAction) {
            // 查詢口味動作
            List<PosAttActionDto> actionDtoList = posAttActionService.queryList(PosAttActionStatusEnum.SHOW.getValue());
            // 獲取當前口味的口味動作
            List<PosAttSettingDto> currentAttSettingList = posAttDto != null ? posAttSettingService.queryListByAtt(posAttDto.getAGroup(), posAttDto.getCode(), null) : new ArrayList<>();
            Map<String, PosAttSettingDto> attSettingDtoMap = currentAttSettingList.stream().collect(Collectors.toMap(PosAttSettingDto::getActionCode, Function.identity(), (key1, key2) -> key2));
            // 添加口味動作面板
            if (AppUtils.isNotBlank(actionDtoList)) {
                int perPageCount = 6;
                int attIndex = page * (perPageCount - 1);
                int pageCount = 0;
                String[] languages = null;
                if (AppUtils.isNotBlank(actionDtoList)) {
                    pageCount = actionDtoList.size() / perPageCount + (actionDtoList.size() % perPageCount > 0 ? 1 : 0);
                    for (int column = 0; column < numberAndInfoCount; column++) {
                        if (attIndex < actionDtoList.size() && attIndex < (perPageCount * (page + 1) - (1 + page))) {
                            PosAttActionDto actionDto = actionDtoList.get(attIndex);
                            languages = new String[]{actionDto.getDesc1(), actionDto.getDesc2(), actionDto.getDesc3(), actionDto.getDesc4()};
                            DwButton btn = new DwButton(FontSizeEnum.font14);
                            if (posAttDto != null && !"FALSE".equals(actionDto.getIsMust()) && (attSettingDtoMap == null || attSettingDtoMap.get(actionDto.getActionCode()) == null)) {
                                btn.setDisable(true);
                            } else {
                                btn.setDisable(false);
                            }
                            btn.getStyleClass().add("itemInfo");
                            btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                            btn.getStyleClass().add("whiteText");
                            btn.setAlignment(Pos.CENTER);
                            GridPane.setMargin(btn, new Insets(1, 0, 1, column == 0 ? 0 : 1));
                            btn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
                            numberAndInfoPane.add(btn, column, 1);
                            btn.setOnMouseClicked(event -> {
                                clickActionDto = actionDto;
                                //clickAttSettingDto = attSettingDtoMap.get(actionDto.getActionCode());
                                actionClick();
                            });
                            attIndex++;
                        } else {
                            DwButton btn = new DwButton(FontSizeEnum.font14);
                            btn.getStyleClass().add("itemInfo");
                            btn.setText("");
                            btn.getStyleClass().add("whiteText");
                            btn.setAlignment(Pos.CENTER);
                            GridPane.setMargin(btn, new Insets(1, 0, 1, column == 0 ? 0 : 1));
                            btn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
                            numberAndInfoPane.add(btn, column, 1);
                        }
                    }
                }
                if (AppUtils.isBlank(currentAttSettingList)) {
                    clickPosAttDto = posAttDto;
                    actionClick();
                }
                if (actionDtoList.size() == (perPageCount * (page + 1) - page)) {
                    PosAttActionDto actionDto = actionDtoList.get(attIndex);
                    languages = new String[]{actionDto.getDesc1(), actionDto.getDesc2(), actionDto.getDesc3(), actionDto.getDesc4()};
                    DwButton btn = new DwButton(FontSizeEnum.font14);
                    if (posAttDto != null && !"FALSE".equals(actionDto.getIsMust()) && (attSettingDtoMap == null || attSettingDtoMap.get(actionDto.getActionCode()) == null)) {
                        btn.setDisable(true);
                    } else {
                        btn.setDisable(false);
                    }
                    btn.getStyleClass().add("itemInfo");
                    btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                    btn.getStyleClass().add("whiteText");
                    btn.setAlignment(Pos.CENTER);
                    GridPane.setMargin(btn, new Insets(1, 0, 1, 1));
                    btn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
                    btn.setOnMouseClicked(event -> {
                        clickActionDto = actionDto;
                        //clickAttSettingDto = attSettingDtoMap.get(actionDto.getActionCode());
                        actionClick();
                    });
                    numberAndInfoPane.add(btn, numberAndInfoPaneCount - 2, 1);
                } else if (actionDtoList.size() > (perPageCount * (page + 1)) || (page + 1) == pageCount) {
                    // 下一页
                    DwButton btn = new DwButton(FontSizeEnum.font14);
                    btn.getStyleClass().add("itemInfo");
                    btn.setText((page + 1) + "/" + pageCount);
                    btn.getStyleClass().add("whiteText");
                    btn.setAlignment(Pos.CENTER);
                    GridPane.setMargin(btn, new Insets(1, 0, 1, 1));
                    btn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
                    int finalPageCount = pageCount;
                    btn.setOnMouseClicked(event -> showNumberAndActionBtn(isShowAction, posAttDto, (page + 1) == finalPageCount ? 0 : page + 1));
                    numberAndInfoPane.add(btn, numberAndInfoPaneCount - 2, 1);
                }
                backBtn.setGraphic(null);
                backBtn.setText(Main.languageMap.get("att.reback"));
                backBtn.getStyleClass().add("whiteText");
                //backBtn.setOnMouseClicked(event -> showAtt(attHeadDto, headPage, itemCode));
                backBtn.setOnMouseClicked(event -> infoPaneInit(0));
                numberAndInfoPane.add(backBtn, numberAndInfoPaneCount - 1, 1);
            } else {
                showNumberAndActionBtn(false, null, 0);
            }
        } else {
            for (int i = 0; i < numberAndInfoCount; i++) {
                DwLabel btn = new DwLabel(FontSizeEnum.font18);
                btn.getStyleClass().add("orderCount");
                switch (i) {
                    case 0:
                        btn.setText("2");
                        break;
                    case 1:
                        btn.setText("3");
                        break;
                    case 2:
                        btn.setText("5");
                        break;
                    case 3:
                        btn.setText("6");
                        break;
                    case 4:
                        btn.setText("8");
                        break;
                }
                btn.setAlignment(Pos.CENTER);
                btn.getStyleClass().add("whiteText");
                btn.setOnMouseClicked(event -> numberClick(Integer.parseInt(btn.getText())));
                GridPane.setMargin(btn, new Insets(0, 1, 0, i == 0 ? 0 : 1));
                btn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
                numberAndInfoPane.add(btn, i, 1);
            }
            backBtn = new DwLabel(FontSizeEnum.font16);
            backBtn.getStyleClass().add("keyboard");
            backBtn.setGraphic(new ImageView(AppUtils.loadImage("back.png")));
            backBtn.setAlignment(Pos.CENTER);
            GridPane.setMargin(backBtn, new Insets(0, 1, 0, 1));
            backBtn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
            backBtn.setOnMouseClicked(event -> initPerAddItemCount());
            numberAndInfoPane.add(backBtn, numberAndInfoPaneCount - 2, 1);

            //DwLabel keyboardBtn = new DwLabel("ORDER_COUNT_KEYBOARD", FontSizeEnum.font12);
            DwLabel keyboardBtn = new DwLabel(FontSizeEnum.font12);
            keyboardBtn.getStyleClass().add("keyboard");
            keyboardBtn.setGraphic(new ImageView(AppUtils.loadImage("keyboard.png")));
            keyboardBtn.setAlignment(Pos.CENTER);
            keyboardBtn.setOnMouseClicked(event -> {
                        if (currentRightPage.equals(OrderRightPartEnum.KEYBORAD)) {
                            openItemChooseView();
                        } else {
                            openSearchKeyBoradView(null);

                        }
                    }
            );
            GridPane.setMargin(keyboardBtn, new Insets(0, 2, 0, 1));
            keyboardBtn.setPrefSize(numberAndInfoPane.getPrefWidth() / numberAndInfoPaneCount, numberAndInfoPane.getPrefHeight());
            numberAndInfoPane.add(keyboardBtn, numberAndInfoPaneCount - 1, 1);
        }
    }

    private void actionClick() {
        if (clickPosAttDto != null) {
            // 檢查是否有口味動作
            // 獲取當前口味的口味動作
            List<PosAttSettingDto> currentAttSettingList = clickPosAttDto != null ? posAttSettingService.queryListByAtt(clickPosAttDto.getAGroup(), clickPosAttDto.getCode(), "TRUE") : new ArrayList<>();
            Map<String, PosAttSettingDto> attSettingDtoMap = currentAttSettingList.stream().collect(Collectors.toMap(PosAttSettingDto::getActionCode, Function.identity(), (key1, key2) -> key2));
            if (AppUtils.isNotBlank(currentAttSettingList) && clickActionDto == null) {
                showNumberAndActionBtn(true, clickPosAttDto, 0);
            } else {
                if (AppUtils.isBlank(currentAttSettingList)) {
                    if (clickActionDto != null && !"FALSE".equals(clickActionDto.getIsMust())) {
                        clickActionDto = null;
                        clickAttSettingDto = null;
                    }
                } else {
                    clickAttSettingDto = attSettingDtoMap.get(clickActionDto.getActionCode());
                }
                String[] actionLanguages = new String[]{"", "", "", ""};
                String actionCode = "";
                if (clickActionDto != null) {
                    actionLanguages = new String[]{clickActionDto.getDesc1(), clickActionDto.getDesc2(), clickActionDto.getDesc3(), clickActionDto.getDesc4()};
                    actionCode = clickActionDto.getActionCode();
                }
                String actionLanguage = LanguageEnum.getLanguage(actionLanguages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));


                String[] attLanguages = new String[]{clickPosAttDto.getDesc1(), clickPosAttDto.getDesc2(), clickPosAttDto.getDesc3(), clickPosAttDto.getDesc4()};
                String attLanguage = LanguageEnum.getLanguage(attLanguages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                String calType = clickPosAttDto.getCalType();
                String amtPro = clickPosAttDto.getAmtPro();
                BigDecimal calAmount = clickPosAttDto.getCalAmount();
                BigDecimal calQty = clickPosAttDto.getCalQty();


                if (foodTableView.getItems().size() > 0) {
                    int rowIndex = foodTableView.getSelectionModel().getSelectedIndex();
                    rowIndex = rowIndex < 0 ? 0 : rowIndex;
                    TableViewDto oldTvd = foodTableView.getItems().get(rowIndex);
                    if ((AppUtils.isBlank(oldTvd.getItemCode()) && AppUtils.isNotBlank(oldTvd.mealCodeProperty())) || AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(oldTvd.getItemCode()))) {
                        return;
                    }
                    if (!oldTvd.isPrinter()) {
                        String viewItemQty = oldTvd.getQty();
                        String viewItemCode = oldTvd.getItemCode();
                        String viewItemName = oldTvd.getItemName();
                        Double viewItemPrice = oldTvd.getPrice();
                        String viewItemAtt = AppUtils.isBlank(oldTvd.getItemAtt()) ? "" : oldTvd.getItemAtt();
                        String viewItemKicmsg = AppUtils.isBlank(oldTvd.getItemKicMsg()) ? "" : oldTvd.getItemKicMsg();
                        String viewItemAttCode = AppUtils.isBlank(oldTvd.getItemAttCode()) ? "" : oldTvd.getItemAttCode();
                        String viewKicMsgCode = AppUtils.isBlank(oldTvd.getKicMsgCode()) ? "" : oldTvd.getKicMsgCode();
                        Double viewAttAmt = AppUtils.isNotBlank(oldTvd.getAttAmt()) ? oldTvd.getAttAmt() : 0.00;
                        String viewItemAttPrices = AppUtils.isBlank(oldTvd.getAttPrices()) ? "" : oldTvd.getAttPrices();
                        String viewAttSubtractQty = AppUtils.isBlank(oldTvd.getAttSubtractQty()) ? "" : oldTvd.getAttSubtractQty();


                        BigDecimal foodAmt = DecimalUtil.multiply(new BigDecimal(viewItemQty), new BigDecimal(viewItemPrice));

                        BigDecimal settingCalAmount = new BigDecimal(0.00);
                        String settingPriceName = "";
                        if (AppUtils.isNotBlank(clickAttSettingDto)) {
                            settingCalAmount = clickAttSettingDto.getCalAmount();
                            settingPriceName = "+" + settingCalAmount;
                        } else {
                            if (calType.equals(PosAttCalTypeEnum.ADD.getValue())) {
                                settingPriceName = "+" + calAmount;
                                settingCalAmount = calAmount;
                            } else if (calType.equals(PosAttCalTypeEnum.MULTIPLY.getValue())) {
                                settingPriceName = "*" + calAmount;
                                //如果是乘，则只拿倍数乘单价算出来单份价格，再下边再乘数量。
                                settingCalAmount = DecimalUtil.multiply(calAmount, new BigDecimal(viewItemPrice));
                            } else if (calType.equals(PosAttCalTypeEnum.SUBTRACT.getValue())) {
                                if (amtPro.equals(AMTPROEnum.AMT.getValue())) {
                                    //半份按照實際金額扣減
                                    settingPriceName = "-" + calAmount;
                                    //如果是半份，则只拿倍数乘商品金額算出来半份金額，再下边再加口味數量,如果已經有半份的口味則不再處理第二次的半份。
                                    if (!viewItemAttPrices.contains(settingPriceName)) {
                                        settingCalAmount = DecimalUtil.subtract(BigDecimal.ZERO, calAmount);
                                    }
                                } else if (amtPro.equals(AMTPROEnum.PRO.getValue())) {
                                    //半份按照百分比扣減
                                    settingPriceName = "/" + calAmount;
                                    //如果是半份，则只拿倍数乘商品金額算出来半份金額，再下边再加口味數量,如果已經有半份的口味則不再處理第二次的半份。
                                    if (!viewItemAttPrices.contains(settingPriceName)) {
                                        settingCalAmount = DecimalUtil.subtract(BigDecimal.ZERO, DecimalUtil.multiply(new BigDecimal(viewItemPrice), calAmount));
                                    }
                                }

                            }
                        }


                        BigDecimal attSingleAmt = DecimalUtil.multiply(new BigDecimal(viewItemQty), settingCalAmount);
                        viewAttAmt = Double.parseDouble(DecimalUtil.add(new BigDecimal(viewAttAmt), attSingleAmt).toString());
                        TableViewDto tvd = new TableViewDto(viewItemQty,
                                viewItemCode,
                                viewItemName, viewItemAtt + actionLanguage + attLanguage + "/", viewItemKicmsg,
                                viewItemPrice,
                                Double.parseDouble(DecimalUtil.add(foodAmt, new BigDecimal(viewAttAmt)).toString()),
                                viewItemAttCode + clickPosAttDto.getAGroup() + ":" + clickPosAttDto.getCode() + ":" + actionCode + "@",
                                viewKicMsgCode, viewAttAmt, viewItemAttPrices + settingPriceName + "@", oldTvd.getOrgPrice(),
                                viewAttSubtractQty + calQty + "@", false, 0, oldTvd.getService(), oldTvd.getMealCode(), oldTvd.getSgroup(), oldTvd.getScount(), oldTvd.getGpCount(), oldTvd.getItemPrn(), oldTvd.getCombId(), "", "", oldTvd.getCatt(), oldTvd.getAttMax(), oldTvd.getAttMin(), oldTvd.getCartId(), false, oldTvd.gettDate());
                        tvd.setHoldOn(oldTvd.getHoldOn());
                        tvd.setId(oldTvd.getId());
                        tableViewData.set(rowIndex, tvd);
                        foodTableView.getSelectionModel().select(rowIndex);
                        foodTableView.scrollTo(rowIndex);
                        //如果左邊頁面已經打開了口味修改頁面,則添加口味的時候直接刷新
                        if (currentRightPage.equals(OrderRightPartEnum.ATTMODIFY)) {
                            openAttModifyView(tvd);
                        }

                        //    initItemCheckAtt(foodTableView.getItems().get(foodTableView.getSelectionModel().getSelectedIndex()));

                    } else {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("att.sendItem.check"), resultMap, orderStage);

                    }
                }
                clickPosAttDto = null;
                clickActionDto = null;
                clickAttSettingDto = null;
            }
        }
    }

    /**
     * 设置每次添加的菜品数量
     *
     * @param number
     */
    private void numberClick(int number) {
        if (perAddItemCount == 1) {
            perAddItemCount = number;
        } else {
            perAddItemCount += number;
        }
        perAddItemCountLabel.getStyleClass().add("showBorder");
        perAddItemCountLabel.setAlignment(Pos.CENTER);
        perAddItemCountLabel.setText(String.valueOf(perAddItemCount));
    }

    /**
     * 初始化每次添加的菜品数量
     */
    private void initPerAddItemCount() {
        perAddItemCount = 1;
        perAddItemCountLabel.setAlignment(Pos.CENTER_RIGHT);
        perAddItemCountLabel.getStyleClass().add("hiddenBorder");
        perAddItemCountLabel.setText(Main.languageMap.get("tran.input"));
    }

    /**
     * 计算账单菜品总金额/添加菜品时重新计算菜品总数量
     */
    public void calculate() {
        try {
            //如果是已經結賬后又重新點或者又用折扣或者点了口味只要重新算价钱，這個時候要判斷tran表中的isSettled=FALSE,如果是等於則要把FALSE設置成"".
            if (null != posTran.getSettled() && posTran.getSettled().equals("FALSE") && !isUpdateOrder) {
                posTran.setSettled("");
                posTranService.updateById(posTran);

                Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
                posOrderWrapper.eq("OUTLET", Main.posOutlet);
                posOrderWrapper.eq("REF_NUM", posTran.getRefNum());
                posOrderWrapper.eq("SUB_REF", posTran.getSubRef());
                posOrderWrapper.eq("TYPE", posTran.getTranType());
                PosOrder posOrder = new PosOrder();
                posOrder.setChgAmt(BigDecimal.ZERO);
                posOrder.setServCost(BigDecimal.ZERO);
                posOrderService.update(posOrder, posOrderWrapper);
            }

            settleComponent.createPosItemDtoMap();
            settleComponent.getTableViewData(tableViewData);
            settleComponent.getOrderAmt();
            settleComponent.calculateDisTableView("other", posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum(), isUpdateOrder, DateUtil.DateToString(posTran.getInTime(), "HH:mm"));

            final BigDecimal[] sum = {new BigDecimal("0.00"), BigDecimal.ZERO};
            final int[] count = {0};
            if (AppUtils.isNotBlank(settleComponent.foodTableViewDatas)) {
                settleComponent.foodTableViewDatas.forEach(tableViewDto -> {
                    sum[0] = DecimalUtil.add(sum[0], tableViewDto != null && tableViewDto.getPosOrderDiscDto() != null && tableViewDto.getPosOrderDiscDto().getCostAmt() != null ? tableViewDto.getPosOrderDiscDto().getCostAmt() : BigDecimal.ZERO);
                    sum[1] = DecimalUtil.add(sum[1], new BigDecimal(tableViewDto.getAmt()));
                    count[0] += Integer.parseInt(tableViewDto.getQty());
                });
            } else {
                tableViewData.forEach(tableViewDto -> {
                    sum[0] = DecimalUtil.add(sum[0], new BigDecimal(tableViewDto.getAmt()));
                    count[0] += Integer.parseInt(tableViewDto.getQty());
                });
            }
            moneyInfoLabel.setText(String.valueOf(DecimalUtil.setScale(sum[0], 2, BigDecimal.ROUND_HALF_UP)));
            moneyAmtInfoLabel.setText(String.valueOf(DecimalUtil.setScale(sum[1], 2, BigDecimal.ROUND_HALF_UP)));
            itemCountLabel.setText(String.valueOf(count[0]));


            if (isCoupon) {
                moneyInfoLabel.getStyleClass().remove("whiteText");
                moneyInfoLabel.getStyleClass().add("redText");
            } else {
                moneyInfoLabel.getStyleClass().remove("redText");
                moneyInfoLabel.getStyleClass().add("whiteText");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 全选/取消全选
     */
    private void selectAllItemRows() {
        if (foodTableView.getSelectionModel().getSelectionMode() == SelectionMode.SINGLE) {
            foodTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            foodTableView.getSelectionModel().selectAll();
        } else {
            foodTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            foodTableView.getSelectionModel().clearSelection();
        }

    }

    /**
     * 多選或當選切換
     */
    private void multiSelectItemRows() {
        if (foodTableView.getSelectionModel().getSelectionMode() == SelectionMode.SINGLE) {
            foodTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            foodTableView.getSelectionModel().clearSelection();
        } else {
            foodTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            foodTableView.getSelectionModel().clearSelection();
        }

    }

    /**
     * 删除选中的点菜记录
     */
    public void deleteItem() {
        //是否加载指定菜品
        final Boolean[] isInitRightItem = {false};
        ObservableList<TableViewDto> selectedItems = foodTableView.getSelectionModel().getSelectedItems();
        AtomicBoolean hasPrinted = new AtomicBoolean(false);
        //删除单个菜品的时候
        if (selectedItems != null && selectedItems.size() == 1) {
            //如果是套餐组别的菜时
            if (AppUtils.isNotBlank(selectedItems.get(0).getMealCode()) && !selectedItems.get(0).isPrinter()) {
                int index = tableViewData.indexOf(selectedItems.get(0));
                //该组别已选且未列印
                if (AppUtils.isNotBlank(selectedItems.get(0).getItemCode())) {
                    List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(selectedItems.get(0).getMealCode());
                    posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                        if (posSetmealGroupDto.getSGroup().equals(selectedItems.get(0).getSgroup()) && posSetmealGroupDto.getSCount().equals(selectedItems.get(0).getScount()) && !selectedItems.get(0).getScount().equals(selectedItems.get(0).getGpCount())) {
                            String[] languages = new String[]{posSetmealGroupDto.getGpDesc1(), posSetmealGroupDto.getGpDesc2(), posSetmealGroupDto.getGpDesc3(), posSetmealGroupDto.getGpDesc4()};
                            String gpName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            TableViewDto t = new TableViewDto("" + perAddItemCount,
                                    "",
                                    Main.languageMap.get("notsure.item") + gpName, "", "", 0.00, 0.00, "",
                                    "", 0.00, "", 0.00,
                                    "", false, 0, selectedItems.get(0).getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(),
                                    selectedItems.get(0).getItemPrn(), comId, "", "", "", 0, 0, selectedItems.get(0).getCartId(), false, selectedItems.get(0).gettDate());
                            t.setId(selectedItems.get(0).getId());
                            tableViewData.set(index, t);
                            foodTableView.getSelectionModel().select(index);
                            foodTableView.scrollTo(index);
                            if (AppUtils.isNotBlank(t.getMealCode()) && !t.getScount().equals(t.getGpCount()) && !t.isPrinter()) {
                                List<PosItemDto> posItemList = new ArrayList<PosItemDto>();
                                posSetmealGroupDto.getSetmealDetailList().forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                                    PosItemDto posItem = new PosItemDto();
                                    posItem.setItemCode(posSetmealDetailDto.getItemCode());
                                    posItem.setDesc1(posSetmealDetailDto.getItemDesc1());
                                    posItem.setPrice(posSetmealDetailDto.getAddPrice() == null ? BigDecimal.ZERO : posSetmealDetailDto.getAddPrice());
                                    posItem.setOrgPrice(BigDecimal.ZERO);
                                    posItem.setDiscont(posSetmealDetailDto.getDiscont());
                                    posItemList.add(posItem);
                                });
                                ItemChooseController itemChooseController = (ItemChooseController) itemChooseView.getPresenter();
                                itemChooseController.initRightItems(1, 0, posItemList);
                                isInitRightItem[0] = true;
                            }
                        }
                    });
                }
            }
            //单选单个套餐时删除不了，必须整个套餐删除
            else if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(selectedItems.get(0).getItemCode())) && !selectedItems.get(0).isPrinter()) {
                return;
            }
            //未列印的单品直接删除
            else if (!selectedItems.get(0).isPrinter()) {
                tableViewData.remove(selectedItems.get(0));
            } else if (selectedItems.get(0).isPrinter() && AppUtils.isBlank(selectedItems.get(0).getMealCode())) {
                hasPrinted.set(true);
            }
        }
        //多选的情况
        else if (selectedItems != null && selectedItems.size() > 1) {
            List<TableViewDto> deleteTableViewDtoList = new ArrayList<TableViewDto>();
            selectedItems.forEach(tableViewDto -> {
                //单品
                if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isBlank(tableViewDto.getMealCode()) && AppUtils.isNotBlank(tableViewDto.getItemCode()) && AppUtils.isBlank(setmealGroupDtoObservableMap.get(tableViewDto.getItemCode()))) {
                    deleteTableViewDtoList.add(tableViewDto);
                }
                //主套餐
                else if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(tableViewDto.getItemCode()))) {
                    deleteTableViewDtoList.add(tableViewDto);
                    for (int i = tableViewData.indexOf(tableViewDto) + 1; i < tableViewData.size(); i++) {
                        if (AppUtils.isNotBlank(tableViewData.get(i).getMealCode()) && tableViewData.get(i).getMealCode().equals(tableViewDto.getItemCode())) {
                            deleteTableViewDtoList.add(tableViewData.get(i));
                        } else {
                            break;
                        }
                    }
                }
                //套餐组别
                else if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isNotBlank(tableViewDto.getMealCode()) && !deleteTableViewDtoList.contains(tableViewDto)) {
                    int index = tableViewData.indexOf(tableViewDto);
                    if (AppUtils.isNotBlank(tableViewDto.getItemCode())) {
                        List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(tableViewDto.getMealCode());
                        posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                            if (posSetmealGroupDto.getSGroup().equals(tableViewDto.getSgroup()) && posSetmealGroupDto.getSCount().equals(tableViewDto.getScount()) && !tableViewDto.getScount().equals(tableViewDto.getGpCount())) {
                                String[] languages = new String[]{posSetmealGroupDto.getGpDesc1(), posSetmealGroupDto.getGpDesc2(), posSetmealGroupDto.getGpDesc3(), posSetmealGroupDto.getGpDesc4()};
                                String gpName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                                TableViewDto t = new TableViewDto("" + perAddItemCount,
                                        "",
                                        Main.languageMap.get("notsure.item") + gpName, "", "", 0.00, 0.00, "",
                                        "", 0.00, "", 0.00,
                                        "", false, 0, tableViewDto.getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(), "", comId, "", "", "", 0, 0, tableViewDto.getCartId(), false, tableViewDto.gettDate());
                                t.setId(tableViewDto.getId());
                                tableViewData.set(index, t);
                                //deleteTableViewDtoList.add(tableViewDto);
                                //foodTableView.getSelectionModel().select(index);
                            }
                        });
                    }
                } else if (tableViewDto.isPrinter()) {
                    hasPrinted.set(true);
                }

            });

            //批量删除
            if (AppUtils.isNotBlank(deleteTableViewDtoList)) {
                deleteTableViewDtoList.forEach((TableViewDto tableViewDto) -> {
                    tableViewData.remove(tableViewDto);
                });
            }
        }
        if (hasPrinted.get()) {
            if (selectedItems.size() > 1) {
                CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                cancelItemController.initData(selectedItems, false);
                cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
            } else if (selectedItems.size() == 1) {
                /*取消的時候，不再驗證商品數量
                if (Integer.parseInt(selectedItems.get(0).getQty()) == 1) {
                    CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                    cancelItemController.initData(selectedItems.get(0), "1", false);
                    cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
                } else {
                    String result = ShowViewUtil.showNumbericKeyboard(orderStage, Main.languageMap.get("global.cancelnum"), selectedItems.get(0).getQty(), false);
                    if (AppUtils.isNotBlank(result) && !ResultEnum.NO.getValue().equals(result)) {
                        if (Integer.parseInt(selectedItems.get(0).getQty()) < Integer.parseInt(result) || Integer.parseInt(result) < 1) {
                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.illegalparameters"), resultMap, orderStage);
                        } else {
                            CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                            cancelItemController.initData(selectedItems.get(0), result, false);
                            cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
                        }
                    }
                }*/
                CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                cancelItemController.initData(selectedItems.get(0), selectedItems.get(0).getQty(), false);
                cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
            }

        }
//        if (!isInitRightItem[0]) {
//            ItemChooseController itemChooseController = (ItemChooseController) itemChooseView.getPresenter();
//            itemChooseController.initRight();
//        }
        /*calculate();*/
    }

    /**
     * 改单情况下删除菜品
     */

    public void deleteHaveSendItem() {
        //是否加载指定菜品
        final Boolean[] isInitRightItem = {false};
        ObservableList<TableViewDto> selectedItems = foodTableView.getSelectionModel().getSelectedItems();
        AtomicBoolean hasPrinted = new AtomicBoolean(false);
        //删除单个菜品的时候
        if (selectedItems != null && selectedItems.size() == 1) {
            //如果是套餐组别的菜时
            if (AppUtils.isNotBlank(selectedItems.get(0).getMealCode()) && !selectedItems.get(0).isPrinter()) {
                int index = tableViewData.indexOf(selectedItems.get(0));
                //该组别已选且未列印
                if (AppUtils.isNotBlank(selectedItems.get(0).getItemCode())) {
                    List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(selectedItems.get(0).getMealCode());
                    posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                        if (posSetmealGroupDto.getSGroup().equals(selectedItems.get(0).getSgroup()) && posSetmealGroupDto.getSCount().equals(selectedItems.get(0).getScount()) && !selectedItems.get(0).getScount().equals(selectedItems.get(0).getGpCount())) {
                            String[] languages = new String[]{posSetmealGroupDto.getGpDesc1(), posSetmealGroupDto.getGpDesc2(), posSetmealGroupDto.getGpDesc3(), posSetmealGroupDto.getGpDesc4()};
                            String gpName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            TableViewDto t = new TableViewDto("" + perAddItemCount,
                                    "",
                                    Main.languageMap.get("notsure.item") + gpName, "", "", 0.00, 0.00, "",
                                    "", 0.00, "", 0.00,
                                    "", false, 0, selectedItems.get(0).getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(),
                                    selectedItems.get(0).getItemPrn(), comId, "", "", "", 0, 0, selectedItems.get(0).getCartId(), false, selectedItems.get(0).gettDate());
                            t.setId(selectedItems.get(0).getId());
                            tableViewData.set(index, t);
                            foodTableView.getSelectionModel().select(index);
                            foodTableView.scrollTo(index);
                            if (AppUtils.isNotBlank(t.getMealCode()) && !t.getScount().equals(t.getGpCount()) && !t.isPrinter()) {
                                List<PosItemDto> posItemList = new ArrayList<PosItemDto>();
                                posSetmealGroupDto.getSetmealDetailList().forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                                    PosItemDto posItem = new PosItemDto();
                                    posItem.setItemCode(posSetmealDetailDto.getItemCode());
                                    posItem.setDesc1(posSetmealDetailDto.getItemDesc1());
                                    posItem.setPrice(posSetmealDetailDto.getAddPrice() == null ? new BigDecimal(0.00) : posSetmealDetailDto.getAddPrice());
                                    posItem.setDiscont(posSetmealDetailDto.getDiscont());
                                    posItemList.add(posItem);
                                });
                                ItemChooseController itemChooseController = (ItemChooseController) itemChooseView.getPresenter();
                                itemChooseController.initRightItems(1, 0, posItemList);
                                isInitRightItem[0] = true;
                            }
                        }
                    });
                }
            }
            //单选单个套餐时删除不了，必须整个套餐删除
            else if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(selectedItems.get(0).getItemCode())) && !selectedItems.get(0).isPrinter()) {
                return;
            }
            //未列印的单品直接删除
            else if (!selectedItems.get(0).isPrinter()) {
                tableViewData.remove(selectedItems.get(0));
            } else if (selectedItems.get(0).isPrinter() && AppUtils.isBlank(selectedItems.get(0).getMealCode())) {
                hasPrinted.set(true);
            }
        }
        //多选的情况
        else if (selectedItems != null && selectedItems.size() > 1) {
            List<TableViewDto> deleteTableViewDtoList = new ArrayList<TableViewDto>();
            selectedItems.forEach(tableViewDto -> {
                //单品
                if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isBlank(tableViewDto.getMealCode()) && AppUtils.isNotBlank(tableViewDto.getItemCode()) && AppUtils.isBlank(setmealGroupDtoObservableMap.get(tableViewDto.getItemCode()))) {
                    deleteTableViewDtoList.add(tableViewDto);
                }
                //主套餐
                else if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(tableViewDto.getItemCode()))) {
                    deleteTableViewDtoList.add(tableViewDto);
                    for (int i = tableViewData.indexOf(tableViewDto) + 1; i < tableViewData.size(); i++) {
                        if (AppUtils.isNotBlank(tableViewData.get(i).getMealCode()) && tableViewData.get(i).getMealCode().equals(tableViewDto.getItemCode())) {
                            deleteTableViewDtoList.add(tableViewData.get(i));
                        } else {
                            break;
                        }
                    }
                }
                //套餐组别
                else if (tableViewData.contains(tableViewDto) && !tableViewDto.isPrinter() && AppUtils.isNotBlank(tableViewDto.getMealCode()) && !deleteTableViewDtoList.contains(tableViewDto)) {
                    int index = tableViewData.indexOf(tableViewDto);
                    if (AppUtils.isNotBlank(tableViewDto.getItemCode())) {
                        List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(tableViewDto.getMealCode());
                        posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                            if (posSetmealGroupDto.getSGroup().equals(tableViewDto.getSgroup()) && posSetmealGroupDto.getSCount().equals(tableViewDto.getScount()) && !tableViewDto.getScount().equals(tableViewDto.getGpCount())) {
                                String[] languages = new String[]{posSetmealGroupDto.getGpDesc1(), posSetmealGroupDto.getGpDesc2(), posSetmealGroupDto.getGpDesc3(), posSetmealGroupDto.getGpDesc4()};
                                String gpName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                                TableViewDto t = new TableViewDto("" + perAddItemCount,
                                        "",
                                        Main.languageMap.get("notsure.item") + gpName, "", "", 0.00, 0.00, "",
                                        "", 0.00, "", 0.00,
                                        "", false, 0, tableViewDto.getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(), "", comId, "", "", "", 0, 0, tableViewDto.getCartId(), false, tableViewDto.gettDate());
                                t.setId(tableViewDto.getId());
                                tableViewData.set(index, t);
                            }
                        });
                    }
                } else if (tableViewDto.isPrinter()) {
                    hasPrinted.set(true);
                }

            });

            //批量删除
            if (AppUtils.isNotBlank(deleteTableViewDtoList)) {
                deleteTableViewDtoList.forEach((TableViewDto tableViewDto) -> {
                    tableViewData.remove(tableViewDto);
                });
            }
        }
        //删除已列印的菜品
        if (hasPrinted.get()) {
            if (selectedItems.size() > 1) {
                CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                cancelItemController.initData(selectedItems, true);
                cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
            } else if (selectedItems.size() == 1) {
                if (Integer.parseInt(selectedItems.get(0).getQty()) == 1) {
                    CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                    cancelItemController.initData(selectedItems.get(0), "1", true);
                    cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
                } else {
                    String result = ShowViewUtil.showNumbericKeyboard(orderStage, Main.languageMap.get("global.cancelnum"), selectedItems.get(0).getQty(), false);
                    if (!ResultEnum.NO.getValue().equals(result)) {
                        if (Integer.parseInt(selectedItems.get(0).getQty()) < Integer.parseInt(result) || Integer.parseInt(result) < 1) {
                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.illegalparameters"), resultMap, orderStage);
                        } else {
                            CancelItemController cancelItemController = (CancelItemController) cancelItemView.getPresenter();
                            cancelItemController.initData(selectedItems.get(0), result, true);
                            cancelItemView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
                        }
                    }
                }
            }

        }


    }


    /**
     * 送單方法
     * 1、先判斷foodTableView中有沒有記錄如果有記錄則執行送單流程
     * 2、判斷臺號是否存在，存在則繼續送單
     */
    public void sendOrders(boolean isSendOrderToKitchen,boolean canClose) {
        boolean canSend = true;
        try {
            if (AppUtils.isNotBlank(tableViewData)) {
                List<SendOrderDto> sendOrderList = new LinkedList<>();
                if (AppUtils.isNotBlank(posTran) && AppUtils.isNotBlank(posTran.getTableNum())) {
                    sendOrder.setDisable(true);
                    for (int i = 0; i < tableViewData.size(); i++) {
                        TableViewDto tvd = tableViewData.get(i);
                        TableViewDto settleTvd = settleComponent.foodTableViewDatas.get(i);
                        //因為用券或者有折扣或者分單等操作會重算所有菜品，這個時候需要把issendorder改成true。如果是已經送單的記錄又用了券，這個時候也要改成true,並且需用再送單，所以這個時候不再判斷isSendOrder
                        //if (tvd.isSendOrder()) { // 未送單
                        String[] attCodes = null;
                        String[] attNames = null;
                        String[] attGroups = null;
                        String[] actionCodes = null;
                        String hasAttCodes = "";
                        if (AppUtils.isNotBlank(tvd.getItemAttCode())) {
                            int attIdx = 0;
                            String[] _attCodes = tvd.getItemAttCode().split("@");
                            attNames = tvd.getItemAtt().split("/");
                            attCodes = new String[_attCodes.length];
                            attGroups = new String[_attCodes.length];
                            actionCodes = new String[_attCodes.length];
                            for (String attCode : _attCodes) {
                                if (AppUtils.isNotBlank(attCode)) {
                                    String[] codes = attCode.split(":");
                                    attCodes[attIdx] = codes[1];
                                    hasAttCodes += "," + codes[1];
                                    attGroups[attIdx] = codes[0];
                                    actionCodes[attIdx] = codes.length > 2 ? codes[2] : null;
                                    attIdx++;
                                }
                            }
                        }

                        // 檢查是否有必選口味還沒選擇
                        if (mustAttMap.get(tvd.getItemCode()) != null) {
                            int attMin = mustAttMap.get(tvd.getItemCode()).getAttMin(); // 必選口味數量
                            String selectedAttCodes = AppUtils.isNotBlank(hasAttCodes) ? "'" + hasAttCodes.substring(1).replaceAll(",", "','") + "'" : "'0'";
                            List<PosItemMustAttDto> selectedMustAttList = posAttService.checkMustAttByItemCode(tvd.getItemCode().trim(), selectedAttCodes);
                            if (AppUtils.isBlank(selectedMustAttList) || selectedMustAttList.size() < attMin) {
                                // 選中記錄
                                foodTableView.getSelectionModel().select(tvd);
                                // 顯示必選口味
                                checkMustAtt(mustAttMap.get(tvd.getItemCode()), 0);
                                // 設置不提交
                                canSend = false;
                                break;
                            } else {
                                canSend = true;
                            }
                        }

                        //如果是改单，选择套餐时，套餐下的组别必须全部选好才能送单
                        if (isUpdateOrder && AppUtils.isBlank(tvd.getItemCode()) && AppUtils.isNotBlank(tvd.getMealCode())) {
                            canSend = false;
                            break;
                        }

                        SendOrderDto dto = new SendOrderDto();
                        if (!tvd.isPrinter()) {
                            dto.setPrinter(true);
                        } else {
                            dto.setPrinter(false);
                        }
                        dto.setIopen(tvd.getIopen());
                        dto.setHoldOn(tvd.getHoldOn());
                        dto.setCartId(tvd.getCartId());
                        dto.setId(tvd.getId());
                        dto.setItemCode(tvd.getItemCode());
                        dto.setItemName(tvd.getItemName());
                        dto.setService(tvd.getService());
                        dto.setQty(Integer.parseInt(tvd.getQty()));
                        dto.setAmt(new BigDecimal(tvd.getAmt()));
                        dto.setItemKicMsg(String.valueOf(tvd.getHoldOn()));
                        dto.setOrgPrice(tvd.getOrgPrice() + "");
                        dto.setAttAmt(new BigDecimal(tvd.getAttAmt()));
                        dto.setPrice(new BigDecimal(String.valueOf(tvd.getPrice())));
                        dto.setPrinterCode(tvd.getItemPrn());
                        dto.setSetmealItemIdx(tvd.getSetmealItemIdx());
                        dto.setItemIdx(String.valueOf(tvd.getItemIdx()));
                        dto.setTDate(tvd.gettDate());
                        //判断该记录是否为套餐
                        if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(dto.getItemCode()))) {
                            dto.setIsMeal(true);
                        } else {
                            dto.setIsMeal(false);
                        }
                        //設定折扣金額

                        dto.setItemDisc(settleTvd != null && settleTvd.getPosOrderDiscDto() != null && settleTvd.getPosOrderDiscDto().getItemDisc() != null ? settleTvd.getPosOrderDiscDto().getItemDisc() : BigDecimal.ZERO);
                        dto.setCatDisc(settleTvd != null && settleTvd.getPosOrderDiscDto() != null && settleTvd.getPosOrderDiscDto().getCatDisc() != null ? settleTvd.getPosOrderDiscDto().getCatDisc() : BigDecimal.ZERO);
                        if (AppUtils.isNotBlank(tvd.getMealCode())) {
                            if (AppUtils.isBlank(tvd.getItemCode())) {
                                if (isUpdateOrder) {
                                    Map<String, String> resultMap = new LinkedHashMap<>();
                                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("bill.haveNotselectedItem"), resultMap, null);
                                    return;
                                } else {
                                    dto.setItemCode("0000");
                                    dto.setSgroup(tvd.getSgroup());
                                }
                            }
                            dto.setMealCode(tvd.getMealCode());
                        }
                        dto.setAttPrices(AppUtils.isNotBlank(tvd.getAttPrices()) ? tvd.getAttPrices().split("@") : new String[]{"0.00"});
                        dto.setAttCode(attCodes);
                        dto.setAttGroup(attGroups);
                        dto.setActionCode(actionCodes);
                        dto.setAttName(attNames);
                        //服務費
                        dto.setServAmt(settleTvd != null && settleTvd.getPosOrderDiscDto() != null && settleTvd.getPosOrderDiscDto().getServCost() != null ? settleTvd.getPosOrderDiscDto().getServCost() : BigDecimal.ZERO);
                        sendOrderList.add(dto);
                        //}
                    }
                    if (canSend && AppUtils.isNotBlank(sendOrderList)) {
                        boolean isPrinter = true;
                        if (!isSendOrderToKitchen) {
                            isPrinter = false;
                        }

                        if (!isUpdateOrder) {
                            //正常送单
                            sendOrderController.sendOrders(posTran, sendOrderList, isPrinter, PrinterTypeEnums.N, null, null, null, null, false);
                        } else {
                            isPrinter = false;
                            //改单时 如果送单了 把postran的settle值改为false
                            boolean state = sendOrderController.sendOrders(posTran, sendOrderList, isPrinter, PrinterTypeEnums.N, null, null, null, null, true);
                            //改单时，送单成功后，修改pos_tran_his表的settled为false,表示该单做过了修改
                            if (state) {
                                if (posTran instanceof PosTranHis) {
                                    PosTranHis posTranHis = (PosTranHis) posTran;
                                    posTranHis.setSettled("");
                                    posTranHisService.updateById(posTranHis);
                                    this.posTran.setSettled("");
                                    getOrderHisList();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //这里怀疑迟早还是要换回来的...
//            if(!isUpdateOrder){
//                getOrderList();
//            }
//            else{
//                getOrderHisList();
//            }
            sendOrder.setDisable(false);
            if (canSend) {
                //如果是改单，不能关闭点菜界面，如果正常点菜才关闭点菜界面。
                if (!isUpdateOrder) {
                    if(canClose){
                        //清除桌台操作记录
                        deletePosTableAction(posTableDto.getRoomNum(), stationId);
                        MainController mainController = (MainController) mainView.getPresenter();
                        mainController.iniData();
                        Main.showInitialView(mainView.getClass());
                    }
                }
            }
        }
    }

    /**
     * 點擊整行的方法執行的操作。
     */
    class TableRowControl extends TableRow<TableViewDto> {

        public TableRowControl() {
            super();
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY)
                            && event.getClickCount() == 1
                            && TableRowControl.this.getIndex() < foodTableView.getItems().size()) {
                        if (foodTableView.getSelectionModel().getSelectedIndex() >= 0) {
                            //點擊行的時候判斷菜品選擇模塊有沒顯示
                            if (!currentRightPage.equals(OrderRightPartEnum.ITEMCHOOSE)) {
                                openItemChooseView();
                            }

                            ObservableList<TableViewDto> selectedItems = foodTableView.getSelectionModel().getSelectedItems();
                            if (selectedItems != null && selectedItems.size() > 0) {
                                TableViewDto tableViewDto = selectedItems.get(0);
                                ItemChooseController itemChooseController = (ItemChooseController) itemChooseView.getPresenter();
                                if (AppUtils.isNotBlank(tableViewDto.getMealCode()) && !tableViewDto.getScount().equals(tableViewDto.getGpCount()) && !tableViewDto.isPrinter()) {
                                    List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(tableViewDto.getMealCode());
                                    posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                                        if (posSetmealGroupDto.getSCount().equals(tableViewDto.getScount()) && posSetmealGroupDto.getGpCount().equals(tableViewDto.getGpCount()) && posSetmealGroupDto.getSGroup().equals(tableViewDto.getSgroup())) {
                                            List<PosItemDto> posItemList = new ArrayList<PosItemDto>();
                                            posSetmealGroupDto.getSetmealDetailList().forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                                                PosItemDto posItem = new PosItemDto();
                                                posItem.setItemCode(posSetmealDetailDto.getItemCode());
                                                posItem.setDesc1(posSetmealDetailDto.getItemDesc1());
                                                posItem.setPrice(posSetmealDetailDto.getAddPrice() == null ? BigDecimal.ZERO : posSetmealDetailDto.getAddPrice());
                                                posItem.setOrgPrice(BigDecimal.ZERO);
                                                posItem.setDiscont(posSetmealDetailDto.getDiscont());
                                                posItemList.add(posItem);
                                            });

                                            itemChooseController.initRightItems(1, 0, posItemList);
                                        }

                                    });
                                }
                                //必选组别菜品
//                                else if (AppUtils.isNotBlank(tableViewDto.getMealCode()) && tableViewDto.getScount().equals(tableViewDto.getGpCount()) && !tableViewDto.isPrinter()) {
//                                    itemChooseController.initRight();
//                                }
                                //单品
//                                else {
//                                    foodTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
////                                    itemChooseController.initRight();
//                                }
                                //主套餐
                                if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(tableViewDto.getItemCode()))) {
                                    for (int i = foodTableView.getSelectionModel().getSelectedIndex() + 1; i < foodTableView.getItems().size(); i++) {
                                        if (AppUtils.isNotBlank(foodTableView.getItems().get(i).getMealCode()) && tableViewDto.getItemCode().equals(foodTableView.getItems().get(i).getMealCode())) {
                                            foodTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                                            foodTableView.getSelectionModel().select(foodTableView.getItems().get(i));
                                            foodTableView.scrollTo(foodTableView.getItems().get(i));
                                        } else {
                                            break;
                                        }
                                    }
//                                    itemChooseController.initRight();

                                }
                                //檢查是否必選口味
                                PosItemDto posItem = new PosItemDto();
                                posItem.setItemCode(tableViewDto.getItemCode());
                                posItem.setDesc1(tableViewDto.getItemName());
                                posItem.setPrice(new BigDecimal(tableViewDto.getPrice()));
                                posItem.setOrgPrice(new BigDecimal("0.00"));
                                posItem.setAttMax(tableViewDto.getAttMax());
                                posItem.setAttMin(tableViewDto.getAttMin());
                                posItem.setCatt(tableViewDto.getCatt());
                                checkMustAtt(posItem, 0);
                            }
                        }
                    }
                }
            });
        }

    }


    public void addListener() {
        foodTableView.getItems().addListener(new ListChangeListener<TableViewDto>() {
            @Override
            public void onChanged(Change<? extends TableViewDto> c) {
                if (c.next()) {
                    AtomicBoolean checkBestPriceFlag = new AtomicBoolean(false);
                    c.getRemoved().forEach(remove -> {
                        if (null != remove.getCombId() && !remove.getCombId().equals(comId)) {
                            checkBestPriceFlag.set(true);
                        }
                    });
                    c.getAddedSubList().forEach(add -> {
                        if (null != add.getCombId() && !add.getCombId().equals(comId)) {
                            checkBestPriceFlag.set(true);
                        }
                    });
                    if (checkBestPriceFlag.get()) {
                        Platform.runLater(() -> {
                            mathBestItemPrice();
                        });
                    }
                    Platform.runLater(() -> {
                        calculate();
                    });
                    initPerAddItemCount();
                }
            }
        });


    }


    private void mathBestItemPrice() {
        //每次重置已匹配標記
        tableViewData.forEach(tableViewDto -> {
            tableViewDto.setFlag(false);
            //add by liang 20180504,如果组合后的商品加了口味，需要加口味金额
            tableViewDto.setAmt(tableViewDto.getPrice() * Integer.parseInt(tableViewDto.getQty()) + tableViewDto.getAttAmt());
        });
        Map<Integer, Boolean> isMath = new HashMap<>();
        if (tableViewData.size() > 1) {
            for (int i = 0; i < tableViewData.size(); i++) {
                //如果是一條記錄裡面有多個數量
                for (int qtyLoop = 0; qtyLoop < Integer.parseInt(tableViewData.get(i).getQty()); qtyLoop++) {
                    List<MathBestItemPrice> tvList = new ArrayList<>();

                    for (int j = 0; j < tableViewData.size(); j++) {
                        if (!tableViewData.get(j).isFlag()) {
                            //2.匹配组别内的每个元素,组合成一个KEY然后去匹配COMB(组合)里面的,如果有值则证明存在组合.
                            //3.然后将所有匹配到的组合放进去一个LIST里面去求GAPAMT的最大值,
                            String comKey = tableViewData.get(i).getCombId() + "&" + tableViewData.get(j).getCombId();
                            PosComb pc = posCombObservableMap.get(comKey);

                            if (AppUtils.isNotBlank(pc)) {
                                MathBestItemPrice tv = new MathBestItemPrice();
                                tv.setItemCode(tableViewData.get(j).getItemCode());
                                tv.setAmt(tableViewData.get(j).getAmt());
                                tv.setCombAmt(pc.getVItemPrice().doubleValue());
                                tv.setGapAmt(tableViewData.get(j).getPrice() - tv.getCombAmt());
                                tv.setIndex(j);
                                tvList.add(tv);
                            }

                        }
                    }
                    //4.然后具有最大GAPAMT的对象则为最优的匹配结果,然后更新LISTVIEW里面的值
                    if (tvList.size() > 0) {
                        MathBestItemPrice bestMatch = tvList.stream().max(Comparator.comparing(MathBestItemPrice::getGapAmt)).get();
                        //4.1 如果匹配到的數據大於1 ,則拆分一條數據出來,組合的那條記錄數量用戶是1
                        TableViewDto tvd = tableViewData.get(bestMatch.getIndex());
                        int oldDataQty = Integer.parseInt(tvd.getQty());
                        if (oldDataQty > 1) {
                            //克隆出來原來的對象
                            int newDataQty = oldDataQty - 1;
                            double newDataAmt = newDataQty * tvd.getPrice() + tvd.getAttAmt() / oldDataQty * newDataQty;
                            TableViewDto newTableData = new TableViewDto(String.valueOf(newDataQty), tvd.getItemCode(), tvd.getItemName(), tvd.getItemAtt(), tvd.getItemKicMsg(), tvd.getPrice(),
                                    newDataAmt, tvd.getItemAttCode(), tvd.getKicMsgCode(),
                                    tvd.getAttAmt() / oldDataQty * newDataQty, tvd.getAttPrices(),
                                    tvd.getOrgPrice(), tvd.getAttSubtractQty(),
                                    tvd.isPrinter(), 0, tvd.getService(), "", "", "", "", tvd.getItemPrn(), tvd.getCombId(), "", "", tvd.getCatt(), tvd.getAttMax(), tvd.getAttMin(), tvd.getCartId(), false, tvd.gettDate());

                            //原來的記錄
                            tableViewData.get(bestMatch.getIndex()).setQty("1");
                            tableViewData.get(bestMatch.getIndex()).setAttAmt(tvd.getAttAmt() / oldDataQty);
                            //如果已經送單，組合的這個副商品不需要再插入信息
                            if (!tvd.isSendOrder()) {
                                tableViewData.get(bestMatch.getIndex()).setItemAttCode("");
                                tableViewData.get(bestMatch.getIndex()).setSendOrder(true);
                            }
                            //tableViewData.get(bestMatch.getIndex()).setAmt(tableViewData.get(bestMatch.getIndex()).getPrice());//
                            tableViewData.get(bestMatch.getIndex()).setAmt(tableViewData.get(bestMatch.getIndex()).getAmt() / Double.parseDouble(tableViewData.get(bestMatch.getIndex()).getQty()));

                            tableViewData.add(newTableData);

                        }
                        //4.2更新已组合标记,这样下面的组合就不会匹配到这个编号
                        tableViewData.get(bestMatch.getIndex()).setFlag(true);
                        //4.3更新B边的售价为组合价
                        //tableViewData.get(bestMatch.getIndex()).setAmt(bestMatch.getCombAmt());
                        //add by liang 20180504,如果组合后的商品加了口味，需要加口味金额
                        tableViewData.get(bestMatch.getIndex()).setAmt(bestMatch.getCombAmt() + tableViewData.get(bestMatch.getIndex()).getAttAmt());
                        System.out.println("A:" + tableViewData.get(i).getItemCode() + "金额:" + tableViewData.get(i).getAmt() + "+B:" + bestMatch.getItemCode() +
                                ",组合价" + bestMatch.getCombAmt() + "=组合金额" + (tableViewData.get(i).getAmt() + bestMatch.getCombAmt()));

                    } else if (!tableViewData.get(i).isFlag()) {
                        System.out.println("未组合编号:" + tableViewData.get(i).getItemCode() + ",金额:" + tableViewData.get(i).getAmt() + ",金额:" + tableViewData.get(i).getPrice());
                        //4.3如果未找到匹配的組合,則還原金額(含上一次已經匹配,但本次被擠走的編號)
                        //add by liang 20180504,如果组合后的商品加了口味，需要加口味金额
                        tableViewData.get(i).setAmt(tableViewData.get(i).getPrice() * Integer.parseInt(tableViewData.get(i).getQty()) + tableViewData.get(i).getAttAmt());
                    }

                }

            }
        }

    }

    /**
     * 獲得已點菜品列表
     */
    public void getOrderList() {
        List<OrderListDto> orderList = posOrderService.getOrderList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), TranTypeEnum.N.getValue());
        tableViewData.removeAll(tableViewData);
        //记录套餐某组别未选菜的位置
        final int[] mealIndex = {-1};
        orderList.forEach(posOrder -> {
            try {
                String itemName = LanguageEnum.getLanguage(new String[]{posOrder.getDesc1(), posOrder.getDesc2(), posOrder.getDesc3(), posOrder.getDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                String attName = LanguageEnum.getLanguage(new String[]{posOrder.getAttDesc1(), posOrder.getAttDesc2(), posOrder.getAttDesc3(), posOrder.getAttDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                attName = AppUtils.isNotBlank(attName) ? attName : "";

                Integer qty = posOrder.getQty() + posOrder.getCancel();
                double amt = DecimalUtil.bigDecimalByPloy(posOrder.getAmt().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(qty))).doubleValue();
                TableViewDto dto = null;
                //如果该记录为套餐时记录其位置
                if (AppUtils.isNotBlank(posOrder.getSetMeal()) && Integer.parseInt(posOrder.getSetMeal()) > 0) {
                    int index = orderList.indexOf(posOrder);
                    if (index >= 0) {
                        mealIndex[0] = index;
                    } else {
                        mealIndex[0] = -1;
                    }

                }
                if ("0000".equals(posOrder.getItemCode())) {
                    dto = new TableViewDto(posOrder.getQty().toString(), "", itemName, attName, "",
                            0.00, 0.00, "", "", 0.00, "0.00",
                            0.00, "0", false, posOrder.getItemIdx(), posOrder.getService(), "", "", "", "", "", comId, "", "", "", 0, 0, posOrder.getCartId(), false, null);
                    dto.setMealCode(posOrder.getSetMealCode());
                    dto.setSgroup(posOrder.getSgroup());
                    dto.setSetmealItemIdx(posOrder.getSepChar());
                    if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(posOrder.getSetMealCode()))) {
                        List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(posOrder.getSetMealCode());
                        TableViewDto finalDto = dto;
                        posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                            if (posSetmealGroupDto.getSGroup().equals(finalDto.getSgroup())) {
                                finalDto.setScount(posSetmealGroupDto.getSCount());
                                finalDto.setGpCount(posSetmealGroupDto.getGpCount());
                                finalDto.setSgroup(posSetmealGroupDto.getSGroup());
                                finalDto.setItemName(Main.languageMap.get("notsure.item") + posSetmealGroupDto.getGpDesc1());
                                finalDto.setSendOrder(false);
                                finalDto.setIsHoldOn(true);
                            }
                        });

                    }
                } else {
                    dto = new TableViewDto(String.valueOf(qty), posOrder.getItemCode(), itemName, attName, "",
                            posOrder.getUnitPrice().doubleValue(), amt, posOrder.getAttCode(), "", posOrder.getChangeAmt().doubleValue(), posOrder.getSettingPrice(),
                            Double.valueOf(posOrder.getKconfirm()), posOrder.getSubtractQty(), true, posOrder.getItemIdx(), posOrder.getService(), "", "", "", "", posOrder.getItemPrn(), posOrder.getCombId(), posOrder.getOrderTime(), posOrder.getStaffName(), "", 0, 0, posOrder.getCartId(), false, posOrder.getTDate());

                    dto.setSendOrder(false);
                    dto.setSetmealItemIdx(posOrder.getSepChar());
                    if (AppUtils.isNotBlank(posOrder.getSetMeal()) && "-1".equals(posOrder.getSetMeal())) {
                        dto.setMealCode(posOrder.getSetMealCode());
                    }
                }
                dto.setHoldOn(Integer.parseInt(posOrder.getKicMess()));
                dto.setUrgeCount(posOrder.getUrgeCount() == 0 ? null : String.valueOf(posOrder.getUrgeCount()));
                dto.setId(posOrder.getId());
                System.out.println(dto);
                tableViewData.add(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 獲得已點菜品列表
     */
    public void getOrderHisList() {
        List<OrderListDto> orderList = posOrderHisService.getOrderHisList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), TranTypeEnum.N.getValue());
        tableViewData.removeAll(tableViewData);
        //记录套餐某组别未选菜的位置
        final int[] mealIndex = {-1};
        orderList.forEach(posOrder -> {
            try {
                String itemName = LanguageEnum.getLanguage(new String[]{posOrder.getDesc1(), posOrder.getDesc2(), posOrder.getDesc3(), posOrder.getDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                String attName = LanguageEnum.getLanguage(new String[]{posOrder.getAttDesc1(), posOrder.getAttDesc2(), posOrder.getAttDesc3(), posOrder.getAttDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                attName = AppUtils.isNotBlank(attName) ? attName : "";
                Integer qty = posOrder.getQty() + posOrder.getCancel();
                double amt = DecimalUtil.bigDecimalByPloy(posOrder.getAmt().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(qty))).doubleValue();
                TableViewDto dto = null;
                //如果该记录为套餐时记录其位置
                if (AppUtils.isNotBlank(posOrder.getSetMeal()) && Integer.parseInt(posOrder.getSetMeal()) > 0) {
                    int index = orderList.indexOf(posOrder);
                    if (index >= 0) {
                        mealIndex[0] = index;
                    } else {
                        mealIndex[0] = -1;
                    }

                }
                if ("0000".equals(posOrder.getItemCode())) {
                    dto = new TableViewDto(posOrder.getQty().toString(), "", itemName, attName, "",
                            0.00, 0.00, "", "", 0.00, "0.00",
                            0.00, "0", false, posOrder.getItemIdx(), posOrder.getService(), "", "", "", "", "", comId, "", "", "", 0, 0, posOrder.getCartId(), false, posOrder.getTDate());
                    dto.setMealCode(posOrder.getSetMealCode());
                    dto.setSgroup(posOrder.getSgroup());
                    dto.setSetmealItemIdx(posOrder.getSepChar());
                    if (AppUtils.isNotBlank(setmealGroupDtoObservableMap.get(posOrder.getSetMealCode()))) {
                        List<PosSetmealGroupDto> posSetmealGroupDtoList = setmealGroupDtoObservableMap.get(posOrder.getSetMealCode());
                        TableViewDto finalDto = dto;
                        posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                            if (posSetmealGroupDto.getSGroup().equals(finalDto.getSgroup())) {
                                finalDto.setScount(posSetmealGroupDto.getSCount());
                                finalDto.setGpCount(posSetmealGroupDto.getGpCount());
                                finalDto.setSgroup(posSetmealGroupDto.getSGroup());
                                finalDto.setItemName(Main.languageMap.get("notsure.item") + posSetmealGroupDto.getGpDesc1());
                                finalDto.setSendOrder(false);
                            }
                        });

                    }
                } else {
                    dto = new TableViewDto(String.valueOf(qty), posOrder.getItemCode(), itemName, attName, posOrder.getKicMess(),
                            posOrder.getUnitPrice().doubleValue(), amt, posOrder.getAttCode(), "", posOrder.getChangeAmt().doubleValue(), posOrder.getSettingPrice(),
                            Double.valueOf(posOrder.getKconfirm()), posOrder.getSubtractQty(), true, posOrder.getItemIdx(), posOrder.getService(), "", "", "", "", posOrder.getItemPrn(), posOrder.getCombId(), posOrder.getOrderTime(), posOrder.getStaffName(), "", 0, 0, posOrder.getCartId(), false, posOrder.getTDate());
                    dto.setSendOrder(false);
                    dto.setSetmealItemIdx(posOrder.getSepChar());
                    if (AppUtils.isNotBlank(posOrder.getSetMeal()) && "-1".equals(posOrder.getSetMeal())) {
                        dto.setMealCode(posOrder.getSetMealCode());
                    }
                }
                dto.setHoldOn(Integer.parseInt(posOrder.getKicMess()));
                dto.setId(posOrder.getId());
                dto.setUrgeCount(posOrder.getUrgeCount() == 0 ? null : String.valueOf(posOrder.getUrgeCount()));
                System.out.println(dto);
                tableViewData.add(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 向右邊填充口味修改
     */
    public void openAttModifyView(TableViewDto tvd) {

        currentRightPage = OrderRightPartEnum.ATTMODIFY;
        AttModifyController attModifyController = (AttModifyController) attModifyView.getPresenter();
        attModifyController.initData(tvd);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(attModifyView.getView());
    }

    /**
     * 向右邊填充菜品選擇
     */
    public void openItemChooseView() {

        currentRightPage = OrderRightPartEnum.ITEMCHOOSE;
        ItemChooseController itemChooseController = (ItemChooseController) itemChooseView.getPresenter();
        itemChooseController.initRight();
        rightPane.getChildren().clear();
        rightPane.getChildren().add(itemChooseView.getView());

    }

    /**
     * 向右邊填充數字鍵盤
     */
    public void openSearchKeyBoradView(TableViewDto tvd) {
        currentRightPage = OrderRightPartEnum.KEYBORAD;
        SearchKeyBoardController searchKeyBoardController = (SearchKeyBoardController) searchKeyBoardView.getPresenter();
        searchKeyBoardController.initData(tvd);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(searchKeyBoardView.getView());

    }

    /**
     * 向右邊填充數字鍵盤
     */
    public void openCouponView(LogTypeEnum discTypeEnum) {
//        currentRightPage = OrderRightPartEnum.COUPON;
        CouponController couponController = (CouponController) couponView.getPresenter();
        couponController.initDataFoodTableView(posTran, isUpdateOrder, discTypeEnum,initDataType);
//        rightPane.getChildren().clear();
//        rightPane.getChildren().add(couponView.getView());
        couponView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);


    }


    /**
     * 去除某桌台的操作记录，便于其他账号能够操作该台
     */
    public void deletePosTableAction(String tableNum, String stationId) {
        Wrapper<PosTableAction> posTableActionWrapper = new EntityWrapper<>();
        if (AppUtils.isNotBlank(tableNum)) {
            posTableActionWrapper.eq("TABLE_NUM", tableNum);
        }
        posTableActionWrapper.eq("STATION_ID", stationId);
        posTableActionService.delete(posTableActionWrapper);
    }


    public void sendNettyMessageByHoldOn() {
        //最早叫起时间
        final Date[] earliestDate = {null};
        final String[] dateStr = {""};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (tableViewData != null && tableViewData.size() > 0) {
            tableViewData.forEach((TableViewDto tableViewDto) -> {
                if (HoldOnEnum.HOLDON.getValue() == tableViewDto.getHoldOn()) {
                    if (AppUtils.isNotBlank(tableViewDto.gettDate())) {
                        Date tDate = null;
                        try {
                            tDate = simpleDateFormat.parse(tableViewDto.gettDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (AppUtils.isNotBlank(earliestDate[0])) {
                            if (tDate.before(earliestDate[0])) {
                                earliestDate[0] = tDate;
                                dateStr[0] = tableViewDto.gettDate();
                            }
                        } else {
                            earliestDate[0] = tDate;
                            dateStr[0] = tableViewDto.gettDate();
                        }
                    }
                }
            });
            //存在最早叫起时间后，发送netty消息去通知message更新叫起的桌台map
            if (AppUtils.isNotBlank(earliestDate[0])) {
                Map<String, String> tableMap = new HashedMap();
                tableMap.put(posTableDto.getRoomNum(), dateStr[0]);
                nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON, tableMap);
            } else {
                Map<String, String> tableMap = new HashedMap();
                tableMap.put(posTableDto.getRoomNum(), "");
                nettyComponent.sendMessage(NettyMessageTypeEnum.IMMEDIATELY, tableMap);
            }
        }
        //如果不存在记录就发即起消息，避免message的叫起map中存在该台号
        else {
            Map<String, String> tableMap = new HashedMap();
            tableMap.put(posTableDto.getRoomNum(), "");
            nettyComponent.sendMessage(NettyMessageTypeEnum.IMMEDIATELY, tableMap);
        }
    }

    /**
     * 渲染外賣顯示
     * @param posOrderAddress
     */
    public void initAddress(PosOrderAddress posOrderAddress) {

        if (AppUtils.isNotBlank(posOrderAddress)) {
            StringBuffer addressInfo = new StringBuffer();
            addressInfo.append("地址:").append(posOrderAddress.getAddress()).append(posOrderAddress.getPhase()).append("期").append(posOrderAddress.getTower()).append("座").append(posOrderAddress.getFloor())
                    .append("樓").append(posOrderAddress.getRoom()).append("室\n").append("姓名:").append(posOrderAddress.getLinkMan()).append("  電話:").append(posOrderAddress.getTelephone()).append("  備註").append(posOrderAddress.getRemark());
            addressInfoPane.setPrefSize(leftFlowPane.getPrefWidth(), addressInfoPaneHeight);
            tableViewHeightProp.set(tableViewHeight - addressInfoPaneHeight);
            addressInfoLable.setText(addressInfo.toString());
            FlowPane.setMargin(addressInfoLable, new Insets(0, 0, 0, 10));
//            addressInfoLable.setPadding(new Insets(0, 0, 0, 10));
            addressInfoLable.setVisible(true);
            addressInfoLable.setOnMouseClicked(event -> {
                AddressController addressController = (AddressController) addressView.getPresenter();
                addressController.initData(posTran);
                addressView.showViewAndWait(parentFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
            });
            addressInfoPane.setVisible(true);

        } else {
            addressInfoPane.setPrefSize(0, 0);
            addressInfoPane.setVisible(false);
            addressInfoLable.setVisible(false);
            tableViewHeightProp.set(tableViewHeight);
        }
    }

    /**
     * 檢查是否有外賣
     */
    public void checkAddress() {

        Wrapper<PosOrderAddress> posOrderAddressWrapper = new EntityWrapper<>();
        posOrderAddressWrapper.eq("REF_NUM", posTran.getRefNum());
        posOrderAddressWrapper.eq("SUB_REF", posTran.getSubRef());
        posOrderAddressWrapper.eq("IN_DATE", posTran.getInDate());
        posOrderAddressWrapper.eq("OUTLET",posTran.getOutlet());
        PosOrderAddress posOrderAddress = posOrderAddressService.selectOne(posOrderAddressWrapper);
        initAddress(posOrderAddress);
    }


    public void refreshItems(){
        Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
        memPeriodWrapper.eq("ISVALID", "Y");
        memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
        MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);
        List<TopButtonDto> topButtonDtoList;
        topButtonDtoList = topButtonService.getTopButtonList(Main.posOutlet, DateUtil.getWeek(DateUtil.getNowTime()), Main.posPeriodMap.get("price"), AppUtils.isNotBlank(memPeriod) ? memPeriod.getCode() : "ALL");
        topButtonDtos = FXCollections.observableArrayList(topButtonDtoList);
    }
}



