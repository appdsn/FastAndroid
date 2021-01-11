package com.appdsn.commoncore.widget.banner.transformer;

import android.support.v4.view.ViewPager;
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
public abstract class BannerPageTransformer implements ViewPager.PageTransformer {
    public void transformPage(View view, float position) {
        if (position < -1.0f) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            // [-1,0]
            // Use the default slide transition when moving to the left page
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            // (0,1]
            handleRightPage(view, position);
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            handleInvisiblePage(view, position);
        }
    }

    public abstract void handleInvisiblePage(View view, float position);

    public abstract void handleLeftPage(View view, float position);

    public abstract void handleRightPage(View view, float position);

    public static BannerPageTransformer getPageTransformer(TransitionEffect effect) {
        switch (effect) {
            case Default:
                return new DefaultPageTransformer();
            default:
                return new DefaultPageTransformer();
        }
    }
}
