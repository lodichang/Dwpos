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
public class CouponDto {
    private String id;
    private String couponType;
    private String couponCode;
    private String couponName;
    private String useTime;
    private String disAmt;

}
