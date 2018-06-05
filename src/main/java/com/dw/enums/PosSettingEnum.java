package com.dw.enums;


import lombok.Getter;


@Getter
public enum PosSettingEnum {
    language("language"),
    languagedefault("languagedefault"),
    //找零差額配置默認參數
    rounding_default("rounding_default"),
    //系統全局計算金額的小數位數
    decimal_num("decimal_num"),
    //結賬時收取客人金額時的金額為小數位數
    settledecimal_num("settledecimal_num"),

    outlet("outlet");
    private String value;

    PosSettingEnum(String value) {
        this.value = value;
    }
}

