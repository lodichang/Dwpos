package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.entity.PosTable;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TableStateEnum;
import com.dw.enums.TableTypeEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.service.PosTableService;
import com.dw.util.AppUtils;
import com.dw.util.DragUtil;
import com.dw.util.ShowViewUtil;
import com.dw.util.SpringContextUtil;
import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

//import org.apache.commons.collections.map.HashedMap;

/**
 * Created by lodi on 2018/3/6.
 */
@Getter
@Setter
public class TableEditController extends FlowPane  {

   @FXML
   private DwLabel tableEditTitleLabel;

   @FXML
   private FlowPane tableEditFlowPane;

   @FXML
   private FlowPane tableNoFlowPane;

   @FXML
   private DwLabel tableNoLabel;

   @FXML
   private TextField tableNoTextField;

    @FXML
    private FlowPane roomTypeFlowPane;

    @FXML
    private DwLabel roomTypeLabel;

    @FXML
    private ComboBox  roomTypeComboBox;


   @FXML
   private FlowPane remarksFlowPane;

   @FXML
   private DwLabel remarksLabel;


   @FXML
   private RadioButton yesRemarkRadioButton;

   @FXML
   private RadioButton noRemarkRadioButton;


   @FXML
   private FlowPane isOnlineFlowPane;

   @FXML
   private DwLabel isOnlineLabel;

   @FXML
   private RadioButton notOnlineRadioButton;

   @FXML
   private RadioButton canOnlineRadioButton;

   @FXML
   private FlowPane maxPersonFlowPane;

    @FXML
    private DwLabel maxPersonLabel;

    @FXML
    private TextField maxPersonTextField;


   @FXML
   private FlowPane buttomFlowPane;

   @FXML
   private DwButton confirmButton;

   @FXML
   private DwButton cancleButton;

   private DwButton addTableButton;

   private Stage parentStage;

   private Stage tableEditStage;

   private String roomType;

   private String selectedFloor;

   private TableSettingsController tableSettingsController;

   final ToggleGroup remarksGroup = new ToggleGroup();

   final ToggleGroup isOnlineGroup = new ToggleGroup();

   /*@Autowired
   private PosTableService posTableService;*/

    /*public TableEditController() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("controller/view/TableEditView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }*/




