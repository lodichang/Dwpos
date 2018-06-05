package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.component.NettyComponent;
import com.dw.entity.PosTable;
import com.dw.service.PosTableService;
import com.dw.view.DataSetView;
import com.dw.view.MainView;
import com.sun.javafx.robot.impl.FXRobotHelper;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import com.dw.Main;
import com.dw.dto.BillDto;
import com.dw.dto.PosTableDto;
import com.dw.dto.TopButtonDto;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.mapper.BillMapper;
import com.dw.mapper.PosTableMapper;
import com.dw.mapper.TopButtonMapper;
import com.dw.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lodi on 2018/3/6.
 */
@Getter
@Setter
@Component
public class TableSettingsController extends FlowPane  {

    @FXML
    private BorderPane tableSettingsFlowPane;

    @FXML
    private FlowPane topFlowPane;

    @FXML
    private FlowPane areaFlowPane;

    @FXML
    private FlowPane pageFlowPane;

    @FXML
    private DwButton upAreaPageButton;

    @FXML
    private DwButton downAreaPageButton;

    @FXML
    private AnchorPane tablesAnchorPane;

    @FXML
    private FlowPane buttomFlowPane;

    @FXML
    private DwButton  addTableButton;

    @FXML
    private DwButton  deleteTableButton;

    @FXML
    private DwButton  moveTableButton;

    @FXML
    private DwButton  editTableButton;

    @FXML
    private DwButton confirmButton;

    @FXML
    private DwButton cancleButton;

    @FXML
    private DwButton  previousPageButton;

    @FXML
    public DwButton  nextPageButton;

    @FXML
    private DwButton  exitButton;

    @FXML
    private VBox centerVBox;

    @Autowired
    private MainView mainView;

    private Stage newTableSettingStage;

    private List<PosTableDto> posTableDtoList;

    //当前页
    private Integer currentPage = 1;

    private Integer currentAreaPage = 1;

    //当前的区域Id
    private String currentArea;

    private String[] areaArrage;


    public  List<FlowPane> tableFlowPaneList = new LinkedList<>();

    public  List<Stage> tableFlowPaneStageList = new LinkedList<>();


    private List<Label> checkLabels =  new LinkedList<>();

    private List<FlowPane> buttonFlowPanes = new LinkedList<>();

    public  List<String> disableIds = new LinkedList<>();

    private FlowPane currentButtonFlowPane;

    @Autowired
    private PosTableService posTableService;

    @Autowired
    private NettyComponent nettyComponent;

    //已经移动过的檯號
    public static Map<String,FlowPane> moveFlowPaneMap = new HashedMap();
    boolean checkLeaveSeat = false;

    private String[] colors = {"#3F51B5s","#FF4081","#E51C23","#009688","#259B24","#FF9800","#866CBD","#E84FC6","#EDB179","#75B0E6","#D5E339","#D6593E"};

    private String[] formats = {

            "3X3","3X4","3X5","3X6","3X7","3X8",

            "4X3","4X4","4X5","4X6","4X7","4X8",

            "5X3","5X4","5X5","5X6","5X7","5X8",

            "6X3","6X4","6X5","6X6","6X7","6X8",

            "7X3","7X4","7X5","7X6","7X7","7X8",

            "8X3","8X4","8X5","8X6","8X7","8X8",
    };



