package com.appdsn.commoncore.http.callback;

import okhttp3.ResponseBody;


public abstract class StringCallback extends HttpCallback<String> {
    @Override
    public String parseResponse(ResponseBody body) throws Exception {
        return body.string();
    }
}
