package com.appdsn.fastdemo.xrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.fastdemo.R;

public class XRecyclerViewActivity extends BaseAppActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_xrecycler_view;
    }

    @Override
    protected void initVariable(Intent intent) {
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("XRecyclerView使用");
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void loadData() {
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btnSimple:
                startActivity(XRecyclerSimpleActivity.class);
                break;
            case R.id.btnMulti:
                startActivity(XRecyclerMultiActivity.class);
                break;
            case R.id.btnExpanded:
                startActivity(XRecyclerExpandedActivity.class);
                break;
        }
    }
}
