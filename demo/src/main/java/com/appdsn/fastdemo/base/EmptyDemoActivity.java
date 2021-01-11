package com.appdsn.fastdemo.base;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdsn.commoncore.base.BaseMVPActivity;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.fastdemo.R;


public class EmptyDemoActivity extends BaseMVPActivity implements View.OnClickListener {

    private View mBtnSetEmpty;
    private View mBtnShowEmpty;
    private View mBtnShowError;
    private View mBtnShowLoading;
    private View mBtnShowContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_empty_demo;
    }

    @Override
    protected void initVariable(Intent intent) {
        setCenterTitle("各种空状态使用");
        setLeftButton(R.drawable.base_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        mBtnSetEmpty = findViewById(R.id.btnSetEmpty);
        mBtnShowEmpty = findViewById(R.id.btnShowEmpty);
        mBtnShowError = findViewById(R.id.btnShowError);
        mBtnShowLoading = findViewById(R.id.btnShowLoading);
        mBtnShowContent = findViewById(R.id.btnShowContent);
    }

    @Override
    protected void setListener() {
        mBtnSetEmpty.setOnClickListener(this);
        mBtnShowEmpty.setOnClickListener(this);
        mBtnShowError.setOnClickListener(this);
        mBtnShowLoading.setOnClickListener(this);
        mBtnShowContent.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetEmpty:
                initErrorView();
                initEmptyView();
                initLoadingView();
                ToastUtils.showShort("设置成功，可以显示了");
                break;

            case R.id.btnShowEmpty:
                showEmptyView();
                break;

            case R.id.btnShowError:
                showErrorView();
                break;

            case R.id.btnShowLoading:
                showLoadingView();
                break;

            case R.id.btnShowContent:
                showContentView();
                break;
        }
    }

    private void initErrorView() {
        View errorView = mInflater.inflate(R.layout.common_empty_view, null);
        ImageView mErrorImg = errorView.findViewById(R.id.ivImg);
        TextView mErrorDesc = errorView.findViewById(R.id.tvDesc);
        mErrorDesc.setText("暂时没有网络啦~");
        mErrorImg.setImageResource(R.drawable.common_bg_no_network);
        mErrorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击了");
            }
        });

        setErrorView(errorView);
    }

    private void initEmptyView() {
        View emptyView = mInflater.inflate(R.layout.common_empty_view, null);
        ImageView mEmptyImg = emptyView.findViewById(R.id.ivImg);
        TextView mEmptyDesc = emptyView.findViewById(R.id.tvDesc);
        mEmptyDesc.setText("暂时没有消息呀~");
        mEmptyImg.setImageResource(R.drawable.common_bg_no_data);
        mEmptyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击了");
            }
        });

        setEmptyView(emptyView);
    }

    private void initLoadingView() {
        View loadingView = mInflater.inflate(R.layout.common_loading_view, null);
        ImageView loadingIv = loadingView.findViewById(R.id.iv_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingIv.getDrawable();
        if (animationDrawable != null) {
            animationDrawable.start();
        }

        setLoadingView(loadingView);
    }
}
