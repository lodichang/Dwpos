package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/27.
 */
public enum AMTPROEnum {
    AMT("按實際金額","AMT"),
    PRO("按百分比","PRO");



    private String name;
    private String value;

    AMTPROEnum(String name,String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
