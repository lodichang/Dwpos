package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosPayDto;
import com.dw.dto.PosPayMentDto;
import com.dw.entity.PosPayment;
import com.dw.mapper.PosPayMentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosPayMentService extends ServiceImpl<PosPayMentMapper, PosPayment>{


    public List<PosPayMentDto> getPayMentList( String outlet){
        return baseMapper.getPayMentList(outlet);
    }


    public List<PosPayDto> getPosPayList( String outlet, String refNum, String subRef, String tranType){
        return baseMapper.getPosPayList(outlet, refNum, subRef, tranType);
    }


}
