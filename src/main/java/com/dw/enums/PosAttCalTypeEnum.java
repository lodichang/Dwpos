package com.dw.enums;


public  enum PosAttCalTypeEnum {
    ADD("加", "A"),
    SUBTRACT("减", "S"),
    MULTIPLY("乘", "M");

    private String name;
    private String value;

    PosAttCalTypeEnum(String name, String value) {
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
