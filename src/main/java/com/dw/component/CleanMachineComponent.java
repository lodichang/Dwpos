package com.dw.component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.controller.BillsController;
import com.dw.dto.PosSettingDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.ShowViewUtil;
import com.dw.view.BillsView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.util.calendar.BaseCalendar;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by li.yongliang on 2018/5/18.
 */
@Getter
@Setter
@Component
public class CleanMachineComponent {


    @Autowired
    private BillsView billsView;
    @Autowired
    private BillsController billsController;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private PosGenphService posGenphService;
    @Autowired
    private PosGeneralService posGeneralService;
    @Autowired
    private PosDayeLogService posDayeLogService;
    @Autowired
    private PosSettingService posSettingService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosLogHisService posLogHisService;
    @Autowired
    private PosPayService posPayService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosTAttHisService posTAttHisService;

    /**
     * 清機方法
     */
    public int cleanMachine() {

        //判斷能不能清機
        if (!canClear()) {
            return -1;
            //return false;
        }

        //如果能清機開始清機操作。
        //得到當前日期和時間
        String nowDate = DateUtil.getCurrTimeYmd();
        String nowTime = DateUtil.DateToString(new Date(), DateUtil.TIME_FORMAT);
        //獲取上一營業結束時間
        Wrapper<PosGenph> posGenphWrapper = new EntityWrapper<>();
        posGenphWrapper.eq("OUTLET", Main.posOutlet);
        posGenphWrapper.eq("G_DATE", "DATE_ADD('" + nowDate + "',INTERVAL -1 DAY)");
        PosGenph posGenph = posGenphService.selectOne(posGenphWrapper);
        String dayendTime = "";
        if (AppUtils.isNotBlank(posGenph)) {
            dayendTime = DateUtil.DateToString(posGenph.getDayEnd(), DateUtil.TIME_FORMAT);
        } else {
            Wrapper<PosGeneral> posGeneralWrapper = new EntityWrapper<>();
            posGeneralWrapper.eq("OUTLET", Main.posOutlet);
            posGeneralWrapper.eq("G_COUNT", "0" + DateUtil.getWeek(DateUtil.getDay(DateUtil.StringToDate(nowDate, DateUtil.CM_DATE_FORMAT), -1)));
            PosGeneral posGeneral = posGeneralService.selectOne(posGeneralWrapper);
            if (AppUtils.isNotBlank(posGeneral)) {
                dayendTime = DateUtil.DateToString(posGeneral.getDayEnd(), DateUtil.TIME_FORMAT);
            } else {
                dayendTime = "04:00:00";
            }
        }
        //得到清機日期
        String today_date = "";
        //根據當前時間和營業結束時間比對，dayendTime大於等於當前時間,證明是上一天的營業時間,
        if (DateUtil.DatetimeCompare(DateUtil.StringToDate(dayendTime, DateUtil.TIME_FORMAT), DateUtil.StringToDate(nowTime, DateUtil.TIME_FORMAT), DateUtil.TIME_FORMAT)) {
            today_date = DateUtil.DateToString(DateUtil.getDay(DateUtil.StringToDate(nowDate, DateUtil.CM_DATE_FORMAT), -1), DateUtil.CM_DATE_FORMAT);
        } else {
            today_date = nowDate;
        }

        //根據清機日期，獲取當天營業結束時間
        String todayDayendTime = "";
        Wrapper<PosGenph> posGenphEndTimeWrapper = new EntityWrapper<>();
        posGenphEndTimeWrapper.eq("OUTLET", Main.posOutlet);
        posGenphEndTimeWrapper.eq("G_DATE", today_date);
        PosGenph posGenphEndTime = posGenphService.selectOne(posGenphEndTimeWrapper);
        if (AppUtils.isNotBlank(posGenphEndTime)) {
            todayDayendTime = DateUtil.DateToString(posGenphEndTime.getDayEnd(), DateUtil.TIME_FORMAT);
        } else {
            Wrapper<PosGeneral> posGeneralEndTimeWrapper = new EntityWrapper<>();
            posGeneralEndTimeWrapper.eq("OUTLET", Main.posOutlet);
            posGeneralEndTimeWrapper.eq("G_COUNT", "0" + DateUtil.getWeek(DateUtil.StringToDate(today_date, DateUtil.CM_DATE_FORMAT)));
            PosGeneral posGeneral = posGeneralService.selectOne(posGeneralEndTimeWrapper);
            if (AppUtils.isNotBlank(posGeneral)) {
                todayDayendTime = DateUtil.DateToString(posGeneral.getDayEnd(), DateUtil.TIME_FORMAT);
            } else {
                todayDayendTime = "04:00:00";
            }
        }

        //把之前沒有執行成功的情機操作再執行一次
        Wrapper<PosDayeLog> posDayeLogWrapper = new EntityWrapper<>();
        posDayeLogWrapper.eq("STAT", "0");
        List<PosDayeLog> posDayeLogList = posDayeLogService.selectList(posDayeLogWrapper);
        for(int i=0;i<posDayeLogList.size();i++){
            //調用清機方法
            try {
                posTranHisService.dayend(posDayeLogList.get(i).getCleandate());

            }catch (Exception e){
                e.printStackTrace();
                return -2;
            }
        }

        //得到最後一次的清機日期
        Date lastCleanDate;
        Wrapper<PosDayeLog> posDayeLogYesterDayWrapper = new EntityWrapper<>();
        posDayeLogYesterDayWrapper.orderBy("SEQ", false);
        PosDayeLog posDayeLog = posDayeLogService.selectOne(posDayeLogYesterDayWrapper);
        if (AppUtils.isNotBlank(posDayeLog)) {
            lastCleanDate = DateUtil.getDay(posDayeLog.getCleandate(), 1);
        } else {
            //如果沒有清機記錄根據最早入單日期先插入一條
            Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
            posTranHisWrapper.where("ifnull(MEMBER,'')=''");
            posTranHisWrapper.orderBy("IN_DATE", true);
            PosTranHis posTranHis = posTranHisService.selectOne(posTranHisWrapper);
            if (AppUtils.isNotBlank(posTranHis)) {
                lastCleanDate = DateUtil.getDay(posTranHis.getInDate(), -1);
            } else {
                lastCleanDate = DateUtil.StringToDate(today_date, DateUtil.CM_DATE_FORMAT);
            }
            PosDayeLog newPosDayeLog = new PosDayeLog();
            newPosDayeLog.setSeq("0");
            newPosDayeLog.setCleandate(lastCleanDate);
            newPosDayeLog.setCleantime(DateUtil.StringToDate("00:00:00", DateUtil.TIME_FORMAT));
            newPosDayeLog.setStat("1");
            posDayeLogService.insert(newPosDayeLog);
        }

        //循環清機，把之前沒有清的都清一遍
        while (DateUtil.dateCompare(DateUtil.StringToDate(today_date, DateUtil.CM_DATE_FORMAT), lastCleanDate)) {
            Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
            posTranHisWrapper.where("date_format(concat(IN_DATE,' ',IN_TIME),'%Y-%m-%d %H:%i:%s') >date_format(concat('" + today_date + "',' ','" + dayendTime + "'),'%Y-%m-%d %H:%i:%s')");
            posTranHisWrapper.and("date_format(concat(IN_DATE,' ',IN_TIME),'%Y-%m-%d %H:%i:%s') <=date_format(concat(date_add('" + today_date + "',INTERVAL 1 DAY),' ','" + todayDayendTime + "'),'%Y-%m-%d %H:%i:%s')");
            List<PosTranHis> posTranHisList = posTranHisService.selectList(posTranHisWrapper);

            if (DateUtil.StringToDate(today_date, DateUtil.CM_DATE_FORMAT).compareTo(lastCleanDate) == 0 && AppUtils.isBlank(posTranHisList)) {
                System.out.println("當天不執行清機操作");
            } else {
                //執行清機
                try {
                    posTranHisService.dayend(lastCleanDate);

                }catch (Exception e){
                    e.printStackTrace();
                    return -3;
                }
            }
            //日期加1
            lastCleanDate = DateUtil.getDay(lastCleanDate, 1);
        }
        //以下清除臨時表數據
        return 1;
    }

