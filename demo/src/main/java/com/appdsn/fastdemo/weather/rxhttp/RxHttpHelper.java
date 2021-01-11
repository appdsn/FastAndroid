package com.appdsn.fastdemo.weather.rxhttp;

import android.text.TextUtils;

import com.appdsn.commoncore.http.callback.ApiCallback;
import com.appdsn.commoncore.http.exception.ApiException;
import com.appdsn.commoncore.utils.GsonUtils;
import com.appdsn.commoncore.utils.ParseUtils;
import com.appdsn.fastdemo.weather.AttentionCityHelper;
import com.appdsn.fastdemo.weather.entity.AttentionCity;
import com.appdsn.fastdemo.weather.entity.AttentionResponse;
import com.appdsn.fastdemo.weather.response.AttentionResponseEntity;
import com.appdsn.fastdemo.weather.response.WeatherBean;
import com.appdsn.fastdemo.weather.utils.GZipUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/8 11:50
 */
public class RxHttpHelper {
    public static void init() {
        RxHttp.setDebug(true);
        RxHttp.setOnParamAssembly(new CommonParamAssembly());
        RxHttp.setResultDecoder(new CommonResultDecoder());
    }

    public static void requestAttentionCityWeather(ApiCallback<List<AttentionCity>> callback) {
        List<AttentionCity> attentionCityList = AttentionCityHelper.getAttentionCityList();
        Map<String, AttentionCity> areaCodeMap = new HashMap<>(); //key存放areaCode
        StringBuffer areaCodesBuffer = new StringBuffer();
        areaCodesBuffer.setLength(0);
        boolean isFirst = true;

        for (AttentionCity weatherEntity : attentionCityList) {
            if (weatherEntity == null) {
                continue;
            }

            String areaCode = "" + weatherEntity.getAreaCode();
            if (TextUtils.isEmpty(areaCode)) {
                return;
            }

            if (isFirst) {
                isFirst = false;
            } else {
                areaCodesBuffer.append(",");
            }
            areaCodesBuffer.append(areaCode);
            areaCodeMap.put(areaCode, weatherEntity);
        }

        // 输出数据库中areaCode
        String areaCodesStr = areaCodesBuffer.toString();
        if (TextUtils.isEmpty(areaCodesStr)) {
            return;
        }
        String encodeStr = URLEncoder.encode(areaCodesStr);

        RxHttp.get("/data/today")       //发送表单形式的post请求
                .add("areaCode", encodeStr)
                .asResponse(AttentionResponse.class)
                .observeOn(AndroidSchedulers.mainThread()) //指定在主线程回调
                .subscribe(attentionResponse -> {//成功回调
                    if (attentionResponse == null) {
                        //返回缓存数据
                        return;
                    }
                    String json = GZipUtils.decompress(attentionResponse.today);
                    List<AttentionResponseEntity> attentionList = GsonUtils.fromJson(json, GsonUtils.getListType(AttentionResponseEntity.class));

                    if (attentionList != null && !attentionList.isEmpty()) {
                        for (AttentionResponseEntity response : attentionList) {
                            if (response == null) {
                                continue;
                            }

                            AttentionCity attentionCity = areaCodeMap.get(response.areaCode);
                            if (attentionCity == null) {
                                continue;
                            }

                            attentionCity.setLowTemperature("" + response.getTemperMin());
                            attentionCity.setHighTemperature("" + response.getTemperMax());
                            attentionCity.setWeatherDate(response.date);

                            attentionCity.setSkyCondition(response.getSkyConValue());
                            attentionCity.setSunRiseTime(ParseUtils.parseLong(response.getSunrise()));
                            attentionCity.setSunSetTime(ParseUtils.parseLong(response.getSunset()));
                        }
                    }
                    AttentionCityHelper.saveOrUpdateAttentionCityWeather(attentionCityList);

                    if (callback != null) {
                        callback.onSuccess(attentionCityList);
                    }
                }, throwable -> {
                    //失败回调
                    if (callback != null) {
                        callback.onFailure(new ApiException("-1", throwable.getMessage()), "-1", throwable.getMessage());
                    }
                });

    }


