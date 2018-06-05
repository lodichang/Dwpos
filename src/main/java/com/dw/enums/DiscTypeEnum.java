package com.dw.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liang.caixing on 2018/4/28.
 */
public enum DiscTypeEnum {
    DHDP("DHDP", "茶類折扣"),
    DTAX("DTAX", "稅項折扣"),
    DSCP("DSCP", "大大類折扣"),
    DCTP("DCTP", "大類折扣"),
    DCIP("DCIP", "禮券系統--食品折扣"),
    DCVP("DCVP", "禮券系統--禮券折扣"),
    GZZK("GZZK", "挂账折扣"),
    YFZK("YFZK", "预付卡折扣"),
    XJZK("XJZK", "现金券折扣"),
    QTZK("QTZK", "其他折扣"),
    DHDC("DHDC", "免人頭費"),
    DORP("DORP", "單折扣%"),
    DORA("DORA", "单折扣$"),
    WBIL("WBIL", "全单折"),
    DSRV("DSRV", "服务费"),
    DRMP("DRMP", "房折扣%"),
    DRMA("DRMA", "房折扣$"),
    DSCT("DSCT", "服務費選項"),
    COMB("COMB", "組別折扣");




    private String name; //描述
    private String value; //值


    DiscTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // 根据value返回枚举类型,主要在switch中使用
    public static DiscTypeEnum getByValue(String value) {
        for (DiscTypeEnum code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return null;
    }

    public static String getNameByValue(String value) {
        for (DiscTypeEnum code : values()) {
            if (code.getValue().equals(value)) {
                return code.getName();
            }
        }
        return null;
    }



    public static Map<String,String> getDiscs(){
        LinkedHashMap<String,String> weekMap= new LinkedHashMap<>();
        DiscTypeEnum[] discTypeEnums =  DiscTypeEnum.values();
        for( DiscTypeEnum discTypeEnum : discTypeEnums){
            weekMap.put( discTypeEnum.getName(), discTypeEnum.getValue());
        }
        return weekMap;
    }

}
