package com.dw.model;

import javafx.beans.property.StringProperty;

/**
 * Created by lodi on 2018/3/6.
 */
public class OrderDetail {

    private StringProperty seat;

    private StringProperty serialNumber;

    private StringProperty dish;

    private StringProperty  taste;

    private StringProperty number;

    private StringProperty eliminate;

    private StringProperty free;

    private StringProperty staffer;

    private StringProperty time;

    private StringProperty  price;

    public String getSeat() {
        return seat.get();
    }

    public StringProperty seatProperty() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat.set(seat);
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

    public String getDish() {
        return dish.get();
    }

    public StringProperty dishProperty() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish.set(dish);
    }

    public String getTaste() {
        return taste.get();
    }

    public StringProperty tasteProperty() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste.set(taste);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getEliminate() {
        return eliminate.get();
    }

    public StringProperty eliminateProperty() {
        return eliminate;
    }

    public void setEliminate(String eliminate) {
        this.eliminate.set(eliminate);
    }

    public String getFree() {
        return free.get();
    }

    public StringProperty freeProperty() {
        return free;
    }

    public void setFree(String free) {
        this.free.set(free);
    }

    public String getStaffer() {
        return staffer.get();
    }

    public StringProperty stafferProperty() {
        return staffer;
    }

    public void setStaffer(String staffer) {
        this.staffer.set(staffer);
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

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }
}
