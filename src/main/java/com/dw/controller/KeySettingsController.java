package com.dw.controller;

import com.dw.Main;
import com.dw.model.Goods;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lodi on 2018/3/6.
 */
@Getter
@Setter
public class KeySettingsController {

    @FXML
    private FlowPane leftContentFlowPane;

    @FXML
    private TableView<Goods> goodsTableView;

    @FXML
    private TableColumn<Goods,String> numberColum;

    @FXML
    private TableColumn<Goods,String> goodNameColum;

    @FXML
    private TableColumn<Goods,String> priceColum;

    @FXML
    private Button foodButton;

    @FXML
    private Button colorButton;

    @FXML
    private Button classificationButton;

    @FXML
    private Button formatButton;

    @FXML
    private Button notListedButton;

    @FXML
    private Button  closeButton;

    @FXML
    private Button descriptionButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button deletePageButton;

    @FXML
    private Button addPageButton;

    @FXML
    private Label pagesLabel;

    @FXML
    private Button searchButton;

    @FXML
    private Button settingButton;

    @FXML
    private Button backButton;

    @FXML
    private Button previousPageButton;

    @FXML
    private Button nextPageButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button previousCategoryButton;

    @FXML
    private Button nextCategoryButton;


    private Rectangle2D primaryScreenBounds;

    private Main main;

    private List<Label> checkLabels =  new LinkedList<>();

    private List<FlowPane> buttonFlowPanes = new LinkedList<>();

    private FlowPane currentButtonFlowPane;

    private String[] colors = {"#3F51B5","#FF4081","#E51C23","#009688","#259B24","#FF9800","#866CBD","#E84FC6","#EDB179","#75B0E6","#D5E339","#D6593E"};

    private String[] formats = {

            "3X3","3X4","3X5","3X6","3X7","3X8",

            "4X3","4X4","4X5","4X6","4X7","4X8",

            "5X3","5X4","5X5","5X6","5X7","5X8",

            "6X3","6X4","6X5","6X6","6X7","6X8",

            "7X3","7X4","7X5","7X6","7X7","7X8",

            "8X3","8X4","8X5","8X6","8X7","8X8",
    };

    /**
     * 初始化方法
     */
    @FXML
    private void initialize() {
        numberColum.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        goodNameColum.setCellValueFactory(cellData -> cellData.getValue().goodNameProperty());
        priceColum.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
    }

    /**
     * 顏色
     */
    @FXML
    public void showColors() {
        leftContentFlowPane.getChildren().remove(0, leftContentFlowPane.getChildren().size());
        leftContentFlowPane.setHgap(primaryScreenBounds.getWidth() / 7 * 3 / 80);
        leftContentFlowPane.setVgap(primaryScreenBounds.getHeight() / 10 * 9 / 200);
        double colorLabelWidth = (primaryScreenBounds.getWidth() / 7 * 3 - primaryScreenBounds.getWidth() / 7 * 3 / 80 * 3) / 3;
        double colorLabelHeight = (primaryScreenBounds.getHeight() / 10 * 9 - primaryScreenBounds.getHeight() / 10 * 9 / 200 * 4) / 4;
        for (int i = 0; i < 12; i++) {
            Label colorLabel = new Label();
            colorLabel.setId(colors[i]);
            colorLabel.setPrefHeight(colorLabelHeight);
            colorLabel.setPrefWidth(colorLabelWidth);
            colorLabel.setStyle("-fx-background-color:" + colors[i]);
            colorLabel.setOnMouseClicked(event -> {
                if (currentButtonFlowPane != null) {
                    ObservableList<Node> nodes = currentButtonFlowPane.getChildren();
                    for (Node node : nodes) {
                        node.setStyle("-fx-background-color:" + colorLabel.getId());
                    }
                }
            });
            leftContentFlowPane.getChildren().add(colorLabel);
        }
    }

        /**
         * 格式
         */
        @FXML
        public void showFormats() {
            leftContentFlowPane.getChildren().remove(0,leftContentFlowPane.getChildren().size());
            leftContentFlowPane.setHgap(primaryScreenBounds.getWidth()/7*3/80);
            leftContentFlowPane.setVgap(primaryScreenBounds.getHeight()/10*9/200);
            double formatLabelWidth = (primaryScreenBounds.getWidth()/7*3 - primaryScreenBounds.getWidth()/7*3/80*7)/6;
            double formatLabelHeight = (primaryScreenBounds.getHeight()/10*9 -  primaryScreenBounds.getHeight()/10*9/200*7)/6;
            for(int i = 0;i<36;i++){
                Label formatLabel  = new Label();
                formatLabel.setText(formats[i]);
                formatLabel.setPrefHeight(formatLabelHeight);
                formatLabel.setPrefWidth(formatLabelWidth);
                formatLabel.setStyle("-fx-background-color: #EDEDED; -fx-alignment: center; -fx-font-size: 15px");
                formatLabel.setOnMouseClicked(event -> {
                });
                leftContentFlowPane.getChildren().add(formatLabel);
            }
        }

    /**
     * 格式
     */
    @FXML
    public void showClassification() {
        leftContentFlowPane.getChildren().remove(0,leftContentFlowPane.getChildren().size());
        leftContentFlowPane.setHgap(primaryScreenBounds.getWidth()/7*3/80);
        leftContentFlowPane.setVgap(primaryScreenBounds.getHeight()/10*9/200);
        leftContentFlowPane.getChildren().add(goodsTableView);
        for(int k = 0;k<16;k++){
            Goods goods = new Goods("","厨部","","");
            goodsTableView.getItems().add(goods);
        }

    }





}
