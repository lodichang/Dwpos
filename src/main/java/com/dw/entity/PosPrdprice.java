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
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
 * 时段对应价钱名称表 实体类
 */
@TableName("tb_pos_prdprice")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrdprice implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = getNowTime();
    @TableField("P_CODE")
    private String pcode; //价钱编号
    @TableField("OUTLINE")
    private String outline; //所属线        
    @TableField("DESC1")
    private String desc1; //价钱名称      
    @TableField("DESC2")
    private String desc2; //价钱名称2     
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
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
