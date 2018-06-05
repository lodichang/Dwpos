package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
*  实体类
*/
@TableName("tb_pos_print_manager")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrintManager implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date version = getNowTime(); //
    @TableField("IS_USE")
    private Boolean isUse; //
    @TableField("PRINT_IMEI")
    private String printImei; //
    @TableField("PRINT_TOKEN")
    private String printToken; //
    @TableField("APP_KEY")
    private String appKey; //
    @TableField("PRINT_NAME")
    private String printName;
    @TableField("BRANCH_ID")
    private String branchId; //
    @TableField("SITE_ID")
    private String siteId; //
    @TableField("PRINT_BAND")
    private String printBand; //
    @TableField("IS_PRIMARY")
    private Integer isPrimary; //是否为主印机 1为主， 0为不是主打印机
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
}
