package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by li.yongliang on 2018/5/3.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PosSetmealDetailDto implements Serializable {

    private String itemCode;
    private BigDecimal addPrice;
    private BigDecimal catPerc;
    private String canChange;
    private String itemDesc1;
    private String itemDesc2;
    private String itemDesc3;
    private String itemDesc4;
    //    商品圖片
    private String itemImg;
    private String itemPrn;

    private String discont;
}
