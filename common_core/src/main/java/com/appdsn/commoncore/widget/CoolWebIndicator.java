package com.appdsn.commoncore.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.coolindicator.sdk.CoolIndicator;
import com.just.agentweb.AgentWebUtils;
import com.just.agentweb.BaseIndicatorView;

/**
 * web加载进度指示器
 *
 * @Author: wangbaozhong
 * @Date: 2020/10/22 10:19
 */
public class CoolWebIndicator extends BaseIndicatorView {

    private static final String TAG = CoolWebIndicator.class.getSimpleName();
    private CoolIndicator mCoolIndicator = null;


    public CoolWebIndicator(Context context) {
        this(context, null);
    }

    public CoolWebIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CoolWebIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCoolIndicator = CoolIndicator.create((Activity) context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCoolIndicator.setProgressDrawable(context.getResources().getDrawable(com.coolindicator.sdk.R.drawable.default_drawable_indicator, context.getTheme()));
        } else {
            mCoolIndicator.setProgressDrawable(context.getResources().getDrawable(com.coolindicator.sdk.R.drawable.default_drawable_indicator));
        }

        this.addView(mCoolIndicator, offerLayoutParams());

    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
        mCoolIndicator.start();
    }

    @Override
    public void setProgress(int newProgress) {
    }

    @Override
    public void hide() {
        mCoolIndicator.complete();
    }

    @Override
    public LayoutParams offerLayoutParams() {
        return new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, AgentWebUtils.dp2px(getContext(), 3));
    }
}