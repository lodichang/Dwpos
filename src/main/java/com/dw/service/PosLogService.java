package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.CouponDto;
import com.dw.dto.PosLogDto;
import com.dw.dto.TableViewDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PayBillException;
import com.dw.exception.PosOrderException;
import com.dw.mapper.MemPeriodMapper;
import com.dw.mapper.PosLogMapper;
import com.dw.mapper.PosOrderMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PosLogService extends ServiceImpl<PosLogMapper, PosLog> {
    @Autowired
    private PosLogMapper posLogMapper;
    @Autowired
    private MemPeriodMapper memPeriodMapper;
    @Autowired
    private PosOrderMapper posOrderMapper;

    public void insertPosLog(PosLogDto logDto) {
        posLogMapper.insertPosLog(logDto);
    }

    public List<CouponDto> getCouponData(String outlet, String refNum, String subRef, String indate, String tableNo, String discType, String cardId) {
        return posLogMapper.getCouponData(outlet, refNum, subRef, indate, tableNo, discType, cardId);
    }

    public void deleteByRefNum(String outlet, String refNum, String subRef, String tableNo) {
        posLogMapper.deleteByRefNum(outlet, refNum, subRef, tableNo);
    }

    public void updateCouponType(String type, String id) {
        posLogMapper.updateCouponType(type, id);
    }

    public void updateDisType(String type, String outlet, String refNum, String subRef, String tableNo, String couponCode, String disId) {
        posLogMapper.updateDisType(type, outlet, refNum, subRef, tableNo, couponCode, disId);
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
                Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                posLogWrapper.eq("OUTLET", Main.posOutlet);
                posLogWrapper.eq("LOG_TYPE", LogTypeEnum.DSRV.getValue());
                posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                posLogWrapper.eq("REF_NUM", posTran.getRefNum());
                posLogWrapper.eq("SUB_REF", posTran.getSubRef());
                posLogMapper.delete(posLogWrapper);
                //再插入服務費記錄
                PosLog posLog = new PosLog();
                posLog.setOutlet(Main.posOutlet);
                posLog.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
                posLog.setTTime(new Date());
                posLog.setStaff(Main.posStaff.getCode());
                posLog.setLogType(LogTypeEnum.DSRV.getValue());
                posLog.setType(TranTypeEnum.N.getValue());
                posLog.setRefNum(posTran.getRefNum());
                posLog.setSubRef(posTran.getSubRef());
                posLog.setTable1(posTran.getTableNum());
                posLog.setAmt1(new BigDecimal(1.00));
                posLog.setAmt2(new BigDecimal(servPro));

                //根據服務費收費費率計算每個商品的服務費。
                Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
                posOrderWrapper.eq("OUTLET", Main.posOutlet);
                posOrderWrapper.eq("REF_NUM", posTran.getRefNum());
                posOrderWrapper.eq("SUB_REF", posTran.getSubRef());
                posOrderWrapper.eq("TYPE", posTran.getTranType());
                posOrderWrapper.eq("SERVICE", "TRUE");
                posOrderWrapper.gt("QTY+CANCEL+FREE", 0);
                List<PosOrder> posOrders = posOrderMapper.selectList(posOrderWrapper);
                if (serviceType) {
                    posOrders.forEach(po -> {
                        BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(servPro), po.getAmt()), new BigDecimal(100)).doubleValue());
                        po.setServCost(servCost);
                        posOrderMapper.updateById(po);
                        service[0] = DecimalUtil.add(service[0], servCost);
                    });
                } else {
                    posOrders.forEach(po -> {
                        BigDecimal itemOrderAmt = DecimalUtil.subtract(po.getAmt(), po.getOrderDisc());
                        BigDecimal servCost = DecimalUtil.doubleToBigDecimalByPloy(DecimalUtil.divide(DecimalUtil.multiply(new BigDecimal(servPro), itemOrderAmt), new BigDecimal(100)).doubleValue());
                        po.setServCost(servCost);
                        posOrderMapper.updateById(po);
                        service[0] = DecimalUtil.add(service[0], servCost);
                    });
                }

                posLog.setAmt3(service[0]);
                posLogMapper.insert(posLog);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return service[0];
    }

    public PosLogHis convertToHis(PosLog posLog) {
        PosLogHis posLogHis = new PosLogHis();
        BeanUtils.copyProperties(posLog,posLogHis);
        return posLogHis;
    }

    /**
     * 追單方法
     * 1.先查找LOG表是否已經有該菜品的追單記錄,有則取最大記錄累加,然後插入新的記錄
     *
     * @param tableViewDto
     * @return 返回追單次數
     */
    @Transactional(rollbackFor = PosOrderException.class)
    public PosLog urgeOrder(TableViewDto tableViewDto) throws PosOrderException {

        try {
            PosOrder posOrder = posOrderMapper.selectById(tableViewDto.getId());
            if (AppUtils.isBlank(posOrder)) {
                throw new PosOrderException(Main.languageMap.get("settle.unsend"));
            }
            //追單最大次數
            int urgeCount = baseMapper.getMaxIndex(Main.posOutlet, LogTypeEnum.URGE.getValue(), TranTypeEnum.N.getValue(), posOrder.getRefNum(), posOrder.getSubRef(), posOrder.getItemIdx());
            urgeCount = urgeCount + 1;
            PosLog posLogURGE = new PosLog();
            posLogURGE.setOutlet(Main.posOutlet);
            posLogURGE.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
            posLogURGE.setTTime(new Date());
            posLogURGE.setStaff(Main.posStaff.getCode());
            posLogURGE.setLogType(LogTypeEnum.URGE.getValue());
            posLogURGE.setType(TranTypeEnum.N.getValue());
            posLogURGE.setRefNum(posOrder.getRefNum());
            posLogURGE.setSubRef(posOrder.getSubRef());
            posLogURGE.setTable1(posOrder.getTableNum());
            posLogURGE.setQty1(urgeCount);//最單次數
            posLogURGE.setQty2(posOrder.getQty());//食品數量
            posLogURGE.setQty3(DateUtil.getTimeBetween((DateUtil.getFormatDay(posOrder.getTDate()) + " " + DateUtil.getFormatTime(posOrder.getTTime())), DateUtil.getCurrTimeYmdHms()));//逾期時長
            posLogURGE.setRemark1(posOrder.getItemCode());//食品編號
            posLogURGE.setRemark2(tableViewDto.getItemName());//食品名稱
            posLogURGE.setTIndex(posOrder.getItemIdx()); //食品所在賬單的IDX
            posLogURGE.setAmt1(posOrder.getAmt()); //食品總價
            int insertFlag = baseMapper.insert(posLogURGE);
            if (insertFlag < 1) {
                throw new PayBillException(Main.languageMap.get("exception.insertfailed"));
            }
            return posLogURGE;
        } catch (
                PosOrderException e)

        {
            throw e;
        }
    }


    public List<PosLogDto> getPosLogList(String outlet, String refNum, String subRef, String tranType){
        return baseMapper.getPosLogList(outlet, refNum, subRef, tranType);
    }
}
