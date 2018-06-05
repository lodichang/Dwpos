package com.dw.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liang.caixing on 2017/10/23.
 */
public enum TableTypeEnum {
    CARRYOUT("A", "外賣"),
    CARRYOUTPLACE("D", "外賣檔"),
    ROOM("B","包房"),
    GENERAL("T", "普通台"),
    TEST("S", "試飛臺不計入收入");

    private String value;
    private String name;

    TableTypeEnum(String value,String name){
        this.name = name;
        this.value = value;
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

    public static Map<String,String> getTableTypes(){
        LinkedHashMap<String,String> typeMap= new LinkedHashMap<>();
        TableTypeEnum[] tableTypeEnums =  TableTypeEnum.values();
        for( TableTypeEnum tableTypeEnum : tableTypeEnums){
            typeMap.put( tableTypeEnum.getName(), tableTypeEnum.getValue());
        }
        return typeMap;
    }

    public static String getValueByName(String name){
        for( TableTypeEnum tableTypeEnum :  TableTypeEnum.values()){
           if(tableTypeEnum.getName().equals(name)){
               return tableTypeEnum.getValue();
           }
        }
        return  null;
    }
    public static String getNameByValue(String value){
        for( TableTypeEnum tableTypeEnum :  TableTypeEnum.values()){
            if(tableTypeEnum.getValue().equals(value)){
                return tableTypeEnum.getName();
            }
        }
        return  null;
    }


}
