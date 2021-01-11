package com.appdsn.fastdemo.weather.response;


import java.io.Serializable;

public class WeatherBean implements Serializable {

    /**
     * seventyTwoHours : {"areaCode":"101011600","content":"eyJzdGF0dXMiO..."}
     * sixteenDay : {"areaCode":"101020600","content":"","countyName":"H4sIAAAAA.....","living":"H4sIAAAAA.....","serverTime":""}
     * realTime : {"areaCode":"101030300","content":"H4sIAAAAA.....","latitude":"39.717379","longitude":"117.309863"}
     * alertInfo : [{"pub_date":"2019-07-16T22:55:00+08:00","level":"黄色","areCode":"101010300","subtitle":"朝阳雷电黄色预警","subDesc":"朝阳区气象台16日22时55分继续发布雷电黄色预警,预计今天夜间至17日08时，朝阳区有雷阵雨天气，有雷电活动，局地短时雨强较大，请注意防范。","description":"朝阳区气象台16日22时55分继续发布雷电黄色预警,预计今天夜间至17日08时，朝阳区有雷阵雨天气，有雷电活动，局地短时雨强较大，请注意防范。","source":"国家预警中心","title":"朝阳区气象台继续发布雷电黄色预警","type":"雷电","status":"","countyName":"朝阳"}]
     */
    public SeventyTwoHoursBean seventyTwoHours;
    public SixteenDayBean sixteenDay;
    public RealTimeBean realTime;
    public AlertInfo alertInfo;
    public LivingBean living;
    public SeventyTwoHoursBean threeHundredSixtyHours;
    /**
     * 用于标记每次请求刷新结果，true:都请求成功,false:存在一个请求失败
     */
    public boolean combineRequestResult = false;

    public static class SeventyTwoHoursBean implements Serializable {
        /**
         * areaCode : 101011600
         * content : eyJzdGF0dXMiO...
         */
        public String areaCode;
        public String content;
    }

    public static class SixteenDayBean implements Serializable {
        /**
         * areaCode : 101020600
         * content :
         * countyName : H4sIAAAAA.....
         * living : H4sIAAAAA.....
         * serverTime :
         */
        public String areaCode;
        public String content;
        public String countyName;
        public String living;
        public String serverTime;
    }

    public static class RealTimeBean implements Serializable {
        /**
         * areaCode : 101030300
         * content : H4sIAAAAA.....
         * latitude : 39.717379
         * longitude : 117.309863
         */
        public String areaCode;
        public String content;
        public String latitude;
        public String longitude;
    }

    public static class AlertInfo implements Serializable {
        /**
         * content : eyJzdGF0dXMiO...
         */
        public String content;
    }

    public static class LivingBean implements Serializable {

        /**
         * areaCode : c101010200
         * latitude : 39.959912
         * longitude : 116.298056
         * content : "H4sIAAAAAAAAAIVU3XLS"
         */

        public String areaCode;
        public String latitude;
        public String longitude;
        public String content;
    }
}
