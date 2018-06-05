package com.dw.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CouponTypeEnum {
	GIFT("GIFT","禮品券"),
	CASH("CASH","現金券"),
	DISCOUNT("DISCOUNT","折扣券"),
	HAOCOUPON("HAOCOUPON","補飛券"),
	OTHER("OTHER","其他");

	private final String name;
	private final String code;

	CouponTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getNameByCode(String code) {
		switch (code) {
			case "GIFT" : return "禮品券";
			case "CASH" : return "現金券";
			case "OTHER" : return "其他";
			case "DISCOUNT" : return "折扣券";
			case "HAOCOUPON" : return "補飛券";
			default: return null;
		}
	}

	public static String getRateByCode(String code, String couponConfig) {
		switch (code) {
			case "GIFT" : return couponConfig.split(",")[0] + "折";
			case "CASH" : return "抵用" + couponConfig.split(",")[0] + "元";
			case "OTHER" : return couponConfig;
			case "DISCOUNT" : return "優惠折扣" + couponConfig.split(",")[0];
			default: return couponConfig;
		}
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public static Map<String,String> getCouponTypeEnum(){
		LinkedHashMap<String,String> cMap = new LinkedHashMap<>();
		CouponTypeEnum[] couponTypeEnums = CouponTypeEnum.values();
		for (CouponTypeEnum c:couponTypeEnums){
			cMap.put(c.getName(),c.getCode());
		}
		return cMap;
	}
}
