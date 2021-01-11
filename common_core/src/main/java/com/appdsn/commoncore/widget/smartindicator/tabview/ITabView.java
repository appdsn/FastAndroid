package com.appdsn.commoncore.widget.smartindicator.tabview;

import android.content.Context;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @Author: wangbaozhong
 * @Date: 2019/11/14 10:50
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public interface ITabView {
    View createTabView(Context context, int position, LinearLayout parrent);

    /**
     * 被选中
     */
    void onSelected(View tabView, int position, RectF barRectF);

    /**
     * 未被选中
     */
    void onDeselected(View tabView, int position);

    /**
     * 当page在互动的过程中，可以联动indicator
     *
     * @param tabView       是当前的tabView
     * @param position      是当前的tabView对应的索引
     * @param selectPercent 是当前的tabView在正常状态下，任何属性需要改变的百分比
     */
    void onScroll(View tabView, int position, float selectPercent, RectF barRectF);

}
