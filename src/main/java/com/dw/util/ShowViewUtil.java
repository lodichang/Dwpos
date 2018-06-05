package com.dw.util;

import com.dw.Main;
import com.dw.controller.SearchTableController;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.ResultEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lodi on 2018/4/11.
 */
public class ShowViewUtil {

    public static String showWarningView(String title, String content, Map<String, String> map, Stage parentStage) {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        StringBuffer result = new StringBuffer("");
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.NONE);
        confirmStage.initStyle(StageStyle.TRANSPARENT);
        if (parentStage == null) {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            confirmStage.initOwner(stage.get(0));
        } else {
            confirmStage.initOwner(parentStage);
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/dw/view/WarningView.fxml"));
            FlowPane flowPane = null;
            flowPane = loader.load();

            double flowPaneWidth = primaryScreenBounds.getWidth() / 2;
            double flowPaneHeight = primaryScreenBounds.getHeight() / 3;
            flowPane.setPrefHeight(flowPaneHeight);
            flowPane.setPrefWidth(flowPaneWidth);
            Label titleLabel = (Label) flowPane.getChildren().get(0);
            titleLabel.setPrefHeight(flowPaneHeight / 6 - 6);
            titleLabel.setPrefWidth(flowPaneWidth - 10);
            titleLabel.setText(title);
            Label contentLabel = (Label) flowPane.getChildren().get(1);
            contentLabel.setPrefHeight((flowPaneHeight / 6 - 6) * 3);
            contentLabel.setPrefWidth(flowPaneWidth - 10);
            contentLabel.setWrapText(true);
            contentLabel.setText(content);
            FlowPane buttomFlowPane = (FlowPane) flowPane.getChildren().get(2);

            buttomFlowPane.setPrefHeight(flowPaneHeight / 6 * 2 - 6);
            buttomFlowPane.setPrefWidth(flowPaneWidth - 10);

            double buttonWidth = (flowPaneWidth - 6) / (map.size() + 1);
            double buttonHeight = (flowPaneHeight / 6 * 2 - 6) / 3 * 2;

            buttomFlowPane.setPadding(new Insets((flowPaneHeight / 6 * 2 - 6) / 3 / 2, 0, 0, buttonWidth / (map.size() + 1)));
            buttomFlowPane.setHgap(buttonWidth / (map.size() + 1));


            for (String key : map.keySet()) {
                String buttonContent = key;
                String buttonResult = map.get(key);
                Button button = new DwButton(FontSizeEnum.font18);
                button.setPrefWidth(buttonWidth);
                button.setPrefHeight(buttonHeight);
                button.setText(key);
                button.setOnAction(event -> {
                    confirmStage.close();
                    result.append(map.get(key));
                });
                buttomFlowPane.getChildren().add(button);
            }
            FlowPane finalFlowPane = flowPane;

            confirmStage.setScene(new Scene(finalFlowPane));

            confirmStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String showChoseView( Map<String, String> map, Stage parentStage) {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        StringBuffer result = new StringBuffer("");
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.NONE);
        confirmStage.initStyle(StageStyle.TRANSPARENT);
        if (parentStage == null) {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            confirmStage.initOwner(stage.get(0));
        } else {
            confirmStage.initOwner(parentStage);
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/dw/view/ChoseView.fxml"));
            FlowPane flowPane = null;
            flowPane = loader.load();

            double flowPaneWidth = primaryScreenBounds.getWidth() / 2;
            double flowPaneHeight = primaryScreenBounds.getHeight() / 3;
            flowPane.setPrefHeight(flowPaneHeight);
            flowPane.setPrefWidth(flowPaneWidth);

            double buttonWidth = (flowPaneWidth - 10 ) / (map.size() + 1);
            double buttonHeight = (flowPaneHeight - 10) / 5 ;

            //flowPane.setPadding(new Insets((flowPaneHeight / 6 * 2 - 6) / 3 / 2, 0, 0, buttonWidth / (map.size() + 1)));
            flowPane.setHgap(buttonWidth /map.size());


            for (String key : map.keySet()) {
                String buttonContent = key;
                String buttonResult = map.get(key);
                Button button = new DwButton(FontSizeEnum.font18);
                button.setPrefWidth(buttonWidth);
                button.setPrefHeight(buttonHeight);
                button.setText(key);
                button.setOnAction(event -> {
                    confirmStage.close();
                    result.append(map.get(key));
                });
                flowPane.getChildren().add(button);
            }
            FlowPane finalFlowPane = flowPane;

            confirmStage.setScene(new Scene(finalFlowPane));

            confirmStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }



    public static String showKeyword(Stage parentStage, String text) {

        StringBuffer result = new StringBuffer("");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("/com/dw/view/SearchTableView.fxml"));
            VBox searchBox = fxmlLoader.load();
            double searchBoxWidth = parentStage.getWidth() * 0.85;
            double searchBoxHeight = parentStage.getHeight() * 0.7;
            SearchTableController searchTableController = fxmlLoader.getController();
            searchBox.setPrefSize(searchBoxWidth, searchBoxHeight);
            //獲取搜索欄
            FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
            searchFlowPane.getStyleClass().add("searchFlowPane");
            searchFlowPane.setHgap(searchBoxWidth / 8 / 4);
            searchFlowPane.setPadding(new Insets(0, searchBoxWidth / 8 / 8, 0, searchBoxWidth / 8 / 8));
            //取台號搜索窗口的1/5
            searchFlowPane.setPrefHeight(searchBoxHeight / 3 / 2);
            searchFlowPane.setPrefWidth(searchBoxWidth);
            //清空按鈕
            Button clearAllButton = (Button) searchFlowPane.getChildren().get(0);
            clearAllButton.setPrefWidth(searchBoxWidth / 8);
            clearAllButton.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            clearAllButton.getStyleClass().add("clearAllButton");

            //搜索欄
            TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
            searchTextField.setPrefWidth(searchBoxWidth / 8 * 5);
            searchTextField.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            searchTextField.getStyleClass().add("searchTextField");
            searchTextField.requestFocus();
            searchTextField.setFocusTraversable(true);
            if (AppUtils.isNotBlank(text)) {
                searchTextField.setText(text);
            }

            //去除按鈕
            Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
            deleteButton.setPrefWidth(searchBoxWidth / 8);
            deleteButton.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            deleteButton.getStyleClass().add("deleteButton");

            double keyBtnWidht = (searchBoxWidth / 4 * 3 - 9 * 5) / 11;
            //double keyBtnHeight = searchBoxHeight / 3 * 2 / 3 * 0.8;
            double keyBtnHeight = searchBoxWidth / 4 * 3 / 11;
            //中间的键盘部分
            HBox hBox = (HBox) searchBox.getChildren().get(1);
            hBox.setPrefWidth(searchBoxWidth);
            hBox.setPrefHeight(searchBoxHeight / 3 * 2);

            //英文键盘部分
            FlowPane englishKeyPane = (FlowPane) hBox.getChildren().get(0);
            englishKeyPane.setPrefHeight(searchBoxHeight / 3 * 2);
            englishKeyPane.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane.getStyleClass().add("keyPane");

            //第一行字母
            FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
            englishKeyPane1.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane1.setPrefHeight(keyBtnHeight + 20);
            englishKeyPane1.getStyleClass().add("englishKeyPane");
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
                    default:
                        return "";
                }

                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane1.getChildren().add(englishButton);
            }


            //第二行字母
            FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
            englishKeyPane2.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane2.setPrefHeight(keyBtnHeight * 2 + 10);
            englishKeyPane2.getStyleClass().add("englishKeyPane");
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
                    default:
                        return "";
                }
                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane2.getChildren().add(englishButton);
            }


            //第三行字母
            FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
            englishKeyPane3.setPrefWidth(searchBoxWidth / 4 * 3);
            //englishKeyPane3.setPrefHeight(searchBoxHeight / 3 * 2 / 3);
            englishKeyPane3.setPrefHeight(keyBtnHeight + 20);
            //englishKeyPane3.setPrefHeight(keyBtnHeight);
            englishKeyPane3.getStyleClass().add("englishKeyPane");
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
                    default:
                        return null;
                }
                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane3.getChildren().add(englishButton);
            }

            //获取右边的数字键盘
            FlowPane numberKeyPane = (FlowPane) hBox.getChildren().get(1);
            numberKeyPane.setPrefHeight(searchBoxHeight / 3 * 2);
            numberKeyPane.setPrefWidth(searchBoxWidth / 4);
            numberKeyPane.getStyleClass().add("numberKeyPane");
            numberKeyPane.setHgap(5);
            numberKeyPane.setVgap(10);
            numberKeyPane.setPadding(new Insets(10, 0, 0, 0));
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
                    default:
                        return "";
                }
                numberButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                //numberButton.setPrefWidth(searchBoxWidth / 4 / 3 - 15);
                // numberButton.setPrefHeight(searchBoxHeight  / 3 * 2 / 4 * 0.9);
                numberButton.setOnAction(event1 -> {
                    searchTableController.addTextField(numberButton.getText());
                });
                numberButton.getStyleClass().add("numberButton");
                numberKeyPane.getChildren().add(numberButton);
            }

            //获取底部
            FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
            buttonFlowPane.setHgap(searchBoxWidth / 8);
            buttonFlowPane.setPrefWidth(searchBoxWidth);
            buttonFlowPane.setPrefHeight(searchBoxHeight / 3 / 2);
            buttonFlowPane.getStyleClass().add("buttonFlowPane");
            Button confirmButton = new Button();
            confirmButton.setPrefWidth(searchBoxWidth / 3);
            confirmButton.setPrefHeight(searchBoxHeight / 3 / 2 * 0.8);
            confirmButton.getStyleClass().add("confirmButton");
            confirmButton.setText("確定");
            buttonFlowPane.getChildren().add(confirmButton);
            Button closeButton = new Button();
            closeButton.setPrefWidth(searchBoxWidth / 3);
            closeButton.setPrefHeight(searchBoxHeight / 3 / 2 * 0.8);
            closeButton.getStyleClass().add("closeButton");
            closeButton.setText("關閉");
            buttonFlowPane.getChildren().add(closeButton);
            Stage searchTableStage = new Stage();
            searchTableStage.initOwner(parentStage);
            searchTableStage.setScene(new Scene(searchBox));
            searchTableStage.initModality(Modality.APPLICATION_MODAL);
            searchTableStage.initStyle(StageStyle.TRANSPARENT);
            closeButton.setOnAction(event1 -> {
                result.append(ResultEnum.NO.getValue());
                searchTableStage.close();
            });
            confirmButton.setOnAction(event -> {
                result.append(searchTableController.getTableNumberTextField().getText());
                searchTableStage.close();
            });
            searchTableStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static String showKeywordToPay(Stage parentStage, String text) {

        StringBuffer result = new StringBuffer("");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("/com/dw/view/SearchTableView.fxml"));
            VBox searchBox = fxmlLoader.load();
            Stage searchTableStage = new Stage();
            searchTableStage.initOwner(parentStage);
            searchTableStage.setScene(new Scene(searchBox));
            searchTableStage.initModality(Modality.APPLICATION_MODAL);
            searchTableStage.initStyle(StageStyle.TRANSPARENT);

            double searchBoxWidth = parentStage.getWidth() * 0.85;
            double searchBoxHeight = parentStage.getHeight() * 0.7;
            SearchTableController searchTableController = fxmlLoader.getController();
            searchBox.setPrefSize(searchBoxWidth, searchBoxHeight);
            //獲取搜索欄
            FlowPane searchFlowPane = (FlowPane) searchBox.getChildren().get(0);
            searchFlowPane.getStyleClass().add("searchFlowPane");
            searchFlowPane.setHgap(searchBoxWidth / 8 / 4);
            searchFlowPane.setPadding(new Insets(0, searchBoxWidth / 8 / 8, 0, searchBoxWidth / 8 / 8));
            //取台號搜索窗口的1/5
            searchFlowPane.setPrefHeight(searchBoxHeight / 3 / 2);
            searchFlowPane.setPrefWidth(searchBoxWidth);
            //清空按鈕
            Button clearAllButton = (Button) searchFlowPane.getChildren().get(0);
            clearAllButton.setPrefWidth(searchBoxWidth / 8);
            clearAllButton.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            clearAllButton.getStyleClass().add("clearAllButton");

            //搜索欄
            TextField searchTextField = (TextField) searchFlowPane.getChildren().get(1);
            searchTextField.setPrefWidth(searchBoxWidth / 8 * 5);
            searchTextField.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            searchTextField.getStyleClass().add("searchTextField");
            searchTextField.requestFocus();
            searchTextField.setFocusTraversable(true);
            searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        String scanCode = searchTextField.getText();
                        if (AppUtils.isNotBlank(scanCode)) {
                            //useCouponTableView(scanCode);
                            //調用付款接口
                            System.out.println("綁定事返回YES調用付款接口");
                            result.append(searchTableController.getTableNumberTextField().getText());
                            searchTableStage.close();
                        }
                    }

                }
            });
            if (AppUtils.isNotBlank(text)) {
                searchTextField.setText(text);
            }

            //去除按鈕
            Button deleteButton = (Button) searchFlowPane.getChildren().get(2);
            deleteButton.setPrefWidth(searchBoxWidth / 8);
            deleteButton.setPrefHeight(searchBoxHeight / 3 * 2 / 5 / 2);
            deleteButton.getStyleClass().add("deleteButton");

            double keyBtnWidht = (searchBoxWidth / 4 * 3 - 9 * 5) / 11;
            double keyBtnHeight = searchBoxWidth / 4 * 3 / 11;
            //中间的键盘部分
            HBox hBox = (HBox) searchBox.getChildren().get(1);
            hBox.setPrefWidth(searchBoxWidth);
            hBox.setPrefHeight(searchBoxHeight / 3 * 2);

            //英文键盘部分
            FlowPane englishKeyPane = (FlowPane) hBox.getChildren().get(0);
            englishKeyPane.setPrefHeight(searchBoxHeight / 3 * 2);
            englishKeyPane.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane.getStyleClass().add("keyPane");

            //第一行字母
            FlowPane englishKeyPane1 = (FlowPane) englishKeyPane.getChildren().get(0);
            englishKeyPane1.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane1.setPrefHeight(keyBtnHeight + 20);
            englishKeyPane1.getStyleClass().add("englishKeyPane");
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
                    default:
                        return "";
                }

                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane1.getChildren().add(englishButton);
            }


            //第二行字母
            FlowPane englishKeyPane2 = (FlowPane) englishKeyPane.getChildren().get(1);
            englishKeyPane2.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane2.setPrefHeight(keyBtnHeight * 2 + 10);
            englishKeyPane2.getStyleClass().add("englishKeyPane");
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
                    default:
                        return "";
                }
                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane2.getChildren().add(englishButton);
            }


            //第三行字母
            FlowPane englishKeyPane3 = (FlowPane) englishKeyPane.getChildren().get(2);
            englishKeyPane3.setPrefWidth(searchBoxWidth / 4 * 3);
            englishKeyPane3.setPrefHeight(keyBtnHeight + 20);
            englishKeyPane3.getStyleClass().add("englishKeyPane");
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
                    default:
                        return null;
                }
                englishButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                englishButton.getStyleClass().add("englishButton");
                englishButton.setOnAction(event1 -> {
                    searchTableController.addTextField(englishButton.getText());
                });
                englishKeyPane3.getChildren().add(englishButton);
            }

            //获取右边的数字键盘
            FlowPane numberKeyPane = (FlowPane) hBox.getChildren().get(1);
            numberKeyPane.setPrefHeight(searchBoxHeight / 3 * 2);
            numberKeyPane.setPrefWidth(searchBoxWidth / 4);
            numberKeyPane.getStyleClass().add("numberKeyPane");
            numberKeyPane.setHgap(5);
            numberKeyPane.setVgap(10);
            numberKeyPane.setPadding(new Insets(10, 0, 0, 0));
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
                    default:
                        return "";
                }
                numberButton.setPrefSize(keyBtnWidht, keyBtnHeight);
                //numberButton.setPrefWidth(searchBoxWidth / 4 / 3 - 15);
                // numberButton.setPrefHeight(searchBoxHeight  / 3 * 2 / 4 * 0.9);
                numberButton.setOnAction(event1 -> {
                    searchTableController.addTextField(numberButton.getText());
                });
                numberButton.getStyleClass().add("numberButton");
                numberKeyPane.getChildren().add(numberButton);
            }

            //获取底部
            FlowPane buttonFlowPane = (FlowPane) searchBox.getChildren().get(2);
            buttonFlowPane.setHgap(searchBoxWidth / 8);
            buttonFlowPane.setPrefWidth(searchBoxWidth);
            buttonFlowPane.setPrefHeight(searchBoxHeight / 3 / 2);
            buttonFlowPane.getStyleClass().add("buttonFlowPane");
            Button confirmButton = new Button();
            confirmButton.setPrefWidth(searchBoxWidth / 3);
            confirmButton.setPrefHeight(searchBoxHeight / 3 / 2 * 0.8);
            confirmButton.getStyleClass().add("confirmButton");
            confirmButton.setText("確定");
            buttonFlowPane.getChildren().add(confirmButton);
            Button closeButton = new Button();
            closeButton.setPrefWidth(searchBoxWidth / 3);
            closeButton.setPrefHeight(searchBoxHeight / 3 / 2 * 0.8);
            closeButton.getStyleClass().add("closeButton");
            closeButton.setText("關閉");
            buttonFlowPane.getChildren().add(closeButton);

            closeButton.setOnAction(event1 -> {
                result.append(ResultEnum.NO.getValue());
                searchTableStage.close();
            });
            confirmButton.setOnAction(event -> {
                result.append(searchTableController.getTableNumberTextField().getText());
                searchTableStage.close();
            });
            searchTableStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static String showNumbericKeyboard(Stage parentStage, String title, String defaultNum,boolean isShowOpenTableTime) {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        StringBuffer result = new StringBuffer("");
        Stage confirmStage = new Stage();
        if (parentStage == null) {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            confirmStage.initOwner(stage.get(0));
        } else {
            confirmStage.initOwner(parentStage);
        }
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.initStyle(StageStyle.TRANSPARENT);
        //初次進入標識
        AtomicBoolean firtTimeFlag = new AtomicBoolean(true);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/dw/view/NumbericKeyboardView.fxml"));
            FlowPane flowPane = loader.load();
            flowPane.getStyleClass().add("numberBtnPane");
            double flowPaneWidth = primaryScreenBounds.getWidth() / 3;
            double flowPaneHeight = primaryScreenBounds.getHeight() / 4 * 3;

            // 獲取標題面板
            FlowPane titlePane = (FlowPane) flowPane.getChildren().get(0);
            titlePane.setPrefSize(flowPaneWidth, flowPaneHeight / 7);
            DwLabel titleLabel = new DwLabel(FontSizeEnum.font24);
            titleLabel.setPrefSize(titlePane.getPrefWidth(), titlePane.getPrefHeight());
            titleLabel.setText(title);
            titleLabel.getStyleClass().add("label");
            titlePane.getChildren().add(titleLabel);


            // 獲取輸入框面板
            FlowPane numberPane = (FlowPane) flowPane.getChildren().get(1);
            numberPane.setPrefSize(flowPaneWidth, flowPaneHeight / 7);
            TextField numbericTextField = (TextField) numberPane.getChildren().get(0);
            numbericTextField.setText(defaultNum);
            numbericTextField.selectAll();

            numbericTextField.setPrefSize(flowPaneWidth - 10, numberPane.getPrefHeight());
            numbericTextField.getStyleClass().add("numbericTextField");
           /* Button deleteBtn = (Button) numberPane.getChildren().get(1);
            deleteBtn.setOnMouseClicked(event -> {
                if(numbericTextField.getText().length()>0){
                    numbericTextField.setText(numbericTextField.getText().substring(0,numbericTextField.getText().length()-1));
                }
            });*/

            // 獲取數字按鈕面板
            FlowPane numberBtnPane = (FlowPane) flowPane.getChildren().get(2);
            numberBtnPane.setPrefSize(flowPaneWidth, flowPaneHeight / 7 * 4);
            numberBtnPane.getStyleClass().add("numberBtnPane");
            GridPane gridPane = (GridPane) numberBtnPane.getChildren().get(0);
            gridPane.setPrefSize(flowPaneWidth, flowPaneHeight / 7 * 4);
//            gridPane.getStyleClass().add("oneLine");
            double btnWidth = (gridPane.getPrefWidth() - 20) / 3;
            double btnHeight = (gridPane.getPrefHeight() - 25) / 4;
            for (int k = 0; k < 12; k++) {
                Button numberButton = new Button();
                numberButton.setPrefSize(btnWidth, btnHeight);
                numberButton.getStyleClass().add("numberBtn");
                switch (k) {
                    case 0:
                        gridPane.add(numberButton, 0, 0);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("7");
                        break;
                    case 1:
                        gridPane.add(numberButton, 1, 0);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("8");
                        break;
                    case 2:
                        gridPane.add(numberButton, 2, 0);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("9");
                        break;
                    case 3:
                        gridPane.add(numberButton, 0, 1);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("4");
                        break;
                    case 4:
                        gridPane.add(numberButton, 1, 1);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("5");
                        break;
                    case 5:
                        gridPane.add(numberButton, 2, 1);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("6");
                        break;
                    case 6:
                        gridPane.add(numberButton, 0, 2);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("1");
                        break;
                    case 7:
                        gridPane.add(numberButton, 1, 2);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("2");
                        break;
                    case 8:
                        gridPane.add(numberButton, 2, 2);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("3");
                        break;
                    case 9:
                        gridPane.add(numberButton, 0, 3);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("0");
                        break;
                    case 10:
                        gridPane.add(numberButton, 1, 3);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText(".");
                        break;
                    case 11:
                        gridPane.add(numberButton, 2, 3);
                        GridPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
                        numberButton.setText("←");
                        break;
                    default:
                        return "";
                }
                if ("←".equals(numberButton.getText())) {
                    numberButton.setOnMouseClicked(event -> {
                        if (numbericTextField.getText().length() > 0) {
                            numbericTextField.setText(numbericTextField.getText().substring(0, numbericTextField.getText().length() - 1));
                        }
                    });
                } else {
                    numberButton.setOnAction(event1 -> {
                        if (firtTimeFlag.get()) {
                            numbericTextField.setText(numberButton.getText());
                            firtTimeFlag.set(false);
                        } else {
                            numbericTextField.setText(numbericTextField.getText() + numberButton.getText());
                        }
                    });
                }
            }
            // 獲取底部操作按鈕面板
            FlowPane bottomPane = (FlowPane) flowPane.getChildren().get(3);
            bottomPane.setPrefSize(flowPaneWidth, flowPaneHeight / 7);
            if(isShowOpenTableTime){
                DwButton confirmButton = new DwButton(FontSizeEnum.font20);
                confirmButton.getStyleClass().add("confirmNumberBtn");
                confirmButton.setText(Main.languageMap.get("numberkeyboard.confirm"));
                confirmButton.setPrefSize((bottomPane.getPrefWidth() - 20) / 3, bottomPane.getPrefHeight() - 10);
                bottomPane.getChildren().add(confirmButton);

                DwButton openTableButton = new DwButton(FontSizeEnum.font20);
                openTableButton.getStyleClass().add("confirmNumberBtn");
                openTableButton.setText(Main.languageMap.get("table.showOpenTime"));
                openTableButton.setPrefSize((bottomPane.getPrefWidth() - 20) / 3, bottomPane.getPrefHeight() - 10);
                bottomPane.getChildren().add(openTableButton);
                openTableButton.setOnAction(event -> {
                    result.append("showTime"+ numbericTextField.getText());
                    confirmStage.close();
                });



                DwButton closeButton = new DwButton(FontSizeEnum.font20);
                closeButton.setText(Main.languageMap.get("numberkeyboard.cancel"));
                closeButton.getStyleClass().add("cancelNumberBtn");
                closeButton.setPrefSize((bottomPane.getPrefWidth() - 20) / 3, bottomPane.getPrefHeight() - 10);
                bottomPane.getChildren().add(closeButton);
                bottomPane.setHgap(5);

                closeButton.setOnAction(event1 -> {
                    // result.append(ResultEnum.NO.getValue());
                    confirmStage.close();
                });
                confirmButton.setOnAction(event -> {
                    result.append(numbericTextField.getText());
                    confirmStage.close();
                });


            }else{
                DwButton confirmButton = new DwButton(FontSizeEnum.font20);
                confirmButton.getStyleClass().add("confirmNumberBtn");
                confirmButton.setText(Main.languageMap.get("numberkeyboard.confirm"));
                confirmButton.setPrefSize((bottomPane.getPrefWidth() - 15) / 2, bottomPane.getPrefHeight() - 10);
                bottomPane.getChildren().add(confirmButton);
                DwButton closeButton = new DwButton(FontSizeEnum.font20);
                closeButton.setText(Main.languageMap.get("numberkeyboard.cancel"));
                closeButton.getStyleClass().add("cancelNumberBtn");
                closeButton.setPrefSize((bottomPane.getPrefWidth() - 15) / 2, bottomPane.getPrefHeight() - 10);
                bottomPane.getChildren().add(closeButton);
                bottomPane.setHgap(5);

                closeButton.setOnAction(event1 -> {
                    // result.append(ResultEnum.NO.getValue());
                    confirmStage.close();
                });
                confirmButton.setOnAction(event -> {
                    result.append(numbericTextField.getText());
                    confirmStage.close();
                });
            }
            confirmStage.setScene(new Scene(flowPane));
            confirmStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
