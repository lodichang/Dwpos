package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosPayDto;
import com.dw.dto.PosPayMentDto;
import com.dw.entity.PosPayment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PosPayMentMapper extends BaseMapper<PosPayment> {

    List<PosPayMentDto> getPayMentList(@Param("outlet") String outlet);


    List<PosPayDto> getPosPayList(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);
}
