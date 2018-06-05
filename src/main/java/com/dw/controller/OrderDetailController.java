package com.dw.controller;

import com.dw.model.OrderDetail;
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
public class OrderDetailController {


    @FXML
    private Label operatorLabel;

    @FXML
    private Label tableNoLabel;

    @FXML
    private Label orderCountLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label memberNumberLabel;


    @FXML
    private TableView<OrderDetail> orderDetailView;

    @FXML
    private TableColumn<OrderDetail, String> seatColum;

    @FXML
    private TableColumn<OrderDetail, String> serialNumberColum;

    @FXML
    private TableColumn<OrderDetail, String> dishColum;

    @FXML
    private TableColumn<OrderDetail, String> tasteColum;

    @FXML
    private TableColumn<OrderDetail, String> numberColum;

    @FXML
    private TableColumn<OrderDetail, String> eliminateColum;

    @FXML
    private TableColumn<OrderDetail, String> freeColum;

    @FXML
    private TableColumn<OrderDetail, String> stafferColum;

    @FXML
    private TableColumn<OrderDetail, String> timeColum;

    @FXML
    private TableColumn<OrderDetail, String> priceColum;


    @FXML
    private Button orderButton;

    @FXML
    private Button cancleButton;

    @FXML
    private Button memberButton;

    @FXML
    private Button discountButton;

    @FXML
    private Button turnTableButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button messageButton;

    @FXML
    private Button searchOrderButton;

    @FXML
    private Button pullFlyButton;

    @FXML
    private Button otherButton;

    @FXML
    private Button invoicingButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;




    @FXML
    private void initialize() {
        seatColum.setCellValueFactory(cellData -> cellData.getValue().seatProperty());
        serialNumberColum.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty());
        dishColum.setCellValueFactory(cellData -> cellData.getValue().dishProperty());
        tasteColum.setCellValueFactory(cellData -> cellData.getValue().tasteProperty());
        numberColum.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        eliminateColum.setCellValueFactory(cellData -> cellData.getValue().eliminateProperty());
        freeColum.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        stafferColum.setCellValueFactory(cellData -> cellData.getValue().stafferProperty());
        timeColum.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        priceColum.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
    }


}
