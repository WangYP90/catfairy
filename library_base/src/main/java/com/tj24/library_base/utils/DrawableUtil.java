package com.tj24.library_base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * @Description:drawable util
 * @Createdtime:2019/3/17 22:32
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class DrawableUtil {
    /**
     * 将drawable转换为bitmap
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable){
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config;
        if (drawable.getOpacity() != PixelFormat.OPAQUE){
            config = Bitmap.Config.ARGB_8888;
        }  else {
            config = Bitmap.Config.RGB_565;
        }
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
