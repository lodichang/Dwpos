package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosAttActionDto;
import com.dw.entity.PosAttAction;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by li.yongliang on 2018/4/16.
 * @author li.yongliang
 */
public interface PosAttActionMapper extends BaseMapper<PosAttAction> {

    List<PosAttActionDto> queryList(@Param("isShow")String isShow);

    PosAttAction queryByCode(@Param("code")String code);
}
