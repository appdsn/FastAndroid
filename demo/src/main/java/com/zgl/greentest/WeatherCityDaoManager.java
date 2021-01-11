package com.zgl.greentest;

import android.content.Context;
import android.util.Log;

import com.appdsn.commoncore.database.BaseDaoManager;
import com.appdsn.commoncore.utils.ContextUtils;
import com.zgl.greentest.gen.DaoMaster;
import com.zgl.greentest.gen.DaoSession;
import com.zgl.greentest.gen.WeatherCityDao;

import org.greenrobot.greendao.database.Database;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/8/26 18:37
 */
public class WeatherCityDaoManager extends BaseDaoManager<DaoMaster, WeatherCityDaoManager.WeatherCityOpenHelper, DaoSession> {
    private final static WeatherCityDaoManager INSTANCE = new WeatherCityDaoManager();

    public static WeatherCityDaoManager getInstance() {
        return INSTANCE;
    }

    private WeatherCityDaoManager() {
        init(ContextUtils.getContext(), "weather_city.db", true);
    }

    @Override
    protected void clear(DaoSession daoSession) {
        daoSession.clear();
    }

    public static class WeatherCityOpenHelper extends DaoMaster.OpenHelper {

        public WeatherCityOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for weather_city.db ");
            WeatherCityDao.createTable(db, true);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            WeatherCityDao.dropTable(db, true);
            onCreate(db);
        }
    }
}
