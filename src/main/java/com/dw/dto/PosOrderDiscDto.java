package com.dw.dto;

import com.dw.entity.PosOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by liang.caixing on 2018/4/28.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosOrderDiscDto extends PosOrder{

    private BigDecimal costAmt;   //减去所有折扣后的金额
    private String cat; //商品所属大类
    private String scat; //大大类编号
    private String disc; //折扣編號
    private String itemService; //服務費,TRUE--是，FALSE--否，Z
    private String billDisc; //是否可以折扣
    private String pservice; //分店價格獨有價格-服務費,TRUE--是，FALSE--否，Z--跟食品設定
    private String pbillDisc; //分店價格獨有價格-沒有折扣,TRUE--是，FALSE--否，Z--跟食品設定
    private BigDecimal dscpDisc = new BigDecimal(0.00); //大大類折扣
    private BigDecimal dctpDisc = new BigDecimal(0.00); //大類折扣
    private BigDecimal dorpDisc = new BigDecimal(0.00); //單折扣%
    private BigDecimal doraDisc = new BigDecimal(0.00); //单折扣$
    private BigDecimal dsrvDisc = new BigDecimal(0.00); //服务费
    private String comb;//組別編號
}
