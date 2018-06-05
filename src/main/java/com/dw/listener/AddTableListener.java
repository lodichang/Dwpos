package com.dw.listener;

import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.controller.TableSettingsController;
import com.dw.entity.PosTable;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.service.PosTableService;
import com.dw.util.DragUtil;
import com.dw.util.SpringContextUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;

/**
 * Created by lodi on 2018/4/9.
 */
public class AddTableListener implements EventHandler<MouseEvent> {
    private double xOffset = 20;
    private double yOffset = 20;
    private final Stage stage;
    boolean isAllow = true;
    private PosTable currentPosTable;
    private TableSettingsController tableSettingsController;
    private Node node;

    public AddTableListener(Stage stage, PosTable posTable, TableSettingsController tableSettingsController, Node node) {
        this.stage = stage;
        this.currentPosTable = posTable;
        this.tableSettingsController = tableSettingsController;
        this.node = node;
    }

    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED && isAllow) {
            isAllow = false;
            BigDecimal yRatio =  new BigDecimal(stage.getY()).divide(new BigDecimal(Main.primaryScreenBounds.getMaxY()),8,BigDecimal.ROUND_DOWN);
            BigDecimal xRatio =  new BigDecimal(stage.getX()).divide(new BigDecimal(Main.primaryScreenBounds.getMaxX()),8,BigDecimal.ROUND_DOWN);
            currentPosTable.setXRatio(xRatio);
            currentPosTable.setYRatio(yRatio);
            PosTableService posTableService = (PosTableService) SpringContextUtil.getBean(PosTableService.class);
            posTableService.updateById(currentPosTable);
            //发送netty刷新台号
            NettyComponent nettyComponent = (NettyComponent) SpringContextUtil.getBean(NettyComponent.class);
            if(nettyComponent != null){
                nettyComponent.sendMessage(NettyMessageTypeEnum.ADDTABLE);
            }
            tableSettingsController.getTableFlowPaneStageList().forEach((Stage stage)->{
                stage.close();
            });
           /* newTableSettingsController.getTableFlowPaneList().add((FlowPane) node);
            newTableSettingsController.getTableFlowPaneStageList().add(stage);*/
            DragUtil.stage = null;
            tableSettingsController.refreshTables(tableSettingsController.getCurrentPage(), tableSettingsController.getCurrentArea(), tableSettingsController.getNewTableSettingStage(),false);
            /*if(AppUtils.isNotBlank(newTableSettingsController.disableIds)){
                newTableSettingsController.disableIds.clear();
            }
            newTableSettingsController.disableIds.add("cancleButton");
            if(newTableSettingsController.nextPageButton.isDisable()){
                newTableSettingsController.disableIds.add("nextPageButton");
            }*/
           // newTableSettingsController.setDistable(true,newTableSettingsController.disableIds);


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
                stage.setX(event.getScreenX() - xOffset);
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

    public void enableDrag(Node node) {
        node.setOnMousePressed(this);
        node.setOnMouseMoved(this);
        node.setOnMouseExited(this);
    }
}
