package com.appdsn.fastdemo;

import com.appdsn.commoncore.base.BaseApplication;
import com.appdsn.commoncore.database.DbHelper;
import com.appdsn.fastdemo.weather.rxhttp.RxHttpHelper;

public class DemoApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        DbHelper.copyDbFile(this, "weather_city.db");
        RxHttpHelper.init();
    }

}
