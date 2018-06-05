package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by li.yongliang on 2018/4/12.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TopButtonDto implements Serializable {

    //id
    private String id;
    //中文繁體
    private String deschk;
    //中文簡體
    private String desccn;
    //英文名稱
    private String descen;
    //其他語言
    private String descot;
    private String display;
    private String disseq;
    private String forecolor;
    private String backcolor;
    private String activePeriod;
    private String activeZone;
    private String startdate;
    private String enddate;
    private String day1;
    private String day2;
    private String day3;
    private String day4;
    private String day5;
    private String day6;
    private String day7;
    private String holiday;
    private String exHoliday;
    private String buttonImg;


    //菜品信息
    private List<PosItemDto> posItemDtoList = new LinkedList<>();
}
