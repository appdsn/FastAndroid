package com.appdsn.fastdemo.weather;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.appdsn.commoncore.base.BaseActivity;
import com.appdsn.commoncore.http.callback.ApiCallback;
import com.appdsn.commoncore.http.exception.ApiException;
import com.appdsn.commoncore.utils.BarUtils;
import com.appdsn.commoncore.utils.ToastUtils;
import com.appdsn.commoncore.widget.xrecyclerview.BaseRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.MultiRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.weather.entity.AttentionCity;
import com.appdsn.fastdemo.weather.rxhttp.RxHttpHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangbaozhong
 * @Date: 2020/5/28 15:15
 */
public class SetCityActivity extends BaseActivity {
    private ArrayList<AttentionCity> mDataList = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;
    private View mLaySetCity;
    private View mBtnAddCity;
    private View mBtnEditCity;
    private View mTvFinishSet;
    private View mLaySearchCity;

    public static final int TYPE_ATTENTION = 0;
    public static final int TYPE_EDIT = 1;
    private AttentionCity mDefaultCity;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_city;
    }

    @Override
    protected void initVariable(Intent intent) {
    }

    @Override
    protected void initViews(FrameLayout frameLayout, Bundle bundle) {
        setCenterTitle("城市管理");
        setLeftButton(R.drawable.base_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BarUtils.setStatusBarColor(this, R.color.white);
        mLaySetCity = findViewById(R.id.laySetCity);
        mBtnAddCity = findViewById(R.id.btnAddCity);
        mBtnEditCity = findViewById(R.id.btnEditCity);
        mTvFinishSet = findViewById(R.id.tvFinishSet);
        mLaySearchCity = findViewById(R.id.laySearchCity);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MultiRecyclerAdapter<AttentionCity>(mContext, mDataList) {
            @Override
            public int getLayoutResId(int viewType) {
                if (viewType == TYPE_EDIT) {
                    return R.layout.item_set_city_edit;
                } else {
                    return R.layout.item_set_city_attention;
                }
            }

            @Override
            public int getItemViewType(int position, AttentionCity itemData) {
                return itemData.itemType;
            }

            @Override
            public void convert(CommonViewHolder holder, AttentionCity itemData, int position) {
                holder.setText(R.id.tvCityName, itemData.getAreaName());
                holder.setText(R.id.tvCityAddress, itemData.getProvince());
                if (itemData.getIsPosition()) {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_location_city);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.setText(R.id.tvCityName, itemData.getProvince());
                    holder.getTextView(R.id.tvCityName).setCompoundDrawables(null, null, drawable, null);
                } else {
                    holder.setText(R.id.tvCityName, itemData.getAreaName());
                    holder.getTextView(R.id.tvCityName).setCompoundDrawables(null, null, null, null);
                }

                if (itemData.getIsDefault()) {
                    holder.getView(R.id.layMarker).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.layMarker).setVisibility(View.GONE);
                }

                if (getItemViewType(position, itemData) == TYPE_EDIT) {
                    if (itemData.getIsDefault()) {
                        mDefaultCity = itemData;
                        holder.getView(R.id.tvCitySet).setEnabled(false);
                        holder.getTextView(R.id.tvCitySet).setTextColor(getResources().getColor(R.color.white_50));
                        holder.setBackgroundResource(R.id.tvCitySet, R.drawable.shape_comm_bg_gray);
                    } else {
                        holder.getView(R.id.tvCitySet).setEnabled(true);
                        holder.getTextView(R.id.tvCitySet).setTextColor(getResources().getColor(R.color.white));
                        holder.setBackgroundResource(R.id.tvCitySet, R.drawable.shape_comm_bg_red);
                    }

                    holder.setOnClickListener(R.id.ivCityDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemData.getIsDefault()) {
                                ToastUtils.showShort("默认城市不能删除");
                            } else {
                                AttentionCityHelper.deleteAttentionCity(itemData);
                                WeatherCityHelper.updateAttentionCityByCode(itemData.getAreaCode(), false);

                                mDataList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                    holder.setOnClickListener(R.id.tvCitySet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemData.setIsDefault(true);
                            if (mDefaultCity != null) {
                                mDefaultCity.setIsDefault(false);
                            }
                            AttentionCityHelper.updateAttentionCity(itemData);
                            notifyDataSetChanged();
                        }
                    });
                } else {


                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mBtnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddCityActivity.class);
            }
        });
        mBtnEditCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvFinishSet.setVisibility(View.VISIBLE);
                mLaySetCity.setVisibility(View.GONE);
                changeModel(true);
            }
        });
        mTvFinishSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvFinishSet.setVisibility(View.GONE);
                mLaySetCity.setVisibility(View.VISIBLE);
                changeModel(false);
            }
        });
        mLaySearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddCityActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {
        mDataList.addAll(AttentionCityHelper.getAttentionCityList());
        changeModel(false);

        RxHttpHelper.requestAttentionCityWeather(new ApiCallback<List<AttentionCity>>() {
            @Override
            public void onFailure(ApiException e, String code, String message) {

            }

            @Override
            public void onSuccess(List<AttentionCity> result) {

            }
        });
    }

    private void changeModel(boolean isEditModel) {
        int itemType;
        if (isEditModel) {
            itemType = TYPE_EDIT;
        } else {
            itemType = TYPE_ATTENTION;
        }
        for (int i = 0; i < mDataList.size(); i++) {
            mDataList.get(i).itemType = itemType;
        }
        mAdapter.notifyDataSetChanged();
    }
}
