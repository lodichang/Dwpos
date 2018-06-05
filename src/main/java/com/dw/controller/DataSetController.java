package com.dw.controller;

import com.dw.Main;
import com.dw.model.Goods;
import com.dw.util.NumberFormatUtils;
import com.dw.view.FunctionsView;
import com.dw.view.ItemModifyView;
import com.dw.view.TableSettingView;
import com.sun.javafx.robot.impl.FXRobotHelper;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by lodi on 2017/12/21.
 */
@Getter
@Setter
@FXMLController
public class DataSetController implements Initializable {


    @FXML
    private FlowPane root;

    @FXML
    private FlowPane dataSetFlowPane;

    @FXML
    private Button stopTasteButton;

    @FXML
    private Button stopFoodButton;

    @FXML
    private Button buckleChangeStewButton;

    @FXML
    private Button keyDesignButton;

    @FXML
    private Button deskDesignButton;

    @FXML
    private Button chargeCategoriesButton;

    @FXML
    private Button properFoodButton;

    @FXML
    private Button otherSettingButton;

    @FXML
    private Button closeButton;

    @FXML
    private FlowPane buttomFlowPane;


    private Rectangle2D primaryScreenBounds;

    private Main main;

  /*  @Autowired
    NewTableSettingsController newTableSettingsController;*/

