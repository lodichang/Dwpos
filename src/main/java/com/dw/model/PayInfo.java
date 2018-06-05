package com.dw.model;

import com.dw.enums.BillTypeEnum;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;

import java.math.BigDecimal;

/**
 * 付款信息表
 */

public class PayInfo {
    private StringProperty stationId = new SimpleStringProperty("");
    private StringProperty period = new SimpleStringProperty("");
    private StringProperty refNum = new SimpleStringProperty("");
    private StringProperty refNumShow = new SimpleStringProperty("");
    private StringProperty tableNum = new SimpleStringProperty("");
    private StringProperty tranType = new SimpleStringProperty("");
    private StringProperty inTime = new SimpleStringProperty("");
    private StringProperty name1 = new SimpleStringProperty("");
    private StringProperty person= new SimpleStringProperty("");
    private DoubleProperty billAmt= new SimpleDoubleProperty(0.00);
    private DoubleProperty cashAmt = new SimpleDoubleProperty(0.00);
    private DoubleProperty otherAmt = new SimpleDoubleProperty(0.00);
    private DoubleProperty rounding = new SimpleDoubleProperty(0.00);
    private DoubleProperty orderDisc = new SimpleDoubleProperty(0.00);
    private DoubleProperty owed = new SimpleDoubleProperty(0.00);
    private StringProperty memNum = new SimpleStringProperty("");
    private StringProperty subRef = new SimpleStringProperty("");
    private DoubleProperty tips = new SimpleDoubleProperty(0.00);
    private BillTypeEnum billType;
    private StringProperty invoiceNumber = new SimpleStringProperty("");



    private DoubleProperty orderAmt = new SimpleDoubleProperty(0.00);
    private DoubleProperty servAmt = new SimpleDoubleProperty(0.00);

    public void setPayInfo(String stationId, String period, String refNum, String refNumShow, String tableNum, String tranType, String inTime, String name1, Integer person, BigDecimal billAmt, BigDecimal cashAmt, BigDecimal otherAmt, BigDecimal rounding, BigDecimal orderDisc, String subRef, BillTypeEnum billType,BigDecimal owed,BigDecimal orderAmt,BigDecimal servAmt,String invoiceNumber) {
        this.stationId.set(stationId);
        this.period.set(period);
        this.refNum.set(refNum);
        this.refNumShow.set(refNumShow);
        this.tableNum.set(tableNum);
        this.tranType.set(tranType);
        this.inTime.set(inTime);
        this.name1.set(name1);
        this.person.set(person.toString());
        this.billAmt.set(billAmt.doubleValue());
        this.cashAmt.set(cashAmt.doubleValue());
        this.otherAmt.set(otherAmt.doubleValue());
        this.rounding.set(rounding.doubleValue());
        this.orderDisc.set(orderDisc.doubleValue());
        //this.owed.set(0.00);
        this.owed.set(owed.doubleValue());
        this.memNum.set("");
        this.subRef.set(subRef);
        this.billType = billType;
        this.orderAmt.set(orderAmt.doubleValue());
        this.servAmt.set(servAmt.doubleValue());
        this.invoiceNumber.set(invoiceNumber);
    }

    public PayInfo(){}


    public String getStationId() {
        return stationId.get();
    }

    public StringProperty stationIdProperty() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId.set(stationId);
    }

    public String getPeriod() {
        return period.get();
    }

    public StringProperty periodProperty() {
        return period;
    }

    public void setPeriod(String period) {
        this.period.set(period);
    }

    public String getRefNum() {
        return refNum.get();
    }

    public StringProperty refNumProperty() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum.set(refNum);
    }

    public String getTableNum() {
        return tableNum.get();
    }

    public StringProperty tableNumProperty() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum.set(tableNum);
    }

    public String getTranType() {
        return tranType.get();
    }

    public StringProperty tranTypeProperty() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType.set(tranType);
    }

    public String getInTime() {
        return inTime.get();
    }

    public StringProperty inTimeProperty() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime.set(inTime);
    }

    public String getName1() {
        return name1.get();
    }

    public StringProperty name1Property() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1.set(name1);
    }

    public String getPerson() {
        return person.get();
    }

    public StringProperty personProperty() {
        return person;
    }

    public void setPerson(String person) {
        this.person.set(person);
    }

    public double getBillAmt() {
        return billAmt.get();
    }

    public DoubleProperty billAmtProperty() {
        return billAmt;
    }

    public void setBillAmt(double billAmt) {
        this.billAmt.set(billAmt);
    }

    public double getCashAmt() {
        return cashAmt.get();
    }

    public DoubleProperty cashAmtProperty() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt.set(cashAmt);
    }

    public double getOtherAmt() {
        return otherAmt.get();
    }

    public DoubleProperty otherAmtProperty() {
        return otherAmt;
    }

    public void setOtherAmt(double otherAmt) {
        this.otherAmt.set(otherAmt);
    }

    public double getRounding() {
        return rounding.get();
    }

    public DoubleProperty roundingProperty() {
        return rounding;
    }

    public void setRounding(double rounding) {
        this.rounding.set(rounding);
    }

    public double getOrderDisc() {
        return orderDisc.get();
    }

    public DoubleProperty orderDiscProperty() {
        return orderDisc;
    }

    public void setOrderDisc(double orderDisc) {
        this.orderDisc.set(orderDisc);
    }

    public double getOwed() {
        return owed.get();
    }

    public DoubleProperty owedProperty() {
        return owed;
    }

    public void setOwed(double owed) {
        this.owed.set(owed);
    }

    public String getMemNum() {
        return memNum.get();
    }

    public StringProperty memNumProperty() {
        return memNum;
    }

    public void setMemNum(String memNum) {
        this.memNum.set(memNum);
    }

    public String getRefNumShow() {
        return refNumShow.get();
    }

    public StringProperty refNumShowProperty() {
        return refNumShow;
    }

    public void setRefNumShow(String refNumShow) {
        this.refNumShow.set(refNumShow);
    }

    public String getSubRef() {
        return subRef.get();
    }

    public StringProperty subRefProperty() {
        return subRef;
    }

    public void setSubRef(String subRef) {
        this.subRef.set(subRef);
    }

    public BillTypeEnum getBillType() {
        return billType;
    }

    public void setBillType(BillTypeEnum billType) {
        this.billType = billType;
    }

    public double getOrderAmt() {
        return orderAmt.get();
    }

    public DoubleProperty orderAmtProperty() {
        return orderAmt;
    }

    public void setOrderAmt(double orderAmt) {
        this.orderAmt.set(orderAmt);
    }

    public double getServAmt() {
        return servAmt.get();
    }

    public DoubleProperty servAmtProperty() {
        return servAmt;
    }

    public void setServAmt(double servAmt) {
        this.servAmt.set(servAmt);
    }


    public double getTips() {
        return tips.get();
    }

    public DoubleProperty tipsProperty() {
        return tips;
    }

    public void setTips(double tips) {
        this.tips.set(tips);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }
}
