package com.dw.extended;

import com.dw.Main;
import com.dw.enums.FontSizeEnum;
import com.dw.util.AppUtils;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 擴展BUTTON，用以增加權限驗證
 */
public class DwButton extends Button implements EventHandler<MouseEvent> {
    private Map<String, DwButton> exclusionMap = new HashMap<>();
    private List<DwButton> exclusionButtons = new ArrayList<>();
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

    public void exclusion(){}

    //代碼生成-需要驗證權限
    public DwButton(String id, FontSizeEnum fontSize) {
        this.setUserData(id);
        this.fontProperty().setValue(Main.defaultFont);
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
        this.setWrapText(true);

        if (AppUtils.isBlank(this.getUserData())) {
            System.out.println("警告**********：繼承此類型的按鈕需要配置ID");
        }

        //權限驗證
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());
    }

    //代碼生成-需要驗證權限
    public DwButton(String id ) {
        this.setUserData(id);
        this.fontProperty().setValue(Main.defaultFont);
        this.setWrapText(true);
        if (AppUtils.isBlank(this.getUserData())) {
            System.out.println("警告**********：繼承此類型的按鈕需要配置ID");
        }

        //權限驗證
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());
    }

    //fxml用的-需要驗證權限
    public DwButton() {
        this.setFont(Main.defaultFont);
        this.setWrapText(true);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());
    }

    //只改變文字大小-不驗證權限
    public DwButton(FontSizeEnum fontSize) {

        this.fontProperty().setValue(Main.defaultFont);
//        Platform.runLater(() -> this.setStyle("-fx-font-size: " + fontSize.getValue() + "px"));
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
        this.setWrapText(true);
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());
    }

    //在初始化的時候即刻檢查權限，然後按鈕變成灰色
    public DwButton(String id, FontSizeEnum fontSize, Boolean checkRightOnInit) {
        this.setUserData(id);
        this.fontProperty().setValue(Main.defaultFont);
        this.setWrapText(true);

        if (checkRightOnInit) {
            String rightKey = this.getUserData().toString().split("-")[0];
            String isAess = Main.staffRight.get(rightKey);
            if (AppUtils.isBlank(isAess)) {
//                Platform.runLater(() -> this.setStyle("-fx-font-size: " + fontSize.getValue() + "px;-fx-background-color: #d0d0d0;-fx-text-fill: #707070;"));
                this.setStyle("-fx-font-size: " + fontSize.getValue() + "px;-fx-background-color: #d0d0d0;-fx-text-fill: #707070;");
                this.setDisable(true);
            } else {
//                Platform.runLater(() -> this.setStyle("-fx-font-size: " + fontSize.getValue() + "px"));
              this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
            }
        } else {
//            Platform.runLater(() -> this.setStyle("-fx-font-size: " + fontSize.getValue() + "px"));
             this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
            //權限驗證
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        }
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());

    }

    //在初始化的時候是否加陰影效果
    public DwButton(FontSizeEnum fontSize,Boolean needShadow) {
        this.fontProperty().setValue(Main.defaultFont);
//        Platform.runLater(() -> this.setStyle("-fx-font-size: " + fontSize.getValue() + "px"));
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
        this.setWrapText(true);

        if(needShadow) {
            DropShadow shadow = new DropShadow();
            //当鼠标进入按钮时添加阴影特效
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                this.setEffect(shadow);
            });
            //当鼠标离开按钮时移除阴影效果
            this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                this.setEffect(null);
            });
        }
        this.addEventHandler(ActionEvent.ACTION, (e)-> checkExclusion());
    }

    /**
     * 执行互斥方法
     */
    private void checkExclusion() {
        //exclusionMap.forEach((k,v) -> v.exclusion());
        //exclusionButtons.forEach(DwButton::exclusion);

        exclusionButtons.forEach(dwButton -> {
            if(!(AppUtils.isNotBlank(this.getUserData()) ? this.getUserData().toString() : this.getClass().toString()).equals(AppUtils.isNotBlank(dwButton.getUserData()) ? dwButton.getUserData().toString() : dwButton.getClass().toString())){
                dwButton.exclusion();
            }
        });
    }

    @Override
    public void handle(MouseEvent event) {
        //權限驗證主邏輯代碼 --方便開發先註釋
//        String rightKey = this.getUserData().toString().split("-")[0];
//
//        String isAess = Main.staffRight.get(rightKey);
//        if(AppUtils.isBlank(isAess)) {
//            Map<String, String> resultMap = new LinkedHashMap<String, String>();
//            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
//            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"),Main.languageMap.get("right.denied"),resultMap,null);
//
//        }
    }

    public void setFontSize(FontSizeEnum fontSize) {
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
    }

    /**
     * 初始化一般按鈕
     * @param width
     * @param height
     * @param text
     * @param styleClass
     */
    public void initButton(double width, double height, String text, String styleClass){
        this.setPrefSize(width, height);
        this.setText(text);
        this.getStyleClass().add(styleClass);
    }

    public void addExclusion(List<DwButton> button) {
       /* for(int i=0;i<button.length;i++){
            exclusionMap.put(AppUtils.isNotBlank(this.getUserData()) ? this.getUserData().toString() : this.getClass().toString(), button[i]);
        }
*/
        exclusionButtons = button;
    }


}

