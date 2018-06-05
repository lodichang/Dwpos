package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 桌台折扣规则表 实体类
*/
@TableName("tb_pos_tablediscrule")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTableDiscRule implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("RULEID")
    private String ruleid; //规则ID
    @TableField("ROOM_TYPE")
    private String roomType; //桌台类型
    @TableField("ADVANCE_TIME")
    private String advanceTime; //提前结账时间
    @TableField("DISC_COST")
    private BigDecimal discCost; //提前离座折扣金额
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号
    @TableField("REMARK1")
    private String remark1; //备注1
    @TableField("REMARK2")
    private String remark2; //备注2
    @TableField("REMARK3")
    private String remark3; //备注3
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
}
