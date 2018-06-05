package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class PosOrderBillDto extends PosOrderDto {

    private List<String> orderList;//订单详情

    private String orderCount;//合计项目

    private String orderAmt;//小计

    private String billAmt;//总金额

    private String everyOneAmt;//人均消费

    private String inDate;//入座时间

    private String billDate;//结账时间

    private String staffName;//收银名

    private String tranCode;//条码

    private String servCost;//服务费

    private String discAmt;//折扣金额

    private List<String> posOrderCancle;//取消列表

    private String overAmt;//找零差额

    private String  invoiceNumber;//发票编号


}
