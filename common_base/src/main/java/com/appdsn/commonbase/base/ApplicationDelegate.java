package com.appdsn.commonbase.base;

import android.app.Application;
import android.content.Context;
import android.webkit.WebView;

import com.appdsn.commonbase.BuildConfig;
import com.appdsn.commonbase.config.AppConfig;
import com.appdsn.commonbase.http.HttpHelper;
import com.appdsn.commonbase.scheme.SchemeSDK;
import com.appdsn.commonbase.scheme.config.SchemeEntity;
import com.appdsn.commonbase.scheme.listener.JSMethodCallback;
import com.appdsn.commonbase.scheme.listener.OnSchemeHandler;
import com.appdsn.commonbase.utils.ChannelUtils;
import com.appdsn.commoncore.base.IApplicationDelegate;
import com.appdsn.library.MobShareSDK;
import com.appdsn.library.model.ShareConfig;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiaoniu.statistic.Configuration;
import com.xiaoniu.statistic.NiuDataAPI;

/**
 * Created by admin on 2017/6/8.
 */

public class ApplicationDelegate implements IApplicationDelegate {

    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        SchemeSDK.init(application, BuildConfig.DEBUG,
                new OnSchemeHandler() {
                    @Override
                    public boolean onHandleScheme(Context context, SchemeEntity schemeEntity, WebView webView, JSMethodCallback callback) {
                        return false;
                    }

                    @Override
                    public boolean isLogin() {
                        return false;
                    }
                });
        AppConfig.obtainEnvironmentConfig();
        HttpHelper.init();

        if (BuildConfig.DEBUG) {
            CrashReport.initCrashReport(application, "3e2eaa061f", BuildConfig.DEBUG);
        } else {
            CrashReport.initCrashReport(application, "79671a8883", BuildConfig.DEBUG);
        }
        initNiuData(application);
        initXNShareSDK(application);
    }

    private void initXNShareSDK(Application application) {
        ShareConfig config = new ShareConfig();
        config.setQQ(BuildConfig.QQ_APP_ID, "121313122");
        config.setWeixin(BuildConfig.WX_APP_ID, "2124124");
        config.setUMeng("5a12384aa40fa3551f0001d1", "渠道号");
        MobShareSDK.init(application, config);
    }

    public void initNiuData(Application application) {
        //测试环境
        NiuDataAPI.init(application, new Configuration().serverUrl(AppConfig.sBigDataReport)
                //.debugOn() //切换到sdk默认的测试环境地址
                .logOpen()//打开sdk日志信息
                .channel(ChannelUtils.getChannelId())
        );
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

}
