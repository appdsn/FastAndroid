package com.appdsn.fastdemo.indicator;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.adapter.IndicatorAdapter;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.BaseScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.LineScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.TextTabView;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;

public class NoViewPagerActivity extends FragmentActivity {
    private SmartIndicator indicator;
    private String[] tabNames = {"首页", "关注", "消息", "我的", "CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "NOUGAT", "DONUT"};
    private int[] tabNormalDrawables = {R.drawable.main_no, R.drawable.follow_no, R.drawable.message_no, R.drawable.mine_no};
    private int[] tabSelectedDrawables = {R.drawable.main_yes, R.drawable.follow_yes, R.drawable.message_yes, R.drawable.mine_yes};
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_viewpager);
        setTitle("NO ViewPager Activity");
        initFragments();
        switchPages(0);

        initSmartIndicator1();
        initSmartIndicator2();
        initSmartIndicator3();
    }

    private void initSmartIndicator1() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator1);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setNormalTextColor(Color.parseColor("#9e9e9e"));
                tabView.setSelectedTextColor(Color.parseColor("#333333"));
                tabView.setText(tabNames[position]);
                tabView.setTextSize(12);
                tabView.setSelectedTextSize(16);
                tabView.setPadding(30, 0, 30, 0);
                tabView.setScrollScale(true);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.RED);//滚动块颜色
                scrollBar.setHeight(dip2px(context, 2));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setWidthWrapContent(20, 20);//滚动块宽度，不设置默认和每个tabview宽度一致
                scrollBar.setWidthFix(20);
                scrollBar.setStartInterpolator(new AccelerateInterpolator());
                scrollBar.setEndInterpolator(new DecelerateInterpolator(2f));
                scrollBar.setScrollEnable(true);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.setOnTabClickListener(new SmartIndicator.OnTabClickListener() {
            @Override
            public boolean onClick(View tabView, int position) {
                switchPages(position);
                return true;
            }
        });

        indicator.setOnTabSelectedListener(new SmartIndicator.OnTabSelectedListener() {
            @Override
            public void onSelected(View tabView, int position) {
                Log.i("123", "onSelected:" + position);
            }

            @Override
            public void onDeselected(View tabView, int position) {
                Log.i("123", "onDeselected:" + position);
            }
        });
    }

    private void initSmartIndicator2() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator2);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setNormalTextColor(Color.parseColor("#9e9e9e"));
                tabView.setSelectedTextColor(Color.parseColor("#333333"));
                tabView.setText(tabNames[position]);
                tabView.setTextSize(14);
                tabView.setPadding(30, 0, 30, 0);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.parseColor("#ebe4e3"));//滚动块颜色
                scrollBar.setHeight(dip2px(context, 30));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setWidthWrapContent(20, 20);//滚动块宽度，不设置默认和每个tabview宽度一致
                scrollBar.setRoundRadius(dip2px(context, 15));
                scrollBar.setGravity(BaseScrollBar.Gravity.CENTER, 0);
                scrollBar.setScrollEnable(true);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });

        indicator.setOnTabClickListener(new SmartIndicator.OnTabClickListener() {
            @Override
            public boolean onClick(View tabView, int position) {
                switchPages(position);
                return true;
            }
        });
    }

    private void initSmartIndicator3() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator3);
        indicator.setFixEnableAsync(true);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setNormalDrawable(tabNormalDrawables[position], 0);
                tabView.setSelectedDrawable(tabSelectedDrawables[position], 0);
                tabView.setText(tabNames[position]);
                tabView.setNormalTextColor(Color.GRAY);
                tabView.setSelectedTextColor(Color.RED);
                tabView.setPadding(0, 10, 0, 0);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                return null;
            }

            @Override
            public int getTabCount() {
                return 4;
            }
        });

        indicator.setOnTabClickListener(new SmartIndicator.OnTabClickListener() {
            @Override
            public boolean onClick(View tabView, int position) {
                switchPages(position);
                return true;
            }
        });
    }


    private void switchPages(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = fragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = fragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = fragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.content, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initFragments() {
        for (int i = 0; i < tabNames.length; i++) {
            TestFragment testFragment = TestFragment.create(tabNames[i]);
            fragments.add(testFragment);
        }
    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
