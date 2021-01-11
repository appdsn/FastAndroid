package com.appdsn.commonbase.event;

/**
 * @author zq
 * @date 2019/5/13.
 * description：
 */
public class EventCode {
    //刷新朋友列表
    public static final int EVENT_CODE_REFRESH_FRIENDS_LIST = 10001;

    //刷新我的关注列表
    public static final int EVENT_CODE_REFRESH_MY_FOLLOW_LIST = 10002;

    //刷新我的粉丝列表
    public static final int EVENT_CODE_REFRESH_MY_FANS_LIST = 10003;

    //接唱发布页选择的卡片位置
    public static final int EVENT_CODE_GET_VOICE_PUBLISH_POSITION = 10004;

    //说情话发布页选择的卡片位置
    public static final int EVENT_CODE_GET_LOVE_PUBLISH_POSITION = 10005;

    //搜索结果列表点击关闭上一页面
    public static final int EVENT_CODE_CLOSE_LAST_ACTIVITY_FROM_SEARCH_RESULT = 10006;

    //搜索歌词点击唱歌发布页替换当前页歌词
    public static final int EVENT_CODE_REPLACE_WORD_WHEN_CLICKED_SING = 10007;

    //搜索歌词点击讲情话发布页替换当前页歌词
    public static final int EVENT_CODE_REPLACE_WORD_WHEN_CLICKED_VOICE = 10008;

    //刷新我的昵称
    public static final int EVENT_CODE_REFRESH_MY_NICK_NAME = 20001;

    //刷新我的信息
    public static final int EVENT_CODE_REFRESH_MY_PROFILE = 20002;

    //刷新GET匹配次数
    public static final int EVENT_CODE_REFRESH_GET_COUNT = 30001;

    //举报成功
    public static final int EVENT_CODE_REPORT_SECCESS = 30002;

    //语音匹配接听成功
    public static final int EVENT_CODE_VOICE_GET_SUCCESS = 30003;
}
