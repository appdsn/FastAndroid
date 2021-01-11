package com.appdsn.jpush;

import android.content.Context;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/12/9 11:10
 */
public interface XNPushCallback {
    void onRegisterSuccess(Context context, String regId);

    void handleCustomMessage(Context context, CustomMessage customMessage);

    void handleClickNotification(Context context, NotificationMessage notification);

    void handleReceiveNotification(Context context, NotificationMessage notification);
}
