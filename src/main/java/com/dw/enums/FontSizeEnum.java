package com.dw.enums;

/**
 * Created by lodi on 2017/11/23.
 */
public  enum FontSizeEnum {
  font14(14),font16(16),font18(18),font20(20),font22(22),font24(24),font28(28),font12(12),font10(10);

    private Integer value;

    FontSizeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }





}
