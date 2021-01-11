package com.appdsn.library.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/13 18:40
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @Version: 1.0
 */
public class MobLoginInfo implements Serializable {
    public Map<String, String> originData;//原始数据
    public String name;//昵称
    public String gender;//性别 男，女
    public String iconUrl;//用户头像
    public String openid;//微信和QQ
    public String unionid;//微信专用
    public String city;
    public String province;


    @Override
    public String toString() {
        if (originData != null) {
            return originData.toString();
        }
        return "name:" + name + "gender:" + gender + "openid:" + openid
                + "unionid:" + unionid + "city:" + city + "iconUrl:" + iconUrl;
    }
}
