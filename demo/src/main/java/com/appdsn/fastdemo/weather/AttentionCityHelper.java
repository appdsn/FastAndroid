package com.appdsn.fastdemo.weather;

import com.appdsn.fastdemo.weather.entity.AttentionCity;
import com.appdsn.fastdemo.weather.entity.WeatherCity;
import com.zgl.greentest.AttentionCityDaoManager;
import com.zgl.greentest.gen.AttentionCityDao;

import java.util.List;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/3 20:25
 */
public class AttentionCityHelper {

    /**
     * 插入新的城市
     */
    public static AttentionCity insertAttentionCity(WeatherCity weatherCity) {
        AttentionCity attentionCity = new AttentionCity();
        List<AttentionCity> list = AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao().loadAll();
        if (list == null || list.size() <= 0) {
            attentionCity.setIsDefault(true);
        } else {
            for (int i = 0; i < list.size(); i++) {
                AttentionCity city = list.get(i);
                if (weatherCity.getAreaCode().equals(city.getAreaCode())) {
                    attentionCity = city;
                    break;
                }
            }
        }

        getAttentionCityEntity(attentionCity, weatherCity);
        AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao().insertOrReplace(attentionCity);
        return attentionCity;
    }

    public static AttentionCity getAttentionCityEntity(WeatherCity weatherCity) {
        AttentionCity attentionCity = new AttentionCity();
        return getAttentionCityEntity(attentionCity, weatherCity);
    }

    public static AttentionCity getAttentionCityEntity(AttentionCity attentionCity, WeatherCity weatherCity) {
        attentionCity.setAreaCode(weatherCity.getAreaCode());
        attentionCity.setAreaName(weatherCity.getAreaName());
        attentionCity.setAreaCodeParent(weatherCity.getAreaCodeParent());
        attentionCity.setAttentionTime(System.currentTimeMillis());
        attentionCity.setCityType(weatherCity.getCityType());
        attentionCity.setCountry(weatherCity.getCountry());
        attentionCity.setProvince(weatherCity.getProvince());
        attentionCity.setCity(weatherCity.getCity());
        attentionCity.setDistrict(weatherCity.getDistrict());
        attentionCity.setIsPosition(weatherCity.isPosition);
        return attentionCity;
    }

    public static List<AttentionCity> getAttentionCityList() {
        List<AttentionCity> attentionCityList = AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao()
                .queryBuilder().orderAsc(AttentionCityDao.Properties.AttentionTime).list();
        return attentionCityList;
    }

    public static void updateAttentionCity(AttentionCity attentionCity) {
        AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao().update(attentionCity);
    }

    public static void deleteAttentionCity(AttentionCity attentionCity) {
        AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao().delete(attentionCity);
    }

    public static void saveOrUpdateAttentionCityWeather(List<AttentionCity> attentionCityList) {
        if (attentionCityList != null && !attentionCityList.isEmpty()) {
            AttentionCityDaoManager.getInstance().getDaoSession().getAttentionCityDao().insertOrReplaceInTx(attentionCityList);
        }
    }
}
