package com.tj24.appmanager.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.tj24.appmanager.R;
import com.tj24.appmanager.common.Const;
import com.tj24.appmanager.common.keepAlive.ScreenManager;
import com.tj24.appmanager.common.keepAlive.ScreenReceiverUtil;
import com.tj24.appmanager.login.UserHelper;
import com.tj24.appmanager.model.CloudModel;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.Sputil;

public class AliveService extends Service {

    public static final String TAG = AliveService.class.getSimpleName();
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 移除"1像素"
            mScreenManager.finishActivity();

            //开启扫描service
            ScanTopService.startSkanTopService(AliveService.this,true);
        }
        @Override
        public void onSreenOff() {
            mScreenManager.startActivity();
            //关闭扫描service
            ScanTopService.stopTopScanService();

            //检查是否要自动备份 必须登录，自动备份开启并且距离上次备份超过一天则备份
            testAutoUpload();
        }
        @Override
        public void onUserPresent() {
            //解锁
        }
    };

    public IBinder onBind(Intent intent) {
        return null;
    }

    public  static void startAliveService(Context mContext){
        Intent intent=new Intent(mContext,AliveService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent);
        }else {
            mContext.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "app";
            String CHANNEL_NAME = "app";
            channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
        startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        AliveService.startAliveService(this);
        mScreenListener.stopScreenReceiverListener();
        super.onDestroy();
    }


    /**
     * 检测自动备份
     */
    private void testAutoUpload() {
        if(UserHelper.getCurrentUser()!=null){
            if(Sputil.read(getString(R.string.app_sp_auto_upload),true)){
                if(System.currentTimeMillis() - (Sputil.read(Const.SP_LAST_UPDATE, 0L)) >24*3600*1000){
                    new CloudModel(this).readyPush(true);
                    LogUtil.i(TAG,"自动备份开始！");
                }
            }
        }
    }
}
