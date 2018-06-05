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

/**
* 折扣类型表 实体类
*/
@TableName("tb_pos_discbase")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosDiscbase implements Serializable{
    @TableField("id")
    private String id; //
    @TableField("version")
    private Date version; //
    @TableField("D_NAME")
    private String dName; //
    @TableField("D_AMT")
    private String dAmt; //
    @TableField("LOG_TYPE")
    private String logType; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
