package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 桌台信息表 实体类
*/
//@Accessors(chain = true)implements Serializable
@NoArgsConstructor
@Getter
@Setter
@ToString
   public class PosTableDto implements Serializable {

     private String id ; //

     private Date version ;

     private String outlet ; //分店编号

     private String roomNum ; //桌号

     private String roomType ; //桌台类型

     private String desc1 ; //名称

     private String desc2 ; //名称

     private String desc3 ; //名称

     private String desc4 ; //名称

      private String printDesc ; //名称

     private BigDecimal price1 ; //

     private BigDecimal price2 ; //

     private BigDecimal price3 ; //

     private String size ; //桌台大小

     private String color ; //桌台颜色

     private BigDecimal xRatio ; //占屏幕X坐标的比例

     private BigDecimal yRatio ; //占屏幕Y坐标的比例

     private Integer pageNumber ; //

     private BigDecimal service ; //

     private String floor ; //区域

     private String remarks ; //

     private String timeSpan ; //翻台时长，设置开台到结账时间

     private String updatedBy ; //修改人员工编号     

     private String remark1 ; //备注1              

     private String remark2 ; //备注2              

     private String remark3 ; //备注3              

     private String lastUpdateNameId ; //

     private Date lastUpdateTime ; //

     private String isOnline ; //是否網上訂座:1開放,0不開放

     private String minperson ; //最小人數

     private String maxperson ; //最大人數

     private String tableState;

     private Integer currentPerson;

     private Date inTime;

     private String parentTableNum;

}
