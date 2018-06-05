package com.dw.util;

import com.dw.Main;
import com.dw.enums.PosSettingEnum;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class DecimalUtil {

	private static final int DEC_POINT_NUM = 2;//计算时精确到几位小数点
	private static final String DECIMAL_FROMAT = "0.00";//格式化数据时，参照的格式
	private static final String STRING_FROMAT = "%.2f";//格式化数据时，参照的格式
	/**
	 * 按照设计好的精度，计算两个BigDecimal类型数据之和
	 * @param num1
	 * @param num2
	 * @return 计算结果
	 */
	public static BigDecimal add(BigDecimal num1,BigDecimal num2){
		return num1.add(num2).setScale(DEC_POINT_NUM, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 按照设计好的精度，计算两个BigDecimal类型数据之积
	 * @param num1
	 * @param num2
	 * @return 计算结果
	 */
	public static BigDecimal multiply(BigDecimal num1,BigDecimal num2){
		return num1.multiply(num2).setScale(DEC_POINT_NUM, BigDecimal.ROUND_FLOOR);
	}
	public static BigDecimal multiplyUp(BigDecimal num1,BigDecimal num2){
		return num1.multiply(num2).setScale(DEC_POINT_NUM, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal multiplyUp4(BigDecimal num1,BigDecimal num2){
		return num1.multiply(num2).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 按照设计好的精度，计算两个BigDecimal类型数据之商
	 * @param num1
	 * @param num2
	 * @return 计算结果
	 */
	public static BigDecimal divide(BigDecimal num1,BigDecimal num2){
		return num1.divide(num2,DEC_POINT_NUM, BigDecimal.ROUND_FLOOR);
	}
	/**
	 * 按照设计好的精度，计算两个BigDecimal类型数据之商 四舍五入保留两位小数
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal divideUP(BigDecimal num1,BigDecimal num2){
		return num1.divide(num2,DEC_POINT_NUM, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal divideUP4(BigDecimal num1,BigDecimal num2){
		return num1.divide(num2,4, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 把一个BigDecimal类型的数据转换成指定格式的String

	 * @return
	 */
	public static String decimalFormat(BigDecimal num){
		DecimalFormat format = new DecimalFormat(DECIMAL_FROMAT);
		return format.format(num);
	}
	
	/**
	 * 按照设计好的精度，计算两个BigDecimal类型数据之差
	 * @param num1
	 * @param num2
	 * @return 计算结果
	 */
	public static BigDecimal subtract(BigDecimal num1,BigDecimal num2){
		return num1.subtract(num2).setScale(DEC_POINT_NUM, BigDecimal.ROUND_HALF_UP);
	}
	
	
	/**
	 * 将给定的字符串转换成指定格式的BigDecimal类型数据
	 * @param str 待转换的数据
	 */
	public static BigDecimal getBigDecimal(String str){
		if(str==null || ("").equals(str)){
			str="0.00";
		}
		BigDecimal decimal = new BigDecimal(str);
		return decimal.setScale(DEC_POINT_NUM, BigDecimal.ROUND_FLOOR);
	}

	public static BigDecimal setScale(BigDecimal decimal, int scale, int roundingMode) {
		return decimal.setScale(scale, roundingMode);
	}

	/**
	 * double 根據系統策略來轉換 BigDecimal
	 * @param doubleVal
	 * @return
	 */

	public static BigDecimal doubleToBigDecimalByPloy(double doubleVal){
		return new BigDecimal(doubleVal).setScale(Integer.parseInt(Main.posSetting.get(PosSettingEnum.decimal_num.getValue())), Integer.parseInt(Main.posSetting.get(PosSettingEnum.rounding_default.getValue())));

	}

	public static BigDecimal bigDecimalByPloy(BigDecimal bigDecimal){
		return  bigDecimal.setScale(Integer.parseInt(Main.posSetting.get(PosSettingEnum.decimal_num.getValue())), Integer.parseInt(Main.posSetting.get(PosSettingEnum.rounding_default.getValue())));

	}

	public static BigDecimal doubleToBigDecimalByPloy(double doubleVal,int decimalLength){
		return new BigDecimal(doubleVal).setScale(decimalLength, Integer.parseInt(Main.posSetting.get(PosSettingEnum.rounding_default.getValue())));

	}


	public static void main(String args[]){
		System.out.println(DecimalUtil.getBigDecimal("12.415"));
	}
}
