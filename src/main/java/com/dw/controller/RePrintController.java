package com.dw.controller;

import com.ablegenius.netty.client.MessageNonAck;
import com.ablegenius.netty.common.Message;
import com.alibaba.fastjson.JSONObject;
import com.dw.Main;
import com.dw.dto.PrinterDto;
import com.dw.dto.RePrinterDto;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.PrintStateEnum;
import com.dw.enums.PrinterTypeEnums;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.netty.NettyClient;
import com.dw.service.PosPrinterTaskService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.ablegenius.netty.common.NettyCommonProtocol.REQUEST;


/**
 * Created by lodi on 2018/1/3.
 */
@Getter
@Setter
@FXMLController
public class RePrintController implements Initializable {
    @FXML
    private FlowPane printFolwPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private TableView<RePrinterDto> printsView;
    @FXML
    private HBox buttomHox;
    @FXML
    private FlowPane leftButtomPane;
    @FXML
    private FlowPane centerButtomFlowPane;
    @FXML
    private FlowPane rightButtomPane;
    @FXML
    private DwButton close_Button;
    @FXML
    private DwButton reprint_Button;

    //主单号
    private TableColumn<RePrinterDto, String> refNumCol;
    //台号
    private TableColumn<RePrinterDto, String> tableNoCol;
    //账单状态
    private TableColumn<RePrinterDto, String> typeCol;
    //入座人數
    private TableColumn<RePrinterDto, String> personsCol;
    //送單員工
    private TableColumn<RePrinterDto, String> staffNameCol;
    // 印機名稱
    private TableColumn<RePrinterDto, String> printerNameCol;
    // 送單時間
    private TableColumn<RePrinterDto, String> sendTimeCol;
    //打印状态
    private TableColumn<RePrinterDto, String> printStatusCol;
    //是否为套餐
    private TableColumn<RePrinterDto, String> itemNameCol;
    //備註
    private TableColumn<RePrinterDto, String> remarkeCol;

    private ObservableList<RePrinterDto> PosPrinterTasks = FXCollections.observableArrayList();
    @Autowired
    private PosPrinterTaskService posPrinterTaskService;
    @Autowired
    private MainView mainView;
    @Autowired
    private NettyClient nettyClient;

    private final String pattern = "yyyy-MM-dd";

    private List<String> printStatList = new ArrayList<>();

