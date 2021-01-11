package com.appdsn.fastdemo.indicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appdsn.fastdemo.R;

public class MainIndicatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_main);
        setTitle("SmartIndicator");
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                openActivity(FixTabActivity.class);
                break;
            case R.id.btn2:
                openActivity(ScrollTabActivity.class);
                break;
            case R.id.btn3:
                openActivity(CircleTabActivity.class);
                break;
            case R.id.btn4:
                openActivity(NoViewPagerActivity.class);
                break;
            default:
                break;
        }
    }

    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent();
        intent.setClass(this, pClass);
        super.startActivity(intent);
    }

}
