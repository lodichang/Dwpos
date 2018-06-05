package com.dw.enums;

/**
 * Created by lodi on 2017/11/23.
 */
public  enum FlowPaneTypeEnum {
  AREA(0),STREET(1),ADDRESS(2);

    private Integer value;

    FlowPaneTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }





}
