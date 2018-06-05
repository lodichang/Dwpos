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
* 原因表 实体类
*/
@TableName("tb_pos_reason")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosReason implements Serializable{
    @TableField("ID")
    private String id; //
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("CODE")
    private String code; //原因编号
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("DESC1")
    private String desc1; //原因名称
    @TableField("DESC2")
    private String desc2; //原因名称
    @TableField("DESC3")
    private String desc3; //原因名称
    @TableField("DESC4")
    private String desc4; //原因名称
    @TableField("PRIN_DESC")
    private String prinDesc; //原因名称
    @TableField("TYPE")
    private String type; //类型
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号     
    @TableField("REMARK1")
    private String remark1; //备注1              
    @TableField("REMARK2")
    private String remark2; //备注2              
    @TableField("REMARK3")
    private String remark3; //备注3              
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
