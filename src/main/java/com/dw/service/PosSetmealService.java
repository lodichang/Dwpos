package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosSetmealGroupDto;
import com.dw.dto.PosStaffDto;
import com.dw.dto.PosStaffRightDto;
import com.dw.entity.PosSetmeal;
import com.dw.entity.PosStaff;
import com.dw.mapper.PosSetmealMapper;
import com.dw.mapper.PosStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosSetmealService extends ServiceImpl<PosSetmealMapper, PosSetmeal> {
    public List<PosSetmealGroupDto> getSetmealList(){
        return baseMapper.getSetmealList();
    }

}
