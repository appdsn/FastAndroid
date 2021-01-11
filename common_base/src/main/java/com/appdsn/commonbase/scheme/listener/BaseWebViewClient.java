package com.appdsn.commonbase.scheme.listener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.appdsn.commonbase.scheme.SchemeSDK;
import com.just.agentweb.WebViewClient;

/**
 * Desc:WebView client 基类
 */
public class BaseWebViewClient extends WebViewClient {
    private Activity mActivity;

    public BaseWebViewClient(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Context context = view.getContext();
        if (SchemeSDK.parseWebUrl(context, view, url)) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        Context context = view.getContext();
        if (SchemeSDK.parseWebUrl(context, view, url)) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
