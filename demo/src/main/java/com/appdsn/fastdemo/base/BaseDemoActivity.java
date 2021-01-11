package com.appdsn.fastdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.fastdemo.R;


public class BaseDemoActivity extends BaseAppActivity {

    private View mBtnTitleBar;
    private View mBtnEmpty;
    private View mBtnIntro;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_demo;
    }

    @Override
    protected void initVariable(Intent intent) {
        setCenterTitle("base基类使用");
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        mBtnTitleBar = findViewById(R.id.btnTitleBar);
        mBtnEmpty = findViewById(R.id.btnEmpty);
        mBtnIntro = findViewById(R.id.btnIntro);
    }

    @Override
    protected void setListener() {
        mBtnTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TitleBarDemoActivity.class);
            }
        });

        mBtnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EmptyDemoActivity.class);
            }
        });

        mBtnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BaseIntroDemoActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
