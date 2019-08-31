package com.tj24.library_base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @Description:屏幕相关
 * @Createdtime:2019/3/3 0:22
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ScreenUtil {
	public static final String TAG = ScreenUtil.class.getSimpleName();

	private static ScreenUtil instance;

	private  int height;

	private  int width;

	private Context context;

	private ScreenUtil(Context context) {
		this.context = context;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	public static ScreenUtil getInstance(Context context) {
		if (instance == null) {
			instance = new ScreenUtil(context);
		}
		return instance;
	}

	//得到手机屏幕的宽度, pix单位
	public int getScreenWidth() {
		return width;
	}

	//得到手机屏幕的G度, pix单位
	public  int getScreenHeight() {
		return height;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static float px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale + 0.5f);
	}

	/**
	 * 检测是否又刘海屏
	 *
	 * @param context
	 * @return
	 */
	public static boolean hasNotchInScreen(Context context) {
		boolean ret = false;
		try {
			ClassLoader cl = context.getClassLoader();
			Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
			Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
			ret = (boolean) get.invoke(HwNotchSizeUtil);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "hasNotchInScreen ClassNotFoundException");
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "hasNotchInScreen NoSuchMethodException");
		} catch (Exception e) {
			Log.e(TAG, "hasNotchInScreen Exception");
		} finally {
			return ret;
		}
	}

	/**
	 * 获取系统状态栏高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	/**
	 * 获取actionBar高度
	 *
	 * @param context
	 * @return
	 */
	public static int getActionBarHeight(Context context) {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}
}