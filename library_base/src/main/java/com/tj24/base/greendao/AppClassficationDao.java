package com.tj24.base.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tj24.base.bean.appmanager.AppClassfication;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "APP_CLASSFICATION".
*/
public class AppClassficationDao extends AbstractDao<AppClassfication, String> {

    public static final String TABLENAME = "APP_CLASSFICATION";

    /**
     * Properties of entity AppClassfication.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property SortName = new Property(2, String.class, "sortName", false, "SORT_NAME");
        public final static Property IsDefault = new Property(3, boolean.class, "isDefault", false, "IS_DEFAULT");
        public final static Property Order = new Property(4, int.class, "order", false, "ORDER");
    }


    public AppClassficationDao(DaoConfig config) {
        super(config);
    }
    
    public AppClassficationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"APP_CLASSFICATION\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"SORT_NAME\" TEXT," + // 2: sortName
                "\"IS_DEFAULT\" INTEGER NOT NULL ," + // 3: isDefault
                "\"ORDER\" INTEGER NOT NULL );"); // 4: order
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"APP_CLASSFICATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AppClassfication entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String sortName = entity.getSortName();
        if (sortName != null) {
            stmt.bindString(3, sortName);
        }
        stmt.bindLong(4, entity.getIsDefault() ? 1L: 0L);
        stmt.bindLong(5, entity.getOrder());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AppClassfication entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String sortName = entity.getSortName();
        if (sortName != null) {
            stmt.bindString(3, sortName);
        }
        stmt.bindLong(4, entity.getIsDefault() ? 1L: 0L);
        stmt.bindLong(5, entity.getOrder());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public AppClassfication readEntity(Cursor cursor, int offset) {
        AppClassfication entity = new AppClassfication( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sortName
            cursor.getShort(offset + 3) != 0, // isDefault
            cursor.getInt(offset + 4) // order
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AppClassfication entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSortName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsDefault(cursor.getShort(offset + 3) != 0);
        entity.setOrder(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final String updateKeyAfterInsert(AppClassfication entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(AppClassfication entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AppClassfication entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
