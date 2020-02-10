package com.tj24.wanandroid;

import android.util.Log;

import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.base.app.IApplication;

public class WanAndroidApp extends BaseApplication implements IApplication {
    public static final String TAG = WanAndroidApp.class.getSimpleName();

    @Override
    public void onCreat(BaseApplication application) {
        Log.e(TAG,"开始初始化WanAndroidApp!");
    }
}
