package com.dw.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.component.SettleComponent;
import com.dw.dto.*;
import com.dw.entity.*;
import com.dw.enums.*;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.model.CouponParm;
import com.dw.service.*;
import com.dw.util.*;
import com.dw.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.dw.util.DateUtil.CM_DATE_FORMAT;
import static com.dw.util.DateUtil.DateToString;
import static com.dw.util.DateUtil.getCurrTime;
import static com.dw.util.DecimalUtil.bigDecimalByPloy;
import static com.dw.util.DecimalUtil.divide;

@Getter
@Setter
@FXMLController
public class CouponController implements Initializable {
    @FXML
    private VBox couponMainPane;
    @FXML
    private HBox couponTopPane;
    @FXML
    private VBox couponPane;
    @FXML
    private ListView<String> dischdListView;
    @FXML
    private HBox topPane;
    @FXML
    private TextField textField;
    @FXML
    private DwButton keyboard;
    @FXML
    private TableView<CouponDto> tableViewId;
    @FXML
    private HBox buttomPane;
    @FXML
    private DwButton cancelCouponButton;
    @FXML
    private DwButton nextPage;
    @FXML
    private DwButton prePage;
    @FXML
    private DwButton close_Button;
    @FXML
    private TableColumn colCouponType;
    @FXML
    private TableColumn colCoupon;
    @FXML
    private TableColumn colTime;
    @FXML
    private TableColumn colDisAmt;


    @Autowired
    private PosLogService posLogService;
    @Autowired
    private PosLogHisService posLogHisService;
    @Autowired
    private PosDischdService dischdService;
    @Autowired
    private PosDiscdetdService discdetdService;
    @Autowired
    private PosOrderService posOrderService;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
    @Autowired
    private SettleComponent settleComponent;
    @Autowired
    private MainView mainView;

    private Stage couponStage;
    private CouponParm couponParm = new CouponParm();
    private ObservableList<CouponDto> couponDtos = FXCollections.observableArrayList();
    private ObservableList<String> dischdStr = FXCollections.observableArrayList();

    private String indate = DateToString(new Date(), CM_DATE_FORMAT);
    private Integer week = DateUtil.getWeek(new Date());
    private String inTime = getCurrTime().substring(11, 19);

    //账单明细
    private List<PosOrderDiscDto> orderList = new ArrayList<>();
    //账单食品总费用
    private BigDecimal orderAmt = BigDecimal.ZERO;
    //是否改單操作true是，false否
    private boolean isUpdateOrder = false;
    private LogTypeEnum discTypeEnum;
    private String initDataType = "";

