package com.appdsn.commoncore.widget.smartindicator.scrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.appdsn.commoncore.widget.smartindicator.model.TabPositionInfo;


/**
 * 贝塞尔曲线ViewPager指示器
 */
public class BezierScrollBar extends View implements IScrollBar {
    protected Paint mPaint;
    protected float mYOffset; // 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
    protected BaseScrollBar.Gravity mGravity = BaseScrollBar.Gravity.BOTTOM;
    protected Interpolator mStartInterpolator = new AccelerateInterpolator();
    protected Interpolator mEndInterpolator = new DecelerateInterpolator();

    private float mLeftCircleRadius;
    private float mLeftCircleX;
    private float mRightCircleRadius;
    private float mRightCircleX;
    private float mCircleY;

    private float mMaxCircleRadius;
    private float mMinCircleRadius;
    private Path mPath = new Path();

    public BezierScrollBar(Context context) {
        super(context);
        mMaxCircleRadius = dip2px(3.5);
        mMinCircleRadius = dip2px(2);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setGravity(BaseScrollBar.Gravity gravity, float yOffset) {
        mGravity = gravity;
        mYOffset = yOffset;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setMaxCircleRadius(float maxCircleRadius) {
        mMaxCircleRadius = maxCircleRadius;
    }

    public void setMinCircleRadius(float minCircleRadius) {
        mMinCircleRadius = minCircleRadius;
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
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mLeftCircleX, mCircleY, mLeftCircleRadius, mPaint);
        canvas.drawCircle(mRightCircleX, mCircleY, mRightCircleRadius, mPaint);
        drawBezierCurve(canvas);
    }

    /**
     * 绘制贝塞尔曲线
     */
    private void drawBezierCurve(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(mRightCircleX, mCircleY);
        mPath.lineTo(mRightCircleX, mCircleY - mRightCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, mCircleY, mLeftCircleX, mCircleY - mLeftCircleRadius);
        mPath.lineTo(mLeftCircleX, mCircleY + mLeftCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, mCircleY, mRightCircleX, mCircleY + mRightCircleRadius);
        mPath.close();  // 闭合
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public RectF onScroll(TabPositionInfo curTabInfo, TabPositionInfo nextTabInfo, float positionOffset) {
        if (curTabInfo == null || nextTabInfo == null) {
            return null;
        }

        float leftX = curTabInfo.getLeft() + (curTabInfo.getRight() - curTabInfo.getLeft()) / 2;
        float rightX = nextTabInfo.getLeft() + (nextTabInfo.getRight() - nextTabInfo.getLeft()) / 2;
        mLeftCircleX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mRightCircleX = leftX + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset);
        mLeftCircleRadius = mMaxCircleRadius + (mMinCircleRadius - mMaxCircleRadius) * mEndInterpolator.getInterpolation(positionOffset);
        mRightCircleRadius = mMinCircleRadius + (mMaxCircleRadius - mMinCircleRadius) * mStartInterpolator.getInterpolation(positionOffset);

        if (mGravity == BaseScrollBar.Gravity.CENTER) {
            mCircleY = getHeight() / 2 + mYOffset;
        } else if (mGravity == BaseScrollBar.Gravity.TOP) {
            mCircleY = getTop() + mMaxCircleRadius + mYOffset;
        } else {
            mCircleY = getHeight() - mMaxCircleRadius + mYOffset;
        }
        invalidate();

        return null;
    }

    @Override
    public RectF onSelected(TabPositionInfo curTabInfo) {
        return null;
    }

    private int dip2px(double dpValue) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
