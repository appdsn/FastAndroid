package com.appdsn.commonbase.scheme.config;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/12/8 18:33
 */
public interface FunctionType {
    String H5 = "h5";//打开h5页面， 需要依赖url字段
    String ACTIVITY = "activity";//打开原生页面， 需要依赖path字段和tabId字段
    String SHARE = "share";//分享
    String CLOSE = "close";//关闭当前页面
    String CHAT = "chat";//跳转到聊天页面
    String HOME = "home";//跳转到首页面
}