    public static void requestWeatherDataCombine(AttentionCity weatherCity, ApiCallback<List<AttentionCity>> callback) {
        String keys = "sixteenDay,seventyTwoHours,alertInfo,living";
        String keysReal = "realTime";
        String areaCode = weatherCity.getAreaCode();
        String cityName = weatherCity.getAreaName();

        RxHttp.get("/data/baseGroup")       //发送表单形式的post请求
                .add("areaCode", areaCode)
                .add("keys", keys)
                .asResponse(WeatherBean.class)
                .observeOn(AndroidSchedulers.mainThread()) //指定在主线程回调
                .subscribe(weatherBeanGroup -> {//成功回调

                    RxHttp.get("/data/baseGroup")       //发送表单形式的post请求
                            .add("areaCode", areaCode)
                            .add("keys", keysReal)
                            .asResponse(WeatherBean.class)
                            .observeOn(AndroidSchedulers.mainThread()) //指定在主线程回调
                            .subscribe(weatherBeanReal -> {//成功回调
                                if (weatherBeanGroup != null && weatherBeanReal != null) {
                                    weatherBeanGroup.realTime = weatherBeanReal.realTime;
                                }
                                parseWeatherData(weatherBeanGroup, areaCode, cityName);
                            }, throwable -> {
                                //失败回调
                                if (callback != null) {
                                    callback.onFailure(new ApiException("-1", throwable.getMessage()), "-1", throwable.getMessage());
                                }
                            });

                }, throwable -> {
                    //失败回调
                    if (callback != null) {
                        callback.onFailure(new ApiException("-1", throwable.getMessage()), "-1", throwable.getMessage());
                    }
                });

    }

    /**
     * 请求天气数据
     * 72小时天气接口+16天天气接口+实时预告接口 + 查询恶劣天气接口
     */
    public static void requestWeatherData(AttentionCity weatherCity, ApiCallback<List<AttentionCity>> callback) {
        String keys = "sixteenDay,seventyTwoHours,alertInfo,living";
        String areaCode = weatherCity.getAreaCode();
        String cityName = weatherCity.getAreaName();

        RxHttp.get("/data/baseGroup")       //发送表单形式的post请求
                .add("areaCode", areaCode)
                .add("keys", keys)
                .asResponse(WeatherBean.class)
                .observeOn(AndroidSchedulers.mainThread()) //指定在主线程回调
                .subscribe(weatherBean -> {//成功回调
                    parseWeatherData(weatherBean, areaCode, cityName);
                }, throwable -> {
                    //失败回调
                    if (callback != null) {
                        callback.onFailure(new ApiException("-1", throwable.getMessage()), "-1", throwable.getMessage());
                    }
                });
    }

    public static void requestRealTimeData(AttentionCity weatherCity, ApiCallback<List<AttentionCity>> callback) {
        String keys = "realTime";
        String areaCode = weatherCity.getAreaCode();
        String cityName = weatherCity.getAreaName();

        RxHttp.get("/data/baseGroup")       //发送表单形式的post请求
                .add("areaCode", areaCode)
                .add("keys", keys)
                .asResponse(WeatherBean.class)
                .observeOn(AndroidSchedulers.mainThread()) //指定在主线程回调
                .subscribe(weatherBean -> {//成功回调
                    parseWeatherData(weatherBean, areaCode, cityName);
                }, throwable -> {
                    //失败回调
                    if (callback != null) {
                        callback.onFailure(new ApiException("-1", throwable.getMessage()), "-1", throwable.getMessage());
                    }
                });
    }

    private static void parseWeatherData(WeatherBean weatherBean, String areaCode, String cityName) {

    }
}
