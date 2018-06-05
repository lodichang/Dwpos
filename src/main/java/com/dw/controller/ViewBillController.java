package com.dw.controller;

import com.dw.Main;
import com.dw.dto.*;
import com.dw.enums.LanguageEnum;
import com.dw.enums.PosSettingEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.print.PrintRxTx;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosOrderHisService;
import com.dw.service.PosPayMentService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.BillsView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@FXMLController
public class ViewBillController implements Initializable {
    @FXML
    private VBox viewBillVBox;
    @FXML
    private HBox topBillHbox;
    @FXML
    private TableView orderListView;
    @FXML
    private TableView paylist;
    @FXML
    private HBox buttomFlowPane;
    @FXML
    private Button reprintBtn;
    @FXML
    private HBox infoHBox;
    @FXML
    private VBox infoLeftVBox;
    @FXML
    private VBox infoRightVBox;
    @FXML
    private Button closeButton;
    @FXML
    private Label refNumLab;
    @FXML
    private Label refNum;
    @FXML
    private Label tableNumLab;
    @FXML
    private Label tableNum;
    @FXML
    private Label personLab;
    @FXML
    private Label person;
    @FXML
    private Label billAmtTopLab;
    @FXML
    private Label billAmtTop;
    @FXML
    private Label itemDiscLab;
    @FXML
    private Label itemDisc;
    @FXML
    private Label catDiscLab;
    @FXML
    private Label catDisc;
    @FXML
    private Label orderDiscLab;
    @FXML
    private Label orderDisc;
    @FXML
    private Label orderAmtLab;
    @FXML
    private Label orderAmt;
    @FXML
    private Label serverLab;
    @FXML
    private Label server;
    @FXML
    private Label billAmtLab;
    @FXML
    private Label billAmt;
    //食品列表
    private TableColumn<ViewBillItemDto, String> itemCodeCol;
    private TableColumn<ViewBillItemDto, String> itemDescCol;
    private TableColumn<ViewBillItemDto, String> attDescCol;
    private TableColumn<ViewBillItemDto, String> qtyCol;
    private TableColumn<ViewBillItemDto, String> cancelCol;
    private TableColumn<ViewBillItemDto, String> freeCol;
    private TableColumn<ViewBillItemDto, String> staffDescCol;
    private TableColumn<ViewBillItemDto, String> tTimeCol;
    private TableColumn<ViewBillItemDto, String> amtCol;
    //付款列表
    private TableColumn<PosPayDto, String> rTimeCol;
    private TableColumn<PosPayDto, String> payTyepCol;
    private TableColumn<PosPayDto, String> payDiscCol;
    private TableColumn<PosPayDto, String> amountCol;
    private TableColumn<PosPayDto, String> payStaffCol;

    private ObservableList<ViewBillItemDto> viewBillItemDtoList = FXCollections.observableArrayList();
    private ObservableList<PosPayDto> posPayDtos = FXCollections.observableArrayList();

    private BillDto bill;