   /* @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void initialize() {
        try {
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void show() throws IOException {
        checkLeaveSeat = Boolean.parseBoolean(AppUtils.isNotBlank(Main.posSetting.get("checkLeaveSeat")) ? Main.posSetting.get("checkLeaveSeat") : "false");
        DragUtil.addMoveListener(tableSettingsFlowPane);
        //修改頁面按鈕的distable屬性
        if(AppUtils.isNotBlank(this.getDisableIds())){
            this.getDisableIds().clear();
        }
        this.getDisableIds().add("cancleButton");
        this.setDistable(true,this.getDisableIds());

        double tableSettingsWidth = Main.primaryScreenBounds.getWidth();
        double tableSettingsHeight = Main.primaryScreenBounds.getHeight();
        tableSettingsFlowPane.setPrefWidth(tableSettingsWidth);
        tableSettingsFlowPane.setPrefHeight(tableSettingsHeight);

        topFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*2);
        topFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth());

        areaFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth()/5*4);
        areaFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*2);
        areaFlowPane.setPadding(new Insets(5,0,0,5));
        areaFlowPane.setHgap(5);
        areaFlowPane.getStyleClass().add("areaFlowPane");

        String areas = Main.posSetting.get("area");
        double areaButtonWidth = ( Main.primaryScreenBounds.getWidth() / 5 * 4 - (Main.showAreasPageSize+1)*5)/Main.showAreasPageSize;
        double areaButtonHeight = (Main.primaryScreenBounds.getHeight() / 18 * 2 - 10);
        if(AppUtils.isNotBlank(areas)) {
            String[] areaArrage = areas.split(",");
            this.areaArrage = areaArrage;
            currentArea = areaArrage[0];
            int end;
            if(areaArrage.length>=Main.showAreasPageSize){
                end = Main.showAreasPageSize;
            }
            else{
                end = areaArrage.length;
            }
            for (int i = 0;i<end;i++) {
                DwButton dwButton = new DwButton();
                dwButton.setText(areaArrage[i]);
                dwButton.setFontSize(FontSizeEnum.font24);
                dwButton.setUserData("optionArea-" + areaArrage[i]);
                dwButton.setBindData(areaArrage[i]);
                dwButton.setPrefHeight(areaButtonHeight);
                dwButton.setPrefWidth(areaButtonWidth);
                dwButton.getStyleClass().add("areabutton");
                ButtonUtil.shadowButton(dwButton);
                areaFlowPane.getChildren().add(dwButton);
                dwButton.setOnAction(event -> {
                    areaFlowPane.getChildren().forEach((Node node) -> {
                        DwButton areaDwButton = (DwButton) node;
                        if(AppUtils.isNotBlank(disableIds)){
                            disableIds.clear();
                        }
                        setDistable(true,disableIds);
                        if (areaDwButton.getBindData().equals(dwButton.getBindData())) {
                            areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                            areaDwButton.getStyleClass().add("selectedButton");
                        } else {
                            areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                            areaDwButton.getStyleClass().add("areabutton");
                        }
                    });
                    refreshTables(1,dwButton.getBindData().toString(),FXRobotHelper.getStages().get(0),false);
                });
            }
            areaFlowPane.getChildren().get(0).getStyleClass().remove(0, areaFlowPane.getChildren().get(0).getStyleClass().size());
            areaFlowPane.getChildren().get(0).getStyleClass().add("selectedButton");
        }

        pageFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth()/5-1);
        pageFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*2);
        pageFlowPane.setPadding(new Insets(5,0,0,0));
        pageFlowPane.setHgap(5);
        pageFlowPane.getStyleClass().add("pageFlowPane");

        upAreaPageButton.setGraphic(new ImageView(AppUtils.loadImage("leftArrow.png")));
        upAreaPageButton.setPrefWidth((Main.primaryScreenBounds.getWidth()/5-1-10)/2);
        upAreaPageButton.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*2-10);
        upAreaPageButton.getStyleClass().add("pageButton");
        upAreaPageButton.setOnAction(event -> {
            if (currentAreaPage > 1 && AppUtils.isNotBlank(areaArrage)) {
                currentAreaPage = currentAreaPage - 1;
                int startIndex = currentAreaPage * Main.showAreasPageSize - Main.showAreasPageSize;
                int endIndex = currentAreaPage * Main.showAreasPageSize;
                areaFlowPane.getChildren().remove(0,areaFlowPane.getChildren().size());
                List<String> showAreaList = new LinkedList<String>();
                for(int i = startIndex;i<endIndex;i++){
                    showAreaList.add(areaArrage[i]);
                }
                reloadAreas(showAreaList,areaFlowPane);
            }
        });


        downAreaPageButton.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
        downAreaPageButton.setPrefWidth((Main.primaryScreenBounds.getWidth()/5-1-10)/2);
        downAreaPageButton.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*2-10);
        downAreaPageButton.getStyleClass().add("pageButton");
        downAreaPageButton.setOnAction(event -> {
            if ( AppUtils.isNotBlank(areaArrage) && (areaArrage.length - currentAreaPage*Main.showAreasPageSize) > 0) {
                currentAreaPage = currentAreaPage + 1;
                int endIndex;
                int startIndex = (currentAreaPage-1) * Main.showAreasPageSize;
                if((areaArrage.length - (currentAreaPage -1)*Main.showAreasPageSize) >=  Main.showAreasPageSize){
                    endIndex = currentAreaPage * Main.showAreasPageSize - 1;
                }
                else{
                    endIndex = areaArrage.length;
                }
                areaFlowPane.getChildren().remove(0,areaFlowPane.getChildren().size());
                List<String> showAreaList = new LinkedList<String>();
                for(int i = startIndex;i<endIndex;i++){
                    showAreaList.add(areaArrage[i]);
                }
                reloadAreas(showAreaList,areaFlowPane);
            }
        });

        centerVBox.setPrefSize(Main.primaryScreenBounds.getWidth(),Main.primaryScreenBounds.getHeight()/18*16);
        //桌臺顯示區域
        tablesAnchorPane.setPrefHeight(Main.primaryScreenBounds.getHeight()/18*14);
        tablesAnchorPane.setPrefWidth(Main.primaryScreenBounds.getWidth());
        tablesAnchorPane.getStyleClass().add("tablesFlowPane");

        //按鈕部分
        double buttomFlowPaneWidth = Main.primaryScreenBounds.getWidth();
        double buttomFlowPaneHeight = Main.primaryScreenBounds.getHeight()/18*2;
        buttomFlowPane.setPrefWidth(buttomFlowPaneWidth);
        buttomFlowPane.setPrefHeight(buttomFlowPaneHeight);
        buttomFlowPane.getStyleClass().add("buttomFlowPane");

        buttomFlowPane.setPadding(new Insets(5,0,0,5));
        buttomFlowPane.setHgap(5);
        double buttonWidth = (buttomFlowPaneWidth-55)/9;
        double buttonHeight =(buttomFlowPaneHeight-15);

        this.getAddTableButton().setPrefHeight(buttonHeight);
        this.getAddTableButton().setPrefWidth(buttonWidth);
        this.getAddTableButton().setFontSize(FontSizeEnum.font24);
        this.getAddTableButton().getStyleClass().add("button");
        this.getAddTableButton().setText(Main.languageMap.get("table.add"));

        this.getDeleteTableButton().setPrefHeight(buttonHeight);
        this.getDeleteTableButton().setPrefWidth(buttonWidth);
        this.getDeleteTableButton().setFontSize(FontSizeEnum.font24);
        this.getDeleteTableButton().getStyleClass().add("button");
        this.getDeleteTableButton().setText(Main.languageMap.get("table.delete"));

        this.getMoveTableButton().setPrefHeight(buttonHeight);
        this.getMoveTableButton().setPrefWidth(buttonWidth);
        this.getMoveTableButton().setFontSize(FontSizeEnum.font24);
        this.getMoveTableButton().getStyleClass().add("button");
        this.getMoveTableButton().setText(Main.languageMap.get("table.move"));

        this.getEditTableButton().setPrefHeight(buttonHeight);
        this.getEditTableButton().setPrefWidth(buttonWidth);
        this.getEditTableButton().setFontSize(FontSizeEnum.font24);
        this.getEditTableButton().getStyleClass().add("button");
        this.getEditTableButton().setText(Main.languageMap.get("table.edit"));

        this.getConfirmButton().setPrefHeight(buttonHeight);
        this.getConfirmButton().setPrefWidth(buttonWidth);
        this.getConfirmButton().setFontSize(FontSizeEnum.font24);
        this.getConfirmButton().getStyleClass().add("button");
        this.getConfirmButton().setText(Main.languageMap.get("table.confirm"));



        this.getCancleButton().setPrefHeight(buttonHeight);
        this.getCancleButton().setPrefWidth(buttonWidth);
        this.getCancleButton().setFontSize(FontSizeEnum.font24);
        this.getCancleButton().getStyleClass().add("button");
        this.getCancleButton().setText(Main.languageMap.get("table.cancle"));

        this.getPreviousPageButton().setPrefHeight(buttonHeight);
        this.getPreviousPageButton().setPrefWidth(buttonWidth);
        this.getPreviousPageButton().setFontSize(FontSizeEnum.font24);
        this.getPreviousPageButton().getStyleClass().add("button");
        this.getPreviousPageButton().setText(Main.languageMap.get("table.previousPage"));
        this.getPreviousPageButton().setOnAction(event -> {
            if(currentPage > 1 && AppUtils.isNotBlank(currentArea) ){
                refreshTables(currentPage-1,currentArea,FXRobotHelper.getStages().get(0),false);
            }
        });

        this.getNextPageButton().setPrefWidth(buttonWidth);
        this.getNextPageButton().setPrefHeight(buttonHeight);
        this.getNextPageButton().setFontSize(FontSizeEnum.font24);
        this.getNextPageButton().getStyleClass().add("button");
        this.getNextPageButton().setText(Main.languageMap.get("table.nextPage"));
        this.getNextPageButton().setOnAction(event -> {
            refreshTables(currentPage+1,currentArea,FXRobotHelper.getStages().get(0),false);
        });

        this.getExitButton().setPrefWidth(buttonWidth);
        this.getExitButton().setPrefHeight(buttonHeight);
        this.getExitButton().setFontSize(FontSizeEnum.font24);
        this.getExitButton().getStyleClass().add("button");
        this.getExitButton().setText(Main.languageMap.get("table.exit"));
        ObservableList<Stage> stages = FXRobotHelper.getStages();
        refreshTables(1,currentArea,stages.get(0),false);

    }




    /**
     * 添加檯
     */
    @FXML
    public void addTable () throws IOException {
        tableFlowPaneList.forEach((FlowPane flowPane)-> {
            flowPane.setOnMousePressed(null);
            flowPane.setOnMouseDragged(null);
            flowPane.setOnMouseMoved(null);
            flowPane.setOnMouseExited(null);
            flowPane.setOnMouseClicked(null);
        });
        if(AppUtils.isNotBlank(disableIds)){
            disableIds.clear();
        }
        disableIds.add("addTableButton");
        disableIds.add("cancleButton");
        disableIds.add("confirmButton");
        setDistable(true,disableIds);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/com/dw/view/TableEditView.fxml"));
        loader.load();
        TableEditController tableEditController = loader.getController();
        tableEditController.setParentStage(FXRobotHelper.getStages().get(0));
        tableEditController.setTableSettingsController(this);
        tableEditController.show(null);

    }

