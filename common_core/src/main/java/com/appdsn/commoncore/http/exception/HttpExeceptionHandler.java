package com.appdsn.commoncore.http.exception;

import retrofit2.HttpException;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/21 17:50
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public class HttpExeceptionHandler {

    public final ApiException handleException(Throwable e) {
        e.printStackTrace();//打印错误信息
        ApiException apiException = null;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //统一处理，或者转化错误等
            apiException = new ApiException(httpException.code() + "", httpException.message(), httpException);
        } else if (e instanceof ApiException) {
            apiException = (ApiException) e;
        } else {
            apiException = new ApiException("-1", e.getMessage(), e);
        }
        handleException(apiException);

        return apiException;
    }

    public void handleException(ApiException e) {

    }
}
