package com.appdsn.commoncore.widget.smartindicator.tabview;

import android.content.Context;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 带文本的指示器标题
 *
 * @author wangbaozhong
 */
public class ImageTabView implements ITabView {
    private ImageView mImageView;
    private int mSelectedDrawable;
    private int mNormalDrawable;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private LinearLayout.LayoutParams mLayoutParams;
    private ImageView.ScaleType mScaleType;

    private void init(ImageView imageView) {
        imageView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        if (mScaleType != null) {
            imageView.setScaleType(mScaleType);
        }
        if (mLayoutParams != null) {
            imageView.setLayoutParams(mLayoutParams);
        }
    }

    @Override
    public View createTabView(Context context, int position, LinearLayout parrent) {
        mImageView = new ImageView(context);
        init(mImageView);
        return mImageView;
    }

    @Override
    public void onSelected(View tabView, int position, RectF barRectF) {
        if (mSelectedDrawable > 0) {
            mImageView.setImageResource(mSelectedDrawable);
        }
    }

    @Override
    public void onDeselected(View tabView, int position) {
        if (mNormalDrawable > 0) {
            mImageView.setImageResource(mNormalDrawable);
        }
    }

    @Override
    public void onScroll(View tabView, int position, float selectPercent, RectF barRectF) {
    }

    public void setSelectedDrawable(int drawableRes) {
        mSelectedDrawable = drawableRes;
    }

    public void setNormalDrawable(int drawableRes) {
        mNormalDrawable = drawableRes;
    }

    public void setDrawable(int drawableRes) {
        setNormalDrawable(drawableRes);
        setSelectedDrawable(drawableRes);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        mPaddingTop = top;
        mPaddingBottom = bottom;
    }

    public void setLayoutParams(LinearLayout.LayoutParams params) {
        mLayoutParams = params;
    }
}
