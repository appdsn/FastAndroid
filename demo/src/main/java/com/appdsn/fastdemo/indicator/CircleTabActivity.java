package com.appdsn.fastdemo.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.appdsn.commoncore.widget.smartindicator.SmartIndicator;
import com.appdsn.commoncore.widget.smartindicator.adapter.CircleIndicatorAdapter;
import com.appdsn.fastdemo.R;

import java.util.ArrayList;


public class CircleTabActivity extends FragmentActivity {

    private LoopViewPager viewPager;
    final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        setTitle("CircleTabActivity");
        viewPager = (LoopViewPager) findViewById(R.id.viewPager);
        viewPager.setCanLoop(false);
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
        CircleIndicatorAdapter circleIndicatorAdapter = new CircleIndicatorAdapter(this);
        circleIndicatorAdapter.setCircleCount(fragments.size());
        circleIndicatorAdapter.setCircleRadius(10);
        circleIndicatorAdapter.setFrontCircleColor(Color.RED);
        circleIndicatorAdapter.setBackCircleColor(Color.LTGRAY);

        indicator.setAdapter(circleIndicatorAdapter);
        indicator.bindViewPager(viewPager);
    }

    private void initSmartIndicator2() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator2);
        CircleIndicatorAdapter circleIndicatorAdapter = new CircleIndicatorAdapter(this);
        circleIndicatorAdapter.setCircleCount(fragments.size());
        circleIndicatorAdapter.setCircleRadius(10);
        circleIndicatorAdapter.setFrontCircleColor(Color.RED);
        circleIndicatorAdapter.setBackCircleColor(Color.LTGRAY);
        circleIndicatorAdapter.setStrokeWidth(3);

        indicator.setAdapter(circleIndicatorAdapter);
        indicator.bindViewPager(viewPager);
    }

    private void initSmartIndicator3() {
        SmartIndicator indicator = (SmartIndicator) findViewById(R.id.indicator3);
        CircleIndicatorAdapter circleIndicatorAdapter = new CircleIndicatorAdapter(this);
        circleIndicatorAdapter.setCircleCount(fragments.size());
        circleIndicatorAdapter.setCircleRadius(10);
        circleIndicatorAdapter.setFrontCircleColor(Color.RED);
        circleIndicatorAdapter.setBackCircleColor(Color.LTGRAY);
        circleIndicatorAdapter.setScaleRadius(15);

        indicator.setAdapter(circleIndicatorAdapter);
        indicator.bindViewPager(viewPager);
    }

    private void initFragments() {
        for (int i = 0; i < 6; i++) {
            TestFragment testFragment = new TestFragment();
            fragments.add(testFragment);
        }
    }

}
