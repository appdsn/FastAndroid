package com.appdsn.fastdemo.imageselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.widget.imageSelector.CameraCallback;
import com.appdsn.commonbase.widget.imageSelector.ImageSelectorActivity;
import com.appdsn.commonbase.widget.imageSelector.SelectConfig;
import com.appdsn.commonbase.widget.imageSelector.TakePictureFragment;
import com.appdsn.commonbase.widget.imageSelector.previewImageActivity.PreviewImageActivity;
import com.appdsn.commonbase.widget.imageSelector.previewImageActivity.UCropImageActivity;
import com.appdsn.commoncore.imageloader.ImageLoader;
import com.appdsn.commoncore.utils.log.LogUtils;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;

/**
 * Desc:
 * <p>
 *
 * @author: ZhouTao
 * Date: 2019/10/15
 * Copyright: Copyright (c) 2016-2020
 * Company: @appdsn
 * Update
 */
public class ImageSelectActivityGuide extends BaseAppActivity {

    private ImageView image;
    private TakePictureFragment takePictureFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_select_guide;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("图片选择器使用");
        image = findViewById(R.id.image);

        takePictureFragment = new TakePictureFragment();
        takePictureFragment.setCameraCallback(new CameraCallback() {
            @Override
            public void onCameraTakeComplete(String path) {
                ImageLoader.displayImage(path, image);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            /*
             *选择模式下返回多张
             */
            if (resultCode == 777) {
                ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorActivity.INTENT_IMAGE_LIST);
                LogUtils.d("", " result crop : " + images);
                if (images.size() > 0) {
                    ImageLoader.displayImage(images.get(0), image);
                }
                /*
                 *裁剪模式下返回单张
                 */
            } else if (resultCode == 778) {

                String path = data.getStringExtra(UCropImageActivity.SAVE_URL);
                ImageLoader.displayImage(path, image);
                LogUtils.d("", " result select : " + image);
            }
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }

    public void doClick(View view) {
        switch (view.getId()) {
            //拍照获取图片
            case R.id.take_picture_btn:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (takePictureFragment.isAdded()) {
                    fragmentTransaction.remove(takePictureFragment);
                }
                fragmentTransaction.add(R.id.idContainers, takePictureFragment);
                fragmentTransaction.show(takePictureFragment).commit();
                //选择单张图片
            case R.id.select_single_image_btn:
                ImageSelectorActivity.start(ImageSelectActivityGuide.this,
                        new SelectConfig());
                break;
            //选择9张图片
            case R.id.select_nine_image_btn:
                ImageSelectorActivity.start(ImageSelectActivityGuide.this,
                        new SelectConfig()
                                .setMaxNum(9));
                break;
            //选择图片并裁剪
            case R.id.select_image_and_crop_btn:
                ImageSelectorActivity.start(ImageSelectActivityGuide.this,
                        new SelectConfig()
                                .setCropMode(true)
                                .setForceSelect(true));
                break;
            //预览图片
            case R.id.preview_single_img_btn:
                ArrayList<String> data = new ArrayList<>();
                data.add("http://seopic.699pic.com/photo/40163/5826.jpg_wh1200.jpg");
                PreviewImageActivity.start(ImageSelectActivityGuide.this, data, false, 0);
                break;
            //预览多张图片
            case R.id.preview_muti_img_btn:
                data = new ArrayList<>();
                data.add("http://seopic.699pic.com/photo/40163/5826.jpg_wh1200.jpg");
                data.add("http://photocdn.sohu.com/20160227/mp60830207_1456544934128_8.jpeg");
                data.add("http://b-ssl.duitang.com/uploads/item/201210/26/20121026224237_SCTrJ.jpeg");
                PreviewImageActivity.start(ImageSelectActivityGuide.this, data, false, 0);
                break;
            //预览并下载图片
            case R.id.preview_muti_img_and_down_btn:
                data = new ArrayList<>();
                data.add("http://seopic.699pic.com/photo/40163/5826.jpg_wh1200.jpg");
                data.add("http://photocdn.sohu.com/20160227/mp60830207_1456544934128_8.jpeg");
                data.add("http://b-ssl.duitang.com/uploads/item/201210/26/20121026224237_SCTrJ.jpeg");
                PreviewImageActivity.start(ImageSelectActivityGuide.this, data, true, 0);
                break;
            default:
                break;
        }
    }

}
