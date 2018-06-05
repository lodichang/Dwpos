package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosOutletDto;
import com.dw.mapper.PosOutletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosOutletService extends ServiceImpl<PosOutletMapper, PosOutletDto> {
    @Autowired
    private PosOutletMapper posOutletMapper;

    public List<PosOutletDto> getOutletList(String outlet){
        return posOutletMapper.getOutletList(outlet);
    }

}
