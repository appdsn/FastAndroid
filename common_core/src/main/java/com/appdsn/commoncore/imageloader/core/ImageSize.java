package com.appdsn.commoncore.imageloader.core;

//对应重写图片size
public final class ImageSize {
    private int width = 0;
    private int height = 0;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}