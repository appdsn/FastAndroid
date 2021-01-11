package com.appdsn.commoncore.utils;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Desc: 本地数据存储工具类，保存任意类型数据包括集合和自定义实体类等
 * Copyright: Copyright (c) 2016-2020
 * ""
 * Update Comments:
 *
 * @author tiexueshuai
 */
public class SPUtils {

    /**
     * This will init the SPUtils without password protection.
     *
     * @param context is used to instantiate context based objects.
     *                ApplicationContext will be used
     */
    public static void init(Context context) {
        Hawk.init(context).build();
    }

    /**
     * Saves any type including any collection, primitive values or custom objects
     *
     * @param key   is required to differentiate the given data
     * @param value is the data that is going to be encrypted and persisted
     * @return true if the operation is successful. Any failure in any step will return false
     */
    public static <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    /**
     * Gets the original data along with original type by the given key.
     * This is not guaranteed operation since Hawk uses serialization. Any change in in the requested
     * data type might affect the result. It's guaranteed to return primitive types and String type
     *
     * @param key is used to get the persisted data
     * @return the original object
     */
    public static <T> T get(String key) {
        return Hawk.get(key);
    }

    /**
     * Gets the saved data, if it is null, default value will be returned
     *
     * @param key          is used to get the saved data
     * @param defaultValue will be return if the response is null
     * @return the saved object
     */
    public static <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    /**
     * Checks the given key whether it exists or not
     *
     * @param key is the key to check
     * @return true if it exists in the storage
     */
    public static boolean contains(String key) {
        return Hawk.contains(key);
    }

    /**
     * Removes the given key/value from the storage
     *
     * @param key is used for removing related data from storage
     * @return true if delete is successful
     */
    public static boolean delete(String key) {
        return Hawk.delete(key);
    }

    /**
     * Clears the storage, note that crypto data won't be deleted such as salt key etc.
     * Use resetCrypto in order to deleteAll crypto information
     *
     * @return true if deleteAll is successful
     */
    public static boolean deleteAll() {
        return Hawk.deleteAll();
    }

}

