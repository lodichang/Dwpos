package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.MemPeriod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/26.
 */
@Repository
public interface MemPeriodMapper extends BaseMapper<MemPeriod> {

    List<MemPeriod> getServicePeriodList(@Param("inTime") String inTime);
}
