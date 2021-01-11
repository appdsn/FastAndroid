package com.appdsn.commoncore.imageloader.core;

import android.view.View;


public class ImageLoaderRequest {
    private View displayView;  // 图片容器
    private ImageLoaderOptions loaderOptions;


    public ImageLoaderRequest imageLoaderOptions(ImageLoaderOptions loaderOptions) {
        this.loaderOptions = loaderOptions;
        return this;
    }

    public ImageLoaderOptions getImageLoaderOptions() {
        return loaderOptions;
    }


    public ImageLoaderRequest displayView(View displayView) {
        this.displayView = displayView;
        return this;
    }

    public View getDisplayView() {
        return displayView;
    }

    public ImageLoaderCallBack getLoaderResultCallBack() {
        return loaderOptions.getLoaderResultCallBack();
    }
}
