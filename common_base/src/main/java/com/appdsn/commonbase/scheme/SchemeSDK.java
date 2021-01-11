package com.appdsn.commonbase.scheme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appdsn.commonbase.scheme.config.SchemeConfig;
import com.appdsn.commonbase.scheme.config.SchemeEntity;
import com.appdsn.commonbase.scheme.listener.JSMethodCallback;
import com.appdsn.commonbase.scheme.listener.OnSchemeHandler;
import com.appdsn.commonbase.scheme.utils.Abi64WebViewCompat;
import com.appdsn.commonbase.scheme.utils.SchemeUtils;
import com.appdsn.commoncore.utils.ToastUtils;


/**
 * Desc:路由代理入口
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/12
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
public class SchemeSDK {

    private static OnSchemeHandler sSchemeHandler;

    /**
     * 初始化
     *
     * @param application 应用application
     * @param isDebug     是否调试模式
     */
    public static void init(Application application, boolean isDebug, OnSchemeHandler schemeHandler) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (isDebug) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，
            // 必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(application);
        Abi64WebViewCompat.obliterate(application);
        sSchemeHandler = schemeHandler;
    }

    public static boolean openH5(Context context, String url) {
        if (SchemeUtils.isH5Url(url)) {
            ARouter.getInstance().build(SchemeConfig.PATH_BROWSER_ACTIVITY)
                    .withString(SchemeConfig.KEY_H5_URL, url)
                    .navigation(context);
            return true;
        }
        return false;
    }

    /**
     * 协议格式如：xxx://xxxx.com/path?key1=value1&&key2=value2
     */
    public static boolean openScheme(Context context, String url) {
        if (SchemeUtils.isScheme(url)) {
            ARouter.getInstance().build(Uri.parse(url)).
                    navigation(context);
            return true;
        }
        return false;
    }

    /**
     * 打开支付宝或者微信通过scheme
     */
    public static boolean openAliOrWx(Context context, String url) {
        if (SchemeUtils.isAliOrWxScheme(url)) {
            try {
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.setData(Uri.parse(url));
                context.startActivity(it);
            } catch (Exception e) {
                ToastUtils.showShort(url.contains("weixin://") ? "您未安装微信" : "您未安装支付宝");
            }
            return true;
        }
        return false;
    }

    /**
     * path:arouterActivity页面路径
     */
    public static boolean openPath(Context context, String path) {
        try {
            ARouter.getInstance().build(path).navigation(context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开协议
     *
     * @param context 上下文
     * @param url     协议地址
     */
    public static boolean openUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (SchemeUtils.isScheme(url)) {
            return openScheme(context, url);
        } else if (SchemeUtils.isAliOrWxScheme(url)) {
            return openAliOrWx(context, url);
        } else if (SchemeUtils.isH5Url(url)) {
            return openH5(context, url);
        } else {
            return openPath(context, url);
        }
    }


    /**
     * 解析重定向Url
     *
     * @param webView webView控件
     * @param url     重定向的url
     * @return 是否拦截重定向 默认false
     */
    public static boolean parseWebUrl(Context context, final WebView webView, String url) {
        if (SchemeUtils.isScheme(url)) {
            if (sSchemeHandler != null) {
                final SchemeEntity entity = SchemeUtils.convertUrlToEntity(url);
                final String method = SchemeUtils.getValueFromUrl(SchemeConfig.KEY_CALLBACK_METHOD, url);

                sSchemeHandler.onHandleScheme(context, entity, webView, new JSMethodCallback() {
                    @Override
                    public void callJsMethod() {
                        quickCallJs(method);
                    }

                    @Override
                    public void callJsMethod(String paramJson) {
                        quickCallJs(method, paramJson);
                    }

                    public void quickCallJs(String method, String... params) {
                        if (TextUtils.isEmpty(method)) {
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("javascript:" + method);
                        if (params == null || params.length == 0) {
                            sb.append("()");
                        } else {
                            sb.append("(").append(params).append(")");
                        }
                        webView.loadUrl(sb.toString());
                    }
                });
            }
            return true;
        } else if (SchemeUtils.isAliOrWxScheme(url)) {
            openAliOrWx(context, url);
            return true;
        }
        return false;
    }

    public static boolean parsePushMessage(Context context, SchemeEntity entity) {
        if (sSchemeHandler != null) {
            sSchemeHandler.onHandleScheme(context, entity, null, null);
        }
        return true;
    }

    public static OnSchemeHandler getSchemeHandler() {
        return sSchemeHandler;
    }
}
