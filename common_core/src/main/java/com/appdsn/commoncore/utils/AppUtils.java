package com.appdsn.commoncore.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.appdsn.commoncore.provider.CommonFileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Desc:
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 17:48
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class AppUtils {

    private AppUtils() {
    }

    public interface OnAppStatusChangedListener {
        void onForeground();

        void onBackground();
    }

    /**
     * Register the status of application changed listener.
     *
     * @param listener The status of application changed listener
     */
    public static void registerAppStatusChangedListener(final AppUtils.OnAppStatusChangedListener listener) {
        ActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
    }

    /**
     * Unregister the status of application changed listener.
     *
     * @param listener The status of application changed listener
     */
    public static void unregisterAppStatusChangedListener(final AppUtils.OnAppStatusChangedListener listener) {
        ActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
    }

    public static void installApp(final String filePath) {
        installApp(getFileByPath(filePath));
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final File file) {
        if (!isFileExists(file)) {
            return;
        }
        Intent intent = getInstallAppIntent(file, true);
        if (intent != null) {
            ContextUtils.getContext().startActivity(intent);
        }
    }

    public static void installAppForResult(final Activity activity,
                                           final String filePath,
                                           final int requestCode) {
        installAppForResult(activity, getFileByPath(filePath), requestCode);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param activity    The activity.
     * @param file        The file.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void installAppForResult(final Activity activity,
                                           final File file,
                                           final int requestCode) {
        if (!isFileExists(file)) {
            return;
        }
        Intent intent = getInstallAppIntent(file, false);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    public static void uninstallApp(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        try {
            ContextUtils.getContext().startActivity(getUninstallAppIntent(packageName, true));
        } catch (Exception e) {
        }
    }

    /**
     * Uninstall the app.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void uninstallAppForResult(final Activity activity,
                                             final String packageName,
                                             final int requestCode) {
        if (isSpace(packageName) || activity == null) {
            return;
        }
        try {
            activity.startActivityForResult(getUninstallAppIntent(packageName, false), requestCode);
        } catch (Exception e) {
        }
    }

    /**
     * Return whether the app is installed.
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageManager packageManager = ContextUtils.getContext().getPackageManager();
        try {
            return packageManager.getApplicationInfo(packageName, 0) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return whether the application with root permission.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppRoot() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
        if (result.result == 0) {
            return true;
        }
        if (result.errorMsg != null) {
            Log.d("AppUtils", "isAppRoot() called" + result.errorMsg);
        }
        return false;
    }

    /**
     * Return whether it is a system application.
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppSystem(final String packageName) {
        if (isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return whether application is foreground.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground() {
        ActivityManager am =
                (ActivityManager) ContextUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(ContextUtils.getContext().getPackageName());
            }
        }
        return false;
    }

    /**
     * Return whether application is foreground.
     * <p>Target APIs greater than 21 must hold
     * {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground(final String packageName) {
        return !isSpace(packageName) && packageName.equals(getForegroundProcessName());
    }

    public static void launchApp(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        try {
            ContextUtils.getContext().startActivity(getLaunchAppIntent(packageName, true));
        } catch (Exception e) {
        }
    }

    /**
     * Launch the application.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void launchAppForResult(final Activity activity,
                                          final String packageName,
                                          final int requestCode) {
        if (isSpace(packageName) || activity == null) {
            return;
        }
        try {
            activity.startActivityForResult(getLaunchAppIntent(packageName, false), requestCode);
        } catch (Exception e) {
        }
    }

    /**
     * Relaunch the application.
     */
    public static void relaunchApp() {
        relaunchApp(false);
    }

    /**
     * Relaunch the application.
     *
     * @param isKillProcess True to kill the process, false otherwise.
     */
    public static void relaunchApp(final boolean isKillProcess) {
        PackageManager packageManager = ContextUtils.getContext().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(ContextUtils.getContext().getPackageName());
        if (intent == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ContextUtils.getContext().startActivity(intent);
        if (!isKillProcess) {
            return;
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        launchAppDetailsSettings(ContextUtils.getContext().getPackageName());
    }

    /**
     * Launch the application's details settings.
     *
     * @param packageName The name of the package.
     */
    public static void launchAppDetailsSettings(final String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        try {
            ContextUtils.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
        }
    }

    /**
     * Exit the application.
     */
    public static void exitApp() {
        List<Activity> activityList = ActivityUtils.getActivityList();
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
        }
        System.exit(0);
    }

    public static Drawable getAppIcon() {
        return getAppIcon(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's icon.
     *
     * @param packageName The name of the package.
     * @return the application's icon
     */
    public static Drawable getAppIcon(final String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the application's name.
     *
     * @return the application's name
     */
    public static String getAppName() {
        return getAppName(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's name.
     *
     * @param packageName The name of the package.
     * @return the application's name
     */
    public static String getAppName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the application's path.
     *
     * @return the application's path
     */
    public static String getAppPath() {
        return getAppPath(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's path.
     *
     * @param packageName The name of the package.
     * @return the application's path
     */
    public static String getAppPath(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the application's version name.
     *
     * @return the application's version name
     */
    public static String getVersionName() {
        return getVersionName(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    public static String getVersionName(final String packageName) {
        if (isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the application's version code.
     *
     * @return the application's version code
     */
    public static int getVersionCode() {
        return getVersionCode(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    public static int getVersionCode(final String packageName) {
        if (isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 返回apk安装包Icon
     *
     * @param filePath apk 安装包路径.
     */
    public static Drawable getApkIconFromPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        try {
            PackageManager packageManager = ContextUtils.getContext().getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (packageArchiveInfo != null) {
                ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
                applicationInfo.sourceDir = filePath;
                applicationInfo.publicSourceDir = filePath;
                return applicationInfo.loadIcon(packageManager);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getApkNameFromPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        try {
            PackageManager packageManager = ContextUtils.getContext().getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (packageArchiveInfo != null) {
                ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
                applicationInfo.sourceDir = filePath;
                applicationInfo.publicSourceDir = filePath;
                return (String) applicationInfo.loadLabel(packageManager);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static AppInfo getAppInfo() {
        return getAppInfo(ContextUtils.getContext().getPackageName());
    }

    /**
     * Return the application's information.
     * <ul>
     * <li>name of package</li>
     * <li>icon</li>
     * <li>name</li>
     * <li>path of package</li>
     * <li>version name</li>
     * <li>version code</li>
     * <li>is system</li>
     * </ul>
     *
     * @param packageName The name of the package.
     * @return 当前应用的 AppInfo
     */
    public static AppInfo getAppInfo(final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the applications' information.
     *
     * @return the applications' information
     */
    public static List<AppInfo> getAllAppInfo() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = ContextUtils.getContext().getPackageManager();
        List<PackageInfo> installedPackages = getAllPackageInfo();
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) {
                continue;
            }
            list.add(ai);
        }
        return list;
    }

    @SuppressLint("NewApi")
    public static synchronized List<PackageInfo> getAllPackageInfo() {
        PackageManager packageManager = ContextUtils.getContext().getPackageManager();
        if (packageManager == null) {
            return null;
        }
        List<PackageInfo> packages = new LinkedList<>();
        int sizeBefore = 0;
        try {
            packages = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
            sizeBefore = packages.size();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (sizeBefore <= 10) {
            List<PackageInfo> list = getInstalledPackages(packageManager);
            if (list.size() > sizeBefore) {
                packages = list;
            }
        }

        return packages;
    }

    public static class AppInfo {
        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;
        private long packageSize = 0;
        private Signature[] signatures;
        private boolean isSystemSignature;
        private long firstInstallTime;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(final Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(final boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(final String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(final String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(final int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(final String versionName) {
            this.versionName = versionName;
        }

        public long getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(long packageSize) {
            this.packageSize = packageSize;
        }

        public Signature[] getSignatures() {
            return signatures;
        }

        public void setSignatures(Signature[] signatures) {
            this.signatures = signatures;
        }

        public boolean isSystemSignature() {
            return isSystemSignature;
        }

        public void setSystemSignature(boolean systemSignature) {
            isSystemSignature = systemSignature;
        }

        public long getFirstInstallTime() {
            return firstInstallTime;
        }

        public void setFirstInstallTime(long firstInstallTime) {
            this.firstInstallTime = firstInstallTime;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath,
                       String versionName, int versionCode, boolean isSystem, Signature[] signatures, long firstInstallTime) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
            this.setSignatures(signatures);
            this.setFirstInstallTime(firstInstallTime);
            if (packagePath != null) {
                File sourceFile = new File(packagePath);
                if (sourceFile.exists()) {
                    setPackageSize(sourceFile.length());
                }
            }

            Signature[] sysSignatures = SignatureUtils.getSystemSignature();
            if (sysSignatures != null && signatures != null) {
                HashSet<String> signaturesSet = new HashSet<>();
                for (Signature signature : signatures) {
                    String sigStr = SignatureUtils.parseSignatureMD5(signature);
                    if (!TextUtils.isEmpty(sigStr)) {
                        signaturesSet.add(sigStr);
                    }
                }

                for (Signature sysSignature : sysSignatures) {
                    String sysStr = SignatureUtils.parseSignatureMD5(sysSignature);
                    if (signaturesSet.contains(sysStr)) {
                        this.setSystemSignature(true);
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "pkg name: " + getPackageName() +
                    "\napp icon: " + getIcon() +
                    "\napp name: " + getName() +
                    "\napp path: " + getPackagePath() +
                    "\napp v name: " + getVersionName() +
                    "\napp v code: " + getVersionCode() +
                    "\nis system: " + isSystem();
        }
    }

    private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem, pi.signatures, pi.firstInstallTime);
    }

    private static List<PackageInfo> getInstalledPackages(PackageManager pm) {
        Process process = null;
        BufferedReader br = null;
        List<PackageInfo> list = new LinkedList<>();
        try {
            process = Runtime.getRuntime().exec("pm list packages");
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String pkgName;
            while ((pkgName = br.readLine()) != null) {
                pkgName = pkgName.substring(pkgName.indexOf(':') + 1);
                list.add(pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES));
            }
            process.waitFor();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }

        return list;
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        try {
            String[] args1 = {"chmod", "771", file.getPath().substring(0, file.getPath().lastIndexOf("/"))};
            Process p1 = Runtime.getRuntime().exec(args1);
            p1.waitFor();
            p1.destroy();
            String[] args2 = {"chmod", "777", file.getPath()};
            Process p2 = Runtime.getRuntime().exec(args2);
            p2.waitFor();
            p2.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(CommonFileProvider.getUriForFile(ContextUtils.getContext(), file), type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (ActivityUtils.resolveActivity(intent)) {
            return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
        }
        return null;
    }

    private static Intent getUninstallAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static Intent getLaunchAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = ContextUtils.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static String getForegroundProcessName() {
        ActivityManager am =
                (ActivityManager) ContextUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && pInfo.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance
                        == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName;
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            PackageManager pm = ContextUtils.getContext().getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list =
                    pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            Log.i("ProcessUtils", list.toString());
            if (list.size() <= 0) {
                Log.i("ProcessUtils",
                        "getForegroundProcessName: noun of access to usage information.");
                return "";
            }
            try {// Access to usage information.
                ApplicationInfo info =
                        pm.getApplicationInfo(ContextUtils.getContext().getPackageName(), 0);
                AppOpsManager aom =
                        (AppOpsManager) ContextUtils.getContext().getSystemService(Context.APP_OPS_SERVICE);
                //noinspection ConstantConditions
                if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != AppOpsManager.MODE_ALLOWED) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ContextUtils.getContext().startActivity(intent);
                }
                if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != AppOpsManager.MODE_ALLOWED) {
                    Log.i("ProcessUtils",
                            "getForegroundProcessName: refuse to device usage stats.");
                    return "";
                }
                UsageStatsManager usageStatsManager = (UsageStatsManager) ContextUtils.getContext()
                        .getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsList = null;
                if (usageStatsManager != null) {
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 86400000 * 7;
                    usageStatsList = usageStatsManager
                            .queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                    beginTime, endTime);
                }
                if (usageStatsList == null || usageStatsList.isEmpty()) {
                    return null;
                }
                UsageStats recentStats = null;
                for (UsageStats usageStats : usageStatsList) {
                    if (recentStats == null
                            || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                return recentStats == null ? null : recentStats.getPackageName();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
