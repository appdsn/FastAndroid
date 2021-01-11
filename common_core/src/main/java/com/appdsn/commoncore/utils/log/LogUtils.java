package com.appdsn.commoncore.utils.log;

import java.util.List;
import java.util.Map;

/**
 * Desc:格式化打印Log信息，更加美观直观
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:07
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public final class LogUtils {

    private static final LoggerPrinter printer = new LoggerPrinter();
    public static boolean sShowThreadInfo = false;
    public static boolean sDebug = true;
    public static String sTag = "LogUtils";

    private LogUtils() {
    }

    public static void setDebug(boolean debug, boolean showThreadInfo) {
        sDebug = debug;
    }

    public static void setThreadInfo(boolean showThreadInfo) {
        sShowThreadInfo = showThreadInfo;
    }

    public static String getFormatLog() {
        return printer.getFormatLog();
    }

    public static void d(String tag, String message) {
        sTag = tag;
        printer.d(message, "");
    }

    public static void e(String tag, String message) {
        sTag = tag;
        printer.e((Throwable) null, message);
    }

    public static void e(String tag, Throwable throwable, String message) {
        sTag = tag;
        printer.e(throwable, message, "");
    }

    public static void i(String tag, String message) {
        sTag = tag;
        printer.i(message, "");
    }

    public static void v(String tag, String message) {
        sTag = tag;
        printer.v(message, "");
    }

    public static void w(String tag, String message) {
        sTag = tag;
        printer.w(message, "");
    }

    public static void wtf(String tag, String message) {
        sTag = tag;
        printer.wtf(message, "");
    }

    public static void json(String tag, String json) {
        sTag = tag;
        printer.json(json);
    }

    public static void xml(String tag, String xml) {
        sTag = tag;
        printer.xml(xml);
    }

    public static void map(String tag, Map map) {
        sTag = tag;
        printer.map(map);
    }

    public static void list(String tag, List list) {
        sTag = tag;
        printer.list(list);
    }

    public static void object(String tag, Object object) {
        sTag = tag;
        printer.mix(object);
    }

    public static void mix(String tag, Object... mixMsg) {
        sTag = tag;
        printer.mix(mixMsg);
    }
}
