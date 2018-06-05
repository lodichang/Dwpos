package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.RePrinterDto;
import com.dw.entity.PosPrinterTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosPrinterTaskMapper extends BaseMapper<PosPrinterTask> {


    List<RePrinterDto> getPrinterTasks(@Param("outlet") String outlet, @Param("printStat") String printStat);
}