    @Autowired
    private TableSettingView tableSettingView;
    @Autowired
    private ItemModifyView itemModifyView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDataSet();
    }


    public void showDataSet() {
        root.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());

        double gap = 10.00;
        //按鈕數量
        int btnSize = 5;
        //底部按鈕的寬度
        double subNodeWidth = Main.primaryScreenBounds.getWidth() / btnSize - gap - btnSize / gap;
        //底部按鈕的高度
        double subNodeHeight = Main.primaryScreenBounds.getHeight() / 8 - 10;

        dataSetFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 8 * 7);

        dataSetFlowPane.setPadding(new Insets(10, 0, 0, 5));
        dataSetFlowPane.setHgap(gap);
        dataSetFlowPane.setVgap(gap);
        stopTasteButton.setPrefWidth(subNodeWidth);
        stopTasteButton.setPrefHeight(subNodeHeight);
        stopFoodButton.setPrefWidth(subNodeWidth);
        stopFoodButton.setPrefHeight(subNodeHeight);
        buckleChangeStewButton.setPrefWidth(subNodeWidth);
        buckleChangeStewButton.setPrefHeight(subNodeHeight);
        keyDesignButton.setPrefWidth(subNodeWidth);
        keyDesignButton.setPrefHeight(subNodeHeight);
        deskDesignButton.setPrefWidth(subNodeWidth);
        deskDesignButton.setPrefHeight(subNodeHeight);
        chargeCategoriesButton.setPrefWidth(subNodeWidth);
        chargeCategoriesButton.setPrefHeight(subNodeHeight);
        properFoodButton.setPrefWidth(subNodeWidth);
        properFoodButton.setPrefHeight(subNodeHeight);
        otherSettingButton.setPrefWidth(subNodeWidth);
        otherSettingButton.setPrefHeight(subNodeHeight);
        buttomFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 8);
        buttomFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
        buttomFlowPane.setPadding(new Insets(0, 10, 0, 0));
        buttomFlowPane.setAlignment(Pos.CENTER_RIGHT);
        closeButton.setPrefWidth(subNodeWidth);
        closeButton.setPrefHeight(subNodeHeight);
    }


    //暫停口味
    @FXML
    public void stopTastesView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/TasteIndex.fxml"));
        HBox hBox = loader.load();
        TasteIndexController tasteIndexController = loader.getController();
        double tasteIndexWidth = primaryScreenBounds.getWidth();
        double tasteIndexHeight = primaryScreenBounds.getHeight();
        tasteIndexController.setPrimaryScreenBounds(primaryScreenBounds);
        hBox.setPrefWidth(tasteIndexWidth);
        hBox.setPrefHeight(tasteIndexHeight);
        //口味暫停主頁左側顯示表格
        double tableViewWidth = tasteIndexWidth / 10 * 4;
        double tableViewHeight = tasteIndexHeight / 10 * 9;
        FlowPane tableFlowPane = (FlowPane) hBox.getChildren().get(0);
        tableFlowPane.setPrefWidth(tableViewWidth);
        tableFlowPane.setPrefHeight(primaryScreenBounds.getHeight());
        tasteIndexController.getFoodTableView().setPrefHeight(tableViewHeight);
        tasteIndexController.getFoodTableView().setPrefWidth(primaryScreenBounds.getWidth() / 10 * 4);
        tasteIndexController.getNumberColum().setPrefWidth(tableViewWidth / 10);
        tasteIndexController.getGoodNameColum().setPrefWidth(tableViewWidth / 10 * 6);
        tasteIndexController.getPriceColum().setPrefWidth(tableViewWidth / 10 * 2);
        tasteIndexController.getMessageColum().setPrefWidth(tableViewWidth / 10);
    /*    FlowPane tableFlowPane = (FlowPane) tableVBox.getChildren().get(0);
        tableFlowPane.setPrefHeight(tasteIndexHeight/10*9);
        tableFlowPane.setPrefWidth(tableVboxWidth);*/


      /*  //左侧底部
        FlowPane tableLabelFlowPane = (FlowPane)  tableFlowPane.getChildren().get(1);
        double tableLabelWidth = tableViewWidth/2;
        double tableLabelHeight = tasteIndexHeight/10;
        tableLabelFlowPane.setPrefHeight(tableLabelHeight);
        tableLabelFlowPane.setPrefWidth(tableLabelWidth);
        tasteIndexController.getTableLabel().setPrefWidth(tableLabelWidth);
        tasteIndexController.getTableLabel().setPrefHeight(tableLabelHeight);
        tasteIndexController.getTableLabel().setText("55桌 0/0 0.00");
*/

        FlowPane tableButtonFlowPane = (FlowPane) tableFlowPane.getChildren().get(1);
        double tableButtonFlowPaneWidth = tasteIndexWidth / 10 * 4;
        double tableButtonFlowPaneHeight = tasteIndexHeight / 10;
        tableButtonFlowPane.setPrefHeight(tableButtonFlowPaneHeight);
        tableButtonFlowPane.setPrefWidth(tableButtonFlowPaneWidth);
        double tableLabelWidth = tableViewWidth / 5;
        double tableLabelHeight = tasteIndexHeight / 10;
        tasteIndexController.getTableNoLabel().setPrefHeight(tableLabelHeight);
        tasteIndexController.getTableNoLabel().setPrefWidth(tableLabelWidth);
        tasteIndexController.getTableNoLabel().setText("55桌");
        tasteIndexController.getTotalNumberLabel().setPrefHeight(tableLabelHeight);
        tasteIndexController.getTotalNumberLabel().setPrefWidth(tableLabelWidth);
        tasteIndexController.getTotalNumberLabel().setText("0/0");
        tasteIndexController.getTotalPriceLabel().setPrefHeight(tableLabelHeight);
        tasteIndexController.getTotalPriceLabel().setPrefWidth(tableLabelWidth);
        tasteIndexController.getTotalPriceLabel().setText("0.00");
        double tableButtonlWidth = tableViewWidth / 5;
        double tableButtonlHeight = tasteIndexHeight / 10;
        tasteIndexController.getClearButton().setPrefHeight(tableButtonlHeight);
        tasteIndexController.getClearButton().setPrefWidth(tableButtonlWidth);


        //口味暫停主頁中間食品，口味顯示部分
        FlowPane goodsFlowPane = (FlowPane) hBox.getChildren().get(1);
        double goodsFlowPaneWidth = primaryScreenBounds.getWidth() / 10 * 5;
        double goodsFlowPaneHeight = primaryScreenBounds.getHeight();
        goodsFlowPane.setPrefWidth(goodsFlowPaneWidth);
        goodsFlowPane.setPrefHeight(goodsFlowPaneHeight);

        FlowPane itemsFlowPane = (FlowPane) goodsFlowPane.getChildren().get(0);
        double itemsFlowPaneWidth = goodsFlowPaneWidth;
        double itemsFlowPaneHeight = goodsFlowPaneHeight / 10 * 9;
        itemsFlowPane.setPrefWidth(itemsFlowPaneWidth);
        itemsFlowPane.setPrefHeight(itemsFlowPaneHeight);
        itemsFlowPane.setHgap(itemsFlowPaneWidth / 60);
        itemsFlowPane.setVgap(itemsFlowPaneHeight / 80);
        itemsFlowPane.setPadding(new Insets(itemsFlowPaneHeight / 80, 0, 0, itemsFlowPaneWidth / 45));

        for (int i = 0; i < 20; i++) {
            FlowPane itemFlowPane = new FlowPane();
            double itemFlowPaneWidth = itemsFlowPaneWidth / 6;
            double itemFlowPaneHeight = itemsFlowPaneHeight / 5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight / 5 * 3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg", itemImageWidth, itemImageHeight, true, true);
            javafx.scene.image.ImageView itemImageView = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);

   /*         itemLabel.setText("半隻貴妃雞仔蛋");

            itemLabel.setPrefWidth(itemFlowPaneWidth);
            itemLabel.setPrefHeight(itemLabelHeight);
            itemLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");*/
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane = new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight / 5 * 2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight / 5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("半隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth / 2;
            double codeLabelHeight = itemFlowPaneHeight / 5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth / 2;
            double priceLabelHeight = itemFlowPaneHeight / 5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                boolean isExist = false;
                Integer selectedIndex = -1;
                ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
                for (int k = 0; k < tasteIndexController.getFoodTableView().getItems().size(); k++) {
                    if (goods.get(k).getGoodName().equals(codeLabel.getText() + "半隻貴妃雞仔蛋")) {
                        isExist = true;
                        selectedIndex = k;
                    }
                }
                if (!isExist) {
                    goods.add(new Goods("1", codeLabel.getText() + "半隻貴妃雞仔蛋", "35.00", ""));
                    tasteIndexController.getFoodTableView().getSelectionModel().selectLast();
                } else {
                    if (selectedIndex != -1) {
                        tasteIndexController.getFoodTableView().getItems().get(selectedIndex).setNumber((Integer.parseInt(tasteIndexController.getFoodTableView().getItems().get(selectedIndex).getNumber()) + 1) + "");

                    }
                }
                Integer totalNumber = 0;
                Double totalPrice = 0.00d;
                for (Goods good : goods) {
                    totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                    totalPrice = totalPrice + Integer.parseInt(good.getNumber()) * Double.parseDouble(good.getPrice());
                }
                tasteIndexController.getTotalNumberLabel().setText(goods.size() + "/" + totalNumber);
                tasteIndexController.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
               /* tasteIndexController.getFoodTableView().getItems().add(new Goods("1", itemLabel.getText(),priceLabel.getText(),""));
                tasteIndexController.getFoodTableView().getSelectionModel().selectLast();*/
                if (tasteIndexController.getSearchFoodController() != null) {
                    tasteIndexController.getSearchFoodController().getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalNumberLabel().setText(tasteIndexController.getTotalNumberLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalPriceLabel().setText(tasteIndexController.getTotalPriceLabel().getText());
                }
                if (!codeLabel.getText().equals("0007")) {
                    return;
                }
                FlowPane cateFlowPane = (FlowPane) ((FlowPane) hBox.getChildren().get(2)).getChildren().get(0);
                cateFlowPane.getChildren().remove(0, cateFlowPane.getChildren().size());
                double categorysFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
                double categorysFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 8;
                Label catlabel = new Label();
                catlabel.setPrefWidth(categorysFlowPaneWidth);
                catlabel.setPrefHeight(categorysFlowPaneHeight / 10 + 10);
                catlabel.setText("食品口味");
                catlabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                cateFlowPane.getChildren().add(catlabel);
                itemsFlowPane.getChildren().remove(0, itemsFlowPane.getChildren().size());
                for (int k = 0; k < 20; k++) {
                    FlowPane itemTasteFlowPane = new FlowPane();
                    double itemTasteFlowPaneWidth = itemsFlowPaneWidth / 6;
                    double itemTasteFlowPaneHeight = itemsFlowPaneHeight / 5;
                    itemTasteFlowPane.setPrefHeight(itemTasteFlowPaneHeight);
                    itemTasteFlowPane.setPrefWidth(itemTasteFlowPaneWidth);
                    Label itemTasteLabel = new Label();
                    itemTasteLabel.setText("加辣");
                    double itemTasteLabelWidth = itemTasteFlowPaneWidth;
                    double itemTasteLabelHeight = itemTasteFlowPaneHeight / 5 * 4;
                    itemTasteLabel.setPrefWidth(itemTasteLabelWidth);
                    itemTasteLabel.setPrefHeight(itemTasteLabelHeight);
                    itemTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");
                    itemTasteFlowPane.getChildren().add(itemTasteLabel);
                    FlowPane remarkTasteFlowPane = new FlowPane();
                    double remarkTasteFlowPaneWidth = itemTasteFlowPaneWidth;
                    double remarkTasteFlowPaneHeight = itemTasteFlowPaneHeight / 5;
                    remarkTasteFlowPane.setPrefWidth(remarkTasteFlowPaneWidth);
                    remarkTasteFlowPane.setPrefHeight(remarkTasteFlowPaneHeight);
                    Label codeTasteLabel = new Label();
                    codeTasteLabel.setText("01");
                    codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: lightsalmon  ");
                    double codeTasteLabelWidth = itemTasteLabelWidth / 2;
                    double codeTasteLabelHeight = itemTasteFlowPaneHeight / 5;
                    codeTasteLabel.setPrefWidth(codeTasteLabelWidth);
                    codeTasteLabel.setPrefHeight(codeTasteLabelHeight);
                    remarkTasteFlowPane.getChildren().add(codeTasteLabel);
                    Label priceTasteLabel = new Label();
                    priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
                    priceTasteLabel.setText("+1.00");
                    double priceTasteLabelWidth = itemTasteLabelWidth / 2;
                    double priceTasteLabelHeight = itemTasteFlowPaneHeight / 5;
                    priceTasteLabel.setPrefWidth(priceTasteLabelWidth);
                    priceTasteLabel.setPrefHeight(priceTasteLabelHeight);
                    remarkTasteFlowPane.getChildren().add(priceTasteLabel);
                    itemTasteFlowPane.getChildren().add(remarkTasteFlowPane);
                    itemTasteFlowPane.setOnMousePressed(event1 -> {
                        FXMLLoader confirmTasteloader = new FXMLLoader();
                        confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmTasteView.fxml"));
                        try {
                            FlowPane confirmTasteFlowPane = confirmTasteloader.load();
                            double confirmTasteWidth = primaryScreenBounds.getWidth() / 2;
                            double confirmTasteHeight = primaryScreenBounds.getHeight() / 3;
                            confirmTasteFlowPane.setPrefHeight(confirmTasteHeight);
                            confirmTasteFlowPane.setPrefWidth(confirmTasteWidth);
                            Label label = (Label) confirmTasteFlowPane.getChildren().get(0);
                            label.setText("確定要暫停[" + itemTasteLabel.getText() + "]?");
                            double labelWidth = confirmTasteWidth;
                            double labelHeight = confirmTasteHeight / 3 * 2;
                            label.setPrefWidth(labelWidth);
                            label.setPrefHeight(labelHeight);
                            FlowPane buttomFlowPane = (FlowPane) confirmTasteFlowPane.getChildren().get(1);
                            double buttomFlowPaneWidth = confirmTasteWidth;
                            double buttomFlowPaneHeight = confirmTasteHeight / 3;
                            buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                            buttomFlowPane.setPrefWidth(confirmTasteWidth);
                            buttomFlowPane.setHgap(buttomFlowPaneWidth / 20);
                            Stage confirmStage = new Stage();
                            Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                            noButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                            noButton.setPrefWidth(buttomFlowPaneWidth / 3);
                            noButton.setOnAction(event2 -> {
                                confirmStage.close();
                            });
                            Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                            yesButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                            yesButton.setPrefWidth(buttomFlowPaneWidth / 3);
                            yesButton.setOnAction(event2 -> {
                                codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                priceTasteLabel.setText("暫停");
                                priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                confirmStage.close();

                            });
                            confirmStage.setScene(new Scene(confirmTasteFlowPane));
                            confirmStage.initModality(Modality.NONE);
                            confirmStage.initStyle(StageStyle.TRANSPARENT);
                            confirmStage.initOwner(tasteIndexController.getStage());
                            confirmStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    itemsFlowPane.getChildren().add(itemTasteFlowPane);
                }
            });
        }


        FlowPane goodsButtomFlowPane = (FlowPane) goodsFlowPane.getChildren().get(1);
        double goodsButtomFlowPaneWidth = primaryScreenBounds.getWidth() / 10 * 5;
        double goodsButtomFlowPaneHeight = primaryScreenBounds.getHeight() / 10;
        goodsButtomFlowPane.setHgap(5);
        goodsButtomFlowPane.setPadding(new Insets(0, 0, 0, 10));
        goodsButtomFlowPane.setPrefWidth(goodsButtomFlowPaneWidth);
        goodsButtomFlowPane.setPrefHeight(goodsButtomFlowPaneHeight);
        tasteIndexController.getCancleButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        tasteIndexController.getCancleButton().setPrefHeight(goodsButtomFlowPaneHeight);
        tasteIndexController.getPreviousGoodsButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        tasteIndexController.getPreviousGoodsButton().setPrefHeight(goodsButtomFlowPaneHeight);
        tasteIndexController.getNextGoodsButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        tasteIndexController.getNextGoodsButton().setPrefHeight(goodsButtomFlowPaneHeight);


        //口味暫停主頁右側大類
        FlowPane categoryFlowPane = (FlowPane) hBox.getChildren().get(2);
        double categoryFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categoryFlowPaneHeight = primaryScreenBounds.getHeight();
        categoryFlowPane.setPrefWidth(categoryFlowPaneWidth);
        categoryFlowPane.setPrefHeight(categoryFlowPaneHeight);

        FlowPane categorysFlowPane = (FlowPane) categoryFlowPane.getChildren().get(0);
        double categorysFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categorysFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 8;
        categorysFlowPane.setPrefHeight(categorysFlowPaneHeight);
        categorysFlowPane.setPrefWidth(categorysFlowPaneWidth);
        for (int i = 0; i < 8; i++) {
            Label label = new Label();
            label.setPrefWidth(categorysFlowPaneWidth);
            label.setPrefHeight(categorysFlowPaneHeight / 10 + 10);
            label.setText("蔬菜類");
            label.setOnMouseClicked(event -> {
                Integer index = categorysFlowPane.getChildren().indexOf(label);
                if (index != -1) {
                    for (Node otherLabel : categorysFlowPane.getChildren()) {
                        otherLabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                    }
                    label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                }
                itemsFlowPane.getChildren().remove(0, itemsFlowPane.getChildren().size());
                refreshItems(itemsFlowPane, itemsFlowPaneWidth, itemsFlowPaneHeight, tasteIndexController, hBox);
            });
            if (i == 0) {
                label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
            } else {
                label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
            }
            categorysFlowPane.getChildren().add(label);
        }


        FlowPane categoryButtomFlowPane = (FlowPane) categoryFlowPane.getChildren().get(1);
        double categoryButtomPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categoryButtomPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        categoryButtomFlowPane.setVgap(10);
        categoryButtomFlowPane.setPrefHeight(categoryButtomPaneHeight);
        categoryButtomFlowPane.setPrefWidth(categoryButtomPaneWidth);
        tasteIndexController.getTasteButton().setPrefWidth(primaryScreenBounds.getWidth() / 10);
        tasteIndexController.getTasteButton().setPrefHeight(categoryButtomPaneHeight / 2 - 10);
        FlowPane categoryPageFlowPane = (FlowPane) categoryButtomFlowPane.getChildren().get(1);
        double categoryPageFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categoryPageFlowPaneHeight = categoryButtomPaneHeight / 2;
        categoryPageFlowPane.setPrefHeight(categoryPageFlowPaneHeight);
        categoryPageFlowPane.setPrefWidth(categoryPageFlowPaneWidth);
        categoryPageFlowPane.setHgap(1);
        tasteIndexController.getPreviousCategoryButton().setPrefWidth(categoryButtomPaneWidth / 2 - 1);
        tasteIndexController.getPreviousCategoryButton().setPrefHeight(categoryButtomPaneHeight / 2);
        tasteIndexController.getNextCategoryButton().setPrefWidth(categoryButtomPaneWidth / 2 - 1);
        tasteIndexController.getNextCategoryButton().setPrefHeight(categoryButtomPaneHeight / 2);
        Stage stage = new Stage();
        stage.setScene(new Scene(hBox));
        stage.setIconified(false);
        //stage.setFullScreen(true);
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.TRANSPARENT);
        tasteIndexController.setStage(stage);
        tasteIndexController.setItemsFlowPane(itemsFlowPane);
        tasteIndexController.setCategorysFlowPane(categorysFlowPane);
        tasteIndexController.setHbox(hBox);
        stage.show();
    }


    //暫停食品
    @FXML
    public void stopfoodsView() {
        Main.showInitialView(ItemModifyView.class);

        ItemModifyController itemModifyController = (ItemModifyController) itemModifyView.getPresenter();
        itemModifyController.initData();
        itemModifyView.showViewAndWait(Modality.APPLICATION_MODAL);

    }


    //修改扣燉
    @FXML
    public void modifyBuckleStew() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/BuckleStewIndex.fxml"));
        HBox hBox = loader.load();
        BuckleStewIndexController buckleStewIndexController = loader.getController();
        double buckleStewIndexWidth = primaryScreenBounds.getWidth();
        double buckleStewIndexHeight = primaryScreenBounds.getHeight();
        hBox.setPrefWidth(buckleStewIndexWidth);
        hBox.setPrefHeight(buckleStewIndexHeight);
        //食品暫停主頁左側顯示表格
        double tableViewWidth = buckleStewIndexWidth / 10 * 4;
        double tableViewHeight = buckleStewIndexHeight / 10 * 9;
        FlowPane tableFlowPane = (FlowPane) hBox.getChildren().get(0);
        tableFlowPane.setPrefWidth(tableViewWidth);
        tableFlowPane.setPrefHeight(primaryScreenBounds.getHeight());
        buckleStewIndexController.getFoodTableView().setPrefHeight(tableViewHeight);
        buckleStewIndexController.getFoodTableView().setPrefWidth(primaryScreenBounds.getWidth() / 10 * 4);
        buckleStewIndexController.getNumberColum().setPrefWidth(tableViewWidth / 10);
        buckleStewIndexController.getGoodNameColum().setPrefWidth(tableViewWidth / 10 * 6);
        buckleStewIndexController.getPriceColum().setPrefWidth(tableViewWidth / 10 * 2);
        buckleStewIndexController.getMessageColum().setPrefWidth(tableViewWidth / 10);
    /*    FlowPane tableFlowPane = (FlowPane) tableVBox.getChildren().get(0);
        tableFlowPane.setPrefHeight(tasteIndexHeight/10*9);
        tableFlowPane.setPrefWidth(tableVboxWidth);*/


        FlowPane tableButtonFlowPane = (FlowPane) tableFlowPane.getChildren().get(1);
        double tableButtonFlowPaneWidth = buckleStewIndexWidth / 10 * 4;
        double tableButtonFlowPaneHeight = buckleStewIndexHeight / 10;
        tableButtonFlowPane.setPrefHeight(tableButtonFlowPaneHeight);
        tableButtonFlowPane.setPrefWidth(tableButtonFlowPaneWidth);
        double tableLabelWidth = tableViewWidth / 5;
        double tableLabelHeight = buckleStewIndexHeight / 10;
        buckleStewIndexController.getTableNoLabel().setPrefHeight(tableLabelHeight);
        buckleStewIndexController.getTableNoLabel().setPrefWidth(tableLabelWidth);
        buckleStewIndexController.getTableNoLabel().setText("55桌");
        buckleStewIndexController.getTotalNumberLabel().setPrefHeight(tableLabelHeight);
        buckleStewIndexController.getTotalNumberLabel().setPrefWidth(tableLabelWidth);
        buckleStewIndexController.getTotalNumberLabel().setText("0/0");
        buckleStewIndexController.getTotalPriceLabel().setPrefHeight(tableLabelHeight);
        buckleStewIndexController.getTotalPriceLabel().setPrefWidth(tableLabelWidth);
        buckleStewIndexController.getTotalPriceLabel().setText("0.00");
        double tableButtonlWidth = tableViewWidth / 5;
        double tableButtonlHeight = buckleStewIndexHeight / 10;
        buckleStewIndexController.getClearButton().setPrefHeight(tableButtonlHeight);
        buckleStewIndexController.getClearButton().setPrefWidth(tableButtonlWidth);

        //口味暫停主頁中間食品，口味顯示部分
        FlowPane goodsFlowPane = (FlowPane) hBox.getChildren().get(1);
        double goodsFlowPaneWidth = primaryScreenBounds.getWidth() / 10 * 5;
        double goodsFlowPaneHeight = primaryScreenBounds.getHeight();
        goodsFlowPane.setPrefWidth(goodsFlowPaneWidth);
        goodsFlowPane.setPrefHeight(goodsFlowPaneHeight);

        FlowPane itemsFlowPane = (FlowPane) goodsFlowPane.getChildren().get(0);
        double itemsFlowPaneWidth = goodsFlowPaneWidth;
        double itemsFlowPaneHeight = goodsFlowPaneHeight / 10 * 9;
        itemsFlowPane.setPrefWidth(itemsFlowPaneWidth);
        itemsFlowPane.setPrefHeight(itemsFlowPaneHeight);
        itemsFlowPane.setHgap(itemsFlowPaneWidth / 60);
        itemsFlowPane.setVgap(itemsFlowPaneHeight / 80);
        itemsFlowPane.setPadding(new Insets(itemsFlowPaneHeight / 80, 0, 0, itemsFlowPaneWidth / 45));

        for (int i = 0; i < 20; i++) {
            FlowPane itemFlowPane = new FlowPane();
            double itemFlowPaneWidth = itemsFlowPaneWidth / 6;
            double itemFlowPaneHeight = itemsFlowPaneHeight / 5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight / 5 * 3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg", itemImageWidth, itemImageHeight, true, true);
            javafx.scene.image.ImageView itemImageView = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane = new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight / 5 * 2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight / 5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("半隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth / 2;
            double codeLabelHeight = itemFlowPaneHeight / 5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth / 2;
            double priceLabelHeight = itemFlowPaneHeight / 5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                FXMLLoader confirmTasteloader = new FXMLLoader();
                confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmBuckleStewView.fxml"));
                try {
                    FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                    double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                    double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                    confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                    confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                    Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                    label.setText("此食品沒有限售數量，是否輸入?");
                    double labelWidth = confirmFoodWidth;
                    double labelHeight = confirmFoodHeight / 3 * 2;
                    label.setPrefWidth(labelWidth);
                    label.setPrefHeight(labelHeight);
                    FlowPane buttomFlowPane = (FlowPane) confirmFoodFlowPane.getChildren().get(1);
                    double buttomFlowPaneWidth = confirmFoodWidth;
                    double buttomFlowPaneHeight = confirmFoodHeight / 3;
                    buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                    buttomFlowPane.setPrefWidth(confirmFoodWidth);
                    buttomFlowPane.setHgap(buttomFlowPaneWidth / 20);
                    Stage confirmStage = new Stage();
                    Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                    noButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    noButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    noButton.setOnAction(event2 -> {
                        confirmStage.close();
                    });
                    Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                    yesButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    yesButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    yesButton.setOnAction(event2 -> {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Main.class.getResource("controller/view/ModifyBuckleStewView.fxml"));
                        try {
                            VBox searchBox = fxmlLoader.load();
                            ModifyBuckleStewController modifyBuckleStewController = fxmlLoader.getController();
                            searchBox.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2);
                            searchBox.setPrefWidth(primaryScreenBounds.getWidth());
                            //獲取搜索欄
                            FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
                            searchFlowPane.setHgap(primaryScreenBounds.getWidth() / 8 / 4);
                            searchFlowPane.setPadding(new Insets(0, primaryScreenBounds.getWidth() / 8 / 8, 0, primaryScreenBounds.getWidth() / 8 / 8));
                            //取台號搜索窗口的1/5
                            searchFlowPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5);
                            searchFlowPane.setPrefWidth(primaryScreenBounds.getWidth());

                            Label titleLabel = (Label) searchFlowPane.getChildren().get(0);
                            titleLabel.setPrefWidth(primaryScreenBounds.getWidth() / 8 * 2);
                            titleLabel.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            //titleLabel.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                            //搜索欄
                            TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
                            searchTextField.setPrefWidth(primaryScreenBounds.getWidth() / 8 * 4);
                            searchTextField.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            searchTextField.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder");
                            searchTextField.setPromptText("請輸入數量");

                            //去除按鈕
                            Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
                            deleteButton.setPrefWidth(primaryScreenBounds.getWidth() / 8);
                            deleteButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            deleteButton.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                            //中间的键盘部分
                            HBox keyBox = (HBox) searchBox.getChildren().get(1);
                            keyBox.setPrefWidth(primaryScreenBounds.getWidth());
                            keyBox.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            //英文键盘部分
                            FlowPane englishKeyPane = (FlowPane) keyBox.getChildren().get(0);
                            englishKeyPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            englishKeyPane.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);

                            //第一行字母
                            FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
                            englishKeyPane1.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane1.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane1.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane1.setHgap(5);
                            for (int j = 0; j < 10; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("Q");
                                        break;
                                    case 1:
                                        englishButton.setText("W");
                                        break;
                                    case 2:
                                        englishButton.setText("E");
                                        break;
                                    case 3:
                                        englishButton.setText("R");
                                        break;
                                    case 4:
                                        englishButton.setText("T");
                                        break;
                                    case 5:
                                        englishButton.setText("Y");
                                        break;
                                    case 6:
                                        englishButton.setText("U");
                                        break;
                                    case 7:
                                        englishButton.setText("I");
                                        break;
                                    case 8:
                                        englishButton.setText("O");
                                        break;
                                    case 9:
                                        englishButton.setText("P");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane1.getChildren().add(englishButton);
                            }
                            //第二行字母
                            FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
                            englishKeyPane2.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane2.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane2.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane2.setHgap(5);
                            for (int j = 0; j < 9; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("A");
                                        break;
                                    case 1:
                                        englishButton.setText("S");
                                        break;
                                    case 2:
                                        englishButton.setText("D");
                                        break;
                                    case 3:
                                        englishButton.setText("F");
                                        break;
                                    case 4:
                                        englishButton.setText("G");
                                        break;
                                    case 5:
                                        englishButton.setText("H");
                                        break;
                                    case 6:
                                        englishButton.setText("J");
                                        break;
                                    case 7:
                                        englishButton.setText("K");
                                        break;
                                    case 8:
                                        englishButton.setText("L");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane2.getChildren().add(englishButton);
                            }
                            //第三行字母
                            FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
                            englishKeyPane3.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane3.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane3.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane3.setHgap(5);
                            for (int j = 0; j < 8; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("Z");
                                        break;
                                    case 1:
                                        englishButton.setText("X");
                                        break;
                                    case 2:
                                        englishButton.setText("C");
                                        break;
                                    case 3:
                                        englishButton.setText("V");
                                        break;
                                    case 4:
                                        englishButton.setText("B");
                                        break;
                                    case 5:
                                        englishButton.setText("N");
                                        break;
                                    case 6:
                                        englishButton.setText("M");
                                        break;
                                    case 7:
                                        englishButton.setText("-");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane3.getChildren().add(englishButton);
                            }

                            //获取右边的数字键盘
                            //英文键盘部分
                            FlowPane numberKeyPane = (FlowPane) keyBox.getChildren().get(1);
                            numberKeyPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            numberKeyPane.setPrefWidth(primaryScreenBounds.getWidth() / 4);
                            numberKeyPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center");
                            numberKeyPane.setHgap(5);
                            numberKeyPane.setVgap(10);
                            for (int k = 0; k < 12; k++) {
                                Button numberButton = new Button();
                                switch (k) {
                                    case 0:
                                        numberButton.setText("7");
                                        break;
                                    case 1:
                                        numberButton.setText("8");
                                        break;
                                    case 2:
                                        numberButton.setText("9");
                                        break;
                                    case 3:
                                        numberButton.setText("4");
                                        break;
                                    case 4:
                                        numberButton.setText("5");
                                        break;
                                    case 5:
                                        numberButton.setText("6");
                                        break;
                                    case 6:
                                        numberButton.setText("1");
                                        break;
                                    case 7:
                                        numberButton.setText("2");
                                        break;
                                    case 8:
                                        numberButton.setText("3");
                                        break;
                                    case 9:
                                        numberButton.setText("0");
                                        break;
                                    case 10:
                                        numberButton.setText(".");
                                        break;
                                    case 11:
                                        numberButton.setText("/");
                                        break;
                                }
                                numberButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 / 3 - 15);
                                numberButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 4 * 0.85);
                                numberButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(numberButton.getText());
                                });
                                numberButton.setStyle("-fx-background-color: #e86238;-fx-font-size: 30;-fx-text-fill: white");
                                numberKeyPane.getChildren().add(numberButton);
                            }

                            //获取底部
                            FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
                            buttonFlowPane.setHgap(primaryScreenBounds.getWidth() / 8);
                            buttonFlowPane.setPrefWidth(primaryScreenBounds.getWidth());
                            buttonFlowPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5);
                            buttonFlowPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center; ");
                            Button closeButton = new Button();
                            closeButton.setPrefWidth(primaryScreenBounds.getWidth() / 3);
                            closeButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                            closeButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #aa5b5b");
                            closeButton.setText("關閉");
                            buttonFlowPane.getChildren().add(closeButton);
                            Button confirmButton = new Button();
                            confirmButton.setPrefWidth(primaryScreenBounds.getWidth() / 3);
                            confirmButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                            confirmButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #5caa88");
                            confirmButton.setText("確定");
                            buttonFlowPane.getChildren().add(confirmButton);
                            Stage modifyBuckleStewStage = new Stage();
                            modifyBuckleStewStage.initOwner(buckleStewIndexController.getStage());
                            modifyBuckleStewStage.setScene(new Scene(searchBox));
                            modifyBuckleStewStage.initModality(Modality.APPLICATION_MODAL);
                            modifyBuckleStewStage.initStyle(StageStyle.TRANSPARENT);
                            modifyBuckleStewStage.show();
                            confirmButton.setOnAction(even -> {
                                try {
                                    BigDecimal bigDecimal = new BigDecimal(modifyBuckleStewController.getTableNumberTextField().getText()).setScale(1, BigDecimal.ROUND_DOWN);
                                    String code = codeLabel.getText();
                                    ObservableList<Node> nodes = itemsFlowPane.getChildren();
                                    if (nodes != null && nodes.size() > 0) {
                                        for (Node node : nodes) {
                                            FlowPane flowPane = (FlowPane) node;
                                            FlowPane remarksFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                            Label codesLabel = (Label) remarksFlowPane.getChildren().get(0);
                                            if (codesLabel.getText().equals(code)) {
                                                Label pricesLabel = (Label) remarksFlowPane.getChildren().get(1);
                                                pricesLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                                pricesLabel.setText(bigDecimal.toString());
                                                codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                            }
                                        }
                                    }
                                    modifyBuckleStewStage.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            closeButton.setOnAction(event1 -> {
                                modifyBuckleStewStage.close();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        confirmStage.close();
                    });
                    confirmStage.setScene(new Scene(confirmFoodFlowPane));
                    confirmStage.initModality(Modality.APPLICATION_MODAL);
                    confirmStage.initStyle(StageStyle.TRANSPARENT);
                    confirmStage.initOwner(buckleStewIndexController.getStage());
                    confirmStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
        FlowPane goodsButtomFlowPane = (FlowPane) goodsFlowPane.getChildren().get(1);
        double goodsButtomFlowPaneWidth = primaryScreenBounds.getWidth() / 10 * 5;
        double goodsButtomFlowPaneHeight = primaryScreenBounds.getHeight() / 10;
        goodsButtomFlowPane.setHgap(5);
        goodsButtomFlowPane.setPadding(new Insets(0, 0, 0, 10));
        goodsButtomFlowPane.setPrefWidth(goodsButtomFlowPaneWidth);
        goodsButtomFlowPane.setPrefHeight(goodsButtomFlowPaneHeight);
        buckleStewIndexController.getCancleButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        buckleStewIndexController.getCancleButton().setPrefHeight(goodsButtomFlowPaneHeight);
        buckleStewIndexController.getPreviousGoodsButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        buckleStewIndexController.getPreviousGoodsButton().setPrefHeight(goodsButtomFlowPaneHeight);
        buckleStewIndexController.getNextGoodsButton().setPrefWidth(goodsButtomFlowPaneWidth / 3 - 10);
        buckleStewIndexController.getNextGoodsButton().setPrefHeight(goodsButtomFlowPaneHeight);

        //食品暫停主頁右側大類
        FlowPane categoryFlowPane = (FlowPane) hBox.getChildren().get(2);
        double categoryFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categoryFlowPaneHeight = primaryScreenBounds.getHeight();
        categoryFlowPane.setPrefWidth(categoryFlowPaneWidth);
        categoryFlowPane.setPrefHeight(categoryFlowPaneHeight);

        FlowPane categorysFlowPane = (FlowPane) categoryFlowPane.getChildren().get(0);
        double categorysFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categorysFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 8;
        categorysFlowPane.setPrefHeight(categorysFlowPaneHeight);
        categorysFlowPane.setPrefWidth(categorysFlowPaneWidth);
        for (int k = 0; k < 8; k++) {
            Label label = new Label();
            label.setPrefWidth(categorysFlowPaneWidth);
            label.setPrefHeight(categorysFlowPaneHeight / 10 + 10);
            label.setText("蔬菜類");
            if (k == 0) {
                label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
            } else {
                label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
            }
            label.setOnMouseClicked(event -> {
                Integer index = categorysFlowPane.getChildren().indexOf(label);
                if (index != -1) {
                    for (Node otherLabel : categorysFlowPane.getChildren()) {
                        otherLabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                    }
                    label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                }
                itemsFlowPane.getChildren().remove(0, itemsFlowPane.getChildren().size());
                refreshItems(itemsFlowPane, itemsFlowPaneWidth, itemsFlowPaneHeight, buckleStewIndexController, hBox);
            });
            categorysFlowPane.getChildren().add(label);
        }

        FlowPane categoryButtomFlowPane = (FlowPane) categoryFlowPane.getChildren().get(1);
        double categoryButtomPaneWidth = primaryScreenBounds.getWidth() / 10;
        double categoryButtomPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        categoryButtomFlowPane.setPadding(new Insets(categoryButtomPaneHeight / 2, 0, 0, 0));
        categoryButtomFlowPane.setPrefHeight(categoryButtomPaneHeight);
        categoryButtomFlowPane.setPrefWidth(categoryButtomPaneWidth);
        categoryButtomFlowPane.setHgap(1);
        buckleStewIndexController.getPreviousCategoryButton().setPrefWidth(categoryButtomPaneWidth / 2 - 1);
        buckleStewIndexController.getPreviousCategoryButton().setPrefHeight(categoryButtomPaneHeight / 2);
        buckleStewIndexController.getNextCategoryButton().setPrefWidth(categoryButtomPaneWidth / 2 - 1);
        buckleStewIndexController.getNextCategoryButton().setPrefHeight(categoryButtomPaneHeight / 2);
        Stage stage = new Stage();
        stage.setScene(new Scene(hBox));
        stage.setIconified(false);
        //stage.setFullScreen(true);
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.TRANSPARENT);
        buckleStewIndexController.setStage(stage);
        buckleStewIndexController.setPrimaryScreenBounds(primaryScreenBounds);
        stage.show();
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void refreshItems(FlowPane itemsFlowPane, double itemsFlowPaneWidth, double itemsFlowPaneHeight, TasteIndexController tasteIndexController, HBox hBox) {
        for (int i = 0; i < 20; i++) {
            FlowPane itemFlowPane = new FlowPane();
            double itemFlowPaneWidth = itemsFlowPaneWidth / 6;
            double itemFlowPaneHeight = itemsFlowPaneHeight / 5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight / 5 * 3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg", itemImageWidth, itemImageHeight, true, true);
            javafx.scene.image.ImageView itemImageView = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);

   /*         itemLabel.setText("半隻貴妃雞仔蛋");

            itemLabel.setPrefWidth(itemFlowPaneWidth);
            itemLabel.setPrefHeight(itemLabelHeight);
            itemLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");*/
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane = new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight / 5 * 2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight / 5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("全只隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth / 2;
            double codeLabelHeight = itemFlowPaneHeight / 5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth / 2;
            double priceLabelHeight = itemFlowPaneHeight / 5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                boolean isExist = false;
                Integer selectedIndex = -1;
                ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
                for (int k = 0; k < tasteIndexController.getFoodTableView().getItems().size(); k++) {
                    if (goods.get(k).getGoodName().equals(codeLabel.getText() + "半隻貴妃雞仔蛋")) {
                        isExist = true;
                        selectedIndex = k;
                    }
                }
                if (!isExist) {
                    goods.add(new Goods("1", codeLabel.getText() + "半隻貴妃雞仔蛋", "35.00", ""));
                    tasteIndexController.getFoodTableView().getSelectionModel().selectLast();
                } else {
                    if (selectedIndex != -1) {
                        tasteIndexController.getFoodTableView().getItems().get(selectedIndex).setNumber((Integer.parseInt(tasteIndexController.getFoodTableView().getItems().get(selectedIndex).getNumber()) + 1) + "");

                    }
                }
                Integer totalNumber = 0;
                Double totalPrice = 0.00d;
                for (Goods good : goods) {
                    totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                    totalPrice = totalPrice + Integer.parseInt(good.getNumber()) * Double.parseDouble(good.getPrice());
                }
                tasteIndexController.getTotalNumberLabel().setText(goods.size() + "/" + totalNumber);
                tasteIndexController.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
               /* tasteIndexController.getFoodTableView().getItems().add(new Goods("1", itemLabel.getText(),priceLabel.getText(),""));
                tasteIndexController.getFoodTableView().getSelectionModel().selectLast();*/
                if (tasteIndexController.getSearchFoodController() != null) {
                    tasteIndexController.getSearchFoodController().getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalNumberLabel().setText(tasteIndexController.getTotalNumberLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalPriceLabel().setText(tasteIndexController.getTotalPriceLabel().getText());
                }
                if (!codeLabel.getText().equals("0007")) {
                    return;
                }
                FlowPane cateFlowPane = (FlowPane) ((FlowPane) hBox.getChildren().get(2)).getChildren().get(0);
                cateFlowPane.getChildren().remove(0, cateFlowPane.getChildren().size());
                double categorysFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
                double categorysFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 8;
                Label catlabel = new Label();
                catlabel.setPrefWidth(categorysFlowPaneWidth);
                catlabel.setPrefHeight(categorysFlowPaneHeight / 10 + 10);
                catlabel.setText("食品口味");
                catlabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                cateFlowPane.getChildren().add(catlabel);
                itemsFlowPane.getChildren().remove(0, itemsFlowPane.getChildren().size());
                for (int k = 0; k < 20; k++) {
                    FlowPane itemTasteFlowPane = new FlowPane();
                    double itemTasteFlowPaneWidth = itemsFlowPaneWidth / 6;
                    double itemTasteFlowPaneHeight = itemsFlowPaneHeight / 5;
                    itemTasteFlowPane.setPrefHeight(itemTasteFlowPaneHeight);
                    itemTasteFlowPane.setPrefWidth(itemTasteFlowPaneWidth);
                    Label itemTasteLabel = new Label();
                    itemTasteLabel.setText("加辣");
                    double itemTasteLabelWidth = itemTasteFlowPaneWidth;
                    double itemTasteLabelHeight = itemTasteFlowPaneHeight / 5 * 4;
                    itemTasteLabel.setPrefWidth(itemTasteLabelWidth);
                    itemTasteLabel.setPrefHeight(itemTasteLabelHeight);
                    itemTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");
                    itemTasteFlowPane.getChildren().add(itemTasteLabel);
                    FlowPane remarkTasteFlowPane = new FlowPane();
                    double remarkTasteFlowPaneWidth = itemTasteFlowPaneWidth;
                    double remarkTasteFlowPaneHeight = itemTasteFlowPaneHeight / 5;
                    remarkTasteFlowPane.setPrefWidth(remarkTasteFlowPaneWidth);
                    remarkTasteFlowPane.setPrefHeight(remarkTasteFlowPaneHeight);
                    Label codeTasteLabel = new Label();
                    codeTasteLabel.setText("01");
                    codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: lightsalmon  ");
                    double codeTasteLabelWidth = itemTasteLabelWidth / 2;
                    double codeTasteLabelHeight = itemTasteFlowPaneHeight / 5;
                    codeTasteLabel.setPrefWidth(codeTasteLabelWidth);
                    codeTasteLabel.setPrefHeight(codeTasteLabelHeight);
                    remarkTasteFlowPane.getChildren().add(codeTasteLabel);
                    Label priceTasteLabel = new Label();
                    priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
                    priceTasteLabel.setText("+1.00");
                    double priceTasteLabelWidth = itemTasteLabelWidth / 2;
                    double priceTasteLabelHeight = itemTasteFlowPaneHeight / 5;
                    priceTasteLabel.setPrefWidth(priceTasteLabelWidth);
                    priceTasteLabel.setPrefHeight(priceTasteLabelHeight);
                    remarkTasteFlowPane.getChildren().add(priceTasteLabel);
                    itemTasteFlowPane.getChildren().add(remarkTasteFlowPane);
                    itemTasteFlowPane.setOnMousePressed(event1 -> {
                        FXMLLoader confirmTasteloader = new FXMLLoader();
                        confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmTasteView.fxml"));
                        try {
                            FlowPane confirmTasteFlowPane = confirmTasteloader.load();
                            double confirmTasteWidth = primaryScreenBounds.getWidth() / 2;
                            double confirmTasteHeight = primaryScreenBounds.getHeight() / 3;
                            confirmTasteFlowPane.setPrefHeight(confirmTasteHeight);
                            confirmTasteFlowPane.setPrefWidth(confirmTasteWidth);
                            Label label = (Label) confirmTasteFlowPane.getChildren().get(0);
                            label.setText("確定要暫停[" + itemTasteLabel.getText() + "]?");
                            double labelWidth = confirmTasteWidth;
                            double labelHeight = confirmTasteHeight / 3 * 2;
                            label.setPrefWidth(labelWidth);
                            label.setPrefHeight(labelHeight);
                            FlowPane buttomFlowPane = (FlowPane) confirmTasteFlowPane.getChildren().get(1);
                            double buttomFlowPaneWidth = confirmTasteWidth;
                            double buttomFlowPaneHeight = confirmTasteHeight / 3;
                            buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                            buttomFlowPane.setPrefWidth(confirmTasteWidth);
                            buttomFlowPane.setHgap(buttomFlowPaneWidth / 20);
                            Stage confirmStage = new Stage();
                            Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                            noButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                            noButton.setPrefWidth(buttomFlowPaneWidth / 3);
                            noButton.setOnAction(event2 -> {
                                confirmStage.close();
                            });
                            Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                            yesButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                            yesButton.setPrefWidth(buttomFlowPaneWidth / 3);
                            yesButton.setOnAction(event2 -> {
                                codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                priceTasteLabel.setText("暫停");
                                priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                confirmStage.close();

                            });
                            confirmStage.setScene(new Scene(confirmTasteFlowPane));
                            confirmStage.initModality(Modality.NONE);
                            confirmStage.initStyle(StageStyle.TRANSPARENT);
                            confirmStage.initOwner(tasteIndexController.getStage());
                            confirmStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    itemsFlowPane.getChildren().add(itemTasteFlowPane);
                }
            });
        }
    }

    public void refreshItems(FlowPane itemsFlowPane, double itemsFlowPaneWidth, double itemsFlowPaneHeight, FoodIndexController foodIndexController, HBox hBox) {
        for (int i = 0; i < 20; i++) {
            FlowPane itemFlowPane = new FlowPane();
            double itemFlowPaneWidth = itemsFlowPaneWidth / 6;
            double itemFlowPaneHeight = itemsFlowPaneHeight / 5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight / 5 * 3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg", itemImageWidth, itemImageHeight, true, true);
            javafx.scene.image.ImageView itemImageView = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);

   /*         itemLabel.setText("半隻貴妃雞仔蛋");

            itemLabel.setPrefWidth(itemFlowPaneWidth);
            itemLabel.setPrefHeight(itemLabelHeight);
            itemLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");*/
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane = new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight / 5 * 2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight / 5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("全只隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth / 2;
            double codeLabelHeight = itemFlowPaneHeight / 5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth / 2;
            double priceLabelHeight = itemFlowPaneHeight / 5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                if (!codeLabel.getText().equals("0007")) {
                    return;
                }
                FXMLLoader confirmTasteloader = new FXMLLoader();
                confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmFoodView.fxml"));
                try {
                    FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                    double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                    double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                    confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                    confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                    Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                    label.setText("確定要暫停[" + itemNameLabel.getText() + "]?");
                    double labelWidth = confirmFoodWidth;
                    double labelHeight = confirmFoodHeight / 3 * 2;
                    label.setPrefWidth(labelWidth);
                    label.setPrefHeight(labelHeight);
                    FlowPane buttomFlowPane = (FlowPane) confirmFoodFlowPane.getChildren().get(1);
                    double buttomFlowPaneWidth = confirmFoodWidth;
                    double buttomFlowPaneHeight = confirmFoodHeight / 3;
                    buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                    buttomFlowPane.setPrefWidth(confirmFoodWidth);
                    buttomFlowPane.setHgap(buttomFlowPaneWidth / 20);
                    Stage confirmStage = new Stage();
                    Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                    noButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    noButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    noButton.setOnAction(event2 -> {
                        confirmStage.close();
                    });
                    Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                    yesButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    yesButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    yesButton.setOnAction(event2 -> {
                        codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                        priceLabel.setText("暫停");
                        priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                        confirmStage.close();

                    });
                    confirmStage.setScene(new Scene(confirmFoodFlowPane));
                    confirmStage.initModality(Modality.NONE);
                    confirmStage.initStyle(StageStyle.TRANSPARENT);
                    confirmStage.initOwner(foodIndexController.getStage());
                    confirmStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void refreshItems(FlowPane itemsFlowPane, double itemsFlowPaneWidth, double itemsFlowPaneHeight, BuckleStewIndexController buckleStewIndexController, HBox hBox) {
        for (int i = 0; i < 20; i++) {
            FlowPane itemFlowPane = new FlowPane();
            double itemFlowPaneWidth = itemsFlowPaneWidth / 6;
            double itemFlowPaneHeight = itemsFlowPaneHeight / 5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight / 5 * 3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg", itemImageWidth, itemImageHeight, true, true);
            javafx.scene.image.ImageView itemImageView = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane = new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight / 5 * 2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight / 5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("全隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth / 2;
            double codeLabelHeight = itemFlowPaneHeight / 5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth / 2;
            double priceLabelHeight = itemFlowPaneHeight / 5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                FXMLLoader confirmTasteloader = new FXMLLoader();
                confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmBuckleStewView.fxml"));
                try {
                    FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                    double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                    double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                    confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                    confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                    Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                    label.setText("此食品沒有限售數量，是否輸入?");
                    double labelWidth = confirmFoodWidth;
                    double labelHeight = confirmFoodHeight / 3 * 2;
                    label.setPrefWidth(labelWidth);
                    label.setPrefHeight(labelHeight);
                    FlowPane buttomFlowPane = (FlowPane) confirmFoodFlowPane.getChildren().get(1);
                    double buttomFlowPaneWidth = confirmFoodWidth;
                    double buttomFlowPaneHeight = confirmFoodHeight / 3;
                    buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                    buttomFlowPane.setPrefWidth(confirmFoodWidth);
                    buttomFlowPane.setHgap(buttomFlowPaneWidth / 20);
                    Stage confirmStage = new Stage();
                    Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                    noButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    noButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    noButton.setOnAction(event2 -> {
                        confirmStage.close();
                    });
                    Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                    yesButton.setPrefHeight(buttomFlowPaneHeight / 3 * 2);
                    yesButton.setPrefWidth(buttomFlowPaneWidth / 3);
                    yesButton.setOnAction(event2 -> {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Main.class.getResource("controller/view/ModifyBuckleStewView.fxml"));
                        try {
                            VBox searchBox = fxmlLoader.load();
                            ModifyBuckleStewController modifyBuckleStewController = fxmlLoader.getController();
                            searchBox.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2);
                            searchBox.setPrefWidth(primaryScreenBounds.getWidth());
                            //獲取搜索欄
                            FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
                            searchFlowPane.setHgap(primaryScreenBounds.getWidth() / 8 / 4);
                            searchFlowPane.setPadding(new Insets(0, primaryScreenBounds.getWidth() / 8 / 8, 0, primaryScreenBounds.getWidth() / 8 / 8));
                            //取台號搜索窗口的1/5
                            searchFlowPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5);
                            searchFlowPane.setPrefWidth(primaryScreenBounds.getWidth());

                            Label titleLabel = (Label) searchFlowPane.getChildren().get(0);
                            titleLabel.setPrefWidth(primaryScreenBounds.getWidth() / 8 * 2);
                            titleLabel.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            //titleLabel.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                            //搜索欄
                            TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
                            searchTextField.setPrefWidth(primaryScreenBounds.getWidth() / 8 * 4);
                            searchTextField.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            searchTextField.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder");
                            searchTextField.setPromptText("請輸入數量");

                            //去除按鈕
                            Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
                            deleteButton.setPrefWidth(primaryScreenBounds.getWidth() / 8);
                            deleteButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                            deleteButton.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                            //中间的键盘部分
                            HBox keyBox = (HBox) searchBox.getChildren().get(1);
                            keyBox.setPrefWidth(primaryScreenBounds.getWidth());
                            keyBox.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            //英文键盘部分
                            FlowPane englishKeyPane = (FlowPane) keyBox.getChildren().get(0);
                            englishKeyPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            englishKeyPane.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);

                            //第一行字母
                            FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
                            englishKeyPane1.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane1.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane1.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane1.setHgap(5);
                            for (int j = 0; j < 10; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("Q");
                                        break;
                                    case 1:
                                        englishButton.setText("W");
                                        break;
                                    case 2:
                                        englishButton.setText("E");
                                        break;
                                    case 3:
                                        englishButton.setText("R");
                                        break;
                                    case 4:
                                        englishButton.setText("T");
                                        break;
                                    case 5:
                                        englishButton.setText("Y");
                                        break;
                                    case 6:
                                        englishButton.setText("U");
                                        break;
                                    case 7:
                                        englishButton.setText("I");
                                        break;
                                    case 8:
                                        englishButton.setText("O");
                                        break;
                                    case 9:
                                        englishButton.setText("P");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane1.getChildren().add(englishButton);
                            }
                            //第二行字母
                            FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
                            englishKeyPane2.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane2.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane2.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane2.setHgap(5);
                            for (int j = 0; j < 9; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("A");
                                        break;
                                    case 1:
                                        englishButton.setText("S");
                                        break;
                                    case 2:
                                        englishButton.setText("D");
                                        break;
                                    case 3:
                                        englishButton.setText("F");
                                        break;
                                    case 4:
                                        englishButton.setText("G");
                                        break;
                                    case 5:
                                        englishButton.setText("H");
                                        break;
                                    case 6:
                                        englishButton.setText("J");
                                        break;
                                    case 7:
                                        englishButton.setText("K");
                                        break;
                                    case 8:
                                        englishButton.setText("L");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane2.getChildren().add(englishButton);
                            }
                            //第三行字母
                            FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
                            englishKeyPane3.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3);
                            englishKeyPane3.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
                            englishKeyPane3.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                            englishKeyPane3.setHgap(5);
                            for (int j = 0; j < 8; j++) {
                                Button englishButton = new Button();
                                switch (j) {
                                    case 0:
                                        englishButton.setText("Z");
                                        break;
                                    case 1:
                                        englishButton.setText("X");
                                        break;
                                    case 2:
                                        englishButton.setText("C");
                                        break;
                                    case 3:
                                        englishButton.setText("V");
                                        break;
                                    case 4:
                                        englishButton.setText("B");
                                        break;
                                    case 5:
                                        englishButton.setText("N");
                                        break;
                                    case 6:
                                        englishButton.setText("M");
                                        break;
                                    case 7:
                                        englishButton.setText("-");
                                        break;
                                }
                                englishButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                                englishButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 * 3 / 11);
                                englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                englishButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(englishButton.getText());
                                });
                                englishKeyPane3.getChildren().add(englishButton);
                            }

                            //获取右边的数字键盘
                            //英文键盘部分
                            FlowPane numberKeyPane = (FlowPane) keyBox.getChildren().get(1);
                            numberKeyPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                            numberKeyPane.setPrefWidth(primaryScreenBounds.getWidth() / 4);
                            numberKeyPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center");
                            numberKeyPane.setHgap(5);
                            numberKeyPane.setVgap(10);
                            for (int k = 0; k < 12; k++) {
                                Button numberButton = new Button();
                                switch (k) {
                                    case 0:
                                        numberButton.setText("7");
                                        break;
                                    case 1:
                                        numberButton.setText("8");
                                        break;
                                    case 2:
                                        numberButton.setText("9");
                                        break;
                                    case 3:
                                        numberButton.setText("4");
                                        break;
                                    case 4:
                                        numberButton.setText("5");
                                        break;
                                    case 5:
                                        numberButton.setText("6");
                                        break;
                                    case 6:
                                        numberButton.setText("1");
                                        break;
                                    case 7:
                                        numberButton.setText("2");
                                        break;
                                    case 8:
                                        numberButton.setText("3");
                                        break;
                                    case 9:
                                        numberButton.setText("0");
                                        break;
                                    case 10:
                                        numberButton.setText(".");
                                        break;
                                    case 11:
                                        numberButton.setText("/");
                                        break;
                                }
                                numberButton.setPrefWidth(primaryScreenBounds.getWidth() / 4 / 3 - 15);
                                numberButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 4 * 0.85);
                                numberButton.setOnAction(event1 -> {
                                    modifyBuckleStewController.addTextField(numberButton.getText());
                                });
                                numberButton.setStyle("-fx-background-color: #e86238;-fx-font-size: 30;-fx-text-fill: white");
                                numberKeyPane.getChildren().add(numberButton);
                            }

                            //获取底部
                            FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
                            buttonFlowPane.setHgap(primaryScreenBounds.getWidth() / 8);
                            buttonFlowPane.setPrefWidth(primaryScreenBounds.getWidth());
                            buttonFlowPane.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5);
                            buttonFlowPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center; ");
                            Button closeButton = new Button();
                            closeButton.setPrefWidth(primaryScreenBounds.getWidth() / 3);
                            closeButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                            closeButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #aa5b5b");
                            closeButton.setText("關閉");
                            buttonFlowPane.getChildren().add(closeButton);
                            Button confirmButton = new Button();
                            confirmButton.setPrefWidth(primaryScreenBounds.getWidth() / 3);
                            confirmButton.setPrefHeight(primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                            confirmButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #5caa88");
                            confirmButton.setText("確定");
                            buttonFlowPane.getChildren().add(confirmButton);
                            Stage modifyBuckleStewStage = new Stage();
                            modifyBuckleStewStage.initOwner(buckleStewIndexController.getStage());
                            modifyBuckleStewStage.setScene(new Scene(searchBox));
                            modifyBuckleStewStage.initModality(Modality.APPLICATION_MODAL);
                            modifyBuckleStewStage.initStyle(StageStyle.TRANSPARENT);
                            modifyBuckleStewStage.show();
                            confirmButton.setOnAction(even -> {
                                try {
                                    BigDecimal bigDecimal = new BigDecimal(modifyBuckleStewController.getTableNumberTextField().getText()).setScale(1, BigDecimal.ROUND_DOWN);
                                    String code = codeLabel.getText();
                                    ObservableList<Node> nodes = itemsFlowPane.getChildren();
                                    if (nodes != null && nodes.size() > 0) {
                                        for (Node node : nodes) {
                                            FlowPane flowPane = (FlowPane) node;
                                            FlowPane remarksFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                            Label codesLabel = (Label) remarksFlowPane.getChildren().get(1);
                                            if (codesLabel.getText().equals(code)) {
                                                Label pricesLabel = (Label) remarksFlowPane.getChildren().get(2);
                                                pricesLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                                pricesLabel.setText(bigDecimal.toString());
                                                codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                            }
                                        }
                                    }
                                    modifyBuckleStewStage.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            closeButton.setOnAction(event1 -> {
                                modifyBuckleStewStage.close();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        confirmStage.close();
                    });
                    confirmStage.setScene(new Scene(confirmFoodFlowPane));
                    confirmStage.initModality(Modality.APPLICATION_MODAL);
                    confirmStage.initStyle(StageStyle.TRANSPARENT);
                    confirmStage.initOwner(buckleStewIndexController.getStage());
                    confirmStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    //收費類別
    @FXML
    public void chargeCategories() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/ChargeCategoriesView.fxml"));
        FlowPane flowPane = loader.load();
        ChargeCategoriesController chargeCategoriesController = loader.getController();
        double chargeCategoriesWidth = primaryScreenBounds.getWidth() / 10 * 8;
        double chargeCategoriesHeight = primaryScreenBounds.getHeight() / 5 * 4 - 6;
        flowPane.setPrefHeight(chargeCategoriesHeight);
        flowPane.setPrefWidth(chargeCategoriesWidth);
        Label titleLabel = (Label) flowPane.getChildren().get(0);
        double titleLabelWidth = chargeCategoriesWidth - 6;
        double titleLabelHeight = chargeCategoriesHeight / 9;
        titleLabel.setPrefWidth(titleLabelWidth);
        titleLabel.setPrefHeight(titleLabelHeight);
        FlowPane contentFlowPane = (FlowPane) flowPane.getChildren().get(1);
        double contentFlowPaneWidth = chargeCategoriesWidth - 6;
        double contentFlowPaneHeight = chargeCategoriesHeight / 9 * 6;
        double contentLabelFlowPaneWidth = (chargeCategoriesWidth - 6) / 6;
        double contentLabelFlowPaneHeight = chargeCategoriesHeight / 6;
        contentFlowPane.setPrefWidth(contentFlowPaneWidth);
        contentFlowPane.setPrefHeight(contentFlowPaneHeight);
        contentFlowPane.setHgap(contentLabelFlowPaneWidth / 8);
        contentFlowPane.setVgap(contentLabelFlowPaneHeight / 8);
        contentFlowPane.setPadding(new Insets(contentLabelFlowPaneHeight / 8, 0, 0, contentLabelFlowPaneWidth / 4));
        for (int i = 0; i < 15; i++) {
            Button button = new Button();
            button.setText("稻香中央菜牌-標準收費");
            button.setPrefWidth(contentLabelFlowPaneWidth);
            button.setPrefHeight(contentLabelFlowPaneHeight);
            button.setStyle("-fx-font-size: 26px; -fx-background-color: #27728A;-fx-text-fill: white; -fx-wrap-text: true");
            contentFlowPane.getChildren().add(button);
        }
        FlowPane buttonFlowPane = (FlowPane) flowPane.getChildren().get(2);
        double buttonFlowPaneWidth = chargeCategoriesWidth - 6;
        double buttonFlowPaneHeight = chargeCategoriesHeight / 9 * 2 - 7;
        buttonFlowPane.setPrefWidth(buttonFlowPaneWidth);
        buttonFlowPane.setPrefHeight(buttonFlowPaneHeight);
        buttonFlowPane.setHgap(chargeCategoriesWidth / 20);
        double buttonWidth = buttonFlowPaneWidth / 5;
        double buttonHeight = buttonFlowPaneHeight / 3 * 2;
        chargeCategoriesController.getNextPageButton().setPrefWidth(buttonWidth);
        chargeCategoriesController.getNextPageButton().setPrefHeight(buttonHeight);
        chargeCategoriesController.getCloseButton().setPrefWidth(buttonWidth);
        chargeCategoriesController.getCloseButton().setPrefHeight(buttonWidth);
        double textFieldWidth = chargeCategoriesWidth / 5 * 2;
        double textFieldHeight = chargeCategoriesHeight / 9 * 1.8;
        chargeCategoriesController.getSearchTextField().setPrefWidth(textFieldWidth);
        chargeCategoriesController.getSearchTextField().setPrefHeight(textFieldHeight);
        chargeCategoriesController.getCloseButton().setPrefWidth(buttonWidth);
        chargeCategoriesController.getCloseButton().setPrefHeight(buttonHeight);
        Stage chargeCategoriesStage = new Stage();
        chargeCategoriesController.setStage(chargeCategoriesStage);
        chargeCategoriesStage.setScene(new Scene(flowPane));
        chargeCategoriesStage.initModality(Modality.APPLICATION_MODAL);
        chargeCategoriesStage.initStyle(StageStyle.TRANSPARENT);
        chargeCategoriesStage.show();
    }

    //專有食品
    @FXML
    public void properFood() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/ProperFoodView.fxml"));
        FlowPane properFoodFlowPane = loader.load();
        ProperFoodController properFoodController = loader.getController();
        double properFoodFlowPaneWidth = primaryScreenBounds.getWidth();
        double properFoodFlowPaneHeight = primaryScreenBounds.getHeight();
        properFoodFlowPane.setPrefHeight(properFoodFlowPaneHeight);
        properFoodFlowPane.setPrefWidth(properFoodFlowPaneWidth);
        double tableViewWidth = primaryScreenBounds.getWidth();
        double tableViewHeight = primaryScreenBounds.getHeight() / 10 * 8;
        properFoodController.getFoodTableView().setPrefWidth(tableViewWidth);
        properFoodController.getFoodTableView().setPrefHeight(tableViewHeight);
        properFoodController.getCategoryColum().setPrefWidth(tableViewWidth / 20 * 2);
        properFoodController.getProperFoodNoColum().setPrefWidth(tableViewWidth / 20 * 2);
        properFoodController.getProperFoodNameColum().setPrefWidth(tableViewWidth / 20 * 2.5);
        properFoodController.getProperFoodPrice1Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice2Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice3Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice4Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice5Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice6Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice7Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice8Colum().setPrefWidth(tableViewWidth / 20 * 1.5);
        properFoodController.getProperFoodPrice9Colum().setPrefWidth(tableViewWidth / 20 * 1.5);

        double buttomFlowPaneWidth = primaryScreenBounds.getWidth();
        double buttomFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        FlowPane buttomFlowPane = (FlowPane) properFoodFlowPane.getChildren().get(1);
        buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
        buttomFlowPane.setPrefWidth(buttomFlowPaneWidth);

        double leftButtomFlowPaneWidth = primaryScreenBounds.getWidth() / 10 * 8;
        double leftButtomFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        properFoodController.getLeftButtomFlowPane().setPrefHeight(leftButtomFlowPaneHeight);
        properFoodController.getLeftButtomFlowPane().setPrefWidth(leftButtomFlowPaneWidth);
        properFoodController.getLeftButtomFlowPane().setPadding(new Insets(leftButtomFlowPaneHeight / 10, 0, 0, leftButtomFlowPaneHeight / 10));
        properFoodController.getLeftButtomFlowPane().setHgap(leftButtomFlowPaneHeight / 10);
        double leftButtonWidth = leftButtomFlowPaneWidth / 10 * 1.5;
        double leftButtonHeight = leftButtomFlowPaneHeight / 10 * 8;
        properFoodController.getUpdateButton().setPrefHeight(leftButtonHeight);
        properFoodController.getUpdateButton().setPrefWidth(leftButtonWidth);
        properFoodController.getReSetButton().setPrefHeight(leftButtonHeight);
        properFoodController.getReSetButton().setPrefWidth(leftButtonWidth);
        double centerButtomFlowPaneWidth = primaryScreenBounds.getWidth() / 10;
        double centerButtomFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        double centerButtonWidth = primaryScreenBounds.getWidth() / 10;
        double centerButtonHeight = centerButtomFlowPaneHeight / 10 * 8;
        properFoodController.getCenterButtomFlowPane().setPrefHeight(centerButtomFlowPaneHeight);
        properFoodController.getCenterButtomFlowPane().setPrefWidth(centerButtomFlowPaneWidth);
        properFoodController.getCenterButtomFlowPane().setAlignment(Pos.CENTER);
        properFoodController.getCloseButton().setPrefHeight(centerButtonHeight);
        properFoodController.getCloseButton().setPrefWidth(centerButtonWidth);
        double rightButtomFlowPaneWidth = primaryScreenBounds.getWidth() / 10 / 10 * 9;
        double rightButtomFlowPaneHeight = primaryScreenBounds.getHeight() / 10 * 2;
        double rightButtonWidth = rightButtomFlowPaneWidth / 2;
        double rightButtonHeight = rightButtomFlowPaneHeight / 10 * 4;
        properFoodController.getRightButtomFlowPane().setPrefHeight(rightButtomFlowPaneHeight);
        properFoodController.getRightButtomFlowPane().setPrefWidth(rightButtomFlowPaneWidth);
        properFoodController.getRightButtomFlowPane().setAlignment(Pos.CENTER);
        properFoodController.getUpButton().setPrefHeight(rightButtonHeight);
        properFoodController.getUpButton().setPrefWidth(rightButtonWidth);
        properFoodController.getDownButton().setPrefHeight(rightButtonHeight);
        properFoodController.getDownButton().setPrefWidth(rightButtonWidth);
        Stage properFoodStage = new Stage();
        properFoodController.getCloseButton().setOnAction(event -> {
            properFoodStage.close();
        });
        properFoodStage.setScene(new Scene(properFoodFlowPane));
        properFoodStage.setIconified(false);
        //stage.setFullScreen(true);
        properFoodStage.setX(primaryScreenBounds.getMinX());
        properFoodStage.setY(primaryScreenBounds.getMinY());
        properFoodStage.initModality(Modality.NONE);
        properFoodStage.initStyle(StageStyle.TRANSPARENT);
        properFoodStage.show();
    }

    /**
     * 按鍵設定
     */
    @FXML
    public void showKeyDesign() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/KeySettings.fxml"));
        FlowPane keySettingsFlowPane = loader.load();
        KeySettingsController keySettingsController = loader.getController();
        double keySettingsFlowPaneWidth = primaryScreenBounds.getWidth();
        double keySettingsFlowPaneHeight = primaryScreenBounds.getHeight();
        keySettingsFlowPane.setPrefWidth(keySettingsFlowPaneWidth);
        keySettingsFlowPane.setPrefHeight(keySettingsFlowPaneHeight);

        FlowPane leftFlowPane = (FlowPane) keySettingsFlowPane.getChildren().get(0);
        double leftFlowPaneWidth = primaryScreenBounds.getWidth() / 7 * 3;
        double leftFlowPaneHeight = primaryScreenBounds.getHeight();

        leftFlowPane.setPrefWidth(leftFlowPaneWidth);
        leftFlowPane.setPrefHeight(leftFlowPaneHeight);

        FlowPane leftContentFlowPane = (FlowPane) leftFlowPane.getChildren().get(0);
        double leftContentFlowPaneWidth = leftFlowPaneWidth;
        double leftContentFlowPaneHeight = leftFlowPaneHeight / 10 * 9;
        leftContentFlowPane.setPrefHeight(leftContentFlowPaneHeight);
        leftContentFlowPane.setPrefWidth(leftFlowPaneWidth);

        double tableViewWidth = leftFlowPaneWidth;
        double tableViewHeight = leftFlowPaneHeight / 10 * 9;
        keySettingsController.getGoodsTableView().setPrefWidth(tableViewWidth);
        keySettingsController.getGoodsTableView().setPrefHeight(tableViewHeight);

        keySettingsController.getNumberColum().setPrefWidth(tableViewWidth / 6);
        keySettingsController.getGoodNameColum().setPrefWidth(tableViewWidth / 6 * 4);
        keySettingsController.getPriceColum().setPrefWidth(tableViewWidth / 6);

        FlowPane leftButtomFlowPane = (FlowPane) leftFlowPane.getChildren().get(1);
        double leftButtomFlowPaneWidth = leftFlowPaneWidth;
        double leftButtomFlowPaneHeight = leftFlowPaneHeight / 10;
        leftButtomFlowPane.setPrefHeight(leftButtomFlowPaneHeight);
        leftButtomFlowPane.setPrefWidth(leftButtomFlowPaneWidth);
        leftButtomFlowPane.setHgap(leftButtomFlowPaneWidth / 96);
        double leftButtonWidth = (leftButtomFlowPaneWidth - leftButtomFlowPaneWidth / 100 * 6) / 6;
        double leftButtonHeight = leftButtomFlowPaneHeight - leftButtomFlowPaneHeight / 100 * 2;

        keySettingsController.getFoodButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getFoodButton().setPrefHeight(leftButtonHeight);
        keySettingsController.getColorButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getColorButton().setPrefHeight(leftButtonHeight);
        keySettingsController.getClassificationButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getClassificationButton().setPrefHeight(leftButtonHeight);
        keySettingsController.getFormatButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getFormatButton().setPrefHeight(leftButtonHeight);
        keySettingsController.getNotListedButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getNotListedButton().setPrefHeight(leftButtonHeight);
        keySettingsController.getCloseButton().setPrefWidth(leftButtonWidth);
        keySettingsController.getCloseButton().setPrefHeight(leftButtonHeight);

        FlowPane centerFlowPane = (FlowPane) keySettingsFlowPane.getChildren().get(1);
        double centerFlowPaneHeight = keySettingsFlowPaneHeight;
        double centerFlowPaneWidth = keySettingsFlowPaneWidth / 7 * 3;
        centerFlowPane.setPrefHeight(centerFlowPaneHeight);
        centerFlowPane.setPrefWidth(centerFlowPaneWidth);

        FlowPane contentFlowPane = (FlowPane) centerFlowPane.getChildren().get(0);
        double contentFlowPaneWidth = centerFlowPaneWidth;
        double contentFlowPaneHeight = centerFlowPaneHeight / 10 * 8;
        contentFlowPane.setPrefWidth(contentFlowPaneWidth);
        contentFlowPane.setPrefHeight(contentFlowPaneHeight);
        contentFlowPane.setPadding(new Insets(contentFlowPaneHeight / 100, 0, 0, contentFlowPaneWidth / 100));
        contentFlowPane.setHgap(contentFlowPaneHeight / 50);
        contentFlowPane.setVgap(contentFlowPaneWidth / 50);

        double buttonFlowPaneWidth = (contentFlowPaneWidth - contentFlowPaneWidth / 50 * 5) / 4;
        double buttonFlowPaneHeight = (contentFlowPaneHeight - contentFlowPaneHeight / 50 * 5) / 4;

        for (int i = 0; i < 16; i++) {
            FlowPane buttonFlowPane = new FlowPane();
            buttonFlowPane.setPrefWidth(buttonFlowPaneWidth);
            buttonFlowPane.setPrefHeight(buttonFlowPaneHeight);
            buttonFlowPane.setStyle("-fx-background-color: #EDEDED");
            FlowPane buttonTopFlownPane = new FlowPane();
            double buttonTopHeight = buttonFlowPaneHeight / 4;
            double buttonTopWidth = buttonFlowPaneWidth;
            buttonTopFlownPane.setPrefWidth(buttonTopWidth);
            buttonTopFlownPane.setPrefHeight(buttonTopHeight);
            buttonTopFlownPane.setPadding(new Insets(buttonTopHeight / 50, 0, 0, buttonTopWidth / 90));
            Label checkLabel = new Label();
            double checkLabelWidth = buttonFlowPaneWidth / 5;
            double checkLabelHeight = buttonFlowPaneWidth / 5;
            checkLabel.setPrefHeight(checkLabelHeight);
            checkLabel.setPrefWidth(checkLabelWidth);
            checkLabel.setStyle("-fx-border-color: #34495E; -fx-border-width: 3px");
            buttonTopFlownPane.getChildren().add(checkLabel);
            buttonFlowPane.getChildren().add(buttonTopFlownPane);

            double nameLabelWidth = buttonFlowPaneWidth;
            double nameLabelHeight = buttonFlowPaneHeight / 4 * 2;
            Label nameLabel = new Label();
            nameLabel.setPrefWidth(nameLabelWidth);
            nameLabel.setPrefHeight(nameLabelHeight);
            nameLabel.setText("半隻雞");
            nameLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bolder;-fx-text-fill: #1d1d1d");
            buttonFlowPane.getChildren().add(nameLabel);

            FlowPane buttomButtonFlowPane = new FlowPane();
            buttomButtonFlowPane.setPrefWidth(buttonFlowPaneWidth);
            buttomButtonFlowPane.setPrefHeight(buttonFlowPaneHeight / 4);

            Label codeLabel = new Label();
            codeLabel.setPrefWidth(buttonFlowPaneWidth / 2);
            codeLabel.setPrefHeight(buttonFlowPaneWidth / 4);
            codeLabel.setText("000" + i);
            codeLabel.setStyle("-fx-text-fill: #E3A7A3; -fx-text-alignment: center;-fx-font-size: 15px");
            buttomButtonFlowPane.getChildren().add(codeLabel);

            Label priceLabel = new Label();
            priceLabel.setPrefWidth(buttonFlowPaneWidth / 2);
            priceLabel.setPrefHeight(buttonFlowPaneWidth / 4);
            priceLabel.setText("35.00");
            priceLabel.setStyle("-fx-text-fill: #1d1d1d; -fx-text-alignment: center;-fx-font-size: 15px");
            buttomButtonFlowPane.getChildren().add(priceLabel);

            buttonFlowPane.getChildren().add(buttomButtonFlowPane);
            buttonFlowPane.setOnMouseClicked(event -> {
                int index = keySettingsController.getCheckLabels().indexOf(checkLabel);
                if (index >= 0) {
                    for (int j = 0; j < keySettingsController.getCheckLabels().size(); j++) {
                        if (j != index) {
                            keySettingsController.getCheckLabels().get(j).setStyle("-fx-border-color: #34495E; -fx-border-width: 3px;-fx-background-color: #EDEDED");
                        } else {
                            keySettingsController.getCheckLabels().get(j).setStyle("-fx-border-color: #34495E; -fx-border-width: 3px;-fx-background-color: #E39B46");
                            keySettingsController.setCurrentButtonFlowPane(keySettingsController.getButtonFlowPanes().get(j));
                        }
                    }
                }

            });
            contentFlowPane.getChildren().add(buttonFlowPane);
            keySettingsController.getCheckLabels().add(checkLabel);
            keySettingsController.getButtonFlowPanes().add(buttonFlowPane);
        }

        FlowPane centerButtomFlowPane = (FlowPane) centerFlowPane.getChildren().get(1);
        double centerButtomFlowPaneWidth = centerFlowPaneWidth;
        double centerButtomFlowPaneHeight = centerFlowPaneHeight / 10 * 2;
        centerButtomFlowPane.setPrefWidth(centerButtomFlowPaneWidth);
        centerButtomFlowPane.setPrefHeight(centerButtomFlowPaneHeight);
        centerButtomFlowPane.setPadding(new Insets(centerButtomFlowPaneHeight / 50, 0, 0, centerButtomFlowPaneWidth / 50));
        centerButtomFlowPane.setHgap(contentFlowPaneWidth / 50);
        centerButtomFlowPane.setVgap(contentFlowPaneHeight / 100);

        double buttomButtonWidth = (centerButtomFlowPaneWidth - centerButtomFlowPaneWidth / 50 * 6) / 5;
        double buttomButtonHeight = (centerButtomFlowPaneHeight - centerButtomFlowPaneHeight / 50 * 5) / 2;
        keySettingsController.getDescriptionButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getDescriptionButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getDeleteButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getDeleteButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getDeletePageButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getDeletePageButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getAddPageButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getAddPageButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getPagesLabel().setPrefHeight(buttomButtonHeight);
        keySettingsController.getPagesLabel().setPrefWidth(buttomButtonWidth);
        keySettingsController.getSearchButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getSearchButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getSettingButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getSettingButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getBackButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getBackButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getPreviousPageButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getPreviousPageButton().setPrefWidth(buttomButtonWidth);
        keySettingsController.getNextPageButton().setPrefHeight(buttomButtonHeight);
        keySettingsController.getNextPageButton().setPrefWidth(buttomButtonWidth);

        //類別部分
        FlowPane rightFlowPane = (FlowPane) keySettingsFlowPane.getChildren().get(2);
        double rightFlowPaneWidth = primaryScreenBounds.getWidth() / 7 - primaryScreenBounds.getWidth() / 7 / 50;
        double rightFlowPaneHeight = primaryScreenBounds.getHeight();
        rightFlowPane.setPrefWidth(rightFlowPaneWidth);
        rightFlowPane.setPrefHeight(rightFlowPaneHeight);

        FlowPane categorysFlowPane = (FlowPane) rightFlowPane.getChildren().get(0);
        double categorysFlowPaneWidth = rightFlowPaneWidth;
        double categoryFlowPaneHeight = rightFlowPaneHeight / 10 * 9;
        categorysFlowPane.setPrefHeight(categoryFlowPaneHeight);
        categorysFlowPane.setPrefWidth(categorysFlowPaneWidth);

        double categoryLabelWidth = rightFlowPaneWidth;
        double categoryLabelHeight = categoryFlowPaneHeight / 9 - 2;
        for (int j = 0; j < 8; j++) {
            Label categoryLabel = new Label();
            categoryLabel.setPrefHeight(categoryLabelHeight);
            categoryLabel.setPrefWidth(categoryLabelWidth);
            categoryLabel.setText("海鮮類");
            categoryLabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;-fx-background-color:#34495E;-fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
            categorysFlowPane.getChildren().add(categoryLabel);
        }


        FlowPane rightButtomFlowPane = (FlowPane) rightFlowPane.getChildren().get(1);

        double rightButtomFlowPaneWidth = rightFlowPaneWidth;
        double rightButtomFlowPaneHeight = rightFlowPaneHeight / 10;
        rightButtomFlowPane.setPrefWidth(rightButtomFlowPaneWidth);
        rightButtomFlowPane.setPrefHeight(rightButtomFlowPaneHeight);
        rightButtomFlowPane.setHgap(rightButtomFlowPaneWidth / 100);
        rightButtomFlowPane.setPadding(new Insets(centerButtomFlowPaneHeight / 100, 0, 0, 0));

        double categoryButtonWidth = (rightButtomFlowPaneWidth - rightButtomFlowPaneWidth / 100) / 2;
        double categoryButtonHeight = buttomButtonHeight;

        keySettingsController.getPreviousCategoryButton().setPrefWidth(categoryButtonWidth);
        keySettingsController.getPreviousCategoryButton().setPrefHeight(categoryButtonHeight);
        keySettingsController.getNextCategoryButton().setPrefWidth(categoryButtonWidth);
        keySettingsController.getNextCategoryButton().setPrefHeight(categoryButtonHeight);

        keySettingsController.setPrimaryScreenBounds(primaryScreenBounds);
        keySettingsController.setMain(main);

        Stage keySettingStage = new Stage();
        keySettingsController.getCloseButton().setOnAction(event -> {
            keySettingStage.close();
        });
        keySettingStage.setScene(new Scene(keySettingsFlowPane));
        keySettingStage.setIconified(false);
        //stage.setFullScreen(true);
        keySettingStage.setX(primaryScreenBounds.getMinX());
        keySettingStage.setY(primaryScreenBounds.getMinY());
        keySettingStage.initModality(Modality.NONE);
        keySettingStage.initStyle(StageStyle.TRANSPARENT);
        keySettingStage.show();

    }


    /**
     * 桌臺設計
     */
    @FXML
    public void showTableSetting() {
        //啟動新的線程進行轉場，提高性能
        TableSettingsController tableSettingsController = (TableSettingsController) tableSettingView.getPresenter();
        Main.showInitialView(tableSettingView.getClass());
        ObservableList<Stage> stages = FXRobotHelper.getStages();
        tableSettingsController.setNewTableSettingStage(stages.get(0));
        tableSettingsController.refreshTables(1, tableSettingsController.getCurrentArea(), stages.get(0), false);
    }

    @FXML
    public void closeStage() {
        Main.showInitialView(FunctionsView.class);
    }


}