    public boolean canClear() {
        //先检查今天有没有清机
        if (hasClear()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("alreadyclean.cannotclean"), resultMap, null);
            return false;
        }
        //查詢有沒有未結賬的賬單或者結賬沒拍腳的賬單??????
        if (hasUnSettle()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("clean.unpayorunsettle"), resultMap, null);
            return false;
        }
        //查詢有沒有改單未拍腳的賬單
        if (hasUnPay()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("clean.updateorderunpay"), resultMap, null);
            return false;
        }
        //最后检查是否有数据还没转更
        if (hasUnChange()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("clean.unturnmore"), resultMap, null);
            return false;
        }
        //是否存在没有付款记录的单子
        if (checkPaymentRecords()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("clean.payrecordsnofound"), resultMap, null);
            return false;
        }
        return true;
    }

    //檢查今天有沒有清機
    private boolean hasClear() {
        int diffDays = posDayeLogService.getDiffDays();
        //如果查到已经清机
        if (diffDays > 0) {
            return true;
        }
        return false;
    }

    //查詢有沒有未結賬的賬單或者結賬沒拍腳的賬單
    private boolean hasUnSettle() {
        Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
        posTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
        List<PosTran> posTranList = posTranService.selectList(posTranWrapper);

        if (AppUtils.isNotBlank(posTranList)) {
            return true;
        }
        return false;
    }

    //查詢有沒有改單未拍腳的賬單
    private boolean hasUnPay() {
        Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
        posTranHisWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
        posTranHisWrapper.where("ifnull(settled,'')='false'");
        List<PosTranHis> posTranHisList = posTranHisService.selectList(posTranHisWrapper);
        if (AppUtils.isNotBlank(posTranHisList)) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否有未转更的账单
     *
     * @return
     */
    private boolean hasUnChange() {
        Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
        posTranHisWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
        posTranHisWrapper.where("ifnull(PERIOD,0) <='0'");
        List<PosTranHis> posTranHisList = posTranHisService.selectList(posTranHisWrapper);
        if (AppUtils.isNotBlank(posTranHisList)) {
            return true;
        }
        return false;
    }

    /**
     * 验证付款记录是否丢失
     *
     * @return 验证结果
     */
    private boolean checkPaymentRecords() {
        List<PosTranHis> posTranHisList = posTranHisService.checkPaymentRecords();
        if (AppUtils.isNotBlank(posTranHisList)) {
            PosTranHis posTranHis = posTranHisList.get(0);
            String tableNum = posTranHis.getTableNum();
            String refNum = posTranHis.getRefNum();
            List<Integer> indexs = new ArrayList();
            //1.先查找
            if (indexs.size() == 0) {
                //1.1.取得单号长度
                int refNumLength = Integer.parseInt(Main.posSetting.get("refnum_length"));
                for (int i = 0; i < billsController.getBillsView().getItems().size(); i++) {
                    if (refNum.length() >= refNumLength && billsController.getBillsView().getItems().get(i).getRefNumShow().contains(refNum)) {
                        indexs.add(i);
                    } else if (tableNum.length() < refNumLength && billsController.getBillsView().getItems().get(i).getTableNum().contains(tableNum)) {
                        indexs.add(i);
                    }
                }
            }
            //2.检索下一个
            if (indexs.size() > 0) {
                billsController.getBillsView().getSelectionModel().select(indexs.get(0));
                billsController.getBillsView().scrollTo(indexs.get(0) - 5);
                indexs.remove(0);
            }
            return true;
        }
        return false;
    }


    /*****************************************************轉更*********************************************************************/
    public int turnMore() {
        int result = canTurnMore();
        if(result>=0){
            return result;
        }
        //更新相對應表的period字段，并插入轉更日誌
        if (posTranHisService.updateTurnData()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("turnmore.dataerror"), resultMap, null);
            return 2;
        }
        return 3;
    }

    public int canTurnMore(){
        int result = -1;
        //判斷有沒有記錄
        if (checkTranRecords()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("turnmore.norecords"), resultMap, null);
            result = 0;
        }
        //检查是否有未结账的单子或者有没有付款记录单子
        if (hasUnPay() || checkPaymentRecords()) {
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put(ResultEnum.YES.getName(), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("turnmore.unpaycannotturnmore"), resultMap, null);
            result = 1;
        }
        return result;
    }

    private boolean checkTranRecords() {
        //判斷有沒有記錄
        Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
        posTranHisWrapper.where("ifnull(PERIOD,0) <='0'");
        List<PosTranHis> posTranHisList = posTranHisService.selectList(posTranHisWrapper);
        if (AppUtils.isBlank(posTranHisList)) {
            return true;
        }
        return false;
    }


}
