package com.appdsn.fastdemo.xrecyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commoncore.utils.ThreadUtils;
import com.appdsn.commoncore.widget.xrecyclerview.BaseRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.SingleRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.commoncore.widget.xrecyclerview.decoration.LinearItemDecoration;
import com.appdsn.commoncore.widget.xrecyclerview.loadmore.OnLoadListener;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;

public class XRecyclerSimpleActivity extends BaseAppActivity {

    private ArrayList<String> mDataList;
    private XRecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;
    private TextView mHeaderView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_xrecycler_simple;
    }

    @Override
    protected void initVariable(Intent intent) {
        mDataList = new ArrayList<>();
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("单一布局");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(2, Color.LTGRAY));
    }

    @Override
    protected void setListener() {
        mAdapter = new SingleRecyclerAdapter<String>(mContext, R.layout.item_recycler_child_one) {
            @Override
            public void convert(CommonViewHolder holder, String itemData, int position) {
                holder.setText(R.id.tvDesc, itemData);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPreLoadNumber(2);
        mRecyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(XRecyclerView recyclerView) {
                ThreadUtils.runOnUiThreadDelay(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(mDataList);
                        if (mAdapter.getItemCount() >= 30) {
                            mRecyclerView.onLoadSuccess(false);
                        } else {
                            mRecyclerView.onLoadSuccess(true);
                        }
                    }
                }, 2000);

            }
        });
    }

    @Override
    protected void loadData() {
        for (int i = 0; i < 10; i++) {
            mDataList.add("单一布局 " + i);
        }
        mAdapter.setData(mDataList);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btnHeader:
                if (mHeaderView == null) {
                    mHeaderView = new TextView(mContext);
                    mHeaderView.setGravity(Gravity.CENTER);
                    mHeaderView.setPadding(0, 100, 0, 100);
                    mHeaderView.setText("我是添加的头部");
                    mRecyclerView.setHeaderView(mHeaderView);
                } else {
                    mHeaderView = null;
                    mRecyclerView.removeHeaderView();
                }
                break;
            case R.id.btnLoadMore:
                mRecyclerView.setAutoLoadEnabled(!mRecyclerView.mLoadEnabled);
                break;
        }
    }
}
