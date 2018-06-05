package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderListDto implements Serializable {
    private String id;//order表Id
    private String refNum; //主单号
    private String subRef; //副单号
    private String tableNum; //桌号
    private String itemCode;
    private String desc1; //名称1
    private String desc2; //名称2
    private String desc3; //名称3
    private String desc4; //名称4
    private String attDesc1;//口味名稱
    private String attDesc2;//口味名稱
    private String attDesc3;//口味名稱
    private String attDesc4;//口味名稱
    private Integer qty; //数量
    private Integer cancel; //取消数量
    private Integer free; //免费数量
    private String staff; //操作员
    private String staffDesc; //操作员名稱
    private Date tTime; //入单时间
    private BigDecimal amt; //金额
    private BigDecimal orderDisc;//折扣金額
    private Long itemIdx; //商品流水号
    private String sepChar; //主套餐流水号
    private BigDecimal unitPrice; //单价
    private String kconfirm; //原价字段
    private BigDecimal changeAmt;//口味加价
    private String service; // SERVICE默认插入的是true
//    private Boolean comb; //組合屬性:0否1是
    private String attCode; //口味編號
    private String subtractQty; //口味數量
    private String settingPrice; //口味價格
    private String itemPrn;// 菜品指定的印记编号
    private String kicMess;//是否叫起
    private String setMealCode;//所属套餐编号


    private String setMeal;

    private String sgroup;

    private String combId;

    private String orderTime;//下单时间

    private String staffName;//下单员名称

    private String servCost;//服务费

    private Integer urgeCount; //追單數量

    private String cartId;

    private BigDecimal catDisc;
    private BigDecimal itemDisc;

    private String iPrint;//是否前台小票显示

    private String tDate;//下单日期字符串


}
