package com.appdsn.commoncore.widget.xrecyclerview;

import android.content.Context;

import java.util.List;

/**
 * @Desc: 多布局支持
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/16 11:01
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public abstract class MultiRecyclerAdapter<Model> extends BaseRecyclerAdapter<Model> {

    public MultiRecyclerAdapter(Context context) {
        super(context, null);
    }

    public MultiRecyclerAdapter(Context context, List<Model> datas) {
        super(context, datas);
    }
}
