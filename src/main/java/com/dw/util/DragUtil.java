package com.dw.util;

import com.dw.controller.TableSettingsController;
import com.dw.entity.PosTable;
import com.dw.listener.AddTableListener;
import com.dw.listener.ClickListener;
import com.dw.listener.DragListener;
import com.dw.listener.MoveListener;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/4/9.
 */
@Setter
@Getter
public class DragUtil {

    public static double clickX;

    public static double clickY;

    public static double moveX;

    public static double moveY;

    public static Stage stage;


    public static void addDragListener(Stage stage ,Node root,Stage parentStage) {
        new DragListener(stage,root,parentStage).enableDrag(root);
    }

    public static void addClickListener( Node root) {
        new ClickListener().enableDrag(root);
    }

    public static void addMoveListener( Node root) {
        new MoveListener().enableDrag(root);
    }


    public static void addTableListener(Stage stage, Node root, PosTable posTable, TableSettingsController tableSettingsController) {
        new AddTableListener(stage,posTable, tableSettingsController,root).enableDrag(root);
    }

}
