package com.appdsn.commoncore.widget.xrecyclerview.loadmore;

import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/15 20:13
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public interface OnLoadListener {

    /**
     * 加载更多时会被调用或上拉时调用，子类实现具体的业务逻
     */
    void onLoad(XRecyclerView recyclerView);
}
