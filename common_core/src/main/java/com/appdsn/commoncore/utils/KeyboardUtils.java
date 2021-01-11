package com.appdsn.commoncore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.lang.reflect.Field;


/**
 * Desc:键盘显示和隐藏
 *
 * @Author: wangbaozhong 2019/10/28 13:54
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class KeyboardUtils {

    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Show the soft input.
     */
    public static void showSoftInput(Activity activity) {
        if (!isSoftInputVisible(activity)) {
            toggleSoftInput();
        }
    }

    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    public static void showSoftInput(final View view) {
        showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * Show the soft input.
     *
     * @param view  The view.
     * @param flags Provides additional operating flags.  Currently may be
     *              0 or have the {@link InputMethodManager#SHOW_IMPLICIT} bit set.
     */
    public static void showSoftInput(final View view, final int flags) {
        InputMethodManager imm =
                (InputMethodManager) ContextUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput();
                }
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(final Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            View decorView = activity.getWindow().getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(activity);
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) ContextUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Toggle the soft input display or not.
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) ContextUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(0, 0);
    }

    private static int sDecorViewDelta = 0;

    /**
     * Return whether soft input is visible.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        return getDecorViewInvisibleHeight(activity.getWindow()) > 0;
    }

    private static int getDecorViewInvisibleHeight(final Window window) {
        final View decorView = window.getDecorView();
        if (decorView == null) {
            return 0;
        }
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight() + getStatusBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    /**
     * Register soft input changed listener.
     *
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final Activity activity,
                                                        final OnSoftInputChangedListener listener) {
        registerSoftInputChangedListener(activity.getWindow(), listener);
    }

    /**
     * Register soft input changed listener.
     *
     * @param window   The window.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final Window window,
                                                        final OnSoftInputChangedListener listener) {
        final int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final FrameLayout contentView = window.findViewById(android.R.id.content);
        final int[] decorViewInvisibleHeightPre = {getDecorViewInvisibleHeight(window)};
        contentView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int height = getDecorViewInvisibleHeight(window);
                        if (decorViewInvisibleHeightPre[0] != height) {
                            listener.onSoftInputChanged(height);
                            decorViewInvisibleHeightPre[0] = height;
                        }
                    }
                });
    }

    /**
     * Fix the bug of 5497 in Android.
     * <p>Don't set adjustResize</p>
     *
     * @param activity The activity.
     */
    public static void fixAndroidBug5497(final Activity activity) {
        fixAndroidBug5497(activity.getWindow());
    }

    /**
     * Fix the bug of 5497 in Android.
     * <p>Don't set adjustResize</p>
     *
     * @param window The window.
     */
    public static void fixAndroidBug5497(final Window window) {
//        int softInputMode = window.getAttributes().softInputMode;
//        window.setSoftInputMode(softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final FrameLayout contentView = window.findViewById(android.R.id.content);
        final View contentViewChild = contentView.getChildAt(0);
        final int paddingBottom = contentViewChild.getPaddingBottom();
        final int[] contentViewInvisibleHeightPre5497 = {getContentViewInvisibleHeight(window)};
        contentView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int height = getContentViewInvisibleHeight(window);
                        if (contentViewInvisibleHeightPre5497[0] != height) {
                            contentViewChild.setPadding(
                                    contentViewChild.getPaddingLeft(),
                                    contentViewChild.getPaddingTop(),
                                    contentViewChild.getPaddingRight(),
                                    paddingBottom + getDecorViewInvisibleHeight(window)
                            );
                            contentViewInvisibleHeightPre5497[0] = height;
                        }
                    }
                });
    }

    /**
     * Fix the leaks of soft input.
     * <p>Call the function in {Activity#onDestroy()}.</p>
     */
    public static void fixSoftInputLeaks(final Activity activity) {
        Window window = activity.getWindow();
        InputMethodManager imm =
                (InputMethodManager) ContextUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] leakViews = new String[]{"mLastSrvView", "mCurRootView", "mServedView", "mNextServedView"};
        for (String leakView : leakViews) {
            try {
                Field leakViewField = InputMethodManager.class.getDeclaredField(leakView);
                if (leakViewField == null) {
                    continue;
                }
                if (!leakViewField.isAccessible()) {
                    leakViewField.setAccessible(true);
                }
                Object obj = leakViewField.get(imm);
                if (!(obj instanceof View)) {
                    continue;
                }
                View view = (View) obj;
                if (view.getRootView() == window.getDecorView().getRootView()) {
                    leakViewField.set(imm, null);
                }
            } catch (Throwable ignore) {/**/}
        }
    }

    private static int getContentViewInvisibleHeight(final Window window) {
        final View contentView = window.findViewById(android.R.id.content);
        if (contentView == null) {
            return 0;
        }
        final Rect outRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(contentView.getBottom() - outRect.bottom);
        if (delta <= getStatusBarHeight() + getNavBarHeight()) {
            return 0;
        }
        return delta;
    }


    private static int getStatusBarHeight() {
        Resources resources = ContextUtils.getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static int getNavBarHeight() {
        Resources res = ContextUtils.getContext().getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    private static Activity getActivityByView(View view) {
        return getActivityByContext(view.getContext());
    }

    private static Activity getActivityByContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }

}