package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.dw.util.DateUtil.getNowTime;

/**
 * 共同口味表 实体类
 */
@TableName("tb_pos_atthead")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosAttHead implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("CODE")
    private String code; //共同口味编号              
    @TableField("OUTLINE")
    private String outline; //所属线                    
    @TableField("DESC1")
    private String desc1; //名称1                     
    @TableField("DESC2")
    private String desc2; //名称2
    @TableField("DESC3")
    private String desc3; //名称1
    @TableField("DESC4")
    private String desc4; //名称2
    @TableField("PRINT_DESC")
    private String printDesc; //名称2
    @TableField("TYPE")
    private String type; //处理方式,0--個別顯示，1-合併顯示， 2-重量計算
    @TableField("REMARKS")
    private String remarks; //备注                      
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
    @TableField("DISPLAY")
    private Boolean display;
    @TableField("SORT")
    private int sort;

    private List<PosAtt> posAttList;
}