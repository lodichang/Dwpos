package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class PosOrderCashDto extends PosOrderDto {

    private List<String> orderList;

    private List<String> payList;

    private String itemDisc;

    private String catDisc;

    private String orderDisc;

    private String orderAmt;

    private String server;

    private String billAmt;

    private String change;

}
