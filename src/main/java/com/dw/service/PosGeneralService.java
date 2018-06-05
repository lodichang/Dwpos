package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosGeneralDto;
import com.dw.entity.PosGeneral;
import com.dw.mapper.PosGeneralMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosGeneralService extends ServiceImpl<PosGeneralMapper, PosGeneral> {
    @Autowired
    private PosGeneralMapper posGeneralMapper;

    public List<PosGeneralDto> getGeneralList( String outlet,  String gCount){
        return posGeneralMapper.getGeneralList(outlet, gCount);
    }
}
