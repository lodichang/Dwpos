package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosGenphDto;
import com.dw.entity.PosGenph;
import com.dw.mapper.PosGenphMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosGenphService extends ServiceImpl<PosGenphMapper, PosGenph> {
    @Autowired
    private PosGenphMapper posGenphMapper;

    public List<PosGenphDto> getGenphList(String outlet, String gDate){
        return posGenphMapper.getGenphList(outlet, gDate);
    }
}
