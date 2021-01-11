package com.appdsn.commonbase.http;

import com.appdsn.commoncore.event.EventBusHelper;
import com.appdsn.commoncore.event.EventMessage;
import com.appdsn.commoncore.http.exception.ApiException;
import com.appdsn.commoncore.http.exception.HttpExeceptionHandler;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/21 18:48
 * @Copyright: Copyright (c) 2016-2020
 * @Company:
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public class GetExceptionHandler extends HttpExeceptionHandler {

    @Override
    public void handleException(ApiException apiException) {
        if (apiException.getCode().equals("100011")) {//登录失效
            EventMessage<String> eventMessage = new EventMessage<>(0, "Session invalidation");
            EventBusHelper.post(eventMessage);
        }
        if (apiException.getCode().equals("100012")) {//账号封禁
            EventMessage<String> eventMessage = new EventMessage<>(0, "Account Banned");
            EventBusHelper.post(eventMessage);
        }
    }
}
