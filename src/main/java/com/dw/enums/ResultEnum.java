package com.dw.enums;


import lombok.Getter;


@Getter
public enum ResultEnum {
    YES("是","YES"),
    NO("否","NO"),
    REOPEN("重新开台","REOPEN");

    private String name;
    private String value;

    ResultEnum(String name,String value) {
        this.name = name;
        this.value = value;
    }
}

