package com.appdsn.commoncore.http.function;

import com.appdsn.commoncore.http.EHttp;
import com.appdsn.commoncore.http.callback.HttpCallback;
import com.appdsn.commoncore.http.exception.ApiException;
import com.appdsn.commoncore.http.model.ResultWrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by wbz360 on 2018/3/19.
 */

public class CallbackObserver<R> extends DisposableObserver<ResultWrap<R>> {
    private Object tag;
    private HttpCallback<R> callback;

    public CallbackObserver(Object tag, HttpCallback<R> callback) {
        this.tag = tag;
        this.callback = callback;
        addDisposable(tag);
    }

    public void addDisposable(Object tag) {
        HashMap<Object, List<Disposable>> disposables = EHttp.getInstance().disposables;
        List<Disposable> tagList = disposables.get(tag);
        if (tagList == null) {
            tagList = new ArrayList<>();
            disposables.put(tag, tagList);
        }
        tagList.add(this);
    }

    public void removeDisposable() {
        HashMap<Object, List<Disposable>> disposables = EHttp.getInstance().disposables;
        List<Disposable> tagList = disposables.get(tag);
        if (tagList == null) {
            tagList = new ArrayList<>();
            disposables.put(tag, tagList);
        }
        tagList.remove(this);
    }

    @Override
    protected void onStart() {
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    public void onNext(@NonNull ResultWrap<R> result) {
        if (callback != null) {
            callback.onSuccess(result.get());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (callback != null) {
            ApiException apiException = null;
            if (e instanceof ApiException) {
                apiException = (ApiException) e;
            } else {
                apiException = EHttp.getInstance().getExceptionHandler().handleException(e);
            }
            callback.onFailure(apiException, apiException.getCode(), apiException.getMessage());
            onComplete();//出错也调用完成
        }
    }

    @Override
    public void onComplete() {
        removeDisposable();
        if (callback != null) {
            callback.onComplete();
        }
    }
}
