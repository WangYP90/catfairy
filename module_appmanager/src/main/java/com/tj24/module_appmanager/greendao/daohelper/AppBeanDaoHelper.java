package com.tj24.module_appmanager.greendao.daohelper;

import com.tj24.library_base.utils.ListUtil;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.greendao.base.AppsBaseDao;
import com.tj24.module_appmanager.greendao.dao.AppBeanDao;
import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

public class AppBeanDaoHelper extends AppsBaseDao<AppBean,AppBeanDao> {
    public static AppBeanDaoHelper daoHelper;
    public static AppBeanDaoHelper getInstance(){
        if(daoHelper == null){
            synchronized (AppBeanDaoHelper.class){
                if(daoHelper ==null){
                    daoHelper = new AppBeanDaoHelper();
                }
            }
        }
        return daoHelper;
    }

    @Override
    public AbstractDao getDao() {
        return mDaosession.getAppBeanDao();
    }

    /**
     * 根据classfication ID 查找app
     * @param id
     * @return
     */
    public List<AppBean> queryAppByClassficationId(String id){
        List<AppBean> appBeans = new ArrayList<>();
        for(AppBean appBean : queryAll()){
            if(!ListUtil.isNullOrEmpty(appBean.getType()) && appBean.getType().contains(id)){
                appBeans.add(appBean);
            }
        }
        return appBeans;
    }

}
