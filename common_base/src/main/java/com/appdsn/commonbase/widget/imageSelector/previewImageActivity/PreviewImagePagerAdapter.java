package com.appdsn.commonbase.widget.imageSelector.previewImageActivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdsn.commoncore.imageloader.ImageLoader;
import com.appdsn.commoncore.widget.photoview.OnOutsidePhotoTapListener;
import com.appdsn.commoncore.widget.photoview.OnPhotoTapListener;
import com.appdsn.commoncore.widget.photoview.PhotoView;

import java.util.List;

/**
 * Desc:图片预览适配器
 * <p>
 * Author: zhoutao
 * Date: 2019/10/11
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:zhoutao@appdsn.com
 * Update Comments:
 *
 * @author zhoutao
 */
public class PreviewImagePagerAdapter extends PagerAdapter implements OnOutsidePhotoTapListener, OnPhotoTapListener {

    private Context mContext;
    private List<String> mDataList;

    public PreviewImagePagerAdapter(Context context, List<String> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setMinimumScale(0.5f);
        String image = mDataList.get(position);
        if (null != image) {
            ImageLoader.displayImage(image, photoView);
        }
        photoView.setOnOutsidePhotoTapListener(this);
        photoView.setOnPhotoTapListener(this);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        finishActivity();
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        finishActivity();
    }

    private void finishActivity() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }

}
