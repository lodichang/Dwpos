package com.dw.dto;

import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by lodi on 2017/12/20.
 */
public class AnnouncementDto implements Serializable {

     private StringProperty goodCodeName;



    public StringProperty goodCodeNameProperty() {
        return goodCodeName;
    }

    public void setGoodCodeName(String goodCodeName) {
        this.goodCodeName.set(goodCodeName);
    }
}
