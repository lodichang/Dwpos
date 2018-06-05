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
 * 按键设计表 实体类
 */
@TableName(value = "tb_pos_keydesign")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosKeydesign implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version= DateUtil.getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线编号
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("OUTLET_ID")
    private String outletId;//分店id
    @TableField("KEYID")
    private String keyid; //按键ID
    /*@TableField(el = "posKeydesc.id",value= "KEYID")//暂时先不关联
    private PosKeydesc posKeydesc;*/
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号
    /*@TableField(el= "posItem.itemCode,posItem.outline",value= "ITEM_CODE")
    private PosItem posItem;*/
    @TableField("KEY_SORT")
    private Long keySort; //按键排序序号
    @TableField("ITEM_SORT")
    private Long itemSort; //商品排序序号
    @TableField("PERIOD")
    private String period;
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
    @TableField("CAN_ORDER")
    private String canOrder;        //是否可下單
    @TableField("IS_RECOMMEND_ITEM")
    private String isRecommendItem;  //是否設置為推薦
    @TableField("MUST_CODE")
    private String mustCode;        //必選菜品編號
    @TableField("DESCRIPTION")
    private String description;     //必選菜品描述
    @TableField("ATT_CODE")
    private String attCode;  //首頁顯示口味編號 by wen.jing
    @TableField("ATT_DESC")
    private String attDesc;  //首頁顯示口味名稱 by wen.jing
}
