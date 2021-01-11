package com.appdsn.commonbase.http;


import com.appdsn.commonbase.config.AppConfig;
import com.appdsn.commoncore.http.EHttp;
import com.appdsn.commoncore.http.callback.HttpCallback;
import com.appdsn.commoncore.http.model.CommonSSLFactory;
import com.appdsn.commoncore.http.request.HttpRequest;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class HttpHelper {
    public static void init() {
        //全局设置，所有请求起作用
        EHttp.Builder builder = new EHttp.Builder()
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .apiResult(GetCommonResult.class)
                .baseUrl(AppConfig.sBaseApiHost)
                .addInterceptor(new RequestParamInterceptor())
                .setExceptionHandler(new GetExceptionHandler())
                .sslSocketFactory(CommonSSLFactory.ignoreSSLSocketFactory());
        EHttp.init(builder);
    }

    /*R是服务端返回的data字段*/
    public static <R> Disposable get(Object tag, String url, HttpCallback<R> callback) {
        return EHttp.get(tag, url, callback);
    }

    /*tag标记一类网络请求订阅，可以用来取消所有带有此tag的订阅，一般传入Context对象*/
    public static <R> Disposable post(Object tag, String url, HttpRequest request, HttpCallback<R> callback) {
        return EHttp.post(tag, url, request, callback);
    }

    /*可以自定义ApiService,调用此方法执行Observable请求*/
    public static <R> Disposable execute(Object tag, Observable<ResponseBody> observable, HttpCallback<R> callback) {
        return EHttp.execute(tag, observable, callback);
    }

    /**
     * 取消同一个tag对应的所有网络请求
     */
    public static void cancelTag(Object tag) {
        EHttp.cancelTag(tag);
    }
}
