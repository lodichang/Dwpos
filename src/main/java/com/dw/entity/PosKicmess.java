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
* 厨房讯息表 实体类
*/
@TableName("tb_pos_kicmess")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
   public class PosKicmess implements Serializable{
     @TableField("ID")
     private String id; //
     @TableField("VERSION")
     private Date version = DateUtil.getNowTime(); //
     @TableField("CODE")
     private String code; //讯息编号
     @TableField("OUTLINE")
     private String outline; //线
     @TableField("DESC1")
     private String desc1; //名称1
     @TableField("DESC2")
     private String desc2; //名称2
     @TableField("SHORT_DESC")
     private String shortDesc; //短名称
     @TableField("CAN_ORDER")
     private String canOrder; //
     @TableField("CAN_ROOM")
     private String canRoom; //
     @TableField("NEED_QTY")
     private String needQty; //
     @TableField("REMARKS")
     private String remarks; //
     @TableField("UPDATED_BY")
     private String updatedBy; //修改人员工编号     
     @TableField("REMARK1")
     private String remark1; //备注1              
     @TableField("REMARK2")
     private String remark2; //备注2              
     @TableField("REMARK3")
     private String remark3; //备注3              
     @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
     private String lastUpdateNameId; //
     @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
     private Date lastUpdateTime; //
}
