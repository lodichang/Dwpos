package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.CleanDto;
import com.dw.dto.PosPayDto;
import com.dw.entity.*;
import com.dw.enums.PayMentEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PayBillException;
import com.dw.mapper.PosPayMapper;
import com.dw.model.PayInfo;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PosPayService extends ServiceImpl<PosPayMapper, PosPay> {

    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosTranHisService posTranHisService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private PosOrderHisService posOrderHisService;
    @Autowired
    private PosTAttService posTAttService;
    @Autowired
    private PosTAttHisService posTAttHisService;
    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosLogHisService posLogHisService;
    @Autowired
    private PosPayMentService posPayMentService;


    /**
     * 1.處理刪除列表的付款記錄--原單狀態變成V，另外插入一條R的數據
     * 2.保存新的付款記錄
     * 3.剪切RAN_HIS表的cashamt,otheramt,paytype1,2,3,4,5,並刪除TRAN表的數據(正常結賬拍腳才會在TRAN，如果是改腳則直接更新TRAN_HIS),剪切ORDER數據到ORDER_HIS
     *
     * @param payInfo
     * @param newList
     * @param removeList
     */
    @Transactional(rollbackFor = PayBillException.class)
    public boolean addPaybill(PayInfo payInfo, List<PosPayDto> newList, List<PosPayDto> removeList) throws PayBillException {
        AtomicBoolean addPaybillFlag = new AtomicBoolean(false);
        try {
            //獲得最大INDEX
            AtomicLong maxIndex = new AtomicLong(baseMapper.getMaxIndex(payInfo.getRefNum(), payInfo.getSubRef(), Main.posOutlet));


            //1.處理刪除列表的付款記錄
            removeList.forEach(removePayLog -> {
                //有ID的才是原來的記錄
                if (AppUtils.isNotBlank(removePayLog.getId())) {
                    maxIndex.getAndIncrement();
                    int removeFlag = 0;
                    PosPay offsetOld = new PosPay(removePayLog.getId(), removePayLog.getOutlet(), removePayLog.getStation(), removePayLog.getRefNum(), removePayLog.getSubRef(), TranTypeEnum.A.getValue(), removePayLog.getPayType(),
                            removePayLog.getBillAmt(), removePayLog.getQty(), removePayLog.getAmount(), removePayLog.getNetAmount(), removePayLog.getOverAmt(), removePayLog.getRoom(), removePayLog.getRTime(), removePayLog.getRDate(), removePayLog.getStaff(), Main.posStaff.getCode(), new Date());
                    removeFlag = baseMapper.updateById(offsetOld);
                    if (removeFlag == 0) {
                        throw new PayBillException(Main.languageMap.get("exception.updatefailed"));
                    }

                    PosPay offsetNew = new PosPay(removePayLog.getOutlet(), removePayLog.getStation(), removePayLog.getRefNum(), removePayLog.getSubRef(), TranTypeEnum.R.getValue(), removePayLog.getPayType(),
                            removePayLog.getBillAmt(), removePayLog.getQty(), removePayLog.getAmount(), removePayLog.getNetAmount(), removePayLog.getOverAmt(), removePayLog.getRoom(), new Date(), new Date(), Main.posStaff.getCode(), maxIndex.longValue(), Main.posStaff.getCode(), new Date(),removePayLog.getTips());
                    removeFlag = baseMapper.insert(offsetNew);
                    if (removeFlag == 0) {
                        throw new PayBillException(Main.languageMap.get("exception.insertfailed"));
                    }
                }
            });
            //2.保存新的付款記錄
            newList.forEach(newPayLog -> {
                int newFlag = 0;
                maxIndex.getAndIncrement();
                PosPay offsetNew = new PosPay(newPayLog.getOutlet(), newPayLog.getStation(), newPayLog.getRefNum(), newPayLog.getSubRef(), TranTypeEnum.N.getValue(), newPayLog.getPayType(),
                        newPayLog.getBillAmt(), newPayLog.getQty(), newPayLog.getAmount(), newPayLog.getNetAmount(), newPayLog.getOverAmt(), newPayLog.getRoom(), new Date(), new Date(), Main.posStaff.getCode(), maxIndex.longValue(), Main.posStaff.getCode(), new Date(),newPayLog.getTips());
                newFlag = baseMapper.insert(offsetNew);
                if (newFlag == 0) {
                    throw new PayBillException(Main.languageMap.get("exception.insertfailed"));
                }
            });


            //3.更新TRAN_HIS表的cashamt,otheramt,paytype1,2,3,4,5,並刪除TRAN表的數據(正常a結賬拍腳纔會在TRAN，如果是該腳則直接更新TRAN_HIS)
            switch (payInfo.getBillType()) {
                //歷史改腳
                case ORDERHISBILL:
                    Wrapper<PosTranHis> posTranHisWrapper = new EntityWrapper<>();
                    posTranHisWrapper.eq("REF_NUM", payInfo.getRefNum());
                    posTranHisWrapper.eq("SUB_REF", payInfo.getSubRef());
                    posTranHisWrapper.eq("OUTLET", Main.posOutlet);
                    List<PosTranHis> posTranHisList = posTranHisService.selectList(posTranHisWrapper);
                    if (AppUtils.isBlank(posTranHisList)) {
                        throw new PayBillException(Main.languageMap.get("exception.datanotfound"));
                    }
                    PosTranHis posTranHis = processPosTranHis(posTranHisList.get(0), newList);
                    addPaybillFlag.set(posTranHisService.updateById(posTranHis));
                    addPaybillFlag.set(true);
                    break;
                case ORDERBILL:
                    //正常拍腳
                    //剪切PosTran到PosTranHis
                    Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                    posTranWrapper.eq("REF_NUM", payInfo.getRefNum());
                    posTranWrapper.eq("SUB_REF", payInfo.getSubRef());
                    posTranWrapper.eq("OUTLET", Main.posOutlet);
                    List<PosTran> posTranList = posTranService.selectList(posTranWrapper);
                    if (AppUtils.isBlank(posTranList)) {
                        throw new PayBillException(Main.languageMap.get("exception.datanotfound"));
                    }
                    //轉換
                    PosTranHis posTranHisN = posTranService.convertToHis(posTranList.get(0));
                    processPosTranHis(posTranHisN, newList);
                    addPaybillFlag.set(posTranHisService.insert(posTranHisN));
                    addPaybillFlag.set(posTranService.delete(posTranWrapper));
                    //剪切PosOrder到PosOrderHis
                    //把折扣数据拆入到posOrder表，然后再剪切数据，还未处理
                    Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
                    posOrderWrapper.eq("REF_NUM", payInfo.getRefNum());
                    posOrderWrapper.eq("SUB_REF", payInfo.getSubRef());
                    posOrderWrapper.eq("OUTLET", Main.posOutlet);
                    List<PosOrder> posOrderList = posOrderService.selectList(posOrderWrapper);
                    if (AppUtils.isBlank(posOrderList)) {
                        throw new PayBillException(Main.languageMap.get("exception.datanotfound"));
                    }
                    posOrderList.forEach(posOrder -> {
                        //PosOrderHis posOrderHis = (PosOrderHis) posOrder;
                        PosOrderHis posOrderHis = posOrderService.convertToHis(posOrder);
                        addPaybillFlag.set(posOrderHisService.insert(posOrderHis));
                        addPaybillFlag.set(posOrderService.deleteById(posOrder.getId()));
                        //剪切PosTAtt到PosTAttHis
                        Wrapper<PosTAtt> posTAttWrapper = new EntityWrapper<>();
                        posTAttWrapper.eq("OUTLET", Main.posOutlet);
                        posTAttWrapper.eq("ITEM_IDX", posOrderHis.getItemIdx());
                        List<PosTAtt> posTAttList = posTAttService.selectList(posTAttWrapper);
                        posTAttList.forEach(posTAtt -> {
                            //PosTAttHis posTAttHis = (PosTAttHis) posTAtt;
                            PosTAttHis posTAttHis =posTAttService.convertToHis(posTAtt);
                            addPaybillFlag.set(posTAttHisService.insert(posTAttHis));
                            addPaybillFlag.set(posTAttService.delete(posTAttWrapper));
                        });
                    });

                    //剪切PosLog到PosLogHis
                    Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                    posLogWrapper.eq("REF_NUM", payInfo.getRefNum());
                    posLogWrapper.eq("SUB_REF", payInfo.getSubRef());
                    posLogWrapper.eq("OUTLET", Main.posOutlet);
                    List<PosLog> posLogList = posLogService.selectList(posLogWrapper);
                    if (AppUtils.isBlank(posLogList)) {
                        throw new PayBillException(Main.languageMap.get("exception.datanotfound"));
                    }
                    posLogList.forEach(posLog -> {
                        PosLogHis posLogHis = posLogService.convertToHis(posLog);
                        addPaybillFlag.set(posLogHisService.insert(posLogHis));
                        addPaybillFlag.set(posLogService.deleteById(posLog.getId()));
                    });


                    break;
                default:
                    break;


            }
            if (!addPaybillFlag.get()) {
                throw new PayBillException(Main.languageMap.get("exception.innererror"));
            }
            return addPaybillFlag.get();

        } catch (PayBillException e) {
            throw e;
        } catch (Exception e1) {
            throw new PayBillException(Main.languageMap.get("exception.innererror" + e1.getMessage()));
        }
    }


    private PosTranHis processPosTranHis(PosTranHis posTranHis, List<PosPayDto> newList) {
        BigDecimal cashAmt = new BigDecimal(newList.stream().filter(posPay -> posPay.getPayType().equals(PayMentEnum.CASH.getValue())).collect(Collectors.summarizingDouble((PosPayDto::getAmtountDoubleVale))).getSum());
        BigDecimal otherAmt = posTranHis.getBillAmt().subtract(cashAmt);

        for (int i = 0; i < newList.size(); i++) {
            switch (i) {
                case 0:
                    String payType1 = AppUtils.isNotBlank(newList.get(i)) ? newList.get(i).getPayType() : "";
                    posTranHis.setPaytype1(payType1);
                    posTranHis.setPaytype2("");
                    posTranHis.setPaytype3("");
                    posTranHis.setPaytype4("");
                    posTranHis.setPaytype5("");
                    break;
                case 1:
                    String payType2 = AppUtils.isNotBlank(newList.get(i)) ? newList.get(i).getPayType() : "";
                    posTranHis.setPaytype2(payType2);
                    posTranHis.setPaytype3("");
                    posTranHis.setPaytype4("");
                    posTranHis.setPaytype5("");
                    break;
                case 2:
                    String payType3 = AppUtils.isNotBlank(newList.get(i)) ? newList.get(i).getPayType() : "";
                    posTranHis.setPaytype3(payType3);
                    posTranHis.setPaytype4("");
                    posTranHis.setPaytype5("");
                    break;
                case 3:
                    String payType4 = AppUtils.isNotBlank(newList.get(i)) ? newList.get(i).getPayType() : "";
                    posTranHis.setPaytype4(payType4);
                    posTranHis.setPaytype4("");
                    break;
                case 4:
                    String payType5 = AppUtils.isNotBlank(newList.get(i)) ? newList.get(i).getPayType() : "";
                    posTranHis.setPaytype5(payType5);
                    break;
                default:
                    break;
            }

        }
        posTranHis.setCashAmt(cashAmt);
        posTranHis.setOtherAmt(otherAmt);
        posTranHis.setBillStaff(Main.posStaff.getCode());
        posTranHis.setPayStaff(Main.posStaff.getCode());



        posTranHis.setBillDate(new Date());
        posTranHis.setBillTime(new Date());
        posTranHis.setSettled("TRUE");
        return posTranHis;
    }

    public void updatePay(String cleanDate,int dayCount){
        baseMapper.updatePay(cleanDate,dayCount);
    }

    public void insertPayClean(int dayCount){
        baseMapper.insertPayClean(dayCount);
    }

    public void deletePay(int dayCount){
        baseMapper.deletePay(dayCount);
    }


    public void deleteOldPay(String refNum,String subRef,String outlet,String tranType){
        //獲得最大INDEX
        AtomicLong maxIndex = new AtomicLong(baseMapper.getMaxIndex(refNum, subRef, Main.posOutlet));
        //加載付款記錄
        List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, refNum, subRef,tranType);
        //1.處理刪除列表的付款記錄
        posPayDtoList.forEach(removePayLog -> {
            //有ID的才是原來的記錄
            if (AppUtils.isNotBlank(removePayLog.getId())) {
                maxIndex.getAndIncrement();
                int removeFlag = 0;
                PosPay offsetOld = new PosPay(removePayLog.getId(), removePayLog.getOutlet(), removePayLog.getStation(), removePayLog.getRefNum(), removePayLog.getSubRef(), TranTypeEnum.A.getValue(), removePayLog.getPayType(),
                        removePayLog.getBillAmt(), removePayLog.getQty(), removePayLog.getAmount(), removePayLog.getNetAmount(), removePayLog.getOverAmt(), removePayLog.getRoom(), removePayLog.getRTime(), removePayLog.getRDate(), removePayLog.getStaff(), Main.posStaff.getCode(), new Date());
                baseMapper.updateById(offsetOld);
                PosPay offsetNew = new PosPay(removePayLog.getOutlet(), removePayLog.getStation(), removePayLog.getRefNum(), removePayLog.getSubRef(), TranTypeEnum.R.getValue(), removePayLog.getPayType(),
                        removePayLog.getBillAmt(), removePayLog.getQty(), removePayLog.getAmount(), removePayLog.getNetAmount(), removePayLog.getOverAmt(), removePayLog.getRoom(), new Date(), new Date(), Main.posStaff.getCode(), maxIndex.longValue(), Main.posStaff.getCode(), new Date(), removePayLog.getTips());
                baseMapper.insert(offsetNew);
            }
        });
    }


    public List<CleanDto> cleanGetPayRecord(String zones){
        return baseMapper.cleanGetPayRecord(zones);
    }

    public List<CleanDto> turnMoreGetPayRecord(String zones){
        return baseMapper.turnMoreGetPayRecord(zones);
    }
}
