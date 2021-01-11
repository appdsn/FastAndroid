package com.appdsn.commoncore.base;

import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appdsn.commoncore.utils.ToastUtils;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebUIControllerImplBase;
import com.just.agentweb.BaseIndicatorView;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IUrlLoader;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.QuickCallJs;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.WebViewClient;

/**
 * source code  https://github.com/Justson/AgentWeb
 */
public abstract class BaseAgentWebActivity extends BaseActivity implements QuickCallJs {

    protected AgentWeb.CommonBuilder mCommonBuilder;
    protected AgentWeb mAgentWeb;
    private AgentWeb.PreAgentWeb mPreAgentWeb;

    @Override
    protected void onViewInit() {
        super.onViewInit();
        buildAgentWeb();
    }

    protected void buildAgentWeb() {
        AgentWeb.IndicatorBuilder indicatorBuilder = AgentWeb.with(this).setAgentWebParent(getAgentWebParent(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (getCustomIndicator() != null) {
            mCommonBuilder = indicatorBuilder.setCustomIndicator(getCustomIndicator());
        } else {
            mCommonBuilder = indicatorBuilder.useDefaultIndicator(getIndicatorColor(), getIndicatorHeight());
        }

        mCommonBuilder
                .setWebChromeClient(getWebChromeClient())
                .setWebViewClient(getWebViewClient())//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebView(getCustomWebView())
                .setWebLayout(getCustomWebLayout())
                .setPermissionInterceptor(getPermissionInterceptor())
                .setAgentWebUIController(getAgentWebUIController())
                .interceptUnkownUrl()
                .setOpenOtherPageWays(getOpenOtherAppWay())
                .setAgentWebWebSettings(getCustomWebSettings())
                .setMainFrameErrorView(getErrorLayoutResId(), getErrorClickViewId())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK);

        mPreAgentWeb = mCommonBuilder.createAgentWeb();
        mAgentWeb = mPreAgentWeb.get();
        /*开启软件加速，防止网页加载不出来的bug, 注意：开启软件加速，视频会播放不了*/
//        getWebView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        addJavascriptInterface(getWebView());
    }

    @Override
    protected void loadData() {
        mPreAgentWeb.go(getUrl());
    }

    protected abstract void addJavascriptInterface(WebView webView);

    protected abstract String getUrl();

    protected abstract WebViewClient getWebViewClient();

    protected abstract WebChromeClient getWebChromeClient();

    /**
     * 设置错误布局资源ID
     */
    protected @LayoutRes
    int getErrorLayoutResId() {
        return com.just.agentweb.R.layout.agentweb_error_page;
    }

    /**
     * 设置错误布局资源ID
     */
    protected @IdRes
    int getErrorClickViewId() {
        return 0;
    }

    public void reload() {
        if (mAgentWeb != null) {
            this.mAgentWeb.getUrlLoader().reload();
        }
    }

    public void loadUrl(String url) {
        if (mAgentWeb != null) {
            this.mAgentWeb.getUrlLoader().loadUrl(url);
        }
    }

    public IUrlLoader getUrlLoader() {
        if (mAgentWeb != null) {
            return this.mAgentWeb.getUrlLoader();
        }
        return null;
    }

    public AgentWeb getAgentWeb() {
        return this.mAgentWeb;
    }

    public WebView getWebView() {
        if (mAgentWeb != null) {
            return this.mAgentWeb.getWebCreator().getWebView();
        }
        return null;
    }

    /**
     * 设置WebView容器
     */
    protected ViewGroup getAgentWebParent() {
        return getBodyView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    public WebSettings getWebSettings() {
        if (getWebView() != null) {
            return getWebView().getSettings();
        }
        return null;
    }

    protected IAgentWebSettings getCustomWebSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        new DefaultDownloadImpl(BaseAgentWebActivity.this, webView, this.mAgentWeb.getPermissionInterceptor()) {
                            @Override
                            protected ResourceRequest createResourceRequest(String url) {
                                return DownloadImpl.getInstance()
                                        .with(BaseAgentWebActivity.this.getApplicationContext())
                                        .url(url)
                                        .quickProgress()
                                        .addHeader("", "")
                                        .setEnableIndicator(true)
                                        .autoOpenIgnoreMD5()
                                        .setRetry(5)
                                        .setBlockMaxTime(100000L);
                            }

                            @Override
                            protected void taskEnqueue(ResourceRequest resourceRequest) {
                                DownloadListenerAdapter downloadListenerAdapter = getDownloadListener();
                                if (downloadListenerAdapter != null) {
                                    resourceRequest.enqueue(downloadListenerAdapter);
                                } else {
                                    super.taskEnqueue(resourceRequest);
                                }
                            }
                        });
            }
        };
    }

    /**
     * 需要监听下载进度，需要重新该方法
     */
    protected DownloadListenerAdapter getDownloadListener() {
        return new DownloadListenerAdapter() {
            @Override
            public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                ToastUtils.showLong("开始下载");
            }

            @MainThread
            @Override
            public void onProgress(String url, long downloaded, long length, long usedTime) {
                super.onProgress(url, downloaded, length, usedTime);
            }

            @Override
            public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                return super.onResult(throwable, path, url, extra);
            }
        };
    }

    /**
     * 自定义加载动画
     */
    protected BaseIndicatorView getCustomIndicator() {
        return null;
    }

    /*设置默认加载进度颜色值*/
    protected @ColorInt
    int getIndicatorColor() {
        return -1;
    }

    /*设置默认加载进度高度，单位dp*/
    protected int getIndicatorHeight() {
        return -1;
    }

    /*自定义WebView*/
    protected @Nullable
    WebView getCustomWebView() {
        return null;
    }

    /*自定义WebLayout,内部提供WebView*/
    protected @Nullable
    IWebLayout getCustomWebLayout() {
        return null;
    }

    protected @Nullable
    PermissionInterceptor getPermissionInterceptor() {
        return null;
    }

    public @Nullable
    AgentWebUIControllerImplBase getAgentWebUIController() {
        return null;
    }

    public @Nullable
    DefaultWebClient.OpenOtherPageWays getOpenOtherAppWay() {
        return DefaultWebClient.OpenOtherPageWays.ASK;//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
    }

    @Override
    public void quickCallJs(String method, ValueCallback<String> callback, String... params) {
        if (mAgentWeb != null) {
            mAgentWeb.getJsAccessEntrace().quickCallJs(method, callback, params);
        }
    }

    @Override
    public void quickCallJs(String method, String... params) {
        if (mAgentWeb != null) {
            mAgentWeb.getJsAccessEntrace().quickCallJs(method, params);
        }
    }

    @Override
    public void quickCallJs(String method) {
        if (mAgentWeb != null) {
            mAgentWeb.getJsAccessEntrace().quickCallJs(method);
        }
    }
}
