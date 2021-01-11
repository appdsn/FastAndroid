package com.appdsn.commoncore.http.model;

/**
 * Desc:Rxjava的apply等方法返回的结果不能为null，否则直接抛异常，并走向RxJava的onError()
 */
public class ResultWrap<R> {
    // 接收到的返回结果
    private R result;

    public ResultWrap(R result) {
        this.result = result;
    }

    public R get() {
        return result;
    }
}