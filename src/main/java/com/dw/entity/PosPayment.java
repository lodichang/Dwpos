package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
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
 * pos付款方式表 实体类
 */
@TableName("tb_pos_payment")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PosPayment implements Serializable {
    @TableField("ID")
    private String id; //
    @TableField("VERSION")
    private Date version = new Date(); //
    @TableField("CODE")
    private String code; //
    @TableField("OUTLINE")
    private String outline; //
    @TableField("TYPE")
    private String type; //
    @TableField("DESC1")
    private String desc1; //
    @TableField("DESC2")
    private String desc2; //
    @TableField("DESC3")
    private String desc3; //
    @TableField("DESC4")
    private String desc4; //
    @TableField("SEQ")
    private Long seq; //
    @TableField("CHARGE")
    private BigDecimal charge; //
    @TableField("NON_SALES")
    private String nonSales; //
    @TableField("PIC")
    private String pic; //
    @TableField("ROOM_NUM")
    private String roomNum; //
    @TableField("CARD_NUM")
    private String cardNum; //
    @TableField("CARD_LEN")
    private String cardLen; //
    @TableField("CUST_NAME")
    private String custName; //
    @TableField("AUTHOR")
    private String author; //
    @TableField("EXPIRE")
    private String expire; //
    @TableField("CAN_SELECT")
    private String canSelect; //
    @TableField("INPUT_TYPE")
    private String inputType; //
    @TableField("DISC_TYPE")
    private String discType; //
    @TableField("OVER_TYPE")
    private String overType; //
    @TableField("INCLUDE")
    private String include; //
    @TableField("REMARKS")
    private String remarks; //
    @TableField("START_DATE")
    private String startDate; //
    @TableField("END_DATE")
    private String endDate; //
    @TableField("STARTTIME")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date starttime; //
    @TableField("ENDTIME")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date endtime; //
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
    @TableField("pay_img")
    private String payImg;
}
