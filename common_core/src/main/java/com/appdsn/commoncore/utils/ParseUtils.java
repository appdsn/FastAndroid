package com.appdsn.commoncore.utils;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/8 21:15
 */
public class ParseUtils {
    public static long parseLong(String data) {
        try {
            return Long.parseLong(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
