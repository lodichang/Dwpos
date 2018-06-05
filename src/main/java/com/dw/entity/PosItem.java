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
 * 商品基本信息表 实体类
 */
@TableName("tb_pos_item")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosItem implements Serializable {
    @TableField("id")
    private String id; //
    @TableField("version")
    @Builder.Default
    private Date version = getNowTime(); //
    @TableField("OUTLINE")
    private String outline; //线编号                  
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号
    @TableField("DESC1")
    private String desc1; //名称1                   
    @TableField("DESC2")
    private String desc2; //名称2
    @TableField("DESC3")
    private String desc3; //名称3
    @TableField("DESC4")
    private String desc4; //名称4
    @TableField("TYPE")
    private String type; //是否套餐                2为套餐,1不是套餐
    @TableField("CAT")
    private String cat; //商品所属大类            
    @TableField("ITEMTYPE")
    private String itemtype; //食品餐种                
    @TableField("ITEMSET")
    private String itemset; //食品类别                
    @TableField("IOPEN")
    private String iopen; //是否手入价钱            
    @TableField("SERVICE")
    private String service; //服务费                  
    @TableField("IPRINT")
    private String iprint; //必须列印                
    @TableField("PRN")
    private String prn; //印机位                  
    @TableField("ATT_TYPE")
    private String attType; //                        
    @TableField("ATT_MAX")
    private Integer attMax; //最多共同口味            
    @TableField("ATT_MIN")
    private Integer attMin; //最少共同口味            
    @TableField("CATT")
    private String catt; //共同口味                可多选，多个共同口味直接字符串拼接
    @TableField("SLOD_OUT")
    private String slodOut; //永久停售                
    @TableField("DISCONT")
    private String discont; //暂停                    
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
    @TableField("COST")
    private BigDecimal cost; //成本价                  
    @TableField("ROOM_SEL")
    private String roomSel; //是否按件数列印          
    @TableField("SUMMARY")
    private String summary; //清机列印                
    @TableField("MAX_DISC")
    private String maxDisc; //                        
    @TableField("BILL_DISC")
    private String billDisc; //是否可以折扣            
    @TableField("CAN_FREE")
    private String canFree; //招待等级                
    @TableField("CAN_CANCEL")
    private String canCancel; //取消/落單等级           
    @TableField("I_DECIMAL")
    private String iDecimal; //                        
    @TableField("TAX")
    private String tax; //是否收税                
    @TableField("FREE_MIN")
    private Integer freeMin; //免房租的分钟数          
    @TableField("DEF_STOCK")
    private Integer defStock; //                        
    @TableField("AUTO_REPRT")
    private Integer autoReprt; //條碼自動重印
    @TableField("INCLUDE")
    private String include; //适用分店                "TRUE-ITEM_S表存不销售的分店编号,FALSE-ITEM_S表存可以销售的分店编号"
    @TableField("REMARKS")
    private String remarks; //此商品绑定的原价商品编号
    @TableField("UPDATED_BY")
    private String updatedBy; //                        
    @TableField("REMARK1")
    private String remark1; //                        
    @TableField("REMARK2")
    private String remark2; //                        
    @TableField("REMARK3")
    private String remark3; //
    @TableField("CLOUD_PRINTER")
    private String cloudPrinter; // 
    @TableField("ITEM_IMG")
    private String itemImg;//商品图片
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //最後更新人
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //最後更新時間
    @TableField("COMB")
    private Boolean comb; //組合屬性:0否1是
    @TableField("BULK_PURCHASE")
    private String bulkPurchase;//是否团购
    @TableField("ACCOUNT_AMT")
    private BigDecimal accountAmt;//挂账金额
    @TableField("ACCOUNT_PAYTYPE")
    private String accountPaytype;//挂账付款方式
    @TableField("PURCHASE_TYPE")
    private String purchaseType;//团购券类型（口碑、美团等）
    @TableField("VALUATION")
    private String valuation;   //計價方式
    @TableField("COMB_ID")
    private String combId;

}
