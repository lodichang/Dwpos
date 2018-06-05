package com.dw.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.Main;
import com.dw.dto.PosItemDto;
import com.dw.entity.PosItem;
import com.dw.entity.PosOrder;
import com.dw.entity.PosSuspendi;
import com.dw.entity.PosTAtt;
import com.dw.enums.ItemDiscontEnum;
import com.dw.mapper.PosItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PosItemService extends ServiceImpl<PosItemMapper, PosItem> {

    @Autowired
    private PosSuspendiService posSuspendiService;

    public PosItem selectByCode(String itemCode) {
        return baseMapper.selectByCode(itemCode);
    }

    public List<PosItemDto> getItems(String outlet, String priceCode) {
        return baseMapper.getItems(outlet, priceCode);
    }

    public PosItemDto searchItem(String outlet, String priceCode, String itemCode) {
        return baseMapper.searchItem(outlet, priceCode, itemCode);
    }

    /**
     * 暂停食品或开启
     *
     * @param posItemDto
     */
    public void pauseOrOpen(PosItemDto posItemDto) {
        if (posItemDto.getDiscont().equals(ItemDiscontEnum.PAUSE.getValue())) {


            baseMapper.disCountItem(posItemDto.getItemCode(), ItemDiscontEnum.SELLING.getValue());
            Wrapper<PosSuspendi> suspendiWrapper = new EntityWrapper<>();
            suspendiWrapper.eq("OUTLET", Main.posOutlet);
            suspendiWrapper.eq("ITEM_CODE", posItemDto.getItemCode());
            posSuspendiService.delete(suspendiWrapper);


        } else if (posItemDto.getDiscont().equals(ItemDiscontEnum.SELLING.getValue())) {

            baseMapper.disCountItem(posItemDto.getItemCode(), ItemDiscontEnum.PAUSE.getValue());
            PosSuspendi posSuspendi = new PosSuspendi();
            posSuspendi.setItemCode(posItemDto.getItemCode());
            posSuspendi.setOutlet(Main.posOutlet);
            posSuspendi.setUpdatedBy(Main.posStaff.getCode());
            posSuspendi.setLastUpdateNameId(Main.posStaff.getCode());
            posSuspendi.setLastUpdateTime(new Date());
            posSuspendiService.insert(posSuspendi);
        }
    }
}
