package com.dw.extended;

import com.dw.Main;
import com.dw.controller.TakeOrderIndexController;
import com.dw.dto.TableViewDto;
import com.dw.enums.CalculationEnum;
import com.dw.enums.FontSizeEnum;
import com.dw.enums.HoldOnEnum;
import com.dw.enums.ResultEnum;
import com.dw.util.AppUtils;
import com.dw.util.DecimalUtil;
import com.dw.util.ShowViewUtil;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.Binding;
import java.math.BigDecimal;

/**
 * Created by li.yongliang on 2018/4/17.
 */
public class DwFoodChoosePane extends Pane {

    private TakeOrderIndexController toiController;
    private boolean isOpen = false;

    public DwFoodChoosePane(TakeOrderIndexController takeOrderIndexController, TableViewDto tableViewDto, String optionType, int colNum, TableColumn<TableViewDto, String> itemCodeCol, int rowindex) {
        toiController = takeOrderIndexController;
        //tableViewDto = tableViewDto;


        boolean flag = tableViewDto.isFlag();

        if (optionType.equals("addItems")) {
            if (colNum == 3) {
                if (AppUtils.isBlank(toiController.getSetmealGroupDtoObservableMap().get(tableViewDto.getItemCode()))) {
                    Button label = new Button();
                    label.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
                    label.setPrefWidth(itemCodeCol.getPrefWidth() * 0.6);
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-border-width: 0;-fx-background-color: #EEEEEE;");

                    label.setOnMouseClicked(event -> {
                        if (isOpen) {
//                            label.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
                            label.setAccessibleText("rightArrow");
                            toiController.openItemChooseView();
                            isOpen = false;
                        } else {
                            if (AppUtils.isNotBlank(tableViewDto.getItemAtt())) {
//                                label.setGraphic(new ImageView(AppUtils.loadImage("leftArrow.png")));
                                label.setAccessibleText("leftArrow");
                                toiController.openAttModifyView(tableViewDto);
                                isOpen = true;
                            }
                        }
                    });

                    this.getChildren().add(label);
                }
            } else if (colNum == 2) {
                Label amtLb = new DwLabel(FontSizeEnum.font14);
                amtLb.textProperty().bind(tableViewDto.amtProperty().asString());
                amtLb.setAlignment(Pos.CENTER);
                amtLb.setPrefWidth(itemCodeCol.getMinWidth());
                amtLb.setPadding(new Insets(0, 6, 0, 0));

                //綁定控制priceLb 的這個監聽
                BooleanBinding showPriceLab = new BooleanBinding() {
                    {
                        bind(tableViewDto.qtyProperty());
                    }

                    @Override
                    protected boolean computeValue() {
                        return !tableViewDto.qtyProperty().get().equals("1");
                    }
                };


                HBox hBox = new HBox();
                hBox.setPrefWidth(itemCodeCol.getPrefWidth());
                Label priceLb = new DwLabel(FontSizeEnum.font12);
                priceLb.visibleProperty().bind(showPriceLab);
                priceLb.textProperty().bind(tableViewDto.priceProperty().asString());
                priceLb.setAlignment(Pos.BOTTOM_LEFT);
                priceLb.setPrefWidth(itemCodeCol.getPrefWidth() / 2);


                BooleanBinding iconshowbind = new BooleanBinding() {
                    {
                        bind(priceLb.textProperty());
                    }

                    @Override
                    protected boolean computeValue() {
                        boolean iconshow = tableViewDto.orgPriceProperty().get() * Double.parseDouble(tableViewDto.qtyProperty().get()) > Double.parseDouble(amtLb.textProperty().get());
                        return iconshow;
                    }
                };

                ImageView disImg = new ImageView(AppUtils.loadImage("discountcn.png"));
                disImg.visibleProperty().bind(iconshowbind);
                disImg.setFitWidth(itemCodeCol.getPrefWidth() / 3);
                disImg.setFitHeight(itemCodeCol.getPrefWidth() / 3);


                hBox.getChildren().addAll(priceLb, disImg);


                BorderPane borderPane = new BorderPane();

                borderPane.setRight(amtLb);
                borderPane.setBottom(hBox);
                //未列印並且未組合才可以改價格
                if (!tableViewDto.isPrinter() && !flag) {
                    borderPane.setOnMouseClicked(event -> {
                        String result = ShowViewUtil.showNumbericKeyboard(toiController.getOrderStage(), Main.languageMap.get("tran.input")+Main.languageMap.get("global.amt"), String.valueOf(tableViewDto.getPrice()), false);
                        if (AppUtils.isNotBlank(result) && !ResultEnum.NO.getValue().equals(result)) {
                            if (AppUtils.isNotBlank(result) && Double.parseDouble(result.trim()) > 0) {
                                recalculateGoodsAmount(tableViewDto, result.trim(), CalculationEnum.UNITPRICE.getValue(), rowindex);
                            }
                        }

                    });
                }
                this.getChildren().add(borderPane);
            } else if (colNum == 1) {
                Label itemLb = new DwLabel(FontSizeEnum.font14);
                itemLb.setText(tableViewDto.getItemCode() + "" + tableViewDto.getItemName());
                itemLb.setAlignment(Pos.CENTER_LEFT);
                itemLb.setWrapText(true);
                itemLb.setPrefWidth(itemCodeCol.getMinWidth());

                Label infoLb = new DwLabel(FontSizeEnum.font12);
                if (AppUtils.isNotBlank(tableViewDto.getItemAtt()) && AppUtils.isNotBlank(tableViewDto.getItemKicMsg())) {
                    infoLb.setText(tableViewDto.getItemAtt() + "/" + tableViewDto.getItemKicMsg());
                } else {
                    infoLb.setText(tableViewDto.getItemAtt() + " " + tableViewDto.getItemKicMsg());
                }


                StringBinding attAndKicMsg = new StringBinding() {
                    {
                        bind(tableViewDto.itemAttProperty(), tableViewDto.itemKicMsgProperty());
                    }

                    @Override
                    protected String computeValue() {
                        String attmsg = "";
                        if (AppUtils.isNotBlank(tableViewDto.itemAttProperty().get()) && AppUtils.isNotBlank(tableViewDto.itemKicMsgProperty().get())) {
                            attmsg = tableViewDto.itemAttProperty().get() + "/" + tableViewDto.itemKicMsgProperty().get();
                        } else {
                            attmsg = tableViewDto.itemAttProperty().get() + " " + tableViewDto.itemKicMsgProperty().get();
                        }
                        return attmsg;
                    }
                };

                infoLb.textProperty().bind(attAndKicMsg);
                infoLb.setAlignment(Pos.BOTTOM_LEFT);
                infoLb.setWrapText(true);
                infoLb.setPrefWidth(itemCodeCol.getMinWidth() * 0.74);

                BooleanBinding handshowbind = new BooleanBinding() {
                    {
                        super.bind(tableViewDto.holdOnProperty());
                    }

                    @Override
                    protected boolean computeValue() {
                        boolean iconshow = tableViewDto.holdOnProperty().get() == HoldOnEnum.HOLDON.getValue();
                        return iconshow;
                    }
                };

                Label handLb = new DwLabel(FontSizeEnum.font12);
                handLb.visibleProperty().bind(handshowbind);
                handLb.setGraphic(new ImageView(AppUtils.loadImage("hand-green.png")));
                handLb.setAlignment(Pos.TOP_RIGHT);
                handLb.setPrefWidth(itemCodeCol.getMinWidth() * 0.12);

                Label printLb = new DwLabel(FontSizeEnum.font12);
                printLb.graphicProperty().bind(new When(tableViewDto.isPrinterProperty()).then(new ImageView(AppUtils.loadImage("print-green.png"))).otherwise(new ImageView()));
                //利用綁定屬性來刷新按鈕的值-wen.jing
                printLb.textProperty().bind(tableViewDto.urgeCountProperty());
                printLb.setPadding(new Insets(0, 10, 0, 0));
                printLb.setAlignment(Pos.TOP_RIGHT);
                printLb.setPrefWidth(itemCodeCol.getMinWidth() * 0.12);
                HBox hBox = new HBox();
                hBox.getChildren().addAll(handLb, printLb);
                BorderPane borderPane = new BorderPane();
                //显示下单时间和下单人
                if (AppUtils.isNotBlank(tableViewDto.getOrderTime()) && AppUtils.isNotBlank(tableViewDto.getStaffName())) {
                    //下单时间label
                    Label orderTimeLabel = new DwLabel(FontSizeEnum.font12);
                    orderTimeLabel.setPrefWidth((itemCodeCol.getMinWidth() - itemCodeCol.getMinWidth() * 0.12) / 2);
                    orderTimeLabel.setText(tableViewDto.getOrderTime());
                    orderTimeLabel.setAlignment(Pos.CENTER);
                    //下单员label
                    Label staffNameLabel = new DwLabel(FontSizeEnum.font12);
                    staffNameLabel.setPrefWidth((itemCodeCol.getMinWidth() - itemCodeCol.getMinWidth() * 0.12) / 2);
                    staffNameLabel.setText(tableViewDto.getStaffName());
                    staffNameLabel.setAlignment(Pos.CENTER);
                    HBox orderBox = new HBox();
                    orderBox.getChildren().addAll(orderTimeLabel, staffNameLabel);
                    borderPane.setBottom(orderBox);
                }
                borderPane.setTop(itemLb);
                borderPane.setLeft(infoLb);
                borderPane.setRight(hBox);
                this.getChildren().add(borderPane);
            } else if (colNum == 0) {
                if (AppUtils.isBlank(tableViewDto.getMealCode())) {
                    Button qtyLb = new DwButton(FontSizeEnum.font14);
                    qtyLb.textProperty().bind(tableViewDto.qtyProperty());
                    qtyLb.setPrefSize(itemCodeCol.getPrefWidth() * 0.5, itemCodeCol.getPrefWidth() * 0.6);
                    qtyLb.setStyle("-fx-border-width: 0;-fx-background-color: #EEEEEE;");
                    qtyLb.setAlignment(Pos.CENTER_LEFT);
                    qtyLb.setOnMouseClicked(event -> {
                        if (!tableViewDto.isPrinter() && !flag) {
                            String result = ShowViewUtil.showNumbericKeyboard(toiController.getOrderStage(), Main.languageMap.get("tran.input")+Main.languageMap.get("global.qty"), tableViewDto.getQty(), false);
                            if (!ResultEnum.NO.getValue().equals(result)) {
                                if (AppUtils.isNotBlank(result) && Integer.parseInt(result.trim()) > 0) {
                                    recalculateGoodsAmount(tableViewDto, result.trim(), CalculationEnum.NUMBER.getValue(), rowindex);
                                }
                            }
                        } else {
                            toiController.getFoodTableView().getSelectionModel().select(rowindex);
                            toiController.deleteItem();
                        }
                    });

                    this.getChildren().add(qtyLb);
                }
            }
        }
    }

