package com.tj24.module_appmanager.greendao.daohelper;

import com.tj24.module_appmanager.bean.MsgApk;
import com.tj24.module_appmanager.greendao.base.AppsBaseDao;
import com.tj24.module_appmanager.greendao.dao.MsgApkDao;
import org.greenrobot.greendao.AbstractDao;

public class MsgApkDaoHelper extends AppsBaseDao<MsgApk, MsgApkDao> {
    public static MsgApkDaoHelper daoHelper;
    public static MsgApkDaoHelper getInstance(){
        if(daoHelper == null){
            synchronized (MsgApkDaoHelper.class){
                if(daoHelper ==null){
                    daoHelper = new MsgApkDaoHelper();
                }
            }
        }
        return daoHelper;
    }
    @Override
    public AbstractDao getDao() {
        return mDaosession.getMsgApkDao();
    }
}
