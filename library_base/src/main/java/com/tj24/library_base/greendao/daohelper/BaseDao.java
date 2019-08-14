package com.tj24.library_base.greendao.daohelper;

import android.content.Context;
import com.tj24.library_base.base.app.BaseApplication;
import com.tj24.library_base.greendao.DaoSession;
import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * @Description:封装greendao基本操作方法
 * @Createdtime:2019/3/2 22:41
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public abstract class BaseDao<T,S extends AbstractDao> {
    private Context mContext;
    private S mDdao;
    public DaoSession mDaosession;
    public BaseDao() {
        mContext = BaseApplication.getContext();
        mDdao = (S) getDao();
        mDaosession = GreenDaoManager.getDaoSession();
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
