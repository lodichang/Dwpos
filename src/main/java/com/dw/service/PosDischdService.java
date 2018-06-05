package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosDisDto;
import com.dw.entity.PosDischd;
import com.dw.mapper.PosDischdMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PosDischdService extends ServiceImpl<PosDischdMapper, PosDischd> {

    public List<PosDischd> getPosDischds(String indate,String inTime,String week, String type){

        return baseMapper.getPosDischds(indate, inTime, week, type);
    }

    public List<PosDisDto> getPosDisDetail(String code){
        return baseMapper.getPosDisDetail(code);
    }


}
