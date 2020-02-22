package com.tj24.appmanager.util;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.tj24.appmanager.R;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.OSUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtils {
    private static final String TAG = ViewUtils.class.getSimpleName();
    @SuppressWarnings("ResourceType")
    /**
     * 测量view
     */
    public static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    /**
     * 设置Toolbar上的图标颜色。
     * @param isDark
     * true表示设置成深色，false表示设置成浅色。
     */
    public  static void  setToolbarIconColor(AppCompatActivity activity, Toolbar toolbar , Boolean isDark ) {
        try {
            // change back button color.
            int color;
            if (isDark) {
                color = ContextCompat.getColor(activity, R.color.base_black_666);
            } else {
                color = ContextCompat.getColor(activity, R.color.base_white_text);
            }
            Drawable backArrow = ContextCompat.getDrawable(activity, R.drawable.abc_ic_ab_back_material);
           if(backArrow!=null){
               backArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
           }
            ActionBar  actionBar = activity.getSupportActionBar();
            if(actionBar !=null){
                actionBar.setHomeAsUpIndicator(backArrow);
            }
            // change overflow button color.
            Drawable drawable = toolbar.getOverflowIcon();
            if (drawable != null) {
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), color);
                toolbar.setOverflowIcon(drawable);
            }
            // change title text color.
            toolbar.setTitleTextColor(color);
        } catch (Exception e) {
            LogUtil.e(TAG,e.toString());
        }
    }

    public static void setLightStatusBar(Window window,View view) {
        if (OSUtils.isMiUI8OrLower()) {
            setMiUIStatusBarLightMode(window, true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = view.getSystemUiVisibility();
                flags = flags|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
            }
        }
    }

    public static  void clearLightStatusBar( Window window, View view) {
        if (OSUtils.isMiUI8OrLower()) {
            setMiUIStatusBarLightMode(window, false);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = view.getSystemUiVisibility();
                flags = flags & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
            }
        }
    }

    public static boolean  viewsIntersect(View view1,View view2){
        if (view1 == null || view2 == null) return false;
        int [] view1Loc = new int [2];
        view1.getLocationOnScreen(view1Loc);
        Rect view1Rect = new Rect(view1Loc[0],
                view1Loc[1],
                view1Loc[0] + view1.getWidth(),
                view1Loc[1] + view1.getHeight());
        int [] view2Loc = new int [2];
        view2.getLocationOnScreen(view2Loc);
        Rect view2Rect = new Rect(view2Loc[0],
                view2Loc[1],
                view2Loc[0] + view2.getWidth(),
                view2Loc[1] + view2.getHeight());
        return view1Rect.intersect(view2Rect);
    }

    /**
     * 设置小米手机状态栏字体图标颜色模式，需要MIUI 6以上系统才支持。
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     */
    public static void setMiUIStatusBarLightMode(Window window,Boolean dark) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager.LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int  darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.class,Integer.class);
                if (dark) { // 将状态栏字体和图标设成黑色
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else { // 将状态栏字体设成原色
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch ( Exception e) {
               LogUtil.e(TAG,e.toString());
            }

        }
    }
}
