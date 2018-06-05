package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosAttActionDto;
import com.dw.entity.PosAttAction;
import com.dw.mapper.PosAttActionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PosAttActionService extends ServiceImpl<PosAttActionMapper, PosAttAction> {

    public List<PosAttActionDto> queryList(String isShow){
        return baseMapper.queryList(isShow);
    }

    public PosAttAction queryByCode(String code) {
        return baseMapper.queryByCode(code);
    }

}
