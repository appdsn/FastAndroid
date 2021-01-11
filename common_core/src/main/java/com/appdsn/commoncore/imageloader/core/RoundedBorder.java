package com.appdsn.commoncore.imageloader.core;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

/**
 * @Desc: java类作用描述
 */
public class RoundedBorder {
    public static Bitmap roundedBorder(
            @NonNull BitmapPool pool, @NonNull Bitmap inBitmap, float cornerRadius, boolean isCircle, float borderWidth, int borderColor, CornerPosition cornerPosition) {
        // Alpha is required for this transformation.
        Bitmap.Config safeConfig = getAlphaSafeConfig(inBitmap);
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);

        result.setHasAlpha(true);

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setShader(shader);
        RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());
        rect.inset(borderWidth / 2, borderWidth / 2);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (isCircle) {
            canvas.drawOval(rect, bitmapPaint);
            canvas.drawOval(rect, borderPaint);
        } else {
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, bitmapPaint);
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint);
            redrawBitmapForSquareCorners(canvas, rect, bitmapPaint, cornerRadius, cornerPosition);
            redrawBorderForSquareCorners(canvas, rect, borderPaint, cornerRadius, cornerPosition, borderWidth);
        }
        canvas.setBitmap(null);
        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    private static void redrawBitmapForSquareCorners(Canvas canvas, RectF rect, Paint paint, float cornerRadius, CornerPosition cornerPosition) {
        if (cornerPosition.allCorner()) {
            // no square corners
            return;
        }

        if (cornerRadius == 0) {
            return; // no round corners
        }

        float left = rect.left;
        float top = rect.top;
        float right = left + rect.width();
        float bottom = top + rect.height();
        float radius = cornerRadius;
        RectF mSquareCornersRect = new RectF();

        if (!cornerPosition.mTopLeft) {
            mSquareCornersRect.set(left, top, left + radius, top + radius);
            canvas.drawRect(mSquareCornersRect, paint);
        }

        if (!cornerPosition.mTopRight) {
            mSquareCornersRect.set(right - radius, top, right, radius);
            canvas.drawRect(mSquareCornersRect, paint);
        }

        if (!cornerPosition.mBottomRight) {
            mSquareCornersRect.set(right - radius, bottom - radius, right, bottom);
            canvas.drawRect(mSquareCornersRect, paint);
        }

        if (!cornerPosition.mBottomLeft) {
            mSquareCornersRect.set(left, bottom - radius, left + radius, bottom);
            canvas.drawRect(mSquareCornersRect, paint);
        }
    }

    private static void redrawBorderForSquareCorners(Canvas canvas, RectF rect, Paint paint, float cornerRadius, CornerPosition cornerPosition, float borderWidth) {
        if (cornerPosition.allCorner()) {
            // no square corners
            return;
        }

        if (cornerRadius == 0) {
            return; // no round corners
        }

        float left = rect.left;
        float top = rect.top;
        float right = left + rect.width();
        float bottom = top + rect.height();
        float radius = cornerRadius;
        float offset = borderWidth / 2;

        if (!cornerPosition.mTopLeft) {
            canvas.drawLine(left - offset, top, left + radius, top, paint);
            canvas.drawLine(left, top - offset, left, top + radius, paint);
        }

        if (!cornerPosition.mTopRight) {
            canvas.drawLine(right - radius - offset, top, right, top, paint);
            canvas.drawLine(right, top - offset, right, top + radius, paint);
        }

        if (!cornerPosition.mBottomRight) {
            canvas.drawLine(right - radius - offset, bottom, right + offset, bottom, paint);
            canvas.drawLine(right, bottom - radius, right, bottom, paint);
        }

        if (!cornerPosition.mBottomLeft) {
            canvas.drawLine(left - offset, bottom, left + radius, bottom, paint);
            canvas.drawLine(left, bottom - radius, left, bottom, paint);
        }
    }


    private static Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }

        return Bitmap.Config.ARGB_8888;
    }

    private static Bitmap getAlphaSafeBitmap(
            @NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Bitmap.Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap =
                pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*paint*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap;
    }
}
