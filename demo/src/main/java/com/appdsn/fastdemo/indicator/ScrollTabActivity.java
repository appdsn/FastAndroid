package com.appdsn.fastdemo.indicator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.adapter.IndicatorAdapter;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.BaseScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.BezierScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.DrawableScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.LineScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ClipTabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.TextTabView;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;


public class ScrollTabActivity extends FragmentActivity {
    private ViewPager viewPager;
    private String[] tabNames = {"全部", "前端开发", "后端开发", "设计", "移动开发", "其他类干货", "正在热映", "即将上映"};
    private String[] tabNamesSelected = {"HOME", "HTML5", "SERVICE", "UI", "APP", "OTHER", "HOT", "WILL"};

    final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        setTitle("ScrollTabActivity");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
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
        initSmartIndicator4();
        initSmartIndicator5();
        initSmartIndicator6();
        initSmartIndicator7();
        initSmartIndicator8();
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
        indicator.bindViewPager(viewPager, 4);
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
                tabView.setNormalTextSize(12);
                tabView.setSelectedTextSize(16);
                tabView.setPadding(30, 0, 30, 0);

                tabView.setScrollScale(false);
                tabView.setSelectedTypeface(Typeface.DEFAULT_BOLD);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.RED);//滚动块颜色
                scrollBar.setHeight(dip2px(context, 2));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setRoundRadius(2);
                scrollBar.setWidthFix(20);
                scrollBar.setScrollEnable(false);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager, 4);
    }

    private void initSmartIndicator3() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator3);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                TextTabView tabView = new TextTabView();
                tabView.setNormalTextColor(Color.parseColor("#9e9e9e"));
                tabView.setSelectedTextColor(Color.parseColor("#333333"));
                tabView.setNormalText(tabNames[position]);
                tabView.setSelectedText(tabNamesSelected[position]);
                tabView.setTextSize(14);
                tabView.setSelectedTextSize(16);
                tabView.setPadding(30, 0, 30, 0);
                tabView.setScrollScale(false);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.RED);//滚动块颜色
                scrollBar.setHeight(dip2px(context, 2));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setWidthWrapContent(20, 20);//滚动块宽度，不设置默认和每个tabview宽度一致
                scrollBar.setGravity(BaseScrollBar.Gravity.TOP, 10);
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
        indicator.bindViewPager(viewPager, 4);
    }

    private void initSmartIndicator4() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator4);
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
        indicator.bindViewPager(viewPager, 4);
    }

    private void initSmartIndicator5() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator5);
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
                tabView.setScrollScale(true);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                BezierScrollBar scrollBar = new BezierScrollBar(context);
                scrollBar.setColor(Color.RED);//滚动块颜色
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager, 4);
    }

    private void initSmartIndicator6() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator6);
        indicator.setAdapter(new IndicatorAdapter() {
            @Override
            public void onAttachToIndicator(Context context, SmartIndicator indicator) {
            }

            @Override
            public ITabView getTabView(Context context, int position, LinearLayout parrent) {
                ClipTabView tabView = new ClipTabView(context);
                tabView.setTextColor(Color.parseColor("#9e9e9e"));
                tabView.setClipColor(Color.parseColor("#333333"));
                tabView.setText(tabNames[position]);
                int padding = dip2px(context, 10);
                tabView.setPadding(padding, 0, padding, 0);
                tabView.setTextSize(14);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                LineScrollBar scrollBar = new LineScrollBar(context);
                scrollBar.setColor(Color.parseColor("#ebe4e3"));
                scrollBar.setHeight(dip2px(context, 30));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setWidthWrapContent(20, 20);//滚动块宽度，不设置默认和每个tabview宽度一致
                scrollBar.setRoundRadius(dip2px(context, 15));
                scrollBar.setGravity(BaseScrollBar.Gravity.CENTER, 0);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager, 4);
    }


    private void initSmartIndicator7() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator7);
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
                tabView.setSelectedTextSize(16);
                tabView.setPadding(30, 0, 30, 0);
                tabView.setScrollScale(true);
                return tabView;
            }

            @Override
            public IScrollBar getScrollBar(Context context) {
                DrawableScrollBar scrollBar = new DrawableScrollBar(context);
                scrollBar.setColor(Color.RED);//滚动块颜色
                scrollBar.setHeight(dip2px(context, 8));//滚动块高度，不设置默认和每个tabview高度一致
                scrollBar.setWidthFix(dip2px(context, 16));
                scrollBar.setDrawable(R.drawable.icon_jiantou);
                scrollBar.setScrollEnable(true);
                return scrollBar;
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        indicator.bindViewPager(viewPager, 4);
    }

    private void initSmartIndicator8() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator8);
        indicator.setFollowTouch(false);
//        indicator.setSmoothScrollEnable(false);
        indicator.setEnablePivotScroll(false);
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
        indicator.bindViewPager(viewPager, 4);
    }

    private void initFragments() {
        for (int i = 0; i < tabNames.length; i++) {
            TestFragment testFragment = new TestFragment();
            fragments.add(testFragment);
        }
    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
