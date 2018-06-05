package com.dw.print;

public class Style {
	
	/*
	 * 需要两个参数 CARD_CODE 卡号    ，TABLE_NUM 台号
	 * #QRCODE05CARD_CODE 命令说明：
	 * #QRCODE打印二维码；
	 * 05 二维码大小，取值范围05，06，07~16，注意这里一定要是两位数，
	 * CARD_CODE 二维码内容能
	*/
	private static String[] style4={
		"~0,210",
		"#INITIALIZE",
		"#CENTER",
		"#QRCODE05CARD_CODE",
		"$卡号：|CARD_CODE",
		"#BIG2",
		"$台号：|TABLE_NUM",
		"#GOHEAD5",
		"#GOHEAD5",
		"#CUTPAPER"
	};

}
