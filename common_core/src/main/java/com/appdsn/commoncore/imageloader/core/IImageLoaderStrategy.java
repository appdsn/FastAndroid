package com.appdsn.commoncore.imageloader.core;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IImageLoaderStrategy {
    void displayImage(@NonNull ImageLoaderRequest request);

    void loadImage(@NonNull ImageLoaderRequest request);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    //在application的onCreate中初始化
    void init(ImageLoaderConfig config);
}
