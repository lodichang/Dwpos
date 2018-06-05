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
 * 食品口味表 实体类
 */
@TableName("tb_pos_att")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosAtt implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("A_GROUP")
    private String aGroup; //共同口味编号或食品编号    
    @TableField("OUTLINE")
    private String outline; //所属线                    
    @TableField("CODE")
    private String code; //共同口味子编号            
    @TableField("DESC1")
    private String desc1; //名称1
    @TableField("DESC2")
    private String desc2; //名称2
    @TableField("DESC3")
    private String desc3; //名称3
    @TableField("DESC4")
    private String desc4; //名称4
    @TableField("PRINT_DESC")
    private String printDesc; //名称4
    @TableField("AMT_PRO")
    private String amtPro; // 百分比或者实际金额,cal_amount字段需要根據此字段判斷是按百分比還是按金額，amt金额、pro百分比
    @TableField("BILL_PRINT")
    private String billPrint; //是否列印,A----加,M----乘  
    @TableField("CAL_TYPE")
    private String calType; //计算方式                  
    @TableField("CAL_AMOUNT")
    private BigDecimal calAmount; //计算数目
    @TableField("CAL_QTY")
    private BigDecimal calQty; //计算数量
    @TableField("STK_TYPE")
    private String stkType; //                          
    @TableField("ING_TYPE")
    private String ingType; //                          
    @TableField("OUTLET")
    private String outlet; //适用分店,000指所有分店    
    @TableField("CAN_CHANGE")
    private String canChange; //選中該口味的菜將不可被換走
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}