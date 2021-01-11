package com.appdsn.library.model;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/10 16:24
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class ShareParams {
    private ShareType mShareType;
    private Activity mActivity;
    private String mTitle;
    private String mDesc;
    private String mUrl;
    private Object mImage;

    public ShareParams(Activity activity, ShareType type) {
        mActivity = activity;
        mShareType = type;
    }

    /*imageUrl支持三种类型：一：网络连接，二：资源ID，三：File图片文件*/
    public static ShareParams create(Activity activity, ShareType type) {
        ShareParams params = new ShareParams(activity, type);
        return params;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public ShareParams setTitle(String title) {
        mTitle = title;
        return this;
    }

    public ShareParams setDesc(String desc) {
        mDesc = desc;
        return this;
    }

    /*链接，视频，音乐都在该方法*/
    public ShareParams setUrl(String url) {
        mUrl = url;
        return this;
    }

    public ShareParams setImage(String imgUrl) {
        mImage = imgUrl;
        return this;
    }

    public ShareParams setImage(int imgResId) {
        mImage = imgResId;
        return this;
    }

    public ShareParams setImage(File imgFile) {
        mImage = imgFile;
        return this;
    }

    public ShareParams setImage(Bitmap imgBitmap) {
        mImage = imgBitmap;
        return this;
    }

    public ShareParams setImage(byte[] imgBytes) {
        mImage = imgBytes;
        return this;
    }

    public ShareType getShareType() {
        return mShareType;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getUrl() {
        return mUrl;
    }

    public Object getImage() {
        return mImage;
    }
}
