package com.appdsn.commoncore.widget.banner;

import android.view.View;

/**
 * Desc:
 * <p>
 * Author: ZhangQi
 * Date: 2019/9/9
 * Copyright: Copyright (c) 2016-2020
 * ""
 * ""
 * Update Comments:
 *
 * @author zhangqi
 */
public abstract class BannerOnNoDoubleClickListener implements View.OnClickListener {
    private int mThrottleFirstTime = 1000;
    private long mLastClickTime = 0;

    public BannerOnNoDoubleClickListener() {
    }

    public BannerOnNoDoubleClickListener(int throttleFirstTime) {
        mThrottleFirstTime = throttleFirstTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTime > mThrottleFirstTime) {
            mLastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
