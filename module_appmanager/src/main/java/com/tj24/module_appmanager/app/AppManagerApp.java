package com.tj24.module_appmanager.app;

import android.util.Log;
import com.tj24.library_base.base.app.BaseApplication;
import com.tj24.library_base.base.app.IApplication;
import com.tj24.module_appmanager.greendao.base.AppsDaoManager;

public class AppManagerApp implements IApplication {
    public static final String TAG = AppManagerApp.class.getSimpleName();
    @Override
    public void onCreat(BaseApplication application) {
        Log.e(TAG,"开始初始化application!");

        AppsDaoManager.init();
    }
}
