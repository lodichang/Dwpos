package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosAttSettingDto;
import com.dw.entity.PosAttSetting;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/16.
 */
public interface PosAttSettingMapper extends BaseMapper<PosAttSetting> {

    List<PosAttSettingDto> queryListByAtt(@Param("headCode")String headCode, @Param("attCode")String attCode, @Param("isMust") String isMust);


    PosAttSetting queryByHeadAttAndAction(@Param("headCode")String headCode, @Param("attCode")String attCode, @Param("actionCode")String actionCode);
}
