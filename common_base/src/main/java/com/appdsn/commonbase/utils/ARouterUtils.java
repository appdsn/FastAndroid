package com.appdsn.commonbase.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

public class ARouterUtils {
    private ARouterUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 使用 {@link ARouter} 根据 {@code path} 跳转到对应的页面, 这个方法因为没有使用 {@link Activity}跳转
     * 所以 {@link ARouter} 会自动给 {@link Intent} 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 {@link ARouter#getInstance()#navigation(Context, String)} 并传入 {@link Activity}
     *
     * @param path {@code path}
     */
    public static void navigation(String path) {
        navigation((Context) null, path, null, null, -1);
    }

    /**
     * 使用 {@link ARouter} 根据 {@code path} 跳转到对应的页面, 如果参数 {@code context} 传入的不是 {@link Activity}
     * {@link ARouter} 就会自动给 {@link Intent} 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 {@link Activity} 作为 {@code context} 传入
     *
     * @param context
     * @param path
     */
    public static void navigation(Context context, String path) {
        navigation(context, path, null, null, -1);
    }

    public static void navigation(Context context, String path, int[] flags, Bundle bundle, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Postcard postcard = ARouter.getInstance().build(path);
        if (null != bundle) {
            postcard.with(bundle);
        }
        if (null != flags) {
            for (int flag : flags) {
                postcard.addFlags(flag);
            }
        }
        if (requestCode >= 0 && context instanceof Activity) {
            postcard.navigation((Activity) context, requestCode);
        } else {
            postcard.navigation();
        }
    }

    public static void navigation(Fragment fragment, String path, int[] flags, Bundle bundle, int requestCode) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Postcard postcard = ARouter.getInstance().build(path);
        if (null != bundle) {
            postcard.with(bundle);
        }
        if (null != flags) {
            for (int flag : flags) {
                postcard.addFlags(flag);
            }
        }
        if (requestCode >= 0) {
            LogisticsCenter.completion(postcard);
            Class<?> destination = postcard.getDestination();
            Intent intent = new Intent(fragment.getActivity(), destination);
            fragment.startActivityForResult(intent, requestCode);
        } else {
            postcard.navigation();
        }
    }
}
