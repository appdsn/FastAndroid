package com.appdsn.commoncore.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.appdsn.commoncore.imageloader.core.IImageLoaderStrategy;
import com.appdsn.commoncore.imageloader.core.ImageLoaderCallBack;
import com.appdsn.commoncore.imageloader.core.ImageLoaderConfig;
import com.appdsn.commoncore.imageloader.core.ImageLoaderOptions;
import com.appdsn.commoncore.imageloader.core.ImageLoaderRequest;
import com.appdsn.commoncore.imageloader.core.LoaderType;
import com.appdsn.commoncore.imageloader.glide.GlideImageLoader;
import com.appdsn.commoncore.utils.ContextUtils;

import java.util.HashMap;

/**
 * Desc:图片加载：圆角图，圆形图，边框，高斯模糊，GIF，错误图等，支持从网络，文件，资源ID加载
 */
public class ImageLoader {
    private static ImageLoaderOptions sDefaultOptions;
    private static Context sAppContext;
    private static HashMap<LoaderType, IImageLoaderStrategy> sSupportImageLoader = new HashMap<>();
    private static IImageLoaderStrategy sDefaultLoaderStrategy;

    static {
        sSupportImageLoader.put(LoaderType.GLIDE, new GlideImageLoader());
    }

    public static void init(ImageLoaderConfig configuration) {
        sDefaultOptions = configuration.defaultOptions;
        sDefaultLoaderStrategy = sSupportImageLoader.get(configuration.defaultLoaderType);
        sAppContext = configuration.context.getApplicationContext();
        for (IImageLoaderStrategy loaderStrategy : sSupportImageLoader.values()) {
            if (loaderStrategy != null) {
                loaderStrategy.init(configuration);
            }
        }
    }

    /*显示Http网络图片*/
    public static void displayImage(String imageUrl, ImageView displayView) {
        displayImageInner(imageUrl, displayView, -1, -1, false, -1);
    }

    public static void displayImage(String imageUrl, ImageView displayView, int defaultImage) {
        displayImageInner(imageUrl, displayView, defaultImage, -1, false, -1);
    }

    public static void displayRoundImage(String imageUrl, ImageView displayView, int radiusDp) {
        displayImageInner(imageUrl, displayView, -1, radiusDp, false, -1);
    }

    public static void displayRoundImage(String imageUrl, ImageView displayView, int radiusDp, int defaultImage) {
        displayImageInner(imageUrl, displayView, defaultImage, radiusDp, false, -1);
    }

    public static void displayCircleImage(String imageUrl, ImageView displayView) {
        displayImageInner(imageUrl, displayView, -1, -1, true, -1);
    }

    public static void displayCircleImage(String imageUrl, ImageView displayView, int defaultImage) {
        displayImageInner(imageUrl, displayView, defaultImage, -1, true, -1);
    }

    public static void displayBlurImage(String imageUrl, ImageView displayView, int blurValue) {
        displayImageInner(imageUrl, displayView, -1, -1, false, blurValue);
    }

    public static void displayBlurImage(String imageUrl, ImageView displayView, int blurValue, int defaultImage) {
        displayImageInner(imageUrl, displayView, defaultImage, -1, false, blurValue);
    }

    private static void displayImageInner(String imageUrl, ImageView displayView, int defaultImage, int radiusDp, boolean isCircle, int blurValue) {
        ImageLoaderOptions options = ImageLoaderOptions.create(displayView.getContext())
                .imageUrl(imageUrl)
                .roundImage(radiusDp)
                .circleImage(isCircle)
                .blurImage(blurValue)
                .placeholder(defaultImage)
                .error(defaultImage);
        displayImage(displayView, options);
    }

    /*自定义显示效果*/
    public static void displayImage(ImageView displayView, ImageLoaderOptions options) {
        if (options == null
                || displayView == null
                || isDestroyed(options.getContext())) {
            return;
        }
        if (sDefaultLoaderStrategy == null) {
            init(ImageLoaderConfig.newBuilder(ContextUtils.getContext()).build());
        }
        ImageLoaderRequest request = new ImageLoaderRequest();
        request.displayView(displayView);
        request.imageLoaderOptions(sDefaultOptions.apply(options));
        sDefaultLoaderStrategy.displayImage(request);
    }

    /**
     * 预加载网络图片到磁盘缓存， 这里如果传的是activity，如果已经销毁了，会抛出异常
     */
    public static void loadImage(ImageLoaderOptions options) {
        if (options == null || isDestroyed(options.getContext())) {
            return;
        }
        if (sDefaultLoaderStrategy == null) {
            init(ImageLoaderConfig.newBuilder(ContextUtils.getContext()).build());
        }
        options.diskCacheStrategy(ImageLoaderOptions.DiskCacheStrategy.SOURCE);
        ImageLoaderRequest request = new ImageLoaderRequest();
        request.imageLoaderOptions(sDefaultOptions.apply(options));
        sDefaultLoaderStrategy.loadImage(request);
    }

    /*预加载网络图片到磁盘缓存*/
    public static void loadImage(String imageUrl, ImageLoaderCallBack loaderCallBack) {
        ImageLoaderOptions options = ImageLoaderOptions.create(sAppContext).imageUrl(imageUrl).loaderCallback(loaderCallBack);
        loadImage(options);
    }

    public static void clearMemoryCache() {
        if (sDefaultLoaderStrategy != null) {
            sDefaultLoaderStrategy.clearMemoryCache(sAppContext);
        }
    }

    public static void clearDiskCache() {
        if (sDefaultLoaderStrategy != null) {
            sDefaultLoaderStrategy.clearDiskCache(sAppContext);
        }
    }

    private static boolean isDestroyed(Object context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return true;
            }

            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                if (fragmentActivity.getSupportFragmentManager() == null || fragmentActivity.getSupportFragmentManager().isDestroyed()) {
                    return true;
                }
            }
        } else if (context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            if (fragment.getActivity() == null) {
                return true;
            }
        }
        return false;
    }
}
