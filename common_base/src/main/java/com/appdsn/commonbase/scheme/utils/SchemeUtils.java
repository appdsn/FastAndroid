package com.appdsn.commonbase.scheme.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.appdsn.commonbase.scheme.SchemeSDK;
import com.appdsn.commonbase.scheme.config.SchemeConfig;
import com.appdsn.commonbase.scheme.config.SchemeEntity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Set;

/**
 * Desc:Url解析工具类
 */
public class SchemeUtils {

    /**
     * 获取url中的参数
     */
    public static SchemeEntity convertUrlToEntity(String url) {
        try {
            JSONObject jsonObject = new JSONObject();
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            jsonObject.put(SchemeConfig.KEY_PATH, path);

            Set<String> names = uri.getQueryParameterNames();
            for (String name : names) {
                jsonObject.put(name, uri.getQueryParameter(name));
            }

            SchemeEntity entity = new Gson().fromJson(jsonObject.toString(), SchemeEntity.class);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SchemeEntity();
    }

    /**
     * 获取url中的参数
     */
    public static String getValueFromUrl(String key, String url) {
        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(key);
    }

    public static String getValueFromUri(String key, Uri uri) {
        return uri.getQueryParameter(key);
    }

    /**
     * 是否支付宝或者微信scheme
     *
     * @param url 重定向url
     * @return 是否支付宝或者微信scheme 默认false
     */
    public static boolean isAliOrWxScheme(String url) {
        return url.contains("alipays://")
                || url.contains("weixin://")
                || url.contains("alipayqr://");
    }

    /**
     * 是否是H5链接
     *
     * @return 是否是H5链接 默认false
     */
    public static boolean isH5Url(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    /**
     * 是否是路由协议
     *
     * @param url 协议地址
     * @return 是否为协议   默认false
     */
    public static boolean isScheme(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        return (TextUtils.equals(SchemeConfig.SCHEME, scheme)
                && TextUtils.equals(SchemeConfig.HOST, host));
    }

    public static boolean isLogin() {
        if (SchemeSDK.getSchemeHandler() != null) {
            return SchemeSDK.getSchemeHandler().isLogin();
        }
        return false;
    }
}
