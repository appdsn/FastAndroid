package com.appdsn.commoncore;

import android.app.Application;
import android.content.Context;

import com.appdsn.commoncore.imageloader.ImageLoader;
import com.appdsn.commoncore.imageloader.core.ImageLoaderConfig;
import com.appdsn.commoncore.utils.ActivityLifecycleImpl;
import com.appdsn.commoncore.utils.ContextUtils;
import com.appdsn.commoncore.utils.CrashUtils;
import com.appdsn.commoncore.utils.SPUtils;


public class CommonCore {
    private static boolean sHasInit;

    /**
     * 不要在attachBaseContext初始化，会报错
     */
    public static void init(Context context) {
        if (context != null && !sHasInit) {
            ContextUtils.init((Application) context.getApplicationContext());
            ActivityLifecycleImpl.INSTANCE.init((Application) context.getApplicationContext());
            SPUtils.init(context);
            ImageLoader.init(ImageLoaderConfig.newBuilder(context).build());
            CrashUtils.init();
            sHasInit = true;
        }
    }
}
