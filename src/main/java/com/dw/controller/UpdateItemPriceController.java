package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.PosItemDto;
import com.dw.entity.PosItem;
import com.dw.entity.PosPrice;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.ResultEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.mapper.PosPriceMapper;
import com.dw.service.PosItemService;
import com.dw.service.PosPriceService;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
public class UpdateItemPriceController implements Initializable {

    @FXML
    private FlowPane updatePriceflowPane;
    @FXML
    private DwLabel price1Label;
    @FXML
    private TextField price1TextField;
    @FXML
    private DwLabel price2Label;
    @FXML
    private TextField price2TextField;
    @FXML
    private DwLabel price3Label;
    @FXML
    private TextField price3TextField;
    @FXML
    private DwLabel price4Label;
    @FXML
    private TextField price4TextField;
    @FXML
    private DwLabel price5Label;
    @FXML
    private TextField price5TextField;
    @FXML
    private DwLabel price6Label;
    @FXML
    private TextField price6TextField;
    @FXML
    private DwLabel price7Label;
    @FXML
    private TextField price7TextField;
    @FXML
    private DwLabel price8Label;
    @FXML
    private TextField price8TextField;
    @FXML
    private DwLabel price9Label;
    @FXML
    private TextField price9TextField;
    @FXML
    private FlowPane buttomFlowPane;

    @FXML
    private DwButton updateButton;

    @FXML
    private DwButton cancleButton;

    @Autowired
    private ItemModifyController itemModifyController;

    private PosItemDto posItemDto;

    private PosPrice posPrice;

    @Autowired
    private PosItemService posItemService;

    @Autowired
    private ItemChooseController itemChooseController;

    @Autowired
    private TakeOrderIndexController takeOrderIndexController;

    @Autowired
    private PosPriceService posPriceService;

