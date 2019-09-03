package com.tj24.base.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tj24.base.bean.appmanager.MsgApk;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MSG_APK".
*/
public class MsgApkDao extends AbstractDao<MsgApk, String> {

    public static final String TABLENAME = "MSG_APK";

    /**
     * Properties of entity MsgApk.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property PackageName = new Property(1, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property AppName = new Property(2, String.class, "appName", false, "APP_NAME");
        public final static Property CreatTimeMills = new Property(3, long.class, "creatTimeMills", false, "CREAT_TIME_MILLS");
        public final static Property CreatDay = new Property(4, String.class, "creatDay", false, "CREAT_DAY");
        public final static Property CreatHour = new Property(5, String.class, "creatHour", false, "CREAT_HOUR");
        public final static Property Action = new Property(6, String.class, "action", false, "ACTION");
    }


    public MsgApkDao(DaoConfig config) {
        super(config);
    }
    
    public MsgApkDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MSG_APK\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"PACKAGE_NAME\" TEXT," + // 1: packageName
                "\"APP_NAME\" TEXT," + // 2: appName
                "\"CREAT_TIME_MILLS\" INTEGER NOT NULL ," + // 3: creatTimeMills
                "\"CREAT_DAY\" TEXT," + // 4: creatDay
                "\"CREAT_HOUR\" TEXT," + // 5: creatHour
                "\"ACTION\" TEXT);"); // 6: action
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MSG_APK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MsgApk entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(3, appName);
        }
        stmt.bindLong(4, entity.getCreatTimeMills());
 
        String creatDay = entity.getCreatDay();
        if (creatDay != null) {
            stmt.bindString(5, creatDay);
        }
 
        String creatHour = entity.getCreatHour();
        if (creatHour != null) {
            stmt.bindString(6, creatHour);
        }
 
        String action = entity.getAction();
        if (action != null) {
            stmt.bindString(7, action);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MsgApk entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(3, appName);
        }
        stmt.bindLong(4, entity.getCreatTimeMills());
 
        String creatDay = entity.getCreatDay();
        if (creatDay != null) {
            stmt.bindString(5, creatDay);
        }
 
        String creatHour = entity.getCreatHour();
        if (creatHour != null) {
            stmt.bindString(6, creatHour);
        }
 
        String action = entity.getAction();
        if (action != null) {
            stmt.bindString(7, action);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MsgApk readEntity(Cursor cursor, int offset) {
        MsgApk entity = new MsgApk( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // packageName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // appName
            cursor.getLong(offset + 3), // creatTimeMills
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // creatDay
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // creatHour
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // action
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MsgApk entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPackageName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAppName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCreatTimeMills(cursor.getLong(offset + 3));
        entity.setCreatDay(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreatHour(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAction(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MsgApk entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MsgApk entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MsgApk entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
