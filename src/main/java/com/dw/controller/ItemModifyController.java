package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.CacheManagerComponent;
import com.dw.component.NettyComponent;
import com.dw.dto.PosItemDto;
import com.dw.dto.TableViewDto;
import com.dw.dto.TopButtonDto;
import com.dw.entity.MemPeriod;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwImageView;
import com.dw.extended.DwLabel;
import com.dw.service.MemPeriodService;
import com.dw.service.PosItemService;
import com.dw.service.TopButtonService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

/**
 * Created by wen.jing on 2018/5/16
 */
@Setter
@Getter
@FXMLController
public class ItemModifyController implements Initializable {
    @FXML
    private HBox itemModifyPane;
    @FXML
    private FlowPane leftFlowPane;
    @FXML
    private GridPane topButtonGridPane;
    @FXML
    private GridPane itemsButtonGridPane;
    @FXML
    private VBox featurePane;
    @FXML
    private FlowPane rightFlowPane;
    @Autowired
    private UpdateItemNameView updateItemNameView;
    @Autowired
    private UpdateItemPriceView updateItemPriceView;

    @Autowired
    private MainView mainView;
    private double mainwidth = Main.primaryScreenBounds.getWidth();
    private double mainheight = Main.primaryScreenBounds.getHeight();
    private double leftFlowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.4;
    private double rightFlowPaneWidth = mainwidth * 0.5;
    private double featurePaneWidth = Main.primaryScreenBounds.getWidth() * 0.1;
    private int topButtonRows = 4;
    private int topButtonCols = 5;
    private int topButtonItemRows = 6;
    private int topButtonItemCols = 5;
    private Stage itemStage;
    private ItemModifyTypeEnum currentType;

    @Autowired
    private TopButtonService topButtonService;
    @Autowired
    private MemPeriodService memPeriodService;
    @Autowired
    private PosItemService posItemService;

    @Autowired
    private NettyComponent nettyComponent;
    @Autowired
    private CacheManagerComponent cacheManagerComponent;

    private int currTopButtonPos = 0;

    private ObservableList<TopButtonDto> topButtonDtos = FXCollections.observableArrayList();

    ToggleButton pauseItemBtn;
    ToggleButton priceItemBtn;
    ToggleButton descItemBtn;


    private Integer currentTopButtonPage;//当前选择按鍵页码

    private Integer currentTopButtonCount;//按鍵总页数

    private Integer currentItemPage;//当前选择菜品页码

