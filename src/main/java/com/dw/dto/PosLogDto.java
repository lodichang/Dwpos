package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosLogDto implements Serializable {
    private String id;
    private Date version = new Date();
    private String outlet;
    private Date tDate;
    private Date tTime;
    private String staff;
    private String logType;
    private String type;
    private String refNum;
    private String subRef;
    private String table1;
    private String table2;
    private Integer qty1;
    private Integer qty2;
    private Integer qty3;
    private BigDecimal amt1;
    private BigDecimal amt2;
    private BigDecimal amt3;
    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;
    private String member;
}
