package com.dw.print;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

public class TestMain {

	public static void main(String[] args) {
//		System.out.println(TestMain.toStringHex1("014"));
//	    StringBuffer code = new StringBuffer("123456789");//待转换成二维码的字符串
//				//计算成8进制
//				StringBuffer code1 =new StringBuffer("/0").append( Integer.toOctalString(code.length()+3));
//				System.out.println(code1.toString());
//				//拼接指令
//				String data1 =  "\035(k"+code1+"\000\061\120\060" 
//						+ "123456789\n";
//				System.out.println(data1);
//				//正常的指令
//				String data = "\035(k\014\000\061\120\060"
//						+ "123456789\n";
//				System.out.println(data);
				String select_model =  "\035(k\004\000\061\101\062\000";
				System.out.println(select_model);

	}

	public static String toStringHex1(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 8));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 设定数据
	 */
	private static HashMap<String, String> getDataMap() {
		HashMap<String, String> printmessage = new HashMap<String, String>();
		printmessage.put("OUTLET_NAME", "测试分店");// 分店名称
		printmessage.put("STATION", "测试终端");
		printmessage.put("STAFF", "测试员工");
		printmessage.put("BILL_NO", "test00000001");
		printmessage.put("SYSTEMDATE_TIEM",
				PrintFun.getSystemDateTime("yyyy-MM-dd HH:mm:ss"));
		printmessage.put("BILL_DATE", "2015-03-17");
		printmessage.put("CARD_CODE", "0008853886");
		printmessage.put("RECHARGE_AMT", "1000.00");
		printmessage.put("GIVE_AMT", "100.00");
		printmessage.put("TOTAL_AMT", "1100.00");
		return printmessage;
	}

	private static String[] style = { "~0,20,150,40", "#CENTER", "#BIG2",
			"$OUTLET_NAME", "发卡", "#LEFT", "#INITIALIZE",
			"$终端编号：|STATION|    员工：|STAFF", "$交易单号：|BILL_NO| 交易日期：|BILL_DATE",
			"$列印时间：|SYSTEMDATE_TIEM", "#R|3|COMPART", "$卡号：|CARD_CODE",
			"$充值金额：|RECHARGE_AMT", "$赠送金额：|GIVE_AMT", "$卡内总额：|TOTAL_AMT",
			"#R|3|COMPART", "#CENTER", "#BIG2", "发卡成功！", "#LEFT",
			"#INITIALIZE", "#GOHEAD5", "#CUTPAPER" };

	public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
		HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
		Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
		while (thePorts.hasMoreElements()) {
			CommPortIdentifier com = (CommPortIdentifier) thePorts
					.nextElement();
			switch (com.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				try {
					CommPort thePort = com.open("CommUtil", 50);
					thePort.close();
					h.add(com);
				} catch (PortInUseException e) {
					System.out.println("Port, " + com.getName()
							+ ", is in use.");
				} catch (Exception e) {
					System.err.println("Failed to open port " + com.getName());
					e.printStackTrace();
				}
			}
		}
		return h;
	}
}
