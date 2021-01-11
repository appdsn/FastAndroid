package com.appdsn.library;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/16 13:44
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class MobStatisticSDK {
    private static Context sContext;
    private static Map<String, String> sGlobalMap = new HashMap<>();

    public static Context getContext() {
        return sContext;
    }

    /**
     * 如果需要在某个子进程中统计自定义事件，则需保证在此子进程中进行SDK初始化。
     */
    public static void init(Context context, String umAppKey, String channel) {
        sContext = context.getApplicationContext();
        UMConfigure.init(context, umAppKey, channel, UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // isEnable: false-关闭错误统计功能；true-打开错误统计功能（默认打开）
        MobclickAgent.setCatchUncaughtExceptions(true);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
        //SDK会将30秒内的连续两次进入应用看成一次完整的使用过程,使用App之间间隔超过30秒,Android启动次数+1
        MobclickAgent.setSessionContinueMillis(30 * 1000);
    }

    /**
     * 打开日志开关
     */
    public static void setLogEnabled(boolean enabled) {
        UMConfigure.setLogEnabled(enabled);
    }

    /**
     * 设置全局事件参数
     */
    public static final void setGlobalKV(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            sGlobalMap.put(key, value);
        }
    }

    /**
     * 如果开发者自己捕获了错误，需要手动上传到【友盟+】服务器可以调用下面方法
     * 使用自定义错误，查看时请在错误列表页面选择【自定义错误】。
     */
    public static void reportError(String error) {
        MobclickAgent.reportError(getContext(), error);
    }

    /**
     * 如果开发者自己捕获了错误，需要手动上传到【友盟+】服务器可以调用下面方法
     * 使用自定义错误，查看时请在错误列表页面选择【自定义错误】。
     */
    public static void reportError(Throwable throwable) {
        MobclickAgent.reportError(getContext(), throwable);
    }

    /**
     * 调用如下自定义事件统计接口时(注意：需要先在【友盟+】网站注册事件ID)
     */
    public static final void onEvent(String eventId) {
        if (TextUtils.isEmpty(eventId)) {
            return;
        }
        if (sGlobalMap.isEmpty()) {
            MobclickAgent.onEvent(getContext(), eventId);
        } else {
            MobclickAgent.onEvent(getContext(), eventId, sGlobalMap);
        }
    }

    /**
     * 调用如下自定义事件统计接口时(注意：需要先在【友盟+】网站注册事件ID)
     *
     * @param eventId 不能为null，也不能为空字符串"", 且参数长度不能超过128个字符。
     *                不能和以下保留字冲突： "id", "ts", "du", "$stfl", "$!deeplink", "$!link"
     * @param label   不能为null，也不能为空字符串"" 参数label长度不能超过256个字符
     */
    public static final void onEvent(String eventId, String label) {
        if (TextUtils.isEmpty(eventId) || TextUtils.isEmpty(label)) {
            return;
        }
        if (sGlobalMap.isEmpty()) {
            MobclickAgent.onEvent(getContext(), eventId, label);
        } else {
            HashMap map = new HashMap();
            map.put(eventId, label);
            map.putAll(sGlobalMap);
            onEvent(eventId, map);
        }
    }

    /**
     * 调用如下自定义事件统计接口时(注意：需要先在【友盟+】网站注册事件ID)
     *
     * @param extraData 参数map最多支持100个K-V值。参数map不能为null，且map不能为空map
     *                  map中的key不能和以下保留字冲突： "id", "ts", "du", "$stfl", "$!deeplink", "$!link"
     */
    public static final void onEvent(String eventId, Map<String, String> extraData) {
        if (TextUtils.isEmpty(eventId) || extraData == null) {
            return;
        }
        extraData.putAll(sGlobalMap);
        if (extraData.isEmpty()) {
            return;
        }
        MobclickAgent.onEvent(getContext(), eventId, extraData);
    }

    /**
     * 对非Activity页面，如Fragment、自定义View等非标准页面进行统计,一次成对的 onPageStart -> onPageEnd 调用。
     * 进入页面时调用：比如在onResume方法调用
     */
    public static final void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    /**
     * 对非Activity页面，如Fragment、自定义View等非标准页面进行统计,一次成对的 onPageStart -> onPageEnd 调用。
     * 离开页面时调用：比如在onPause方法调用
     */
    public static final void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }
}

