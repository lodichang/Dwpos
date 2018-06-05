package com.dw.extended;

import com.alibaba.fastjson.JSONObject;
import com.dw.Main;
import com.dw.controller.MemberController;
import com.dw.dto.CouponGetDto;
import com.dw.dto.PosBindUseCoupon;
import com.dw.dto.PosOrderDiscDto;
import com.dw.entity.PosLog;
import com.dw.enums.*;
import com.dw.model.CouponParm;
import com.dw.util.AppUtils;
import com.dw.util.HttpClientUtil;
import com.dw.util.ShowViewUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigDecimal;
import java.util.*;

import static com.dw.util.DecimalUtil.bigDecimalByPloy;
import static com.dw.util.DecimalUtil.divide;

public class DwCouponPane extends Pane     {

    public DwCouponPane(MemberController memberController,Double width, Double height, String cardno,Stage memberStage, CouponGetDto couponGetDto, CouponParm couponParm,List<PosOrderDiscDto> orderList) {
        Button couponActionBut = new DwButton(FontSizeEnum.font16);
        couponActionBut.setText(Main.languageMap.get("member.clickUseCoupon"));
        couponActionBut.setPrefWidth(width);
        couponActionBut.setPrefHeight(height);
        couponActionBut.setStyle("-fx-border-width: 0;-fx-background-color: null;");
        couponActionBut.setAlignment(Pos.CENTER);
        couponActionBut.setOnMouseClicked(event -> {
            if(AppUtils.isNotBlank(orderList)){
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("member.useCouponTip1"), resultMap, memberStage);

                if(result.equals(ResultEnum.YES.getValue())){
                    //調用券接口
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("outline", Main.outline));
                    params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
                    params.add(new BasicNameValuePair("tableNo", couponParm.getTableNo()));
                    params.add(new BasicNameValuePair("scanCode", couponGetDto.getCouponNum()));
                    params.add(new BasicNameValuePair("refNum", couponParm.getRefNum()));
                    params.add(new BasicNameValuePair("subRef", couponParm.getSubRef()));
                    params.add(new BasicNameValuePair("tIndex", couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                    params.add(new BasicNameValuePair("channel", "HTML"));
                    params.add(new BasicNameValuePair("isSendMsg", "false"));
                    params.add(new BasicNameValuePair("isVoucher", 0 + ""));
                    String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("bindOrUseCoupon");

                    try {
                        String responseStr = HttpClientUtil.post(url, params);
                        JSONObject resultJson = JSONObject.parseObject(responseStr);
                        if (AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")) {
                            String useGiftCouponListStr = resultJson.get("data").toString();
                            JSONArray jsonArray = JSONArray.fromObject(useGiftCouponListStr);
                            //獲取優惠券基本信息
                            List<PosBindUseCoupon> useGiftCouponList = (List<PosBindUseCoupon>) JSONArray.toCollection(jsonArray, PosBindUseCoupon.class);

                            if(AppUtils.isNotBlank(useGiftCouponList)){
                                PosBindUseCoupon useCouponEntity  = useGiftCouponList.get(0);
                                //計算IQ券折扣金額
                                Map<String,Object> calculateResult = calculateIQCouponDiscAmount(memberController,useCouponEntity,orderList);

                                if(AppUtils.isNotBlank(calculateResult) && (Integer)calculateResult.get("code") == 1){
                                    PosLog log = new PosLog();
                                    log.setOutlet(Main.posSetting.get("outlet"));
                                    log.setTDate(new Date());
                                    log.setTTime(new Date());
                                    log.setStaff(Main.posStaff.getCode());
                                    log.setLogType(LogTypeEnum.UCOP.getValue());
                                    log.setType("N");
                                    log.setTIndex(couponParm.gettIndex().equals("null")?0:Long.parseLong(couponParm.gettIndex()));
                                    log.setRefNum(couponParm.getRefNum());
                                    log.setSubRef(couponParm.getSubRef());
                                    log.setTable1(couponParm.getTableNo());
                                    log.setRemark1(couponGetDto.getCouponNum());
                                    log.setAmt3(new BigDecimal(calculateResult.get("disAmt").toString()));
                                    log.setRemark2(useCouponEntity.getCouponType());
                                    log.setRemark3(useCouponEntity.getRemark3());
                                    log.setAmt1(useCouponEntity.getDiscAmt());
                                    log.setAmt2(useCouponEntity.getRealAmt());
                                    memberController.getPosLogService().insert(log);
                                    showWarningView(Main.languageMap.get("coupon.useSuccess"),memberStage);
                                    memberController.initCouponTableData(cardno);
                                }
                            }
                        }else{
                            //用券失敗
                            showWarningView(resultJson.get("msg").toString(),memberStage);
                        }

                    }catch (Exception e){
                        showWarningView(Main.languageMap.get("network.refused"),memberStage);
                        e.printStackTrace();
                        throw e;
                    }
                }
            }else{
                showWarningView(Main.languageMap.get("member.useCouponTip2"),memberStage);
            }
        });

        this.getChildren().add(couponActionBut);
    }