     public  void show(String roomNum) {
        PosTableService posTableService = (PosTableService) SpringContextUtil.getBean(PosTableService.class);
        Wrapper<PosTable> posTabWrapper = new EntityWrapper<>();
        posTabWrapper.eq("ROOM_NUM",roomNum);
        PosTable posTable = posTableService.selectOne(posTabWrapper);
        double tableEditFlowPaneWidth  = Main.primaryScreenBounds.getWidth()/2;
        double tableEditFlowPaneHeight = parentStage.getHeight()/5*4;
        tableEditFlowPane.setPrefWidth(tableEditFlowPaneWidth);
        tableEditFlowPane.setPrefHeight(tableEditFlowPaneHeight);
        tableEditFlowPane.getStyleClass().add("tableEditFlowPane");

        double contentFlowPaneWidth = tableEditFlowPaneWidth;
        double contentFlowPaneHeight = tableEditFlowPaneHeight/10;

        double labelWidth = contentFlowPaneWidth/2;
        double labelHeight = contentFlowPaneHeight-contentFlowPaneHeight/100;

        double textFieldWidth = contentFlowPaneWidth/2/1.5;
        double textFieldHeight = contentFlowPaneHeight/1.5;

        double radioButtonWidth =  contentFlowPaneWidth/2/3.1;
        double radioButtonHeight = contentFlowPaneHeight;

        double comboBoxWidth =   contentFlowPaneWidth/2/1.5;
        double comboBoxHeight =  contentFlowPaneHeight/1.5;

        double buttonWidth =  contentFlowPaneWidth/5;
        double buttonHeight =  contentFlowPaneHeight-contentFlowPaneHeight/100;

        //標題欄
        tableEditTitleLabel.setPrefWidth(contentFlowPaneWidth);
        tableEditTitleLabel.setPrefHeight(contentFlowPaneHeight*2);
        tableEditTitleLabel.setText(Main.languageMap.get("table.tableEditTitle"));
        tableEditTitleLabel.setFontSize(FontSizeEnum.font24);
        tableEditTitleLabel.getStyleClass().add("tableEditTitleLabel");

        //檯號欄
        tableNoFlowPane.getStyleClass().add("tableNoFlowPane");
        tableNoFlowPane.setPrefWidth(contentFlowPaneWidth);
        tableNoFlowPane.setPrefHeight(contentFlowPaneHeight);
        tableNoLabel.setPrefWidth(labelWidth);
        tableNoLabel.setPrefHeight(labelHeight);
        tableNoLabel.setFontSize(FontSizeEnum.font22);
        tableNoLabel.setText(Main.languageMap.get("table.num"));
        tableNoLabel.getStyleClass().add("label");
        tableNoTextField.setPrefWidth(textFieldWidth);
        tableNoTextField.setPrefHeight(textFieldHeight);
        tableNoTextField.setFont(Font.font(FontSizeEnum.font22.getValue()));
        if(AppUtils.isNotBlank(posTable)) {
            tableNoTextField.setText(posTable.getRoomNum());
            tableNoTextField.setDisable(true);
        }


         //餐檯類型
         roomTypeFlowPane.getStyleClass().add("roomTypeFlowPane");
         roomTypeFlowPane.setPrefWidth(contentFlowPaneWidth);
         roomTypeFlowPane.setPrefHeight(contentFlowPaneHeight);
         roomTypeLabel.setPrefWidth(labelWidth);
         roomTypeLabel.setPrefHeight(labelHeight);
         roomTypeLabel.setFontSize(FontSizeEnum.font22);
         roomTypeLabel.getStyleClass().add("label");
         roomTypeLabel.setText(Main.languageMap.get("table.type"));
         roomTypeComboBox.setPrefWidth(comboBoxWidth);
         roomTypeComboBox.setPrefHeight(comboBoxHeight);
         roomTypeComboBox.getStyleClass().add("comboBox");
         Map<String,String> map = new HashMap();

         for(String key : TableTypeEnum.getTableTypes().keySet()){
             roomTypeComboBox.getItems().add(key);
         }
         roomTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

             @Override
             public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                 roomType = TableTypeEnum.getValueByName(newValue.toString());
             }
         });
       if(AppUtils.isNotBlank(posTable)){
          roomTypeComboBox.getSelectionModel().select(TableTypeEnum.getNameByValue(posTable.getRoomType()));
        }

        //是否容許網上訂台

         isOnlineFlowPane.getStyleClass().add("remarksFlowPane");
         isOnlineFlowPane.setPrefWidth(contentFlowPaneWidth);
         isOnlineFlowPane.setPrefHeight(contentFlowPaneHeight);
         isOnlineLabel.setPrefWidth(labelWidth);
         isOnlineLabel.setPrefHeight(labelHeight);
         isOnlineLabel.setFontSize(FontSizeEnum.font22);
         isOnlineLabel.getStyleClass().add("label");
         isOnlineLabel.setText(Main.languageMap.get("table.isOnline"));
         notOnlineRadioButton.setPrefWidth(radioButtonWidth);
         notOnlineRadioButton.setPrefHeight(radioButtonHeight);
         notOnlineRadioButton.setFont(new Font(FontSizeEnum.font18.getValue()));
         notOnlineRadioButton.getStyleClass().add("radioButton");
         notOnlineRadioButton.setText(Main.languageMap.get("popups.no"));
         notOnlineRadioButton.setToggleGroup(isOnlineGroup);
         notOnlineRadioButton.setUserData(0);


         canOnlineRadioButton.setPrefWidth(radioButtonWidth);
         canOnlineRadioButton.setPrefHeight(radioButtonHeight);
         canOnlineRadioButton.setFont(new Font(FontSizeEnum.font18.getValue()));
         canOnlineRadioButton.getStyleClass().add("radioButton");
         canOnlineRadioButton.setText(Main.languageMap.get("popups.yes"));
         canOnlineRadioButton.setToggleGroup(isOnlineGroup);
         canOnlineRadioButton.setUserData(1);

         if(AppUtils.isNotBlank(posTable)){
            if("1".equals(posTable.getIsOnline())) {
                isOnlineGroup.selectToggle(canOnlineRadioButton);
            }
            else{
                isOnlineGroup.selectToggle(notOnlineRadioButton);
            }
         }

         //是否搭臺

         remarksFlowPane.getStyleClass().add("remarksFlowPane");
         remarksFlowPane.setPrefWidth(contentFlowPaneWidth);
         remarksFlowPane.setPrefHeight(contentFlowPaneHeight);
         remarksLabel.setPrefWidth(labelWidth);
         remarksLabel.setPrefHeight(labelHeight);
         remarksLabel.setFontSize(FontSizeEnum.font22);
         remarksLabel.getStyleClass().add("label");
         remarksLabel.setText(Main.languageMap.get("table.remarks"));
         yesRemarkRadioButton.setPrefWidth(radioButtonWidth);
         yesRemarkRadioButton.setPrefHeight(radioButtonHeight);
         yesRemarkRadioButton.setFont(new Font(FontSizeEnum.font18.getValue()));
         yesRemarkRadioButton.getStyleClass().add("radioButton");
         yesRemarkRadioButton.setText(Main.languageMap.get("popups.yes"));
         yesRemarkRadioButton.setUserData(1);
         yesRemarkRadioButton.setToggleGroup(remarksGroup);

         noRemarkRadioButton.setPrefWidth(radioButtonWidth);
         noRemarkRadioButton.setPrefHeight(radioButtonHeight);
         noRemarkRadioButton.setFont(new Font(FontSizeEnum.font18.getValue()));
         noRemarkRadioButton.getStyleClass().add("radioButton");
         noRemarkRadioButton.setText(Main.languageMap.get("popups.no"));
         noRemarkRadioButton.setToggleGroup(remarksGroup);
         noRemarkRadioButton.setUserData(0);

         if(AppUtils.isNotBlank(posTable)){
             if("1".equals(posTable.getRemarks())) {
                 remarksGroup.selectToggle(yesRemarkRadioButton);
             }
             else{
                 remarksGroup.selectToggle(noRemarkRadioButton);
             }
         }


         //座位數量欄
         maxPersonFlowPane.setPrefWidth(contentFlowPaneWidth);
         maxPersonFlowPane.setPrefHeight(contentFlowPaneHeight);
         maxPersonLabel.setPrefWidth(labelWidth);
         maxPersonLabel.setPrefHeight(labelHeight);
         maxPersonLabel.setFontSize(FontSizeEnum.font22);
         maxPersonLabel.setText(Main.languageMap.get("table.maxPerson"));
         maxPersonLabel.getStyleClass().add("label");
         maxPersonTextField.setPrefWidth(textFieldWidth);
         maxPersonTextField.setPrefHeight(textFieldHeight);
         maxPersonTextField.setFont(Font.font(FontSizeEnum.font22.getValue()));
         if(AppUtils.isNotBlank(posTable)){
             maxPersonTextField.setText(posTable.getMaxperson());
         }

         //按鈕欄
         buttomFlowPane.setPrefWidth(contentFlowPaneWidth);
         buttomFlowPane.setPrefHeight(contentFlowPaneHeight*2);
         buttomFlowPane.getStyleClass().add("buttomFlowPane");
         buttomFlowPane.setHgap(contentFlowPaneWidth/10);
         confirmButton.setPrefWidth(buttonWidth);
         confirmButton.setPrefHeight(buttonHeight);
         confirmButton.setFontSize(FontSizeEnum.font22);
         confirmButton.setText(Main.languageMap.get("table.confirm"));
         confirmButton.getStyleClass().add("button");
         cancleButton.setPrefWidth(buttonWidth);
         cancleButton.setPrefHeight(buttonHeight);
         cancleButton.setFontSize(FontSizeEnum.font22);
         cancleButton.setText(Main.languageMap.get("table.cancle"));
         cancleButton.getStyleClass().add("button");
         Stage tableEditStage = new Stage();
         tableEditStage.setScene(new Scene(tableEditFlowPane));
         tableEditStage.initOwner(parentStage);
         tableEditStage.setIconified(false);
        /* tableEditStage.initModality(Modality.NONE);
         tableEditStage.initStyle(StageStyle.TRANSPARENT);*/
         tableEditStage.initModality(Modality.APPLICATION_MODAL);
         tableEditStage.initStyle(StageStyle.TRANSPARENT);
         cancleButton.setOnAction(event -> {
             List<String> buttonList = new ArrayList<String>();
             if(tableSettingsController.getCancleButton().isDisabled() && tableSettingsController.getEditTableButton().isDisabled()){
                 buttonList.add("cancleButton");
                 buttonList.add("editTableButton");
                 buttonList.add("previousPageButton");
                 buttonList.add("nextPageButton");
                 buttonList.add("confirmButton");
             }
             else{
                 buttonList.add("cancleButton");
                 buttonList.add("confirmButton");
             }
             tableSettingsController.setDistable(true,buttonList);
             tableEditStage.close();

         });
         DragUtil.addClickListener(confirmButton);
         confirmButton.setOnAction(event -> {
             try {
             if(AppUtils.isBlank(roomNum)){
                     Wrapper<PosTable> posTableDtoWrapper = new EntityWrapper<PosTable>();
                     posTableDtoWrapper.eq("ROOM_NUM",tableNoTextField.getText());
                     // PosTableService posTableService = (PosTableService) SpringContextUtil.getBean(PosTableService.class);
                     List<PosTable> otherPosTables = posTableService.selectList(posTableDtoWrapper);
                     if(AppUtils.isNotBlank(otherPosTables)) {
                         Map<String, String> resultMap = new LinkedHashMap<String, String>();
                         resultMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                         String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.existError"), resultMap, tableEditStage);
                         return;
                     }
                     if(remarksGroup.getSelectedToggle() == null || AppUtils.isBlank(roomType) || AppUtils.isBlank(maxPersonTextField.getText())
                             || isOnlineGroup.getSelectedToggle() == null || AppUtils.isBlank(tableNoTextField.getText()))
                     {
                         Map<String, String> resultMap = new LinkedHashMap<String, String>();
                         resultMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                         String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.lackParameter"), resultMap, tableEditStage);
                         return;
                     }

                     PosTable posTableDto = new PosTable();
                     posTableDto.setRemarks(remarksGroup.getSelectedToggle().getUserData().toString());
                     posTableDto.setMaxperson(maxPersonTextField.getText());
                     posTableDto.setRoomType(roomType);
                     posTableDto.setRoomNum(tableNoTextField.getText());
                     posTableDto.setIsOnline(isOnlineGroup.getSelectedToggle().getUserData().toString());
                     posTableDto.setFloor(tableSettingsController.getCurrentArea());
                     posTableDto.setPageNumber(tableSettingsController.getCurrentPage());
                     posTableDto.setYRatio(new BigDecimal(0.456));
                     posTableDto.setXRatio(new BigDecimal(0.654));
                     posTableDto.setOutlet(Main.posOutlet);
                     posTableService.addTable(posTableDto);
                     Wrapper<PosTable> posTableWrapper = new EntityWrapper<PosTable>();
                     posTableWrapper.eq("ROOM_NUM",posTableDto.getRoomNum());
                     posTableWrapper.eq("FLOOR",posTableDto.getFloor());
                     posTableDto = posTableService.selectOne(posTableWrapper);
                     tableEditStage.close();

                     double tableFlowPaneWidth = Main.primaryScreenBounds.getWidth()/17;
                     double tableFlowPaneHeight = Main.primaryScreenBounds.getHeight()/15;
                     //顯示台號flowPane
                     FlowPane tableFlowPane = new FlowPane();
                     tableFlowPane.setPrefWidth(tableFlowPaneWidth);
                     tableFlowPane.setPrefHeight(tableFlowPaneHeight);
                     tableFlowPane.getStylesheets().add(Main.class.getResource("/com/dw/view/TableSettings.css").toExternalForm());
                     tableFlowPane.getStyleClass().remove(0,tableFlowPane.getStyleClass().size());
                     tableFlowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(null));

                     //顯示台號的Label
                     DwLabel tableNoLabel = new DwLabel();
                     Double tableNoLabelWidth = tableFlowPaneWidth-2;
                     Double tableNoLabelHeight = (tableFlowPaneHeight-2)/5*3;
                     tableNoLabel.setPrefWidth(tableNoLabelWidth);
                     tableNoLabel.setPrefHeight(tableNoLabelHeight);
                     tableNoLabel.setText(posTableDto.getRoomNum());
                     tableFlowPane.setId(posTableDto.getRoomNum());
                     tableNoLabel.setFontSize(FontSizeEnum.font24);
                     tableNoLabel.getStyleClass().remove(0,tableNoLabel.getStyleClass().size());
                     tableNoLabel.getStyleClass().add(TableStateEnum.getStyleByValue(null));
                     tableFlowPane.getChildren().add(tableNoLabel);


                     //顯示人數的Label
                     DwLabel numberLabel = new DwLabel();
                     Double numberLabelWidth = tableFlowPaneWidth-2;
                     Double numberLabelHeight = (tableFlowPaneHeight-2)/5;
                     numberLabel.setPrefWidth(numberLabelWidth);
                     numberLabel.setPrefHeight(numberLabelHeight);
                     numberLabel.setText(0 + "/" + posTableDto.getMaxperson());
                     numberLabel.setFontSize(FontSizeEnum.font14);
                     numberLabel.getStyleClass().remove(0,numberLabel.getStyleClass().size());
                     numberLabel.getStyleClass().add(TableStateEnum.getStyleByValue(null));
                     tableFlowPane.getChildren().add(numberLabel);

                     //顯示入座時間的Label
                     DwLabel timeLabel = new DwLabel();
                     Double timeLabelWidth = tableFlowPaneWidth-2;
                     Double timeLabelHeight = (tableFlowPaneHeight-2)/5;
                     timeLabel.setPrefWidth(timeLabelWidth);
                     timeLabel.setPrefHeight(timeLabelHeight);
                     timeLabel.setFontSize(FontSizeEnum.font14);
                     timeLabel.getStyleClass().remove(0,timeLabel.getStyleClass().size());
                     timeLabel.getStyleClass().add(TableStateEnum.getStyleByValue(null));
                     tableFlowPane.getChildren().add(timeLabel);

                     //顯示桌臺窗口
                     Stage buttonStage = new Stage();
                     buttonStage.setScene(new Scene(tableFlowPane));
                     buttonStage.initOwner(FXRobotHelper.getStages().get(0));
                     buttonStage.initModality(Modality.NONE);
                     buttonStage.setX(DragUtil.clickX);
                     buttonStage.setY(DragUtil.clickY);
                     buttonStage.initStyle(StageStyle.TRANSPARENT);
                     buttonStage.initOwner(parentStage);
                     DragUtil.stage = buttonStage;
                     tableSettingsController.getTableFlowPaneList().add(tableFlowPane);
                     tableSettingsController.getTableFlowPaneStageList().add(buttonStage);
                     DragUtil.addTableListener(buttonStage, tableFlowPane,posTableDto, tableSettingsController);
                     buttonStage.show();
             }
             else{
                if(posTable != null){
                    posTable.setRoomType(roomType);
                    posTable.setRemarks(remarksGroup.getSelectedToggle().getUserData().toString());
                    posTable.setMaxperson(maxPersonTextField.getText());
                    posTable.setIsOnline(isOnlineGroup.getSelectedToggle().getUserData().toString());
                    posTableService.updateTable(posTable);
                    tableSettingsController.getTableFlowPaneList().forEach((FlowPane flowPane) ->{
                        if(posTable.getRoomNum().equals(flowPane.getId())){
                           DwLabel dwLabel = (DwLabel) flowPane.getChildren().get(1);
                           dwLabel.setText("0/"+ posTable.getMaxperson());
                        }
                    });
                    tableEditStage.close();
                    Map<String,String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(),ResultEnum.YES.getValue());
                    String resul = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"),Main.languageMap.get("table.editSuccess"),resultMap,FXRobotHelper.getStages().get(0));
                   // newTableSettingsController.refreshTables(newTableSettingsController.getCurrentPage(),newTableSettingsController.getCurrentArea(), newTableSettingsController.getNewTableSettingStage());
                }

             }
             } catch (Exception e) {
                 e.printStackTrace();
                 tableEditStage.close();
                 Map<String,String> resultMap = new LinkedHashMap<>();
                 resultMap.put(ResultEnum.YES.getName(),ResultEnum.YES.getValue());
                 String resul = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"),Main.languageMap.get("exception.innererror"),resultMap,FXRobotHelper.getStages().get(0));
                /* if(AppUtils.isNotBlank(tableSettingsController.disableIds)){
                     tableSettingsController.disableIds.clear();
                 }
                 tableSettingsController.disableIds.add("cancleButton");
                 tableSettingsController.disableIds.add("confirmButton");
                 tableSettingsController.setDistable(true, tableSettingsController.disableIds);*/
             }
             if(AppUtils.isNotBlank(tableSettingsController.disableIds)){
                 tableSettingsController.disableIds.clear();
             }
             tableSettingsController.disableIds.add("cancleButton");
             tableSettingsController.disableIds.add("confirmButton");
             tableSettingsController.setDistable(true, tableSettingsController.disableIds);
         });
         this.tableEditStage = tableEditStage;
         tableEditStage.show();
    }



    @FXML
    public void inputTableNo()  {
        String text = ShowViewUtil.showKeyword(parentStage,tableNoTextField.getText());
        if(ResultEnum.NO.getValue().equals(text)){
            return;
        }
        else{
            tableNoTextField.setText(text);
        }
    }


    @FXML
    public void inputPerson() {
        String text = ShowViewUtil.showNumbericKeyboard(tableEditStage,"請輸入人數",maxPersonTextField.getText(),false);
        if(ResultEnum.NO.getValue().equals(text)){
            return;
        }
        else{
            maxPersonTextField.setText(text);
        }
    }





}
