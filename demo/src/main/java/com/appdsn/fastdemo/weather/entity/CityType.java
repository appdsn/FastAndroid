package com.appdsn.fastdemo.weather.entity;


/**
 * 城市等级类型
 */
public interface CityType {
    /**
     * 省份及直辖市，如“湖北省”
     */
    int FIRST_LEVEL = 1;

    /**
     * 地级市，如“武汉市”
     */
    int SECOND_LEVEL = 2;

    /**
     * 县级市/县/区
     */
    int THIRD_LEVEL = 3;

    /**
     * 乡镇/街道
     */
    int FOURTH_LEVEL = 4;

    /**
     * 景区
     */
    int SCENE_LEVEL = 5;

}
