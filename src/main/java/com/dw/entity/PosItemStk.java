package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
 * 商品扣燉表 实体类
 */
@TableName("tb_pos_itemstk")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosItemStk implements Serializable {
    @TableField("id")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号
    @TableField("STOCK")
    private String stock; //數量
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
