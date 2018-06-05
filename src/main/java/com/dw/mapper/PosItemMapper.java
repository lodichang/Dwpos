package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosItemDto;
import com.dw.entity.PosItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosItemMapper extends BaseMapper<PosItem> {
    PosItem selectByCode(@Param("itemCode") String itemCode);

    List<PosItemDto> getItems(@Param("outlet") String outlet, @Param("priceCode") String priceCode);

    PosItemDto searchItem(@Param("outlet") String outlet, @Param("priceCode") String priceCode, @Param("itemCode") String itemCode);

    @Update("UPDATE tb_pos_item SET DISCONT= #{discount} WHERE ITEM_CODE = #{itemCode}")
    int disCountItem(@Param("itemCode") String ItemCode, @Param("discount") String discount);
}
