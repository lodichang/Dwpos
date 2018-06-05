package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.BillDto;
import com.dw.mapper.BillMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillService extends ServiceImpl<BillMapper, BillDto> {


    public List<BillDto> getBillData( String outlet){
        return baseMapper.getBillData(outlet);
    }

    public List<BillDto> getOrderBillData(String outlet){
        return baseMapper.getOrderBillData(outlet);
    }
}