    /**
     * 移動檯
     */
    @FXML
    public void moveTable () throws IOException {
        tablesAnchorPane.getChildren().remove(0,tablesAnchorPane.getChildren().size());
        refreshTables(currentPage, currentArea, newTableSettingStage, true);
        for (int i = 0; i < tableFlowPaneList.size(); i++) {
            tableFlowPaneList.get(i).setOnMousePressed(null);
            tableFlowPaneList.get(i).setOnMouseDragged(null);
            tableFlowPaneList.get(i).setOnMouseMoved(null);
            tableFlowPaneList.get(i).setOnMouseExited(null);
            tableFlowPaneList.get(i).setOnMouseClicked(null);
            DragUtil.addDragListener(tableFlowPaneStageList.get(i), tableFlowPaneList.get(i), newTableSettingStage);
        }
    }



    /**
     * 移動檯
     */
    @FXML
    public void editTable () throws IOException {
        if(AppUtils.isNotBlank(disableIds)){
            disableIds.clear();
        }
        disableIds.add("editTableButton");
        disableIds.add("cancleButton");
        disableIds.add("confirmButton");
        setDistable(true,disableIds);
        tableFlowPaneList.forEach((FlowPane flowPane)->{
            flowPane.setOnMousePressed(null);
            flowPane.setOnMouseDragged(null);
            flowPane.setOnMouseMoved(null);
            flowPane.setOnMouseExited(null);
            flowPane.setOnMouseClicked(event -> {
                if(flowPane.getUserData() != null){
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                    String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.haveDrive"), resultMap, newTableSettingStage);
                    return;
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/com/dw/view/TableEditView.fxml"));
                try {
                    loader.load();
                    TableEditController tableEditController = loader.getController();
                    tableEditController.setParentStage(FXRobotHelper.getStages().get(0));
                    tableEditController.setTableSettingsController(this);
                    tableEditController.show(flowPane.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        });








    }







    /**
     * 取消
     */
    @FXML
    public void cancle() throws IOException {
        moveFlowPaneMap.clear();
        refreshTables(currentPage,currentArea,newTableSettingStage,false);

    }


    /**
     * 刪除
     */
    @FXML
    public void deleteTable() throws Exception {
        try {
            if (AppUtils.isNotBlank(disableIds)) {
                disableIds.clear();
            }
            disableIds.add("deleteTableButton");
            disableIds.add("confirmButton");
            disableIds.add("cancleButton");
            setDistable(true, disableIds);
            tableFlowPaneList.forEach((FlowPane flowPane) -> {
                flowPane.setOnMousePressed(null);
                flowPane.setOnMouseDragged(null);
                flowPane.setOnMouseMoved(null);
                flowPane.setOnMouseExited(null);
                flowPane.setOnMouseClicked(null);
            });
            String result = ShowViewUtil.showKeyword(FXRobotHelper.getStages().get(0), null);
            if (!ResultEnum.NO.getValue().equals(result)) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                String resul = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("table.sureDelete"), resultMap, FXRobotHelper.getStages().get(0));
                if (ResultEnum.YES.getValue().equals(resul)) {
                    final boolean[] isExist = {false};
                    for(FlowPane flowPane : tableFlowPaneList){
                       /* tableFlowPaneList.forEach((FlowPane flowPane) -> {*/
                        if (flowPane.getId().equals(result)) {
                            isExist[0] = true;
                            if (flowPane.getUserData() == null) {
                                Wrapper<PosTable> posTableWrapper = new EntityWrapper<>();
                                posTableWrapper.eq("ROOM_NUM", result);
                                PosTable p = posTableService.selectOne(posTableWrapper);
                                try {
                                    posTableService.deleteTable(p);
                                    //刪除成功後發送netty刷新台號
                                    nettyComponent.sendMessage(NettyMessageTypeEnum.DELETETABLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Map<String, String> resuMap = new LinkedHashMap<String, String>();
                                    resuMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.notExist"), resuMap, FXRobotHelper.getStages().get(0));
                                    if (AppUtils.isNotBlank(disableIds)) {
                                        disableIds.clear();
                                    }
                                    disableIds.add("cancleButton");
                                    disableIds.add("confirmButton");
                                    setDistable(true, disableIds);
                                    return;
                                }
                                Map<String, String> resulMap = new LinkedHashMap<>();
                                resulMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("table.deleteSuccess"), resulMap, FXRobotHelper.getStages().get(0));
                                refreshTables(currentPage, currentArea, newTableSettingStage, false);
                                break;

                                  /*  int index =  tableFlowPaneList.indexOf(flowPane);
                                    if(index >=0){
                                        tableFlowPaneList.remove(flowPane);
                                        Stage stage =  tableFlowPaneStageList.get(index);
                                        if(stage != null){
                                           stage.close();
                                           tableFlowPaneStageList.remove(index);
                                        }
                                    }*/
                            }
                            else {
                                Map<String, String> resuMap = new LinkedHashMap<String, String>();
                                resuMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.haveDrive"), resuMap, FXRobotHelper.getStages().get(0));
                                if (AppUtils.isNotBlank(disableIds)) {
                                    disableIds.clear();
                                }
                                disableIds.add("cancleButton");
                                disableIds.add("confirmButton");
                                setDistable(true, disableIds);
                            }
                        }
                    }
                    if (!isExist[0]) {
                        Map<String, String> resuMap = new LinkedHashMap<String, String>();
                        resuMap.put(Main.languageMap.get("table.know"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.error"), Main.languageMap.get("table.notExist"), resuMap, FXRobotHelper.getStages().get(0));
                        if (AppUtils.isNotBlank(disableIds)) {
                            disableIds.clear();
                        }
                        disableIds.add("cancleButton");
                        disableIds.add("confirmButton");
                        setDistable(true, disableIds);
                    }
                }
                else{
                    if (AppUtils.isNotBlank(disableIds)) {
                        disableIds.clear();
                    }
                    disableIds.add("cancleButton");
                    disableIds.add("confirmButton");
                    setDistable(true, disableIds);
                }
            }
            else{
                if (AppUtils.isNotBlank(disableIds)) {
                    disableIds.clear();
                }
                disableIds.add("cancleButton");
                disableIds.add("confirmButton");
                setDistable(true, disableIds);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void exit() throws IOException {
        if(AppUtils.isNotBlank(tableFlowPaneList) && AppUtils.isNotBlank(tableFlowPaneStageList)){
            for(int i = 0;i< tableFlowPaneList.size();i++){
                tableFlowPaneStageList.get(i).close();
            }
            tableFlowPaneList.clear();
            tableFlowPaneStageList.clear();
        }
        //Main.showInitialView(DataSetView.class);
        MainController mainController = (MainController) mainView.getPresenter();
        mainController.iniData();
        Main.showInitialView(mainView.getClass());
    }



    /**
     * 批量修改按鈕distable屬性
     * @param param
     * @param disableIds
     */
    public void setDistable(Boolean param,List<String> disableIds){
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                    for (Node node : buttomFlowPane.getChildren()) {
                        if (disableIds.contains(node.getId())) {
                            node.setDisable(param);
                        } else {
                            if (param == true) {
                                node.setDisable(false);
                            } else {
                                node.setDisable(true);
                            }
                        }
                    }
                return null;
            }
        };
        new Thread(task).start();
    }


    /**
     * 刷新台号
     */

    public void refreshTables(Integer page,String area,Stage parentStage,boolean isStage){
            if (AppUtils.isNotBlank(disableIds)) {
                disableIds.clear();
            }
            if (!isStage) {
                disableIds.add("cancleButton");
                disableIds.add("confirmButton");
            } else {
                this.getDisableIds().add("addTableButton");
                this.getDisableIds().add("deleteTableButton");
                this.getDisableIds().add("moveTableButton");
                this.getDisableIds().add("editTableButton");
                this.getDisableIds().add("previousPageButton");
                this.getDisableIds().add("nextPageButton");
                this.getDisableIds().add("exitButton");
            }
            setDistable(true, this.getDisableIds());

            posTableDtoList = posTableService.getTablesByFloor(page, area,null,checkLeaveSeat);
            double tableFlowPaneWidth = Main.primaryScreenBounds.getWidth() / 14;
            double tableFlowPaneHeight = Main.primaryScreenBounds.getHeight() / 15;
            if (AppUtils.isNotBlank(posTableDtoList)) {
                currentArea = area;
                currentPage = page;
                tableFlowPaneStageList.forEach((Stage stage) -> {
                    stage.close();
                });
                tablesAnchorPane.getChildren().remove(0, tablesAnchorPane.getChildren().size());
                tableFlowPaneStageList.clear();
                tableFlowPaneList.clear();
            } else if (AppUtils.isBlank(posTableDtoList) && !area.equals(currentArea)) {
                currentArea = area;
                currentPage = page;
                tableFlowPaneStageList.forEach((Stage stage) -> {
                    stage.close();
                });
                tablesAnchorPane.getChildren().remove(0, tablesAnchorPane.getChildren().size());
                tableFlowPaneStageList.clear();
                tableFlowPaneList.clear();
                return;
            } else if (AppUtils.isBlank(posTableDtoList) && area.equals(currentArea) && AppUtils.isNotBlank(tableFlowPaneList)) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("table.addPage"), resultMap, FXRobotHelper.getStages().get(0));
                if (ResultEnum.YES.getValue().equals(result)) {
                    currentPage = currentPage + 1;
                    tableFlowPaneStageList.forEach((Stage stage) -> {
                        stage.close();
                    });
                    tablesAnchorPane.getChildren().remove(0, tablesAnchorPane.getChildren().size());
                    tableFlowPaneStageList.clear();
                    tableFlowPaneList.clear();
                    //修改頁面按鈕的distable屬性
               /* if(AppUtils.isNotBlank(this.getDisableIds())){
                    this.getDisableIds().clear();
                }
               *//* this.getDisableIds().add("nextPageButton");*//*
                this.getDisableIds().add("cancleButton");
                this.setDistable(true,this.getDisableIds());*/
                    return;
                } else {
                    return;
                }
            } else {
                return;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            BigDecimal maxX = new BigDecimal(Main.primaryScreenBounds.getMaxX());
            BigDecimal maxY = new BigDecimal(Main.primaryScreenBounds.getMaxY());
            for (int i = 0; i < posTableDtoList.size(); i++) {
                //顯示台號flowPane
                FlowPane tableFlowPane = new FlowPane();
                tableFlowPane.setUserData(posTableDtoList.get(i).getTableState());
                tableFlowPane.setId(posTableDtoList.get(i).getRoomNum());
                tableFlowPane.setPrefWidth(tableFlowPaneWidth);
                tableFlowPane.setPrefHeight(tableFlowPaneHeight);
                tableFlowPane.getStylesheets().add(Main.class.getResource("/com/dw/view/TableSettings.css").toExternalForm());
                tableFlowPane.getStyleClass().remove(0, tableFlowPane.getStyleClass().size());
                tableFlowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(posTableDtoList.get(i).getTableState()));

                //顯示台號的Label
                DwLabel tableNoLabel = new DwLabel();
                Double tableNoLabelWidth = tableFlowPaneWidth - 2;
                Double tableNoLabelHeight = (tableFlowPaneHeight - 2) / 5 * 3;
                tableNoLabel.setPrefWidth(tableNoLabelWidth);
                tableNoLabel.setPrefHeight(tableNoLabelHeight);
                tableNoLabel.setText(posTableDtoList.get(i).getRoomNum());
                tableFlowPane.setId(posTableDtoList.get(i).getRoomNum());
                tableNoLabel.setFontSize(FontSizeEnum.font24);
                tableNoLabel.getStyleClass().remove(0, tableNoLabel.getStyleClass().size());
                tableNoLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDtoList.get(i).getTableState()));
                tableFlowPane.getChildren().add(tableNoLabel);


                //顯示人數的Label
                DwLabel numberLabel = new DwLabel();
                Double numberLabelWidth = tableFlowPaneWidth - 2;
                Double numberLabelHeight = (tableFlowPaneHeight - 2) / 5;
                numberLabel.setPrefWidth(numberLabelWidth);
                numberLabel.setPrefHeight(numberLabelHeight);
                if (posTableDtoList.get(i).getCurrentPerson() == null) {
                    numberLabel.setText(0 + "/" + posTableDtoList.get(i).getMaxperson());
                } else {
                    numberLabel.setText(posTableDtoList.get(i).getCurrentPerson() + "/" + posTableDtoList.get(i).getMaxperson());
                }
                numberLabel.setFontSize(FontSizeEnum.font14);
                numberLabel.getStyleClass().remove(0, numberLabel.getStyleClass().size());
                numberLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDtoList.get(i).getTableState()));
                tableFlowPane.getChildren().add(numberLabel);

                //顯示入座時間的Label
                DwLabel timeLabel = new DwLabel();
                Double timeLabelWidth = tableFlowPaneWidth - 2;
                Double timeLabelHeight = (tableFlowPaneHeight - 2) / 5;
                timeLabel.setPrefWidth(timeLabelWidth);
                timeLabel.setPrefHeight(timeLabelHeight);
                if (posTableDtoList.get(i).getInTime() != null) {
                    timeLabel.setText(simpleDateFormat.format(posTableDtoList.get(i).getInTime()));
                }
                timeLabel.setFontSize(FontSizeEnum.font14);
                timeLabel.getStyleClass().remove(0, timeLabel.getStyleClass().size());
                timeLabel.getStyleClass().add(TableStateEnum.getStyleByValue(posTableDtoList.get(i).getTableState()));
                tableFlowPane.getChildren().add(timeLabel);
                if (isStage) {
                    //顯示桌臺窗口
                    int finalI = i;
                    Stage buttonStage = new Stage();
                    buttonStage.setScene(new Scene(tableFlowPane));
                    buttonStage.initOwner(FXRobotHelper.getStages().get(0));
                    buttonStage.setX(posTableDtoList.get(finalI).getXRatio().multiply(maxX).doubleValue());
                    buttonStage.setY(posTableDtoList.get(finalI).getYRatio().multiply(maxY).doubleValue());
                    buttonStage.initModality(Modality.NONE);
                    buttonStage.initStyle(StageStyle.TRANSPARENT);
                    buttonStage.initOwner(parentStage);
                    tableFlowPaneStageList.add(buttonStage);
                    tableFlowPaneList.add(tableFlowPane);
                    buttonStage.show();
                } else {
                    tableFlowPane.setTranslateX(posTableDtoList.get(i).getXRatio().multiply(maxX).doubleValue());
                    tableFlowPane.setTranslateY(posTableDtoList.get(i).getYRatio().multiply(maxY).doubleValue() - Main.primaryScreenBounds.getHeight() / 18 * 2);
                    tableFlowPaneList.add(tableFlowPane);
                    tablesAnchorPane.getChildren().add(tableFlowPane);
                }
            }
    }

