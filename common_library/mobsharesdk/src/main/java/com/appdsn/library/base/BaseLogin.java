package com.appdsn.library.base;

import android.app.Activity;

import com.appdsn.library.listener.OnMobLoginListener;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/11 10:53
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public abstract class BaseLogin {
    public abstract void login(Activity activity, OnMobLoginListener shareListener);
}
