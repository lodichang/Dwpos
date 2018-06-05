package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 桌台信息表 实体类
 */
@TableName("tb_pos_table")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTable implements Serializable{
    @TableField("ID")
    private String id ; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = new Date() ; //
    @TableField("OUTLET")
    private String outlet ; //分店编号
    @TableField("ROOM_NUM")
    private String roomNum ; //桌号
    @TableField("ROOM_TYPE")
    private String roomType ; //桌台类型
    @TableField("DESC1")
    private String desc1 ; //名称
    @TableField("DESC2")
    private String desc2 ; //名称
    @TableField("DESC3")
    private String desc3 ; //
    @TableField("DESC4")
    private String desc4 ; //
    @TableField("PRINT_DESC")
    private String printDesc ; //
    @TableField("PRICE_1")
    private BigDecimal price1 ; //
    @TableField("PRICE_2")
    private BigDecimal price2 ; //
    @TableField("PRICE_3")
    private BigDecimal price3 ; //
    @TableField("SIZE")
    private String size ; //桌台大小
    @TableField("COLOR")
    private String color ; //桌台颜色
    @TableField("X_RATIO")
    private BigDecimal xRatio ; //占屏幕X坐标的比例
    @TableField("Y_RATIO")
    private BigDecimal yRatio ; //占屏幕Y坐标的比例
    @TableField("PAGE_NUMBER")
    private Integer pageNumber ; //
    @TableField("SERVICE")
    private BigDecimal service ; //
    @TableField("FLOOR")
    private String floor ; //区域
    @TableField("REMARKS")
    private String remarks ; //
    @TableField("TIME_SPAN")
    private String timeSpan ; //翻台时长，设置开台到结账时间
    @TableField("UPDATED_BY")
    private String updatedBy ; //修改人员工编号
    @TableField("REMARK1")
    private String remark1 ; //备注1
    @TableField("REMARK2")
    private String remark2 ; //备注2
    @TableField("REMARK3")
    private String remark3 ; //备注3
    @TableField(value = "LAST_UPDATE_NAME_ID",strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId ; //
    @TableField(value = "LAST_UPDATE_TIME", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime ; //
    @TableField("IS_ONLINE")
    private String isOnline ; //是否網上訂座:1開放,0不開放
    @TableField("MINPERSON")
    private String minperson ; //最小人數
    @TableField("MAXPERSON")
    private String maxperson ; //最大人數
    @TableField("OUTLINE")
    private String outline ; //
}
