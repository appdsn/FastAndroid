package com.appdsn.library.sina;

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
public class SinaShare extends BaseShare {

    @Override
    public void share(PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        ShareTransActivity.startSinaShare(params.getActivity(), platform, params, shareListener);
    }

    public void startShare(final Activity activity, final PlatformType platform, ShareParams params, final OnMobShareListener shareListener) {
        params.setActivity(activity);
        ShareAction shareAction = ShareHelper.getShareAction(params);
        shareAction.setPlatform(SHARE_MEDIA.SINA);//传入平台

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
