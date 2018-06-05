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
* 预设折扣明细表 实体类
*/
@TableName("tb_pos_discdet")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosDiscdet implements Serializable{
    @TableField("id")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("CODE")
    private String code; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("TYPE")
    private String type; //
    @TableField("DETAIL")
    private String detail; //
    @TableField("D_VALUE")
    private BigDecimal dValue; //
    @TableField("D_OPEN")
    private String dOpen; //
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
