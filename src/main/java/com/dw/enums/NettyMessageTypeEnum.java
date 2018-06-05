package com.dw.enums;

public enum NettyMessageTypeEnum {
    CHANGEPERSON("CHANGEPERSON", "改人數"),
    SPLITTABLE("SPLITTABLE", "分台"),
    OPENTABLE("OPENTABLE", "開台"),
    SENDORDER("SENDORDER", "送單"),
    REMEDYORDER("REMEDYORDER", "補單"),
    SETTLE("SETTLE", "結帳"),
    CHANGETABLE("CHANGETABLE", "轉台"),
    CHARGE("CHARGE", "拍腳"),
    HOLDON("HOLDON", "叫起"),
    IMMEDIATELY("IMMEDIATELY", "即起"),
    CLEANSPLITTABLE("CLEANSPLITTABLE", "清空台"),
    ADDTABLE("ADDTABLE", "添加台"),
    DELETETABLE("DELETETABLE", "刪除台"),
    MOVETABLE("MOVETABLE", "移動台"),
    CHANGEORDER("CHANGEORDER", "改單"),
    DAILYKNOT("DAILYKNOT", "日結"),
    CLEARPOS("CLEARPOR", "清機"),
    PAUSEATT("PAUSEATT", "口味暫停"),
    PAUSEITEM("PAUSEITEM", "食品暫停"),
    BUCKLESTEWCHANGE("BUCKLESTEWCHANGE", "扣燉更改"),
    TOPBUTTONCHANGE("TOPBUTTONCHANGE", "按鍵更改"),
    DOWNLOADDATA("DOWNLOADDATA", "雲端數據下載"),
    HOLDONNOTIFY("HOLDONNOTIFY", "叫起15分鐘提示"),
    TABLETIMEOUTNOTIFY("TABLETIMEOUTNOTIFY", "桌台到時提醒"),
    PERIODCHANGE("PERIODCHANGE", "時段轉換"),
    AUTHORITYCHANGE("AUTHORITYCHANGE", "用戶權限更改"),
    KITCHENPRINT("KITCHENPRINT", "後廚出飛"),
    CHECKHOLDON("CHECKHOLDON","叫起閃爍"),
    PRINTTICKET("PRINTTICKET","打印上菜紙"),
    UNCHECKHOLDON("UNCHECKHOLDON","取消叫起閃爍"),
    SHOWTIMEOUT("SHOWTIMEOUT","閃爍超時桌臺"),
    UNSHOWTIMEOUT("UNSHOWTIMEOUT","取消閃爍超時桌臺"),
    HOLDONTIMEOUT("HOLDONTIMEOUT","叫起超时闪烁"),
    UPDATEITEMPRICE("UPDATEITEMPRICE","更改菜品價格"),
    UPDATEITEMNAME("UPDATEITEMNAME","更改菜品名稱");
    private String value;
    private String name;

    NettyMessageTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
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
}
