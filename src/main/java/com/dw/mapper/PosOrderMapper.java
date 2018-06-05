package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.OrderListDto;
import com.dw.dto.PosOrderAttDto;
import com.dw.dto.PosOrderDiscDto;
import com.dw.dto.PosOrderPrinterDto;
import com.dw.entity.PosOrder;
import com.dw.enums.TranTypeEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PosOrderMapper extends BaseMapper<PosOrder> {

    int existsEssentialCat(@Param("outlet") String outlet, @Param("catCode") String catCode, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    List<PosOrder> getAmtByCategory(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType, @Param("tableNum") String tableNum, @Param("category") String category);

    List<PosOrder> getOrderAmt(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    List<PosOrder> getChgAmt(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    void updateChgAmt(@Param("sub_roundc") BigDecimal sub_roundc, @Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);

    List<PosOrderDiscDto> getAvailableOrderList(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("inDate") String inDate);

    @Update("UPDATE tb_pos_order SET CAT_DISC = (CAT_DISC+#{cat_disc}) WHERE ID = #{id}")
    void updateCateDisc(@Param("id") String id, @Param("cat_disc") BigDecimal cat_disc);

    @Update("UPDATE tb_pos_order SET ITEM_DISC = (ITEM_DISC+#{item_disc}) WHERE ID = #{id}")
    void updateItemDisc(@Param("id") String id, @Param("item_disc") BigDecimal item_disc);

    @Update("UPDATE tb_pos_order SET ITEM_DISC = 0.00,CAT_DISC = 0.00 WHERE REF_NUM = #{refNum} AND SUB_REF = #{subRef} AND T_DATE = #{t_date} AND TYPE = #{type} AND TABLE_NUM =#{tableNo}")
    void updateDisValue(@Param("refNum") String refNum,@Param("subRef") String subRef,@Param("t_date") String t_date,@Param("type") String type,@Param("tableNo") String tableNo);

    List<OrderListDto> getOrderList(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("outlet") String outlet, @Param("tranType") String tranType);

    @Update("UPDATE tb_pos_order set TABLE_NUM =#{toTableNo}  WHERE REF_NUM = #{refNum} AND SUB_REF = #{subRef} AND TYPE = #{type}")
    int updateTransferOrder(@Param("refNum") String refNum,@Param("subRef") String subRef,@Param("type") String type,@Param("toTableNo") String toTableNo);

    @Update("UPDATE tb_pos_order set TABLE_NUM =#{toTableNo} ,REF_NUM = #{toRefNum} , SUB_REF = #{toSubRef} , TYPE = #{toType}  WHERE REF_NUM = #{refNum} AND SUB_REF = #{subRef} AND TYPE = #{type}")
    int updateTransferExistOrder(@Param("refNum") String refNum,@Param("subRef") String subRef,@Param("type") String type,@Param("toTableNo") String toTableNo,@Param("toRefNum") String toRefNum,@Param("toSubRef") String toSubRef,@Param("toType") String toType);

    List<PosOrderPrinterDto> queryAllByRefNum(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("itemIdxs") String itemIdxs);

    List<PosOrderAttDto> queryOrderAttByItemIdx(@Param("itemIdxs") String itemIdxs);

    List<PosOrderAttDto> queryOrderAttByRef(@Param("refNum") String refNum, @Param("subRef") String subRef);

    void deleteByRefNum(@Param("outlet") String outlet,
                        @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tableNo") String tableNo);
}
