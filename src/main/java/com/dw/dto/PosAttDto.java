package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosAttDto implements Serializable {
    private String id;
    private String aGroup;
    private String outline;
    private String code;
    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String printDesc;
    private String billPrint;
    private String calType;
    private String amtPro;
    private BigDecimal calAmount;
    private BigDecimal calQty;
    private String stkType;
    private String ingType;
    private String canChange;
    private String remark1;
    private String remark2;
    private String remark3;
}
