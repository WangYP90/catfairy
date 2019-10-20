package com.tj24.appmanager.model;

import android.content.Context;

/**
 * @Description:基类model
 * @Createdtime:2019/3/24 14:40
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public abstract class BaseAppsManagerModel {

    public Context mContext;
    public BaseAppsManagerModel(Context mContext) {
        this.mContext = mContext;
    }

    public static final String APP_BEANS = "appbeans";
    public static final String APP_CLASSIFICATION = "appclassification";


   public interface OnConfirm{
        void onConfirm(Object object);
    }
}
