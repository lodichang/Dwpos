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

import static com.dw.util.DateUtil.getNowTime;

/**
* pos客戶端按鍵設計大類表 实体类
*/
@TableName("tb_pos_topbutton")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosButton implements Serializable{
    @TableField("ID")
    private String id; //流水號
    @TableField("VERSION")
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet;
    @TableField("DESCHK")
    private String deschk; //
    @TableField("DESCCN")
    private String desccn; //中文名稱
    @TableField("DESCEN")
    private String descen; //英文名稱
    @TableField("PRINT_DESC")
    private String printDesc; //打印名稱
    @TableField("DESCOT")
    private String descot; //其他語言名稱
    @TableField("DISPLAY")
    private Boolean display; //是否有效
    @TableField("DISSEQ")
    private Integer disseq; //前台顯示排序
    @TableField("FORECOLOR")
    private Integer forecolor; //前景色
    @TableField("BACKCOLOR")
    private Integer backcolor; //背景色
    @TableField("ACTIVE_PERIOD")
    private String activePeriod; //適用時段 a,b,c,d,
    @TableField("ACTIVE_ZONE")
    private String activeZone; //適用區域 a,b,c,d,
    @TableField("STARTDATE")
    private Date startdate; //有效起始日期
    @TableField("ENDDATE")
    private Date enddate; //結束日期
    @TableField("DAY1")
    private Boolean day1; //星期1是否有效 1-有效，0-無效
    @TableField("DAY2")
    private Boolean day2; //星期2是否有效 1-有效，0-無效
    @TableField("DAY3")
    private Boolean day3; //星期3是否有效 1-有效，0-無效
    @TableField("DAY4")
    private Boolean day4; //星期4是否有效 1-有效，0-無效
    @TableField("DAY5")
    private Boolean day5; //星期5是否有效 1-有效，0-無效
    @TableField("DAY6")
    private Boolean day6; //星期6是否有效 1-有效，0-無效
    @TableField("DAY7")
    private Boolean day7; //星期7是否有效 1-有效，0-無效
    @TableField("HOLIDAY")
    private Boolean holiday; //節假日是否有效 1-有效，0-無效
    @TableField("EX_HOLIDAY")
    private Boolean exHoliday; //公共假期除外 1-有效，0-無效
    @TableField("BUTTON_IMG")
    private Boolean buttonImg; //按钮图片，如果有图片则默认显示图片，如果没有图片显示名称
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =  FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =  FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
}
