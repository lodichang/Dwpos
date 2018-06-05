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
public class PosItemMustAttDto implements Serializable {
    private String attCode;
    private String attDesc1;
    private String attDesc2;
    private String attDesc3;
    private String attDesc4;
    private String attPrintDesc;
    private String attBillPrint;
    private String attCalType;
    private String attAmtPro;
    private BigDecimal attCalAmount;
    private String attStkType;
    private String attIngType;
    private String attCanChange;
    private String attRemark1;
    private String attRemark2;
    private String attRemark3;
    private String groupCode;
    private String groupDesc1;
    private String groupDesc2;
    private String groupDesc3;
    private String groupDesc4;
    private String groupPrintDesc;
    private String groupType;
    private int groupDisplay;
    private int groupSort;
    private String groupRemarks;
    private String groupCanChange;
    private String groupRemark1;
    private String groupRemark2;
    private String groupRemark3;
    private BigDecimal attCalQty;
}
