package com.tj24.appmanager.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.login.Version;

public class SplashActivity extends BaseActivity {

    private static final long TIME_MIN = 1500;
    private static final int  TIME_MAX = 3100;
    private static final int MSG_FORWARD = 0;
    private View mWallView;
    private TextView tvJump;
    private ImageView ivLeague;
    private boolean isLogin;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_FORWARD){
                forwardToNextActivity();
            }
        }
    };

    long enterTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        delayToForward();
        initSth();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_spanish;
    }

    private void initView() {
        mWallView = findViewById(R.id.wall);
        tvJump = findViewById(R.id.tv_jump);
        ivLeague = findViewById(R.id.iv_league);
        enterTime = System.currentTimeMillis();
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_FORWARD);
            }
        });
    }

    private void initSth() {
    }

    /**
     * 开启最大延迟跳转
     */
    private void delayToForward() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendForwardMsg();
            }
        }).start();
    }

    /**
     * 发送跳转的消息
     */
    private void sendForwardMsg(){
        long spentTime = System.currentTimeMillis() - enterTime;
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_FORWARD;
        if(spentTime <TIME_MIN){
            mHandler.sendMessageDelayed(msg,TIME_MIN - spentTime);
        }else {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 跳转逻辑
     */
    private void forwardToNextActivity() {
        if(isLogin){
            startActivity(new Intent(this,MainActivity.class));
        }else {
            if(isActive){
                LoginActivity.actionStartWithTransition(this,ivLeague,false,new Version());
            }else {
                LoginActivity.actionStart(this,false,new Version());
            }
        }
    }

    /**
     * 禁用后退键
     */
    @Override
    public void onBackPressed() {

    }
}
