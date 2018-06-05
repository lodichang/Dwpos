package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by li.yongliang on 2018/5/3.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PosSetmealGroupDto implements Serializable {
    private String code;
    private String sGroup;
    private String gpDesc1;
    private String gpDesc2;
    private String gpDesc3;
    private String gpDesc4;
    private String gpCount;
    private String sCount;
    private Boolean isHoldOn;


    private List<PosSetmealDetailDto> setmealDetailList;

}
