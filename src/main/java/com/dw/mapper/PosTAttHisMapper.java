package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosTAtt;
import com.dw.entity.PosTAttHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosTAttHisMapper extends BaseMapper<PosTAttHis> {

    void insertAttClean(@Param("dayCount") int dayCount);

    void deleteAttHis(@Param("dayCount") int dayCount);

}
