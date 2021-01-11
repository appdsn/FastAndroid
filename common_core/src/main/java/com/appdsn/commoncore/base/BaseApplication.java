package com.appdsn.commoncore.base;

import android.app.Application;
import android.content.Context;

import com.appdsn.commoncore.CommonCore;
import com.appdsn.commoncore.utils.ManifestParser;

import java.util.List;

/**
 * Desc:项目中如有自定义Application必须继承该类，否则需要在Manifest中配置该类
 */
public class BaseApplication extends Application {
    private List<IApplicationDelegate> mAppDelegateList;

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mAppDelegateList = new ManifestParser(this).parse();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CommonCore.init(this);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }
}
