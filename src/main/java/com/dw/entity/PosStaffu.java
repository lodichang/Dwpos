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
* 前台员工权限表 实体类
*/
@TableName("tb_pos_staffu")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosStaffu implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("CODE")
    private String code; //员工编号
    @TableField("S_RIGHT")
    private String sRight; //权限编号
    @TableField("R_GRADE")
    private String rGrade; //权限值
    @TableField("REMARK")
    private String remark; //备注
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