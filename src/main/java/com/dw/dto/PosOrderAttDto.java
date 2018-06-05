package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by li.yongliang on 2018/4/12.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosOrderAttDto implements Serializable {
    private String itemIdx;
    private String attCode;
    private String groupCode;
    private String actionCode;
    private String actionName;
    private String attName;
    private BigDecimal changeAmt;
}
