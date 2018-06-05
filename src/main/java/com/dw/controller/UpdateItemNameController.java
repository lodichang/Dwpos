package com.dw.controller;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.PosItemDto;
import com.dw.dto.TableViewDto;
import com.dw.entity.PosItem;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.ResultEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.handwrite.hanzidict.HanziDict;
import com.dw.service.PosItemService;
import com.dw.util.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by wen.jing on 2018/5/15
 */
@Setter
@Getter
@FXMLController
public class UpdateItemNameController implements Initializable {

    @FXML
    private FlowPane updateNameflowPane;
    @FXML
    private DwLabel desc1Label;
    @FXML
    private TextField desc1TextField;
    @FXML
    private DwLabel desc2Label;
    @FXML
    private TextField desc2TextField;
    @FXML
    private DwLabel desc3Label;
    @FXML
    private TextField desc3TextField;
    @FXML
    private DwLabel desc4Label;
    @FXML
    private TextField desc4TextField;

    @FXML
    private FlowPane buttomFlowPane;

    @FXML
    private DwButton updateButton;

    @FXML
    private DwButton cancleButton;

    @Autowired
    private ItemModifyController itemModifyController;

    private PosItemDto posItemDto;

    @Autowired
    private PosItemService posItemService;

    @Autowired
    private ItemChooseController itemChooseController;

    @Autowired
    private TakeOrderIndexController takeOrderIndexController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPaneComponent();
    }

    public void mainPaneComponent() {
        updateNameflowPane.setPrefSize(itemModifyController.getLeftFlowPane().getPrefWidth(), itemModifyController.getLeftFlowPane().getPrefHeight());
        updateNameflowPane.getStyleClass().add("topFlowPane");
        updateNameflowPane.setVgap(10);
        double labelWidth = updateNameflowPane.getPrefWidth() / 6;
        double labelHeight = (updateNameflowPane.getPrefHeight()) / 10;
        double textFieldWidth = updateNameflowPane.getPrefWidth() / 6 * 4.5;
        double textFiedsHeight = (updateNameflowPane.getPrefHeight()) / 10;
        desc1Label.setPrefSize(labelWidth, labelHeight);
        desc1Label.setText(Main.languageMap.get("posItem.name1"));
        desc1TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        desc1TextField.setOnMouseClicked(event -> {
            HandWriteUtil.showHandWriteView(desc1TextField,itemModifyController.getItemStage());
        });
        desc2Label.setPrefSize(labelWidth, labelHeight);
        desc2Label.setText(Main.languageMap.get("posItem.name2"));
        desc2TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        desc2TextField.setOnMouseClicked(event -> {
            HandWriteUtil.showHandWriteView(desc2TextField,itemModifyController.getItemStage());
        });
        desc3Label.setPrefSize(labelWidth, labelHeight);
        desc3Label.setText(Main.languageMap.get("posItem.name3"));
        desc3TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        desc3TextField.setOnMouseClicked(event -> {
            HandWriteUtil.showHandWriteView(desc3TextField,itemModifyController.getItemStage());
        });
        desc4Label.setPrefSize(labelWidth, labelHeight);
        desc4Label.setText(Main.languageMap.get("posItem.name4"));
        desc4TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        desc4TextField.setOnMouseClicked(event -> {
            HandWriteUtil.showHandWriteView(desc4TextField,itemModifyController.getItemStage());
        });
        buttomFlowPane.setPrefSize(itemModifyController.getLeftFlowPane().getPrefWidth(), updateNameflowPane.getPrefHeight() / 10);
        buttomFlowPane.setHgap(updateNameflowPane.getPrefWidth() / 4 / 2);
        buttomFlowPane.getStyleClass().add("buttomFlowPane");
        updateButton.setPrefSize(buttomFlowPane.getPrefWidth() / 4, buttomFlowPane.getPrefHeight());
        updateButton.getStyleClass().add("confirmButton");
        updateButton.setText(Main.languageMap.get("table.confirm"));
        updateButton.setOnAction(event -> {
            updateItemName();
        });
        cancleButton.setPrefSize(buttomFlowPane.getPrefWidth() / 4, buttomFlowPane.getPrefHeight());
        cancleButton.getStyleClass().add("closeButton");
        cancleButton.setOnAction(event -> {
            itemModifyController.getLeftFlowPane().getChildren().clear();
        });
        cancleButton.setText(Main.languageMap.get("global.close"));
    }

    public void initData(PosItemDto posItemDto) {
        this.posItemDto = posItemDto;
        itemModifyController.getLeftFlowPane().getChildren().clear();
        desc1TextField.setText(posItemDto.getDesc1());
        desc2TextField.setText(posItemDto.getDesc2());
        desc3TextField.setText(posItemDto.getDesc3());
        desc4TextField.setText(posItemDto.getDesc4());
        itemModifyController.getLeftFlowPane().getChildren().add(updateNameflowPane);
    }

    public void updateItemName() {
        try {
            Wrapper<PosItem> posItemWrapper = new EntityWrapper<>();
            posItemWrapper.eq("ITEM_CODE", posItemDto.getItemCode());
            PosItem posItem = posItemService.selectOne(posItemWrapper);
            if (posItem != null) {
                posItem.setDesc1(desc1TextField.getText());
                posItem.setDesc2(desc2TextField.getText());
                posItem.setDesc3(desc3TextField.getText());
                posItem.setDesc4(desc4TextField.getText());
            }
            posItemService.updateById(posItem);
            posItemDto.setDesc1(desc1TextField.getText());
            posItemDto.setDesc2(desc2TextField.getText());
            posItemDto.setDesc3(desc3TextField.getText());
            posItemDto.setDesc4(desc4TextField.getText());
            itemModifyController.initRightItems(itemModifyController.getCurrentItemPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getTopButtonDtos().get(itemModifyController.getCurrTopButtonPos()).getPosItemDtoList());
            //刷新点菜界面的items
            takeOrderIndexController.refreshItems();
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("posItem.updateName.success"), resultMap, itemModifyController.getItemStage());
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("posItem.updateName.error"), resultMap, itemModifyController.getItemStage());
        }
    }


}
