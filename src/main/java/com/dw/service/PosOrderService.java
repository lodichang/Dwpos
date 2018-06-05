package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PayBillException;
import com.dw.exception.PosOrderException;
import com.dw.mapper.*;
import com.dw.util.*;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.vo.PosHoldOnTableVo;
import javafx.collections.ObservableList;
import org.apache.commons.collections.map.HashedMap;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dw.enums.AMTPROEnum.AMT;

@Service
public class PosOrderService extends ServiceImpl<PosOrderMapper, PosOrder> {
    @Autowired
    private PosTAttMapper posTAttMapper;
    @Autowired
    private PosItemStkMapper posItemStkMapper;
    @Autowired
    private PosTranService posTranService;
    @Autowired
    private PosItemStkService posItemStkService;
    @Autowired
    private PosLogMapper posLogMapper;
    @Autowired
    private PosTableMapper posTableMapper;
    @Autowired
    private NettyComponent nettyComponent;

    @Transactional(rollbackFor = PosOrderException.class)
    public boolean sendOrder(List<PosOrder> posOrderList, List<PosTAtt> posTAttList, List<PosItemStk> posItemStkList) {
        boolean status = false;
        try {
            posOrderList.forEach(posOrder -> this.insertOrUpdate(posOrder));
            posTAttList.forEach(posTAtt -> posTAttMapper.insert(posTAtt));
            posItemStkList.forEach(posItemStk -> posItemStkMapper.updateById(posItemStk));
            status = true;
        } catch (PosOrderException e) {
            e.printStackTrace();
            throw e;
        }
        return status;
    }

    public int existsEssentialCat(String outlet, String catCode, String refNum, String subRef, String tranType) {
        return baseMapper.existsEssentialCat(outlet, catCode, refNum, subRef, tranType);
    }


