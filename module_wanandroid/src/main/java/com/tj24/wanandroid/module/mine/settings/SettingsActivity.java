package com.tj24.wanandroid.module.mine.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;

public class SettingsActivity extends BaseWanAndroidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_settings;
    }

    public static void actionStart(Context context){
        Intent i = new Intent(context, SettingsActivity.class);
        context.startActivity(i);
    }
}
