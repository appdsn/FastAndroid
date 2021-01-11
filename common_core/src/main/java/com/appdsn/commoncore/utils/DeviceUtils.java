package com.appdsn.commoncore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Desc:设备信息，唯一识别码
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 17:50
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class DeviceUtils {
    private static String sIMEI;
    private static String sMAC;
    private static String sAndroidId;
    private static String sBrand;
    private static String sModel;
    private volatile static String sUDID;
    private final static String KEY_DEVICE_ID = "KEY_UDID";

    private DeviceUtils() {
    }

    /**
     * Return whether device is rooted.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
                "/system/sbin/", "/usr/bin/", "/vendor/bin/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    public static String getSDKVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    public static int getSDKVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static String getBrand() {
        if (sBrand == null) {
            sBrand = Build.BRAND;
        }
        return sBrand;
    }

    /**
     * Return the manufacturer of the product/hardware.
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        if (sModel != null) {
            return sModel;
        }
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        sModel = model;
        return model;
    }

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        if (sAndroidId != null) {
            return sAndroidId;
        }
        String id = Settings.Secure.getString(
                ContextUtils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if ("9774d56d682e549c".equals(id) || id == null) {
            id = "";
        }
        sAndroidId = id;
        return id;
    }

    /**
     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    public static String[] getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS;
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return new String[]{Build.CPU_ABI};
        }
    }

    /**
     * Return whether device is emulator.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmulator() {
        boolean checkProperty = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) {
            return true;
        }

        String operatorName = "";
        TelephonyManager tm = (TelephonyManager) ContextUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
                operatorName = name;
            }
        }
        boolean checkOperatorName = operatorName.toLowerCase().equals("android");
        if (checkOperatorName) {
            return true;
        }

        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        boolean checkDial = intent.resolveActivity(ContextUtils.getContext().getPackageManager()) == null;
        if (checkDial) {
            return true;
        }

//        boolean checkDebuggerConnected = Debug.isDebuggerConnected();
//        if (checkDebuggerConnected) return true;

        return false;
    }

    /**
     * Return the unique device id.
     * <p>If the version of SDK is greater than 28, it will return an empty string.</p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        if (!TextUtils.isEmpty(sIMEI)) {
            return sIMEI;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            TelephonyManager tm = (TelephonyManager) ContextUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            if (!TextUtils.isEmpty(deviceId)) {
                sIMEI = deviceId;
                return deviceId;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String imei = tm.getImei();
                if (!TextUtils.isEmpty(imei)) {
                    sIMEI = imei;
                    return imei;
                }
                String meid = tm.getMeid();
                sIMEI = meid;
                return TextUtils.isEmpty(meid) ? "" : meid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the serial of device.
     *
     * @return the serial of device
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getSerial() {
        try {
            String serial = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            if (serial != null && serial.equals(Build.UNKNOWN)) {
                serial = "";
            }
            return serial;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the IMSI.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMSI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMSI() {
        try {
            TelephonyManager tm = (TelephonyManager) ContextUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />},
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET, CHANGE_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddress((String[]) null);
        if (!macAddress.equals("") || getWifiEnabled()) {
            return macAddress;
        }
        setWifiEnabled(true);
        setWifiEnabled(false);
        return getMacAddress((String[]) null);
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param excepts 排除之外的，默认是02:00:00:00:00:00
     * @return the MAC address
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static String getMacAddress(final String... excepts) {
        String macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByWifiInfo();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        return "";
    }

    private static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) ContextUtils.getContext().getSystemService(Context.WIFI_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isWifiEnabled();
    }

    /**
     * Enable or disable wifi.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    private static void setWifiEnabled(final boolean enabled) {
        try {
            @SuppressLint("WifiManagerLeak")
            WifiManager manager = (WifiManager) ContextUtils.getContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return;
            }
            if (enabled == manager.isWifiEnabled()) {
                return;
            }
            manager.setWifiEnabled(enabled);
        } catch (Exception ignore) {
        }
    }

    private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (excepts == null || excepts.length == 0) {
            return !"02:00:00:00:00:00".equals(address);
        }
        for (String filter : excepts) {
            if (address.equals(filter)) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private static String getMacAddressByWifiInfo() {
        try {
            final WifiManager wifi = (WifiManager) ContextUtils.getContext()
                    .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                final WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    return info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) {
                            return inetAddress;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Return the unique device id.
     * <pre>{1}{UUID(deviceId)}</pre>
     * <pre>{2}{UUID(macAddress )}</pre>
     * <pre>{3}{UUID(androidId )}</pre>
     * <pre>{4}{UUID(serial )}</pre>
     * <pre>{0}{UUID(random    )}</pre>
     *
     * @return the unique device id
     */
    @SuppressLint("MissingPermission")
    public static String getUDID() {
        if (sUDID == null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextUtils.getContext());
            String id = sharedPref.getString(KEY_DEVICE_ID, null);
            if (id != null) {
                sUDID = id;
                return sUDID;
            }
            try {
                String deviceId = getDeviceId();
                if (!TextUtils.isEmpty(deviceId)) {
                    return saveUdid("1", deviceId);
                }

                String macAddress = getMacAddress();
                if (!TextUtils.isEmpty(macAddress)) {
                    return saveUdid("2", macAddress);
                }

                String androidId = getAndroidID();
                if (!TextUtils.isEmpty(androidId)) {
                    return saveUdid("3", androidId);
                }

                String serial = getSerial();
                if (!TextUtils.isEmpty(serial)) {
                    return saveUdid("4", serial);
                }

            } catch (Exception ignore) {
            }

            return saveUdid("0", "");
        }
        return sUDID;
    }

    private static String saveUdid(String prefix, String id) {
        if (id.equals("")) {
            id = readUdidFromFile();
            if (!TextUtils.isEmpty(id)) {
                sUDID = id;
            } else {
                sUDID = prefix + UUID.randomUUID().toString().replace("-", "");
            }
        } else {
            sUDID = prefix + UUID.nameUUIDFromBytes(id.getBytes()).toString().replace("-", "");
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextUtils.getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_DEVICE_ID, sUDID);
        editor.apply();
        writeUdidToFile(sUDID);
        return sUDID;
    }

    private synchronized static String readUdidFromFile() {
        RandomAccessFile f = null;
        try {
            File udidFile = new File(Environment.getExternalStorageDirectory(), ".common_udid");
            if (udidFile.exists()) {
                f = new RandomAccessFile(udidFile, "r");
                byte[] bytes = new byte[(int) f.length()];
                f.readFully(bytes);
                return new String(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static void writeUdidToFile(String udid) {
        FileOutputStream out = null;
        try {
            File udidFile = new File(Environment.getExternalStorageDirectory(), ".common_udid");
            out = new FileOutputStream(udidFile);
            out.write(udid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
