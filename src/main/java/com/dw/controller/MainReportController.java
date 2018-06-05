package com.dw.controller;

import com.dw.Main;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import com.dw.view.MainView;
import com.dw.view.ReportParamChooseView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2018/5/26.
 */
@Getter
@Setter
@FXMLController
public class MainReportController {

    @Autowired
    private MainView mainView;
    @Autowired
    private ReportParamChooseView reportParamChooseView;
    @FXML
    private VBox root;
    @FXML
    private FlowPane reportsFlowPane;
    @FXML
    private FlowPane buttomFlowPane;
    @FXML
    private void initialize (){
        show();
    }

    private void show(){
        double width = Main.primaryScreenBounds.getWidth();
        double height = Main.primaryScreenBounds.getHeight();

        root.setPrefSize(width,height);

        double gap = 10.00;
        //按鈕數量
        int btnSize = 5;
        //按鈕的寬度
        double subNodeWidth = width / btnSize - gap;
        //按鈕的高度
        double subNodeHeight = height / 8 - 10;

        reportsFlowPane.setPrefSize(width, height / 8 * 7);
        reportsFlowPane.setPadding(new Insets(10, 0, 0, 5));
        reportsFlowPane.setHgap(gap);
        reportsFlowPane.setVgap(gap);
        initReportsPane(subNodeWidth,subNodeHeight);


        buttomFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 8);
        buttomFlowPane.setPadding(new Insets(0, 10, 0, 0));
        buttomFlowPane.setAlignment(Pos.CENTER_RIGHT);
        initBottomPane(subNodeWidth,subNodeHeight);

    }

    private void initReportsPane(double subNodeWidth,double subNodeHeight){
        DwButton periodBtn = new DwButton(FontSizeEnum.font22);
        periodBtn.setPrefSize(subNodeWidth,subNodeHeight);
        periodBtn.setText(Main.languageMap.get("report.name.periodreport"));
        periodBtn.setOnMouseClicked((MouseEvent event) -> {
            //跳轉到選擇條件界面
            ReportParamChooseController reportParamChooseController = (ReportParamChooseController) reportParamChooseView.getPresenter();
            reportParamChooseController.initData(Main.languageMap.get("report.name.periodreport"),"1");
            Main.showInitialView(reportParamChooseView.getClass());
        });

        DwButton salesBtn = new DwButton(FontSizeEnum.font22);
        salesBtn.setPrefSize(subNodeWidth,subNodeHeight);
        salesBtn.setText(Main.languageMap.get("report.name.foodsellreport"));
        salesBtn.setOnMouseClicked(event->{
            //跳轉到選擇條件界面
            ReportParamChooseController reportParamChooseController = (ReportParamChooseController) reportParamChooseView.getPresenter();
            reportParamChooseController.initData(Main.languageMap.get("report.name.foodsellreport"),"2");
            Main.showInitialView(reportParamChooseView.getClass());
        });

        DwButton cleanBtn = new DwButton(FontSizeEnum.font22);
        cleanBtn.setPrefSize(subNodeWidth,subNodeHeight);
        cleanBtn.setText(Main.languageMap.get("report.name.cleanturnmorereport"));
        cleanBtn.setOnMouseClicked(event->{
            //跳轉到選擇條件界面
            ReportParamChooseController reportParamChooseController = (ReportParamChooseController) reportParamChooseView.getPresenter();
            reportParamChooseController.initData(Main.languageMap.get("report.name.cleanturnmorereport"),"3");
            Main.showInitialView(reportParamChooseView.getClass());
        });


        reportsFlowPane.getChildren().addAll(periodBtn,salesBtn,cleanBtn);
    }

    private void initBottomPane(double subNodeWidth,double subNodeHeight){
        DwButton closeBtn = new DwButton(FontSizeEnum.font24);

        closeBtn.setPrefSize(subNodeWidth,subNodeHeight);
        closeBtn.setText(Main.languageMap.get("global.close"));
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setOnMouseClicked(event -> {
            //啟動新的線程進行轉場，提高性能
            MainController mainController = (MainController) mainView.getPresenter();
            mainController.iniData();
            Main.showInitialView(mainView.getClass());
        });

        buttomFlowPane.getChildren().addAll(closeBtn);
    }
}
