package com.appdsn.commoncore.widget.banner;

/**
 * Desc:
 * <p>
 * Author: ZhangQi
 * Date: 2019/9/9
 * Copyright: Copyright (c) 2016-2020
 * ""
 * ""
 * Update Comments:
 * Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
 *
 * @author zhangqi
 */
public class BannerLocalImageSize {
    private int maxWidth;
    private int maxHeight;
    private float minWidth;
    private float minHeight;

    public BannerLocalImageSize(int maxWidth, int maxHeight, float minWidth, float minHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }
}
