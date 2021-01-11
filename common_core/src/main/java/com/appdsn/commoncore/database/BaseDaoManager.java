package com.appdsn.commoncore.database;

import android.content.Context;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.ParameterizedType;

/*创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级*/
public abstract class BaseDaoManager<Master extends AbstractDaoMaster, Helper extends DatabaseOpenHelper, Session extends AbstractDaoSession> {
    private String DB_NAME;
    private Master mDaoMaster;
    private Helper mHelper;
    private Session mDaoSession;

    public void init(Context context, String dbName, boolean isDebug) {//初始化创建数据与表;
        this.DB_NAME = dbName;
        mHelper = getHelperInstance(context, getDB_NAME());
        mDaoMaster = getMasterInstance(mHelper.getWritableDb());
        mDaoSession = (Session) mDaoMaster.newSession();
        if (isDebug) {
            setDebug();
        }
    }

    public Session getDaoSession() {
        return mDaoSession;
    }

    public Database getDatabase() {
        return mDaoMaster.getDatabase();
    }

    private <Helper> Helper getHelperInstance(Context context, String dbName) {
        try {
            return ((Class<Helper>) ((ParameterizedType) (this.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[1])
                    .getConstructor(Context.class, String.class).newInstance(context, dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <Master> Master getMasterInstance(Database database) {
        try {
            return ((Class<Master>) ((ParameterizedType) (this.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[0])
                    .getConstructor(Database.class).newInstance(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开输出日志，默认关闭
     */
    private void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    private void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    private void closeDaoSession() {
        if (mDaoSession != null) {
            clear(mDaoSession);
            mDaoSession = null;
        }
    }

    /*清空所有数据表的缓存数据*/
    protected abstract void clear(Session daoSession);

    private String getDB_NAME() {
        if (TextUtils.isEmpty(DB_NAME)) {
            DB_NAME = "base.db";
        }
        return DB_NAME;
    }
}
