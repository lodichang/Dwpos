package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosGenphDto;
import com.dw.entity.PosGenph;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/14.
 */
public interface PosGenphMapper extends BaseMapper<PosGenph> {

    List<PosGenphDto> getGenphList(@Param("outlet") String outlet,@Param("gDate") String gDate);
}
