package com.dw.util;

import com.dw.extended.DwButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by lodi on 2018/4/17.
 */
public class ButtonUtil {

    public static  void shadowButton(DwButton btn){
        DropShadow shadow = new DropShadow(15, Color.RED);
//当鼠标进入按钮时添加阴影特效
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btn.setEffect(shadow);
        });
//当鼠标离开按钮时移除阴影效果
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btn.setEffect(null);
        });
    }


}
