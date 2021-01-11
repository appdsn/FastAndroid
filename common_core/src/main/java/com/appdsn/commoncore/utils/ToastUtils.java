package com.appdsn.commoncore.utils;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Desc:可自定义toast布局显示样式
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 11:17
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class ToastUtils {
    private static ToastConfig sToastConfig = new ToastConfig();
    private static Toast sToast;

    public static class ToastConfig {
        private int mGravity = -1;
        private int mXOffset = -1;
        private int mYOffset = -1;
        private int mBgResource = -1;
        private int mMsgColor = -1;
        private int mMsgTextSize = -1;
        private int mMsgTextId = -1;
        private View mCustomView = null;

        public static ToastConfig create() {
            return new ToastConfig();
        }

        public ToastConfig setsGravity(final int gravity, final int xOffset, final int yOffset) {
            mGravity = gravity;
            mXOffset = xOffset;
            mYOffset = yOffset;
            return this;
        }

        public ToastConfig setsBgResource(@DrawableRes final int bgResource) {
            this.mBgResource = bgResource;
            return this;
        }

        public ToastConfig setsMsgColor(int msgColor) {
            this.mMsgColor = msgColor;
            return this;
        }

        public ToastConfig setsMsgTextSize(int textSize) {
            this.mMsgTextSize = textSize;
            return this;
        }

        public ToastConfig setsCustomView(View customView, int textId) {
            this.mCustomView = customView;
            this.mMsgTextId = textId;
            return this;
        }

        public ToastConfig setsCustomView(int customLayout, int textId) {
            this.mCustomView = View.inflate(ContextUtils.getContext(), customLayout, null);
            this.mMsgTextId = textId;
            return this;
        }
    }

    public static void init(ToastConfig config) {
        sToastConfig = config;
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(final CharSequence text) {
        showToast(text == null ? "null" : text, Toast.LENGTH_SHORT, sToastConfig.mCustomView, sToastConfig.mMsgTextId);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(final CharSequence text) {
        showToast(text == null ? "null" : text, Toast.LENGTH_LONG, sToastConfig.mCustomView, sToastConfig.mMsgTextId);
    }

    public static void showCustomShort(@LayoutRes final int layoutId) {
        showToast("", Toast.LENGTH_SHORT, getView(layoutId), -1);
    }

    /**
     * Show custom toast for a short period of time.
     *
     * @param view The view of toast.
     */
    public static void showCustomShort(final View view) {
        showToast("", Toast.LENGTH_SHORT, view, -1);
    }

    /**
     * Show custom toast for a long period of time.
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    public static void showCustomLong(@LayoutRes final int layoutId) {
        showToast("", Toast.LENGTH_LONG, getView(layoutId), -1);
    }

    /**
     * Show custom toast for a long period of time.
     *
     * @param view The view of toast.
     */
    public static void showCustomLong(final View view) {
        showToast("", Toast.LENGTH_LONG, view, -1);
    }

    /**
     * Cancel the toast.
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    private static void showToast(final CharSequence text, final int duration, final View customView, final int textId) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sToast != null) {
                    sToast.cancel();
                }
                sToast = makeToast(duration, customView);
                final View toastView = sToast.getView();
                if (toastView == null) {
                    return;
                }
                if (customView == null) {
                    final TextView tvMessage = toastView.findViewById(android.R.id.message);
                    tvMessage.setText(text);
                    if (sToastConfig.mMsgColor != -1) {
                        tvMessage.setTextColor(sToastConfig.mMsgColor);
                    }
                    if (sToastConfig.mMsgTextSize != -1) {
                        tvMessage.setTextSize(sToastConfig.mMsgTextSize);
                    }
                    if (sToastConfig.mBgResource != -1) {
                        tvMessage.setBackgroundColor(Color.TRANSPARENT);
                        toastView.setBackgroundResource(sToastConfig.mBgResource);
                    }
                } else {
                    try {
                        final TextView tvMessage = toastView.findViewById(textId);
                        if (tvMessage != null) {
                            tvMessage.setText(text);
                        }
                    } catch (Exception e) {
                    }
                }
                if (sToastConfig.mGravity != -1 || sToastConfig.mXOffset != -1 || sToastConfig.mYOffset != -1) {
                    sToast.setGravity(sToastConfig.mGravity, sToastConfig.mXOffset, sToastConfig.mYOffset);
                }
                sToast.show();
            }
        });
    }

    private static Toast makeToast(int duration, View customView) {
        Toast toast = null;
        if (customView == null) {
            toast = Toast.makeText(ContextUtils.getContext(), "", duration);
        } else {
            toast = new Toast(ContextUtils.getContext());
            toast.setDuration(duration);
            toast.setView(customView);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                //noinspection JavaReflectionMemberAccess
                Field mTNField = Toast.class.getDeclaredField("mTN");
                mTNField.setAccessible(true);
                Object mTN = mTNField.get(toast);
                Field mTNmHandlerField = mTNField.getType().getDeclaredField("mHandler");
                mTNmHandlerField.setAccessible(true);
                Handler tnHandler = (Handler) mTNmHandlerField.get(mTN);
                mTNmHandlerField.set(mTN, new SafeHandler(tnHandler));
            } catch (Exception ignored) {/**/}
        }
        return toast;
    }

    private static class SafeHandler extends Handler {
        private Handler impl;

        SafeHandler(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                impl.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }
    }

    private static View getView(@LayoutRes final int layoutId) {
        try {
            return View.inflate(ContextUtils.getContext(), layoutId, null);
        } catch (Exception ignore) {

        }
        return null;
    }
}
