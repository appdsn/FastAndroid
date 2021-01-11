package com.appdsn.commoncore.utils.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.appdsn.commoncore.utils.ContextUtils;


public class AMapLocationManger {
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLocationListener;
    private AMapLocationClientOption mLocationOption;
    private LocationBean mLocationBean;
    private static AMapLocationManger instance = null;
    private OnLocationListener mOnLocationListener;

    /**
     * 获取单例
     *
     * @return 实例
     */
    public static AMapLocationManger getInstance() {
        if (instance == null) {
            instance = new AMapLocationManger();
            return instance;
        }
        return instance;
    }

    private AMapLocationManger() {
        mLocationBean = new LocationBean();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mLocationBean.setCity(aMapLocation.getCity());
                        mLocationBean.setCityCode(aMapLocation.getCityCode());
                        mLocationBean.setLng(aMapLocation.getLongitude());
                        mLocationBean.setLot(aMapLocation.getLatitude());
                        mLocationBean.setLocationSuccess(true);
                        mOnLocationListener.onLocationSuccess(mLocationBean);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        mLocationBean.setLocationSuccess(false);
                        mOnLocationListener.onLocationFailed(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(ContextUtils.getContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 开启定位
     *
     * @param context
     */
    public void startLocation(Context context) {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 设置定位的监听器
     *
     * @param listener
     */
    public void setOnPickListener(OnLocationListener listener) {
        this.mOnLocationListener = listener;
    }

}
