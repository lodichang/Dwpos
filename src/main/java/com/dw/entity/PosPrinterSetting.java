package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
*  实体类
*/
@TableName("tb_pos_printer_setting")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrinterSetting implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private Date version = new Date(); //
    @TableField("PRINT_BAND")
    private String printBand; //
    @TableField("PRINT_CONTEXT")
    private String printContext; //
    @TableField("PRINT_TYPE")
    private String printType; //
    @TableField("IS_PRIMARY")
    private Integer isPrimary;
    @TableField("SIET_ID")
    private String sietId; //
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
