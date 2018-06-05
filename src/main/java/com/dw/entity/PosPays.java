package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * pos付款方式对应分店表 实体类
 */
@TableName("tb_pos_pays")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPays implements Serializable{
    @TableField("ID")
    private String id ; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = new Date() ; //
    @TableField("OUTLET")
    private String outlet ; //
    @TableField("PAYTYPE")
    private String paytype ; //
    @TableField("OUTLINE")
    private String outline ; //
    @TableField("UPDATED_BY")
    private String updatedBy ; //修改人员工编号
    @TableField("REMARK1")
    private String remark1 ; //备注1
    @TableField("REMARK2")
    private String remark2 ; //备注2
    @TableField("REMARK3")
    private String remark3 ; //备注3
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId ; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime ; //
}
