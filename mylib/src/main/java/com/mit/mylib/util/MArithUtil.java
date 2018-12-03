package com.mit.mylib.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MArithUtil {
    private static final int DEF_DIV_SCALE = 10;

    private MArithUtil() {
    }

    public static double scale(double d) {
        return (new BigDecimal(d)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    //scale保留小数点后几位
    public static double scale(int scale, double d) {
        return (new BigDecimal(d)).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    //判断输入字符各种情况
    public static String scale(String str) {
        return MStringUtils.isEmpty(str)?"0":(str.equals(".")?"0":(str.startsWith(".")?"0" + str:(str.endsWith(".")?str + "0":str)));
    }

    public static double add(double d1, double d2) {
        return add(Double.toString(d1), Double.toString(d2));
    }

    public static double add(String d1, String d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }

    public static double sub(double d1, double d2) {
        return sub(Double.toString(d1), Double.toString(d2));
    }

    public static double sub(String d1, String d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double d1, double d2) {
        return mul(Double.toString(d1), Double.toString(d2));
    }

    public static double mul(String d1, String d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double d1, double d2) {
        return div(d1, d2, 10);
    }

    public static double div(double d1, double d2, boolean up) {
        return div(Double.toString(d1), Double.toString(d2), 10, up?4:1);
    }

    public static double div(double d1, double d2, int scale) {
        return div(Double.toString(d1), Double.toString(d2), scale, 4);
    }

    public static double div(String d1, String d2, int scale, int roundingMode) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(d1);
            BigDecimal b2 = new BigDecimal(d2);
            if(b2.doubleValue() == 0.0D) {
                b2 = new BigDecimal("1");
            }

            return b1.divide(b2, scale, roundingMode).doubleValue();
        }
    }
}
