package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
*  实体类
*/
@TableName("tb_pos_service_log")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosServiceLog implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("TYPE")
    private String type; //
    @TableField("DESC")
    private String desc; //
    @TableField("BEFORE_ORDER")
    private Boolean beforeOrder;
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
