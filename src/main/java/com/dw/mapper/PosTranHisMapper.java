package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosTran;
import com.dw.entity.PosTranHis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PosTranHisMapper extends BaseMapper<PosTranHis> {
    List<PosTranHis> queryListByTable(@Param("tableNo")String tableNo, @Param("tranType")String tranType);

    PosTranHis getOnePosTranHis(@Param("outlet") String outlet,@Param("refNum") String refNum, @Param("subRef") String subRef,@Param("tranType") String tranType);

    List<PosTranHis> checkPaymentRecords();

    void updateTranHis(@Param("cleanDate") String cleanDate,@Param("dayCount") int dayCount);

    void insertTranClean(@Param("dayCount") int dayCount);

    void deleteTranHis(@Param("dayCount") int dayCount);

    void updateTranHisPeriod(@Param("period") int period);
}
