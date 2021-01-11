package com.appdsn.commoncore.utils;


import android.app.Application;

/**
 * Desc:获取全局的ApplicationContext
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 17:48
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class ContextUtils {

    private static Application sContext;

    private ContextUtils() {
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Application context) {
        sContext = context;
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Application getContext() {
        return sContext;
    }
}