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

@TableName("tb_pos_printer_task")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrinterTask implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLET")
    private String outlet; // 分店
    @TableField("STAFF_NAME")
    private String staffName; // 员工名稱
    @TableField("STAFF")
    private String staff; // 员工编号
    @TableField("TYPE")
    private String type; // 类型
    @TableField("TITLE")
    private String title; // 类型
    @TableField("REASON")
    private String reason; // 类型
    @TableField("PERSONS")
    private int persons; // 桌台
    @TableField("TABLE_NUM")
    private String tableNum; // 桌台
    @TableField("DEF_TABLE_NUM")
    private String defTableNum; // 桌台
    @TableField("REF_NUM")
    private String refNum; // 主单号
    @TableField("SUB_REF")
    private String subRef; //副单号
    @TableField("PLACE")
    private String place; //印机檔口
    @TableField("PRINTER")
    private String printer; //印机编号
    @TableField("DEF_PRINTER")
    private String defPrinter; //指定打印印機
    @TableField("STATION")
    private String station; //收银终端
    @TableField("SEND_TIME")
    private Date sendTime; // 送單日期
    @TableField("PRINT_TIME")
    private Date printTime; // 打印時間
    @TableField("PRINT_STATUS")
    private String printStatus; // 打印任务状态，參考PrintStateEnum
    @TableField("CURRENT_PAGE")
    private int currentPage; // 當前打印頁數
    @TableField("TOTAL_PAGE")
    private int totalPage;// 總打印頁數
    @TableField("SETMEAL")
    private String setmeal;// 是否套餐
    @TableField("SERIAL_NUMBER")
    private String serialNumber; // 流水号

}
