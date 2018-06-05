package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.BillDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMapper extends BaseMapper<BillDto> {


    List<BillDto> getBillData(@Param("outlet") String outlet);

    List<BillDto> getOrderBillData(@Param("outlet") String outlet);
}
