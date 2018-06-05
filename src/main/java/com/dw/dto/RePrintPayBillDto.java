package com.dw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lodi on 2018/4/27.
 */
@Setter
@Getter
public class RePrintPayBillDto extends PosPayBillDto {

    private String outletName;

    private String tel;

    private String tableNo;

}
