package com.tj24.module_appmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.tj24.library_base.utils.DateUtil;
import com.tj24.library_base.utils.LogUtil;
import com.tj24.library_base.utils.StringUtil;
import com.tj24.module_appmanager.bean.MsgApk;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;
import com.tj24.module_appmanager.greendao.daohelper.MsgApkDaoHelper;

public class ApkChangeReceiver extends BroadcastReceiver {
    public static final String TAG = ApkChangeReceiver.class.getSimpleName();

    private static final String ACTION_ADD = "android.intent.action.PACKAGE_ADDED";
    private static final String ACTION_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    private static final String ACTION_REPLACED = "android.intent.action.PACKAGE_REPLACED";

    private ApkChangerListner apkChangeListner;

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        //接收安装广播
        if (intent.getAction().equals(ACTION_ADD)) {
            boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING,false);
            int uid = intent.getIntExtra(Intent.EXTRA_UID,0);
            Log.i(TAG, "install: isReplacing = " + isReplacing);
            Log.i(TAG, "install: uid = " + uid);
            if(!isReplacing){  //第一次安装
                if(apkChangeListner != null){
                    apkChangeListner.onAddApk(packageName);
                    creatMsgApk("安装",packageName);
                    LogUtil.i(TAG, "安装了:" + packageName + "包名的程序");
                }
            }

        }
        //接收卸载广播
        if (intent.getAction().equals(ACTION_REMOVED)) {
            if(apkChangeListner != null){
                apkChangeListner.onRemoveApk(packageName);
                creatMsgApk("卸载",packageName);
                LogUtil.i(TAG, "卸载了:" + packageName + "包名的程序");
            }
        }
        //替换广播
        if (intent.getAction().equals(ACTION_REPLACED)) {
            if(apkChangeListner != null) {
                apkChangeListner.onReplacedApk(packageName);
                creatMsgApk("更新",packageName);
                LogUtil.i(TAG, "更新了:" + packageName + "包名的程序");
            }
        }
    }

    /**
     * 创建消息并保存
     *
     * @param action
     */
    private void creatMsgApk(String action,String packageName) {
        MsgApk msgApk = new MsgApk();
        msgApk.setId(StringUtil.getUuid());
        msgApk.setPackageName(packageName);
        msgApk.setAppName(AppBeanDaoHelper.getInstance().queryObjById(packageName).getName());
        long timeMills = System.currentTimeMillis();
        msgApk.setCreatTimeMills(timeMills);
        msgApk.setCreatDay(DateUtil.formatLong(DateUtil.SDF_1,timeMills));
        msgApk.setCreatHour(DateUtil.formatLong(DateUtil.SDF_2,timeMills));
        msgApk.setAction(action);
        MsgApkDaoHelper.getInstance().insertObj(msgApk);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        return intentFilter;
    }

    public void register(Context context) {
        context.registerReceiver(this, getIntentFilter());
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public void setApkChangeListner(ApkChangerListner apkChangeListner){
        this.apkChangeListner = apkChangeListner;
    }

    public interface ApkChangerListner{
        void onAddApk(String packageName);
        void onRemoveApk(String packageName);
        void onReplacedApk(String packageName);
    }
}
