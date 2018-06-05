package com.dw.extended;

import com.dw.Main;
import com.dw.enums.ResultEnum;
import com.dw.util.AppUtils;
import com.dw.util.ShowViewUtil;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lodi on 2018/4/18.
 */
public class DwFlowPane extends FlowPane implements EventHandler<MouseEvent>{

    /**
     * 用於存放綁定按鈕的對象
     */
    private Object bindData;

    public Object getBindData() {
        return bindData;
    }

    public void setBindData(Object bindData) {
        this.bindData = bindData;
    }

   public void DwFlowPane(String id){
       this.setUserData(id);
       this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
   }


    public void DwFlowPane(){
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
    }

    @Override
    public void handle(MouseEvent event) {
        //權限驗證主邏輯代碼 --方便開發先註釋
        String rightKey = this.getUserData().toString().split("-")[0];

        String isAess = Main.staffRight.get(rightKey);
       if (AppUtils.isBlank(isAess)) {
           Map<String, String> resultMap = new LinkedHashMap<String, String>();
           resultMap.put((Main.languageMap.get("popups.ok")), ResultEnum.YES.getValue());
           ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("right.denied"), resultMap, null);

        }

    }


}
