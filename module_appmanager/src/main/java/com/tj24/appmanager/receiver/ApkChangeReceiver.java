package com.tj24.appmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.tj24.appmanager.R;
import com.tj24.base.utils.DateUtil;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.StringUtil;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.MsgApk;
import com.tj24.appmanager.bean.event.ApkChangeEvent;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.daohelper.MsgApkDaoHelper;
import com.tj24.appmanager.model.ApkModel;
import org.greenrobot.eventbus.EventBus;

public class ApkChangeReceiver extends BroadcastReceiver {
    public static final String TAG = ApkChangeReceiver.class.getSimpleName();

    private static final String ACTION_ADD = "android.intent.action.PACKAGE_ADDED";
    private static final String ACTION_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    private static final String ACTION_REPLACED = "android.intent.action.PACKAGE_REPLACED";

    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String packageName = intent.getData().getSchemeSpecificPart();
        //接收安装广播
        if (intent.getAction().equals(ACTION_ADD)) {
            boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING,false);
            int uid = intent.getIntExtra(Intent.EXTRA_UID,0);
            Log.i(TAG, "install: isReplacing = " + isReplacing);
            Log.i(TAG, "install: uid = " + uid);
            if(!isReplacing){  //第一次安装
                creatMsgApk(context.getString(R.string.app_install),packageName);
                EventBus.getDefault().postSticky(new ApkChangeEvent(packageName,ApkChangeEvent.ACTION_ADD));
                LogUtil.i(TAG, "安装了:" + packageName + "包名的程序");
            }
        }
        //接收卸载广播
        if (intent.getAction().equals(ACTION_REMOVED)) {
            boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING,false);
            int uid = intent.getIntExtra(Intent.EXTRA_UID,0);
            if(!isReplacing){
                creatMsgApk(context.getString(R.string.app_uninstall),packageName);
                EventBus.getDefault().postSticky(new ApkChangeEvent(packageName,ApkChangeEvent.ACTION_DEL));
                LogUtil.i(TAG, "卸载了:" + packageName + "包名的程序");
            }
        }
        //替换广播
        if (intent.getAction().equals(ACTION_REPLACED)) {
            creatMsgApk(context.getString(R.string.app_replaced),packageName);
            EventBus.getDefault().postSticky(new ApkChangeEvent(packageName,ApkChangeEvent.ACTION_REPLACE));
            LogUtil.i(TAG, "更新了:" + packageName + "包名的程序");
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
        AppBean appBean = AppBeanDaoHelper.getInstance().queryObjById(packageName);
        if(appBean!=null && appBean.getName()!=null){
            msgApk.setAppName(appBean.getName());
        }else {
            PackageInfo packageInfo = null;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                AppBean bean = ApkModel.conversToAppInfo(packageInfo,context);
                msgApk.setAppName(bean.getName());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
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

}
