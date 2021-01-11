package com.appdsn.commonbase.sonic;

import android.os.Bundle;
import android.webkit.WebView;

import com.just.agentweb.AgentWeb;
import com.tencent.sonic.sdk.SonicSessionClient;

import java.util.HashMap;

/**
 * Desc:
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/29
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
public class SonicSessionClientImpl extends SonicSessionClient {
    private AgentWeb mAgentWeb;

    public void bindWebView(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    public WebView getWebView() {
        return this.mAgentWeb.getWebCreator().getWebView();
    }

    @Override
    public void loadUrl(String url, Bundle extraData) {
        this.mAgentWeb.getUrlLoader().loadUrl(url);
    }

    @Override
    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.mAgentWeb.getUrlLoader().loadUrl(baseUrl);
    }

    @Override
    public void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers) {
        loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl);
    }

}
