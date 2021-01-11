package com.appdsn.library.base;

import com.appdsn.library.listener.OnMobShareListener;
import com.appdsn.library.model.PlatformType;
import com.appdsn.library.model.ShareParams;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:54
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public abstract class BaseShare {
    public abstract void share(PlatformType platform, ShareParams params, OnMobShareListener shareListener);
}
