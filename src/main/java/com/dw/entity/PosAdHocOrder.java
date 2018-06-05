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
* pos系统订单明细表 实体类
*/
@TableName("tb_pos_ad_hoc_order")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosAdHocOrder implements Serializable{
    @TableField("ID")
    private String id; //PERSONS
    @TableField("VERSION")
    private Date version; //
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("OUTLINE")
    private String outline; //
    @TableField("REF_NUM")
    private String refNum; //主单号
    @TableField("SUB_REF")
    private String subRef; //副单号
    @TableField("TYPE")
    private String type; //账单类型
    @TableField("TABLE_NUM")
    private String tableNum; //桌号
    @TableField("ITEM_CODE")
    private String itemCode; //商品编号	
    @TableField("ITEM_IDX")
    private Long itemIdx; //商品流水号
    @TableField("SETMEAL")
    private String setmeal; //是否套餐(0-单项食品，>0套餐主体，<0套餐明细
    @TableField("SEAT")
    private String seat; //
    @TableField("SERVICE")
    private String service; //
    @TableField("T_DATE")
    private Date tDate; //入单日期
    @TableField("T_TIME")
    private Date tTime; //入单时间	
    @TableField("STAFF")
    private String staff; //操作员	
    @TableField("PRICE_TYPE")
    private String priceType; //
    @TableField("QTY")
    private Integer qty; //数量
    @TableField("AMT")
    private BigDecimal amt; //金额	
    @TableField("CHG_AMT")
    private BigDecimal chgAmt; //付款折扣
    @TableField("IDISC_TYPE")
    private String idiscType; //
    @TableField("IDISC_QTY")
    private String idiscQty; //
    @TableField("ITEM_DISC")
    private BigDecimal itemDisc; //
    @TableField("CAT_DISC")
    private BigDecimal catDisc; //
    @TableField("ORDER_DISC")
    private BigDecimal orderDisc; //所有折扣
    @TableField("TAX")
    private BigDecimal tax; //
    @TableField("CANCEL")
    private Integer cancel; //取消数量
    @TableField("FREE")
    private Integer free; //免费数量
    @TableField("COST")
    private BigDecimal cost; //成本
    @TableField("KIC_MESS")
    private String kicMess; //厨房讯息
    @TableField("TA")
    private String ta; //
    @TableField("REASON")
    private String reason; //原因编号
    @TableField("SEP_CHAR")
    private String sepChar; //套餐主体流水号
    @TableField("PERIOD")
    private String period; //更次
    @TableField("PINDEX")
    private String pindex; //
    @TableField("OINDEX")
    private String oindex; //
    @TableField("KCONFIRM")
    private String kconfirm; //原价字段	
    @TableField("REMARKS")
    private String remarks; //
    @TableField("ITEM_INDEX")
    private Long itemIndex; //
    @TableField("SETMEAL_CODE")
    private String setmealCode; //套餐主体编号
    @TableField("ITEM_IMG")
    private String itemImg; //商品图片
    @TableField("BATCH_NUMBER")
    private String batchNumber; //打印批号
    @TableField("ATT_CODE")
    private String attCode; //菜品口味
    @TableField("CHANGE_AMT")
    private BigDecimal changeAmt; //
    @TableField("PRINT_STATE")
    private String printState; //打印状态
    @TableField("SERV_COST")
    private BigDecimal servCost; //服务费
    @TableField("BULK_PURCHASE")
    private String bulkPurchase; //
    @TableField("ACCOUNT_AMT")
    private BigDecimal accountAmt; //
    @TableField("ACCOUNT_PAYTYPE")
    private String accountPaytype; //
    @TableField("PURCHASE_TYPE")
    private String purchaseType; //
    @TableField("APP_AUTHTOKEN")
    private String appAuthtoken; //
    @TableField("EID")
    private String eid; //
    @TableField("ENAME")
    private String ename; //
    @TableField("ROOM_NUM")
    private String roomNum; //叫號號碼
    @TableField("LAST_UPDATE_NAME_ID")
    private String lastUpdateNameId; //
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime; //
    @TableField("PERSONS")
    private Integer persons; //
}
