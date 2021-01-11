package com.appdsn.jpush;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/10/30 20:23
 */
public class XNPushReceiver extends JPushMessageReceiver {
    private static final String TAG = "XNPushReceiver";

    @Override
    public void onRegister(Context var1, String var2) {
        Log.d(TAG, "JPush 用户注册成功：" + var2);
        if (XNPushSDK.getPushCallback() != null) {
            XNPushSDK.getPushCallback().onRegisterSuccess(var1, var2);
        }
    }

    @Override
    public void onTagOperatorResult(Context var1, JPushMessage var2) {
        Log.d(TAG, "onTagOperatorResult：" + var2.getErrorCode() + ":" + var2.getTags());
        int sequence = var2.getSequence();
        OnOperatorCallback callback = XNPushSDK.getOperatorMap().remove(sequence);
        if (callback != null) {
            if (var2.getErrorCode() == 0) {
                callback.onSuccess(var2.getTags().toString());
            } else {
                callback.onFailed(sequence, var2.getErrorCode());
            }
        }
    }

    @Override
    public void onAliasOperatorResult(Context var1, JPushMessage var2) {
        Log.d(TAG, "onAliasOperatorResult：" + var2.toString());
        int sequence = var2.getSequence();
        OnOperatorCallback callback = XNPushSDK.getOperatorMap().remove(sequence);
        if (callback != null) {
            if (var2.getErrorCode() == 0) {
                callback.onSuccess(var2.getAlias().toString());
            } else {
                callback.onFailed(sequence, var2.getErrorCode());
            }
        }
    }

    @Override
    public void onMessage(Context var1, CustomMessage var2) {
        super.onMessage(var1, var2);
        Log.d(TAG, "接受到推送下来的自定义消息" + var2.toString());
        if (XNPushSDK.getPushCallback() != null) {
            XNPushSDK.getPushCallback().handleCustomMessage(var1, var2);
        }
    }

    @Override
    public void onNotifyMessageOpened(Context var1, NotificationMessage var2) {
        super.onNotifyMessageOpened(var1, var2);
        Log.d(TAG, "用户点击打开了通知");
        if (XNPushSDK.getPushCallback() != null) {
            XNPushSDK.getPushCallback().handleClickNotification(var1, var2);
        }
    }

    @Override
    public void onNotifyMessageArrived(Context var1, NotificationMessage var2) {
        super.onNotifyMessageArrived(var1, var2);
        Log.d(TAG, "接收到推送下来的通知" + var2.toString());
        if (XNPushSDK.getPushCallback() != null) {
            XNPushSDK.getPushCallback().handleReceiveNotification(var1, var2);
        }
    }
}
