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
* 订座时段表 实体类
*/
@TableName(value = "tb_mem_period")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemPeriod implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //  給version默認值
    @TableField("REGION_ID")
    private String regionId;
    @TableField("OUTLET")
    private String outlet; //
    @TableField("CODE")
    private String code; //
    @TableField("NAME")
    private String name; //
    @TableField("NAME2")
    private String name2; //
    @TableField("NAME3")
    private String name3; //
    @TableField("NAME4")
    private String name4; //
    @TableField("PRINT_DESC")
    private String printDesc; //
    @TableField("SORT")
    private String sort;
    @TableField("ISVALID")
    private String isvalid;
    @TableField("STIME")
    private String stime;
    @TableField("ETIME")
    private String etime;
    @TableField("ISSERV")
    private String isserv;
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField(value = "LAST_UPDATE_NAME_ID", strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
