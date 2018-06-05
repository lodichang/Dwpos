package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.CleanDto;
import com.dw.entity.PosPay;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PosPayMapper extends BaseMapper<PosPay> {
    @Select("SELECT ifnull(max(R_INDEX),0) R_INDEX FROM tb_pos_pay WHERE REF_NUM = #{refNum} and SUB_REF = #{subRef} and OUTLET = #{outlet}")
    int getMaxIndex(@Param("refNum") String refNum,  @Param("subRef") String subRef, @Param("outlet") String outlet);



    void updatePay(@Param("cleanDate") String cleanDate,@Param("dayCount") int dayCount);

    void insertPayClean(@Param("dayCount") int dayCount);

    void deletePay(@Param("dayCount") int dayCount);

    List<CleanDto> cleanGetPayRecord(@Param("zones") String zones);

    List<CleanDto> turnMoreGetPayRecord(@Param("zones") String zones);



}
