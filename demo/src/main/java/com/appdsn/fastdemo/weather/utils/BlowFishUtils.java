package com.appdsn.fastdemo.weather.utils;


/**
 * Blowfish加密算法的特点：
 * <p>
 * 1. 对称加密，即加密的密钥和解密的密钥是相同的；
 * 2. 每次加密之后的结果是不同的；
 * 3. 可逆的；
 * 4. 速度快，加密和解密的过程基本上由ADD和XOR指令运算组成；
 * 5. 免费，任何人都可以免费使用不需要缴纳版权费；
 * 6. BlowFish 每次只能加密和解密8字节数据；
 */
public class BlowFishUtils {

    /**
     * 加密
     */
    public static String encryptString(String password, String data) {
        return BlowFishEnum.INSTANCE.getBlowFish(password).encryptString(data);
    }

    /**
     * 解密
     */
    public static String decryptString(String password, String data) {
        return BlowFishEnum.INSTANCE.getBlowFish(password).decryptString(data);
    }

    /**
     * 加密
     */
    public static String encryptString(String data) {
        return BlowFishEnum.INSTANCE.getBlowFish().encryptString(data);
    }

    /**
     * 解密
     */
    public static String decryptString(String data) {
        return BlowFishEnum.INSTANCE.getBlowFish().decryptString(data);
    }

}
