package com.tj24.module_appmanager.greendao.daohelper;

import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.greendao.base.AppsBaseDao;
import com.tj24.module_appmanager.greendao.dao.AppClassficationDao;
import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class AppClassificationDaoHelper extends AppsBaseDao<AppClassfication,AppClassficationDao> {
    public static AppClassificationDaoHelper daoHelper;
    public static AppClassificationDaoHelper getInstance(){
        if(daoHelper == null){
            synchronized (AppClassificationDaoHelper.class){
                if(daoHelper ==null){
                    daoHelper = new AppClassificationDaoHelper();
                }
            }
        }
        return daoHelper;
    }

    @Override
    public AbstractDao getDao() {
        return mDaosession.getAppClassficationDao();
    }

    /**
     *  查找非 默认的classfication
     * @return
     */
    public List<AppClassfication> queryNoDefaultAppClasfications(){
        return mDdao.queryBuilder().where(AppClassficationDao.Properties.IsDefault.notEq(true)).list();
    }

    /**
     * 查询全部并排序
     * @return
     */
    public List<AppClassfication> queryAllAndSort(){
        return mDdao.queryBuilder().orderAsc(AppClassficationDao.Properties.Order).list();
    }

}
