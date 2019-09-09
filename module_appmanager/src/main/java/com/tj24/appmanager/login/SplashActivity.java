package com.tj24.appmanager.login;

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
import com.tj24.base.utils.LogUtil;

public class SplashActivity extends BaseActivity {

    private static final long TIME_MIN = 1500;
    private static final int  TIME_MAX = 3100;
    private static final int MSG_FORWARD = 0;
    private TextView tvJump;
    private ImageView ivLeague;
    private boolean isLogin;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_FORWARD){
                mHandler.removeMessages(MSG_FORWARD);
                forwardToNextActivity();
            }
        }
    };

    long enterTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //开启最大延迟跳转
        mHandler.sendEmptyMessageDelayed(MSG_FORWARD,TIME_MAX);
        initSth();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_spanish;
    }

    private void initView() {
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

    /**
     * 初始化需要的工作
     */
    private void initSth() {
        sendForwardMsg();
    }

    /**
     * 发送跳转的消息
     */
    private void sendForwardMsg(){
        long spentTime = System.currentTimeMillis() - enterTime;
        LogUtil.e("dd", "spentTime=" + spentTime + "---enterTime=" + enterTime);
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
        isLogin = false;
        if(isLogin){
            MainActivity.startMain(mActivity);
        }else {
            if(isActive){
                LoginActivity.actionStartWithTransition(this,ivLeague,false,new Version());
            }else {
                LoginActivity.actionStart(this,false,new Version());
            }
        }
        finish();
    }

    /**
     * 禁用后退键
     */
    @Override
    public void onBackPressed() {

    }
}
