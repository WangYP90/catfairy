package com.tj24.library_base.utils;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.core.graphics.ColorUtils;

public class ColorUtil {
    private static final String TAG = ColorUtil.class.getSimpleName();

    private static final int  IS_LIGHT = 0;
    private static final int  IS_DARK = 1;
    private static final int   LIGHTNESS_UNKNOWN = 2;

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    @CheckResult
    @ColorInt
    public static int  modifyAlpha(@ColorInt int  color, @IntRange int alpha) {
        return color&0x00ffffff|(alpha << 24);
    }

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    @CheckResult
    @ColorInt
    public static int  modifyAlpha(@ColorInt int  color,  float alpha) {
        return modifyAlpha(color, (255f * alpha));
    }


    /**
     * 判断传入的图片的颜色属于深色还是浅色。
     * @param bitmap
     * 图片的Bitmap对象。
     * @return 返回true表示图片属于深色，返回false表示图片属于浅色。
     */
    public static boolean isBitmapDark(Palette palette, Bitmap  bitmap){
        boolean isDark;
        int lightness = isDark(palette);
        if (lightness == LIGHTNESS_UNKNOWN) {
            isDark = isDark(bitmap, bitmap.getWidth() / 2, 0);
        } else {
            isDark = lightness == IS_DARK;
        }
        return isDark;
    }

    /**
     * Checks if the most populous color in the given palette is dark
     *
     *
     * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
     * guaranteed to find the most populous color.
     */
    public static int isDark(Palette palette) {
        if(palette==null) return LIGHTNESS_UNKNOWN;
        Palette.Swatch mostPopulous = getMostPopulousSwatch(palette);
        if(isDark(mostPopulous.getHsl())){
            return IS_DARK;
        }else {
            return IS_LIGHT;
        }
    }

    /**
     * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
     * with a large image!! If palette fails then check the color of the specified pixel
     */
    public static boolean isDark(Bitmap bitmap, int backupPixelXt, int backupPixelY){
        // first try palette with a small color quant size
        Palette palette = Palette.from(bitmap).maximumColorCount(3).generate();
        boolean isDark;
        if (palette.getSwatches().size() > 0) {
            isDark = isDark(palette) == IS_DARK;
        } else {
            // if palette failed, then check the color of the specified pixel
            isDark = isDark(bitmap.getPixel(backupPixelXt, backupPixelY));
        }
        return isDark;
    }

    /**
     * Convert to HSL & check that the lightness value
     */
    private static boolean isDark(@ColorInt int color){
        float [] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        return isDark(hsl);
    }

    /**
     * Check that the lightness value (0–1)
     */
    private static boolean isDark( float[] hsl) { // @Size(3)
        return hsl[2] < 0.8f;
    }

    private static Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }
}
