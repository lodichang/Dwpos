package com.dw.enums;

/**
 * Created by lodi on 2017/11/23.
 */
public  enum PosAttActionStatusEnum {
    SHOW("點菜時顯示", "TRUE"),
    HIDDEN("點菜時隱藏", "FALSE");

    private String name;
    private String value;

    PosAttActionStatusEnum(String name, String value) {
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
