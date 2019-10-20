package com.tj24.appmanager.daohelper;

import com.tj24.base.bean.appmanager.login.Suggestion;
import com.tj24.base.greendao.SuggestionDao;
import com.tj24.base.greendao.daohelper.BaseDao;

import org.greenrobot.greendao.AbstractDao;

public class SuggestionDaoHelper extends BaseDao<Suggestion, SuggestionDao> {
    public static SuggestionDaoHelper daoHelper;
    public static SuggestionDaoHelper getInstance(){
        if(daoHelper == null){
            synchronized (SuggestionDao.class){
                if(daoHelper ==null){
                    daoHelper = new SuggestionDaoHelper();
                }
            }
        }
        return daoHelper;
    }
    @Override
    public AbstractDao getDao() {
        return mDaosession.getSuggestionDao();
    }

    /**
     * 查询最近的一条提交记录 的时间
     * @return
     */
    public long queryLastCreatTime(){
        Suggestion last = mDdao.queryBuilder().orderDesc(SuggestionDao.Properties.CreatTime).limit(1).unique();
        if(last != null){
            return last.getCreatTime();
        }else {
            return 0;
        }
    }
}
