package com.appdsn.commoncore.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Desc:空状态View管理类
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:09
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class EmptyLayout extends FrameLayout {
    private View mLoadingView;
    private View mErrorView;
    private View mContentView;
    private View mEmptyView;
    private LayoutInflater mInflater;
    private static final String TAG = EmptyLayout.class.getSimpleName();

    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EmptyLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        View view = getChildAt(0);
        setContentView(view);
    }

    public static EmptyLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

    public static EmptyLayout wrap(Activity activity) {
        return wrap(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static EmptyLayout wrap(View contentView) {
        if (contentView == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup contentParent = (ViewGroup) contentView.getParent();
        Context context = contentView.getContext();
        if (contentParent == null || context == null) {
            throw new RuntimeException("parent view can not be null");
        }

        int index = contentParent.indexOfChild(contentView);
        View oldContent = contentView;
        contentParent.removeView(oldContent);

        //setup content layout
        EmptyLayout emptyLayout = new EmptyLayout(context);
        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(emptyLayout, index, lp);
        emptyLayout.addContentView(oldContent);

        return emptyLayout;
    }

    public void showLoading() {
        if (isMainThread()) {
            showView(mLoadingView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mLoadingView);
                }
            });
        }
    }

    public void showError() {
        if (isMainThread()) {
            showView(mErrorView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mErrorView);
                }
            });
        }

    }

    public void showContent() {
        if (isMainThread()) {
            showView(mContentView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mContentView);
                }
            });
        }
    }

    public void showEmpty() {
        if (isMainThread()) {
            showView(mEmptyView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mEmptyView);
                }
            });
        }
    }


    private void showView(View view) {
        if (view == null) return;

        if (view == mLoadingView) {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mErrorView != null)
                mErrorView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mErrorView) {
            mErrorView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mContentView) {
            mContentView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mErrorView != null)
                mErrorView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mEmptyView) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mErrorView != null)
                mErrorView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        }
    }

    public void setLoadingView(int layoutId) {
        setLoadingView(mInflater.inflate(layoutId, null));
    }

    public void setEmptyView(int layoutId) {
        setEmptyView(mInflater.inflate(layoutId, null));
    }

    public void setErrorView(int layoutId) {
        setErrorView(mInflater.inflate(layoutId, null));
    }

    public void setLoadingView(View view) {
        if (view == null) {
            return;
        }
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        view.setVisibility(GONE);
        addView(view);
        mLoadingView = view;
    }

    public void setEmptyView(View view) {
        if (view == null) {
            return;
        }
        View emptyView = mEmptyView;
        if (emptyView != null) {
            removeView(emptyView);
        }
        view.setVisibility(GONE);
        addView(view);
        mEmptyView = view;
    }

    public void setErrorView(View view) {
        if (view == null) {
            return;
        }
        View retryView = mErrorView;
        if (retryView != null) {
            removeView(retryView);
        }
        view.setVisibility(GONE);
        addView(view);
        mErrorView = view;
    }

    public void addContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            removeView(contentView);
        }
        addView(view);
        mContentView = view;
    }

    public void setContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            removeView(contentView);
        }
        mContentView = view;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
