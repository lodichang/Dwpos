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
*  实体类
*/
@TableName("tb_operation_log")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OperationLog implements Serializable{
    @TableField("ID")
    private String id; //`
    @TableField(value = "VERSION",  strategy =   FieldStrategy.IGNORED)
    private Date version = getNowTime(); //
    @TableField("ENTITY_NAME")
    private String entityName; //
    @TableField("BATCH_NO")
    private String batchNo; //
    @TableField("COMPANY_ID")
    private String companyId; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("OUTLET")
    private String outlet; //
    @TableField("STATE")
    private Integer state; //
    @TableField("TYPE")
    private String type; //
    @TableField("NEW_CONTENT")
    private String newContent; //
    @TableField("OLD_CONTENT")
    private String oldContent; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
    @TableField(value = "CREATE_TIME", strategy =   FieldStrategy.IGNORED)
    private Long createTime;//获取时间戳
}
