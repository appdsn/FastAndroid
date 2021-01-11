package com.appdsn.commonbase.statistics;

import org.json.JSONException;
import org.json.JSONObject;

//继承至BaseAppActivity和BaseAppFragment不需要填pageId，可以自动取到
public enum NormalStatisticsEvent {
    /**
     * 联系人页面
     */
    rolemessage_box_click("rolemessage_box_click", "点击角色消息框"),
    addrole_button_click("addrole_button_click", "点击添加角色按钮"),
    leftslide_rolemessage_box_custom("leftslide_rolemessage_box_custom", "左滑角色消息框"),
    delete_button_click("delete_button_click", "点击删除按钮"),
    ok_button_click("ok_button_click", "点击确定按钮"),

    /**
     * 爱豆添加搜索页
     */
    addrole_return_button_click("addrole_return_button_click", "添加角色返回按钮点击"),
    search_box_click("search_box_click", "搜索输入框点击"),
    roleclassification_tab_1_click("roleclassification_tab_1_click", "角色分类tab1"),
    roleclassification_tab_2_click("roleclassification_tab_2_click", "角色分类tab2"),
    roleclassification_tab_3_click("roleclassification_tab_3_click", "角色分类tab3"),
    addfriend_button_1_click("addfriend_button_1_click", "添加好友按钮点击"),
    send_message_button_click("send_message_button_click", "发消息按钮点击"),
    create_role_button_click("create_role_button_click", "添加角色列表页面浏览"),

    /**
     * 聊天详情页
     */
    chatdetails_return_button_click("chatdetails_return_button_click", "聊天详情返回按钮点击"),
    role_setting_button_click("role_setting_button_click", "角色设置按钮点击"),
    longpress_message_content_click("longpress_message_content_click", "长按消息内容"),
    reply_button_click("reply_button_click", "回复按钮点击"),
    like_button_click("like_button_click", "点赞按钮点击"),
    text_input_box_click("text_input_box_click", "文本输入框点击"),
    mood_scene_tab_click("mood_scene_tab_click", "心情场景tab点击"),
    send_picture_tab_click("send_picture_tab_click", "发我照片tab点击"),
    food_scene_tab_click("food_scene_tab_click", "饮食场景tab点击"),
    sport_scene_tab_click("sport_scene_tab_click", "运动场景tab点击"),

    /**
     * 爱豆设置页面
     */
    role_setting_return_button_click("role_setting_return_button_click", "角色设置返回按钮点击"),
    role_setting_delete_button_click("role_setting_delete_button_click", "角色设置删除按钮点击"),
    role_setting_save_button_click("role_setting_save_button_click", "角色设置保存按钮点击"),

    /**
     * 协议弹窗页1
     */
    agreement_page1_click1("agreement_page1_click1", "点击“同意并继续"),
    agreement_page1_click2("agreement_page1_click2", "点击“不同意”"),

    /**
     * 协议弹窗页2
     */
    agreement_page2_click1("agreement_page2_click1", "点击“仍不同意”"),
    agreement_page2_click2("agreement_page2_click2", "点击“我再想想”"),

    /**
     * 经期页面展示
     */
    first_period_click("first_period_click", "点击最后一次大姨妈"),
    period_day_click("period_day_click", "点击经期天数"),
    period_cycle_click("period_cycle_click", "点击月经周期"),
    period_remind_click("period_remind_click", "点击经期开始提醒"),
    period_skip_click("period_skip_click", "点击“跳过”"),
    period_save("period_save", "点击“保存”"),

    /**
     * 首页
     */
    home_page1_view_page("home_page1_view_page", "尚未设置经期"),
    home_page2_view_page("home_page2_view_page", "已经设置经期"),
    week_click("week_click", "点击日期"),//todo 添加额外参数
    information_tab_click("information_tab_click", "点击信息流类目"),
    feed_list_click("feed_list_click", "文章点击，不含广告点击"),
    feed_back_click("feed_back_click", "信息流吸顶返回"),
    feed_toptimes_click("feed_toptimes_click", "信息流吸顶"),

