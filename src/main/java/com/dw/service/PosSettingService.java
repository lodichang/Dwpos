package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosSettingDto;
import com.dw.entity.PosSetting;
import com.dw.mapper.PosSettingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosSettingService extends ServiceImpl<PosSettingMapper, PosSetting> {
    public PosSettingDto queryByKey(String key) {
        return baseMapper.queryByKey(key);
    }

    public PosSettingDto queryTranOrderNo() {
        return baseMapper.queryTranOrderNo();
    }

    public PosSettingDto queryPrinterTaskNo() {
        return baseMapper.queryPrinterTaskNo();
    }

    public PosSettingDto queryOrderIndex() {
        return baseMapper.queryOrderIndex();
    }

    public PosSettingDto queryInvoiceNumber() {
        return baseMapper.queryInvoiceNumber();
    }

    public void updateSetting(String id, String keyValue) {
        baseMapper.updateSetting(id, keyValue);
    }

    public List<PosSettingDto> getPosSetting() {
        return baseMapper.getPosSetting();
    }

    public PosSettingDto queryCleanSeq() {
        return baseMapper.queryCleanSeq();
    }

    public PosSettingDto queryPeriodSeq() {
        return baseMapper.queryPeriodSeq();
    }


}
