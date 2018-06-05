package com.dw.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.CouponGetDto;
import com.dw.dto.MemberTranDto;
import com.dw.dto.PosOrderDiscDto;
import com.dw.entity.PosLog;
import com.dw.entity.PosTran;
import com.dw.enums.LogTypeEnum;
import com.dw.enums.ResultEnum;
import com.dw.enums.TranTypeEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwCouponPane;
import com.dw.extended.DwLabel;
import com.dw.model.CouponParm;
import com.dw.model.MemberDetail;
import com.dw.service.PosLogService;
import com.dw.service.PosOrderService;
import com.dw.util.AppUtils;
import com.dw.util.DateUtil;
import com.dw.util.HttpClientUtil;
import com.dw.util.ShowViewUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import static com.dw.util.DateUtil.CM_DATE_FORMAT;
import static com.dw.util.DateUtil.covertToDateString;


@Getter
@Setter
@FXMLController
public class MemberController implements Initializable {
    @FXML
    private FlowPane memberFlowPane;
    @FXML
    private FlowPane topFlowPane1;
    @FXML
    private FlowPane topFlowPane2;
    @FXML
    private TableView memberTableView;
    @FXML
    private HBox buttomFlowPane;
    @FXML
    private DwLabel cardnoLabel;
    @FXML
    private DwLabel nameLabel;
    @FXML
    private DwLabel telLabel;
    @FXML
    private DwLabel birthLabel;
    @FXML
    private DwLabel amtLabel;
    @FXML
    private DwLabel gitAmtLabel;
    @FXML
    private DwLabel integralLabel;
    @FXML
    private DwLabel expireDateLabel;

    @FXML
    private DwLabel cardnoValue;
    @FXML
    private DwLabel nameValue;
    @FXML
    private DwLabel telValue;
    @FXML
    private DwLabel birthValue;
    @FXML
    private DwLabel amtValue;
    @FXML
    private DwLabel gitAmtValue;
    @FXML
    private DwLabel integralValue;
    @FXML
    private DwLabel expireDateValue;

    @FXML
    private FlowPane buttomFlowPane_left;
    @FXML
    private FlowPane buttomFlowPane_center;
    @FXML
    private FlowPane buttomFlowPane_right;

    private MemberDetail memberDetail = new MemberDetail();

    private TableColumn<MemberTranDto, String> indateCol;
    private TableColumn<MemberTranDto, String> refNumCol;
    private TableColumn<MemberTranDto, String> typeCol;
    private TableColumn<MemberTranDto, String> outletCol;
    private TableColumn<MemberTranDto, String> payAmtCol;
    private TableColumn<MemberTranDto, String> gitAmtCol;
    private TableColumn<MemberTranDto, String> integralCol;

    private ObservableList<MemberTranDto> memberTranDtos = FXCollections.observableArrayList();

    private TableColumn<CouponGetDto, String> couponTypeCol;
    private TableColumn<CouponGetDto, String> couponTitleCol;
    private TableColumn<CouponGetDto, String> couponEndTimeCol;
    private TableColumn<CouponGetDto, String> couponRateCol;
    private TableColumn<CouponGetDto, String> couponOperateCol;
    private TableColumn<CouponGetDto, String> couponNumCol;

    private ObservableList<CouponGetDto> couponGetDtoList = FXCollections.observableArrayList();

    private CouponParm couponParm = new CouponParm();

    @Autowired
    private PosOrderService posOrderService;
    //账单明细
    private List<PosOrderDiscDto> orderList = new ArrayList<>();
    //账单食品总费用
    private BigDecimal orderAmt = BigDecimal.ZERO;
    @Autowired
    private PosLogService posLogService;


    /*@FXML
    private Button closeBut;*/


