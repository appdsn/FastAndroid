package com.appdsn.commonbase.utils;

import android.text.TextUtils;

import com.appdsn.commoncore.utils.ContextUtils;
import com.leon.channel.helper.ChannelReaderUtil;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/3/18 20:18
 * @Copyright: Copyright (c) 2016-2020
 * @Company:
 * @Version: 1.0
 */
public class ChannelUtils {
    /**
     * 获取市场渠道名
     *
     * @return channel
     */
    public static String getChannelId() {
        String channel = "";
        try {
            channel = ChannelReaderUtil.getChannel(ContextUtils.getContext());
        } catch (Exception e) {
        }
        return !TextUtils.isEmpty(channel) ? channel : "official";
    }
}

