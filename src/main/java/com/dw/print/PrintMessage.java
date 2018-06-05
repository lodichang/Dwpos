package com.dw.print;

import java.awt.*;

public class PrintMessage {
	public static Font DRIVERS_PRINT = new Font("宋体", 0, 10);
	public static String[]	POPUPBOX_TWOBUTTON = {"確定","取消"}; 
	public static String[]	CFG_MESSAGE={"配置文件路徑錯誤","配置項數據類型錯誤或缺少配置項","缺少配置文件！"};
	public static String[]	ASK_MESSGE = {"提示", "確定","問題"}; 
	public static String[]	LOGO_MESSAGE={"LOGO圖片路徑錯誤，是否繼續？","LOGO圖片文件不存在，將無法打印LOGO"};
	public static String[]	PORTEXCEPTION={"端口未找到","端口正在使用","端口初始化失敗","中文亂碼處理失敗","打開錢箱失敗","校驗信息讀寫失敗","主體數據打印失敗","切紙命令寫入失敗","打印機連接異常，請檢查"};
	public static String[]	CLASSEXCEPTION={"打印文件找不到或已損壞"};
}
