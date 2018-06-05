package com.dw.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by lodi on 2018/1/15.
 */
public class NumberFormatUtils {
    /**
     * DecimalFormat转换最简便
     */
    public static String KeepTwoDecimalPlaces(Double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(f);
    }
}
