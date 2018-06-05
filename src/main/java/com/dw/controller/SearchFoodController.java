package com.dw.controller;

import com.dw.Main;
import com.dw.model.Goods;
import com.dw.util.NumberFormatUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
 * Created by lodi on 2018/1/3.
 */
@Getter
@Setter
public class SearchFoodController {

    @FXML
    private Label tableNoLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalNumberLabel;

    @FXML
    private  TextField textField;

    @FXML
    private Button numberButton;

    @FXML
    private Button changPriceButton;

    @FXML
    private Button   taseButton;

    @FXML
    private Button  addButton;

    @FXML
    private Button  subButton;

    @FXML
    private Button sevenButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button   nineButton;

    @FXML
    private Button  aButton;

    @FXML
    private Button  searchFoodButton;

    @FXML
    private Button fourButton;

    @FXML
    private Button fiveButton;

    @FXML
    private Button sixButton;

    @FXML
    private Button bButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button oneButton;

    @FXML
    private Button twoButton;

    @FXML
    private Button threeButton;

    @FXML
    private Button  cButton;

    @FXML
    private Button reInputButton;

    @FXML
    private Button  zeroButton;

    @FXML
    private Button  pointButton;

    @FXML
    private Button  vButton;

    @FXML
    private Button  okButton;

    private Stage keyWordStage;

    private Rectangle2D primaryScreenBounds;


    private TasteIndexController tasteIndexController;

    private FoodIndexController foodIndexController;

    private BuckleStewIndexController buckleStewIndexController;


