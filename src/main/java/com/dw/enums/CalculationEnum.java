package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/24.
 * 在點菜界面修改數量或者修改單價以後對食品總價進行修改。這個枚舉用來標記重新計價方式
 */
public enum CalculationEnum {
    NUMBER("按數量重算總價","NUMBER"),
    UNITPRICE("按單價重算總價","UNITPRICE");

    private String name;
    private String value;



    CalculationEnum(String name, String value){
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
