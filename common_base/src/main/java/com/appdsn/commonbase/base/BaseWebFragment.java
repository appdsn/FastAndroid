package com.appdsn.commonbase.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.appdsn.commonbase.R;
import com.appdsn.commonbase.scheme.listener.BaseWebViewClient;
import com.appdsn.commoncore.base.BaseAgentWebFragment;
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
public class BaseWebFragment extends BaseAgentWebFragment {
    private String mUrl;

    /**
     * 实例化浏览器Fragment
     *
     * @param jumpUrl 跳转url
     * @return BaseWebFragment
     */
    public static BaseWebFragment newInstance(String jumpUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("url", jumpUrl);
        BaseWebFragment xBrowserFragment = new BaseWebFragment();
        xBrowserFragment.setArguments(bundle);
        return xBrowserFragment;
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initVariable(Bundle bundle) {
        mUrl = bundle.getString("url");
    }

    @Override
    protected void initViews(FrameLayout frameLayout, Bundle bundle) {
        hideTitleBar();
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
        return new WebLoadingIndicator(getContext());
//        return null;
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    @Override
    protected WebViewClient getWebViewClient() {
        return new BaseWebViewClient(getActivity());
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
                if (getActivity() != null && mWebListener != null) {
                    mWebListener.onReceivedTitle(title);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onVisibleToUser(boolean visible) {
        super.onVisibleToUser(visible);
        if (visible) {

        } else {

        }
    }

    /**
     * 返回true，web自己返回上一级页面， 返回false 可以finish当前activity
     */
    public boolean canGoBack() {
        return mAgentWeb.back();
    }

    private WebListener mWebListener;

    public void setWebListener(WebListener webListener) {
        mWebListener = webListener;
    }

    public interface WebListener {
        /**
         * 接收标题栏
         *
         * @param title 标题
         */
        void onReceivedTitle(String title);
    }
}
