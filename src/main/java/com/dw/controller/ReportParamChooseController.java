package com.dw.controller;

import com.dw.Main;
import com.dw.entity.PosTran;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.service.PosTranService;
import com.dw.util.AppUtils;
import com.dw.view.MainReportView;
import com.dw.view.ReportView;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.nio.ch.SelectorImpl;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by li.yongliang on 2018/5/26.
 */
@Getter
@Setter
@FXMLController
public class ReportParamChooseController {

    @FXML
    private BorderPane reportParamPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private HBox bottomHbox;
    @FXML
    private VBox leftVbox;
    @FXML
    private VBox rightVbox;



    @Autowired
    private MainReportView mainReportView;
    @Autowired
    private ReportView reportView;
    double reportParamsWidth = Main.primaryScreenBounds.getWidth();
    double reportParamsHeight = Main.primaryScreenBounds.getHeight();
    double centerWidth = reportParamsWidth/2-2;
    double centerHeight = reportParamsHeight/8*6;
    private final String pattern = "yyyy-MM-dd";
    DatePicker leftDatePicker = new DatePicker();
    DatePicker rightDatePicker = new DatePicker();
    DwLabel leftLabel = new DwLabel(FontSizeEnum.font20);
    DwLabel rightLabel = new DwLabel(FontSizeEnum.font20);
    //ChoiceBox cb = new ChoiceBox();
    ComboBox periodCb = new ComboBox();
    List<String> periodList = new ArrayList<>();

    ComboBox floorCb = new ComboBox();
    List<String> floorList = new ArrayList<>();

    private StringProperty rptTitle = new SimpleStringProperty("");
    private StringProperty reportType = new SimpleStringProperty("");

    @Autowired
    private PosTranService posTranService;

    @FXML
    private void initialize (){

        initReportParams();
    }

    private void initReportParams(){


        reportParamPane.setPrefSize(reportParamsWidth,reportParamsHeight);

        topFlowPane.setPrefSize(reportParamPane.getPrefWidth(),reportParamPane.getPrefHeight()/8);
        topFlowPane.setAlignment(Pos.CENTER);
        initTopFlowPane();


        leftVbox.setPrefSize(centerWidth,centerHeight);
        leftVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(centerWidth,centerHeight);
        leftVbox.setAlignment(Pos.TOP_CENTER);
        initCenterVbox(centerWidth,centerHeight);

        bottomHbox.setPrefSize(reportParamPane.getPrefWidth(),reportParamPane.getPrefHeight()/8);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setSpacing(bottomHbox.getPrefWidth()/6*2/3-4);
        bottomHbox.setPadding(new Insets(8));
        initBottomHbox();

    }

    private void initTopFlowPane(){
        DwButton rptTitleBtn = new DwButton(FontSizeEnum.font28);
        rptTitleBtn.setPrefSize(topFlowPane.getPrefWidth(),topFlowPane.getPrefHeight());
        rptTitleBtn.textProperty().bind(rptTitle);

        topFlowPane.getChildren().addAll(rptTitleBtn);
    }

