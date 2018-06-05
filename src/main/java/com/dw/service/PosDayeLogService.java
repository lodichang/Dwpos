package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.CouponDto;
import com.dw.dto.PosLogDto;
import com.dw.dto.TableViewDto;
import com.dw.entity.*;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.exception.PayBillException;
import com.dw.exception.PosOrderException;
import com.dw.mapper.MemPeriodMapper;
import com.dw.mapper.PosDayeLogMapper;
import com.dw.mapper.PosLogMapper;
import com.dw.mapper.PosOrderMapper;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.DecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PosDayeLogService extends ServiceImpl<PosDayeLogMapper, PosDayeLog> {

    public int getDiffDays(){
        return baseMapper.getDiffDays();
    }
}