    private Integer currentItemPageCount;//菜品总页数

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            itemStage = (Stage) itemModifyPane.getScene().getWindow();
        });
        mainComponent();
    }

    public void initData() {
        if (AppUtils.isNotBlank(pauseItemBtn) | AppUtils.isNotBlank(priceItemBtn)) {
            pauseItemBtn.setSelected(false);
            priceItemBtn.setSelected(false);
            descItemBtn.setSelected(false);

        }
        currTopButtonPos = 0;
        currentType = null;
        leftFlowPane.getChildren().clear();
        getDataTask();
    }

    public void getDataTask() {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
                memPeriodWrapper.eq("ISVALID", "Y");
                // memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
                MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);
                List<TopButtonDto> topButtonDtoList = topButtonService.getTopButtonListNoCache(Main.posOutlet, DateUtil.getWeek(DateUtil.getNowTime()), Main.posPeriodMap.get("price"), AppUtils.isNotBlank(memPeriod) ? memPeriod.getCode() : "ALL");
                topButtonDtos = FXCollections.observableArrayList(topButtonDtoList);
                Platform.runLater(() -> {
                    initRight();
                });

                return null;
            }
        };
        new Thread(task).start();
    }


    public void mainComponent() {

        leftFlowPane.setPrefSize(leftFlowPaneWidth, mainheight);
        featurePane.setPrefSize(featurePaneWidth, mainheight);
        initFeatrurePane();
        rightFlowPane.setPrefSize(rightFlowPaneWidth, mainheight);
        rightFlowPane.setAlignment(Pos.TOP_RIGHT);
        topButtonGridPane.setPrefSize(rightFlowPaneWidth, mainheight * 0.4);
        topButtonGridPane.setAlignment(Pos.TOP_RIGHT);
        topButtonGridPane.getStyleClass().add("rightFunction");
        itemsButtonGridPane.setPrefSize(rightFlowPaneWidth, mainheight * 0.598);
        itemsButtonGridPane.setAlignment(Pos.BOTTOM_RIGHT);
    }

    /**
     * 食品操作功能.例如暫停,修改價格之類的操作都可以在這裡做
     */
    public void initFeatrurePane() {
        double btnheight = mainheight / 5;
        //暫停

        //设定组别
        ToggleGroup btnGroup = new ToggleGroup();


        pauseItemBtn = new ToggleButton(Main.languageMap.get("global.pause"));
        pauseItemBtn.getStyleClass().add("featrurebtn");
        pauseItemBtn.setPrefSize(featurePaneWidth, btnheight);
        pauseItemBtn.setToggleGroup(btnGroup);
        pauseItemBtn.setUserData(ItemModifyTypeEnum.PAUSE);
        VBox.setMargin(pauseItemBtn, new Insets(2));
        featurePane.getChildren().add(pauseItemBtn);
        priceItemBtn = new ToggleButton(Main.languageMap.get("global.price"));
        priceItemBtn.getStyleClass().add("featrurebtn");
        priceItemBtn.setPrefSize(featurePaneWidth, btnheight);
        priceItemBtn.setToggleGroup(btnGroup);
        priceItemBtn.setUserData(ItemModifyTypeEnum.PRICE);
        VBox.setMargin(priceItemBtn, new Insets(2));
        featurePane.getChildren().add(priceItemBtn);
        descItemBtn = new ToggleButton(Main.languageMap.get("global.editdesc"));
        descItemBtn.getStyleClass().add("featrurebtn");
        descItemBtn.setPrefSize(featurePaneWidth, btnheight);
        descItemBtn.setToggleGroup(btnGroup);
        descItemBtn.setUserData(ItemModifyTypeEnum.DESC);
        VBox.setMargin(descItemBtn, new Insets(2));
        featurePane.getChildren().add(descItemBtn);


        btnGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov,
                 Toggle toggle, Toggle new_toggle) -> {
                    if (new_toggle != null) {
                        currentType = (ItemModifyTypeEnum) btnGroup.getSelectedToggle().getUserData();
                        leftFlowPane.getChildren().clear();
                    }

                });


        //關閉按鈕
        Button closeBtn = new Button();
        closeBtn.setPrefSize(featurePaneWidth, btnheight);
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setText(Main.languageMap.get("global.close"));
        VBox.setMargin(closeBtn, new Insets(2));
        closeBtn.setOnAction(event -> {
            //Main.showInitialView(DataSetView.class);
            MainController mainController = (MainController) mainView.getPresenter();
            mainController.iniData();
            Main.showInitialView(mainView.getClass());
        });
        featurePane.getChildren().add(closeBtn);


    }

    public void initRight() {
        //上半部分
        int page = 1;
        int pageCount = (topButtonDtos.size() > topButtonRows * topButtonCols ? (topButtonDtos.size() / (topButtonRows * topButtonCols - 1) + (topButtonDtos.size() % (topButtonRows * topButtonCols - 1) > 0 ? 1 : 0)) : 1);
        nextPageTopButton(page, pageCount, topButtonDtos);
    }

    /**
     * topButton按鈕內容填充
     * 實現點擊按鈕事件
     */
    private void topButtonClick(Button btn, ObservableList<TopButtonDto> topButtonDtos) {
        btn.setOnMouseClicked(event -> {
            String btnText = btn.getText();
            String[] btnTexts = btnText.split("/", -1);
            int clickpage = Integer.parseInt(btnTexts[0]);
            int clickpagecount = Integer.parseInt(btnTexts[1]);
            if (clickpage == clickpagecount) {
                clickpage = 0;
            }
            clickpage++;
            btn.setText(clickpage + "/" + clickpagecount);
            nextPageTopButton(clickpage, clickpagecount, topButtonDtos);
        });
    }

    /**
     * 翻頁加載的方法
     * 當topButton的記錄數超過20以後需要進行翻頁操作
     */
    private void nextPageTopButton(int page, int pageCount, ObservableList<TopButtonDto> topButtonDtos) {
        currentTopButtonPage = page;
        currentTopButtonCount = pageCount;
        topButtonGridPane.getChildren().clear();
        String[] languages = null;
        for (int i = 1; i <= topButtonRows; i++) {
            int k = topButtonCols - 1;
            for (int j = 1; j <= topButtonCols; j++) {
                //Button btn = new Button();

                Button btn = new DwButton(FontSizeEnum.font16);
                btn.setPrefSize(rightFlowPaneWidth / 5, topButtonGridPane.getPrefHeight() / 4);
                GridPane.setMargin(btn, new Insets(1, 1, 2, 0));
                btn.getStyleClass().add("topButtonStyle");
                if (j * i == topButtonRows * topButtonCols) {
                    btn.setText(page + "/" + pageCount);
                    //實現點擊事件
                    topButtonClick(btn, topButtonDtos);
                } else {
                    if ((i * j + k * (i - 1) - 1 + (page - 1) * (topButtonRows * topButtonCols) - (page - 1)) < topButtonDtos.size()) {
                        int pos = i * j + k * (i - 1) - 1 + (page - 1) * (topButtonRows * topButtonCols) - (page - 1);
                        TopButtonDto topButtonDto = topButtonDtos.get(pos);
                        if (AppUtils.isNotBlank(topButtonDto.getButtonImg())) {
                            Image btnImg = AppUtils.loadImage(topButtonDto.getButtonImg());
                            ImageView btnImgView = new ImageView(btnImg);
                            btnImgView.setFitWidth(btn.getPrefWidth() * 0.9);
                            btnImgView.setFitHeight(btn.getPrefHeight() * 0.9);
                            btn.setGraphic(btnImgView);
                        } else {
                            languages = new String[]{topButtonDto.getDeschk(), topButtonDto.getDesccn(), topButtonDto.getDescen(), topButtonDto.getDescot()};
                            btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                        }
                        //點擊TOPBUTTON的事件
                        btn.setOnMouseClicked(event -> {
                            currTopButtonPos = pos;
                            initRightItems(1, 0, topButtonDto.getPosItemDtoList());
                        });
                    } else {
                        btn.setText("");
                    }
                }
                k--;
                topButtonGridPane.add(btn, j - 1, i - 1);
            }
        }
        int pos = currTopButtonPos == 1 ? (page - 1) * (topButtonRows * topButtonCols) - (page - 1) : currTopButtonPos;
        currTopButtonPos = pos;
        initRightItems(1, 0, topButtonDtos.get(pos).getPosItemDtoList());
    }

    /**
     * 翻頁加載的方法
     * 當topButton的記錄數超過20以後需要進行翻頁操作
     */
    public  void nextPageTopButton(int topButtonpPage, int topButtonPageCount,int itemPage,int itemPageCount, ObservableList<TopButtonDto> topButtonDtos) {
        currentTopButtonPage = topButtonpPage;
        currentTopButtonCount = topButtonPageCount;
        topButtonGridPane.getChildren().clear();
        String[] languages = null;
        for (int i = 1; i <= topButtonRows; i++) {
            int k = topButtonCols - 1;
            for (int j = 1; j <= topButtonCols; j++) {
                //Button btn = new Button();

                Button btn = new DwButton(FontSizeEnum.font16);
                btn.setPrefSize(rightFlowPaneWidth / 5, topButtonGridPane.getPrefHeight() / 4);
                GridPane.setMargin(btn, new Insets(1, 1, 2, 0));
                btn.getStyleClass().add("topButtonStyle");
                if (j * i == topButtonRows * topButtonCols) {
                    btn.setText(topButtonpPage + "/" + topButtonPageCount);
                    //實現點擊事件
                    topButtonClick(btn, topButtonDtos);
                } else {
                    if ((i * j + k * (i - 1) - 1 + (topButtonpPage - 1) * (topButtonRows * topButtonCols) - (topButtonpPage - 1)) < topButtonDtos.size()) {
                        int pos = i * j + k * (i - 1) - 1 + (topButtonpPage - 1) * (topButtonRows * topButtonCols) - (topButtonpPage - 1);
                        TopButtonDto topButtonDto = topButtonDtos.get(pos);
                        if (AppUtils.isNotBlank(topButtonDto.getButtonImg())) {
                            Image btnImg = AppUtils.loadImage(topButtonDto.getButtonImg());
                            ImageView btnImgView = new ImageView(btnImg);
                            btnImgView.setFitWidth(btn.getPrefWidth() * 0.9);
                            btnImgView.setFitHeight(btn.getPrefHeight() * 0.9);
                            btn.setGraphic(btnImgView);
                        } else {
                            languages = new String[]{topButtonDto.getDeschk(), topButtonDto.getDesccn(), topButtonDto.getDescen(), topButtonDto.getDescot()};
                            btn.setText(LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                        }
                        //點擊TOPBUTTON的事件
                        btn.setOnMouseClicked(event -> {
                            currTopButtonPos = pos;
                            initRightItems(1, 0, topButtonDto.getPosItemDtoList());
                        });
                    } else {
                        btn.setText("");
                    }
                }
                k--;
                topButtonGridPane.add(btn, j - 1, i - 1);
            }
        }
        int pos = currTopButtonPos == 1 ? (topButtonpPage - 1) * (topButtonRows * topButtonCols) - (topButtonpPage - 1) : currTopButtonPos;
        currTopButtonPos = pos;
        initRightItems(itemPage, itemPageCount, topButtonDtos.get(pos).getPosItemDtoList());
    }


    /**
     * 當菜品數量超過30個的時候會翻頁，翻頁的onclick操作如下
     */
    private void itemOnclickPageTurn(Label itemDescLabel, List<PosItemDto> posItemDtos) {
        itemDescLabel.setWrapText(true);
        itemDescLabel.setPadding(new Insets(0, 0, 0, 5));
        itemDescLabel.setOnMouseClicked(event -> {
            String itemDescText = itemDescLabel.getText();
            String[] itemDescTexts = itemDescText.split("/", -1);
            int clickpage = Integer.parseInt(itemDescTexts[0]);
            int clickpagecount = Integer.parseInt(itemDescTexts[1]);
            if (clickpage == clickpagecount) {
                clickpage = 0;
            }
            clickpage++;
            itemDescLabel.setText(clickpage + "/" + clickpagecount);
            initRightItems(clickpage, clickpagecount, posItemDtos);
        });
    }

    /**
     * getNextPageItems
     * 得到下一頁的items
     * 如果是初始化的話 page = 0,pageCount = 0
     */
    public void initRightItems(int page, int pageCount, List<PosItemDto> posItemDtoList) {
        currentItemPage = page;
        currentItemPageCount = pageCount;
        itemsButtonGridPane.getChildren().remove(0, itemsButtonGridPane.getChildren().size());
        String[] languages = null;
        for (int i = 1; i <= topButtonItemRows; i++) {
            int k = topButtonItemCols - 1;
            for (int j = 1; j <= topButtonItemCols; j++) {
                String itemDesc = "";
                String itemCode = "";
                String itemPrice = "";
                String itemImg = "";

                BorderPane itemBorderPane = new BorderPane();
                itemBorderPane.getStyleClass().add("itemBorderStyle");


                if (i * j == topButtonItemRows * topButtonItemCols) {
                    //分页算法,计算总数
                    if (pageCount == 0) {
                        int perPageCount = topButtonItemRows * topButtonItemCols - 1;
                        pageCount = posItemDtoList.size() / perPageCount + (posItemDtoList.size() % perPageCount > 0 ? 1 : 0);
                    }
                    //top
                    Label itemDescLabel = new DwLabel(FontSizeEnum.font12);
                    itemDescLabel.setText(page + "/" + pageCount);
                    itemDescLabel.setPrefSize(itemsButtonGridPane.getPrefWidth() * 0.175, itemsButtonGridPane.getPrefHeight() * 0.124);

                    itemOnclickPageTurn(itemDescLabel, posItemDtoList);
                    itemBorderPane.setCenter(itemDescLabel);
                } else {
                    if ((i * j + k * (i - 1) - 1 + (page - 1) * (topButtonItemRows * topButtonItemCols) - (page - 1)) < posItemDtoList.size()) {

                        PosItemDto posItemDto = posItemDtoList.get(i * j + k * (i - 1) - 1 + (page - 1) * (topButtonItemRows * topButtonItemCols) - (page - 1));
                        itemImg = posItemDto.getItemImg();
                        languages = new String[]{posItemDto.getDesc1(), posItemDto.getDesc2(), posItemDto.getDesc3(), posItemDto.getDesc4()};
                        itemDesc = (LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
                        itemCode = posItemDto.getItemCode();
                        itemPrice = AppUtils.isBlank(posItemDto.getPrice()) ? "0.00" : posItemDto.getPrice().toString();

                        //如果暂停，则红色背景
                        if (posItemDto.getDiscont() != null && posItemDto.getDiscont().equals(ItemDiscontEnum.PAUSE.getValue())) {
//                            itemBorderPane.getStyleClass().add("itemBorderDiscontStyle");
                            itemBorderPane.setBackground(new Background(new BackgroundFill(new ImagePattern(AppUtils.loadImage("X.png")), CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                        //點擊事件
                        itemBorderPane.setOnMouseClicked(event -> {
                            modifyItem(posItemDto);
                        });

                    }
                    itemBorderPaneInit(itemBorderPane, itemDesc, itemCode, itemPrice, itemImg);
                }
                k--;
                itemsButtonGridPane.add(itemBorderPane, j - 1, i - 1);
            }
        }
    }

    /**
     * 向RightItems填充數據
     */
    private void itemBorderPaneInit(BorderPane itemBorderPane, String itemDesc, String itemCode, String itemPrice, String itemImg) {

        //top
        Label itemDescLabel = new DwLabel(FontSizeEnum.font12);
        itemDescLabel.setAlignment(Pos.CENTER);
        itemDescLabel.setPrefSize(itemsButtonGridPane.getPrefWidth() * 0.175, itemsButtonGridPane.getPrefHeight() * 0.124);
        itemDescLabel.setWrapText(true);
        itemDescLabel.setPadding(new Insets(0, 0, 0, 0));
        if (AppUtils.isNotBlank(itemImg)) {
//            Image btnImg = AppUtils.loadImage(itemImg);
            ImageView btnImgView = new DwImageView(itemImg);
            btnImgView.setFitWidth(itemDescLabel.getPrefWidth());
            btnImgView.setFitHeight(itemDescLabel.getPrefHeight() * 0.9);
            itemDescLabel.setGraphic(btnImgView);
        } else {
            itemDescLabel.setText(itemDesc);
        }

        //left
        Label itemCodeLable = new DwLabel(FontSizeEnum.font10);
        itemCodeLable.setText(itemCode);
        itemCodeLable.setWrapText(true);
//        itemCodeLable.setPadding(new Insets(0, 0, 0, 5));
        //itemCodeLable.setTextFill(Color.web("#B55F58"));
        //right
        Label itemPriceLable = new DwLabel(FontSizeEnum.font10);
        itemPriceLable.setText(itemPrice);
        itemPriceLable.setWrapText(true);

//        itemPriceLable.setPadding(new Insets(0, 5, 0, 0));
        itemPriceLable.setAlignment(Pos.BOTTOM_RIGHT);


        itemBorderPane.setTop(itemDescLabel);
        itemBorderPane.setLeft(itemCodeLable);
        itemBorderPane.setRight(itemPriceLable);
    }

    public void modifyItem(PosItemDto posItemDto) {
        if (AppUtils.isBlank(currentType)) {
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.pleaseselectaction"), resultMap, itemStage);
            return;
        }

        switch (currentType) {
            case PAUSE:
                //暂停
                String itemState = posItemDto.getDiscont();
                //如果是永久停售,则不允许销售
                if (itemState.equals(ItemDiscontEnum.PERMANENTPAUSE.getValue())) {
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.itemcannotopen"), resultMap, itemStage);
                    return;
                }


                String popText = "";
                if (itemState.equals(ItemDiscontEnum.SELLING.getValue())) {
                    popText = Main.languageMap.get("global.itempause");
                } else if (itemState.equals(ItemDiscontEnum.PAUSE.getValue())) {
                    popText = Main.languageMap.get("global.itemopen");
                }

                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), popText, resultMap, itemStage);
                if (AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())) {

                    posItemService.pauseOrOpen(posItemDto);

//                    if (itemState.equals(ItemDiscontEnum.SELLING.getValue())) {
//                        posItemDto.setDiscont(ItemDiscontEnum.PAUSE.getValue());
//                    } else if (itemState.equals(ItemDiscontEnum.PAUSE.getValue())) {
//                        posItemDto.setDiscont(ItemDiscontEnum.SELLING.getValue());
//                    }
                    //发送通知客户端更新
                    nettyComponent.sendMessage(NettyMessageTypeEnum.PAUSEITEM);
                    // 更新topButton
                    cacheManagerComponent.clearByName("com.dw.mapper.TopButtonMapper");
                    cacheManagerComponent.clearByName("com.dw.mapper.PosSetmealMapper");
                    getDataTask();
                }

                break;
            case STOCK:
                //口段
                break;
            case LAYOUT:
                //布局
                break;
            case PRICE:
                //价格
                UpdateItemPriceController updateItemPriceController = (UpdateItemPriceController) updateItemPriceView.getPresenter();
                updateItemPriceController.initData(posItemDto);
                // 更新topButton
                cacheManagerComponent.clearByName("com.dw.mapper.TopButtonMapper");
                break;
            case DESC:
                //改名字
                UpdateItemNameController updateItemNameController = (UpdateItemNameController) updateItemNameView.getPresenter();
                updateItemNameController.initData(posItemDto);
                // 更新topButton
                cacheManagerComponent.clearByName("com.dw.mapper.TopButtonMapper");
                break;

            default:
        }
    }


   /* *//**
     * 向右邊填充
     *//*
    public void openSearchKeyBoradView(TableViewDto tvd) {
        currentRightPage = OrderRightPartEnum.KEYBORAD;
        SearchKeyBoardController searchKeyBoardController = (SearchKeyBoardController) searchKeyBoardView.getPresenter();
        searchKeyBoardController.initData(tvd);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(searchKeyBoardView.getView());

    }*/

    public void refreshItems(){
        Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
        memPeriodWrapper.eq("ISVALID", "Y");
        // memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
        MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);
        List<TopButtonDto> topButtonDtoList = topButtonService.getTopButtonListNoCache(Main.posOutlet, DateUtil.getWeek(DateUtil.getNowTime()), Main.posPeriodMap.get("price"), AppUtils.isNotBlank(memPeriod) ? memPeriod.getCode() : "ALL");
        topButtonDtos.clear();
        this.topButtonDtos = FXCollections.observableArrayList(topButtonDtoList);
    }


}