    public PosOrderHis convertToHis(PosOrder posOrder) {
        PosOrderHis posOrderHis = new PosOrderHis();
        BeanUtils.copyProperties(posOrder,posOrderHis);
        return posOrderHis;
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
            Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
            posOrderWrapper.eq("ITEM_IDX", tableViewDto.getItemIdx());
            PosOrder posOrder = baseMapper.selectList(posOrderWrapper).get(0);
            // 查詢口味
            Wrapper<PosTAtt> attWrapper = new EntityWrapper<>();
            attWrapper.eq("ITEM_IDX", tableViewDto.getItemIdx());
            List<PosTAtt> attList = posTAttMapper.selectList(attWrapper);
            if (AppUtils.isBlank(posOrder)) {
                throw new PosOrderException(Main.languageMap.get("exception.datanotfound"));
            }
            posOrder.setCancel(posOrder.getCancel() + Integer.parseInt(cancelNum) * -1);
            posOrder.setReason(reasonCode);
            Integer updateFlag = baseMapper.updateById(posOrder);
            if (updateFlag == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.updatefailed"));
            }
            // 2.複製取消數據,然後處理QTY,AMT,ITEM_DISC,CAT_DISC,ORDER_DISC再插入數據庫
            posOrder.setCancel(Integer.parseInt(cancelNum));
            posOrder.setAmt(DecimalUtil.bigDecimalByPloy(posOrder.getAmt().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrder.setItemDisc(DecimalUtil.bigDecimalByPloy(posOrder.getItemDisc().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrder.setCatDisc(DecimalUtil.bigDecimalByPloy(posOrder.getCatDisc().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrder.setOrderDisc(DecimalUtil.bigDecimalByPloy(posOrder.getOrderDisc().divide(new BigDecimal(posOrder.getQty())).multiply(new BigDecimal(cancelNum)).multiply(new BigDecimal(-1))));
            posOrder.setQty(Integer.parseInt(cancelNum) * -1);
            //posOrder.setCancel(Integer.parseInt(cancelNum));和114行重複了
            posOrder.setStaff(Main.posStaff.getCode());
            //
            posOrder.setItemIndex(posOrder.getItemIdx());
            posOrder.setItemIdx(Long.parseLong(posTranService.getOrderIndex()));
            posOrder.setTDate(new Date());
            posOrder.setTTime(new Date());
            posOrder.setId(null);
            posOrder.setSepChar(AppUtils.isBlank(posOrder.getSepChar())?posOrder.getSepChar():"-" +posOrder.getSepChar());
            updateFlag = baseMapper.insert(posOrder);
            if (updateFlag == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.insertfailed"));
            }
            // 更新口味
            attList.forEach(posTAtt -> {
                posTAtt.setId(null);
                posTAtt.setItemIdx(posOrder.getItemIdx());
                posTAttMapper.insert(posTAtt);
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
            cancelOrderDto.setCancelOrder(posOrder);
            return cancelOrderDto;

        } catch (PosOrderException e) {
            throw e;
        } catch (Exception e1) {
            throw new PosOrderException(Main.languageMap.get("exception.innererror" + e1.getMessage()));
        }

    }


    public List<PosOrderDiscDto> getAvailableOrderList(String outlet, String refNum, String subRef, String inDate) {
        return baseMapper.getAvailableOrderList(outlet, refNum, subRef, inDate);
    }

    public void updateCateDisc(String id, BigDecimal cat_disc) {
        baseMapper.updateCateDisc(id, cat_disc);
    }

    public void updateItemDisc(String id, BigDecimal item_disc) {
        baseMapper.updateItemDisc(id, item_disc);
    }


    public void updateDisValue(String refNum, String subRef, String t_date, String type, String tableNo) {
        baseMapper.updateDisValue(refNum, subRef, t_date, type, tableNo);
    }


    @Transactional(rollbackFor = PayBillException.class)
    public void splitDiscToOrder(BigDecimal discAmt, String outlet, String refNum, String subRef, String tranType) {
        //1、先把chgamt字段更新為0,
        Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
        posOrderWrapper.eq("OUTLET", outlet);
        posOrderWrapper.eq("REF_NUM", refNum);
        posOrderWrapper.eq("SUB_REF", subRef);
        posOrderWrapper.eq("TYPE", tranType);

        PosOrder posOrder = new PosOrder();
        posOrder.setChgAmt(BigDecimal.ZERO);

        this.update(posOrder, posOrderWrapper);
        //2、把discAmt拆分到每個菜品
        //2.1、得到可以折扣的菜品信息
        List<PosOrder> orderAmtList = baseMapper.getOrderAmt(outlet, refNum, subRef, tranType);
        if (AppUtils.isNotBlank(orderAmtList)) {
            BigDecimal orderAmt = orderAmtList.get(0).getAmt();
            if (orderAmt.compareTo(BigDecimal.ZERO) > 0) {
                posOrderWrapper.gt("AMT", 0);
                posOrderWrapper.gt("QTY+CANCEL+FREE", 0);
                List<PosOrder> posOrders = this.selectList(posOrderWrapper);
                posOrders.forEach(po -> {
                    BigDecimal itemOrderAmt = DecimalUtil.subtract(po.getAmt(), po.getOrderDisc());
                    BigDecimal chgAmt = DecimalUtil.divideUP(DecimalUtil.multiply(discAmt, itemOrderAmt), orderAmt);
                    po.setChgAmt(chgAmt.compareTo(po.getAmt()) >= 0 ? po.getAmt() : chgAmt);
                    baseMapper.updateById(po);
                });
                List<PosOrder> chgPosList = baseMapper.getChgAmt(outlet, refNum, subRef, tranType);
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

    public List<OrderListDto> getOrderList(String refNum, String subRef, String outlet, String tranType) {
        return baseMapper.getOrderList(refNum, subRef, outlet, tranType);
    }

    /**
     * 分單
     * 1.寫入LOG
     * 2.將指定的order數據更新到指定的賬單
     *
     * @param tableViewDtos
     * @param toTran
     * @throws PosOrderException
     */
    @Transactional(rollbackFor = PosOrderException.class)
    public void splitItemToTable(ObservableList<TableViewDto> tableViewDtos, PosTran toTran) throws PosOrderException {
        AtomicInteger tranferFlag = new AtomicInteger(0);
        final String[] fromtableNum = {""};
        try {
            tableViewDtos.forEach(tableViewDto -> {
                PosOrder posOrder = baseMapper.selectById(tableViewDto.getId());
                if (AppUtils.isBlank(posOrder)) {
                    throw new PosOrderException(Main.languageMap.get("settle.unsend"));
                }
                if(AppUtils.isBlank(fromtableNum[0])){
                    fromtableNum[0] = posOrder.getTableNum();
                }
                PosLog posLogTFER = new PosLog();
                posLogTFER.setOutlet(Main.posOutlet);
                posLogTFER.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
                posLogTFER.setTTime(new Date());
                posLogTFER.setStaff(Main.posStaff.getCode());
                posLogTFER.setLogType(LogTypeEnum.TFER.getValue());
                posLogTFER.setType(TranTypeEnum.N.getValue());
                posLogTFER.setRefNum(toTran.getRefNum());
                posLogTFER.setSubRef(toTran.getSubRef());
                posLogTFER.setTable1(posOrder.getTableNum());
                posLogTFER.setTable2(toTran.getTableNum());
                posLogTFER.setRemark1(posOrder.getItemCode());
                posLogTFER.setTIndex(posOrder.getItemIndex());
                posLogTFER.setAmt1(posOrder.getAmt());
                tranferFlag.set(posLogMapper.insert(posLogTFER));
                if (tranferFlag.get() == 0) {
                    throw new PosOrderException(Main.languageMap.get("exception.innererror"));
                }
                //2.將指定的order數據更新到指定的賬單
                posOrder.setRefNum(toTran.getRefNum());
                posOrder.setSubRef(toTran.getSubRef());
                posOrder.setTableNum(toTran.getTableNum());
                posOrder.setType(toTran.getTranType());
                posOrder.setAmt(posOrder.getUnitPrice().multiply(new BigDecimal(posOrder.getQty())));
                tranferFlag.set(baseMapper.updateById(posOrder));
                if (tranferFlag.get() == 0) {
                    throw new PosOrderException(Main.languageMap.get("exception.innererror"));
                }
            });
            //转移成功后发netty消息更新message叫起桌台map
            List<PosHoldOnTableVo> posHoldOnToTableVoList = posTableMapper.getHoldOnTables(toTran.getTableNum());
            if(AppUtils.isNotBlank(posHoldOnToTableVoList)){
                Map<String,String> tableMap = new HashedMap();
                tableMap.put(toTran.getTableNum(),posHoldOnToTableVoList.get(0).getMinTime());
                nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON,tableMap);
            }
            //转移成功后发netty消息更新message叫起桌台map
            List<PosHoldOnTableVo> posHoldOnFromTableVoList = posTableMapper.getHoldOnTables(fromtableNum[0]);
            if(AppUtils.isNotBlank(posHoldOnFromTableVoList)){
                Map<String,String> tableMap = new HashedMap();
                tableMap.put(fromtableNum[0],posHoldOnFromTableVoList.get(0).getMinTime());
                nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON,tableMap);
            }
            else{
                Map<String,String> tableMap = new HashedMap();
                tableMap.put(fromtableNum[0],"");
                nettyComponent.sendMessage(NettyMessageTypeEnum.IMMEDIATELY,tableMap);
            }
        } catch (PosOrderException e) {
            throw e;
        }
    }

    public List<PosOrderPrinterDto> queryAllByRefNum(String refNum, String subRef, String itemIdxs) {
        return baseMapper.queryAllByRefNum(refNum, subRef, itemIdxs);
    }

    public List<PosOrderAttDto> queryOrderAttByItemIdx(String itemIdxs) {
        return baseMapper.queryOrderAttByItemIdx(itemIdxs);
    }

    public List<PosOrderAttDto> queryOrderAttByRef(String refNum, String subRef) {
        return baseMapper.queryOrderAttByRef(refNum, subRef);
    }

    public void deleteByRefNum(String outlet, String refNum, String subRef, String tableNo) {
        baseMapper.deleteByRefNum(outlet, refNum, subRef, tableNo);
    }

}

