package com.appdsn.commoncore.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.appdsn.commoncore.widget.roundedimageview.RoundedImageView;

/**
 * Desc:环境切换工具
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/10
 * Copyright: Copyright (c) 2016-2022
 * ""
 * <p>
 * Update Comments:
 *
 * @author anyabo
 */
public class EnvironmentSwitchUtils {
    /**
     * 移动限制偏移
     */
    private static final int MOVE_LIMIT_OFFSET = 20;
    /**
     * 图片dp大小
     */
    private static final int IMAGE_DP_SIZE = 40;

    /**
     * 显示调试按钮在Window上
     *
     * @param activity            要显示的activity
     * @param switchClickListener 调试开关按钮点击监听回调
     */
    public static void showDebugInWindow(final Activity activity,
                                         final OnEnvironmentSwitchClickListener switchClickListener) {
        try {
            final WindowManager windowManager = (WindowManager) activity.
                    getSystemService(Context.WINDOW_SERVICE);
            final WindowManager.LayoutParams params = buildImageToWindowParams(activity);
            final RoundedImageView imageView = buildImageView(activity);
            try {
                windowManager.addView(imageView, params);
            } catch (Exception e) {
                return;
            }
            imageView.setOnTouchListener(new View.OnTouchListener() {
                float lastX, lastY;
                int oldOffsetX, oldOffsetY;
                int tag = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int action = event.getAction();
                    float x = event.getX();
                    float y = event.getY();
                    if (tag == 0) {
                        oldOffsetX = params.x;
                        oldOffsetY = params.y;
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        lastX = x;
                        lastY = y;
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        params.x += (int) (x - lastX) / 3;
                        params.y += (int) (y - lastY) / 3;
                        tag = 1;
                        windowManager.updateViewLayout(imageView, params);
                    } else if (action == MotionEvent.ACTION_UP) {
                        int newOffsetX = params.x;
                        int newOffsetY = params.y;
                        if (Math.abs(oldOffsetX - newOffsetX) <= MOVE_LIMIT_OFFSET
                                && Math.abs(oldOffsetY - newOffsetY) <= MOVE_LIMIT_OFFSET) {
                            if (switchClickListener != null) {
                                switchClickListener.onClick();
                            }
                        } else {
                            tag = 0;
                        }
                    }
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 构建要展示的图片
     *
     * @param context 上下文
     * @return ImageView
     */
    private static RoundedImageView buildImageView(Context context) {
        RoundedImageView imageView = new RoundedImageView(context.getApplicationContext());
        imageView.setOval(true);
        int size = DisplayUtils.dp2px(IMAGE_DP_SIZE);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(context.getResources()
                .getColor(android.R.color.holo_purple));
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    /**
     * 构建图片在Window上的Params
     *
     * @param activity 当前要展示的activity
     * @return WindowManager.LayoutParams
     */
    private static WindowManager.LayoutParams buildImageToWindowParams(Activity activity) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.width = DisplayUtils.dp2px(IMAGE_DP_SIZE);
        params.height = DisplayUtils.dp2px(IMAGE_DP_SIZE);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        return params;
    }

    /**
     * 重启APP
     *
     * @param context 上下文
     */
    public static void restartApp(final Context context) {
        ToastUtils.showShort("应用重启中");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName());
                PendingIntent restartIntent = PendingIntent.getActivity(context,
                        0, intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC,
                        System.currentTimeMillis() + 200, restartIntent);
                System.exit(0);
            }
        }, 500);
    }

    /**
     * 环境开关点击监听
     */
    public interface OnEnvironmentSwitchClickListener {
        /**
         * 触发点击
         */
        void onClick();
    }

}