    @Autowired
    private NettyComponent nettyComponent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPaneComponent();
    }

    public void mainPaneComponent() {
        updatePriceflowPane.setPrefSize(itemModifyController.getLeftFlowPane().getPrefWidth(), itemModifyController.getLeftFlowPane().getPrefHeight());
        updatePriceflowPane.getStyleClass().add("topFlowPane");
        updatePriceflowPane.setVgap(updatePriceflowPane.getPrefHeight() / 11 / 15);
        double labelWidth = updatePriceflowPane.getPrefWidth() / 6;
        double labelHeight = (updatePriceflowPane.getPrefHeight()) / 11;
        double textFieldWidth = updatePriceflowPane.getPrefWidth() / 6 * 4.5;
        double textFiedsHeight = (updatePriceflowPane.getPrefHeight()) / 11;
        price1Label.setPrefSize(labelWidth, labelHeight);
        price1Label.setText(Main.languageMap.get("posItem.price1"));
        price1TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price1TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price1TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price1TextField.setText(result);
            }
        });
        price2Label.setPrefSize(labelWidth, labelHeight);
        price2Label.setText(Main.languageMap.get("posItem.price2"));
        price2TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price2TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price2TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price2TextField.setText(result);
            }
        });
        price3Label.setPrefSize(labelWidth, labelHeight);
        price3Label.setText(Main.languageMap.get("posItem.price3"));
        price3TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price3TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price3TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price3TextField.setText(result);
            }
        });
        price4Label.setPrefSize(labelWidth, labelHeight);
        price4Label.setText(Main.languageMap.get("posItem.price4"));
        price4TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price4TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price4TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price4TextField.setText(result);
            }
        });
        price5Label.setPrefSize(labelWidth, labelHeight);
        price5Label.setText(Main.languageMap.get("posItem.price5"));
        price5TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price5TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price5TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price5TextField.setText(result);
            }
        });
        price6Label.setPrefSize(labelWidth, labelHeight);
        price6Label.setText(Main.languageMap.get("posItem.price6"));
        price6TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price6TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price6TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price6TextField.setText(result);
            }
        });
        price7Label.setPrefSize(labelWidth, labelHeight);
        price7Label.setText(Main.languageMap.get("posItem.price7"));
        price7TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price7TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price7TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price7TextField.setText(result);
            }
        });
        price8Label.setPrefSize(labelWidth, labelHeight);
        price8Label.setText(Main.languageMap.get("posItem.price8"));
        price8TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price8TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price8TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price8TextField.setText(result);
            }
        });
        price9Label.setPrefSize(labelWidth, labelHeight);
        price9Label.setText(Main.languageMap.get("posItem.price9"));
        price9TextField.setPrefSize(textFieldWidth, textFiedsHeight);
        price9TextField.setOnMouseClicked(event -> {
            String result = ShowViewUtil.showNumbericKeyboard(itemModifyController.getItemStage(),"",price9TextField.getText(), false);
            if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                price9TextField.setText(result);
            }
        });
        buttomFlowPane.setPrefSize(itemModifyController.getLeftFlowPane().getPrefWidth(), updatePriceflowPane.getPrefHeight() / 10);
        buttomFlowPane.setHgap(updatePriceflowPane.getPrefWidth() / 4 / 2);
        buttomFlowPane.getStyleClass().add("buttomFlowPane");
        updateButton.setPrefSize(buttomFlowPane.getPrefWidth() / 4, buttomFlowPane.getPrefHeight());
        updateButton.getStyleClass().add("confirmButton");
        updateButton.setText(Main.languageMap.get("table.confirm"));
        updateButton.setOnAction(event -> {
            updateItemPrice();
        });
        cancleButton.setPrefSize(buttomFlowPane.getPrefWidth() / 4, buttomFlowPane.getPrefHeight());
        cancleButton.getStyleClass().add("closeButton");
        cancleButton.setText(Main.languageMap.get("global.close"));
        cancleButton.setOnAction(event -> {
            itemModifyController.getLeftFlowPane().getChildren().clear();
        });
    }

    public void initData(PosItemDto posItemDto) {
        this.posItemDto = posItemDto;
        Wrapper<PosPrice> posPriceWrapper = new EntityWrapper<>();
        posPriceWrapper.eq("OUTLET", Main.posOutlet);
        posPriceWrapper.eq("ITEM_CODE", posItemDto.getItemCode());
        posPriceWrapper.eq("IOPEN", "FALSE");
        posPrice = posPriceService.selectOne(posPriceWrapper);
        itemModifyController.getLeftFlowPane().getChildren().clear();
        price1TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice1()) ? posPrice.getPrice1().toString() : "0.00");
        price2TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice2()) ? posPrice.getPrice2().toString() : "0.00");
        price3TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice3()) ? posPrice.getPrice3().toString() : "0.00");
        price4TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice4()) ? posPrice.getPrice4().toString() : "0.00");
        price5TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice5()) ? posPrice.getPrice5().toString() : "0.00");
        price6TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice6()) ? posPrice.getPrice6().toString() : "0.00");
        price7TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice7()) ? posPrice.getPrice7().toString() : "0.00");
        price8TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice8()) ? posPrice.getPrice8().toString() : "0.00");
        price9TextField.setText(AppUtils.isNotBlank(posPrice) && AppUtils.isNotBlank(posPrice.getPrice9()) ? posPrice.getPrice9().toString() : "0.00");
        itemModifyController.getLeftFlowPane().getChildren().add(updatePriceflowPane);
    }

    public void updateItemPrice() {
        try {
            if (AppUtils.isBlank(posPrice)) {
                posPrice = new PosPrice();
                posPrice.setOutline(Main.outline);
                posPrice.setOutlet(Main.posOutlet);
                posPrice.setItemCode(posItemDto.getItemCode());
                posPrice.setIopen("FALSE");
                posPrice.setLastUpdateNameId(Main.posStaff.getCode());

            }
            posPrice.setPrice1(DecimalUtil.getBigDecimal(price1TextField.getText()));
            posPrice.setPrice2(DecimalUtil.getBigDecimal(price2TextField.getText()));
            posPrice.setPrice3(DecimalUtil.getBigDecimal(price3TextField.getText()));
            posPrice.setPrice4(DecimalUtil.getBigDecimal(price4TextField.getText()));
            posPrice.setPrice5(DecimalUtil.getBigDecimal(price5TextField.getText()));
            posPrice.setPrice6(DecimalUtil.getBigDecimal(price6TextField.getText()));
            posPrice.setPrice7(DecimalUtil.getBigDecimal(price7TextField.getText()));
            posPrice.setPrice8(DecimalUtil.getBigDecimal(price8TextField.getText()));
            posPrice.setPrice9(DecimalUtil.getBigDecimal(price9TextField.getText()));
            posPriceService.insertOrUpdate(posPrice);
            //发送netty消息
            nettyComponent.sendMessage(NettyMessageTypeEnum.UPDATEITEMPRICE);
            itemModifyController.refreshItems();
            itemModifyController.nextPageTopButton(itemModifyController.getCurrentTopButtonPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getCurrentItemPage(),itemModifyController.getCurrentItemPageCount(),itemModifyController.getTopButtonDtos());
            //刷新点菜界面的items
            takeOrderIndexController.refreshItems();
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("posItem.updatePrice.success"), resultMap, itemModifyController.getItemStage());
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("posItem.updatePrice.error"), resultMap, itemModifyController.getItemStage());
        }
    }


}