    private String showWarningView(String msg,Stage memberStage){
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
        return ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"),msg, resultMap, memberStage);
    }

    /**
     * 計算IQ券的優惠金額
     * @param posBindUseCoupon
     * @return
     */
    private Map<String,Object> calculateIQCouponDiscAmount(MemberController memberController,PosBindUseCoupon posBindUseCoupon,List<PosOrderDiscDto> orderList){
        Map<String,Object> result = new HashedMap();
        String msg = "";
        Integer code = -1;
        BigDecimal disAmt = BigDecimal.ZERO;
        String couponType = posBindUseCoupon.getCouponType();
        if(AppUtils.isNotBlank(couponType)){
            BigDecimal sumCostAmt = new BigDecimal(orderList.stream().mapToDouble(w -> w.getCostAmt().doubleValue()).sum());
            if(AppUtils.isNotBlank(orderList)){
                for (PosOrderDiscDto orderDisc:orderList){
                    if(couponType.equals(CouponTypeEnum.CASH.getCode())){
                        //現金券  计算公式(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))/sum(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))*50
                        BigDecimal cashDis = bigDecimalByPloy(divide(orderDisc.getCostAmt(),sumCostAmt).multiply(posBindUseCoupon.getFaceAmt()));
                        memberController.getPosOrderService().updateItemDisc(orderDisc.getId(),cashDis);
                        disAmt = disAmt.add(cashDis);
                        code = 1;
                    }else if(couponType.equals(CouponTypeEnum.GIFT.getCode())){
                        //禮品券
                        if(AppUtils.isNotBlank(posBindUseCoupon.getRemark3())){
                            if(orderDisc.getItemCode().equals(posBindUseCoupon.getRemark3()) && AppUtils.isNotBlank(posBindUseCoupon.getDiscAmt())){
                                //針對單個商品打折
                                BigDecimal aDouble =new BigDecimal(100.00);
                                BigDecimal dis = aDouble.subtract(posBindUseCoupon.getDiscAmt());
                                BigDecimal discost = orderDisc.getCostAmt().multiply(dis.multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                memberController.getPosOrderService().updateItemDisc(orderDisc.getId(),bigDecimalByPloy(discost));
                                disAmt = disAmt.add(discost);
                                code = 1;
                            }
                        }else{
                            code = -1;
                            msg = Main.languageMap.get("coupon.couponTypeError1");
                            break;
                        }
                    }else if(couponType.equals(CouponTypeEnum.DISCOUNT.getCode())){
                        //折扣券
                        BigDecimal discost = orderDisc.getCostAmt().multiply(posBindUseCoupon.getDiscAmt().multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                        memberController.getPosOrderService().updateItemDisc(orderDisc.getId(),bigDecimalByPloy(discost));
                        disAmt = disAmt.add(discost);
                        code = 1;
                    }
                }
            }
        }else{
            msg = Main.languageMap.get("coupon.couponTypeError2");
            code = -1;
        }
        result.put("msg",msg);
        result.put("disAmt",disAmt);
        result.put("code",code);
        return result;
    }


}
