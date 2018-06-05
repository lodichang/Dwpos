package com.dw.dto;

import com.dw.enums.HoldOnEnum;
import com.dw.util.AppUtils;
import com.dw.util.IDManager;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.*;

/**
 * Created by li.yongliang on 2018/4/15.
 */

@ToString
@NoArgsConstructor
public class TableViewDto implements Serializable {

    private String cartId;
    //已送單order表id
    private String id;
    private boolean isSendOrder = true; // 是否送單到POS_ORDER表，默認true，數據庫加載出來為false，拆出來的為true

    //份數
    private SimpleStringProperty qty;
    //商品編號
    private SimpleStringProperty itemCode;
    //商品名稱
    private SimpleStringProperty itemName;
    //口味名稱拼成的字符串，用“/”分割
    private SimpleStringProperty itemAtt;
    //廚房訊息拼成的字符串
    private SimpleStringProperty itemKicMsg;
    //商品單價
    private SimpleDoubleProperty price;
    //總價
    private SimpleDoubleProperty amt;
    //口味編號拼成的字符串“@”分割，共同口味編號，子編號，動作編號用:分割
    private SimpleStringProperty itemAttCode;
    //廚房訊息編號拼成的字符串
    private SimpleStringProperty kicMsgCode;
    //口味总价
    private SimpleDoubleProperty attAmt;
    //口味價錢拼接字符串“@”分割
    private SimpleStringProperty attPrices;
    //商品原價
    private SimpleDoubleProperty orgPrice;
    //口味扣減數量拼接字符串，@分割
    private SimpleStringProperty attSubtractQty;
    //是否已經打印小票，新加的默認false，從表裡查出來的默認true。
    private SimpleBooleanProperty isPrinter;
    private boolean flag = false;

    //是否收取服務費，TRUE是，FALSE否
    private SimpleStringProperty service;
    private SimpleLongProperty itemIdx; //商品流水号
    //菜品指定的印機編號
    private String itemPrn;
    //是否叫起
    private IntegerProperty holdOn = new SimpleIntegerProperty(HoldOnEnum.CANCELCALL.getValue());


    //// 本次套餐送单食品itemIdx
    private String setmealItemIdx;

    //所属主套餐编号
    private SimpleStringProperty mealCode;

    //套餐组别编号
    private SimpleStringProperty sgroup;

    //可选数量
    private SimpleStringProperty scount;

    //必选数量
    private SimpleStringProperty gpCount;

    private SimpleStringProperty combId;
    //自由價
    private SimpleStringProperty iopen = new SimpleStringProperty();

    private SimpleStringProperty orderTime;//下单时间

    private SimpleStringProperty staffName;//下单员名称

    private SimpleStringProperty urgeCount = new SimpleStringProperty();//追單次數

    private String tDate;//下单日期

    private PosOrderDiscDto posOrderDiscDto = new PosOrderDiscDto();
    //最少必選口味
    private SimpleIntegerProperty attMin = new SimpleIntegerProperty(0);
    //最多必選口味
    private SimpleIntegerProperty attMax = new SimpleIntegerProperty(0);
    private SimpleStringProperty catt = new SimpleStringProperty();//共同口味，可多选，多个共同口味直接字符串拼接

    private SimpleBooleanProperty isHoldOn ;//是否默认叫起

    public TableViewDto(String qty, String itemCode, String itemName, String itemAtt, String itemKicMsg, Double price, Double amt, Boolean isPrinter, String combId, String cartId) {
        this.qty = new SimpleStringProperty(qty);
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemAtt = new SimpleStringProperty(itemAtt);
        this.itemKicMsg = new SimpleStringProperty(itemKicMsg);
        this.price = new SimpleDoubleProperty(price);
        this.amt = new SimpleDoubleProperty(amt);
        this.isPrinter = new SimpleBooleanProperty(isPrinter);
        this.isSendOrder = true;
        this.combId = new SimpleStringProperty(combId);
        this.cartId = cartId;
    }

    public TableViewDto(String qty, String itemCode, String itemName, String itemAtt, String itemKicMsg, Double price, Double amt, Boolean isPrinter, Double orgPrice, String mealCode, String sgroup, String scount, String gpCount, String service, String combId, String cartId) {
        this.qty = new SimpleStringProperty(qty);
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemAtt = new SimpleStringProperty(itemAtt);
        this.itemKicMsg = new SimpleStringProperty(itemKicMsg);
        this.price = new SimpleDoubleProperty(price);
        this.orgPrice = new SimpleDoubleProperty(orgPrice);
        this.amt = new SimpleDoubleProperty(amt);
        this.isPrinter = new SimpleBooleanProperty(isPrinter);
        this.mealCode = new SimpleStringProperty(mealCode);
        this.sgroup = new SimpleStringProperty(sgroup);
        this.scount = new SimpleStringProperty(scount);
        this.gpCount = new SimpleStringProperty(gpCount);
        this.service = new SimpleStringProperty(service);
        this.isSendOrder = true;
        this.combId = new SimpleStringProperty(combId);
        this.cartId = cartId;
    }


