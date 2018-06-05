package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 商品組合表 实体类
*/
@TableName("tb_pos_comb")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosComb implements Serializable{
    @TableField("id")
    private String id; //
    @TableField("VERSION")
    private Date version; //
    @TableField("OUTLINE")
    private String outline; //线编号
    @TableField("M_ITEM_CODE")
    private String mItemCode; //主商品编号
    @TableField("V_ITEM_CODE")
    private String vItemCode; //副商品编号
    @TableField("V_ITEM_PRICE")
    private BigDecimal vItemPrice; //副商品價格
    @TableField("STATUS")
    private Boolean status; //狀態:0停用1啟用
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
    @TableField("COMB_GROUP")
    private String combGroup; //组合名称

    public String getPosCombKey(){
        return mItemCode+"&"+vItemCode;
    }
}
