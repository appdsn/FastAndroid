package com.appdsn.commoncore.http.function;

import com.appdsn.commoncore.http.callback.HttpCallback;
import com.appdsn.commoncore.http.model.ResultWrap;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by wbz360 on 2018/3/21.
 */

public class ResultFunction<R> implements Function<ResponseBody, ResultWrap<R>> {
    private HttpCallback<R> callback;

    public ResultFunction(HttpCallback<R> callback) {
        this.callback = callback;
    }

    //如果解析数据有错，向上抛出异常，由后面ErrorFunction处理
    @Override
    public ResultWrap<R> apply(@NonNull ResponseBody body) throws Exception {
        if (callback != null) {
            Object result = callback.parseResponse(body);
            return new ResultWrap<R>((R) result);
        }
        return new ResultWrap(body);
    }
}
