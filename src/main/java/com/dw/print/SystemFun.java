package com.dw.print;


public class SystemFun {

	private   int PRINTER_BAUDRATE;//打印机波特率
	private  String LOCALPORT;//打印机连接串口
	private   String ENCODE_FORMAT = "";//打印时使用的字符集格式,香港印机用BIG5,国内的用GBK


	public int getPRINTER_BAUDRATE() {
		return PRINTER_BAUDRATE;
	}

	public void setPRINTER_BAUDRATE(int PRINTER_BAUDRATE) {
		this.PRINTER_BAUDRATE = PRINTER_BAUDRATE;
	}

	public String getLOCALPORT() {
		return LOCALPORT;
	}

	public void setLOCALPORT(String LOCALPORT) {
		this.LOCALPORT = LOCALPORT;
	}

	public String getENCODE_FORMAT() {
		return ENCODE_FORMAT;
	}

	public void setENCODE_FORMAT(String ENCODE_FORMAT) {
		this.ENCODE_FORMAT = ENCODE_FORMAT;
	}
}
