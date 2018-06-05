package com.dw.component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.controller.MainController;
import com.dw.controller.OpenTableController;
import com.dw.controller.SendOrderController;
import com.dw.controller.TakeOrderIndexController;
import com.dw.dto.TableViewDto;
import com.dw.entity.PosLog;
import com.dw.entity.PosTable;
import com.dw.entity.PosTran;
import com.dw.enums.*;
import com.dw.exception.PosOrderException;
import com.dw.print.PrintStyleUtils;
import com.dw.service.PosLogService;
import com.dw.service.PosOrderService;
import com.dw.service.PosTableService;
import com.dw.service.PosTranService;
import com.dw.util.AppUtils;
import com.dw.util.ShowViewUtil;
import com.dw.view.MainView;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * * @author wen.jing
 * 點菜主界面組件
 * 1.分單
 * 2.轉台
 * 3.追單
 * 4.修改口味
 */
@Getter
@Setter
@Component
public class TakerOrderIndexComponemt {
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private PosTableService posTableService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private OpenTableController openTableController;
    @Autowired
    private SendOrderController sendOrderController;
    @Autowired
    private MainView mainView;
    @Autowired
    private NettyComponent nettyComponent;

    /**
     * 分單方法
     * 1.判斷原始檯是否結賬,如果結賬則提示已經結賬不能轉台
     * 2.判斷目標檯是否有開台,如果沒有,執行開檯流程,并將數據寫入新單,并寫入LOG
     * 3.如果已經存在的賬單,則直接寫入,并寫入LOG
     *
     * @param posOrders
     * @param toTableNum
     * @param stage
     */
    public void handleSplit(ObservableList<TableViewDto> posOrders, PosTran formPosTran, String toTableNum, Stage stage) {
        if (!formPosTran.getTableNum().equals(toTableNum) && posOrders.size() > 0) {
            StringBuffer itemIdx = new StringBuffer();
            posOrders.forEach(po -> itemIdx.append(po.getItemIdx() + ";"));

            Wrapper<PosTable> posTableWrapper = new EntityWrapper<>();
            posTableWrapper.eq("OUTLET", Main.posOutlet);
            posTableWrapper.eq("ROOM_NUM", toTableNum);
            PosTable posTable = posTableService.selectOne(posTableWrapper);
            if (AppUtils.isBlank(posTable)) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.tablenotfound"), resultMap, stage);
            } else {
                Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                posTranWrapper.eq("REF_NUM", formPosTran.getRefNum());
                posTranWrapper.eq("TRAN_TYPE", formPosTran.getTranType());
                posTranWrapper.eq("OUTLET", formPosTran.getOutlet());
                PosTran orgTran = posTranService.selectOne(posTranWrapper);
                //1.判斷原始檯是否結賬,如果結賬則提示已經結賬不能轉台
                if (AppUtils.isBlank(orgTran) || AppUtils.isNotBlank(orgTran.getSettled())) {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), orgTran.getTableNum() + Main.languageMap.get("global.orderalreadybillcannottran"), resultMap, stage);
                } else {
                    //判斷目標檯是否有結賬
                    Wrapper<PosTran> toPosTranWrapper = new EntityWrapper<>();
                    toPosTranWrapper.eq("TABLE_NUM", toTableNum);
                    toPosTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                    toPosTranWrapper.eq("OUTLET", formPosTran.getOutlet());
                    PosTran toTran = posTranService.selectOne(toPosTranWrapper);
                    if (AppUtils.isNotBlank(toTran) && AppUtils.isNotBlank(toTran.getSettled())) {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), toTran.getTableNum() + Main.languageMap.get("global.orderalreadybillcannottran"), resultMap, stage);
                    } else if (AppUtils.isBlank(toTran)) {
                        //2.判斷目標檯是否有開台,如果沒有,執行開檯流程,并將數據寫入新單,并寫入LOG
                        try {
                            PosTran newTran = openTableController.openTable(toTableNum);
                            posOrderService.splitItemToTable(posOrders, newTran);
                            //打印單品轉台廚房單
                            sendOrderController.sendOrders(newTran, null, true, PrinterTypeEnums.S, orgTran.getTableNum(), toTableNum, itemIdx.toString(), null, false);
                            // 打印上菜紙
                            PrintStyleUtils.printTicket(orgTran);
                            // 因還沒開台，所以上面toTran沒數據，此時需要重新查詢數據
                            toPosTranWrapper = new EntityWrapper<>();
                            toPosTranWrapper.eq("TABLE_NUM", toTableNum);
                            toPosTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                            toPosTranWrapper.eq("OUTLET", formPosTran.getOutlet());
                            toTran = posTranService.selectOne(toPosTranWrapper);
                            PrintStyleUtils.printTicket(toTran);
                            //返回點菜頁面,并刷新賬單
                            takeOrderIndexController.initData(takeOrderIndexController.getPosTran(), takeOrderIndexController.getPosTableDto(), takeOrderIndexController.getIsUpdateOrder(), InitDataTypeEnum.SETTLE.getValue());
                            //分臺成功後發送netty刷新檯號
                            nettyComponent.sendMessage(NettyMessageTypeEnum.SPLITTABLE);
                        } catch (PosOrderException e) {
                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), e.getMessage(), resultMap, stage);
                        }
                    } else if (AppUtils.isNotBlank(toTran) && AppUtils.isBlank(toTran.getSettled())) {
                        //3.如果已經存在的賬單,則直接寫入,并寫入LOG
                        try {
                            posOrderService.splitItemToTable(posOrders, toTran);
                            //打印單品轉台廚房單
                            sendOrderController.sendOrders(toTran, null, true, PrinterTypeEnums.S, orgTran.getTableNum(), toTableNum, itemIdx.toString(), null, false);
                            // 打印上菜紙
                            PrintStyleUtils.printTicket(orgTran);
                            PrintStyleUtils.printTicket(toTran);
                            //返回點菜頁面,并刷新賬單
                            takeOrderIndexController.initData(takeOrderIndexController.getPosTran(), takeOrderIndexController.getPosTableDto(), takeOrderIndexController.getIsUpdateOrder(), InitDataTypeEnum.SETTLE.getValue());
                            //分臺成功後發送netty刷新檯號
                            nettyComponent.sendMessage(NettyMessageTypeEnum.SPLITTABLE);
                        } catch (PosOrderException e) {
                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), e.getMessage(), resultMap, stage);
                        }

                    }
                    //todo 上云
                }

            }

        }
    }

    /**
     * 轉台方法
     * 1.判斷原始檯是否結賬,如果結賬則提示已經結賬不能轉台
     * 2.判斷目標檯是否有開台,如果沒有,則將原本數據更改檯號,并寫入LOG.
     * 3.如果已經開檯并未結賬,則提示合併,合併后需重新計算TRAN表的金額,以及把原始檯的明細,更改單號.
     *
     * @param formPosTran
     * @param toTableNum
     * @param stage
     * @return
     */
    public void handleTransfer(PosTran formPosTran, String toTableNum, Stage stage) {

        if (!formPosTran.getTableNum().equals(toTableNum)) {
            Wrapper<PosTable> posTableWrapper = new EntityWrapper<>();
            posTableWrapper.eq("OUTLET", Main.posOutlet);
            posTableWrapper.eq("ROOM_NUM", toTableNum);
            PosTable posTable = posTableService.selectOne(posTableWrapper);
            if (AppUtils.isBlank(posTable)) {
                Map<String, String> resultMap = new LinkedHashMap<>();
                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), Main.languageMap.get("global.tablenotfound"), resultMap, stage);
            } else {
                Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                posTranWrapper.eq("REF_NUM", formPosTran.getRefNum());
                posTranWrapper.eq("TRAN_TYPE", formPosTran.getTranType());
                posTranWrapper.eq("OUTLET", formPosTran.getOutlet());
                PosTran orgTran = posTranService.selectOne(posTranWrapper);
                //1.判斷原始檯是否結賬,如果結賬則提示已經結賬不能轉台
                if (AppUtils.isBlank(orgTran) || AppUtils.isNotBlank(orgTran.getSettled())) {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), orgTran.getTableNum() + Main.languageMap.get("global.orderalreadybillcannottran"), resultMap, stage);
                } else {
                    //判斷目標檯是否有結賬
                    Wrapper<PosTran> toPosTranWrapper = new EntityWrapper<>();
                    toPosTranWrapper.eq("TABLE_NUM", toTableNum);
                    toPosTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                    toPosTranWrapper.eq("OUTLET", formPosTran.getOutlet());
                    PosTran toTran = posTranService.selectOne(toPosTranWrapper);
                    if (AppUtils.isNotBlank(toTran) && AppUtils.isNotBlank(toTran.getSettled())) {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), toTran.getTableNum() + Main.languageMap.get("global.orderalreadybillcannottran"), resultMap, stage);
                    } else if (AppUtils.isBlank(toTran)) {
                        //2.判斷目標檯是否有開台,如果沒有,則將原本數據更改檯號,并寫入LOG.
                        try {
                            String orgTableNum = orgTran.getTableNum();
                            posTranService.transferToEmptyTable(orgTran, toTableNum);
                            //打印全單轉台廚房單
                            sendOrderController.sendOrders(orgTran, null, true, PrinterTypeEnums.F, orgTableNum, toTableNum, null, null, false);
                            // 打印上菜紙
                            PrintStyleUtils.printTicket(orgTran);
                            //发送netty刷新桌台
                            nettyComponent.sendMessage(NettyMessageTypeEnum.CHANGETABLE);
                            MainController mainController = (MainController) mainView.getPresenter();
                            mainController.iniData();
                            Main.showInitialView(mainView.getClass());
                        } catch (PosOrderException e) {
                            Map<String, String> resultMap = new LinkedHashMap<>();
                            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), e.getMessage(), resultMap, stage);
                        }
                    } else if (AppUtils.isNotBlank(toTran) && AppUtils.isBlank(toTran.getSettled())) {
                        //3.如果已經開檯并未結賬,則提示合併,合併后需重新計算TRAN表的金額,以及把原始檯的明細,更改單號.
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        resultMap.put(ResultEnum.NO.getName(), ResultEnum.NO.getValue());
                        String result = ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), toTran.getTableNum() + Main.languageMap.get("global.mergebill"), resultMap, stage);
                        if (result.equals(ResultEnum.YES.getValue())) {

                            try {
                                String orgTableNum = orgTran.getTableNum();
                                posTranService.transferToExistTable(orgTran, toTran);
                                //打印單品轉台廚房單
                                sendOrderController.sendOrders(toTran, null, true, PrinterTypeEnums.F, orgTableNum, toTableNum, null, null, false);
                                // 打印上菜紙
                                PrintStyleUtils.printTicket(toTran);
                                //发送netty刷新桌台
                                nettyComponent.sendMessage(NettyMessageTypeEnum.CHANGETABLE);
                                MainController mainController = (MainController) mainView.getPresenter();
                                mainController.iniData();
                                Main.showInitialView(mainView.getClass());
                            } catch (PosOrderException e) {
                                resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                                ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), e.getMessage(), resultMap, stage);
                            }
                        }
                    }
                    //todo 上云
                }
            }
        }
    }

    /**
     * 追單方法
     * 1.判斷是否結賬,如果已經結賬則提示不能追單
     * 2.追單菜品寫入LOG表,并送單
     *
     * @param posOrders
     * @param posTran
     * @param stage
     */
    public void handleUrge(ObservableList<TableViewDto> posOrders, PosTran posTran, Stage stage) {
        //1.判斷是否結賬,如果已經結賬則提示不能追單
        if (AppUtils.isBlank(posTran) || AppUtils.isNotBlank(posTran.getSettled())) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), posTran.getTableNum() + Main.languageMap.get("global.orderalreadybillcannottran"), resultMap, stage);
        } else {
            posOrders.forEach(posOrder -> {
                //判斷已選菜品裡面是否有未送單的食品
                if (null == posOrder.getId()) {
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), posOrder.getItemCode() + Main.languageMap.get("settle.unsend"), resultMap, stage);
                } else {
                    //2.追單菜品寫入LOG表,并送單
                    try {
                        PosLog posLog = this.posLogService.urgeOrder(posOrder);
                        posOrder.setUrgeCount(String.valueOf(posLog.getQty1()));
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), posTran.getTableNum() + Main.languageMap.get("global.guresuccess"), resultMap, stage);
                        //打印追單
                        sendOrderController.sendOrders(posTran, null, true, PrinterTypeEnums.U, posTran.getTableNum(), null, String.valueOf(posOrder.getItemIdx()), posLog.getId(), false);
                    } catch (PosOrderException e) {
                        Map<String, String> resultMap = new LinkedHashMap<>();
                        resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("global.prompt"), e.getMessage(), resultMap, stage);
                    }
                }
            });

        }

    }
}
