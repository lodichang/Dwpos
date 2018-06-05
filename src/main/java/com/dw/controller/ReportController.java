package com.dw.controller;

import com.dw.Main;
import com.dw.component.CleanMachineComponent;
import com.dw.dto.CleanDto;
import com.dw.dto.PeriodReportDto;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.ResultEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.print.GetPrintOrReportStr;
import com.dw.print.PrintRxTx;
import com.dw.service.PosOrderHisService;
import com.dw.service.PosPayService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.BillsView;
import com.dw.view.MainReportView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by li.yongliang on 2018/5/23.
 */
@Getter
@Setter
@FXMLController
public class ReportController implements Initializable {

    @Value("${STATION_ID}")
    private String stationid;
    @FXML
    private FlowPane reportFlowPane;
    @FXML
    private BorderPane reportBorderPane;
    @FXML
    private FlowPane topFlowPane;

    @FXML
    private FlowPane centerFlowPane;
    @FXML
    private VBox rightVbox;
    @FXML
    private ScrollPane scrollPane;

    private TableView<CleanDto> cleanDtoTableView = new TableView<>();

    private TableColumn<CleanDto, String> codeCol;

    private TableColumn<CleanDto, String> desc1Col;

    private TableColumn<CleanDto, String> couCol;

    private TableColumn<CleanDto, String> amt1Col;

    private TableColumn<CleanDto, String> amt2Col;

    @Autowired
    private PosPayService posPayService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private CleanMachineComponent cleanMachineComponent;


    private StringProperty printall = new SimpleStringProperty("");
    //報表顯示的表頭
    private StringProperty reportTitle = new SimpleStringProperty("");
    private StringProperty actionName = new SimpleStringProperty("");
    //需要打印的數據列表
    List<String> list_1 = new ArrayList<>();
    //表樣
    String style = "";
    //打印的時候小票換行列數
    int printDisplayCols = 0;
    Map<String, Object> mapmessage = new HashMap<String, Object>();
    private Stage reportStage;
    private String reportType;
    private double width = Main.primaryScreenBounds.getWidth();
    private double height = Main.primaryScreenBounds.getHeight();
    DwButton closeBtn = new DwButton(FontSizeEnum.font28);
    DwButton actionBtn = new DwButton(FontSizeEnum.font28);
    DwButton printBtn = new DwButton(FontSizeEnum.font28);

