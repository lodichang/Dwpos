package com.dw.controller;

import com.dw.Main;
import com.dw.model.StandbyMoney;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
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
 * Created by lodi on 2018/2/24.
 */
@Getter
@Setter
public class StandbyMoneyController {


    @FXML
    private TableView<StandbyMoney> moneyTableView;

    @FXML
    private TableColumn<StandbyMoney,String> machineNoColum;

    @FXML
    private TableColumn<StandbyMoney,String> timeColum;

    @FXML
    private TableColumn<StandbyMoney,String> serialNumberColum;

    @FXML
    private TableColumn<StandbyMoney,String> reasonColum;

    @FXML
    private TableColumn<StandbyMoney,String> silverHeadColum;

    @FXML
    private TableColumn<StandbyMoney,String> deliveryColum;


    @FXML
    private Button silverHeadButton;

    @FXML
    private Button deliveryButton;

    @FXML
    private Label totalChargeLabel;

    @FXML
    private Label silverHeadLabel;

    @FXML
    private Label deliveryLabel;

    @FXML
    private Button closeButton;

    @FXML
    private Button  upButton;

    @FXML
    private Button downButton;

    private Rectangle2D primaryScreenBounds;

    private Stage standbyMoneyStage;

    @FXML
    private void initialize() {
        machineNoColum.setCellValueFactory(cellData -> cellData.getValue().machineNoProperty());
        timeColum.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        serialNumberColum.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty());
        reasonColum.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        silverHeadColum.setCellValueFactory(cellData -> cellData.getValue().silverHeadProperty());
        deliveryColum.setCellValueFactory(cellData -> cellData.getValue().deliveryProperty());
    }

    //银头，交收
    @FXML
    public void showSliverHeadView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/view/SilverHeadView.fxml"));
        FlowPane flowPane =  loader.load();
        SilverHeadViewController silverHeadViewController = loader.getController();
        double chargeCategoriesWidth = primaryScreenBounds.getWidth()/10*8;
        double chargeCategoriesHeight = primaryScreenBounds.getHeight()/5*4-6;
        flowPane.setPrefHeight(chargeCategoriesHeight);
        flowPane.setPrefWidth(chargeCategoriesWidth);
        Label titleLabel = (Label) flowPane.getChildren().get(0);
        double titleLabelWidth = chargeCategoriesWidth-6;
        double titleLabelHeight = chargeCategoriesHeight/9;
        titleLabel.setPrefWidth(titleLabelWidth);
        titleLabel.setPrefHeight(titleLabelHeight);
        FlowPane contentFlowPane = (FlowPane) flowPane.getChildren().get(1);
        double contentFlowPaneWidth = chargeCategoriesWidth-6;
        double contentFlowPaneHeight = chargeCategoriesHeight/9*6;
        double contentLabelFlowPaneWidth = (chargeCategoriesWidth-6)/6;
        double contentLabelFlowPaneHeight = chargeCategoriesHeight/6;
        contentFlowPane.setPrefWidth(contentFlowPaneWidth);
        contentFlowPane.setPrefHeight(contentFlowPaneHeight);
        contentFlowPane.setHgap(contentLabelFlowPaneWidth/8);
        contentFlowPane.setVgap(contentLabelFlowPaneHeight/8);
        contentFlowPane.setPadding(new Insets(contentLabelFlowPaneHeight/8,0,0,contentLabelFlowPaneWidth/4));
        for(int i = 0;i < 15;i++){
            Button button = new Button();
            button.setText("加鐘");
            button.setPrefWidth(contentLabelFlowPaneWidth);
            button.setPrefHeight(contentLabelFlowPaneHeight);
            button.setStyle("-fx-font-size: 26px; -fx-background-color: #27728A;-fx-text-fill: white; -fx-wrap-text: true");
            contentFlowPane.getChildren().add(button);
        }
        FlowPane buttonFlowPane = (FlowPane) flowPane.getChildren().get(2);
        double buttonFlowPaneWidth = chargeCategoriesWidth-6;
        double buttonFlowPaneHeight = chargeCategoriesHeight/9*2-7;
        buttonFlowPane.setPrefWidth(buttonFlowPaneWidth);
        buttonFlowPane.setPrefHeight(buttonFlowPaneHeight);
        buttonFlowPane.setHgap(chargeCategoriesWidth/20);
        double buttonWidth = buttonFlowPaneWidth/5;
        double buttonHeight = buttonFlowPaneHeight/3*2;
        silverHeadViewController.getNextPageButton().setPrefWidth(buttonWidth);
        silverHeadViewController.getNextPageButton().setPrefHeight(buttonHeight);
        silverHeadViewController.getCloseButton().setPrefWidth(buttonWidth);
        silverHeadViewController.getCloseButton().setPrefHeight(buttonWidth);
        double textFieldWidth = chargeCategoriesWidth/5*2;
        double textFieldHeight = chargeCategoriesHeight/9*1.8;
        silverHeadViewController.getSearchTextField().setPrefWidth(textFieldWidth);
        silverHeadViewController.getSearchTextField().setPrefHeight(textFieldHeight);
        silverHeadViewController.getCloseButton().setPrefWidth(buttonWidth);
        silverHeadViewController.getCloseButton().setPrefHeight(buttonHeight);
        Stage  silverHeadStage = new Stage();
        silverHeadViewController.setStage(silverHeadStage);
        silverHeadStage.setScene(new Scene(flowPane));
        silverHeadStage.initOwner(standbyMoneyStage);
        silverHeadStage.initModality(Modality.APPLICATION_MODAL);
        silverHeadStage.initStyle(StageStyle.TRANSPARENT);
        silverHeadStage.show();
    }

}
