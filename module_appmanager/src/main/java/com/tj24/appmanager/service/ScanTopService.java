package com.tj24.appmanager.service;

import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.appmanager.R;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.ToastUtil;

public class ScanTopService extends IntentService {
    private static final String TAG = ScanTopService.class.getSimpleName();
    public static final long TOPSERVICE_CIRCLE = 2000; //扫描周期
    public static MaterialDialog permissionDialog;
    private static  boolean isScan = true;
    private AppBean appBean;
    public ScanTopService( ) {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (isScan){
            if (intent != null && ApkModel.getTopApp(this)!=null) {
                appBean = ApkModel.getTopApp(this);
                if(appBean!=null){
                    appBean.setTopProcessTime(appBean.getTopProcessTime()+TOPSERVICE_CIRCLE);
                    AppBeanDaoHelper.getInstance().insertOrReplaceObj(appBean);
                    try {
                        Thread.sleep(TOPSERVICE_CIRCLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 确保有查看其他应用的使用情况的权限 并开启扫描时长的service
     * @param mContext
     * @param isScreenOnStart  是否是接收到亮屏广播而来 。若是，则不弹出引导权限的dialog，
     *                          引导权限的dialog只在首页弹出 和 设置页面手动开启时弹出
     */
    public static void startSkanTopService(final Context mContext, boolean isScreenOnStart) {
        if(ApkModel.isUseGranted()){
            isScan = true;
            Intent intent=new Intent(mContext,ScanTopService.class);
            mContext.startService(intent);
            LogUtil.i(TAG,"ScanTopService已经开启！");
            ToastUtil.showShortToast(mContext,mContext.getString(R.string.app_granted_permission));
        }else if(!isScreenOnStart){
            permissionDialog = new MaterialDialog.Builder(mContext)
                    .content(mContext.getString(R.string.app_open_permission))
                    .positiveText(mContext.getString(R.string.app_confirm))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent i = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                    }).canceledOnTouchOutside(true)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * 停止扫描
     */
    public static void stopTopScanService(){
        isScan = false;
        LogUtil.i(TAG,"ScanTopService已经关闭！");
    }
}
