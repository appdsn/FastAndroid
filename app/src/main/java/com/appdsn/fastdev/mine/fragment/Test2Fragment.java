package com.appdsn.fastdev.mine.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.appdsn.commoncore.base.BaseFragment;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/11/6 11:35
 */
public class Test2Fragment extends BaseFragment {
    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initVariable(Bundle arguments) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("测试页面2");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onVisibleToUser(boolean visible) {
        super.onVisibleToUser(visible);
        Log.i("123", "onVisibleToUser_test2:" + visible);
    }
}
