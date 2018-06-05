package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class PosOrderDetailDto extends PosOrderDto {

    private List<String> orderList;//订单详情

    private String orderCount;//合计项目

    private String orderAmt;//小计

    private String billAmt;//总金额

    private String tranCode;//条码 规则：分店编号+订单编号+子单号+类型

    private String servCost;//服务费

    private String discAmt;//折扣金额

    private String isUpdate;//更改标识

    private String tableNum;


}
