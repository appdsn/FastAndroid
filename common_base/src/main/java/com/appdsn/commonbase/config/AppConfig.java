package com.appdsn.commonbase.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.appdsn.commonbase.BuildConfig;
import com.appdsn.commonbase.widget.SwitchEnvironmentDialog;
import com.appdsn.commoncore.utils.EnvironmentSwitchUtils;
import com.appdsn.commoncore.utils.SPUtils;
import com.appdsn.commoncore.utils.ToastUtils;

/**
 * Desc:应用配置
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/10
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
public class AppConfig {
    /**
     * FIXME 正式发布的 App请把修改为false
     */
    public static final boolean IS_DEBUG = true;

    /**
     * 本地缓存文件名
     */
    private static final String SP_FILE_NAME = "app_config";

    /**
     * 环境存储SharedPreferences的key
     */
    public static final String SP_ENVIRONMENT_KEY = "sp_environment_flag";
    /**
     * 测试环境标志位
     */
    public static final int TEST_ENVIRONMENT_FLAG = 1;
    /**
     * 生产环境标志位
     */
    public static final int PRODUCT_ENVIRONMENT_FLAG = 2;

    /**
     * 接口应用标识APP_ID
     */
    public static String sAppId = BuildConfig.PRODUCT_APPID;
    /**
     * 接口应用秘钥
     */
    public static String sApiSecret = BuildConfig.PRODUCT_API_APPSECRET;

    /**
     * 基础接口域名
     */
    public static String sBaseApiHost = BuildConfig.PRODUCT_BASE_API_HOST;
    /**
     * H5域名
     */
    public static String sH5Host = BuildConfig.PRODUCT_H5_HOST;
    /**
     * 大数据上报域名
     */
    public static String sBigDataReport = BuildConfig.PRODUCT_BIGDATA_REPORT;
    /**
     * EMQ域名
     */
    public static String sMQTTHost = BuildConfig.PRODUCT_MQTT_HOST;
    /**
     * 融云KEY
     */
    public static String sRONGCloudKey = BuildConfig.PRODUCT_RONG_CLOUD_KEY;

    /**
     * 显示调试按钮在Window上
     *
     * @param activity 要显示的activity
     */
    public static void showDebugInWindow(final Activity activity) {
        if (!IS_DEBUG) {
            return;
        }
        EnvironmentSwitchUtils.showDebugInWindow(activity,
                new EnvironmentSwitchUtils.OnEnvironmentSwitchClickListener() {
                    @Override
                    public void onClick() {
                        SwitchEnvironmentDialog.showDialog(activity,
                                new SwitchEnvironmentDialog.EnvironmentOptionListener() {
                                    @Override
                                    public void doCheckTestEnvironment(Dialog dialog) {
                                        changeEnvironment(dialog, activity, TEST_ENVIRONMENT_FLAG);
                                    }

                                    @Override
                                    public void doCheckProductEnvironment(Dialog dialog) {
                                        changeEnvironment(dialog, activity, PRODUCT_ENVIRONMENT_FLAG);
                                    }
                                });
                    }
                });
    }

    /**
     * 获取环境配置信息
     */
    public static void obtainEnvironmentConfig() {
        int defaultHostFlag = IS_DEBUG ? TEST_ENVIRONMENT_FLAG : PRODUCT_ENVIRONMENT_FLAG;
        int spFlag = SPUtils.get(SP_ENVIRONMENT_KEY, defaultHostFlag);
        switch (spFlag) {
            case TEST_ENVIRONMENT_FLAG:
                sAppId = BuildConfig.TEST_APPID;
                sApiSecret = BuildConfig.TEST_API_APPSECRET;
                sBaseApiHost = BuildConfig.TEST_BASE_API_HOST;
                sH5Host = BuildConfig.TEST_H5_HOST;
                sBigDataReport = BuildConfig.TEST_BIGDATA_REPORT;
                sMQTTHost = BuildConfig.TEST_MQTT_HOST;
                sRONGCloudKey = BuildConfig.TEST_RONG_CLOUD_KEY;
                break;
            case PRODUCT_ENVIRONMENT_FLAG:
                sAppId = BuildConfig.PRODUCT_APPID;
                sApiSecret = BuildConfig.PRODUCT_API_APPSECRET;
                sBaseApiHost = BuildConfig.PRODUCT_BASE_API_HOST;
                sH5Host = BuildConfig.PRODUCT_H5_HOST;
                sBigDataReport = BuildConfig.PRODUCT_BIGDATA_REPORT;
                sMQTTHost = BuildConfig.PRODUCT_MQTT_HOST;
                sRONGCloudKey = BuildConfig.PRODUCT_RONG_CLOUD_KEY;
                break;
            default:
                break;
        }
    }

    /**
     * 切换环境
     *
     * @param dialog  切换环境弹窗
     * @param context 上下文
     * @param flag    环境标志位
     */
    public static void changeEnvironment(Dialog dialog, Context context, int flag) {
        int defaultHostFlag = IS_DEBUG ? TEST_ENVIRONMENT_FLAG : PRODUCT_ENVIRONMENT_FLAG;
        int spFlag = SPUtils.get(SP_ENVIRONMENT_KEY, defaultHostFlag);
        if (spFlag == flag) {
            ToastUtils.showShort("已经切换到当前环境,无须重复切换");
            return;
        }
        dialog.dismiss();
        SPUtils.put(SP_ENVIRONMENT_KEY, flag);
        //TODO 环境切换 清楚当前环境的用户信息、储存信息等
        EnvironmentSwitchUtils.restartApp(context);
    }

}
