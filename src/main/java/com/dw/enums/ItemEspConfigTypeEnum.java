package com.dw.enums;

/**
 * Created by liang.caixing on 2018/5/2.
 * 分店食品價格設定
 */
public enum ItemEspConfigTypeEnum {
    TRUE("TRUE", "是"),FALSE("FALSE", "否"),Z("Z", "跟食品設定");


    private String name; //描述
    private String value; //值

    ItemEspConfigTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }



    // 根据value返回枚举类型,主要在switch中使用
    public static ItemEspConfigTypeEnum getByValue(String value) {
        for (ItemEspConfigTypeEnum code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }

        }
        return null;
    }
}
