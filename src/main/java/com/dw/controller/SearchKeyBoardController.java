package com.dw.controller;

import com.dw.Main;
import com.dw.dto.PosItemDto;
import com.dw.dto.TableViewDto;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.service.PosItemService;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.util.IDManager;
import com.sun.javafx.robot.impl.FXRobotHelper;
import de.felixroske.jfxsupport.FXMLController;
import com.dw.handwrite.hanzidict.HanziDict;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by wen.jing on 2018/5/15
 */
@Setter
@Getter
@FXMLController
public class SearchKeyBoardController implements Initializable {
    @FXML
    private VBox searchKeyBoradPane;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private ItemChooseController itemChooseController;
    @Autowired
    private PosItemService posItemService;


    private Stage keyBoradStage;

    @FXML
    private FlowPane topFlowPane;

    @FXML
    private DwLabel titleLabel;

    @FXML
    private FlowPane textFieldFlowPane;

    @FXML
    private TextField qtyTextField;

    @FXML
    private DwLabel xLabel;

    @FXML
    private TextField itemCodeTextField;

    @FXML
    private HBox centerPane;

    @FXML
    private FlowPane letterPane;
    @FXML
    private FlowPane numberPane;

    @FXML
    private VBox rightFeaturePane;

    @FXML
    private DwButton updateButton;

    @FXML
    private DwButton cancleButton;

    @FXML
    private DwButton inputButton;

    @FXML
    private DwButton handWriteButton;

    private TextField currTextField;

