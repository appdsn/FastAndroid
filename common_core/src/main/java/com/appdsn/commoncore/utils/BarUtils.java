package com.appdsn.commoncore.utils;


import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;

public class BarUtils {

    /**
     * 设置状态栏颜色
     *
     * @param colorId 颜色资源id
     */
    public static void setStatusBarColor(Activity activity, @ColorRes int colorId) {
        setStatusBarColor(ImmersionBar.with(activity), colorId, false, true);
    }

    public static void setStatusBarColor(Fragment fragment, @ColorRes int colorId) {
        setStatusBarColor(ImmersionBar.with(fragment), colorId, false, true);
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorId 颜色资源id
     * @param isDark  状态栏字体颜色模式：黑色或者白色，默认白色字体
     */
    public static void setStatusBarColor(Activity activity, @ColorRes int colorId, boolean isDark) {
        setStatusBarColor(ImmersionBar.with(activity), colorId, isDark, false);
    }

    public static void setStatusBarColor(Fragment fragment, @ColorRes int colorId, boolean isDark) {
        setStatusBarColor(ImmersionBar.with(fragment), colorId, isDark, false);
    }

    private static void setStatusBarColor(ImmersionBar immersionBar, @ColorRes int colorId, boolean isDark, boolean isAuto) {
        immersionBar
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(colorId)    //状态栏颜色，不写默认透明色
                .flymeOSStatusBarFontColor(colorId);  //修改flyme OS状态栏字体颜色
        if (isAuto) {
            immersionBar.autoStatusBarDarkModeEnable(true, 0.2f); //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
        } else {
            immersionBar.statusBarDarkFont(isDark);    //状态栏字体是深色，不写默认false为亮色
        }
        immersionBar.init();
    }

    public static void setStatusBarTranslucent(Fragment fragment) {
        setStatusBarTranslucent(ImmersionBar.with(fragment), null, null, false, true);
    }

    public static void setStatusBarTranslucent(Activity activity) {
        setStatusBarTranslucent(ImmersionBar.with(activity), null, null, false, true);
    }

    /**
     * paddingTopView通常是根布局
     */
    public static void setStatusBarTranslucentPaddingTop(Fragment fragment, View paddingTopView) {
        setStatusBarTranslucent(ImmersionBar.with(fragment), paddingTopView, null, false, true);
    }

    /**
     * paddingTopView通常是根布局
     */
    public static void setStatusBarTranslucentPaddingTop(Activity activity, View paddingTopView) {
        setStatusBarTranslucent(ImmersionBar.with(activity), paddingTopView, null, false, true);
    }

    /**
     * paddingTopView通常是根布局
     */
    public static void setStatusBarTranslucentPaddingTop(Fragment fragment, View paddingTopView, boolean isDark) {
        setStatusBarTranslucent(ImmersionBar.with(fragment), paddingTopView, null, isDark, false);
    }

    /**
     * paddingTopView通常是根布局
     */
    public static void setStatusBarTranslucentPaddingTop(Activity activity, View paddingTopView, boolean isDark) {
        setStatusBarTranslucent(ImmersionBar.with(activity), paddingTopView, null, isDark, false);
    }

    /**
     * marginTopView通常是最上面的一个子布局
     */
    public static void setStatusBarTranslucentMarginTop(Fragment fragment, View marginTopView) {
        setStatusBarTranslucent(ImmersionBar.with(fragment), null, marginTopView, false, true);
    }

    /**
     * marginTopView通常是最上面的一个子布局
     */
    public static void setStatusBarTranslucentMarginTop(Activity activity, View marginTopView) {
        setStatusBarTranslucent(ImmersionBar.with(activity), null, marginTopView, false, true);
    }

    /**
     * marginTopView通常是最上面的一个子布局
     */
    public static void setStatusBarTranslucentMarginTop(Fragment fragment, View marginTopView, boolean isDark) {
        setStatusBarTranslucent(ImmersionBar.with(fragment), null, marginTopView, isDark, false);
    }

    /**
     * marginTopView通常是最上面的一个子布局
     */
    public static void setStatusBarTranslucentMarginTop(Activity activity, View marginTopView, boolean isDark) {
        setStatusBarTranslucent(ImmersionBar.with(activity), null, marginTopView, isDark, false);
    }

    private static void setStatusBarTranslucent(ImmersionBar immersionBar, View paddingTopView, View marginTopView, boolean isDark, boolean isAuto) {
        immersionBar.fitsSystemWindows(false);  //使用该属性,必须指定状态栏颜色
        if (paddingTopView != null) {
            immersionBar.titleBar(paddingTopView);    //解决状态栏和布局重叠问题，任选其一
        } else if (marginTopView != null) {
            immersionBar.titleBarMarginTop(marginTopView); //解决状态栏和布局重叠问题，任选其一
        }
        if (isAuto) {
            immersionBar.autoStatusBarDarkModeEnable(true, 0.2f); //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
        } else {
            immersionBar.statusBarDarkFont(isDark); //状态栏字体是深色，不写默认false为亮色
        }
        immersionBar.init();  //必须调用方可应用以上所配置的参数
    }

    public static int getStatusBarHeight(Activity activity) {
        return ImmersionBar.getStatusBarHeight(activity);
    }

    public static int getNavigationBarHeight(Activity activity) {
        return ImmersionBar.getNavigationBarHeight(activity);
    }

    public static int getNavigationBarHeight(Fragment fragment) {
        return ImmersionBar.getNavigationBarHeight(fragment);
    }

    public static void reset(Activity activity) {
        ImmersionBar.with(activity).reset().init();
    }

    public static void reset(Fragment fragment) {
        ImmersionBar.with(fragment).reset().init();
    }
}
