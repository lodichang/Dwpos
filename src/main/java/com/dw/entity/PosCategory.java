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
 * 大类表 实体类
 */
@TableName("tb_pos_category")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosCategory implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = getNowTime();//
    @TableField("CAT_CODE")
    private String catCode; //大类编号           
    @TableField("OUTLINE")
    private String outline; //所属线             
    @TableField("DESC1")
    private String desc1; //大类描述1          
    @TableField("DESC2")
    private String desc2; //大类描述2
    @TableField("DESC3")
    private String desc3; //大类描述3
    @TableField("DESC4")
    private String desc4; //大类描述4
    @TableField("PRINT_DESC")
    private String printDesc; //列印名稱
    @TableField("SCAT")
    private String scat; //大大类编号         
    @TableField("MAX_DISC")
    private BigDecimal maxDisc; //最大折扣率
    @TableField("AMOUNT1")
    private BigDecimal amount1; //金额1              
    @TableField("AMOUNT2")
    private BigDecimal amount2; //金额2              
    @TableField("AMOUNT3")
    private BigDecimal amount3; //金额3              
    @TableField("REMARKS")
    private String remarks; //备注               
    @TableField("DISC")
    private String disc; // 可与单折扣一起使用
    @TableField("FIRST_SINGLE")
    private Boolean firstSingle;//首單是否必选？ false 否，true是
    @TableField("ADD_SINGLE")
    private Boolean addSingle;//加单是否显示？ false不显示，true是
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
