package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* pos优惠券表 实体类
*/
@TableName("tb_pos_coupon")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosCoupon implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version; //
    @TableField("CODE")
    private String code; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("DESC1")
    private String desc1; //
    @TableField("DESC2")
    private String desc2; //
    @TableField("DISC_AMT")
    private BigDecimal discAmt; //
    @TableField("DISC_PERC")
    private BigDecimal discPerc; //
    @TableField("SELL_PRICE")
    private BigDecimal sellPrice; //
    @TableField("ALL_ITEM")
    private String allItem; //
    @TableField("SDATE")
    private Date sdate; //
    @TableField("EDATE")
    private Date edate; //
    @TableField("C_INDEX")
    private String cIndex; //
    @TableField("INCLUDE")
    private String include; //
    @TableField("REMARKS")
    private String remarks; //
    @TableField("DISC")
    private String disc; //
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号     
    @TableField("REMARK1")
    private String remark1; //备注1              
    @TableField("REMARK2")
    private String remark2; //备注2              
    @TableField("REMARK3")
    private String remark3; //备注3              
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
