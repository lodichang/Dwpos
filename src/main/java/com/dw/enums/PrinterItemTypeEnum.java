package com.dw.enums;

public enum PrinterItemTypeEnum {
    //類型，ITEM-菜品，ATT-口味，KICM-廚房訊息
    ITEM("菜品","ITEM"),
    ATT("口味","ATT"),
    KICM("廚房訊息","KICM");

    private String name;
    private String value;

    PrinterItemTypeEnum(String name, String value){
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
