package com.appdsn.commonbase.scheme.listener;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.appdsn.commonbase.scheme.config.SchemeConfig;
import com.appdsn.commonbase.scheme.utils.SchemeUtils;
import com.appdsn.commoncore.utils.log.LogUtils;

/**
 * Desc:WebView client 基类
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/11
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
@Interceptor(priority = 2)
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        LogUtils.i("", "LoginInterceptor 开始执行");
        boolean needLogin = false;
        String url = postcard.getUri().toString();
        try {
            String needLoginStr = SchemeUtils.getValueFromUrl(SchemeConfig.KEY_NEED_LOGIN, url);
            if ("1".equals(needLoginStr)) {
                needLogin = true;
            }
        } catch (Exception e) {
        }
        //是否需要登录跳转
        if (needLogin) {
            //用户是否登录
            boolean isLogin = SchemeUtils.isLogin();
            LogUtils.i("", "LoginInterceptor 是否已登录: " + isLogin);
            //判断用户的登录情况是否拦截
            if (isLogin) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(null);
                // 跳转下一步的url拦截下来，方便登录后的DeepLink
                ARouter.getInstance().build(SchemeConfig.PATH_LOGIN_ACTIVITY)
                        .withString(SchemeConfig.KEY_SCHEME_URL, url)
                        .navigation();
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        LogUtils.i("", "LoginInterceptor 初始化");
    }

}
