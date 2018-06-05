package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
*  实体类
*/
@TableName("tb_outlet_config")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutletConfig implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("OUTLET")
    private String outlet; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("COMPANY_ID")
    private String companyId; //
    @TableField("KEY")
    private String key; //
    @TableField("VALUE")
    private String value; //
    @TableField("DESCRIPT")
    private String descript; //
}
