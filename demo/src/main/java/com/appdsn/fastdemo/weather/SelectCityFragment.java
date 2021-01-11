package com.appdsn.fastdemo.weather;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appdsn.commoncore.base.BaseFragment;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.MultiRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.weather.entity.CityType;
import com.appdsn.fastdemo.weather.entity.WeatherCity;

import java.util.ArrayList;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/1 21:01
 */
public class SelectCityFragment extends BaseFragment {
    private ArrayList<WeatherCity> mDataList = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private MultiRecyclerAdapter mAdapter;
    public static final int TYPE_CITY = 1;

    public static final String KEY_PARENT_CITY = "KEY_PARENT_CITY";
    public static final String KEY_DETAIL_ADDRESS = "KEY_DETAIL_ADDRESS";

    private AddCityActivity mCityActivity;
    private String mAreaCodeParent;
    private WeatherCity mParentWeatherCity;
    private ArrayList<String> mAddressList;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_city;
    }

    public static SelectCityFragment getInstance(WeatherCity weatherCity, ArrayList<String> detailAddress) {
        if (weatherCity == null || detailAddress == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectCityFragment.KEY_PARENT_CITY, weatherCity);
        bundle.putSerializable(SelectCityFragment.KEY_DETAIL_ADDRESS, detailAddress);
        SelectCityFragment selectCityFragment = new SelectCityFragment();
        selectCityFragment.setArguments(bundle);
        return selectCityFragment;
    }

    @Override
    protected void initVariable(Bundle arguments) {
        mCityActivity = (AddCityActivity) mActivity;
        mParentWeatherCity = (WeatherCity) arguments.getSerializable(KEY_PARENT_CITY);
        mAreaCodeParent = mParentWeatherCity.getAreaCode();
        mAddressList = (ArrayList<String>) arguments.getSerializable(KEY_DETAIL_ADDRESS);
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        setCenterTitle("选择城市");
        setLeftButton(R.drawable.base_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityActivity.popSelectFragment();
            }
        });
        TextView tvParentCity = bodyView.findViewById(R.id.tvParentCity);
        String address = "";
        for (int i = 0; i < mAddressList.size(); i++) {
            address += mAddressList.get(i);
            if (i != mAddressList.size() - 1) {
                address += ">";
            }
        }
        tvParentCity.setText(address);
        initRecyclerView(bodyView);
    }

    private void initRecyclerView(FrameLayout bodyView) {
        mRecyclerView = bodyView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAdapter = new MultiRecyclerAdapter<WeatherCity>(getActivity(), mDataList) {
            @Override
            public int getLayoutResId(int viewType) {
                return R.layout.item_select_city_name;
            }

            @Override
            public int getItemViewType(int position, WeatherCity itemData) {
                return TYPE_CITY;
            }

            @Override
            public void convert(CommonViewHolder holder, WeatherCity itemData, int position) {
                holder.setText(R.id.tvAction, itemData.getAreaName());
                holder.getView(R.id.tvAction).setSelected(itemData.getIsAttention() && itemData.getIsLastArea());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mRecyclerView.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                WeatherCity itemData = (WeatherCity) mAdapter.getItemData(position);
                if (!itemData.getIsLastArea()) {
                    ArrayList<String> addressList = (ArrayList<String>) mAddressList.clone();
                    addressList.add(itemData.getAreaName());
                    mCityActivity.addSelectFragment(itemData, addressList);
                } else {
                    mCityActivity.addAttentionCity(itemData);
                }
            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
        mDataList.addAll(WeatherCityHelper.getAreaCityList(mAreaCodeParent));
        if (mParentWeatherCity.getCityType() > CityType.FIRST_LEVEL
                && mParentWeatherCity.getCityType() < CityType.FOURTH_LEVEL) {
            //浅克隆(ShallowClone)
            WeatherCity weatherCityClone = (WeatherCity) mParentWeatherCity.clone();
            if (weatherCityClone != null && !TextUtils.isEmpty(weatherCityClone.getAreaCode())) {
                //将上一级的城市加入到当前子集的第一个，并且标记它没有下一级了，用户点击后直接添加该城市
                weatherCityClone.setIsLastArea(true);
                mDataList.add(0, weatherCityClone);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
