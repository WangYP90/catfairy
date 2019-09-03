package com.tj24.appmanager.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.tj24.appmanager.R;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.Version;

public class LoginActivity extends BaseActivity {
    private static final String  EXTRA_HAS_NEWVERSION = "hasNewVersion";
    private static final String  EXTRA_VERSION_CONTENT = "versionContent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_login;
    }

    public static void actionStart(Activity activity, boolean hasNewVersion, Version version){
        Intent intent = new Intent(activity,LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void actionStartWithTransition(Activity activity, View view,boolean hasNewVersion, Version version){
        Intent intent = new Intent(activity,LoginActivity.class);
        activity.startActivity(intent);
    }
}
