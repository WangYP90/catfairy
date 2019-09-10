package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tj24.appmanager.R;
import com.tj24.base.base.ui.BaseActivity;

public class UserHomePageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.app_activity_user_home_page);
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_user_home_page;
    }

    public static void actionStart(Context context){
        Intent i = new Intent(context,UserHomePageActivity.class);
        context.startActivity(i);
    }
}
