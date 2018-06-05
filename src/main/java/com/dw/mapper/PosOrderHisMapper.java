package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.*;
import com.dw.entity.PosOrder;
import com.dw.entity.PosOrderHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PosOrderHisMapper extends BaseMapper<PosOrderHis> {
    List<ViewBillItemDto> viewOrder( @Param("refNum") String refNum, @Param("subRef") String subRef,@Param("outlet") String outlet,@Param("tranType") String tranType);

    List<OrderListDto> getOrderHisList(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("outlet") String outlet, @Param("tranType") String tranType);

    List<PosOrderCancleDto> getOrderHisCancleList(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("outlet") String outlet,@Param("tableNo") String tableNo,@Param("tranType") String tranType);

    List<PosOrderHis> getAmtByCategory(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType, @Param("tableNum") String tableNum, @Param("category") String category);

    List<PosOrderHis> getOrderAmt(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    List<PosOrderHis> getChgAmt(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    void updateChgAmt(@Param("sub_roundc") BigDecimal sub_roundc, @Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);


    void updateOrderHis(@Param("cleanDate") String cleanDate,@Param("dayCount") int dayCount);

    void insertOrderClean(@Param("dayCount") int dayCount);

    void deleteOrderHis(@Param("dayCount") int dayCount);

    void updateOrderHisPeriod(@Param("period") int period);


    List<PeriodReportDto> getPeriodReportRecords(@Param("outlet") String outlet,@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("stationid") String stationid);

    List<PeriodReportDto> getFoodSellRecords(@Param("outlet") String outlet,@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("stationid") String stationid);

    List<CleanDto> getCleanOrTurnMoreRecords(@Param("outlet") String outlet, @Param("startdate") String startdate, @Param("period") String period, @Param("floor") String floor);


}
