package com.appdsn.commoncore.utils.log;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

final class LoggerPrinter {

    /**
     * Android一个日志条目不能超过4076字节，
     * 这里设置以4000字节来计算
     */
    private static final int CHUNK_SIZE = 4000;
    /**
     * 样式
     */
    private static final char TOP_LEFT_CORNER = '┏';
    private static final char BOTTOM_LEFT_CORNER = '┗';
    private static final char MIDDLE_CORNER = '┠';
    private static final char HORIZONTAL_DOUBLE_LINE = '┃';
    private static final String DOUBLE_DIVIDER = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    private static final String SINGLE_DIVIDER = "──────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER;

    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    private StringBuilder logStr = new StringBuilder();

    private Gson gson = new Gson();

    /**
     * 返回最后一次格式化的打印结果样式
     *
     * @return
     */

    public String getFormatLog() {
        return logStr.toString();
    }


    public void d(String message, Object... args) {
        log(Log.DEBUG, message, args);
    }


    public void e(String message, Object... args) {
        e(null, message, args);
    }


    public void e(Throwable throwable, String message, Object... args) {
        if (throwable != null && message != null) {
            message += " : " + throwable.toString();
        }
        if (throwable != null && message == null) {
            message = throwable.toString();
        }
        if (message == null) {
            message = "message/exception 为空！";
        }
        log(Log.ERROR, message, args);
    }


    public void w(String message, Object... args) {
        log(Log.WARN, message, args);
    }


    public void i(String message, Object... args) {
        log(Log.INFO, message, args);
    }


    public void v(String message, Object... args) {
        log(Log.VERBOSE, message, args);
    }


    public void wtf(String message, Object... args) {
        log(Log.ASSERT, message, args);
    }

    public void mix(Object... message) {
        if (message == null) {
            d("mix 数据为空！");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < message.length; i++) {
            Object msg = message[i];
            if (msg instanceof String) {
                String msgStr = (String) msg;
                if (msgStr.startsWith("{") || msgStr.startsWith("[")) {
                    stringBuilder.append(getJson(msgStr));
                } else if (msgStr.startsWith("<") && msgStr.endsWith(">")) {
                    stringBuilder.append(getXml(msgStr));
                } else {
                    stringBuilder.append(msgStr);
                }
            } else if (msg instanceof Map) {
                stringBuilder.append(getMap((Map) msg));
            } else if (msg instanceof List) {
                stringBuilder.append(getList((List) msg));
            } else {
                stringBuilder.append(getJson(gson.toJson(msg)));
            }
            stringBuilder.append(LINE_SEPARATOR);
        }

        d(stringBuilder.toString());
    }


    /**
     * 打印json
     */
    public void json(String json) {
        d(getJson(json));
    }

    /**
     * 格式化json
     */
    private String getJson(String json) {
        String message = json;
        if (TextUtils.isEmpty(json)) {
            message = "json 数据为空！";
            return message;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jo = new JSONObject(json);
                message = jo.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray ja = new JSONArray(json);
                message = ja.toString(4);
            }
        } catch (Exception e) {
            message = (e.getCause().getMessage() + LINE_SEPARATOR + json);
        }
        return message;
    }

    /**
     * 打印xml
     */
    public void xml(String xml) {
        d(getXml(xml));
    }

    /**
     * 格式化xml
     */
    private String getXml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            return "xml 数据为空！";
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            String message = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEPARATOR);
            return message;
        } catch (TransformerException e) {
            return (e.getCause().getMessage() + LINE_SEPARATOR + xml);
        }
    }

    /**
     * 打印Map集合
     */
    public void map(Map map) {
        d(getMap(map));
    }

    /**
     * 格式化Map集合
     */
    private String getMap(Map map) {
        if (map != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object entry : map.entrySet()) {
                stringBuilder.append("[key] → ");
                stringBuilder.append(((Map.Entry) entry).getKey());
                stringBuilder.append(",[value] → ");
                stringBuilder.append(gson.toJson(((Map.Entry) entry).getValue()));
                stringBuilder.append(LINE_SEPARATOR);
            }
            return (stringBuilder.toString());
        } else {
            return "map 数据为空！";
        }
    }

    /**
     * 格式化List集合
     */
    public void list(List list) {
        d(getList(list));
    }

    /**
     * 格式化List集合
     */
    private String getList(List list) {
        if (list != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append("[" + i + "] → ");
                stringBuilder.append(gson.toJson(list.get(i)));
                stringBuilder.append(LINE_SEPARATOR);
            }
            return (stringBuilder.toString());
        } else {
            return "list 数据为空！";
        }
    }

    /**
     * 同步日志打印顺序
     */
    private synchronized void log(int priority, String msg, Object... args) {
        if (!LogUtils.sDebug) {
            return;
        }
        logStr.delete(0, logStr.length());
        String message = args.length == 0 ? msg : String.format(msg, args);
        logChunk(priority, TOP_BORDER);
        if (LogUtils.sShowThreadInfo) {
            //打印线程
            getStackInfo(priority);
        }
        //得到系统的默认字符集的信息字节（UTF-8）
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            logContent(priority, message);
            logChunk(priority, BOTTOM_BORDER);
            return;
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //创建系统的默认字符集的一个新的字符串（UTF-8）
            logContent(priority, new String(bytes, i, count));
        }
        logChunk(priority, BOTTOM_BORDER);
    }

    private void logContent(int priority, String chunk) {
        String[] lines = chunk.split(LINE_SEPARATOR);
        for (String line : lines) {
            logChunk(priority, HORIZONTAL_DOUBLE_LINE + " " + line);
        }
    }

    private void logChunk(int priority, String chunk) {
        logStr.append(LINE_SEPARATOR);
        logStr.append(chunk);
        String TAG = LogUtils.sTag;
        switch (priority) {
            case Log.ERROR:
                Log.e(TAG, chunk);
                break;
            case Log.INFO:
                Log.i(TAG, chunk);
                break;
            case Log.VERBOSE:
                Log.v(TAG, chunk);
                break;
            case Log.WARN:
                Log.w(TAG, chunk);
                break;
            case Log.ASSERT:
                Log.wtf(TAG, chunk);
                break;
            case Log.DEBUG:
            default:
                Log.d(TAG, chunk);
                break;
        }
    }

    /**
     * 打印堆栈信息.
     */
    private void getStackInfo(int priority) {
        logChunk(priority, HORIZONTAL_DOUBLE_LINE + "[Thread] → " + Thread.currentThread().getName());
        logChunk(priority, MIDDLE_BORDER);
        String str = "";
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();

        for (int i = 0; i < traces.length; i++) {
            StackTraceElement element = traces[i];
            StringBuilder perTrace = new StringBuilder(str);
            if (element.isNativeMethod()) {
                continue;
            }
            String className = element.getClassName();
            if (className.startsWith("android.")
                    || className.contains("com.android")
                    || className.contains("java.lang")
                    || className.contains("com.youth.xframe")) {
                continue;
            }
            perTrace.append(element.getClassName())
                    .append('.')
                    .append(element.getMethodName())
                    .append("  (")
                    .append(element.getFileName())
                    .append(':')
                    .append(element.getLineNumber())
                    .append(")");
            str += "  ";
            logContent(priority, perTrace.toString());
        }
        logChunk(priority, MIDDLE_BORDER);
    }
}
