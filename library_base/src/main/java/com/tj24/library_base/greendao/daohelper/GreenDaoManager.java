package com.tj24.library_base.greendao.daohelper;

import android.database.sqlite.SQLiteDatabase;
import com.tj24.library_base.base.app.BaseApplication;
import com.tj24.library_base.greendao.DaoMaster;
import com.tj24.library_base.greendao.DaoSession;

/**
 * greendao 管理类
 */
public class GreenDaoManager  {

    private static final String DB_NAME = "DB_LIB";

    private static DbUpgradeOpenHelper devOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static void init() {
        devOpenHelper = new DbUpgradeOpenHelper(BaseApplication.getContext(),DB_NAME,null);   //数据库名
        database = devOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
    public static SQLiteDatabase getDb() {
        return database;
    }
}
