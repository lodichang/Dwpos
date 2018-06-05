package com.dw.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.CancelOrderDto;
import com.dw.dto.TableViewDto;
import com.dw.entity.*;

import com.dw.enums.*;
import com.dw.exception.PosOrderException;
import com.dw.extended.DwButton;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.ShowViewUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dw.enums.HoldOnEnum.CANCELCALL;
import static com.dw.enums.HoldOnEnum.HOLDON;
import static com.dw.enums.HoldOnEnum.SERVE;

@FXMLController
public class CancelItemController implements Initializable {
    private Stage cancelStage;
    @FXML
    private VBox cancelItemPane;
    @FXML
    private HBox cancelNumPane;
    @FXML
    private FlowPane cancelReasonPane;
    @FXML
    private Label cancelNumLable;
    @FXML
    private Label cancelNum;
    @FXML
    private Label cancelReasonPrompt;
    @FXML
    private Label itemInfo;
    @FXML
    private Button closeButton;
    @Autowired
    private PosReasonService posReasonService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private PosPrinterTaskService printerTaskService;
    @Autowired
    private SendOrderController sendOrderController;
    @Autowired
    private PosTableService posTableService;

    @Value("${STATION_ID}")
    private String stationId;

    private StringProperty cancelNumProperty = new SimpleStringProperty("0");
    private StringProperty itemInfoProperty = new SimpleStringProperty("0");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> cancelStage = (Stage) cancelItemPane.getScene().getWindow());
        mainComponent();
    }

    private void mainComponent() {
        cancelItemPane.setPadding(new Insets(10));
        cancelItemPane.setPrefSize(Main.primaryScreenBounds.getWidth() * 0.6, Main.primaryScreenBounds.getHeight() * 0.6);
        cancelNumPane.setPrefSize(cancelItemPane.getPrefWidth(), cancelItemPane.getPrefHeight() * 0.2);
        itemInfo.setPrefSize(cancelNumPane.getPrefWidth() * 0.35, cancelNumPane.getPrefHeight());
        itemInfo.textProperty().bind(itemInfoProperty);
        cancelNumLable.setText(Main.languageMap.get("global.cancelnum"));
        cancelNumLable.setPrefSize(cancelNumPane.getPrefWidth() * 0.15, cancelNumPane.getPrefHeight());
        cancelReasonPrompt.setText(Main.languageMap.get("global.selectcancelreason"));
        cancelNum.textProperty().bind(cancelNumProperty);
        cancelNum.setPrefSize(cancelNumPane.getPrefWidth() * 0.3, cancelNumPane.getPrefHeight());
        closeButton.setPrefSize(cancelNumPane.getPrefWidth() * 0.2, cancelNumPane.getPrefHeight() * 0.6);
        closeButton.setText(Main.languageMap.get("global.close"));

        cancelReasonPane.setPrefSize(cancelItemPane.getPrefWidth(), cancelItemPane.getPrefHeight() * 0.8);
        cancelReasonPane.setHgap(10);
        cancelReasonPane.setVgap(10);
    }


    public void initData(TableViewDto tableViewDto, String cancelNum, Boolean isUpdateOrder) {
        List<PosReason> posReasonList = posReasonService.selectList(null);
        cancelNumProperty.set(cancelNum);
        itemInfoProperty.set(tableViewDto.getItemCode() + " " + tableViewDto.getItemName());
        generateReason(posReasonList, tableViewDto, cancelNum, isUpdateOrder);

    }

    public void generateReason(List<PosReason> posReasons, TableViewDto tableViewDto, String cancelNum, Boolean isUpdateOrder) {
        cancelReasonPane.getChildren().clear();
        double resonBtnWidth = cancelReasonPane.getPrefWidth() / 5 - 15;
        double resonBtnHeight = cancelReasonPane.getPrefHeight() / 5 - 10;
        posReasons.forEach(posReason -> {
            Button cancelBtn = new DwButton(FontSizeEnum.font16);
            cancelBtn.setPrefSize(resonBtnWidth, resonBtnHeight);
            cancelBtn.setText(posReason.getDesc1());
            cancelBtn.setOnAction(event -> {
                try {
                    CancelOrderDto cancelOrderDto = null;
                    if (isUpdateOrder) {
                        cancelOrderDto = posOrderHisService.cancelItem(tableViewDto, cancelNum, posReason.getCode());
                        if (cancelOrderDto.isCanceled()) {
                            Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<PosTranHis>();
                            posTranHisWrapper.eq("REF_NUM", cancelOrderDto.getCancelOrder().getRefNum());
                            posTranHisWrapper.eq("SUB_REF", cancelOrderDto.getCancelOrder().getSubRef());
                            posTranHisWrapper.eq("OUTLET", Main.posOutlet);
                            posTranHisWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                            PosTranHis posTranHis = posTranHisService.selectOne(posTranHisWrapper);
                            if (posTranHis != null) {
                                posTranHis.setSettled("");
                                posTranHisService.updateById(posTranHis);
                            }
                            takeOrderIndexController.getPosTran().setSettled("");
                        }
                    }
                    else {
                        cancelOrderDto = posOrderService.cancelItem(tableViewDto, cancelNum, posReason.getCode());
                    }
                    if (cancelOrderDto.isCanceled()) {
                        //不在改单情况下的删除菜品
                        Map<String, PosPrinterTask> printerTaskMap = new HashMap<>();// prn task
                        Map<String, List<PosPrinterTaskDetail>> printerTaskDetailMap = new HashMap<>();// serialnumber list
                        StringBuilder serialNumbers = new StringBuilder();
                        if (AppUtils.isBlank(takeOrderIndexController.getSetmealGroupDtoObservableMap().get(tableViewDto.getItemCode())) && cancelOrderDto.getCancelOrder() instanceof PosOrder) {
                            setPrinterList(printerTaskMap, printerTaskDetailMap, serialNumbers, tableViewDto, cancelOrderDto, posReason.getPrinDesc());
                        }
                        // 取消食品需要单个单个打印，所以每一个食品都是一个打印任务
                        try {
                            // 添加打印任務
                            try {
                                // 添加打印任務
                                List<PosPrinterTask> taskList = new ArrayList<>();
                                List<PosPrinterTaskDetail> printerTaskDetailList = new ArrayList<>();
                                printerTaskMap.forEach((k, v) -> taskList.add(v));
                                printerTaskDetailMap.forEach((k, v) -> printerTaskDetailList.addAll(v));
                                printerTaskService.printBatch(taskList, printerTaskDetailList);
                                sendOrderController.printer(serialNumbers.toString(), cancelOrderDto.getCancelOrder().getRefNum(), cancelOrderDto.getCancelOrder().getSubRef(), PrinterTypeEnums.C);

                                //重新打一张收银总单
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //改单的话加载posOrderHis
                            if (cancelOrderDto.getCancelOrder() instanceof PosOrderHis) {
                                takeOrderIndexController.getOrderHisList();
                            } else {
                                takeOrderIndexController.getOrderList();
                                //删除菜品之后，刷新该台在message的叫起map
                                takeOrderIndexController.sendNettyMessageByHoldOn();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cancelStage.close();
                    } else {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        String result = ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.cancelfailure"), resultMap, null);
                        if (ResultEnum.YES.getValue().equals(result)) {
                            cancelStage.close();
                        }
                    }
                } catch (PosOrderException e) {
                    e.printStackTrace();
                }
            });
            cancelReasonPane.getChildren().add(cancelBtn);

        });

    }

    @FXML
    private void close() {

        cancelStage.close();
    }


    /**
     * 取消多個
     */
    public void initData(ObservableList<TableViewDto> selectedItems, Boolean isUpdateOrder) {
        List<PosReason> posReasonList = posReasonService.selectList(null);
        cancelNumProperty.set("所有");
        itemInfoProperty.set("所有選中商品");
        generateReason(posReasonList, selectedItems, isUpdateOrder);
    }

    /**
     * 取消多個只選一個原因
     */
    public void generateReason(List<PosReason> posReasons, ObservableList<TableViewDto> selectedItems, boolean isUpdateOrder) {
        cancelReasonPane.getChildren().clear();
        double resonBtnWidth = cancelReasonPane.getPrefWidth() / 5 - 15;
        double resonBtnHeight = cancelReasonPane.getPrefHeight() / 5 - 10;
        posReasons.forEach(posReason -> {
            Button cancelBtn = new DwButton(FontSizeEnum.font16);
            cancelBtn.setPrefSize(resonBtnWidth, resonBtnHeight);
            cancelBtn.setText(posReason.getDesc1());
            cancelBtn.setOnAction(event -> {
                try {
                    final String[] refNum = {""};
                    final String[] subRef = {""};
                    final String[] tableNum = {""};
                    StringBuilder itemIdxs = new StringBuilder();
                    Map<String, PosPrinterTask> printerTaskMap = new HashMap<>();// prn task
                    Map<String, List<PosPrinterTaskDetail>> printerTaskDetailMap = new HashMap<>();// serialnumber list
                    StringBuilder serialNumbers = new StringBuilder();
                    selectedItems.forEach(tableViewDto -> {
                        if (tableViewDto.isPrinter() && AppUtils.isNotBlank(tableViewDto.getItemCode()) && !tableViewDto.getItemCode().equals("0000")) {

                            itemIdxs.append(";").append(tableViewDto.getItemIdx());
                            //改单删除多个时，删除第一个成功后就false,避免多次修改posOrderHis的settled字段
                            boolean isUpdateOrderCancle = true;
                            CancelOrderDto cancelOrderDto = null;
                            if (!isUpdateOrder) {
                                cancelOrderDto = posOrderService.cancelItem(tableViewDto, tableViewDto.getQty(), posReason.getCode());
                            } else {
                                cancelOrderDto = posOrderHisService.cancelItem(tableViewDto, tableViewDto.getQty(), posReason.getCode());
                                if(cancelOrderDto.isCanceled() &&  isUpdateOrderCancle){
                                    Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
                                    posTranHisWrapper.eq("OUTLET", Main.posOutlet);
                                    posTranHisWrapper.eq("REF_NUM", takeOrderIndexController.getPosTran().getRefNum());
                                    posTranHisWrapper.eq("SUB_REF", takeOrderIndexController.getPosTran().getSubRef());
                                    posTranHisWrapper.eq("TRAN_TYPE", takeOrderIndexController.getPosTran().getTranType());
                                    PosTranHis posTranHis = posTranHisService.selectOne(posTranHisWrapper);
                                    if(posTranHis != null){
                                        posTranHis.setSettled("");
                                        posTranHisService.updateById(posTranHis);
                                        takeOrderIndexController.getPosTran().setSettled("");
                                        isUpdateOrderCancle = false;
                                    }
                                }
                            }
                            if (cancelOrderDto.isCanceled()) {
                                if (AppUtils.isBlank(refNum[0])) {
                                    refNum[0] = cancelOrderDto.getCancelOrder().getRefNum();
                                    subRef[0] = cancelOrderDto.getCancelOrder().getSubRef();
                                    tableNum[0] = cancelOrderDto.getCancelOrder().getTableNum();
                                }
                                if (AppUtils.isNotBlank(tableViewDto.getItemPrn())) {
                                    // 有主套餐编号的为子套餐
                                    setPrinterList(printerTaskMap, printerTaskDetailMap, serialNumbers, tableViewDto, cancelOrderDto, posReason.getPrinDesc());
                                }
                            }
                        }
                        if (tableViewDto.getItemCode().equals("0000") || AppUtils.isBlank(tableViewDto.getItemCode())) {
                            //為什麼送單再加載出來套餐內未選擇的菜品的編號是空而不是0000
                            posOrderService.deleteById(tableViewDto.getId());
                        }
                    });
                    try {
                        // 添加打印任務
                        List<PosPrinterTask> taskList = new ArrayList<>();
                        List<PosPrinterTaskDetail> printerTaskDetailList = new ArrayList<>();
                        printerTaskMap.forEach((k, v) -> taskList.add(v));
                        printerTaskDetailMap.forEach((k, v) -> printerTaskDetailList.addAll(v));
                        printerTaskService.printBatch(taskList, printerTaskDetailList);
                        sendOrderController.printer(serialNumbers.toString(), refNum[0], subRef[0], PrinterTypeEnums.C);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(isUpdateOrder){
                        takeOrderIndexController.getOrderHisList();
                    }
                    else{
                        takeOrderIndexController.getOrderList();
                        //删除菜品之后，刷新该台在message的叫起map
                        takeOrderIndexController.sendNettyMessageByHoldOn();
                    }
                    cancelStage.close();
                } catch (PosOrderException e) {
                    e.printStackTrace();
                }
            });
            cancelReasonPane.getChildren().add(cancelBtn);

        });

    }

    private void setPrinterList(Map<String, PosPrinterTask> printerTaskMap, Map<String, List<PosPrinterTaskDetail>> printerTaskDetailMap, StringBuilder serialNumbers, TableViewDto tableViewDto, CancelOrderDto cancelOrderDto, String reason) {
        // 獲取桌台，用於判斷是否外賣
        PosTable table = posTableService.getTableByNum(cancelOrderDto.getCancelOrder().getTableNum());
        Map<String, String> prnMap = new HashMap<>();
        Arrays.asList(tableViewDto.getItemPrn().split(",")).forEach(prn -> {
            if (AppUtils.isNotBlank(prn)) {
                prnMap.put(prn.trim(), prn.trim());
            }
        });
        prnMap.forEach((k, v) -> {
            if (AppUtils.isNotBlank(k)) {
                AtomicInteger attIndex = new AtomicInteger(1);
                if (AppUtils.isBlank(printerTaskMap.get(k))) {
                    PosPrinterTask printerTask = new PosPrinterTask();
                    printerTask.setVersion(new Date());
                    printerTask.setOutlet(Main.posSetting.get("outlet"));
                    printerTask.setStaffName(Main.posStaff.getName1());
                    printerTask.setStaff(Main.posStaff.getCode());
                    printerTask.setType(PrinterTypeEnums.C.getValue());
                    printerTask.setReason(reason);
                    printerTask.setPersons(takeOrderIndexController.getPosTran().getPerson());
                    printerTask.setTableNum(cancelOrderDto.getCancelOrder().getTableNum());
                    printerTask.setRefNum(cancelOrderDto.getCancelOrder().getRefNum());
                    printerTask.setSubRef(cancelOrderDto.getCancelOrder().getSubRef());
                    printerTask.setPrinter(k.trim());
                    printerTask.setStation(stationId);
                    printerTask.setSendTime(new Date());
                    printerTask.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                    printerTask.setCurrentPage(1);
                    printerTask.setTotalPage(1);
                    printerTask.setSerialNumber(sendOrderController.getPrinterTaskNo());
                    serialNumbers.append(";").append(printerTask.getSerialNumber());
                    // 是否套餐
                    if (Integer.parseInt(cancelOrderDto.getCancelOrder().getSetmeal()) > 0) {
                        printerTask.setSetmeal("TRUE");
                    } else {
                        printerTask.setSetmeal("FALSE");
                    }
                    if (table.getRoomType().equals(TableTypeEnum.CARRYOUT.getValue()) || table.getRoomType().equals(TableTypeEnum.CARRYOUTPLACE.getValue())) {
                        printerTask.setTitle(TableTypeEnum.getNameByValue(table.getRoomType()));
                    }
                    printerTaskMap.put(k, printerTask);
                }

                if (AppUtils.isBlank(printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()))) {
                    printerTaskDetailMap.put(printerTaskMap.get(k).getSerialNumber(), new ArrayList<>());
                }

                // 打印明細
                HoldOnEnum holdOnEnum = HoldOnEnum.getEnumByValue(tableViewDto.getHoldOn());

                PosPrinterTaskDetail taskItemDetail = new PosPrinterTaskDetail();
                taskItemDetail.setVersion(new Date());
                taskItemDetail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                taskItemDetail.setQty(new BigDecimal(String.valueOf(cancelOrderDto.getCancelOrder().getQty())));
                taskItemDetail.setSpec(String.valueOf(cancelOrderDto.getCancelOrder().getQty()) + "x");
                taskItemDetail.setAmt(cancelOrderDto.getCancelOrder().getAmt());
                taskItemDetail.setItemCode(tableViewDto.getItemCode());
                taskItemDetail.setItemName(tableViewDto.getItemName());
                taskItemDetail.setItemIdx(String.valueOf(cancelOrderDto.getCancelOrder().getItemIdx()));
                taskItemDetail.setSort(attIndex.get());
                attIndex.set(attIndex.get() + 1);
                taskItemDetail.setType(PrinterItemTypeEnum.ITEM.getValue());
                taskItemDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                taskItemDetail.setKicmType(holdOnEnum.getValue());
                printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(taskItemDetail);

                // 添加口味信息打印
                String detailAttCode = "";
                String detailAttGroupCode = "";
                String detailActionCode = "";
                if (AppUtils.isNotBlank(tableViewDto.getItemAttCode())) {
                    String[] _attCodes = tableViewDto.getItemAttCode().split("@");
                    String[] prices = AppUtils.isNotBlank(tableViewDto.getAttPrices()) ? tableViewDto.getAttPrices().split("@") : new String[]{"0.00"};
                    String[] names = AppUtils.isNotBlank(tableViewDto.getItemAtt()) ? tableViewDto.getItemAtt().split("/") : new String[]{"0"};
                    int attIdx = 0;
                    for (String attCode : _attCodes) {
                        if (AppUtils.isNotBlank(attCode)) {
                            String[] codes = attCode.split(":");
                            PosPrinterTaskDetail detail = new PosPrinterTaskDetail();
                            detail.setVersion(new Date());
                            detail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                            detail.setQty(new BigDecimal("0"));
                            detail.setSpec("**");
                            detail.setAmt(attIdx >= prices.length ? new BigDecimal("0.00") : new BigDecimal(prices[attIdx]));
                            detail.setItemCode(tableViewDto.getItemCode());
                            detail.setItemName(attIdx >= names.length ? "" : names[attIdx]);
                            detail.setItemIdx(String.valueOf(cancelOrderDto.getCancelOrder().getItemIdx()));
                            detail.setSort(attIndex.get());
                            attIndex.set(attIndex.get() + 1);
                            detail.setType(PrinterItemTypeEnum.ATT.getValue());
                            detail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                            detail.setAttCode(codes[1]);
                            detail.setAttGroupCode(codes[0]);
                            detail.setAttActionCode(codes.length > 2 ? codes[2] : null);
                            detail.setKicmType(holdOnEnum.getValue());
                            printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(detail);
                            attIdx++;
                            detailActionCode += "," + detail.getAttActionCode();
                            detailAttCode += "," + detail.getAttCode();
                            detailAttGroupCode += "," + detail.getAttGroupCode();
                        }
                    }
                }
                taskItemDetail.setAttCode(detailAttCode);
                taskItemDetail.setAttGroupCode(detailAttGroupCode);
                taskItemDetail.setAttActionCode(detailActionCode);

                // 廚房信息
                if (holdOnEnum.equals(HOLDON) || holdOnEnum.equals(SERVE)) {
                    PosPrinterTaskDetail taskDetail = new PosPrinterTaskDetail();
                    taskDetail.setVersion(new Date());
                    taskDetail.setSerialNumber(printerTaskMap.get(k).getSerialNumber());
                    taskDetail.setQty(BigDecimal.ZERO);
                    taskDetail.setSpec("**");
                    taskDetail.setAmt(BigDecimal.ZERO);
                    taskDetail.setItemCode(tableViewDto.getItemCode());
                    taskDetail.setItemName(holdOnEnum.getName());
                    taskDetail.setItemIdx(String.valueOf(cancelOrderDto.getCancelOrder().getItemIdx()));
                    taskDetail.setKicmType(holdOnEnum.getValue());
                    taskDetail.setSort(attIndex.get());
                    attIndex.set(attIndex.get() + 1);
                    taskDetail.setType(PrinterItemTypeEnum.KICM.getValue());
                    taskDetail.setPrintStatus(PrintStateEnum.UNPRINT.getValue());
                    printerTaskDetailMap.get(printerTaskMap.get(k).getSerialNumber()).add(taskDetail);
                }

            }
        });
    }
}

