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
public class PosTranDto implements Serializable {
    private String id;
    private Date version = new Date();
    private String refNum;
    private String subRef;
    private String stationId;
    private String outlet;
    private String tableNum;
    private String tranType;
    private String openStaff;
    private String billStaff;
    private String payStaff;
    private String servStaff;
    private String leaveStaff;
    private Integer person;
    private Date inDate;
    private Date inTime;
    private Date billDate;
    private Date billTime;
    private Date leaveDate;
    private Date leaveTime;
    private String headCount;
    private BigDecimal orderAmt;
    private BigDecimal itemDisc;
    private BigDecimal catDisc;
    private BigDecimal roomAmt;
    private BigDecimal roomDisc;
    private BigDecimal headAmt;
    private BigDecimal headDisc;
    private BigDecimal servAmt;
    private BigDecimal taxAmt;
    private BigDecimal taxAmt1;
    private BigDecimal orderDisc;
    private BigDecimal rounding;
    private BigDecimal billAmt;
    private BigDecimal cashAmt;
    private BigDecimal otherAmt;
    private BigDecimal nsalesAmt;
    private BigDecimal overAmt;
    private String tips;
    private String member;
    private String payCount;
    private String voidRef;
    private String reason;
    private Integer tIndex;
    private String period;
    private String receipt;
    private String prtCount;
    private String paytype1;
    private String paytype2;
    private String paytype3;
    private String paytype4;
    private String paytype5;
    private String settled;
    private String printState;
    private String remarks;
}
