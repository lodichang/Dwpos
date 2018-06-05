package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosSetmealGroupDto;
import com.dw.entity.PosSetmeal;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by li.yongliang on 2018/5/3.
 */
@Repository
public interface PosSetmealMapper extends BaseMapper<PosSetmeal> {

    List<PosSetmealGroupDto> getSetmealList();
}
