package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.RePrinterDto;
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
public class PosPrinterTaskService extends ServiceImpl<PosPrinterTaskMapper, PosPrinterTask> {
    @Autowired
    private PosPrinterTaskDetailService taskDetailService;

    public List<RePrinterDto> getPrinterTasks(String outlet, String printStat){
        return baseMapper.getPrinterTasks(outlet, printStat);
    }

    @Transactional(rollbackFor = PosOrderException.class)
    public boolean printBatch(List<PosPrinterTask> posPrinterTasks, List<PosPrinterTaskDetail> posPrinterTaskDetails) {
        boolean status = false;
        try {
            if (AppUtils.isNotBlank(posPrinterTasks)) {
                this.insertBatch(posPrinterTasks);
            }
            if (AppUtils.isNotBlank(posPrinterTaskDetails)) {
                taskDetailService.insertBatch(posPrinterTaskDetails);
            }
            status = true;
        } catch (PosOrderException e) {
            e.printStackTrace();
            throw e;
        }
        return status;
    }

}
