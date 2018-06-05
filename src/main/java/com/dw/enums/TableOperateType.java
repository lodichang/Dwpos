package com.dw.enums;


public enum TableOperateType {
    ADD("添加桌台","add"),
    UPDATE("修改桌台","update"),
    DELETE("刪除桌台","delete");

    private final String name;
    private final String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    TableOperateType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
