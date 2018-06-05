package com.dw.controller;

import com.dw.Main;
import com.dw.model.Goods;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by lodi on 2018/1/9.
 */
@Getter
@Setter
public class BuckleStewIndexController {

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

    @FXML
    private FlowPane itemsFlowPane;

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

    @FXML
    private Label tableNoLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalNumberLabel;

    private Rectangle2D primaryScreenBounds;


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
    public void openKeyWord() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/SearchFoodView.fxml"));
        FlowPane searchFoodFlowPane =  loader.load();
        SearchFoodController  searchFoodController = loader.getController();
        searchFoodController.setPrimaryScreenBounds(primaryScreenBounds);
        double searchFoodFlowPaneWidth = primaryScreenBounds.getWidth()/10*4 + primaryScreenBounds.getWidth()/300;
        double searchFoodFlowPaneHeight = primaryScreenBounds.getHeight()/2;
        searchFoodFlowPane.setPrefHeight(searchFoodFlowPaneHeight);
        searchFoodFlowPane.setPrefWidth(searchFoodFlowPaneWidth);
        double labelWidth = searchFoodFlowPaneWidth/5;
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
        searchFoodController.getTextField().setPrefWidth(searchFoodFlowPaneWidth);
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
                            FXMLLoader fxmlLoader  = new FXMLLoader();
                            fxmlLoader.setLocation(Main.class.getResource("controller/view/ModifyBuckleStewView.fxml"));
                            try {
                                VBox searchBox = fxmlLoader.load();
                                ModifyBuckleStewController  modifyBuckleStewController = fxmlLoader.getController();
                                searchBox.setPrefHeight(primaryScreenBounds.getHeight()/3*2);
                                searchBox.setPrefWidth(primaryScreenBounds.getWidth());
                                //獲取搜索欄
                                FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
                                searchFlowPane.setHgap(primaryScreenBounds.getWidth()/8/4);
                                searchFlowPane.setPadding(new Insets(0,primaryScreenBounds.getWidth()/8/8,0,primaryScreenBounds.getWidth()/8/8));
                                //取台號搜索窗口的1/5
                                searchFlowPane.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5);
                                searchFlowPane.setPrefWidth(primaryScreenBounds.getWidth());

