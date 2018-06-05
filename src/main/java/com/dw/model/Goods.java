package com.dw.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by lodi on 2018/1/10.
 */
public class Goods implements Serializable {

    private StringProperty number;

    private StringProperty goodName;

    private StringProperty price;

    private StringProperty message;


    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getGoodName() {
        return goodName.get();
    }

    public StringProperty goodNameProperty() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName.set(goodName);
    }


    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public Goods(String number, String goodName, String price, String message) {
        this.number = new SimpleStringProperty(number);
        this.goodName = new SimpleStringProperty(goodName);
        this.price = new SimpleStringProperty(price);
        this.message = new SimpleStringProperty(message);
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
