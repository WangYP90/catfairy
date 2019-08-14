package com.tj24.library_base.utils;

import java.text.DecimalFormat;

/**
 * @Description:计算相关帮助类
 * @Createdtime:2019/3/3 0:33
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class MathUtil {

    /**
     * 两位小数
     * @param a
     * @return
     */
    public static String formatDouble(double a){
        DecimalFormat df = new DecimalFormat("#0.00");
        return  df.format(a);
    }

    /**
     * 整数
     * @param a
     * @return
     */
    public static String formatDoubleZ(double a){
        DecimalFormat df = new DecimalFormat("#");
        return  df.format(a);
    }
}
