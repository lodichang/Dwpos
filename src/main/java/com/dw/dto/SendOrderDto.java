package com.dw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendOrderDto {
    private String id;//order表Id
    private String itemCode;//商品編號
    private String itemName; // 食品名稱
    private String service;//是否收取服務費，TRUE是，FALSE否
    private int qty;// 點餐數量
    private BigDecimal amt; // //總價
    private String itemKicMsg;// 廚房訊息
    private String orgPrice; //商品原價
    private BigDecimal attAmt;//口味总价
    private BigDecimal price;//商品單價
    private String[] attName;//食品口味名稱
    private String[] attCode;//食品口味编号
    private String[] attGroup;//共同口味编号
    private String[] actionCode;//食品口味動作編號，加，少，轉，凈，走等
    private String[] attPrices;//口味價錢
    private boolean isPrinter = true; // 是否後廚除非，默認除非
    private String printerCode; // 印機編號，多個","分隔
    private String mealCode;//所属套餐编号
    private Boolean isMeal;//是否为套餐
    private String sgroup;//组别
    private String setmealItemIdx; // 套餐第二次送單需要設置第一次送單的主套餐的itemIdx
    private String cartId;
    private BigDecimal itemDisc;
    private BigDecimal catDisc;
    private String speciPrintId; //指定打印機編號
    private String itemIdx;
    private BigDecimal servAmt;//服務費
    private int holdOn;
    private String iopen;
    private String tDate;
}