    /**
     * 信息流详情页
     */
    recommend_list_click("recommend_list_click", "点击推荐文章"),

    /**
     * 经期记录页
     */
    notset_period_calendar_view_page("notset_period_calendar_view_page", "未设置过周期曝光时"),
    been_set_period_calendar_view_page("been_set_period_calendar_view_page", "已设置过周期曝光时"),
    notset_period_custom("notset_period_custom", "尚未设置经期弹窗展示"),
    explanation_view_page("explanation_view_page", "名词解释页面曝光时"),
    periodset_click1("periodset_click1", "点击“暂时不用”"),
    periodset_click2("periodset_click2", "点击“立即设置”"),
    backups_period_custom("backups_period_custom", "备份弹窗展示"),
    periodbackups_click1("periodbackups_click1", "点击“暂时不用”"),
    periodbackups_click2("periodbackups_click2", "点击“立即备份”"),
    periodset_calendar_click("periodset_calendar_click", "点击日期区域"),
    periodset_entrance_click("periodset_entrance_click", "点击姨妈设置"),
    calendar_bottom_1_click("calendar_bottom_1_click", "点击大姨妈开关"),//todo 额外参数
    calendar_bottom_2_click("calendar_bottom_2_click", "点击爱爱开关"),//todo 额外参数
    calendar_bottom_3_click("calendar_bottom_3_click", "点击”体温“"),//todo 额外参数
    calendar_bottom_4_click("calendar_bottom_4_click", "点击”症状“"),//todo 额外参数
    calendar_bottom_5_click("calendar_bottom_5_click", "点击”心情“"),//todo 额外参数
    symptom_view_page("symptom_view_page", "症状设置页曝光时"),//todo 额外参数
    symptom_A_click("symptom_A_click", "症状选择"),

    /**
     * 登录页面
     */
    own_number_view_page("own_number_view_page", "一键登录页面曝光时"),
    own_message_view_page("own_message_view_page", "短信验证页面曝光时"),
    own_binding_view_page("own_binding_view_page", "绑定手机号页面曝光时"),
    own_number_click("own_number_click", "点击“一键登录”按钮"),
    own_weixin_click("own_weixin_click", "点击微信登录"),
    own_message_click("own_message_click", "点击“短信验证”登录"),
    own_sina_click("own_sina_click", "点击“微博”登录"),
    own_message_getcaptcha_click("own_message_getcaptcha_click", "短信登录时点击获取验证码"),
    own_message_log_click("own_message_log_click", "短信登录时点击登录按钮"),
    binding_message_getcaptcha_click("binding_message_getcaptcha_click", "绑定时点击获取验证码"),
    binding_message_log_click("binding_message_log_click", "绑定时点击立即绑定按钮"),

    /**
     * 首页底部tab点击
     */
    home_page_click("home_page_click", "点击底部tab首页"),
    red_calendar_page_click("red_calendar_page_click", "点击底部tab记录"),
    identity_page_click("identity_page_click", "点击底部tab我的"),
    accompany_click("accompany_click", "点击底部tab陪我状态"),


    /**
     * 广告埋点
     */
    ad_click("ad_click", "广告点击"),//todo 额外参数
    ad_show("ad_show", "广告展示"),
    ad_request("ad_request", "广告请求"),


    return_click("return_click", "返回点击"),
    cancel_click("cancel_click", "取消点击"),
    empty("", "");

    NormalStatisticsEvent(String eventCode, String eventName) {
        this.eventCode = eventCode;
        this.eventName = eventName;
    }

    private String eventCode;
    private String eventName;
    private JSONObject extension;

    public String getEventCode() {
        return eventCode;
    }

    public JSONObject getExtension() {
        return extension;
    }

    public String getEventName() {
        return eventName;
    }

    public NormalStatisticsEvent setExtension(JSONObject extension) {
        if (extension != null) {
            this.extension = extension;
        }
        return this;
    }

    public NormalStatisticsEvent setExtension(String... extension) {
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

    public NormalStatisticsEvent putExtension(String key, String value) {
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
