package com.dw.enums;

/**
 * Created by lodi on 2017/11/23.
 */
public  enum PageSizeEnum {
  FOUR(4);

    private Integer value;

    PageSizeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }





}
