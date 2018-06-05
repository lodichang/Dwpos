package com.dw.model;

import javafx.beans.property.StringProperty;

/**
 * Created by lodi on 2018/2/24.
 */
public class ConsumerDetail {

    private StringProperty date;

    private StringProperty orderNo;

    private StringProperty tranType;

    private StringProperty branch;

    private StringProperty consumerAmount;

    private StringProperty givenAmount;

    private StringProperty givenIntergal;

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getOrderNo() {
        return orderNo.get();
    }

    public StringProperty orderNoProperty() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo.set(orderNo);
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

    public String getBranch() {
        return branch.get();
    }

    public StringProperty branchProperty() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch.set(branch);
    }

    public String getConsumerAmount() {
        return consumerAmount.get();
    }

    public StringProperty consumerAmountProperty() {
        return consumerAmount;
    }

    public void setConsumerAmount(String consumerAmount) {
        this.consumerAmount.set(consumerAmount);
    }

    public String getGivenAmount() {
        return givenAmount.get();
    }

    public StringProperty givenAmountProperty() {
        return givenAmount;
    }

    public void setGivenAmount(String givenAmount) {
        this.givenAmount.set(givenAmount);
    }

    public String getGivenIntergal() {
        return givenIntergal.get();
    }

    public StringProperty givenIntergalProperty() {
        return givenIntergal;
    }

    public void setGivenIntergal(String givenIntergal) {
        this.givenIntergal.set(givenIntergal);
    }


}
