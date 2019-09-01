package com.tj24.base.greendao.daohelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.tj24.base.greendao.AppBeanDao;
import com.tj24.base.greendao.AppClassficationDao;
import com.tj24.base.greendao.DaoMaster;
import com.tj24.base.greendao.MsgApkDao;

import org.greenrobot.greendao.database.Database;


/**
 * @Description:数据库升级帮助类
 * @Createdtime:2019/3/3 0:17
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class DbUpgradeOpenHelper extends DaoMaster.OpenHelper {

    public DbUpgradeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                    onCreateAllTables(db,ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                    onDropAllTables(db,ifExists);
            }
        }, AppBeanDao.class, AppClassficationDao.class, MsgApkDao.class);
    }
}
