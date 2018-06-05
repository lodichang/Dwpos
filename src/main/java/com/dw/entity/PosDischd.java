package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
* 预设折扣主信息表 实体类
*/
@TableName("tb_pos_dischd")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosDischd implements Serializable{
    @TableField("id")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("TYPE")
    private String type; //
    @TableField("CODE")
    private String code; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("DESC1")
    private String desc1; //
    @TableField("DESC2")
    private String desc2; //
    @TableField("DESC3")
    private String desc3; //名称3
    @TableField("DESC4")
    private String desc4; //名称4
    @TableField("PRINT_DESC")
    private String printDesc; //名称4
    @TableField("GRADE")
    private String grade; //
    @TableField("SDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sdate; //
    @TableField("EDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date edate; //
    @TableField("STIME")
    private String stime; //
    @TableField("ETIME")
    private String etime; //
    @TableField("WEEKDAY")
    private String weekday; //  插入格式是 1,2,3 代表星期一，二，三
    @TableField("D_INDEX")
    private String dIndex; //
    @TableField("INCLUDE")
    private String include; // 如果為TRUE PosDiscs裡面的關聯分店為不適用分店，FALSE PosDiscs裡面的關聯分店為適用分店
    @TableField("ONLINE")
    private Integer online; // 是否允許在線點菜適用
    @TableField("ALL_USE")
    private Integer allUse; // 全單折扣
    @TableField("REMARKS")
    private String remarks; //
    @TableField("REMARK1")
    private String remark1; //
    @TableField("REMARK2")
    private String remark2; //
    @TableField("REMARK3")
    private String remark3; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
}
