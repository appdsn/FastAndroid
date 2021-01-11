package com.zgl.greentest;

import android.content.Context;
import android.util.Log;

import com.appdsn.commoncore.database.BaseDaoManager;
import com.appdsn.commoncore.utils.ContextUtils;
import com.zgl.greentest.gen.AttentionCityDao;
import com.zgl.greentest.gen.DaoMaster;
import com.zgl.greentest.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/8/26 18:37
 */
public class AttentionCityDaoManager extends BaseDaoManager<DaoMaster, AttentionCityDaoManager.AttentionCityOpenHelper, DaoSession> {
    private final static AttentionCityDaoManager INSTANCE = new AttentionCityDaoManager();

    public static AttentionCityDaoManager getInstance() {
        return INSTANCE;
    }

    private AttentionCityDaoManager() {
        init(ContextUtils.getContext(), "attention_city.db", true);
    }

    @Override
    protected void clear(DaoSession daoSession) {
        daoSession.clear();
    }

    public static class AttentionCityOpenHelper extends DaoMaster.OpenHelper {

        public AttentionCityOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for attention_city.db ");
            AttentionCityDao.createTable(db, true);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            AttentionCityDao.dropTable(db, true);
            onCreate(db);
        }
    }
}
