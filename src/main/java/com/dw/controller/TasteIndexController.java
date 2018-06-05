package com.dw.controller;

import com.dw.Main;
import com.dw.model.Goods;
import com.dw.util.NumberFormatUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * Created by lodi on 2018/1/9.
 */
@Getter
@Setter
public class TasteIndexController {

    @FXML
    private TableView<Goods> foodTableView;

    @FXML
    private TableColumn<Goods,String> numberColum;

    @FXML
    private TableColumn<Goods,String> goodNameColum;

    @FXML
    private TableColumn<Goods,String> priceColum;

    @FXML
    private TableColumn<Goods,String> messageColum;

    /*@FXML
    private Label tableLabel;*/

    @FXML
    private Button clearButton;

    @FXML
    private Button cancleButton;

    @FXML
    private Button previousGoodsButton;

    @FXML
    private Button nextGoodsButton;

    @FXML
    private Button tasteButton;

    @FXML
    private Button previousCategoryButton;

    @FXML
    private Button nextCategoryButton;

    @FXML
    private Stage stage;

    private Rectangle2D primaryScreenBounds;

    private FlowPane itemsFlowPane;

    @FXML
    private Label tableNoLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalNumberLabel;

    @FXML
    private SearchFoodController searchFoodController;

    private FlowPane categorysFlowPane;

    private HBox hbox;


    /**
     * 初始化方法
     */
    @FXML
    private void initialize() {
        numberColum.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        goodNameColum.setCellValueFactory(cellData -> cellData.getValue().goodNameProperty());
        priceColum.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        messageColum.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
    }

    @FXML
    public void close(){
        stage.close();
    }

