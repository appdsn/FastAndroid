package com.appdsn.commonbase.widget.imageSelector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdsn.commonbase.R;
import com.appdsn.commoncore.base.BasePresenter;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.SingleRecyclerAdapter;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageSelectorPresenter extends BasePresenter<ImageSelectorActivity> {
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> mTmpDir = new HashMap<String, Integer>();
    private ImageFloder mImageAll, mCurrentImageFolder;
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();
    private ContentResolver mContentResolver;
    /**
     * 最多选择图片的个数
     */
    public static int MAX_NUM = 9;
    // Get relevant columns for use later.
    String[] projection = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Video.VideoColumns.DURATION
    };
    Uri queryUri = MediaStore.Files.getContentUri("external");
    /**
     * 是否显示视频
     */
    private boolean isShowVideo;
    /**
     * 只显示视频
     */
    private boolean isOnlyShowVideo;
    private ImageRecycleAdapter mImageRecycleAdapter;
    private SingleRecyclerAdapter mImageDirRecycleAdapter;


    public void initData(Context context, int maxSelect, TextView btConfirm) {
        mImageAll = new ImageFloder();
        mImageAll.setDir("/所有图片");
        mCurrentImageFolder = mImageAll;
        mDirPaths.add(mImageAll);

        mImageRecycleAdapter = new ImageRecycleAdapter(context, R.layout.grid_item_picture, maxSelect, btConfirm);
        mImageRecycleAdapter.addNewData(mCurrentImageFolder);

        mImageDirRecycleAdapter = initDirAdapter(context);

        mContentResolver = context.getContentResolver();
        getThumbnail();
    }

    //显示关闭目录
    public void switchDir(RecyclerView imageDirList) {
        if (imageDirList.getVisibility() == View.VISIBLE) {
            hideListAnimation(imageDirList);
        } else {
            imageDirList.setVisibility(View.VISIBLE);
            showListAnimation(imageDirList);
        }
    }

    private void showListAnimation(RecyclerView imageDirList) {
        AlphaAnimation ta = new AlphaAnimation(0f, 1f);
        ta.setDuration(300);
        imageDirList.startAnimation(ta);
    }

    private void hideListAnimation(final RecyclerView imageDirList) {
        AlphaAnimation ta = new AlphaAnimation(1f, 0f);
        ta.setDuration(200);
        imageDirList.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageDirList.setVisibility(View.GONE);
            }
        });
    }

    private SingleRecyclerAdapter<ImageFloder> initDirAdapter(Context context) {
        return new SingleRecyclerAdapter<ImageFloder>(context, R.layout.grid_item_dir_picture, mDirPaths) {
            @Override
            public void convert(CommonViewHolder helper, ImageFloder item, int position) {
                ImageView image = helper.getView(R.id.id_dir_item_image);
                helper.setText(R.id.tvName, item.getName());
                helper.setText(R.id.tvCount, item.images.size() + "张");
                Glide.with(image).load(item.getFirstImagePath()).into(image);
            }
        };
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {


        try {
            Cursor mCursor = mContentResolver.query(queryUri, projection, getResolverSelection(), null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
            if (mCursor != null) {
                if (mCursor.moveToFirst()) {

                    int _path = mCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int _type = mCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
                    int _duration = mCursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);
                    do {

                        // 获取图片的路径
                        String path = mCursor.getString(_path);

                        int type = mCursor.getInt(_type);
                        if (TextUtils.isEmpty(path)) {
                            continue;
                        }
                        File file = new File(path);
                        if (!file.exists() || isWebp(path)) {
                            continue;
                        }

                        double size = file.length() / 1000;

                        if (size <= 10) {  //小于10kb
                            continue;
                        }

                        long duration = 0;
                        // Log.e("TAG", path);
                        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                            duration = mCursor.getLong(_duration);
                        }
                        mImageAll.images.add(new ImageItem(path, type, duration));
                        if (TextUtils.isEmpty(mImageAll.getFirstImagePath())) {
                            mImageAll.setFirstImagePath(path);
                        }
                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null) {
                            continue;
                        }
                        ImageFloder imageFloder = null;
                        String dirPath = parentFile.getAbsolutePath();
                        if (!mTmpDir.containsKey(dirPath)) {
                            // 初始化imageFloder
                            imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                            mDirPaths.add(imageFloder);
                            mTmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                        } else {
                            imageFloder = mDirPaths.get(mTmpDir.get(dirPath));
                        }
                        imageFloder.images.add(new ImageItem(path, type, duration));

//                        if (mImageAll.images.size() % 5 == 0) {
//                            mImageRecycleAdapter.notifyDataSetChanged();
//                        }

                    } while (mCursor.moveToNext());
                }
                mCursor.close();
                mTmpDir = null;
            }
        } catch (Exception e) {
        }
        mImageRecycleAdapter.notifyDataSetChanged();
        if (mImageDirRecycleAdapter != null) {
            mImageDirRecycleAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取 contentResolver查询条件
     *
     * @return
     */
    private String getResolverSelection() {
        StringBuilder sb = new StringBuilder();
        if (isOnlyShowVideo) {
            sb.append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                    .append("=")
                    .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            sb.append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                    .append("=")
                    .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
            if (isShowVideo) {
                sb.append(" OR ")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                        .append("=")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
            }
        }
        return sb.toString();
    }

    /**
     * 是否webp
     */
    private boolean isWebp(String url) {
        boolean isGif = false;
        if (!TextUtils.isEmpty(url)) {
            int index = url.lastIndexOf(".");
            if (index >= 0 && index < url.length()) {
                String prefix = url.substring(index + 1);
                if (TextUtils.equals("webp", prefix)) {
                    isGif = true;
                }
            }
        }

        return isGif;
    }

    public ImageRecycleAdapter getAdapter() {
        return mImageRecycleAdapter;
    }

    public SingleRecyclerAdapter<ImageFloder> getDirAdapter() {
        return mImageDirRecycleAdapter;
    }


    public void joinDirForPosition(int position, TextView titleDir) {
        mCurrentImageFolder = mDirPaths.get(position);
        mImageRecycleAdapter.addNewData(mCurrentImageFolder);
        titleDir.setText(mCurrentImageFolder.getName());
    }
}
