package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.PosAttDto;
import com.dw.dto.PosItemMustAttDto;
import com.dw.entity.PosAtt;
import com.dw.mapper.PosAttMapper;
import com.dw.util.AppUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PosAttService extends ServiceImpl<PosAttMapper, PosAtt> {


    public Map<String, List<PosAttDto>> selectListForMap() {
        try {
            class PosAttResultHandler implements ResultHandler {
                private Map<String, List<PosAttDto>> retMap = new HashMap<>();

                @Override
                public void handleResult(ResultContext context) {
                    final PosAttDto retObject = (PosAttDto) context.getResultObject();
                    if (AppUtils.isBlank(retMap.get(retObject.getAGroup()))) {
                        retMap.put(retObject.getAGroup(), new ArrayList<>());
                    }
                    retMap.get(retObject.getAGroup()).add(retObject);
                }
            }
            PosAttResultHandler handler = new PosAttResultHandler();

            baseMapper.selectListForMap(handler);

            return handler.retMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PosAtt> queryListByHeadCode(String headCode) {
        return baseMapper.queryListByHeadCode(headCode);
    }

    public PosAtt queryByGroupAndAttCode(String aGroup, String attCode) {
        return baseMapper.queryByGroupAndAttCode(aGroup, attCode);
    }

    public List<PosItemMustAttDto> queryMustAttByItemCode(String itemCode, int start, int end) {
        return baseMapper.queryMustAttByItemCode(itemCode, start, end);
    }

    public List<PosItemMustAttDto> checkMustAttByItemCode(String itemCode, String selectedAttCodes) {
        return baseMapper.checkMustAttByItemCode(itemCode, selectedAttCodes);
    }

    public int queryMustAttCountByItemCode(String itemCode) {
        return baseMapper.queryMustAttCountByItemCode(itemCode);
    }
}
