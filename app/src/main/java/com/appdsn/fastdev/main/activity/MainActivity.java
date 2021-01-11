package com.appdsn.fastdev.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.appdsn.commonbase.base.BaseAppActivity;
import com.appdsn.commonbase.config.AppConfig;
import com.appdsn.commonbase.config.RouterExtra;
import com.appdsn.commonbase.config.RouterPath;
import com.appdsn.commonbase.utils.statistics.StatisticsUtils;
import com.appdsn.commoncore.event.BindEventBus;
import com.appdsn.commoncore.update.UpdateAgent;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.commoncore.widget.DragFloatActionButton;
import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.fastdev.R;
import com.appdsn.fastdev.main.adapter.MainPagerAdapter;
import com.appdsn.fastdev.main.adapter.MainTabAdapter;
import com.appdsn.fastdev.main.presenter.MainPresenter;

import java.util.ArrayList;

@BindEventBus
@Route(path = RouterPath.MAIN_ACTIVITY)
public class MainActivity extends BaseAppActivity<MainActivity, MainPresenter> {
    private ViewPager mViewPager;
    private SmartIndicator mIndicator;
    private MainPagerAdapter mPagerAdapter;
    private MainTabAdapter mTabAdapter;
    @Autowired(name = RouterExtra.TAB_INDEX)
    int mTabIndex = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariable(Intent intent) {
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mTabAdapter = new MainTabAdapter(mInflater);
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        hideTitleBar();
        mViewPager = findViewById(R.id.mainViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mIndicator = findViewById(R.id.mainTabIndicator);
        setStatusBarTranslucent();
        AppConfig.showDebugInWindow(this);

        UpdateAgent.Builder builder = new UpdateAgent.Builder();
        UpdateAgent updateAgent = builder.with(this).setDownloadUrl("http://tbs-real.oss-cn-shanghai.aliyuncs.com/apk/official.apk")
                .setContent("我是更新内容")
                .setForceUpdate(false)
                .setTitle("v 1.2.0")
                .setUpdate(true)
                .setUpdateLayoutId(R.layout.activity_update_dialog)
                .setDownloadLayoutId(R.layout.custom_download_dialog)
                .build();

        updateAgent.check();
        new DragFloatActionButton.Builder()
                .setGravity(Gravity.RIGHT | Gravity.TOP)
//                .setOnClickListener(v -> startActivityForResult(RouteConstants.TEST_SKIP_ACTIVITY, CHANGE_LAN))
                .setMargin(DisplayUtils.dp2px(14), 0, DisplayUtils.dp2px(14), DisplayUtils.dp2px(50))
                .setSize(DisplayUtils.dp2px(64), DisplayUtils.dp2px(64))
                .setImageUrl("https://media.giphy.com/media/3DsNP07nApt1eEyjvM/giphy.gif")
                .isCircle(true)
                .isStickySide(false)
                .bindActivity(this)
                .create();
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mTabIndex > 0 && mTabIndex <= mIndicator.getTabCount()) {
            mIndicator.setCurrentTab(mTabIndex - 1);
        }
    }

    @Override
    protected void loadData() {
        mPresenter.initFragment();
    }

    public void loadFragmentDataSuccess(ArrayList<Fragment> fragments, String[] tabNames, int[] tabDrawables) {
        mPagerAdapter.setData(fragments);
        mTabAdapter.setData(tabNames, tabDrawables);

        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.setSmoothScrollEnable(false);
        mIndicator.bindViewPager(mViewPager);
        mIndicator.setAdapter(mTabAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatisticsUtils.setTopStatisticsPage(null);
    }

    private static final int DELAY = 2000;
    private long mBackPressTime;

    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mBackPressTime < DELAY) {
            super.onBackPressed();
        } else {
            ToastUtils.showShort("再按一次退出程序");
            mBackPressTime = currentTimeMillis;
        }
    }

}
