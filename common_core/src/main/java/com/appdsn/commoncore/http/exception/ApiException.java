package com.appdsn.commoncore.http.exception;

/**
 * 统一处理了API异常错误EHttp
 */
public class ApiException extends Exception {
    private String code = "-1";

    public ApiException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}