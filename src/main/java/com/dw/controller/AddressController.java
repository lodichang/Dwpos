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
import com.sun.jndi.cosnaming.IiopUrl;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.dw.util.DateUtil.*;
import static com.dw.util.DecimalUtil.bigDecimalByPloy;
import static com.dw.util.DecimalUtil.divide;
import static com.dw.util.DecimalUtil.doubleToBigDecimalByPloy;

@Getter
@Setter
@FXMLController
public class AddressController implements Initializable {
    @FXML
    private FlowPane addressParentFlowPane;
    @FXML
    private FlowPane leftFlowPane;
    @FXML
    private FlowPane rightFlowPane;
    @FXML
    private FlowPane areaFlowPane;
    @FXML
    private FlowPane streetFlowPane;
    @FXML
    private FlowPane addressFlowPane;
    @FXML
    private FlowPane msgFlowPane;
    @FXML
    private FlowPane keyboardFlowPane;
    @FXML
    private HBox numberFlowPane;
    @FXML
    private FlowPane numberLeftFlowPane;
    @FXML
    private FlowPane numberRightFlowPane;
    @FXML
    private DwButton updateButton;
    @FXML
    private DwButton cancleButton;
    @FXML
    private DwButton inputButton;
    @FXML
    private FlowPane letterFlowPane;
    @FXML
    private FlowPane telephonePane;
    @FXML
    private DwLabel phoneLabel;
    @FXML
    private TextField telephone;


    @FXML
    private FlowPane addressPane;
    @FXML
    private DwLabel addressLabel;
    @FXML
    private TextField address;

    @FXML
    private FlowPane linkmanPane;

    @FXML
    private DwLabel linkmanLabel;
    @FXML
    private TextField linkman;
    @FXML
    private DwLabel phaseLabel;
    @FXML
    private TextField phase;

    @FXML
    private FlowPane floorPane;

    @FXML
    private DwLabel towerLabel;
    @FXML
    private TextField tower;
    @FXML
    private DwLabel floorLabel;
    @FXML
    private TextField floor;
    @FXML
    private DwLabel roomLabel;
    @FXML
    private TextField room;

    @FXML
    private FlowPane remarkPane;
    @FXML
    private DwLabel remarkLabel;
    @FXML
    private TextField remark;


    private TextField currTextField;

    private PosAreaDto currentPosAreaDto;

    private Integer totalAreaPage;

    private Integer currentAreaPage;

    private Integer areaPageSize = 5;

    private PosStreetDto currentPosStreet;

    private Integer totalStreetPage;

    private Integer streetPageSize = 8;

    private Integer currentStreetPage;

    private IiopUrl.Address currentAddress;

    private Integer totalAddressPage;

    private Integer currentAddressPage;

    private Integer addressPageSize = 11;

    @Autowired
    private TakeOrderIndexController takeOrderIndexController;

    private Stage addressStage;

    @Autowired
    private PosAreaService posAreaService;

    @Autowired
    private PosStreetService posStreetService;

    @Autowired
    private PosAddressService posAddressService;

    public List<PosAreaDto> posAreaDtos = new LinkedList<>();

    @Autowired
    private PosOrderAddressService posOrderAddressService;

    private PosOrderAddress posOrderAddress;

    private PosTran posTran;

