package com.appdsn.commoncore.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.appdsn.commoncore.R;
import com.appdsn.commoncore.utils.ActivityUtils;
import com.appdsn.commoncore.utils.ThreadUtils;

import java.lang.reflect.Field;

public abstract class BaseDialogFragment extends DialogFragment {
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        this.mActivity = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResId() <= 0) {
            return null;
        }
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        if (null != this.getDialog()) {
            this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        initViews(rootView);
        return rootView;
    }

    @Override
    public int getTheme() {
        int theme = setTheme();
        if (theme != View.NO_ID) {
            return theme;
        }
        return R.style.BaseDialogTheme;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        setWindowStyle(window);
        return dialog;
    }

    protected abstract int getLayoutResId();

    protected abstract void initViews(View rootView);

    protected abstract int setTheme();

    protected abstract void setWindowStyle(Window window);


    /**
     * 反射解决 FragmentDialog 在activity 调用onSaveInstanceState之后调用show的问题
     *
     * @param manager The FragmentManager this fragment will be added to.
     * @param tag     The tag for this fragment, as per
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Class c = DialogFragment.class;
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public void show(final FragmentActivity activity) {
        final String tag = getClass().getSimpleName();
        ThreadUtils.runOnUiThread(new Runnable() {
            @SuppressLint("CommitTransaction")
            @Override
            public void run() {
                if (!ActivityUtils.isDestroyed(activity)) {
                    FragmentManager fm = activity.getSupportFragmentManager();
                    Fragment prev = fm.findFragmentByTag(tag);
                    if (prev != null) {
                        fm.beginTransaction().remove(prev);
                    }
                    BaseDialogFragment.super.show(fm, tag);
                }
            }
        });
    }

    @Override
    public void dismiss() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityUtils.isDestroyed(mActivity)) {
                    BaseDialogFragment.super.dismissAllowingStateLoss();
                }
            }
        });
    }
}
