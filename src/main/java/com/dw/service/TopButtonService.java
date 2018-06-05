package com.dw.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dw.dto.TopButtonDto;
import com.dw.entity.PosButton;
import com.dw.mapper.TopButtonMapper;
import com.dw.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopButtonService extends ServiceImpl<TopButtonMapper, PosButton> {


    public List<TopButtonDto> getTopButtonList(String outlet, int dayWeek, String priceCode, String periodCode) {
        List<TopButtonDto> topButtonDtoList = baseMapper.getTopButtonList(outlet, dayWeek, priceCode);
        if (!periodCode.equals("ALL")) {
            try {
                List<TopButtonDto> topButtonPeriodList = topButtonDtoList.stream().filter(s -> s.getActivePeriod().contains(periodCode)).collect(Collectors.toList());
                if (AppUtils.isNotBlank(topButtonPeriodList)) {
                    return topButtonPeriodList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return topButtonDtoList;
    }

    /**
     * 获取点菜数据,没有缓存版本
     *
     * @param outlet
     * @param dayWeek
     * @param priceCode
     * @param periodCode
     * @return
     */
    public List<TopButtonDto> getTopButtonListNoCache(String outlet, int dayWeek, String priceCode, String periodCode) {
        List<TopButtonDto> topButtonDtoList = baseMapper.getTopButtonListNoCache(outlet, dayWeek, priceCode);
        if (!periodCode.equals("ALL")) {
            try {
                List<TopButtonDto> topButtonPeriodList = topButtonDtoList.stream().filter(s -> s.getActivePeriod().contains(periodCode)).collect(Collectors.toList());
                if (AppUtils.isNotBlank(topButtonPeriodList)) {
                    return topButtonPeriodList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return topButtonDtoList;
    }


}