    @FXML
    public void inputZero(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ zeroButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(zeroButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputOne(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ oneButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(oneButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputTwo(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ twoButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(twoButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputThree(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ threeButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(threeButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputFour(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ fourButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(fourButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputFive(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ fiveButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(fiveButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputSix(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ sixButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(sixButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputSeven(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ sevenButton.getText());
        }
       else  if(textField.getText().length() >= 4){
            textField.setText(sevenButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputRight(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ rightButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(rightButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputNine(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ nineButton.getText());
        }
       else  if(textField.getText().length() >= 4){
            textField.setText(nineButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputPoint(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ pointButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(pointButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputA(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ aButton.getText());
        }
       else  if(textField.getText().length() >= 4){
            textField.setText(aButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputB(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ bButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(bButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputC(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ cButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(cButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void inputV(){
        if(textField.getText().length()<4){
            textField.setText(textField.getText()+ vButton.getText());
        }
        else if(textField.getText().length() >= 4){
            textField.setText(vButton.getText());
        }
        numberButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
        changPriceButton.setStyle("-fx-background-color: #5E9BB5; -fx-font-size: 30px; -fx-text-fill: white;-fx-alignment: center");
    }
    @FXML
    public void delete(){
        if(textField.getText().length()>0 && textField.getText().length()<=4){
            textField.setText(textField.getText().substring(0,textField.getText().length()-1));
            if(textField.getText().length() == 0){
                getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
            }
        }
    }
    @FXML
    public void reInput(){
            textField.setText("");
        getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
        getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
    }

    @FXML
    public void closeKeyWordView(){
      keyWordStage.close();
    }

    @FXML
    public  void changNumber() throws IOException {
        try {
            if(textField.getText().length()>0){
                if(foodIndexController != null){
                    return;
                }
               else if(buckleStewIndexController != null){
                    return;
               }
               else if(tasteIndexController != null){
                    if(tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem() == null){
                        FXMLLoader  loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
                        FlowPane confirmFlowPane = loader.load();
                        double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
                        double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
                        confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
                        confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
                        double labelWidth =  confirmFlowPaneWidth;
                        double labelHeight = confirmFlowPaneHeight/3*2;
                        Label label = (Label) confirmFlowPane.getChildren().get(0);
                        label.setText("請先選擇菜品");
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
                        confirmStage.initOwner(tasteIndexController.getStage());
                        confirmStage.setScene(new Scene(confirmFlowPane));
                        confirmStage.initModality(Modality.APPLICATION_MODAL);
                        confirmStage.initStyle(StageStyle.TRANSPARENT);
                        confirmStage.show();
                        return;
                    }
                    ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
                    Integer number = Integer.parseInt(textField.getText());
                    tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem().setNumber(number.toString());
                    textField.setText("");
                    Integer totalNumber = 0;
                    Double totalPrice = 0.00d;
                    for(Goods good : goods){
                        totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                        totalPrice = totalPrice + Integer.parseInt(good.getNumber())*Double.parseDouble(good.getPrice());
                    }
                    getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                    getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");

                    this.getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
                    this.getTotalNumberLabel().setText( tasteIndexController.getFoodTableView().getItems().size() + "/" + totalNumber);
                    this.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
                    tasteIndexController.getTotalNumberLabel().setText(this.getTotalNumberLabel().getText());
                    tasteIndexController.getTotalPriceLabel().setText(this.getTotalPriceLabel().getText());


               }
            }

        } catch (NumberFormatException e) {
            textField.setText("");
            FXMLLoader  loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
            FlowPane confirmFlowPane = loader.load();
            double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
            double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
            confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
            confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
            double labelWidth =  confirmFlowPaneWidth;
            double labelHeight = confirmFlowPaneHeight/3*2;
            Label label = (Label) confirmFlowPane.getChildren().get(0);
            label.setText("只能輸入數字");
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
            confirmStage.initOwner(tasteIndexController.getStage());
            confirmStage.setScene(new Scene(confirmFlowPane));
            confirmStage.initModality(Modality.APPLICATION_MODAL);
            confirmStage.initStyle(StageStyle.TRANSPARENT);
            confirmStage.show();
        }

    }

    @FXML
    public  void changPrice() throws IOException {
        try {
            if(textField.getText().length()>0){
                if(foodIndexController != null){
                    return;
                }
                else if(buckleStewIndexController != null){
                    return;
                }
                else if(tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem() == null){
                    FXMLLoader  loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
                    FlowPane confirmFlowPane = loader.load();
                    double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
                    double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
                    confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
                    confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
                    double labelWidth =  confirmFlowPaneWidth;
                    double labelHeight = confirmFlowPaneHeight/3*2;
                    Label label = (Label) confirmFlowPane.getChildren().get(0);
                    label.setText("請先選擇菜品");
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
                    confirmStage.initOwner(tasteIndexController.getStage());
                    confirmStage.setScene(new Scene(confirmFlowPane));
                    confirmStage.initModality(Modality.APPLICATION_MODAL);
                    confirmStage.initStyle(StageStyle.TRANSPARENT);
                    confirmStage.show();
                    return;
                }
                ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
                Double price = Double.parseDouble(textField.getText());
             /*   BigDecimal bg = new BigDecimal(price);
                double p = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();*/
                tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem().setPrice(NumberFormatUtils.KeepTwoDecimalPlaces(price));
                textField.setText("");
                Integer totalNumber = 0;
                Double totalPrice = 0.00d;
                for(Goods good : goods){
                    totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                    totalPrice = totalPrice + Integer.parseInt(good.getNumber())*Double.parseDouble(good.getPrice());
                }
                getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                this.getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
                this.getTotalNumberLabel().setText( tasteIndexController.getFoodTableView().getItems().size() + "/" + totalNumber);
                this.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
                tasteIndexController.getTotalNumberLabel().setText(this.getTotalNumberLabel().getText());
                tasteIndexController.getTotalPriceLabel().setText(this.getTotalPriceLabel().getText());
            }
        } catch (NumberFormatException e) {
            textField.setText("");
            FXMLLoader  loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
            FlowPane confirmFlowPane = loader.load();
            double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
            double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
            confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
            confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
            double labelWidth =  confirmFlowPaneWidth;
            double labelHeight = confirmFlowPaneHeight/3*2;
            Label label = (Label) confirmFlowPane.getChildren().get(0);
            label.setText("只能輸入數字");
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
            confirmStage.initOwner(tasteIndexController.getStage());
            confirmStage.setScene(new Scene(confirmFlowPane));
            confirmStage.initModality(Modality.APPLICATION_MODAL);
            confirmStage.initStyle(StageStyle.TRANSPARENT);
            confirmStage.show();
        }

    }

    @FXML
    public  void addNumber() {
        if(tasteIndexController != null){
            Goods good =  tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem();
            if(good != null){
                good.setNumber((Integer.parseInt(good.getNumber()) + 1) + "");
            }
            ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
            Integer totalNumber = 0;
            Double totalPrice = 0.00d;
            for(Goods g : goods){
                totalNumber = totalNumber + Integer.parseInt(g.getNumber());
                totalPrice = totalPrice + Integer.parseInt(g.getNumber())*Double.parseDouble(g.getPrice());
            }
            this.getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
            this.getTotalNumberLabel().setText( tasteIndexController.getFoodTableView().getItems().size() + "/" + totalNumber);
            this.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
            tasteIndexController.getTotalNumberLabel().setText(this.getTotalNumberLabel().getText());
            tasteIndexController.getTotalPriceLabel().setText(this.getTotalPriceLabel().getText());
        }
    }


    @FXML
    public  void subNumber() {
        if(tasteIndexController != null){
            ObservableList<Goods> goods = tasteIndexController.getFoodTableView().getItems();
            Goods good =  tasteIndexController.getFoodTableView().getSelectionModel().getSelectedItem();
            if(good != null && Integer.parseInt(good.getNumber())>1){
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
            this.getTableNoLabel().setText(tasteIndexController.getTableNoLabel().getText());
            this.getTotalNumberLabel().setText( tasteIndexController.getFoodTableView().getItems().size() + "/" + totalNumber);
            this.getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
            tasteIndexController.getTotalNumberLabel().setText(this.getTotalNumberLabel().getText());
            tasteIndexController.getTotalPriceLabel().setText(this.getTotalPriceLabel().getText());
        }

    }

    @FXML
    public  void searchFood() throws IOException {
          String s = textField.getText();
          if(s.length()>0 && s.length()<4){
              Integer zeroCount = 4 - s.length();
              String foodCode = "";
              for(int i =0;i<zeroCount;i++){
                  foodCode = foodCode + "0";
              }
              if("0007".equals(foodCode + s) && foodIndexController != null){
                  textField.setText(foodCode + s +" 半隻貴妃雞仔蛋");
                  FXMLLoader confirmTasteloader = new FXMLLoader();
                  confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmFoodView.fxml"));
                  try {
                      FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                      double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                      double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                      confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                      confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                      Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                      label.setText("確定要暫停[" + "半隻貴妃雞仔蛋" + "]?");
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
                          ObservableList<Node> nodes = foodIndexController.getItemsFlowPane().getChildren();
                          if(nodes != null && nodes.size()>0){
                              for(Node node  : nodes ){
                                  FlowPane flowPane = (FlowPane) node;
                                  FlowPane remarkFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                  Label codeLabel = (Label) remarkFlowPane.getChildren().get(1);
                                  if(codeLabel.getText().equals(getTextField().getText().split(" ")[0])){
                                      codeLabel.setStyle("-fx-background-color:#F5F5F5; -fx-alignment: center; -fx-font-size: 15px; -fx-text-fill:red");
                                      Label priceLabel = (Label) remarkFlowPane.getChildren().get(2);
                                      priceLabel.setText("暫停");
                                      priceLabel.setStyle("-fx-background-color:#F5F5F5; -fx-text-alignment: center;-fx-font-size: 15px; -fx-text-fill: red ");
                                  }
                              }
                          }
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
                  return;
              }
              else if("0007".equals(foodCode + s) && buckleStewIndexController != null){
                  textField.setText(foodCode + s +" 半隻貴妃雞仔蛋");
                  FXMLLoader confirmTasteloader = new FXMLLoader();
                  confirmTasteloader.setLocation(Main.class.getResource("controller/view/ConfirmBuckleStewView.fxml"));
                  try {
                      FlowPane confirmFoodFlowPane = confirmTasteloader.load();
                      double confirmFoodWidth = primaryScreenBounds.getWidth() / 2;
                      double confirmFoodHeight = primaryScreenBounds.getHeight() / 3;
                      confirmFoodFlowPane.setPrefHeight(confirmFoodHeight);
                      confirmFoodFlowPane.setPrefWidth(confirmFoodWidth);
                      Label label = (Label) confirmFoodFlowPane.getChildren().get(0);
                      label.setText("此食品沒有限售數量,是否輸入");
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
                              modifyBuckleStewStage.initOwner(buckleStewIndexController.getStage());
                              modifyBuckleStewStage.setScene(new Scene(searchBox));
                              modifyBuckleStewStage.initModality(Modality.APPLICATION_MODAL);
                              modifyBuckleStewStage.initStyle(StageStyle.TRANSPARENT);
                              modifyBuckleStewStage.show();
                              confirmButton.setOnAction(event -> {
                                  try {
                                      BigDecimal bigDecimal = new BigDecimal(modifyBuckleStewController.getTableNumberTextField().getText()).setScale(1,BigDecimal.ROUND_DOWN);
                                      String code = getTextField().getText().split(" ")[0];
                                      ObservableList<Node> nodes = buckleStewIndexController.getItemsFlowPane().getChildren();
                                      if(nodes != null && nodes.size()>0){
                                          for(Node node  : nodes ){
                                              FlowPane flowPane = (FlowPane) node;
                                              FlowPane remarkFlowPane = (FlowPane) flowPane.getChildren().get(1);
                                              Label codeLabel = (Label) remarkFlowPane.getChildren().get(1);
                                              if(codeLabel.getText().equals(getTextField().getText().split(" ")[0])){
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
                      confirmStage.initModality(Modality.NONE);
                      confirmStage.initStyle(StageStyle.TRANSPARENT);
                      confirmStage.initOwner(buckleStewIndexController.getStage());
                      confirmStage.show();

                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  return;
              }
              else if("0007".equals(foodCode + s) && tasteIndexController != null){
                  textField.setText(foodCode + s + " 半隻貴妃雞仔蛋");
                  getNumberButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                  getChangPriceButton().setStyle("-fx-background-color: #ADADAD;-fx-text-fill: #6E6E6E;-fx-font-size: 30px;-fx-alignment: center;");
                  ObservableList<Goods> goods =  tasteIndexController.getFoodTableView().getItems();
                  boolean isExist = false;
                  Integer  selectedIndex = -1;
                  for(int i = 0;i<goods.size();i++){
                      if(goods.get(i).getGoodName().equals("0007半隻貴妃雞仔蛋")){
                          isExist = true;
                          selectedIndex = i;
                      }
                  }
                  if(!isExist){
                      tasteIndexController.getFoodTableView().getItems().add(new Goods("1","0007半隻貴妃雞仔蛋","35.00",""));
                      tasteIndexController.getFoodTableView().getSelectionModel().selectLast();
                  }
                  else{
                      if(selectedIndex != -1){
                          tasteIndexController.getFoodTableView().getItems().get(selectedIndex).setNumber((Integer.parseInt(tasteIndexController.getFoodTableView().getItems().get(selectedIndex).getNumber())+1) + "");
                      }
                  }
                  Integer totalNumber = 0;
                  Double totalPrice = 0.00d;
                  for(Goods good : goods){
                      totalNumber = totalNumber + Integer.parseInt(good.getNumber());
                      totalPrice = totalPrice + Integer.parseInt(good.getNumber())*Double.parseDouble(good.getPrice());
                  }
                  getTableNoLabel().setText(tableNoLabel.getText());
                  getTotalNumberLabel().setText(tasteIndexController.getFoodTableView().getItems().size() + "/" + totalNumber);
                  getTotalPriceLabel().setText(NumberFormatUtils.KeepTwoDecimalPlaces(totalPrice));
                  totalNumberLabel.setText(getTotalNumberLabel().getText());
                  totalPriceLabel.setText(getTotalPriceLabel().getText());
                  tasteIndexController.getItemsFlowPane().getChildren().remove(0, tasteIndexController.getItemsFlowPane().getChildren().size());
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
                              confirmStage.initOwner(tasteIndexController.getStage());
                              confirmStage.show();

                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      });
                      tasteIndexController.getItemsFlowPane().getChildren().add(itemTasteFlowPane);
                  }
              }
              else{
                  textField.setText("");
                  FXMLLoader  confirmloader = new FXMLLoader();
                  confirmloader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
                  FlowPane confirmFlowPane = confirmloader.load();
                  double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
                  double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
                  confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
                  confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
                  double labelWidth =  confirmFlowPaneWidth;
                  double labelHeight = confirmFlowPaneHeight/3*2;
                  Label label = (Label) confirmFlowPane.getChildren().get(0);
                  label.setText("沒有該食品");
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
                  if(foodIndexController != null){
                      confirmStage.initOwner(foodIndexController.getStage());
                  }
                  else if(tasteIndexController != null){
                      confirmStage.initOwner(tasteIndexController.getStage());
                  }
                  if(buckleStewIndexController != null){
                      confirmStage.initOwner(buckleStewIndexController.getStage());
                  }
                  confirmStage.setScene(new Scene(confirmFlowPane));
                  confirmStage.initModality(Modality.APPLICATION_MODAL);
                  confirmStage.initStyle(StageStyle.TRANSPARENT);
                  confirmStage.show();
              }
          }
          else{
              textField.setText("");
              FXMLLoader  confirmloader = new FXMLLoader();
              confirmloader.setLocation(Main.class.getResource("controller/view/ConfirmView.fxml"));
              FlowPane confirmFlowPane = confirmloader.load();
              double confirmFlowPaneWidth =  primaryScreenBounds.getWidth()/3;
              double confirmFlowPaneHeight = primaryScreenBounds.getHeight()/3;
              confirmFlowPane.setPrefHeight(confirmFlowPaneHeight);
              confirmFlowPane.setPrefWidth(confirmFlowPaneWidth);
              double labelWidth =  confirmFlowPaneWidth;
              double labelHeight = confirmFlowPaneHeight/3*2;
              Label label = (Label) confirmFlowPane.getChildren().get(0);
              label.setText("沒有該食品");
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
              if(foodIndexController != null){
                  confirmStage.initOwner(foodIndexController.getStage());
              }
              else if(tasteIndexController != null){
                  confirmStage.initOwner(tasteIndexController.getStage());
              }
              if(buckleStewIndexController != null){
                  confirmStage.initOwner(buckleStewIndexController.getStage());
              }
              confirmStage.setScene(new Scene(confirmFlowPane));
              confirmStage.initModality(Modality.APPLICATION_MODAL);
              confirmStage.initStyle(StageStyle.TRANSPARENT);
              confirmStage.show();
          }

    }


}
