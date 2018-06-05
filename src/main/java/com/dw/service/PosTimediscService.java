package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.entity.*;
import com.dw.enums.AMTPROEnum;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PosOrderException;
import com.dw.mapper.*;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by li.yongliang on 2018/4/27.
 */
@Service
public class PosTimediscService extends ServiceImpl<PosTimediscMapper,PosTimedisc>{

    @Autowired
    private PosLogMapper posLogMapper;
    @Autowired
    private PosLogHisMapper posLogHisMapper;
    @Autowired
    private PosOrderMapper posOrderMapper;
    @Autowired
    private PosOrderHisMapper posOrderHisMapper;

    @Transactional(rollbackFor = PosOrderException.class)
    public BigDecimal advanceCategoryLeaveDisc(String outlet,PosTran posTran){
        String refNum = posTran.getRefNum();
        String subRef = posTran.getSubRef();
        String tableNum = posTran.getTableNum();
        BigDecimal[] leaveDisc = {BigDecimal.ZERO};
        List<PosTimedisc> posTimediscList = baseMapper.getDiscRulesList(outlet);
        try {
            if(AppUtils.isNotBlank(posTimediscList)){
                posTimediscList.forEach(ptd -> {
                    String amtPro = ptd.getAmtPro();
                    String category = ptd.getCategory();
                    BigDecimal singleLeaveDisc = BigDecimal.ZERO;
                    //先刪除記錄
                    Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                    posLogWrapper.eq("OUTLET", outlet);
                    posLogWrapper.eq("LOG_TYPE", LogTypeEnum.TDIS.getValue());
                    posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                    posLogWrapper.eq("REF_NUM",refNum);
                    posLogWrapper.eq("SUB_REF",subRef);
                    posLogWrapper.eq("REMARK3",category);
                    posLogMapper.delete(posLogWrapper);
                    //再插入提前離座折扣記錄
                    PosLog posLog = new PosLog();
                    posLog.setOutlet(outlet);
                    posLog.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(),"yyyy-MM-dd"));
                    posLog.setTTime(new Date());
                    posLog.setStaff(Main.posStaff.getCode());
                    posLog.setLogType(LogTypeEnum.TDIS.getValue());
                    posLog.setType(TranTypeEnum.N.getValue());
                    posLog.setRefNum(refNum);
                    posLog.setSubRef(subRef);
                    posLog.setTable1(tableNum);
                    posLog.setAmt1(ptd.getDiscCost());
                    posLog.setRemark1(ptd.getAdvanceTime());
                    posLog.setRemark2(ptd.getCode());
                    posLog.setRemark3(category);
                    posLog.setRemark4(amtPro);
                    if(amtPro.equals(AMTPROEnum.AMT.getValue())){
                        //按實際折扣，//存放實際折扣金額
                        singleLeaveDisc = DecimalUtil.multiply(new BigDecimal(posTran.getPerson()),ptd.getDiscCost());
                        posLog.setAmt2(singleLeaveDisc);
                    }else if(amtPro.equals(AMTPROEnum.PRO.getValue())){
                        List<PosOrder> posOrderList = posOrderMapper.getAmtByCategory(outlet,refNum,subRef,TranTypeEnum.N.getValue(),tableNum,category);
                        if(AppUtils.isNotBlank(posOrderList)){
                            BigDecimal amt = posOrderList.get(0).getAmt();
                            singleLeaveDisc = DecimalUtil.multiply(amt,ptd.getDiscCost());
                            posLog.setAmt2(singleLeaveDisc);
                        }else{
                            posLog.setAmt2(new BigDecimal(0.00));
                        }
                    }
                    posLogMapper.insert(posLog);
                    //判斷每一個提前離座折扣是按照百分比折扣還是按照金額折扣，然後算出來折扣金額。
                    leaveDisc[0] = DecimalUtil.add(leaveDisc[0],singleLeaveDisc);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return leaveDisc[0];
    }


    @Transactional(rollbackFor = PosOrderException.class)
    public BigDecimal advanceCategoryLeaveDiscHis(String outlet,PosTran posTran){
        String refNum = posTran.getRefNum();
        String subRef = posTran.getSubRef();
        String tableNum = posTran.getTableNum();
        BigDecimal[] leaveDisc = {BigDecimal.ZERO};
        List<PosTimedisc> posTimediscList = baseMapper.getDiscRulesList(outlet);
        try {
            if(AppUtils.isNotBlank(posTimediscList)){
                posTimediscList.forEach(ptd -> {
                    String amtPro = ptd.getAmtPro();
                    String category = ptd.getCategory();
                    BigDecimal singleLeaveDisc = BigDecimal.ZERO;
                    //先刪除記錄
                    Wrapper<PosLogHis> posLogHisWrapper = new EntityWrapper<>();
                    posLogHisWrapper.eq("OUTLET", outlet);
                    posLogHisWrapper.eq("LOG_TYPE", LogTypeEnum.TDIS.getValue());
                    posLogHisWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                    posLogHisWrapper.eq("REF_NUM",refNum);
                    posLogHisWrapper.eq("SUB_REF",subRef);
                    posLogHisWrapper.eq("REMARK3",category);
                    posLogHisMapper.delete(posLogHisWrapper);
                    //再插入提前離座折扣記錄
                    PosLogHis posLogHis = new PosLogHis();
                    posLogHis.setOutlet(outlet);
                    posLogHis.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(),"yyyy-MM-dd"));
                    posLogHis.setTTime(new Date());
                    posLogHis.setStaff(Main.posStaff.getCode());
                    posLogHis.setLogType(LogTypeEnum.TDIS.getValue());
                    posLogHis.setType(TranTypeEnum.N.getValue());
                    posLogHis.setRefNum(refNum);
                    posLogHis.setSubRef(subRef);
                    posLogHis.setTable1(tableNum);
                    posLogHis.setAmt1(ptd.getDiscCost());
                    posLogHis.setRemark1(ptd.getAdvanceTime());
                    posLogHis.setRemark2(ptd.getCode());
                    posLogHis.setRemark3(category);
                    posLogHis.setRemark4(amtPro);
                    if(amtPro.equals(AMTPROEnum.AMT.getValue())){
                        //按實際折扣，//存放實際折扣金額
                        singleLeaveDisc = DecimalUtil.multiply(new BigDecimal(posTran.getPerson()),ptd.getDiscCost());
                        posLogHis.setAmt2(singleLeaveDisc);
                    }else if(amtPro.equals(AMTPROEnum.PRO.getValue())){
                        List<PosOrderHis> posOrderHisList = posOrderHisMapper.getAmtByCategory(outlet,refNum,subRef,TranTypeEnum.N.getValue(),tableNum,category);
                        if(AppUtils.isNotBlank(posOrderHisList)){
                            BigDecimal amt = posOrderHisList.get(0).getAmt();
                            singleLeaveDisc = DecimalUtil.multiply(amt,ptd.getDiscCost());
                            posLogHis.setAmt2(singleLeaveDisc);
                        }else{
                            posLogHis.setAmt2(new BigDecimal(0.00));
                        }
                    }
                    posLogHisMapper.insert(posLogHis);
                    //判斷每一個提前離座折扣是按照百分比折扣還是按照金額折扣，然後算出來折扣金額。
                    leaveDisc[0] = DecimalUtil.add(leaveDisc[0],singleLeaveDisc);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return leaveDisc[0];
    }
}