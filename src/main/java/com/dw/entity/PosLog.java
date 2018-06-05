package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 日志表 实体类
*/
@TableName("tb_pos_log")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosLog implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("OUTLET")
    private String outlet; //
    @TableField("T_DATE")
    private Date tDate; //
    @TableField("T_TIME")
    private Date tTime; //
    @TableField("STAFF")
    private String staff; //
    @TableField("LOG_TYPE")
    private String logType; //
    @TableField("TYPE")
    private String type; //
    @TableField("T_INDEX")
    private Long tIndex; //
    @TableField("REF_NUM")
    private String refNum; //
    @TableField("SUB_REF")
    private String subRef; //
    @TableField("TABLE1")
    private String table1; //
    @TableField("TABLE2")
    private String table2; //
    @TableField("QTY1")
    private Integer qty1; //
    @TableField("QTY2")
    private Integer qty2; //
    @TableField("QTY3")
    private Integer qty3; //
    @TableField("AMT1")
    private BigDecimal amt1; //
    @TableField("AMT2")
    private BigDecimal amt2; //
    @TableField("AMT3")
    private BigDecimal amt3; //
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField("REMARK4")
    private String remark4; //
    @TableField("MEMBER")
    private String member;
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
