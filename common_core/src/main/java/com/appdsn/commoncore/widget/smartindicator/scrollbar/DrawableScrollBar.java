package com.appdsn.commoncore.widget.smartindicator.scrollbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

/**
 * 图片指示器
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/16 19:07
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class DrawableScrollBar extends BaseScrollBar {

    private Bitmap mBitmap;

    public DrawableScrollBar(Context context) {
        super(context);
    }

    public void setDrawable(int drawableRes) {
        try {
            mBitmap = ((BitmapDrawable) getResources().getDrawable(drawableRes)).getBitmap();
        } catch (Exception e) {
        }
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    protected void drawScrollBar(Canvas canvas, RectF rect, Paint paint) {
        if (mBitmap != null) {
            Rect srcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, srcRect, rect, paint);
        }
    }
}
