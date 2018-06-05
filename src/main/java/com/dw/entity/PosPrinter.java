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
import java.util.Date;

/**
* 印机基础信息表 实体类
*/
@TableName("tb_pos_printer")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrinter implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField(value = "VERSION",  strategy =   FieldStrategy.IGNORED)
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //所属线
    @TableField("P_CODE")
    private String pcode; //印机编号
    @TableField("DESC1")
    private String desc1; //名称1
    @TableField("DESC2")
    private String desc2; //名称2
    @TableField("PORT")
    private String port; //端口号
    @TableField("PANTRY")
    private String pantry; //
    @TableField("SINGLE_PRT")
    private String singlePrt; //是否单独列印	TRUE-列印单，FALSE-列印总
    @TableField("SUMMARY")
    private String summary; //
    @TableField("IS_PANTRY")
    private String isPantry; //
    @TableField("REDIRECT")
    private String redirect; //
    @TableField("DELAY")
    private String delay; //
    @TableField("REMARKS")
    private String remarks; //
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号
    @TableField("REMARK1")
    private String remark1; //备注1
    @TableField("REMARK2")
    private String remark2; //备注2
    @TableField("REMARK3")
    private String remark3; //备注3
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; ////最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間

}
