package com.dw.enums;

/**
 * Created by liang.caixing on 2018/4/25.
 */
public enum LogTypeEnum {
    UCOP("用券", "UCOP"),
    DSRV("服務費", "DSRV"),
    TDIS("提前離座折扣", "TDIS"),
    //UDIS("使用折扣", "UDIS"),
    FULL("全單折扣", "FUCD"),
    SINGLE("單項折扣", "SICD"),
    CTBL("轉台", "CTBL"),
    URGE("追單", "URGE"),
    CTBD("轉台菜品", "CTBD"),
    DAYE("清機","DAYE"),
    MDAY("清機","MDAY"),
    TFER("分單", "TFER"),
    PERS("更改人數", "PERS"),
    BIND("綁卡", "BIND");


    private String name;
    private String value;

    LogTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getTypeByValue(String code) {
        String name = "未知類型";
        LogTypeEnum[] enums = LogTypeEnum.values();
        for (LogTypeEnum e : enums) {
            if (e.getValue().equals(code)) {
                name = e.getName();
                break;
            }
        }
        return name;
    }
}