    private void initCenterVbox(double centerWidth,double centerHeight){


        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        leftDatePicker.setPrefSize(centerWidth,centerHeight/8*7);
        leftDatePicker.setValue(LocalDate.now());
        leftDatePicker.setShowWeekNumbers(true);
        leftDatePicker.setOnMouseClicked(event -> {
            System.out.println(leftDatePicker.getValue());
        });

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setPrefSize(leftDatePicker.getPrefWidth()/7,leftDatePicker.getPrefHeight()/6);
                        setStyle("-fx-font-size: 22px");
                        //setStyle("-fx-min-width: 100px;-fx-min-height: 100px;-fx-font-size: 26px");
                        leftLabel.setText(Main.languageMap.get("report.startdate")+leftDatePicker.getValue());

                    }
                };
            }
        };
        leftDatePicker.setDayCellFactory(dayCellFactory);
        leftDatePicker.setConverter(converter);
        leftDatePicker.setPromptText(pattern.toLowerCase());
        leftDatePicker.setShowWeekNumbers(false);
        leftDatePicker.setOnAction(event -> {
            if(("3").equals(reportType.get().toString())) {
                periodCb.getItems().clear();
                periodList.clear();
                periodList.add(Main.languageMap.get("turnmore.allperiod"));
                List<PosTran> posTranList = posTranService.getPeriod(Main.posOutlet, "" + leftDatePicker.getValue());
                for (PosTran posTran : posTranList) {
                    periodList.add(posTran.getPeriod());
                }
                periodCb.setItems(FXCollections.observableArrayList(periodList));
                periodCb.getSelectionModel().select(0);
            }
        });
        DatePickerSkin datePickerSkin = new DatePickerSkin(leftDatePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        popupContent.applyCss();
        popupContent.lookup(".spinner-label").setStyle("-fx-font-size: 22px;-fx-text-fill: #000000;");

        rightDatePicker.setPrefSize(centerWidth,centerHeight/8*7);
        rightDatePicker.setValue(LocalDate.now());
        rightDatePicker.setShowWeekNumbers(false);
        final Callback<DatePicker, DateCell> rightDayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setPrefSize(rightDatePicker.getPrefWidth()/7,rightDatePicker.getPrefHeight()/6);
                        setStyle("-fx-font-size: 22px");
                        rightLabel.setText(Main.languageMap.get("report.enddate")+rightDatePicker.getValue());
                    }
                };
            }
        };
        rightDatePicker.setDayCellFactory(rightDayCellFactory);
        rightDatePicker.setConverter(converter);
        rightDatePicker.setPromptText(pattern.toLowerCase());
        DatePickerSkin rightDatePickerSkin = new DatePickerSkin(rightDatePicker);
        Node rightPopupContent = rightDatePickerSkin.getPopupContent();
        rightPopupContent.applyCss();
        rightPopupContent.lookup(".spinner-label").setStyle("-fx-font-size: 22px;-fx-text-fill: #000000;");


        leftLabel.setPrefSize(centerWidth,centerHeight/8);
        leftLabel.setText(Main.languageMap.get("report.startdate")+leftDatePicker.getValue());

        rightLabel.setPrefSize(centerWidth,centerHeight/8);
        rightLabel.setText(Main.languageMap.get("report.enddate")+rightDatePicker.getValue());

        leftVbox.getChildren().addAll(popupContent,leftLabel);
        rightVbox.getChildren().addAll(rightPopupContent,rightLabel);
    }

    private void initBottomHbox(){
        DwButton confirmBtn = new DwButton(FontSizeEnum.font20);
        confirmBtn.setPrefSize(bottomHbox.getPrefWidth()/6,bottomHbox.getPrefHeight());
        confirmBtn.setText(Main.languageMap.get("report.confirm"));
        confirmBtn.setOnMouseClicked(event -> {
            Map<String,Object> paramsMap = new HashMap<>();
            String startDate = leftDatePicker.getValue().toString();
            String endDate = rightDatePicker.getValue().toString();
            System.out.println("startDate:"+startDate+"--------------endDate:"+endDate+"----------------reportType:"+reportType.get().toString()+"---------------reportType:"+reportType.get().toString());
            if("1".equals(reportType.get().toString()) || "2".equals(reportType.get().toString())) {
                paramsMap.clear();
                paramsMap.put("startDate", startDate);
                paramsMap.put("endDate", endDate);
            }else if("3".equals(reportType.get().toString())){
                System.out.println(periodCb.getSelectionModel().getSelectedItem().toString());
                String period = periodCb.getSelectionModel().getSelectedItem().toString();
                period = (Main.languageMap.get("turnmore.allperiod")).equals(period)?"":period;
                paramsMap.clear();
                paramsMap.put("startDate", startDate);
                paramsMap.put("period",period);

                String floor = floorCb.getSelectionModel().getSelectedItem().toString();
                floor = (Main.languageMap.get("zone.allzones")).equals(floor)?"A,B,C,D,E,F,G":floor;
                paramsMap.put("floor",floor);
            }
            ReportController reportController = (ReportController) reportView.getPresenter();
            reportController.initDataReport(reportType.get().toString(),rptTitle.get().toString(),4,paramsMap);
            Main.showInitialView(reportView.getClass());
        });

        Callback<ListView, ListCell> callback = new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                ListCell<String> cell = new ListCell<String>(){
                    @Override
                    public void updateItem(String item,boolean empty){
                        super.updateItem(item, empty);
                        if(AppUtils.isNotBlank(item)){
                            setText(item);
                            setStyle("-fx-background-color: #5E9BB5;-fx-text-fill: #d0d0d0;-fx-font-size: 20px;");
                        }else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        //更次
        periodCb.setPrefSize(bottomHbox.getPrefWidth()/6,bottomHbox.getPrefHeight());
        periodCb.setCellFactory(callback);
       /* periodCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                //System.out.println(periodNameList.get(newValue.intValue()<0?0:newValue.intValue())+"aaaaaaaaaaaaaaaaaaaaaaaa");
            }
        });*/

        //區域
        floorCb.setPrefSize(bottomHbox.getPrefWidth()/6,bottomHbox.getPrefHeight());
        floorCb.setCellFactory(callback);


        DwButton closeBtn = new DwButton(FontSizeEnum.font20);
        closeBtn.setPrefSize(bottomHbox.getPrefWidth()/6,bottomHbox.getPrefHeight());
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setText(Main.languageMap.get("global.close"));
        closeBtn.setOnMouseClicked(event -> {
            Main.showInitialView(mainReportView.getClass());

        });

        bottomHbox.getChildren().addAll(confirmBtn,periodCb,floorCb,closeBtn);
    }

    public void initData(String rptTitle,String rptType){

        Platform.runLater(()->{
            leftDatePicker.setValue(LocalDate.now());
            rightDatePicker.setValue(LocalDate.now());
            this.rptTitle.set(rptTitle);
            this.reportType.set(rptType);
            if("3".equals(reportType.get().toString())){
                periodCb.getItems().clear();
                periodList.clear();
                periodList.add(Main.languageMap.get("turnmore.allperiod"));
                periodCb.setItems(FXCollections.observableArrayList(periodList));
                periodCb.getSelectionModel().select(0);

                floorCb.getItems().clear();
                floorList.clear();
                floorList.add(AppUtils.isNotBlank(Main.posSetting.get("area")) ? Main.languageMap.get("zone.allzones") : "");
                if (AppUtils.isNotBlank(Main.posSetting.get("area"))){
                    floorList.addAll(1,Arrays.asList(Main.posSetting.get("area").split(",")));
                }
                floorCb.setItems(FXCollections.observableArrayList(floorList));
                floorCb.getSelectionModel().select(0);

                floorCb.setVisible(true);
                periodCb.setVisible(true);
                rightVbox.setPrefSize(0,0);
                rightVbox.setVisible(false);
                rightDatePicker.setVisible(false);
                rightDatePicker.setPrefSize(0,0);
                leftDatePicker.setPrefSize(centerWidth*2,centerHeight/8*7);
                leftVbox.setPrefSize((centerWidth+2)*2,centerHeight);
                leftLabel.setPrefSize(centerWidth*2,centerHeight/8);


            }else if("1".equals(reportType.get().toString()) || "2".equals(reportType.get().toString())){
                periodCb.setVisible(false);
                floorCb.setVisible(false);
                rightVbox.setPrefSize(centerWidth,centerHeight);
                rightVbox.setVisible(true);
                rightDatePicker.setVisible(true);
                rightDatePicker.setPrefSize(centerWidth,centerHeight/8*7);
                leftDatePicker.setPrefSize(centerWidth,centerHeight/8*7);
                leftVbox.setPrefSize(centerWidth,centerHeight);
                leftLabel.setPrefSize(centerWidth,centerHeight/8);
            }
        });

    }
}
