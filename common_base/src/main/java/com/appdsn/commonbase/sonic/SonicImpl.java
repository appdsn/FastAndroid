package com.appdsn.commonbase.sonic;

import android.content.Context;

import com.just.agentweb.AgentWeb;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

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
public class SonicImpl {
    private SonicSession mSonicSession;
    private Context mContext;
    private String mUrl;
    private SonicSessionClientImpl sonicSessionClient;

    public SonicImpl(String url, Context context) {
        this.mUrl = url;
        this.mContext = context;
    }

    public void onCreateSession() {
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        SonicEngine.createInstance(new DefaultSonicRuntimeImpl(mContext.getApplicationContext()), new SonicConfig.Builder().build());
        // create sonic session and run sonic flow
        mSonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
        if (null != mSonicSession) {
            mSonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            // throw new UnknownError("create session fail!");
//            Toast.makeText(mContext, "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
    }

    public SonicSessionClientImpl getSonicSessionClient() {
        return this.sonicSessionClient;
    }

    public SonicSession getSonicSession() {
        return mSonicSession;
    }

    public void bindAgentWeb(AgentWeb agentWeb) {
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(agentWeb);
            sonicSessionClient.clientReady();
        } else { // default mode
            agentWeb.getUrlLoader().loadUrl(mUrl);
        }
    }

    public void destroy() {
        if (mSonicSession != null) {
            mSonicSession.destroy();
        }
    }

}
