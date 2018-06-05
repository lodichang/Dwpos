package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.component.NettyComponent;
import com.dw.dto.PosOrderAddressDto;
import com.dw.dto.PosSettingDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PosOrderException;
import com.dw.mapper.PosLogMapper;
import com.dw.mapper.PosOrderMapper;
import com.dw.mapper.PosTableMapper;
import com.dw.mapper.PosTranMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.IDManager;
import com.dw.vo.PosHoldOnTableVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PosTranService extends ServiceImpl<PosTranMapper, PosTran> {

    @Autowired
    private PosSettingService posSettingService;
    @Autowired
    private PosOrderMapper posOrderMapper;
    @Autowired
    private PosLogMapper posLogMapper;
    @Autowired
    private PosTableMapper posTableMapper;
    @Autowired
    private NettyComponent nettyComponent;


    public int queryByRefNum(String refNum, String inDate) {
        return baseMapper.queryByRefNum(refNum, inDate);
    }

    public PosTran queryByRefNumOnly(String refNum) {
        return baseMapper.queryByRefNumOnly(refNum);
    }

    public List<PosTran> queryListByTable(String tableNo, String tranType) {
        return baseMapper.queryListByTable(tableNo, tranType);
    }

    public void insertPosTran(PosTran tran) {
        //baseMapper.insertPosTran(tran);
        baseMapper.insert(tran);
    }

    public String getTranOrderNo() {
        String tranOrderNo = null;
        try {

            // 查詢賬單編號
            PosSettingDto settingDto = posSettingService.queryTranOrderNo();
            tranOrderNo = settingDto.getPosValue();
            // 更新賬單編號
            posSettingService.updateSetting(settingDto.getId(), String.valueOf(Integer.parseInt(settingDto.getPosValue()) + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tranOrderNo;
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



    public List<PosTran> queryList() {
        return baseMapper.selectList(null);
    }

    public PosTranHis convertToHis(PosTran posTran) {
        PosTranHis pt = new PosTranHis();
        BeanUtils.copyProperties(posTran,pt);
        return pt;


    }

    /**
     * 處理轉台-空檯
     * 1.更新TRAN表檯號
     * 2.更新ORDER表檯號
     * 3.寫入LOG,含ORDER每條食品明細
     *
     * @param orgTran
     * @param toTableNum
     * @return
     */
    @Transactional(rollbackFor = PosOrderException.class)
    public void transferToEmptyTable(PosTran orgTran, String toTableNum) throws PosOrderException {
        AtomicInteger tranferFlag = new AtomicInteger(0);
        try {
            String oldTableNum = orgTran.getTableNum();
            PosLog posLogCTBL = new PosLog();
            posLogCTBL.setOutlet(Main.posOutlet);
            posLogCTBL.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
            posLogCTBL.setTTime(new Date());
            posLogCTBL.setStaff(Main.posStaff.getCode());
            posLogCTBL.setLogType(LogTypeEnum.CTBL.getValue());
            posLogCTBL.setType(TranTypeEnum.N.getValue());
            posLogCTBL.setRefNum(orgTran.getRefNum());
            posLogCTBL.setSubRef(orgTran.getSubRef());
            posLogCTBL.setTable1(orgTran.getTableNum());
            posLogCTBL.setTable2(toTableNum);
            tranferFlag.set(posLogMapper.insert(posLogCTBL));
            if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }
            Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
            posOrderWrapper.eq("REF_NUM", orgTran.getRefNum());
            posOrderWrapper.eq("SUB_REF", orgTran.getSubRef());
            posOrderWrapper.eq("TYPE", orgTran.getTranType());
            List<PosOrder> posOrderList = posOrderMapper.selectList(posOrderWrapper);
            posOrderList.forEach(posOrder -> {
                PosLog posLogCTBD = new PosLog();
                posLogCTBD.setOutlet(Main.posOutlet);
                posLogCTBD.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
                posLogCTBD.setTTime(new Date());
                posLogCTBD.setStaff(Main.posStaff.getCode());
                posLogCTBD.setLogType(LogTypeEnum.CTBD.getValue());
                posLogCTBD.setType(TranTypeEnum.N.getValue());
                posLogCTBD.setRefNum(posOrder.getRefNum());
                posLogCTBD.setSubRef(posOrder.getSubRef());
                posLogCTBD.setTable1(posOrder.getTableNum());
                posLogCTBD.setTable2(toTableNum);
                posLogCTBD.setRemark1(posOrder.getItemCode());
                posLogCTBD.setTIndex(posOrder.getItemIndex());
                posLogCTBD.setAmt1(posOrder.getAmt());
                tranferFlag.set(posLogMapper.insert(posLogCTBD));
                if (tranferFlag.get() == 0) {
                    throw new PosOrderException(Main.languageMap.get("exception.innererror"));
                }
            });

            tranferFlag.set(posOrderMapper.updateTransferOrder(orgTran.getRefNum(), orgTran.getSubRef(), orgTran.getTranType(), toTableNum));
            /*if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }*/
            orgTran.setTableNum(toTableNum);
            tranferFlag.set(baseMapper.updateById(orgTran));
            if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }
            //转台后要发起netty消息更新叫起的台号map
            List<PosHoldOnTableVo> posHoldOnTableVoList = posTableMapper.getHoldOnTables(toTableNum);
            if(AppUtils.isNotBlank(posHoldOnTableVoList)){
                Map<String,String> tableMap = new HashedMap();
                tableMap.put(toTableNum,posHoldOnTableVoList.get(0).getMinTime());
                nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON,tableMap);
            }
            Map<String,String> tableMap = new HashedMap();
            tableMap.put(oldTableNum,"");
            nettyComponent.sendMessage(NettyMessageTypeEnum.IMMEDIATELY,tableMap);
        } catch (PosOrderException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 處理轉台-已經存在的檯
     * 1.重新計算目標單號的TRAN,刪除原始單號
     * 2.更新ORDER表檯號,以及單號,子單號
     * 3.寫入LOG,含目標ORDER每條食品明細
     *
     * @param orgTran
     * @param toTran
     */
    @Transactional(rollbackFor = PosOrderException.class)
    public void transferToExistTable(PosTran orgTran, PosTran toTran)throws PosOrderException {
        AtomicInteger tranferFlag = new AtomicInteger(0);
        try {
            //寫入轉台日誌
            PosLog posLogCTBL = new PosLog();
            posLogCTBL.setOutlet(Main.posOutlet);
            posLogCTBL.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
            posLogCTBL.setTTime(new Date());
            posLogCTBL.setStaff(Main.posStaff.getCode());
            posLogCTBL.setLogType(LogTypeEnum.CTBL.getValue());
            posLogCTBL.setType(TranTypeEnum.N.getValue());
            posLogCTBL.setRefNum(toTran.getRefNum());
            posLogCTBL.setSubRef(toTran.getSubRef());
            posLogCTBL.setTable1(orgTran.getTableNum());
            posLogCTBL.setTable2(toTran.getTableNum());
            tranferFlag.set(posLogMapper.insert(posLogCTBL));
            if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }
            //寫入轉台明細日誌
            Wrapper<PosOrder> posOrderWrapper = new EntityWrapper<>();
            posOrderWrapper.eq("REF_NUM", orgTran.getRefNum());
            posOrderWrapper.eq("SUB_REF", orgTran.getSubRef());
            posOrderWrapper.eq("TYPE", orgTran.getTranType());
            List<PosOrder> posOrderList = posOrderMapper.selectList(posOrderWrapper);
            posOrderList.forEach(posOrder -> {
                PosLog posLogCTBD = new PosLog();
                posLogCTBD.setOutlet(Main.posOutlet);
                posLogCTBD.setTDate(DateUtil.StringToDate(DateUtil.getCurrTimeYmd(), "yyyy-MM-dd"));
                posLogCTBD.setTTime(new Date());
                posLogCTBD.setStaff(Main.posStaff.getCode());
                posLogCTBD.setLogType(LogTypeEnum.CTBD.getValue());
                posLogCTBD.setType(TranTypeEnum.N.getValue());
                posLogCTBD.setRefNum(toTran.getRefNum());
                posLogCTBD.setSubRef(toTran.getSubRef());
                posLogCTBD.setTable1(posOrder.getTableNum());
                posLogCTBD.setTable2(toTran.getTableNum());
                posLogCTBD.setRemark1(posOrder.getItemCode());
                posLogCTBD.setTIndex(posOrder.getItemIndex());
                posLogCTBD.setAmt1(posOrder.getAmt());
                tranferFlag.set(posLogMapper.insert(posLogCTBD));
                if (tranferFlag.get() == 0) {
                    throw new PosOrderException(Main.languageMap.get("exception.innererror"));
                }
            });
            //寫入轉台優惠券折扣的日誌
            posLogMapper.updateTransferExistOrderLog(orgTran.getRefNum(), orgTran.getSubRef(), orgTran.getTranType(), toTran.getTableNum(), toTran.getRefNum(),
                    toTran.getSubRef(), toTran.getTranType(), "'UCOP','FUCD','SICD'");
            //刪除原始檯除優惠券折扣的日誌
            posLogMapper.deletTransferExistOrderLog(orgTran.getRefNum(), orgTran.getSubRef(), orgTran.getTranType(), "'UCOP','FUCD','SICD'");

            //移動原始檯的明細到目標檯
            tranferFlag.set(posOrderMapper.updateTransferExistOrder(orgTran.getRefNum(), orgTran.getSubRef(), orgTran.getTranType(), toTran.getTableNum(), toTran.getRefNum(), toTran.getSubRef(), toTran.getTranType()));
            /*if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }*/
            //合併目標檯的人數
            toTran.setPerson(toTran.getPerson() + orgTran.getPerson());
            tranferFlag.set(baseMapper.updateById(toTran));
            if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }
            //刪除原始檯
            tranferFlag.set(baseMapper.deleteById(orgTran));
            if (tranferFlag.get() == 0) {
                throw new PosOrderException(Main.languageMap.get("exception.innererror"));
            }
            //合拼完之后发送netty消息更新叫起map
            List<PosHoldOnTableVo> posHoldOnTableVoList = posTableMapper.getHoldOnTables(toTran.getTableNum());
            if(AppUtils.isNotBlank(posHoldOnTableVoList)){
                Map<String,String> tableMap = new HashedMap();
                tableMap.put(toTran.getTableNum(),posHoldOnTableVoList.get(0).getMinTime());
                nettyComponent.sendMessage(NettyMessageTypeEnum.HOLDON,tableMap);
            }
            Map<String,String> tableMap = new HashedMap();
            tableMap.put(orgTran.getTableNum(),"");
            nettyComponent.sendMessage(NettyMessageTypeEnum.IMMEDIATELY,tableMap);
        } catch (PosOrderException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 更改人數方法，更新posTran，插入一條日誌
     * @param result 新的開台人數
     * */
    public void updatePersons(PosTran posTran,String result){

        String oldPersons = posTran.getPerson().toString();

        posTran.setPerson(Integer.parseInt(result));
        posTran.setHeadCount(result);
        baseMapper.updateById(posTran);

        PosLog posLog = new PosLog();
        posLog.setOutlet(Main.posSetting.get("outlet"));
        posLog.setTDate(new Date());
        posLog.setTTime(new Date());
        posLog.setStaff(Main.posStaff.getCode());
        posLog.setLogType(LogTypeEnum.PERS.getValue());
        posLog.setType(TranTypeEnum.N.getValue());
        posLog.setRefNum(posTran.getRefNum());
        posLog.setSubRef(posTran.getSubRef());
        posLog.setTable1(posTran.getTableNum());
        posLog.setId(IDManager.generateIDs());
        posLog.setRemark1(oldPersons);
        posLog.setRemark2(result);
        posLogMapper.insert(posLog);
    }


    public PosOrderAddressDto getPosOrderAddress(String refNum,String subRef){
        return baseMapper.getPosOrderAddress(refNum,subRef);
    }

    //获取发票编号
    public String getInvoiceNumber() {
        String invoiceNumber = null;
        try {

            // 查詢发票編號
            PosSettingDto settingDto = posSettingService.queryInvoiceNumber();
            invoiceNumber = settingDto.getPosValue();
            // 更新发票编号
            posSettingService.updateSetting(settingDto.getId(), String.valueOf(Integer.parseInt(settingDto.getPosValue()) + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceNumber;
    }

    public List<PosTran> getPeriod(String outlet,String inDate){
        return baseMapper.getPeriod(outlet,inDate);
    }

}
