package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.PosTAtt;
import com.dw.entity.PosTAttHis;
import com.dw.mapper.PosTAttMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosTAttService extends ServiceImpl<PosTAttMapper, PosTAtt> {

    public List<PosTAtt> selectListByItemIdx(Long itemIdx) {
        return baseMapper.selectListByItemIdx(itemIdx);
    }

    public PosTAttHis convertToHis(PosTAtt posTAtt){
        PosTAttHis posTAttHis = new PosTAttHis();
        BeanUtils.copyProperties(posTAtt,posTAttHis);
        return posTAttHis;
    }

    public void deleteByOrderRefNum(String outlet,String refNum,String subRef,String tableNum){
        baseMapper.deleteByOrderRefNum(outlet,refNum,subRef,tableNum);
    }
}
