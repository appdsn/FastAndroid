package com.appdsn.commonbase.statistics;

import org.json.JSONException;
import org.json.JSONObject;

/*没有继承BaseAppActivity和BaseAppFragment得页面，需要自定义传递统计字段*/
public enum OtherStatisticsEvent {

    /*举例*/
    example("唯一事件码", "事件名称", "", "");


    OtherStatisticsEvent(String eventCode, String eventName, String sourcePage, String currentPage) {
        this.sourcePage = sourcePage;
        this.currentPage = currentPage;
        this.eventCode = eventCode;
        this.eventName = eventName;
    }

    private String sourcePage;
    private String currentPage;
    private String eventCode;//有需要统计页面显示事件才填
    private String eventName;
    private JSONObject extension;

    public String getSourcePage() {
        return sourcePage;
    }

    public String getcCurrentPage() {
        return currentPage;
    }

    public String getEventCode() {
        return eventCode;
    }

    public JSONObject getExtension() {
        return extension;
    }

    public String getEventName() {
        return eventName;
    }

    public OtherStatisticsEvent setExtension(JSONObject extension) {
        if (extension != null) {
            this.extension = extension;
        }
        return this;
    }

    public OtherStatisticsEvent setExtension(String... extension) {
        if (extension != null && extension.length > 0) {
            JSONObject jObj = new JSONObject();
            for (int i = 0; i < extension.length; i++) {
                try {
                    String key = extension[i++];
                    String value = "";
                    if (i < extension.length) {
                        value = extension[i];
                    }
                    jObj.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            this.extension = jObj;
        }
        return this;
    }

    public OtherStatisticsEvent putExtension(String key, String value) {
        if (extension == null) {
            extension = new JSONObject();
        }
        try {
            extension.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
