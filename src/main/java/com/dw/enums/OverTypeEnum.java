package com.dw.enums;

/**
 * Created by li.yongliang on 2018/5/14.
 */
public enum OverTypeEnum {
    T("小費","T"),
    C("現金找零","C"),
    O("記賬多付","O"),
    N("等值金額","N");

    private String name;
    private String value;

    OverTypeEnum(String name,String value){
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
