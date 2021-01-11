package com.appdsn.commoncore.widget.smartindicator.tabview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 带文本的指示器标题
 *
 * @author wangbaozhong
 */
public class TextTabView implements ITabView {
    private TextView mTextView;
    private Typeface mSelectedTypeface = Typeface.DEFAULT;
    private Typeface mNormalTypeface = Typeface.DEFAULT;
    protected int mSelectedColor = Color.BLACK;
    protected int mNormalColor = Color.BLACK;
    private String mSelectedText;
    private String mNormalText;
    private float mSelectedSize = 14;
    private float mNormalSize = 14;
    private int mSelectedDrawable;
    private int mNormalDrawable;
    private int mSelectedDrawablePosition;
    private int mNormalDrawablePosition;
    private boolean mScrollScale;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private LinearLayout.LayoutParams mLayoutParams;

    private void init(TextView textView) {
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(mNormalSize);
        textView.setTextColor(mNormalColor);
        textView.setText(mNormalText);
        if (mLayoutParams != null) {
            textView.setLayoutParams(mLayoutParams);
        }
    }

    @Override
    public View createTabView(Context context, int position, LinearLayout parrent) {
        mTextView = new TextView(context);
        init(mTextView);
        return mTextView;
    }

    @Override
    public void onSelected(View tabView, int position, RectF barRectF) {
        TextView textView = mTextView;
        textView.setTextColor(mSelectedColor);
        textView.setText(mSelectedText);
        textView.setTypeface(mSelectedTypeface);
        if (!mScrollScale) {
            textView.setScaleX(mSelectedSize / mNormalSize);
            textView.setScaleY(mSelectedSize / mNormalSize);
        }
        if (mSelectedDrawable > 0) {
            switch (mSelectedDrawablePosition) {
                case Gravity.LEFT:
                    textView.setCompoundDrawablesWithIntrinsicBounds(mSelectedDrawable, 0, 0, 0);
                    break;
                case Gravity.RIGHT:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, mSelectedDrawable, 0);
                    break;
                case Gravity.BOTTOM:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, mSelectedDrawable);
                    break;
                case Gravity.TOP:
                default:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, mSelectedDrawable, 0, 0);
                    break;
            }
        }
    }

    @Override
    public void onDeselected(View tabView, int position) {
        TextView textView = mTextView;
        textView.setTextColor(mNormalColor);
        textView.setText(mNormalText);
        textView.setTypeface(mNormalTypeface);
        if (!mScrollScale) {
            textView.setScaleX(1);
            textView.setScaleY(1);
        }
        if (mNormalDrawable > 0) {
            switch (mNormalDrawablePosition) {
                case Gravity.LEFT:
                    textView.setCompoundDrawablesWithIntrinsicBounds(mNormalDrawable, 0, 0, 0);
                    break;
                case Gravity.RIGHT:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, mNormalDrawable, 0);
                    break;
                case Gravity.BOTTOM:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, mNormalDrawable);
                    break;
                case Gravity.TOP:
                default:
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, mNormalDrawable, 0, 0);
                    break;
            }
        }
    }

    @Override
    public void onScroll(View tabView, int position, float selectPercent, RectF barRectF) {
        TextView textView = mTextView;
        if (mScrollScale) {
            float curSize = 1 + (mSelectedSize / mNormalSize - 1) * selectPercent;
            textView.setScaleX(curSize);
            textView.setScaleY(curSize);
        }
    }

    public void setText(String text) {
        mNormalText = mSelectedText = text;
    }

    public void setSelectedText(String text) {
        mSelectedText = text;
    }

    public void setNormalText(String text) {
        mNormalText = text;
    }

    public void setTextColor(int color) {
        mNormalColor = mSelectedColor = color;
    }

    public void setSelectedTextColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setNormalTextColor(int normalColor) {
        mNormalColor = normalColor;
    }

    public void setSelectedTypeface(Typeface typeface) {
        if (typeface != null) {
            mSelectedTypeface = typeface;
        }
    }

    public void setNormalTypeface(Typeface typeface) {
        if (typeface != null) {
            mNormalTypeface = typeface;
        }
    }

    public void setTextSize(float textSize) {
        if (textSize > 0) {
            mNormalSize = mSelectedSize = textSize;
        }
    }

    public void setSelectedTextSize(float textSize) {
        if (textSize > 0) {
            mSelectedSize = textSize;
        }
    }

    public void setNormalTextSize(float textSize) {
        if (textSize > 0) {
            mNormalSize = textSize;
        }
    }

    /**
     * position图片位置：Gravity.LEFT，Gravity.RIGHT, Gravity.TOP,Gravity.BOTTOM
     */
    public void setSelectedDrawable(int drawableRes, int position) {
        mSelectedDrawable = drawableRes;
        mSelectedDrawablePosition = position;
    }

    /**
     * position图片位置：Gravity.LEFT，Gravity.RIGHT, Gravity.TOP,Gravity.BOTTOM
     */
    public void setNormalDrawable(int drawableRes, int position) {
        mNormalDrawable = drawableRes;
        mNormalDrawablePosition = position;
    }

    /**
     * position图片位置：Gravity.LEFT，Gravity.RIGHT, Gravity.TOP,Gravity.BOTTOM
     */
    public void setDrawable(int drawableRes, int position) {
        setNormalDrawable(drawableRes, position);
        setSelectedDrawable(drawableRes, position);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        mPaddingTop = top;
        mPaddingBottom = bottom;
    }

    /*是否支持滚动过程中缩放字体大小*/
    public void setScrollScale(boolean scrollScale) {
        mScrollScale = scrollScale;
    }

    public void setLayoutParams(LinearLayout.LayoutParams params) {
        mLayoutParams = params;
    }
}
