package com.appdsn.commoncore.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/9 22:41
 */
public class ConvertUtils {
    /**
     * Bytes to Serializable.
     */
    public static Object bytes2Object(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Serializable to bytes.
     */
    public static byte[] serializable2Bytes(final Serializable serializable) {
        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Bytes to Parcelable
     */
    public static <T> T bytes2Parcelable(final byte[] bytes,
                                         final Parcelable.Creator<T> creator) {
        if (bytes == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

    /**
     * Parcelable to bytes.
     */
    public static byte[] parcelable2Bytes(final Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    /**
     * Bytes to JSONArray.
     */
    public static JSONArray bytes2JSONArray(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new JSONArray(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSONArray to bytes.
     */
    public static byte[] jsonArray2Bytes(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        return jsonArray.toString().getBytes();
    }

    /**
     * Bytes to JSONObject.
     */
    public static JSONObject bytes2JSONObject(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new JSONObject(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSONObject to bytes.
     */
    public static byte[] jsonObject2Bytes(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        return jsonObject.toString().getBytes();
    }

    /**
     * Bytes to string.
     */
    public static String bytes2String(final byte[] bytes) {
        return bytes2String(bytes, "");
    }

    /**
     * Bytes to string.
     */
    public static String bytes2String(final byte[] bytes, final String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(bytes);
        }
    }

    /**
     * String to bytes.
     */
    public static byte[] string2Bytes(final String string) {
        return string2Bytes(string, "");
    }

    /**
     * String to bytes.
     */
    public static byte[] string2Bytes(final String string, final String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string.getBytes();
        }
    }

    private static String getSafeCharset(String charsetName) {
        String cn = charsetName;
        if (TextUtils.isEmpty(charsetName) || !Charset.isSupported(charsetName)) {
            cn = "UTF-8";
        }
        return cn;
    }
}
