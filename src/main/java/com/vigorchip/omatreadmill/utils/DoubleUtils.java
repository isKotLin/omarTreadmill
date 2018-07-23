package com.vigorchip.omatreadmill.utils;

import java.math.BigDecimal;

/**
 * Created by wr-app1 on 2018/4/25.
 * double值丢失精度
 */

public class DoubleUtils {
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

}
