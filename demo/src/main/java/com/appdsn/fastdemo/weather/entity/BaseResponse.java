package com.appdsn.fastdemo.weather.entity;

import java.io.Serializable;


/**
 * 网络返回数据基类
 */

public class BaseResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
    private long timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return code == ErrorCode.SUCCESS;
    }
}