    /**
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> addressStage = (Stage) addressParentFlowPane.getScene().getWindow());
        initView();
        initAddress();
    }

    /**
     * 加载页面
     */
    public void initView() {
        double addreddFlowPaneWidth = Main.primaryScreenBounds.getWidth() * 0.7;
        double addreddFlowPaneHeight = Main.primaryScreenBounds.getHeight() * 0.8;
        addressParentFlowPane.setPrefSize(addreddFlowPaneWidth, addreddFlowPaneHeight);
        addressParentFlowPane.setPadding(new Insets(5, 0, 0, 5));
        addressParentFlowPane.setHgap(5);
        leftFlowPane.setPrefSize((addreddFlowPaneWidth - 15) / 2, addreddFlowPaneHeight - 10);
        rightFlowPane.setPrefSize((addreddFlowPaneWidth - 15) / 2, addreddFlowPaneHeight - 10);
        leftFlowPane.setVgap(5);
        areaFlowPane.setPrefSize(leftFlowPane.getPrefWidth() - 2, leftFlowPane.getPrefHeight() / 11 * 2 - 2);
        areaFlowPane.setHgap(2);
        areaFlowPane.setVgap(2);
        areaFlowPane.getStyleClass().add("areaFlowPane");
        streetFlowPane.setPrefSize(leftFlowPane.getPrefWidth() - 2, leftFlowPane.getPrefHeight() / 11 * 3 - 2);
        streetFlowPane.setHgap(2);
        streetFlowPane.setVgap(2);
        streetFlowPane.getStyleClass().add("streetFlowPane");
        addressFlowPane.setPrefSize(leftFlowPane.getPrefWidth() - 2, leftFlowPane.getPrefHeight() / 11 * 6 - 2);
        addressFlowPane.setHgap(2);
        addressFlowPane.setVgap(2);
        addressFlowPane.getStyleClass().add("addressFlowPane");
        rightFlowPane.setVgap(5);
        msgFlowPane.setPrefSize(rightFlowPane.getPrefWidth() - 2, rightFlowPane.getPrefHeight() / 3 - 2);
        msgFlowPane.setPadding(new Insets(5, 0, 0, 0));
        msgFlowPane.setVgap(5);
        telephonePane.setPrefSize(msgFlowPane.getPrefWidth(), (msgFlowPane.getPrefHeight() - 2) / 5);
        addressPane.setPrefSize(msgFlowPane.getPrefWidth(), (msgFlowPane.getPrefHeight() - 2) / 5);
        linkmanPane.setPrefSize(msgFlowPane.getPrefWidth(), (msgFlowPane.getPrefHeight() - 2) / 5);
        floorPane.setPrefSize(msgFlowPane.getPrefWidth(), (msgFlowPane.getPrefHeight() - 2) / 5);
        remarkPane.setPrefSize(msgFlowPane.getPrefWidth(), (msgFlowPane.getPrefHeight() - 2) / 5);
        keyboardFlowPane.setPrefSize(msgFlowPane.getPrefWidth(), rightFlowPane.getPrefHeight() - msgFlowPane.getPrefHeight() - 15);
        initMsg();
        initKeyboard();
    }

