package com.dw.listener;

import com.dw.Main;
import com.dw.controller.TableEditController;
import com.dw.controller.TableSettingsController;
import com.dw.enums.ResultEnum;
import com.dw.util.DragUtil;
import com.dw.util.ShowViewUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lodi on 2018/4/9.
 */
public class DragListener implements EventHandler<MouseEvent> {
    private double xOffset = 20;
    private double yOffset = 20;
    private final Stage stage;
    boolean isAllow = false ;
    private Node node;
    private Stage parentStage;

    public DragListener(Stage stage,Node node,Stage parentStage) {
        this.stage = stage;
        this.node = node;
        this.parentStage = parentStage;
    }

   /* public DragListener(Node node,Stage parentStage) {
        this.node = node;
        this.parentStage = parentStage;
    }*/

    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            FlowPane flowPane = (FlowPane) this.node;
            if(flowPane.getUserData() != null){
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.haveDrive"), resultMap, parentStage);
                return;
            }
            if(!isAllow){
                isAllow = true;
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
                DragUtil.stage = stage;
            }
            else{
                isAllow = false;
                TableSettingsController.moveFlowPaneMap.put(flowPane.getId(),flowPane);
                DragUtil.stage = null;
            }

        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED ) {
            double x = event.getSceneX();
            double y = event.getScreenY();
            stage.setX(event.getScreenX() - xOffset);
            if (event.getScreenY() - yOffset < 0) {
                stage.setY(0);
            } else {
                stage.setY(event.getScreenY() - yOffset);

            }
        }
        else if (event.getEventType() == MouseEvent.MOUSE_MOVED && isAllow) {
            double x = event.getSceneX();
            double y = event.getScreenY();
            if(event.getScreenX() - xOffset < 0){
                stage.setX(0);
            }
            else{
                stage.setX(event.getScreenX() - xOffset);
            }
            if (event.getScreenY() - yOffset < 0) {
                stage.setY(0);
            } else {
                stage.setY(event.getScreenY() - yOffset);
            }
        }
        else if (event.getEventType() == MouseEvent.MOUSE_EXITED && isAllow) {
            stage.setX(DragUtil.moveX - xOffset);
            stage.setY(DragUtil.moveY - yOffset);
        }
    }

   /* @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            FlowPane flowPane = (FlowPane) this.node;
            if(flowPane.getUserData() != null){
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.haveDrive"), resultMap, parentStage);
                return;
            }
            if(!isAllow){
                isAllow = true;
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
               // DragUtil.stage = stage;
            }
            else{
                isAllow = false;
                DragUtil.stage = null;
            }

        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED ) {
            double x = event.getSceneX();
            double y = event.getScreenY();
            node.setTranslateX(event.getScreenX() - xOffset);
            if (event.getScreenY() - yOffset < 0) {
                node.setTranslateY(0);
            } else {
                node.setTranslateY(event.getScreenY() - yOffset);

            }
        }
        else if (event.getEventType() == MouseEvent.MOUSE_MOVED && isAllow) {
            double x = event.getSceneX();
            double y = event.getScreenY();
            if(event.getScreenX() - xOffset < 0){
                node.setTranslateX(0);
            }
            else{
                node.setTranslateX(event.getScreenX() - xOffset);
            }
            if (event.getScreenY() - yOffset < 0) {
                node.setTranslateY(0);
            } else {
                node.setTranslateY(event.getScreenY() - yOffset);
            }
        }
        else if (event.getEventType() == MouseEvent.MOUSE_EXITED && isAllow) {
            node.setTranslateX(DragUtil.moveX - xOffset);
            node.setTranslateY(DragUtil.moveY - yOffset);
        }
    }*/

    public void enableDrag(Node node) {
        node.setOnMousePressed(this);
        //node.setOnMouseDragged(this);
        node.setOnMouseMoved(this);
        node.setOnMouseExited(this);
    }
}
