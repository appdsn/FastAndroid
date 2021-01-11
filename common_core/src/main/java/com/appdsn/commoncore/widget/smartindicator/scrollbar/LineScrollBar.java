package com.appdsn.commoncore.widget.smartindicator.scrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Desc:线型颜色滑块
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/29 16:23
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class LineScrollBar extends BaseScrollBar {

    private int mRoundRadius;

    public LineScrollBar(Context context) {
        super(context);
    }

    public void setRoundRadius(int roundRadius) {
        this.mRoundRadius = roundRadius;
    }

    @Override
    protected void drawScrollBar(Canvas canvas, RectF rect, Paint paint) {
        canvas.drawRoundRect(rect, mRoundRadius, mRoundRadius, paint);
    }
}
