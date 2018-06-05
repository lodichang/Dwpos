package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.dw.util.DateUtil.getNowTime;

/**
 * 商品各分店价钱表 实体类
 */
@TableName("tb_pos_price")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPrice implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //所属线编号     
    @TableField("OUTLET")
    private String outlet; //分店编号       
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号       
    @TableField("PRICE_TYPE")
    private String priceType; //收费类别       
    @TableField("SUMMARY")
    private String summary; //清機列印,TRUE--是，FALSE--否，Z--跟食品設定       
    @TableField("SERVICE")
    private String service; //服務費,TRUE--是，FALSE--否，Z--跟食品設定         
    @TableField("BILL_DISC")
    private String billDisc; //沒有折扣,TRUE--是，FALSE--否，Z--跟食品設定       
    @TableField("IOPEN")
    private String iopen; //是否手写价钱   
    @TableField("PRICE1")
    private BigDecimal price1; //价钱1
    @TableField("PRICE2")
    private BigDecimal price2; //价钱2          
    @TableField("PRICE3")
    private BigDecimal price3; //价钱3          
    @TableField("PRICE4")
    private BigDecimal price4; //价钱4          
    @TableField("PRICE5")
    private BigDecimal price5; //价钱5          
    @TableField("PRICE6")
    private BigDecimal price6; //价钱6          
    @TableField("PRICE7")
    private BigDecimal price7; //价钱7          
    @TableField("PRICE8")
    private BigDecimal price8; //价钱8          
    @TableField("PRICE9")
    private BigDecimal price9; //价钱9          
    @TableField("REMARKS")
    private String remarks; //备注           
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
