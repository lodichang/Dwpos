package com.dw.enums;

/**
 * Created by li.yongliang on 2018/4/25.
 * 點菜界面右邊菜單顯示枚舉
 */
public enum OrderRightPartEnum {
    ITEMCHOOSE("菜品選擇"),
    ATTMODIFY("口味修改"),
    COUPON("優惠券"),
    KEYBORAD("鍵盤");


    private String name;

    OrderRightPartEnum(String name){
        this.name = name;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
