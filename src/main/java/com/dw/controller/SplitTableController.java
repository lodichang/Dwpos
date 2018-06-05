package com.dw.controller;

import com.dw.Main;
import com.dw.component.UpdateTablePersonComponent;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.print.PrintStyleUtils;
import com.dw.service.*;
import com.dw.util.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


@Getter
@Setter
@FXMLController
public class SplitTableController implements Initializable {
    @FXML
    private FlowPane splitTablePane;
    @FXML
    private FlowPane mainFlowPane;
    @FXML
    private FlowPane leftFlowPane;
    @FXML
    private FlowPane rightFlowPane;
    private Stage splitTableStage;
    @Autowired
    private OpenTableController openTableController;
    @Autowired
    private MainController mainController;
    @Autowired
    private PosTableService posTableService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private UpdateTablePersonComponent updateTablePersonComponent;

    private DwButton clearTableBtn;
    private DwButton closeBtn;
    //父臺號，通過這個臺號來查詢搭檯和普通臺，父臺號和普通臺臺號相同
    private String parentTableNum;
    //判斷能不能清空臺,默认能清空
    private boolean canClearTable = true;
    //是否检查桌台是否离座
    private boolean checkLeaveSeat = false;
    //是否可以更新人數
    private boolean isUpdatingPerson = false;


    /**
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* Platform.runLater(() -> {
                splitTableStage = (Stage) splitTablePane.getScene().getWindow();
            }
        );*/

        splitTableComponent();
    }

    /**
     *
     */
    public void splitTableComponent() {
        double tableMainWidth = Main.primaryScreenBounds.getWidth() * 0.8;
        double tableMainHeight = Main.primaryScreenBounds.getHeight() * 0.4;
        splitTablePane.setPrefSize(tableMainWidth,tableMainHeight);
        splitTablePane.setStyle("-fx-background-color: #34495E;");
        //.getStyleClass().add("splitTablePane");
        leftFlowPane.setAlignment(Pos.TOP_LEFT);
        leftFlowPane.setPrefSize(tableMainWidth*0.7,tableMainHeight);
        leftFlowPane.setPadding(new Insets(10));
        initLeftFlowPane();

        rightFlowPane.setPrefSize(tableMainWidth*0.28,tableMainHeight);
        rightFlowPane.setAlignment(Pos.CENTER);
        //rightFlowPane.setStyle("-fx-border-color: #5CAA88;-fx-border-width: 2;");
        initRightFlowPane();

    }
    private void initLeftFlowPane(){

    }

    private void initRightFlowPane(){
        clearTableBtn = new DwButton(FontSizeEnum.font28);
        clearTableBtn.setPrefSize(rightFlowPane.getPrefWidth(),rightFlowPane.getPrefHeight()/3);
        clearTableBtn.setText(Main.languageMap.get("tables.clearTable"));
        clearTableBtn.setOnMouseClicked(event -> {
            List<PosTableDto> posTableDtoList = posTableService.getDisplayTableByTableNum(parentTableNum,checkLeaveSeat);
            for(int i=0;i<posTableDtoList.size();i++){
                if(AppUtils.isNotBlank(posTableDtoList.get(i).getTableState())){
                    canClearTable = false;
                    break;
                }
            }
            if(canClearTable){
                this.mainController.updateTableRemark1(parentTableNum,"1",0);
                this.mainController.getTablesAnchorPane().getChildren().remove(splitTablePane);
                mainController.refreshTables(posTableDtoList.get(0).getPageNumber(),posTableDtoList.get(0).getFloor());
            }else{
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("tables.cannotClearTable"), resultMap, splitTableStage);
            }
            resetUpdatePerson();
        });

        closeBtn = new DwButton(FontSizeEnum.font28);
        closeBtn.setText(Main.languageMap.get("global.close"));
        closeBtn.setPrefSize(rightFlowPane.getPrefWidth(),rightFlowPane.getPrefHeight()/3);
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setOnMouseClicked(event -> {
            //splitTableStage.close();
            this.mainController.getTablesAnchorPane().getChildren().remove(splitTablePane);
            this.mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
            resetUpdatePerson();

        });
        FlowPane.setMargin(clearTableBtn,new Insets(20));
        FlowPane.setMargin(closeBtn,new Insets(20));

        rightFlowPane.getChildren().addAll(clearTableBtn,closeBtn);

    }


    /**
     * 初始化方法
     */
    public void initData(PosTableDto posTableDto,boolean checkLeaveSeat) {
        canClearTable = true;
        this.checkLeaveSeat = checkLeaveSeat;
        this.parentTableNum = posTableDto.getRoomNum();
        loadTables(parentTableNum);

    }

    public void loadTables(String tableNum){
        Platform.runLater(()->{
            List<PosTableDto> posTableDtoList = posTableService.getDisplayTableByTableNum(tableNum,checkLeaveSeat);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            leftFlowPane.getChildren().clear();
            double btnWidth = Main.primaryScreenBounds.getWidth() / 14;
            double btnHeight = Main.primaryScreenBounds.getHeight() / 15;
            posTableDtoList.forEach(posTableDto -> {
                FlowPane tableFlowPane = new FlowPane();
                tableFlowPane.setPrefWidth(btnWidth);
                tableFlowPane.setPrefHeight(btnHeight);
                tableFlowPane.getStyleClass().remove(0, tableFlowPane.getStyleClass().size());
                tableFlowPane.getStyleClass().add(TableStateEnum.getParentStyleByValue(posTableDto.getTableState()));
                //改变父台号的posTableDto
                if(!posTableDto.getRoomNum().contains("-")){
                    mainController.getGroupbyMap().get(posTableDto.getRoomNum()).set(0,posTableDto);
                }
                mainController.getTablesMap().put(posTableDto.getRoomNum(),tableFlowPane);
                this.mainController.initSplitTableFlowPane(tableFlowPane, posTableDto);

                tableFlowPane.setOnMouseClicked(event -> {
                    Platform.runLater(() -> {
                        this.mainController.clickAction(tableFlowPane, posTableDto,SplitTableClickEnum.SPLITSINGLETABLE.getValue());
                        resetUpdatePerson();
                    });
                });

                FlowPane.setMargin(tableFlowPane,new Insets(6));
                leftFlowPane.getChildren().add(tableFlowPane);
            });
        });

    }


    private void resetUpdatePerson(){
        this.mainController.setUpdatingPerson(false);
        this.mainController.getUpdatePersonBtn().getStyleClass().remove("yellow");

        this.mainController.setPrint(false);
        this.mainController.getPrintBtn().getStyleClass().remove("yellow");
    }


}
