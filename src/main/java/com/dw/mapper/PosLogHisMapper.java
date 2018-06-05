package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.CouponDto;
import com.dw.entity.PosLogHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosLogHisMapper extends BaseMapper<PosLogHis> {

    void insertPosLogHis(@Param("dayCount") int dayCount);

    void updateLogHis(@Param("cleanDate") String cleanDate,@Param("dayCount") int dayCount);

    void insertLogClean(@Param("dayCount") int dayCount);

    void deleteLogHis(@Param("dayCount") int dayCount);

    List<CouponDto> getCouponData(@Param("outlet") String outlet,
                                  @Param("refNum") String refNum, @Param("subRef") String subRef,
                                  @Param("indate") String indate, @Param("tableNo") String tableNo, @Param("discType")String discType, @Param("cardId")String cardId);
}
