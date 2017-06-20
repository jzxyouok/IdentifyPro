package com.bobao.identifypro.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/7/17.
 */
public class BigDecimalUtils {

    /**
     * 相减
     * return 四舍五入
     */
    public static double sub(double minuend, double... subtrahends) {
        BigDecimal result = new BigDecimal(minuend);
        for (double subtrahend:subtrahends){
            BigDecimal temp = new BigDecimal(subtrahend);
            result = result.subtract(temp);
        }
        return result.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 相乘
     */
    public static double mul(double v1, double v2) {
        BigDecimal a = new BigDecimal(v1);
        BigDecimal b = new BigDecimal(v2);
        BigDecimal c = a.multiply(b);
        return c.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 相加
     */
    public static double add(double v1, double v2) {
        BigDecimal a = new BigDecimal(v1);
        BigDecimal b = new BigDecimal(v2);
        BigDecimal c = a.add(b);
        return c.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
