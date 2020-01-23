package com.tj24.appmanager.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj24.appmanager.R;
import com.tj24.appmanager.R2;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.utils.UserHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    private static final long TIME_MIN = 1500;
    private static final int TIME_MAX = 3100;
    private static final int MSG_FORWARD = 0;
    @BindView(R2.id.tv_jump)
    TextView tvJump;
    @BindView(R2.id.iv_league)
    ImageView ivLeague;

    //防止两次跳转
    boolean isJump = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_FORWARD) {
                if(!isJump){
                    isJump = true;
                    forwardToNextActivity();
                }
            }
        }
    };

    long enterTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterTime = System.currentTimeMillis();
        //开启最大延迟跳转
        mHandler.sendEmptyMessageDelayed(MSG_FORWARD, TIME_MAX);
        sendForwardMsg();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_spanish;
    }


    @OnClick(R2.id.tv_jump)
    public void onClick() {
        mHandler.sendEmptyMessage(MSG_FORWARD);
    }

    /**
     * 发送跳转的消息
     */
    private void sendForwardMsg() {
        long spentTime = System.currentTimeMillis() - enterTime;
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_FORWARD;
        if (spentTime < TIME_MIN) {
            mHandler.sendMessageDelayed(msg, TIME_MIN - spentTime);
        } else {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 跳转逻辑
     */
    private void forwardToNextActivity() {
        if (UserHelper.getCurrentUser() != null) {
            MainActivity.startMain(mActivity);
        } else {
            if (isActive) {
                LoginActivity.actionStartWithTransition(this, ivLeague);
            } else {
                LoginActivity.actionStart(this);
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
