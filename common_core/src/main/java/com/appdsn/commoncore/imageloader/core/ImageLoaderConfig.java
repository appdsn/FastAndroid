package com.appdsn.commoncore.imageloader.core;

import android.content.Context;

public class ImageLoaderConfig {
    public Context context;
    public ImageLoaderOptions defaultOptions;
    public LoaderType defaultLoaderType;

    private ImageLoaderConfig(Builder builder) {
        context = builder.context;
        defaultLoaderType = builder.defaultLoaderType;
        defaultOptions = builder.defaultOptions;
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Context context;
        private ImageLoaderOptions defaultOptions;
        private LoaderType defaultLoaderType;

        private Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder defaultImageLoader(LoaderType loaderType) {
            defaultLoaderType = loaderType;
            return this;
        }

        public Builder defaultImageLoaderOptions(ImageLoaderOptions defaultOptions) {
            this.defaultOptions = defaultOptions;
            return this;
        }

        public ImageLoaderConfig build() {
            initEmptyDefaultValues();
            return new ImageLoaderConfig(this);
        }

        private void initEmptyDefaultValues() {
            if (defaultLoaderType == null) {
                defaultLoaderType = LoaderType.GLIDE;
            }
            if (defaultOptions == null) {
                defaultOptions = ImageLoaderOptions.create(context);
            }
        }
    }
}
