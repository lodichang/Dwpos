package com.dw.component;

import com.dw.Main;
import com.dw.controller.MainController;
import com.dw.dto.*;
import com.dw.entity.PosTran;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TableStateEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.netty.NettyClient;
import com.dw.service.PosLogService;
import com.dw.service.PosOrderService;
import com.dw.service.PosTAttService;
import com.dw.service.PosTranService;
import com.dw.util.*;
import com.dw.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UpdateTablePersonComponent {
    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private MainView mainView;
    private PosTableDto posTableDto;
    private PosTran posTran;
    private int persons = 0;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTAttService posTAttService;
    @Autowired
    private NettyComponent nettyUtil;

    public UpdateTablePersonComponent() {

    }

    /**
     * 1.判斷是否可以修改入座人數：未開臺-false，開臺未點菜-true，開臺已點菜-true，已付款未離座-false
     * 2.判斷是否有點菜，如果有點菜，則只能修改入座人數，且入座人數不能小於0
     * 3.判斷如果沒有點菜記錄，則可以直接把入座人數改為0，清空桌台，注意：清空桌台需要刪除tran表和log表
     */
    public void init(PosTableDto posTableDto) {
        this.posTableDto = posTableDto;
        if (posTableDto.getTableState().equals(TableStateEnum.OFFTHETABLE.getValue()) || posTableDto.getTableState().equals(TableStateEnum.OPENTABLEORDER.getValue())) {
            List<PosTran> posTranDtos = posTranService.queryListByTable(posTableDto.getRoomNum(), TranTypeEnum.N.getValue());
            if (AppUtils.isNotBlank(posTranDtos)) {
                posTran = posTranDtos.get(0);
                persons = posTran.getPerson();
                // 檢查是否有點菜
                List<OrderListDto> orderList = posOrderService.getOrderList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), TranTypeEnum.N.getValue());
                // 更改人數界面
                String result = ShowViewUtil.showNumbericKeyboard(Main.getStage(), Main.languageMap.get("tran.input")+Main.languageMap.get("global.person"), persons + "", true);
                if (!ResultEnum.NO.getValue().equals(result) && AppUtils.isNotBlank(result)) {
                    persons = Integer.parseInt(result.trim()); // 獲取修改人數
                    Map<String, String> resultMap = new LinkedHashMap<>();
                    resultMap.put(Main.languageMap.get("global.close"), ResultEnum.YES.getValue());
                    if (persons == 0) {
                        if (AppUtils.isNotBlank(orderList) && orderList.size() > 0) { // 已點菜，不能修改人數
                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("table.already.order") + Main.languageMap.get("table.update.person.notallow"), resultMap, null);
                        } else {
                            // 未點菜或者點菜全部取消，清空台
                            posLogService.deleteByRefNum(posTran.getOutlet(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum());
                            //清空點菜記錄對應的口味
                            posTAttService.deleteByOrderRefNum(posTran.getOutlet(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum());
                            //清空點菜記錄
                            posOrderService.deleteByRefNum(posTran.getOutlet(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum());
                            //清空tran表
                            posTranService.deleteById(posTran.getId());
                        }
                    } else { // 直接修改人數
                        posTranService.updatePersons(posTran, persons + "");
                    }
                    // 關閉並刷新首頁桌台狀態
                    MainController mainController = (MainController) mainView.getPresenter();
                    Main.showInitialView(mainView.getClass());
                    mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
                    //  netty通知其他POS端刷新
                    nettyUtil.sendMessage(NettyMessageTypeEnum.CHANGEPERSON);
                }
            }
        } else {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(Main.languageMap.get("global.close"), ResultEnum.YES.getValue());
            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), TableStateEnum.getNameByValue(posTableDto.getTableState()) + Main.languageMap.get("table.update.person.notallow"), resultMap, null);
            if (ResultEnum.YES.getValue().equals(result)) {
                MainController mainController = (MainController) mainView.getPresenter();
                Main.showInitialView(mainView.getClass());
                mainController.refreshTables(mainController.getCurrentTablesPage(), mainController.getCurrentArea());
            }
        }
    }
}
