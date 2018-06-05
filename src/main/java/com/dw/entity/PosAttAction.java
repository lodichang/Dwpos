package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 食品口味特殊處理類型 实体类
*/
@TableName("tb_pos_attaction")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosAttAction implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet;
    @TableField("ACTION_CODE")
    private String actionCode; //食品口味特殊處理類型編號
    @TableField("IS_SHOW")
    private String isShow; //點菜時是否顯示，TRUE-顯示，FALSE-不顯示
    @TableField("IS_MUST")
    private String isMust; // 是否必选动作，TRUE-是，FALSE-否
    @TableField("DESC1")
    private String desc1; //名称1，繁体
    @TableField("DESC2")
    private String desc2; //名称2，中文简体
    @TableField("DESC3")
    private String desc3; //名称3，英文
    @TableField("DESC4")
    private String desc4; //名称4，其他语言
    @TableField("PRINT_DESC")
    private String printDesc; //名称5，后厨印机打印名称
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
