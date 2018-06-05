package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.LanguageConfig;
import com.dw.mapper.LanguageConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LanguageConfigService extends ServiceImpl<LanguageConfigMapper, LanguageConfig> {

    public List<LanguageConfig> getLanguageConfig( String languageDefault){
        return baseMapper.getLanguageConfig(languageDefault);
    }
}
