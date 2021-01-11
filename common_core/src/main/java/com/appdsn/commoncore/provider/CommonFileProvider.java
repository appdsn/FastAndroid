package com.appdsn.commoncore.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Desc:通用的FileProvider，为了支持7.0传递文件路径URL，可以直接调用getUriForFile方法
 * <p>
 */
public class CommonFileProvider extends FileProvider {
    public static final String SHARE_FILE_AUTHORITY_SUFFIX = ".common.provider";

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, getShareFileAuthority(context), file);
        }
        return Uri.fromFile(file);
    }

    public static Uri getUriForFile(Context context, String filePath) {
        try {
            return getUriForFile(context, new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getShareFileAuthority(Context context) {
        if (context != null) {
            return context.getPackageName() + SHARE_FILE_AUTHORITY_SUFFIX;
        }
        return "";
    }
}
