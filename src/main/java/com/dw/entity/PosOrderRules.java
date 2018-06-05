package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* tb_pos_order_rules系统订单明细表 实体类
*/
@TableName("tb_pos_order_rules")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosOrderRules implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @DateTimeFormat
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("PERSON_MIN")
    private String personMin; //最小人數
    @TableField("PERSON_MAX")
    private String personMax; //最大人數
    @TableField("MAX_QTY")
    private BigDecimal maxQty; //最大份數
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //

}