    public void reloadAreas(List<String> areaArrage,FlowPane parentFlowPane){
        double areaButtonWidth = ( Main.primaryScreenBounds.getWidth() / 5 * 4 - (Main.showAreasPageSize+1)*5)/Main.showAreasPageSize;
        double areaButtonHeight = (Main.primaryScreenBounds.getHeight() / 18 * 2 - 10);
        areaArrage.forEach((String area)->{
            DwButton dwButton = new DwButton();
            dwButton.setText(area);
            dwButton.setFontSize(FontSizeEnum.font24);
            dwButton.setUserData("optionArea-" + area);
            dwButton.setBindData(area);
            dwButton.setPrefHeight(areaButtonHeight);
            dwButton.setPrefWidth(areaButtonWidth);
            dwButton.getStyleClass().add("areabutton");
            ButtonUtil.shadowButton(dwButton);
            parentFlowPane.getChildren().add(dwButton);
            dwButton.setOnAction(event -> {
                if(AppUtils.isNotBlank(disableIds)){
                    disableIds.clear();
                }
                setDistable(true,disableIds);
                parentFlowPane.getChildren().forEach((Node node) -> {
                    DwButton areaDwButton = (DwButton) node;
                    if (areaDwButton.getBindData().equals(dwButton.getBindData())) {
                        areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                        areaDwButton.getStyleClass().add("selectedButton");
                    } else {
                        areaDwButton.getStyleClass().remove(0, areaDwButton.getStyleClass().size());
                        areaDwButton.getStyleClass().add("areabutton");
                    }
                });
                refreshTables(1,dwButton.getBindData().toString(),FXRobotHelper.getStages().get(0),false);
            });
            if(dwButton.getBindData().equals(currentArea)){
                dwButton.getStyleClass().remove(0, dwButton.getStyleClass().size());
                dwButton.getStyleClass().add("selectedButton");
            }
        });
    }

