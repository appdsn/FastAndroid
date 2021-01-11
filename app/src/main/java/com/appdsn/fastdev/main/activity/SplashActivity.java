package com.appdsn.fastdev.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.utils.statistics.PageStatisticsEvent;
import com.appdsn.commoncore.utils.ThreadUtils;
import com.appdsn.fastdev.R;
import com.appdsn.fastdev.main.presenter.SplashPresenter;

public class SplashActivity extends BaseAppActivity<SplashActivity, SplashPresenter> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        hideTitleBar();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public PageStatisticsEvent getPageEvent() {
        return PageStatisticsEvent.empty;
    }

    @Override
    protected void loadData() {
        ThreadUtils.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }
}
