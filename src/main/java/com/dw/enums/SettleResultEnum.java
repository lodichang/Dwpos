package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/26.
 */
public enum SettleResultEnum {
    CAN_SETTLED("結賬正常完成",0),
    HAS_NO_ORDER("沒有點菜記錄",1),
    AMT_LESS_ZERO("賬單金額小於等於0",2),
    NO_REQUIRED("沒有必點菜品",3),
    CANCEL_SETTLED("取消結賬",4),
    ZERO_AMT("賬單金額為0",5),
    HOLDON("存在叫起菜品",6),
    CHECK_ERROR("CAN_SETTLED",-1);


    private String name;
    private int value;

    SettleResultEnum(String name,int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}

