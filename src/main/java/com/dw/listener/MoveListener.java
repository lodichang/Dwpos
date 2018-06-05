package com.dw.listener;

import com.dw.util.DragUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Created by lodi on 2018/4/9.
 */
public class MoveListener implements EventHandler<MouseEvent> {
    private double xOffset = 20;
    private double yOffset = 20;
    private boolean isAllow = true;


    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED && isAllow) {
            if(DragUtil.stage != null){
                DragUtil.moveX = event.getSceneX();
                DragUtil.moveY = event.getScreenY();
                DragUtil.stage.setX( DragUtil.moveX - xOffset);
                DragUtil.stage.setY( DragUtil.moveY - yOffset);
                isAllow = false;
            }
        }
        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            if(DragUtil.stage != null){
                DragUtil.moveX = event.getSceneX();
                DragUtil.moveY = event.getScreenY();
                DragUtil.stage.setX( DragUtil.moveX - xOffset);
                DragUtil.stage.setY( DragUtil.moveY - yOffset);
            }

        }

    }

    public void enableDrag(Node node) {
        node.setOnMouseMoved(this);
    }
}
