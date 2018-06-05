package com.dw.util;

import com.dw.handwrite.hanzidict.HanziDict;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lodi on 2018/6/4.
 */
public class HandWriteUtil {

    public static void showHandWriteView(javafx.scene.control.TextField textField,Stage stage){
        Stage handWriteStage = new Stage();
        JPanel dict = new HanziDict() {
            @Override
            public void close(String text) {
                if(AppUtils.isNotBlank(text)){
                    textField.setText(text);
                }
                Platform.runLater(() -> handWriteStage.close());
            }

        };
        handWriteStage.initModality(Modality.NONE);
        handWriteStage.initStyle(StageStyle.TRANSPARENT);
        handWriteStage.initOwner(stage);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(dict);
        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);
        handWriteStage.setScene(new Scene(pane, dict.getPreferredSize().getWidth(), dict.getPreferredSize().getHeight()));
        pane.setStyle("-fx-background-color: #34495E");
        dict.setBackground(new Color(52,73,94));
        handWriteStage.showAndWait();
    }
}
