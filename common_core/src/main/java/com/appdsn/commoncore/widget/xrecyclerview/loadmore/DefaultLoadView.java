package com.appdsn.commoncore.widget.xrecyclerview.loadmore;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdsn.commoncore.R;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/15 19:44
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public class DefaultLoadView extends ILoadView {
    private ProgressBar mProgressBar;
    private TextView mHintView;
    private View mLoadLayout;//用于滑到底部自动加载的Footer
    private String mNoDataText = "没有更多数据了";
    private String mNormalText = "上拉自动加载更多";
    private String mLoadingText = "正在加载更多...";
    private String mFailedText = "加载失败，点击重新加载";

    @Override
    public int getLayoutResId() {
        return R.layout.common_layout_load_more_footer;
    }

    @Override
    public void initView(View loadView) {
        mLoadLayout = loadView;
        mProgressBar = (ProgressBar) loadView.findViewById(R.id.loadProgress);
        mHintView = (TextView) loadView.findViewById(R.id.loadHint);
    }

    @Override
    public void onStateChanged(LoadState newState) {
        switch (newState) {
            case NORMAL:
                mProgressBar.setVisibility(View.GONE);
                mHintView.setText(mNormalText);
                break;
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mHintView.setText(mLoadingText);
                break;
            case NO_MORE_DATA:
                mProgressBar.setVisibility(View.GONE);
                mHintView.setText(mNoDataText);
                break;
            case FAILED:
                mProgressBar.setVisibility(View.GONE);
                mHintView.setText(mFailedText);
                break;
            default:
                break;
        }
    }

    public void setNoDataText(String noDataText) {
        this.mNoDataText = noDataText;
    }

    public void setNormalText(String normalText) {
        this.mNormalText = normalText;
    }

    public void setLoadingText(String loadingText) {
        this.mLoadingText = loadingText;
    }

    public void setFailedText(String failedText) {
        this.mFailedText = failedText;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public TextView getHintView() {
        return mHintView;
    }

    public View getLoadView() {
        return mLoadLayout;
    }
}
