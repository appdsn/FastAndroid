package com.appdsn.commonbase.widget.imageSelector.previewImageActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.appdsn.commonbase.R;
import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.widget.imageSelector.SelectConfig;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.commoncore.utils.DrawableUtils;
import com.appdsn.commoncore.utils.log.LogUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;
import com.yalantis.ucrop.callback.BitmapCropCallback;

import java.io.File;

/**
 * Desc:图片裁剪界面
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
public class UCropImageActivity extends BaseAppActivity implements UCropFragmentCallback {
    public static String CROP_RATIO_X = "crop_ratio_x";
    public static String CROP_RATIO_Y = "crop_ratio_y";
    public static String CROP_URL = "crop_url";
    public static String SAVE_URL = "save_url";

    private String cropUrl;
    private String saveUrl;
    private int cropRatioX;
    private int cropRatioY;
    UCropFragment uCropFragment;

    public static void start(Activity context, String url, SelectConfig config) {
        context.startActivityForResult(new Intent(context, UCropImageActivity.class)
                .putExtra(UCropImageActivity.CROP_RATIO_X, config.getRatioX())
                .putExtra(UCropImageActivity.CROP_RATIO_Y, config.getRatioY())
                .putExtra(UCropImageActivity.CROP_URL, url), 770);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_crop_image;
    }

    @Override
    protected void initVariable(Intent intent) {
        cropUrl = intent.getStringExtra(CROP_URL);
        saveUrl = intent.getStringExtra(SAVE_URL);
        cropRatioX = intent.getIntExtra(CROP_RATIO_X, 1);
        cropRatioY = intent.getIntExtra(CROP_RATIO_Y, 1);
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        Button rightBtn = new Button(mContext);
        rightBtn.setText("确定");
        rightBtn.setTextColor(getResources().getColor(R.color.white));
        rightBtn.setGravity(Gravity.CENTER);
        Drawable drawable = DrawableUtils.generateSelector(
                DrawableUtils.generateDrawable("#E9EBEE", DisplayUtils.dp2px(36))
                , DrawableUtils.generateDrawable("#262626", DisplayUtils.dp2px(36)));
        rightBtn.setBackground(drawable);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtils.dp2px(66), DisplayUtils.dp2px(36));
        layoutParams.rightMargin = DisplayUtils.dp2px(14);
        addRightButton(rightBtn, layoutParams);


        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropAndSaveImage();
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
        Uri uri = Uri.fromFile(new File(cropUrl));
        if (uri == null) {
            return;
        }

        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getPath(), imageFileName)));
        uCrop.withAspectRatio(cropRatioX, cropRatioY);
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.color_242121));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.color_242121));
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(true);
        options.setCompressionQuality(100);
        options.setFreeStyleCropEnabled(false);
        options.setShowCropGrid(false);
        options.setRootViewBackgroundColor(Color.parseColor("#FFFFFF"));
        uCrop.withOptions(options);
//        uCrop.withMaxResultSize(720, 1080);

        showCropView(uCrop.getFragment(uCrop.getIntent(mContext).getExtras()));
    }


    public void showCropView(UCropFragment uCropFragment) {
        this.uCropFragment = uCropFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_view, uCropFragment);
        fragmentTransaction.show(uCropFragment);
        fragmentTransaction.commit();


    }

    private void cropAndSaveImage() {
        uCropFragment.cropAndSaveImage(new BitmapCropCallback() {
            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                saveUrl = resultUri.getPath();
                LogUtils.d("", "onBitmapCropped: " + saveUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(778, new Intent().putExtra(SAVE_URL, saveUrl));
                        finish();
                    }
                });
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                LogUtils.i("", "onCropFailure: " + t.getMessage());
            }
        });
    }

    private String getPath() {
        //Environment.getExternalStorageDirectory().getAbsolutePath()
        //getCacheDir().getAbsolutePath()
        final String path = getCacheDir().getAbsolutePath() + File.separator + getPackageName() + "/crop/Image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    @Override
    public void loadingProgress(boolean showLoader) {

    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {

    }
}
