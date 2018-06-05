package com.dw.controller;

import com.dw.Main;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/2/26.
 */
@Getter
@Setter
public class WorkerSetController {


    private Main main;

    private Rectangle2D primaryScreenBounds;

    @FXML
    private Button closeButton;

    @FXML
    private Button alterPaswordButton;

    @FXML
    private Button addWorkerButton;

    @FXML
    private Button workerSignOffButton;

    @FXML
    public void alterPaswordView(){

    }


    @FXML
    public void addWorkerView(){

    }

    @FXML
    public void workerSignOffStew(){

    }

}
