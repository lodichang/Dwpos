package com.dw.component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.entity.PosTable;
import com.dw.service.PosTableService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitSplitTableComponent {
    @Autowired
    private PosTableService tableService;

    public void init() {
        String[] splits = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        Wrapper<PosTable> wrapper = new EntityWrapper<>();
        wrapper.eq("REMARKS", "0");
        List<PosTable> tableList = tableService.selectList(wrapper);
        List<PosTable> splitList = new ArrayList<>();
        tableList.forEach(table -> {
            String name = table.getRoomNum();
            for (int i = 0; i < splits.length; i++) {
                PosTable t = new PosTable();
                BeanUtils.copyProperties(table, t, "id");
                t.setRemarks("1");
                t.setRemark1("0");
                t.setIsOnline("0");
                t.setRoomNum(name + "-" + splits[i]);
                splitList.add(t);
            }
        });
        tableService.insertBatch(splitList);
    }
}
