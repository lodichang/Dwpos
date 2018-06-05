package com.dw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Title: ID管理器
 *
 * Description: ID管理器
 *
 * Company: Copyright @ 2012  版权所有
 *
 * @author: 聂水根
 *
 * @date: 2012-11-2
 *
 * @version 1.0 初稿
 *
 */

public class IDManager {
	
	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static AtomicInteger counter = new AtomicInteger(0);
	public IDManager() {
	}
	
	/**
	 * Title : 根据uuid的方式来生成主键 
	 * @author : 聂水根
	 * @return
	 */
	public static String generateIDs(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * Title : 生成单个主键
	 * @author : 聂水根
	 * @param userId
	 * @return
	 */
	public static  String generateIDs(String userId) {
		if (userId == null || "".equals(userId)) {
			userId = "0";
		}
		long curentTime = System.currentTimeMillis();
		String ID = String.valueOf(curentTime) + userId
				+ Math.round(Math.random() * 1000000);
		return ID;
	}
	
	public static String generateID() {
		UUID uuid = UUID.randomUUID();
		String chars = null;
		try {
			chars = IDManager.Md5Security(uuid.toString().replaceAll("-", ""), 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chars;
	}
	
	public static String code() {
	    String code = null;
	    try {
	        synchronized (IDManager.class) {
	            UUID uuid = UUID.randomUUID();
	            String chars = uuid.toString().replaceAll("-", "");
	            code = IDManager.Md5Security(chars, 5);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return code;
	}
	
	public static String Md5Security(String input, int bit) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
			if (bit == 16) {
				return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
			}
			if (bit == 5) {
				return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 13);
			}
			return bytesToHex(md.digest(input.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}
	
	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0) {
				t += 256;
			}
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}
	
	/**
	 * Title : 批量生成主键ID
	 * @author : 聂水根
	 * @param userId
	 * @param num
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String[] generateIDBatch(String userId, int num) {
		if (num == 0) {
			return null;
		}
		String[] returnValue = new String[num];
		if (userId == null || "".equals(userId)) {
			userId = "0";
		}
		int iUserId = Integer.parseInt(userId);
		Date d = new Date();
		StringBuffer sb = new StringBuffer();
		int year = 1900 + d.getYear() - 2000;
		sb.append(year).append(toValue(d.getMonth() + 1));
		sb.append(toValue(d.getDate())).append(toValue(d.getHours()));
		sb.append(toValue(d.getMinutes())).append(toValue(d.getSeconds()));
		sb.append(iUserId).append(Math.round(Math.random() * 100));
		for (int i = 0; i < num; i++) {
			returnValue[i] = sb.append(i).toString();
		}
		return returnValue;
	}

	private static String toValue(int value) {
		String strRet = String.valueOf(value);
		if (value < 10) {
			strRet = "0" + Integer.toString(value);
		}
		return strRet;
	}



	public static long getAtomicCounter() {
		if (counter.get() > 999999) {
			counter.set(1);
		}
		long time = System.currentTimeMillis();
		long returnValue = time + counter.incrementAndGet();
		return returnValue;
	}

	private static long incrementAndGet() {
		return counter.incrementAndGet();
	}

	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 99999;
	public static synchronized long next(){
		if (seq > ROTATION) {
			seq = 0;
		}
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
		return Long.parseLong(str);
	}

	//根据指定长度生成字母和数字的随机数
	//0~9的ASCII为48~57
	//A~Z的ASCII为65~90
	//a~z的ASCII为97~122
	public static String createRandomCharData(int length)
	{
		StringBuilder sb=new StringBuilder();
		Random rand=new Random();//随机用以下三个随机生成器
		Random randdata=new Random();
		int data=0;
		for(int i=0;i<length;i++)
		{
			int index=rand.nextInt(3);
			//目的是随机选择生成数字，大小写字母
			switch(index)
			{
				case 0:
					data=randdata.nextInt(10);//仅仅会生成0~9
					sb.append(data);
					break;
				case 1:
					data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
					sb.append((char)data);
					break;
				case 2:
					data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
					sb.append((char)data);
					break;
			}
		}
		String result=sb.toString();
		return result;
	}

	public static void main(String[] args) {
		try {
			System.out.println(IDManager.generateIDs());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
