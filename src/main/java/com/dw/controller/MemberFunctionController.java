package com.dw.controller;

import com.dw.model.ConsumerDetail;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/2/24.
 */
@Getter
@Setter
public class MemberFunctionController {


    @FXML
    private Label cardNoLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label givingBalanceLabel;

    @FXML
    private Label integralLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private TableView<ConsumerDetail> consumerTableView;

    @FXML
    private TableColumn<ConsumerDetail,String> dateColum;

    @FXML
    private TableColumn<ConsumerDetail,String> orderNoColum;

    @FXML
    private TableColumn<ConsumerDetail,String> tranTypeColum;

    @FXML
    private TableColumn<ConsumerDetail,String> branchColum;

    @FXML
    private TableColumn<ConsumerDetail,String> consumerAmountColum;

    @FXML
    private TableColumn<ConsumerDetail,String> givenAmountColum;

    @FXML
    private TableColumn<ConsumerDetail,String> givenIntergalColum;

    @FXML
    private Button searchButton;

    @FXML
    private Button checkCouponButton;

    @FXML
    private Button exchangeIntegralButton;

    @FXML
    private Button sureExchangeButton;

    @FXML
    private Button printButton;

    @FXML
    private Button otherButton;

    @FXML
    private Button  upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        dateColum.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        orderNoColum.setCellValueFactory(cellData -> cellData.getValue().orderNoProperty());
        tranTypeColum.setCellValueFactory(cellData -> cellData.getValue().tranTypeProperty());
        branchColum.setCellValueFactory(cellData -> cellData.getValue().branchProperty());
        consumerAmountColum.setCellValueFactory(cellData -> cellData.getValue().consumerAmountProperty());
        givenAmountColum.setCellValueFactory(cellData -> cellData.getValue().givenAmountProperty());
        givenIntergalColum.setCellValueFactory(cellData -> cellData.getValue().givenIntergalProperty());
    }



}