    private String printStat = "";
    final String[] greetings = new String[] { "UNPRINT", "PREPRINT", "PRINTED", "CANCEL"};
    
    


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
        initComponent();
        loadTableViewData();
    }

    private void initComponent(){
        double mainWidth = Main.primaryScreenBounds.getWidth();
        double mainHeight = Main.primaryScreenBounds.getHeight();
        printFolwPane.setPrefSize(mainWidth,mainHeight);

        topFlowPane.setPrefSize(mainWidth,mainHeight*0.08);
        topFlowPane.setPadding(new Insets(5, 10, 5, 10));
        DwLabel printStatLabel = new DwLabel(FontSizeEnum.font20);
        printStatLabel.setText(Main.languageMap.get("reprint.printStat"));
        printStatLabel.setTextFill(Color.web("#d0d0d0"));
        printStatLabel.setPrefSize(mainWidth/10,mainHeight*0.08*0.8);
        topFlowPane.getChildren().add(printStatLabel);
        printStatList.add("未打印");
        printStatList.add("等待打印");
        printStatList.add("已經打印");
        printStatList.add("取消");
        ChoiceBox<String> cb = new ChoiceBox<String>(FXCollections.observableArrayList(printStatList));
        cb.getSelectionModel().selectedIndexProperty()
                .addListener((ov, value, new_value) -> printStat = greetings[new_value.intValue()]);
        cb.setPrefSize(mainWidth/7,mainHeight*0.08*0.6);
        topFlowPane.getChildren().add(cb);
        DwButton searchButton = new DwButton();
        searchButton.setPrefSize(mainWidth/10,mainHeight*0.08*0.8);
        searchButton.setText("搜索");
        searchButton.setOnAction(event -> {
            loadTableViewData();
        });
        topFlowPane.getChildren().add(searchButton);
        printsView.setPrefSize(mainWidth,mainHeight *0.82);

        refNumCol = new TableColumn<>();
        refNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRefNum()));

        typeCol = new TableColumn<>();
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getType().equals(PrinterTypeEnums.N.getValue()) ? "普通" : (cellData.getValue().getType().equals(PrinterTypeEnums.C.getValue()) ? "消單" : (cellData.getValue().getType().equals(PrinterTypeEnums.H.getValue()) ? "即起" : (cellData.getValue().getType().equals(PrinterTypeEnums.U.getValue()) ? "追單" : (cellData.getValue().getType().equals(PrinterTypeEnums.S.getValue()) ? "單項轉移" : (cellData.getValue().getType().equals(PrinterTypeEnums.F.getValue()) ? "轉台" : cellData.getValue().getType())) )))
        ));

        tableNoCol = new TableColumn<>();
        tableNoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableNum()));

        personsCol = new TableColumn<>();
        personsCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPersons())));

        staffNameCol = new TableColumn<>();
        staffNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStaffName()));

        printerNameCol = new TableColumn<>();
        printerNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrinterName()));

        sendTimeCol = new TableColumn<>();
        sendTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.DateToString(cellData.getValue().getSendTime(), DateUtil.TIME_FORMAT)));

        printStatusCol = new TableColumn<>();
        printStatusCol.setCellValueFactory(cellData -> new SimpleStringProperty(PrintStateEnum.getPrintStateEnumByValue(cellData.getValue().getPrintStatus()).getName()));

        itemNameCol = new TableColumn<>();
        itemNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));

        remarkeCol = new TableColumn<>();
        remarkeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemark()));
        printsView.getColumns().addAll(tableNoCol,typeCol,staffNameCol,printerNameCol,sendTimeCol,printStatusCol,itemNameCol,remarkeCol);

        double columWidth = mainWidth / 8 - 5;
        refNumCol.setPrefWidth(columWidth);
        refNumCol.setText(Main.languageMap.get("reprint.refnum"));
        typeCol.setPrefWidth(columWidth);
        typeCol.setText(Main.languageMap.get("reprint.type"));
        tableNoCol.setPrefWidth(columWidth);
        tableNoCol.setText(Main.languageMap.get("reprint.tableNo"));
        personsCol.setPrefWidth(columWidth);
        personsCol.setText(Main.languageMap.get("reprint.person"));
        staffNameCol.setPrefWidth(columWidth);
        staffNameCol.setText(Main.languageMap.get("reprint.staffName"));
        printerNameCol.setPrefWidth(columWidth);
        printerNameCol.setText(Main.languageMap.get("reprint.printerName"));
        sendTimeCol.setPrefWidth(columWidth);
        sendTimeCol.setText(Main.languageMap.get("reprint.sendTime"));
        printStatusCol.setPrefWidth(columWidth);
        printStatusCol.setText(Main.languageMap.get("reprint.printStat"));
        remarkeCol.setPrefWidth(columWidth);
        remarkeCol.setText(Main.languageMap.get("reprint.remark"));
        itemNameCol.setPrefWidth(columWidth);
        itemNameCol.setText(Main.languageMap.get("reprint.itemName"));

        double buttomHoxWidth = Main.primaryScreenBounds.getWidth();
        double buttomHoxHeigth = Main.primaryScreenBounds.getHeight() / 15 * 3;
        buttomHox.setPrefSize(buttomHoxWidth, mainHeight *0.1);
        leftButtomPane.setPrefSize(buttomHoxWidth *0.4,buttomHoxHeigth);
        centerButtomFlowPane.setPrefSize(buttomHoxWidth *0.4,buttomHoxHeigth);
        rightButtomPane.setPrefSize(buttomHoxWidth *0.2,buttomHoxHeigth);
        close_Button.setPrefSize(120,70);
        close_Button.setText(Main.languageMap.get("global.close"));
        close_Button.getStyleClass().add("closeBtn");
        close_Button.setOnAction(event -> {
            MainController mainController = (MainController) mainView.getPresenter();
            mainController.iniData();
            Main.showInitialView(mainView.getClass());
        });

        reprint_Button.setPrefSize(120,70);
        reprint_Button.setText(Main.languageMap.get("reprint.rebtn"));
        reprint_Button.getStyleClass().add("closeBtn");
        reprint_Button.setOnAction(event -> {
            int rowIndex = printsView.getSelectionModel().getSelectedIndex();
            if (rowIndex >= 0) {
                RePrinterDto reDto = printsView.getItems().get(rowIndex);
                // 异步打印，防止卡死
                Task task = new Task<Void>() {
                    @Override
                    public Void call() {
                        Channel channel = nettyClient.getChannel();
                        String _serialNumbers = reDto.getSerialNumber();
                        // 打印
                        PrinterDto dto = new PrinterDto(NettyMessageTypeEnum.KITCHENPRINT);
                        dto.setType(PrinterTypeEnums.getEnumByValue(reDto.getType()));
                        dto.setStationId(reDto.getStation());
                        dto.setRefNum(dto.getRefNum());
                        dto.setSubRef(dto.getSubRef());
                        //dto.setItemIdx(_itemIdxs);
                        dto.setSerialNumbers(_serialNumbers);
                        //dto.setFromTableNum(fromTabelNum);
                        //dto.setToTableNum(toTabelNum);
                        String text = JSONObject.toJSONString(dto);
                        Message message = new Message();
                        message.sign(REQUEST);
                        message.setClientId(NettyClient.NETTY_CLIENT_ID);
                        message.data(text);

                        channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
                            if (!future.isSuccess()) {
                                System.out.println("發送後廚打印任務失敗:" + future.cause().getMessage());
                            } else {
                                System.out.println("發送後廚打印任務成功");
                            }
                        });
                        //防止对象处理发生异常的情况
                        MessageNonAck msgNonAck = new MessageNonAck(message, channel);
                        nettyClient.getClientConnector().addNeedAckMessageInfo(msgNonAck);
                        return null;
                    }

                };

                new Thread(task).start();
            }
        });
        rightButtomPane.setHgap(10);
    }

    private void loadTableViewData(){
        List<RePrinterDto> list = posPrinterTaskService.getPrinterTasks(Main.posSetting.get("outlet"),printStat);
        PosPrinterTasks = FXCollections.observableArrayList(list);
        printsView.setItems(PosPrinterTasks);
    }


    private StringConverter converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
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
}
