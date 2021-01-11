package com.appdsn.commoncore.widget.smartindicator.model;

/**
 * Desc:tabview位置信息
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/12 17:20
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class TabPositionInfo {
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public TabPositionInfo(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public int getLeft() {
        return mLeft;
    }

    public int getTop() {
        return mTop;
    }

    public int getRight() {
        return mRight;
    }

    public int getBottom() {
        return mBottom;
    }

    public int getHeight() {
        return mBottom - mTop;
    }

    public int getWidth() {
        return mRight - mLeft;
    }

    public int getCenterX() {
        return mLeft + getWidth() / 2;
    }

    public int getCenterY() {
        return mTop + getHeight() / 2;
    }
}
