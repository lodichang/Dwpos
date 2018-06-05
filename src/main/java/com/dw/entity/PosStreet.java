package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 街道 实体类
 */
@TableName("tb_pos_street")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosStreet implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = new Date(); //
    @TableField("AREA_ID")
    private String areaId; //區ID
    @TableField("ACTIVE")
    private Boolean active; //是否有效
    @TableField("DESC1")
    private String desc1; //名称1，繁体
    @TableField("DESC2")
    private String desc2; //名称2，中文简体
    @TableField("DESC3")
    private String desc3; //名称3，英文
    @TableField("DESC4")
    private String desc4; //名称4，其他语言
    @TableField("REMARK")
    private String remark; //备注1

    @TableField(value = "LAST_UPDATE_NAME_ID", strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}