package com.appdsn.fastdev.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.appdsn.commonbase.config.RouterPath;
import com.appdsn.commonbase.scheme.model.BaseRouterExtra;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.commoncore.utils.log.LogUtils;

/**
 * Desc:浏览器Activity
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
@Route(path = RouterPath.BROWSER_ACTIVITY)
public class BrowserActivity extends BaseBrowserActivity {
    @Autowired(name = BaseRouterExtra.EXTRA_URL)
    String mJumpUrl = "";

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected String getH5Url() {
        return mJumpUrl;
    }

    @Override
    protected ArrayMap<String, Object> getJavaScriptObjects() {
        //示例js交互代码
        ArrayMap<String, Object> javaScriptObjects = new ArrayMap<>();
        javaScriptObjects.put("native", new JsInterface());
        //...
        return javaScriptObjects;
    }

    public class JsInterface {
        @JavascriptInterface
        public String getXnData() {
            return "";
        }
    }

    /**
     * 下面三个方法，根据项目需求选择性重写自定义
     */
    @Override
    public void share(Activity activity, WebView webView, String oriUrl, boolean onlyShareImg, String shareTitle, String shareContent, String shareImg, String shareUrl) {
        super.share(activity, webView, oriUrl, onlyShareImg, shareTitle, shareContent, shareImg, shareUrl);
        ToastUtils.showShort("是否只是分享图片 : " + onlyShareImg + "分享标题 : " + shareTitle
                + "分享内容 : " + shareContent + "分享图片 : "
                + shareImg + "分享地址 : " + shareUrl);
    }

    @Override
    public void close(Activity activity, WebView webView, String oriUrl) {
        LogUtils.i("", "关闭" + oriUrl);
    }

    @Override
    public void customInterceptor(Activity activity, WebView webView, String oriUrl, String path) {
        LogUtils.i("", "自定义" + oriUrl);
    }

}
