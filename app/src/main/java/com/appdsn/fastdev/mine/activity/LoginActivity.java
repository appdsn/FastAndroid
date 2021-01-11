package com.appdsn.fastdev.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.config.RouterPath;
import com.appdsn.commonbase.scheme.SchemeProxy;
import com.appdsn.commonbase.scheme.model.BaseRouterExtra;
import com.appdsn.commonbase.scheme.model.BaseSharedPreferencesKey;
import com.appdsn.commoncore.utils.SPUtils;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.fastdev.R;

/**
 * Desc:登录的页
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
@Route(path = RouterPath.LOGIN_ACTIVITY)
public class LoginActivity extends BaseAppActivity {
    @Autowired(name = BaseRouterExtra.EXTRA_URL)
    String mJumpUrl = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout view, Bundle savedInstanceState) {
        setCenterTitle("登录页");
        findViewById(R.id.login_btn).setOnClickListener(v -> {
            SPUtils.put(BaseSharedPreferencesKey.SP_USER_IS_LOGIN_IN, true);
            ToastUtils.showShort("登录成功");
            finish();
            SchemeProxy.openScheme(v.getContext(), mJumpUrl);
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }

}
