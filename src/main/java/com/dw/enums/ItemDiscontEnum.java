package com.dw.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luo.dachang on 2017/8/3.
 */
public enum ItemDiscontEnum {

    SELLING("销售中","0"),
    PAUSE("暂停","1"),
    PERMANENTPAUSE("永久暂停","2")
    ;

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ItemDiscontEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Map<String,String> getPosItemStatus(){
        LinkedHashMap<String,String> posItemStatuMap= new LinkedHashMap<>();
        ItemDiscontEnum[] PosItemStatusEnu= ItemDiscontEnum.values();
        for( ItemDiscontEnum  posItemStatusEnum :  PosItemStatusEnu){
            posItemStatuMap.put( posItemStatusEnum.getName(), posItemStatusEnum.getValue());
        }
        return posItemStatuMap;
    }




}


