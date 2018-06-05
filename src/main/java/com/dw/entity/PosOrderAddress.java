package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 外賣單地址 实体类
 */
@TableName("tb_pos_order_address")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosOrderAddress implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = new Date(); //
    @TableField("ADDRESS_ID")
    private String addressId; //地址ID
    @TableField("TELEPHONE")
    private String telephone; //電話號碼
    @TableField("ADDRESS")
    private String address; //詳細地址
    @TableField("LINK_MAN")
    private String linkMan; //聯絡人
    @TableField("PHASE")
    private String phase; //第幾期
    @TableField("TOWER")
    private String tower; //第幾座
    @TableField("FLOOR")
    private String floor; //第幾層
    @TableField("ROOM")
    private String room; //第幾室
    @TableField("REMARK")
    private String remark; //備註
    @TableField("REF_NUM")
    private String refNum; //單號
    @TableField("SUB_REF")
    private String subRef; //副單號
    @TableField("IN_DATE")
    private Date inDate; //开台时间
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField(value = "LAST_UPDATE_NAME_ID", strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
