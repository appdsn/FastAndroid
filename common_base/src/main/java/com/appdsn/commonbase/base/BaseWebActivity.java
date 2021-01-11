package com.appdsn.commonbase.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.appdsn.commonbase.R;
import com.appdsn.commonbase.scheme.listener.BaseWebViewClient;
import com.appdsn.commoncore.base.BaseAgentWebActivity;
import com.appdsn.commoncore.event.BindEventBus;
import com.just.agentweb.BaseIndicatorView;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

/**
 * Desc:
 * <p>
 * Author: AnYaBo
 * Date: 2019/7/4
 * Copyright: Copyright (c) 2016-2020
 * Company: @小牛科技
 * Update Comments:
 *
 * @author anyabo
 */
@BindEventBus
public class BaseWebActivity extends BaseAgentWebActivity {
    private String mUrl;
    private String mTitle;

    /**
     * 实例化浏览器Fragment
     *
     * @param jumpUrl 跳转url
     * @return BaseWebFragment
     */
    public static void start(Activity activity, String jumpUrl, String title) {
        Intent intent = new Intent(activity, BaseWebActivity.class);
        intent.putExtra("url", jumpUrl);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initVariable(Intent intent) {
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
    }

    @Override
    protected void initViews(FrameLayout frameLayout, Bundle bundle) {
        setLeftButton(R.drawable.base_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (!TextUtils.isEmpty(mTitle)) {
            setCenterTitle(mTitle, R.color.color_794D14);
        } else {
            hideTitleBar();
        }
    }

    @Override
    protected void setListener() {
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void addJavascriptInterface(WebView webView) {

    }

    @Override
    protected BaseIndicatorView getCustomIndicator() {
        return new WebLoadingIndicator(mContext);
//        return null;
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    @Override
    protected WebViewClient getWebViewClient() {
        return new BaseWebViewClient(this);
    }

    @Override
    protected WebChromeClient getWebChromeClient() {
        return new CustomWebChromeClient();
    }

    /**
     * 错误页面
     */
    @Override
    protected int getErrorLayoutResId() {
        return R.layout.common_empty_view;
    }

    @Override
    protected int getErrorClickViewId() {
        return R.id.layEmpty;
    }

    private boolean mIsLoaded;

    @Override
    protected void loadData() {
        super.loadData();
        getWebView().setVerticalScrollBarEnabled(false); //垂直不显示
        mIsLoaded = true;
    }


    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            try {
                if (view != null && !TextUtils.isEmpty(view.getUrl())
                        && view.getUrl().contains(title)) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回true，web自己返回上一级页面， 返回false 可以finish当前activity
     */
    public boolean canGoBack() {
        return mAgentWeb.back();
    }
}
