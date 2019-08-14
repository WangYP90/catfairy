package com.tj24.module_appmanager.greendao.base;

import android.content.Context;
import com.tj24.library_base.base.app.BaseApplication;
import com.tj24.module_appmanager.greendao.dao.DaoSession;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public abstract class AppsBaseDao<T,S extends AbstractDao> {
    private Context mContext;
    public S mDdao;
    public DaoSession mDaosession;
    public QueryBuilder mQueryBuilder;
    public AppsBaseDao() {
        mContext = BaseApplication.getContext();
        mDaosession = AppsDaoManager.getDaoSession();
        mDdao = (S) getDao();
        mQueryBuilder = mDdao.queryBuilder();
    }

    public abstract AbstractDao getDao();

    public T queryObjById(Object id){
        if(id==null) return null;
        return (T) mDdao.load(id);
    }

    public List<T> queryAll(){
        return  mDdao.loadAll();
    }

    public void insertObj(T t){
        if(t==null) return;
        mDdao.insertOrReplace(t);
    }

    public void insertList(List<T> ts){
        if(ts==null) return;
        mDdao.insertOrReplaceInTx(ts);
    }

    public void  deleteObj(T t){
        if(t==null) return;
        mDdao.delete(t);
    }

    public void deleteList(List<T> ts){
        if(ts==null) return;
        mDdao.deleteInTx(ts);
    }

    public void deleteById(Object id){
        if(id==null) return;
        mDdao.deleteByKey(id);
    }

    public void deleteAll(){
        mDdao.deleteAll();
    }
}
