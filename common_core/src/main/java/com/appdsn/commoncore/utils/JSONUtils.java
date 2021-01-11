package com.appdsn.commoncore.utils;

import android.text.TextUtils;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON数据常用操作
 */
public class JSONUtils {

    public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static long getLong(String jsonData, String key, long defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getLong(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static int getInt(String jsonData, String key, int defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getInt(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static double getDouble(String jsonData, String key, double defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getDouble(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static String getString(String jsonData, String key, String defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getString(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static String[] getStringArray(JSONObject jsonObject, String key, String[] defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                String[] value = new String[statusArray.length()];
                for (int i = 0; i < statusArray.length(); i++) {
                    value[i] = statusArray.getString(i);
                }
                return value;
            }
        } catch (JSONException e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static String[] getStringArray(String jsonData, String key, String[] defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringArray(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static List<String> getStringList(JSONObject jsonObject, String key, List<String> defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < statusArray.length(); i++) {
                    list.add(statusArray.getString(i));
                }
                return list;
            }
        } catch (JSONException e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static List<String> getStringList(String jsonData, String key, List<String> defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringList(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultValue) {
        if (jsonObject == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String jsonData, String key, boolean defaultValue) {
        if (TextUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getBoolean(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static Map<String, String> getMap(JSONObject jsonObject, String key) {
        return parseKeyAndValueToMap(JSONUtils.getString(jsonObject, key, null));
    }

    public static Map<String, String> getMap(String jsonData, String key) {

        if (jsonData == null) {
            return null;
        }
        if (jsonData.length() == 0) {
            return new HashMap<String, String>();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getMap(jsonObject, key);
        } catch (JSONException e) {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if (sourceObj == null) {
            return null;
        }

        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        for (Iterator iter = sourceObj.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            if (keyAndValueMap != null && !TextUtils.isEmpty(key)) {
                keyAndValueMap.put(key, getString(sourceObj, key, ""));
            }
        }
        return keyAndValueMap;
    }

    private static Map<String, String> parseKeyAndValueToMap(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(source);
            return parseKeyAndValueToMap(jsonObject);
        } catch (JSONException e) {
            return null;
        }
    }

    public static final JSONObject build(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return new JSONObject();
        } else {
            try {
                return new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }

    /**
     * 合并JSONObject
     *
     * @param source
     * @param dest   最终要合并的对象
     */
    public static void mergeJSONObject(JSONObject source, JSONObject dest) {
        try {
            Iterator<String> superPropertiesIterator = source.keys();
            while (superPropertiesIterator.hasNext()) {
                String key = superPropertiesIterator.next();
                Object value = source.get(key);
                dest.put(key, value);
            }
        } catch (Exception e) {
        }
    }

    public static String buildRequestParams(Pair<String, String>... params) {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Pair<String, String> pair : params) {
                jsonObject.put(pair.first, pair.second);
            }
        } catch (Exception e) {
        }
        return jsonObject.toString();
    }
}
