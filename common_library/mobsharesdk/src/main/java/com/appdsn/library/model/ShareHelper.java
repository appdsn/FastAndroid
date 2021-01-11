package com.appdsn.library.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.appdsn.library.listener.OnMobLoginListener;
import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.qq.QQLogin;
import com.appdsn.library.qq.QQShare;
import com.appdsn.library.sina.SinaLogin;
import com.appdsn.library.sina.SinaShare;
import com.appdsn.library.weixin.WeiXinLogin;
import com.appdsn.library.weixin.WeiXinShare;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:41
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class ShareHelper {
    public static void login(Activity activity, PlatformType platform, OnMobLoginListener loginListener) {
        if (!isInstall(activity, platform)) {
            if (loginListener != null) {
                loginListener.onError(platform, -1, new RuntimeException("未安装该应用"));
            }
            return;
        }
        if (platform == PlatformType.QQ || platform == PlatformType.QZONE) {
            new QQLogin().login(activity, loginListener);
        } else if (platform == PlatformType.WEIXIN || platform == PlatformType.WEIXIN_CIRCLE) {
            new WeiXinLogin().login(activity, loginListener);
        } else if (platform == PlatformType.WEIBO) {
            new SinaLogin().login(activity, loginListener);
        }
    }

    public static void share(PlatformType platform, ShareParams params, OnMobShareListener shareListener) {
        if (!isInstall(params.getActivity(), platform)) {
            if (shareListener != null) {
                shareListener.onError(platform, new RuntimeException("未安装该应用"));
            }
            return;
        }
        if (platform == PlatformType.QQ || platform == PlatformType.QZONE) {
            new QQShare().share(platform, params, shareListener);
        } else if (platform == PlatformType.WEIXIN || platform == PlatformType.WEIXIN_CIRCLE) {
            new WeiXinShare().share(platform, params, shareListener);
        } else if (platform == PlatformType.WEIBO) {
            new SinaShare().share(platform, params, shareListener);
        }
    }

    public static void open(PlatformType[] platformList, ShareBoardConfig config, final ShareParams params, final OnMobShareListener shareListener) {
        if (params == null || platformList == null) {
            return;
        }
        ShareAction shareAction = new ShareAction(params.getActivity());
        shareAction.setDisplayList(getSHARE_MEDIA(platformList));
        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.QQ) {
                    share(PlatformType.QQ, params, shareListener);
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    share(PlatformType.WEIXIN, params, shareListener);
                } else if (share_media == SHARE_MEDIA.QZONE) {
                    share(PlatformType.QZONE, params, shareListener);
                } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    share(PlatformType.WEIXIN_CIRCLE, params, shareListener);
                } else if (share_media == SHARE_MEDIA.SINA) {
                    share(PlatformType.WEIBO, params, shareListener);
                }
            }
        }).open(config);
    }

    public static void open(PlatformType[] platformList, final ShareParams params, final OnMobShareListener shareListener) {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);//设置位置
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);//设置图片形状
        config.setIndicatorVisibility(false);//是否指示翻页指示器
        config.setCancelButtonVisibility(true);//取消按钮是否可见
        open(platformList, config, params, shareListener);
    }

    public static ShareAction getShareAction(ShareParams params) {
        ShareAction shareAction = new ShareAction(params.getActivity());
        BaseMediaObject mediaObject = null;
        switch (params.getShareType()) {
            case TEXT_ONLY:
                shareAction.withText(params.getTitle());
                break;
            case IMAGE_ONLY:
                shareAction.withMedia(getUMImage(params));
                break;
            case WEB_MIX:
                mediaObject = new UMWeb(params.getUrl());
                shareAction.withMedia((UMWeb) mediaObject);
                break;
            case VIDEO_MIX:
                mediaObject = new UMVideo(params.getUrl());
                shareAction.withMedia((UMVideo) mediaObject);
                break;
            case MUSIC_MIX:
                mediaObject = new UMusic(params.getUrl());
                shareAction.withMedia((UMusic) mediaObject);
                break;
        }
        if (mediaObject != null) {
            mediaObject.setTitle(params.getTitle());
            UMImage thumb = getUMImage(params);
            if (thumb != null) {
                mediaObject.setThumb(thumb);
            }
            mediaObject.setDescription(params.getDesc());
        }
        return shareAction;
    }

    private static UMImage getUMImage(ShareParams params) {
        UMImage umImage = null;
        if (params.getImage() instanceof String) {
            umImage = new UMImage(params.getActivity(), (String) params.getImage());//网络图片
        } else if (params.getImage() instanceof Integer) {
            umImage = new UMImage(params.getActivity(), (int) params.getImage());//网络图片
        } else if (params.getImage() instanceof File) {
            umImage = new UMImage(params.getActivity(), (File) params.getImage());//网络图片
        } else if (params.getImage() instanceof Bitmap) {
            umImage = new UMImage(params.getActivity(), (Bitmap) params.getImage());//网络图片
        } else if (params.getImage() instanceof byte[]) {
            umImage = new UMImage(params.getActivity(), (byte[]) params.getImage());//网络图片
        }
        return umImage;
    }

    private static SHARE_MEDIA[] getSHARE_MEDIA(PlatformType[] platformList) {
        SHARE_MEDIA[] share_media = new SHARE_MEDIA[platformList.length];
        for (int i = 0; i < platformList.length; i++) {
            PlatformType type = platformList[i];
            share_media[i] = getSHARE_MEDIA(type);
        }
        return share_media;
    }

    public static SHARE_MEDIA getSHARE_MEDIA(PlatformType type) {
        SHARE_MEDIA share_media = null;
        if (type == PlatformType.QQ) {
            share_media = SHARE_MEDIA.QQ;
        } else if (type == PlatformType.QZONE) {
            share_media = SHARE_MEDIA.QZONE;
        } else if (type == PlatformType.WEIXIN) {
            share_media = SHARE_MEDIA.WEIXIN;
        } else if (type == PlatformType.WEIXIN_CIRCLE) {
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if (type == PlatformType.WEIBO) {
            share_media = SHARE_MEDIA.SINA;
        }
        return share_media;
    }

    public static boolean isInstall(Activity activity, PlatformType platformType) {
        return UMShareAPI.get(activity).isInstall(activity, getSHARE_MEDIA(platformType));
    }
}
