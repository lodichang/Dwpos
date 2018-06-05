package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/25.
 * 食品修改類型
 */
public enum ItemModifyTypeEnum {
    PAUSE("菜品暫停"),
    PRICE("價格"),
    DESC("改名字"),
    LAYOUT("佈局設計"),
    STOCK("扣燉");


    private String name;

    ItemModifyTypeEnum(String name) {
        this.name = name;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
