package com.appdsn.commonbase.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.appdsn.commonbase.utils.ChannelUtils;
import com.appdsn.commoncore.utils.AppUtils;
import com.appdsn.commoncore.utils.ContextUtils;
import com.appdsn.commoncore.utils.TimeUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tie on 2017/5/17.
 */
public class RequestParamInterceptor implements Interceptor {
    private static final String TAG = "RequestParamInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.addHeader("uid", UUID.randomUUID().toString());
//        requestBuilder.addHeader("request-agent", "1");//1：android、2：iOS、3：PC、4、H5、5：wechat
//        requestBuilder.addHeader("device-id", AndroidUtil.getUdid());
        requestBuilder.addHeader("source", "3");//1—iPhone；2—iPad；3—Android；4—微信公众号；5—H5 6—小程序 7-服务端调用
        requestBuilder.addHeader("osversion", Build.VERSION.SDK_INT + "");
        requestBuilder.addHeader("phone-model", Build.MODEL);
        requestBuilder.addHeader("channel", ChannelUtils.getChannelId());
        requestBuilder.addHeader("appversion", AppUtils.getVersionCode() + "");
//        requestBuilder.addHeader("app-name", AndroidUtil.getAppNum());
//        requestBuilder.addHeader("app-id", BuildConfig.API_APPID);
        requestBuilder.addHeader("timestamp", TimeUtils.formatDate(System.currentTimeMillis()));
        requestBuilder.addHeader("signature", "");
//        requestBuilder.addHeader("customer-id", AndridUtil.getCustomerId());
        SharedPreferences sp = ContextUtils.getContext().getSharedPreferences("get", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        requestBuilder.addHeader("user-token", token);
        requestBuilder.addHeader("biz-code", "get");//业务线


        if (original.body() instanceof FormBody) {

            FormBody oidFormBody = (FormBody) original.body();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < oidFormBody.size(); i++) {
                map.put(oidFormBody.encodedName(i), oidFormBody.value(i));
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

            requestBuilder.method(original.method(), requestBody);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    /**
     * 通过HmacSHA256进行哈希
     *
     * @param stringToSign
     * @param appSecret
     * @return
     */
    public String hashByHmacSHA256(String stringToSign, String appSecret) {
        String signature;
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = appSecret.getBytes(Charset.forName("UTF-8"));
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            byte[] data = hmacSha256.doFinal(stringToSign.getBytes(Charset.forName("UTF-8")));
            StringBuffer buffer = new StringBuffer();
            for (byte item : data) {
                buffer.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            signature = buffer.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("通过HmacSHA256进行哈希出现异常：" + e.getMessage());
        }
        return signature;
    }
}

