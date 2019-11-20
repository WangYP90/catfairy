package com.tj24.appmanager.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Handler;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.appmanager.R;
import com.tj24.appmanager.activity.MainActivity;
import com.tj24.appmanager.common.Const;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;

import java.util.List;

public class BusinessModel extends BaseAppsManagerModel {
    public static final String APPCLASSIFICATIONS = "apppclassifications";
    private MaterialDialog permissionDialog;
    public BusinessModel(Activity mContext) {
        super(mContext);
    }

    /**
     * 初始化 我的应用 和 系统应用
     */
    public void initDeafultData() {
        AppClassfication system = new AppClassfication();
        system.setId(Const.CLASSFICATION_SYSTEM_ID);
        system.setName(Const.CLASSFICATION_SYSTEM_NAME);
        system.setSortName(OrderConfig.ORDER_LAST_USE);
        system.setIsDefault(true);
        system.setOrder(0);

        AppClassfication custom = new AppClassfication();
        custom.setId(Const.CLASSFICATION_CUSTOM_ID);
        custom.setName(Const.CLASSFICATION_CUSTOM_NAME);
        custom.setSortName(OrderConfig.ORDER_LAST_USE);
        custom.setIsDefault(true);
        custom.setOrder(1);

        AppClassificationDaoHelper.getInstance().insertOrReplaceObj(system);
        AppClassificationDaoHelper.getInstance().insertOrReplaceObj(custom);

    }

    /**
     * 获取所有的APP分类
     * @return
     */
    public List<AppClassfication> queryAllAppClassfications() {
        return AppClassificationDaoHelper.getInstance().queryAllAndSort();
    }


    /**
     * 刷新APP列表
     */
    public void refreshApp(Handler handler) {
        List<PackageInfo> packageInfos = ApkModel.getAllPackageInfos(mContext);
        new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.app_waiting))
                .content(mContext.getString(R.string.app_hard_scanning)).contentGravity(GravityEnum.CENTER)
                .progress(false,packageInfos.size(),true)
                .canceledOnTouchOutside(false)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MaterialDialog progressDialog = (MaterialDialog) dialog;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (progressDialog.getCurrentProgress() != progressDialog.getMaxProgress()){
                                    if(progressDialog.isCancelled()){
                                        break;
                                    }
                                    List<AppBean> appBeans = ApkModel.scanLocalInstallAppList(mContext,packageInfos,progressDialog);
                                    AppBeanDaoHelper.getInstance().insertList(appBeans);
                                    handler.sendEmptyMessage(MainActivity.MSG_REFRESH);
                                }
                                ((Activity)mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setContent(mContext.getString(R.string.app_scaned_apps_num,packageInfos.size()));
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }).start();
                    }
                }).show();
    }
}
