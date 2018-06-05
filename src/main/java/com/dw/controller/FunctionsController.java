package com.dw.controller;

import com.dw.Main;
import com.dw.view.DataSetView;
import com.dw.view.EtViewView;
import com.dw.view.MainView;
import com.dw.view.RePrintView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by lodi on 2017/12/21.
 */
@Getter
@Setter
@FXMLController
public class FunctionsController {

    @FXML
    private Button dataButton;

    @FXML
    private Button memberButton;

    @FXML
    private Button workerButton;

    @FXML
    private Button spareWatchButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button rePrintButton;

    @FXML
    private Button removeLockButton;

    @FXML
    private Button systemSettingButton;

    @FXML
    private Button otherMiscellaneousButton;

    @FXML
    private Button closeButton;

    @FXML
    private VBox root;
    @FXML
    private FlowPane functionsFlowPane;
    @FXML
    private FlowPane buttomFlowPane;

    private Stage funStage = null;

    @Autowired
    private MainView mainView;
    @Autowired
    private RePrintView rePrintView;
    @Autowired
    private EtViewView etViewView;

    @FXML
    private void initialize() {
        showFunctions();
    }


    public void showFunctions() {
        root.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
        double gap = 10.00;
        //按鈕數量
        int btnSize = 5;
        //底部按鈕的寬度
        double subNodeWidth = Main.primaryScreenBounds.getWidth() / btnSize - gap - btnSize / gap;
        //底部按鈕的高度
        double subNodeHeight = Main.primaryScreenBounds.getHeight() / 8 - 10;

        functionsFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 8 * 7);

        functionsFlowPane.setPadding(new Insets(10, 0, 0, 5));
        functionsFlowPane.setHgap(gap);
        functionsFlowPane.setVgap(gap);

