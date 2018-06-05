package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 食品口味對應特別處理表 实体类
*/
@TableName("tb_pos_attsetting")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosAttSetting implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet;
    @TableField("A_GROUP")
    private String aGroup; //共同口味编号或食品编号
    @TableField("CODE")
    private String code; //共同口味子编号
    @TableField("ACTION_CODE")
    private String actionCode; //共同口味子编号特別處理編號
    @TableField("CAL_AMOUNT")
    private BigDecimal calAmount; //计算金額
    @TableField("CAL_QTY")
    private BigDecimal calQty; //减数量
    @TableField("REMARK1")
    private String remark1; //备注1
    @TableField("REMARK2")
    private String remark2; //备注2
    @TableField("REMARK3")
    private String remark3; //备注3
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =  FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =  FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
}
