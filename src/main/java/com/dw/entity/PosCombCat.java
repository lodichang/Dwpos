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
 * 食品組別 实体类
 */
@TableName("tb_pos_comb_cat")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosCombCat implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = getNowTime();//
    @TableField("OUTLINE")
    private String outline; //所属线
    @TableField("COMB_CODE")
    private String combCode; //食品組別编号
    @TableField("DESC1")
    private String desc1; //描述1
    @TableField("DESC2")
    private String desc2; //描述2
    @TableField("DESC3")
    private String desc3; //描述3
    @TableField("DESC4")
    private String desc4; //描述4
    @TableField("PRINT_DESC")
    private String printDesc; //列印名稱
    @TableField("STATUS")
    private Boolean status; //狀態:0停用1啟用
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