    @FXML
    public void confirm(){
   if(moveTableButton.isDisable()){
            try{
                for(int i = 0;i<tableFlowPaneList.size();i++){
                    tableFlowPaneList.get(i).setOnMousePressed(null);
                    tableFlowPaneList.get(i).setOnMouseDragged(null);
                    tableFlowPaneList.get(i).setOnMouseMoved(null);
                    tableFlowPaneList.get(i).setOnMouseExited(null);
                    tableFlowPaneList.get(i).setOnMouseClicked(null);
                    if(AppUtils.isNotBlank(moveFlowPaneMap.get(tableFlowPaneList.get(i).getId()))){
                        BigDecimal x = new BigDecimal(tableFlowPaneStageList.get(i).getX());
                        BigDecimal y = new BigDecimal(tableFlowPaneStageList.get(i).getY());
                        BigDecimal yRatio =  y.divide(new BigDecimal(Main.primaryScreenBounds.getMaxY()),8,BigDecimal.ROUND_DOWN);
                        BigDecimal xRatio =  x.divide(new BigDecimal(Main.primaryScreenBounds.getMaxX()),8,BigDecimal.ROUND_DOWN);
                        posTableService.updatePosTableXY(xRatio,yRatio,tableFlowPaneList.get(i).getId());
                    }
                }
                moveFlowPaneMap.clear();
                //发送netty刷信桌臺
                nettyComponent.sendMessage(NettyMessageTypeEnum.MOVETABLE);
                refreshTables(currentPage,currentArea,newTableSettingStage,false);
            } catch (Exception e) {
                e.printStackTrace();
                Map<String,String> resultMap = new LinkedHashMap<>();
                resultMap.put(Main.languageMap.get("table.confirm"),"confirm");
                ShowViewUtil.showWarningView(Main.languageMap.get("tip_title"),Main.languageMap.get("table.moveError"),resultMap,FXRobotHelper.getStages().get(0));
                return;
            }
        }
       /* if(AppUtils.isNotBlank(disableIds)){
            disableIds.clear();
        }
        disableIds.add("cancleButton");
        disableIds.add("confirmButton");
        setDistable(true,disableIds);*/
    }



}
