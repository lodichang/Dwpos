package com.dw.enums;

import lombok.Getter;

@Getter
public enum TranTypeEnum {
    N("N", "正常单"),
    A("A", "改单"),
    V("V", "消单"),
    R("R", "消单/改单的负记录");

    private String value;
    private String name;

    TranTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
}

