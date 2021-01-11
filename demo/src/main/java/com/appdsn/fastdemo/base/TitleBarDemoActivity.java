package com.appdsn.fastdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.fastdemo.R;

public class TitleBarDemoActivity extends BaseAppActivity implements View.OnClickListener {

    private View mBtnSetLeft;
    private View mBtnAddRight;
    private View mBtnSetTitle;
    private View mBtnSetCover;
    private View mBtnTranslucent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_title_bar_demo;
    }

    @Override
    protected void initVariable(Intent intent) {
        setCenterTitle("自定义中间标题");
        setTitleBarBackground(R.color.color_CCCCCC);
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        mBtnSetLeft = findViewById(R.id.btnSetLeft);
        mBtnAddRight = findViewById(R.id.btnAddRight);
        mBtnSetTitle = findViewById(R.id.btnSetTitle);
        mBtnSetCover = findViewById(R.id.btnSetCover);
        mBtnTranslucent = findViewById(R.id.btnTranslucent);
    }

    @Override
    protected void setListener() {
        mBtnSetLeft.setOnClickListener(this);
        mBtnAddRight.setOnClickListener(this);
        mBtnSetTitle.setOnClickListener(this);
        mBtnSetCover.setOnClickListener(this);
        mBtnTranslucent.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetLeft:
                setLeftButton(R.drawable.ic_back_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;

            case R.id.btnAddRight:
                ImageView btnCamera = addRightButton(R.drawable.icon_camera, 25, 25, 16);
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("按钮点击了");
                    }
                });
                break;

            case R.id.btnSetTitle:
                setCenterTitle("");
                setLeftTitle("自定义左边标题", R.color.color_5180EF);
                break;

            case R.id.btnSetCover:
                setTitleBarBackground(R.color.transparent);//背景透明
                setTitleBarCover(true);
                break;

            case R.id.btnTranslucent:
//                setStatusBarColor(Color.BLUE);设置状态栏颜色，和setStatusBarTranslucent二选一使用，同时使用，最后设置的生效
                setStatusBarTranslucent();
                break;
        }
    }
}
