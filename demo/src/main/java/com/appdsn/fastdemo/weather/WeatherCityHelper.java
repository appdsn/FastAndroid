package com.appdsn.fastdemo.weather;

import android.database.Cursor;

import com.appdsn.commoncore.utils.GsonUtils;
import com.appdsn.commoncore.utils.ResourceUtils;
import com.appdsn.fastdemo.weather.entity.WeatherCity;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgl.greentest.WeatherCityDaoManager;
import com.zgl.greentest.gen.DaoSession;
import com.zgl.greentest.gen.WeatherCityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/3 20:25
 */
public class WeatherCityHelper {

    public static List<WeatherCity> getProvinceCityList() {
        try {
            String cityJson = ResourceUtils.readAssets2String("province_city_data.json");
            List<WeatherCity> provinceCityList = GsonUtils.fromJson(cityJson, new TypeToken<List<WeatherCity>>() {
            }.getType());
            List<String> list = getAreaCodeList(provinceCityList);
            provinceCityList = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCode.in(list)).list();
            return provinceCityList;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<WeatherCity> getRecommendCityList() {
        try {
            String cityJson = ResourceUtils.readAssets2String("recommend_city_data.json");
            List<WeatherCity> recommendCityList = GsonUtils.fromJson(cityJson, new TypeToken<List<WeatherCity>>() {
            }.getType());
            List<String> list = getAreaCodeList(recommendCityList);
            List<WeatherCity> attentionCityList = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCode.in(list)).list();
            recommendCityList.clear();
            for (int i = 0; i < attentionCityList.size(); i++) {
                WeatherCity city = attentionCityList.get(i).clone();
                city.setIsLastArea(true);
                recommendCityList.add(city);
            }
            return recommendCityList;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> getAreaCodeList(List<WeatherCity> cityList) {
        List<String> codeList = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            codeList.add(cityList.get(i).getAreaCode());
        }
        return codeList;
    }

    public static List<Object> getSelectCityList() {
        List<Object> selectCityList = new ArrayList<>();
        List<WeatherCity> recommendCityList = getRecommendCityList();
        if (recommendCityList != null && recommendCityList.size() > 0) {
            selectCityList.add("热门城市");
            selectCityList.addAll(recommendCityList);
        }
        List<WeatherCity> provinceCityList = getProvinceCityList();
        if (provinceCityList != null && provinceCityList.size() > 0) {
            selectCityList.add("选择省份");
            selectCityList.addAll(provinceCityList);
        }
        return selectCityList;
    }

    public static List<WeatherCity> getAreaCityList(String areaCodeParent) {
        return WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCodeParent.eq(areaCodeParent)).list();
    }

    public static List<WeatherCity> searchCityList(String keyWord) {
        List<WeatherCity> searchWeatherCityResult = new ArrayList<>();
        LinkedHashSet<WeatherCity> rawSearchWeatherCitySet = new LinkedHashSet<>();
        //LinkedHashSet不能添加重复数据并能保证添加顺序
        try {
            rawSearchWeatherCitySet.addAll(searchAreaName(keyWord));
            rawSearchWeatherCitySet.addAll(searchDistrict(keyWord));
            rawSearchWeatherCitySet.addAll(searchCity(keyWord));
            rawSearchWeatherCitySet.addAll(searchProvince(keyWord));
            rawSearchWeatherCitySet.addAll(searchCountry(keyWord));
            searchWeatherCityResult.addAll(rawSearchWeatherCitySet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchWeatherCityResult;

    }

    /**
     * 搜索区、县
     */
    public static List<WeatherCity> searchAreaName(String name) {
        QueryBuilder queryBuilder = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder();
        queryBuilder.where(WeatherCityDao.Properties.AreaName.like("%" + name + "%"));
        List<WeatherCity> list = queryBuilder.list();
        return list;
    }

    /**
     * 搜索区、县
     */
    public static List<WeatherCity> searchDistrict(String district) {
        QueryBuilder queryBuilder = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder();
        queryBuilder.where(WeatherCityDao.Properties.District.like("%" + district + "%"));
        List<WeatherCity> list = queryBuilder.list();
        return list;
    }

    /**
     * 搜索城市
     */
    public static List<WeatherCity> searchCity(String city) {
        QueryBuilder queryBuilder = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder();
        queryBuilder.where(WeatherCityDao.Properties.City.like("%" + city + "%"));
        List<WeatherCity> list = queryBuilder.list();
        return list;
    }

    /**
     * 搜索省
     */
    public static List<WeatherCity> searchProvince(String province) {
        QueryBuilder queryBuilder = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder();
        queryBuilder.where(WeatherCityDao.Properties.Province.like("%" + province + "%"));
        List<WeatherCity> list = queryBuilder.list();
        return list;
    }

    /**
     * 搜索国家
     */
    public static List<WeatherCity> searchCountry(String country) {
        QueryBuilder queryBuilder = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder();
        queryBuilder.where(WeatherCityDao.Properties.Country.like("%" + country + "%"));
        List<WeatherCity> list = queryBuilder.list();
        return list;
    }

    public static boolean updateAttentionCityByCode(String areaCode, boolean isAttention) {
        List<WeatherCity> list = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCode.eq(areaCode)).list();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setIsAttention(isAttention);
            }
            WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().updateInTx(list);
        }
        return true;
    }

    public static WeatherCity findLocationWeatherCity(double longitude, double latitude) {
        String sql = "SELECT areaCode, ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((? * PI() / 180 - latitude * PI() / 180) / 2), 2) + COS(? * PI() / 180) * COS(latitude * PI() / 180) * POW(SIN((? * PI() / 180 - longitude * PI() / 180) / 2), 2))) * 1000) AS distance FROM weather_city ORDER BY distance ASC LIMIT 0, 1";
        DaoSession session = WeatherCityDaoManager.getInstance().getDaoSession();
        Cursor cursor = session.getDatabase().rawQuery(sql, new String[]{latitude + "", latitude + "", longitude + ""});
        if (cursor != null && cursor.moveToFirst()) {
            String areaCode = cursor.getString(cursor.getColumnIndex("areaCode"));
            List<WeatherCity> list = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCode.eq(areaCode)).list();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            cursor.close();
        }
        return null;
    }

    public static WeatherCity findLocationWeatherCity2(double longitude, double latitude) {
        String sql = "SELECT areaCode, ((? - latitude) * (? - latitude) + (? - longitude) * (? - longitude)) AS distance FROM weather_city ORDER BY distance ASC LIMIT 0, 1";
        DaoSession session = WeatherCityDaoManager.getInstance().getDaoSession();
        Cursor cursor = session.getDatabase().rawQuery(sql, new String[]{latitude + "", latitude + "", longitude + "", longitude + ""});
        if (cursor != null && cursor.moveToFirst()) {
            String areaCode = cursor.getString(cursor.getColumnIndex("areaCode"));
            List<WeatherCity> list = WeatherCityDaoManager.getInstance().getDaoSession().getWeatherCityDao().queryBuilder().where(WeatherCityDao.Properties.AreaCode.eq(areaCode)).list();
            if (list != null && list.size() > 0) {
                WeatherCity weatherCity = list.get(0);
                weatherCity.isPosition = true;
                return weatherCity;
            }
            cursor.close();
        }
        return null;
    }
}
