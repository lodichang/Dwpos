package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.CouponDto;
import com.dw.dto.PosLogDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.mapper.MemPeriodMapper;
import com.dw.mapper.PosLogHisMapper;
import com.dw.mapper.PosLogMapper;
import com.dw.mapper.PosOrderHisMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PosLogHisService extends ServiceImpl<PosLogHisMapper, PosLogHis> {


    @Autowired
    private MemPeriodMapper memPeriodMapper;
    @Autowired
    private PosOrderHisMapper posOrderHisMapper;

    public List<CouponDto> getCouponData(String outlet, String refNum, String subRef, String indate, String tableNo, String discType, String cardId) {
        return baseMapper.getCouponData(outlet, refNum, subRef, indate, tableNo, discType, cardId);
    }

    /**
     * @param serviceType         服務費類型，TRUE先算+1服務費，然後算其他折扣。FALSE先算折扣，然後算+1服務費。
     * @param allDayServiceCharge 是否全天收取服務費
     * @param servPro             服務費費率
     * @param posTran             tran表的一些信息，主要用到了refNum，subRef，tableNum，Order_AMT(賬單總金額）
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal insertLogCalculateService(boolean serviceType, boolean allDayServiceCharge, String servPro, PosTran posTran) {
        final BigDecimal[] service = {BigDecimal.ZERO};
        if (!allDayServiceCharge) {
            List<MemPeriod> memPeriodList = memPeriodMapper.getServicePeriodList(DateUtil.DateToString(posTran.getInTime(), "HH:mm"));
            if (AppUtils.isNotBlank(memPeriodList)) {
                allDayServiceCharge = true;
            }
        }
        if (allDayServiceCharge) {
            try {
                //先刪除存在的服務費記錄
                Wrapper<PosLogHis> posLogHisWrapper = new EntityWrapper<>();
                posLogHisWrapper.eq("OUTLET", Main.posOutlet);
                posLogHisWrapper.eq("LOG_TYPE", LogTypeEnum.DSRV.getValue());
                posLogHisWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                posLogHisWrapper.eq("REF_NUM", posTran.getRefNum());
                posLogHisWrapper.eq("SUB_REF", posTran.getSubRef());
                baseMapper.delete(posLogHisWrapper);
                //再插入服務費記錄
                PosLogHis posLogHis = new PosLogHis();
                posLogHis.setOutlet(Main.posOutlet);
                posLogHis.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
                posLogHis.setTTime(new Date());
                posLogHis.setStaff(Main.posStaff.getCode());
                posLogHis.setLogType(LogTypeEnum.DSRV.getValue());
                posLogHis.setType(TranTypeEnum.N.getValue());
                posLogHis.setRefNum(posTran.getRefNum());
                posLogHis.setSubRef(posTran.getSubRef());
                posLogHis.setTable1(posTran.getTableNum());
                posLogHis.setAmt1(new BigDecimal(1.00));
                posLogHis.setAmt2(new BigDecimal(servPro));

                //根據服務費收費費率計算每個商品的服務費。
                Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
                posOrderHisWrapper.eq("OUTLET", Main.posOutlet);
                posOrderHisWrapper.eq("REF_NUM", posTran.getRefNum());
                posOrderHisWrapper.eq("SUB_REF", posTran.getSubRef());
                posOrderHisWrapper.eq("TYPE", posTran.getTranType());
                posOrderHisWrapper.eq("SERVICE", "TRUE");
                posOrderHisWrapper.gt("QTY+CANCEL+FREE", 0);
                List<PosOrderHis> posOrders = posOrderHisMapper.selectList(posOrderHisWrapper);
                if (serviceType) {
                    posOrders.forEach(po -> {
                        BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(servPro), po.getAmt()), new BigDecimal(100)).doubleValue());
                        po.setServCost(servCost);
                        posOrderHisMapper.updateById(po);
                        service[0] = DecimalUtil.add(service[0], servCost);
                    });
                } else {
                    posOrders.forEach(po -> {
                        BigDecimal itemOrderAmt = DecimalUtil.subtract(po.getAmt(), po.getOrderDisc());
                        BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(servPro), itemOrderAmt), new BigDecimal(100)).doubleValue());
                        po.setServCost(servCost);
                        posOrderHisMapper.updateById(po);
                        service[0] = DecimalUtil.add(service[0], servCost);
                    });
                }

                posLogHis.setAmt3(service[0]);
                baseMapper.insert(posLogHis);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return service[0];
    }

    public void insertPosLogHis(int dayCount){
        baseMapper.insertPosLogHis(dayCount);
    }

    public void updateLogHis(String cleanDate,int dayCount){
        baseMapper.updateLogHis(cleanDate,dayCount);
    }

    public void insertLogClean(int dayCount){
        baseMapper.insertLogClean(dayCount);
    }

    public void deleteLogHis(int dayCount){
        baseMapper.deleteLogHis(dayCount);
    }

}
