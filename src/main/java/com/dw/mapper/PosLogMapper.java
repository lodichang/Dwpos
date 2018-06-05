package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.CouponDto;
import com.dw.dto.PosLogDto;
import com.dw.entity.PosLog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosLogMapper extends BaseMapper<PosLog> {

    @Insert("INSERT INTO tb_pos_log(ID,VERSION,OUTLET,T_DATE,T_TIME,STAFF,LOG_TYPE,TYPE,REF_NUM,SUB_REF,TABLE1) VALUES(" +
            "#{logDto.id},#{logDto.version},#{logDto.outlet},#{logDto.tDate},#{logDto.tTime},#{logDto.staff},#{logDto.logType},#{logDto.type},#{logDto.refNum}," +
            "#{logDto.subRef},#{logDto.table1})")
    void insertPosLog(@Param("logDto") PosLogDto logDto);

    List<CouponDto> getCouponData(@Param("outlet") String outlet,
                                  @Param("refNum") String refNum, @Param("subRef") String subRef,
                                  @Param("indate") String indate, @Param("tableNo") String tableNo, @Param("discType")String discType, @Param("cardId")String cardId);

    @Update("UPDATE tb_pos_log SET TYPE = #{type} WHERE id = #{id}")
    void updateCouponType(@Param("type") String type, @Param("id") String id);


    @Update("UPDATE tb_pos_log SET TYPE = #{type} WHERE OUTLET =#{outlet} " +
            "and REF_NUM = #{refNum} and SUB_REF = #{subRef} and TABLE1 = #{tableNo} and REMARK1 = #{couponCode} and REMARK4 = #{disId}")
    void updateDisType(@Param("type") String type, @Param("outlet") String outlet,
                       @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tableNo") String tableNo, @Param("couponCode") String couponCode,
                       @Param("disId") String disId);

    @Update("UPDATE tb_pos_log set TABLE2 =#{toTableNo} ,REF_NUM = #{toRefNum} , SUB_REF = #{toSubRef} , TYPE = #{toType}  WHERE REF_NUM = #{refNum} AND SUB_REF = #{subRef} AND TYPE = #{type} and LOG_TYPE IN (#{logType})")
    void    updateTransferExistOrderLog(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("type") String type, @Param("toTableNo") String toTableNo, @Param("toRefNum") String toRefNum, @Param("toSubRef") String toSubRef, @Param("toType") String toType, @Param("logType") String logType);

    @Delete("DELETE FROM tb_pos_log  WHERE REF_NUM = #{refNum} AND SUB_REF = #{subRef} AND TYPE = #{type} and LOG_TYPE NOT IN (#{logType})")
    void deletTransferExistOrderLog(@Param("refNum") String refNum, @Param("subRef") String subRef, @Param("type") String type,  @Param("logType") String logType);

    @Select("SELECT ifnull(max(QTY1),0) T_INDEX FROM tb_pos_log WHERE REF_NUM = #{refNum} and SUB_REF = #{subRef} and OUTLET = #{outlet} and T_INDEX = #{itemIdx} and type = #{tranType} and LOG_TYPE = #{logType}")
    int getMaxIndex(@Param("outlet")String posOutlet, @Param("logType") String logType, @Param("tranType") String tranType, @Param("refNum") String refNum,@Param("subRef") String subRef,@Param("itemIdx") Long itemIdx);

    void deleteByRefNum(@Param("outlet") String outlet,
                        @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tableNo") String tableNo);

    List<PosLogDto> getPosLogList(@Param("outlet") String outlet, @Param("refNum") String refNum, @Param("subRef") String subRef, @Param("tranType") String tranType);
}
