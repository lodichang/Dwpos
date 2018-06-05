package com.dw.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.dto.*;
import com.dw.entity.LanguageConfig;
import com.dw.entity.MemPeriod;
import com.dw.entity.PosComb;
import com.dw.entity.PosTableAction;
import com.dw.enums.NumberEnum;
import com.dw.enums.PosSettingEnum;
import com.dw.enums.ResultEnum;
import com.dw.print.SystemFun;
import com.dw.service.*;
import com.dw.util.*;
import com.dw.view.LoginView;
import com.dw.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by lodi on 2017/12/21.
 */

@Getter
@Setter
@FXMLController
public class LoginController implements Initializable {
    @Value("${STATION_ID}")
    private String stationId;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private TextField accountTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label titleLab;

    @FXML
    private FlowPane bodyFlowPane;
    @FXML
    private FlowPane titleFlowPane;
    @FXML
    private FlowPane textFlowPane;
    @FXML
    private FlowPane buttonFlowPane;
    @FXML
    private FlowPane leftFlowPane;
    @FXML
    private FlowPane rightFlowPane;

    //當前選中的文本框：“account賬號文本框  password密碼文本框”
    private String selectedTextField = "account";

    @Autowired
    private PosStaffService posStaffService;

    @Autowired
    private PosSettingService posSettingService;
    @Autowired
    private LanguageConfigService languageConfigService;
    @Autowired
    private PosOutletService posOutletService;
    @Autowired
    private PosGenphService posGenphService;
    @Autowired
    private PosGeneralService posGeneralService;
    @Autowired
    private MemPeriodService memPeriodService;
    @Autowired
    private TopButtonService topButtonService;
    @Autowired
    private PosSetmealService posSetmealService;
    @Autowired
    private PosCombService posCombService;
    @Autowired
    private PosAttHeadService posAttHeadService;


    @Autowired
    private MainView mainView;

    @Autowired
    private PosTableActionService posTableActionService;


    @FXML
    public void selectAccountAction() {
        selectedTextField = "account";
    }

    @FXML
    public void selectPasswordAction() {
        selectedTextField = "password";
    }

    public void removeText() {
        if ("account".equals(selectedTextField)) {
            if (accountTextField.getText().length() > 0) {
                accountTextField.setText(accountTextField.getText().substring(0, accountTextField.getText().length() - 1));
            }
        } else if ("password".equals(selectedTextField)) {
            if (passwordTextField.getText().length() > 0) {
                passwordTextField.setText(passwordTextField.getText().substring(0, passwordTextField.getText().length() - 1));
            }
        }
    }

    @Autowired
    private LoginView loginView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginShowView();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                //這裡是後台操作方法
                loadSystemConfig();
                return null;
            }
        };
        new Thread(task).start();


    }


    public void loginShowView() {
        loginPane.setPrefSize(Main.primaryScreenBounds.getWidth(), Main.primaryScreenBounds.getHeight());

//            FlowPane bodyFlowPane = (FlowPane) anchorPane.getChildren().get(0);
        bodyFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3);
        bodyFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5);
        bodyFlowPane.setLayoutX(Main.primaryScreenBounds.getWidth() / 3);
        bodyFlowPane.setLayoutY(Main.primaryScreenBounds.getHeight() / 20);

        //標題
//            FlowPane titleFlowPane = (FlowPane) bodyFlowPane.getChildren().get(0);
        titleFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 1.5);
        titleFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3);
        titleLab = (Label) titleFlowPane.getChildren().get(0);
        titleLab.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 1.5);
        titleLab.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3);
        titleLab.setText(Main.languageMap.get("login.title"));

        //内容部分
//            FlowPane textFlowPane = (FlowPane) bodyFlowPane.getChildren().get(1);
        textFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 8.5);
        textFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95);
        textFlowPane.setVgap(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 8.5 / 50);

        //账号栏
