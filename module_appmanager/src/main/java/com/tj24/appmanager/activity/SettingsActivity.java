package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.appmanager.R;
import com.tj24.appmanager.fragment.MainSettingsFragment;
import com.tj24.appmanager.login.LoginInterceptorCallBack;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.constant.ARouterPath;
import com.tj24.base.utils.UserHelper;

public class SettingsActivity extends BaseActivity {

    private static final String SETTINGS_TYPE = "settingsType";
    public static int SETTINGS_MAIN = 1;
    public static int SETTINGS_CLOUD = 2;

    private FrameLayout fragmentContainer;

    private Fragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        gotoSettings();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_settings;
    }

    private void initView() {
        fragmentContainer = findViewById(R.id.fragment_container);
        setTitle("设置");
    }

    private void gotoSettings() {
        int settingsType = getIntent().getIntExtra(SETTINGS_TYPE,SETTINGS_MAIN);
        if(settingsType == SETTINGS_MAIN){
            mFragment = new MainSettingsFragment();
        }else if(settingsType == SETTINGS_CLOUD){
           if(UserHelper.getCurrentUser() == null){
               LoginInterceptorCallBack.interruptToLogin(mActivity);
           }else {
               mFragment = (Fragment) ARouter.getInstance().build(ARouterPath.AppManager.CLOUD_SETTING_FRAGMENT)
                       .navigation(mActivity,new LoginInterceptorCallBack(mActivity));
           }
        }

        if (mFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount == 1){
            finish();
        }else {
            if (backStackEntryCount > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context, int type){
        Intent i = new Intent(context,SettingsActivity.class);
        i.putExtra(SETTINGS_TYPE,type);
        context.startActivity(i);
    }
}
