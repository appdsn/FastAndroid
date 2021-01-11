package com.appdsn.library.listener;

import com.appdsn.library.model.PlatformType;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:33
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public interface OnMobShareListener {
    /**
     * @param platform 平台类型
     * @descrption 分享开始的回调
     */
    void onStart(PlatformType platform);

    /**
     * @param platform 平台类型
     * @descrption 分享成功的回调
     */
    void onResult(PlatformType platform);

    /**
     * @param platform 平台类型
     * @param t        错误原因
     * @descrption 分享失败的回调
     */
    void onError(PlatformType platform, Throwable t);

    /**
     * @param platform 平台类型
     * @descrption 分享取消的回调
     */
    void onCancel(PlatformType platform);
}
