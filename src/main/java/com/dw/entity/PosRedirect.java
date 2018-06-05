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
import java.util.Date;

/**
* 食品特殊印机位表 实体类
*/
@TableName("tb_pos_redirect")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosRedirect implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version= DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //所属线
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号
    @TableField("OUTLET")
    private String outlet; //分店
    @TableField("FROM_TIME")
    private String fromTime; //开始时间
    @TableField("TO_TIME")
    private String toTime; //结束时间
    @TableField("PRINTER")
    private String printer; //印机编号
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号
    @TableField("REMARK1")
    private String remark1; //备注1
    @TableField("REMARK2")
    private String remark2; //备注2
    @TableField("REMARK3")
    private String remark3; //备注3
    @TableField("CLOUD_PRINTER")
    private String cloudPrinter; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
