package com.appdsn.commoncore.widget.smartindicator.scrollbar;

import android.graphics.RectF;

import com.appdsn.commoncore.widget.smartindicator.model.TabPositionInfo;

/**
 * Desc:自定义滑块接口
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/29 16:22
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public interface IScrollBar {
    /**
     * 必须是一个继承自View的类,positionOffset是滑动离开curTabView的比例，接近nextTabView的比例
     */
    RectF onScroll(TabPositionInfo curTabInfo, TabPositionInfo nextTabInfo, float positionOffset);

    RectF onSelected(TabPositionInfo curTabInfo);
}