        dataButton.setPrefSize(subNodeWidth, subNodeHeight);
        memberButton.setPrefSize(subNodeWidth, subNodeHeight);
        workerButton.setPrefSize(subNodeWidth, subNodeHeight);
        spareWatchButton.setPrefSize(subNodeWidth, subNodeHeight);
        restartButton.setPrefSize(subNodeWidth, subNodeHeight);
        rePrintButton.setPrefSize(subNodeWidth, subNodeHeight);
        removeLockButton.setPrefSize(subNodeWidth, subNodeHeight);
        systemSettingButton.setPrefSize(subNodeWidth, subNodeHeight);
        otherMiscellaneousButton.setPrefSize(subNodeWidth, subNodeHeight);
        buttomFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 8 * 1);
        buttomFlowPane.setPadding(new Insets(0, 10, 0, 0));
        buttomFlowPane.setAlignment(Pos.CENTER_RIGHT);
        closeButton.setPrefSize(subNodeWidth, subNodeHeight);
    }


    @FXML
    public void closeStage() {
        // Main.showInitialView(MainView.class);
        //啟動新的線程進行轉場，提高性能
        MainController mainController = (MainController) mainView.getPresenter();
        mainController.iniData();
        Main.showInitialView(mainView.getClass());
    }


    @FXML
    public void showDatas() {
        Main.showInitialView(DataSetView.class);
    }


    //會員功能
    @FXML
    public void showMemberFunction() throws IOException {
        FXMLLoader memberFunctionLoader = new FXMLLoader();
        memberFunctionLoader.setLocation(Main.class.getResource("controller/view/MemberFunction.fxml"));
        FlowPane memberFunctionFlowPane = memberFunctionLoader.load();
        MemberFunctionController memberFunctionController = memberFunctionLoader.getController();
        double memberFunctionWidth = Main.primaryScreenBounds.getWidth();
        double memberFunctionHeight = Main.primaryScreenBounds.getHeight();
        memberFunctionFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight());
        memberFunctionFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
        FlowPane topFlowPane1 = (FlowPane) memberFunctionFlowPane.getChildren().get(0);
        double topMemberFunctionWidth = Main.primaryScreenBounds.getWidth();
        double topMemberFunctionHeight = Main.primaryScreenBounds.getHeight() / 20;
        topFlowPane1.setPrefHeight(topMemberFunctionHeight);
        topFlowPane1.setPrefWidth(topMemberFunctionWidth);
        double labelWidth = Main.primaryScreenBounds.getWidth() / 5;
        memberFunctionController.getCardNoLabel().setPrefWidth(labelWidth);
        memberFunctionController.getNameLabel().setPrefWidth(labelWidth);
        memberFunctionController.getTelephoneLabel().setPrefWidth(labelWidth);
        memberFunctionController.getBirthdayLabel().setPrefWidth(labelWidth);
        FlowPane topFlowPane2 = (FlowPane) memberFunctionFlowPane.getChildren().get(1);
        topFlowPane2.setPrefHeight(topMemberFunctionHeight);
        topFlowPane2.setPrefWidth(topMemberFunctionWidth);
        memberFunctionController.getBalanceLabel().setPrefWidth(labelWidth);
        memberFunctionController.getGivingBalanceLabel().setPrefWidth(labelWidth);
        memberFunctionController.getIntegralLabel().setPrefWidth(labelWidth);
        memberFunctionController.getEndDateLabel().setPrefWidth(labelWidth);
        double consumerTableViewWidth = Main.primaryScreenBounds.getWidth();
        double consumerTableViewHeight = Main.primaryScreenBounds.getHeight() / 20 * 16;
        memberFunctionController.getConsumerTableView().setPrefWidth(consumerTableViewWidth);
        memberFunctionController.getConsumerTableView().setPrefHeight(consumerTableViewHeight);
        double columWidth = Main.primaryScreenBounds.getWidth() / 7;
        memberFunctionController.getDateColum().setPrefWidth(columWidth);
        memberFunctionController.getOrderNoColum().setPrefWidth(columWidth);
        memberFunctionController.getTranTypeColum().setPrefWidth(columWidth);
        memberFunctionController.getBranchColum().setPrefWidth(columWidth);
        memberFunctionController.getConsumerAmountColum().setPrefWidth(columWidth);
        memberFunctionController.getGivenAmountColum().setPrefWidth(columWidth);
        memberFunctionController.getGivenIntergalColum().setPrefWidth(columWidth);
        FlowPane buttomFlowPane = (FlowPane) memberFunctionFlowPane.getChildren().get(3);
        double buttomFlowPaneWidth = Main.primaryScreenBounds.getWidth();
        double buttomFlowPaneHeight = Main.primaryScreenBounds.getHeight() / 20 * 2;
        buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
        buttomFlowPane.setPrefWidth(buttomFlowPaneWidth);
        double buttonWidth = Main.primaryScreenBounds.getWidth() / 12;
        double buttonHeight = buttomFlowPaneHeight / 5 * 4;
        buttomFlowPane.setPadding(new Insets(buttonHeight / 10, 0, 0, buttonWidth / 5));
        buttomFlowPane.setHgap(buttonWidth / 13);
        memberFunctionController.getSearchButton().setPrefWidth(buttonWidth);
        memberFunctionController.getSearchButton().setPrefHeight(buttonHeight);
        memberFunctionController.getCheckCouponButton().setPrefHeight(buttonHeight);
        memberFunctionController.getCheckCouponButton().setPrefWidth(buttonWidth);
        memberFunctionController.getExchangeIntegralButton().setPrefWidth(buttonWidth * 2);
        memberFunctionController.getExchangeIntegralButton().setPrefHeight(buttonHeight);
        memberFunctionController.getSureExchangeButton().setPrefWidth(buttonWidth * 2);
        memberFunctionController.getSureExchangeButton().setPrefHeight(buttonHeight);
        memberFunctionController.getPrintButton().setPrefWidth(buttonWidth);
        memberFunctionController.getPrintButton().setPrefHeight(buttonHeight);
        memberFunctionController.getOtherButton().setPrefHeight(buttonHeight);
        memberFunctionController.getUpButton().setPrefWidth(buttonWidth / 5 * 3);
        memberFunctionController.getUpButton().setPrefHeight(buttonHeight);
        memberFunctionController.getDownButton().setPrefHeight(buttonHeight);
        memberFunctionController.getDownButton().setPrefWidth(buttonWidth / 5 * 3);
        memberFunctionController.getCloseButton().setPrefWidth(buttonWidth * 2);
        memberFunctionController.getCloseButton().setPrefHeight(buttonHeight);
        Stage memberFunctionStage = new Stage();
        memberFunctionController.getCloseButton().setOnAction(event -> {
            memberFunctionStage.close();
        });
        memberFunctionController.getSearchButton().setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("controller/view/SearchTableView.fxml"));
            try {
                VBox searchBox = fxmlLoader.load();
                SearchTableController searchTableController = fxmlLoader.getController();
                searchBox.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2);
                searchBox.setPrefWidth(Main.primaryScreenBounds.getWidth());
                //獲取搜索欄
                FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
                searchFlowPane.setHgap(Main.primaryScreenBounds.getWidth() / 8 / 4);
                searchFlowPane.setPadding(new Insets(0, Main.primaryScreenBounds.getWidth() / 8 / 8, 0, Main.primaryScreenBounds.getWidth() / 8 / 8));
                //取台號搜索窗口的1/5
                searchFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5);
                searchFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
                //清空按鈕
                Button clearAllButton = (Button) searchFlowPane.getChildren().get(0);
                clearAllButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 8);
                clearAllButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                clearAllButton.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                //搜索欄
                TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
                searchTextField.setPrefWidth(Main.primaryScreenBounds.getWidth() / 8 * 5);
                searchTextField.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                searchTextField.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder");
                searchTextField.setPromptText("請輸入要搜索的台號");

                //去除按鈕
                Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
                deleteButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 8);
                deleteButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 / 2);
                deleteButton.setStyle("-fx-font-size: 30px;-fx-font-weight: bolder; -fx-background-color:#aa5b5b; -fx-text-fill: white ");

                //中间的键盘部分
                HBox hBox = (HBox) searchBox.getChildren().get(1);
                hBox.setPrefWidth(Main.primaryScreenBounds.getWidth());
                hBox.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                //英文键盘部分
                FlowPane englishKeyPane = (FlowPane) hBox.getChildren().get(0);
                englishKeyPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                englishKeyPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3);

                //第一行字母
                FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
                englishKeyPane1.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3);
                englishKeyPane1.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
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
                    englishButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                    englishButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3 / 11);
                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                    englishButton.setOnAction(event1 -> {
                        searchTableController.addTextField(englishButton.getText());
                    });
                    englishKeyPane1.getChildren().add(englishButton);
                }


                //第二行字母
                FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
                englishKeyPane2.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3);
                englishKeyPane2.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
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
                    englishButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                    englishButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3 / 11);
                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                    englishButton.setOnAction(event1 -> {
                        searchTableController.addTextField(englishButton.getText());
                    });
                    englishKeyPane2.getChildren().add(englishButton);
                }


                //第三行字母
                FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
                englishKeyPane3.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3);
                englishKeyPane3.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3);
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
                    englishButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 3 / 3 * 2.5);
                    englishButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 * 3 / 11);
                    englishButton.setStyle("-fx-text-fill: white; -fx-background-color: #deb189; -fx-font-size: 30px;");
                    englishButton.setOnAction(event1 -> {
                        searchTableController.addTextField(englishButton.getText());
                    });
                    englishKeyPane3.getChildren().add(englishButton);
                }

                //获取右边的数字键盘
                //英文键盘部分
                FlowPane numberKeyPane = (FlowPane) hBox.getChildren().get(1);
                numberKeyPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3);
                numberKeyPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4);
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
                    numberButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 4 / 3 - 15);
                    numberButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 * 3 / 4 * 0.85);
                    numberButton.setOnAction(event1 -> {
                        searchTableController.addTextField(numberButton.getText());
                    });
                    numberButton.setStyle("-fx-background-color: #e86238;-fx-font-size: 30;-fx-text-fill: white");
                    numberKeyPane.getChildren().add(numberButton);
                }

                //获取底部
                FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
                buttonFlowPane.setHgap(Main.primaryScreenBounds.getWidth() / 8);
                buttonFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
                buttonFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5);
                buttonFlowPane.setStyle("-fx-background-color: #ebebeb; -fx-alignment: center; ");
                Button closeButton = new Button();
                closeButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3);
                closeButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                closeButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #aa5b5b");
                closeButton.setText("關閉");
                buttonFlowPane.getChildren().add(closeButton);
                Button confirmButton = new Button();
                confirmButton.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3);
                confirmButton.setPrefHeight(Main.primaryScreenBounds.getHeight() / 3 * 2 / 5 / 3 * 2);
                confirmButton.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bolder; -fx-background-color: #5caa88");
                confirmButton.setText("確定");
                buttonFlowPane.getChildren().add(confirmButton);
                Stage searchTableStage = new Stage();
                searchTableStage.initOwner(memberFunctionStage);
                searchTableStage.setScene(new Scene(searchBox));
                searchTableStage.show();
                closeButton.setOnAction(event1 -> {
                    searchTableStage.close();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        memberFunctionStage.setScene(new Scene(memberFunctionFlowPane));
        memberFunctionStage.setIconified(false);
        memberFunctionStage.setX(Main.primaryScreenBounds.getMinX());
        memberFunctionStage.setY(Main.primaryScreenBounds.getMinY());
        memberFunctionStage.initModality(Modality.NONE);
        memberFunctionStage.initStyle(StageStyle.TRANSPARENT);
        memberFunctionStage.show();

    }


    //員工設定
    @FXML
    public void WorkersSet() throws IOException {
        etViewView.showViewAndWait(functionsFlowPane.getScene().getWindow(), Modality.APPLICATION_MODAL);
    }


    //備用金表
    @FXML
    public void showStandbyMoney() throws IOException {
        FXMLLoader standbyMoneyLoader = new FXMLLoader();
        standbyMoneyLoader.setLocation(Main.class.getResource("controller/view/StandbyMoney.fxml"));
        FlowPane standByMoneyFlowPane = standbyMoneyLoader.load();
        StandbyMoneyController standbyMoneyController = standbyMoneyLoader.getController();
        double standByMoneyWidth = Main.primaryScreenBounds.getWidth();
        double standByMoneyHeight = Main.primaryScreenBounds.getHeight();
        standByMoneyFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight());
        standByMoneyFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
        double tableViewWidth = Main.primaryScreenBounds.getWidth();
        double tableViewHeight = Main.primaryScreenBounds.getHeight() / 10 * 8;
        standbyMoneyController.getMoneyTableView().setPrefHeight(tableViewHeight);
        standbyMoneyController.getMoneyTableView().setPrefWidth(tableViewWidth);
        double columWidth = Main.primaryScreenBounds.getWidth() / 7;
        standbyMoneyController.getMachineNoColum().setPrefWidth(columWidth);
        standbyMoneyController.getTimeColum().setPrefWidth(columWidth);
        standbyMoneyController.getSerialNumberColum().setPrefWidth(columWidth);
        standbyMoneyController.getReasonColum().setPrefWidth(columWidth * 2);
        standbyMoneyController.getSilverHeadColum().setPrefWidth(columWidth);
        standbyMoneyController.getDeliveryColum().setPrefWidth(columWidth);
        FlowPane buttomFlowPane = (FlowPane) standByMoneyFlowPane.getChildren().get(1);
        double buttomPaneWidth = Main.primaryScreenBounds.getWidth();
        double buttomPaneHeight = Main.primaryScreenBounds.getHeight() / 10 * 2;
        buttomFlowPane.setPrefWidth(buttomPaneWidth);
        buttomFlowPane.setPrefHeight(buttomPaneHeight);
        buttomFlowPane.setPadding(new Insets(buttomPaneHeight / 30, 0, 0, buttomPaneWidth / 120));
        buttomFlowPane.setHgap(buttomPaneWidth / 150);
        double buttonWidth = buttomPaneWidth / 10;
        double buttonHeight = buttomPaneHeight / 10 * 9;
        standbyMoneyController.getSilverHeadButton().setPrefHeight(buttonHeight);
        standbyMoneyController.getSilverHeadButton().setPrefWidth(buttonWidth);
        standbyMoneyController.getDeliveryButton().setPrefHeight(buttonHeight);
        standbyMoneyController.getDeliveryButton().setPrefWidth(buttonWidth);
        FlowPane centerFlowPane = (FlowPane) buttomFlowPane.getChildren().get(2);
        double centerFlowPaneWidth = buttomPaneWidth / 10 * 6;
        double centerFlowPaneHeight = buttonHeight;
        centerFlowPane.setPrefHeight(centerFlowPaneHeight);
        centerFlowPane.setPrefWidth(centerFlowPaneWidth);
        double labelWidth = centerFlowPaneWidth / 3 - 2;
        double labelHeight = centerFlowPaneHeight - 4;
        standbyMoneyController.getTotalChargeLabel().setPrefHeight(labelHeight);
        standbyMoneyController.getTotalChargeLabel().setPrefWidth(labelWidth);
        standbyMoneyController.getSilverHeadLabel().setPrefHeight(labelHeight);
        standbyMoneyController.getSilverHeadLabel().setPrefWidth(labelWidth);
        standbyMoneyController.getDeliveryLabel().setPrefWidth(labelWidth);
        standbyMoneyController.getDeliveryLabel().setPrefHeight(labelHeight);
        standbyMoneyController.getCloseButton().setPrefHeight(buttonHeight);
        standbyMoneyController.getCloseButton().setPrefWidth(buttonWidth);
        FlowPane rightFlowPane = (FlowPane) buttomFlowPane.getChildren().get(4);
        rightFlowPane.setPrefWidth(buttonWidth / 2);
        rightFlowPane.setPrefHeight(buttonHeight);
        standbyMoneyController.getUpButton().setPrefWidth(buttonWidth / 2);
        standbyMoneyController.getUpButton().setPrefHeight(buttonHeight / 2);
        standbyMoneyController.getDownButton().setPrefHeight(buttonHeight / 2);
        standbyMoneyController.getDownButton().setPrefWidth(buttonWidth / 2);
        Stage standByMoneyStage = new Stage();
        standbyMoneyController.getCloseButton().setOnAction(event -> {
            standByMoneyStage.close();
        });
        standByMoneyStage.setScene(new Scene(standByMoneyFlowPane));
        standByMoneyStage.initModality(Modality.NONE);
        standByMoneyStage.initStyle(StageStyle.TRANSPARENT);
        standbyMoneyController.setStandbyMoneyStage(standByMoneyStage);
        standByMoneyStage.setX(Main.primaryScreenBounds.getMinX());
        standByMoneyStage.setY(Main.primaryScreenBounds.getMinY());
//        standbyMoneyController.setMain.primaryScreenBounds(Main.primaryScreenBounds);
        standByMoneyStage.show();
    }

    //開機重啓
    @FXML
    public void showRestartView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/RestartView.fxml"));
        FlowPane restartFlowPane = loader.load();
        RestartController restartController = loader.getController();
        double restartWidth = Main.primaryScreenBounds.getWidth() / 3;
        double restartHeight = Main.primaryScreenBounds.getHeight() / 4 * 3;
        restartFlowPane.setPrefHeight(restartHeight);
        restartFlowPane.setPrefWidth(restartWidth);
        Label titleLabel = (Label) restartFlowPane.getChildren().get(0);
        double titleLabelWidth = restartWidth;
        double titleLabelHeight = restartHeight / 6;
        titleLabel.setPrefWidth(titleLabelWidth);
        titleLabel.setPrefHeight(titleLabelHeight);
        FlowPane contentFlowPane = (FlowPane) restartFlowPane.getChildren().get(1);
        double contentFlowPaneWidth = Main.primaryScreenBounds.getWidth() / 3;
        double contentFlowPaneHeight = restartHeight / 6 * 3;
        contentFlowPane.setPrefWidth(contentFlowPaneWidth);
        contentFlowPane.setPrefHeight(contentFlowPaneHeight);
        double buttonWidth = contentFlowPaneWidth / 3 * 2;
        double buttonHeight = contentFlowPaneHeight / 4;
        contentFlowPane.setPadding(new Insets(buttonHeight / 5, 0, 0, buttonWidth / 5));
        contentFlowPane.setVgap(buttonHeight / 5);
        restartController.getRestartButton().setPrefWidth(buttonWidth);
        restartController.getRestartButton().setPrefHeight(buttonHeight);
        restartController.getTurnOffButton().setPrefWidth(buttonWidth);
        restartController.getTurnOffButton().setPrefHeight(buttonHeight);
        restartController.getExitButton().setPrefHeight(buttonHeight);
        restartController.getExitButton().setPrefWidth(buttonWidth);
        FlowPane buttomFlowPane = (FlowPane) restartFlowPane.getChildren().get(2);
        double buttomFlowPaneWidth = Main.primaryScreenBounds.getWidth() / 3;
        double buttomFlowPaneHeight = restartHeight / 6 * 2;
        buttomFlowPane.setPrefWidth(buttomFlowPaneWidth);
        buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
        restartController.getCloseButton().setPrefHeight(buttonHeight);
        restartController.getCloseButton().setPrefWidth(buttonWidth);
        Stage restartViewStage = new Stage();
        restartController.setStage(restartViewStage);
        restartViewStage.setScene(new Scene(restartFlowPane));
        restartViewStage.initModality(Modality.APPLICATION_MODAL);
        restartViewStage.initStyle(StageStyle.TRANSPARENT);
        restartViewStage.show();
    }

    //重印廚飛
    @FXML
    public void showRePrintView() throws IOException {
        Main.showInitialView(rePrintView.getClass());

    }


    //其他雜項
    @FXML
    public void showOtherView() throws IOException {
        FXMLLoader otherLoader = new FXMLLoader();
        otherLoader.setLocation(Main.class.getResource("controller/view/OtherView.fxml"));
        FlowPane flowPane = otherLoader.load();
        double otherViewWidth = Main.primaryScreenBounds.getWidth() / 10 * 8;
        double otherViewHeight = Main.primaryScreenBounds.getHeight() / 18 * 17;
        double oneotherViewWidth = Main.primaryScreenBounds.getWidth() / 10;
        double oneotherViewHeight = Main.primaryScreenBounds.getHeight() / 18;
        OtherController otherController = otherLoader.getController();
        flowPane.setPrefHeight(otherViewHeight);
        flowPane.setPrefWidth(otherViewWidth);
        FlowPane buttonsFlowPane = (FlowPane) flowPane.getChildren().get(0);
        buttonsFlowPane.setHgap(otherViewWidth / 20);
        buttonsFlowPane.setVgap(otherViewWidth / 20);
        buttonsFlowPane.setPrefHeight(oneotherViewHeight * 15);
        buttonsFlowPane.setPrefWidth(otherViewWidth);
        buttonsFlowPane.setPadding(new Insets(oneotherViewHeight, 0, 0, oneotherViewHeight));
        otherController.getPrintTestButton().setPrefWidth(otherViewWidth / 4);
        otherController.getAnalogyToDownload().setPrefWidth(otherViewWidth / 4);
//        otherController.setMain.primaryScreenBounds(Main.primaryScreenBounds);
        FlowPane buttomFlowPane = (FlowPane) flowPane.getChildren().get(1);
        buttomFlowPane.setPrefHeight(oneotherViewHeight * 2);
        buttomFlowPane.setPrefWidth(otherViewWidth);
        buttomFlowPane.setAlignment(Pos.TOP_RIGHT);
        otherController.getCloseButton().setPrefWidth(otherViewWidth / 4);
        otherController.getCloseButton().setOnAction(event -> {
            FXMLLoader functionLoader = new FXMLLoader();
            functionLoader.setLocation(Main.class.getResource("controller/view/Functions.fxml"));
            try {
                FlowPane functionslowPane = functionLoader.load();
                FunctionsController functionsController = functionLoader.getController();
                functionsController.showFunctions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.getChildren().add(flowPane);
    }


}
