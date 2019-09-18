package com.tj24.catfairy;


import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.tj24.base.base.app.BaseApplication;

/**
 * @Description:主app只提供一个application
 * @Createdtime:2019/3/2 22:42
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class MainApplication extends BaseApplication {
    public static final String TAG = MainApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"开始初始化application!");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
