package com.appdsn.commoncore.utils;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 获取应用或者安装包签名信息
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 17:33
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class SignatureUtils {
    private static final String SYSTEM_PACKAGE_NAME = "android";

    /**
     * 说明：根据apk文件获取apk签名
     */
    public static String getApkSignature(File file) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            JarEntry je = jarFile.getJarEntry("AndroidManifest.xml");
            byte[] readBuffer = new byte[8192];
            Certificate[] certs = loadCertificates(jarFile, je, readBuffer);
            if (certs == null || certs.length <= 0) {
                return "";
            }
            Signature s = new Signature(certs[0].getEncoded());
            return bytes2HexString(hashTemplate(s.toByteArray(), "MD5"))
                    .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
        } catch (Exception ex) {
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * Return the application's signature.
     *
     * @return the application's signature
     */
    public static Signature[] getAppSignature() {
        return getAppSignature(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's signature.
     *
     * @return the application's signature
     */
    public static Signature[] getSystemSignature() {
        return getAppSignature(SYSTEM_PACKAGE_NAME);
    }

    /**
     * Return the application's signature.
     *
     * @param packageName The name of the package.
     * @return the application's signature
     */
    public static Signature[] getAppSignature(final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1() {
        return getAppSignatureSHA1(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1(final String packageName) {
        return getAppSignatureHash(packageName, "SHA1");
    }

    /**
     * Return the application's signature for SHA256 value.
     *
     * @return the application's signature for SHA256 value
     */
    public static String getAppSignatureSHA256() {
        return getAppSignatureSHA256(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's signature for SHA256 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for SHA256 value
     */
    public static String getAppSignatureSHA256(final String packageName) {
        return getAppSignatureHash(packageName, "SHA256");
    }

    /**
     * Return the application's signature for MD5 value.
     *
     * @return the application's signature for MD5 value
     */
    public static String getAppSignatureMD5() {
        return getAppSignatureMD5(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's signature for MD5 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for MD5 value
     */
    public static String getAppSignatureMD5(final String packageName) {
        return getAppSignatureHash(packageName, "MD5");
    }

    public static String parseSignatureMD5(Signature signature) {
        if (signature == null) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature.toByteArray(), "MD5"))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    public static String parseSignatureSHA256(Signature signature) {
        if (signature == null) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature.toByteArray(), "SHA256"))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    public static String parseSignatureSHA1(Signature signature) {
        if (signature == null) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature.toByteArray(), "SHA1"))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    private static String getAppSignatureHash(final String packageName, final String algorithm) {
        if (TextUtils.isEmpty(packageName)) {
            return "";
        }
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return "";
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return "";
        }
        int len = bytes.length;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        if (jarFile == null || readBuffer == null) {
            return null;
        }
        try {
            InputStream is = jarFile.getInputStream(je);
            while (is.read(readBuffer, 0, readBuffer.length) != -1) {
            }
            is.close();
            return je != null ? je.getCertificates() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
