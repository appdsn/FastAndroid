package com.appdsn.commoncore.widget.xrecyclerview.entity;

import java.io.Serializable;

public class MultiItemEntity implements Serializable {
    private int mItemType = 0;

    public MultiItemEntity(int itemType) {
        mItemType = itemType;
    }

    public int getItemType() {
        return mItemType;
    }
}
