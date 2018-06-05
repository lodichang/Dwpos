package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.PosSettingDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.mapper.PosTranHisMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PosTranHisService extends ServiceImpl<PosTranHisMapper, PosTranHis> {

    @Autowired
    private PosSettingService posSettingService;
    @Autowired
    private PosPayService posPayService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosLogHisService posLogHisService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosTAttHisService posTAttHisService;
    @Autowired
    private PosDayeLogService posDayeLogService;
    @Autowired
    private PosGeneralService posGeneralService;
    @Autowired
    private PosGenphService posGenphService;
    @Autowired
    private PosPrinterTaskService posPrinterTaskService;
    @Autowired
    private PosPrinterTaskDetailService posPrinterTaskDetailService;

    public List<PosTranHis> queryListByTable(String tableNo, String tranType) {
        return baseMapper.queryListByTable(tableNo, tranType);
    }

    public PosTranHis getOnePosTranHis(String outlet, String refNum, String subRef, String tranType) {
        return baseMapper.getOnePosTranHis(outlet, refNum, subRef, tranType);
    }

    public String getOrderIndex() {
        String orderIndex = null;
        try {

            // 查詢賬單編號
            PosSettingDto settingDto = posSettingService.queryOrderIndex();
            orderIndex = settingDto.getPosValue();
            // 更新賬單編號
            posSettingService.updateSetting(settingDto.getId(), String.valueOf(Integer.parseInt(settingDto.getPosValue()) + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderIndex;
    }


    public List<PosTranHis> checkPaymentRecords() {
        return baseMapper.checkPaymentRecords();
    }


    /**
     * 轉移數據到clean表
     */
    @Transactional(rollbackFor = Exception.class)
    public void moveDataToClean(String cleanDate, int dayCount) {
        //清除打印日誌
        Wrapper<PosPrinterTask> posPrinterTaskWrapper = new EntityWrapper<>();
        posPrinterTaskService.delete(posPrinterTaskWrapper);

        Wrapper<PosPrinterTaskDetail> posPrinterTaskDetailWrapper = new EntityWrapper<>();
        posPrinterTaskDetailService.delete(posPrinterTaskDetailWrapper);

        //轉移其他數據
        cleanDate = cleanDate.replaceAll("-", "");
        posPayService.updatePay(cleanDate, dayCount);
        posLogHisService.updateLogHis(cleanDate, dayCount);
        posOrderHisService.updateOrderHis(cleanDate, dayCount);
        baseMapper.updateTranHis(cleanDate, dayCount);


        posTAttHisService.insertAttClean(dayCount);
        posOrderHisService.insertOrderClean(dayCount);
        posLogHisService.insertLogClean(dayCount);
        posPayService.insertPayClean(dayCount);
        baseMapper.insertTranClean(dayCount);


        posTAttHisService.deleteAttHis(dayCount);
        posOrderHisService.deleteOrderHis(dayCount);
        posLogHisService.deleteLogHis(dayCount);
        posPayService.deletePay(dayCount);
        baseMapper.deleteTranHis(dayCount);




    }


    /*
    public void updateTranHis(String cleanDate,int dayCount){
        baseMapper.updateTranHis(cleanDate,dayCount);
    }

    public void insertTranClean(int dayCount){
        baseMapper.insertTranClean(dayCount);
    }

    public void deleteTranHis(int dayCount){
        baseMapper.deleteTranHis(dayCount);
    }
    */
    public void updateTranHisPeriod(int period){
        baseMapper.updateTranHisPeriod(period);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateTurnData() {
        //查询当前更次的总销售额
        Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
        posTranHisWrapper.eq("ifnull(PERIOD,'0')", "0");
        posTranHisWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
        List<PosTranHis> posTranHisList = baseMapper.selectList(posTranHisWrapper);
        BigDecimal billAmt = new BigDecimal(posTranHisList.stream().mapToDouble(posTranHis -> posTranHis.getBillAmt().doubleValue()).sum());

        //查询本次更次
        int period = 1;
        PosSettingDto posSettingDto = posSettingService.queryPeriodSeq();
        if (AppUtils.isNotBlank(posSettingDto)) {
            period = Integer.parseInt(posSettingDto.getPosValue());
            //修改基础更次表
            posSettingService.updateSetting(posSettingDto.getId(), String.valueOf(period + 1));
        }
        /*
            开始设置更次
            orderHis
            tranHis
        */
        posOrderHisService.updateOrderHisPeriod(period);

        baseMapper.updateTranHisPeriod(period);

        //记录日志
        PosLogHis posLogHis = new PosLogHis();
        posLogHis.setOutlet(Main.posOutlet);
        posLogHis.setTDate(new Date());
        posLogHis.setTTime(new Date());
        posLogHis.setStaff(Main.posStaff.getCode());
        posLogHis.setLogType(LogTypeEnum.MDAY.getValue());
        posLogHis.setType(TranTypeEnum.N.getValue());
        posLogHis.setQty1(period);
        posLogHis.setAmt1(billAmt);
        posLogHisService.insert(posLogHis);

        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean dayend(Date thisTimeCleanDate) {
        boolean cleanFlag = false;

        String startTime = "";
        String endTime = "";

        //得到最後一次清機的日期時間
        Wrapper<PosDayeLog> posDayeLogWrapper = new EntityWrapper<>();
        posDayeLogWrapper.orderBy("SEQ", false);
        PosDayeLog posDayeLog = posDayeLogService.selectOne(posDayeLogWrapper);
        if (AppUtils.isNotBlank(posDayeLog)) {
            startTime = DateUtil.DateToString(posDayeLog.getCleandate(), DateUtil.CM_DATE_FORMAT) + " " + DateUtil.DateToString(posDayeLog.getCleantime(), DateUtil.TIME_FORMAT);
        }

        //根據傳進來的清機日期獲取這個日期營業結束時間
        Wrapper<PosGenph> posGenphWrapper = new EntityWrapper<>();
        posGenphWrapper.eq("OUTLET", Main.posOutlet);
        posGenphWrapper.eq("G_DATE", DateUtil.DateToString(thisTimeCleanDate, DateUtil.CM_DATE_FORMAT));
        PosGenph posGenph = posGenphService.selectOne(posGenphWrapper);
        if (AppUtils.isNotBlank(posGenph)) {
            endTime = DateUtil.DateToString(DateUtil.getDay(thisTimeCleanDate, 1), DateUtil.CM_DATE_FORMAT) + " " + DateUtil.DateToString(posGenph.getDayEnd(), DateUtil.TIME_FORMAT);
        } else {
            Wrapper<PosGeneral> posGeneralWrapper = new EntityWrapper<>();
            posGeneralWrapper.eq("OUTLET", Main.posOutlet);
            posGeneralWrapper.eq("G_COUNT", "0" + DateUtil.getWeek(thisTimeCleanDate));
            PosGeneral posGeneral = posGeneralService.selectOne(posGeneralWrapper);
            if (AppUtils.isNotBlank(posGeneral)) {
                endTime = DateUtil.DateToString(DateUtil.getDay(thisTimeCleanDate, 1), DateUtil.CM_DATE_FORMAT) + " " + DateUtil.DateToString(posGeneral.getDayEnd(), DateUtil.TIME_FORMAT);
            }
        }

        //得到前一天的清機序號
        int dayCount = 1;
        PosSettingDto posSettingDto = posSettingService.queryCleanSeq();
        if (AppUtils.isNotBlank(posSettingDto)) {
            dayCount = Integer.parseInt(posSettingDto.getPosValue()) + 1;
            //清機序號+1
            posSettingService.updateSetting(posSettingDto.getId(), String.valueOf(dayCount));
        }
        //更新相應數據
        boolean updateFlag = updateCleanData(startTime, endTime, dayCount);
        if (updateFlag) {
            //判斷當前清機序號是否存在，存在更新，不存在插入一條情機記錄
            Wrapper<PosDayeLog> posDayeLogWrapper1 = new EntityWrapper<>();
            posDayeLogWrapper1.eq("CLEANDATE", thisTimeCleanDate);
            PosDayeLog posDayeLog1 = posDayeLogService.selectOne(posDayeLogWrapper1);
            if (AppUtils.isNotBlank(posDayeLog1)) {
                posDayeLog1.setStat("1");
                posDayeLogService.updateById(posDayeLog1);
            } else {
                PosDayeLog posDayeLog2 = new PosDayeLog();
                posDayeLog2.setSeq(String.valueOf(dayCount));
                posDayeLog2.setCleandate(thisTimeCleanDate);
                posDayeLog2.setCleantime(DateUtil.StringToDate(DateUtil.DateToString(new Date(), DateUtil.TIME_FORMAT), DateUtil.TIME_FORMAT));
                posDayeLog2.setStat("1");
                posDayeLog2.setLastUpdateNameId(Main.posStaff.getCode());
                posDayeLogService.insert(posDayeLog2);
            }
        } else {
            //判斷當前清機序號是否存在，存在更新，不存在插入一條情機記錄
            Wrapper<PosDayeLog> posDayeLogWrapper1 = new EntityWrapper<>();
            posDayeLogWrapper1.eq("CLEANDATE", thisTimeCleanDate);
            PosDayeLog posDayeLog1 = posDayeLogService.selectOne(posDayeLogWrapper1);
            if (AppUtils.isBlank(posDayeLog1)) {
                PosDayeLog posDayeLog2 = new PosDayeLog();
                posDayeLog2.setSeq(String.valueOf(dayCount));
                posDayeLog2.setCleandate(thisTimeCleanDate);
                posDayeLog2.setCleantime(DateUtil.StringToDate(DateUtil.DateToString(new Date(), DateUtil.TIME_FORMAT), DateUtil.TIME_FORMAT));
                posDayeLog2.setStat("0");
                posDayeLog2.setLastUpdateNameId(Main.posStaff.getCode());
                posDayeLogService.insert(posDayeLog2);
            }
        }

        //清機完成以後把更次記錄中更次重置為1
        PosSettingDto posSetting = posSettingService.queryPeriodSeq();
        if (AppUtils.isNotBlank(posSetting)) {
            //修改基础更次表
            posSettingService.updateSetting(posSetting.getId(), "1");
        }

        //轉移數據到clean表
        this.moveDataToClean(DateUtil.DateToString(thisTimeCleanDate, DateUtil.CM_DATE_FORMAT), dayCount);

        return true;
    }


    /**
     * 更新情機中的數據
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean updateCleanData(String startTime, String endTime, int dayCount) {
        try {
            //更新log表清機序號
            Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
            posLogWrapper.where("IFNULL(MEMBER,'') = '' ");
            posLogWrapper.and("date_format(concat(T_DATE,' ',T_TIME),'%Y-%m-%d %H:%i:%s') >date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')");
            posLogWrapper.and("date_format(concat(T_DATE,' ',T_TIME),'%Y-%m-%d %H:%i:%s') <=date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");
            PosLog posLog = new PosLog();
            posLog.setMember(String.valueOf(dayCount));
            posLogService.update(posLog, posLogWrapper);

            //更新loghis表的清機序號
            Wrapper<PosLogHis> posLogHisWrapper = new EntityWrapper<>();
            posLogHisWrapper.where("IFNULL(MEMBER,'') = '' ");
            posLogHisWrapper.and("date_format(concat(T_DATE,' ',T_TIME),'%Y-%m-%d %H:%i:%s') >date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')");
            posLogHisWrapper.and("date_format(concat(T_DATE,' ',T_TIME),'%Y-%m-%d %H:%i:%s') <=date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");
            PosLogHis posLogHis = new PosLogHis();
            posLogHis.setMember(String.valueOf(dayCount));
            posLogHisService.update(posLogHis, posLogHisWrapper);

            //更新tranHis表的清機序號
            Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
            posTranHisWrapper.where("IFNULL(MEMBER,'') = '' ");
            posTranHisWrapper.where("date_format(concat(IN_DATE,' ',IN_TIME),'%Y-%m-%d %H:%i:%s') >date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')");
            posTranHisWrapper.and("date_format(concat(IN_DATE,' ',IN_TIME),'%Y-%m-%d %H:%i:%s') <=date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");
            PosTranHis posTranHis = new PosTranHis();
            posTranHis.setMember(String.valueOf(dayCount));
            posTranHis.setLastUpdateNameId(Main.posStaff.getCode());
            posTranHis.setLastUpdateTime(new Date());
            baseMapper.update(posTranHis, posTranHisWrapper);

            //把log表數據轉移到logHis表
            posLogHisService.insertPosLogHis(dayCount);
            //刪掉log表中數據
            Wrapper<PosLog> posLogWrapper1 = new EntityWrapper<>();
            posLogWrapper1.eq("MEMBER", dayCount);
            posLogService.delete(posLogWrapper1);

            //插入清機日誌
            PosLogHis insertLog = new PosLogHis();
            insertLog.setOutlet(Main.posOutlet);
            insertLog.setTDate(new Date());
            insertLog.setTTime(new Date());
            insertLog.setStaff(Main.posStaff.getCode());
            insertLog.setLogType(LogTypeEnum.DAYE.getValue());
            insertLog.setType(TranTypeEnum.N.getValue());
            insertLog.setMember(String.valueOf(dayCount));
            insertLog.setRemark3(String.valueOf(dayCount));
            posLogHisService.insert(insertLog);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