    @Autowired
    private BillsView billsView;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosPayMentService posPayMentService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainComponent();

    }


    private void mainComponent() {
        viewBillVBox.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());
        topInfoComponent();
        tableComponentOrder();
        tableComponentPay();
        bottonComponent();

    }

    private void tableComponentPay() {
        paylist.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 14 * 3);
        double columWidth = (Main.primaryScreenBounds.getWidth()) / 5;

        rTimeCol = new TableColumn<>();
        rTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.DateToString(cellData.getValue().getRTime(), "HH:mm")));
        rTimeCol.setPrefWidth(columWidth);
        rTimeCol.setText(Main.languageMap.get("global.time"));

        payTyepCol = new TableColumn<>();
        payTyepCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayType()));
        payTyepCol.setPrefWidth(columWidth);
        payTyepCol.setText(Main.languageMap.get("global.itemcode"));

        payDiscCol = new TableColumn<>();
        payDiscCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayDisc()));
        payDiscCol.setPrefWidth(columWidth);
        payDiscCol.setText(Main.languageMap.get("global.payment"));

        amountCol = new TableColumn<>();
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount().toString()));
        amountCol.setPrefWidth(columWidth);
        amountCol.setText(Main.languageMap.get("global.amt"));

        payStaffCol = new TableColumn<>();
        payStaffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayStaff().toString()));
        payStaffCol.setPrefWidth(columWidth);
        payStaffCol.setText(Main.languageMap.get("global.staff"));

        paylist.getColumns().addAll(rTimeCol, payTyepCol, payDiscCol, amountCol, payStaffCol);

    }

    private void tableComponentOrder() {
        orderListView.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 14 * 8);
        double columWidth = (Main.primaryScreenBounds.getWidth() - 40) / 12;

        itemCodeCol = new TableColumn<>();
        itemCodeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemCode()));
        itemCodeCol.setPrefWidth(columWidth);
        itemCodeCol.setText(Main.languageMap.get("global.itemcode"));

        itemDescCol = new TableColumn<>();
        itemDescCol.setCellValueFactory(cellData -> {
            String[] languages = new String[]{cellData.getValue().getDesc1(), cellData.getValue().getDesc2(), cellData.getValue().getDesc3(), cellData.getValue().getDesc4()};
            String languagedefault = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
            return new SimpleStringProperty(languagedefault);
        });
        itemDescCol.setPrefWidth(columWidth * 2);
        itemDescCol.setText(Main.languageMap.get("global.itemdesc"));

        attDescCol = new TableColumn<>();
        attDescCol.setCellValueFactory(cellData -> {
            String[] languages = new String[]{cellData.getValue().getAttDesc1(), cellData.getValue().getAttDesc2(), cellData.getValue().getAttDesc3(), cellData.getValue().getAttDesc4()};
            String languagedefault = LanguageEnum.getLanguage(languages, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
            return new SimpleStringProperty(languagedefault);
        });
        attDescCol.setPrefWidth(columWidth * 3);
        attDescCol.setText(Main.languageMap.get("global.att"));

        qtyCol = new TableColumn<>();
        qtyCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQty().toString()));
        qtyCol.setPrefWidth(columWidth);
        qtyCol.setText(Main.languageMap.get("global.qty"));

        cancelCol = new TableColumn<>();
        cancelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCancel().toString()));
        cancelCol.setPrefWidth(columWidth);
        cancelCol.setText(Main.languageMap.get("global.cancel"));

        freeCol = new TableColumn<>();
        freeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFree().toString()));
        freeCol.setPrefWidth(columWidth);
        freeCol.setText(Main.languageMap.get("global.free"));

        staffDescCol = new TableColumn<>();
        staffDescCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStaffDesc()));
        staffDescCol.setPrefWidth(columWidth);
        staffDescCol.setText(Main.languageMap.get("global.staff"));

        tTimeCol = new TableColumn<>();
        tTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.DateToString(cellData.getValue().getTTime(), "HH:mm")));
        tTimeCol.setPrefWidth(columWidth);
        tTimeCol.setText(Main.languageMap.get("global.time"));

        amtCol = new TableColumn<>();
        amtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmt().toString()));
        amtCol.setPrefWidth(columWidth);
        amtCol.setText(Main.languageMap.get("global.amt"));

        orderListView.getColumns().addAll(itemCodeCol, itemDescCol, attDescCol, qtyCol, cancelCol, freeCol, staffDescCol, tTimeCol, amtCol);


    }


    private void topInfoComponent() {
        topBillHbox.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 14 * 0.8);
        double lableWidht = Main.primaryScreenBounds.getWidth() / 16;
        refNumLab.setText(Main.languageMap.get("global.refnum"));
        refNumLab.setPrefWidth(lableWidht);
        refNumLab.getStyleClass().add("lab");
        refNum.setPrefWidth(lableWidht * 2);
        refNum.getStyleClass().add("labVal");
        tableNumLab.setText(Main.languageMap.get("global.tablenum"));
        tableNumLab.setPrefWidth(lableWidht);
        tableNumLab.getStyleClass().add("lab");
        tableNum.getStyleClass().add("labVal");
        tableNum.setPrefWidth(lableWidht);
        personLab.setText(Main.languageMap.get("global.person"));
        personLab.getStyleClass().add("lab");
        personLab.setPrefWidth(lableWidht);
        person.getStyleClass().add("labVal");
        person.setPrefWidth(lableWidht);
        billAmtTopLab.setText(Main.languageMap.get("global.billamt"));
        billAmtTopLab.getStyleClass().add("lab");
        billAmtTopLab.setPrefWidth(lableWidht);
        billAmtTop.getStyleClass().add("labVal");
        billAmtTop.setPrefWidth(lableWidht * 2);
    }

    private void bottonComponent() {
        //时间格式
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        buttomFlowPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight() / 14 * 2.2);
        //buttomFlowPane.setPadding(new Insets(5));
        double nodeWidth = (Main.primaryScreenBounds.getWidth()) / 8;
        double nodeHeight = Main.primaryScreenBounds.getHeight() / 14 * 2.2;
        reprintBtn.setPrefWidth(nodeWidth);
        reprintBtn.setPrefHeight(nodeHeight);
        reprintBtn.setText(Main.languageMap.get("global.reprint"));
        reprintBtn.setOnAction(event -> {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    PrintStyleUtils.printPayBill(bill);
                    return null;
                }
            };
            new Thread(task).start();
           /* final BigDecimal[] payAmt = {new BigDecimal(0.00)};
            final BigDecimal[] backAmt = {new BigDecimal(0.00)};
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //付款成功后打印付款清单
            //加載付款記錄
            List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, bill.getRefNum(), bill.getSubRef(), bill.getTranType());
            RePrintPayBillDto payBillDto = new RePrintPayBillDto();
            payBillDto.setTableNo("    " + bill.getTableNum());
            payBillDto.setTel(Main.posOutletDto.getTel());
            payBillDto.setOutletName(Main.posOutletDto.getName1());
            payBillDto.setRefNum(bill.getRefNum());
            payBillDto.setStaffName("  " + Main.posStaff.getName1());
            payBillDto.setPrintDate(simpleDateFormat.format(new Date()));
            List<String> payList = new LinkedList<String>();
            if (AppUtils.isNotBlank(posPayDtoList)) {
                posPayDtoList.forEach((PosPayDto posPaydto) -> {
                    payList.add(posPaydto.getPayDisc() + "|" + posPaydto.getNetAmount());
                    payAmt[0] = payAmt[0].add(posPaydto.getNetAmount());
                    backAmt[0] = backAmt[0].add(posPaydto.getOverAmt());
                });
            }
            payBillDto.setPayList(payList);
            payBillDto.setBillAmt(bill.getBillAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "");
            payBillDto.setPayAmt(payAmt[0].toString());
            payBillDto.setBackAmt(backAmt[0].toString());

            payBillDto.setOrderAmt(String.valueOf(bill.getOrderAmt()));
            payBillDto.setOrderDisc(String.valueOf(bill.getOrderDisc()));
            payBillDto.setRounding(String.valueOf(bill.getRounding()));
            payBillDto.setServAmt(String.valueOf(bill.getServAmt()));

            Platform.runLater(() -> {
                PrintRxTx.printRxTxInstance().printAction(payBillDto);
            });*/
    });

    infoBoxComponent(nodeWidth *6, nodeHeight);
    closeButton.setPrefSize(nodeWidth,nodeHeight);
    closeButton.setText(Main.languageMap.get("global.close"));
    closeButton.getStyleClass().add("closeButton");
    closeButton.setOnAction(event -> {
        BillsController billsController = (BillsController) billsView.getPresenter();
        billsController.loadBillData();
        Main.showInitialView(billsView.getClass());
    });


}

    private void infoBoxComponent(double infosubPaneWidth, double infosubPaneHeight) {
        infoHBox.setPrefSize(infosubPaneWidth, infosubPaneHeight);
        infoHBox.setPadding(new Insets(10));

        infoLeftVBox.setPrefSize(infosubPaneWidth / 2, infosubPaneHeight);
        //  itemDiscLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight/4);
        itemDiscLab.setText(Main.languageMap.get("global.itemdisc"));
        catDiscLab.setText(Main.languageMap.get("global.catdisc"));
        // catDiscLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight/4);
        orderDiscLab.setText(Main.languageMap.get("global.orderdisc"));
//        orderDiscLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight/4);
        orderAmtLab.setText(Main.languageMap.get("global.orderamt"));
//        orderAmtLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight/4);
        infoRightVBox.setPrefSize(infosubPaneWidth / 2, infosubPaneHeight);
        infoRightVBox.setPadding(new Insets(10));
//        serverLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight / 2);
        serverLab.setText(Main.languageMap.get("global.server"));
//        billAmtLab.setPrefSize(infosubPaneWidth / 4, infosubPaneHeight / 2);
        billAmtLab.setText(Main.languageMap.get("global.billamt"));
    }


    public void initData(BillDto billDto) {
        refNum.setText(billDto.getRefNumShow());
        tableNum.setText(billDto.getTableNum());
        person.setText(billDto.getPerson().toString());
        billAmtTop.setText(billDto.getBillAmt().toString());
        itemDisc.setText(billDto.getItemDisc().toString());
        catDisc.setText(billDto.getCatDisc().toString());
        orderDisc.setText(billDto.getOrderDisc().toString());
        orderAmt.setText(billDto.getOrderAmt().toString());
        server.setText(billDto.getServAmt().toString());
        billAmt.setText(billDto.getBillAmt().toString());
        bill = billDto;

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                List<ViewBillItemDto> viewBillItemDtos = posOrderHisService.viewOrder(billDto.getRefNum(), billDto.getSubRef(), billDto.getTranType());


                viewBillItemDtoList = FXCollections.observableArrayList(viewBillItemDtos);
                orderListView.setItems(viewBillItemDtoList);

                List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, billDto.getRefNum(), billDto.getSubRef(), billDto.getTranType());
                posPayDtos = FXCollections.observableArrayList(posPayDtoList);
                paylist.setItems(posPayDtos);


                return null;
            }
        };
        new Thread(task).start();

    }


}
