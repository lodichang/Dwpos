package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.entity.PosTAtt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosTAttMapper extends BaseMapper<PosTAtt> {

    List<PosTAtt> selectListByItemIdx(@Param("itemIdx") Long itemIdx);

    void deleteByOrderRefNum(@Param("outlet") String outlet,@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tableNo") String tableNo);

}
