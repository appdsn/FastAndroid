package com.appdsn.commoncore.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Activity生命周期管理类
 *
 * @Author: wangbaozhong
 * @Date:
 */
public class ActivityUtils {

    private ActivityUtils() {
    }

    public static class ActivityLifecycleCallbacks {

        public void onActivityCreated(Activity activity) {/**/}

        public void onActivityStarted(Activity activity) {/**/}

        public void onActivityResumed(Activity activity) {/**/}

        public void onActivityPaused(Activity activity) {/**/}

        public void onActivityStopped(Activity activity) {/**/}

        public void onActivityDestroyed(Activity activity) {/**/}

        public void onLifecycleChanged(Activity activity, Lifecycle.Event event) {/**/}
    }

    /**
     * Add callbacks of activity lifecycle.
     *
     * @param activity  The activity.
     * @param callbacks The callbacks.
     */
    public static void addActivityLifecycleCallbacks(final Activity activity,
                                                     final ActivityUtils.ActivityLifecycleCallbacks callbacks) {
        ActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
    }

    /**
     * Remove callbacks of activity lifecycle.
     *
     * @param activity The activity.
     */
    public static void removeActivityLifecycleCallbacks(final Activity activity) {
        ActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
    }

    /**
     * Remove callbacks of activity lifecycle.
     *
     * @param activity  The activity.
     * @param callbacks The callbacks.
     */
    public static void removeActivityLifecycleCallbacks(final Activity activity,
                                                        final ActivityUtils.ActivityLifecycleCallbacks callbacks) {
        ActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    public static List<Activity> getActivityList() {
        return ActivityLifecycleImpl.INSTANCE.getActivityList();
    }

    /**
     * Start home activity.
     */
    public static void startHomeActivity() {
        try {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(homeIntent);
        } catch (Exception e) {//throws SecurityException
        }
    }

    /**
     * Start the launcher activity.
     */
    public static void startLauncherActivity() {
        startLauncherActivity(ContextUtils.getContext().getPackageName());
    }

    /**
     * Start the launcher activity.
     *
     * @param pkg The name of the package.
     */
    public static void startLauncherActivity(final String pkg) {
        startActivity(getLaunchAppIntent(pkg));
    }

    /**
     * Start the activity.
     *
     * @param activity The host activity.
     * @param clz      will be start activity class.
     */
    public static boolean startActivityForResult(Activity activity,
                                                 final Class<? extends Activity> clz,
                                                 final int requestCode) {
        Intent intent = new Intent(ContextUtils.getContext(), clz);
        return startActivityForResult(activity, intent, requestCode, -1, -1);
    }

    public static boolean startActivityForResult(Activity activity,
                                                 final Class<? extends Activity> clz,
                                                 final int requestCode,
                                                 final Bundle extras) {
        Intent intent = new Intent(ContextUtils.getContext(), clz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return startActivityForResult(activity, intent, requestCode, -1, -1);
    }


    /**
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     * @param requestCode If >= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    @SuppressLint("ResourceType")
    public static boolean startActivityForResult(final Activity activity,
                                                 final Intent intent,
                                                 final int requestCode,
                                                 @AnimRes final int enterAnim,
                                                 @AnimRes final int exitAnim) {
        if (!resolveActivity(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (enterAnim >= 0 || exitAnim >= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeCustomAnimation(activity, enterAnim, exitAnim).toBundle());
            } else {
                activity.startActivityForResult(intent, requestCode);
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        return true;
    }

    public static void startActivity(final Class<? extends Activity> clz) {
        Intent intent = new Intent(ContextUtils.getContext(), clz);
        startActivity(intent);
    }

    /**
     * Start the activity.
     *
     * @param clz The activity class.
     */
    public static void startActivity(final Class<? extends Activity> clz, final Bundle extras) {
        Intent intent = new Intent(ContextUtils.getContext(), clz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }

    public static void startActivity(final Intent intent) {
        startActivity(intent, -1, -1);
    }

    public static boolean startActivity(final Intent intent, final int enterAnim, final int exitAnim) {
        if (!resolveActivity(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        Context context = getTopActivity() != null ? getTopActivity() : ContextUtils.getContext();
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (enterAnim >= 0 || exitAnim >= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle());
            } else {
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
                }
            }
        } else {
            context.startActivity(intent);
        }
        return true;
    }

    public static void finishActivity(final Class<? extends Activity> clz) {
        finishActivity(clz, false);
    }

    /**
     * Finish the activity.
     *
     * @param clz        The activity class.
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishActivity(final Class<? extends Activity> clz,
                                      final boolean isLoadAnim) {
        List<Activity> activities = getActivityList();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                if (!isLoadAnim) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }

    /**
     * Finish the activity.
     *
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishActivity(final Class<? extends Activity> clz,
                                      @AnimRes final int enterAnim,
                                      @AnimRes final int exitAnim) {
        List<Activity> activities = getActivityList();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

    /**
     * Finish all of activities.
     */
    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    /**
     * Finish all of activities.
     *
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishAllActivities(final boolean isLoadAnim) {
        List<Activity> activityList = getActivityList();
        for (Activity act : activityList) {
            // sActivityList remove the index activity at onActivityDestroyed
            act.finish();
            if (!isLoadAnim) {
                act.overridePendingTransition(0, 0);
            }
        }
    }

    /**
     * Return the top activity in activity's stack.
     *
     * @return the top activity in activity's stack
     */
    public static Activity getTopActivity() {
        return ActivityLifecycleImpl.INSTANCE.getTopActivity();
    }

    public static Intent getLaunchAppIntent(final String pkg) {
        Intent intent = ContextUtils.getContext().getPackageManager().getLaunchIntentForPackage(pkg);
        if (intent == null) {
            return null;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * Return the name of launcher activity.
     *
     * @param pkg The name of the package.
     * @return the name of launcher activity
     */
    public static String getLauncherActivity(final String pkg) {
        if (TextUtils.isEmpty(pkg)) {
            return "";
        }
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkg);
        PackageManager pm = ContextUtils.getContext().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        if (info == null || info.size() == 0) {
            return "";
        }
        return info.get(0).activityInfo.name;
    }

    /**
     * Return whether the activity exists.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean resolveActivity(final Intent intent) {
        try {
            return ContextUtils.getContext()
                    .getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    .size() > 0;
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean isDestroyed(Activity activity) {
        return !(activity != null && !activity.isFinishing()
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed()));
    }
}
