package com.dw.model;

import com.dw.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * Created by liang.caixing on 2018/4/25.
 */

public class CouponParm {

    private StringProperty tableNo = new SimpleStringProperty("");
    private StringProperty refNum = new SimpleStringProperty("");
    private StringProperty subRef = new SimpleStringProperty("");
    private StringProperty tIndex = new SimpleStringProperty("");
    private StringProperty inTime = new SimpleStringProperty("");

    public CouponParm() {
    }

    public void setCouponParm(String tableNo, String refNum, String subRef, Long tIndex, Date inTime) {
        this.tableNo.set(tableNo);
        this.refNum.set(refNum);
        this.subRef.set(subRef);
        this.tIndex.set(String.valueOf(tIndex));
        this.inTime.set(DateUtil.DateToString(inTime,"HH:mm"));
    }

    public String getTableNo() {
        return tableNo.get();
    }

    public StringProperty tableNoProperty() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo.set(tableNo);
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

    public String getSubRef() {
        return subRef.get();
    }

    public StringProperty subRefProperty() {
        return subRef;
    }

    public void setSubRef(String subRef) {
        this.subRef.set(subRef);
    }

    public String gettIndex() {
        return tIndex.get();
    }

    public StringProperty tIndexProperty() {
        return tIndex;
    }

    public void settIndex(String tIndex) {
        this.tIndex.set(tIndex);
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
}
