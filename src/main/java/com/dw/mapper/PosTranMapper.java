package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosOrderAddressDto;
import com.dw.entity.PosTran;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosTranMapper extends BaseMapper<PosTran> {

    /**
     * 根据账单号和日期查询账单，不使用缓存
     * @param refNum
     * @param inDate
     * @return
     */
    @Select("SELECT COUNT(REF_NUM) FROM tb_pos_tran WHERE REF_NUM=#{refNum} AND DATE_FORMAT(IN_DATE,'%Y-%m-%d')=#{inDate}")
    int queryByRefNum(@Param("refNum")String refNum, @Param("inDate") String inDate);

    @Select("SELECT * FROM tb_pos_tran WHERE REF_NUM=#{refNum}")
    PosTran queryByRefNumOnly(@Param("refNum")String refNum);

    List<PosTran> queryListByTable(@Param("tableNo")String tableNo, @Param("tranType")String tranType);

    @Insert("INSERT INTO tb_pos_tran(ID,VERSION,REF_NUM,SUB_REF,STATION_ID,OUTLET,TABLE_NUM,TRAN_TYPE,OPEN_STAFF,PERSON,IN_DATE,IN_TIME,HEAD_COUNT,ORDER_DISC) VALUES(" +
            "#{tranDto.id},#{tranDto.version},#{tranDto.refNum},#{tranDto.subRef},#{tranDto.stationId},#{tranDto.outlet},#{tranDto.tableNum},#{tranDto.tranType},#{tranDto.openStaff}," +
            "#{tranDto.person},#{tranDto.inDate},#{tranDto.inTime},#{tranDto.headCount},#{tranDto.orderDisc})")
    void insertPosTran(@Param("tranDto")PosTran tran);

    PosOrderAddressDto getPosOrderAddress(@Param("refNum") String refNum, @Param("subRef") String subRef);

    List<PosTran> getPeriod(@Param("outlet") String outlet,@Param("inDate") String inDate);
}
