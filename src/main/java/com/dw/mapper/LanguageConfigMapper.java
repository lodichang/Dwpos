package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.LanguageConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageConfigMapper extends BaseMapper<LanguageConfig> {
    List<LanguageConfig> getLanguageConfig(@Param("languageDefault") String languageDefault);
}
