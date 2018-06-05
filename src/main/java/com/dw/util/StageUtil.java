package com.dw.util;

import com.dw.Main;
import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StageUtil {

   public static <T> T replaceSceneContent(String fxml, Stage stage) {
         Parent page = null;
         Object controller = null;
         try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(fxml));
            page = loader.load();
            controller = loader.getController();
         } catch (IOException e) {
            e.printStackTrace();
         }
         Scene scene = stage.getScene();
         if (scene == null) {
            scene = new Scene(page, Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
            stage.setX(Main.primaryScreenBounds.getMinX());
            stage.setY(Main.primaryScreenBounds.getMinY());
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
         }
         else {
            stage.getScene().setRoot(page);
         }
         if (AppUtils.isBlank(stage.getOwner())) {
            ObservableList<Stage> stages = FXRobotHelper.getStages();
            stage.initOwner(stages.get(0));
         }
         stage.sizeToScene();
         return (T) controller;
      }
}
