package com.appdsn.fastdemo.weather.rxhttp;


import android.os.Build;

import com.appdsn.commoncore.utils.DeviceUtils;
import com.appdsn.fastdemo.weather.utils.BlowFishUtils;

import java.util.Locale;
import java.util.TimeZone;

import rxhttp.wrapper.callback.Function;
import rxhttp.wrapper.param.Param;

/**
 * Desc:添加公共请求头
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/8 11:51
 */
public class CommonParamAssembly implements Function<Param, Param> {
    private static String PRODUCT_ID = "132";//牛数埋点id
    /**
     * Android: 0 IOS: 1 PC:2 H5:3
     */
    public static final String OS_VERSION = "0";

    /**
     * Android、iOS、PC、H5、wechat
     */
    public static final String DEVICE_TYPE = "Android";

    @Override
    public Param apply(Param param) throws Exception {
        try {
            long timeStamp = System.currentTimeMillis();
            param.addHeader("Domain-Name", "weather"); //必传
            param.addHeader("Content-Type", "application/json;charset=utf-8"); //必传,上下文类型
            param.addHeader("request-id", getRequestId(timeStamp)); //必传，全局唯一ID
            param.addHeader("deviceType", DEVICE_TYPE); //必传，请求来源，
            param.addHeader("os-version", OS_VERSION); //必传，手机操作系统
            param.addHeader("uuid", DeviceUtils.getUDID()); //必传，手机的硬件设备的唯一ID
            param.addHeader("sdk-version", DeviceUtils.getSDKVersionCode() + ""); //必传，手机SDK版本号
            param.addHeader("phone-model", DeviceUtils.getModel()); //必传，手机型号
            try {
                String channel = getChannel();
                //必传，上传渠道号
                param.addHeader("channel", channel);
                String parentChannel = channel;
                // wt_360,jwt_360,fwt_360 特殊处理
                if (!"wt_360".equals(channel) && !"jwt_360".equals(channel) && !"fwt_360".equals(channel)) {
                    parentChannel = channel.replaceAll("[^\\D.]*", "");
                }
                // 必传，父级别渠道号
                param.addHeader("channel-parent", parentChannel);
                // 必传，手机系统版本
                String releaseVersion = Build.VERSION.RELEASE;
                param.addHeader("phone-version", "" + releaseVersion);
                String releaseParenetVersion = releaseVersion;
                int index = releaseVersion.indexOf(".");
                if (index != -1) {
                    releaseParenetVersion = releaseVersion.substring(0, index);
                }
                // 必传，手机系统版本
                param.addHeader("phone-version-parent", releaseParenetVersion + ".0");
            } catch (Exception e) {
            }
            param.addHeader("version", getVersionName()); //必传，应用版本名，比如：1.0.0
            param.addHeader("versionCode", getVersionCode()); //必传，应用版本号，比如：3

            param.addHeader("app-name-code", "3"); //必传，1-即刻天气，2-知心天气，3-马甲X
            param.addHeader("source", "3");

            param.addHeader("timestamp", timeStamp + ""); //必传，请求时间戳毫秒值
            param.addHeader("GMT-TimeZone", getGmtTimeZone() + ""); //必传，请求传时区

            param.addHeader("package-name", getPackageName()); //可选
            param.addHeader("app-id", PRODUCT_ID);  //可选,APP的唯一标识,由服务端提供，用于做安全控制

            param.addHeader("UserId", "");
            param.addHeader("customer-id", "");
            param.addHeader("sign", "");
            param.addHeader("access-token", "");
            param.addHeader("language", getSystemLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    /**
     * @param timeStamp
     * @return
     */
    private static String getRequestId(long timeStamp) {
        String pwd = PRODUCT_ID + "$0";
        String data = PRODUCT_ID + "$0" + "$" + timeStamp;
        String result = BlowFishUtils.encryptString(pwd, data);
        return result;
    }

    /**
     * 获取时区
     *
     * @return
     */
    public static String getGmtTimeZone() {
        int timeZone = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / (3600 * 1000);
        return String.valueOf(timeZone);
    }

    public static String getSystemLanguage() {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*todo */
    private static String getChannel() {

        return "fwt_test";
    }

    /*todo */
    private static String getPackageName() {

        return "com.yitong.weather";
    }

    /*todo */
    private static String getVersionName() {

        return "3.0.0_dev_20200907";
    }

    /*todo */
    private static String getVersionCode() {

        return "25";
    }


}
