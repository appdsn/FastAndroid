package com.appdsn.fastdemo.weather.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;


@Entity(nameInDb = "weather_city")
public class WeatherCity implements Serializable, Cloneable {
    public static final long serialVersionUID = 1L;

    @Id
    @Property(nameInDb = "id")
    private Long id;

    /**
     * 城市编码 areaCode : c310114
     * 城市(省/市/区/乡镇)名 areaName : 嘉定区
     * 下一级城市信息 childCities : xzhk
     */
    @Property(nameInDb = "areaCode")
    @NotNull
    @Unique
    private String areaCode;

    /**
     * 城市(省/市/区/乡镇)名
     */
    @Property(nameInDb = "areaName")
    @NotNull
    private String areaName;

    /**
     * 上一级的城市编码
     */
    @Property(nameInDb = "areaCodeParent")
    private String areaCodeParent;

    /**
     * 当前城市层级，(1:省/直辖市  2:市区  3:区/县  4:乡镇  5:景点)
     * 有的城市可能最后一级是 3:区/县 ，有的可能是 4:乡镇
     */
    @Property(nameInDb = "cityType")
    private int cityType;

    /**
     * 是否是最后一级城市，false:否，true:是最后一级
     */
    @Property(nameInDb = "isLastArea")
    private boolean isLastArea;

    //国家
    @Property(nameInDb = "country")
    private String country;

    //省份，如湖北省
    @Property(nameInDb = "province")
    private String province;

    //城市，如武汉市
    @Property(nameInDb = "city")
    private String city;

    //区、县
    @Property(nameInDb = "district")
    private String district;

    @Property(nameInDb = "longitude")
    private double longitude;

    @Property(nameInDb = "latitude")
    private double latitude;

    @Property(nameInDb = "isRecommend")
    private boolean isRecommend;

    /**
     * 自己定义的字段，用于判断该城市用户是否已经添加，需要查询数据库表赋值
     */
    @Property(nameInDb = "isAttention")
    private boolean isAttention;

    @Transient
    public boolean isPosition;

    @Generated(hash = 294519829)
    public WeatherCity(Long id, @NotNull String areaCode, @NotNull String areaName,
                       String areaCodeParent, int cityType, boolean isLastArea, String country,
                       String province, String city, String district, double longitude,
                       double latitude, boolean isRecommend, boolean isAttention) {
        this.id = id;
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.areaCodeParent = areaCodeParent;
        this.cityType = cityType;
        this.isLastArea = isLastArea;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isRecommend = isRecommend;
        this.isAttention = isAttention;
    }

    @Generated(hash = 187543502)
    public WeatherCity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCodeParent() {
        return this.areaCodeParent;
    }

    public void setAreaCodeParent(String areaCodeParent) {
        this.areaCodeParent = areaCodeParent;
    }

    public int getCityType() {
        return this.cityType;
    }

    public void setCityType(int cityType) {
        this.cityType = cityType;
    }

    public boolean getIsLastArea() {
        return this.isLastArea;
    }

    public void setIsLastArea(boolean isLastArea) {
        this.isLastArea = isLastArea;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean getIsRecommend() {
        return this.isRecommend;
    }

    public void setIsRecommend(boolean isRecommend) {
        this.isRecommend = isRecommend;
    }

    public boolean getIsAttention() {
        return this.isAttention;
    }

    public void setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    /**
     * WeatherCity类下面都是基本数据类型，可以用浅克隆
     */
    @Override
    public WeatherCity clone() {
        WeatherCity areaInfoResponseEntity = null;
        try {
            areaInfoResponseEntity = (WeatherCity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return areaInfoResponseEntity;
    }
}
