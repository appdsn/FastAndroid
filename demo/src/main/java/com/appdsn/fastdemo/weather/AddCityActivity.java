package com.appdsn.fastdemo.weather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appdsn.commoncore.base.BaseActivity;
import com.appdsn.commoncore.event.EventBusHelper;
import com.appdsn.commoncore.event.EventMessage;
import com.appdsn.commoncore.utils.BarUtils;
import com.appdsn.fastdemo.MainActivity;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.weather.entity.AttentionCity;
import com.appdsn.fastdemo.weather.entity.EventCode;
import com.appdsn.fastdemo.weather.entity.WeatherCity;

import java.util.ArrayList;

/**
 * @Author: wangbaozhong
 * @Date: 2020/5/28 15:14
 */
public class AddCityActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_city;
    }

    @Override
    protected void initVariable(Intent intent) {

    }

    @Override
    protected void initViews(FrameLayout frameLayout, Bundle bundle) {
        hideTitleBar();
        BarUtils.setStatusBarColor(this, R.color.white);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
        addFragment(new AddCityFragment(), R.id.fragment_container);
    }

    public void addSelectFragment(WeatherCity weatherCity, ArrayList<String> detailAddress) {
        SelectCityFragment fragment = SelectCityFragment.getInstance(weatherCity, detailAddress);
        if (fragment != null) {
            addFragmentStack(fragment, R.id.fragment_container);
        }
    }

    public void popSelectFragment() {
        popFragmentStack();
    }

    public void addAttentionCity(WeatherCity city) {
        if (city.getIsAttention()) {
            //点击的城市已经关注过了
            //EventBus.getDefault().post(new SwichAttentionDistrictEvent(WeatherCityHelper.getUserAttentionCityEntity(areaInfoResponseEntity)));
        } else {
            city.setIsAttention(true);
            WeatherCityHelper.updateAttentionCityByCode(city.getAreaCode(), true);
            AttentionCity attentionCity = AttentionCityHelper.insertAttentionCity(city);
            EventBusHelper.post(new EventMessage(EventCode.EVENT_CODE_ADD_ATTENTION_CITY, attentionCity));
        }
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}
