package com.appdsn.commoncore.imageloader.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.widget.ImageView;

import java.io.File;


public class ImageLoaderOptions implements Cloneable {
    /*基础属性*/
    private Context appContext;
    private Object context;  //生命周期管理
    private Object imageUrl;  // 图片地址
    private ImageSize imageSize;  //设置图片的大小
    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_INSIDE;
    private int errorDrawable = -1;  //是否展示加载错误的图片
    private int holderDrawable = -1;  // 设置展位图

    /*图片变换*/
    private int blurValue = -1;   // 高斯模糊参数，越大越模糊
    private Boolean asGif = null;   //是否作为gif展示
    private Boolean crossFade = null; //是否渐变平滑的显示图片
    private Boolean isCircle = null;//圆形图片
    /*缓存相关*/
    private Boolean skipMemoryCache = null; //是否跳过内存缓存
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.DEFAULT; //磁盘缓存策略

    /*加载回调*/
    private ImageLoaderCallBack loaderResultCallBack;   // 返回图片加载结果

    /*圆角边框相关*/
    private int mBorderColor = Color.TRANSPARENT;
    private float mBorderWidth = -1f;
    private float mCornerRadius = -1f;//圆角半径
    private CornerPosition mCornerPosition = new CornerPosition(true, true, true, true);

    private ImageLoaderOptions(Object context) {
        this.context = context;
    }

    /*imageUrl支持三种类型：一：网络连接，二：资源ID，三：File图片文件*/
    public static ImageLoaderOptions create(@NonNull Context context) {
        ImageLoaderOptions options = new ImageLoaderOptions(context);
        options.setAppContext(context);
        return options;
    }

    public static ImageLoaderOptions create(@NonNull Activity activity) {
        ImageLoaderOptions options = new ImageLoaderOptions(activity);
        options.setAppContext(activity);
        return options;
    }

    public static ImageLoaderOptions create(@NonNull Fragment fragment) {
        ImageLoaderOptions options = new ImageLoaderOptions(fragment);
        options.setAppContext(fragment.getContext());
        return options;
    }

    private void setAppContext(Context context) {
        this.appContext = context;
    }

    public ImageLoaderOptions imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ImageLoaderOptions imageUrl(Integer resourceId) {
        this.imageUrl = resourceId;
        return this;
    }

    public ImageLoaderOptions imageUrl(File imageFile) {
        this.imageUrl = imageFile;
        return this;
    }

    public ImageLoaderOptions placeholder(@DrawableRes int resourceId) {
        this.holderDrawable = resourceId;
        return this;
    }

    public ImageLoaderOptions error(@DrawableRes int resourceId) {
        this.errorDrawable = resourceId;
        return this;
    }

    /*Transition变换使得RoundedImageView无效*/
    public ImageLoaderOptions crossFade(Boolean crossFade) {
        this.crossFade = crossFade;
        return this;
    }

    public ImageLoaderOptions blurImage(@IntRange(from = 0) int blurValue) {
        this.blurValue = blurValue;
        return this;
    }

    public ImageLoaderOptions circleImage(Boolean circle) {
        this.isCircle = circle;
        return this;
    }

    public ImageLoaderOptions roundImage(@Dimension(unit = Dimension.DP) int cornerRadiusDp) {
        this.mCornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDp, appContext.getResources().getDisplayMetrics());
        return this;
    }

    public ImageLoaderOptions cornerPosition(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        mCornerPosition = new CornerPosition(topLeft, topRight, bottomLeft, bottomRight);
        return this;
    }

    public ImageLoaderOptions border(float borderWidthDp, @ColorInt int borderColor) {
        mBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderWidthDp, appContext.getResources().getDisplayMetrics());
        mBorderColor = borderColor;
        return this;
    }

    /*Glide默认是会在内存中缓存处理图(RESULT)的，可以跳过内存缓存。*/
    public ImageLoaderOptions skipMemoryCache(Boolean skipMemoryCache) {
        this.skipMemoryCache = skipMemoryCache;
        return this;

    }

    public ImageLoaderOptions diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
        return this;

    }

    /**
     * 设置图片容器的大小，图片会根据图片容器的宽高范围自动缩放图片到合适的大小
     */
    public ImageLoaderOptions override(int width, int height) {
        this.imageSize = new ImageSize(width, height);
        return this;
    }

    /**
     * 设置图片的裁剪方式，默认原图等比例
     */
    public ImageLoaderOptions scaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    /*传入的图片必须是gif图，其他图会加载失败,当然不使用asGif()方法同样也可以自动识别并加载gif图*/
    public ImageLoaderOptions asGif(Boolean asGif) {
        this.asGif = asGif;
        return this;
    }

    public ImageLoaderOptions loaderCallback(ImageLoaderCallBack resultCallBack) {
        this.loaderResultCallBack = resultCallBack;
        return this;
    }

    public ImageLoaderOptions apply(ImageLoaderOptions other) {
        ImageLoaderOptions result = clone();
        if (other != null) {
            if (other.context != null) {
                result.context = other.context;
            }
            if (other.imageUrl != null) {
                result.imageUrl = other.imageUrl;
            }
            if (other.imageSize != null) {
                result.imageSize = other.imageSize;
            }
            if (other.errorDrawable > 0) {
                result.errorDrawable = other.errorDrawable;
            }
            if (other.holderDrawable > 0) {
                result.holderDrawable = other.holderDrawable;
            }
            if (other.blurValue >= 0) {
                result.blurValue = other.blurValue;
            }
            if (other.mCornerRadius >= 0) {
                result.mCornerRadius = other.mCornerRadius;
            }
            if (other.mCornerPosition != null) {
                result.mCornerPosition = other.mCornerPosition;
            }

            if (other.mBorderWidth >= 0) {
                result.mBorderWidth = other.mBorderWidth;
                result.mBorderColor = other.mBorderColor;
            }

            if (other.diskCacheStrategy != DiskCacheStrategy.DEFAULT) {
                result.diskCacheStrategy = other.diskCacheStrategy;
            }

            if (other.loaderResultCallBack != null) {
                result.loaderResultCallBack = other.loaderResultCallBack;
            }

            if (other.asGif != null) {
                result.asGif = other.asGif;
            }

            if (other.crossFade != null) {
                result.crossFade = other.crossFade;
            }

            if (other.isCircle != null) {
                result.isCircle = other.isCircle;
            }

            if (other.skipMemoryCache != null) {
                result.skipMemoryCache = other.skipMemoryCache;
            }

            result.scaleType = other.scaleType;
        }
        return result;
    }

    @Override
    public ImageLoaderOptions clone() {
        try {
            ImageLoaderOptions result = (ImageLoaderOptions) super.clone();
            return result;
        } catch (Exception e) {
        }
        return this;
    }

    public Object getContext() {
        return context;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public int getHolderDrawable() {
        return holderDrawable;
    }

    public int getBlurValue() {
        return blurValue;
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public boolean isAsGif() {
        return asGif != null && asGif;
    }

    public boolean isCrossFade() {
        return crossFade != null && crossFade;
    }

    public boolean isCircle() {
        return isCircle != null && isCircle;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache != null && skipMemoryCache;
    }

    public DiskCacheStrategy getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public ImageLoaderCallBack getLoaderResultCallBack() {
        return loaderResultCallBack;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public CornerPosition getCornerPosition() {
        return mCornerPosition;
    }

    //对应磁盘缓存策略
    public enum DiskCacheStrategy {
        All,// 同时缓存原图和结果图
        NONE, //不缓存文件
        SOURCE,//缓存原图
        RESULT,//缓存转换后的图
        DEFAULT//默认
    }
}
