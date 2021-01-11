package com.appdsn.fastdev.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.adapter.IndicatorAdapter;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.TextTabView;
import com.appdsn.fastdev.R;

public class MainTabAdapter implements IndicatorAdapter {

    private LayoutInflater mInflater;
    private String[] mTabNames;
    private int[] mTabDrawables;

    public MainTabAdapter(LayoutInflater inflater) {
        mInflater = inflater;
    }

    private View mMsgRedDot;

    public void setData(String[] tabNames, int[] tabDrawables) {
        mTabNames = tabNames;
        mTabDrawables = tabDrawables;
    }

    @Override
    public void onAttachToIndicator(Context context, SmartIndicator indicator) {
        indicator.setFixEnableAsync(true);
    }

    @Override
    public ITabView getTabView(Context context, int position, LinearLayout parrent) {
        TextTabView tabView = new TextTabView() {
            @Override
            public View createTabView(Context context, int position, LinearLayout parrent) {
                View view = super.createTabView(context, position, parrent);
                FrameLayout tabLayout = (FrameLayout) mInflater.inflate(R.layout.layout_main_tab_item, null);
                tabLayout.addView(view);
                if (position == 2) {
                    mMsgRedDot = tabLayout.findViewById(R.id.tvRedDot);
                    tabLayout.bringChildToFront(mMsgRedDot);
                }
                return tabLayout;
            }
        };
        tabView.setText(mTabNames[position]);
        tabView.setDrawable(mTabDrawables[position], 0);
        return tabView;
    }

    @Override
    public IScrollBar getScrollBar(Context context) {
        return null;
    }

    @Override
    public int getTabCount() {
        return mTabNames.length;
    }


    public void showMsgRedDot() {
        if (mMsgRedDot != null) {
            mMsgRedDot.setVisibility(View.VISIBLE);
        }
    }

    public void hideMsgRedDot() {
        if (mMsgRedDot != null) {
            mMsgRedDot.setVisibility(View.GONE);
        }
    }
}
