package com.appdsn.commoncore.imageloader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.appdsn.commoncore.imageloader.core.IImageLoaderStrategy;
import com.appdsn.commoncore.imageloader.core.ImageLoaderCallBack;
import com.appdsn.commoncore.imageloader.core.ImageLoaderConfig;
import com.appdsn.commoncore.imageloader.core.ImageLoaderOptions;
import com.appdsn.commoncore.imageloader.core.ImageLoaderRequest;
import com.appdsn.commoncore.utils.ThreadUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class GlideImageLoader implements IImageLoaderStrategy {
    private Context context;
    private ImageLoaderConfig config;

    @Override
    public void init(ImageLoaderConfig config) {
        this.config = config;
        this.context = config.context;
    }

    @Override
    public void displayImage(@NonNull final ImageLoaderRequest request) {
        RequestBuilder<Drawable> builder = getRequestBuilder(request);
        setListener(builder, request.getLoaderResultCallBack());
        builder.into((ImageView) request.getDisplayView());
    }

    @Override
    public void loadImage(@NonNull final ImageLoaderRequest request) {
        RequestBuilder<Drawable> builder = getRequestBuilder(request);
        setListener(builder, request.getLoaderResultCallBack());
        builder.into(new SimpleTarget<Drawable>() {//滚动会卡顿
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

            }
        });
    }

    private void setListener(RequestBuilder<Drawable> builder, final ImageLoaderCallBack callBack) {
        builder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (callBack != null) {
                    callBack.onLoadFailed(e);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (callBack != null) {
//                    Bitmap bitmap = BitmapUtils.drawableToBitmap(resource);
//                    callBack.onLoadSuccess(new BitmapDrawable(bitmap));
                    callBack.onLoadSuccess(resource);
                }
                return false;
            }
        });
    }

    private RequestBuilder<Drawable> getRequestBuilder(ImageLoaderRequest request) {
        ImageLoaderOptions options = request.getImageLoaderOptions();
        RequestManager requestManager = getRequestManager(options.getContext());
        RequestOptions requestOptions = getRequestOptions(request);

        RequestBuilder builder = null;
        if (options.isAsGif()) {
            builder = requestManager.asGif();
        } else {
            builder = requestManager.asDrawable();
        }

        if (options.getImageUrl() instanceof Integer) {
            builder.load((Integer) options.getImageUrl());
        } else {
            builder.load(options.getImageUrl());
        }

        builder.apply(requestOptions);

        if (options.isCrossFade()) {
            builder.transition(DrawableTransitionOptions.withCrossFade());
        }
        return builder;
    }

    /**
     * 说明：这里如果传的是activity，如果已经销毁了，会抛出异常
     */
    private RequestManager getRequestManager(Object contextObj) {
        if (contextObj instanceof FragmentActivity) {
            return Glide.with((FragmentActivity) contextObj);
        } else if (contextObj instanceof Activity) {
            return Glide.with((Activity) contextObj);
        } else if (contextObj instanceof Fragment) {
            return Glide.with((Fragment) contextObj);
        } else if (contextObj instanceof Context) {
            return Glide.with((Context) contextObj);
        }
        return Glide.with(context);
    }

    private RequestOptions getRequestOptions(ImageLoaderRequest request) {
        ImageLoaderOptions options = request.getImageLoaderOptions();
        RequestOptions requestOptions = new RequestOptions();

        if (options.getHolderDrawable() > 0) {
            requestOptions.placeholder(options.getHolderDrawable());
        }
        if (options.getErrorDrawable() > 0) {
            requestOptions.error(options.getErrorDrawable());
        }

        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            if (ImageLoaderOptions.DiskCacheStrategy.NONE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.All == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            } else if (ImageLoaderOptions.DiskCacheStrategy.SOURCE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.RESULT == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            }
        }

        if (options.isSkipMemoryCache()) {
            requestOptions.skipMemoryCache(true);
        }
        if (options.getImageSize() != null) {
            /*控制输出图片大小：如果控件为wrap_content, 则输出大小取屏幕高度*/
            requestOptions.override(options.getImageSize().getWidth(), options.getImageSize().getHeight());
        } else {
            /*该策略会使输入图片不会超过原始图片大小大小（Bitmap toTransform为原始大小）*/
            requestOptions.dontTransform();
            requestOptions.downsample(DownsampleStrategy.CENTER_INSIDE);
        }

        List<Transformation> list = new ArrayList<Transformation>();
        if (options.getBlurValue() > 0) {
            list.add(new BlurTransformation(context, options.getBlurValue()));
        }
        if (options.getCornerRadius() > 0 || options.isCircle() || options.getBorderWidth() > 0) {
            ImageView.ScaleType scaleType = options.getScaleType();
            if (request.getDisplayView() instanceof ImageView) {
                ImageView imageView = (ImageView) request.getDisplayView();
                scaleType = imageView.getScaleType();
            }
            RoundedTransformation transformation = RoundedTransformation.create(options.getCornerRadius(), scaleType);
            transformation.setBorderColor(options.getBorderColor());
            transformation.setBorderWidth(options.getBorderWidth());
            transformation.setOval(options.isCircle());
            transformation.setCornerPosition(options.getCornerPosition());
            transformation.setDisplayView(request.getDisplayView());
            list.add(transformation);
        }

        if (list.size() > 0) {
            Transformation[] transformations = list.toArray(new Transformation[list.size()]);
            requestOptions.transforms(transformations);
        }

        return requestOptions;
    }


    @Override
    public void clearMemoryCache(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context).clearMemory();
        }
    }

    @Override
    public void clearDiskCache(final Context context) {
        ThreadUtils.execute(new ThreadUtils.ThreadTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                //必须在子线程中  This method must be called on a background thread.
                Glide.get(context).clearDiskCache();
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }
}
