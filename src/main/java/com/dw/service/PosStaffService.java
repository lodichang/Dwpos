package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosStaffDto;
import com.dw.dto.PosStaffRightDto;
import com.dw.entity.PosStaff;
import com.dw.mapper.PosStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosStaffService extends ServiceImpl<PosStaffMapper, PosStaff> {

    @Autowired
    private PosStaffMapper staffMapper;

    public List<PosStaffRightDto> getStaffRightList(String staffCode) {
        return staffMapper.getStaffRightList(staffCode);
    }


    public PosStaffDto staffLogin(String staffCode, String staffPassWd, String staffCardCode) {
        return staffMapper.staffLogin(staffCode, staffPassWd, staffCardCode);
    }


}
