package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.exception.PayBillException;
import com.dw.exception.PosOrderException;
import com.dw.mapper.PosItemStkMapper;
import com.dw.mapper.PosOrderHisMapper;
import com.dw.mapper.PosTAttHisMapper;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PosOrderHisService extends ServiceImpl<PosOrderHisMapper, PosOrderHis> {

    @Autowired
    private PosTAttHisMapper posTAttHisMapper;

    @Autowired
    private PosItemStkMapper posItemStkMapper;

    @Autowired
    private PosTranHisService posTranHisService;

    @Autowired
    private PosItemStkService posItemStkService;


    public List<ViewBillItemDto> viewOrder(String refNum, String subRef, String tranType) {

        return baseMapper.viewOrder(refNum, subRef, Main.posOutlet, tranType);
    }

    public List<OrderListDto> getOrderHisList(String refNum, String subRef, String outlet, String tranType) {
        return baseMapper.getOrderHisList(refNum, subRef, outlet, tranType);
    }

    @Transactional(rollbackFor = PosOrderException.class)
    public boolean sendOrder(List<PosOrder> posOrderList, List<PosTAtt> posTAttList, List<PosItemStk> posItemStkList) {
        boolean status = false;
        try {
            posOrderList.forEach((PosOrder posOrder) ->{
                  if(posOrder instanceof PosOrderHis){
                      PosOrderHis posOrderHis =(PosOrderHis)posOrder;
                      this.insertOrUpdate(posOrderHis);
                  }

            });
            posTAttList.forEach((PosTAtt posTAtt) ->{
                if(posTAtt instanceof  PosTAttHis){
                    PosTAttHis posTAttHis = (PosTAttHis) posTAtt;
                    posTAttHisMapper.insert(posTAttHis);
                }
            });
            posItemStkList.forEach(posItemStk -> posItemStkMapper.updateById(posItemStk));
            status = true;
        } catch (PosOrderException e) {
            e.printStackTrace();
            throw e;
        }
        return status;
    }

    public List<PosOrderCancleDto> getOrderHisCancleList(String refNum, String subRef, String outlet, String tableNo,String tranType) {
        return baseMapper.getOrderHisCancleList(refNum, subRef, outlet,tableNo, tranType);
    }


    @Transactional(rollbackFor = PayBillException.class)
    public void splitDiscToOrder(BigDecimal discAmt, String outlet, String refNum, String subRef, String tranType) {
        //1、先把chgamt字段更新為0,
        Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
        posOrderHisWrapper.eq("OUTLET", outlet);
        posOrderHisWrapper.eq("REF_NUM", refNum);
        posOrderHisWrapper.eq("SUB_REF", subRef);
        posOrderHisWrapper.eq("TYPE", tranType);

        PosOrderHis posOrderHis = new PosOrderHis();
        posOrderHis.setChgAmt(BigDecimal.ZERO);

        this.update(posOrderHis, posOrderHisWrapper);
        //2、把discAmt拆分到每個菜品
        //2.1、得到可以折扣的菜品信息
        List<PosOrderHis> orderAmtList = baseMapper.getOrderAmt(outlet, refNum, subRef, tranType);
        if (AppUtils.isNotBlank(orderAmtList)) {
            BigDecimal orderAmt = orderAmtList.get(0).getAmt();
            if (orderAmt.compareTo(BigDecimal.ZERO) > 0) {
                posOrderHisWrapper.gt("AMT", 0);
                posOrderHisWrapper.gt("QTY+CANCEL+FREE", 0);
                List<PosOrderHis> posOrderHisList = this.selectList(posOrderHisWrapper);
                posOrderHisList.forEach(po -> {
                    BigDecimal itemOrderAmt = DecimalUtil.subtract(po.getAmt(), po.getOrderDisc());
                    BigDecimal chgAmt = DecimalUtil.divideUP(DecimalUtil.multiply(discAmt, itemOrderAmt), orderAmt);
                    po.setChgAmt(chgAmt.compareTo(po.getAmt()) >= 0 ? po.getAmt() : chgAmt);
                    baseMapper.updateById(po);
                });
                List<PosOrderHis> chgPosList = baseMapper.getChgAmt(outlet, refNum, subRef, tranType);
                if (AppUtils.isNotBlank(chgPosList)) {
                    BigDecimal chgAmt = chgPosList.get(0).getChgAmt();
                    BigDecimal amt = chgPosList.get(0).getAmt();
                    BigDecimal sub_roundc = DecimalUtil.subtract(discAmt.compareTo(amt) >= 0 ? amt : discAmt, chgAmt);
                    if (sub_roundc.compareTo(BigDecimal.ZERO) != 0) {
                        baseMapper.updateChgAmt(sub_roundc, outlet, refNum, subRef, tranType);
                    }
                }
            }
        }
    }


    /**
     * 1.找到需要取消的數據將cancel更新為-1
     * 2.複製取消數據,然後處理QTY,AMT,ITEM_DISC,CAT_DISC,ORDER_DISC再插入數據庫
     * 3.更新扣燉表的數量
     *
     * @param tableViewDto
     * @param cancelNum
     * @param reasonCode
     * @return
     * @throws PosOrderException
     */
    @Transactional(rollbackFor = PosOrderException.class)
    public CancelOrderDto cancelItem(TableViewDto tableViewDto, String cancelNum, String reasonCode) throws PosOrderException {
        CancelOrderDto cancelOrderDto = new CancelOrderDto();
        try {
            cancelOrderDto.setCanceled(true);
            cancelOrderDto.setOgriItemIdx(tableViewDto.getItemIdx());
            cancelOrderDto.setItemPrn(tableViewDto.getItemPrn());
            //1.找到需要取消的數據將cancel更新為-1
            Wrapper<PosOrderHis> posOrderHisWrapper = new EntityWrapper<>();
            posOrderHisWrapper.eq("ITEM_IDX", tableViewDto.getItemIdx());

            PosOrderHis posOrderHis = baseMapper.selectList(posOrderHisWrapper).get(0);
            // 查詢口味
            Wrapper<PosTAttHis> attHisWrapper = new EntityWrapper<>();
            attHisWrapper.eq("ITEM_IDX", tableViewDto.getItemIdx());
            List<PosTAttHis> attHisList = posTAttHisMapper.selectList(attHisWrapper);
            if (AppUtils.isBlank(posOrderHis)) {
                throw new PosOrderException(Main.languageMap.get("exception.datanotfound"));
            }
            posOrderHis.setCancel(posOrderHis.getCancel() + Integer.parseInt(cancelNum) * -1);
            posOrderHis.setReason(reasonCode);
            Integer updateFlag = baseMapper.updateById(posOrderHis);
            if (updateFlag == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.updatefailed"));
            }
            // 2.複製取消數據,然後處理QTY,AMT,ITEM_DISC,CAT_DISC,ORDER_DISC再插入數據庫
            posOrderHis.setCancel(Integer.parseInt(cancelNum));
            posOrderHis.setAmt(DecimalUtil.bigDecimalByPloy(posOrderHis.getAmt().divide(new BigDecimal(posOrderHis.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrderHis.setItemDisc(DecimalUtil.bigDecimalByPloy(posOrderHis.getItemDisc().divide(new BigDecimal(posOrderHis.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrderHis.setCatDisc(DecimalUtil.bigDecimalByPloy(posOrderHis.getCatDisc().divide(new BigDecimal(posOrderHis.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrderHis.setOrderDisc(DecimalUtil.bigDecimalByPloy(posOrderHis.getOrderDisc().divide(new BigDecimal(posOrderHis.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrderHis.setQty(Integer.parseInt(cancelNum) * -1);
            posOrderHis.setCancel(Integer.parseInt(cancelNum));
            posOrderHis.setStaff(Main.posStaff.getCode());
            posOrderHis.setItemIndex(posOrderHis.getItemIdx());
            posOrderHis.setItemIdx(Long.parseLong(posTranHisService.getOrderIndex()));
            posOrderHis.setTDate(new Date());
            posOrderHis.setTTime(new Date());
            posOrderHis.setId(null);
            posOrderHis.setSepChar("-" + posOrderHis.getSepChar());
            updateFlag = baseMapper.insert(posOrderHis);
            if (updateFlag == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.insertfailed"));
            }
            // 更新口味
            attHisList.forEach(posTAttHis -> {
                posTAttHis.setId(null);
                posTAttHis.setItemIdx(posOrderHis.getItemIdx());
                posTAttHisMapper.insert(posTAttHis);
            });

            //3.更新扣燉表的數量
            Wrapper<PosItemStk> posItemStkWrapper = new EntityWrapper<>();
            posItemStkWrapper.eq("ITEM_CODE", tableViewDto.getItemCode());
            PosItemStk posItemStk = posItemStkService.selectOne(posItemStkWrapper);
            if (AppUtils.isNotBlank(posItemStk)) {
                posItemStk.setStock(String.valueOf(Integer.parseInt(posItemStk.getStock()) + Integer.parseInt(cancelNum)));
                cancelOrderDto.setCanceled(posItemStkService.updateById(posItemStk));
                if (!cancelOrderDto.isCanceled()) {
                    throw new PosOrderException(Main.languageMap.get("exception.updatefailed"));
                }
            }
            cancelOrderDto.setCancelOrder(posOrderHis);
            return cancelOrderDto;

        } catch (PosOrderException e) {
            throw e;
        } catch (Exception e1) {
            throw new PosOrderException(Main.languageMap.get("exception.innererror" + e1.getMessage()));
        }

    }


    public void updateOrderHis(String cleanDate,int dayCount){
        baseMapper.updateOrderHis(cleanDate,dayCount);
    }

    public void insertOrderClean(int dayCount){
        baseMapper.insertOrderClean(dayCount);
    }

    public void deleteOrderHis(int dayCount){
        baseMapper.deleteOrderHis(dayCount);
    }

    public void updateOrderHisPeriod(int period){
        baseMapper.updateOrderHisPeriod(period);
    }

    public List<PeriodReportDto> getPeriodReportRecords(String outlet,String startdate,String enddate,String stationid){
        return baseMapper.getPeriodReportRecords(outlet,startdate,enddate,stationid);
    }

    public List<PeriodReportDto> getFoodSellRecords(String outlet,String startdate,String enddate,String stationid){
        return baseMapper.getFoodSellRecords(outlet,startdate,enddate,stationid);
    }

    public List<CleanDto> getCleanOrTurnMoreRecords(String outlet,String startdate,String period,String floor){
        return baseMapper.getCleanOrTurnMoreRecords(outlet,startdate,period,floor);
    }

}
