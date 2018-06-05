package com.dw.enums;

/**
 * Created by lodi on 2017/11/23.
 */
public  enum ColorEnum {
  ZERO(0),ONE(1),TWO(2),THREE(3),FOUR(4),FIVE(5),SIX(6),SEVEN(7),RIGHT(8),NINE(9);

    private Integer value;

    ColorEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }





}
