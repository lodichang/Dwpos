package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosAttSettingDto;
import com.dw.entity.PosAttSetting;
import com.dw.mapper.PosAttSettingMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PosAttSettingService extends ServiceImpl<PosAttSettingMapper, PosAttSetting> {

    public List<PosAttSettingDto> queryListByAtt(String headCode, String attCode,String isMust){
        return baseMapper.queryListByAtt(headCode, attCode, isMust);
    }

    public PosAttSetting queryByHeadAttAndAction(String headCode, String attCode, String actionCode){
        return baseMapper.queryByHeadAttAndAction(headCode, attCode, actionCode);
    }
}
