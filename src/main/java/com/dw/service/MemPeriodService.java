package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosSettingDto;
import com.dw.entity.MemPeriod;
import com.dw.entity.PosTran;
import com.dw.entity.PosTranHis;
import com.dw.mapper.MemPeriodMapper;
import com.dw.mapper.PosTranMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemPeriodService extends ServiceImpl<MemPeriodMapper, MemPeriod> {

}