    @FXML
    public void openKeyWord() throws IOException {
        FXMLLoader  loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/SearchFoodView.fxml"));
        FlowPane searchFoodFlowPane =  loader.load();
        SearchFoodController  searchFoodController = loader.getController();
        searchFoodController.setPrimaryScreenBounds(primaryScreenBounds);
        double searchFoodFlowPaneWidth = primaryScreenBounds.getWidth()/10*4 + primaryScreenBounds.getWidth()/300;
        double searchFoodFlowPaneHeight = primaryScreenBounds.getHeight()/2;
        searchFoodFlowPane.setPrefHeight(searchFoodFlowPaneHeight);
        searchFoodFlowPane.setPrefWidth(searchFoodFlowPaneWidth);
        FlowPane labelFlowPane = (FlowPane) searchFoodFlowPane.getChildren().get(0);
        labelFlowPane.setPrefHeight(searchFoodFlowPaneHeight/7);
        labelFlowPane.setPrefWidth(searchFoodFlowPaneWidth);
        double labelWidth = searchFoodFlowPaneWidth/3-10;
        double labelHeight =  searchFoodFlowPaneHeight/7;
        searchFoodController.getTableNoLabel().setText(tableNoLabel.getText());
        searchFoodController.getTableNoLabel().setPrefWidth(labelWidth);
        searchFoodController.getTableNoLabel().setPrefHeight(labelHeight);
        searchFoodController.getTotalNumberLabel().setPrefHeight(labelHeight);
        searchFoodController.getTotalNumberLabel().setPrefWidth(labelWidth);
        searchFoodController.getTotalNumberLabel().setText(totalNumberLabel.getText());
        searchFoodController.getTotalPriceLabel().setPrefHeight(labelHeight);
        searchFoodController.getTotalPriceLabel().setPrefWidth(labelWidth);
        searchFoodController.getTotalPriceLabel().setText(totalPriceLabel.getText());
        double textFieldWidth = searchFoodFlowPaneWidth;
        double textFieldlHeight =  searchFoodFlowPaneHeight/7;

        searchFoodController.getTextField().setPromptText("請輸入");
        searchFoodController.getTextField().setPrefWidth(searchFoodFlowPaneWidth );
        searchFoodController.getTextField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if(searchFoodController.getTextField().getText().length() == 4){
                     searchFoodController.getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                     searchFoodController.getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                     if(!searchFoodController.getTextField().getText().equals("0007")){
                         searchFoodController.getTextField().setText("");
                         FXMLLoader  loader = new FXMLLoader();
                         loader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
                         FlowPane confirmFlowPane = null;
                         try {
                             confirmFlowPane = loader.load();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
                         double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
                         confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
                         confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
                         double labelWidth =  confirmFlowPaneWidth;
                         double labelHeight = confirmFlowPaneHeight/3*2;
                         Label label = (Label) confirmFlowPane.getChildren().get(0);
                         label.setText("查询不到该食品");
                         label.setPrefHeight(labelHeight);
                         label.setPrefWidth(labelWidth);
                         double buttomFlowPaneWidth =  confirmFlowPaneWidth;
                         double buttomFlowPaneHeight = confirmFlowPaneHeight/3;
                         FlowPane buttomFlowPane = (FlowPane) confirmFlowPane.getChildren().get(1);
                         buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                         buttomFlowPane.setPrefWidth(buttomFlowPaneWidth);
                         Button confirmButton = (Button) buttomFlowPane.getChildren().get(0);
                         double confirmButtonWidth =  confirmFlowPaneWidth/3;
                         double confirmButtonHeight = confirmFlowPaneHeight/4;
                         confirmButton.setPrefHeight(confirmButtonHeight);
                         confirmButton.setPrefWidth(confirmButtonWidth);
                         Stage confirmStage = new Stage();
                         confirmButton.setOnAction(event -> {
                             confirmStage.close();
                         });
                         confirmStage.initOwner(getStage());
                         confirmStage.setScene(new Scene(confirmFlowPane));
                         confirmStage.initModality(Modality.APPLICATION_MODAL);
                         confirmStage.initStyle(StageStyle.TRANSPARENT);
                         confirmStage.show();
                         return;
                     }
                     searchFoodController.getTextField().setText("0007 半隻貴妃雞仔蛋");
                     searchFoodController.getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                     searchFoodController.getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                     ObservableList<Goods> goods =  foodTableView.getItems();
                     boolean isExist = false;
                     Integer  selectedIndex = -1;
                     for(int i = 0;i<goods.size();i++){
                         if(goods.get(i).getGoodName().equals("0007半隻貴妃雞仔蛋")){
                             isExist = true;
                             selectedIndex = i;
                         }
                     }
                     if(!isExist){
                         foodTableView.getItems().add(new Goods("1","0007半隻貴妃雞仔蛋","35.00",""));
                         foodTableView.getSelectionModel().selectLast();
                     }
                     else{
                         if(selectedIndex != -1){
                             foodTableView.getItems().get(selectedIndex).setNumber((Integer.parseInt(foodTableView.getItems().get(selectedIndex).getNumber())+1) + "");
                         }
                     }
                     Integer totalNumber = 0;
                     Double totalPrice = 0.00d;
                     for(Goods good : goods){
                         totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                         totalPrice = totalPrice + Integer.parseInt(good.getNumber())*Double.parseDouble(good.getPrice());
                     }
                     searchFoodController.getTableNoLabel().setText(tableNoLabel.getText());
                     searchFoodController.getTotalNumberLabel().setText(  foodTableView.getItems().size() + "/" + totalNumber);
                     searchFoodController.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
                     totalNumberLabel.setText( searchFoodController.getTotalNumberLabel().getText());
                     totalPriceLabel.setText(searchFoodController.getTotalPriceLabel().getText());
                     itemsFlowPane.getChildren().remove(0,itemsFlowPane.getChildren().size());
                     double itemsFlowPaneWidth =primaryScreenBounds.getWidth()/10*5;
                     double itemsFlowPaneHeight = primaryScreenBounds.getHeight()/10*9;
                     for(int k = 0;k<20;k++) {
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
                             FXMLLoader  confirmTasteloader = new FXMLLoader();
                             confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmTasteView.fxml"));
                             try {
                                 FlowPane confirmTasteFlowPane  = confirmTasteloader.load();
                                 double confirmTasteWidth = primaryScreenBounds.getWidth()/2;
                                 double confirmTasteHeight = primaryScreenBounds.getHeight()/3;
                                 confirmTasteFlowPane.setPrefHeight(confirmTasteHeight);
                                 confirmTasteFlowPane.setPrefWidth(confirmTasteWidth);
                                 Label label = (Label) confirmTasteFlowPane.getChildren().get(0);
                                 label.setText("確定要暫停["+  itemTasteLabel.getText() + "]?");
                                 double labelWidth = confirmTasteWidth;
                                 double labelHeight = confirmTasteHeight/3*2;
                                 label.setPrefWidth(labelWidth);
                                 label.setPrefHeight(labelHeight);
                                 FlowPane buttomFlowPane = (FlowPane) confirmTasteFlowPane.getChildren().get(1);
                                 double buttomFlowPaneWidth = confirmTasteWidth;
                                 double buttomFlowPaneHeight = confirmTasteHeight/3;
                                 buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                                 buttomFlowPane.setPrefWidth(confirmTasteWidth);
                                 buttomFlowPane.setHgap(buttomFlowPaneWidth/20);
                                 Stage confirmStage = new Stage();
                                 Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                                 noButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                                 noButton.setPrefWidth(buttomFlowPaneWidth/3);
                                 noButton.setOnAction(event2 -> {
                                     confirmStage.close();
                                 });
                                 Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                                 yesButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                                 yesButton.setPrefWidth(buttomFlowPaneWidth/3);
                                 yesButton.setOnAction(event2 -> {
                                     codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                     priceTasteLabel.setText("暫停");
                                     priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                     confirmStage.close();

                                 });
                                 confirmStage.setScene(new Scene(confirmTasteFlowPane));
                                 confirmStage.initModality(Modality.NONE);
                                 confirmStage.initStyle(StageStyle.TRANSPARENT);
                                 confirmStage.initOwner(stage);
                                 confirmStage.show();

                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         });
                         itemsFlowPane.getChildren().add(itemTasteFlowPane);

                     }
                 }
            }
        });



        FlowPane keywordFlowPane = (FlowPane) searchFoodFlowPane.getChildren().get(2);
        double keywordFlowPaneWidth = searchFoodFlowPaneWidth;
        double keywordFlowPaneHeight =  searchFoodFlowPaneHeight/7*5;
        keywordFlowPane.setPrefWidth(keywordFlowPaneWidth);
        keywordFlowPane.setPrefHeight(keywordFlowPaneHeight);
        keywordFlowPane.setPadding(new Insets(5,0,0,0));

        FlowPane topKeywordFlowPane = (FlowPane) keywordFlowPane.getChildren().get(0);
        double topKeywordFlowPaneWidth = keywordFlowPaneWidth;
        double topKeywordFlowPaneHeight = searchFoodFlowPaneHeight/7*4;
        topKeywordFlowPane.setPrefHeight(topKeywordFlowPaneHeight);
        topKeywordFlowPane.setPrefWidth(topKeywordFlowPaneWidth);
        topKeywordFlowPane.setHgap(5);
        topKeywordFlowPane.setVgap(5);
        double buttonWidth =  topKeywordFlowPaneWidth/5-5;
        double buttonHeight = topKeywordFlowPaneHeight/4-5;
        searchFoodController.getNumberButton().setPrefHeight(buttonHeight);
        searchFoodController.getNumberButton().setPrefWidth(buttonWidth);
        searchFoodController.getChangPriceButton().setPrefWidth(buttonWidth);
        searchFoodController.getChangPriceButton().setPrefHeight(buttonHeight);
        searchFoodController.getTaseButton().setPrefHeight(buttonHeight);
        searchFoodController.getTaseButton().setPrefWidth(buttonWidth);
        searchFoodController.getAddButton().setPrefHeight(buttonHeight);
        searchFoodController.getAddButton().setPrefWidth(buttonWidth);
        searchFoodController.getSubButton().setPrefHeight(buttonHeight);
        searchFoodController.getSubButton().setPrefWidth(buttonWidth);
        searchFoodController.getSevenButton().setPrefWidth(buttonWidth);
        searchFoodController.getSevenButton().setPrefHeight(buttonHeight);
        searchFoodController.getRightButton().setPrefWidth(buttonWidth);
        searchFoodController.getRightButton().setPrefHeight(buttonHeight);
        searchFoodController.getNineButton().setPrefHeight(buttonHeight);
        searchFoodController.getNineButton().setPrefWidth(buttonWidth);
        searchFoodController.getAButton().setPrefHeight(buttonHeight);
        searchFoodController.getAButton().setPrefWidth(buttonWidth);
        searchFoodController.getSearchFoodButton().setPrefWidth(buttonWidth);
        searchFoodController.getSearchFoodButton().setPrefHeight(buttonHeight);
        searchFoodController.getFourButton().setPrefHeight(buttonHeight);
        searchFoodController.getFourButton().setPrefWidth(buttonWidth);
        searchFoodController.getFiveButton().setPrefHeight(buttonHeight);
        searchFoodController.getFiveButton().setPrefWidth(buttonWidth);
        searchFoodController.getSixButton().setPrefHeight(buttonHeight);
        searchFoodController.getSixButton().setPrefWidth(buttonWidth);
        searchFoodController.getBButton().setPrefHeight(buttonHeight);
        searchFoodController.getBButton().setPrefWidth(buttonWidth);
        searchFoodController.getDeleteButton().setPrefWidth(buttonWidth);
        searchFoodController.getDeleteButton().setPrefHeight(buttonHeight);
        searchFoodController.getOneButton().setPrefHeight(buttonHeight);
        searchFoodController.getOneButton().setPrefWidth(buttonWidth);
        searchFoodController.getTwoButton().setPrefHeight(buttonHeight);
        searchFoodController.getTwoButton().setPrefWidth(buttonWidth);
        searchFoodController.getThreeButton().setPrefHeight(buttonHeight);
        searchFoodController.getThreeButton().setPrefWidth(buttonWidth);
        searchFoodController.getBButton().setPrefHeight(buttonHeight);
        searchFoodController.getCButton().setPrefWidth(buttonWidth);
        searchFoodController.getReInputButton().setPrefWidth(buttonWidth);
        searchFoodController.getReInputButton().setPrefHeight(buttonHeight);


        FlowPane buttomKeywordFlowPane = (FlowPane) keywordFlowPane.getChildren().get(1);
        double buttomKeywordFlowPaneWidth = keywordFlowPaneWidth;
        double buttomKeywordFlowPaneHeight = searchFoodFlowPaneHeight/7;
        buttomKeywordFlowPane.setPrefHeight(topKeywordFlowPaneHeight);
        buttomKeywordFlowPane.setPrefWidth(topKeywordFlowPaneWidth);
        buttomKeywordFlowPane.setPadding(new Insets(5,0,0,0));
        buttomKeywordFlowPane.setHgap(5);
        buttomKeywordFlowPane.setVgap(5);
        double zeroButtonWidth =  buttomKeywordFlowPaneWidth/5*2-5;
        double zeroButtonHeight = buttomKeywordFlowPaneHeight-5;
        searchFoodController.getZeroButton().setPrefHeight(zeroButtonHeight);
        searchFoodController.getZeroButton().setPrefWidth(zeroButtonWidth);

        double otherButtonWidth =  buttomKeywordFlowPaneWidth/5-5;
        double otherButtonHeight = buttomKeywordFlowPaneHeight-5;
        searchFoodController.getPointButton().setPrefHeight(otherButtonHeight);
        searchFoodController.getPointButton().setPrefWidth(otherButtonWidth);
        searchFoodController.getVButton().setPrefHeight(otherButtonHeight);
        searchFoodController.getVButton().setPrefWidth(otherButtonWidth);
        searchFoodController.getOkButton().setPrefHeight(otherButtonHeight);
        searchFoodController.getOkButton().setPrefWidth(otherButtonWidth);
        Stage keywordStage = new Stage();
        keywordStage.setScene(new Scene(searchFoodFlowPane));
        keywordStage.setY(searchFoodFlowPaneHeight-buttomKeywordFlowPaneWidth/10);
        keywordStage.setX(-keywordFlowPaneWidth/300);
        keywordStage.initOwner(stage);
       //keywordStage.initModality(Modality.NONE);
        keywordStage.initStyle(StageStyle.TRANSPARENT);
        searchFoodController.setKeyWordStage(keywordStage);
        searchFoodController.setTasteIndexController(this);
        this.setSearchFoodController(searchFoodController);
        keywordStage.show();

    }

   //清除
    @FXML
    public  void subNumber() {
        ObservableList<Goods> goods = getFoodTableView().getItems();
        Goods good =  getFoodTableView().getSelectionModel().getSelectedItem();
        if(good != null){
            if(Integer.parseInt(good.getNumber())>1){
                good.setNumber((Integer.parseInt(good.getNumber()) - 1) +  "");
            }
            else{
                goods.remove(good);
            }
            Integer totalNumber = 0;
            Double totalPrice = 0.00d;
            for(Goods g : goods){
                totalNumber = totalNumber + Integer.parseInt(g.getNumber());
                totalPrice = totalPrice + Integer.parseInt(g.getNumber())*Double.parseDouble(g.getPrice());
            }
            this.getTableNoLabel().setText(getTableNoLabel().getText());
            this.getTotalNumberLabel().setText( getFoodTableView().getItems().size() + "/" + totalNumber);
            this.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
            getTotalNumberLabel().setText(this.getTotalNumberLabel().getText());
            getTotalPriceLabel().setText(this.getTotalPriceLabel().getText());
            if(this.getCategorysFlowPane() != null && this.getCategorysFlowPane().getChildren().size()>0){
                Label categoryLabel = (Label) this.getCategorysFlowPane().getChildren().get(0);
                if("食品口味".equals(categoryLabel.getText())){
                    this.getCategorysFlowPane().getChildren().remove(0,getCategorysFlowPane().getChildren().size());
                    this.getItemsFlowPane().getChildren().remove(0,itemsFlowPane.getChildren().size());
                    double itemsFlowPaneWidth = primaryScreenBounds.getWidth()/10*5;
                    double itemsFlowPaneHeight = primaryScreenBounds.getHeight()/10*9;
                    for(int i = 0;i<20;i++){
                        FlowPane itemFlowPane =  new FlowPane();
                        double itemFlowPaneWidth =  itemsFlowPaneWidth/6;
                        double itemFlowPaneHeight = itemsFlowPaneHeight/5;
                        itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
                        itemFlowPane.setPrefHeight(itemFlowPaneHeight);
                        itemFlowPane.setPrefWidth(itemFlowPaneWidth);
                        double itemImageWidth = itemFlowPaneWidth;
                        double itemImageHeight = itemFlowPaneHeight/5*3;
                        //菜品图片
                        Image itemImage = new Image("/image/chicken.jpg",itemImageWidth,itemImageHeight,true,true);
                        javafx.scene.image.ImageView itemImageView  = new javafx.scene.image.ImageView();
                        itemImageView.setFitWidth(itemImageWidth);
                        itemImageView.setFitHeight(itemImageHeight);
                        itemImageView.setImage(itemImage);

   /*         itemLabel.setText("半隻貴妃雞仔蛋");

            itemLabel.setPrefWidth(itemFlowPaneWidth);
            itemLabel.setPrefHeight(itemLabelHeight);
            itemLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");*/
                        itemFlowPane.getChildren().add(itemImageView);
                        FlowPane remarkFlowPane =  new FlowPane();
                        double remarkFlowPaneWidth = itemFlowPaneWidth;
                        double remarkFlowPaneHeight = itemFlowPaneHeight/5*2;
                        remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
                        remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
                        double itemNameLabelWidth = itemImageWidth;
                        double itemNameLabelHeight = itemFlowPaneHeight/5;
                        Label itemNameLabel = new Label();
                        itemNameLabel.setText("半隻貴妃雞仔蛋");
                        itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
                        itemNameLabel.setPrefWidth(itemNameLabelWidth);
                        itemNameLabel.setPrefHeight(itemNameLabelHeight);
                        remarkFlowPane.getChildren().add(itemNameLabel);
                        Label codeLabel = new Label();
                        codeLabel.setText("000"+i);
                        codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
                        double codeLabelWidth = itemImageWidth/2;
                        double codeLabelHeight = itemFlowPaneHeight/5;
                        codeLabel.setPrefWidth(codeLabelWidth);
                        codeLabel.setPrefHeight(codeLabelHeight);
                        remarkFlowPane.getChildren().add(codeLabel);
                        Label priceLabel = new Label();
                        priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
                        priceLabel.setText("35.00");
                        double priceLabellWidth = itemImageWidth/2;
                        double priceLabelHeight = itemFlowPaneHeight/5;
                        priceLabel.setPrefWidth(priceLabellWidth);
                        priceLabel.setPrefHeight(priceLabelHeight);
                        remarkFlowPane.getChildren().add(priceLabel);
                        itemFlowPane.getChildren().add(remarkFlowPane);
                        itemsFlowPane.getChildren().add(itemFlowPane);
                        itemFlowPane.setOnMousePressed(event -> {
                            boolean isExist = false;
                            Integer  selectedIndex = -1;
                            ObservableList<Goods> itemGoods = getFoodTableView().getItems();
                            for(int k = 0;k<getFoodTableView().getItems().size();k++){
                                if(itemGoods.get(k).getGoodName().equals(codeLabel.getText()+ "半隻貴妃雞仔蛋")){
                                    isExist = true;
                                    selectedIndex = k;
                                }
                            }
                            if(!isExist){
                                itemGoods.add(new Goods("1",codeLabel.getText() + "半隻貴妃雞仔蛋","35.00",""));
                                getFoodTableView().getSelectionModel().selectLast();
                            }
                            else{
                                if(selectedIndex != -1){
                                    getFoodTableView().getItems().get(selectedIndex).setNumber((Integer.parseInt(getFoodTableView().getItems().get(selectedIndex).getNumber())+1) + "");

                                }
                            }
                            Integer totalNum = 0;
                            Double totalPrices = 0.00d;
                            for(Goods g : itemGoods){
                                totalNum = totalNum + Integer.parseInt(g.getNumber());
                                totalPrices = totalPrices + Integer.parseInt(g.getNumber())*Double.parseDouble(g.getPrice());
                            }
                            getTotalNumberLabel().setText(itemGoods.size()+"/" + totalNum);
                            getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrices));
               /* tasteIndexController.getFoodTableView().getItems().add(new Goods("1", itemLabel.getText(),priceLabel.getText(),""));
                tasteIndexController.getFoodTableView().getSelectionModel().selectLast();*/
                            if(getSearchFoodController() != null){
                                getSearchFoodController().getTableNoLabel().setText(getTableNoLabel().getText());
                                getSearchFoodController().getTotalNumberLabel().setText(getTotalNumberLabel().getText());
                                getSearchFoodController().getTotalPriceLabel().setText(getTotalPriceLabel().getText());
                            }
                            if(!codeLabel.getText().equals("0007")){
                                return;
                            }
                            FlowPane cateFlowPane = this.getCategorysFlowPane();
                            cateFlowPane.getChildren().remove(0,cateFlowPane.getChildren().size());
                            double categorysFlowPaneWidth = primaryScreenBounds.getWidth()/10;
                            double categorysFlowPaneHeight =  primaryScreenBounds.getHeight()/10*8;
                            Label catlabel = new Label();
                            catlabel.setPrefWidth(categorysFlowPaneWidth);
                            catlabel.setPrefHeight(categorysFlowPaneHeight/10+10);
                            catlabel.setText("食品口味");
                            catlabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                            cateFlowPane.getChildren().add(catlabel);
                            itemsFlowPane.getChildren().remove(0,itemsFlowPane.getChildren().size());
                            for(int k = 0;k<20;k++) {
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
                                    FXMLLoader  confirmTasteloader = new FXMLLoader();
                                    confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmTasteView.fxml"));
                                    try {
                                        FlowPane confirmTasteFlowPane  = confirmTasteloader.load();
                                        double confirmTasteWidth = primaryScreenBounds.getWidth()/2;
                                        double confirmTasteHeight = primaryScreenBounds.getHeight()/3;
                                        confirmTasteFlowPane.setPrefHeight(confirmTasteHeight);
                                        confirmTasteFlowPane.setPrefWidth(confirmTasteWidth);
                                        Label label = (Label) confirmTasteFlowPane.getChildren().get(0);
                                        label.setText("確定要暫停["+  itemTasteLabel.getText() + "]?");
                                        double labelWidth = confirmTasteWidth;
                                        double labelHeight = confirmTasteHeight/3*2;
                                        label.setPrefWidth(labelWidth);
                                        label.setPrefHeight(labelHeight);
                                        FlowPane buttomFlowPane = (FlowPane) confirmTasteFlowPane.getChildren().get(1);
                                        double buttomFlowPaneWidth = confirmTasteWidth;
                                        double buttomFlowPaneHeight = confirmTasteHeight/3;
                                        buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                                        buttomFlowPane.setPrefWidth(confirmTasteWidth);
                                        buttomFlowPane.setHgap(buttomFlowPaneWidth/20);
                                        Stage confirmStage = new Stage();
                                        Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                                        noButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                                        noButton.setPrefWidth(buttomFlowPaneWidth/3);
                                        noButton.setOnAction(event2 -> {
                                            confirmStage.close();
                                        });
                                        Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                                        yesButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                                        yesButton.setPrefWidth(buttomFlowPaneWidth/3);
                                        yesButton.setOnAction(event2 -> {
                                            codeTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                            priceTasteLabel.setText("暫停");
                                            priceTasteLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                            confirmStage.close();

                                        });
                                        confirmStage.setScene(new Scene(confirmTasteFlowPane));
                                        confirmStage.initModality(Modality.NONE);
                                        confirmStage.initStyle(StageStyle.TRANSPARENT);
                                        confirmStage.initOwner(getStage());
                                        confirmStage.show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                itemsFlowPane.getChildren().add(itemTasteFlowPane);
                            }
                        });
                    }
                    double categorysFlowPaneWidth = primaryScreenBounds.getWidth()/10;
                    double categorysFlowPaneHeight =  primaryScreenBounds.getHeight()/10*8;
                    categorysFlowPane.setPrefHeight(categorysFlowPaneHeight);
                    categorysFlowPane.setPrefWidth(categorysFlowPaneWidth);
                    for(int i = 0;i<8;i++){
                        Label label = new Label();
                        label.setPrefWidth(categorysFlowPaneWidth);
                        label.setPrefHeight(categorysFlowPaneHeight/10+10);
                        label.setText("蔬菜類");
                        label.setOnMouseClicked(event -> {
                            Integer index = categorysFlowPane.getChildren().indexOf(label);
                            if(index != -1){
                                for(Node otherLabel : categorysFlowPane.getChildren()){
                                    otherLabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                                }
                                label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                                itemsFlowPane.getChildren().remove(0,itemsFlowPane.getChildren().size());
                                this.refreshItems(itemsFlowPane,itemsFlowPaneWidth,itemsFlowPaneHeight,this,hbox);
                            }
                           // itemsFlowPane.getChildren().remove(0,itemsFlowPane.getChildren().size());
                            //refreshItems(itemsFlowPane,itemsFlowPaneWidth,itemsFlowPaneHeight,tasteIndexController,hBox);
                        });
                        if(i == 0){
                            label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: goldenrod; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                        }
                        else{
                            label.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                        }
                        categorysFlowPane.getChildren().add(label);
                    }

                }
            }
        }

    }

    public void refreshItems(FlowPane itemsFlowPane,double itemsFlowPaneWidth,double itemsFlowPaneHeight,TasteIndexController tasteIndexController,HBox hBox){
        for(int i = 0;i<20;i++){
            FlowPane itemFlowPane =  new FlowPane();
            double itemFlowPaneWidth =  itemsFlowPaneWidth/6;
            double itemFlowPaneHeight = itemsFlowPaneHeight/5;
            itemFlowPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightsalmon;");
            itemFlowPane.setPrefHeight(itemFlowPaneHeight);
            itemFlowPane.setPrefWidth(itemFlowPaneWidth);
            double itemImageWidth = itemFlowPaneWidth;
            double itemImageHeight = itemFlowPaneHeight/5*3;
            //菜品图片
            Image itemImage = new Image("/image/chicken.jpg",itemImageWidth,itemImageHeight,true,true);
            javafx.scene.image.ImageView itemImageView  = new javafx.scene.image.ImageView();
            itemImageView.setFitWidth(itemImageWidth);
            itemImageView.setFitHeight(itemImageHeight);
            itemImageView.setImage(itemImage);

   /*         itemLabel.setText("半隻貴妃雞仔蛋");

            itemLabel.setPrefWidth(itemFlowPaneWidth);
            itemLabel.setPrefHeight(itemLabelHeight);
            itemLabel.setStyle("-fx-background-color:#F5F5F5; -fx-font-size: 22px; -fx-text-fill: #1d1d1d; -fx-wrap-text: true");*/
            itemFlowPane.getChildren().add(itemImageView);
            FlowPane remarkFlowPane =  new FlowPane();
            double remarkFlowPaneWidth = itemFlowPaneWidth;
            double remarkFlowPaneHeight = itemFlowPaneHeight/5*2;
            remarkFlowPane.setPrefWidth(remarkFlowPaneWidth);
            remarkFlowPane.setPrefHeight(remarkFlowPaneHeight);
            double itemNameLabelWidth = itemImageWidth;
            double itemNameLabelHeight = itemFlowPaneHeight/5;
            Label itemNameLabel = new Label();
            itemNameLabel.setText("全只隻貴妃雞仔蛋");
            itemNameLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            itemNameLabel.setPrefWidth(itemNameLabelWidth);
            itemNameLabel.setPrefHeight(itemNameLabelHeight);
            remarkFlowPane.getChildren().add(itemNameLabel);
            Label codeLabel = new Label();
            codeLabel.setText("000"+i);
            codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill: #1d1d1d  ");
            double codeLabelWidth = itemImageWidth/2;
            double codeLabelHeight = itemFlowPaneHeight/5;
            codeLabel.setPrefWidth(codeLabelWidth);
            codeLabel.setPrefHeight(codeLabelHeight);
            remarkFlowPane.getChildren().add(codeLabel);
            Label priceLabel = new Label();
            priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; ");
            priceLabel.setText("35.00");
            double priceLabellWidth = itemImageWidth/2;
            double priceLabelHeight = itemFlowPaneHeight/5;
            priceLabel.setPrefWidth(priceLabellWidth);
            priceLabel.setPrefHeight(priceLabelHeight);
            remarkFlowPane.getChildren().add(priceLabel);
            itemFlowPane.getChildren().add(remarkFlowPane);
            itemsFlowPane.getChildren().add(itemFlowPane);
            itemFlowPane.setOnMousePressed(event -> {
                boolean isExist = false;
                Integer  selectedIndex = -1;
                ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
                for(int k = 0;k<tasteIndexController.getFoodTableView().getItems().size();k++){
                    if(goods.get(k).getGoodName().equals(codeLabel.getText()+ "半隻貴妃雞仔蛋")){
                        isExist = true;
                        selectedIndex = k;
                    }
                }
                if(!isExist){
                    goods.add(new Goods("1",codeLabel.getText() + "半隻貴妃雞仔蛋","35.00",""));
                    tasteIndexController.getFoodTableView().getSelectionModel().selectLast();
                }
                else{
                    if(selectedIndex != -1){
                        tasteIndexController.getFoodTableView().getItems().get(selectedIndex).setNumber((Integer.parseInt( tasteIndexController.getFoodTableView().getItems().get(selectedIndex).getNumber())+1) + "");

                    }
                }
                Integer totalNumber = 0;
                Double totalPrice = 0.00d;
                for(Goods good : goods){
                    totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                    totalPrice = totalPrice + Integer.parseInt(good.getNumber())*Double.parseDouble(good.getPrice());
                }
                tasteIndexController.getTotalNumberLabel().setText(goods.size()+"/" + totalNumber);
                tasteIndexController.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
               /* tasteIndexController.getFoodTableView().getItems().add(new Goods("1", itemLabel.getText(),priceLabel.getText(),""));
                tasteIndexController.getFoodTableView().getSelectionModel().selectLast();*/
                if(tasteIndexController.getSearchFoodController() != null){
                    tasteIndexController.getSearchFoodController().getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalNumberLabel().setText(tasteIndexController.getTotalNumberLabel().getText());
                    tasteIndexController.getSearchFoodController().getTotalPriceLabel().setText(tasteIndexController.getTotalPriceLabel().getText());
                }
                if(!codeLabel.getText().equals("0007")){
                    return;
                }
                FlowPane cateFlowPane = (FlowPane)(( FlowPane) hBox.getChildren().get(2)).getChildren().get(0);
                cateFlowPane.getChildren().remove(0,cateFlowPane.getChildren().size());
                double categorysFlowPaneWidth = primaryScreenBounds.getWidth()/10;
                double categorysFlowPaneHeight =  primaryScreenBounds.getHeight()/10*8;
                Label catlabel = new Label();
                catlabel.setPrefWidth(categorysFlowPaneWidth);
                catlabel.setPrefHeight(categorysFlowPaneHeight/10+10);
                catlabel.setText("食品口味");
                catlabel.setStyle(" -fx-wrap-text: true;-fx-text-fill: white;-fx-font-size: 30;  -fx-background-color: #34495E; -fx-alignment: center; -fx-font-weight: bolder;-fx-border-width: 0 0 2 0; -fx-border-color: white");
                cateFlowPane.getChildren().add(catlabel);
                itemsFlowPane.getChildren().remove(0,itemsFlowPane.getChildren().size());
                for(int k = 0;k<20;k++) {
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
                        FXMLLoader  confirmTasteloader = new FXMLLoader();
                        confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmTasteView.fxml"));
                        try {
                            FlowPane confirmTasteFlowPane  = confirmTasteloader.load();
                            double confirmTasteWidth = primaryScreenBounds.getWidth()/2;
                            double confirmTasteHeight = primaryScreenBounds.getHeight()/3;
                            confirmTasteFlowPane.setPrefHeight(confirmTasteHeight);
                            confirmTasteFlowPane.setPrefWidth(confirmTasteWidth);
                            Label label = (Label) confirmTasteFlowPane.getChildren().get(0);
                            label.setText("確定要暫停["+  itemTasteLabel.getText() + "]?");
                            double labelWidth = confirmTasteWidth;
                            double labelHeight = confirmTasteHeight/3*2;
                            label.setPrefWidth(labelWidth);
                            label.setPrefHeight(labelHeight);
                            FlowPane buttomFlowPane = (FlowPane) confirmTasteFlowPane.getChildren().get(1);
                            double buttomFlowPaneWidth = confirmTasteWidth;
                            double buttomFlowPaneHeight = confirmTasteHeight/3;
                            buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
                            buttomFlowPane.setPrefWidth(confirmTasteWidth);
                            buttomFlowPane.setHgap(buttomFlowPaneWidth/20);
                            Stage confirmStage = new Stage();
                            Button noButton = (Button) buttomFlowPane.getChildren().get(0);
                            noButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                            noButton.setPrefWidth(buttomFlowPaneWidth/3);
                            noButton.setOnAction(event2 -> {
                                confirmStage.close();
                            });
                            Button yesButton = (Button) buttomFlowPane.getChildren().get(1);
                            yesButton.setPrefHeight(buttomFlowPaneHeight/3*2);
                            yesButton.setPrefWidth(buttomFlowPaneWidth/3);
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

}
