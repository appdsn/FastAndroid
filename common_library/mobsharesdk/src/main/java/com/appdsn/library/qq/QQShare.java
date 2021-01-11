package com.appdsn.library.qq;

import android.app.Activity;

import com.appdsn.library.base.BaseShare;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.model.PlatformType;
import com.appdsn.library.model.ShareHelper;
import com.appdsn.library.model.ShareParams;
import com.appdsn.library.model.ShareTransActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:44
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class QQShare extends BaseShare {

    @Override
    public void share(PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        ShareTransActivity.startQQShare(params.getActivity(), platform, params, shareListener);
    }

    public void startShare(final Activity activity, final PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        params.setActivity(activity);
        ShareAction shareAction = ShareHelper.getShareAction(params);

        if (platform == PlatformType.QQ) {
            shareAction.setPlatform(SHARE_MEDIA.QQ);//传入平台
        } else {
            shareAction.setPlatform(SHARE_MEDIA.QZONE);//传入平台
        }
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onStart(platform);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onResult(platform);
                }
                activity.finish();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (shareListener != null) {
                    shareListener.onError(platform, throwable);
                }
                activity.finish();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onCancel(platform);
                }
            }
        });
        shareAction.share();
    }
}
