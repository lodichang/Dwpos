package com.dw.enums;

/**
 * Created by li.yongliang on 2018/5/22.
 */
public enum SplitTableClickEnum {
    SINGLE("沒有分臺點擊","SINGLE"),
    SPLITTABLE("有分臺點擊","SPLITTABLE"),
    SPLITSINGLETABLE("有分臺分臺頁面內點擊","SPLITSINGLETABLE");



    private String name;
    private String value;
    SplitTableClickEnum(String name,String value){
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
