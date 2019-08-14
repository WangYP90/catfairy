package com.tj24.library_base.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * @Description:字符串相关
 * @Createdtime:2019/3/3 0:23
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class StringUtil {
    /**
     * 将一段字符中的制定字符变为红色
     *
     * @param arg0 父字符串
     * @param arg1 指定子字符
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder formatStrColor(String arg0, String arg1) {
        int from = arg0.indexOf(arg1);
        int to = from + arg1.length();
        if (from >= 0 && to <= arg0.length()) {
            SpannableStringBuilder builder = new SpannableStringBuilder(arg0);
            builder.setSpan(new ForegroundColorSpan(Color.RED), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        } else {
            return null;
        }
    }

    /**
     * 生成唯一识别码
     * @return
     */
    public static String getUuid() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid;
    }
}
