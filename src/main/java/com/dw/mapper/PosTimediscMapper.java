package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosTimedisc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/27.
 */
@Repository
public interface PosTimediscMapper extends BaseMapper<PosTimedisc> {
    List<PosTimedisc> getDiscRulesList(@Param("outlet") String outlet);
}
