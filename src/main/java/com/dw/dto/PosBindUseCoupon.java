package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by liang.caixing on 2018/5/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PosBindUseCoupon {
    private String cardCoupon; //卡號或者券號
    private String couponType; //如果是券號，則記錄券類型
    private BigDecimal faceAmt; //如果是券，拆分出面值
    private BigDecimal realAmt; //如果是券,拆分實際金額
    private BigDecimal discAmt; //如果是券，拆分出折扣金額

    private String remark3;    //禮品券記錄商品編號

}
