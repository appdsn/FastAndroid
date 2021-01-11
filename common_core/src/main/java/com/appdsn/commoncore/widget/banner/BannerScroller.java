package com.appdsn.commoncore.widget.banner;

import android.content.Context;
import android.widget.Scroller;

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
public class BannerScroller extends Scroller {
    private int mDuration = 1000;

    public BannerScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
