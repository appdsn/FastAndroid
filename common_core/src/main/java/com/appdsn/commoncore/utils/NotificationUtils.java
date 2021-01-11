package com.appdsn.commoncore.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.lang.reflect.Method;

import static android.app.Notification.VISIBILITY_SECRET;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class NotificationUtils {
    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "消息通知";
    private static final String CHANNEL_DESCRIPTION = "this is default channel!";
    private static NotificationManager sManager;

    /*NotificationManager.IMPORTANCE_DEFAULT*/
    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(String channelId, String channelName, int importance, boolean hasSound) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.canBypassDnd();//是否绕过请勿打扰模式
        channel.enableLights(true);//闪光灯
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
        channel.canShowBadge();//桌面launcher的消息角标
        channel.enableVibration(false);//是否允许震动
        channel.getAudioAttributes();//获取系统通知响铃声音的配置
        channel.getGroup();//获取通知取到组
        channel.setBypassDnd(true);//设置可绕过  请勿打扰模式
//        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
        channel.shouldShowLights();//是否会有灯光
        if (!hasSound) {
            channel.setSound(null, null);
        }
        getManager().createNotificationChannel(channel);
    }

    public static NotificationManager getManager() {
        if (sManager == null) {
            sManager = (NotificationManager) ContextUtils.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return sManager;
    }

    public static void sendNotification(String title, String content, int icon, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = getNotification(title, content, icon);
        builder.setContentIntent(pendingIntent);
        getManager().notify(3000, builder.build());
    }

    public static NotificationCompat.Builder getNotification(String title, String content, int icon) {
        NotificationCompat.Builder builder = createBuilder();
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(ContextUtils.getContext().getResources(), icon));
        //点击自动删除通知
        builder.setAutoCancel(true);
        return builder;
    }

    public static NotificationCompat.Builder createBuilder() {
        return createBuilder(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT, true);
    }

    public static NotificationCompat.Builder createBuilder(String channelId, String channelName, int importance, boolean hasSound) {
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, importance, hasSound);
            builder = new NotificationCompat.Builder(ContextUtils.getContext(), channelId);
//            builder.setOnlyAlertOnce(true);
        } else {
            builder = new NotificationCompat.Builder(ContextUtils.getContext());
            builder.setPriority(PRIORITY_DEFAULT);
        }
        return builder;
    }

    //关闭statusbar，自定义notification按钮时，很多机器在点击按钮时statusbar不会关闭，需强制关闭
    // 需要配置权限<uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
    @SuppressLint("WrongConstant")
    public static void collapseStatusBar() {
        Object sbservice = ContextUtils.getContext().getSystemService("statusbar");
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapse;
            if (currentApiVersion <= 16) {
                collapse = statusBarManager.getMethod("collapse");
            } else {
                collapse = statusBarManager.getMethod("collapsePanels");
            }
            collapse.setAccessible(true);
            collapse.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
