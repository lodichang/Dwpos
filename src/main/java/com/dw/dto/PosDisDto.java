package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liang.caixing on 2018/4/28.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosDisDto {
    private String code;
    private String disName;
    private String sdate;
    private String edate;
    private String allUse;
    private String type;
    private String detail;
    private String dvalue;

}
