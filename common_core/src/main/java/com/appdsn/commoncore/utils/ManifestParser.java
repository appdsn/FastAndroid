package com.appdsn.commoncore.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.appdsn.commoncore.base.IApplicationDelegate;

import java.util.ArrayList;
import java.util.List;


/**
 * Manifest文件中配置如下信息，必须继承BaseApplication
 * <meta-data
 * android:name="com.example.xxxApplication"
 * android:value="IModuleConfig" />
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/1 15:53
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public final class ManifestParser {
    private static final String MODULE_VALUE = "IModuleConfig";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<IApplicationDelegate> parse() {
        List<IApplicationDelegate> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        IApplicationDelegate delegate = parseModule(key);
                        if (delegate != null) {
                            modules.add(delegate);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modules;
    }

    private static IApplicationDelegate parseModule(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object module = clazz.newInstance();
            if (module instanceof IApplicationDelegate) {
                return (IApplicationDelegate) module;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}