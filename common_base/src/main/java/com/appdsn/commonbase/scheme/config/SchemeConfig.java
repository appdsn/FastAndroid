package com.appdsn.commonbase.scheme.config;

/**
 * Desc:自行配置项目中使用到的字段
 *
 * @Author: wangbaozhong
 * @Date: 2020/12/8 16:44
 */
public interface SchemeConfig {
    /**
     * 配置项目自己的协议头
     */
    String SCHEME = "hdj";
    String HOST = "com.xiaoniu.doudouyima";

    /**
     * 是否需要登录
     */
    String KEY_NEED_LOGIN = "login";

    /**
     * 跳转url key
     */
    String KEY_H5_URL = "url";

    /**
     * 路径 key
     */
    String KEY_PATH = "path";

    /**
     * 跳转url key
     */
    String KEY_SCHEME_URL = "scheme_url";

    /**
     * 跳转url key
     */
    String KEY_CALLBACK_METHOD = "callbackMethod";

    /**
     * 浏览器页
     */
    String PATH_BROWSER_ACTIVITY = "/main/browser";

    /**
     * 登录页
     */
    String PATH_LOGIN_ACTIVITY = "/mine/login";

}
