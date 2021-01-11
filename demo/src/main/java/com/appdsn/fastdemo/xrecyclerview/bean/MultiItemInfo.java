package com.appdsn.fastdemo.xrecyclerview.bean;

import java.io.Serializable;

public class MultiItemInfo implements Serializable {
    public String mTitle = "";
    public int mItemType = 0;

    public MultiItemInfo(int itemType, String title) {
        mItemType = itemType;
        mTitle = title;
    }
}