    private void initMsg() {
        double labelWidth = telephonePane.getPrefWidth() / 6;
        double labelHeight = (telephonePane.getPrefHeight() - 35) / 5;
        phoneLabel.setFontSize(FontSizeEnum.font16);
        phoneLabel.setPrefSize(labelWidth - 10, labelHeight);
        phoneLabel.setTextAlignment(TextAlignment.RIGHT);
        phoneLabel.setText("電話: ");
        phoneLabel.getStyleClass().add("text-label");
        telephone.setFont(Main.defaultFont);
        telephone.getStyleClass().add("text-field");
        telephone.setPrefSize(telephonePane.getPrefWidth() / 6 * 5 - 1, labelHeight);
        telephone.setOnMousePressed(event -> {
            currTextField = telephone;
        });

        addressLabel.setFontSize(FontSizeEnum.font16);
        addressLabel.setPrefSize(labelWidth - 10, labelHeight);
        addressLabel.setText("地址: ");
        addressLabel.getStyleClass().add("text-label");
        address.setFont(Main.defaultFont);
        address.getStyleClass().add("text-field");
        address.setDisable(true);
        address.setPrefSize(addressPane.getPrefWidth() / 6 * 5 - 1, labelHeight);

        linkmanLabel.setFontSize(FontSizeEnum.font16);
        linkmanLabel.setPrefSize(labelWidth - 10, labelHeight);
        linkmanLabel.setText("姓名: ");
        linkmanLabel.getStyleClass().add("text-label");
        linkman.setFont(Main.defaultFont);
        linkman.getStyleClass().add("text-field");
        linkman.setPrefSize(linkmanPane.getPrefWidth() / 6 * 3, labelHeight);
        linkman.setOnMousePressed(event -> {
            currTextField = linkman;
        });
        phaseLabel.setFontSize(FontSizeEnum.font16);
        phaseLabel.setText("期: ");
        phaseLabel.setPrefSize(labelWidth - 3, labelHeight);
        phaseLabel.getStyleClass().add("text-label");
        phase.setFont(Main.defaultFont);
        phase.getStyleClass().add("text-field");
        phase.setPrefSize(linkmanPane.getPrefWidth() / 6, labelHeight);
        phase.setOnMousePressed(event -> {
            currTextField = phase;
        });
        //double _floorPaneTextFieldWidth = (floorPane.getPrefWidth() - labelWidth * 3 - paddingSize * 7) / 3 - 0.15;
        towerLabel.setFontSize(FontSizeEnum.font16);
        towerLabel.setText("座: ");
        towerLabel.setPrefSize(labelWidth - 10, labelHeight);
        towerLabel.getStyleClass().add("text-label");
        tower.setFont(Main.defaultFont);
        tower.getStyleClass().add("text-field");
        tower.setPrefSize(floorPane.getPrefWidth() / 6, labelHeight);
        tower.setOnMousePressed(event -> {
            currTextField = tower;
        });
        floorLabel.setFontSize(FontSizeEnum.font16);
        floorLabel.setPrefSize(labelWidth - 3, labelHeight);
        floorLabel.setText("樓: ");
        floorLabel.getStyleClass().add("text-label");
        floor.setFont(Main.defaultFont);
        floor.getStyleClass().add("text-field");
        floor.setPrefSize(floorPane.getPrefWidth() / 6, labelHeight);
        floor.setOnMousePressed(event -> {
            currTextField = floor;
        });
        roomLabel.setFontSize(FontSizeEnum.font16);
        roomLabel.setPrefSize(labelWidth - 3, labelHeight);
        roomLabel.setText("室: ");
        roomLabel.getStyleClass().add("text-label");
        room.setFont(Main.defaultFont);
        room.getStyleClass().add("text-field");
        room.setPrefSize(floorPane.getPrefWidth() / 6, labelHeight);
        room.setOnMousePressed(event -> {
            currTextField = room;
        });
        remarkLabel.setFontSize(FontSizeEnum.font16);
        remarkLabel.setPrefSize(labelWidth - 10, labelHeight);
        remarkLabel.setText("備註: ");
        remarkLabel.getStyleClass().add("text-label");
        remark.setFont(Main.defaultFont);
        remark.getStyleClass().add("text-field");
        remark.setPrefSize(addressPane.getPrefWidth() / 6 * 5 - 1, labelHeight);
        remark.setOnMousePressed(event -> {
            currTextField = remark;
        });
    }

    private void initKeyboard() {
        numberPaneComponent((keyboardFlowPane.getPrefWidth() - 2) * 0.7, keyboardFlowPane.getPrefHeight() / 2);
        rightFeaturePane((keyboardFlowPane.getPrefWidth() - 2) * 0.3, keyboardFlowPane.getPrefHeight() / 2);
        letterPaneComponent(keyboardFlowPane.getPrefWidth(), keyboardFlowPane.getPrefHeight() / 2);
    }

    public void rightFeaturePane(double width, double height) {
        numberRightFlowPane.setPrefSize(width, height);
        numberRightFlowPane.setVgap(2);
        numberRightFlowPane.getStyleClass().add("numberRightFlowPane");
        double btnHeight = (height - 2 * 2) / 4;
        //修改
        updateButton.setPrefSize(width * 0.95, btnHeight);
        updateButton.getStyleClass().add("clearAllButton");
        updateButton.setText(Main.languageMap.get("keyword.update"));
        updateButton.setOnAction(event -> currTextField.setText(""));
        //VBox.setMargin(updateButton, new Insets(2, 2, 2, 0));

        //取消
        cancleButton.setPrefSize(width * 0.95, btnHeight);
        cancleButton.getStyleClass().add("closeButton");
        cancleButton.setText(Main.languageMap.get("keyword.cancle"));
        cancleButton.setOnAction(event -> {
            addressStage.close();
        });


        //输入
        inputButton.setPrefSize(width * 0.95, btnHeight * 2);
        inputButton.getStyleClass().add("confirmButton");
        inputButton.setText(Main.languageMap.get("keyword.input"));
        inputButton.setOnAction(event -> {
            if (AppUtils.isBlank(posOrderAddress)) {
                posOrderAddress = new PosOrderAddress();
                posOrderAddress.setRefNum(posTran.getRefNum());
                posOrderAddress.setSubRef(posTran.getSubRef());
                posOrderAddress.setOutlet(posTran.getOutlet());
                posOrderAddress.setInDate(posTran.getInDate());
            }
            posOrderAddress.setTelephone(telephone.getText());
            posOrderAddress.setAddress(address.getText());
            posOrderAddress.setLinkMan(linkman.getText());
            posOrderAddress.setPhase(phase.getText());
            posOrderAddress.setTower(tower.getText());
            posOrderAddress.setFloor(floor.getText());
            posOrderAddress.setRoom(room.getText());
            posOrderAddress.setRemark(remark.getText());
            posOrderAddress.setLastUpdateNameId(Main.posStaff.getCode());
            posOrderAddressService.insertOrUpdate(posOrderAddress);
            //傳遞地址信息回點菜界面

            takeOrderIndexController.initAddress(posOrderAddress);
            addressStage.close();
        });


    }

