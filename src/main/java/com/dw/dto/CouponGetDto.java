package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liang.caixing on 2018/5/4.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CouponGetDto {
    private String id;
    private String image;
    private String title;
    private String endTime;
    private String type;
    private String rate;
    private String couponNum;
}