    /**
     * 通過傳入人數修改總金額 從TakeOrderIndexController剪切至這裡 --wen.jing
     */
    public void recalculateGoodsAmount(TableViewDto tvd, String inputInfo, String type, int rowindex) {

        BigDecimal qty = new BigDecimal(tvd.getQty());
        if (type.equals(CalculationEnum.NUMBER.getValue())) {
            BigDecimal amt = new BigDecimal(tvd.getAmt());
            BigDecimal singlePrice = DecimalUtil.divide(amt, qty);
            BigDecimal attAmt = new BigDecimal(tvd.getAttAmt());
            BigDecimal singleAttPrice = DecimalUtil.divide(attAmt, qty);

            tvd.setAmt(DecimalUtil.multiply(new BigDecimal(inputInfo), singlePrice).doubleValue());
            tvd.setAttAmt(DecimalUtil.multiply(new BigDecimal(inputInfo), singleAttPrice).doubleValue());
            tvd.setQty(inputInfo);

            //  toiController.getTableViewData().set(rowindex, tvd);
        } else if (type.equals(CalculationEnum.UNITPRICE.getValue())) {
            //原來的單價
            BigDecimal price = new BigDecimal(tvd.getPrice());
            //輸入的新單價
            BigDecimal inputPrice = new BigDecimal(inputInfo);

            if (price.compareTo(inputPrice) != 0) {
                BigDecimal foodAmt = DecimalUtil.multiply(qty, inputPrice);
                BigDecimal viewAttAmt = BigDecimal.ZERO;
                String tvdAttPrices = tvd.getAttPrices();
                if (AppUtils.isNotBlank(tvdAttPrices)) {
                    String[] attPrices = tvdAttPrices.split("@");
                    String calAmount = "";
                    String calQty = "";
                    for (int i = 0; i < attPrices.length; i++) {
                        String singleAttPrice = attPrices[i];
                        String calType = attPrices[i].substring(0, 1);
                        String settingCalAmount = attPrices[i].substring(1);
                        BigDecimal attSingleAmt = BigDecimal.ZERO;
                        if (calType.equals("+")) {
                            attSingleAmt = DecimalUtil.multiply(qty, new BigDecimal(settingCalAmount));
                        } else if (calType.equals("*")) {
                            attSingleAmt = DecimalUtil.multiply(qty, DecimalUtil.multiply(new BigDecimal(settingCalAmount), inputPrice));
                        } else if (calType.equals("/")) {
                            //半份按照百分比扣減
                            if (!singleAttPrice.equals(calAmount)) {
                                attSingleAmt = DecimalUtil.subtract(BigDecimal.ZERO, DecimalUtil.multiply(new BigDecimal(settingCalAmount), foodAmt));
                                calAmount = singleAttPrice;
                            }
                        } else if (calType.equals("-")) {
                            //半份按照實際金額扣減
                            if (!singleAttPrice.equals(calQty)) {
                                attSingleAmt = DecimalUtil.subtract(BigDecimal.ZERO, DecimalUtil.multiply(qty, new BigDecimal(settingCalAmount)));
                                calQty = singleAttPrice;
                            }
                        }
                        viewAttAmt = DecimalUtil.add(viewAttAmt, attSingleAmt);
                    }
                }

                tvd.setAmt(DecimalUtil.add(foodAmt, viewAttAmt).doubleValue());
                tvd.setAttAmt(viewAttAmt.doubleValue());
                tvd.setPrice(Double.parseDouble(inputInfo));
                //       toiController.getTableViewData().set(rowindex, tvd);

            }
        }
        toiController.calculate();
    }


}
