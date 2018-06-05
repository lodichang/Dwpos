package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosAttDto;
import com.dw.dto.PosItemMustAttDto;
import com.dw.entity.PosAtt;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosAttMapper extends BaseMapper<PosAtt> {

    void selectListForMap(ResultHandler handler);

    List<PosAtt> queryList();

    List<PosAtt> queryListByHeadCode(@Param("headCode") String headCode);

    PosAtt queryByGroupAndAttCode(@Param("aGroup") String aGroup, @Param("attCode")String attCode);

    List<PosItemMustAttDto> queryMustAttByItemCode(@Param("itemCode") String itemCode, @Param("start")int start, @Param("end") int end);

    List<PosItemMustAttDto> checkMustAttByItemCode(@Param("itemCode") String itemCode, @Param("selectedAttCodes")String selectedAttCodes);

    int queryMustAttCountByItemCode(@Param("itemCode") String itemCode);
}