    public TableViewDto(String qty, String itemCode, String itemName, String itemAtt, String itemKicMsg, Double price, Double amt, String cartId) {
        this.qty = new SimpleStringProperty(qty);
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemAtt = new SimpleStringProperty(itemAtt);
        this.itemKicMsg = new SimpleStringProperty(itemKicMsg);
        this.price = new SimpleDoubleProperty(price);
        this.amt = new SimpleDoubleProperty(amt);
        this.isSendOrder = true;
        this.cartId = cartId;

    }


    //    點菜的時候用到的構造方法
    public TableViewDto(String qty, String itemCode, String itemName, String itemAtt, String itemKicMsg, Double price, Double amt,
                        String itemAttCode, String kicMsgCode, Double attAmt, String attPrices, Double orgPrice, String attSubtractQty, Boolean isPrinter, long itemIdx,
                        String service, String mealCode, String sgroup, String scount, String gpCount, String itemPrn, String combId, String orderTime, String staffName,
                        String catt, int attMax, int attMin, String cartId,Boolean isHoldOn,String tDate) {
        this.qty = new SimpleStringProperty(qty);
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemAtt = new SimpleStringProperty(itemAtt);
        this.itemKicMsg = new SimpleStringProperty(itemKicMsg);
        this.price = new SimpleDoubleProperty(price);
        this.amt = new SimpleDoubleProperty(amt);
        this.itemAttCode = new SimpleStringProperty(itemAttCode);
        this.kicMsgCode = new SimpleStringProperty(kicMsgCode);
        this.attAmt = new SimpleDoubleProperty(attAmt);
        this.attPrices = new SimpleStringProperty(attPrices);
        this.orgPrice = new SimpleDoubleProperty(orgPrice);
        this.attSubtractQty = new SimpleStringProperty(attSubtractQty);
        this.isPrinter = new SimpleBooleanProperty(isPrinter);
        //用於保存組合前的價格,默認等於AMT
        this.itemIdx = new SimpleLongProperty(itemIdx);
        this.service = new SimpleStringProperty(service);

        this.mealCode = new SimpleStringProperty(mealCode);
        this.sgroup = new SimpleStringProperty(sgroup);
        this.scount = new SimpleStringProperty(scount);
        this.gpCount = new SimpleStringProperty(gpCount);

        this.isSendOrder = true;
        this.itemPrn = itemPrn;
        this.combId = new SimpleStringProperty(combId);

        this.orderTime = new SimpleStringProperty(orderTime);
        this.staffName = new SimpleStringProperty(staffName);

        this.catt = new SimpleStringProperty(catt);
        this.attMax = new SimpleIntegerProperty(attMax);
        this.attMin = new SimpleIntegerProperty(attMin);
        this.cartId=cartId;
        this.isHoldOn = new SimpleBooleanProperty(isHoldOn);
        this.tDate = tDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSendOrder() {
        return isSendOrder;
    }

    public void setSendOrder(boolean sendOrder) {
        isSendOrder = sendOrder;
    }

    public String getQty() {
        return qty.get();
    }

    public SimpleStringProperty qtyProperty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty.set(qty);
    }

    public String getItemCode() {
        return itemCode.get();
    }

