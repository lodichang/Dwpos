package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosComb;

import java.util.List;

public interface PosCombMapper  extends BaseMapper<PosComb> {
    List<PosComb> getPosCombList();
}
