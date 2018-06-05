package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.PosComb;
import com.dw.mapper.PosCombMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosCombService   extends ServiceImpl<PosCombMapper, PosComb> {
    public List<PosComb> getPosCombList(){
        return baseMapper.getPosCombList();
    }
}
