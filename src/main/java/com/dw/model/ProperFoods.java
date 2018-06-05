package com.dw.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by lodi on 2018/2/2.
 */
public class ProperFoods {

    private StringProperty category;

    private StringProperty goodNo;

    private StringProperty goodName;

    private StringProperty price1;

    private StringProperty price2;

    private StringProperty price3;

    private StringProperty price4;

    private StringProperty price5;

    private StringProperty price6;

    private StringProperty price7;

    private StringProperty price8;

    private StringProperty price9;

    public ProperFoods(String category, String goodNo, String goodName, String price1, String price2, String price3, String price4, String price5, String price6, String price7, String price8, String price9) {
        this.category = new SimpleStringProperty(category);
        this.goodNo = new SimpleStringProperty(goodNo);
        this.goodName = new SimpleStringProperty(goodName);
        this.price1 = new SimpleStringProperty(price1);
        this.price2 = new SimpleStringProperty(price2);
        this.price3 = new SimpleStringProperty(price3);
        this.price4 = new SimpleStringProperty(price4);
        this.price5 = new SimpleStringProperty(price5);
        this.price6 = new SimpleStringProperty(price6);
        this.price7 = new SimpleStringProperty(price7);
        this.price8 = new SimpleStringProperty(price8);
        this.price9 = new SimpleStringProperty(price9);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getGoodNo() {
        return goodNo.get();
    }

    public StringProperty goodNoProperty() {
        return goodNo;
    }

    public void setGoodNo(String goodNo) {
        this.goodNo.set(goodNo);
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

    public String getPrice1() {
        return price1.get();
    }

    public StringProperty price1Property() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1.set(price1);
    }

    public String getPrice2() {
        return price2.get();
    }

    public StringProperty price2Property() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2.set(price2);
    }

    public String getPrice3() {
        return price3.get();
    }

    public StringProperty price3Property() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3.set(price3);
    }

    public String getPrice4() {
        return price4.get();
    }

    public StringProperty price4Property() {
        return price4;
    }

    public void setPrice4(String price4) {
        this.price4.set(price4);
    }

    public String getPrice5() {
        return price5.get();
    }

    public StringProperty price5Property() {
        return price5;
    }

    public void setPrice5(String price5) {
        this.price5.set(price5);
    }

    public String getPrice6() {
        return price6.get();
    }

    public StringProperty price6Property() {
        return price6;
    }

    public void setPrice6(String price6) {
        this.price6.set(price6);
    }

    public String getPrice7() {
        return price7.get();
    }

    public StringProperty price7Property() {
        return price7;
    }

    public void setPrice7(String price7) {
        this.price7.set(price7);
    }

    public String getPrice8() {
        return price8.get();
    }

    public StringProperty price8Property() {
        return price8;
    }

    public void setPrice8(String price8) {
        this.price8.set(price8);
    }

    public String getPrice9() {
        return price9.get();
    }

    public StringProperty price9Property() {
        return price9;
    }

    public void setPrice9(String price9) {
        this.price9.set(price9);
    }
}
