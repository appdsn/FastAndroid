package com.appdsn.fastdemo.weather.response;

import com.appdsn.commoncore.utils.TimeUtils;

public class AttentionResponseEntity {

    /**
     * areaCode : c211302007
     * astro : {"date":1583424000000,"sunrise":1583447040000,"sunset":1583488440000}
     * date : 1583424000000
     * skyCon : {"date":1583424000000,"dateTime":1583424000000,"desc":"轻度雾霾","descOfDay":"轻度雾霾","descOfNight":"小雪","value":"LIGHT_HAZE","valueOfDay":"LIGHT_HAZE","valueOfNight":"LIGHT_SNOW"}
     * temperature : {"avg":2.89,"date":1583424000000,"max":15,"min":-2}
     * wind : {"avg":{"direction":202.56,"speed":12.25},"date":1583424000000,"directionOfDesc":"东南风","max":{"direction":163,"speed":25.2},"min":{"direction":210.64,"speed":5.3},"speedOfDesc":"1-4级","value":{"direction":0,"speed":0}}
     */

    public String areaCode;
    public AstroBean astro;
    public long date;
    public SkyConBean skyCon;
    public TemperatureBean temperature;
    public WindBean wind;

    public String getSunrise() {
        if (astro == null) {
            return "";
        }
        return "" + astro.sunrise;
    }

    public String getSunset() {
        if (astro == null) {
            return "";
        }
        return "" + astro.sunset;
    }

    public String getSkyConValue() {
        if (skyCon == null) {
            return "";
        }
        return skyCon.value;
    }

    /**
     * 获取日期
     *
     * @return yyyy-MM-dd HH:mm
     */
    public String getDate() {
        return TimeUtils.formatDate(date);
    }

    /**
     * 最高温度
     *
     * @return
     */
    public int getTemperMax() {
        if (temperature == null) {
            return 0;
        }
        return (int) Math.round(temperature.max);
    }

    /**
     * 最低温度
     *
     * @return
     */
    public int getTemperMin() {
        if (temperature == null) {
            return 0;
        }
        return (int) Math.round(temperature.min);
    }

    public static class AstroBean {
        /**
         * date : 1583424000000
         * sunrise : 1583447040000
         * sunset : 1583488440000
         */

        public long date;
        public long sunrise;
        public long sunset;
    }

    public static class SkyConBean {
        /**
         * date : 1583424000000
         * dateTime : 1583424000000
         * desc : 轻度雾霾
         * descOfDay : 轻度雾霾
         * descOfNight : 小雪
         * value : LIGHT_HAZE
         * valueOfDay : LIGHT_HAZE
         * valueOfNight : LIGHT_SNOW
         */

        public long date;
        public long dateTime;
        public String desc;
        public String descOfDay;
        public String descOfNight;
        public String value;
        public String valueOfDay;
        public String valueOfNight;
    }

    public static class TemperatureBean {
        /**
         * avg : 2.89
         * date : 1583424000000
         * max : 15.0
         * min : -2.0
         */

        public double avg;
        public long date;
        public double max;
        public double min;
    }

    public static class WindBean {
        /**
         * avg : {"direction":202.56,"speed":12.25}
         * date : 1583424000000
         * directionOfDesc : 东南风
         * max : {"direction":163,"speed":25.2}
         * min : {"direction":210.64,"speed":5.3}
         * speedOfDesc : 1-4级
         * value : {"direction":0,"speed":0}
         */

        public AvgBean avg;
        public long date;
        public String directionOfDesc;
        public MaxBean max;
        public MinBean min;
        public String speedOfDesc;
        public ValueBean value;

        public static class AvgBean {
            /**
             * direction : 202.56
             * speed : 12.25
             */

            public double direction;
            public double speed;
        }

        public static class MaxBean {
            /**
             * direction : 163.0
             * speed : 25.2
             */

            public double direction;
            public double speed;
        }

        public static class MinBean {
            /**
             * direction : 210.64
             * speed : 5.3
             */

            public double direction;
            public double speed;
        }

        public static class ValueBean {
            /**
             * direction : 0.0
             * speed : 0.0
             */

            public double direction;
            public double speed;
        }
    }

    @Override
    public String toString() {
        return "AttentionEntity{" +
                "areaCode='" + areaCode + '\'' +
                ", astro=" + astro +
                ", date=" + date +
                ", skyCon=" + skyCon +
                ", temperature=" + temperature +
                ", wind=" + wind +
                '}';
    }
}
