package com.tj24.login.app;

import android.util.Log;

import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.base.app.IApplication;

public class LoginApp implements IApplication {

    public static final String TAG = LoginApp.class.getSimpleName();

    @Override
    public void onCreat(BaseApplication application) {
        Log.e(TAG,"开始初始化LoginApp!");
    }
}
