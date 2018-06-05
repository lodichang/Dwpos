package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/27.
 */
public enum PrintOrderBillLanuageEnum {
    CHINESE("中文","CHINESE"),
    ENGLISH("英文","ENGLISH");



    private String name;
    private String value;

    PrintOrderBillLanuageEnum(String name, String value){
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
