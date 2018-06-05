package com.dw.enums;

import com.dw.Main;
import com.dw.util.AppUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liang.caixing on 2017/10/23.
 */
public enum TableStateEnum {
    NOTDRIVE(Main.languageMap.get("table.notDrive"),null ,"notDrive","notDriveTableFlowPane"),
    OFFTHETABLE(Main.languageMap.get("table.offTheTable"),"B", "offTheTable","offTheTableFlowPane"),
    OPENTABLEORDER(Main.languageMap.get("table.openTableOrder"),"C","openTableOrder","openTableOrderFlowPane"),
    HASINVOICED(Main.languageMap.get("table.hasInvoiced"),"D", "hasInvoiced","hasInvoicedFlowPane"),
    HASPAYED(Main.languageMap.get("table.hasPayed"),"E","hasPayed","hasPayedFlowPane"),
    SPLITTABLE(Main.languageMap.get("global.splitTable"),"S","splitTable","splitTableFlowPane"),
    HOLDON(Main.languageMap.get("table.holdon"),"H","holdOnTable","holdOnTableFlowPane");



    private String name;
    private String value;
    private String style;
    private String parentStyle;

    TableStateEnum(String name, String value,String style,String parentStyle){
        this.name = name;
        this.value = value;
        this.style = style;
        this.parentStyle = parentStyle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getParentStyle() {
        return parentStyle;
    }

    public void setParentStyle(String parentStyle) {
        this.parentStyle = parentStyle;
    }

    public static Map<String,String> getTableTypes(){
        LinkedHashMap<String,String> typeMap= new LinkedHashMap<>();
        TableStateEnum[] tableTypeEnums =  TableStateEnum.values();
        for( TableStateEnum tableTypeEnum : tableTypeEnums){
                typeMap.put(tableTypeEnum.getName(), tableTypeEnum.getValue());
        }
        return typeMap;
    }

    public static String getNameByValue(String name){
        for( TableStateEnum tableTypeEnum :  TableStateEnum.values()){
           if(tableTypeEnum.getName().equals(name)){
               return tableTypeEnum.getValue();
           }
        }
        return  null;
    }

    public static  String getStyleByValue(String value){
        for( TableStateEnum tableTypeEnum :  TableStateEnum.values()){
            if(tableTypeEnum.getValue() == null && value == null){
                return tableTypeEnum.getStyle();
            }
            else if(tableTypeEnum.getValue() !=null && tableTypeEnum.getValue().equals(value)){
                return tableTypeEnum.getStyle();
            }
        }
        return null;
    }

    public static  String getParentStyleByValue(String value){
        for( TableStateEnum tableTypeEnum :  TableStateEnum.values()){
            if(tableTypeEnum.getValue() == null && value == null){
                return tableTypeEnum.getParentStyle();
            }
            else if(tableTypeEnum.getValue() !=null && tableTypeEnum.getValue().equals(value)){
                return tableTypeEnum.getParentStyle();
            }
        }
        return null;
    }






}
