package com.appdsn.commoncore.base;

import com.appdsn.commoncore.widget.xrecyclerview.entity.MultiItemEntity;

/**
 * Desc:所有java bean对象必须继承该类，提供了类型设置，可以结合XRecyclerView快速获取多布局类型
 */
public class BaseEntity extends MultiItemEntity {
    public BaseEntity() {
        super(0);
    }

    public BaseEntity(int itemType) {
        super(itemType);
    }
}
