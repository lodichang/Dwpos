package com.dw.controller;

import com.dw.dto.AnnouncementDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by lodi on 2017/12/20.
 */
@Getter
@Setter
public class AnnouncementController extends TableRow<AnnouncementDto> {

    @FXML
    private TableView<AnnouncementDto> tgoodsTab;

    @FXML
    private TableColumn<AnnouncementDto, String> itemCodeNameCol;


    private ObservableList<AnnouncementDto> outOfStockTgoodsData = FXCollections.observableArrayList();

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    /**
     * 初始化方法
     */
    @FXML
    private void initialize() {
        itemCodeNameCol.setCellValueFactory(cellData -> cellData.getValue().goodCodeNameProperty());
    }


}
