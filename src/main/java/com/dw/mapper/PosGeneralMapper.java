package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosGeneralDto;
import com.dw.entity.PosGeneral;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/14.
 */
public interface PosGeneralMapper extends BaseMapper<PosGeneral> {
    List<PosGeneralDto> getGeneralList(@Param("outlet") String outlet,@Param("gCount") String gCount);
}
