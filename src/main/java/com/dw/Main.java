package com.dw;


import com.dw.dto.PosOutletDto;
import com.dw.dto.PosStaffDto;
import com.dw.dto.TopButtonDto;
import com.dw.entity.PosSetting;
import com.dw.extended.DwLabel;
import com.dw.extended.DwSplashScreen;
import com.dw.print.RXTXInit;
import com.dw.print.SystemFun;
import com.dw.util.AppUtils;
import com.dw.view.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;
import java.util.Map;

/**
 *
 */
@MapperScan("com.dw.mapper*")
@SpringBootApplication
@EnableCaching
public class Main extends AbstractJavaFxApplicationSupport {

    private static String selectedTextField;
    public static Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
    //系統語言包--全局
    public static ObservableMap<String, String> languageMap = FXCollections.observableHashMap();
    //系統設定--全局
    public static ObservableMap<String, String> posSetting = FXCollections.observableHashMap();
    //用戶權限-全局
    public static ObservableMap<String, String> staffRight = FXCollections.observableHashMap();
    //當前時段和時段價錢
    public static Map<String, String> posPeriodMap = FXCollections.observableHashMap();

    public static PosStaffDto posStaff;
    //分店編號
    public static String posOutlet;

    public static String outline;

    public static PosOutletDto posOutletDto;

    public static Font defaultFont = AppUtils.getDefaultFont();

    //默認一頁顯示四個區域
    public static Integer showAreasPageSize = 3;

    public static DwLabel timeLabel;

    public static SystemFun   systemFun;





    public static void main(String[] args) {

        //launch(Main.class, LoginView.class, args);
        launch(Main.class, LoginView.class,new DwSplashScreen(), args);

    }

    static {
//        RXTXInit.loadLib();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }



}
