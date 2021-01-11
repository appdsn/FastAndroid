package com.appdsn.library.weixin;

import com.appdsn.library.base.BaseShare;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.model.PlatformType;
import com.appdsn.library.model.ShareHelper;
import com.appdsn.library.model.ShareParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Desc:
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:45
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class WeiXinShare extends BaseShare {

    @Override
    public void share(PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        ShareAction shareAction = ShareHelper.getShareAction(params);

        if (platform == PlatformType.WEIXIN) {
            shareAction.setPlatform(SHARE_MEDIA.WEIXIN);//传入平台
        } else {
            shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);//传入平台
        }
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onStart(PlatformType.WEIXIN);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onResult(PlatformType.WEIXIN);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (shareListener != null) {
                    shareListener.onError(PlatformType.WEIXIN, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onCancel(PlatformType.WEIXIN);
                }
            }
        });
        shareAction.share();
    }
}
