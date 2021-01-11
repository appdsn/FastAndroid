package com.appdsn.commoncore.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Keep;

/**
 * modulelib初始化提供接口，可以在Manifest文件中配置如下信息，必须继承BaseApplication
 * <meta-data
 * android:name="com.example.xxxApplicationDelegate"
 * android:value="IModuleConfig" />
 */
@Keep
public interface IApplicationDelegate {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);

}
