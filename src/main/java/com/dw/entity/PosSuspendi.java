package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
* pos系统推送暂停或扣炖为零商品到云端表 实体类
*/
@TableName("tb_pos_suspendi")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosSuspendi implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //所属线
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号
    @TableField("UPDATED_BY")
    private String updatedBy; //
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField(value = "LAST_UPDATE_NAME_ID", strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME", strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
}