                                Label titleLabel= (Label) searchFlowPane.getChildren().get(0);
                                titleLabel.setPrefWidth(primaryScreenBounds.getWidth()/8*2);
                                titleLabel.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5/2);
                                //titleLabel.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                                //搜索欄
                                TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
                                searchTextField.setPrefWidth(primaryScreenBounds.getWidth()/8*4);
                                searchTextField.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5/2);
                                searchTextField.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder");
                                searchTextField.setPromptText("請輸入數量");

                                //去除按鈕
                                Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
                                deleteButton.setPrefWidth(primaryScreenBounds.getWidth()/8);
                                deleteButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5/2);
                                deleteButton.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                                //中间的键盘部分
                                HBox hBox = (HBox) searchBox.getChildren().get(1);
                                hBox.setPrefWidth(primaryScreenBounds.getWidth());
                                hBox.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3);
                                //英文键盘部分
                                FlowPane englishKeyPane = (FlowPane) hBox.getChildren().get(0);
                                englishKeyPane.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3);
                                englishKeyPane.setPrefWidth(primaryScreenBounds.getWidth()/4*3);

                                //第一行字母
                                FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
                                englishKeyPane1.setPrefWidth(primaryScreenBounds.getWidth()/4*3);
                                englishKeyPane1.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3);
                                englishKeyPane1.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                                englishKeyPane1.setHgap(5);
                                for(int j = 0;j<10;j++){
                                    Button englishButton = new Button();
                                    switch (j){
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
                                    englishButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3/3*2.5);
                                    englishButton.setPrefWidth(primaryScreenBounds.getWidth()/4*3/11);
                                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                    englishButton.setOnAction(event1 -> {
                                        modifyBuckleStewController.addTextField(englishButton.getText());
                                    });
                                    englishKeyPane1.getChildren().add(englishButton);
                                }



                                //第二行字母
                                FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
                                englishKeyPane2.setPrefWidth(primaryScreenBounds.getWidth()/4*3);
                                englishKeyPane2.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3);
                                englishKeyPane2.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                                englishKeyPane2.setHgap(5);
                                for(int j = 0;j<9;j++){
                                    Button englishButton = new Button();
                                    switch (j){
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
                                    englishButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3/3*2.5);
                                    englishButton.setPrefWidth(primaryScreenBounds.getWidth()/4*3/11);
                                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                    englishButton.setOnAction(event1 -> {
                                        modifyBuckleStewController.addTextField(englishButton.getText());
                                    });
                                    englishKeyPane2.getChildren().add(englishButton);
                                }


                                //第三行字母
                                FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
                                englishKeyPane3.setPrefWidth(primaryScreenBounds.getWidth()/4*3);
                                englishKeyPane3.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3);
                                englishKeyPane3.setStyle("-fx-background-color: #ebebeb;-fx-alignment: center;-fx-text-alignment: center");
                                englishKeyPane3.setHgap(5);
                                for(int j = 0;j<8;j++){
                                    Button englishButton = new Button();
                                    switch (j){
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
                                    englishButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/3/3*2.5);
                                    englishButton.setPrefWidth(primaryScreenBounds.getWidth()/4*3/11);
                                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                                    englishButton.setOnAction(event1 -> {
                                        modifyBuckleStewController.addTextField(englishButton.getText());
                                    });
                                    englishKeyPane3.getChildren().add(englishButton);
                                }

                                //获取右边的数字键盘
                                //英文键盘部分
                                FlowPane numberKeyPane = (FlowPane) hBox.getChildren().get(1);
                                numberKeyPane.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3);
                                numberKeyPane.setPrefWidth(primaryScreenBounds.getWidth()/4);
                                numberKeyPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center");
                                numberKeyPane.setHgap(5);
                                numberKeyPane.setVgap(10);
                                for(int k = 0;k<12;k++){
                                    Button numberButton = new Button();
                                    switch (k){
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
                                    numberButton.setPrefWidth(primaryScreenBounds.getWidth()/4/3-15);
                                    numberButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5*3/4*0.85);
                                    numberButton.setOnAction(event1 -> {
                                        modifyBuckleStewController.addTextField(numberButton.getText());
                                    });
                                    numberButton.setStyle("-fx-background-color: #e86238;-fx-font-size: 30;-fx-text-fill: white");
                                    numberKeyPane.getChildren().add(numberButton);
                                }

                                //获取底部
                                FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
                                buttonFlowPane.setHgap(primaryScreenBounds.getWidth()/8);
                                buttonFlowPane.setPrefWidth(primaryScreenBounds.getWidth());
                                buttonFlowPane.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5);
                                buttonFlowPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center; ");
                                Button closeButton = new Button();
                                closeButton.setPrefWidth(primaryScreenBounds.getWidth()/3);
                                closeButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5/3*2);
                                closeButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #aa5b5b");
                                closeButton.setText("關閉");
                                buttonFlowPane.getChildren().add(closeButton);
                                Button confirmButton = new Button();
                                confirmButton.setPrefWidth(primaryScreenBounds.getWidth()/3);
                                confirmButton.setPrefHeight(primaryScreenBounds.getHeight()/3*2/5/3*2);
                                confirmButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #5caa88");
                                confirmButton.setText("確定");
                                buttonFlowPane.getChildren().add(confirmButton);
                                Stage modifyBuckleStewStage = new Stage();
                                modifyBuckleStewStage.initOwner(getStage());
                                modifyBuckleStewStage.setScene(new Scene(searchBox));
                                modifyBuckleStewStage.initModality(Modality.APPLICATION_MODAL);
                                modifyBuckleStewStage.initStyle(StageStyle.TRANSPARENT);
                                modifyBuckleStewStage.show();
                                confirmButton.setOnAction(event -> {
                                    try {
                                        BigDecimal bigDecimal = new BigDecimal(modifyBuckleStewController.getTableNumberTextField().getText()).setScale(1,BigDecimal.ROUND_DOWN);
                                        String code = searchFoodController.getTextField().getText().split(" ")[0];
                                        ObservableList<Node> nodes = itemsFlowPane.getChildren();
                                        if(nodes != null && nodes.size()>0){
                                            for(Node node  : nodes ){
                                                FlowPane flowPane = (FlowPane) node;
                                                FlowPane remarkFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                                Label codeLabel = (Label) remarkFlowPane.getChildren().get(1);
                                                if(codeLabel.getText().equals(searchFoodController.getTextField().getText().split(" ")[0])){
                                                    Label priceLabel = (Label) remarkFlowPane.getChildren().get(2);
                                                    priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                                    priceLabel.setText(bigDecimal.toString());
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
                        confirmStage.initOwner(stage);
                        confirmStage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
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
        searchFoodController.getCButton().setPrefHeight(buttonHeight);
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
        searchFoodController.setBuckleStewIndexController(this);
        keywordStage.show();

    }

    @FXML
    public void close(){
        stage.close();
    }
}
