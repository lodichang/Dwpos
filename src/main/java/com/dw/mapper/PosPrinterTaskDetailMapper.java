package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosPrinterTask;
import com.dw.entity.PosPrinterTaskDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosPrinterTaskDetailMapper extends BaseMapper<PosPrinterTaskDetail> {
}