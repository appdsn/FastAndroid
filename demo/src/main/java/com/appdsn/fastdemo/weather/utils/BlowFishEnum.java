package com.appdsn.fastdemo.weather.utils;

import java.util.HashMap;
import java.util.Map;

public enum BlowFishEnum {
    /**
     * 单例
     */
    INSTANCE;

    public static final Map<String, BlowFish> STRING_BLOW_FISH_MAP = new HashMap<>();

    public BlowFish getBlowFish() {
        return getBlowFish(null);
    }

    public BlowFish getBlowFish(String password) {
        //如果密码为空，使用默认的密码
        if (password == null) {
            password = "@3*Sx0,`j[/la!";
        }
        if (STRING_BLOW_FISH_MAP.get(password) != null) {
            return STRING_BLOW_FISH_MAP.get(password);
        }
        BlowFish blowFish = new BlowFish(password);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            STRING_BLOW_FISH_MAP.putIfAbsent(password, blowFish);
//        } else {
        STRING_BLOW_FISH_MAP.put(password, blowFish);
//        }
        return blowFish;
    }
}