    public SimpleStringProperty itemCodeProperty() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode.set(itemCode);
    }

    public String getItemName() {
        return itemName.get();
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getItemAtt() {
        return itemAtt.get();
    }

    public SimpleStringProperty itemAttProperty() {
        return itemAtt;
    }

    public void setItemAtt(String itemAtt) {
        this.itemAtt.set(itemAtt);
    }

    public String getItemKicMsg() {
        return itemKicMsg.get();
    }

    public SimpleStringProperty itemKicMsgProperty() {
        return itemKicMsg;
    }

    public void setItemKicMsg(String itemKicMsg) {
        this.itemKicMsg.set(itemKicMsg);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getAmt() {
        return amt.get();
    }

    public SimpleDoubleProperty amtProperty() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt.set(amt);
    }

    public String getItemAttCode() {
        return itemAttCode.get();
    }

    public SimpleStringProperty itemAttCodeProperty() {
        return itemAttCode;
    }

    public void setItemAttCode(String itemAttCode) {
        this.itemAttCode.set(itemAttCode);
    }

    public String getKicMsgCode() {
        return kicMsgCode.get();
    }

    public SimpleStringProperty kicMsgCodeProperty() {
        return kicMsgCode;
    }

    public void setKicMsgCode(String kicMsgCode) {
        this.kicMsgCode.set(kicMsgCode);
    }

    public double getAttAmt() {
        return attAmt.get();
    }

    public SimpleDoubleProperty attAmtProperty() {
        return attAmt;
    }

    public void setAttAmt(double attAmt) {
        this.attAmt.set(attAmt);
    }

    public String getAttPrices() {
        return attPrices.get();
    }

    public SimpleStringProperty attPricesProperty() {
        return attPrices;
    }

    public void setAttPrices(String attPrices) {
        this.attPrices.set(attPrices);
    }

    public double getOrgPrice() {
        return orgPrice.get();
    }

    public SimpleDoubleProperty orgPriceProperty() {
        return orgPrice;
    }

    public void setOrgPrice(double orgPrice) {
        this.orgPrice.set(orgPrice);
    }

    public String getAttSubtractQty() {
        return attSubtractQty.get();
    }

    public SimpleStringProperty attSubtractQtyProperty() {
        return attSubtractQty;
    }

    public void setAttSubtractQty(String attSubtractQty) {
        this.attSubtractQty.set(attSubtractQty);
    }

    public boolean isPrinter() {
        return isPrinter.get();
    }

    public SimpleBooleanProperty isPrinterProperty() {
        return isPrinter;
    }

    public void setIsPrinter(boolean isPrinter) {
        this.isPrinter.set(isPrinter);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getService() {
        return service.get();
    }

    public SimpleStringProperty serviceProperty() {
        return service;
    }

    public void setService(String service) {
        this.service.set(service);
    }

    public long getItemIdx() {
        return itemIdx.get();
    }

    public SimpleLongProperty itemIdxProperty() {
        return itemIdx;
    }

    public void setItemIdx(long itemIdx) {
        this.itemIdx.set(itemIdx);
    }

    public String getItemPrn() {
        return itemPrn;
    }

    public void setItemPrn(String itemPrn) {
        this.itemPrn = itemPrn;
    }

    public String getMealCode() {
        return mealCode.get();
    }

    public SimpleStringProperty mealCodeProperty() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode.set(mealCode);
    }

    public String getSgroup() {
        return sgroup.get();
    }

    public SimpleStringProperty sgroupProperty() {
        return sgroup;
    }

    public void setSgroup(String sgroup) {
        this.sgroup.set(sgroup);
    }

    public String getScount() {
        return scount.get();
    }

    public SimpleStringProperty scountProperty() {
        return scount;
    }

    public void setScount(String scount) {
        this.scount.set(scount);
    }

    public String getGpCount() {
        return gpCount.get();
    }

    public SimpleStringProperty gpCountProperty() {
        return gpCount;
    }

    public void setGpCount(String gpCount) {
        this.gpCount.set(gpCount);
    }

    public String getSetmealItemIdx() {
        return setmealItemIdx;
    }

    public void setSetmealItemIdx(String setmealItemIdx) {
        this.setmealItemIdx = setmealItemIdx;
    }


    public String getCombId() {
        return combId.get();
    }

    public SimpleStringProperty combIdProperty() {
        return combId;
    }

    public void setCombId(String combId) {
        this.combId.set(combId);
    }

    public PosOrderDiscDto getPosOrderDiscDto() {
        return posOrderDiscDto;
    }

    public void setPosOrderDiscDto(PosOrderDiscDto posOrderDiscDto) {
        this.posOrderDiscDto = posOrderDiscDto;
    }

    public boolean isIsPrinter() {
        return isPrinter.get();
    }

    public String getOrderTime() {
        return orderTime.get();
    }

    public SimpleStringProperty orderTimeProperty() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime.set(orderTime);
    }

    public String getStaffName() {
        return staffName.get();
    }

    public SimpleStringProperty staffNameProperty() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName.set(staffName);
    }


    public int getHoldOn() {
        return holdOn.get();
    }

    public IntegerProperty holdOnProperty() {
        return holdOn;
    }

    public void setHoldOn(int holdOn) {
        this.holdOn.set(holdOn);
    }

    public String getUrgeCount() {
        return urgeCount.get();
    }

    public SimpleStringProperty urgeCountProperty() {
        return urgeCount;
    }

    public void setUrgeCount(String urgeCount) {
        this.urgeCount.set(urgeCount);
    }

    public int getAttMin() {
        return attMin.get();
    }

    public SimpleIntegerProperty attMinProperty() {
        return attMin;
    }

    public void setAttMin(int attMin) {
        this.attMin.set(attMin);
    }

    public int getAttMax() {
        return attMax.get();
    }

    public SimpleIntegerProperty attMaxProperty() {
        return attMax;
    }

    public void setAttMax(int attMax) {
        this.attMax.set(attMax);
    }

    public String getCatt() {
        return catt.get();
    }

    public SimpleStringProperty cattProperty() {
        return catt;
    }

    public void setCatt(String catt) {
        this.catt.set(catt);
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public boolean isIsHoldOn() {
        return isHoldOn.get();
    }

    public SimpleBooleanProperty isHoldOnProperty() {
        return isHoldOn;
    }

    public void setIsHoldOn(boolean isHoldOn) {
        this.isHoldOn.set(isHoldOn);
    }

    public String getIopen() {
        return iopen.get();
    }

    public SimpleStringProperty iopenProperty() {
        return iopen;
    }

    public void setIopen(String iopen) {
        this.iopen.set(iopen);
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
}
