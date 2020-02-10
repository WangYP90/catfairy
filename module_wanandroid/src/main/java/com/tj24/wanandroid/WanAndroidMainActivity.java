package com.tj24.wanandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tj24.base.constant.ARouterPath;

@Route(path = ARouterPath.WanAndroid.MAIN_ACTIVITY)
public class WanAndroidMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wan_android_main);
    }
}
