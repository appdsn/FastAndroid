package com.appdsn.commoncore.widget.commondialog;

public class UCheck {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }

    public static String isNull(String str) {
        return isEmpty(str) ? "" : str;
    }

    public static String isNull(String str, final String defaul) {
        return isEmpty(str) ? defaul : str;
    }
}
