package com.tj24.appmanager.daohelper;

import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.base.greendao.AppClassficationDao;
import com.tj24.base.greendao.daohelper.BaseDao;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class AppClassificationDaoHelper extends BaseDao<AppClassfication, AppClassficationDao> {
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
