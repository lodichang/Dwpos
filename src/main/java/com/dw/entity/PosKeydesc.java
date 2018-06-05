package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
* 按键名称表 实体类
*/
@TableName("tb_pos_keydesc")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosKeydesc implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version= DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线信息
    @TableField("DESC1")
    private String desc1; //按键名称
    @TableField("DESC2")
    private String desc2; //按键名称2
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
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
    @TableField("IS_RECOMMEND")
    private String isRecommend;             //是否推荐菜品
    @TableField("SORT")
    private String sort;              //排序
    @TableField("IS_SHOW")
    private String isShow; //是否顯示
}
