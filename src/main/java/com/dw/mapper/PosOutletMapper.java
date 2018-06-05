package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosOutletDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/14.
 */
public interface PosOutletMapper extends BaseMapper<PosOutletDto> {

    List<PosOutletDto> getOutletList(@Param("outlet") String outlet);
}
