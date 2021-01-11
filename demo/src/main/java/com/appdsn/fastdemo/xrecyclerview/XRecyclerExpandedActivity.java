package com.appdsn.fastdemo.xrecyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.ExpandedRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.commoncore.widget.xrecyclerview.decoration.LinearItemDecoration;
import com.appdsn.commoncore.widget.xrecyclerview.entity.ExpandedItemEntity;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.xrecyclerview.bean.ChildItemInfo;
import com.appdsn.fastdemo.xrecyclerview.bean.GroupItemInfo;

import java.util.ArrayList;

public class XRecyclerExpandedActivity extends BaseAppActivity {

    private ArrayList<ExpandedItemEntity> mDataList;
    private XRecyclerView mRecyclerView;
    private ExpandedRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_xrecycler_expanded;
    }

    @Override
    protected void initVariable(Intent intent) {
        mDataList = new ArrayList<>();
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("多级分组可收缩");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(2, Color.LTGRAY));

        mAdapter = new ExpandedRecyclerAdapter(mContext) {
            @Override
            public void convert(CommonViewHolder holder, final ExpandedItemEntity itemData, final int position) {
                int itemType = getItemViewType(position);
                if (itemType == 0) {
                    final GroupItemInfo itemInfo = (GroupItemInfo) itemData;
                    holder.setText(R.id.tvDesc, itemInfo.title);
                    holder.setChecked(R.id.cbSelect, itemInfo.selected == 1);
                    holder.setText(R.id.tvSize, "已经选中  " + itemInfo.selectedChildSize);

                    /*展开或收缩子节点*/
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            expandGroup(itemInfo, position);
                        }
                    });
                } else {
                    final ChildItemInfo itemInfo = (ChildItemInfo) itemData;
                    holder.setText(R.id.tvDesc, itemInfo.title);
                    holder.setChecked(R.id.cbSelect, itemInfo.selected == 1);
                    holder.setText(R.id.tvSize, itemInfo.selfSize + "");
                }

                holder.getView(R.id.cbSelect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*取消或者选中全部子项*/
                        selectChild(itemData);
                    }
                });
            }

            @Override
            public int getLayoutResId(int viewType) {
                if (viewType == 0) {
                    return R.layout.item_recycler_group_one;
                } else {
                    return R.layout.item_recycler_child_one;
                }
            }

            @Override
            public int getItemViewType(int position, ExpandedItemEntity itemData) {
                if (itemData instanceof GroupItemInfo) {
                    return 0;
                } else {
                    return 1;
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
        GroupItemInfo groupItemInfo = new GroupItemInfo(0);
        groupItemInfo.title = "v分组一";
        groupItemInfo.expanded = true;
        for (int i = 0; i < 5; i++) {
            ChildItemInfo info = new ChildItemInfo(1);
            info.selfSize = 50;
            info.title = "    我是子节点";
            groupItemInfo.addChildItem(info);
        }
        GroupItemInfo groupItemInfo1 = new GroupItemInfo(0);
        groupItemInfo1.title = "    v 二级分组";
        groupItemInfo1.expanded = false;
        for (int i = 0; i < 5; i++) {
            ChildItemInfo info = new ChildItemInfo(1);
            info.selfSize = 50;
            info.title = "            我是子节点";
            groupItemInfo1.addChildItem(info);
        }
        groupItemInfo.addChildItem(groupItemInfo1);
        mDataList.add(groupItemInfo);

        GroupItemInfo groupItemInfo2 = new GroupItemInfo(0);
        groupItemInfo2.title = "v 分组二";
        for (int i = 0; i < 5; i++) {
            ChildItemInfo info = new ChildItemInfo(1);
            info.selfSize = 50;
            info.title = "    我是子节点";
            groupItemInfo2.addChildItem(info);
        }
        mDataList.add(groupItemInfo2);

        GroupItemInfo groupItemInfo3 = new GroupItemInfo(0);
        groupItemInfo3.title = "v 分组三";
        groupItemInfo3.expanded = true;
        for (int i = 0; i < 5; i++) {
            ChildItemInfo info = new ChildItemInfo(1);
            info.selfSize = 50;
            info.title = "    我是子节点";
            groupItemInfo3.addChildItem(info);
        }

        mDataList.add(groupItemInfo3);
        mAdapter.setData(mDataList);
    }
}
