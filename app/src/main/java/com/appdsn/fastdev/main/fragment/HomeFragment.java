package com.appdsn.fastdev.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppFragment;
import com.appdsn.commonbase.utils.statistics.PageStatisticsEvent;
import com.appdsn.commoncore.widget.pullrefreshlayout.PullRefreshLayout;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.fastdev.R;
import com.appdsn.fastdev.main.presenter.HomePresenter;

public class HomeFragment extends BaseAppFragment<HomeFragment, HomePresenter> {

    private XRecyclerView mRecyclerView;
    private PullRefreshLayout mPullRefreshLayout;

    @Override
    public PageStatisticsEvent getPageEvent() {
        return PageStatisticsEvent.get_page;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initVariable(Bundle arguments) {

    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        mPullRefreshLayout = (PullRefreshLayout) bodyView.findViewById(R.id.pullRefreshLayout);
        mRecyclerView = bodyView.findViewById(R.id.content);
        setStatusBarTranslucent();
        setCenterTitle("主页");
    }

    @Override
    protected void setListener() {
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullRefreshLayout pullRefreshLayout) {
                pullRefreshLayout.finishRefresh();
                showEmptyView();
            }

            @Override
            public void onLoadMore(PullRefreshLayout pullRefreshLayout) {
                pullRefreshLayout.finishLoadMore(true);
            }
        });
    }


    @Override
    protected void loadData() {
        mPullRefreshLayout.autoRefresh(200);
    }

    @Override
    protected void onVisibleToUser(boolean visible) {
        super.onVisibleToUser(visible);
        Log.i("123", "onVisibleToUser_main:" + visible);
    }
}
