package com.appdsn.commoncore.http.model;

/**
 * 提供的默认的Api返回结果类型
 */
public class CommonResult<T> extends ApiResult<T> {
    private String code;
    private String msg;
    private T data;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isResultOk() {
        return "200".equals(code);
    }
}
