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
public class PosPayDto implements Serializable {

    private String id; //

    private String outlet; //分店编号


    private String station; //机器编号

    private String refNum; //账单编号

    private String subRef; //副编号

    private Long rIndex; //付款流水号

    private String tranType; //付款记录类型

    private String staff; //操作员

    private String seat; //

    private String payType; //付款方式
    private String inputType; //
    private Date rDate; //付款日期
    private Date rTime; //付款时间

    private BigDecimal billAmt; //账单金额

    private Integer qty; //数量

    private BigDecimal amount; //账单金额

    private String currency;

    private BigDecimal exRate;

    private BigDecimal netAmount; //付款金额

    private BigDecimal tips; //消费

    private BigDecimal service; //服务器

    private BigDecimal overAmt; //超付金额

    private String room;

    private String cardNo; //卡号

    private String custName;

    private String expDate;

    private String appNo;

    private String troutd;

    private String shift;

    private String covers;

    private String taxRef;

    private String rd;

    private String voidIndex;

    private String remarks;

    private String payDisc;

    private String payStaff;

    public PosPayDto(String outlet, String station, String refNum, String subRef, String tranType, String payType, BigDecimal billAmt, Integer qty, BigDecimal amount, BigDecimal netAmount, BigDecimal overAmt, String room, Date rTime, Date rDate, String payDisc, String payStaff, String staff,BigDecimal tips) {
        this.outlet = outlet;
        this.station = station;
        this.refNum = refNum;
        this.subRef = subRef;
        this.tranType = tranType;
        this.payType = payType;
        this.billAmt = billAmt;
        this.qty = qty;
        this.amount = amount;
        this.netAmount = netAmount;
        this.overAmt = overAmt;
        this.room = room;
        this.rTime = rTime;
        this.rDate = rDate;
        this.payDisc = payDisc;
        this.payStaff = payStaff;
        this.staff = staff;
        this.tips  = tips;
    }



    public double getAmtountDoubleVale() {
        return this.amount.doubleValue();
    }

    public double getTipsDoubleVale() {
        return this.tips.doubleValue();
    }
}
