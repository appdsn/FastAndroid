package com.appdsn.fastdemo.http;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.fastdemo.R;

public class HttpDemoActivity extends BaseAppActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_http_demo;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("Http请求使用介绍");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }
}
