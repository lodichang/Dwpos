package com.dw.controller;

import com.dw.model.ProperFoods;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/2/2.
 */
@Getter
@Setter
public class ProperFoodController {

    @FXML
    private TableView<ProperFoods> foodTableView;

    @FXML
    private TableColumn<ProperFoods,String> categoryColum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodNoColum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodNameColum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice1Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice2Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice3Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice4Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice5Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice6Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice7Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice8Colum;

    @FXML
    private TableColumn<ProperFoods,String> properFoodPrice9Colum;

    @FXML
    private FlowPane leftButtomFlowPane;

    @FXML
    private Button updateButton;

    @FXML
    private Button reSetButton;

    @FXML
    private FlowPane centerButtomFlowPane;

    @FXML
    private Button closeButton;

    @FXML
    private FlowPane rightButtomFlowPane;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;


    /**
     * 初始化方法
     */
    @FXML
    private void initialize() {
        categoryColum.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        properFoodNoColum.setCellValueFactory(cellData -> cellData.getValue().goodNoProperty());
        properFoodNameColum.setCellValueFactory(cellData -> cellData.getValue().goodNameProperty());
        properFoodPrice1Colum.setCellValueFactory(cellData -> cellData.getValue().price1Property());
        properFoodPrice2Colum.setCellValueFactory(cellData -> cellData.getValue().price2Property());
        properFoodPrice3Colum.setCellValueFactory(cellData -> cellData.getValue().price3Property());
        properFoodPrice4Colum.setCellValueFactory(cellData -> cellData.getValue().price4Property());
        properFoodPrice5Colum.setCellValueFactory(cellData -> cellData.getValue().price5Property());
        properFoodPrice6Colum.setCellValueFactory(cellData -> cellData.getValue().price6Property());
        properFoodPrice7Colum.setCellValueFactory(cellData -> cellData.getValue().price7Property());
        properFoodPrice8Colum.setCellValueFactory(cellData -> cellData.getValue().price8Property());
        properFoodPrice9Colum.setCellValueFactory(cellData -> cellData.getValue().price9Property());
    }


}
