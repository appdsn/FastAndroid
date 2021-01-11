package com.appdsn.jpush;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/10/30 20:43
 */
public class XNPushSDK {
    private static Context sContext;
    private static XNPushCallback sPushCallback;
    private static HashMap<Integer, OnOperatorCallback> sOperatorMap = new HashMap<>();

    public static void init(Context context, boolean debug, XNPushCallback pushCallback) {
        JPushInterface.setDebugMode(debug);
        JPushInterface.init(context);
        sPushCallback = pushCallback;
        if (context instanceof Application) {
            sContext = context;
        } else {
            sContext = context.getApplicationContext();
        }
    }

    /**
     * 别名相当于用户id,用于标记特定用户，别名有限制，同一个别名只能被10个设备绑定，因此别名可以用用户ID，保证唯一
     * 这个接口是覆盖逻辑，而不是增量逻辑
     */
    public static void setAlias(String alias, OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(0, callback);
        }
        JPushInterface.setAlias(sContext, 0, alias);
    }

    public static void deleteAlias(OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(1, callback);
        }
        JPushInterface.deleteAlias(sContext, 1);
    }

    public static void getAlias(OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(2, callback);
        }
        JPushInterface.getAlias(sContext, 2);
    }

    /**
     * tag相当于分组功能, 不同的设备可以设置相同的tag,最大支持设置1000个，分组推荐使用
     */
    public static void addTags(Set<String> tagSet, OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(3, callback);
        }
        JPushInterface.addTags(sContext, 3, tagSet);
    }

    /**
     * tag相当于分组功能, 被覆盖
     */
    public static void setTags(Set<String> tagSet, OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(4, callback);
        }
        JPushInterface.setTags(sContext, 4, tagSet);
    }

    /**
     * 删除tags
     */
    public static void deleteTags(Set<String> tagSet, OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(5, callback);
        }
        JPushInterface.deleteTags(sContext, 5, tagSet);
    }

    public static void getAllTags(OnOperatorCallback callback) {
        if (callback != null) {
            sOperatorMap.put(6, callback);
        }
        JPushInterface.getAllTags(sContext, 6);
    }

    public static String getRegistrationID() {
        return JPushInterface.getRegistrationID(sContext);
    }

    static XNPushCallback getPushCallback() {
        return sPushCallback;
    }

    static HashMap<Integer, OnOperatorCallback> getOperatorMap() {
        return sOperatorMap;
    }
}
