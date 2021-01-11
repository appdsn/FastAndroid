package com.appdsn.fastdemo.imageloader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.imageloader.ImageLoader;
import com.appdsn.commoncore.imageloader.core.ImageLoaderCallBack;
import com.appdsn.commoncore.imageloader.core.ImageLoaderOptions;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.fastdemo.R;

public class ImageLoaderDemoActivity extends BaseAppActivity {

    private ImageView ivOrigin;
    private ImageView ivRounded;
    private ImageView ivCircle;
    private ImageView ivOval;
    private ImageView ivBorder;
    private ImageView ivCrossFade;
    private String staticUrl = "https://weather-real.oss-cn-shanghai.aliyuncs.com/tianqi/img/cctv.png";
    private String gifUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1674760321,2881373110&fm=26&gp=0.jpg";
    private ImageView ivBlur;
    private ImageView ivGif;
    private View btnClearMemoryCache;
    private View btnClearDiskCache;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_loader_demo;
    }

    @Override
    protected void initVariable(Intent intent) {
        /*初始化imageLoader*/
//        ImageLoader.init(ImageLoaderConfig.newBuilder(this).build());
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("ImageLoader图片加载");
        ivOrigin = findViewById(R.id.ivOrigin);
        ivRounded = findViewById(R.id.ivRounded);

        ivCircle = findViewById(R.id.ivCircle);
        ivOval = findViewById(R.id.ivOval);

        ivBorder = findViewById(R.id.ivBorder);
        ivBlur = findViewById(R.id.ivBlur);

        ivCrossFade = findViewById(R.id.ivCrossFade);
        ivGif = findViewById(R.id.ivGif);

        btnClearMemoryCache = findViewById(R.id.clearMemoryCache);
        btnClearDiskCache = findViewById(R.id.clearDiskCache);
    }

    @Override
    protected void setListener() {
        btnClearMemoryCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.clearMemoryCache();
            }
        });

        btnClearDiskCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.clearDiskCache();
            }
        });
    }

    @Override
    protected void loadData() {
        Log.i("123", "view-Width:" + ivOrigin.getLayoutParams().width + "_" + DisplayUtils.getScreenWidth());
        Log.i("123", "view-height:" + ivOrigin.getLayoutParams().height + "_" + DisplayUtils.getScreenHeight());
        /*原始图片*/
//        ImageLoader.displayImage(staticUrl, ivCircle);
//        ivOrigin.getLayoutParams().width = 300;
//        ivOrigin.getLayoutParams().height = 150;
        /*带边框图片 : border*/
        ImageLoaderOptions options = ImageLoaderOptions.create(this)
                .imageUrl(staticUrl)
                .border(4, Color.GREEN)
                .error(R.drawable.icon_camera)
                .roundImage(10)
                .circleImage(true)
                .override(100, 100)
//                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .loaderCallback(new ImageLoaderCallBack() {
                    @Override
                    public void onLoadFailed(Exception e) {
                        Log.i("123", e.getMessage());
                    }

                    @Override
                    public void onLoadSuccess(Drawable resource) {
                        Log.i("123", "Width:" + resource.getIntrinsicWidth());
                        Log.i("123", "height:" + resource.getIntrinsicHeight());
//                        ivRounded.setImageDrawable(resource);
                    }
                });

        ImageLoader.displayImage(ivOrigin, options);

//        ImageLoader.loadImage(options);

//        /*圆角图片*/
//        ImageLoader.displayRoundImage(staticUrl, ivRounded, 20);
//
//        /*圆形图片：设置ImageView布局高度=布局宽度*/
//        ImageLoader.displayCircleImage(staticUrl, ivCircle);
//
//        /*椭圆图片:设置ImageView布局高度!=布局宽度*/
//        ImageLoader.displayCircleImage(staticUrl, ivOval);
//
//        /*高斯模糊*/
//        ImageLoader.displayBlurImage(staticUrl, ivBlur, 20);
//
//        /*带边框图片 : border*/
//        ImageLoaderOptions options = ImageLoaderOptions.create(this)
//                .displayView(ivBorder)
//                .imageUrl(staticUrl)
//                .border(4, Color.GREEN)
//                .circleImage(true);
//
//        ImageLoader.displayImage(options);
//
//        /*淡入淡出显示:crossFade*/
//        options = ImageLoaderOptions.create(this)
//                .displayView(ivCrossFade)
//                .imageUrl(staticUrl)
//                .border(4, Color.GREEN)
//                .circleImage(true)
//                .crossFade(true);
//        ImageLoader.displayImage(options);
//
//        /*Gif自动显示*/
//        options = ImageLoaderOptions.create(this)
//                .displayView(ivGif)
//                .imageUrl(gifUrl)
//                .border(4, Color.GREEN)
//                .circleImage(true)
//                .crossFade(true);
//        ImageLoader.displayImage(options);
    }
}
