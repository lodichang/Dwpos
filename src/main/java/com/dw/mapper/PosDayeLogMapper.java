package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosDayeLog;
import org.springframework.stereotype.Repository;

/**
 * Created by li.yongliang on 2018/5/17.
 */
@Repository
public interface PosDayeLogMapper extends BaseMapper<PosDayeLog> {

    int getDiffDays();
}
