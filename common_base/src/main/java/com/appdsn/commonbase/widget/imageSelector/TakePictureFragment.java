package com.appdsn.commonbase.widget.imageSelector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdsn.commonbase.utils.ImageFileUtil;
import com.appdsn.commonbase.utils.PictureUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Desc:
 * <p> 相机调用类
 * Author: ZhouTao
 * Date: 2019/11/26
 * Copyright: Copyright (c) 2016-2020
 * Company:
 * Email:zhoutao@appdsn.com
 * Update Comments:
 *
 * @author zhoutao
 */
public class TakePictureFragment extends Fragment {
    public static final String INTENT_ACTION_START_CAMERA = "android.media.action.IMAGE_CAPTURE";
    public static final int REQUEST_CODE_TAKE = 0x000222;// 图片选择请求码
    private File mTempFile;
    CameraCallback mCameraCallback;
    private FragmentActivity mActivity;
    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PicturePicker";

    public void setCameraCallback(CameraCallback mCameraCallback) {
        this.mCameraCallback = mCameraCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        loadData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected void loadData() {
        dir = ImageFileUtil.createDefaultDirectory(mActivity).getAbsolutePath();
        mTempFile = createTempFileByDestDirectory(dir);
        Uri tempUri = getUriFromFile(mActivity, mActivity.getPackageName() + ".common.provider", mTempFile);
        // 启动相机
        Intent intent = new Intent(INTENT_ACTION_START_CAMERA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || null == mCameraCallback) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_TAKE:
                try {
                    //将拍摄后的图片, 压缩到 cameraDestFile 中
                    File cameraDestFile = ImageFileUtil.createCameraDestFile(dir);
                    PictureUtil.doCompress(mTempFile.getAbsolutePath(), cameraDestFile.getAbsolutePath(),
                            80);

                    ImageFileUtil.freshMediaStore(mActivity, cameraDestFile);
                    mCameraCallback.onCameraTakeComplete(cameraDestFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e("TAG", "Picture compress failed after camera take.", e);
                } finally {
                    mTempFile.delete();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取 URI
     */
    public Uri getUriFromFile(Context context, String authority, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 根据目的文件路径, 创建临时文件
     *
     * @param directoryPath 目标文件路径
     * @return 创建的文件
     */
    public File createTempFileByDestDirectory(String directoryPath) {
        // 获取临时文件目录
        File tempDirectory = new File(directoryPath);
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }
        // 创建临时文件
        String tempFileName = "temp_file_" + DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File tempFile = new File(tempDirectory, tempFileName);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
        }
        return tempFile;
    }
}
