package com.dw.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liang.caixing on 2018/4/28.
 */
public enum HoldOnEnum {
    CANCELCALL("取消叫起或者未叫起", 0),
    HOLDON("等叫起", 1),
    SERVE("即起", 2);

    private String name; //描述
    private int value; //值

    HoldOnEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static HoldOnEnum getEnumByValue(int value) {
        switch (value) {
            case 0:
                return CANCELCALL;
            case 1:
                return HOLDON;
            case 2:
                return SERVE;
            default:
                return CANCELCALL;
        }
    }

}
