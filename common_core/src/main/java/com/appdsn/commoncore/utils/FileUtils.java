package com.appdsn.commoncore.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.appdsn.commoncore.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * Desc:文件操作常用方法：删除，创建，移动，拷贝，文件大小格式化，剩余空间等，
 * 保存各种信息到文件（图片，字符串，字节数组等），从文件读取各种信息
 */
public final class FileUtils {
    public static final String ROOT_STORAGE = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID;
    public static final String IMAGE_ROOT = ROOT_STORAGE + "/image";
    public static final String VIDEO_ROOT = ROOT_STORAGE + "/video";
    public static final String LOG_SAVE_PATH = ROOT_STORAGE + "/logs/error";
    public static final String APK_SAVE_PATH = ROOT_STORAGE + "/apk";

    public static final int BYTE = 1;
    public static final int KB = 1024;
    public static final int MB = 1048576;
    public static final int GB = 1073741824;

    /**
     * �?测SD卡是否存�?,包括外部sd卡和虚拟的sd�?
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    /**
     * 得到SD卡根目录
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * Return whether the file exists.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExists(file.getAbsolutePath());
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(final String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(filePath);
    }

    private static boolean isFileExistsApi29(String filePath) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = ContextUtils.getContext().getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) {
                    return false;
                }
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Copy the file.
     *
     * @param srcFile  The source file.
     * @param destFile The destination file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean copyFile(final File srcFile,
                                   final File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    /**
     * Copy the directory.
     *
     * @param srcDir  The source directory.
     * @param destDir The destination directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean copyDir(final File srcDir,
                                  final File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    /**
     * Move the file.
     *
     * @param srcFile  The path of source file.
     * @param destFile The path of destination file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean moveFile(final File srcFile,
                                   final File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    /**
     * Move the directory.
     *
     * @param srcDir  The path of source directory.
     * @param destDir The path of destination directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean moveDir(final File srcDir,
                                  final File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    private static boolean copyOrMoveDir(final File srcDir, final File destDir, final boolean isMove) {
        if (srcDir == null || destDir == null) {
            return false;
        }
        // destDir's path locate in srcDir's path then return false
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) {
            return false;
        }
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }
        if (destDir.exists()) {
            // require delete the old directory
            if (!deleteAllInDir(destDir)) {// unsuccessfully delete then return false
                return false;
            }
        }
        if (!createOrExistsDir(destDir)) {
            return false;
        }
        File[] files = srcDir.listFiles();
        for (File file : files) {
            File oneDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                if (!copyOrMoveFile(file, oneDestFile, isMove)) {
                    return false;
                }
            } else if (file.isDirectory()) {
                if (!copyOrMoveDir(file, oneDestFile, isMove)) {
                    return false;
                }
            }
        }
        return !isMove || deleteAll(srcDir);
    }

    private static boolean copyOrMoveFile(final File srcFile, final File destFile, final boolean isMove) {
        if (srcFile == null || destFile == null) {
            return false;
        }
        // srcFile equals destFile then return false
        if (srcFile.equals(destFile)) {
            return false;
        }
        // srcFile doesn't exist or isn't a file then return false
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        if (destFile.exists()) {
            // require delete the old file
            if (!destFile.delete()) {// unsuccessfully delete then return false
                return false;
            }
        }

        if (!createOrExistsDir(destFile.getParentFile())) {
            return false;
        }

        try {
            return writeStreamToFile(new FileInputStream(srcFile), destFile)
                    && !(isMove && !deleteAll(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rename the file.
     *
     * @param file    The file.
     * @param newName The new name of file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean rename(final File file, final String newName) {
        // file is null then return false
        if (file == null) {
            return false;
        }
        // file doesn't exist then return false
        if (!file.exists()) {
            return false;
        }
        // the new name is space then return false
        if (TextUtils.isEmpty(newName)) {
            return false;
        }
        // the new name equals old name then return true
        if (newName.equals(file.getName())) {
            return true;
        }
        File newFile = new File(file.getParent() + File.separator + newName);
        // the new name of file exists then return false
        return !newFile.exists()
                && file.renameTo(newFile);
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果无法创建文件或文件名为空则返回null
     */
    public static File getFile(String folderName, String fileName) {
        File file = new File(getFolder(folderName), fileName);
        createOrExistsFile(file);
        return file;
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param filePath The path of file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static File getFile(final String filePath) {
        File file = getFileByPath(filePath);
        createOrExistsFile(file);
        return file;
    }

    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static String getFilePath(String folderName, String fileName) {
        File file = getFile(folderName, fileName);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取文件夹对�?
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创�?
     */
    public static File getFolder(String folderName) {
        File file = null;
        if (folderName.startsWith(File.separator)) {
            file = new File(getSDCardPath() + folderName);
        } else {
            file = new File(getSDCardPath() + File.separator + folderName);
        }
        file.mkdirs();
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径 getFolderPath("home/admin") = "sd/home/admin"
     *
     * @return 返回SD卡下的指定文件夹的绝对路�?
     */
    public static String getFolderPath(String folderName) {
        File fileFolder = getFolder(folderName);
        if (fileFolder != null) {
            return fileFolder.getAbsolutePath();
        }
        return null;
    }

    /**
     * 得到SD卡缓存根目录，�?�常是在 /sdcard/Android/data/<application package>/cache 这个路径下面
     * <p>
     * isExternalStorageRemovable()设备的外存是否是可以拆卸的，比如SD卡，是则返回true
     * isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true�? getExternalCacheDir
     * �?getExternalFilesDir getFilesDir() getCacheDir()
     * 这些目录都是属于应用的，当应用被卸载的时候，里面的内容都会被移除
     */
    public static File getCacheDirectory(String folderName) {

        File appCacheDir = null;
        if (isSDCardEnable()) {
            /*
             * /sdcard/Android/data/<application package>/cache
             */
            appCacheDir = ContextUtils.getContext().getExternalCacheDir();
        }
        if (appCacheDir == null) {
            /*
             * /data/data/<application package>/cache
             */
            appCacheDir = ContextUtils.getContext().getCacheDir();
        }
        if (!TextUtils.isEmpty(folderName)) {
            appCacheDir = new File(appCacheDir, folderName);
        }

        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }

        return appCacheDir;
    }

    public static boolean deleteCacheDir(Context context) {
        File cacheFile;
        if (isSDCardEnable()) {
            cacheFile = ContextUtils.getContext().getExternalCacheDir();
        } else {
            cacheFile = ContextUtils.getContext().getCacheDir();
        }
        // 目录此时为空，可以删除
        return deleteAll(cacheFile);
    }

    /**
     * @return 是否删除成功
     */
    public static boolean deleteAll(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    deleteAll(f);
                }
            }
        }
        return file.delete();// 目录此时为空，可以删除
    }

    /**
     * Delete the all in directory.
     *
     * @param dir The directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean deleteAllInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
    }

    /**
     * Delete all files in directory.
     *
     * @param dir The directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean deleteFilesInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
    }

    /**
     * Delete all files that satisfy the filter in directory.
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean deleteFilesInDirWithFilter(final File dir, final FileFilter filter) {
        if (dir == null) {
            return false;
        }
        // dir doesn't exist then return true
        if (!dir.exists()) {
            return true;
        }
        // dir isn't a directory then return false
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file)) {
                    if (!deleteAll(file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static long getCacheFileSize() {
        long s = 0;
        File cacheFile;
        if (isSDCardEnable()) {
            cacheFile = ContextUtils.getContext().getExternalCacheDir();
        } else {
            cacheFile = ContextUtils.getContext().getCacheDir();
        }

        try {
            s = getFileSize(cacheFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    /* 递归 ：取得文件或文件夹大小 */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.isDirectory()) {// 说明是文件夹
            File flist[] = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                size = size + getFileSize(flist[i]);
            }
        } else {
            if (file.exists() && file.isFile()) {
                size = file.length();
            }
        }
        return size;
    }

    public static String formatFileSize(long fileSize) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "0.00B";
        if (fileSize < 1024) {
            if (fileSize != 0) {
                fileSizeString = df.format((double) fileSize) + "B";
            }
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String convertFileSize(long byteSize) {
        if (byteSize < 0) {
            return "shouldn't be less than zero!";
        } else if (byteSize < FileUtils.KB) {
            return String.format("%.3fB", (double) byteSize);
        } else if (byteSize < FileUtils.MB) {
            return String.format("%.3fKB", (double) byteSize / FileUtils.KB);
        } else if (byteSize < FileUtils.GB) {
            return String.format("%.3fMB", (double) byteSize / FileUtils.MB);
        } else {
            return String.format("%.3fGB", (double) byteSize / FileUtils.GB);
        }
    }

    //获取总的内存空间并控制显示
    public static String getTotalSpace() {
        String mToalS = "";
        try {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            float i = 1024 * 1024 * 1024;
            float bytes = 0;
            bytes = sf.getTotalBytes() / i;

            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            mToalS = df.format(bytes);
        } catch (Exception e) {
        }
        return mToalS;
    }

    //    获取剩余的内存空间并控制显示
    public static String getFreeSpace() {
        String mFreeS = "";
        try {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            float i = 1024 * 1024 * 1024;
            float bytes = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bytes = sf.getFreeBytes() / i;
            }
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            mFreeS = df.format(bytes);
        } catch (Exception e) {
        }

        return mFreeS;
    }

    public static long getFileCount(File file) {// 递归求取目录文件个数
        long size = 0;
        File flist[] = file.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileCount(flist[i]);
                size--;
            }
        }
        return size;
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath 路径
     * @return 信息
     */
    public static String getFileExtension(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Return the name of file.
     *
     * @param filePath The path of file.
     * @return the name of file
     */
    public static String getFileName(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    /**
     * Return the name of file without extension.
     *
     * @param filePath The path of file.
     * @return the name of file without extension
     */
    public static String getFileNameNoExtension(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * Return the file's path of directory.
     *
     * @param filePath The path of file.
     * @return the file's path of directory
     */
    public static String getDirName(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    /**
     * 将content://形式的uri转为实际文件路径
     *
     * @param uri 地址
     * @return uri转为实际文件路径
     */
    public static String uriToPath(Uri uri) {

        Cursor cursor = null;
        try {
            if (uri.getScheme().equalsIgnoreCase("file")) {
                return uri.getPath();
            }
            cursor = ContextUtils.getContext().getContentResolver().query(uri, null, null, null,
                    null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA)); // 图片文件路径
            }
        } catch (Exception e) {
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
            return null;
        }
        return null;
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    public static byte[] getFileMD5(final File file) {
        if (file == null) {
            return null;
        }
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) {
                    break;
                }
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从文件中读取文本
     */
    public static String readTextFile(String filePath) {
        InputStream is = getFileInputStream(getFile(filePath));
        return input2String(is);
    }

    /**
     * 从文件中读取文本
     */
    public static String readTextFile(File file) {
        InputStream is = getFileInputStream(file);
        return input2String(is);
    }

    /**
     * 从文件中读取图片
     */
    public static Bitmap readBitmapFile(String filePath) {
        InputStream is = getFileInputStream(getFile(filePath));
        if (is == null) {
            return null;
        }
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 从文件中读取图片
     */
    public static Bitmap readBitmapFile(File file) {
        InputStream is = getFileInputStream(file);
        if (is == null) {
            return null;
        }
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 从文件中读取
     */
    public static byte[] readBytesFile(String filePath) {
        InputStream is = getFileInputStream(getFile(filePath));
        return input2Bytes(is);
    }

    /**
     * 从文件中读取
     */
    public static byte[] readBytesFile(File file) {
        InputStream is = getFileInputStream(file);
        return input2Bytes(is);
    }

    /**
     * 将字符串保存到本地
     */
    public static boolean writeTextToFile(String content, String filePath) {
        byte[] data = content.getBytes();
        File file = getFile(filePath);
        return writeBytesToFile(data, file);
    }

    /**
     * 将字符串保存到本地
     */
    public static boolean writeTextToFile(String content, File file) {
        byte[] data = content.getBytes();
        return writeBytesToFile(data, file);
    }

    /**
     * 将文件保存到本地
     */
    public static boolean writeBytesToFile(byte[] bytes, String filePath) {
        File file = getFile(filePath);
        return writeBytesToFile(bytes, file);
    }

    /**
     * 将文件保存到本地
     */
    public static boolean writeBytesToFile(byte[] bytes, File file) {

        if (file == null || bytes == null) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            closeIO(is, os);
        }

    }

    public static boolean writeBitmapToFile(Bitmap bitmap, File file) {
        return writeBitmapToFile(bitmap, file, CompressFormat.JPEG);
    }

    /**
     * 图片写入文件
     *
     * @return 是否写入成功
     */
    public static boolean writeBitmapToFile(Bitmap bitmap, String filePath, final Bitmap.CompressFormat format) {
        return writeBitmapToFile(bitmap, getFile(filePath), format);
    }

    /**
     * 图片写入文件
     *
     * @return 是否写入成功
     */
    public static boolean writeBitmapToFile(Bitmap bitmap, File file, final Bitmap.CompressFormat format) {
        if (file == null || bitmap == null) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            return bitmap.compress(format, 100, os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(os);

        }
        return false;
    }

    public static boolean writeStreamToFile(InputStream inStream, File file) {
        if (file == null || inStream == null) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = inStream.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            closeIO(inStream, os);
        }
    }

    public static boolean writeStreamToFile(InputStream inStream, String filePath) {

        File file = getFile(filePath);
        return writeStreamToFile(inStream, file);
    }

    public static InputStream getFileInputStream(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 输入流转byte[]<br>
     */
    public static final byte[] input2Bytes(InputStream is) {
        if (is == null) {
            return null;
        }
        BufferedInputStream inStream = new BufferedInputStream(is);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = inStream.read(buffer))) {
                outStream.write(buffer, 0, len);
            }

            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(inStream, outStream);
        }

        return null;
    }

    /**
     * 输入流转字符流
     */
    public static String input2String(InputStream is) {
        if (null == is) {
            return "";
        }
        StringBuilder resultSb = new StringBuilder("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String data;
            while (null != (data = br.readLine())) {
                if (!resultSb.toString().equals("")) {
                    resultSb.append("\r\n");
                }
                resultSb.append(data);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return resultSb.toString();
    }

    /**
     * 将assets文件夹下文件拷贝到/databases/下
     *
     * @param db_name
     */
    public static boolean copyDbFile(String db_name) {
        //创建文件夹
        File dbFile = ContextUtils.getContext().getDatabasePath(db_name);
        if (!dbFile.exists()) {
            dbFile.mkdirs();
        }
        if (dbFile.exists()) {
            return true;
        }
        return ResourceUtils.copyFileFromAssets(db_name, dbFile.getAbsolutePath());
    }

    /**
     * 关闭
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (cb != null) {
                    cb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}