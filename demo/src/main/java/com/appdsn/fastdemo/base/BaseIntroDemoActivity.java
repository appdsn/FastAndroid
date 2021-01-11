package com.appdsn.fastdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.fastdemo.R;


public class BaseIntroDemoActivity extends BaseAppActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_intro_demo;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("base基类使用介绍");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }
}
