package com.dw.controller;

import com.dw.Main;
import com.dw.dto.TableViewDto;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import com.dw.extended.DwLabel;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by wen.jing on 2018/5/14
 */
@Setter
@Getter
@FXMLController
public class AttModifyController implements Initializable {

    @FXML
    private VBox attModifyPane;
    @FXML
    private GridPane attListGrid;
    @FXML
    private Label checkAttPaneTitel;
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;

    private int checkAttRows = 6;
    private int checkAttCols = 2;
    double attWidth = 0;
    double attHieght = 0;
    private Stage attModify;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        attModifytComponent();
    }

    private void attModifytComponent() {

        attModifyPane.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight());
        checkAttPaneTitel.setText(Main.languageMap.get("item.att.list"));
        checkAttPaneTitel.getStyleClass().add("checkAttPaneTitel");
        checkAttPaneTitel.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight() * 0.05);
        attListGrid.setPrefSize(takeOrderIndexController.getRightFlowPaneWidth(), takeOrderIndexController.getTakeOrderIndexHeight() * 0.45);
        attListGrid.setAlignment(Pos.CENTER);
        attListGrid.getStyleClass().add("caonima");
        attWidth = takeOrderIndexController.getRightFlowPaneWidth() / 2;
        attHieght = takeOrderIndexController.getTakeOrderIndexHeight() * 0.45 / checkAttRows;
    }


    /**
     * 當點擊左側已點菜列表中的按鈕時，再右側顯示此菜品選中的口味信息。
     * <p>
     * 初始化菜品選中的口味
     * 增加一個標記參數，用來標記是否默認顯示口味的最後一頁
     */
    public void initData(TableViewDto tvd) {

        int page = 1;
        int pageCount = (tvd.getItemAttCode().split("@").length > checkAttRows * checkAttCols ? (tvd.getItemAttCode().split("@").length / (checkAttRows * checkAttCols - 1) + (tvd.getItemAttCode().split("@").length % (checkAttRows * checkAttCols - 1) > 0 ? 1 : 0)) : 1);
        itemCheckedAttPageTurn(page, pageCount, tvd);

    }


    private void itemCheckedAttPageTurn(int page, int pageCount, TableViewDto tvd) {

        attListGrid.getChildren().clear();


        String itemAttCode = tvd.getItemAttCode();
        String itemAttDesc = tvd.getItemAtt();
        String itemAttPrice = tvd.getAttPrices();
        String attSubtractQty = tvd.getAttSubtractQty();

        if (AppUtils.isNotBlank(itemAttCode)) {
            String[] itemAttCodes = itemAttCode.split("@");
            String[] itemAttDescs = itemAttDesc.split("/");
            String[] itemAttPrices = itemAttPrice.split("@");
            String[] attSubtractQtys = attSubtractQty.split("@");
            for (int i = 1; i <= checkAttRows; i++) {
                int k = checkAttCols - 1;
                for (int j = 1; j <= checkAttCols; j++) {
                    HBox hbox = new HBox();
                    hbox.setPrefSize(attWidth , attHieght);
                    hbox.setSpacing(2);
                    hbox.setStyle("-fx-border-width: 1;-fx-border-color: #d8d8d8;");

                    if (j * i == checkAttRows * checkAttCols) {
                        Label btn = new DwLabel(FontSizeEnum.font14);
                        btn.setPrefSize(attWidth, attHieght);
                        btn.setText(page + "/" + pageCount);
                        btn.setOnMouseClicked(event -> {
                            String btnText = btn.getText();
                            String[] btnTexts = btnText.split("/", -1);
                            int clickpage = Integer.parseInt(btnTexts[0]);
                            int clickpagecount = Integer.parseInt(btnTexts[1]);
                            if (clickpage == clickpagecount) {
                                clickpage = 0;
                            }
                            clickpage++;
                            btn.setText(clickpage + "/" + clickpagecount);
                            itemCheckedAttPageTurn(clickpage, clickpagecount, tvd);
                        });
                        hbox.getChildren().addAll(btn);
                    } else {
                        int attIndex = i * j + k * (i - 1) - 1 + (page - 1) * (checkAttRows * checkAttCols) - (page - 1);
                        if (attIndex < itemAttCodes.length) {
                            String itemAtt = itemAttCodes[attIndex];
                            String finalItemAttCode = StringUtils.join(ArrayUtils.remove(itemAttCodes, attIndex), "@");

                            String attDesc = itemAttDescs[attIndex];
                            String finalItemAttDesc = StringUtils.join(ArrayUtils.remove(itemAttDescs, attIndex), "/");

                            String attPrice = itemAttPrices[attIndex];
                            String[] finalItemAttPrices = ArrayUtils.remove(itemAttPrices, attIndex);
                            String finalItemAttPrice = StringUtils.join(finalItemAttPrices, "@");

                            String finalAttSubtractQty = StringUtils.join(ArrayUtils.remove(attSubtractQtys, attIndex), "@");

                            Label btn = new DwLabel(FontSizeEnum.font14);
                            btn.setPrefSize(attWidth * 0.3, attHieght);
                            btn.setAlignment(Pos.CENTER_LEFT);
                            btn.setWrapText(true);
                            btn.setText(attDesc);

                            Label qtyBtn = new DwLabel(FontSizeEnum.font14);
                            qtyBtn.setPrefSize(attWidth * 0.2, attHieght);
                            qtyBtn.setAlignment(Pos.CENTER_RIGHT);
                            qtyBtn.setWrapText(true);
                            qtyBtn.setText("1");

                            Label amtBtn = new DwLabel(FontSizeEnum.font14);
                            amtBtn.setPrefSize(attWidth * 0.2, attHieght);
                            amtBtn.setAlignment(Pos.CENTER_RIGHT);
                            amtBtn.setWrapText(true);
                            amtBtn.setText(attPrice);


                            Button delBtn = new DwButton(FontSizeEnum.font14);
                            delBtn.getStyleClass().add("delbtn");
                            delBtn.setPrefSize(attWidth * 0.3, attHieght);
                            delBtn.setWrapText(true);
                            delBtn.setText(Main.languageMap.get("tran.delete"));
                            delBtn.setOnMouseClicked(event -> {
                                if (!tvd.isPrinter()) {
                                    attListGrid.getChildren().remove(delBtn.getParent());
                                    tvd.setItemAttCode(AppUtils.isBlank(finalItemAttCode) ? finalItemAttCode : finalItemAttCode + "@");
                                    tvd.setItemAtt(AppUtils.isBlank(finalItemAttDesc) ? finalItemAttDesc : finalItemAttDesc + "/");
                                    tvd.setAttPrices(AppUtils.isBlank(finalItemAttPrice) ? finalItemAttPrice : finalItemAttPrice + "@");
                                    tvd.setAttSubtractQty(AppUtils.isBlank(finalAttSubtractQty) ? finalAttSubtractQty : finalAttSubtractQty + "@");
                                    //得到數量
                                    String qty = tvd.getQty();
                                    BigDecimal price = new BigDecimal(tvd.getPrice());

                                    BigDecimal foodAmt = DecimalUtil.multiply(new BigDecimal(qty), price);
                                    BigDecimal viewAttAmt = BigDecimal.ZERO;
                                    String calAmount = "";
                                    String calQty = "";
                                    for (int f = 0; f < finalItemAttPrices.length; f++) {
                                        String singleAttPrice = finalItemAttPrices[f];
                                        String calType = singleAttPrice.substring(0, 1);
                                        String settingCalAmount = singleAttPrice.substring(1);
                                        BigDecimal attSingleAmt = BigDecimal.ZERO;
                                        if (calType.equals("+")) {
                                            attSingleAmt = DecimalUtil.multiply(new BigDecimal(qty), new BigDecimal(settingCalAmount));
                                        } else if (calType.equals("*")) {
                                            attSingleAmt = DecimalUtil.multiply(new BigDecimal(qty), DecimalUtil.multiply(new BigDecimal(settingCalAmount), price));
                                        } else if (calType.equals("/")) {
                                            //如果是半份，则只拿倍数乘商品金額算出来半份金額，再下边再加口味數量,如果已經有半份的口味則不再處理第二次的半份。
                                            if (!singleAttPrice.equals(calAmount)) {
                                                attSingleAmt = DecimalUtil.subtract(BigDecimal.ZERO, DecimalUtil.multiply(new BigDecimal(settingCalAmount), foodAmt));
                                                calAmount = singleAttPrice;
                                            }
                                        } else if (calType.equals("-")) {
                                            //如果是半份，则只拿倍数乘商品金額算出来半份金額，再下边再加口味數量,如果已經有半份的口味則不再處理第二次的半份。
                                            if (!singleAttPrice.equals(calQty)) {
                                                attSingleAmt = DecimalUtil.subtract(BigDecimal.ZERO, DecimalUtil.multiply(new BigDecimal(qty), new BigDecimal(settingCalAmount)));
                                                calQty = singleAttPrice;
                                            }
                                        }
                                        viewAttAmt = DecimalUtil.add(viewAttAmt, attSingleAmt);
                                    }
                                    tvd.setAmt(Double.parseDouble(DecimalUtil.add(foodAmt, viewAttAmt).toString()));
                                    tvd.setAttAmt(Double.parseDouble(viewAttAmt.toString()));

                                    int pageCount2 = (tvd.getItemAttCode().split("@").length > checkAttRows * checkAttCols ? (tvd.getItemAttCode().split("@").length / (checkAttRows * checkAttCols - 1) + (tvd.getItemAttCode().split("@").length % (checkAttRows * checkAttCols - 1) > 0 ? 1 : 0)) : 1);
                                    itemCheckedAttPageTurn(page, pageCount2, tvd);

                                }
                            });

                            hbox.getChildren().addAll(btn, qtyBtn, amtBtn, delBtn);
                        }
                    }
                    k--;
                    GridPane.setMargin(hbox, new Insets(1));
                    attListGrid.add(hbox, j - 1, i - 1);
                }
            }
        }

    }


}
