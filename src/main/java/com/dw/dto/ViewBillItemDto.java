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
public class ViewBillItemDto implements Serializable {
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


}
