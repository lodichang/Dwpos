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

/**
* 情機記錄實體類
*/
@TableName("tb_pos_dayelog")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosDayeLog implements Serializable{
    @TableField("id")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("SEQ")
    private String seq; //
    @TableField("CLEANDATE")
    private Date cleandate; //
    @TableField("CLEANTIME")
    private Date cleantime; //
    @TableField("STAT")
    private String stat; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
