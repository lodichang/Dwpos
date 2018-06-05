package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2018/5/26.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PeriodReportDto implements Serializable{
    private String id;
    private String scat;
    private String scatname;
    private String period;
    private String periodname;
    private String singleamt;
    private String amt;
    private String pro;
}

