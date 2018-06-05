package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.entity.PosPrinterTask;
import com.dw.entity.PosPrinterTaskDetail;
import com.dw.exception.PosOrderException;
import com.dw.mapper.PosPrinterTaskDetailMapper;
import com.dw.mapper.PosPrinterTaskMapper;
import com.dw.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PosPrinterTaskDetailService extends ServiceImpl<PosPrinterTaskDetailMapper, PosPrinterTaskDetail> {

}
