package com.appdsn.commoncore.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Desc:base类的根布局，支持设置onInterceptTouchEvent事件监听
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:19
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class RootLayout extends RelativeLayout {

    private OnInterceptTouchEventListener mOnInterceptTouchEventListener;

    public RootLayout(Context context) {
        super(context);
    }

    public RootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mOnInterceptTouchEventListener != null) {
            return mOnInterceptTouchEventListener.onInterceptTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setOnInterceptTouchEventListener(OnInterceptTouchEventListener onInterceptTouchEventListener) {
        mOnInterceptTouchEventListener = onInterceptTouchEventListener;
    }

    public interface OnInterceptTouchEventListener {
        boolean onInterceptTouchEvent(MotionEvent event);
    }
}
