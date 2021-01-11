package com.appdsn.library;

import android.app.Activity;
import android.content.Context;

import com.appdsn.library.listener.OnMobLoginListener;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.model.PlatformType;
import com.appdsn.library.model.ShareConfig;
import com.appdsn.library.model.ShareHelper;
import com.appdsn.library.model.ShareParams;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/10 16:15
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class MobShareSDK {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void init(Context context, ShareConfig config) {
        sContext = context.getApplicationContext();
        UMConfigure.init(context, config.mUMAppKey
                , config.mAppChannel, UMConfigure.DEVICE_TYPE_PHONE, "");
        if (config.mConfigMap.containsKey(SHARE_MEDIA.WEIXIN)) {
            PlatformConfig.Platform platform = config.mConfigMap.get(SHARE_MEDIA.WEIXIN);
            PlatformConfig.setWeixin(platform.getAppid(), platform.getAppSecret());
        }

        if (config.mConfigMap.containsKey(SHARE_MEDIA.QQ)) {
            PlatformConfig.Platform platform = config.mConfigMap.get(SHARE_MEDIA.QQ);
            PlatformConfig.setQQZone(platform.getAppid(), platform.getAppSecret());
        }

        if (config.mConfigMap.containsKey(SHARE_MEDIA.SINA)) {
            PlatformConfig.Platform platform = config.mConfigMap.get(SHARE_MEDIA.SINA);
            PlatformConfig.setSinaWeibo(platform.getAppid(), platform.getAppSecret(), "http://sns.whalecloud.com");
        }

        UMShareConfig umShareConfig = new UMShareConfig();
        umShareConfig.isNeedAuthOnGetUserInfo(true);//设置是否为在Token有效期内登录不进行二次授权
        UMShareAPI.get(context).setShareConfig(umShareConfig);
    }

    /*打开分享面板, 自定义分享面板*/
    public static void openShareBoard(PlatformType[] platformList, ShareBoardConfig config, ShareParams params, OnMobShareListener onShareListener) {
        ShareHelper.open(platformList, config, params, onShareListener);
    }

    /*打开分享面板*/
    public static void openShareBoard(PlatformType[] platformList, ShareParams params, OnMobShareListener onShareListener) {
        ShareHelper.open(platformList, params, onShareListener);
    }

    /*直接分享*/
    public static void shareDirect(PlatformType platformType, ShareParams params, OnMobShareListener onShareListener) {
        ShareHelper.share(platformType, params, onShareListener);
    }

    /*第三方登录*/
    public static void login(Activity activity, PlatformType platformType, OnMobLoginListener onLoginListener) {
        ShareHelper.login(activity, platformType, onLoginListener);
    }

    /*对于退出登录，想要删除授权的用户可以使用如下接口*/
    public static void deleteOauth(Activity activity, PlatformType platformType) {
        UMShareAPI.get(activity).deleteOauth(activity, ShareHelper.getSHARE_MEDIA(platformType), null);
    }

    public static boolean isInstall(Activity activity, SHARE_MEDIA media) {
        return UMShareAPI.get(getContext()).isInstall(activity, media);
    }

    public static boolean isInstall(Activity activity, PlatformType platformType) {
        return ShareHelper.isInstall(activity, platformType);
    }
}
