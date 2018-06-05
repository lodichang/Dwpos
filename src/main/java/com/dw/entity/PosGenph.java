package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
 * 特殊日子表 实体类
 */
@TableName("tb_pos_genph")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosGenph implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLET")
    private String outlet; //分店编号      
    @TableField("OUTLINE")
    private String outline; //分店名称      
    @TableField("G_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date gdate; //日期
    @TableField("G_COUNT")
    private String gcount; //星期几
    @TableField("SERVICE")
    private String service; //服务费        
    @TableField("TAX")
    private BigDecimal tax; //税率1         
    @TableField("TAX_1")
    private BigDecimal tax1; //税率2         
    @TableField("HEAD_FEE1")
    private BigDecimal headFee1; //时段1收费     
    @TableField("HEAD_TIME1")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date headTime1; //时段1         
    @TableField("HEAD_FEE2")
    private BigDecimal headFee2; //时段2收费
    @TableField("HEAD_TIME2")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date headTime2; //时段2
    @TableField("HEAD_FEE3")
    private BigDecimal headFee3; //时段3收费     
    @TableField("HEAD_TIME3")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date headTime3; //时段3
    @TableField("HEAD_DESC1")
    private String headDesc1; //入场费名称1   
    @TableField("HEAD_DESC2")
    private String headDesc2; //入场费名称2   
    @TableField("ROOM_TIME1")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date roomTime1; //包房时段1     
    @TableField("ROOM_DISC1")
    private String roomDisc1; //包房时段名称  
    @TableField("ROOM_TIME2")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date roomTime2; //包房时段2
    @TableField("ROOM_DISC2")
    private String roomDisc2; //包房时段2名称 
    @TableField("ROOM_TIME3")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date roomTime3; //包房时段3     
    @TableField("ROOM_DISC3")
    private String roomDisc3; //包房时段3名称 
    @TableField("PERIOD_1")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period1; //时段1
    @TableField("PERIOD_2")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period2; //时段2
    @TableField("PERIOD_3")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period3; //时段3
    @TableField("PERIOD_4")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period4; //时段4
    @TableField("PERIOD_5")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period5; //时段5
    @TableField("PERIOD_6")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period6; //时段6
    @TableField("PERIOD_7")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period7; //时段7
    @TableField("PERIOD_8")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period8; //时段8
    @TableField("PERIOD_9")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date period9; //时段9
    @TableField("DAY_END")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date dayEnd; //营业结束时间
    @TableField("FREE_TIME")
    private String freeTime; //              
    @TableField("PERIOD")
    private String period; //              
    @TableField("ALLOWANCE")
    private String allowance; //              
    @TableField("MIN_TIME")
    private String minTime; //              
    @TableField("PRICETYPE")
    private String pricetype; //收费类别      
    @TableField("REMARKS")
    private String remarks; //备注          
    @TableField("TYPE")
    private String type; //              
    @TableField("PRIORITY")
    private String priority; //              
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
