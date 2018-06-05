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
public class PosStaffDto implements Serializable {
    private String code;
    private String password;
    private String name1;
    private String name2;
    private String freeType;
    private String freeDef;
    private String freeStart;
    private BigDecimal maxAmt;
    private BigDecimal maxDisc;
    private String cardCode;



}
