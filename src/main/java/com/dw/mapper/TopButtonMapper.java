package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.TopButtonDto;
import com.dw.entity.PosButton;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/14.
 */
public interface TopButtonMapper extends BaseMapper<PosButton> {
    List<TopButtonDto> getTopButtonList(@Param("outlet") String outlet, @Param("dayWeek") int dayWeek, @Param("priceCode") String priceCode);
    List<TopButtonDto> getTopButtonListNoCache(@Param("outlet") String outlet, @Param("dayWeek") int dayWeek, @Param("priceCode") String priceCode);
}
