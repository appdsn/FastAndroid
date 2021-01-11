package com.appdsn.fastdemo.weather;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appdsn.commonbase.utils.LocationUtils;
import com.appdsn.commoncore.base.BaseFragment;
import com.appdsn.commoncore.utils.ThreadUtils;
import com.appdsn.commoncore.widget.xrecyclerview.BaseRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.CommonViewHolder;
import com.appdsn.commoncore.widget.xrecyclerview.MultiRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.SingleRecyclerAdapter;
import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;
import com.appdsn.fastdemo.R;
import com.appdsn.fastdemo.weather.entity.WeatherCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/1 21:01
 */
public class AddCityFragment extends BaseFragment {
    private ArrayList<Object> mDataList = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CITY = 1;

    private AddCityActivity mCityActivity;
    private EditText mEtSearchCity;
    private View mIvCleanCity;
    private XRecyclerView mRecyclerViewSearch;
    private SingleRecyclerAdapter<WeatherCity> mSearchAdapter;
    private View mLaySelectCity;
    private View mLayLocation;
    private TextView mTvLocation;
    private WeatherCity mLocationCity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_city;
    }

    @Override
    protected void initVariable(Bundle arguments) {
        mCityActivity = (AddCityActivity) mActivity;
    }

    @Override
    protected void initViews(FrameLayout bodyView, Bundle savedInstanceState) {
        hideTitleBar();
        bodyView.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityActivity.finish();
            }
        });
        mLayLocation = bodyView.findViewById(R.id.layLocation);
        mTvLocation = bodyView.findViewById(R.id.tvLocation);
        initRecyclerView(bodyView);
        initSearchView(bodyView);
    }

    private void initRecyclerView(FrameLayout bodyView) {
        mLaySelectCity = bodyView.findViewById(R.id.laySelectCity);
        mRecyclerView = bodyView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAdapter = new MultiRecyclerAdapter<Object>(getActivity(), mDataList) {
            @Override
            public int getLayoutResId(int viewType) {
                if (viewType == TYPE_TITLE) {
                    return R.layout.item_select_city_title;
                } else {
                    return R.layout.item_select_city_name;
                }
            }

            @Override
            public int getItemViewType(int position, Object itemData) {
                if (itemData instanceof String) {
                    return TYPE_TITLE;
                }
                return TYPE_CITY;
            }

            @Override
            public void convert(CommonViewHolder holder, Object itemData, int position) {
                if (getItemViewType(position, itemData) == TYPE_CITY) {
                    WeatherCity weatherCity = (WeatherCity) itemData;
                    holder.setText(R.id.tvAction, weatherCity.getAreaName());
                    holder.getView(R.id.tvAction).setSelected(weatherCity.getIsAttention() && weatherCity.getIsLastArea());
                } else {
                    String title = (String) itemData;
                    holder.setText(R.id.tvTitle, title);
                }
            }
        };

        mAdapter.setSpanSizeSupport(new BaseRecyclerAdapter.SpanSizeSupport<Object>() {
            @Override
            public boolean isFullSpan(int position, Object itemData) {
                return super.isFullSpan(position, itemData);
            }

            @Override
            public int getSpanSize(int position, Object itemData, int allSize) {
                if (mAdapter.getItemViewType(position) == TYPE_TITLE) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchView(FrameLayout bodyView) {
        mEtSearchCity = bodyView.findViewById(R.id.et_search_city_content);
        mIvCleanCity = bodyView.findViewById(R.id.iv_search_city_clear);

        mEtSearchCity.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    changeContentView(true);
                    startSearch(s.toString());
                } else {
                    changeContentView(false);
                }
            }
        });

        mRecyclerViewSearch = bodyView.findViewById(R.id.recyclerViewSearch);
        mSearchAdapter = new SingleRecyclerAdapter<WeatherCity>(getActivity(), R.layout.item_search_city_name) {
            @Override
            public void convert(CommonViewHolder holder, WeatherCity itemData, int position) {
                holder.setText(R.id.tvCityName, itemData.getAreaName());
            }
        };
        mRecyclerViewSearch.setAdapter(mSearchAdapter);
    }

    @Override
    protected void setListener() {
        mRecyclerView.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Object itemData = mAdapter.getItemData(position);
                if (itemData instanceof WeatherCity) {
                    WeatherCity weatherCity = (WeatherCity) itemData;
                    if (!weatherCity.getIsLastArea()) {
                        ArrayList<String> addressList = new ArrayList<>();
                        addressList.add(weatherCity.getAreaName());
                        mCityActivity.addSelectFragment(weatherCity, addressList);
                    } else {
                        mCityActivity.addAttentionCity(weatherCity);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }
        });

        mIvCleanCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearchCity.setText("");
                changeContentView(false);
            }
        });

        mRecyclerViewSearch.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mCityActivity.addAttentionCity(mSearchAdapter.getItemData(position));
            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                return false;
            }
        });

        mLayLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTvLocation.getText().toString();
                if ("重新定位".equals(text)) {
                    startLocation();
                } else if (mLocationCity != null) {
                    mCityActivity.addAttentionCity(mLocationCity);
                }
            }
        });
    }

    private void changeContentView(boolean showSearch) {
        if (showSearch) {
            mIvCleanCity.setVisibility(View.VISIBLE);
            mRecyclerViewSearch.setVisibility(View.VISIBLE);
            mLaySelectCity.setVisibility(View.GONE);
        } else {
            mIvCleanCity.setVisibility(View.INVISIBLE);
            mRecyclerViewSearch.setVisibility(View.GONE);
            mSearchAdapter.clear();
            mLaySelectCity.setVisibility(View.VISIBLE);
        }
    }

    private String mNextKeyWord;
    private boolean mIsSearch;

    private void startSearch(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) {
            return;
        }
        if (!mIsSearch) {
            mIsSearch = true;
            ThreadUtils.execute(new ThreadUtils.ThreadTask<List<WeatherCity>>() {
                @Override
                public List<WeatherCity> doInBackground() throws Throwable {
                    return WeatherCityHelper.searchCityList(keyWord);
                }

                @Override
                public void onSuccess(List<WeatherCity> result) {
                    setSearchCityData(result);
                    mIsSearch = false;
                    startSearch(mNextKeyWord);
                    mNextKeyWord = "";
                }
            });

        } else {
            mNextKeyWord = keyWord;
        }
    }

    public void setSearchCityData(List<WeatherCity> list) {
        if (list != null && list.size() > 0) {
            mSearchAdapter.setData(list);
        } else {

        }
    }

    @Override
    protected void loadData() {
        mDataList.addAll(WeatherCityHelper.getSelectCityList());
        mAdapter.notifyDataSetChanged();
        startLocation();
    }

    private void startLocation() {
        mTvLocation.setText("定位中");
//        AMapLocationManger.getInstance().setOnPickListener(new OnLocationListener() {
//            @Override
//            public void onLocationSuccess(LocationBean locationBean) {
//                mTvLocation.setText(locationBean.getCity());
//                mLocationCity = WeatherCityHelper.findLocationWeatherCity(locationBean.getLng(), locationBean.getLot());
//            }
//
//            @Override
//            public void onLocationFailed(int errorCode, String errorInfo) {
//                mTvLocation.setText("重新定位");
//            }
//        });
//        AMapLocationManger.getInstance().startLocation(getActivity());

        LocationUtils.getCurrentLocation(mActivity, new LocationUtils.LocationCallBack() {
            @Override
            public void onSuccess(Location location) {
                mLocationCity = WeatherCityHelper.findLocationWeatherCity2(location.getLongitude(), location.getLatitude());
                if (mLocationCity != null) {
                    mLocationCity.setProvince(mLocationCity.getAreaName());
                    mTvLocation.setText(mLocationCity.getProvince());
                }
            }

            @Override
            public void onFail(String msg) {
                mTvLocation.setText("重新定位");
            }
        });
    }
}
