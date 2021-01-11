package com.appdsn.fastdev.mine.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.FrameLayout;

import com.appdsn.commonbase.base.BaseAppFragment;
import com.appdsn.commonbase.utils.statistics.PageStatisticsEvent;
import com.appdsn.commoncore.event.BindEventBus;
import com.appdsn.fastdev.R;
import com.appdsn.fastdev.mine.presenter.MinePresenter;

import java.util.ArrayList;

@BindEventBus
public class MineFragment extends BaseAppFragment<MineFragment, MinePresenter> {

    private ViewPager mViewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initVariable(Bundle arguments) {

    }

    @Override
    protected void initViews(FrameLayout contentView, Bundle savedInstanceState) {
        setCenterTitle("我的");
        setStatusBarTranslucent();
        mViewPager = contentView.findViewById(R.id.mineViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        Test2Fragment test2Fragment = new Test2Fragment();
        TestFragment testFragment = new TestFragment();
        fragments.add(testFragment);
        fragments.add(test2Fragment);

        FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(pagerAdapter);

    }

    @Override
    public PageStatisticsEvent getPageEvent() {
        return PageStatisticsEvent.minePage;
    }

    @Override
    protected void onVisibleToUser(boolean visible) {
        super.onVisibleToUser(visible);
        Log.i("123", "onVisibleToUser_mine:" + visible);
    }
}
