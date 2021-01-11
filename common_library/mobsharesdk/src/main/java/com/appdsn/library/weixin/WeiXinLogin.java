package com.appdsn.library.weixin;

import android.app.Activity;

import com.appdsn.library.base.BaseLogin;
import com.appdsn.library.listener.OnMobLoginListener;
import com.appdsn.library.model.MobLoginInfo;
import com.appdsn.library.model.PlatformType;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:45
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class WeiXinLogin extends BaseLogin {
    @Override
    public void login(Activity activity, final OnMobLoginListener shareListener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onStart(PlatformType.WEIXIN);
                }
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (shareListener != null) {
                    MobLoginInfo loginInfo = new MobLoginInfo();
                    loginInfo.originData = map;
                    if (map != null) {
                        loginInfo.openid = map.get("openid");
                        loginInfo.unionid = map.get("unionid");
                        loginInfo.iconUrl = map.get("iconurl");
                        loginInfo.name = map.get("name");
                        loginInfo.gender = map.get("gender");
                        loginInfo.city = map.get("city");
                        loginInfo.province = map.get("province");
                    }
                    shareListener.onComplete(PlatformType.WEIXIN, i, loginInfo);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (shareListener != null) {
                    shareListener.onError(PlatformType.WEIXIN, i, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (shareListener != null) {
                    shareListener.onCancel(PlatformType.WEIXIN, i);
                }
            }
        });
    }
}
