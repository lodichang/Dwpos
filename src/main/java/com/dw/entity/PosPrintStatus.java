package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("tb_pos_print_status")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrintStatus implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version; //
    @TableField("CHECK_RESULT")
    private String checkResult; //
    @TableField("PRINT_IMEI")
    private String printImei; //
    @TableField("PRING_MSG")
    private String pringMsg; //
    @TableField("PRINT_RESULT")
    private String printResult; //
    @TableField("BRANCH_ID")
    private String branchId; //
    @TableField("DESK_ID")
    private String deskId; //
    @TableField("SITE_ID")
    private String siteId; //
    @TableField("PRINT_KEY")
    private String printKey; //
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
