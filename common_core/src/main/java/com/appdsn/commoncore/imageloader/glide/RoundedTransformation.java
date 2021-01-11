package com.appdsn.commoncore.imageloader.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdsn.commoncore.imageloader.core.CornerPosition;
import com.appdsn.commoncore.imageloader.core.RoundedBorder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class RoundedTransformation extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int mBorderColor = 0;
    private float mBorderWidth = 0f;

    private float mCornerRadius = 0f;

    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    private boolean mOval = false;
    private CornerPosition mCornerPosition;
    private View mDisplayView;

    public static RoundedTransformation create(float radius, ImageView.ScaleType scaleType) {
        return new RoundedTransformation(radius, scaleType);
    }

    private RoundedTransformation(float radius, ImageView.ScaleType scaleType) {
        if (radius > 0) {
            this.mCornerRadius = radius;
        }
        if (scaleType != null) {
            this.mScaleType = scaleType;
        }
    }

    public RoundedTransformation setCornerPosition(CornerPosition cornerPosition) {
        if (cornerPosition == null) {
            cornerPosition = new CornerPosition(true, true, true, true);
        }
        mCornerPosition = cornerPosition;
        return this;
    }

    public RoundedTransformation setBorderColor(int color) {
        mBorderColor = color;
        return this;
    }

    public RoundedTransformation setDisplayView(View displayView) {
        mDisplayView = displayView;
        return this;
    }

    public RoundedTransformation setBorderWidth(float borderWidth) {
        if (borderWidth > 0) {
            mBorderWidth = borderWidth;
        }
        return this;
    }

    public RoundedTransformation setOval(boolean oval) {
        mOval = oval;
        return this;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        outWidth = fixOutWidth(toTransform, outWidth, outHeight);
        outHeight = fixOutHeight(toTransform, outWidth, outHeight);

        Bitmap inBitmap = null;
        switch (mScaleType) {
            case CENTER_CROP:
                inBitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
                break;
            case CENTER_INSIDE:
                inBitmap = TransformationUtils.centerInside(pool, toTransform, outWidth, outHeight);
                break;
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
                inBitmap = TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight);
                break;
            case FIT_XY:
                inBitmap = fitXY(pool, toTransform, outWidth, outHeight);
                break;
            case CENTER:
            case MATRIX:
            default:
                inBitmap = toTransform;
                // Do nothing.
        }
        return RoundedBorder.roundedBorder(pool, inBitmap, mCornerRadius, mOval, mBorderWidth, mBorderColor, mCornerPosition);
    }

    private int fixOutHeight(Bitmap toTransform, int outWidth, int outHeight) {
        if (mDisplayView == null || toTransform == null || mDisplayView.getLayoutParams() == null) {
            return outHeight;
        }
        int paramWidth = mDisplayView.getLayoutParams().width;
        int paramHeight = mDisplayView.getLayoutParams().height;

        if (paramHeight == ViewGroup.LayoutParams.WRAP_CONTENT && paramWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
            float ratio = toTransform.getHeight() * 1f / toTransform.getWidth();
            outHeight = (int) (outWidth * ratio);
        } else if (paramWidth == ViewGroup.LayoutParams.WRAP_CONTENT && paramHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            outHeight = toTransform.getHeight();
        }
        return outHeight;
    }

    private int fixOutWidth(Bitmap toTransform, int outWidth, int outHeight) {
        if (mDisplayView == null || toTransform == null || mDisplayView.getLayoutParams() == null) {
            return outWidth;
        }
        int paramWidth = mDisplayView.getLayoutParams().width;
        int paramHeight = mDisplayView.getLayoutParams().height;

        if (paramWidth == ViewGroup.LayoutParams.WRAP_CONTENT && paramHeight != ViewGroup.LayoutParams.WRAP_CONTENT) {
            float ratio = toTransform.getWidth() * 1f / toTransform.getHeight();
            outWidth = (int) (outHeight * ratio);
        } else if (paramWidth == ViewGroup.LayoutParams.WRAP_CONTENT && paramHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            outWidth = toTransform.getWidth();
        }
        return outWidth;
    }

    private Bitmap fitXY(BitmapPool pool, Bitmap inBitmap, int width, int height) {
        if (inBitmap.getWidth() <= width && inBitmap.getHeight() <= height) {
            return inBitmap;
        } else {
            Bitmap.Config config = getNonNullConfig(inBitmap);
            Bitmap toReuse = pool.get(width, height, config);

            // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
            TransformationUtils.setAlpha(inBitmap, toReuse);

            BitmapShader shader = new BitmapShader(inBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            Paint bitmapPaint = new Paint();
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setStyle(Paint.Style.FILL);
            RectF rect = new RectF(0, 0, width, height);
            RectF bitmapRect = new RectF(0, 0, inBitmap.getWidth(), inBitmap.getHeight());
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(bitmapRect, rect, Matrix.ScaleToFit.FILL);
            Canvas canvas = new Canvas(toReuse);
            canvas.drawBitmap(inBitmap, shaderMatrix, bitmapPaint);
            canvas.setBitmap(null);
            return toReuse;
        }
    }


    @NonNull
    private static Bitmap.Config getNonNullConfig(@NonNull Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundedTransformation &&
                ((RoundedTransformation) o).mBorderColor == mBorderColor &&
                ((RoundedTransformation) o).mBorderWidth == mBorderWidth &&
                ((RoundedTransformation) o).mCornerRadius == mCornerRadius &&
                ((RoundedTransformation) o).mOval == mOval &&
                ((RoundedTransformation) o).mScaleType == mScaleType &&
                ((RoundedTransformation) o).mCornerPosition.equals(mCornerPosition);
    }

    @Override
    public int hashCode() {
        return (int) (ID.hashCode() + mBorderColor * 10000 + mBorderWidth * 1000 + mCornerRadius * 100 + mScaleType.ordinal() * 10 + (mOval ? 1 : 0) + mCornerPosition.getKey());
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + mBorderColor + mBorderWidth + mCornerRadius + mOval + mScaleType + mCornerPosition).getBytes(CHARSET));
    }
}
