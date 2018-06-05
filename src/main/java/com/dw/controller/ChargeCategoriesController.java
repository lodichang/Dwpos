package com.dw.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/1/23.
 */
@Getter
@Setter
public class ChargeCategoriesController {

    @FXML
    private Button nextPageButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField searchTextField;

    private Stage stage;


    @FXML
    public void closeStage(){
        stage.close();
    }

}
