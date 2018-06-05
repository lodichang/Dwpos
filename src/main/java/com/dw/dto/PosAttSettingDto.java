package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by li.yongliang on 2018/4/16.
 * @author
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PosAttSettingDto implements Serializable {

    private String aGroup;
    private String code;
    private String actionCode;
    private BigDecimal calAmount;
    private BigDecimal calQty;
    private String remark1;
    private String remark2;
    private String remark3;
}
