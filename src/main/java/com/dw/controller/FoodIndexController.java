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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
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
public class FoodIndexController {

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
                    confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmFoodView.fxml"));
                    try {
                        FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                        double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                        double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                        confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                        confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                        Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                        label.setText("確定要暫停[" +  searchFoodController.getTextField().getText().split(" ")[1] + "]?");
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
                            ObservableList<Node> nodes = itemsFlowPane.getChildren();
                            if(nodes != null && nodes.size()>0){
                                for(Node node  : nodes ){
                                    FlowPane flowPane = (FlowPane) node;
                                   FlowPane remarkFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                   Label codeLabel = (Label) remarkFlowPane.getChildren().get(1);
                                   if(codeLabel.getText().equals(searchFoodController.getTextField().getText().split(" ")[0])){
                                       Label priceLabel = (Label) remarkFlowPane.getChildren().get(2);
                                       priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                       priceLabel.setText("暫停");
                                       codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                   }
                                }
                            }
                            confirmStage.close();
                        });
                        confirmStage.setScene(new Scene(confirmFoodFlowPane));
                        confirmStage.initModality(Modality.NONE);
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
        searchFoodController.setFoodIndexController(this);
        keywordStage.show();

    }

    @FXML
    public void close(){
        stage.close();
    }
}
