package com.dw.print;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.*;
import com.dw.entity.PosOrder;
import com.dw.entity.PosOrderHis;
import com.dw.entity.PosTran;
import com.dw.entity.PosTranHis;
import com.dw.enums.LanguageEnum;
import com.dw.enums.PosSettingEnum;
import com.dw.enums.PrintOrderBillLanuageEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.model.PayInfo;
import com.dw.service.*;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.util.SpringContextUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.util.EnglishEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lodi on 2018/5/22.
 */
@Component
public class PrintStyleUtils {



    //打印出菜纸
    public static void printTicket(PosTran posTran) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                PosTranService posTranService = (PosTranService) SpringContextUtil.getBean(PosTranService.class);
                PosOrderService posOrderService = (PosOrderService) SpringContextUtil.getBean(PosOrderService.class);
                //送单成功后重新打印账单
                Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                posTranWrapper.eq("REF_NUM", posTran.getRefNum());
                posTranWrapper.eq("SUB_REF", posTran.getSubRef());
                posTranWrapper.eq("TRAN_TYPE", posTran.getTranType());
                posTranWrapper.eq("OUTLET", posTran.getOutlet());
                PosTran posTr = posTranService.selectOne(posTranWrapper);
                if (AppUtils.isNotBlank(posTr)) {
                    if (AppUtils.isBlank(posTran.getPrtCount())) {
                        posTr.setPrtCount("1");
                    } else {
                        posTr.setPrtCount((Integer.parseInt(posTr.getPrtCount()) + 1) + "");
                    }
                    posTranService.updateById(posTr);
                    List<OrderListDto> orderList = posOrderService.getOrderList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), TranTypeEnum.N.getValue());
                    PosOrderDetailDto posOrderDetailDto = new PosOrderDetailDto();
                    posOrderDetailDto.setTel(Main.posOutletDto.getTel());
                    if (Integer.parseInt(posTr.getPrtCount()) > 1) {
                        posOrderDetailDto.setTableNo(posTran.getTableNum() + "  " + Main.languageMap.get("tran_update"));
                    } else {
                        posOrderDetailDto.setTableNo(posTran.getTableNum());
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date nowDate = new Date();
                    posOrderDetailDto.setDateTime(dateFormat.format(nowDate));
                    posOrderDetailDto.setSCode(Main.posStaff.getName1());
                    //账单详情
                    List<String> printOrderList = new LinkedList<>();
                    //小計
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    //總金額
                    BigDecimal totalBillAmt = BigDecimal.ZERO;
                    //折扣金额
                    BigDecimal discAmt = BigDecimal.ZERO;
                    //服务费
                    BigDecimal serviceAmt = BigDecimal.ZERO;
                    //条码
                    String tranCode = posTran.getOutlet() + posTran.getRefNum() + posTran.getSubRef() + posTran.getTranType();

                    if (AppUtils.isNotBlank(orderList)) {
                        for (OrderListDto posOrder : orderList) {
                            //如果改菜品设置了前台不列印就直接跳过
                            if (!"TRUE".equals(posOrder.getIPrint())) {
                                break;
                            }
                            String itemName = LanguageEnum.getLanguage(new String[]{posOrder.getDesc1(), posOrder.getDesc2(), posOrder.getDesc3(), posOrder.getDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            String attName = LanguageEnum.getLanguage(new String[]{posOrder.getAttDesc1(), posOrder.getAttDesc2(), posOrder.getAttDesc3(), posOrder.getAttDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            int itemCodespaceLength = (" " + posOrder.getItemCode() + " ").length();
                            String itemCodespace = "";
                            for (int i = 0; i < itemCodespaceLength; i++) {
                                itemCodespace = itemCodespace + " ";
                            }
                            if (!"0000".equals(posOrder.getItemCode())) {
                                //单品或者套餐
                                if ("0".equals(posOrder.getSetMeal()) || Integer.parseInt(posOrder.getSetMeal()) > 0) {
                                    //项目名称列的总字节数
                                    int length = 35;
                                    int itemCodeLength = (posOrder.getItemCode() + " ").length();
                                    int resultLength = length - itemCodeLength;
                                    if (itemName.getBytes().length > resultLength) {
                                        String firstString = "";
                                        int len = 0;
                                        //如果全部繁体字
                                        if (resultLength % 3 == 0) {
                                            len = resultLength / 3;
                                            firstString = itemName.substring(0, len);
                                        } else {
                                            len = resultLength / 3 + resultLength % 3;
                                            firstString = itemName.substring(0, len);
                                        }

                                        printOrderList.add(("R" + posOrder.getQty() + " X" + "| " + posOrder.getItemCode() + " " + firstString + "|R" + posOrder.getAmt()));
                                        String space = "";
                                        for (int i = 0; i < itemCodeLength; i++) {
                                            space = space + " ";
                                        }
                                        printOrderList.add((" |" + itemCodespace + itemName.substring(len) + "|R"));
                                    } else {
                                        printOrderList.add(("R" + posOrder.getQty() + " X" + "| " + posOrder.getItemCode() + " " + itemName + "|R" + posOrder.getAmt()));
                                    }
                                } else {
                                    //项目名称列的总字节数
                                    int length = Integer.parseInt(Main.posSetting.get("itemName_maxLength"));
                                    int itemCodeLength = (posOrder.getItemCode() + " ").length();
                                    int resultLength = length - itemCodeLength * 2 - 1;
                                    String orderString = "[" + itemName + "]";
                                    if (orderString.getBytes().length > resultLength) {
                                        //显示的字数
                                        int s = (resultLength - 1) / 3;
                                        String firstOrderString = "";
                                        //第一行
                                        if (BigDecimal.ZERO.compareTo(posOrder.getAmt()) < 0) {
                                            firstOrderString = " |" + itemCodespace + posOrder.getItemCode() + " " + orderString.substring(0, s) + "|R" + posOrder.getAmt().toString();
                                        } else {
                                            firstOrderString = " |" + itemCodespace + posOrder.getItemCode() + " " + orderString.substring(0, s) + "|R" + " ";
                                        }
                                        String secondOrderString = " |" + itemCodespace + itemCodespace.substring(1) + orderString.substring(s, orderString.length()) + "|R ";
                                        printOrderList.add(firstOrderString);
                                        printOrderList.add(secondOrderString);
                                    } else {
                                        StringBuffer space = new StringBuffer("");
                                        for (int i = 0; i < itemCodeLength; i++) {
                                            space.append(" ");
                                        }
                                        if (BigDecimal.ZERO.compareTo(posOrder.getAmt()) < 0) {
                                            printOrderList.add("|" + itemCodespace + posOrder.getItemCode() + " [" + itemName + "]|R" + posOrder.getAmt().toString());
                                        } else {
                                            printOrderList.add("|" + itemCodespace + posOrder.getItemCode() + " [" + itemName + "]|R" + " ");
                                        }
                                    }
                                }
                                if (AppUtils.isNotBlank(posOrder.getDesc4()) && "0".equals(posOrder.getSetMeal())) {
                                    printOrderList.add("R |" + "     #3IMAGE-" + posOrder.getDesc4() + "|R");//向打印链表中添加数据，韓文，打印圖片
                                }
                                if ("1".equals(posOrder.getKicMess())) {
                                    printOrderList.add("|" + itemCodespace + "(" + Main.languageMap.get("tran_callup") + ")|R");
                                }
                                printOrderList.add("|" + itemCodespace + "(" + posOrder.getStaffName() + " " + posOrder.getOrderTime() + ")|R");
                                if (AppUtils.isNotBlank(attName)) {
                                    String[] attList = attName.split("/");
                                    for (int i = 0; i < attList.length; i++) {
                                        printOrderList.add("|" + itemCodespace + "*" + attList[i] + "|R");
                                    }
                                }
                                totalPrice = totalPrice.add(posOrder.getAmt());
                                discAmt = discAmt.add(DecimalUtil.add(posOrder.getCatDisc(), posOrder.getItemDisc()));
                                totalBillAmt = totalBillAmt.add(posOrder.getAmt().subtract(DecimalUtil.add(posOrder.getCatDisc(), posOrder.getItemDisc())).add(new BigDecimal(posOrder.getServCost())));
                                serviceAmt = serviceAmt.add(new BigDecimal(posOrder.getServCost()));
                            } else {
                                continue;
                            }
                        }
                    }
                    posOrderDetailDto.setOrderCount(AppUtils.isNotBlank(orderList) ? orderList.size() + "" : "0");
                    posOrderDetailDto.setOrderList(printOrderList);
                    posOrderDetailDto.setOrderAmt(totalPrice.toString());
                    posOrderDetailDto.setBillAmt(totalBillAmt.toString());
                    posOrderDetailDto.setPerson(posTran.getPerson().toString());
                    posOrderDetailDto.setTranCode(tranCode);
                    posOrderDetailDto.setTableNum(posTran.getTableNum());
                    posOrderDetailDto.setServCost(serviceAmt.toString());
                    posOrderDetailDto.setDiscAmt("-" + discAmt.toString());
                    PrintRxTx.printRxTxInstance().printAction(posOrderDetailDto);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    //打印外卖出菜纸
    public static void printTakeOutTicket(PosTran posTran) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                PosTranService posTranService = (PosTranService) SpringContextUtil.getBean(PosTranService.class);
                PosOrderService posOrderService = (PosOrderService) SpringContextUtil.getBean(PosOrderService.class);
                //送单成功后重新打印账单
                Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                posTranWrapper.eq("REF_NUM", posTran.getRefNum());
                posTranWrapper.eq("SUB_REF", posTran.getSubRef());
                posTranWrapper.eq("TRAN_TYPE", posTran.getTranType());
                posTranWrapper.eq("OUTLET", posTran.getOutlet());
                PosTran posTr = posTranService.selectOne(posTranWrapper);
                if (AppUtils.isNotBlank(posTr)) {
                    //获取外卖信息
                    PosOrderAddressDto posOrderAddressDto = posTranService.getPosOrderAddress(posTr.getRefNum(), posTr.getSubRef());
                    if (AppUtils.isBlank(posTran.getPrtCount())) {
                        posTr.setPrtCount("1");
                    } else {
                        posTr.setPrtCount((Integer.parseInt(posTr.getPrtCount()) + 1) + "");
                    }
                    posTranService.updateById(posTr);
                    List<OrderListDto> orderList = posOrderService.getOrderList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), TranTypeEnum.N.getValue());
                    PosTakeOutOrderDetailDto posTakeOutOrderDetailDto = new PosTakeOutOrderDetailDto();
                    posTakeOutOrderDetailDto.setTel(Main.posOutletDto.getTel());
                    if (Integer.parseInt(posTr.getPrtCount()) > 1) {
                        posTakeOutOrderDetailDto.setTableNo(posTran.getTableNum() + "  " + Main.languageMap.get("tran_update"));
                    } else {
                        posTakeOutOrderDetailDto.setTableNo(posTran.getTableNum());
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date nowDate = new Date();
                    posTakeOutOrderDetailDto.setDateTime(dateFormat.format(nowDate));
                    posTakeOutOrderDetailDto.setSCode(Main.posStaff.getName1());
                    //账单详情
                    List<String> printOrderList = new LinkedList<>();
                    //外卖信息
                    List<String> takeOutOrderMsg = new LinkedList<>();
                    //小計
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    //總金額
                    BigDecimal totalBillAmt = BigDecimal.ZERO;
                    //折扣金额
                    BigDecimal discAmt = BigDecimal.ZERO;
                    //服务费
                    BigDecimal serviceAmt = BigDecimal.ZERO;
                    //条码
                    String tranCode = posTran.getOutlet() + posTran.getRefNum() + posTran.getSubRef() + posTran.getTranType();

                    if (AppUtils.isNotBlank(orderList)) {
                        for (OrderListDto posOrder : orderList) {
                            //如果改菜品设置了前台不列印就直接跳过
                            if (!"TRUE".equals(posOrder.getIPrint())) {
                                break;
                            }
                            String itemName = LanguageEnum.getLanguage(new String[]{posOrder.getDesc1(), posOrder.getDesc2(), posOrder.getDesc3(), posOrder.getDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            String attName = LanguageEnum.getLanguage(new String[]{posOrder.getAttDesc1(), posOrder.getAttDesc2(), posOrder.getAttDesc3(), posOrder.getAttDesc4()}, Main.posSetting.get(PosSettingEnum.languagedefault.getValue()));
                            int itemCodespaceLength = (" " + posOrder.getItemCode() + " ").length();
                            String itemCodespace = "";
                            for (int i = 0; i < itemCodespaceLength; i++) {
                                itemCodespace = itemCodespace + " ";
                            }
                            if (!"0000".equals(posOrder.getItemCode())) {
                                //单品或者套餐
                                if ("0".equals(posOrder.getSetMeal()) || Integer.parseInt(posOrder.getSetMeal()) > 0) {
                                    //项目名称列的总字节数
                                    int length = 35;
                                    int itemCodeLength = (posOrder.getItemCode() + " ").length();
                                    int resultLength = length - itemCodeLength;
                                    if (itemName.getBytes().length > resultLength) {
                                        String firstString = "";
                                        int len = 0;
                                        //如果全部繁体字
                                        if (resultLength % 3 == 0) {
                                            len = resultLength / 3;
                                            firstString = itemName.substring(0, len);
                                        } else {
                                            len = resultLength / 3 + resultLength % 3;
                                            firstString = itemName.substring(0, len);
                                        }

                                        printOrderList.add(("R" + posOrder.getQty() + " X" + "| " + posOrder.getItemCode() + " " + firstString + "|R" + posOrder.getAmt()));
                                        String space = "";
                                        for (int i = 0; i < itemCodeLength; i++) {
                                            space = space + " ";
                                        }
                                        printOrderList.add((" |" + itemCodespace + itemName.substring(len) + "|R"));
                                    } else {
                                        printOrderList.add(("R" + posOrder.getQty() + " X" + "| " + posOrder.getItemCode() + " " + itemName + "|R" + posOrder.getAmt()));
                                    }
                                } else {
                                    //项目名称列的总字节数
                                    int length = Integer.parseInt(Main.posSetting.get("itemName_maxLength"));
                                    int itemCodeLength = (posOrder.getItemCode() + " ").length();
                                    int resultLength = length - itemCodeLength * 2 - 1;
                                    String orderString = "[" + itemName + "]";
                                    if (orderString.getBytes().length > resultLength) {
                                        //显示的字数
                                        int s = (resultLength - 1) / 3;
                                        String firstOrderString = "";
                                        //第一行
                                        if (BigDecimal.ZERO.compareTo(posOrder.getAmt()) < 0) {
                                            firstOrderString = " |" + itemCodespace + posOrder.getItemCode() + " " + orderString.substring(0, s) + "|R" + posOrder.getAmt().toString();
                                        } else {
                                            firstOrderString = " |" + itemCodespace + posOrder.getItemCode() + " " + orderString.substring(0, s) + "|R" + " ";
                                        }
                                        String secondOrderString = " |" + itemCodespace + itemCodespace.substring(1) + orderString.substring(s, orderString.length()) + "|R ";
                                        printOrderList.add(firstOrderString);
                                        printOrderList.add(secondOrderString);
                                    } else {
                                        StringBuffer space = new StringBuffer("");
                                        for (int i = 0; i < itemCodeLength; i++) {
                                            space.append(" ");
                                        }
                                        if (BigDecimal.ZERO.compareTo(posOrder.getAmt()) < 0) {
                                            printOrderList.add("|" + itemCodespace + posOrder.getItemCode() + " [" + itemName + "]|R" + posOrder.getAmt().toString());
                                        } else {
                                            printOrderList.add("|" + itemCodespace + posOrder.getItemCode() + " [" + itemName + "]|R" + " ");
                                        }
                                    }
                                }
                                if (AppUtils.isNotBlank(posOrder.getDesc4()) && "0".equals(posOrder.getSetMeal())) {
                                    printOrderList.add("R |" + "     #3IMAGE-" + posOrder.getDesc4() + "|R");//向打印链表中添加数据，韓文，打印圖片
                                }
                                if ("1".equals(posOrder.getKicMess())) {
                                    printOrderList.add("|" + itemCodespace + "(" + Main.languageMap.get("tran_callup") + ")|R");
                                }
                                printOrderList.add("|" + itemCodespace + "(" + posOrder.getStaffName() + " " + posOrder.getOrderTime() + ")|R");
                                if (AppUtils.isNotBlank(attName)) {
                                    String[] attList = attName.split("/");
                                    for (int i = 0; i < attList.length; i++) {
                                        printOrderList.add("|" + itemCodespace + "*" + attList[i] + "|R");
                                    }
                                }
                                totalPrice = totalPrice.add(posOrder.getAmt());
                                discAmt = DecimalUtil.add(discAmt,DecimalUtil.add(posOrder.getCatDisc(), posOrder.getItemDisc()));
                                totalBillAmt = totalBillAmt.add(posOrder.getAmt().subtract(DecimalUtil.add(posOrder.getCatDisc(), posOrder.getItemDisc())).add(new BigDecimal(posOrder.getServCost())));
                                serviceAmt = serviceAmt.add(new BigDecimal(posOrder.getServCost()));
                            } else {
                                continue;
                            }
                        }
                    }
                    if (AppUtils.isNotBlank(posOrderAddressDto)) {
                        if (AppUtils.isNotBlank(posOrderAddressDto.getAddress())) {
                            takeOutOrderMsg.add(posOrderAddressDto.getAddress());
                        }
                        if (AppUtils.isNotBlank(posOrderAddressDto.getRemark())) {
                            takeOutOrderMsg.add(posOrderAddressDto.getRemark());
                        }
                        if (AppUtils.isNotBlank(posOrderAddressDto.getLinkName())) {
                            takeOutOrderMsg.add(posOrderAddressDto.getLinkName());
                        }
                        if (AppUtils.isNotBlank(posOrderAddressDto.getTelephone())) {
                            takeOutOrderMsg.add(posOrderAddressDto.getTelephone());
                        }
                    }
                    posTakeOutOrderDetailDto.setOrderCount(AppUtils.isNotBlank(orderList) ? orderList.size() + "" : "0");
                    posTakeOutOrderDetailDto.setOrderList(printOrderList);
                    posTakeOutOrderDetailDto.setOrderAmt(totalPrice.toString());
                    posTakeOutOrderDetailDto.setBillAmt(totalBillAmt.toString());
                    posTakeOutOrderDetailDto.setPerson(posTran.getPerson().toString());
                    posTakeOutOrderDetailDto.setTranCode(tranCode);
                    posTakeOutOrderDetailDto.setTableNum(posTran.getTableNum());
                    posTakeOutOrderDetailDto.setServCost(serviceAmt.toString());
                    posTakeOutOrderDetailDto.setDiscAmt("-" + discAmt.toString());
                    posTakeOutOrderDetailDto.setTakeOutOrderMsg(takeOutOrderMsg);
                    PrintRxTx.printRxTxInstance().printAction(posTakeOutOrderDetailDto);
                }
                return null;
            }
        };
        new Thread(task).start();
    }


    //打印付款清单
    public static void printPayBill(BillDto billDto) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                PosPayMentService posPayMentService = (PosPayMentService) SpringContextUtil.getBean(PosPayMentService.class);
                PosLogService posLogService = (PosLogService) SpringContextUtil.getBean(PosLogService.class);
                final BigDecimal[] payAmt = {BigDecimal.ZERO};
                final BigDecimal[] backAmt = {BigDecimal.ZERO};
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SimpleDateFormat refNumSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                //付款成功后打印付款清单
                //加載付款記錄
                List<PosPayDto> posPayDtoList = posPayMentService.getPosPayList(Main.posOutlet, billDto.getRefNum(), billDto.getSubRef(), billDto.getTranType());
                PosPayBillDto payBillDto = null;
                if ("TRUE".equals(billDto.getSettled())) {
                    payBillDto = new RePrintPayBillDto();
                } else {
                    payBillDto = new PosPayBillDto();
                }
                payBillDto.setTableNo("    " + billDto.getTableNum());
                payBillDto.setTel(Main.posOutletDto.getTel());
                payBillDto.setOutletName(Main.posOutletDto.getName1());
                payBillDto.setStaffName("  " + Main.posStaff.getName1());
                payBillDto.setPrintDate(simpleDateFormat.format(new Date()));
                List<String> payLists = new LinkedList<String>();
                if (AppUtils.isNotBlank(posPayDtoList)) {
                    posPayDtoList.forEach((PosPayDto posPaydto) -> {
                        payLists.add(posPaydto.getPayDisc() + "|R" + posPaydto.getNetAmount());
                        payAmt[0] = payAmt[0].add(posPaydto.getNetAmount());
                        backAmt[0] = backAmt[0].add(posPaydto.getOverAmt());
                    });
                }
                payBillDto.setPayList(payLists);
                payBillDto.setBillAmt(billDto.getBillAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "");
                payBillDto.setPayAmt(payAmt[0].toString());
                payBillDto.setBackAmt(backAmt[0].toString());

                payBillDto.setOrderAmt(String.valueOf(billDto.getOrderAmt()));
                payBillDto.setOrderDisc(String.valueOf(billDto.getOrderDisc()));
                payBillDto.setRounding(String.valueOf(billDto.getRounding()));
                payBillDto.setServAmt(String.valueOf(billDto.getServAmt()));
                //发票长度
                payBillDto.setRefNum(billDto.getInvoiceNumber());
                //折扣明細
                //加載付款記錄
                List<PosLogDto> posLogDtoList = posLogService.getPosLogList(Main.posOutlet, billDto.getRefNum(), billDto.getSubRef(), billDto.getTranType());
                List<String> discLists = new LinkedList<String>();
                if (AppUtils.isNotBlank(posLogDtoList)) {
                    posLogDtoList.forEach((PosLogDto posLogDto) -> {
                        discLists.add("#NEW_LINE");
                        discLists.add(posLogDto.getRemark3() + ":|R" + posLogDto.getAmt3());
                    });
                }
                payBillDto.setDiscList(discLists);

                PosPayBillDto finalPayBillDto = payBillDto;
                PrintRxTx.printRxTxInstance().printAction(finalPayBillDto);
                return null;
            }
        };
        Thread t = new Thread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //打印发票
    public static void printBillDetail(String refNum, String subRef, String tableNum, String outlet, Boolean isPay, LanguageEnum languageEnum) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                PosTranHisService posTranHisService = (PosTranHisService) SpringContextUtil.getBean(PosTranHisService.class);
                PosOrderHisService posOrderHisService = (PosOrderHisService) SpringContextUtil.getBean(PosOrderHisService.class);
                PosTranService posTranService = (PosTranService) SpringContextUtil.getBean(PosTranService.class);
                PosOrderService posOrderService = (PosOrderService) SpringContextUtil.getBean(PosOrderService.class);
                PosTran posTran = null;
                if (isPay) {
                    Wrapper<PosTranHis> posTranWrapper = new EntityWrapper<>();
                    posTranWrapper.eq("REF_NUM", refNum);
                    posTranWrapper.eq("SUB_REF", subRef);
                    posTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                    posTranWrapper.eq("OUTLET", outlet);
                    posTran = posTranHisService.selectOne(posTranWrapper);
                } else {
                    Wrapper<PosTran> posTranWrapper = new EntityWrapper<>();
                    posTranWrapper.eq("REF_NUM", refNum);
                    posTranWrapper.eq("SUB_REF", subRef);
                    posTranWrapper.eq("TRAN_TYPE", TranTypeEnum.N.getValue());
                    posTranWrapper.eq("OUTLET", outlet);
                    posTran = posTranService.selectOne(posTranWrapper);
                }

                if (posTran != null) {
                    //打印结账账单
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat invoiceFormat = new SimpleDateFormat("yyyyMMdd");
                    List<OrderListDto> orderList = null;
                    if (isPay) {
                        orderList = posOrderHisService.getOrderHisList(posTran.getRefNum(), posTran.getSubRef(), Main.posOutlet, TranTypeEnum.N.getValue());
                    } else {
                        orderList = posOrderService.getOrderList(posTran.getRefNum(), posTran.getSubRef(), Main.posOutlet, TranTypeEnum.N.getValue());
                    }
                    PosOrderBillDto posOrderBillDto = new PosOrderBillDto();
                    posOrderBillDto.setTel(Main.posOutletDto.getTel());
                    posOrderBillDto.setTableNo(tableNum);
                    posOrderBillDto.setInDate(dateFormat.format(posTran.getInDate()) + " " + timeFormat.format(posTran.getInTime()));
                    posOrderBillDto.setBillDate(dateFormat.format(posTran.getBillDate()) + " " + timeFormat.format(posTran.getBillTime()));
                    posOrderBillDto.setSCode(Main.posStaff.getName1());
                    posOrderBillDto.setOutletName(Main.posOutletDto.getName1());
                    //账单详情
                    List<String> printOrderList = new LinkedList<>();
                    //小計
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    //總金額
                    BigDecimal totalBillAmt = BigDecimal.ZERO;
                    //折扣金额
                    BigDecimal discAmt = BigDecimal.ZERO;
                    //服务费
                    BigDecimal serviceAmt = BigDecimal.ZERO;
                    //条码
                    String tranCode = posTran.getOutlet() + posTran.getRefNum() + posTran.getSubRef() + posTran.getTranType();

                    //发票编号
                    String invoiceNumber = posTran.getInvoiceNumber();

                    if (AppUtils.isNotBlank(orderList)) {
                        for (OrderListDto posOrder : orderList) {
                            //如果改菜品设置了前台不列印就直接跳过
                            if (!"TRUE".equals(posOrder.getIPrint())) {
                                break;
                            }
                            String itemName = LanguageEnum.getLanguage(new String[]{posOrder.getDesc1(), posOrder.getDesc2(), posOrder.getDesc3(), posOrder.getDesc4()}, languageEnum.getValue());
                            String attName = LanguageEnum.getLanguage(new String[]{posOrder.getAttDesc1(), posOrder.getAttDesc2(), posOrder.getAttDesc3(), posOrder.getAttDesc4()}, languageEnum.getValue());
                            String attPrice = posOrder.getSettingPrice();
                            List<String> havePriceAttsList = new LinkedList<>();
                            //单品时
                            if ("0".equals(posOrder.getSetMeal())) {
                                if (AppUtils.isNotBlank(attName) && AppUtils.isNotBlank(attPrice)) {
                                    String[] attNames = attName.split("/");
                                    String[] attPrices = attPrice.split("@");
                                    for (int i = 0; i < attNames.length; i++) {
                                        havePriceAttsList.add("&*" + attNames[i] + "|R" + "|R");
                                    }
                                }
                                if(languageEnum.getMapFelid() == LanguageEnum.EN.getMapFelid()){
                                    List<String> itemNames = tranFormItemName(itemName,languageEnum);
                                    if(AppUtils.isNotBlank(itemNames)){
                                        for(int i = 0;i<itemNames.size();i++){
                                            if(i == 0){
                                                printOrderList.add(itemNames.get(i) + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                            }
                                            else{
                                                printOrderList.add(itemNames.get(i) + "|R" + " |R ");
                                            }
                                        }
                                    }
                                    //如果套餐没有英文默认中文
                                    else{
                                        printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                    }
                                }
                                else{
                                    printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                }

                                havePriceAttsList.forEach((String priceAtt) -> {
                                    printOrderList.add(priceAtt);
                                });
                            }
                            //主套餐
                            else if (Integer.parseInt(posOrder.getSetMeal()) > 0) {
                                if(languageEnum.getMapFelid() == LanguageEnum.EN.getMapFelid()){
                                    List<String> itemNames = tranFormItemName(itemName,languageEnum);
                                    if(AppUtils.isNotBlank(itemNames)){
                                        for(int i = 0;i<itemNames.size();i++){
                                            if(i == 0){
                                                printOrderList.add(itemNames.get(i) + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                            }
                                            else{
                                                printOrderList.add(itemNames.get(i) + "|R" + " |R ");
                                            }
                                        }
                                    }
                                    //如果套餐没有英文默认中文
                                    else{
                                        printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                    }
                                }
                                else{
                                    printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                }
                            }
                            //套餐详情
                            else if ("-1".equals(posOrder.getSetMeal())) {
                                if (AppUtils.isNotBlank(attName) && AppUtils.isNotBlank(attPrice)) {
                                    String[] attNames = attName.split("/");
                                    String[] attPrices = attPrice.split("@");
                                    for (int i = 0; i < attNames.length; i++) {
                                        havePriceAttsList.add("&*" + attNames[i] + "|R" + "|R");
                                    }
                                }
                                if(languageEnum.getMapFelid() == LanguageEnum.EN.getMapFelid()){
                                    List<String> itemNames = tranFormItemName(itemName,languageEnum);
                                    if(AppUtils.isNotBlank(itemNames)){
                                        for(int i = 0;i<itemNames.size();i++){
                                            if(i == 0){
                                                printOrderList.add(itemNames.get(i) + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                            }
                                            else{
                                                printOrderList.add(itemNames.get(i) + "|R" + " |R ");
                                            }
                                        }
                                    }
                                    //如果套餐没有英文默认中文
                                    else{
                                        printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                    }
                                }
                                else{
                                    printOrderList.add(itemName + "|R" + posOrder.getQty().toString() + "|R" + posOrder.getAmt());
                                }
                                havePriceAttsList.forEach((String priceAtt) -> {
                                    printOrderList.add(priceAtt);
                                });
                            }
                            totalPrice = totalPrice.add(posOrder.getAmt());
                            totalBillAmt = totalBillAmt.add(posOrder.getAmt().subtract(posOrder.getOrderDisc()).add(new BigDecimal(posOrder.getServCost())));
                            discAmt = DecimalUtil.add(discAmt,posOrder.getOrderDisc());
                            serviceAmt = serviceAmt.add(new BigDecimal(posOrder.getServCost()));
                        }
                    }
                    posOrderBillDto.setOrderCount(AppUtils.isNotBlank(orderList) ? orderList.size() + "" : "0");
                    posOrderBillDto.setStaffName(Main.posStaff.getName1());
                    posOrderBillDto.setOrderList(printOrderList);
                    posOrderBillDto.setOrderAmt(totalPrice.toString());
                    posOrderBillDto.setBillAmt(posTran.getBillAmt() + "");
                    posOrderBillDto.setPerson(posTran.getPerson().toString());
                    posOrderBillDto.setDiscAmt("-" + discAmt.toString());
                    posOrderBillDto.setOverAmt(posTran.getRounding().toString());
                    posOrderBillDto.setServCost(serviceAmt.toString());
                    posOrderBillDto.setInvoiceNumber(invoiceNumber);
                    //取消菜式
                    List<PosOrderCancleDto> posOrderCancleDtos = posOrderHisService.getOrderHisCancleList(posTran.getRefNum(), posTran.getSubRef(), posTran.getOutlet(), posTran.getTableNum(), TranTypeEnum.N.getValue());
                    List<String> posOrderCancle = new LinkedList<String>();
                    if (AppUtils.isNotBlank(posOrderCancleDtos)) {
                        if (LanguageEnum.EN.getMapFelid() == languageEnum.getMapFelid()) {
                            posOrderCancle.add(Main.languageMap.get("food_cancle_english") + "| | ");
                        } else {
                            posOrderCancle.add(Main.languageMap.get("food_cancle") + "| | ");
                        }
                        posOrderCancleDtos.forEach((PosOrderCancleDto posOrderCancleDto) -> {
                            posOrderCancle.add(posOrderCancleDto.getItemName() + "|R" + Math.abs(posOrderCancleDto.getQty()) + "|R" + Math.abs(posOrderCancleDto.getAmt().doubleValue()));
                            posOrderCancle.add("(" + posOrderCancleDto.getReasonContent() + ")" + "| | ");
                        });
                        posOrderBillDto.setPosOrderCancle(posOrderCancle);
                    }
                    PosTran finalPosTran = posTran;
                    posOrderBillDto.setEveryOneAmt(DecimalUtil.divideUP(finalPosTran.getBillAmt(), new BigDecimal(finalPosTran.getPerson().toString())).toString());
                    posOrderBillDto.setPrintLanguage(languageEnum.getMapFelid());
                    PrintRxTx.printRxTxInstance().printAction(posOrderBillDto);
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.start();
       /* try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }


    public static  List<String> tranFormItemName(String itemName, LanguageEnum languageEnum) {
        List<String> itemNames = new LinkedList<>();
        if (LanguageEnum.EN.getMapFelid() == languageEnum.getMapFelid()) {
            if(itemName.substring(0,1).equals("R") || itemName.substring(0,1).equals("L")){
                itemName = " " + itemName;
            }
            //获取食品名称列一行最大长度
           int itemColumLength = Integer.parseInt(Main.posSetting.get("max_bill_itemName_length"));
           if(itemName.length()>itemColumLength){
               int count = itemName.length()/itemColumLength + (itemName.length()%itemColumLength > 0?1:0);
               for(int i = 1;i<=count;i++){
                   if(itemName.length()>=i*itemColumLength){
                       itemNames.add(itemName.substring((i-1)*itemColumLength,i*itemColumLength));
                   }
                   else if(itemName.length() > (i-1)*itemColumLength && itemName.length() < i*itemColumLength){
                       itemNames.add(itemName.substring((i-1)*itemColumLength,itemName.length()));
                   }
                   else{
                       itemNames.add(itemName);
                   }
               }
           }
           else{
               itemNames.add(itemName);
           }
        }
        return itemNames;
    }


}
