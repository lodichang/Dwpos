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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* pos系统订单主表 实体类
*/
@TableName("tb_pos_tran")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosTran implements Serializable{
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = DateUtil.getNowTime(); //
    @TableField("REF_NUM")
    private String refNum; //主单号
    @TableField("SUB_REF")
    private String subRef; //副单号
    @TableField("STATION_ID")
    private String stationId; //机器编号
    @TableField("OUTLET")
    private String outlet; //分店编号
    @TableField("OUTLINE")
    private String outline; //线
    @TableField("COMPANY_ID")
    private String companyId; //公司id
    @TableField("TABLE_NUM")
    private String tableNum; //桌台编号
    @TableField("TRAN_TYPE")
    private String tranType; //账单类型
    @TableField("OPEN_STAFF")
    private String openStaff; //开台员工
    @TableField("BILL_STAFF")
    private String billStaff; //结账员工
    @TableField("PAY_STAFF")
    private String payStaff; //付款员工
    @TableField("SERV_STAFF")
    private String servStaff; //服务员工
    @TableField("PERSON")
    private Integer person; //人数
    @TableField("IN_DATE")
    private Date inDate; //入单日期
    @TableField("IN_TIME")
    private Date inTime; //入单时间
    @TableField("BILL_DATE")
    private Date billDate; //结账日期
    @TableField("BILL_TIME")
    private Date billTime; //结账时间
    @TableField("HEAD_COUNT")
    private String headCount; //人头数
    @TableField("ORDER_AMT")
    private BigDecimal orderAmt; //账单总金额
    @TableField("ITEM_DISC")
    private BigDecimal itemDisc; //
    @TableField("CAT_DISC")
    private BigDecimal catDisc; //
    @TableField("ROOM_AMT")
    private BigDecimal roomAmt; //
    @TableField("ROOM_DISC")
    private BigDecimal roomDisc; //
    @TableField("HEAD_AMT")
    private BigDecimal headAmt; //
    @TableField("HEAD_DISC")
    private BigDecimal headDisc; //
    @TableField("SERV_AMT")
    private BigDecimal servAmt; //
    @TableField("TAX_AMT")
    private BigDecimal taxAmt; //
    @TableField("TAX_AMT_1")
    private BigDecimal taxAmt1; //
    @TableField("ORDER_DISC")
    private BigDecimal orderDisc; //折扣金额
    @TableField("ROUNDING")
    private BigDecimal rounding; //找零差额
    @TableField("BILL_AMT")
    private BigDecimal billAmt; //实际账单金额
    @TableField("CASH_AMT")
    private BigDecimal cashAmt; //现金付款金额
    @TableField("OTHER_AMT")
    private BigDecimal otherAmt; //其它付款方式付款金额
    @TableField("NSALES_AMT")
    private BigDecimal nsalesAmt; //
    @TableField("OVER_AMT")
    private BigDecimal overAmt; //超付金额
    @TableField("TIPS")
    private BigDecimal tips; //小费
    @TableField("MEMBER")
    private String member; //清机序号
    @TableField("PAY_COUNT")
    private String payCount; //
    @TableField("VOID_REF")
    private String voidRef; //
    @TableField("REASON")
    private String reason; //改单、消单原因编号
    @TableField("T_INDEX")
    private Long tIndex; //
    @TableField("PERIOD")
    private String period; //更次
    @TableField("RECEIPT")
    private String receipt; ////线上订单
    @TableField("PRT_COUNT")
    private String prtCount; //打印次数
    @TableField("PAYTYPE_1")
    private String paytype1; //付款方式1
    @TableField("PAYTYPE_2")
    private String paytype2; //付款方式2
    @TableField("PAYTYPE_3")
    private String paytype3; //付款方式3
    @TableField("PAYTYPE_4")
    private String paytype4; //付款方式4
    @TableField("PAYTYPE_5")
    private String paytype5; //付款方式5
    @TableField("SETTLED")
    private String settled; //
    @TableField("PRINT_STATE")
    private String printState; // 打印狀態，UNPRINT("未打印", "UNPRINT"), PREPRINT("等待打印", "PREPRINT"), PRINTED("已經打印", "PRINTED")
    @TableField("INVOICE_NUMBER")
    private String invoiceNumber;
    @TableField("REMARKS")
    private String remarks; //
    @TableField(value = "LAST_UPDATE_NAME_ID",  strategy =   FieldStrategy.IGNORED)
    private String lastUpdateNameId; //
    @TableField(value = "LAST_UPDATE_TIME",  strategy =   FieldStrategy.IGNORED)
    private Date lastUpdateTime; //
    @TableField(value="LEAVE_DATE")
    private Date leaveDate;
    @TableField(value="LEAVE_TIME")
    private Date leaveTime;
}
