package com.appdsn.fastdemo.xrecyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.widget.xrecyclerview.BaseRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.MultiRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.commoncore.widget.xrecyclerview.decoration.LinearItemDecoration;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.xrecyclerview.bean.MultiItemInfo;

import java.util.ArrayList;

public class XRecyclerMultiActivity extends BaseAppActivity {

    private ArrayList<MultiItemInfo> mDataList;
    private XRecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_xrecycler_multi;
    }

    @Override
    protected void initVariable(Intent intent) {
        mDataList = new ArrayList<>();
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("多种布局");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(2, Color.LTGRAY));

        mAdapter = new MultiRecyclerAdapter<MultiItemInfo>(mContext) {
            @Override
            public int getLayoutResId(int viewType) {
                if (viewType == 0) {
                    return R.layout.item_recycler_group_one;
                } else {
                    return R.layout.item_recycler_child_one;
                }
            }

            @Override
            public int getItemViewType(int position, MultiItemInfo itemData) {
                return itemData.mItemType;
            }

            @Override
            public void convert(CommonViewHolder holder, MultiItemInfo itemData, int position) {
                if (getItemViewType(position) == 0) {
                    holder.setText(R.id.tvDesc, itemData.mTitle);
                } else {
                    holder.setText(R.id.tvDesc, itemData.mTitle);
                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
        MultiItemInfo itemInfo = new MultiItemInfo(0, "布局类型一");
        mDataList.add(itemInfo);
        for (int i = 0; i < 5; i++) {
            itemInfo = new MultiItemInfo(1, "布局类型二" + (i + 1));
            mDataList.add(itemInfo);
        }

        itemInfo = new MultiItemInfo(0, "布局类型一");
        mDataList.add(itemInfo);
        for (int i = 0; i < 5; i++) {
            itemInfo = new MultiItemInfo(1, "布局类型二" + (i + 1));
            mDataList.add(itemInfo);
        }

        itemInfo = new MultiItemInfo(0, "布局类型一");
        mDataList.add(itemInfo);
        for (int i = 0; i < 5; i++) {
            itemInfo = new MultiItemInfo(1, "布局类型二" + (i + 1));
            mDataList.add(itemInfo);
        }

        mAdapter.setData(mDataList);
    }
}
