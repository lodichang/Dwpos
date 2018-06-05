package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosDisDto;
import com.dw.entity.PosDischd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PosDischdMapper extends BaseMapper<PosDischd> {

    public List<PosDischd> getPosDischds(@Param("indate") String indate,@Param("inTime") String inTime,@Param("week") String week,@Param("type") String type);


    public List<PosDisDto> getPosDisDetail(@Param("code") String code);
}
