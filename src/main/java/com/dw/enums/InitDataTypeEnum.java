package com.dw.enums;

/**
 * Created by dwtech on 2018/5/31.
 */
public enum InitDataTypeEnum {
    BARCODE("掃條碼結賬","BARCODE"),
    SETTLE("點菜界面結賬","SETTLE");

    InitDataTypeEnum(String name, String value){
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

    private String name;
    private String value;
}
