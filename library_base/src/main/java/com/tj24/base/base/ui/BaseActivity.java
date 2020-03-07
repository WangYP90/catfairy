package com.tj24.base.base.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tj24.base.R;
import com.tj24.base.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;

/**
 * @Description:activity 基类
 * @Createdtime:2019/3/10 19:45
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 标志位
     */
    public  String TAG = this.getClass().getSimpleName();
    /**
     * 判断当前Activity是否在前台。
     */
    public boolean isActive = false;

    /**
     * 当前Activity的实例。
     */
    public Activity mActivity;

    /**
     * Activity中显示加载等待的控件。
     */
    public ProgressBar progressBar;

    private WeakReference<Activity> weakRefActivity;

    /**
     * toolbar
     */
    public Toolbar toolbar;

    public ProgressDialog progressDialog;

    private PermissionListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        setupToolbar();

        mActivity = this;
        weakRefActivity = new WeakReference(this);
        ActivityCollector.add(weakRefActivity);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
    }

    public abstract int getLayoutId();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
        ActivityCollector.remove(weakRefActivity);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        progressBar = findViewById(R.id.loading);
    }



    /**
     * 设置toolbar
     */
    public void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 用于显示popup menu菜单的图标
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    public void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions
     * 要检查和处理的运行时权限数组
     * @param listener
     * 用于接收授权结果的监听器
     */
    public void handlePermissions(String[]permissions,  PermissionListener listener) {
        if (permissions == null || mActivity == null) {
            return;
        }
        mListener = listener;
        List<String> requestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }
        if (!requestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, requestPermissionList.toArray(new String[requestPermissionList.size()]), 1);
        } else {
            listener.onGranted();
        }
    }


    /**
     * 隐藏软键盘。
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void hideSoftKeyboard() {
        try {
            View view = getCurrentFocus();
            if (view != null) {
                IBinder binder = view.getWindowToken();
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    /**
     * 显示软键盘。
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void showSoftKeyboard(EditText editText) {
        try {
            if (editText != null) {
                editText.requestFocus();
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    public void showProgressDialog(String title,String  message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this,title,message,false,false);
        }
    }

    public void  hideProgressDialog() {
       if(progressDialog!=null && progressDialog.isShowing()){
           progressDialog.dismiss();
       }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message messageEvent) {

    }

    public void permissionsGranted() {
        // 由子类来进行具体实现
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
            if(grantResults!=null && grantResults.length>0){
               List<String> deniedPermissions = new ArrayList<>();
               for(int i=0;i<grantResults.length;i++){
                   int grantResult = grantResults[i];
                   String permission = permissions[i];
                   if(grantResult != PackageManager.PERMISSION_GRANTED){
                       deniedPermissions.add(permission);
                   }
               }
               if(deniedPermissions.isEmpty()){
                   mListener.onGranted();
               }else {
                   mListener.onDenied(deniedPermissions);
               }
            }
        }
    }

    public void showShortToast(String toast){
        ToastUtil.showShortToast(this,toast);
    }

    public void showLongToast(String toast){
        ToastUtil.showLongToast(this,toast);
    }
}