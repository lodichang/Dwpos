package com.dw.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by li.yongliang on 2017/10/12.
 */
@TableName("ORDER_GROUP_PURCHASE")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderGroupPurchase implements Serializable {


    private String id;

    private String version;

    private String outline;

    private String outlet;

    private String tableNum;

    private String refNum;

    private String subRef;

    private String type;

    private String setmeal;

    private String itemIdx;

    private String itemCode;

    private String coupNo;

    private String batchNo;

    private String stat;

    private String purchaseType;

    private String sepChar;

    private String setmealCode;

    private String appAuthToken;

    private String eId;

    private String eName;

    private String random;

    private String pringType;

    private String orderOptiontype;
    private String companyId;

    private String lastUpdateTime;


}
