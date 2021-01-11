package com.appdsn.library.model;

import android.text.TextUtils;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/10 18:40
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class ShareConfig {
    public String mUMAppKey = "5a12384aa40fa3551f0001d1";
    public String mAppChannel = "umeng";
    public Map<SHARE_MEDIA, PlatformConfig.Platform> mConfigMap = new HashMap();

    public static ShareConfig create() {
        ShareConfig config = new ShareConfig();
        return config;
    }

    public ShareConfig setUMeng(String appKey, String channel) {
        if (!TextUtils.isEmpty(appKey)) {
            mUMAppKey = appKey;
            mAppChannel = channel;
        }
        return this;
    }

    public ShareConfig setWeixin(String appId, String appSecret) {
        PlatformConfig.APPIDPlatform platform = (PlatformConfig.APPIDPlatform) new PlatformConfig.APPIDPlatform(SHARE_MEDIA.WEIXIN);
        platform.appId = appId;
        platform.appkey = appSecret;
        mConfigMap.put(platform.getName(), platform);
        return this;
    }

    public ShareConfig setQQ(String appId, String appSecret) {
        PlatformConfig.APPIDPlatform platform = (PlatformConfig.APPIDPlatform) new PlatformConfig.APPIDPlatform(SHARE_MEDIA.QQ);
        platform.appId = appId;
        platform.appkey = appSecret;
        mConfigMap.put(platform.getName(), platform);
        return this;
    }

    public ShareConfig setWeiBo(String appId, String appSecret) {
        PlatformConfig.APPIDPlatform platform = (PlatformConfig.APPIDPlatform) new PlatformConfig.APPIDPlatform(SHARE_MEDIA.SINA);
        platform.appId = appId;
        platform.appkey = appSecret;
        mConfigMap.put(platform.getName(), platform);
        return this;
    }
}
