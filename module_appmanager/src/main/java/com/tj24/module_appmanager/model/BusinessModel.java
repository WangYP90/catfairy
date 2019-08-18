package com.tj24.module_appmanager.model;

import android.app.Activity;
import android.os.Handler;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tj24.module_appmanager.activity.MainActivity;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.common.Const;
import com.tj24.module_appmanager.common.OrderConfig;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;
import com.tj24.module_appmanager.greendao.daohelper.AppClassificationDaoHelper;

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

        AppClassificationDaoHelper.getInstance().insertObj(system);
        AppClassificationDaoHelper.getInstance().insertObj(custom);

    }

    /**
     * 获取所有的APP分类
     * @return
     */
    public List<AppClassfication> queryAllAppClassfications() {
        return AppClassificationDaoHelper.getInstance().queryAllAndSort();
    }

    /**
     * 获取APP列表 数据库查询
     * @return
     */
    public List<AppBean> getAppBeansByDb() {
        AppBeanDaoHelper.getInstance().getDao().detachAll();
        return AppBeanDaoHelper.getInstance().queryAll();
    }

    /**
     * 刷新APP列表
     */
    public void refreshApp(Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AppBean> appBeans = ApkModel.scanLocalInstallAppList(mContext.getPackageManager());
                AppBeanDaoHelper.getInstance().insertList(appBeans);
                handler.sendEmptyMessage(MainActivity.MSG_REFRESH);
            }
        }).start();
    }
}
