package com.appdsn.commonbase.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.appdsn.commoncore.base.BaseApplication;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/18 14:04
 * @Copyright: Copyright (c) 2016-2020
 * @Company:
 * @Version: 1.0
 */
public class BaseAppApplication extends BaseApplication {
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
