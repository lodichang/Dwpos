package com.dw.model;

import com.dw.enums.BillTypeEnum;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.DoubleConsumer;

/**
 * Created by liang.caixing on 2018/4/25.
 */
public class SettleInfo {

    private String stationId; //機器編號，非必傳
    private String period; //更次，非必傳
    private String refNum;//單號*必傳，查單用
    private String subRef;//子單號*必傳
    private String tranType;//賬單類型*必傳
    private Date inTime;//入座時間*必傳
    private String name1;// 員工名稱，非必傳
    private Integer person;//人數，*必傳，查單用
    private BigDecimal cashAmt;//現金付款*必傳
    private BigDecimal otherAmt;//其他付款*必傳
    //private BigDecimal rounding;//找零差額，*必傳，查單用
    private String payDesc; //付款方式名稱，非必傳
    private BigDecimal catDisc; //大類折扣，非必傳，查單用
    private BigDecimal itemDisc; //單項折扣，非必傳，查單用
    private BillTypeEnum billType; //結賬類型枚舉
    private String billStaff;//結賬員工編號
    private Date inDate;//开台时间
    private String invoiceNumber;//发票编号



    //檯號*必傳，查單用
    private SimpleStringProperty tableNo = new SimpleStringProperty("");
    private SimpleDoubleProperty orderAmt = new SimpleDoubleProperty(0.00);
    private SimpleDoubleProperty servAmt = new SimpleDoubleProperty(0.00);
    private SimpleDoubleProperty orderDisc = new SimpleDoubleProperty(0.00);
    private SimpleDoubleProperty billAmt = new SimpleDoubleProperty(0.00);
    private SimpleDoubleProperty rounding = new SimpleDoubleProperty(0.00);


    public SettleInfo() {
    }

    public void setSettleInfo(String stationId,String period,String refNum,String subRef,String tranType,Date inTime,String name1,Integer person,BigDecimal cashAmt,BigDecimal otherAmt,Double rounding,
                              String payDesc,BigDecimal catDisc,BigDecimal itemDisc,BillTypeEnum billType,String tableNo,Double orderAmt, Double servAmt,Double orderDisc,Double billAmt,String billStaff,Date indate,String invoiceNumber){
        this.stationId = stationId;
        this.period = period;
        this.refNum = refNum;
        this.subRef = subRef;
        this.tranType = tranType;
        this.inTime = inTime;
        this.name1 = name1;
        this.person = person;
        this.cashAmt = cashAmt;
        this.otherAmt = otherAmt;
        this.rounding.set(rounding);
        this.payDesc = payDesc;
        this.catDisc = catDisc;
        this.itemDisc = itemDisc;
        this.billType  = billType;
        this.billStaff = billStaff;
        this.inDate = indate;
        this.tableNo.set(tableNo);
        this.orderAmt.set(orderAmt);
        this.servAmt.set(servAmt);
        this.orderDisc.set(orderDisc);
        this.billAmt.set(billAmt);
        this.invoiceNumber = invoiceNumber;
    }


    public String getTableNo() {
        return tableNo.get();
    }

    public SimpleStringProperty tableNoProperty() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo.set(tableNo);
    }

    public double getOrderAmt() {
        return orderAmt.get();
    }

    public SimpleDoubleProperty orderAmtProperty() {
        return orderAmt;
    }

    public void setOrderAmt(double orderAmt) {
        this.orderAmt.set(orderAmt);
    }

    public double getServAmt() {
        return servAmt.get();
    }

    public SimpleDoubleProperty servAmtProperty() {
        return servAmt;
    }

    public void setServAmt(double servAmt) {
        this.servAmt.set(servAmt);
    }

    public double getOrderDisc() {
        return orderDisc.get();
    }

    public SimpleDoubleProperty orderDiscProperty() {
        return orderDisc;
    }

    public void setOrderDisc(double orderDisc) {
        this.orderDisc.set(orderDisc);
    }

    public double getBillAmt() {
        return billAmt.get();
    }

    public SimpleDoubleProperty billAmtProperty() {
        return billAmt;
    }

    public void setBillAmt(double billAmt) {
        this.billAmt.set(billAmt);
    }
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getSubRef() {
        return subRef;
    }

    public void setSubRef(String subRef) {
        this.subRef = subRef;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public BigDecimal getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(BigDecimal cashAmt) {
        this.cashAmt = cashAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }


    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public BigDecimal getCatDisc() {
        return catDisc;
    }

    public void setCatDisc(BigDecimal catDisc) {
        this.catDisc = catDisc;
    }

    public BigDecimal getItemDisc() {
        return itemDisc;
    }

    public void setItemDisc(BigDecimal itemDisc) {
        this.itemDisc = itemDisc;
    }

    public BillTypeEnum getBillType() {
        return billType;
    }

    public void setBillType(BillTypeEnum billType) {
        this.billType = billType;
    }

    public String getBillStaff() {
        return billStaff;
    }

    public void setBillStaff(String billStaff) {
        this.billStaff = billStaff;
    }
    public double getRounding() {
        return rounding.get();
    }

    public SimpleDoubleProperty roundingProperty() {
        return rounding;
    }

    public void setRounding(double rounding) {
        this.rounding.set(rounding);
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
