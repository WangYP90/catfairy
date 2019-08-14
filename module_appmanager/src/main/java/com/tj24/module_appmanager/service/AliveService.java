package com.tj24.module_appmanager.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.tj24.module_appmanager.common.keepAlive.ScreenManager;
import com.tj24.module_appmanager.common.keepAlive.ScreenReceiverUtil;

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
            ScanTopService.startSkanTopService(AliveService.this);
        }
        @Override
        public void onSreenOff() {
            mScreenManager.startActivity();

            //关闭扫描service
            ScanTopService.stopTopScanService();
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
}