    public void numberPaneComponent(double width, double height) {
        numberLeftFlowPane.setPrefSize(width, height);
        numberLeftFlowPane.getStyleClass().add("numberLeftFlowPane");
        numberLeftFlowPane.setHgap(2);
        numberLeftFlowPane.setVgap(2);
        double numberButtonHeight = (height - 2 * 3) / 4;
        double numberButtonWidth = (width - 2 * 3) / 3 * 0.95;

        for (int i = 0; i < 12; i++) {
            DwButton numberButton = new DwButton(FontSizeEnum.font18);
            numberButton.getStyleClass().add("numbutton");
            numberButton.setPrefSize(numberButtonWidth, numberButtonHeight);
            switch (i) {
                case 0:
                    numberButton.setText("7");
                    break;
                case 1:
                    numberButton.setText("8");
                    break;
                case 2:
                    numberButton.setText("9");
                    break;
                case 3:
                    numberButton.setText("4");
                    break;
                case 4:
                    numberButton.setText("5");
                    break;
                case 5:
                    numberButton.setText("6");
                    break;
                case 6:
                    numberButton.setText("1");
                    break;
                case 7:
                    numberButton.setText("2");
                    break;
                case 8:
                    numberButton.setText("3");
                    break;
                case 9:
                    numberButton.setText("0");
                    break;
                case 10:
                    numberButton.setText(".");
                    break;
                case 11:
                    numberButton.setText("00");
                    break;
            }
            numberButton.setOnAction(event -> {
                if (AppUtils.isNotBlank(currTextField)) {
                    currTextField.setText(currTextField.getText() + numberButton.getText());
                }
            });
            numberLeftFlowPane.getChildren().add(numberButton);
        }
    }

    public void letterPaneComponent(double width, double height) {
        letterFlowPane.setHgap(2);
        letterFlowPane.setPrefSize(width, height);
        letterFlowPane.setVgap(2);
        double letterButtonWidth = (width - 8 * 2) / 7;
        double letterButtonHeight = (height - 3 * 2) / 4;

        for (int i = 0; i < 28; i++) {
            DwButton letterButton = new DwButton(FontSizeEnum.font16);
            letterButton.setPrefWidth(letterButtonWidth);
            letterButton.setPrefHeight(letterButtonHeight);
            letterButton.getStyleClass().add("letterButton");
            switch (i) {
                case 0:
                    letterButton.setText("A");
                    break;
                case 1:
                    letterButton.setText("B");
                    break;
                case 2:
                    letterButton.setText("C");
                    break;
                case 3:
                    letterButton.setText("D");
                    break;
                case 4:
                    letterButton.setText("E");
                    break;
                case 5:
                    letterButton.setText("F");
                    break;
                case 6:
                    letterButton.setText("G");
                    break;
                case 7:
                    letterButton.setText("H");
                    break;
                case 8:
                    letterButton.setText("I");
                    break;
                case 9:
                    letterButton.setText("J");
                    break;
                case 10:
                    letterButton.setText("K");
                    break;
                case 11:
                    letterButton.setText("L");
                    break;
                case 12:
                    letterButton.setText("M");
                    break;
                case 13:
                    letterButton.setText("N");
                    break;
                case 14:
                    letterButton.setText("O");
                    break;
                case 15:
                    letterButton.setText("P");
                    break;
                case 16:
                    letterButton.setText("Q");
                    break;
                case 17:
                    letterButton.setText("R");
                    break;
                case 18:
                    letterButton.setText("S");
                    break;
                case 19:
                    letterButton.setText("T");
                    break;
                case 20:
                    letterButton.setText("U");
                    break;
                case 21:
                    letterButton.setText("V");
                    break;
                case 22:
                    letterButton.setText("W");
                    break;
                case 23:
                    letterButton.setText("X");
                    break;
                case 24:
                    letterButton.setText("Y");
                    break;
                case 25:
                    letterButton.setText("Z");
                    break;
                case 26:
                    letterButton.setText("-");
                    break;
                case 27:
                    letterButton.setText(" ");
                    break;
            }
            letterButton.setOnAction(event -> {
                if (AppUtils.isNotBlank(currTextField)) {
                    currTextField.setText(currTextField.getText() + letterButton.getText());
                }
            });
            letterFlowPane.getChildren().add(letterButton);
        }
    }

