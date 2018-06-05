package com.dw.model;

import javafx.beans.property.StringProperty;

/**
 * Created by lodi on 2018/2/24.
 */
public class StandbyMoney {

    private StringProperty machineNo;

    private StringProperty time;

    private StringProperty serialNumber;

    private StringProperty reason;

    private StringProperty silverHead;

    private StringProperty delivery;

    public String getMachineNo() {
        return machineNo.get();
    }

    public StringProperty machineNoProperty() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo.set(machineNo);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public StringProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public String getReason() {
        return reason.get();
    }

    public StringProperty reasonProperty() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public String getSilverHead() {
        return silverHead.get();
    }

    public StringProperty silverHeadProperty() {
        return silverHead;
    }

    public void setSilverHead(String silverHead) {
        this.silverHead.set(silverHead);
    }

    public String getDelivery() {
        return delivery.get();
    }

    public StringProperty deliveryProperty() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery.set(delivery);
    }
}
