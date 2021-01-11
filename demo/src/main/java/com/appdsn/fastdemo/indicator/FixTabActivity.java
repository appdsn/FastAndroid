package com.appdsn.fastdemo.indicator;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.adapter.IndicatorAdapter;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.BaseScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.LineScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ClipTabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.TextTabView;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;


public class FixTabActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private String[] tabNames = {"首页", "关注", "消息", "我的"};
    private int[] tabNormalDrawables = {R.drawable.main_no, R.drawable.follow_no, R.drawable.message_no, R.drawable.mine_no};
    private int[] tabSelectedDrawables = {R.drawable.main_yes, R.drawable.follow_yes, R.drawable.message_yes, R.drawable.mine_yes};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);
        setTitle("FixTabActivity");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        initFragments();
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return fragments.get(arg0);
            }
        });

        initSmartIndicator1();
        initSmartIndicator2();
        initSmartIndicator3();
    }

    private void initSmartIndicator1() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator1);
        indicator.setFixEnableAsync(true);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                ClipTabView tabView = new ClipTabView(context);
                tabView.setTextColor(Color.BLACK);
                tabView.setClipColor(Color.WHITE);
                tabView.setText(tabNames[position]);
                tabView.setTextSize(14);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.BLUE);
                scrollBar.setRoundRadius(dip2px(context, 5));
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager);
    }

    private void initSmartIndicator2() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator2);
        indicator.setFixEnableAsync(true);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setTextColor(Color.BLACK);
                tabView.setSelectedTextColor(Color.WHITE);
                tabView.setText(tabNames[position]);
                tabView.setTextSize(14);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setWidthWrapContent(20, 20);
                scrollBar.setHeight(5);
                scrollBar.setColor(Color.WHITE);
                scrollBar.setRoundRadius(dip2px(context, 5));
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager);
    }

    private void initSmartIndicator3() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator3);
        indicator.setFixEnableAsync(true);
        indicator.setScrollBarFront(true);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setNormalDrawable(tabNormalDrawables[position], 0);
                tabView.setSelectedDrawable(tabSelectedDrawables[position], 0);
                tabView.setNormalText(tabNames[position]);
                tabView.setSelectedText("");
                tabView.setNormalTextColor(Color.GRAY);
                tabView.setSelectedTextColor(Color.RED);
                tabView.setPadding(0, 10, 0, 0);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.RED);
                scrollBar.setHeight(dip2px(context, 5));
                scrollBar.setWidthFix(dip2px(context, 5));
                scrollBar.setGravity(BaseScrollBar.Gravity.BOTTOM, -dip2px(context, 8));
                scrollBar.setRoundRadius(dip2px(context, 2.5));
                scrollBar.setScrollEnable(false);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
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

        indicator.bindViewPager(viewPager);
    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    private void initFragments() {
        for (int i = 0; i < tabNames.length; i++) {
            TestFragment testFragment = new TestFragment();
            fragments.add(testFragment);
        }
    }
}
