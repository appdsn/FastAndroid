package com.appdsn.commonbase.widget.imageSelector;

import java.io.Serializable;

/**
 * Desc:打开选择器配置
 * <p>
 * Author: zhoutao
 * Date: 2019/10/15
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:zhoutao@appdsn.com
 * Update Comments:
 *
 * @author zhoutao
 */
public class SelectConfig implements Serializable {
    /**
     * 请求码
     */
    private int requestCode = 1;
    /**
     * 图片选择数量
     * 最小为 1
     * 最大为 9
     * * {@link #getMaxNum()}
     */
    private int maxNum = 1;
    /**
     * 是否需要裁减
     */
    private boolean cropMode;
    /**
     * 强制选择
     */
    private boolean forceSelect;

    /**
     * 裁减比例 默认X=1 Y=1
     */
    private int ratioX = 1;
    private int ratioY = 1;
    /**
     * 图片每行显示数量
     */
    private int spanCount = 4;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getMaxNum() {
        if (maxNum == 0) {
            return 1;
        } else if (maxNum > 9) {
            return 9;
        }
        return maxNum;
    }

    public SelectConfig setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        return this;
    }


    public boolean isCropMode() {
        return cropMode;
    }

    /**
     * 注意:设置为裁减模式时,图片最多只能选中一张
     *
     * @return
     */
    public SelectConfig setCropMode(boolean cropMode) {
        this.cropMode = cropMode;
        return this;
    }

    public boolean isForceSelect() {
        return forceSelect;
    }

    public SelectConfig setForceSelect(boolean forceSelect) {
        this.forceSelect = forceSelect;
        return this;
    }

    public int getRatioX() {
        return ratioX;
    }

    public void setRatioX(int ratioX) {
        this.ratioX = ratioX;
    }

    public int getRatioY() {
        return ratioY;
    }

    public void setRatioY(int ratioY) {
        this.ratioY = ratioY;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }
}