package com.appdsn.commoncore.widget.smartindicator.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.BaseScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.LineScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;

/**
 * Desc:圆形滑块和tab指示器
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/25 14:55
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class CircleIndicatorAdapter implements IndicatorAdapter {
    private float mNormalRadius;
    private Paint mPaint;
    private int mCount;
    private int mFrontColor = Color.RED;
    private float mSelectedRadius;
    private boolean mScale;
    private int mBackColor;

    public CircleIndicatorAdapter(Context context) {
        mNormalRadius = dip2px(context, 3);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onAttachToIndicator(Context context, SmartIndicator indicator) {
        indicator.setFixEnableAsync(true);
        indicator.setScrollBarFront(true);
    }

    @Override
    public ITabView getTabView(Context context, int position, LinearLayout parrent) {
        return new CircleView(context);
    }

    @Override
    public IScrollBar getScrollBar(Context context) {
        if (!mScale) {
            LineScrollBar scrollBar = new LineScrollBar(context);
            scrollBar.setHeight((int) (mNormalRadius * 2));
            scrollBar.setWidthFix((int) (mNormalRadius * 2));
            scrollBar.setColor(mFrontColor);
            scrollBar.setRoundRadius((int) mNormalRadius);
            scrollBar.setGravity(BaseScrollBar.Gravity.CENTER, 0);
            return scrollBar;
        }
        return null;
    }

    @Override
    public int getTabCount() {
        return mCount;
    }

    public void setCircleCount(int count) {
        mCount = count;
    }

    public void setCircleRadius(float radius) {
        if (radius > 0) {
            mNormalRadius = radius;
        }
    }

    public void setScaleRadius(float radius) {
        if (radius > 0) {
            mScale = true;
            mSelectedRadius = radius;
        }
    }

    public void setBackCircleColor(int color) {
        mBackColor = color;
        mPaint.setColor(color);
    }

    public void setStrokeWidth(int strokeWidth) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setFrontCircleColor(int color) {
        mFrontColor = color;
    }

    private class CircleView extends View implements ITabView {
        private float mCurRadius;
        private int mCurColor;

        public CircleView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mScale) {
                mPaint.setColor(mCurColor);
            }
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCurRadius, mPaint);
        }

        @Override
        public View createTabView(Context context, int position, LinearLayout parrent) {
            return this;
        }

        @Override
        public void onSelected(View tabView, int position, RectF barRectF) {
            if (mSelectedRadius <= 0) {
                mSelectedRadius = mNormalRadius;
            }
            mCurRadius = mSelectedRadius;
            if (mScale) {
                mCurColor = eval(1, mBackColor, mFrontColor);
            }
            invalidate();
        }

        @Override
        public void onDeselected(View tabView, int position) {
            mCurRadius = mNormalRadius;
            if (mScale) {
                mCurColor = eval(0, mBackColor, mFrontColor);
            }
            invalidate();
        }

        @Override
        public void onScroll(View tabView, int position, float selectPercent, RectF barRectF) {
            if (mSelectedRadius <= 0) {
                mSelectedRadius = mNormalRadius;
            }
            mCurRadius = mNormalRadius + (mSelectedRadius - mNormalRadius) * selectPercent;
            mCurColor = eval(selectPercent, mBackColor, mFrontColor);
            invalidate();
        }
    }

    public static int eval(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        int currentA = (startA + (int) (fraction * (endA - startA))) << 24;
        int currentR = (startR + (int) (fraction * (endR - startR))) << 16;
        int currentG = (startG + (int) (fraction * (endG - startG))) << 8;
        int currentB = startB + (int) (fraction * (endB - startB));

        return currentA | currentR | currentG | currentB;
    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
