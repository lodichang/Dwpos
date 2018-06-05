package com.dw.controller;

import com.dw.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * Created by lodi on 2018/2/26.
 */
@Getter
@Setter
public class OtherController {


    private Main main;

    private Rectangle2D primaryScreenBounds;

    @FXML
    private Button closeButton;

    @FXML
    private Button printTestButton;

    @FXML
    private Button analogyToDownload;

    @FXML
    public void printTestView() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("controller/view/PrintersView.fxml"));
            FlowPane flowPane =  loader.load();
            PrintersController printersController = loader.getController();
            double printersWidth = primaryScreenBounds.getWidth()/10*8;
            double printersHeight = primaryScreenBounds.getHeight()/5*4-6;
            flowPane.setPrefHeight(printersHeight);
            flowPane.setPrefWidth(printersWidth);
            Label titleLabel = (Label) flowPane.getChildren().get(0);
            double titleLabelWidth = printersWidth-6;
            double titleLabelHeight = printersHeight/9;
            titleLabel.setPrefWidth(titleLabelWidth);
            titleLabel.setPrefHeight(titleLabelHeight);
            FlowPane contentFlowPane = (FlowPane) flowPane.getChildren().get(1);
            double contentFlowPaneWidth = printersWidth-6;
            double contentFlowPaneHeight = printersHeight/9*6;
            double contentLabelFlowPaneWidth = (printersWidth-6)/6;
            double contentLabelFlowPaneHeight = printersHeight/6;
            contentFlowPane.setPrefWidth(contentFlowPaneWidth);
            contentFlowPane.setPrefHeight(contentFlowPaneHeight);
            contentFlowPane.setHgap(contentLabelFlowPaneWidth/8);
            contentFlowPane.setVgap(contentLabelFlowPaneHeight/8);
            contentFlowPane.setPadding(new Insets(contentLabelFlowPaneHeight/8,0,0,contentLabelFlowPaneWidth/4));
            Stage printersStage = new Stage();
            for(int i = 0;i < 15;i++){
                Button button = new Button();
                button.setText("打印機1");
                button.setPrefWidth(contentLabelFlowPaneWidth);
                button.setPrefHeight(contentLabelFlowPaneHeight);
                button.setStyle("-fx-font-size: 26px; -fx-background-color: #27728A;-fx-text-fill: white; -fx-wrap-text: true");
                contentFlowPane.getChildren().add(button);
                button.setOnAction(event -> {
                   /* printerTextField.setText(button.getText());
                    printersStage.close();*/
                });
            }
            FlowPane buttonFlowPane = (FlowPane) flowPane.getChildren().get(2);
            double buttonFlowPaneWidth = printersWidth-6;
            double buttonFlowPaneHeight = printersHeight/9*2-7;
            buttonFlowPane.setPrefWidth(buttonFlowPaneWidth);
            buttonFlowPane.setPrefHeight(buttonFlowPaneHeight);
            buttonFlowPane.setHgap(printersWidth/20);
            double buttonWidth = buttonFlowPaneWidth/5;
            double buttonHeight = buttonFlowPaneHeight/3*2;
            printersController.getNextPageButton().setPrefWidth(buttonWidth);
            printersController.getNextPageButton().setPrefHeight(buttonHeight);
            printersController.getCloseButton().setPrefWidth(buttonWidth);
            printersController.getCloseButton().setPrefHeight(buttonWidth);
            double textFieldWidth = printersWidth/5*2;
            double textFieldHeight = printersHeight/9*1.8;
            printersController.getSearchTextField().setPrefWidth(textFieldWidth);
            printersController.getSearchTextField().setPrefHeight(textFieldHeight);
            printersController.getCloseButton().setPrefWidth(buttonWidth);
            printersController.getCloseButton().setPrefHeight(buttonHeight);

            printersController.setStage(printersStage);
            printersStage.setScene(new Scene(flowPane));
            printersStage.initModality(Modality.APPLICATION_MODAL);
            printersStage.initStyle(StageStyle.TRANSPARENT);
            printersStage.show();
        }


    @FXML
    public void analogyToDownloadView(){

    }

}
