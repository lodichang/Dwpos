package com.dw.enums;


import lombok.Getter;


@Getter
public enum PayMentEnum {
    CASH("現金","CASH"),
    OTHER("其他","OTHER");

    private String name;
    private String value;

    PayMentEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

