package com.appdsn.commoncore.http;

import android.os.Handler;
import android.os.Looper;

import com.appdsn.commoncore.BuildConfig;
import com.appdsn.commoncore.http.baseurl.BaseUrlManager;
import com.appdsn.commoncore.http.callback.HttpCallback;
import com.appdsn.commoncore.http.cookie.SimpleCookieJar;
import com.appdsn.commoncore.http.exception.HttpExeceptionHandler;
import com.appdsn.commoncore.http.function.CallbackObserver;
import com.appdsn.commoncore.http.function.ExceptionFunction;
import com.appdsn.commoncore.http.function.ResultFunction;
import com.appdsn.commoncore.http.function.RetryFunction;
import com.appdsn.commoncore.http.intercepter.HeadersInterceptor;
import com.appdsn.commoncore.http.model.ApiResult;
import com.appdsn.commoncore.http.model.ApiService;
import com.appdsn.commoncore.http.progress.ProgressInfo;
import com.appdsn.commoncore.http.progress.ProgressListener;
import com.appdsn.commoncore.http.progress.ProgressManager;
import com.appdsn.commoncore.http.progress.ProgressRequestBody;
import com.appdsn.commoncore.http.request.HttpRequest;
import com.appdsn.commoncore.http.request.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Desc:快速发起网络请求框架，包括直接get(),post()请求，也支持retrofit风格API
 */
public class EHttp {
    public static final String TAG = "EHttp";
    private static EHttp mInstance;
    private final HttpExeceptionHandler mExceptionHandler;
    public Retrofit.Builder retrofitBuilder;
    private Retrofit mRetrofit;
    public OkHttpClient.Builder okHttpBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Map<String, String> commonHeaders = new HashMap<String, String>();
    private ApiService mApiService;
    public Class<? extends ApiResult> apiResult;//默认值null，可以设置为CommonResult.class
    public Gson gson;
    public HashMap<Object, List<Disposable>> disposables = new HashMap<>();
    public boolean isDebug;
    public int retryCount = 2;//默认重试2次

    private EHttp(Builder buider) {
        this.okHttpBuilder = buider.okHttpBuilder;
        this.isDebug = buider.isDebug;
        this.retryCount = buider.retryCount;
        this.commonHeaders = buider.commonHeaders;
        this.gson = buider.gson;
        this.apiResult = buider.apiResult;
        if (isDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(logging);
        }
        if (!commonHeaders.isEmpty()) {//添加头放在最前面方便其他拦截器可能会用到
            okHttpBuilder.interceptors().add(0, new HeadersInterceptor(commonHeaders));
        }
        BaseUrlManager.getInstance().with(okHttpBuilder);//先添加的拦截器先执行
        ProgressManager.getInstance().with(okHttpBuilder);
        this.mOkHttpClient = okHttpBuilder.build();
        this.retrofitBuilder = buider.retrofitBuilder.client(mOkHttpClient);
        this.mRetrofit = retrofitBuilder.build();
        this.mApiService = mRetrofit.create(ApiService.class);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mExceptionHandler = buider.exceptionHandler;

        /*开启默认高级模式*/
        BaseUrlManager.getInstance().startAdvancedModel(mRetrofit.baseUrl());
    }

    public static void init() {
        mInstance = new EHttp(new Builder());
    }

    public static void init(Builder builder) {
        if (builder == null) {
            builder = new Builder();
        }
        mInstance = new EHttp(builder);
    }

    public static EHttp getInstance() {
        if (mInstance == null) {
            synchronized (EHttp.class) {
                if (mInstance == null) {
                    init();
                }
            }
        }
        return mInstance;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public HttpExeceptionHandler getExceptionHandler() {
        return mExceptionHandler;
    }

    public static <R> Disposable get(Object tag, String url, HttpCallback<R> callback) {
        return get(tag, url, null, callback);
    }

    public static <T> T create(Class<T> apiService) {
        return (T) EHttp.getInstance().getRetrofit().create(apiService);
    }

    public static <R> Disposable get(Object tag, String url, HttpRequest request, HttpCallback<R> callback) {
        if (request == null) {
            request = new HttpRequest();
        }
        Map<String, String> urlParams = request.requestParams.getUrlParams();
        Observable<ResponseBody> observable = request.apiService.get(url, urlParams);
        return execute(tag, observable, callback);
    }

    /*tag标记一类网络请求订阅，可以用来取消所有带有此tag的订阅，一般传入Context对象*/
    public static <R> Disposable post(Object tag, String url, HttpRequest request, HttpCallback<R> callback) {
        if (request == null) {
            request = new HttpRequest();
            request.requestParams.addBodyParams("{}");//给一个默认的类型（application/json;charset=utf-8），防止报错
        }
        RequestParams params = request.requestParams;
        RequestBody body = wrapRequestBody(params.getRequestBody(), callback);
        Map<String, String> urlParams = params.getUrlParams();
        Observable<ResponseBody> observable = request.apiService.postBody(url, urlParams, body);
        return execute(tag, observable, callback);
    }

    /*可以自定义ApiService,调用此方法执行Observable请求*/
    public static <R> Disposable execute(Object tag, Observable<ResponseBody> observable, HttpCallback<R> callback) {
        return observable
                .subscribeOn(Schedulers.io())
                .map(new ResultFunction<R>(callback))//处理结果
                .onErrorResumeNext(new ExceptionFunction<R>(EHttp.getInstance().mExceptionHandler))//处理出错
                .retry(EHttp.getInstance().retryCount, new RetryFunction()) //出错重试
                .observeOn(AndroidSchedulers.mainThread())//caback回调在主线程
                .subscribeWith(new CallbackObserver<R>(tag, callback));
    }

    /*文件上传进度监测*/
    public static <R> RequestBody wrapRequestBody(RequestBody requestBody, final HttpCallback<R> callback) {
        if (callback == null) {
            return requestBody;
        }
        ProgressRequestBody requestBodyWrapper = new ProgressRequestBody(EHttp.getInstance().mHandler, requestBody, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                callback.onUpProgress(progressInfo.getCurrentbytes(), progressInfo.getContentLength());
            }

            @Override
            public void onError(long id, Exception e) {

            }
        }, 10);

