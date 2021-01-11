package com.appdsn.commoncore.widget.xrecyclerview.loadmore;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/15 19:44
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public enum LoadState {
    /**
     * 没有更多数据
     */
    NO_MORE_DATA,

    /**
     * 成功状态，或者初始化的状态
     */
    NORMAL,

    /**
     * 正在加载中
     */
    LOADING,
    /**
     * 加载失败
     */
    FAILED,
}