    private TextField resultListField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            keyBoradStage = (Stage) searchKeyBoradPane.getScene().getWindow();
        });
        mainPaneComponent();
    }

    public void initData(TableViewDto tvd) {
        currTextField = itemCodeTextField;
        itemCodeTextField.setText("");
        qtyTextField.setText("");

        if (AppUtils.isNotBlank(tvd)) { // 手写单
            titleLabel.getStyleClass().remove("title-tips");
            itemCodeTextField.setText(tvd.getItemName());
            qtyTextField.setPromptText(Main.languageMap.get("global.unit.price"));
            titleLabel.setText(Main.languageMap.get("keyword.iopen.title"));
            inputButton.setOnAction(event -> setItemNameNprice(tvd));
        } else {
            titleLabel.getStyleClass().remove("title-tips");
            qtyTextField.setPromptText(Main.languageMap.get("global.qty"));
            titleLabel.setText(Main.languageMap.get("keyword.title"));
            inputButton.setOnAction(event -> searchItem());
        }
    }

    public void mainPaneComponent() {
        qtyTextField.setPromptText(Main.languageMap.get("global.qty"));
        itemCodeTextField.setPromptText(Main.languageMap.get("global.itemdesc"));
        double width = takeOrderIndexController.getRightPane().getPrefWidth();
        double height = takeOrderIndexController.getRightPane().getPrefHeight();
        topPaneComponent(width, height * 0.2);
        centerPaneComponent(width, height * 0.4);
        letterPaneComponent(width, height * 0.38);
        //handWriteComponent(width, height * 0.38);
        currTextField = itemCodeTextField;

        itemCodeTextField.setOnMousePressed(event -> {
            currTextField = itemCodeTextField;
            letterPane.setDisable(false);
        });


        qtyTextField.setOnMousePressed(event -> {
            currTextField = qtyTextField;

            letterPane.setDisable(true);
        });


    }

    public void numberPaneComponent(double width, double height) {
        numberPane.setPrefSize(width, height);
        letterPane.setAlignment(Pos.CENTER);
        double numberButtonHeight = height / 4 - 5;
        double numberButtonWidth = width / 3 - 10;

        for (int i = 0; i < 12; i++) {
            DwButton numberButton = new DwButton(FontSizeEnum.font18);
            numberButton.getStyleClass().add("numbutton");
            numberButton.setPrefSize(numberButtonWidth, numberButtonHeight);
            switch (i) {
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
                    numberButton.setText("00");
                    break;
            }
            numberButton.setOnAction(event -> {

                currTextField.setText(currTextField.getText() + numberButton.getText());

            });
            FlowPane.setMargin(numberButton, new Insets(5, 0, 0, 5));
            numberPane.getChildren().add(numberButton);

        }
    }

    public void letterPaneComponent(double width, double height) {
        letterPane.setPadding(new Insets(5, 0, 0, 0));
        letterPane.setPrefSize(width, height);
        letterPane.setAlignment(Pos.CENTER);

        double letterButtonWidth = width / 7 - 6;
        double letterButtonHeight = (height) / 4 - 5;

        for (int i = 0; i < 28; i++) {
            DwButton letterButton = new DwButton(FontSizeEnum.font16);
            letterButton.setPrefWidth(letterButtonWidth);
            letterButton.setPrefHeight(letterButtonHeight);
            letterButton.getStyleClass().add("letterButton");
            switch (i) {
                case 0:
                    letterButton.setText("A");
                    break;
                case 1:
                    letterButton.setText("B");
                    break;
                case 2:
                    letterButton.setText("C");
                    break;
                case 3:
                    letterButton.setText("D");
                    break;
                case 4:
                    letterButton.setText("E");
                    break;
                case 5:
                    letterButton.setText("F");
                    break;
                case 6:
                    letterButton.setText("G");
                    break;
                case 7:
                    letterButton.setText("H");
                    break;
                case 8:
                    letterButton.setText("I");
                    break;
                case 9:
                    letterButton.setText("J");
                    break;
                case 10:
                    letterButton.setText("K");
                    break;
                case 11:
                    letterButton.setText("L");
                    break;
                case 12:
                    letterButton.setText("M");
                    break;
                case 13:
                    letterButton.setText("N");
                    break;
                case 14:
                    letterButton.setText("O");
                    break;
                case 15:
                    letterButton.setText("P");
                    break;
                case 16:
                    letterButton.setText("Q");
                    break;
                case 17:
                    letterButton.setText("R");
                    break;
                case 18:
                    letterButton.setText("S");
                    break;
                case 19:
                    letterButton.setText("T");
                    break;
                case 20:
                    letterButton.setText("U");
                    break;
                case 21:
                    letterButton.setText("V");
                    break;
                case 22:
                    letterButton.setText("W");
                    break;
                case 23:
                    letterButton.setText("X");
                    break;
                case 24:
                    letterButton.setText("Y");
                    break;
                case 25:
                    letterButton.setText("Z");
                    break;
                case 26:
                    letterButton.setText("-");
                    break;
                case 27:
                    letterButton.setText(" ");
                    break;
            }
            letterButton.setOnAction(event -> {
                currTextField.setText(currTextField.getText() + letterButton.getText());
            });
            FlowPane.setMargin(letterButton, new Insets(5, 0, 0, 5));
            letterPane.getChildren().add(letterButton);
        }
    }

    public void handWriteComponent(double width, double height) {
        letterPane.setPadding(new Insets(5, 0, 0, 0));
        letterPane.setPrefSize(width, height);
        letterPane.setAlignment(Pos.CENTER);
        //letterPane.getChildren().add(this.lookupPanel);
        HanziDict.main(null);

    }

    public void topPaneComponent(double width, double height) {
        //顶部
        topFlowPane.setPrefWidth(width);
        topFlowPane.setPrefHeight(height);

        //顶部标题
        double titleLableWidth = width;
        double titleLabelHeight = height / 5 * 3;

        titleLabel.setPrefWidth(titleLableWidth);
        titleLabel.setPrefHeight(titleLabelHeight);
        titleLabel.setFontSize(FontSizeEnum.font22);
        titleLabel.setText(Main.languageMap.get("keyword.title"));
        titleLabel.getStyleClass().add("titleLabel");

        double textFieldFlowPaneWidth = width;
        double textFieldFlowPaneHeight = height / 5 * 2;

        int topGrap = 5;
        int leftGrap = 10;
        textFieldFlowPane.setPrefWidth(textFieldFlowPaneWidth);
        textFieldFlowPane.setPrefHeight(textFieldFlowPaneHeight);
        textFieldFlowPane.setPadding(new Insets(topGrap, 0, 0, leftGrap));

        qtyTextField.setPrefWidth((textFieldFlowPaneWidth - leftGrap * 2) / 10 * 3);
        qtyTextField.setPrefHeight(textFieldFlowPaneHeight - topGrap * 2);

        xLabel.setPrefWidth((textFieldFlowPaneWidth - leftGrap * 2) / 10);
        xLabel.setPrefHeight(textFieldFlowPaneHeight - topGrap * 2);
        xLabel.getStyleClass().add("label");
        xLabel.setFontSize(FontSizeEnum.font20);

        itemCodeTextField.setPrefWidth((textFieldFlowPaneWidth - leftGrap * 2) / 10 * 6);
        itemCodeTextField.setPrefHeight((textFieldFlowPaneHeight - topGrap * 2));
    }

    public void centerPaneComponent(double width, double height) {
        centerPane.setPrefSize(width, height);
        numberPaneComponent(width / 4 * 3, height);
        rightFeaturePane(width / 4, height);
    }

    public void rightFeaturePane(double width, double height) {
        rightFeaturePane.setPrefSize(width, height);
        double btnHeight = height / 4;
        //修改
        updateButton.setPrefSize(width, btnHeight);
        updateButton.getStyleClass().add("clearAllButton");
        updateButton.setText(Main.languageMap.get("keyword.update"));
        updateButton.setOnAction(event -> currTextField.setText(""));
        VBox.setMargin(updateButton, new Insets(2, 2, 2, 0));

        //取消
        cancleButton.setPrefSize(width, btnHeight);
        cancleButton.getStyleClass().add("closeButton");
        cancleButton.setText(Main.languageMap.get("keyword.cancle"));
        cancleButton.setOnAction(event -> takeOrderIndexController.openItemChooseView());
        VBox.setMargin(cancleButton, new Insets(2, 2, 2, 0));

        //输入
        handWriteButton.setPrefSize(width, btnHeight);
        handWriteButton.getStyleClass().add("closeButton");
        handWriteButton.setText(Main.languageMap.get("keyword.handwrite"));
        handWriteButton.setOnAction(event -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage handWriteStage = new Stage();
                    JPanel dict = new HanziDict() {
                        @Override
                        public void close(String text) {
                            itemCodeTextField.setText(text);
                            Platform.runLater(() -> handWriteStage.close());
                        }
                    };
                    handWriteStage.initModality(Modality.NONE);
                    handWriteStage.initStyle(StageStyle.TRANSPARENT);
                    handWriteStage.initOwner(keyBoradStage);
                    SwingNode swingNode = new SwingNode();
                    swingNode.setContent(dict);
                    StackPane pane = new StackPane();
                    pane.getChildren().add(swingNode);
                    handWriteStage.setScene(new Scene(pane, dict.getPreferredSize().getWidth(), dict.getPreferredSize().getHeight()));
                    pane.setStyle("-fx-background-color: #34495E");
                    dict.setBackground(new Color(52,73,94));
                    handWriteStage.showAndWait();
                }
            });

        });

        //输入
        inputButton.setPrefSize(width, btnHeight);
        inputButton.getStyleClass().add("confirmButton");
        inputButton.setText(Main.languageMap.get("keyword.input"));
        inputButton.setOnAction(event -> searchItem());
        VBox.setMargin(inputButton, new Insets(2, 2, 2, 0));
    }

    public void searchItem() {
        String itemCode = itemCodeTextField.getText();
        int itemQty = AppUtils.isNotBlank(qtyTextField.getText()) ? Integer.parseInt(qtyTextField.getText()) : 0;
        if (AppUtils.isNotBlank(itemCode)) {
            PosItemDto posItemDto = posItemService.searchItem(Main.posOutlet, Main.posPeriodMap.get("price"), itemCode);
            if (AppUtils.isNotBlank(posItemDto)) {
                if (itemQty > 1) {
                    takeOrderIndexController.setPerAddItemCount(itemQty);
                }

                itemChooseController.itemBorderPaneOnClick(posItemDto);
            }
            itemCodeTextField.setText("");
            qtyTextField.setText("");
        }
    }

    public void setItemNameNprice(TableViewDto tvd) {
        if (AppUtils.isNotBlank(itemCodeTextField.getText()) && AppUtils.isNotBlank(qtyTextField.getText())) {
            String iopen = tvd.getIopen();
            tvd = new TableViewDto("" + takeOrderIndexController.getPerAddItemCount(),
                    tvd.getItemCode(),
                    itemCodeTextField.getText(), "", "",
                    Double.parseDouble(qtyTextField.getText().trim()),
                    Double.parseDouble(DecimalUtil.multiply(new BigDecimal("" + takeOrderIndexController.getPerAddItemCount()), new BigDecimal(qtyTextField.getText().trim())).toString()),
                    "", "", 0.00, "", Double.parseDouble(qtyTextField.getText().trim()),
                    "", false, 0, tvd.getService(), "", "", "", "",
                    tvd.getItemPrn(), tvd.getCombId(), "", "", tvd.getCatt(), tvd.getAttMax(),
                    tvd.getAttMin(), IDManager.generateID(), false, tvd.gettDate());
            tvd.setIopen(iopen);
            takeOrderIndexController.getTableViewData().add(tvd);
            takeOrderIndexController.getFoodTableView().getSelectionModel().select(takeOrderIndexController.getTableViewData().size() - 1);
            takeOrderIndexController.getFoodTableView().scrollTo(takeOrderIndexController.getTableViewData().size() - 1);

            takeOrderIndexController.openItemChooseView();
        } else {
            titleLabel.setText(Main.languageMap.get("keyword.iopen.title.tips"));
            titleLabel.getStyleClass().add("title-tips");
        }
    }

}
