package com.tj24.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:时间工具
 * @Createdtime:2019/3/3 0:32
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class DateUtil {

        public static final String SDF_1 = "yyyy-MM-dd";
        public static final String SDF_2 = "HH:mm:ss";
        public static final String SDF_3= "yyyy-MM-dd HH:mm:ss";

    /**
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        if(mss==0 || " ".equals(mss+"".trim())){
            return "0天0小时0分钟0秒";
        }
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + "天" + hours + "小时" + minutes + "分钟"
                + seconds + "秒";
    }

    /**
     * 将毫秒数转换为"yyyy-MM-dd"
     * @param mills
     * @return
     */
    public static String formatLong(String format, long mills){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(mills);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 将日期字符串转换为毫秒数
     * @param format
     * @param time
     * @return
     */
    public static long formatString(String format, String time){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long ss = -1;
        try {
            Date date = sdf.parse(time);
             ss = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ss;
    }

    /**
     * 得到当前时间的字符
     * @param format
     * @return
     */
    public static String getCurrentTime(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
}
