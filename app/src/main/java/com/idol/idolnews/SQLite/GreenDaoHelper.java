package com.idol.idolnews.SQLite;

import android.database.sqlite.SQLiteDatabase;

import com.idol.idolnews.Utils.ContextHolder;
import com.speedystone.greendaodemo.db.DaoMaster;
import com.speedystone.greendaodemo.db.DaoSession;

/**
 * Created by 53478 on 2017/11/24.
 */

public class GreenDaoHelper {
    private static DaoMaster.DevOpenHelper mHelper;
    private static SQLiteDatabase mDb;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    /**
     * 设置greenDao
     */
    public static void initDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(ContextHolder.getContext(), "top_db", null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        if(mDaoSession.equals(null)){
            initDatabase();
        }
        return mDaoSession;
    }
    public static SQLiteDatabase getDb() {
        if (mDb.equals(null)){
            initDatabase();
        }
        return mDb;
    }

}
