package com.appdsn.library.listener;

import com.appdsn.library.model.MobLoginInfo;
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
public interface OnMobLoginListener {
    /**
     * @param platform 平台名称
     * @desc 授权开始的回调
     */
    void onStart(PlatformType platform);

    /**
     * @param platform  平台名称
     * @param action    行为序号，开发者用不上
     * @param loginInfo 用户资料返回
     * @desc 授权成功的回调
     */
    void onComplete(PlatformType platform, int action, MobLoginInfo loginInfo);

    /**
     * @param platform 平台名称
     * @param action   行为序号，开发者用不上
     * @param t        错误原因
     * @desc 授权失败的回调
     */
    void onError(PlatformType platform, int action, Throwable t);

    /**
     * @param platform 平台名称
     * @param action   行为序号，开发者用不上
     * @desc 授权取消的回调
     */
    void onCancel(PlatformType platform, int action);
}
