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
* pos优惠券对应分店表 实体类
*/
@TableName("tb_pos_coupons")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosCoupons implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version; //
    @TableField("OUTLET")
    private String outlet; //
    @TableField("COUPON")
    private String coupon; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("UPDATED_BY")
    private String updatedBy; //修改人员工编号     
    @TableField("REMARK1")
    private String remark1; //备注1              
    @TableField("REMARK2")
    private String remark2; //备注2              
    @TableField("REMARK3")
    private String remark3; //备注3              
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
}
