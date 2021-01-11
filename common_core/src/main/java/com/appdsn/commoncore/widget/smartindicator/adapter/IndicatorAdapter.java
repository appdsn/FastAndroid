package com.appdsn.commoncore.widget.smartindicator.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;

/**
 * Created by wbz360 on 2017/4/7.
 * <p>
 * 获取每一个pager对应的指示器tabitem，scrollBar,扩展性强，可以自定义view
 */
public interface IndicatorAdapter {

    /**
     * 当IndicatorAdapter被添加到ViewPagerIndicator时调用
     */
    void onAttachToIndicator(Context context, SmartIndicator indicator);

    /**
     * 实例化每个tabview（在设置IndicatorAdapter时，会立即循环调用一次性实例化完所有的tabview）
     */
    ITabView getTabView(Context context, int position, LinearLayout parrent);

    /**
     * 实例化一个滚动条，样式可以自定义
     */
    IScrollBar getScrollBar(Context context);

    /**
     * tabview的数量，如果绑定了ViewPager，则tab数量默认和ViewPager数据量保持一致
     */
    int getTabCount();
}
