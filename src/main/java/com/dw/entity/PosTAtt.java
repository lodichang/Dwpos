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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* T_ATT订单商品口味表 实体类
*/
@TableName("tb_pos_t_att")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTAtt implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("ITEM_IDX")
    private Long itemIdx; //订单明细商品流水号
    @TableField("ATT_IDX")
    private String attIdx; //同一商品多个口味流水号
    @TableField("ATT_GROUP")
    private String attGroup; //共同口味编号
    @TableField("ATT_CODE")
    private String attCode; //食品口味编号
    @TableField("ACTION_CODE")
    private String actionCode;//食品口味動作編號，加，少，轉，凈，走等
    @TableField("CHANGE_AMT")
    private BigDecimal changeAmt; //加价金额或乘的倍数
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間

}