    public void initAddress() {
        Wrapper<PosArea> posAreaWrapper = new EntityWrapper<>();
        posAreaWrapper.eq("ACTIVE", "1");
        List<PosArea> posAreas = posAreaService.selectList(posAreaWrapper);
        if (AppUtils.isNotBlank(posAreas)) {
            posAreas.forEach((PosArea posArea) -> {
                PosAreaDto posAreaDto = new PosAreaDto();
                BeanUtils.copyProperties(posArea, posAreaDto);
                List<PosStreetDto> posStreetDtoList = new LinkedList<PosStreetDto>();
                Wrapper<PosStreet> posStreetWrapper = new EntityWrapper<PosStreet>();
                posStreetWrapper.eq("AREA_ID", posArea.getId());
                posStreetWrapper.eq("ACTIVE", "1");
                List<PosStreet> posStreetList = posStreetService.selectList(posStreetWrapper);
                if (AppUtils.isNotBlank(posStreetList)) {
                    posStreetList.forEach((PosStreet posStreet) -> {
                        Wrapper<PosAddress> posAddressWrapper = new EntityWrapper<PosAddress>();
                        posAddressWrapper.eq("STREET_ID", posStreet.getId());
                        posAddressWrapper.eq("ACTIVE", "1");
                        List<PosAddress> posAddressList = posAddressService.selectList(posAddressWrapper);
                        PosStreetDto posStreetDto = new PosStreetDto();
                        BeanUtils.copyProperties(posStreet, posStreetDto);
                        posStreetDto.setPosAddressList(posAddressList);
                        posStreetDtoList.add(posStreetDto);
                    });
                }
                posAreaDto.setPosStreetDtoList(posStreetDtoList);
                posAreaDtos.add(posAreaDto);
            });
        }
    }


    /**
     * 初始化页面参数
     *
     * @param posTran
     */
    public void initData(PosTran posTran) {
        this.posTran = posTran;
        Wrapper<PosOrderAddress> posOrderAddressWrapper = new EntityWrapper<>();
        posOrderAddressWrapper.eq("REF_NUM", posTran.getRefNum());
        posOrderAddressWrapper.eq("SUB_REF", posTran.getSubRef());
        posOrderAddressWrapper.eq("IN_DATE", posTran.getInDate());
        posOrderAddressWrapper.eq("OUTLET",posTran.getOutlet());
        posOrderAddress = posOrderAddressService.selectOne(posOrderAddressWrapper);
        if (AppUtils.isNotBlank(posOrderAddress)) {
            telephone.setText(posOrderAddress.getTelephone());
            address.setText(posOrderAddress.getAddress());
            linkman.setText(posOrderAddress.getLinkMan());
            phase.setText(posOrderAddress.getPhase());
            tower.setText(posOrderAddress.getTower());
            floor.setText(posOrderAddress.getFloor());
            room.setText(posOrderAddress.getRoom());
            remark.setText(posOrderAddress.getRemark());
        } else {
            telephone.setText("");
            address.setText("");
            linkman.setText("");
            phase.setText("");
            tower.setText("");
            floor.setText("");
            room.setText("");
            remark.setText("");
        }
        showAreas(1);
    }

