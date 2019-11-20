package com.tj24.base.greendao.daohelper;

import android.util.Log;

import com.tj24.base.greendao.DaoSession;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * @Description:封装greendao基本操作方法
 * @Createdtime:2019/3/2 22:41
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public abstract class BaseDao<T,S extends AbstractDao> {
    private static final String TAG = "BaseDao";

    public S mDdao;
    public DaoSession mDaosession;
    public BaseDao() {
        mDaosession = GreenDaoManager.getDaoSession();
        mDdao = (S) getDao();
    }

    public abstract AbstractDao getDao();

    /**
     * 通过主键ID 查询一个实体
     * @param id
     * @return
     */
    public T queryObjById(Object id){
        T t = null;
        try{
            t=  (T) mDdao.load(id);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return t;
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<T> queryAll(){
      return  mDdao.loadAll();
    }

    /**
     * 增加或替换 一个实体（没有则增加，有则替换）
     * @param t
     */
    public void insertOrReplaceObj(T t){
        try{
            mDdao.insertOrReplace(t);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 增加或替换list
     * @param ts
     */
    public void inserOrReplacetList(List<T> ts){
        try{
            mDdao.insertOrReplaceInTx(ts);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 新增实体
     * @param t
     */
    public void insertObj(T t){
        try{
            mDdao.insertOrReplace(t);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 新增集合
     * @param ts
     */
    public void inserList(List<T> ts){
        try{
            mDdao.insertOrReplaceInTx(ts);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 更新实体
     * @param t
     */
    public void updateObj(T t){
        try{
            mDdao.update(t);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 更新集合
     * @param ts
     */
    public void updateList(List<T> ts){
        try{
            mDdao.updateInTx(ts);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 删除一个实体
     * @param t
     */
    public void  deleteObj(T t){
        try{
            mDdao.delete(t);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 删除一个集合
     * @param ts
     */
    public void deleteList(List<T> ts){
        try{
            mDdao.deleteInTx(ts);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 通过ID主键删除
     * @param id
     */
    public void deleteById(Object id){
        try{
            mDdao.deleteByKey(id);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll(){
        mDdao.deleteAll();
    }
}
