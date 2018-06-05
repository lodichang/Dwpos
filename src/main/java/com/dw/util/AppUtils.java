
package com.dw.util;


import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.CRC32;

public class AppUtils {

	public static Font getDefaultFont() {
		String fontsPath = System.getProperty("user.dir")  + File.separator + "fonts" + File.separator + "NotoSansCJKtc-Light.ttf";
		Font font = null;
		try {
			font = Font.loadFont(new FileInputStream(fontsPath), 14
			);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return font;
	}

	public static Image loadImage(String name) {
		String imagePath = System.getProperty("user.dir")  + File.separator + "image" + File.separator + name;
		Image image = null;
		try {
			//image = new Image(new FileInputStream(imagePath),20,20,false,true);
			image = new Image(new FileInputStream(imagePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}



	public static Image loadImage(String name, double requestedWidth, double requestedHeight) {
		String imagePath = System.getProperty("user.dir")  + File.separator + "image" + File.separator + name;
		Image image = null;
		try {
			image = new Image(new FileInputStream(imagePath), requestedWidth, requestedHeight,  true, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}

	/** The logger. */


	/**
	 * Get locale cookie.
	 * 
	 * @param request
	 *            the request
	 * @return Locale
	 */


	/**
	 * <p>
	 * String representation of a calendar. Format: MM/DD/YYYY
	 * </p>
	 * 
	 * @param pCalendar
	 *            the calendar
	 * @return String
	 */
	public static String getDisplayDate(Calendar pCalendar) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		if (pCalendar != null)
			return format.format(pCalendar.getTime());
		else
			return "";
	}

	/**
	 * <p>
	 * Convert string representation of a date to calendar.
	 * </p>
	 * 
	 * @param str
	 *            the str
	 * @return Calendar
	 */
	public static Calendar str2Calendar(String str) {
		Calendar cal = null;
		if (isNotBlank(str)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date d = sdf.parse(str);
				cal = Calendar.getInstance();
				cal.setTime(d);
			} catch (ParseException e) {
			}
		}
		return cal;
	}

	/**
	 * <p>
	 * String representation of current date. Format: MM/DD/YYYY
	 * </p>
	 * 
	 * @return String
	 */
	public static String getCurrentDate() {
		return getDisplayDate(GregorianCalendar.getInstance());
	}

	/**
	 * <p>
	 * Get localhost and port url root, i.e. http://localhost:port
	 * </p>
	 * 
	 * @param request
	 *            the request
	 * @return String
	 */


	/**
	 * Gets the gB string.
	 * 
	 * @param s
	 *            the s
	 * @return the gB string
	 */
	public static String getGBString(String s) {
		try {
			return new String(s.getBytes("ISO-8859-1"), "GB2312");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the iSO string.
	 * 
	 * @param s
	 *            the s
	 * @return the iSO string
	 */
	public static String getISOString(String s) {
		try {
			return new String(s.getBytes("GB2312"), "ISO-8859-1");
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Checks if is blank.
	 * 
	 * @param str
	 *            the str
	 * @return true, if is blank
	 */
	public static boolean isBlank(final String str) {
		return (str == null) || (str.trim().length() <= 0) || (str.equals("undefined"));
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param str
	 *            the str
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final String str) {
		return !(isBlank(str));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param objs
	 *            the objs
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Object[] objs) {
		return (objs == null) || (objs.length <= 0);
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param objs
	 *            the objs
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Object[] objs) {
		return !(isBlank(objs));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param objs
	 *            the objs
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Object objs) {
		return (objs == null) || "".equals(objs);
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param objs
	 *            the objs
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Object objs) {
		return !(isBlank(objs));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Collection obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Collection obj) {
		return !(isBlank(obj));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Set obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Set obj) {
		return !(isBlank(obj));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Serializable obj) {
		return obj == null;
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Serializable obj) {
		return !(isBlank(obj));
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is blank
	 */
	public static boolean isBlank(final Map obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(final Map obj) {
		return !(isBlank(obj));
	}

	/**
	 * List2 strings.
	 * 
	 * @param list
	 *            the list
	 * @return the string[]
	 */
	public static String[] list2Strings(List<String> list) {
		String[] value = null;
		try {
			if (list == null)
				return null;
			value = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				value[i] = list.get(i);
			}
		} catch (Exception e) {
			//logger.error("list is null: " + e);
		}
		return value;
	}

	/**
	 * List2 string.
	 * 
	 * @param list
	 *            the list
	 * @return the string
	 */
	public static String list2String(List<Object> list) {
		if (AppUtils.isBlank(list)) {
			return "";
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(list.get(0));
		for (int idx = 1; idx < list.size(); idx++) {
			sbuf.append(",");
			sbuf.append(list.get(idx));
		}
		return sbuf.toString();
	}

	/**
	 * Strings2list.
	 * 
	 * @param args
	 *            the args
	 * @return the list
	 */
	public static List<String> Strings2List(String[] args) {
		List<String> list = new ArrayList<String>();
		try {
			if (args == null)
				return null;
			for (int i = 0; i < args.length; i++) {
				list.add(args[i]);
			}
		} catch (Exception e) {
			//logger.error("list is null: " + e);
		}
		return list;
	}

	/**
	 * Gets the strings.
	 * 
	 * @param str
	 *            the str
	 * @return the strings
	 */
	public static String[] getStrings(String str) {
		List<String> values = getStringCollection(str);
		if (values.size() == 0) {
			return null;
		}
		return values.toArray(new String[values.size()]);
	}

	/**
	 * Gets the string collection.
	 * 
	 * @param str
	 *            the str
	 * @return the string collection
	 */
	public static List<String> getStringCollection(String str) {
		List<String> values = new ArrayList<String>();
		if (str == null)
			return values;
		StringTokenizer tokenizer = new StringTokenizer(str, ",");
		values = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return values;
	}



	/**
	 * Format number.
	 * 
	 * @param number
	 *            the number
	 * @return the string
	 */
	public static String formatNumber(Long number) {
		if (number == null) {
			return null;
		}
		NumberFormat format = NumberFormat.getIntegerInstance();
		// 设置數字的位數 由实际情况的最大數字决定
		format.setMinimumIntegerDigits(8);
		// 是否按每三位隔开,如:1234567 将被格式化为 1,234,567。在这里选择 否
		format.setGroupingUsed(false);
		return format.format(number);
	}

	/**
	 * Gets the cR c32.
	 * 
	 * @param value
	 *            the value
	 * @return the cR c32
	 */
	public static Long getCRC32(String value) {
		CRC32 crc32 = new CRC32();
		crc32.update(value.getBytes());
		return crc32.getValue();
	}



	/**
	 * Array to string.
	 * 
	 * @param strs
	 *            the strs
	 * @return the string
	 */
	public static String arrayToString(String[] strs) {
		if (strs.length == 0) {
			return "";
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(strs[0]);
		for (int idx = 1; idx < strs.length; idx++) {
			sbuf.append(",");
			sbuf.append(strs[idx]);
		}
		return sbuf.toString();
	}

	/**
	 * Gets the default value.
	 * 
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @return the default value
	 */
	public static String getDefaultValue(String value, String defaultValue) {
		if (isNotBlank(value)) {
			return value;
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * 加密算法
	 * algorithm： sha1, MD5
	 * @param password
	 * @param algorithm
	 * @return
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;
		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			e.printStackTrace();
			return password;
		}
		md.reset();
		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);
		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}
	
	/**
	 * 比较2个值是否一致
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isTheSame(Object a, Object b) {
		if(a == null && b == null){
			return true;
		}
		return a != null && a.equals(b);
	}
	
	/**
	 * 把颜文字去掉
	 * @param nickName
	 * @return
	 */
	public static String removeYanText(String nickName){
		if(AppUtils.isBlank(nickName)){
			return "";
		}
		byte[] b_text = nickName.getBytes();
		for (int i = 0; i < b_text.length; i++) {
			if((b_text[i] & 0xF8) == 0xF0){
				for (int j = 0; j < 4; j++) {
					b_text[i+j] = 0x00; //空字符
				}
				i+=3;
			}
		}
		return new String(b_text);
	}

	//sha1加密算法  wenjing
	public static String getSha1(String str){
		if (null == str || 0 == str.length())
			return null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public static String getRandNum(int charCount) {
		String charValue = "";
		for (int i = 0; i < charCount; i++) {
			char c = (char) (randomInt(0, 10) + '0');
			charValue += String.valueOf(c);
		}
		return charValue;
	}

	public static int randomInt(int from, int to) {
		Random r = new Random();
		return from + r.nextInt(to - from);
	}



	/**
	 * 计算地球上任意两点(经纬度)距离
	 *
	 * @param long1 第一点经度
	 * @param lat1 第一点纬度
	 * @param long2 第二点经度
	 * @param lat2 第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double distance(double long1, double lat1, double long2, double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	/**
	 * 获取枚举类
	 */
	public static <T extends Enum<T>> T getEnumByValue(Class<T> enumType, String value) {
		return Enum.valueOf(enumType, value);
	}


	public static String autoGenericCode(String code, int num) {
		String result = "";
		result = String.format("%0" + num + "d", Integer.parseInt(code));

		return result;
	}

	public static String removeRepeatedChar(String s, String split) {
		Set<String> mlinkedset = new LinkedHashSet<>();
		String[] strarray = s.split(split);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strarray.length; i++)
		{
			if (!mlinkedset.contains(strarray[i]))
			{
				mlinkedset.add(strarray[i]);
				sb.append(strarray[i] + split);
			}
		}
		return sb.toString().substring(0, sb.toString().length() - 1);

	}


	public static void main(String[] args) {
		System.out.println(removeRepeatedChar(";997;998;998;998;998;999;999;999;999", ";"));
	}
}
