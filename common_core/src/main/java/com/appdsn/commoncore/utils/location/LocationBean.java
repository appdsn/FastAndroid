package com.appdsn.commoncore.utils.location;

import java.io.Serializable;

/**
 * Desc:
 * <p>
 * Author: ZhangQi
 * Date: 2019/10/16
 * Copyright: Copyright (c) 2016-2020
 * ""
 * ""
 * Update Comments:
 *
 * @author zhangqi
 */
public class LocationBean implements Serializable {
    private boolean locationSuccess;
    private String city;
    private String cityCode;
    private double lng;
    private double lat;

    public boolean isLocationSuccess() {
        return locationSuccess;
    }

    public void setLocationSuccess(boolean locationSuccess) {
        this.locationSuccess = locationSuccess;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLot() {
        return lat;
    }

    public void setLot(double lat) {
        this.lat = lat;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

}
