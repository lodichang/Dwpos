package com.dw.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.entity.PosStaff;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.ResultEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.service.PosStaffService;
import com.dw.util.AppUtils;
import com.dw.util.ShowViewUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

@Getter
@Setter
@FXMLController
public class EtViewController implements Initializable {
    @FXML
    private FlowPane etFlowPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private FlowPane middlePane;
    @FXML
    private FlowPane buttomPane;

    private TextField codetextField;
    private TextField cardtextField;

    private PosStaff posStaff;

    private Stage etStage;
    @Autowired
    private PosStaffService posStaffService;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            etStage = (Stage) topFlowPane.getScene().getWindow();
                }
        );
        couponComponent();

    }

    private void couponComponent() {
        double width = Main.primaryScreenBounds.getWidth() * 0.6;
        double height = Main.primaryScreenBounds.getHeight() * 0.6;

        etFlowPane.setPrefSize(width, height*0.8);

        topFlowPane.setPrefSize(width,height / 10 * 3);
        topFlowPane.setPadding(new Insets(5,0,5,5));
        DwLabel codeLabel = new DwLabel(FontSizeEnum.font24);
        codeLabel.setText(Main.languageMap.get("et.codeLabel"));
        codeLabel.setPrefSize(width*0.2,height / 10 * 3 * 0.8);
        codeLabel.setTextFill(Color.web("#d0d0d0"));
        topFlowPane.getChildren().add(codeLabel);
        codetextField = new TextField();
        codetextField.setPrefSize(width*0.5,height / 10 * 1.2);
        topFlowPane.getChildren().add(codetextField);
        DwButton keyboard1 = new DwButton(FontSizeEnum.font14);
        keyboard1.setGraphic(new ImageView(AppUtils.loadImage("keyboard.png")));
        keyboard1.setPrefSize(width*0.1,height / 10 * 1.2);
        keyboard1.setOnAction(event -> {
            String inCode = ShowViewUtil.showKeyword(Main.getStage(),"");
            if(AppUtils.isNotBlank(inCode) && !inCode.equals(ResultEnum.NO.getValue())){
                checkCode(inCode);
            }
        });
        topFlowPane.getChildren().addAll(keyboard1);

        middlePane.setPrefSize(width,height / 10 * 3);
        middlePane.setPadding(new Insets(5,0,5,5));
        DwLabel cardLabel = new DwLabel(FontSizeEnum.font24);
        cardLabel.setText(Main.languageMap.get("et.cardLabel"));
        cardLabel.setPrefSize(width*0.2,height / 10 * 3 * 0.8);
        cardLabel.setTextFill(Color.web("#d0d0d0"));
        middlePane.getChildren().add(cardLabel);
        cardtextField = new TextField();
        cardtextField.setPrefSize(width*0.5,height / 10 * 1.2);
        cardtextField.setPrefColumnCount(18);
        middlePane.getChildren().add(cardtextField);
        DwButton keyboard2 = new DwButton(FontSizeEnum.font14);
        keyboard2.setGraphic(new ImageView(AppUtils.loadImage("keyboard.png")));
        keyboard2.setPrefSize(width*0.1,height / 10 * 1.2);
        keyboard2.setOnAction(event -> {
            String incardno = ShowViewUtil.showKeyword(Main.getStage(),"");
            if(AppUtils.isNotBlank(incardno) && !incardno.equals(ResultEnum.NO.getValue())){
                cardtextField.setText(incardno);
            }
        });
        middlePane.getChildren().add(keyboard2);

        buttomPane.setPrefSize(width,height / 10 * 3);
        buttomPane.setPadding(new Insets(5,15,5,15));
        DwButton cancalButton = new DwButton();
        cancalButton.setPrefSize(width*0.35,height / 10 * 3 * 0.6);
        cancalButton.setPadding(new Insets(0,10,0,0));
        cancalButton.setText(Main.languageMap.get("et.cancel"));
        cancalButton.setOnAction(event -> {
            closePage();
        });
        FlowPane pane = new FlowPane();
        pane.setPrefWidth(25);
        DwButton updateButton = new DwButton();
        updateButton.setPrefSize(width*0.35,height / 10 * 3 * 0.6);
        updateButton.setText(Main.languageMap.get("et.submit"));
        updateButton.setOnAction(event -> {
            submitData();
        });
        buttomPane.getChildren().addAll(cancalButton,pane,updateButton);







    }

    private void checkCode(String code){
        Wrapper<PosStaff> posStaffWrapper = new EntityWrapper<>();
        posStaffWrapper.eq("CODE",code);
        posStaffWrapper.eq("ISGROUP","0");
        posStaff = posStaffService.selectOne(posStaffWrapper);
        if(AppUtils.isNotBlank(posStaff)){
            codetextField.setText(code);
        }else{
            showDialog(Main.languageMap.get("et.codeError"));
            codetextField.setText("");
            cardtextField.setText("");
        }
    }

    private void submitData(){
        if(AppUtils.isNotBlank(posStaff)){
            posStaff.setCardCode(cardtextField.getText());
            if(posStaffService.updateById(posStaff)){
                showDialog(Main.languageMap.get("et.codesuccess"));
                closePage();
            }else{
                showDialog(Main.languageMap.get("et.codeError1"));
            }
        }
    }

    private void closePage(){
        etStage.close();
        codetextField.setText("");
        cardtextField.setText("");
    }


    private String showDialog(String msg){
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
        String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), msg, resultMap, etStage);
        return result;
    }








}
