package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* pos系统订单付款表 实体类
*/
@TableName("tb_pos_pay")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPay implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("OUTLINE")
    private String outline; //分店编号
    @TableField("STATION")
    private String station; //机器编号
    @TableField("REF_NUM")
    private String refNum; //账单编号
    @TableField("SUB_REF")
    private String subRef; //副编号
    @TableField("R_INDEX")
    private Long rIndex; //付款流水号
    @TableField("TRAN_TYPE")
    private String tranType; //付款记录类型
    @TableField("STAFF")
    private String staff; //操作员
    @TableField("SEAT")
    private String seat; //
    @TableField("PAY_TYPE")
    private String payType; //付款方式
    @TableField("INPUT_TYPE")
    private String inputType; //
    @TableField("R_DATE")
    private Date rDate; //付款日期
    @TableField("R_TIME")
    private Date rTime; //付款时间
    @TableField("BILL_AMT")
    private BigDecimal billAmt; //账单金额
    @TableField("QTY")
    private Integer qty; //数量
    @TableField("AMOUNT")
    private BigDecimal amount; //账单金额
    @TableField("CURRENCY")
    private String currency; //
    @TableField("EX_RATE")
    private BigDecimal exRate; //
    @TableField("NET_AMOUNT")
    private BigDecimal netAmount; //付款金额
    @TableField("TIPS")
    private BigDecimal tips; //消费
    @TableField("SERVICE")
    private BigDecimal service; //服务器
    @TableField("OVER_AMT")
    private BigDecimal overAmt; //超付金额
    @TableField("ROOM")
    private String room; //
    @TableField("CARD_NO")
    private String cardNo; //卡号
    @TableField("CUST_NAME")
    private String custName; //
    @TableField("EXP_DATE")
    private String expDate; //
    @TableField("APP_NO")
    private String appNo; //
    @TableField("TROUTD")
    private String troutd; //
    @TableField("SHIFT")
    private String shift; //
    @TableField("COVERS")
    private String covers; //
    @TableField("TAX_REF")
    private String taxRef; //
    @TableField("RD")
    private String rd; //
    @TableField("VOID_INDEX")
    private String voidIndex; //
    @TableField("REMARKS")
    private String remarks; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //



    public PosPay(String id, String outlet, String station, String refNum, String subRef, String tranType, String payType, BigDecimal billAmt, Integer qty, BigDecimal amount, BigDecimal netAmount, BigDecimal overAmt, String room, Date rTime, Date rDate, String staff, String lastUpdateNameId, Date lastUpdateTime) {
        this.id = id;
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
        this.staff = staff;
        this.lastUpdateNameId = lastUpdateNameId;
        this.lastUpdateTime = lastUpdateTime;


    }

    public PosPay( String outlet, String station, String refNum, String subRef, String tranType, String payType, BigDecimal billAmt, Integer qty, BigDecimal amount, BigDecimal netAmount, BigDecimal overAmt, String room, Date rTime, Date rDate, String staff,long rIndex, String lastUpdateNameId, Date lastUpdateTime,BigDecimal tips) {

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
        this.staff = staff;
        this.rIndex = rIndex;
        this.lastUpdateNameId = lastUpdateNameId;
        this.lastUpdateTime = lastUpdateTime;
        this.tips = tips;

    }

    public double getAmtountDoubleVale() {
        return this.amount.doubleValue();
    }
}
