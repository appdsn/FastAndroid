package com.appdsn.commonbase.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commonbase.scheme.SchemeSDK;


/**
 * Desc:路由分发器Activity
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/14
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
public class SchemeDispatcherActivity extends BaseAppActivity {

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        SchemeSDK.openScheme(this,
                getIntent().getData().toString());
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void loadData() {
        finish();
    }

}
