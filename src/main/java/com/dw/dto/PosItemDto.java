package com.dw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by li.yongliang on 2018/4/12.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosItemDto implements Serializable {

    //中文繁體
    private String desc1;
    //中文簡體
    private String desc2;
    //英文
    private String desc3;
    //其他語言
    private String desc4;
    private String itemCode;
    private BigDecimal price;
    //原價
    private BigDecimal orgPrice;
//    商品圖片
    private String itemImg;

    private String combId;

    //是否收取服務費
    private String service;
    //菜品所屬印機
    private String prn;

    //菜品销售状态
    private String discont;



    //item表标记是否折扣
    private String billDisc;
    //price表标记是否有服务费
    private String priceService;
    //price表标记是否有折扣
    private String priceBillDisc;
    //所属大类编号
    private String cat;
    //所屬大類名稱
    private String catName;
    //所屬大大類編號
    private String scat;
    //所屬大大類名稱
    private String scatName;
    private int attMin;
    private int attMax;
    private String catt; //共同口味                可多选，多个共同口味直接字符串拼接
    private String iopen;
}
