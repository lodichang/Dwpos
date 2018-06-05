package com.dw.controller;

import com.dw.Main;
import com.dw.dto.*;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwImageView;
import com.dw.extended.DwLabel;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.util.IDManager;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wen.jing on 2018/5/14
 */
@Setter
@Getter
@FXMLController
public class ItemChooseController implements Initializable {
    @FXML
    private FlowPane rightFlowPane;
    @FXML
    private GridPane topButtonGridPane;
    @FXML
    private GridPane itemsButtonGridPane;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;


    private int topButtonRows = 4;
    private int topButtonCols = 5;
    private int topButtonItemRows = 6;
    private int topButtonItemCols = 5;
    //默认的
    private final String comId = "1";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRightComponent();
    }


    /**
     * 初始化右边PANE佈局
     */
    private void initRightComponent() {

        rightFlowPane.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight());
        rightFlowPane.setAlignment(Pos.TOP_RIGHT);
        topButtonGridPane.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight() * 0.4);
        topButtonGridPane.setAlignment(Pos.TOP_RIGHT);
        topButtonGridPane.getStyleClass().add("rightFunction");
        itemsButtonGridPane.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight() * 0.598);
        itemsButtonGridPane.setAlignment(Pos.BOTTOM_RIGHT);


    }

    /**
     * 初始化右側大類和第一個大類的菜品
     */
    public void initRight() {
        //上半部分
        int page = 1;
        int pageCount = (takeOrderIndexController.getTopButtonDtos().size() > topButtonRows * topButtonCols ? (takeOrderIndexController.getTopButtonDtos().size() / (topButtonRows * topButtonCols - 1) + (takeOrderIndexController.getTopButtonDtos().size() % (topButtonRows * topButtonCols - 1) > 0 ? 1 : 0)) : 1);
        nextPageTopButton(page, pageCount, takeOrderIndexController.getTopButtonDtos());
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
        topButtonGridPane.getChildren().clear();
        String[] languages = null;
        for (int i = 1; i <= topButtonRows; i++) {
            int k = topButtonCols - 1;
            for (int j = 1; j <= topButtonCols; j++) {
                //Button btn = new Button();

                Button btn = new DwButton(FontSizeEnum.font16);
                btn.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth() / 5, topButtonGridPane.getPrefHeight() / 4);
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
                        btn.setOnMouseClicked(event ->{
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

        initRightItems(1, 0, topButtonDtos.get((page - 1) * (topButtonRows * topButtonCols) - (page - 1)).getPosItemDtoList());
    }


    /**
     * 下半部分
     * 當點擊topButton的時候加載topButton對應的菜品
     */


//    private void initRightItems(List<PosItemDto> posItemDtoList) {
//
//
//        List<PosItemDto> posItemDtos = (AppUtils.isNotBlank(posItemDtoList)) ? posItemDtoList : new LinkedList<PosItemDto>();
//        //清除所有節點
//        itemsButtonGridPane.getChildren().remove(0, itemsButtonGridPane.getChildren().size());
//        if (AppUtils.isNotBlank(posItemDtos) && AppUtils.isNotBlank(posItemDtos.get(0).getItemCode())) {
//            String[] languages = null;
//            int page = 1;
//            int pageCount = (posItemDtos.size() > topButtonItemRows * topButtonItemCols ? (posItemDtos.size() / (topButtonItemRows * topButtonItemCols - 1) + (posItemDtos.size() % (topButtonItemRows * topButtonItemCols - 1) > 0 ? 1 : 0)) : 1);
//            for (int i = 1; i <= topButtonItemRows; i++) {
//                int k = topButtonItemCols - 1;
//                for (int j = 1; j <= topButtonItemCols; j++) {
//                    String itemDesc = "";
//                    String itemCode = "";
//                    String itemPrice = "";
//                    String itemImg = "";
//
//                    BorderPane itemBorderPane = new BorderPane();
//                    itemBorderPane.getStyleClass().add("itemBorderStyle");
//
//                    if (posItemDtos.size() > topButtonItemRows * topButtonItemCols) {
//                        if (i * j == topButtonItemRows * topButtonItemCols) {
//                            //top
//                            Label itemDescLabel = new DwLabel(FontSizeEnum.font12);
//                            itemDescLabel.setText(page + "/" + pageCount);
//                            itemDescLabel.setPrefSize(rightFlowPaneWidth * 0.175, itemsButtonGridPane.getPrefHeight() * 0.124);
//
//                            itemOnclickPageTurn(itemDescLabel, posItemDtos);
//                            itemBorderPane.setCenter(itemDescLabel);
//                        } else {
//
//                            PosItemDto posItemDto = posItemDtos.get(i * j + k * (i - 1) - 1);
//                            itemImg = posItemDto.getItemImg();
//                            languages = new String[]{posItemDto.getDesc1(), posItemDto.getDesc2(), posItemDto.getDesc3(), posItemDto.getDesc4()};
//                            itemDesc = (LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
//                            itemCode = posItemDto.getItemCode();
//                            itemPrice = AppUtils.isBlank(posItemDto.getPrice()) ? "0.00" : posItemDto.getPrice().toString();
//
//                            //如果暂停，则红色背景
//                            if (posItemDto.getDiscont() != null && posItemDto.getDiscont().equals(ItemDiscontEnum.PAUSE.getValue())) {
//                                itemBorderPane.getStyleClass().add("itemBorderDiscontStyle");
//                            } else {
//                                //點擊事件
//                                itemBorderPane.setOnMouseClicked(event -> {
//                                    itemBorderPaneOnClick(posItemDto);
//
//                                });
//                            }
//                            itemBorderPaneInit(itemBorderPane, itemsButtonGridPane, itemDesc, itemCode, itemPrice, itemImg);
//                        }
//                    } else {
//                        if ((i * j + k * (i - 1) - 1) < posItemDtos.size()) {
//                            PosItemDto posItemDto = posItemDtos.get(i * j + k * (i - 1) - 1);
//
//                            itemImg = posItemDto.getItemImg();
//                            languages = new String[]{posItemDto.getDesc1(), posItemDto.getDesc2(), posItemDto.getDesc3(), posItemDto.getDesc4()};
//                            itemDesc = (LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue())));
//                            itemCode = posItemDto.getItemCode();
//                            itemPrice = AppUtils.isBlank(posItemDto.getPrice()) ? "0.00" : posItemDto.getPrice().toString();
//
//                            //如果暂停，则红色背景
//                            if (posItemDto.getDiscont() != null && posItemDto.getDiscont().equals(ItemDiscontEnum.PAUSE.getValue())) {
//                                itemBorderPane.getStyleClass().add("itemBorderDiscontStyle");
//                            } else {
//                                //點擊事件
//                                itemBorderPane.setOnMouseClicked(event -> {
//                                    itemBorderPaneOnClick(posItemDto);
//
//                                });
//                            }
//                        }
//                        itemBorderPaneInit(itemBorderPane, itemDesc, itemCode, itemPrice, itemImg);
//                    }
//                    k--;
//                    itemsButtonGridPane.add(itemBorderPane, j - 1, i - 1);
//
//                }
//            }
//        } else {
//            for (int i = 1; i <= topButtonItemRows; i++) {
//                int k = topButtonItemCols - 1;
//                for (int j = 1; j <= topButtonItemCols; j++) {
//
//                    BorderPane itemBorderPane = new BorderPane();
//                    itemBorderPane.getStyleClass().add("itemBorderStyle");
//
//                    itemBorderPaneInit(itemBorderPane, "", "", "", "");
//                    k--;
//                    itemsButtonGridPane.add(itemBorderPane, j - 1, i - 1);
//                }
//            }
//        }
//    }

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
                            itemBorderPane.setBackground(new Background(new BackgroundFill(new ImagePattern(AppUtils.loadImage("X.png")), CornerRadii.EMPTY, Insets.EMPTY)));
                            //itemBorderPane.getStyleClass().add("itemBorderDiscontStyle");
                        } else {
                            //點擊事件
                            itemBorderPane.setOnMouseClicked(event -> {
                                itemBorderPaneOnClick(posItemDto);
                            });
                        }
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

    /**
     * 点击菜品按钮的处理事件
     *
     * @param posItemDto
     */
    public void itemBorderPaneOnClick(PosItemDto posItemDto) {
        takeOrderIndexController.getFoodTableView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        String[] languages = new String[]{posItemDto.getDesc1(), posItemDto.getDesc2(), posItemDto.getDesc3(), posItemDto.getDesc4()};
        String itemName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));

        ObservableList<TableViewDto> selectedTableViewDtos = takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedItems();
        //套餐详情
        if (AppUtils.isNotBlank(selectedTableViewDtos) && AppUtils.isNotBlank(selectedTableViewDtos.get(0).getMealCode()) && !selectedTableViewDtos.get(0).isPrinter()
                && !selectedTableViewDtos.get(0).getScount().equals(selectedTableViewDtos.get(0).getGpCount()) && checkMealGroup(posItemDto)
                ) {
            TableViewDto selectedTableView = selectedTableViewDtos.get(0);
            List<PosSetmealGroupDto> posSetmealGroupDtoList = takeOrderIndexController.getSetmealGroupDtoObservableMap().get(selectedTableViewDtos.get(0).getMealCode());
            posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                if (posSetmealGroupDto.getSGroup().equals(selectedTableView.getSgroup()) && posSetmealGroupDto.getSCount().equals(selectedTableView.getScount())) {
                    List<PosSetmealDetailDto> posSetmealDetailDtoList = posSetmealGroupDto.getSetmealDetailList();
                    posSetmealDetailDtoList.forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                        if (posItemDto.getItemCode().equals(posSetmealDetailDto.getItemCode())) {
                            //selectedTableView.setItemCode(posItemDto.getItemCode());
                            //selectedTableView.setItemName(posItemDto.getDesc1());
                            TableViewDto t = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                                    posItemDto.getItemCode(),
                                    posItemDto.getDesc1(), "", "", posItemDto.getPrice().doubleValue(), posItemDto.getPrice().doubleValue(), "", "",
                                    0.00, "", posItemDto.getOrgPrice().doubleValue(), "", false, selectedTableView.getItemIdx(), selectedTableView.getService(),
                                    posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(), posSetmealDetailDto.getItemPrn(),
                                    comId, "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), posSetmealGroupDto.getIsHoldOn(), selectedTableView.gettDate());
                            t.setSetmealItemIdx(selectedTableView.getSetmealItemIdx());
                            t.setId(selectedTableView.getId());
                            if (t.isIsHoldOn() && !takeOrderIndexController.getIsUpdateOrder()) {
                                t.setHoldOn(HoldOnEnum.HOLDON.getValue());
                            }
                            int index = takeOrderIndexController.getTableViewData().indexOf(selectedTableView);
                            if (index > -1) {
                                takeOrderIndexController.getTableViewData().set(index, t);
                                if ((index + 1) <= takeOrderIndexController.getTableViewData().size() - 1) {
                                    for (int i = index + 1; i < takeOrderIndexController.getTableViewData().size(); i++) {
                                        if (AppUtils.isNotBlank(takeOrderIndexController.getTableViewData().get(i).getMealCode()) && takeOrderIndexController.getTableViewData().get(i).getMealCode().equals(selectedTableView.getMealCode())) {
                                            if (AppUtils.isBlank(takeOrderIndexController.getTableViewData().get(i).getItemCode())) {
                                                takeOrderIndexController.getFoodTableView().getSelectionModel().select(i);
                                                int finalI = i;
                                                posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroup) -> {
                                                    if (posSetmealGroup.getSCount().equals(takeOrderIndexController.getTableViewData().get(finalI).getScount()) && posSetmealGroup.getGpCount().equals(takeOrderIndexController.getTableViewData().get(finalI).getGpCount()) && posSetmealGroup.getSGroup().equals(takeOrderIndexController.getTableViewData().get(finalI).getSgroup())) {
                                                        List<PosItemDto> posItemList = new ArrayList<PosItemDto>();
                                                        posSetmealGroup.getSetmealDetailList().forEach((PosSetmealDetailDto posSetmealDetail) -> {
                                                            PosItemDto posItem = new PosItemDto();
                                                            posItem.setItemCode(posSetmealDetail.getItemCode());
                                                            posItem.setDesc1(posSetmealDetail.getItemDesc1());
                                                            posItem.setPrice(posSetmealDetail.getAddPrice() == null ? BigDecimal.ZERO : posSetmealDetail.getAddPrice());
                                                            posItem.setOrgPrice(BigDecimal.ZERO);
                                                            posItem.setDiscont(posSetmealDetail.getDiscont());
                                                            posItemList.add(posItem);
                                                        });
                                                        initRightItems(1, 0, posItemList);
                                                        takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedIndex()-5);
                                                    }
                                                });
                                                break;
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            initRight();
                                            takeOrderIndexController.getFoodTableView().getSelectionModel().select(index);
                                            takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedIndex()-5);
                                            break;
                                        }

                                    }
                                } else {
                                    initRight();
                                    takeOrderIndexController.getFoodTableView().getSelectionModel().select(index);
                                    takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedIndex()-5);
                                }

                            }
                        }
                    });
                }
            });
        }
        //主套餐
        else if (AppUtils.isNotBlank(takeOrderIndexController.getSetmealGroupDtoObservableMap().get(posItemDto.getItemCode()))) {
            if (!takeOrderIndexController.getCurrentRightPage().equals(OrderRightPartEnum.ITEMCHOOSE)) {
                takeOrderIndexController.openItemChooseView();
            }
            showMeal(posItemDto);
        }
        //单品
        else {
            if (AppUtils.isBlank(takeOrderIndexController.getTableViewData())) {
                // 檢查是否入價錢
                if (AppUtils.isNotBlank(posItemDto.getIopen()) && ItemIopenEnum.TRUE.getValue().equals(posItemDto.getIopen())) {
                    TableViewDto tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                            posItemDto.getItemCode(),
                            itemName, "", "",
                            Double.parseDouble(posItemDto.getPrice().toString()),
                            Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), posItemDto.getPrice()).toString()),
                            "", "", 0.00, "", Double.parseDouble(posItemDto.getOrgPrice().toString()),
                            "", false, 0, posItemDto.getService(), "", "", "", "",
                            posItemDto.getPrn(), posItemDto.getCombId(), "", "", posItemDto.getCatt(), posItemDto.getAttMax(),
                            posItemDto.getAttMin(), IDManager.generateID(), false, null);
                    tvd.setIopen(posItemDto.getIopen());
                    // 彈出鍵盤
                    takeOrderIndexController.openSearchKeyBoradView(tvd);
                } else {
                    TableViewDto tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                            posItemDto.getItemCode(),
                            itemName, "", "",
                            Double.parseDouble(posItemDto.getPrice().toString()),
                            Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), posItemDto.getPrice()).toString()), "", "", 0.00, "", posItemDto.getOrgPrice().doubleValue(),
                            "", false, 0, posItemDto.getService(), "", "", "", "", posItemDto.getPrn(), posItemDto.getCombId(), "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), false, null);
                    takeOrderIndexController.getTableViewData().add(tvd);
                    takeOrderIndexController.getFoodTableView().getSelectionModel().select(takeOrderIndexController.getTableViewData().size() - 1);
                    takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getTableViewData().size() - 1);
                }
            } else {
                // 檢查是否入價錢
                if (AppUtils.isNotBlank(posItemDto.getIopen()) && ItemIopenEnum.TRUE.getValue().equals(posItemDto.getIopen())) {
                    TableViewDto tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                            posItemDto.getItemCode(),
                            itemName, "", "",
                            Double.parseDouble(posItemDto.getPrice().toString()),
                            Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), posItemDto.getPrice()).toString()),
                            "", "", 0.00, "", Double.parseDouble(posItemDto.getOrgPrice().toString()),
                            "", false, 0, posItemDto.getService(), "", "", "", "",
                            posItemDto.getPrn(), posItemDto.getCombId(), "", "", posItemDto.getCatt(), posItemDto.getAttMax(),
                            posItemDto.getAttMin(), IDManager.generateID(), false, null);
                    tvd.setIopen(posItemDto.getIopen());
                    // 彈出鍵盤
                    takeOrderIndexController.openSearchKeyBoradView(tvd);
                } else { // 不入價錢
                    boolean existsItemCode = false;
                    for (int i = 0; i < takeOrderIndexController.getTableViewData().size(); i++) {
                        String viewItemQty = takeOrderIndexController.getTableViewData().get(i).getQty();
                        String viewItemCode = takeOrderIndexController.getTableViewData().get(i).getItemCode();
                        String viewItemName = takeOrderIndexController.getTableViewData().get(i).getItemName();
                        BigDecimal viewItemPrice = new BigDecimal(takeOrderIndexController.getTableViewData().get(i).getPrice());
                        BigDecimal viewOrgPrice = new BigDecimal(takeOrderIndexController.getTableViewData().get(i).getOrgPrice());
                        String viewItemAtt = takeOrderIndexController.getTableViewData().get(i).getItemAtt();
                        String viewItemKicmsg = takeOrderIndexController.getTableViewData().get(i).getItemKicMsg();
                        boolean viewIsPrinter = takeOrderIndexController.getTableViewData().get(i).isPrinter();
                        boolean isCombFlag = takeOrderIndexController.getTableViewData().get(i).isFlag();
                        String viewMealCode = takeOrderIndexController.getTableViewData().get(i).getMealCode();
                        int viewHoldOn = takeOrderIndexController.getTableViewData().get(i).getHoldOn();
                        if (viewItemCode.equals(posItemDto.getItemCode()) && viewItemName.equals(itemName)
                                && viewItemAtt.equals("") && viewItemKicmsg.equals("")
                                && viewItemPrice.compareTo(posItemDto.getPrice()) == 0
                                && viewOrgPrice.compareTo(posItemDto.getOrgPrice()) == 0
                                && !viewIsPrinter && !isCombFlag && AppUtils.isBlank(viewMealCode)
                                && takeOrderIndexController.getSetmealGroupDtoObservableMap().get(viewItemCode) == null
                                && viewHoldOn==0) {
                            TableViewDto tvd = new TableViewDto("" + (Integer.parseInt(viewItemQty) + takeOrderIndexController.getPerAddItemCount()),
                                    posItemDto.getItemCode(),
                                    itemName, "", "",
                                    Double.parseDouble(posItemDto.getPrice().toString()),
                                    Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + (Integer.parseInt(viewItemQty) + takeOrderIndexController.getPerAddItemCount())), posItemDto.getPrice()).toString()), "", "", 0.00, "",
                                    Double.parseDouble(posItemDto.getOrgPrice().toString()), "", false, 0, posItemDto.getService(), "", "", "", "", posItemDto.getPrn()
                                    , posItemDto.getCombId(), "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), false, takeOrderIndexController.getTableViewData().get(i).gettDate());
                            tvd.setHoldOn(takeOrderIndexController.getTableViewData().get(i).getHoldOn());
                            tvd.setId(takeOrderIndexController.getTableViewData().get(i).getId());
                            takeOrderIndexController.getTableViewData().set(i, tvd);
                            takeOrderIndexController.getFoodTableView().getSelectionModel().select(i);
                            takeOrderIndexController.getFoodTableView().scrollTo(i);
                            existsItemCode = true;
                            break;
                        }
                    }
                    if (!existsItemCode) {
                        TableViewDto tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                                posItemDto.getItemCode(),
                                itemName, "", "",
                                Double.parseDouble(posItemDto.getPrice().toString()),
                                Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), posItemDto.getPrice()).toString()), "", "", 0.00, "",
                                Double.parseDouble(posItemDto.getOrgPrice().toString()), "", false, 0, posItemDto.getService(), "", "", "", "", posItemDto.getPrn(), posItemDto.getCombId(), "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), false, null);
                        takeOrderIndexController.getTableViewData().add(tvd);
                        takeOrderIndexController.getFoodTableView().getSelectionModel().select(takeOrderIndexController.getTableViewData().size() - 1);
                        takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getTableViewData().size() - 1);
                    }
                    //得到菜品總數量
