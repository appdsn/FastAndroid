package com.appdsn.commonbase.scheme.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * Desc:
 * <p>
 * Author: AnYaBo
 * Date: 2019/11/2
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 * 兼容部分机型从arm覆盖安装升级到arm64版本时，WebView 因找不到 lib64 so 库出现奔溃的问题
 * 如：魅族 16X、坚果R1就会出现上述问题
 *
 * @author anyabo
 */
public final class Abi64WebViewCompat {

    //参见源码： com.android.webview.chromium.WebViewChromiumFactoryProvider
    private static final String CHROMIUM_PREFS_NAME = "WebViewChromiumPrefs";

    private static final String APP_WEB_VIEW_DIR_NAME = "app_webview";

    private static final String GPU_CACHE_DIR_NAME = "GPUCache";

    public static void obliterate(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return;
        }

        try {
            final Context appContext = context;

            //移除：shared_prefs/WebViewChromiumPrefs.xml
            final SharedPreferences chromiumPrefs = appContext.getSharedPreferences(
                    CHROMIUM_PREFS_NAME,
                    Context.MODE_PRIVATE
            );
            chromiumPrefs.edit().clear().apply();

            //移除：app_webview 目录
            final File appWebViewDir = new File(
                    appContext.getDataDir() + File.separator
                            + APP_WEB_VIEW_DIR_NAME + File.separator
                            + GPU_CACHE_DIR_NAME
            );
            deleteRecursive(appWebViewDir);
        } catch (Exception e) {
            printInfo(e.getMessage());
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory()) {
                for (File child : fileOrDirectory.listFiles()) {
                    deleteRecursive(child);
                }
            }
            boolean isSuccessDelete = fileOrDirectory.delete();
            printInfo("delete isSuccessDelete: " + isSuccessDelete + " fileName: " + fileOrDirectory);
        } catch (Exception e) {
        }
    }

    private static void printInfo(String message) {
        Log.i("Abi64WebViewCompat", message);
    }

}
