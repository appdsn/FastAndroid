package com.appdsn.commonbase.widget.imageSelector.previewImageActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdsn.commonbase.R;
import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.utils.DownloadUtil;
import com.appdsn.commoncore.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Desc:图片预览界面
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

public class PreviewImageActivity extends BaseAppActivity implements ViewPager.OnPageChangeListener {
    public static String PREVIEW_IMAGE_DATA = "preview_image_data";//图片预览
    public static String PREVIEW_IMAGE_POSITION = "preview_image_position";//图片预览默认的显示位置
    public static String PREVIEW_IMAGE_SHOW_DOWNLOAD = "preview_image_show_download";

    ViewPager mViewPager;
    TextView mTextView;
    private ArrayList<String> mImageArrayList;
    private int position;
    private boolean showDown;
    private ImageView ivDownload;

    /**
     * Description:打开图片预览界面
     *
     * @param context         上下文
     * @param mImageArrayList 图片集合
     * @param showDownLoad    是否显示下载按钮
     * @param position        是否显示指定位置
     */
    public static void start(Activity context, ArrayList<String> mImageArrayList, boolean showDownLoad, int position) {
        context.startActivity(new Intent(context, PreviewImageActivity.class)
                .putExtra(PREVIEW_IMAGE_DATA, mImageArrayList)
                .putExtra(PREVIEW_IMAGE_SHOW_DOWNLOAD, showDownLoad)
                .putExtra(PREVIEW_IMAGE_POSITION, position));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.common_activity_preview_image;
    }

    @Override
    protected void initVariable(Intent intent) {
        mImageArrayList = intent.getStringArrayListExtra(PREVIEW_IMAGE_DATA);
        position = intent.getIntExtra(PREVIEW_IMAGE_POSITION, 0);
        showDown = intent.getBooleanExtra(PREVIEW_IMAGE_SHOW_DOWNLOAD, true);
        if (null == mImageArrayList) {
            mImageArrayList = new ArrayList<>();
        }
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        hideTitleBar();
        setStatusBarTranslucent();
        mViewPager = findViewById(R.id.preview_image_vp_content);
        mTextView = findViewById(R.id.preview_image_tv_indicator);
        ivDownload = findViewById(R.id.preview_image_iv_download);
        if (showDown) {
            ivDownload.setVisibility(View.VISIBLE);
        } else {
            ivDownload.setVisibility(View.GONE);
        }
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mImageArrayList.get(position);
                downloadImage(path);
            }
        });
    }

    public void downloadImage(final String image) {
        Activity activity = this;
        if (activity == null || null == image || TextUtils.isEmpty(image)) {
            ToastUtils.showLong("下载失败");
            return;
        }
        DownloadUtil.get().download(image, getPath(), System.currentTimeMillis() + ".png", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final File file) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showLong("保存成功");
                        // 最后通知图库更新
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
                        mContext.sendBroadcast(intent);
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed(Exception e) {

            }
        });
    }

    private String getPath() {
        final String path = getCacheDir().getAbsolutePath() + File.separator + getPackageName() + "/Image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
        PreviewImagePagerAdapter previewImagePagerAdapter = new PreviewImagePagerAdapter(this, mImageArrayList);
        mViewPager.setAdapter(previewImagePagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);
        //position == 0 时   不会调用onPageSelected方法
        if (position == 0) {
            setPosition(position);
        }
    }

    private void setPosition(int position) {
        this.position = position;
        mTextView.setText(position + 1 + "/" + mImageArrayList.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
