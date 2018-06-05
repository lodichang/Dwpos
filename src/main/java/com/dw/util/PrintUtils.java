/*
package com.dw.util;

import com.dw.Main;
import com.dw.dto.PosOrderDishDto;
import com.dw.entity.PosSetting;
import com.dw.entity.PosTable;
import com.dw.extended.DwSplashScreen;
//import com.dw.print.PrintRxTx;
import com.dw.service.PosSettingService;
import com.dw.view.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.List;
import java.io.IOException;
import java.util.*;

*/
/**
 * Created by nshg on 2017/11/1.
 *//*


@Component
public class PrintUtils {

    private static Logger logger = LoggerFactory.getLogger(PrintUtils.class);
  public static synchronized void print(String printstr, String coup_code, String encode, String printPort, int boudRate) {
        System.out.println(printstr);
        PrintRxTx prt = PrintRxTx.getInstance();
        prt.setEncode_format(encode);
        if (coup_code != null) {
            if (!("").equals(coup_code)) {
                String[] parms = coup_code.split("\\|@\\|");
                prt.getMapmessage().put("coup_code", parms[0]);
                if (parms.length > 1) {
                    prt.getMapmessage().put("item_code", parms[1]);
                }
            }
        }

        try {
            prt.localPrint(printstr, printPort, 9600, "BIG5");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


public static void main(String[] args) throws IOException {
       print("학습영어로 듣는 뉴스|영어 톡톡", null, "ASCII", "COM3", 9600);
    }


 public static void main(String[] args) throws IllegalAccessException {

 // PrintRxTx print = PrintRxTx.getPrint();
        PosOrderDishDto posOrderDishDto = new PosOrderDishDto();
        posOrderDishDto.setBarCode("888");
        posOrderDishDto.setMessage("信息如下");

        java.util.List<String> orderlist = new ArrayList<String>();
        StringBuffer  printrecord = new StringBuffer();
        orderlist.add((printrecord.append("123").append("|R").append("奶茶").append("|R").append(14.00d).toString()));
        posOrderDishDto.setOrderList(orderlist);
        posOrderDishDto.setPrn_name("厨房");
        posOrderDishDto.setS_code("999");
        posOrderDishDto.setStation("前台");
        posOrderDishDto.setTable_Name("A52");
        posOrderDishDto.setZone("A区");
        String printmsg = print.printAction(posOrderDishDto);
        System.out.println(printmsg);
    }



    //POS後廚正常飛單（熱敏，前臺用
    public static void printOrderDishTableStyle(String id ,Map<String,Object> paramMap) throws IllegalAccessException, IOException {
        //PrintRxTx print = PrintRxTx.getPrint();
        PosSettingService posSettingService = new PosSettingService();
        String value = "~0,20,150,40\n#CENTER\n#BIG2\n$PRN_NAM\n" +
                "#LEFT@@" +
                "$ZONE|       |TABLE_NAM\n$MESSAGE\n" +
                "#HEIGHT_BIG1\n" +
                "$BARCODE|/|STATION|-|S_CODE|  |@POSDATE\n" +
                "#R|3|COMPART\n%ORDERLIST\n#R|3|COMPART\n" +
                "#BIG2\n$TABLE_NAM|   |ZONE\n#INITIALIZE\n" +
                "#GOHEAD5\n#CUTPAPER\n";
        String[] styles = value.split("\n");

        PosOrderDishDto posOrderDishDto = new PosOrderDishDto();
        posOrderDishDto.setPrn_name("收銀總單");




        posOrderDishDto.setBarCode("888");
        java.util.List<String> orderlist = new ArrayList<String>();
        for(int i = 0;i<10;i++){
            StringBuffer  printrecord = new StringBuffer();
            orderlist.add((printrecord.append(i + "").append("|R").append("奶茶").append("|R").append(14.00d).toString()));
        }

        posOrderDishDto.setOrderList(orderlist);
        posOrderDishDto.setS_code("小焯仔");
        posOrderDishDto.setStation("點菜員");
        posOrderDishDto.setTable_Name("A5");
        posOrderDishDto.setZone("A區");
        String printmsg = PrintRxTx.printRxTxInstance().printAction(posOrderDishDto);
        System.out.println(printmsg);




    }

}
*/