    private Stage memberStage;


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            memberStage = (Stage) topFlowPane1.getScene().getWindow();
                }
        );
        couponComponent();
        memberTableComponent();

    }

    private void couponComponent() {
        double width = Main.primaryScreenBounds.getWidth() * 0.8;
        double height = Main.primaryScreenBounds.getHeight() * 0.8;

        memberFlowPane.setPrefSize(width, height);

        topFlowPane1.setPrefSize(width,height*0.05);
        topFlowPane2.setPrefSize(width,height*0.05);
        memberTableView.setPrefSize(width,height*0.7);
        buttomFlowPane.setPrefSize(width,height*0.2);

        FlowPane cardnoPane = new FlowPane();
        cardnoPane.setPrefSize(width / 4,height*0.05);
        topFlowPane1.getChildren().add(cardnoPane);
        cardnoLabel.setText(Main.languageMap.get("member.cardno"));
        cardnoValue.textProperty().bind(memberDetail.cardnoProperty());
        cardnoPane.getChildren().add(cardnoLabel);
        cardnoPane.getChildren().add(cardnoValue);

        FlowPane namePane = new FlowPane();
        namePane.setPrefSize(width / 4,height*0.05);
        topFlowPane1.getChildren().add(namePane);
        nameLabel.setText(Main.languageMap.get("member.name"));
        nameValue.textProperty().bind(memberDetail.nameProperty());
        namePane.getChildren().add(nameLabel);
        namePane.getChildren().add(nameValue);

        FlowPane telPane = new FlowPane();
        telPane.setPrefSize(width / 4,height*0.05);
        topFlowPane1.getChildren().add(telPane);
        telLabel.setText(Main.languageMap.get("member.tel"));
        telValue.textProperty().bind(memberDetail.telProperty());
        telPane.getChildren().add(telLabel);
        telPane.getChildren().add(telValue);

        FlowPane birthPane = new FlowPane();
        birthPane.setPrefSize(width / 4,height*0.05);
        topFlowPane1.getChildren().add(birthPane);
        birthLabel.setText(Main.languageMap.get("member.birth"));
        birthValue.textProperty().bind(memberDetail.birthProperty());
        birthPane.getChildren().add(birthLabel);
        birthPane.getChildren().add(birthValue);

        FlowPane amtPane = new FlowPane();
        amtPane.setPrefSize(width / 4,height*0.05);
        topFlowPane2.getChildren().add(amtPane);
        amtLabel.setText(Main.languageMap.get("member.amt"));
        amtValue.textProperty().bind(memberDetail.balanceProperty());
        amtPane.getChildren().add(amtLabel);
        amtPane.getChildren().add(amtValue);
        FlowPane gitAmtPane = new FlowPane();
        gitAmtPane.setPrefSize(width / 4,height*0.05);
        topFlowPane2.getChildren().add(gitAmtPane);
        gitAmtLabel.setText(Main.languageMap.get("member.gitAmt"));
        gitAmtValue.textProperty().bind(memberDetail.gitBalanceProperty());
        gitAmtPane.getChildren().add(gitAmtLabel);
        gitAmtPane.getChildren().add(gitAmtValue);
        FlowPane integralPane = new FlowPane();
        integralPane.setPrefSize(width / 4,height*0.05);
        topFlowPane2.getChildren().add(integralPane);
        integralLabel.setText(Main.languageMap.get("member.integral"));
        integralValue.textProperty().bind(memberDetail.integralProperty());
        integralPane.getChildren().add(integralLabel);
        integralPane.getChildren().add(integralValue);
        FlowPane expireDatePane = new FlowPane();
        expireDatePane.setPrefSize(width / 4,height*0.05);
        topFlowPane2.getChildren().add(expireDatePane);
        expireDateLabel.setText(Main.languageMap.get("member.expireDate"));
        expireDateValue.textProperty().bind(memberDetail.clostDateProperty());
        expireDatePane.getChildren().add(expireDateLabel);
        expireDatePane.getChildren().add(expireDateValue);


        buttomFlowPane_left.setPrefSize(width / 6 * 4,height*0.2);

        double buttonWidth = width / 6 - 8;
        double buttonHeight = height*0.2*0.5;

        DwButton searchBut = new DwButton();
        searchBut.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.searchBut"), "");
        searchBut.setOnAction(event -> {
            searchMember();
        });
        buttomFlowPane_left.getChildren().add(searchBut);
        DwButton cancelCoupon = new DwButton();
        cancelCoupon.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.cancelCoupon"), "");
        cancelCoupon.setOnAction(event -> {
            cancelCouponFun();
        });


        buttomFlowPane_left.getChildren().add(cancelCoupon);
        DwButton cancelBind = new DwButton();
        cancelBind.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.cancelBind"), "");
        cancelBind.setOnAction(event -> {
            bindFun("cancelBind");
        });

        buttomFlowPane_left.getChildren().add(cancelBind);
        DwButton cancelExchange = new DwButton();
        cancelExchange.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.cancelExchange"), "");
        buttomFlowPane_left.getChildren().add(cancelExchange);


        DwButton coupon = new DwButton();
        coupon.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.coupon"), "");
        coupon.setOnAction(event -> {
            couponView();
        });
        buttomFlowPane_left.getChildren().add(coupon);

        DwButton bind = new DwButton();
        bind.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.bind"), "");
        bind.setOnAction(event -> {
            bindFun("bind");
        });
        buttomFlowPane_left.getChildren().add(bind);
        DwButton exchange = new DwButton();
        exchange.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.exchange"), "");
        buttomFlowPane_left.getChildren().add(exchange);
        DwButton print = new DwButton();
        print.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.print"), "");
        buttomFlowPane_left.getChildren().add(print);

        FlowPane.setMargin(searchBut, new Insets(2));
        FlowPane.setMargin(cancelCoupon, new Insets(2));
        FlowPane.setMargin(cancelBind, new Insets(2));
        FlowPane.setMargin(cancelExchange, new Insets(2));
        FlowPane.setMargin(coupon, new Insets(2));
        FlowPane.setMargin(bind, new Insets(2));
        FlowPane.setMargin(exchange, new Insets(2));
        FlowPane.setMargin(print, new Insets(2));


        buttomFlowPane_center.setPrefSize(width / 6,height*0.2);
        DwButton closeBut = new DwButton();
        closeBut.initButton(buttonWidth, buttonHeight*2, Main.languageMap.get("global.close"), "closeBut");
        closeBut.setOnAction(event -> {
            memberStage.close();
            clearFun();
        });
        buttomFlowPane_center.getChildren().add(closeBut);
        FlowPane.setMargin(closeBut, new Insets(1));

        buttomFlowPane_right.setPrefSize(width / 6,height*0.2);

        DwButton pre = new DwButton();
        pre.initButton(buttonWidth-1, buttonHeight, Main.languageMap.get("member.pre"), "");
        pre.setOnAction(event -> {
            /*int rowIndex = memberTableView.getSelectionModel().getSelectedIndex();
            rowIndex = rowIndex>0?rowIndex:0;
            memberTableView.getSelectionModel().select(rowIndex);*/

        });
        buttomFlowPane_right.getChildren().add(pre);
        DwButton next = new DwButton();
        next.initButton(buttonWidth, buttonHeight, Main.languageMap.get("member.next"), "");
        next.setOnAction(event -> {

        });
        buttomFlowPane_right.getChildren().add(next);
        FlowPane.setMargin(pre, new Insets(1));
        FlowPane.setMargin(next, new Insets(1));





    }

    private void memberTableComponent(){
        indateCol = new TableColumn<>();
        indateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillDate()));

        refNumCol = new TableColumn<>();
        refNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRefnum()));

        typeCol = new TableColumn<>();
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTranType()));

        outletCol = new TableColumn<>();
        outletCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOutlet()));

        payAmtCol = new TableColumn<>();
        payAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillBalance().toString()));

        gitAmtCol = new TableColumn<>();
        gitAmtCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillGiftBalance().toString()));

        integralCol = new TableColumn<>();
        integralCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillIntegral()+""));

        memberTableView.getColumns().addAll(indateCol,refNumCol,typeCol,outletCol,payAmtCol,gitAmtCol,integralCol);

        double colWidth = memberTableView.getPrefWidth()/7;
        indateCol.setPrefWidth(colWidth);
        indateCol.setText(Main.languageMap.get("member.indateCol"));
        refNumCol.setPrefWidth(colWidth);
        refNumCol.setText(Main.languageMap.get("member.refNumCol"));
        typeCol.setPrefWidth(colWidth);
        typeCol.setText(Main.languageMap.get("member.typeCol"));
        outletCol.setPrefWidth(colWidth);
        outletCol.setText(Main.languageMap.get("member.outletCol"));
        payAmtCol.setPrefWidth(colWidth);
        payAmtCol.setText(Main.languageMap.get("member.payAmtCol"));
        gitAmtCol.setPrefWidth(colWidth);
        gitAmtCol.setText(Main.languageMap.get("member.gitAmtCol"));
        integralCol.setPrefWidth(colWidth);
        integralCol.setText(Main.languageMap.get("member.integralCol"));

    }

    private void couponTableComponent(){
        couponTypeCol = new TableColumn<>();
        couponTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        couponTitleCol = new TableColumn<>();
        couponTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        couponEndTimeCol = new TableColumn<>();
        couponEndTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
        couponRateCol = new TableColumn<>();
        couponRateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRate()));
        couponOperateCol = new TableColumn<>();
        couponOperateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));
        couponNumCol = new TableColumn<>();
        couponNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCouponNum()));
        if(memberTableView.getColumns().size() != 6){
            //将couponTable转成memberTable
            List<CouponGetDto> clearlist = new ArrayList<>();
            couponGetDtoList = FXCollections.observableArrayList(clearlist);
            memberTableView.setItems(couponGetDtoList);
            memberTableView.getColumns().remove(0,memberTableView.getColumns().size());
            memberTableView.getColumns().addAll(couponTypeCol,couponTitleCol,couponNumCol,couponEndTimeCol,couponRateCol,couponOperateCol);
        }


        double colWidth = memberTableView.getPrefWidth()/6;
        couponTypeCol.setPrefWidth(colWidth);
        couponTypeCol.setText(Main.languageMap.get("member.couponTypeCol"));
        couponTitleCol.setPrefWidth(colWidth);
        couponTitleCol.setText(Main.languageMap.get("member.couponTitleCol"));
        couponEndTimeCol.setPrefWidth(colWidth);
        couponEndTimeCol.setText(Main.languageMap.get("member.couponEndTimeCol"));
        couponRateCol.setPrefWidth(colWidth);
        couponRateCol.setText(Main.languageMap.get("member.couponRateCol"));
        couponOperateCol.setPrefWidth(colWidth);
        couponOperateCol.setText(Main.languageMap.get("member.couponOperate"));
        couponNumCol.setPrefWidth(colWidth);
        couponNumCol.setText(Main.languageMap.get("member.couponNum"));
        double couponOperateButton = 35;
        couponOperateCol.setCellFactory(new Callback<TableColumn<CouponGetDto, String>, TableCell<CouponGetDto, String>>() {
            @Override
            public TableCell<CouponGetDto, String> call( // 单元格内容
                                                         TableColumn<CouponGetDto, String> arg0) {
                return new TableCell<CouponGetDto, String>() {
                    @Override
                    protected void updateItem(final String str, boolean arg1) {
                        super.updateItem(str, arg1);
                        if (arg1) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(new DwCouponPane(MemberController.this,colWidth-10,couponOperateButton,memberDetail.getCardno(),memberStage,this.getTableView().getItems().get(this.getIndex()),couponParm,orderList));
                        }
                    }
                };
            }
        });

        //禁用排序
        couponTypeCol.setSortable(false);
        couponTitleCol.setSortable(false);
        couponEndTimeCol.setSortable(false);
        couponRateCol.setSortable(false);
        couponOperateCol.setSortable(false);



    }

    public void initData(PosTran posTran) {
        couponParm.setCouponParm(posTran.getTableNum(), posTran.getRefNum(), posTran.getSubRef(), posTran.getTIndex(),posTran.getInTime());

        //账单明细
        orderList = posOrderService.getAvailableOrderList(Main.posSetting.get("outlet"),couponParm.getRefNum(),couponParm.getSubRef(), DateUtil.DateToString(new Date(),CM_DATE_FORMAT));
        //计算出食品费
        if(AppUtils.isNotBlank(orderList)) {
            orderAmt = new BigDecimal(orderList.stream().mapToDouble(w -> w.getAmt().doubleValue()).sum());
        }
    }


    public void searchMember(){
        String cardNo = memberDetail.getCardno();
        if(AppUtils.isNotBlank(cardNo)){
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("member.useCouponTip3"), resultMap, memberStage);
            if(AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())){
                cardNo = ShowViewUtil.showKeyword(Main.getStage(),"");
            }else{
                search(cardNo);
            }
            if(AppUtils.isNotBlank(cardNo) && result.equals(ResultEnum.YES.getValue())){
                search(cardNo);
            }
        }else{
            cardNo = ShowViewUtil.showKeyword(Main.getStage(),"");
            if(AppUtils.isNotBlank(cardNo) && !cardNo.equals(ResultEnum.NO.getValue())){
                search(cardNo);
            }else{
                showDialog(Main.languageMap.get("member.enterCardError1"));
            }
        }
    }

    public void search(String cardNo){
        if(memberTableView.getColumns().size() != 7){
            //将couponTable转成memberTable
            List<MemberTranDto> clearlist = new ArrayList<>();
            memberTranDtos = FXCollections.observableArrayList(clearlist);
            memberTableView.setItems(memberTranDtos);
            memberTableView.getColumns().remove(0,memberTableView.getColumns().size());
            memberTableView.getColumns().addAll(indateCol,refNumCol,typeCol,outletCol,payAmtCol,gitAmtCol,integralCol);
        }
        //調用接口/api/pospay/payChecked
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("outline", Main.outline));
        params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
        params.add(new BasicNameValuePair("channel", "HTML"));
        params.add(new BasicNameValuePair("cardno", cardNo));
        String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("apiPayChecked");
        try {
            String responseStr = HttpClientUtil.post(url, params);
            JSONObject resultJson = JSONObject.parseObject(responseStr);
            if(AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")){
                String data = resultJson.get("data").toString();
                if(AppUtils.isNotBlank(data)){
                    JSONObject dataJson = JSONObject.parseObject(data);
                    if(AppUtils.isNotBlank(dataJson.get("memCard"))){
                        JSONObject memCardJson = JSONObject.parseObject(dataJson.get("memCard").toString());
                        String cardno = memCardJson.get("cardno").toString();
                        String integral = memCardJson.get("integral").toString();
                        String closedt = memCardJson.get("closedt").toString();
                        String balance =  memCardJson.get("balance").toString();
                        String giftBalance =  memCardJson.get("giftBalance").toString();
                        memberDetail.setCardno(cardno);
                        memberDetail.setClostDate(covertToDateString(closedt));
                        memberDetail.setBalance(balance);
                        memberDetail.setGitBalance(giftBalance);
                        memberDetail.setIntegral(integral);
                    }
                    if(AppUtils.isNotBlank(dataJson.get("memCust"))){
                        JSONObject memCustJson = JSONObject.parseObject(dataJson.get("memCust").toString());
                        String custna = memCustJson.get("custna").toString();
                        String mobile = memCustJson.get("mobile").toString();
                        String birthMonth = memCustJson.get("birthMonth").toString();
                        memberDetail.setName(custna);
                        memberDetail.setTel(mobile);
                        memberDetail.setBirth(birthMonth);
                    }
                    if(AppUtils.isNotBlank(dataJson.get("memTran"))){
                        List<MemberTranDto> memberTrans = (List<MemberTranDto>) JSONArray.toCollection(JSONArray.fromObject(dataJson.get("memTran").toString()), MemberTranDto.class);
                        if(AppUtils.isNotBlank(memberTrans)){
                            for (MemberTranDto tranDto:memberTrans){
                                tranDto.setBillDate(covertToDateString(tranDto.getBillDate()));
                                tranDto.setTranType(LogTypeEnum.getTypeByValue(tranDto.getTranType()));
                            }
                            memberTranDtos = FXCollections.observableArrayList(memberTrans);
                            memberTableView.setItems(memberTranDtos);
                        }else{
                            showDialog(Main.languageMap.get("member.useTranTip1"));
                            List<MemberTranDto> clearlist = new ArrayList<>();
                            memberTranDtos = FXCollections.observableArrayList(clearlist);
                            memberTableView.setItems(memberTranDtos);
                        }
                    }
                }else{
                    showDialog(Main.languageMap.get("network.refused"));
                }
            }else{
                showDialog(resultJson.get("msg").toString());
            }
        }catch (Exception e){
            showDialog(Main.languageMap.get("network.refused"));
            e.printStackTrace();
            throw e;
        }
    }
    public void couponView(){
        String cardno = memberDetail.getCardno();
        if(AppUtils.isBlank(cardno)){
            String showResult = showDialog(Main.languageMap.get("member.enterCardError1"));
            if(showResult.equals(ResultEnum.YES.getValue())){
                cardno = ShowViewUtil.showKeyword(Main.getStage(),"");
            }
        }else{
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("member.useCouponTip3"), resultMap, memberStage);
            if(AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())){
                cardno = ShowViewUtil.showKeyword(Main.getStage(),"");
            }
        }
        if(AppUtils.isNotBlank(cardno) && !cardno.equals(ResultEnum.NO.getValue())){
            memberDetail.setCardno(cardno);
            unuseCoupon(cardno);
        }
    }

    private void unuseCoupon(String cardno){
        couponTableComponent();
        initCouponTableData(cardno);

    }

    public void initCouponTableData(String cardno){
        //調用接口/api/coupon/unuse
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("channel", "HTML"));
        params.add(new BasicNameValuePair("cardNo", cardno));
        String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("coupon_unuse");
        try {
            String responseStr = HttpClientUtil.post(url, params);
            JSONObject resultJson = JSONObject.parseObject(responseStr);
            if(AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")){
                String data = resultJson.get("data").toString();
                if(AppUtils.isNotBlank(data)){
                    //會員所擁有的券列表
                    List<CouponGetDto> couponGetDtos = (List<CouponGetDto>) JSONArray.toCollection(JSONArray.fromObject(data), CouponGetDto.class);
                    if(AppUtils.isNotBlank(couponGetDtos)){
                        for(CouponGetDto dto:couponGetDtos){
                            if(AppUtils.isNotBlank(dto.getEndTime()))
                                dto.setEndTime(dto.getEndTime().substring(0,9));
                        }
                        couponGetDtoList = FXCollections.observableArrayList(couponGetDtos);
                        memberTableView.setItems(couponGetDtoList);
                    }else{
                        showDialog(Main.languageMap.get("member.useCouponTip4"));
                        List<CouponGetDto> clearlist = new ArrayList<>();
                        couponGetDtoList = FXCollections.observableArrayList(clearlist);
                        memberTableView.setItems(couponGetDtoList);
                    }
                }else{
                    showDialog(Main.languageMap.get("network.refused"));
                }
            }else{
                clearMember();
                List<CouponGetDto> clearlist = new ArrayList<>();
                couponGetDtoList = FXCollections.observableArrayList(clearlist);
                memberTableView.setItems(couponGetDtoList);
                showDialog(resultJson.get("msg").toString());
            }
        }catch (Exception e){
            showDialog(Main.languageMap.get("network.refused"));
            e.printStackTrace();
            throw e;
        }
    }

    private void cancelCouponFun(){
        String couponCode = ShowViewUtil.showKeyword(Main.getStage(),"");
        if(AppUtils.isNotBlank(couponCode) && !couponCode.equals(ResultEnum.NO.getValue())){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("couponNum",couponCode));
            params.add(new BasicNameValuePair("channel", "HTML"));
            String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("cancelCoupon");
            try {
                String responseStr = HttpClientUtil.post(url, params);
                JSONObject resultJson = JSONObject.parseObject(responseStr);
                if (resultJson.get("code").toString().equals("1")) {
                    //取消成功,把Log表數據更新
                    showDialog(Main.languageMap.get("coupon.cancelTip2"));
                    Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                    posLogWrapper.eq("REMARK1",couponCode);
                    PosLog posLog = posLogService.selectOne(posLogWrapper);
                    posLogService.updateCouponType(TranTypeEnum.V.getValue(),posLog.getId());
                    initCouponTableData(memberDetail.getCardno());
                } else {
                    //取消用券失敗
                    showDialog(resultJson.get("msg").toString());
                }
            } catch (Exception e) {
                showDialog(Main.languageMap.get("network.refused"));
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void bindFun(String type){
        String cardNo = memberDetail.getCardno();
        if(AppUtils.isNotBlank(cardNo)){
            Map<String, String> resultMap = new LinkedHashMap<String, String>();
            resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
            resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
            String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("member.useCouponTip3"), resultMap, memberStage);
            if(AppUtils.isNotBlank(result) && result.equals(ResultEnum.YES.getValue())){
                cardNo = ShowViewUtil.showKeyword(Main.getStage(),"");
            }else{
                if(type.equals("bind")){
                    bind(cardNo);
                }else{
                    cancelBind(cardNo);
                }
            }
            if(AppUtils.isNotBlank(cardNo) && result.equals(ResultEnum.YES.getValue())){
                if(type.equals("bind")){
                    bind(cardNo);
                }else{
                    cancelBind(cardNo);
                }
            }
        }else{
            cardNo = ShowViewUtil.showKeyword(Main.getStage(),"");
            if(AppUtils.isNotBlank(cardNo) && !cardNo.equals(ResultEnum.NO.getValue())){
                if(type.equals("bind")){
                    bind(cardNo);
                }else{
                    cancelBind(cardNo);
                }
            }
        }
    }
    private void bind(String cardno){
        //調用接口/api/pospay/payChecked
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("outline", Main.outline));
        params.add(new BasicNameValuePair("outlet", Main.posSetting.get("outlet")));
        params.add(new BasicNameValuePair("channel", "HTML"));
        params.add(new BasicNameValuePair("cardno", cardno));
        String url = Main.posSetting.get("apiUrl")+Main.posSetting.get("apiPayChecked");
        try {
            String responseStr = HttpClientUtil.post(url, params);
            JSONObject resultJson = JSONObject.parseObject(responseStr);
            if(AppUtils.isNotBlank(resultJson.get("code")) && resultJson.get("code").toString().equals("1")){
                String data = resultJson.get("data").toString();
                if(AppUtils.isNotBlank(data)){
                    JSONObject dataJson = JSONObject.parseObject(data);
                    if(AppUtils.isNotBlank(dataJson.get("memCard"))){
                        JSONObject memCardJson = JSONObject.parseObject(dataJson.get("memCard").toString());
                        memberDetail.setCardno(memCardJson.get("cardno").toString());
                        memberDetail.setClostDate(covertToDateString(memCardJson.get("closedt").toString()));
                        memberDetail.setBalance(memCardJson.get("balance").toString());
                        memberDetail.setGitBalance(memCardJson.get("giftBalance").toString());
                        memberDetail.setIntegral(memCardJson.get("integral").toString());
                    }
                    if(AppUtils.isNotBlank(dataJson.get("memCust"))){
                        JSONObject memCustJson = JSONObject.parseObject(dataJson.get("memCust").toString());
                        memberDetail.setName(memCustJson.get("custna").toString());
                        memberDetail.setTel(memCustJson.get("mobile").toString());
                        memberDetail.setBirth(memCustJson.get("birthMonth").toString());
                    }
                    Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
                    posLogWrapper.eq("LOG_TYPE",LogTypeEnum.BIND.getValue());
                    posLogWrapper.eq("TYPE",TranTypeEnum.N.getValue());
                    posLogWrapper.eq("REMARK1",cardno);
                    PosLog bindLog = posLogService.selectOne(posLogWrapper);
                    if(AppUtils.isNotBlank(bindLog)){
                        Map<String, String> resultMap = new LinkedHashMap<String, String>();
                        resultMap.put(Main.languageMap.get("popups.yes"), ResultEnum.YES.getValue());
                        resultMap.put(Main.languageMap.get("popups.no"), ResultEnum.NO.getValue());
                        String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), Main.languageMap.get("member.bindTip1"), resultMap, memberStage);
                        if(result.equals(ResultEnum.YES.getValue())){
                            bindLog.setType(TranTypeEnum.V.getValue());
                            posLogService.updateById(bindLog);

                            PosLog posLog = new PosLog();
                            posLog.setOutlet(Main.posSetting.get("outlet"));
                            posLog.setRefNum(couponParm.getRefNum());
                            posLog.setSubRef(couponParm.getSubRef());
                            posLog.setTIndex(couponParm.gettIndex().equals("null")?0:Long.parseLong(couponParm.gettIndex()));
                            posLog.setTable1(couponParm.getTableNo());
                            posLog.setTDate(new Date());
                            posLog.setTTime(new Date());
                            posLog.setType(TranTypeEnum.N.getValue());
                            posLog.setLogType(LogTypeEnum.BIND.getValue());
                            posLog.setStaff(Main.posStaff.getCode());
                            posLog.setRemark1(memberDetail.getCardno());
                            posLog.setRemark2(memberDetail.getTel());
                            posLog.setRemark3(memberDetail.getIntegral());
                            posLogService.insert(posLog);
                            showDialog(Main.languageMap.get("member.bindSuccess"));
                        }
                    }else{
                        PosLog posLog = new PosLog();
                        posLog.setOutlet(Main.posSetting.get("outlet"));
                        posLog.setRefNum(couponParm.getRefNum());
                        posLog.setSubRef(couponParm.getSubRef());
                        posLog.setTIndex(couponParm.gettIndex().equals("null")?0:Long.parseLong(couponParm.gettIndex()));
                        posLog.setTable1(couponParm.getTableNo());
                        posLog.setTDate(new Date());
                        posLog.setTTime(new Date());
                        posLog.setType(TranTypeEnum.N.getValue());
                        posLog.setLogType(LogTypeEnum.BIND.getValue());
                        posLog.setStaff(Main.posStaff.getCode());
                        posLog.setRemark1(memberDetail.getCardno());
                        posLog.setRemark2(memberDetail.getTel());
                        posLog.setRemark3(memberDetail.getIntegral());
                        posLogService.insert(posLog);
                        showDialog(Main.languageMap.get("member.bindSuccess"));
                    }
                }else{
                    showDialog(Main.languageMap.get("network.refused"));
                }
            }else{
                showDialog(resultJson.get("msg").toString());
            }
        }catch (Exception e){
            showDialog(Main.languageMap.get("network.refused"));
            e.printStackTrace();
            throw e;
        }
    }


    private void cancelBind(String cardno){
        Wrapper<PosLog> posLogWrapper = new EntityWrapper<>();
        posLogWrapper.eq("LOG_TYPE",LogTypeEnum.BIND.getValue());
        posLogWrapper.eq("TYPE",TranTypeEnum.N.getValue());
        posLogWrapper.eq("REMARK1",cardno);
        PosLog bindLog = posLogService.selectOne(posLogWrapper);
        if(AppUtils.isBlank(bindLog)){
            //卡未被綁定,取消失敗
            showDialog(Main.languageMap.get("member.bindTip2"));
        }else{
            bindLog.setType(TranTypeEnum.V.getValue());
            posLogService.updateById(bindLog);
            showDialog(Main.languageMap.get("member.bindTip3"));

        }
    }


    private String showDialog(String msg){
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        resultMap.put(Main.languageMap.get("popups.ok"), ResultEnum.YES.getValue());
        String result = ShowViewUtil.showWarningView(Main.languageMap.get("popups.title.prompt"), msg, resultMap, memberStage);

        return result;
    }

    private void clearFun(){
        clearMember();
        memberTableView.getColumns().remove(0,memberTableView.getColumns().size());
        memberTableView.getColumns().addAll(indateCol,refNumCol,typeCol,outletCol,payAmtCol,gitAmtCol,integralCol);

        List<MemberTranDto> clearlist = new ArrayList<>();
        memberTranDtos = FXCollections.observableArrayList(clearlist);
        memberTableView.setItems(memberTranDtos);
    }


    private void clearMember(){
        memberDetail.setCardno("");
        memberDetail.setClostDate("");
        memberDetail.setBalance("");
        memberDetail.setGitBalance("");
        memberDetail.setIntegral("");
        memberDetail.setName("");
        memberDetail.setTel("");
        memberDetail.setBirth("");
    }






}