//            TextField accountTextField = (TextField) textFlowPane.getChildren().get(0);
        accountTextField.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 1.5);
        accountTextField.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95);
        accountTextField.setPromptText(Main.languageMap.get("login.staffcode"));
        accountTextField.requestFocus();
        accountTextField.setFocusTraversable(true);
        textFlowPane.setHgap(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 3 / 10);

        //密码输入栏
//            PasswordField passwordTextField = (PasswordField) textFlowPane.getChildren().get(1);
        passwordTextField.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 1.5);
        passwordTextField.setPromptText(Main.languageMap.get("login.staffpassword"));
        passwordTextField.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95);

        //按钮部分
//            FlowPane buttonFlowPane = (FlowPane) textFlowPane.getChildren().get(2);
        buttonFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 5);
        buttonFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95);

        //按钮左部分
//            FlowPane leftFlowPane = (FlowPane) buttonFlowPane.getChildren().get(0);
        leftFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 4);
        leftFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * 3);
        leftFlowPane.setPadding(new Insets(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 6.5 / 10, 0, Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 6.5 / 10, 0));
        leftFlowPane.setHgap(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
        leftFlowPane.setVgap(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
        for (int i = 0; i <= 10; i++) {
            Button button = new Button();
            switch (i) {
                case 0:
                    button.setText("7");
                    break;
                case 1:
                    button.setText("8");
                    break;
                case 2:
                    button.setText("9");
                    break;
                case 3:
                    button.setText("4");
                    break;
                case 4:
                    button.setText("5");
                    break;
                case 5:
                    button.setText("6");
                    break;
                case 6:
                    button.setText("1");
                    break;
                case 7:
                    button.setText("2");
                    break;
                case 8:
                    button.setText("3");
                    break;
                case 9:
                    button.setText("0");
                    break;
                case 10:
                    button.setText(".");
                    break;
            }
            button.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242);
            button.setPrefHeight(Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242);
            button.getStyleClass().add("numbtn");
            //按钮设置点击事件
            button.setOnAction(event -> {


                if ("account".equals(getSelectedTextField())) {
                    accountTextField.setText(accountTextField.getText() + button.getText());
                } else if ("password".equals(getSelectedTextField())) {
                    passwordTextField.setText(passwordTextField.getText() + button.getText());
                }
            });
            if (i == 9) {
                button.setPrefWidth((Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242) * 2 + Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
            }
            leftFlowPane.getChildren().add(button);
        }
        //按钮右部分
//            FlowPane rightFlowPane = (FlowPane) buttonFlowPane.getChildren().get(1);
        rightFlowPane.setPrefHeight(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 5);
        rightFlowPane.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 - 1);
        rightFlowPane.setPadding(new Insets(Main.primaryScreenBounds.getHeight() / 10 * 5 / 10 * 6.5 / 10, 0, 0, ((Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 - 1) - Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242) / 1.7));
        rightFlowPane.setHgap(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
        rightFlowPane.setVgap(Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
        for (int i = 0; i <= 2; i++) {
            Button button = new Button();
            if (i == NumberEnum.ZERO.getValue()) {
                button.setText("←");
                button.setOnAction(event -> {
                    removeText();
                });
            } else if (i == NumberEnum.ONE.getValue()) {
                button.setText("x");
                button.setOnAction(event -> {
                    accountTextField.setText("");
                    passwordTextField.setText("");
                    setSelectedTextField("account");

                });
            } else if (i == NumberEnum.TWO.getValue()) {
                button.setText("√");
                button.setOnAction(event -> {

                    //登錄驗證

                    String staffCode = accountTextField.getText();
                    while (staffCode.length() < 7) {
                        staffCode = "0" + staffCode;
                    }
                    accountTextField.setText(staffCode);
                    String staffCardCode = staffCode;
                    if (AppUtils.isBlank(passwordTextField.getText())) {
                        passwordTextField.requestFocus();
                        setSelectedTextField("password");
                    } else if (AppUtils.isNotBlank(staffCode) && AppUtils.isNotBlank(passwordTextField.getText())) {


                        String passPw = Security.encrypt(passwordTextField.getText());

                        PosStaffDto posStaff = posStaffService.staffLogin(staffCode, passPw, staffCardCode);

                        if (AppUtils.isNotBlank(posStaff)) {
                            //加載用戶權限
                            String finalStaffCode = staffCode;
                            Task task = new Task<Void>() {
                                @Override
                                public Void call() {
                                    //這裡是後台操作方法
                                    List<PosStaffRightDto> posStaffRightDtos = posStaffService.getStaffRightList(finalStaffCode);
                                    ObservableMap<String, String> staffRight = FXCollections.observableMap(posStaffRightDtos.stream().collect(Collectors.toMap(PosStaffRightDto::getRightKey, PosStaffRightDto::getRightValue)));
                                    Main.staffRight = staffRight;
                                    //清空该机器上的操作桌台信息
                                    deletePosTableAction(null, stationId);
                                    return null;
                                }


                            };
                            new Thread(task).start();

                            Main.posStaff = posStaff;

                            accountTextField.setText("");
                            passwordTextField.setText("");
                            MainController mainController = (MainController) mainView.getPresenter();
                            mainController.iniData();
                            Main.showInitialView(mainView.getClass());


                        } else {
                            titleLab.setText(Main.languageMap.get("login.title.error"));
                            titleLab.setStyle("-fx-text-fill: #ee4049");
                            accountTextField.setText("");
                            passwordTextField.setText("");
                            setSelectedTextField("account");

                        }


                    }
                });
            }
            button.setPrefWidth(Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242);
            button.setPrefHeight(Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242);
            // button.setPrefHeight(Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242);
            button.getStyleClass().add("zeronumbtn");
            if (i == 2) {
                button.setPrefHeight((Main.primaryScreenBounds.getWidth() / 3 * 0.95 * 0.2242) * 2 + Main.primaryScreenBounds.getWidth() / 3 * 0.95 / 4 * (0.4 / 3));
            }
            rightFlowPane.getChildren().add(button);
        }

    }

    /**
     * 加載系統參數
     */
    public void loadSystemConfig() {
        try {
            List<PosSettingDto> posSettingDtoList = posSettingService.getPosSetting();
            Main.posSetting = FXCollections.observableMap(posSettingDtoList.stream().collect(Collectors.toMap(PosSettingDto::getPosKey, PosSettingDto::getPosValue)));
            if (AppUtils.isNotBlank(Main.posSetting.get(PosSettingEnum.languagedefault.getValue()))) {
                String languageDefault = Main.posSetting.get(PosSettingEnum.languagedefault.getValue());
                List<LanguageConfig> languageConfigList = languageConfigService.getLanguageConfig(languageDefault);
                Main.languageMap = FXCollections.observableMap(languageConfigList.stream().collect(Collectors.toMap(LanguageConfig::getLangKey, LanguageConfig::getLangValue)));
                if (AppUtils.isNotBlank(Main.posSetting.get(stationId + "_PRINTSETTING"))) {
                    SystemFun systemFun = new SystemFun();
                    String value = Main.posSetting.get(stationId + "_PRINTSETTING");
                    String[] arrage = value.split(";");
                    if (arrage != null && arrage.length == 3) {
                        systemFun.setPRINTER_BAUDRATE(Integer.parseInt(arrage[0]));
                        systemFun.setENCODE_FORMAT(arrage[1]);
                        systemFun.setLOCALPORT(arrage[2]);
                        Main.systemFun = systemFun;
                    }
                    /*   SystemFun systemFun =  JSON.parseObject(value, SystemFun.class);
                     */
                }
                if (AppUtils.isNotBlank(Main.posSetting.get(PosSettingEnum.outlet.getValue()))) {

                    List<PosOutletDto> outletList = posOutletService.getOutletList(Main.posSetting.get(PosSettingEnum.outlet.getValue()));
                    if (AppUtils.isNotBlank(outletList)) {
                        Main.posOutletDto = outletList.get(0);
                    }
                    Main.outline = Main.posSetting.get("EXT_REGION_ID");
                    Main.posOutlet = Main.posSetting.get(PosSettingEnum.outlet.getValue());
                    Main.posPeriodMap = getPosPeriodByOutlet(Main.posOutlet);
                    initItemData();
                }
            } else {
                Map<String, String> resultMap = new LinkedHashMap<String, String>();
                resultMap.put("好", ResultEnum.YES.getValue());
                String result = ShowViewUtil.showWarningView("缺少參數", "提示找不到" + PosSettingEnum.languagedefault.getValue(), resultMap, null);
                if (ResultEnum.YES.getValue().equals(result)) {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, String> getPosPeriodByOutlet(String outlet) {
        //根据outlet参数获取当前时段（1-9）和当前价钱（1-9）
        StringBuffer periodString = new StringBuffer();
        String symbol = "@";
        Date stime = DateUtil.getNowTime();
        int period = 1;
        String price = "1";
        BigDecimal servPro = BigDecimal.ZERO;
        String[] prices = null;
        Map<String, String> map = new HashMap<>();


        List<PosOutletDto> posOutletDtoList = posOutletService.getOutletList(outlet);
        if (AppUtils.isNotBlank(posOutletDtoList)) {
            //通过分店编号获取时段信息,当前系统日期yyyy-MM-dd
            List<PosGenphDto> posGenphDtoList = posGenphService.getGenphList(outlet, DateUtil.getFormatDay(DateUtil.getNowTime()));
            if (AppUtils.isBlank(posGenphDtoList)) {
                //如果是null，则从general表取数据。通过分店编号和星期几来获取数据
                List<PosGeneralDto> posGeneralDtoList = posGeneralService.getGeneralList(outlet, "0" + DateUtil.getWeek(DateUtil.getNowTime()));
                if (AppUtils.isNotBlank(posGeneralDtoList)) {
                    PosGeneralDto pgeneral = posGeneralDtoList.get(0);
                    servPro = pgeneral.getService();
                    periodString.append(DateUtil.getFormatTime(pgeneral.getPeriod1())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod2())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod3())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod4())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod5())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod6())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod7())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod8())).append(symbol)
                            .append(DateUtil.getFormatTime(pgeneral.getPeriod9())).append(symbol);
                    if ((DateUtil.timeCompare(stime, pgeneral.getPeriod1()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod2()) && DateUtil.timeCompare(pgeneral.getPeriod2(), pgeneral.getPeriod1()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod1()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod2())) && !DateUtil.timeCompare(pgeneral.getPeriod2(), pgeneral.getPeriod1()))) {
                        period = 1;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod2()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod3()) && DateUtil.timeCompare(pgeneral.getPeriod3(), pgeneral.getPeriod2()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod2()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod3())) && !DateUtil.timeCompare(pgeneral.getPeriod3(), pgeneral.getPeriod2()))) {
                        period = 2;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod3()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod4()) && DateUtil.timeCompare(pgeneral.getPeriod4(), pgeneral.getPeriod3()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod3()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod4())) && !DateUtil.timeCompare(pgeneral.getPeriod4(), pgeneral.getPeriod3()))) {
                        period = 3;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod4()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod5()) && DateUtil.timeCompare(pgeneral.getPeriod5(), pgeneral.getPeriod4()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod4()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod5())) && !DateUtil.timeCompare(pgeneral.getPeriod5(), pgeneral.getPeriod4()))) {
                        period = 4;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod5()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod6()) && DateUtil.timeCompare(pgeneral.getPeriod6(), pgeneral.getPeriod5()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod5()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod6())) && !DateUtil.timeCompare(pgeneral.getPeriod6(), pgeneral.getPeriod5()))) {
                        period = 5;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod6()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod7()) && DateUtil.timeCompare(pgeneral.getPeriod7(), pgeneral.getPeriod6()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod6()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod7())) && !DateUtil.timeCompare(pgeneral.getPeriod7(), pgeneral.getPeriod6()))) {
                        period = 6;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod7()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod8()) && DateUtil.timeCompare(pgeneral.getPeriod8(), pgeneral.getPeriod7()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod7()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod8())) && !DateUtil.timeCompare(pgeneral.getPeriod8(), pgeneral.getPeriod7()))) {
                        period = 7;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod8()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod9()) && DateUtil.timeCompare(pgeneral.getPeriod9(), pgeneral.getPeriod8()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod8()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod9())) && !DateUtil.timeCompare(pgeneral.getPeriod9(), pgeneral.getPeriod8()))) {
                        period = 8;
                    } else if ((DateUtil.timeCompare(stime, pgeneral.getPeriod9()) && !DateUtil.timeCompare(stime, pgeneral.getPeriod1()) && DateUtil.timeCompare(pgeneral.getPeriod9(), pgeneral.getPeriod1()))
                            || ((DateUtil.timeCompare(stime, pgeneral.getPeriod9()) || !DateUtil.timeCompare(stime, pgeneral.getPeriod1())) && !DateUtil.timeCompare(pgeneral.getPeriod9(), pgeneral.getPeriod1()))) {
                        period = 9;
                    }

                    prices = pgeneral.getRemarks().split(",");
                    price = AppUtils.isNotBlank(prices) ? prices[period - 1] : "1";
                }
            } else {
                PosGenphDto pgen = posGenphDtoList.get(0);
                servPro = pgen.getService();
                periodString.append(DateUtil.getFormatTime(pgen.getPeriod1())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod2())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod3())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod4())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod5())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod6())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod7())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod8())).append(symbol)
                        .append(DateUtil.getFormatTime(pgen.getPeriod9())).append(symbol);

                if ((DateUtil.timeCompare(stime, pgen.getPeriod1()) && !DateUtil.timeCompare(stime, pgen.getPeriod2()) && DateUtil.timeCompare(pgen.getPeriod2(), pgen.getPeriod1()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod1()) || !DateUtil.timeCompare(stime, pgen.getPeriod2())) && !DateUtil.timeCompare(pgen.getPeriod2(), pgen.getPeriod1()))) {
                    period = 1;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod2()) && !DateUtil.timeCompare(stime, pgen.getPeriod3()) && DateUtil.timeCompare(pgen.getPeriod3(), pgen.getPeriod2()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod2()) || !DateUtil.timeCompare(stime, pgen.getPeriod3())) && !DateUtil.timeCompare(pgen.getPeriod3(), pgen.getPeriod2()))) {
                    period = 2;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod3()) && !DateUtil.timeCompare(stime, pgen.getPeriod4()) && DateUtil.timeCompare(pgen.getPeriod4(), pgen.getPeriod3()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod3()) || !DateUtil.timeCompare(stime, pgen.getPeriod4())) && !DateUtil.timeCompare(pgen.getPeriod4(), pgen.getPeriod3()))) {
                    period = 3;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod4()) && !DateUtil.timeCompare(stime, pgen.getPeriod5()) && DateUtil.timeCompare(pgen.getPeriod5(), pgen.getPeriod4()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod4()) || !DateUtil.timeCompare(stime, pgen.getPeriod5())) && !DateUtil.timeCompare(pgen.getPeriod5(), pgen.getPeriod4()))) {
                    period = 4;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod5()) && !DateUtil.timeCompare(stime, pgen.getPeriod6()) && DateUtil.timeCompare(pgen.getPeriod6(), pgen.getPeriod5()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod5()) || !DateUtil.timeCompare(stime, pgen.getPeriod6())) && !DateUtil.timeCompare(pgen.getPeriod6(), pgen.getPeriod5()))) {
                    period = 5;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod6()) && !DateUtil.timeCompare(stime, pgen.getPeriod7()) && DateUtil.timeCompare(pgen.getPeriod7(), pgen.getPeriod6()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod6()) || !DateUtil.timeCompare(stime, pgen.getPeriod7())) && !DateUtil.timeCompare(pgen.getPeriod7(), pgen.getPeriod6()))) {
                    period = 6;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod7()) && !DateUtil.timeCompare(stime, pgen.getPeriod8()) && DateUtil.timeCompare(pgen.getPeriod8(), pgen.getPeriod7()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod7()) || !DateUtil.timeCompare(stime, pgen.getPeriod8())) && !DateUtil.timeCompare(pgen.getPeriod8(), pgen.getPeriod7()))) {
                    period = 7;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod8()) && !DateUtil.timeCompare(stime, pgen.getPeriod9()) && DateUtil.timeCompare(pgen.getPeriod9(), pgen.getPeriod8()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod8()) || !DateUtil.timeCompare(stime, pgen.getPeriod9())) && !DateUtil.timeCompare(pgen.getPeriod9(), pgen.getPeriod8()))) {
                    period = 8;
                } else if ((DateUtil.timeCompare(stime, pgen.getPeriod9()) && !DateUtil.timeCompare(stime, pgen.getPeriod1()) && DateUtil.timeCompare(pgen.getPeriod9(), pgen.getPeriod1()))
                        || ((DateUtil.timeCompare(stime, pgen.getPeriod9()) || !DateUtil.timeCompare(stime, pgen.getPeriod1())) && !DateUtil.timeCompare(pgen.getPeriod9(), pgen.getPeriod1()))) {
                    period = 9;
                }

                prices = pgen.getRemarks().split(",");
                price = AppUtils.isNotBlank(prices) ? prices[period - 1] : "1";
            }

            map.put("servPro", String.valueOf(servPro));//服務費費率
            map.put("period", "" + period);//当前时段
            map.put("price", price);//当前时段对应的价钱
            map.put("periodStr", periodString.toString());//9个时段拼接的字符串
        }
        return map;
    }


    /**
     * 去除某桌台的操作记录，便于其他账号能够操作该台
     */
    public void deletePosTableAction(String tableNum, String stationId) {
        Wrapper<PosTableAction> posTableActionWrapper = new EntityWrapper<>();
        if (AppUtils.isNotBlank(tableNum)) {
            posTableActionWrapper.eq("TABLE_NUM", tableNum);
        }
        posTableActionWrapper.eq("STATION_ID", stationId);
        posTableActionService.delete(posTableActionWrapper);
    }

    /**
     * 加载一次菜品数据,写入缓存
     */
    public void initItemData() {
        Wrapper<MemPeriod> memPeriodWrapper = new EntityWrapper<>();
        memPeriodWrapper.eq("ISVALID", "Y");
        memPeriodWrapper.and("((STIME>ETIME and (STIME <= '" + DateUtil.DateToString(new Date(), "HH:mm") + "' or ETIME>'" + DateUtil.DateToString(new Date(), "HH:mm") + "')) or (STIME<ETIME and STIME<='" + DateUtil.DateToString(new Date(), "HH:mm") + "' AND ETIME > '" + DateUtil.DateToString(new Date(), "HH:mm") + "') )");
        MemPeriod memPeriod = memPeriodService.selectOne(memPeriodWrapper);

        //加载套餐组别
        posSetmealService.getSetmealList();
        //菜品数据
        topButtonService.getTopButtonList(Main.posOutlet, DateUtil.getWeek(DateUtil.getNowTime()), Main.posPeriodMap.get("price"), AppUtils.isNotBlank(memPeriod) ? memPeriod.getCode() : "ALL");
        //口味
        posAttHeadService.queryList();

        //初始化组合信息
        Wrapper<PosComb> posCombWrapper = new EntityWrapper<>();
        posCombWrapper.eq("STATUS", "1");
        posCombService.selectList(posCombWrapper);


    }

}
