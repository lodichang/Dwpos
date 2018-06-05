package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* pos系统订单明细表 实体类
*/
@TableName("tb_pos_order")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class PosOrder implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    @DateTimeFormat
    private Date version = DateUtil.getNowTime(); //
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("CART_ID")
    private String cartId; //購物車流水號，點擊菜品時自動生成，適用於沒加入數據庫需要用到地方，如單項折扣使用
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
    @TableField("RECEIVE_ITEM_NAME")
    private String receiveItemName; //手寫單名稱
    @TableField("ITEM_IDX")
    private Long itemIdx; //商品流水号
    @TableField("SETMEAL")
    private String setmeal; //是否套餐(0-单项食品，>0套餐主体，<0套餐明细
    @TableField("SEAT")
    private String seat; // seat默认存座位，现在没有座位的功能，不用添加这个字段。
    @TableField("SERVICE")
    private String service; // SERVICE默认插入的是true
    @TableField("T_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tDate; //入单日期
    @TableField("T_TIME")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date tTime; //入单时间	
    @TableField("STAFF")
    private String staff; //操作员	
    @TableField("PRICE_TYPE")
    private String priceType; // 收费类别 默认0
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
    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice; //单价
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
    private String pindex; //打印编号
    @TableField("OINDEX")
    private String oindex;
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
    private String attCode;//菜品口味
    @TableField("CHANGE_AMT")
    private BigDecimal changeAmt;//口味加价
    @TableField("BULK_PURCHASE")
    private String bulkPurchase;//是否团购
    @TableField("ACCOUNT_AMT")
    private BigDecimal accountAmt;//挂账金额
    @TableField("ACCOUNT_PAYTYPE")
    private String accountPaytype;//挂账付款方式
    @TableField("PURCHASE_TYPE")
    private String purchaseType;//团购券类型（口碑、美团等）
    @TableField("APP_AUTHTOKEN")
    private String appAuthToken;//认领门店返回的token
    @TableField("EID")
    private String eId;//商家登录ERP帐号ID
    @TableField("ENAME")
    private String eName;//商家登录ERP帐号名称
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
    @TableField(value = "PRINT_STATE")
    private String printState;
    @TableField(value = "SERV_COST")
    private BigDecimal servCost;
}
