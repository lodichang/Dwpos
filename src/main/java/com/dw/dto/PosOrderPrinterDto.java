package com.dw.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.dw.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
* pos系统订单明细表 实体类
*/
@Getter
@Setter
@ToString
public class PosOrderPrinterDto {
    private String id; //
    private Date version = DateUtil.getNowTime(); //
    private String outlet; //分店编号
    private String outline; //线
    private String cartId; //購物車流水號，點擊菜品時自動生成，適用於沒加入數據庫需要用到地方，如單項折扣使用
    private String refNum; //主单号
    private String subRef; //副单号
    private String type; //账单类型
    private String tableNum; //桌号
    private String itemCode; //商品编号
    private Long itemIdx; //商品流水号
    private String setmeal; //是否套餐(0-单项食品，>0套餐主体，<0套餐明细
    private String seat; // seat默认存座位，现在没有座位的功能，不用添加这个字段。
    private String service; // SERVICE默认插入的是true
    private Date tDate; //入单日期
    private Date tTime; //入单时间
    private String staff; //操作员
    private String priceType; // 收费类别 默认0
    private Integer qty; //数量
    private BigDecimal amt; //金额
    private BigDecimal chgAmt; //付款折扣
    private String idiscType; //
    private String idiscQty; //
    private BigDecimal itemDisc; //
    private BigDecimal catDisc; //
    private BigDecimal orderDisc; //所有折扣
    private BigDecimal tax; //
    private Integer cancel; //取消数量
    private Integer free; //免费数量
    private BigDecimal cost; //成本
    private BigDecimal unitPrice; //单价
    private String kicMess; //厨房讯息
    private String ta; //
    private String reason; //原因编号
    private String sepChar; //套餐主体流水号
    private String period; //更次
    private String pindex; //打印编号
    private String oindex;
    private String kconfirm; //原价字段
    private String remarks; //
    private Long itemIndex; //
    private String setmealCode; //套餐主体编号
    private String itemImg; //商品图片
    private String batchNumber; //打印批号
    private String attCode;//菜品口味
    private BigDecimal changeAmt;//口味加价
    private String bulkPurchase;//是否团购
    private BigDecimal accountAmt;//挂账金额
    private String accountPaytype;//挂账付款方式
    private String purchaseType;//团购券类型（口碑、美团等）
    private String appAuthToken;//认领门店返回的token
    private String eId;//商家登录ERP帐号ID
    private String eName;//商家登录ERP帐号名称
    private String lastUpdateNameId; //
    private Date lastUpdateTime; //
    private String printState;
    private BigDecimal servCost;
    private String printer;
    private String printDesc;
}