    public void showAreas(Integer page) {
        if (AppUtils.isNotBlank(posAreaDtos)) {
            totalAreaPage = posAreaDtos.size() / areaPageSize + (posAreaDtos.size() % areaPageSize > 0 ? 1 : 0);
            Integer totalCount = page * areaPageSize;
            List<PosAreaDto> showPosAreaDto = new LinkedList<>();
            if ((page - 1) * areaPageSize < posAreaDtos.size() && posAreaDtos.size() <= page * areaPageSize) {
                areaFlowPane.getChildren().clear();
                double areaButtonWidth = (areaFlowPane.getPrefWidth() - 8) / 3;
                double areaButtonHeight = (areaFlowPane.getPrefHeight() - 2) / 2;
                for (int i = (page - 1) * areaPageSize; i < posAreaDtos.size(); i++) {
                    DwButton areaButton = new DwButton(FontSizeEnum.font12);
                    areaButton.setPrefSize(areaButtonWidth, areaButtonHeight);
                    areaButton.setText(posAreaDtos.get(i).getDesc1());
                    areaFlowPane.getChildren().add(areaButton);
                    int finalI = i;
                    int finalI1 = i;
                    int finalI2 = i;
                    areaButton.setOnAction(event -> {
                        currentPosAreaDto = posAreaDtos.get(finalI);
                        List<PosStreetDto> posStreetDtoList = posAreaDtos.get(finalI1).getPosStreetDtoList();
                        if (AppUtils.isNotBlank(posStreetDtoList)) {
                            currentPosStreet = posStreetDtoList.get(0);
                        } else {
                            currentPosStreet = null;
                            currentAddress = null;
                            streetFlowPane.getChildren().clear();
                            addressFlowPane.getChildren().clear();
                        }
                        showStreets(1, posAreaDtos.get(finalI2));
                    });
                }
                if (totalAreaPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.AREA.getValue(), totalAreaPage, page, areaButtonWidth, areaButtonHeight);
                }
                showStreets(1, posAreaDtos.get((page - 1) * areaPageSize));
                currentPosAreaDto = posAreaDtos.get((page - 1) * areaPageSize);

            } else if ((page - 1) * areaPageSize < posAreaDtos.size() && posAreaDtos.size() > page * areaPageSize) {
                areaFlowPane.getChildren().clear();
                double areaButtonWidth = (areaFlowPane.getPrefWidth() - 8) / 3;
                double areaButtonHeight = (areaFlowPane.getPrefHeight() - 2) / 2;
                for (int i = (page - 1) * areaPageSize; i < page * areaPageSize; i++) {
                    DwButton areaButton = new DwButton(FontSizeEnum.font12);
                    areaButton.setPrefSize(areaButtonWidth, areaButtonHeight);
                    areaButton.setText(posAreaDtos.get(i).getDesc1());
                    int finalI = i;
                    int finalI1 = i;
                    int finalI2 = i;
                    areaButton.setOnAction(event -> {
                        currentPosAreaDto = posAreaDtos.get(finalI);
                        List<PosStreetDto> posStreetDtoList = posAreaDtos.get(finalI1).getPosStreetDtoList();
                        if (AppUtils.isNotBlank(posStreetDtoList)) {
                            currentPosStreet = posStreetDtoList.get(0);
                        } else {
                            currentPosStreet = null;
                            currentAddress = null;
                            streetFlowPane.getChildren().clear();
                            addressFlowPane.getChildren().clear();
                        }
                        showStreets(1, posAreaDtos.get(finalI2));
                    });
                    areaFlowPane.getChildren().add(areaButton);
                }
                if (totalAreaPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.AREA.getValue(), totalAreaPage, page, areaButtonWidth, areaButtonHeight);
                }
                showStreets(1, posAreaDtos.get((page - 1) * areaPageSize));
                currentPosAreaDto = posAreaDtos.get((page - 1) * areaPageSize);
            } else {
                return;
            }
        }
    }

    public void showStreets(Integer page, PosAreaDto posAreaDto) {
        List<PosStreetDto> posStreetDtoList = posAreaDto.getPosStreetDtoList();
        if (AppUtils.isNotBlank(posStreetDtoList)) {
            totalStreetPage = posStreetDtoList.size() / streetPageSize + (posStreetDtoList.size() % streetPageSize > 0 ? 1 : 0);
            if ((page - 1) * streetPageSize < posStreetDtoList.size() && posStreetDtoList.size() <= page * streetPageSize) {
                streetFlowPane.getChildren().clear();
                double streetButtonWidth = (streetFlowPane.getPrefWidth() - 10) / 3;
                double streetButtonHeight = (streetFlowPane.getPrefHeight() - 10) / 3;
                for (int i = (page - 1) * streetPageSize; i < posStreetDtoList.size(); i++) {
                    DwButton streetButton = new DwButton(FontSizeEnum.font12);
                    streetButton.setPrefSize(streetButtonWidth, streetButtonHeight);
                    streetButton.setText(posStreetDtoList.get(i).getDesc1());
                    int finalI = i;
                    streetButton.setOnAction(event -> {
                        currentPosStreet = posStreetDtoList.get(finalI);
                        showAddresses(1, posStreetDtoList.get(finalI));
                    });
                    streetFlowPane.getChildren().add(streetButton);
                }
                if (totalStreetPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.STREET.getValue(), totalStreetPage, page, streetButtonWidth, streetButtonHeight);
                }
                showAddresses(1, posStreetDtoList.get((page - 1) * streetPageSize));
                currentPosStreet = posStreetDtoList.get((page - 1) * streetPageSize);

            } else if ((page - 1) * streetPageSize < posStreetDtoList.size() && posStreetDtoList.size() > page * streetPageSize) {
                streetFlowPane.getChildren().clear();
                double streetButtonWidth = (streetFlowPane.getPrefWidth() - 10) / 3;
                double streetButtonHeight = (streetFlowPane.getPrefHeight() - 10) / 2;
                for (int i = (page - 1) * streetPageSize; i < page * areaPageSize; i++) {
                    DwButton streetButton = new DwButton(FontSizeEnum.font12);
                    streetButton.setPrefSize(streetButtonWidth, streetButtonHeight);
                    streetButton.setText(posStreetDtoList.get(i).getDesc1());
                    int finalI = i;
                    streetButton.setOnAction(event -> {
                        currentPosStreet = posStreetDtoList.get(finalI);
                        showAddresses(1, posStreetDtoList.get((page - 1) * streetPageSize));
                    });
                    streetFlowPane.getChildren().add(streetButton);
                }
                if (totalStreetPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.STREET.getValue(), totalStreetPage, page, streetButtonWidth, streetButtonHeight);
                }
                showAddresses(1, posStreetDtoList.get((page - 1) * streetPageSize));
                currentPosStreet = posStreetDtoList.get((page - 1) * streetPageSize);
            } else {
                return;
            }
        } else {
            streetFlowPane.getChildren().clear();
            addressFlowPane.getChildren().clear();
        }
    }

    public void showAddresses(Integer page, PosStreetDto posStreetDto) {
        List<PosAddress> posAddressDtoList = posStreetDto.getPosAddressList();
        if (AppUtils.isNotBlank(posAddressDtoList)) {
            totalAddressPage = posAddressDtoList.size() / addressPageSize + (posAddressDtoList.size() % addressPageSize > 0 ? 1 : 0);
            if ((page - 1) * addressPageSize < posAddressDtoList.size() && posAddressDtoList.size() <= page * addressPageSize) {
                addressFlowPane.getChildren().clear();
                double addressButtonWidth = (addressFlowPane.getPrefWidth() - 10) / 2;
                double addressButtonHeight = (addressFlowPane.getPrefHeight() - 14) / 6;
                for (int i = (page - 1) * addressPageSize; i < posAddressDtoList.size(); i++) {
                    DwButton addressButton = new DwButton(FontSizeEnum.font12);
                    addressButton.setPrefSize(addressButtonWidth, addressButtonHeight);
                    addressButton.setText(posAddressDtoList.get(i).getDesc1());
                    addressButton.setOnAction(event -> {
                        if (AppUtils.isNotBlank(currentPosAreaDto) && AppUtils.isNotBlank(currentPosStreet)) {
                            address.setText(currentPosAreaDto.getDesc1() + " " + currentPosStreet.getDesc1() + " " + addressButton.getText());
                        } else {
                            currentAddress = null;
                            addressFlowPane.getChildren().clear();
                        }
                    });
                    addressFlowPane.getChildren().add(addressButton);
                }
                if (totalAddressPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.ADDRESS.getValue(), totalAddressPage, page, addressButtonWidth, addressButtonHeight);
                }
            } else if ((page - 1) * addressPageSize < posAddressDtoList.size() && posAddressDtoList.size() > page * addressPageSize) {
                addressFlowPane.getChildren().clear();
                double addressButtonWidth = (addressFlowPane.getPrefWidth() - 10) / 2;
                double addressButtonHeight = (addressFlowPane.getPrefHeight() - 14) / 6;
                for (int i = (page - 1) * addressPageSize; i < page * addressPageSize; i++) {
                    DwButton addressButton = new DwButton(FontSizeEnum.font12);
                    addressButton.setPrefSize(addressButtonWidth, addressButtonHeight);
                    addressButton.setText(posAddressDtoList.get(i).getDesc1());
                    addressButton.setOnAction(event -> {
                        if (AppUtils.isNotBlank(currentPosAreaDto) && AppUtils.isNotBlank(currentPosStreet)) {
                            address.setText(currentPosAreaDto.getDesc1() + " " + currentPosStreet.getDesc1() + " " + addressButton.getText());
                        } else {
                            currentAddress = null;
                            addressFlowPane.getChildren().clear();
                        }
                    });
                    addressFlowPane.getChildren().add(addressButton);
                }
                if (totalAddressPage > 1) {
                    showPageFlowPane(FlowPaneTypeEnum.ADDRESS.getValue(), totalAddressPage, page, addressButtonWidth, addressButtonHeight);
                }
            } else {
                return;
            }
        } else {
            addressFlowPane.getChildren().clear();
        }
    }


    public void showPageFlowPane(int type, Integer totalPage, Integer currentPage, double width, double height) {
        FlowPane pageFlowPane = new FlowPane();
        pageFlowPane.setPrefSize(width, height);
        DwButton previousPageButton = new DwButton(FontSizeEnum.font12);
        previousPageButton.setPrefWidth(width / 3 - 1);
        previousPageButton.setPrefHeight(height);
        previousPageButton.setGraphic(new ImageView(AppUtils.loadImage("leftArrow.png")));
        previousPageButton.setOnAction(event -> {
            if (FlowPaneTypeEnum.AREA.getValue() == type) {
                if (currentPage > 1) {
                    showAreas(currentPage - 1);
                }
            } else if (FlowPaneTypeEnum.STREET.getValue() == type) {
                if (currentPage > 1) {
                    showStreets(currentPage - 1, currentPosAreaDto);
                }
            } else if (FlowPaneTypeEnum.ADDRESS.getValue() == type) {
                if (currentPage > 1) {
                    showAddresses(currentPage - 1, currentPosStreet);
                }
            }
        });
        DwLabel pageLabel = new DwLabel();
        pageLabel.setFontSize(FontSizeEnum.font12);
        pageLabel.setPrefSize(width / 3 - 1, height);
        pageLabel.setText(currentPage + "/" + totalPage);
        pageLabel.getStyleClass().add("pageLabel");
        DwButton nextPageButton = new DwButton(FontSizeEnum.font12);
        nextPageButton.setPrefWidth(width / 3 - 1);
        nextPageButton.setPrefHeight(height);
        nextPageButton.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
        nextPageButton.setOnAction(event -> {
            if (FlowPaneTypeEnum.AREA.getValue() == type) {
                showAreas(currentPage + 1);
            } else if (FlowPaneTypeEnum.STREET.getValue() == type) {
                showStreets(currentPage + 1, currentPosAreaDto);
            } else if (FlowPaneTypeEnum.ADDRESS.getValue() == type) {
                showAddresses(currentPage + 1, currentPosStreet);
            }
        });
        pageFlowPane.getChildren().addAll(previousPageButton, pageLabel, nextPageButton);
        if (FlowPaneTypeEnum.AREA.getValue() == type) {
            areaFlowPane.getChildren().add(pageFlowPane);
        } else if (FlowPaneTypeEnum.STREET.getValue() == type) {
            streetFlowPane.getChildren().add(pageFlowPane);
        } else if (FlowPaneTypeEnum.ADDRESS.getValue() == type) {
            addressFlowPane.getChildren().add(pageFlowPane);
        }

    }


}
