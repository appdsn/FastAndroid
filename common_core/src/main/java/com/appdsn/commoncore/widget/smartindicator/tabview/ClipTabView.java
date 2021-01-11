package com.appdsn.commoncore.widget.smartindicator.tabview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 类似今日头条切换效果的指示器标题
 * Created by wangbaozhong on 2019/11/26
 */
public class ClipTabView extends View implements ITabView {
    private String mText;
    private int mTextColor;
    private int mClipColor;
    private Paint mPaint;
    private Rect mTextBounds = new Rect();
    private RectF mBarRectF;

    public ClipTabView(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int textSize = dip2px(context, 16);
        mPaint.setTextSize(textSize);
    }

    @Override
    public View createTabView(Context context, int position, LinearLayout parrent) {
        int padding = dip2px(context, 10);
        setPadding(padding, 0, padding, 0);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureTextBounds();
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int width = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                result = Math.min(width, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                result = Math.min(height, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = (getWidth() - mTextBounds.width()) / 2;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int y = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

        // 画底层
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, x, y, mPaint);

        if (mBarRectF != null) {
            // 画clip层
            canvas.save();
            RectF clipRectF = new RectF();
            if (getLeft() > mBarRectF.left) {
                clipRectF.left = 0;
                clipRectF.right = getWidth() - (getRight() - mBarRectF.right);
            } else {
                clipRectF.left = mBarRectF.left - getLeft();
                clipRectF.right = getWidth() - (getRight() - mBarRectF.right);
            }
            clipRectF.top = mBarRectF.top - getTop();
            clipRectF.bottom = getHeight() - (getBottom() - mBarRectF.bottom);
            canvas.clipRect(clipRectF);
            mPaint.setColor(mClipColor);
            canvas.drawText(mText, x, y, mPaint);
            canvas.restore();
        }
    }

    @Override
    public void onSelected(View tabView, int position, RectF barRectF) {
        if (barRectF != null) {
            mBarRectF = barRectF;
            invalidate();
        }
    }

    @Override
    public void onDeselected(View tabView, int position) {
        mBarRectF = null;
        invalidate();
    }

    @Override
    public void onScroll(View tabView, int position, float selectPercent, RectF barRectF) {
        if (barRectF != null) {
            mBarRectF = barRectF;
            invalidate();
        }
    }

    private void measureTextBounds() {
        mPaint.getTextBounds(mText, 0, mText == null ? 0 : mText.length(), mTextBounds);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(dip2px(getContext(), textSize));
        requestLayout();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public int getClipColor() {
        return mClipColor;
    }

    public void setClipColor(int clipColor) {
        mClipColor = clipColor;
        invalidate();
    }


    public int getContentLeft() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }


    public int getContentTop() {
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }


    public int getContentRight() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }


    public int getContentBottom() {
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    public int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
