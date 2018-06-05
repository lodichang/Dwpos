package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by liang.caixing on 2018/5/4.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class MemberTranDto {

    private String billDate;
    private String refnum;
    private String tranType;
    private String outlet;
    private BigDecimal billBalance;
    private BigDecimal billGiftBalance;
    private Integer billIntegral;


}
