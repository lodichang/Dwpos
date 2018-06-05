package com.dw.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dw.Main;
import com.dw.entity.PosPrinter;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwButton;
import com.dw.service.PosPrinterService;
import com.dw.util.AppUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;



@Getter
@Setter
@FXMLController
public class SpecifiedPrintController implements Initializable {
    @FXML
    private FlowPane specifiedFlowPane;
    @FXML
    private FlowPane topFlowPane;
    @FXML
    private FlowPane buttomFlowPane;
    private Stage printStage;
    @Autowired
    private PosPrinterService posPrinterService;
    private List<PosPrinter> posPrinters = new ArrayList<>();
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
            printStage = (Stage) specifiedFlowPane.getScene().getWindow();
                }
        );
        initData();
        couponComponent();

    }

    private void couponComponent() {
        double width = Main.primaryScreenBounds.getWidth() * 0.5;
        double height = Main.primaryScreenBounds.getHeight() * 0.5;

        specifiedFlowPane.setPrefSize(width, height);
        topFlowPane.setPrefSize(width,height*0.9);
        if(AppUtils.isNotBlank(posPrinters)){
            double rows = posPrinters.size()/3;
            double row = Math.ceil(rows);
            if(row > 0){
                for(int i=0;i<row;i++){
                    FlowPane flowPane = new FlowPane();
                    flowPane.setPrefSize(width,height*0.9/row);
                    flowPane.setPadding(new Insets(15,0,0,15));
                    topFlowPane.getChildren().add(flowPane);
                    for(int j=i*3;j<(1+i)*3;j++){
                        if(j<posPrinters.size()){
                            DwButton btn = new DwButton(FontSizeEnum.font20);
                            btn.setPrefSize((flowPane.getPrefWidth()-60)/3, (flowPane.getPrefWidth()-60)/3);
                            btn.setAlignment(Pos.CENTER_RIGHT);
                            btn.setText(posPrinters.get(j).getDesc1());
                            FlowPane margin_pane = new FlowPane();
                            margin_pane.setPrefWidth(15);
                            flowPane.getChildren().addAll(btn,margin_pane);
                        }
                    }
                }
            }
        }

        buttomFlowPane.setPrefSize(width,height*0.1);
        buttomFlowPane.setPadding(new Insets(0,18,5,width*0.7));
        DwButton closeBut = new DwButton();
        closeBut.initButton(width*0.3, height*0.1, Main.languageMap.get("global.close"), "closeBut");
        closeBut.setOnAction(event -> {
            printStage.close();
        });
        buttomFlowPane.getChildren().add(closeBut);


    }

    private void initData(){
        Wrapper<PosPrinter> posPrinterWrapper = new EntityWrapper<>();
        posPrinterWrapper.orderBy("P_CODE",true);
        posPrinters = posPrinterService.selectList(posPrinterWrapper);

    }
}
