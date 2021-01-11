package com.appdsn.commonbase.statistics;

import android.text.TextUtils;

import com.appdsn.commoncore.utils.log.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/10/9 18:02
 */
public class TalkingDataUtils {

    public static StatisticsPage sTopPage;

    public static void setTopStatisticsPage(String sourcePage, String currentPage) {
        sTopPage = new StatisticsPage(sourcePage, currentPage);
    }

    public static void setTopStatisticsPage(StatisticsPage topPage) {
        sTopPage = topPage;
    }

    public static StatisticsPage getTopStatisticsPage() {
        return sTopPage;
    }

    /**
     * 设置全局事件参数
     */
    public static final void setGlobalKV(String key, String value) {
//        TCAgent.setGlobalKV(key, value);
    }

    /**
     * 对非Activity页面，如Fragment、自定义View等非标准页面进行统计,一次成对的 onPageStart -> onPageEnd 调用。
     * 进入页面时调用：比如在onResume方法调用
     */
    public static final void onPageStart(String pageName) {
//        TCAgent.onPageStart(ContextUtils.getContext(), pageName);
    }

    /**
     * 对非Activity页面，如Fragment、自定义View等非标准页面进行统计,一次成对的 onPageStart -> onPageEnd 调用。
     * 离开页面时调用：比如在onPause方法调用
     */
    public static final void onPageEnd(String pageName) {
//        TCAgent.onPageEnd(ContextUtils.getContext(), pageName);
    }

    public static void onEvent(NormalStatisticsEvent event) {
//        NiuStatisticsUtils.click(event);
//        String curPageId = "默认页";
//        if (sTopPage != null) {
//            curPageId = sTopPage.getCurPageId();
//        }
//        if (event != null) {
//            onEvent(curPageId, event.getEventCode(), event.getExtension());
//        }
    }

    /**
     * sdk 点击埋点（click类型）
     *
     * @param eventCode   事件code
     * @param currentPage 当前页面
     * @param extension   额外参数
     */
    public static void onEvent(String currentPage, String eventCode, JSONObject extension) {
        try {
            Map<String, Object> extraMap = jsonToMap(extension);
            if (extraMap == null) {
                extraMap = new HashMap<>();
            }
            if (!TextUtils.isEmpty(currentPage)) {
                extraMap.put("current_page_id", currentPage);
            }

            LogUtils.mix("click事件", "eventCode => " + eventCode, "extension => " + extraMap.toString());
            onEvent(eventCode, extraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用如下自定义事件统计接口时(注意：需要先在【友盟+】网站注册事件ID)
     *
     * @param eventId 不能为null，也不能为空字符串"", 且参数长度不能超过128个字符。
     *                不能和以下保留字冲突： "id", "ts", "du", "$stfl", "$!deeplink", "$!link"
     *                label   不能为null，也不能为空字符串"" 参数label长度不能超过256个字符
     */
    private static final void onEvent(String eventId) {
        if (TextUtils.isEmpty(eventId)) {
            return;
        }
//        TCAgent.onEvent(ContextUtils.getContext(), eventId, eventId + "_label");
    }

    /**
     * 调用如下自定义事件统计接口时(注意：需要先在【友盟+】网站注册事件ID)
     *
     * @param extraData 参数map最多支持100个K-V值。参数map不能为null，且map不能为空map
     *                  map中的key不能和以下保留字冲突： "id", "ts", "du", "$stfl", "$!deeplink", "$!link"
     */
    private static final void onEvent(String eventId, Map<String, Object> extraData) {
        if (TextUtils.isEmpty(eventId)) {
            return;
        }
        if (extraData == null || extraData.isEmpty()) {
            onEvent(eventId);
        } else {
//            TCAgent.onEvent(ContextUtils.getContext(), eventId, eventId + "_label", extraData);
        }
    }

    private static Map jsonToMap(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> data = new HashMap<>();

        Iterator<String> it = obj.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = it.next();
            String value = obj.optString(key, "");
            data.put(key, value);
        }
        return data;
    }
}
