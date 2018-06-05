
package com.dw.print;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Map;

import com.dw.Main;
import com.dw.enums.LanguageEnum;
import com.dw.qrfactory.FontImage;
import com.dw.dto.*;
import com.dw.util.AppUtils;
import com.dw.util.ReflectionUtil;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintRxTx {
    protected static final Logger logger = LoggerFactory.getLogger(PrintRxTx.class);

    private InputStream inputStream = null; // 读串口
    private SerialPort serialPort = null;
    private OutputStream output = null;
    private OutputStreamWriter writer = null;    //写串口

    private Integer boudrate = null; // 波特率
    private String prtPort = null; // 打印机对应的端口
    private String chartype = null;// 打印机使用的字符集
    private Map<String, Object> mapmessage = null; // 打印内容

    private static PrintRxTx printRxTx;

    private void setMapmessage(Map<String, Object> mapmessage) {
        this.mapmessage = mapmessage;
    }

    private static boolean printererror = true;// 标记印机是否异常

    public static PrintRxTx printRxTxInstance() {
        if (printRxTx == null) {
            printRxTx = new PrintRxTx();
        }
        return printRxTx;
    }

  /*  public static PrintRxTx getPrint() {
        return printRxTxInstance.print;
    }
*/

    private PrintRxTx() {
        boudrate = Main.systemFun.getPRINTER_BAUDRATE();
        prtPort = Main.systemFun.getLOCALPORT();
        chartype = Main.systemFun.getENCODE_FORMAT();
    }


    /**
     * 初始化印机
     */

    public String initPort() {
        StringBuffer error = new StringBuffer();
        try {
            //获得串口ID
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(prtPort);
            serialPort = (SerialPort) portId.open("Thunbergii", 2000);
            // 设置串口初始化参数，依次是波特率，数据位，停止位和 校验
            serialPort.setSerialPortParams(boudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            output = serialPort.getOutputStream(); // 读取串口输出
            inputStream = serialPort.getInputStream();
            //解决中文乱码主要是用OutputStreamWriter包在OutputStream的底层流，设置GBK/BIG5（通用繁体机器）编码读取，
            writer = new OutputStreamWriter(output, chartype);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
            //Log.getLogger().logger.error(e);
            // 提示没有找到该端口
            error.append(Main.languageMap.get("global.protnotfound")).append(prtPort).append("," + Main.languageMap.get("global.plscheckconfig"));
        } catch (PortInUseException e) {
            e.printStackTrace();
            //Log.getLogger().logger.error(e);
            //提示端口正在使用
            error.append(Main.languageMap.get("global.printprot")).append(prtPort).append(Main.languageMap.get("global.protused"));
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            //Log.getLogger().logger.error(e);
            //提示端口初始化失败
            error.append(Main.languageMap.get("global.printprot")).append(prtPort).append(Main.languageMap.get("global.initfailure"));
        } catch (IOException e) {
            e.printStackTrace();
            // Log.getLogger().logger.error(e);
            error.append(Main.languageMap.get("global.printprot")).append(prtPort).append(Main.languageMap.get("global.ioexception"));
        }
        if (error.toString().isEmpty()) {
            return null;
        } else {
            return error.toString();
        }
    }

     /**
     * 打印数据
     *
     * @return
     */

    public synchronized String printAction(PosOrderDto posOrderDto) {
        String errorMessage = "";
        try {
            String style = "";
            errorMessage = initPort();
            if(AppUtils.isBlank(errorMessage)) {
                errorMessage = checkPrinter();
                if(AppUtils.isBlank(errorMessage)){
                    if(posOrderDto instanceof PosOrderCashDto){
                        PosOrderCashDto posOrderCashDto = (PosOrderCashDto) posOrderDto;
                        setMapmessage(ReflectionUtil.getFieldsMap(posOrderCashDto));
                        style = Main.posSetting.get(ReflectionUtil.getClassName(posOrderCashDto));
                    }
                    else if (posOrderDto instanceof PosOrderDetailDto){
                        PosOrderDetailDto posOrderDetailDto = (PosOrderDetailDto) posOrderDto;
                        setMapmessage(ReflectionUtil.getFieldsMap(posOrderDetailDto));
                        style = Main.posSetting.get(ReflectionUtil.getClassName(posOrderDetailDto));
                    }
                    else if (posOrderDto instanceof PosOrderBillDto){
                        PosOrderBillDto posOrderBillDto = (PosOrderBillDto) posOrderDto;
                        //英文發票樣式
                        if(LanguageEnum.EN.getMapFelid() == posOrderBillDto.getPrintLanguage()){
                            style = Main.posSetting.get("PosEnglishOrderBillDto");
                        }
                        else{
                            style = Main.posSetting.get(ReflectionUtil.getClassName(posOrderBillDto));
                        }
                        setMapmessage(ReflectionUtil.getFieldsMap(posOrderBillDto));

                    }
                    else if (posOrderDto instanceof RePrintPayBillDto){
                        RePrintPayBillDto rePrintPayBillDto = (RePrintPayBillDto) posOrderDto;
                        setMapmessage(ReflectionUtil.getFieldsMap(rePrintPayBillDto));
                        style = Main.posSetting.get(ReflectionUtil.getClassName(rePrintPayBillDto));
                    }
                    else if (posOrderDto instanceof PosPayBillDto){
                        PosPayBillDto posPayBillDto = (PosPayBillDto) posOrderDto;
                        setMapmessage(ReflectionUtil.getFieldsMap(posPayBillDto));
                        style = Main.posSetting.get(ReflectionUtil.getClassName(posPayBillDto));
                    }
                    else if (posOrderDto instanceof PosTakeOutOrderDetailDto){
                        PosTakeOutOrderDetailDto posTakeOutOrderDetailDto = (PosTakeOutOrderDetailDto) posOrderDto;
                        setMapmessage(ReflectionUtil.getFieldsMap(posTakeOutOrderDetailDto));
                        style = Main.posSetting.get(ReflectionUtil.getClassName(posTakeOutOrderDetailDto));
                    }
                  if(AppUtils.isNotBlank(style)){
                      String[] styles = style.split("@@");
                      String printall = GetPrintOrReportStr.TranslateTableStyle(styles,1, mapmessage);
                      errorMessage = localPrint(printall);
                      if("success".equals(errorMessage)){
                        return "success";
                      }
                      else{
                          return  Main.languageMap.get("global.printfailure");
                      }
                  }
                  else{
                      errorMessage = Main.languageMap.get("table.noStyles");
                  }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        finally {
            if(serialPort != null){
                serialPort.removeEventListener();
                serialPort.close();
            }
        }
        return errorMessage;
    }


    /**
     * 打印数据
     *
     * @return
     */

    public synchronized String printAction(String style,Map<String,Object> mapmessage) {
        String errorMessage = "";
        try {
            errorMessage = initPort();
            if(AppUtils.isBlank(errorMessage)) {
                errorMessage = checkPrinter();
                if(AppUtils.isBlank(errorMessage)){
                    if(AppUtils.isNotBlank(style)){
                        String[] styles = style.split("@@");
                        String printall = GetPrintOrReportStr.TranslateTableStyle(styles,1, mapmessage);
                        errorMessage = localPrint(printall);
                        if("success".equals(errorMessage)){
                            return "success";
                        }
                        else{
                            return  Main.languageMap.get("global.printfailure");
                        }
                    }
                    else{
                        errorMessage = Main.languageMap.get("table.noStyles");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(serialPort != null){
                serialPort.removeEventListener();
                serialPort.close();
            }
        }
        return errorMessage;
    }


    public synchronized String printByCommand(String CommandId) {
        String errorMessage = "";
        try {
            String style = CommandId;
            errorMessage = initPort();
            if (errorMessage == null) {
                errorMessage = checkPrinter();
                if (errorMessage == null) {

                    if (AppUtils.isNotBlank(style)) {
                        String[] styles = style.split("@@");
                        String printall = GetPrintOrReportStr.TranslateTableStyle(styles, 1, mapmessage);
                        errorMessage = localPrint(printall);
                        if ("success".equals(errorMessage)) {
                            return "success";
                        } else {
                            return Main.languageMap.get("global.printfailure");
                        }
                    } else {
                        errorMessage = Main.languageMap.get("table.noStyles");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
        }
        return errorMessage;
    }

    /**
     * 校验印机
     *
     * @return
     */

    private String checkPrinter() {
        // 发送校验信息
        int count = 0;
        byte[] readBuffer = new byte[1];
        char[] check = {16,4,1};// 或者是{29.114,49}
        try {
            writer.write(check);//写串口
			this.output.write(getBytes(check));
            while (inputStream.available() <= 0 && count < 10) {
                count++;
                Thread.sleep(300);
            }
            if (count >= 10) {
                return Main.languageMap.get("global.printerror");
            }
            while (inputStream.available() > 0) {// 如果串口有信息返回
                inputStream.read(readBuffer);// 读取串口
            }
            if (readBuffer[0] == 22) {// 打印机正常
                return null;
            } else if (readBuffer[0] == 30) {// 打印机没纸
                return Main.languageMap.get("global.printpaperout");
            }
        } catch (IOException e) {// 错误编号5
            e.printStackTrace();
            logger.error(e.getMessage());

            return Main.languageMap.get("global.ioexception");
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Main.languageMap.get("global.printerror");
        }
        return Main.languageMap.get("global.printunknownerror");
    }


/**
     * 校验印机状态
     *
     * @return
     */

    private String PrinterCheck() {
        String errrMsg = null;
        if (printererror) {// 初始化印机端口
            errrMsg = initPort();
        }
//		if (errrMsg == null ) {// 如果端口没有错误，校验打印机状态
//			errrMsg = checkPrinter();
//		}
        if (errrMsg != null) {// 如果印机异常，关闭端口
            setPrinterError();
        } else {
            printererror = false;
        }
        return errrMsg;
    }


/**
     * 打印前台收银小票
     *
     * @return 打印结果
     * @throws IOException
     */

    private String localPrint(String printstr) {
        try {
            // 把打印数据写入串口
            String[] tablestyle = printstr.split("\n");
            for (int i = 0; i < tablestyle.length; i++) {
                System.out.println(tablestyle[i]);
                // 发送命令
                if (tablestyle[i].startsWith("#")) {
                    Delc(tablestyle[i]);
                }
                else if(tablestyle[i].trim().contains("#3IMAGE")){
                    Delc(tablestyle[i]);
                }
                else if (tablestyle[i].startsWith("*")) {// 组合命令
                    DelcNew(tablestyle[i]);
                }
                else if(tablestyle[i].startsWith("&*")){
                    this.output.write((tablestyle[i].substring(1) + "\n")
                            .getBytes(chartype));
                }
                // 发送数据
                else {
                    this.output.write((tablestyle[i] + "\n")
                            .getBytes(chartype));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            setPrinterError();
            // 打印失败
            return "fail";
        }
        return "success";
    }


/**
     * 设定打印机异常
     */

    private void setPrinterError() {
        try {
            printererror = true;
            if (serialPort != null) {
                serialPort.close();
            }
            if (output != null) {
                output.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();

        } finally {
            serialPort = null;
            inputStream = null;
            output =null;
        }
    }


/**
     * 处理命令
     *
     * @param cmd
     * @throws IOException
     */

    private void DelcNew(String cmd) throws IOException {
        cmd = cmd.substring(1);
        String[] str = cmd.split("\\|");
        for (int i = 0; i < str.length; i++) {
            if (str[i].startsWith("#")) {
                Delc(str[i]);
            } else {
                this.output.write(str[i].getBytes(chartype));
            }
        }
    }


/**
     * 转义指令集
     *
     * @param chars
     * @return
     */

    private byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName(chartype);
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }


/**
     * 处理打印命令
     *
     * @param
     * @throws IOException
     */

    private void Delc(String cmdline) throws IOException {
        // 通用指令集
        // writer.flush(); //刷新串口缓存
        if (cmdline.substring(1).equals("CUTPAPER")) { // 切纸
            this.output.write(getBytes(InstructionSet.getCutter()));
        } else if (cmdline.substring(1).equals("OPENBOX")) {// 开钱箱
            this.output.write(getBytes(InstructionSet.getOpenbox()));
        } else if (cmdline.substring(1).equals("GOHEAD5")) {// 进纸5行
            this.output.write(getBytes(InstructionSet.getGoHead5()));
        } else if (cmdline.substring(1).equals("INITIALIZE")) {// 初始化印机
            this.output.write(getBytes(InstructionSet.getInitialize()));
        }
        // 热敏印机指令集
        else if (cmdline.substring(1).equals("PRINT_LOGO")) { // 打印LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo()));
        } else if (cmdline.substring(1).equals("CENTER")) { // 居中打印
            this.output.write(getBytes(InstructionSet.getCenter()));
        } else if (cmdline.substring(1).equals("LEFT")) { // 靠左打印
            this.output.write(getBytes(InstructionSet.getLeft()));
        } else if (cmdline.substring(1).equals("BIG0")) { // // 放大0.5倍
            this.output.write(getBytes(InstructionSet.getBig0()));
        }else if (cmdline.substring(1).equals("BIG1")) { // 放大1倍
            this.output.write(getBytes(InstructionSet.getBig1()));
        } else if (cmdline.substring(1).equals("BIG2")) { // 放大2倍
            this.output.write(getBytes(InstructionSet.getBig2()));
        } else if (cmdline.substring(1).equals("BIG3")) { // 放大3倍
            this.output.write(getBytes(InstructionSet.getBig3()));
        } else if (cmdline.substring(1).equals("BIG4")) { // 放大4倍
            this.output.write(getBytes(InstructionSet.getBig4()));
        } else if (cmdline.substring(1).equals("BOLD")) { // 加粗
            this.output.write(getBytes(InstructionSet.getBold()));
        } else if (cmdline.substring(1).equals("BOLDOFF")) { // 取消加粗
            this.output.write(getBytes(InstructionSet.getBoldoff()));
        } else if (cmdline.substring(1).equals("UNDERLINE")) { // 下划线
            this.output.write(getBytes(InstructionSet.getUnderline()));
        } else if (cmdline.substring(1).equals("UNDERLINEOFF")) { // 取消下划线
            this.output.write(getBytes(InstructionSet.getUnderlineoff()));
        } else if (cmdline.substring(1).equals("HEIGHT_BIG1")) {// 纵向放大一倍
            this.output.write(getBytes(InstructionSet.getHeightBig1()));
        } else if (cmdline.substring(1).equals("UPSIDE_DOWN")) { // 反转180
            this.output.write(getBytes(InstructionSet.getUpsideDown()));
        }
        else if (cmdline.substring(1).equals("NEW_LINE")) { // 反转180
            this.output.write(getBytes(InstructionSet.getNewLine()));
        }
        else if(cmdline.substring(1).startsWith("3IMAGE")){ //打印图片
            String IMAGE =  cmdline.substring(("#3IMAGE-").length());
            if(IMAGE!=null && IMAGE.length()>0) {
               /* if(IMAGE.contains("@")){
                    String[] images = IMAGE.split("@");
                    IMAGE = "   " + images[1] + " " + images[0];
                }
                else{
                    IMAGE = "   "+ IMAGE;
                }*/
                BufferedImage bi= null;
                try {
                    bi = FontImage.getImage(IMAGE,200,20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //255是默认行高
                ArrayList<byte[]> data = PrinterUtils.decodeBitmapToDataList(bi, 96);
                for(byte[] d: data){
                    output.write(d);
                }
            }
        }
        else if(cmdline.contains("3IMAGE")){
            String IMAGE =  cmdline.trim().split("#3IMAGE-")[0] +"     "+ cmdline.split("#3IMAGE-")[1].trim();

            if(IMAGE!=null && IMAGE.length()>0) {
               /* if(IMAGE.contains("@")){
                    String[] images = IMAGE.split("@");
                    IMAGE = "   " + images[1] + " " + images[0];
                }
                else{
                    IMAGE = "   "+ IMAGE;
                }*/
                BufferedImage bi= null;
                try {
                    bi = FontImage.getImage(IMAGE,200,20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //255是默认行高
                ArrayList<byte[]> data = PrinterUtils.decodeBitmapToDataList(bi, 96);
                for(byte[] d: data){
                    output.write(d);
                }
            }
        }
        // 针式印机指令集
        else if (cmdline.substring(1).equals("RED")) { // 打印红色
            this.output.write(getBytes(InstructionSet.getFontColorRed()));
        } else if (cmdline.substring(1).equals("BLACK")) { // 打印黑色
            this.output.write(getBytes(InstructionSet.getFontColorBlack()));
        } else if (cmdline.substring(1).equals("ENLARGE")) {// 放大字体
            this.output.write(getBytes(InstructionSet.getFontSizeLarger1()));
            this.output.write(getBytes(InstructionSet.getFontSizeLarger2()));
        } else if (cmdline.substring(1).equals("NORMAL")) { // 缩小字体
            this.output.write(getBytes(InstructionSet.getFontSizeNormal1()));
            this.output.write(getBytes(InstructionSet.getFontSizeNormal2()));
        } else if (cmdline.substring(1).equals("PRINT_LOGO")) {// 打印第1个LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo()));
            this.output.write(" \n".getBytes());
        } else if (cmdline.substring(1).equals("PRINT_LOGO1")) {// 打印第2个LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo1()));
            this.output.write(" \n".getBytes());
        } else if (cmdline.substring(1).equals("PRINT_LOGO2")) {// 打印第3个LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo2()));
            this.output.write(" \n".getBytes());
        } else if (cmdline.substring(1).equals("PRINT_LOGO3")) {// 打印第4个LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo3()));
            this.output.write(" \n".getBytes());
        } else if (cmdline.substring(1).equals("PRINT_LOGO4")) {// 打印第5个LOGO
            this.output.write(getBytes(InstructionSet.getPrintLogo4()));
            this.output.write(" \n".getBytes());
        } else if (cmdline.substring(1).equals("BIG")) { // 放至最大
            this.output.write(getBytes(InstructionSet.getFontSizeLargest1()));
            this.output.write(getBytes(InstructionSet.getFontSizeLargest2()));
        } else if (cmdline.substring(1).equals("SMALL")) { // 放小
            this.output.write(getBytes(InstructionSet.getFontSizeNormal1()));
            this.output.write(getBytes(InstructionSet.getFontSizeNormal2()));
        } else if (cmdline.substring(1).startsWith("QRCODE")) {// 打印二维码
            String qrcode = cmdline.substring(("#QRCODE01").length());
            if (qrcode != null && qrcode.length() > 0) {
                // QRCode: Select the model
                // GS(k pL=4 pH=0 cn=49 fn=65 n1=49,50,51 n2 =0
                String select_model = "\035(k\004\000\061\101\062\000";
                output.write(select_model.getBytes());

                // 二维码大小
                // GS ( k pL pH cn=49 fn=67 n: Automatic processin;
                int size = 5;
                try {
                    size = Integer.parseInt(cmdline.substring(
                            ("#QRCODE").length(), ("#QRCODE").length() + 2));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    size = 5;
                }
                if (size < 5) {
                    size = 5;
                }
                String octal = Integer.toOctalString(size > 16 ? 16 : size);
                String size_module = "\035(k\003\000\061\103"
                        + toStringHex1(octal.length() > 1 ? octal : "0" + octal);
                System.out.println();
                output.write(size_module.getBytes());

                // 二维码容错率
                // GS ( k pL pH cn=49 fn=69 n=48~51,default 48
                String error_level = "\035(k\003\000\061\105\060";
                output.write(error_level.getBytes());

                // QRCode: Store the data in the symbol storage area存入缓存
                String data = "\035(k"
                        + toStringHex1(Integer
                        .toOctalString(qrcode.length() + 3))
                        + "\000\061\120\060" + qrcode + "\n";
                output.write(data.getBytes());

                // QRCode: Print 打印
                String data_print = "\035(k\003\000\061\121\060\n";
                output.write(data_print.getBytes());
            }

        } else if (cmdline.substring(1).equals("COUP_CODE")) {// 打印条码
            String str = mapmessage.get("coup_code") == null?null:mapmessage.get("coup_code").toString();
            if (str != null) {
                // 居中打印
                byte[] center = { 0x1b, 0x61, 0x01 };
                output.write(center);
                // writer.flush();//刷新串口缓存
                // 条码高度
                byte[] height = { 0x1D, 0x68, 0x64 };
                output.write(height);
                // writer.flush();//刷新串口缓存
                // 条码宽度
                if (str.length() <= 6) {
                    byte[] width = { 0x1D, 0x77, 0x02 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                } else {
                    byte[] width = { 0x1D, 0x77, 0x01 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                }
                byte[] head = { 0x1D, 0x6B, 0x04 };
                byte[] tail = { 0x00, 0x0D, 0x0A };
                byte[] data = str.getBytes(chartype);
                output.write(head);
                output.write(data);
                output.write(tail);

                output.write(("编号：" + str + "\n")
                        .getBytes(chartype));

            }
        } else if (cmdline.substring(1).equals("ITEM_CODE")) {// 打印条码
            String str = mapmessage.get("item_code")==null?null:mapmessage.get("item_code").toString();
            if (str != null) {
                // 居中打印
                byte[] center = { 0x1b, 0x61, 0x01 };
                output.write(center);
                // writer.flush();//刷新串口缓存
                // 条码高度
                byte[] height = { 0x1D, 0x68, 0x64 };
                output.write(height);
                // writer.flush();//刷新串口缓存
                // 条码宽度
                if (str.length() <= 6) {
                    byte[] width = { 0x1D, 0x77, 0x02 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                } else {
                    byte[] width = { 0x1D, 0x77, 0x01 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                }
                byte[] head = { 0x1D, 0x6B, 0x04 };
                byte[] tail = { 0x00, 0x0D, 0x0A };
                byte[] data = str.getBytes(chartype);
                output.write(head);
                output.write(data);
                output.write(tail);
                output.write(getBytes(InstructionSet.getBig1()));
                output.write(("编号：" + str + "\n")
                        .getBytes(chartype));
            }
        }
        else if (cmdline.substring(1).equals("TRAN_CODE")) {// 打印条码
            String str = mapmessage.get("tranCode")==null?null:mapmessage.get("tranCode").toString();
            if (str != null) {
                // 居中打印
                byte[] center = { 0x1b, 0x61, 0x01 };
                output.write(center);
                // writer.flush();//刷新串口缓存
                // 条码高度
                byte[] height = { 0x1D, 0x68, 0x64 };
                output.write(height);
                // writer.flush();//刷新串口缓存
                // 条码宽度
                if (str.length() <= 6) {
                    byte[] width = { 0x1D, 0x77, 0x02 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                } else {
                    byte[] width = { 0x1D, 0x77, 0x01 };
                    output.write(width);
                    // writer.flush();//刷新串口缓存
                }
                byte[] head = { 0x1D, 0x6B, 0x04 };
                byte[] tail = { 0x00, 0x0D, 0x0A };
                byte[] data = str.getBytes(chartype);
                output.write(head);
                output.write(data);
                output.write(tail);
                output.write(getBytes(InstructionSet.getBig1()));
            }
        }
    }

    private String toStringHex1(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 8));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        try {
            s = new String(baKeyword, "ASCII");
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error(e1.getMessage());
        }
        return s;
    }
}
