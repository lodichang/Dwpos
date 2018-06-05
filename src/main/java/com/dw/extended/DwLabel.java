package com.dw.extended;

import com.dw.Main;
import com.dw.enums.FontSizeEnum;
import com.dw.util.AppUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * 擴展BUTTON，用以增加權限驗證
 */
public class DwLabel extends Label implements EventHandler<MouseEvent> {

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

    //代碼生成-需要驗證權限
    public DwLabel(String id, FontSizeEnum fontSize) {
        this.setUserData(id);
        this.setFont(Main.defaultFont);
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
        this.setWrapText(true);
        if (AppUtils.isBlank(this.getUserData())) {
            // System.out.println("警告**********：繼承此類型的按鈕需要配置ID");
        }

        //權限驗證
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
    }

    //代碼生成-需要驗證權限
    public DwLabel(String id ) {
        this.setUserData(id);
        this.setFont(Main.defaultFont);
        this.setWrapText(true);
        if (AppUtils.isBlank(this.getUserData())) {
            // System.out.println("警告**********：繼承此類型的按鈕需要配置ID");
        }
        //權限驗證
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
    }
    //fxml用的-需要驗證權限
    public DwLabel() {
        this.setFont(Main.defaultFont);
        this.setWrapText(true);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
    }
    //只改變文字大小-不驗證權限
    public DwLabel(FontSizeEnum fontSize) {
        this.setFont(Main.defaultFont);
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
        this.setWrapText(true);
    }



    @Override
    public void handle(MouseEvent event) {
        //權限驗證主邏輯代碼 --方便開發先註釋
//        String rightKey = this.getUserData().toString().split("-")[0];
//
//        String isAess = Main.staffRight.get(rightKey);
//        if (AppUtils.isBlank(isAess)) {
//            Map<String, String> resultMap = new LinkedHashMap<String, String>();
//            resultMap.put((Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
//            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("right.denied"), resultMap, null);
//
//        }

    }

    public void setFontSize(FontSizeEnum fontSize) {
        this.setStyle("-fx-font-size: " + fontSize.getValue() + "px");
    }

    /**
     * 初始化一般lable
     * @param width
     * @param height
     * @param text
     * @param styleClass
     */
    public void initLable(double width, double height, String text, String styleClass){
        this.setPrefSize(width, height);
        this.setText(text);
        this.getStyleClass().add(styleClass);
    }

    /**
     * 初始化LABLE,帶圖標,對齊屬性
     * @param width
     * @param height
     * @param text
     * @param styleClass
     */
    public void initLable(double width, double height, String text, String styleClass, ImageView icon, ContentDisplay contentDisplay, Pos pos){
        this.setPrefSize(width, height);
        this.setText(text);
        this.getStyleClass().add(styleClass);
        this.setGraphic(icon);
        this.setContentDisplay(contentDisplay);
        this.setAlignment(pos);
    }


}

