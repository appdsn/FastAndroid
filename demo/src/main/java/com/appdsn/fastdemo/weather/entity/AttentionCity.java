package com.appdsn.fastdemo.weather.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * 关注的城市表，areaCode为城市编码，这里的城市是统称，包含市/区/乡镇/景区等
 * 展示规则：第一优先展示定位城市，其次为默认城市，其余的从数据库表读出后倒序展示
 */
@Entity(nameInDb = "attention_city")
public class AttentionCity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @Property(nameInDb = "id")
    private Long id;

    //当前关注城市的城市编码
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

    //当前城市层级，(1:省/直辖市  2:市区  3:区/县  4:乡镇  5:景点)
    @Property(nameInDb = "cityType")
    private int cityType;

    /**
     * 上一级的城市编码
     */
    @Property(nameInDb = "areaCodeParent")
    private String areaCodeParent;

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

    //天气状态，显示晴、雨等图标
    @Property(nameInDb = "skyCondition")
    private String skyCondition;

    //最低溫度,用字符方便判断是否有值，用double则不方便比较，特别是0时，到底是否有值，保存四舍五入后的值
    @Property(nameInDb = "lowTemperature")
    private String lowTemperature;

    //最高溫度,用字符方便判断是否有值，用double则不方便比较，特别是0时，到底是否有值，保存四舍五入后的值
    @Property(nameInDb = "highTemperature")
    private String highTemperature;

    /**
     * 当天的日出时间，如：05:30
     */
    @Property(nameInDb = "sunRiseTime")
    private long sunRiseTime;

    /**
     * 当天的日落时间，如：18:30
     */
    @Property(nameInDb = "sunSetTime")
    private long sunSetTime;

    //日期，哪一天的天气数据
    @Property(nameInDb = "weatherDate")
    private Long weatherDate;

    //关注时间，用于排序显示
    @Property(nameInDb = "attentionDate")
    private Long attentionTime;

    //是否是默认城市，可能某个城市既是默认城市，又是定位城市
    @Property(nameInDb = "isDefault")
    private boolean isDefault;

    //是否是定位城市,1:是，0：否
    @Property(nameInDb = "isPosition")
    private boolean isPosition;

    @Transient
    public int itemType;

    @Generated(hash = 534583741)
    public AttentionCity(Long id, @NotNull String areaCode,
                         @NotNull String areaName, int cityType, String areaCodeParent,
                         String country, String province, String city, String district,
                         String skyCondition, String lowTemperature, String highTemperature,
                         long sunRiseTime, long sunSetTime, Long weatherDate, Long attentionTime,
                         boolean isDefault, boolean isPosition) {
        this.id = id;
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.cityType = cityType;
        this.areaCodeParent = areaCodeParent;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.skyCondition = skyCondition;
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
        this.sunRiseTime = sunRiseTime;
        this.sunSetTime = sunSetTime;
        this.weatherDate = weatherDate;
        this.attentionTime = attentionTime;
        this.isDefault = isDefault;
        this.isPosition = isPosition;
    }

    @Generated(hash = 1659471182)
    public AttentionCity() {
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

    public int getCityType() {
        return this.cityType;
    }

    public void setCityType(int cityType) {
        this.cityType = cityType;
    }

    public String getAreaCodeParent() {
        return this.areaCodeParent;
    }

    public void setAreaCodeParent(String areaCodeParent) {
        this.areaCodeParent = areaCodeParent;
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

    public String getSkyCondition() {
        return this.skyCondition;
    }

    public void setSkyCondition(String skyCondition) {
        this.skyCondition = skyCondition;
    }

    public String getLowTemperature() {
        return this.lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getHighTemperature() {
        return this.highTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        this.highTemperature = highTemperature;
    }

    public long getSunRiseTime() {
        return this.sunRiseTime;
    }

    public void setSunRiseTime(long sunRiseTime) {
        this.sunRiseTime = sunRiseTime;
    }

    public long getSunSetTime() {
        return this.sunSetTime;
    }

    public void setSunSetTime(long sunSetTime) {
        this.sunSetTime = sunSetTime;
    }

    public Long getWeatherDate() {
        return this.weatherDate;
    }

    public void setWeatherDate(Long weatherDate) {
        this.weatherDate = weatherDate;
    }

    public Long getAttentionTime() {
        return this.attentionTime;
    }

    public void setAttentionTime(Long attentionTime) {
        this.attentionTime = attentionTime;
    }

    public boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean getIsPosition() {
        return this.isPosition;
    }

    public void setIsPosition(boolean isPosition) {
        this.isPosition = isPosition;
    }
}
