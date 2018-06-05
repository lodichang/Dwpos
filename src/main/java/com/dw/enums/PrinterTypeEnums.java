package com.dw.enums;

public enum PrinterTypeEnums {
    N("POS后厨正常飞单","N"),
    C("POS后厨取消飞单","C"),
    R("POS后厨重印飞单","R"),
    I("POS后厨讯息飞单","I"),
    V("POS后厨换菜飞单","V"),
    S("單項食品轉台","S"),
    F("全單轉台","F"),
    P("指定印機列印","P"),
    H("即起","H"),
    T("列印测试","T"),
    U("追单","U");

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

    PrinterTypeEnums(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static PrinterTypeEnums getEnumByValue(String value){
        if(value == null){
            return null;
        }
        for(PrinterTypeEnums stateEnum : PrinterTypeEnums.values()){
            if((stateEnum.getValue().equals(value))){
                return stateEnum;
            }
        }
        return null;
    }
}
