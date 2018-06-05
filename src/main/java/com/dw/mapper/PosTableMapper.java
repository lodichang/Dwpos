package com.dw.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosTableDto;
import com.dw.entity.PosTable;
import com.dw.vo.PosHoldOnTableVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 桌台信息表 Dao接口
 */
@Repository
public interface PosTableMapper extends BaseMapper<PosTable> {

  void addTable(@Param("posTableDto") PosTableDto posTableDto);

  List<PosTableDto> getTablesByFloor(@Param("page") Integer page, @Param("floor") String floor,@Param("tableNum") String tableNum,@Param("checkLeaveSeat") boolean checkLeaveSeat);

  void updatePosTableXY(@Param("xRatio") BigDecimal xRatio, @Param("yRatio") BigDecimal yRatio, @Param("roomNum") String roomNum);

  List<PosTableDto> getSamePosTable(@Param("xRatio") BigDecimal xRatio, @Param("yRatio") BigDecimal yRatio, @Param("roomNum") String roomNum);

  List<PosTable> getsetUpTableByTableNum(@Param("tableNum") String tableNum,@Param("disPlayType") String disPlayType);

  List<PosTableDto> getDisplayTableByTableNum(@Param("tableNum") String tableNum,@Param("checkLeaveSeat") boolean checkLeaveSeat);

  PosTable getTableByNum(@Param("tableNum")String tableNum);

  List<PosHoldOnTableVo> getHoldOnTables(@Param("tableNum") String tableNum);

  String getTableTypeByTableNum(@Param("tableNum") String tableNum);

}
