package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 前台员工信息表 实体类
*/
@TableName("tb_pos_staff")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PosStaff implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("CODE")
    private String code; //编号
    @TableField("PASSWORD")
    private String password; //密码
    @TableField("NAME1")
    private String name1; //名称
    @TableField("NAME2")
    private String name2; //名称2
    @TableField("FREE_TYPE")
    private String freeType; //
    @TableField("FREE_DEF")
    private String freeDef; //
    @TableField("FREE_START")
    private String freeStart; //
    @TableField("MAX_AMT")
    private BigDecimal maxAmt; //
    @TableField("MAX_DISC")
    private BigDecimal maxDisc; //
    @TableField("CARD_CODE")
    private String cardCode; //前台卡号
    @TableField("STAT")
    private String stat; //状态
    @TableField("REMARKS")
    private String remarks; //备注
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
    @TableField("ISGROUP")
    private String isgroup; //是否组别，0--false,1--true
    @TableField("OPERABLE")
    private String operable;
}
