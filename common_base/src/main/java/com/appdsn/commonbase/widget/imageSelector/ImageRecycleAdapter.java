package com.appdsn.commonbase.widget.imageSelector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdsn.commonbase.R;
import com.appdsn.commoncore.imageloader.ImageLoader;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Desc:图片适配器
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
public class ImageRecycleAdapter extends RecyclerView.Adapter<ImageRecycleAdapter.ImageViewHolder> {

    //当前选中的文件夹
    private ImageFloder mCurrentImageFolder;
    /**
     * 已选择的图片
     */
    private ArrayList<String> mSelectedPicture;
    private Context mContext;
    private int mLayoutId;
    private TextView btConfirm;
    private int maxPicture = 1;
    private XRecyclerView.OnItemClickListener onItemClickListener;

    public ImageRecycleAdapter(Context mContext, int layoutId, int maxPicture, TextView btConfirm) {
        super();
        this.mContext = mContext;
        this.btConfirm = btConfirm;
        mSelectedPicture = new ArrayList<>();
        this.mLayoutId = layoutId;
        this.maxPicture = maxPicture;
    }

    @NonNull
    @Override
    public ImageRecycleAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageRecycleAdapter.ImageViewHolder holder, final int position) {
        final ImageItem imageItem = mCurrentImageFolder.images.get(position);
        ImageView iv = holder.getView(R.id.iv);
        ImageLoader.displayImage(imageItem.getPath(), iv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });

        ImageView check = holder.getView(R.id.check);
        check.setSelected(mSelectedPicture.contains(imageItem.getPath()));

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxPicture == 1) {
                    if (!mSelectedPicture.contains(imageItem.getPath())) {
                        if (mSelectedPicture.size() > 0) {
                            notifyPosition(mSelectedPicture.get(0));
                        }
                        mSelectedPicture.clear();
                        mSelectedPicture.add(imageItem.getPath());
                        notifyPosition(imageItem.getPath());
                    } else {
                        mSelectedPicture.remove(imageItem.getPath());
                        notifyPosition(imageItem.getPath());
                    }
                } else {
                    if (!mSelectedPicture.contains(imageItem.getPath())) {
                        if (mSelectedPicture.size() >= maxPicture) {
                            ToastUtils.showLong("最多选中" + maxPicture + "张图片");
                        } else {
                            mSelectedPicture.add(imageItem.getPath());
                        }
                    } else {
                        mSelectedPicture.remove(imageItem.getPath());
                    }
                    notifyPosition(imageItem.getPath());

                }
                String text = mSelectedPicture.size() == 0 ? "确定" : "确定(" + mSelectedPicture.size() + ")";
                btConfirm.setText(text);
            }
        });
    }

    private void notifyPosition(String path) {
        int index = -1;
        for (int i = 0; i < mCurrentImageFolder.images.size(); i++) {
            if (TextUtils.equals(path, mCurrentImageFolder.images.get(i).getPath())) {
                index = i;
            }
        }
        if (index > 0) {
            Log.d("TAG", "notifyPosition: " + index);
            notifyItemChanged(index, null);
        }
    }

    @Override
    public int getItemCount() {
        if (mCurrentImageFolder == null) {
            return 0;
        } else {
            return mCurrentImageFolder.images.size();
        }
    }

    public void addNewData(ImageFloder mImageAll) {
        mCurrentImageFolder = mImageAll;
        notifyDataSetChanged();
    }

    public ArrayList<String> getmSelectedPicture() {
        return mSelectedPicture;
    }

    public void setOnItemClickListener(XRecyclerView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImageFloder getData() {
        return mCurrentImageFolder;
    }

    class ImageViewHolder extends CommonViewHolder {

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
