package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/25.
 * 結賬類型
 */
public enum BillTypeEnum {
    ORDERBILL("正常拍腳","ORDERBILL"),
    ORDERHISBILL("歷史改腳","ORDERHISBILL"),
    OTHER("其他用途","OTHER");

    private String name;
    private String value;

    BillTypeEnum(String name, String value){
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
