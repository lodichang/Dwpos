package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by li.yongliang on 2018/4/27.
 */
@TableName("tb_pos_timedisc")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTimedisc implements Serializable {
    @TableField("ID")
    private String id;
    @TableField("VERSION")
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline;
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("CODE")
    private String code; //提前離座折扣編號
    @TableField("CATEGORY")
    private String category; //提前離座折扣大類編號
    @TableField("START_TIME")
    private String startTime; //生效時間
    @TableField("ADVANCE_TIME")
    private String advanceTime; //結束時間
    @TableField("AMT_PRO")
    private String amtPro;//百分比或者实际金额,DISC_COST字段需要根據此字段判斷是按百分比還是按金額，amt金额、pro百分比
    @TableField("DISC_COST")
    private BigDecimal discCost; //折扣金額或折扣率
    @TableField("REMARK1")
    private String remark1; //備註1
    @TableField("REMARK2")
    private String remark2; //備註2
    @TableField("REMARK3")
    private String remark3; //備註3
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間


}
