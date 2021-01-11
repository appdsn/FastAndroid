package com.appdsn.commonbase.widget.imageSelector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appdsn.commonbase.R;
import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.widget.imageSelector.previewImageActivity.PreviewImageActivity;
import com.appdsn.commonbase.widget.imageSelector.previewImageActivity.UCropImageActivity;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.commoncore.utils.DrawableUtils;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Desc:图片选择界面
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
public class ImageSelectorActivity extends BaseAppActivity<ImageSelectorActivity, ImageSelectorPresenter> implements View.OnClickListener {

    public static final String INTENT_CONFIG = "intent_config";
    public static final String INTENT_IMAGE_LIST = "intent_image_list";
    private RecyclerView mImageList;
    private XRecyclerView imageDirList;
    private TextView btnSelect, btConfirm;
    /**
     * 最多选择图片的个数
     */
    public int maxNum = 1;
    public boolean mNeedCrop = false;
    public boolean forceSelect = false;
    private SelectConfig mConfig;


    /**
     * 打开一个图片选择器.
     *
     * @param context 上下文
     * @param config  配置信息
     * @return 关于返回值
     * <p/>
     * 777 选择返回,最少返回一张最多返回9张 类型为ArrayList<String> ,key = intent_image_list
     * {@link #selectResult}
     * 778 裁减返回,最多返回一张  类型为String,  key = save_url
     * {@link #cropResult}
     * @throws Exception description
     */
    public static void start(Activity context, SelectConfig config) {
        context.startActivityForResult(new Intent(context, ImageSelectorActivity.class)
                        .putExtra(INTENT_CONFIG, config)
                , config.getRequestCode());
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_selector;
    }

    @Override
    protected void initVariable(Intent intent) {
        if (intent.getSerializableExtra(INTENT_CONFIG) != null) {
            mConfig = (SelectConfig) intent.getSerializableExtra(INTENT_CONFIG);
            mNeedCrop = mConfig.isCropMode();
            if (mNeedCrop) {
                maxNum = 1;
            } else {
                maxNum = mConfig.getMaxNum();
            }
            forceSelect = mConfig.isForceSelect();
        }
    }


    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        hideTitleBar();
        mImageList = findViewById(R.id.imageList);
        imageDirList = findViewById(R.id.imageDirList);
        btnSelect = findViewById(R.id.btnSelect);
        btConfirm = findViewById(R.id.btConfirm);
        mImageList.setLayoutManager(new GridLayoutManager(this, mConfig.getSpanCount()));
        mImageList.setItemAnimator(null);
        imageDirList.setLayoutManager(new LinearLayoutManager(this));

        Drawable drawable = DrawableUtils.generateSelector(
                DrawableUtils.generateDrawable("#E9EBEE", DisplayUtils.dp2px(46))
                , DrawableUtils.generateDrawable("#262626", DisplayUtils.dp2px(46)));
        btConfirm.setBackground(drawable);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        btConfirm.setOnClickListener(this);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.switchDir(imageDirList);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (778 == resultCode && data != null) {
            cropResult(data);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            finish();
        } else if (view.getId() == R.id.btConfirm) {
            if (mNeedCrop) {
                ArrayList<String> data = mPresenter.getAdapter().getmSelectedPicture();
                if (data.size() > 0) {
                    startActivityForResult(new Intent(mContext, UCropImageActivity.class)
                            .putExtra(UCropImageActivity.CROP_RATIO_X, mConfig.getRatioX())
                            .putExtra(UCropImageActivity.CROP_RATIO_Y, mConfig.getRatioY())
                            .putExtra(UCropImageActivity.CROP_URL, data.get(0)), 770);
                } else {
                    ToastUtils.showLong("最少选择一张图片哦");
                }
            } else {
                selectResult();
            }

        }
    }

    /**
     * 裁减返回
     *
     * @param data
     */
    private void cropResult(Intent data) {
        String saveUrl = data.getStringExtra(UCropImageActivity.SAVE_URL);
        if (saveUrl != null) {
            setResult(778, data);
            finish();
        }
    }

    /**
     * 选择图片返回
     */
    private void selectResult() {
        ArrayList<String> images = mPresenter.getAdapter().getmSelectedPicture();
        if (images.size() == 0) {
            if (forceSelect) {
                ToastUtils.showLong("最少选择一张图片哦");
            } else {
                finish();
            }
        } else {
            setResult(777, new Intent().putStringArrayListExtra(INTENT_IMAGE_LIST, images));
            finish();
        }
    }

    @Override
    protected void loadData() {
        mPresenter.initData(this, maxNum, btConfirm);
        mImageList.setAdapter(mPresenter.getAdapter());
        imageDirList.setAdapter(mPresenter.getDirAdapter());
        mPresenter.getAdapter().setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }

            @Override
            public void onItemClick(View view, int position) {
                ImageFloder data = mPresenter.getAdapter().getData();

                if (mNeedCrop) {
                    startActivityForResult(new Intent(mContext, UCropImageActivity.class)
                            .putExtra(UCropImageActivity.CROP_URL, data.images.get(position).getPath()), 770);
                } else {
                    ArrayList<String> images = new ArrayList<>();
                    for (ImageItem image : data.images) {
                        images.add(image.getPath());
                    }
                    startActivity(new Intent(mContext, PreviewImageActivity.class)
                            .putStringArrayListExtra(PreviewImageActivity.PREVIEW_IMAGE_DATA, images)
                            .putExtra(PreviewImageActivity.PREVIEW_IMAGE_POSITION, position)
                            .putExtra(PreviewImageActivity.PREVIEW_IMAGE_SHOW_DOWNLOAD, false));
                }

            }
        });

        imageDirList.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }

            @Override
            public void onItemClick(View view, int position) {
                mPresenter.switchDir(imageDirList);
                mPresenter.joinDirForPosition(position, btnSelect);
            }
        });
    }
}
