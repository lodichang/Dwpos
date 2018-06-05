package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.PosTAtt;
import com.dw.entity.PosTAttHis;
import com.dw.mapper.PosTAttHisMapper;
import com.dw.mapper.PosTAttMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosTAttHisService extends ServiceImpl<PosTAttHisMapper, PosTAttHis> {

    public void insertAttClean(int dayCount){
        baseMapper.insertAttClean(dayCount);
    }

    public void deleteAttHis(int dayCount){
        baseMapper.deleteAttHis(dayCount);
    }
}
