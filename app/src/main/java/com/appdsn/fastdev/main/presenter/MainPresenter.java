package com.appdsn.fastdev.main.presenter;

import android.support.v4.app.Fragment;

import com.appdsn.commoncore.base.BasePresenter;
import com.appdsn.fastdev.R;
import com.appdsn.fastdev.main.activity.MainActivity;
import com.appdsn.fastdev.main.fragment.HomeFragment;
import com.appdsn.fastdev.mine.fragment.MineFragment;

import java.util.ArrayList;

public class MainPresenter extends BasePresenter<MainActivity> {

    public void initFragment() {
        ArrayList fragments = new ArrayList<Fragment>();
        HomeFragment homeFragment = new HomeFragment();
        MineFragment mineFragment = new MineFragment();
        fragments.add(homeFragment);
        fragments.add(mineFragment);

        String[] tabNames = {mView.getResources().getString(R.string.home), mView.getResources().getString(R.string.mine)};

        int[] tabDrawables = {R.drawable.main_icon_tab_home_selector, R.drawable.main_icon_tab_mine_selector};
        mView.loadFragmentDataSuccess(fragments, tabNames, tabDrawables);
    }

}
