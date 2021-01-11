package com.appdsn.fastdemo.weather.rxhttp;

import rxhttp.wrapper.callback.Function;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/8 13:35
 */
public class CommonResultDecoder implements Function<String, String> {
    //每次请求成功，都会回调这里，并传入请求返回的密文
    @Override
    public String apply(String s) throws Exception {
//        String plaintext = decode(s);   //将密文解密成明文，解密逻辑自己实现
//        return plaintext;    //返回明文
        return s;
    }
}