//                takeOrderIndexController.infoPaneInit(0, posItemDto.getItemCode());
//                initItemCheckAtt(rightAttFlowPane, foodTableView.getItems().get(foodTableView.getSelectionModel().getSelectedIndex()));
                }
            }
        }
        takeOrderIndexController.checkMustAtt(posItemDto, 0);
    }

    /**
     * 显示整个套餐
     *
     * @param posItemDto
     */
    public void showMeal(PosItemDto posItemDto) {
        String[] languages = new String[]{posItemDto.getDesc1(), posItemDto.getDesc2(), posItemDto.getDesc3(), posItemDto.getDesc4()};
        String itemName = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
        List<PosSetmealGroupDto> posSetmealGroupDtoList = takeOrderIndexController.getSetmealGroupDtoObservableMap().get(posItemDto.getItemCode());
        if (AppUtils.isNotBlank(posSetmealGroupDtoList)) {
            TableViewDto tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                    posItemDto.getItemCode(),
                    itemName, "", "",
                    Double.parseDouble(posItemDto.getPrice().toString()),
                    Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), posItemDto.getPrice()).toString()), "", "", 0.00, "", Double.parseDouble(posItemDto.getOrgPrice().toString()),
                    "", false, 0, posItemDto.getService(), "", "", "", "", posItemDto.getPrn(), comId, "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), false, null);
            takeOrderIndexController.getTableViewData().add(tvd);
            //foodTableView.getSelectionModel().select(tableViewData.size() - 1);
            final boolean[] isSelected = {false};
            //遍历套餐组别
            posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                //必选组别
                if (posSetmealGroupDto.getSCount().equals(posSetmealGroupDto.getGpCount())) {
                    if (AppUtils.isNotBlank(posSetmealGroupDto.getSetmealDetailList())) {
                        posSetmealGroupDto.getSetmealDetailList().forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                            String[] itemlanguages = new String[]{posSetmealDetailDto.getItemDesc1(), posSetmealDetailDto.getItemDesc2(), posSetmealDetailDto.getItemDesc3(), posSetmealDetailDto.getItemDesc4()};
                            String itemDesc = LanguageEnum.getLanguage(itemlanguages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            TableViewDto t = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                                    posSetmealDetailDto.getItemCode(),
                                    itemDesc, "", "", 0.00, 0.00, "",
                                    "", 0.00, "", 0.00,
                                    "", false, 0, posItemDto.getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(), posSetmealDetailDto.getItemPrn(), comId, "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), posSetmealGroupDto.getIsHoldOn(), null);
                            //必选组别默认叫起时
                            if (t.isIsHoldOn() && !takeOrderIndexController.getIsUpdateOrder()) {
                                t.setHoldOn(HoldOnEnum.HOLDON.getValue());
                            }
                            takeOrderIndexController.getTableViewData().add(t);
                        });
                    }
                } else {
                    for (int i = 0; i < Integer.parseInt(posSetmealGroupDto.getSCount()); i++) {
                        String[] gplanguages = new String[]{posSetmealGroupDto.getGpDesc1(), posSetmealGroupDto.getGpDesc2(), posSetmealGroupDto.getGpDesc3(), posSetmealGroupDto.getGpDesc4()};
                        String gpItemName = LanguageEnum.getLanguage(gplanguages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                        TableViewDto t = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                                "",
                                Main.languageMap.get("notsure.item") + gpItemName, "", "", 0.00, 0.00, "",
                                "", 0.00, "", 0.00,
                                "", false, 0, posItemDto.getService(), posSetmealGroupDto.getCode(), posSetmealGroupDto.getSGroup(), posSetmealGroupDto.getSCount(), posSetmealGroupDto.getGpCount(), "", comId, "", "", posItemDto.getCatt(), posItemDto.getAttMax(), posItemDto.getAttMin(), IDManager.generateID(), posSetmealGroupDto.getIsHoldOn(), null);
                        takeOrderIndexController.getTableViewData().add(t);
                        if (i == 0 && !isSelected[0]) {
                            takeOrderIndexController.getFoodTableView().getSelectionModel().select(takeOrderIndexController.getTableViewData().size() - 1);
                            takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedIndex()-5);
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
                            isSelected[0] = true;

                            initRightItems(1, 0, posItemList);
                        }
                    }
                }
            });
        }
    }


    /**
     * 驗證當前點擊的食品按鈕是否是替換所選套餐組別
     */


    public boolean checkMealGroup(PosItemDto posItemDto) {
        ObservableList<TableViewDto> tableViewDtos = takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedItems();
        if (AppUtils.isNotBlank(tableViewDtos)) {
            TableViewDto selectedTableView = tableViewDtos.get(0);
            List<PosSetmealGroupDto> posSetmealGroupDtoList = takeOrderIndexController.getSetmealGroupDtoObservableMap().get(selectedTableView.getMealCode());
            if (AppUtils.isNotBlank(posSetmealGroupDtoList)) {
                final boolean[] flag = {false};
                posSetmealGroupDtoList.forEach((PosSetmealGroupDto posSetmealGroupDto) -> {
                    if (posSetmealGroupDto.getSGroup().equals(selectedTableView.getSgroup()) && posSetmealGroupDto.getSCount().equals(selectedTableView.getScount())) {
                        List<PosSetmealDetailDto> posSetmealDetailDtoList = posSetmealGroupDto.getSetmealDetailList();
                        posSetmealDetailDtoList.forEach((PosSetmealDetailDto posSetmealDetailDto) -> {
                            if (posItemDto.getItemCode().equals(posSetmealDetailDto.getItemCode())) {
                                flag[0] = true;
                            }
                        });
                    }
                });
                return flag[0];
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