    /**
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            couponStage = (Stage) couponMainPane.getScene().getWindow();
        });
        couponComponent();
    }

    /**
     * 加载优惠券页面，含使用预设折扣
     */
    public void couponComponent() {
        double couponMainWidth = Main.primaryScreenBounds.getWidth() * 0.8;
        double couponMainHieght = Main.primaryScreenBounds.getHeight() * 0.8;
        couponMainPane.setPrefSize(couponMainWidth, couponMainHieght);

        double couponWidth = couponMainWidth * 0.7;
        double couponHeight = couponMainHieght * 0.85;

        dischdListView.setPrefSize(couponMainWidth * 0.3, couponHeight);

        topPane.setAlignment(Pos.CENTER);
        topPane.setPrefSize(couponWidth, couponHeight * 0.1);
        HBox.setMargin(textField, new Insets(6));
        HBox.setMargin(keyboard, new Insets(6));

        textField.setPrefSize(couponWidth / 6 * 4, couponHeight * 0.1);
        textField.setText("");
        textField.requestFocus();
        textField.setPromptText(Main.languageMap.get("coupon.inputcode"));
        textField.setFocusTraversable(true);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String scanCode = textField.getText();
                    if (AppUtils.isNotBlank(scanCode)) {
                        useCouponTableView(scanCode);
                    }
                }
            }
        });

        keyboard.setGraphic(new ImageView(AppUtils.loadImage("keyboard.png")));
        keyboard.setPrefSize(couponWidth / 6 * 1, couponHeight * 0.1);
        keyboard.setOnAction(event -> {
            //彈出鍵盤
            String couponCode = ShowViewUtil.showKeyword(couponStage, "");
            if (AppUtils.isNotBlank(couponCode) && !couponCode.equals(ResultEnum.NO.getValue())) {
                textField.setText(couponCode);
                useCouponTableView(couponCode);
            }
        });
        tableViewId.setPrefSize(couponWidth, couponHeight * 0.9);
        DwLabel tableEmptyDesc = new DwLabel(FontSizeEnum.font18);
        tableEmptyDesc.setText("");
        tableViewId.setPlaceholder(tableEmptyDesc);

        colCouponType.setCellValueFactory(new PropertyValueFactory("couponName")); //tableView映射
        colCoupon.setCellValueFactory(new PropertyValueFactory("couponCode"));
        colTime.setCellValueFactory(new PropertyValueFactory("useTime"));
        colDisAmt.setCellValueFactory(new PropertyValueFactory("disAmt"));

        colCouponType.setPrefWidth(tableViewId.getPrefWidth() / 4);
        colCoupon.setPrefWidth(tableViewId.getPrefWidth() / 4);
        colTime.setPrefWidth(tableViewId.getPrefWidth() / 4);
        colDisAmt.setPrefWidth(tableViewId.getPrefWidth() / 4);

        buttomPane.setPrefSize(couponMainWidth, couponMainHieght * 0.15);

        //取消用券/折扣
        cancelCouponButton.setPrefSize(couponMainWidth / 4, couponMainHieght * 0.15);
        cancelCouponButton.setText(Main.languageMap.get("coupon.cancelCoupon"));
        cancelCouponButton.setOnAction(event -> {
            cancelCouponOrDis();
        });

        nextPage.setPrefSize(couponMainWidth / 4, couponMainHieght * 0.15);
        nextPage.setText(Main.languageMap.get("coupon.next"));
        nextPage.setOnAction(event -> {
            //下一頁數據
        });

        prePage.setPrefSize(couponMainWidth / 4, couponMainHieght * 0.15);
        prePage.setText(Main.languageMap.get("coupon.pre"));
        prePage.setOnAction(event -> {
            //上一頁數據
        });
        close_Button.setPrefSize(couponMainWidth / 4, couponMainHieght * 0.15);
        close_Button.setText(Main.languageMap.get("global.close"));
        close_Button.setOnAction(event -> {
            if (AppUtils.isBlank(couponStage)) {
                couponStage = (Stage) couponPane.getScene().getWindow();
            }
            couponStage.close();
            textField.setText("");
            dischdListView.getSelectionModel().select(-1);
            takeOrderIndexController.calculate();
            if (AppUtils.isNotBlank(initDataType) && initDataType.equals(InitDataTypeEnum.BARCODE.getValue())) {
                takeOrderIndexController.closeByInitDataType(couponParm.getRefNum(), couponParm.getSubRef());
            }


        });

        HBox.setMargin(cancelCouponButton, new Insets(4));
        HBox.setMargin(nextPage, new Insets(4));
        HBox.setMargin(prePage, new Insets(4));
        HBox.setMargin(close_Button, new Insets(4));

        dischdListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> {
                    if (AppUtils.isNotBlank(newValue)) {
                        //useDis(newValue);
                        // 使用预设折扣
                        useDisTableView(newValue);
                    }
                });
            }
        });
    }


    /**
     * 使用优惠券(需要先送单)
     */
    private void useCoupon(String scanCode) {
        // 調用券接口
        orderList = posOrderService.getAvailableOrderList(Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), DateUtil.DateToString(new Date(), CM_DATE_FORMAT));
        if (AppUtils.isNotBlank(orderList)) {
            if (AppUtils.isNotBlank(scanCode)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("outline", Main.outline));
                params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
                params.add(new BasicNameValuePair("tableNo", couponParm.getTableNo()));
                params.add(new BasicNameValuePair("scanCode", scanCode));
                params.add(new BasicNameValuePair("refNum", couponParm.getRefNum()));
                params.add(new BasicNameValuePair("subRef", couponParm.getSubRef()));
                params.add(new BasicNameValuePair("tIndex", couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                params.add(new BasicNameValuePair("channel", "HTML"));
                String url = Main.posSetting.get("apiUrl");
                if (scanCode.substring(0, 2).equals("IQ")) {
                    //線上券
                    params.add(new BasicNameValuePair("isSendMsg", "false"));
                    params.add(new BasicNameValuePair("isVoucher", 0 + ""));
                    url += Main.posSetting.get("bindOrUseCoupon");
                } else {
                    //第三方券
                    params.add(new BasicNameValuePair("staff", Main.posStaff.getCode()));
                    url += Main.posSetting.get("useCoupon");
                }
                //发送http请求
                try {
                    String responseStr = HttpClientUtil.post(url, params);
                    JSONObject resultJson = JSONObject.parseObject(responseStr);
                    if (AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")) {
                        //用券成功
                        textField.setText("");
                        if (scanCode.substring(0, 2).equals("IQ")) {
                            //線上券
                            if (AppUtils.isNotBlank(resultJson.get("data"))) {
                                String useGiftCouponListStr = resultJson.get("data").toString();
                                JSONArray jsonArray = JSONArray.fromObject(useGiftCouponListStr);
                                //獲取優惠券基本信息
                                List<PosBindUseCoupon> useGiftCouponList = (List<PosBindUseCoupon>) JSONArray.toCollection(jsonArray, PosBindUseCoupon.class);

                                if (AppUtils.isNotBlank(useGiftCouponList)) {
                                    PosBindUseCoupon useCouponEntity = useGiftCouponList.get(0);
                                    //計算IQ券折扣金額
                                    Map<String, Object> result = calculateIQCouponDiscAmount(useCouponEntity, orderList);
                                    if (AppUtils.isNotBlank(result)) {
                                        if ((Integer) result.get("code") == 1) {
                                            PosLog log = new PosLog();
                                            log.setOutlet(Main.posSetting.get("outlet"));
                                            log.setTDate(new Date());
                                            log.setTTime(new Date());
                                            log.setStaff(Main.posStaff.getCode());
                                            log.setLogType(LogTypeEnum.UCOP.getValue());
                                            log.setType("N");
                                            log.setTIndex(couponParm.gettIndex().equals("null") ? 0 : Long.parseLong(couponParm.gettIndex()));
                                            log.setRefNum(couponParm.getRefNum());
                                            log.setSubRef(couponParm.getSubRef());
                                            log.setTable1(couponParm.getTableNo());
                                            log.setRemark1(scanCode);
                                            log.setAmt3(new BigDecimal(result.get("disAmt").toString()));
                                            log.setRemark2(useCouponEntity.getCouponType());
                                            log.setRemark3(useCouponEntity.getRemark3());
                                            log.setAmt1(useCouponEntity.getDiscAmt());
                                            log.setAmt2(useCouponEntity.getRealAmt());
                                            posLogService.insert(log);
                                            initTableViewData();
                                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.useSuccess"), resultMap, couponStage);
                                        } else {
                                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), resultJson.get("msg").toString(), resultMap, couponStage);
                                        }
                                    }
                                }
                            }
                        } else {
                            //第三方券
                            String posStr = resultJson.get("data").toString();
                            JSONObject posStrJson = JSONObject.parseObject(posStr);
                            PosLog log = JSONObject.parseObject(posStrJson.get("log").toString(), PosLog.class);
                            if (AppUtils.isNotBlank(log)) {
                                //計算每次用券的折扣金額。
                                Map<String, Object> result = calculateCouponDiscAmount(log, orderList);
                                if (AppUtils.isNotBlank(result)) {
                                    if ((Integer) result.get("code") == 1) {
                                        log.setOutlet(Main.posSetting.get("outlet"));
                                        log.setAmt3((BigDecimal) result.get("disAmt"));
                                        posLogService.insert(log);
                                        initTableViewData();
                                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.useSuccess"), resultMap, couponStage);
                                    } else {
                                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), result.get("msg").toString(), resultMap, couponStage);
                                    }
                                }
                            }
                        }
                    } else {
                        //用券失敗
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), resultJson.get("msg").toString(), resultMap, couponStage);
                        textField.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("network.refused"), resultMap, couponStage);
                    textField.setText("");
                }
            }
        } else {
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError2"), resultMap, couponStage);
        }
    }

    /**
     * 使用折扣 disCode（需要先送单）
     *
     * @param disCode
     */
    private void useDis(String disCode) {
        String dischCode = disCode.split("-")[0];
        if (AppUtils.isNotBlank(dischCode)) {
            if (AppUtils.isBlank(orderList)) {
                //無賬單信息,折扣使用失敗
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError2"), resultMap, couponStage);
            } else if (orderAmt.compareTo(BigDecimal.ZERO) == 0) {
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError3"), resultMap, couponStage);
            } else {
                List<PosDisDto> posDisDtoList = dischdService.getPosDisDetail(dischCode);
                if (AppUtils.isNotBlank(posDisDtoList)) {
                    Boolean flag = true;
                    if (posDisDtoList.get(0).getType().equals(DiscTypeEnum.DCTP.getValue()) || posDisDtoList.get(0).getType().equals(DiscTypeEnum.DSCP.getValue())) {
                        String[] typeArr = {DiscTypeEnum.DCTP.getValue(), DiscTypeEnum.DSCP.getValue()};
                        Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                        posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                        posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                        posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                        posLogWrapper.in("LOG_TYPE", typeArr);
                        posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                        if (AppUtils.isNotBlank(posLogService.selectList(posLogWrapper))) {
                            flag = false;
                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError4"), resultMap, couponStage);
                        }
                    }
                    if (flag) {
                        Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                        posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                        posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                        posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                        posLogWrapper.in("LOG_TYPE", new String[]{LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()});
                        posLogWrapper.eq("REMARK1", dischCode);
                        posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                        List<PosLog> poslogHis = posLogService.selectList(posLogWrapper);
                        if (AppUtils.isBlank(poslogHis)) {
                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                            resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useTip1"), resultMap, couponStage);
                            if (AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())) {
                                startDisUse(dischCode, posDisDtoList);
                            }
                        } else {
                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                            resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("global.useddisc"), resultMap, couponStage);
                            if (AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())) {
                                startDisUse(dischCode, posDisDtoList);
                            }
                        }
                    }
                } else {
                    //折扣信息異常
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError1"), resultMap, couponStage);
                }
            }
        }
        dischdListView.getSelectionModel().select(-1);
    }

    private void startDisUse(String dischCode, List<PosDisDto> posDisDtoList) {
        //計算折扣金額並更新order表
        Map<String, Object> map = calculateDiscAmount(dischCode, posDisDtoList);
        if (AppUtils.isNotBlank(map) && AppUtils.isNotBlank(map.get("disAmt"))) {
            BigDecimal disAmt = new BigDecimal(map.get("disAmt").toString());
            PosDisDto posDischd = posDisDtoList.get(0);
            //插入數據到log表
            List<PosLog> posLogs = new ArrayList<>();
            PosLog posLog = new PosLog();
            posLog.setOutlet(Main.posSetting.get("outlet"));
            posLog.setTDate(new Date());
            posLog.setTTime(new Date());
            posLog.setStaff(Main.posStaff.getCode());
            if (discTypeEnum.equals(LogTypeEnum.SINGLE)) {
                posLog.setLogType(LogTypeEnum.SINGLE.getValue());
            } else {
                posLog.setLogType(LogTypeEnum.FULL.getValue());
            }
            posLog.setType(TranTypeEnum.N.getValue());
            posLog.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
            posLog.setRefNum(couponParm.getRefNum());
            posLog.setSubRef(couponParm.getSubRef());
            posLog.setTable1(couponParm.getTableNo());
            posLog.setRemark1(posDisDtoList.get(0).getCode());
            posLog.setRemark2(posDischd.getAllUse() + "");
            posLog.setRemark3(posDischd.getDisName());
            posLog.setAmt3(disAmt);             //記錄折扣金額
            posLogService.insert(posLog);

            for (PosDisDto discdet : posDisDtoList) {
                PosLog posLogDet = new PosLog();
                posLogDet.setOutlet(Main.posSetting.get("outlet"));
                posLogDet.setTDate(new Date());
                posLogDet.setTTime(new Date());
                posLogDet.setStaff(Main.posStaff.getCode());
                posLogDet.setLogType(discdet.getType());
                posLogDet.setType(TranTypeEnum.N.getValue());
                posLogDet.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                posLogDet.setRefNum(couponParm.getRefNum());
                posLogDet.setSubRef(couponParm.getSubRef());
                posLogDet.setTable1(couponParm.getTableNo());
                posLogDet.setRemark1(dischCode);
                posLogDet.setRemark2(discdet.getDetail());
                posLogDet.setRemark3(discdet.getDvalue() + "");
                posLogDet.setRemark4(posLog.getId());
                posLogs.add(posLogDet);
            }

            if (AppUtils.isNotBlank(posLogs)) {
                posLogService.insertBatch(posLogs);
            }
            initTableViewData();
            dischdListView.getSelectionModel().select(-1);
        }

    }

    /**
     * 計算折扣金額
     *
     * @param dischCode
     * @param posDisDtoList
     * @return
     */
    private Map<String, Object> calculateDiscAmount(String dischCode, List<PosDisDto> posDisDtoList) {
        Map<String, Object> map = new HashedMap();
        Integer code = -1;
        String msg = "";
        BigDecimal disAmt = BigDecimal.ZERO;
        orderList = posOrderService.getAvailableOrderList(Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), DateUtil.DateToString(new Date(), CM_DATE_FORMAT));
        for (PosOrderDiscDto orderDisc : orderList) {
            for (PosDisDto dis : posDisDtoList) {
                switch (DiscTypeEnum.getByValue(dis.getType().trim())) {
                    case DSCP: //大大类折扣
                        if (verifyDisc(Integer.parseInt(dis.getAllUse()), dis.getDetail(), orderDisc.getScat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                            BigDecimal getDvalue = new BigDecimal(dis.getDvalue());
                            BigDecimal multiply_getDvalue = getDvalue.multiply(new BigDecimal(0.01));
                            BigDecimal discost = orderDisc.getCostAmt().multiply(multiply_getDvalue).setScale(4, BigDecimal.ROUND_HALF_UP);
                            posOrderService.updateCateDisc(orderDisc.getId(), discost);
                            disAmt = disAmt.add(discost);
                        }
                        break;
                    case DCTP://大类折扣
                        if (verifyDisc(Integer.parseInt(dis.getAllUse()), dis.getDetail(), orderDisc.getCat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                            BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getDvalue()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                            posOrderService.updateCateDisc(orderDisc.getId(), discost);
                            disAmt = disAmt.add(discost);
                        }
                        break;
                    case DORP: //單折扣%
                        if (verifyDiscBill(Integer.parseInt(dis.getAllUse()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                            BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getDvalue()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                            posOrderService.updateItemDisc(orderDisc.getId(), discost);
                            disAmt = disAmt.add(discost);
                        }
                        break;
                    case DORA://單折扣%
                        if (verifyDiscBill(Integer.parseInt(dis.getAllUse()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                            // 把折扣金額換算成賬單的百分比保留四位小数，并四舍五入，然後再計算
                            BigDecimal disRate = new BigDecimal(dis.getDvalue()).divide(orderAmt, 4, BigDecimal.ROUND_HALF_UP);
                            BigDecimal discost = orderDisc.getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                            posOrderService.updateItemDisc(orderDisc.getId(), discost);
                            disAmt = disAmt.add(discost); //此金额最后还要与最折扣金额进行差异处理
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        map.put("disAmt", disAmt);
        return map;
    }

    /**
     * 計算每次用券的折扣金額，可以折上折。
     */
    private Map<String, Object> calculateCouponDiscAmount(PosLog posLog, List<PosOrderDiscDto> orderList) {
        Map<String, Object> result = new HashedMap();
        String couponType = posLog.getRemark2();  //優惠券類型
        String msg = "";
        Integer code = 1;
        BigDecimal disAmt = BigDecimal.ZERO;
        if (AppUtils.isNotBlank(couponType)) {
            BigDecimal sumCostAmt = bigDecimalByPloy(new BigDecimal(orderList.stream().mapToDouble(w -> w.getCostAmt().doubleValue()).sum()));
            if (AppUtils.isNotBlank(orderList)) {
                for (PosOrderDiscDto orderDisc : orderList) {
                    if (couponType.equals(CouponTypeEnum.CASH.getCode())) {
                        //現金券  计算公式(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))/sum(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))*50
                        BigDecimal cashDis = bigDecimalByPloy(divide(orderDisc.getCostAmt(), sumCostAmt).multiply(posLog.getAmt1()));
                        posOrderService.updateItemDisc(orderDisc.getId(), cashDis);
                        disAmt = disAmt.add(cashDis);
                    } else if (couponType.equals(CouponTypeEnum.GIFT.getCode())) {
                        //禮品券
                        if (AppUtils.isNotBlank(posLog.getRemark3())) {
                            if (orderDisc.getItemCode().equals(posLog.getRemark3()) && AppUtils.isNotBlank(posLog.getRemark4())) {
                                //針對單個商品打折
                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(posLog.getRemark4()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                posOrderService.updateItemDisc(orderDisc.getId(), bigDecimalByPloy(discost));
                                disAmt = disAmt.add(discost);
                            }
                        } else {
                            code = -1;
                            msg = Main.languageMap.get("coupon.couponTypeError1");
                            break;
                        }
                    } else if (couponType.equals(CouponTypeEnum.DISCOUNT.getCode())) {
                        //折扣券
                        BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(posLog.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                        posOrderService.updateItemDisc(orderDisc.getId(), bigDecimalByPloy(discost));
                        disAmt = disAmt.add(discost);
                    }
                }
            }
        } else {
            msg = Main.languageMap.get("coupon.couponTypeError2");
            code = -1;
        }
        result.put("msg", msg);
        result.put("disAmt", disAmt);
        result.put("code", code);
        return result;
    }

    /**
     * 計算IQ券的優惠金額
     *
     * @param posBindUseCoupon
     * @return
     */
    public Map<String, Object> calculateIQCouponDiscAmount(PosBindUseCoupon posBindUseCoupon, List<PosOrderDiscDto> orderList) {
        Map<String, Object> result = new HashedMap();
        String msg = "";
        Integer code = -1;
        BigDecimal disAmt = BigDecimal.ZERO;
        String couponType = posBindUseCoupon.getCouponType();
        if (AppUtils.isNotBlank(couponType)) {
            BigDecimal sumCostAmt = new BigDecimal(orderList.stream().mapToDouble(w -> w.getCostAmt().doubleValue()).sum());
            if (AppUtils.isNotBlank(orderList)) {
                for (PosOrderDiscDto orderDisc : orderList) {
                    if (couponType.equals(CouponTypeEnum.CASH.getCode())) {
                        //現金券  计算公式(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))/sum(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))*50
                        BigDecimal cashDis = bigDecimalByPloy(divide(orderDisc.getCostAmt(), sumCostAmt).multiply(posBindUseCoupon.getFaceAmt()));
                        posOrderService.updateItemDisc(orderDisc.getId(), cashDis);
                        disAmt = disAmt.add(cashDis);
                        code = 1;
                    } else if (couponType.equals(CouponTypeEnum.GIFT.getCode())) {
                        //禮品券
                        if (AppUtils.isNotBlank(posBindUseCoupon.getRemark3())) {
                            if (orderDisc.getItemCode().equals(posBindUseCoupon.getRemark3()) && AppUtils.isNotBlank(posBindUseCoupon.getDiscAmt())) {
                                //針對單個商品打折
                                BigDecimal aDouble = new BigDecimal(100.00);
                                BigDecimal dis = aDouble.subtract(posBindUseCoupon.getDiscAmt());
                                BigDecimal discost = orderDisc.getCostAmt().multiply(dis.multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                posOrderService.updateItemDisc(orderDisc.getId(), bigDecimalByPloy(discost));
                                disAmt = disAmt.add(discost);
                                code = 1;
                            }
                        } else {
                            code = -1;
                            msg = Main.languageMap.get("coupon.couponTypeError1");
                            break;
                        }
                    } else if (couponType.equals(CouponTypeEnum.DISCOUNT.getCode())) {
                        //折扣券
                        BigDecimal discost = orderDisc.getCostAmt().multiply(posBindUseCoupon.getDiscAmt().multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                        posOrderService.updateItemDisc(orderDisc.getId(), bigDecimalByPloy(discost));
                        disAmt = disAmt.add(discost);
                        code = 1;
                    }
                }
            }
        } else {
            msg = Main.languageMap.get("coupon.couponTypeError2");
            code = -1;
        }
        result.put("msg", msg);
        result.put("disAmt", disAmt);
        result.put("code", code);
        return result;
    }

    /**
     * 取消优惠券/折扣
     */
    private void cancelCouponOrDis() {
        int rowIndex = tableViewId.getSelectionModel().getSelectedIndex();
        rowIndex = rowIndex > 0 ? rowIndex : 0;
        CouponDto logDto = tableViewId.getItems().get(rowIndex);
        if (AppUtils.isNotBlank(logDto)) {
            String couponCode = logDto.getCouponCode();
            if (logDto.getCouponType().equals(LogTypeEnum.FULL.getValue()) || logDto.getCouponType().equals(LogTypeEnum.SINGLE.getValue())) {
                //取消折扣
                Wrapper<PosLog> logWrapper = new EntityWrapper<>();
                logWrapper.in("LOG_TYPE", new String[]{LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()});
                logWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                logWrapper.eq("REF_NUM", couponParm.getRefNum());
                logWrapper.eq("SUB_REF", couponParm.getSubRef());
                logWrapper.eq("TABLE1", couponParm.getTableNo());
                logWrapper.eq("REMARK1", couponCode);
                PosLog log = posLogService.selectOne(logWrapper);
                if (AppUtils.isNotBlank(log)) {
                    log.setType(TranTypeEnum.V.getValue());
                    if (posLogService.updateById(log)) {
                        posLogService.updateDisType(TranTypeEnum.V.getValue(), Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), couponParm.getTableNo(), couponCode, log.getId());
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("global.alreadycanceldisc"), resultMap, couponStage);
                        initTableViewData();
                        //calculateDis重新計算折扣
                        settleComponent.calculateDisTableView("local", couponParm.getRefNum(), couponParm.getSubRef(), couponParm.getTableNo(), isUpdateOrder, couponParm.getInTime());
                    } else {
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("global.canceldiscfailure"), resultMap, couponStage);
                    }
                } else {
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("global.canceldiscfailure"), resultMap, couponStage);
                }
            } else {
                //調用取消用券的接口
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("couponNum", couponCode));
                params.add(new BasicNameValuePair("channel", "HTML"));
                String url = Main.posSetting.get("apiUrl");
                if (couponCode.substring(0, 2).equals("IQ")) {
                    url += Main.posSetting.get("cancelCoupon");
                } else {
                    url += Main.posSetting.get("cancelSysCoupon");
                }
                try {
                    String responseStr = HttpClientUtil.post(url, params);
                    JSONObject resultJson = JSONObject.parseObject(responseStr);
                    if (resultJson.get("code").toString().equals("1")) {
                        //取消成功,把Log表數據更新
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.cancelTip2"), resultMap, couponStage);
                        posLogService.updateCouponType(TranTypeEnum.V.getValue(), logDto.getId());
                        initTableViewData();
                        //calculateDis重新計算折扣
                        settleComponent.calculateDisTableView("local", couponParm.getRefNum(), couponParm.getSubRef(), couponParm.getTableNo(), isUpdateOrder, couponParm.getInTime());
                    } else {
                        //取消用券失敗
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), resultJson.get("msg").toString(), resultMap, couponStage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("network.refused"), resultMap, couponStage);
                }
            }
        } else {
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.cancelTip1"), resultMap, couponStage);
        }
    }

    /**
     * 需要先送单
     */
    public void calculateDis() {
        //先更新catDis和itemDis為0
        posOrderService.updateDisValue(couponParm.getRefNum(), couponParm.getSubRef(), DateUtil.DateToString(new Date(), CM_DATE_FORMAT), TranTypeEnum.N.getValue(), couponParm.getTableNo());
        //查出所有的用券記錄(要有券的使用順序)
        String[] logType = {LogTypeEnum.UCOP.getValue(), LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()};
        Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
        posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
        posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
        posLogWrapper.eq("TABLE1", couponParm.getTableNo());
        posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
        posLogWrapper.in("LOG_TYPE", logType);
        posLogWrapper.orderBy("VERSION", true);

        List<PosLog> logList = posLogService.selectList(posLogWrapper);
        if (AppUtils.isNotBlank(logList)) {
            for (PosLog log : logList) {
                orderList = posOrderService.getAvailableOrderList(Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), DateUtil.DateToString(new Date(), CM_DATE_FORMAT));
                if (log.getLogType().equals(LogTypeEnum.FULL.getValue()) || log.getLogType().equals(LogTypeEnum.SINGLE.getValue())) {
                    //使用折扣
                    Wrapper<PosLog> logWrapper = new EntityWrapper<>();
                    logWrapper.eq("REMARK4", log.getId());
                    List<PosLog> udisList = posLogService.selectList(logWrapper);
                    if (AppUtils.isNotBlank(udisList)) {
                        BigDecimal disAmt = BigDecimal.ZERO;
                        for (PosOrderDiscDto orderDisc : orderList) {
                            for (PosLog dis : udisList) {
                                switch (DiscTypeEnum.getByValue(dis.getLogType().trim())) {
                                    case DSCP: //大大类折扣
                                        if (verifyDisc(Integer.parseInt(log.getRemark2()), dis.getRemark2(), orderDisc.getScat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                            BigDecimal getDvalue = new BigDecimal(dis.getRemark3());
                                            BigDecimal multiply_getDvalue = getDvalue.multiply(new BigDecimal(0.01));
                                            BigDecimal discost = orderDisc.getCostAmt().multiply(multiply_getDvalue).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            posOrderService.updateCateDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost);
                                        }
                                        break;
                                    case DCTP://大类折扣
                                        if (verifyDisc(Integer.parseInt(log.getRemark2()), dis.getRemark2(), orderDisc.getCat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                            BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            posOrderService.updateCateDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost);
                                        }
                                        break;
                                    case DORP: //單折扣%
                                        if (verifyDiscBill(Integer.parseInt(log.getRemark2()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                            BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            posOrderService.updateItemDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost);
                                        }
                                        break;
                                    case DORA://單折扣%
                                        if (verifyDiscBill(Integer.parseInt(dis.getRemark2()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                            // 把折扣金額換算成賬單的百分比保留四位小数，并四舍五入，然後再計算
                                            BigDecimal disRate = new BigDecimal(dis.getRemark3()).divide(orderAmt, 4, BigDecimal.ROUND_HALF_UP);
                                            BigDecimal discost = orderDisc.getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                                            posOrderService.updateItemDisc(orderDisc.getId(), discost);
                                            disAmt = disAmt.add(discost); //此金额最后还要与最折扣金额进行差异处理
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        //更新log表的折扣金額
                        log.setAmt3(disAmt);
                        posLogService.updateById(log);
                    }
                } else {
                    //使用優惠券
                    if (AppUtils.isNotBlank(log.getRemark1())) {
                        if (log.getRemark1().substring(0, 2).equals("IQ")) {
                            //IQ券重算折扣金額
                            PosBindUseCoupon posBindUseCoupon = new PosBindUseCoupon();
                            posBindUseCoupon.setCardCoupon(log.getRemark1());
                            posBindUseCoupon.setCouponType(log.getRemark2());
                            posBindUseCoupon.setDiscAmt(log.getAmt1());
                            posBindUseCoupon.setFaceAmt(log.getAmt2());
                            posBindUseCoupon.setRealAmt(log.getAmt3());
                            posBindUseCoupon.setRemark3(log.getRemark3());
                            Map<String, Object> result = calculateIQCouponDiscAmount(posBindUseCoupon, orderList);
                            if ((Integer) result.get("code") == 1) {
                                log.setAmt3((BigDecimal) result.get("disAmt"));
                                posLogService.updateById(log);
                            } else {
                                //異常todo
                            }
                        } else {
                            //第三方券重算折扣金額
                            Map<String, Object> result = calculateCouponDiscAmount(log, orderList);
                            if (AppUtils.isNotBlank(result) && (Integer) result.get("code") == 1) {
                                log.setAmt3((BigDecimal) result.get("disAmt"));
                                posLogService.updateById(log);
                            } else {
                                //異常todo
                            }
                        }
                    }
                }
            }

        }
        //刷新tableview數據
        initTableViewData();
    }

    /**
     * 大類、大大類能否使用折扣驗證
     *
     * @param allUse        全單折扣
     * @param orgCatOrScat  折扣配置的大類/大大類
     * @param catOrScat     訂單明細的食品大類/大大類
     * @param itemBillDisc  食品表的能否打折
     * @param priceBillDisc 價格表的能否打折
     * @return
     */
    public boolean verifyDisc(Integer allUse, String orgCatOrScat, String catOrScat, String itemBillDisc, String priceBillDisc) {
        boolean canDisc = false;
        if (allUse == 1 && orgCatOrScat.equals(catOrScat)) {
            canDisc = true;
        } else if (allUse != 1 && orgCatOrScat.equals(catOrScat) && priceBillDisc.equals(ItemEspConfigTypeEnum.Z.getValue())) {
            canDisc = true;
        } else if (allUse != 1 && orgCatOrScat.equals(catOrScat) && priceBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        } else if (allUse != 1 && orgCatOrScat.equals(catOrScat) && AppUtils.isBlank(priceBillDisc) && itemBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        }

        return canDisc;
    }


    /**
     * 賬單折扣能否使用折扣驗證
     *
     * @param allUse        全單折扣
     * @param catDisc       大類能否與折扣同時使用
     * @param itemBillDisc  食品表的能否打折
     * @param priceBillDisc 價格表的能否打折
     * @return
     */
    public boolean verifyDiscBill(Integer allUse, String catDisc, String itemBillDisc, String priceBillDisc) {
        boolean canDisc = false;
        if (allUse == 1) {
            canDisc = true;
        } else if (allUse == 0 && catDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue()) && priceBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        } else if (allUse == 0 && catDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue()) && AppUtils.isBlank(priceBillDisc) && itemBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        }


        return canDisc;
    }

    /**
     * 賬單折扣能否使用折扣驗證
     *
     * @param itemBillDisc  食品表的能否打折
     * @param priceBillDisc 價格表的能否打折
     * @return
     */
    public boolean verifyDiscBill(String itemBillDisc, String priceBillDisc) {
        boolean canDisc = false;
        if (AppUtils.isNotBlank(priceBillDisc) && priceBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        } else if (AppUtils.isNotBlank(itemBillDisc) && itemBillDisc.equals(ItemEspConfigTypeEnum.FALSE.getValue())) {
            canDisc = true;
        }


        return canDisc;
    }

    /******************************************************用券计算foodtableview折扣********************************************************/
    public ObservableList<TableViewDto> foodTableViewDatas;

    /**
     * 初始化页面参数
     *
     * @param posTran
     */
    public void initDataFoodTableView(PosTran posTran, boolean isUpdateOrder, LogTypeEnum logTypeEnum, String initDataType) {
        this.isUpdateOrder = isUpdateOrder;
        this.discTypeEnum = logTypeEnum;
        this.initDataType = initDataType;
        couponParm.setCouponParm(posTran.getTableNum(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTIndex(), posTran.getInTime());
        initTableViewData();
        initListView(logTypeEnum);

        if (logTypeEnum.equals(LogTypeEnum.SINGLE)) {
            List<TableViewDto> selectedItemsList = takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedItems();
            foodTableViewDatas = FXCollections.observableArrayList(selectedItemsList.stream().filter(s -> AppUtils.isBlank(s.getMealCode())).collect(Collectors.toList()));
        } else {
            foodTableViewDatas = takeOrderIndexController.getTableViewData();
        }
        if (AppUtils.isNotBlank(foodTableViewDatas)) {
            orderAmt = new BigDecimal(foodTableViewDatas.stream().mapToDouble(w -> w.getAmt()).sum());

            settleComponent.createPosItemDtoMap();
            settleComponent.getTableViewData(foodTableViewDatas);
            settleComponent.getOrderAmt();
            settleComponent.calculateDisTableView("local", posTran.getRefNum(), posTran.getSubRef(), posTran.getTableNum(), isUpdateOrder, couponParm.getInTime());

        }
    }

    /**
     * 初始化tableview
     */
    public void initTableViewData() {
        TableViewDto dto = takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedItems().get(0);
        if (isUpdateOrder) {
            List<CouponDto> posLogDtoList = posLogHisService.getCouponData(Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), indate, couponParm.getTableNo(), discTypeEnum.getValue(),
                    dto != null ? dto.getCartId() : null);
            couponDtos = FXCollections.observableArrayList(posLogDtoList);
        } else {
            List<CouponDto> posLogDtoList = posLogService.getCouponData(Main.posSetting.get("outlet"), couponParm.getRefNum(), couponParm.getSubRef(), indate, couponParm.getTableNo(), discTypeEnum.getValue(),
                    dto != null ? dto.getCartId() : null);
            couponDtos = FXCollections.observableArrayList(posLogDtoList);
        }
        tableViewId.setItems(couponDtos); //tableview添加list
    }

    /**
     * 初始化listview
     */
    public void initListView(LogTypeEnum discTypeEnum) {
        List<PosDischd> dischds = dischdService.getPosDischds(indate, inTime, week + "", discTypeEnum.getValue());
        if (AppUtils.isNotBlank(dischds)) {
            List<String> list = new ArrayList<>();
            for (PosDischd d : dischds) {
                list.add(d.getCode() + "-" + d.getDesc1());
            }
            dischdStr = FXCollections.observableArrayList(list);
            ObservableList<String> strList = FXCollections.observableArrayList(dischdStr);
            dischdListView.setItems(strList);

            dischdListView.getSelectionModel().select(-1);

        }
    }


    /**
     * 使用折扣 disCode
     *
     * @param disCode
     */
    private void useDisTableView(String disCode) {
        String dischCode = disCode.split("-")[0];
        List<PosLog> posLogList = null;
        TableViewDto singleSelectItem = null;
        if (AppUtils.isNotBlank(dischCode)) {
            // 判斷是否選擇了一個單品或主套餐，套餐明細不參加單項折扣
            if (discTypeEnum.equals(LogTypeEnum.SINGLE)) {
                // 獲取選擇的記錄
                List<TableViewDto> selectedItemsList = takeOrderIndexController.getFoodTableView().getSelectionModel().getSelectedItems();
                singleSelectItem = selectedItemsList.get(0);
            }
            if (AppUtils.isBlank(settleComponent.foodTableViewDatas)) {
                //無賬單信息,折扣使用失敗
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError2"), resultMap, couponStage);
            } else if (orderAmt.compareTo(BigDecimal.ZERO) == 0) {
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError3"), resultMap, couponStage);
            } else {
                List<PosDisDto> posDisDtoList = dischdService.getPosDisDetail(dischCode);
                if (AppUtils.isNotBlank(posDisDtoList)) {
                    Boolean flag = true;
                    if (posDisDtoList.get(0).getType().equals(DiscTypeEnum.DCTP.getValue()) || posDisDtoList.get(0).getType().equals(DiscTypeEnum.DSCP.getValue())) {
                        String[] typeArr = {DiscTypeEnum.DCTP.getValue(), DiscTypeEnum.DSCP.getValue()};
                        if (isUpdateOrder) {
                            Wrapper<PosLogHis> posLogWrapper = new EntityWrapper<>();
                            posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                            posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                            posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                            posLogWrapper.in("LOG_TYPE", typeArr);
                            posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                            List<PosLogHis> posLogHisList = posLogHisService.selectList(posLogWrapper);
                            posLogList = posLogHisList.stream().filter(posLogHis -> posLogHis instanceof PosLog).collect(Collectors.toList());
                        } else {
                            Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                            posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                            posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                            posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                            posLogWrapper.in("LOG_TYPE", typeArr);
                            posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                            posLogList = posLogService.selectList(posLogWrapper);
                        }
                        if (AppUtils.isNotBlank(posLogList)) {
                            flag = false;
                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError4"), resultMap, couponStage);
                        }
                    }
                    if (flag) {
                        if (isUpdateOrder) {
                            Wrapper<PosLogHis> posLogWrapper = new EntityWrapper<>();
                            posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                            posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                            posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                            posLogWrapper.in("LOG_TYPE", new String[]{LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()});
                            posLogWrapper.eq("REMARK1", dischCode);
                            posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                            List<PosLogHis> posLogHisList = posLogHisService.selectList(posLogWrapper);
                            posLogList = posLogHisList.stream().filter(posLogHis -> posLogHis instanceof PosLog).collect(Collectors.toList());
                        } else {
                            Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                            posLogWrapper.eq("REF_NUM", couponParm.getRefNum());
                            posLogWrapper.eq("SUB_REF", couponParm.getSubRef());
                            posLogWrapper.eq("TABLE1", couponParm.getTableNo());
                            posLogWrapper.in("LOG_TYPE", new String[]{LogTypeEnum.FULL.getValue(), LogTypeEnum.SINGLE.getValue()});
                            posLogWrapper.eq("REMARK1", dischCode);
                            posLogWrapper.eq("TYPE", TranTypeEnum.N.getValue());
                            posLogList = posLogService.selectList(posLogWrapper);
                        }
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                        resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                        String result = null;
                        if (AppUtils.isBlank(posLogList)) {
                            result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useTip1"), resultMap, couponStage);
                        } else {
                            result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("global.useddisc"), resultMap, couponStage);
                        }
                        if (AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())) {
                            startDisUseTableView(dischCode, posDisDtoList, singleSelectItem);
                        }
                    }
                } else {
                    //折扣信息異常
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError1"), resultMap, couponStage);
                }
            }
        }
        dischdListView.getSelectionModel().select(-1);
    }


    /**
     * 调用计算折扣的方法，并把折扣信息插入到日志表
     */
    private void startDisUseTableView(String dischCode, List<PosDisDto> posDisDtoList, TableViewDto singleSelectItem) {
        //計算折扣金額並更新order表
        Map<String, Object> map = calculateDiscAmountTableView(dischCode, posDisDtoList, singleSelectItem);
        if (AppUtils.isNotBlank(map) && AppUtils.isNotBlank(map.get("disAmt"))) {
            BigDecimal disAmt = new BigDecimal(map.get("disAmt").toString());
            PosDisDto posDischd = posDisDtoList.get(0);
            //插入數據到log表
            if (isUpdateOrder) {
                List<PosLogHis> posLogs = new ArrayList<>();
                PosLogHis posLogHis = new PosLogHis();
                posLogHis.setOutlet(Main.posSetting.get("outlet"));
                posLogHis.setTDate(new Date());
                posLogHis.setTTime(new Date());
                posLogHis.setStaff(Main.posStaff.getCode());
                if (discTypeEnum.equals(LogTypeEnum.SINGLE)) {
                    posLogHis.setLogType(LogTypeEnum.SINGLE.getValue());
                } else {
                    posLogHis.setLogType(LogTypeEnum.FULL.getValue());
                }

                posLogHis.setType(TranTypeEnum.N.getValue());
                posLogHis.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                posLogHis.setRefNum(couponParm.getRefNum());
                posLogHis.setSubRef(couponParm.getSubRef());
                posLogHis.setTable1(couponParm.getTableNo());
                posLogHis.setRemark1(posDisDtoList.get(0).getCode());
                posLogHis.setRemark2(posDischd.getAllUse() + "");
                posLogHis.setRemark3(posDischd.getDisName());
                if (singleSelectItem != null) {
                    posLogHis.setRemark4(singleSelectItem.getCartId());
                }
                posLogHis.setAmt3(disAmt);             //記錄折扣金額
                posLogHisService.insert(posLogHis);

                for (PosDisDto discdet : posDisDtoList) {
                    PosLogHis posLogDet = new PosLogHis();
                    posLogDet.setOutlet(Main.posSetting.get("outlet"));
                    posLogDet.setTDate(new Date());
                    posLogDet.setTTime(new Date());
                    posLogDet.setStaff(Main.posStaff.getCode());
                    posLogDet.setLogType(discdet.getType());
                    posLogDet.setType(TranTypeEnum.N.getValue());
                    posLogDet.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                    posLogDet.setRefNum(couponParm.getRefNum());
                    posLogDet.setSubRef(couponParm.getSubRef());
                    posLogDet.setTable1(couponParm.getTableNo());
                    posLogDet.setRemark1(dischCode);
                    posLogDet.setRemark2(discdet.getDetail());
                    posLogDet.setRemark3(discdet.getDvalue() + "");
                    posLogDet.setRemark4(posLogHis.getId());
                    posLogs.add(posLogDet);
                }

                if (AppUtils.isNotBlank(posLogs)) {
                    posLogHisService.insertBatch(posLogs);
                }
            } else {
                List<PosLog> posLogs = new ArrayList<>();
                PosLog posLog = new PosLog();
                posLog.setOutlet(Main.posSetting.get("outlet"));
                posLog.setTDate(new Date());
                posLog.setTTime(new Date());
                posLog.setStaff(Main.posStaff.getCode());
                if (discTypeEnum.equals(LogTypeEnum.SINGLE)) {
                    posLog.setLogType(LogTypeEnum.SINGLE.getValue());
                } else {
                    posLog.setLogType(LogTypeEnum.FULL.getValue());
                }

                posLog.setType(TranTypeEnum.N.getValue());
                posLog.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                posLog.setRefNum(couponParm.getRefNum());
                posLog.setSubRef(couponParm.getSubRef());
                posLog.setTable1(couponParm.getTableNo());
                posLog.setRemark1(posDisDtoList.get(0).getCode());
                posLog.setRemark2(posDischd.getAllUse() + "");
                posLog.setRemark3(posDischd.getDisName());
                if (singleSelectItem != null) {
                    posLog.setRemark4(singleSelectItem.getCartId());
                }
                posLog.setAmt3(disAmt);             //記錄折扣金額
                posLogService.insert(posLog);

                for (PosDisDto discdet : posDisDtoList) {
                    PosLog posLogDet = new PosLog();
                    posLogDet.setOutlet(Main.posSetting.get("outlet"));
                    posLogDet.setTDate(new Date());
                    posLogDet.setTTime(new Date());
                    posLogDet.setStaff(Main.posStaff.getCode());
                    posLogDet.setLogType(discdet.getType());
                    posLogDet.setType(TranTypeEnum.N.getValue());
                    posLogDet.setTIndex(Long.parseLong(couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                    posLogDet.setRefNum(couponParm.getRefNum());
                    posLogDet.setSubRef(couponParm.getSubRef());
                    posLogDet.setTable1(couponParm.getTableNo());
                    posLogDet.setRemark1(dischCode);
                    posLogDet.setRemark2(discdet.getDetail());
                    posLogDet.setRemark3(discdet.getDvalue() + "");
                    posLogDet.setRemark4(posLog.getId());
                    posLogs.add(posLogDet);
                }

                if (AppUtils.isNotBlank(posLogs)) {
                    posLogService.insertBatch(posLogs);
                }
            }
            initTableViewData();
            dischdListView.getSelectionModel().select(-1);
        }

    }

    /**
     * 計算折扣金額
     *
     * @param dischCode
     * @param posDisDtoList
     * @return
     */
    private Map<String, Object> calculateDiscAmountTableView(String dischCode, List<PosDisDto> posDisDtoList, TableViewDto singleSelectItem) {
        Map<String, Object> map = new HashedMap();
        Integer code = -1;
        String msg = "";
        BigDecimal disAmt = BigDecimal.ZERO;
        if (discTypeEnum.equals(LogTypeEnum.SINGLE)) {
            if (settleComponent.foodTableViewDatas.get(0) != null) {
                for (PosDisDto dis : posDisDtoList) {
                    BigDecimal singleDiscAmt = BigDecimal.ZERO;
                    switch (DiscTypeEnum.getByValue(dis.getType().trim())) {
                        case DORP: //單折扣%
                            // 判斷是否單項折扣
                            if (verifyDiscBill(settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getBillDisc(), settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getPbillDisc())) { // 判斷是否可以折扣
                                BigDecimal disRate = new BigDecimal(dis.getDvalue()).divide(new BigDecimal("100.00"), 4, BigDecimal.ROUND_HALF_UP);
                                singleDiscAmt = settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                                settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().setItemDisc(DecimalUtil.add(settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getItemDisc(), singleDiscAmt));
                                disAmt = disAmt.add(singleDiscAmt);
                            }
                            break;
                        case DORA://單折扣$
                            if (verifyDiscBill(settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getBillDisc(), settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getPbillDisc())) { // 判斷是否可以折扣
                                singleDiscAmt = new BigDecimal(dis.getDvalue());
                                settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().setItemDisc(DecimalUtil.add(settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getItemDisc(), singleDiscAmt));
                                disAmt = disAmt.add(singleDiscAmt);
                            }
                            break;
                        default:
                            break;
                    }
                    settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().setCostAmt(DecimalUtil.subtract(settleComponent.foodTableViewDatas.get(0).getPosOrderDiscDto().getCostAmt(), singleDiscAmt));
                }
            }
        } else {
            //orderList = posOrderService.getAvailableOrderList(Main.posSetting.get("outlet"),couponParm.getRefNum(),couponParm.getSubRef(),DateUtil.DateToString(new Date(),CM_DATE_FORMAT));
            for (TableViewDto tableViewDto : settleComponent.foodTableViewDatas) {
                PosOrderDiscDto orderDisc = tableViewDto.getPosOrderDiscDto();
                for (PosDisDto dis : posDisDtoList) {
                    BigDecimal singleDiscAmt = BigDecimal.ZERO;
                    switch (DiscTypeEnum.getByValue(dis.getType().trim())) {
                        case DSCP: //大大类折扣
                            if (verifyDisc(Integer.parseInt(dis.getAllUse()), dis.getDetail(), orderDisc.getScat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                BigDecimal getDvalue = new BigDecimal(dis.getDvalue());
                                BigDecimal multiply_getDvalue = getDvalue.multiply(new BigDecimal(0.01));
                                BigDecimal discost = orderDisc.getCostAmt().multiply(multiply_getDvalue).setScale(4, BigDecimal.ROUND_HALF_UP);
                                //posOrderService.updateCateDisc(orderDisc.getId(),discost);
                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                            break;
                        case DCTP://大类折扣
                            if (verifyDisc(Integer.parseInt(dis.getAllUse()), dis.getDetail(), orderDisc.getCat(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getDvalue()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                //posOrderService.updateCateDisc(orderDisc.getId(),discost);
                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                            break;
                        case DORP: //單折扣%
                            if (verifyDiscBill(Integer.parseInt(dis.getAllUse()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getDvalue()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                //posOrderService.updateItemDisc(orderDisc.getId(),discost);
                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                            break;
                        case DORA://單折扣$
                            if (verifyDiscBill(Integer.parseInt(dis.getAllUse()), orderDisc.getDisc(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                // 把折扣金額換算成賬單的百分比保留四位小数，并四舍五入，然後再計算
                                BigDecimal disRate = new BigDecimal(dis.getDvalue()).divide(orderAmt, 4, BigDecimal.ROUND_HALF_UP);
                                BigDecimal discost = orderDisc.getCostAmt().multiply(disRate).setScale(4, BigDecimal.ROUND_HALF_UP);
                                //posOrderService.updateItemDisc(orderDisc.getId(),discost);
                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                disAmt = disAmt.add(discost); //此金额最后还要与最折扣金额进行差异处理
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                            break;
                        case COMB://組別折扣
                            if (verifyDisc(Integer.parseInt(dis.getAllUse()), dis.getDetail(), orderDisc.getComb(), orderDisc.getBillDisc(), orderDisc.getPbillDisc())) {
                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(dis.getDvalue()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                //posOrderService.updateCateDisc(orderDisc.getId(),discost);
                                tableViewDto.getPosOrderDiscDto().setCatDisc(DecimalUtil.add(orderDisc.getCatDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                            break;
                        default:
                            break;
                    }
                    tableViewDto.getPosOrderDiscDto().setCostAmt(DecimalUtil.subtract(orderDisc.getCostAmt(), singleDiscAmt));
                }
            }
        }
        map.put("disAmt", disAmt);
        return map;
    }


    /**
     * 使用优惠券--tableView使用優惠券  接口用券
     */
    private void useCouponTableView(String scanCode) {
        // 調用券接口

        if (AppUtils.isNotBlank(settleComponent.foodTableViewDatas)) {
            if (AppUtils.isNotBlank(scanCode)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("outline", Main.outline));
                params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
                params.add(new BasicNameValuePair("tableNo", couponParm.getTableNo()));
                params.add(new BasicNameValuePair("scanCode", scanCode));
                params.add(new BasicNameValuePair("refNum", couponParm.getRefNum()));
                params.add(new BasicNameValuePair("subRef", couponParm.getSubRef()));
                params.add(new BasicNameValuePair("tIndex", couponParm.gettIndex().equals("null") ? "0" : couponParm.gettIndex()));
                params.add(new BasicNameValuePair("channel", "HTML"));
                String url = Main.posSetting.get("apiUrl");
                if (scanCode.substring(0, 2).equals("IQ")) {
                    //線上券
                    params.add(new BasicNameValuePair("isSendMsg", "false"));
                    params.add(new BasicNameValuePair("isVoucher", 0 + ""));
                    url += Main.posSetting.get("bindOrUseCoupon");
                } else {
                    //第三方券
                    params.add(new BasicNameValuePair("staff", Main.posStaff.getCode()));
                    url += Main.posSetting.get("useCoupon");
                }
                //发送http请求
                try {
                    String responseStr = HttpClientUtil.post(url, params);
                    JSONObject resultJson = JSONObject.parseObject(responseStr);
                    if (AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")) {
                        //用券成功
                        textField.setText("");
                        if (scanCode.substring(0, 2).equals("IQ")) {
                            //線上券
                            if (AppUtils.isNotBlank(resultJson.get("data"))) {
                                String useGiftCouponListStr = resultJson.get("data").toString();
                                JSONArray jsonArray = JSONArray.fromObject(useGiftCouponListStr);
                                //獲取優惠券基本信息
                                List<PosBindUseCoupon> useGiftCouponList = (List<PosBindUseCoupon>) JSONArray.toCollection(jsonArray, PosBindUseCoupon.class);

                                if (AppUtils.isNotBlank(useGiftCouponList)) {
                                    PosBindUseCoupon useCouponEntity = useGiftCouponList.get(0);
                                    //計算IQ券折扣金額
                                    Map<String, Object> result = calculateIQCouponDiscAmountTableView(useCouponEntity, foodTableViewDatas);
                                    if (AppUtils.isNotBlank(result)) {
                                        if ((Integer) result.get("code") == 1) {
                                            PosLog log = new PosLog();
                                            log.setOutlet(Main.posSetting.get("outlet"));
                                            log.setTDate(new Date());
                                            log.setTTime(new Date());
                                            log.setStaff(Main.posStaff.getCode());
                                            log.setLogType(LogTypeEnum.UCOP.getValue());
                                            log.setType("N");
                                            log.setTIndex(couponParm.gettIndex().equals("null") ? 0 : Long.parseLong(couponParm.gettIndex()));
                                            log.setRefNum(couponParm.getRefNum());
                                            log.setSubRef(couponParm.getSubRef());
                                            log.setTable1(couponParm.getTableNo());
                                            log.setRemark1(scanCode);
                                            log.setAmt3(new BigDecimal(result.get("disAmt").toString()));
                                            log.setRemark2(useCouponEntity.getCouponType());
                                            log.setRemark3(useCouponEntity.getRemark3());
                                            log.setAmt1(useCouponEntity.getDiscAmt());
                                            log.setAmt2(useCouponEntity.getRealAmt());
                                            if (isUpdateOrder) {
                                                posLogHisService.insert(posLogService.convertToHis(log));
                                            } else {
                                                posLogService.insert(log);
                                            }
                                            initTableViewData();
                                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.useSuccess"), resultMap, couponStage);
                                        } else {
                                            Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                            resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), resultJson.get("msg").toString(), resultMap, couponStage);
                                        }
                                    }
                                }
                            }
                        } else {
                            //第三方券
                            String posStr = resultJson.get("data").toString();
                            JSONObject posStrJson = JSONObject.parseObject(posStr);
                            PosLog log = JSONObject.parseObject(posStrJson.get("log").toString(), PosLog.class);
                            if (AppUtils.isNotBlank(log)) {
                                //計算每次用券的折扣金額。
                                Map<String, Object> result = calculateCouponDiscAmountTableView(log, foodTableViewDatas);
                                if (AppUtils.isNotBlank(result)) {
                                    if ((Integer) result.get("code") == 1) {
                                        log.setOutlet(Main.posSetting.get("outlet"));
                                        log.setAmt3((BigDecimal) result.get("disAmt"));
                                        if (isUpdateOrder) {
                                            posLogHisService.insert(posLogService.convertToHis(log));
                                        } else {
                                            posLogService.insert(log);
                                        }
                                        initTableViewData();
                                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("coupon.useSuccess"), resultMap, couponStage);
                                    } else {
                                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), result.get("msg").toString(), resultMap, couponStage);
                                    }
                                }
                            }
                        }
                    } else {
                        //用券失敗
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                        ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), resultJson.get("msg").toString(), resultMap, couponStage);
                        textField.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, String> resultMap = new LinkedHashMap<String, String>();
                    resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
                    ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("network.refused"), resultMap, couponStage);
                    textField.setText("");
                }
            }
        } else {
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("dischd.useError2"), resultMap, couponStage);
        }

    }

    /**
     * 計算每次用券的折扣金額，可以折上折。
     */
    public Map<String, Object> calculateCouponDiscAmountTableView(PosLog posLog, ObservableList<TableViewDto> foodTableViewItems) {
        Map<String, Object> result = new HashedMap();
        String couponType = posLog.getRemark2();  //優惠券類型
        String msg = "";
        Integer code = 1;
        BigDecimal disAmt = BigDecimal.ZERO;
        if (AppUtils.isNotBlank(couponType)) {
            BigDecimal sumCostAmt = bigDecimalByPloy(new BigDecimal(foodTableViewItems.stream().mapToDouble(w -> w.getPosOrderDiscDto().getCostAmt().doubleValue()).sum()));
            if (AppUtils.isNotBlank(foodTableViewItems)) {
                for (TableViewDto tableViewDto : foodTableViewItems) {
                    PosOrderDiscDto orderDisc = tableViewDto.getPosOrderDiscDto();
                    BigDecimal singleDiscAmt = BigDecimal.ZERO;
                    if (couponType.equals(CouponTypeEnum.CASH.getCode())) {
                        //現金券  计算公式(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))/sum(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))*50
                        BigDecimal cashDis = bigDecimalByPloy(divide(orderDisc.getCostAmt(), sumCostAmt).multiply(posLog.getAmt1()));

                        tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), cashDis));
                        disAmt = disAmt.add(cashDis);
                        singleDiscAmt = singleDiscAmt.add(cashDis);
                    } else if (couponType.equals(CouponTypeEnum.GIFT.getCode())) {
                        //禮品券
                        if (AppUtils.isNotBlank(posLog.getRemark3())) {
                            if (orderDisc.getItemCode().equals(posLog.getRemark3()) && AppUtils.isNotBlank(posLog.getRemark4())) {
                                //針對單個商品打折
                                BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(posLog.getRemark4()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                            }
                        } else {
                            code = -1;
                            msg = Main.languageMap.get("coupon.couponTypeError1");
                            break;
                        }
                    } else if (couponType.equals(CouponTypeEnum.DISCOUNT.getCode())) {
                        //折扣券
                        BigDecimal discost = orderDisc.getCostAmt().multiply(new BigDecimal(posLog.getRemark3()).multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                        tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                        disAmt = disAmt.add(discost);
                        singleDiscAmt = singleDiscAmt.add(discost);
                    }
                    tableViewDto.getPosOrderDiscDto().setCostAmt(DecimalUtil.subtract(orderDisc.getCostAmt(), singleDiscAmt));
                }
            }
        } else {
            msg = Main.languageMap.get("coupon.couponTypeError2");
            code = -1;
        }
        result.put("msg", msg);
        result.put("disAmt", disAmt);
        result.put("code", code);
        return result;
    }

    /**
     * 計算IQ券的優惠金額
     *
     * @param posBindUseCoupon
     * @return
     */
    public Map<String, Object> calculateIQCouponDiscAmountTableView(PosBindUseCoupon posBindUseCoupon, ObservableList<TableViewDto> foodTableViewItems) {
        Map<String, Object> result = new HashedMap();
        String msg = "";
        Integer code = -1;
        BigDecimal disAmt = BigDecimal.ZERO;
        String couponType = posBindUseCoupon.getCouponType();
        if (AppUtils.isNotBlank(couponType)) {
            BigDecimal sumCostAmt = bigDecimalByPloy(new BigDecimal(foodTableViewItems.stream().mapToDouble(w -> w.getPosOrderDiscDto().getCostAmt().doubleValue()).sum()));
            if (AppUtils.isNotBlank(foodTableViewItems)) {
                for (TableViewDto tableViewDto : foodTableViewItems) {
                    PosOrderDiscDto orderDisc = tableViewDto.getPosOrderDiscDto();
                    BigDecimal singleDiscAmt = BigDecimal.ZERO;
                    if (couponType.equals(CouponTypeEnum.CASH.getCode())) {
                        //現金券  计算公式(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))/sum(amt-ifnull(item_disc,0.00)-ifnull(cat_disc))*50
                        BigDecimal cashDis = bigDecimalByPloy(divide(orderDisc.getCostAmt(), sumCostAmt).multiply(posBindUseCoupon.getFaceAmt()));
                        tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), cashDis));
                        disAmt = disAmt.add(cashDis);
                        singleDiscAmt = singleDiscAmt.add(cashDis);

                        code = 1;
                    } else if (couponType.equals(CouponTypeEnum.GIFT.getCode())) {
                        //禮品券
                        if (AppUtils.isNotBlank(posBindUseCoupon.getRemark3())) {
                            if (orderDisc.getItemCode().equals(posBindUseCoupon.getRemark3()) && AppUtils.isNotBlank(posBindUseCoupon.getDiscAmt())) {
                                //針對單個商品打折
                                BigDecimal aDouble = new BigDecimal(100.00);
                                BigDecimal dis = aDouble.subtract(posBindUseCoupon.getDiscAmt());
                                BigDecimal discost = orderDisc.getCostAmt().multiply(dis.multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                                tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                                disAmt = disAmt.add(discost);
                                singleDiscAmt = singleDiscAmt.add(discost);
                                code = 1;
                            }
                        } else {
                            code = -1;
                            msg = Main.languageMap.get("coupon.couponTypeError1");
                            break;
                        }
                    } else if (couponType.equals(CouponTypeEnum.DISCOUNT.getCode())) {
                        //折扣券
                        BigDecimal discost = orderDisc.getCostAmt().multiply(posBindUseCoupon.getDiscAmt().multiply(new BigDecimal(0.01))).setScale(4, BigDecimal.ROUND_HALF_UP);
                        tableViewDto.getPosOrderDiscDto().setItemDisc(DecimalUtil.add(orderDisc.getItemDisc(), discost));
                        disAmt = disAmt.add(discost);
                        singleDiscAmt = singleDiscAmt.add(discost);
                        code = 1;
                    }
                    tableViewDto.getPosOrderDiscDto().setCostAmt(DecimalUtil.subtract(orderDisc.getCostAmt(), singleDiscAmt));
                }
            }
        } else {
            msg = Main.languageMap.get("coupon.couponTypeError2");
            code = -1;
        }
        result.put("msg", msg);
        result.put("disAmt", disAmt);
        result.put("code", code);
        return result;
    }

}
