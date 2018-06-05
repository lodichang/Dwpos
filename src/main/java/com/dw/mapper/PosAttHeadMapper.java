package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosAttHead;

import java.util.List;

public interface PosAttHeadMapper extends BaseMapper<PosAttHead> {

    List<PosAttHead> queryList();

    PosAttHead queryItemHead();


}
