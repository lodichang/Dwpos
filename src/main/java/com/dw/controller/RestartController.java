package com.dw.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/1/23.
 */
@Getter
@Setter
public class RestartController {


    @FXML
    private Button closeButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button turnOffButton;

    @FXML
    private Button exitButton;

    private Stage stage;

    @FXML
    public void closeStage(){
        stage.close();
    }

}
