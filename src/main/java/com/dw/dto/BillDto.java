package com.dw.dto;

import com.dw.enums.BillTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BillDto implements Serializable {
    private String stationId; //機器編號，非必傳
    private String period; //更次，非必傳
    private String refNumShow; //單號僅作顯示*必傳，查單用
    private String refNum;//單號*必傳，查單用
    private String subRef;//子單號*必傳
    private String tableNum;//檯號*必傳，查單用
    private String tranType;//賬單類型*必傳
    private Date inTime;//入座時間*必傳
    private Date inDate;//入座日期
    private String name1;// 員工名稱，非必傳
    private Integer person;//人數，*必傳，查單用
    private BigDecimal billAmt;//總付款*必傳，查單用
    private BigDecimal cashAmt;//現金付款*非必傳
    private BigDecimal otherAmt;//其他付款*非必傳
    private BigDecimal rounding;//找零差額，*必傳，查單用
    private String payDesc; //付款方式名稱，非必傳
    private BigDecimal orderDisc;//總折扣，*必傳 ，查單用
    private BigDecimal catDisc; //大類折扣，非必傳，查單用
    private BigDecimal itemDisc; //單項折扣，非必傳，查單用
    private BigDecimal orderAmt; //食品总金额，非必傳，查單用
    private BigDecimal servAmt;//服務費，非必傳，查單用
    private BigDecimal owed;//尚欠
    private BillTypeEnum billType; //結賬類型枚舉
    private String billStaff;//結賬員工編號
    private String settled;//拍脚标识
    private String invoiceNumber;//发票编号

    //正常結賬用的構造函數
    public BillDto(String refNumShow, String refNum, String subRef, String tableNum, String tranType, Date inTime, Integer person,BigDecimal orderAmt, BigDecimal billAmt, BigDecimal cashAmt, BigDecimal otherAmt, BigDecimal rounding, BigDecimal orderDisc, BillTypeEnum billType,BigDecimal owed,String billStaff,BigDecimal servAmt,Date inDate,String invoiceNumber) {
        this.refNumShow = refNumShow;
        this.refNum = refNum;
        this.subRef = subRef;
        this.tableNum = tableNum;
        this.tranType = tranType;
        this.inTime = inTime;
        this.person = person;
        this.orderAmt = orderAmt;
        this.billAmt = billAmt;
        this.cashAmt = cashAmt;
        this.otherAmt = otherAmt;
        this.rounding = rounding;
        this.orderDisc = orderDisc;
        this.billType = billType;
        this.owed = owed;
        this.billStaff = billStaff;
        this.servAmt = servAmt;
        this.inDate = inDate;
        this.invoiceNumber = invoiceNumber;
    }

    public BillDto(String refNumShow, String refNum, String subRef, String tableNum, String tranType, Date inTime, Integer person,BigDecimal orderAmt, BigDecimal billAmt, BigDecimal cashAmt, BigDecimal otherAmt, BigDecimal rounding, BigDecimal orderDisc, BillTypeEnum billType,BigDecimal owed,String billStaff,BigDecimal servAmt,String settled) {
        this.refNumShow = refNumShow;
        this.refNum = refNum;
        this.subRef = subRef;
        this.tableNum = tableNum;
        this.tranType = tranType;
        this.inTime = inTime;
        this.person = person;
        this.orderAmt = orderAmt;
        this.billAmt = billAmt;
        this.cashAmt = cashAmt;
        this.otherAmt = otherAmt;
        this.rounding = rounding;
        this.orderDisc = orderDisc;
        this.billType = billType;
        this.owed = owed;
        this.billStaff = billStaff;
        this.servAmt = servAmt;
        this.settled = settled;
    }

    public double getBillAmtDoubleVale() {
        return this.billAmt.doubleValue();
    }

    public double getCashAmttDoubleVale() {
        return this.cashAmt.doubleValue();
    }

    public double getOtherAmttDoubleVale() {
        return this.otherAmt.doubleValue();
    }

    public double getPersonDoubleValue(){ return this.person.doubleValue();}
}
