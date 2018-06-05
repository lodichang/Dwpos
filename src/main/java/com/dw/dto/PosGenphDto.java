package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by li.yongliang on 2018/4/14.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosGenphDto implements Serializable {

    private String id; //

    private Date version; //

    private String outlet; //分店编号

    private String outline; //分店名称

    private Date gdate; //日期

    private String gcount; //星期几

    private BigDecimal service; //服务费

    private BigDecimal tax; //税率1

    private BigDecimal tax1; //税率2

    private BigDecimal headFee1; //时段1收费

    private Date headTime1; //时段1

    private BigDecimal headFee2; //时段2收费

    private Date headTime2; //时段2

    private BigDecimal headFee3; //时段3收费

    private Date headTime3; //时段3

    private String headDesc1; //入场费名称1

    private String headDesc2; //入场费名称2

    private Date roomTime1; //包房时段1

    private String roomDisc1; //包房时段名称

    private Date roomTime2; //包房时段2

    private String roomDisc2; //包房时段2名称

    private Date roomTime3; //包房时段3

    private String roomDisc3; //包房时段3名称

    private Date period1; //时段1

    private Date period2; //时段2

    private Date period3; //时段3

    private Date period4; //时段4

    private Date period5; //时段5

    private Date period6; //时段6

    private Date period7; //时段7

    private Date period8; //时段8

    private Date period9; //时段9

    private Date dayEnd; //营业结束时间

    private String freeTime; //

    private String period; //

    private String allowance; //

    private String minTime; //

    private String pricetype; //收费类别

    private String remarks; //备注

    private String type; //

    private String priority; //

    private String updatedBy; //修改人员工编号

    private String remark1; //备注1

    private String remark2; //备注2

    private String remark3; //备注3

}
