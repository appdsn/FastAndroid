package com.appdsn.commonbase.statistics;

import org.json.JSONException;
import org.json.JSONObject;

//继承至BaseAppActivity和BaseAppFragment不需要填pageId，可以自动取到
public enum PageStatisticsEvent {


    addrole_list_page("addrole_list_page", "addrolelist_page_view", "添加角色列表页面浏览"),
    chat_details_page("chat_details_page", "role_page_view", "聊天页"),
    role_setting_page("role_setting_page", "role_setting_page_view", "爱豆设置页"),
    agreement_page1("agreement_page1", "agreement_page1_custom", "协议弹窗确认页"),
    agreement_page2("agreement_page2", "agreement_page2_custom", "协议弹窗挽留页"),
    periodset_page("periodset_page", "periodset_view_page", "经期设置页"),

    informationdetails_page("informationdetails_page", "informationdetails_pageview_page", "新闻详情页"),
    symptom_view_page("symptom_view_page", "symptom_view_page", "症状设置页曝光"),
    login_start_page("login_start_page", "login_start_page_view", "登录页"),

    red_calendar_page("red_calendar_page", "red_calendar_page_view", "记录页"),
    home_page("home_page", "home_page_view", "首页"),
    mine_page("mine_page", "mine_page_view", "我的页面"),
    chat_list_page("chat_list_page", "chatlist_page_view", "聊天列表页面浏览"),

    empty("", "", "");

    PageStatisticsEvent(String curPageId) {
        this.curPageId = curPageId;
    }

    PageStatisticsEvent(String curPageId, String eventCode, String eventName) {
        this.curPageId = curPageId;
        this.eventCode = eventCode;
        this.eventName = eventName;
    }

    private String curPageId;//页面ID，必填
    private String eventCode;//有需要统计页面显示事件才填
    private String eventName;
    private JSONObject extension;

    public String getCurPageId() {
        return curPageId;
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

    public PageStatisticsEvent setExtension(JSONObject extension) {
        if (extension != null) {
            this.extension = extension;
        }
        return this;
    }

    public PageStatisticsEvent setExtension(String... extension) {
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

    public PageStatisticsEvent putExtension(String key, String value) {
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
