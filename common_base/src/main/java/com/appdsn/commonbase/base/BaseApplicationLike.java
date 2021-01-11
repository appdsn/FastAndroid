package com.appdsn.commonbase.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.appdsn.commoncore.base.IApplicationDelegate;
import com.appdsn.commoncore.utils.ManifestParser;
import com.tencent.tinker.entry.DefaultApplicationLike;

import java.util.List;

public final class BaseApplicationLike extends DefaultApplicationLike {

    private List<IApplicationDelegate> mAppDelegateList;

    public BaseApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                               long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     */
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        mAppDelegateList = new ManifestParser(getApplication()).parse();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate(getApplication());
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