        return requestBodyWrapper;
    }


    /**
     * 取消同一个tag对应的所有网络请求
     */
    public static void cancelTag(Object tag) {
        HashMap<Object, List<Disposable>> disposables = EHttp.getInstance().disposables;
        List<Disposable> tagList = disposables.get(tag);
        if (tagList != null) {
            for (int i = tagList.size() - 1; i >= 0; i--) {
                Disposable disposable = tagList.get(i);
                cancelDisposable(disposable);
                tagList.remove(disposable);
            }
        }

    }

    /**
     * 取消一个网络请求
     */
    public static void cancelDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        private OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        private Map<String, String> commonHeaders = new HashMap<String, String>();
        private Class<? extends ApiResult> apiResult;//默认值null，可以设置为CommonResult.class
        private Gson gson;
        private boolean isDebug = BuildConfig.DEBUG;
        private int retryCount = 2;//默认重试2次
        private HttpExeceptionHandler exceptionHandler = new HttpExeceptionHandler();

        public Builder() {
            /*下面是OKHttp全局的默认配置*/
            okHttpBuilder
                    .cookieJar(new SimpleCookieJar()) //cookie enabled
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS);
            /*下面是Retrofit默认的全局配置*/
            retrofitBuilder
                    .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava2
                    .baseUrl("http://www.xxx.com/");//不设置会报错
            gson = new GsonBuilder()
                    .setDateFormat("yyyy.MM.dd HH:mm:ss")
                    .disableHtmlEscaping()
                    .create();
        }

        public Builder(EHttp eHttp) {
            okHttpBuilder = eHttp.okHttpBuilder;
            retrofitBuilder = eHttp.retrofitBuilder;
            this.isDebug = eHttp.isDebug;
            this.retryCount = eHttp.retryCount;
            this.commonHeaders = eHttp.commonHeaders;
            this.gson = eHttp.gson;
            this.apiResult = eHttp.apiResult;
        }

        public Builder retryCount(int count) {
            this.retryCount = count;
            return this;
        }

        public Builder debug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        //配置通用的ApiResult，默认为null
        public Builder apiResult(Class<? extends ApiResult> resultType) {
            if (resultType != null) {
                this.apiResult = resultType;
            }
            return this;
        }

        public Builder gson(Gson gson) {
            if (gson != null) {
                this.gson = gson;
            }
            return this;
        }

        /*全局的header*/
        public Builder header(String name, String value) {
            commonHeaders.put(name, value);
            return this;
        }

        /*下面是okhttp配置*/
        public Builder connectTimeout(long timeout) {
            if (timeout > 0) {
                okHttpBuilder
                        .connectTimeout(timeout, TimeUnit.MILLISECONDS);
            }
            return this;
        }


        public Builder readTimeout(long timeout) {
            if (timeout > 0) {
                okHttpBuilder
                        .readTimeout(timeout, TimeUnit.MILLISECONDS);
            }
            return this;
        }

        public Builder writeTimeout(long timeout) {
            if (timeout > 0) {
                okHttpBuilder
                        .writeTimeout(timeout, TimeUnit.MILLISECONDS);

            }
            return this;
        }

        public Builder cookieJar(CookieJar cookieJar) {
            if (cookieJar != null) {
                okHttpBuilder
                        .cookieJar(cookieJar);

            }
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            if (sslSocketFactory != null) {
                okHttpBuilder
                        .sslSocketFactory(sslSocketFactory);
            }
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier != null) {
                okHttpBuilder
                        .hostnameVerifier(hostnameVerifier);
            }
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptor != null) {
                okHttpBuilder
                        .addInterceptor(interceptor);
            }
            return this;
        }


        public Builder addNetworkInterceptor(Interceptor interceptor) {
            if (interceptor != null) {
                okHttpBuilder
                        .addNetworkInterceptor(interceptor);
            }
            return this;
        }

        public Builder setExceptionHandler(HttpExeceptionHandler exceptionHandler) {
            if (exceptionHandler != null) {
                this.exceptionHandler = exceptionHandler;
            }
            return this;
        }

        public Builder client(OkHttpClient client) {
            if (client != null) {
                okHttpBuilder = client.newBuilder();
            }
            return this;
        }

        /*下面是retrofit配置*/
        public Builder baseUrl(String baseUrl) {
            if (baseUrl != null) {
                retrofitBuilder.baseUrl(baseUrl);
            }
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            if (factory != null) {
                retrofitBuilder.addConverterFactory(factory);
            }
            return this;
        }


        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            if (factory != null) {
                retrofitBuilder.addCallAdapterFactory(factory);
            }
            return this;
        }
    }
}

