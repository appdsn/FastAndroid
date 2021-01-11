package com.appdsn.commoncore.widget.xrecyclerview.decoration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/15 9:56
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public final class DecorationPaint {

    public static void drawNinePatch(Canvas canvas, NinePatch ninePatch, Rect rect) {
        ninePatch.draw(canvas, rect);
    }

    public static void drawNinePatch(Canvas canvas, NinePatch ninePatch, int left, int top, int right, int bottom) {
        ninePatch.draw(canvas, new Rect(left, top, right, bottom));
    }

    public static void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int left, int top) {
        canvas.drawBitmap(bitmap, left, top, paint);
    }

    public static void drawLine(Canvas canvas, Paint paint, int startX, int startY, int stopX, int stopY) {
        drawDashLine(canvas, paint, 0, 0, startX, startY, stopX, stopY);
    }

    public static void drawDashLine(Canvas canvas, Paint paint, int dashWidth, int dashGap
            , int startX, int startY, int stopX, int stopY) {
        if (dashWidth == 0 && dashGap == 0) {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, dashWidth, paint.getStrokeWidth()}, dashGap);
            paint.setPathEffect(effects);
        }
        canvas.drawLine(startX, startY, stopX, stopY, paint);

    }

    public static void drawPath(Canvas canvas, Paint paint, Path path) {
        canvas.drawPath(path, paint);
    }
}
