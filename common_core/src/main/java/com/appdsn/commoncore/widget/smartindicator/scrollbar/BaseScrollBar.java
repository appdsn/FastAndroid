package com.appdsn.commoncore.widget.smartindicator.scrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.appdsn.commoncore.widget.smartindicator.model.TabPositionInfo;


/**
 * @Author: wangbaozhong
 * @Date: 2019/11/29 16:21
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public abstract class BaseScrollBar extends View implements IScrollBar {
    protected Paint mPaint;
    protected int mHeight;
    protected int mWidth;
    protected boolean isFixWidth;
    protected float mYOffset; // 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
    protected Gravity mGravity = Gravity.BOTTOM;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected boolean mScrollEnable = true;
    // 控制动画
    protected Interpolator mStartInterpolator = new LinearInterpolator();
    protected Interpolator mEndInterpolator = new LinearInterpolator();
    protected RectF mRect = new RectF();

    public BaseScrollBar(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setGravity(Gravity gravity, float yOffset) {
        mGravity = gravity;
        mYOffset = yOffset;
    }

    public void setHeight(int barHeight) {
        if (barHeight > 0) {
            this.mHeight = barHeight;
        }
    }

    /**
     * 默认宽度是tabview的宽度，会随着手指滑动变化，设置固定值后，不会变化
     */
    public void setWidthFix(int barWidth) {
        if (barWidth > 0) {
            this.mWidth = barWidth;
            isFixWidth = true;
        }
    }

    /**
     * 默认宽度是tabview的宽度，会随着手指滑动变化，设置固定值后，不会变化
     */
    public void setWidthWrapContent(int paddingLeft, int paddingRight) {
        isFixWidth = false;
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 是否有滑动变化效果，false在滑动过程中不变化，只有选中后变成最终的样式
     */
    public void setScrollEnable(boolean enable) {
        this.mScrollEnable = enable;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        if (startInterpolator != null) {
            mStartInterpolator = startInterpolator;
        }
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        if (endInterpolator != null) {
            mEndInterpolator = endInterpolator;
        }
    }

    @Override
    public RectF onScroll(TabPositionInfo curTabInfo, TabPositionInfo nextTabInfo, float positionOffset) {
        if (curTabInfo == null || nextTabInfo == null) {
            return null;
        }
        if (!mScrollEnable) {
            return null;
        }
        if (mHeight <= 0) {
            mHeight = getHeight();
        }

        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;

        if (isFixWidth) {
            leftX = curTabInfo.getLeft() + (curTabInfo.getWidth() - mWidth) / 2;
            nextLeftX = nextTabInfo.getLeft() + (nextTabInfo.getWidth() - mWidth) / 2;
            rightX = curTabInfo.getLeft() + (curTabInfo.getWidth() + mWidth) / 2;
            nextRightX = nextTabInfo.getLeft() + (nextTabInfo.getWidth() + mWidth) / 2;
        } else {
            leftX = curTabInfo.getLeft() + mPaddingLeft;
            nextLeftX = nextTabInfo.getLeft() + mPaddingLeft;
            rightX = curTabInfo.getRight() - mPaddingRight;
            nextRightX = nextTabInfo.getRight() - mPaddingRight;
        }
        mRect.left = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mRect.right = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);

        if (mGravity == Gravity.CENTER) {
            mRect.top = getHeight() / 2 - mHeight / 2 + mYOffset;
        } else if (mGravity == Gravity.TOP) {
            mRect.top = getTop() + mYOffset;
        } else {
            mRect.top = getHeight() - mHeight + mYOffset;
        }
        mRect.bottom = mRect.top + mHeight;

        invalidate();

        return mRect;
    }

    @Override
    public RectF onSelected(TabPositionInfo curTabInfo) {
        if (mScrollEnable) {
            return null;
        }
        if (mHeight <= 0) {
            mHeight = getHeight();
        }
        float leftX;
        float rightX;

        if (isFixWidth) {
            leftX = curTabInfo.getLeft() + (curTabInfo.getWidth() - mWidth) / 2;
            rightX = curTabInfo.getLeft() + (curTabInfo.getWidth() + mWidth) / 2;
        } else {
            leftX = curTabInfo.getLeft() + mPaddingLeft;
            rightX = curTabInfo.getRight() - mPaddingRight;
        }

        mRect.left = leftX;
        mRect.right = rightX;

        if (mGravity == Gravity.CENTER) {
            mRect.top = getHeight() / 2 - mHeight / 2 + mYOffset;
        } else if (mGravity == Gravity.TOP) {
            mRect.top = getTop() + mYOffset;
        } else {
            mRect.top = getHeight() - mHeight + mYOffset;
        }
        mRect.bottom = mRect.top + mHeight;

        invalidate();
        return mRect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawScrollBar(canvas, mRect, mPaint);
    }

    /**
     * 子类画具体的指示器样式
     *
     * @param canvas
     * @param rect
     */
    protected abstract void drawScrollBar(Canvas canvas, RectF rect, Paint paint);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public enum Gravity {
        BOTTOM,
        CENTER,
        TOP
    }
}
