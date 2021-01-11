package com.appdsn.commoncore.widget.xrecyclerview;

import android.content.Context;

import java.util.List;

/**
 * @Desc: 单布局样式
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/16 11:00
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public abstract class SingleRecyclerAdapter<Model> extends BaseRecyclerAdapter<Model> {
    private int mLayoutId;

    public SingleRecyclerAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public SingleRecyclerAdapter(Context context, int layoutId, List<Model> datas) {
        super(context, datas);
        this.mLayoutId = layoutId;
    }

    @Override
    public int getLayoutResId(int viewType) {
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position, Model itemData) {
        return 0;
    }
}
