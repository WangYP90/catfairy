package com.tj24.appmanager.daohelper;

import android.database.Cursor;
import android.text.TextUtils;

import com.tj24.appmanager.activity.MesageActivity;
import com.tj24.base.bean.appmanager.MsgApk;
import com.tj24.base.greendao.MsgApkDao;
import com.tj24.base.greendao.daohelper.BaseDao;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

public class MsgApkDaoHelper extends BaseDao<MsgApk, MsgApkDao> {
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

    /**
     * 分页加载数据
     * @param page
     * @return
     */
    public List<MsgApk> queryByPage(int page){
       return mDdao.queryBuilder().offset(page* MesageActivity.PAGE_SIZE).limit(MesageActivity.PAGE_SIZE)
               .orderDesc(MsgApkDao.Properties.CreatTimeMills)
               .list();
    }


    /**
     * 将创建时间字段列出来并去重
     * @return
     */
    public List<String> distincByCreatDay(){
        String sql = "SELECT DISTINCT" + MsgApkDao.Properties.CreatDay.columnName
                + " FROM "
                + MsgApkDao.TABLENAME
                + "WHERE"
                + MsgApkDao.Properties.Action.columnName
                + " ="
                + "卸载"
                + "";
        Cursor c = mDaosession.getDatabase().rawQuery(sql,null);

        List<String> creatDays = new ArrayList<>();
        try {
            if(c.moveToFirst()){
                do{
                    if(!TextUtils.isEmpty(c.getString(0))){
                        creatDays.add(c.getString(0));
                    }
                }while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return creatDays;
    }
    
}
