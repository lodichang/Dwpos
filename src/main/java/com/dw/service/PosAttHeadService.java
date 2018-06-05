package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.PosAttHead;
import com.dw.mapper.PosAttHeadMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PosAttHeadService extends ServiceImpl<PosAttHeadMapper, PosAttHead> {

    public List<PosAttHead> queryList() {
        return baseMapper.queryList();
    }

    public PosAttHead queryItemHead(){
        return baseMapper.queryItemHead();
    }
}
