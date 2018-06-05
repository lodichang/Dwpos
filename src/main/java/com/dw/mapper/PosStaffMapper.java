package com.dw.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dw.dto.PosStaffDto;
import com.dw.dto.PosStaffRightDto;
import com.dw.entity.PosStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PosStaffMapper extends BaseMapper<PosStaff> {
   List<PosStaffRightDto> getStaffRightList(@Param("staffCode") String staffCode);

   PosStaffDto staffLogin(@Param("staffCode") String staffCode, @Param("staffPassWd") String staffPassWd, @Param("staffCardCode") String staffCardCode);


}
