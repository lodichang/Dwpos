package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosSettingDto;
import com.dw.entity.PosSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosSettingMapper extends BaseMapper<PosSetting> {

    List<PosSettingDto> getPosSetting();


    PosSettingDto queryTranOrderNo();

    PosSettingDto queryOrderIndex();

    PosSettingDto queryInvoiceNumber();

    PosSettingDto queryPrinterTaskNo();

    PosSettingDto queryCleanSeq();
    PosSettingDto queryPeriodSeq();

    void updateSetting(@Param("id") String id, @Param("keyValue") String keyValue);

    PosSettingDto queryByKey(@Param("key") String key);
}
