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
* 桌台操作表 实体类
*/
@TableName("tb_pos_table_action")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
   public class PosTableAction implements Serializable{
     @TableField("ID")
     private String id ; //
     @TableField("TABLE_NUM")
     private String tableNum ; //台号
     @TableField("OP_STAFF")
     private String opStaff ; //操作人编号
     @TableField("OP_STAFF_NAME")
     private String opStaffName ; //操作人名称
     @TableField("STATION_ID")
     private String stationId ; //机器Id
     @TableField("T_TIME")
     private Date tTime ; //操作时间
     @TableField("OPERATE_TYPE")
     private String operateType ; //
     @TableField("REMARKS")
     private String remarks ; //
}
