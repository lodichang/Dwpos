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
 * 商品适合销售分店表 实体类
 */
@TableName("tb_pos_items")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosItems implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //所属线编号     
    @TableField("OUTLET")
    private String outlet; //分店编号,ITEM表中include字段如果是TRUE,则记录不适用的分店。如果是FALSE，则记录的是适用的分店编号
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号       
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
