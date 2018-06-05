package com.dw.listener;

import com.dw.util.DragUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Created by lodi on 2018/4/9.
 */
public class ClickListener implements EventHandler<MouseEvent> {
    public  double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            DragUtil.clickX = xOffset;
            DragUtil.clickX = xOffset;
            DragUtil.clickY = yOffset;
        }
    }

    public void enableDrag(Node node) {
        node.setOnMousePressed(this);
    }
}
