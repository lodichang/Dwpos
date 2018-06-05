package com.dw.enums;

import com.dw.util.AppUtils;
import lombok.Getter;

@Getter
public enum LanguageEnum {
    ZH_HK("ZH_HK", "繁體中文", 0),
    ZH_CN("ZH_CN", "簡體中文", 1),
    EN("EN", "英文", 2),
    OTHER("OTHER", "其他语言", 3);

    private String value;
    private String name;
    private int mapFelid;

    LanguageEnum(String value, String name, int mapFelid) {
        this.value = value;
        this.name = name;
        this.mapFelid = mapFelid;
    }

    public static String getLanguage(String[] languages, String key) {
        LanguageEnum enums = AppUtils.getEnumByValue(LanguageEnum.class, key);
        return languages[enums.getMapFelid()];
    }
}

