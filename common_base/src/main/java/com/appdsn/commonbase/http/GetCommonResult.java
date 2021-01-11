package com.appdsn.commonbase.http;

import com.appdsn.commoncore.http.model.ApiResult;

/**
 * 提供的默认的Api返回结果类型
 */
public class GetCommonResult<T> extends ApiResult<T> {
    private int code;
    private String msg;
    private T data;

    @Override
    public String getCode() {
        return code + "";
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isResultOk() {
        return code == 0;
    }
}
