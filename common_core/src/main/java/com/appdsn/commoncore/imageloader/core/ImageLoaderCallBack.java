package com.appdsn.commoncore.imageloader.core;

import android.graphics.drawable.Drawable;

public interface ImageLoaderCallBack {

    void onLoadFailed(Exception e);

    void onLoadSuccess(Drawable resource);
}
