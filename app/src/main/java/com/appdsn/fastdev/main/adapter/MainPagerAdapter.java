package com.appdsn.fastdev.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<Fragment> fragments) {
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
