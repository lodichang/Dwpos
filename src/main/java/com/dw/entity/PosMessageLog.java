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
*  实体类
*/
@TableName("tb_pos_message_log")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosMessageLog implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("BILL_NO")
    private String billNo; //
    @TableField("TYPE")
    private String type; //
    @TableField("TAG")
    private String tag; //
    @TableField("TITLE")
    private String title; //
    @TableField("CHANNEL")
    private String channel; //
    @TableField("CONTENT")
    private String content; //
    @TableField("ATTRIBUTES")
    private String attributes;
    @TableField("STATE")
    private String state;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet;
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
