package com.appdsn.fastdev.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.appdsn.commoncore.base.BaseApplication;

public class YourApplication extends BaseApplication {

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
