package com.dw.enums;

public enum ItemIopenEnum {
    TRUE("TRUE", "入價錢"),
    FALSE("FALSE", "不入價錢");

    private String value;
    private String name;

    ItemIopenEnum(String value, String name) {
        this.name = name;
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
