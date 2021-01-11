package com.appdsn.commonbase.scheme.listener;

import android.content.Context;
import android.webkit.WebView;

import com.appdsn.commonbase.scheme.config.SchemeEntity;

/**
 * web协议处理类
 */
public interface OnSchemeHandler {

    boolean onHandleScheme(Context context, SchemeEntity schemeEntity, WebView webView, JSMethodCallback callback);

    boolean isLogin();
}
