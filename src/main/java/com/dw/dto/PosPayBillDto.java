package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class PosPayBillDto extends PosOrderDto {

    private List<String> payList;//订单详情

    private String billAmt;//发票金额

    private String printDate;//印单日期

    private String staffName;//收银名

    private String payAmt;//付款金额

    private String backAmt;//折扣金额

    private String refNum;//发票编号

    private String orderAmt;//食品金额
    private String orderDisc;//食品折扣
    private String rounding;//找零差额
    private String servAmt;//服务费


    private List<String> discList;//折扣詳情


}
