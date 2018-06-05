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
 * 套餐明细表 实体类
 */
@TableName("tb_pos_setmeal")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosSetmeal implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //套餐所属线                    
    @TableField("CODE")
    private String code; //套餐编号                      
    @TableField("S_GROUP")
    private String sGroup; //套餐组别
    @TableField("GP_DESC1")
    private String gpDesc1; //組別名稱1
    @TableField("GP_DESC2")
    private String gpDesc2; //組別名稱2
    @TableField("GP_DESC3")
    private String gpDesc3; //組別名稱3
    @TableField("GP_DESC4")
    private String gpDesc4; //組別名稱4
    @TableField("S_COUNT")
    private String sCount; //每个组别必选数量              
    @TableField("ITEM_CODE")
    private String itemCode; //套餐内食品编号                
    @TableField("ADD_PRICE")
    private BigDecimal addPrice; //加价,套餐内此商品加价金额
    @TableField("CAT_PERC")
    private BigDecimal catPerc; //套餐内食品占套餐总金额百分比  
    @TableField("REMARKS")
    private String remarks; //备注5位流水号+4位套餐内食品编号
    @TableField("CAN_CHANGE")
    private String canChange; //是否可以换菜
    @TableField("IS_HOLDON")
    private Boolean isHoldOn;//是否默认叫起
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号                
    @TableField("REMARK1")
    private String remark1; //备注1                         
    @TableField("REMARK2")
    private String remark2; //备注2                         
    @TableField("REMARK3")
    private String remark3; //备注3

    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間


}
