package com.dw.controller;

import com.dw.Main;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by li.yongliang on 2018/4/24.
 */
@Setter
@Getter
@FXMLController
public class TranFunctionController implements Initializable {

    @FXML
    private BorderPane tranFunctionBorderPane;
    @FXML
    private FlowPane topFlowPane;

    private Stage parentStage;
    @FXML
    private void close(){
        parentStage.close();
    }


    public void show() {
        double takeOrderIndexWidth = Main.primaryScreenBounds.getWidth();
        double takeOrderIndexHeight = Main.primaryScreenBounds.getHeight();
        tranFunctionBorderPane.setPrefWidth(takeOrderIndexWidth*0.8);
        tranFunctionBorderPane.setPrefHeight(takeOrderIndexHeight*0.8);
        tranFunctionBorderPane.setPadding(new Insets(10,0,0,5));

        topFlowPane.setPrefSize(takeOrderIndexWidth*0.8,takeOrderIndexHeight*0.1);
        topFlowPane.setAlignment(Pos.CENTER_RIGHT);
        Button button = new DwButton(FontSizeEnum.font18);
        button.setText("关闭");
        button.setAlignment(Pos.CENTER);
        button.setPrefSize(topFlowPane.getPrefWidth()/10,topFlowPane.getPrefHeight());
        button.setOnMouseClicked(event -> {
            parentStage.close();
        });

        topFlowPane.getChildren().add(button);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()->{
            parentStage = (Stage) tranFunctionBorderPane.getScene().getWindow();
        });
        show();
    }
}