    @Autowired
    private BillsView billsView;
    @Autowired
    private MainReportView mainReportView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Platform.runLater(() -> {
//            reportStage = (Stage) reportFlowPane.getScene().getWindow();
//        });
        show();
    }

    public void initData(String type, String rptTitle, int printDisplayCols) {
        this.reportType = type;
        this.printDisplayCols = printDisplayCols;

        clearData();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(() -> {
                        reportTitle.set(rptTitle);
                        initPrintData();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void initDataReport(String type, String rptTitle, int printDisplayCols, Map<String, Object> paramsMap) {
        this.reportType = type;
        this.printDisplayCols = printDisplayCols;

        clearData();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(() -> {
                        reportTitle.set(rptTitle);
                        initReportPrintData(paramsMap);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void show() {
        reportFlowPane.setPrefSize(width, height);

        reportBorderPane.setPrefSize(width, height);

        topFlowPane.setPrefSize(reportBorderPane.getPrefWidth(), reportBorderPane.getPrefHeight() / 10);
        topFlowPane.setAlignment(Pos.CENTER);
        initTopFlowPane();

        centerFlowPane.setPrefSize(reportBorderPane.getPrefWidth() * 0.9, reportBorderPane.getPrefHeight() * 0.9);
        centerFlowPane.setAlignment(Pos.CENTER);
        initCenterFlowPane();

        rightVbox.setPrefSize(reportBorderPane.getPrefWidth() * 0.1, reportBorderPane.getPrefHeight() * 0.9);
        rightVbox.setAlignment(Pos.CENTER);
        rightVbox.setSpacing(reportBorderPane.getPrefHeight() / 6);
        initRightVbox();

    }

    private void initTopFlowPane() {
        DwLabel titleLabel = new DwLabel(FontSizeEnum.font28);
        titleLabel.setPrefSize(topFlowPane.getPrefWidth(), topFlowPane.getPrefHeight() * 0.96);
        titleLabel.textProperty().bind(reportTitle);
        titleLabel.setPadding(new Insets(0, 0, 0, 20));
        topFlowPane.getChildren().add(titleLabel);
    }

    private void initCenterFlowPane() {
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(centerFlowPane.getPrefWidth(), centerFlowPane.getPrefHeight());
        scrollPane.setContent(cleanDtoTableView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
       /* DwLabel contentLabel = new DwLabel(FontSizeEnum.font28);
        contentLabel.setPrefWidth(centerFlowPane.getPrefWidth()*0.98);
        contentLabel.setMinHeight(centerFlowPane.getPrefHeight()*0.96);
        //contentLabel.setPrefSize(centerFlowPane.getPrefWidth()*0.98, centerFlowPane.getPrefHeight()*0.9);
        contentLabel.setWrapText(true);

        contentLabel.textProperty().bind(printall);
        //contentLabel.setText("");
        contentLabel.setTextAlignment(TextAlignment.LEFT);
        contentLabel.setAlignment(Pos.TOP_LEFT);
        contentLabel.setPadding(new Insets(20, 20, 20, 20));*/

        cleanDtoTableView.setPrefWidth(centerFlowPane.getPrefWidth());
        cleanDtoTableView.setMinHeight(centerFlowPane.getPrefHeight() - 3);
        cleanDtoTableView.setPadding(new Insets(2, 2, 2, 2));
        cleanDtoTableView.setPlaceholder(new DwLabel());
        cleanDtoTableView.setBorder(Border.EMPTY);
        cleanDtoTableView.getStyleClass().add("noheader");

        codeCol = new TableColumn<>();
        codeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));

        desc1Col = new TableColumn<>();
        desc1Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesc1()));

        couCol = new TableColumn<>();
        couCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCou()));

        amt1Col = new TableColumn<>();
        amt1Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmt1()));


        amt2Col = new TableColumn<>();
        amt2Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmt2()));

        double colWidth = (centerFlowPane.getPrefWidth()-30) / 12;
        codeCol.setPrefWidth(colWidth * 3);
        desc1Col.setPrefWidth(colWidth * 2);
        couCol.setPrefWidth(colWidth * 2);
        amt1Col.setPrefWidth(colWidth * 3);
        amt2Col.setPrefWidth(colWidth * 2);

        cleanDtoTableView.getColumns().addAll(codeCol, desc1Col, couCol, amt1Col, amt2Col);


    }

    private void initRightVbox() {


        printBtn.setPrefSize(rightVbox.getPrefWidth(), rightVbox.getPrefHeight() / 6);
        printBtn.setText(Main.languageMap.get("global.print"));
        printBtn.setOnMouseClicked(event -> {
            Platform.runLater(() -> {
                PrintRxTx.printRxTxInstance().printAction(style, mapmessage);
            });
        });


        closeBtn.setPrefSize(rightVbox.getPrefWidth(), rightVbox.getPrefHeight() / 6);
        closeBtn.setText(Main.languageMap.get("global.close"));
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setOnMouseClicked(event -> {
            if (reportType.equals("1") || reportType.equals("2") || reportType.equals("3")) {
                /*MainReportController mainReportController = (MainReportController) mainReportView.getPresenter();
                mainReportController.loadBillData();*/
                Main.showInitialView(mainReportView.getClass());
            } else {
                BillsController billsController = (BillsController) billsView.getPresenter();
                billsController.loadBillData();
                Main.showInitialView(billsView.getClass());
            }

        });


        actionBtn.setPrefSize(rightVbox.getPrefWidth(), rightVbox.getPrefHeight() / 6);
        //actionBtn.setText("上一个");
        actionBtn.textProperty().bind(actionName);
        actionBtn.setOnMouseClicked(event -> {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
            if (reportType.equals("clean")) {
                int cleanResult = cleanMachineComponent.cleanMachine();
                resultMap.remove(ResultEnum.NO.getName());
                if (cleanResult == 1) {
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("bill.cleanMatchine.true"), resultMap, null);
                } else if (cleanResult == -2) {
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("bill.cleanMatchine.hisfalse"), resultMap, null);
                } else if (cleanResult == -3) {
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("bill.cleanMatchine.false"), resultMap, null);
                }
            } else if (reportType.equals("turnMore")) {
                int turnResult = cleanMachineComponent.turnMore();
                resultMap.remove(ResultEnum.NO.getName());
                if (turnResult == 3) {
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("bill.turnmore.true"), resultMap, null);
                }
            }
        });
        rightVbox.getChildren().addAll(printBtn, closeBtn, actionBtn);
    }

    private void clearData() {
        list_1.clear();
        printall.set("");
        reportTitle.set("");
        style = "";
        mapmessage.clear();
    }

    private void initPrintData() {
        if (reportType.equals("clean") || reportType.equals("lookReport")) {
            if (reportType.equals("lookReport")) {
                actionBtn.setVisible(false);
            }else{
                actionBtn.setVisible(true);
            }
            actionName.set(Main.languageMap.get("bill.cleanMatchine"));
            style = Main.posSetting.get("DAILYCLEANREPORT");
            List<CleanDto> cleanDtoList = posPayService.cleanGetPayRecord(Main.posSetting.get("report.params.floor"));
            for (CleanDto cleanDto : cleanDtoList) {
                String dataStr = "";
                if (AppUtils.isNotBlank(cleanDto.getCode()) && cleanDto.getCode().equals("#SPLIT_SCREEN")) {
                    dataStr = cleanDto.getCode();
                } else {
                    if (printDisplayCols == 5) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                        }
                    } else if (printDisplayCols == 4) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R|R" + cleanDto.getAmt2() + "|R|R");
                            }
                        }
                    } else if (printDisplayCols == 3) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt1()) || AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                            }
                        }
                    }
                }
                list_1.add(dataStr);
            }

            mapmessage.put("orderList", list_1);
            mapmessage.put("PRINTDATE", DateUtil.getCurrTimeYmdHms());
            mapmessage.put("STAFF", Main.posStaff.getName1());
            mapmessage.put("OUTNAME", Main.posOutletDto.getName1());
            mapmessage.put("REPORTTITLE", reportTitle.get());
            List<CleanDto> cleanDtos = cleanDtoList.stream().filter(cleanDto -> AppUtils.isBlank(cleanDto.getCode()) || (AppUtils.isNotBlank(cleanDto.getCode()) && !cleanDto.getCode().startsWith("#"))).collect(Collectors.toList());
            cleanDtoTableView.setItems(FXCollections.observableArrayList(cleanDtos));
        } else if (reportType.equals("turnMore")) {
            actionBtn.setVisible(true);
            actionName.set(Main.languageMap.get("bill.turnmore"));

            style = Main.posSetting.get("DAILYCLEANREPORT");
            List<CleanDto> cleanDtoList = posPayService.turnMoreGetPayRecord(Main.posSetting.get("report.params.floor"));
            for (CleanDto cleanDto : cleanDtoList) {
                String dataStr = "";
                if (AppUtils.isNotBlank(cleanDto.getCode()) && cleanDto.getCode().equals("#SPLIT_SCREEN")) {
                    dataStr = cleanDto.getCode();
                } else {
                    if (printDisplayCols == 5) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                        }
                    } else if (printDisplayCols == 4) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R|R" + cleanDto.getAmt2() + "|R|R");
                            }
                        }
                    } else if (printDisplayCols == 3) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt1()) || AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                            }
                        }
                    }
                }
                list_1.add(dataStr);
            }
            mapmessage.put("orderList", list_1);
            mapmessage.put("PRINTDATE", DateUtil.getCurrTimeYmdHms());
            mapmessage.put("STAFF", Main.posStaff.getName1());
            mapmessage.put("OUTNAME", Main.posOutletDto.getName1());
            mapmessage.put("REPORTTITLE", reportTitle.get());
            List<CleanDto> cleanDtos = cleanDtoList.stream().filter(cleanDto -> AppUtils.isBlank(cleanDto.getCode()) || (AppUtils.isNotBlank(cleanDto.getCode()) && !cleanDto.getCode().startsWith("#"))).collect(Collectors.toList());
            cleanDtoTableView.setItems(FXCollections.observableArrayList(cleanDtos));
        }
    }


    private void initReportPrintData(Map<String, Object> paramsMap) {
        actionBtn.setVisible(false);
        if (reportType.equals("1")) {
            List<CleanDto> cleanDtos = new ArrayList<>();
            style = Main.posSetting.get("DAILYCLEANREPORT");
            List<PeriodReportDto> periodReportDtos = posOrderHisService.getPeriodReportRecords(Main.posOutlet, paramsMap.get("startDate").toString(), paramsMap.get("endDate").toString(),stationid);
            for (PeriodReportDto periodReportDto : periodReportDtos) {
                String dataStr = "";
                if (AppUtils.isNotBlank(periodReportDto.getPeriodname()) && periodReportDto.getPeriodname().equals("#SPLIT_SCREEN")) {
                    dataStr = periodReportDto.getPeriodname();
                } else {
                    dataStr = (AppUtils.isBlank(periodReportDto.getPeriodname())?"":periodReportDto.getPeriodname() + "|R" + periodReportDto.getSingleamt() + "|R" + periodReportDto.getPro() + "|R|R");
                }
                list_1.add(dataStr);

                if ((AppUtils.isNotBlank(periodReportDto.getPeriodname()) && !periodReportDto.getPeriodname().startsWith("#")) || AppUtils.isBlank(periodReportDto.getPeriodname())) {
                    CleanDto cleanDto = new CleanDto();
                    cleanDto.setCode(periodReportDto.getPeriodname());
                    cleanDto.setDesc1(periodReportDto.getSingleamt());
                    cleanDto.setCou(periodReportDto.getPro());

                    cleanDtos.add(cleanDto);
                }
            }

            mapmessage.put("orderList", list_1);
            mapmessage.put("PRINTDATE", DateUtil.getCurrTimeYmdHms());
            mapmessage.put("STAFF", Main.posStaff.getName1());
            mapmessage.put("OUTNAME", Main.posOutletDto.getName1());
            mapmessage.put("REPORTTITLE", reportTitle.get());

            cleanDtoTableView.setItems(FXCollections.observableArrayList(cleanDtos));
        } else if (reportType.equals("2")) {
            List<CleanDto> cleanDtos = new ArrayList<>();
            style = Main.posSetting.get("DAILYCLEANREPORT");
            List<PeriodReportDto> periodReportDtos = posOrderHisService.getFoodSellRecords(Main.posOutlet, paramsMap.get("startDate").toString(), paramsMap.get("endDate").toString(), stationid);
            for (PeriodReportDto periodReportDto : periodReportDtos) {
                String dataStr = "";
                if (AppUtils.isNotBlank(periodReportDto.getPeriodname()) && periodReportDto.getPeriodname().equals("#SPLIT_SCREEN")) {
                    dataStr = periodReportDto.getPeriodname();
                } else {
                    dataStr = (AppUtils.isBlank(periodReportDto.getPeriodname())?"":periodReportDto.getPeriodname() + "|R" + periodReportDto.getSingleamt() + "|R" + periodReportDto.getAmt() + "|R"+ periodReportDto.getPro() +"|R");
                }
                list_1.add(dataStr);

                if ((AppUtils.isNotBlank(periodReportDto.getPeriod()) && !periodReportDto.getPeriod().startsWith("#")) || AppUtils.isBlank(periodReportDto.getPeriod())) {
                    CleanDto cleanDto = new CleanDto();
                    cleanDto.setCode(periodReportDto.getPeriod());
                    cleanDto.setDesc1(periodReportDto.getPeriodname());
                    cleanDto.setCou(periodReportDto.getSingleamt());
                    cleanDto.setAmt1(periodReportDto.getAmt());
                    cleanDto.setAmt2(periodReportDto.getPro());
                    cleanDtos.add(cleanDto);
                }
            }

            mapmessage.put("orderList", list_1);
            mapmessage.put("PRINTDATE", DateUtil.getCurrTimeYmdHms());
            mapmessage.put("STAFF", Main.posStaff.getName1());
            mapmessage.put("OUTNAME", Main.posOutletDto.getName1());
            mapmessage.put("REPORTTITLE", reportTitle.get());

            cleanDtoTableView.setItems(FXCollections.observableArrayList(cleanDtos));
        }else if (reportType.equals("3")) {
            style = Main.posSetting.get("DAILYCLEANREPORT");
            List<CleanDto> cleanDtoList = posOrderHisService.getCleanOrTurnMoreRecords(Main.posOutlet, paramsMap.get("startDate").toString(), paramsMap.get("period").toString(), paramsMap.get("floor").toString());
            for (CleanDto cleanDto : cleanDtoList) {
                String dataStr = "";
                if (AppUtils.isNotBlank(cleanDto.getCode()) && cleanDto.getCode().equals("#SPLIT_SCREEN")) {
                    dataStr = cleanDto.getCode();
                } else {
                    if (printDisplayCols == 5) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                        }
                    } else if (printDisplayCols == 4) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R" + cleanDto.getAmt1() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R|R" + cleanDto.getAmt2() + "|R|R");
                            }
                        }
                    } else if (printDisplayCols == 3) {
                        if (cleanDto.getTitletype().equals("TITLE")) {
                            dataStr = ("&*" + cleanDto.getCode() + "*|R|R|R");
                        } else {
                            dataStr = (cleanDto.getCode() + "|R" + cleanDto.getDesc1() + "|R" + cleanDto.getCou() + "|R");
                            if (AppUtils.isNotBlank(cleanDto.getAmt1()) || AppUtils.isNotBlank(cleanDto.getAmt2())) {
                                list_1.add(dataStr);
                                dataStr = ("|R" + cleanDto.getAmt1() + "|R" + cleanDto.getAmt2() + "|R");
                            }
                        }
                    }
                }
                list_1.add(dataStr);
            }

            mapmessage.put("orderList", list_1);
            mapmessage.put("PRINTDATE", DateUtil.getCurrTimeYmdHms());
            mapmessage.put("STAFF", Main.posStaff.getName1());
            mapmessage.put("OUTNAME", Main.posOutletDto.getName1());
            mapmessage.put("REPORTTITLE", reportTitle.get());
            List<CleanDto> cleanDtos = cleanDtoList.stream().filter(cleanDto -> AppUtils.isBlank(cleanDto.getCode()) || (AppUtils.isNotBlank(cleanDto.getCode()) && !cleanDto.getCode().startsWith("#"))).collect(Collectors.toList());
            cleanDtoTableView.setItems(FXCollections.observableArrayList(cleanDtos));
        }
    }
}
