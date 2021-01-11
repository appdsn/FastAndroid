package com.appdsn.commoncore.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.appdsn.commoncore.R;
import com.appdsn.commoncore.utils.ActivityUtils;
import com.appdsn.commoncore.utils.ThreadUtils;

public abstract class BaseDialog extends Dialog {

    protected Activity mActivity;

    public abstract int getLayoutResId();

    public abstract void initView();

    public abstract void setWindowStyle(Window window);

    public BaseDialog(@NonNull Activity activity) {
        this(activity, R.style.BaseDialogTheme);
    }

    public BaseDialog(@NonNull Activity activity, int themeResId) {
        super(activity, themeResId);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        setWindowStyle(getWindow());
    }

    @Override
    public void show() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ActivityUtils.isDestroyed(mActivity)) {
                    BaseDialog.super.show();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ActivityUtils.isDestroyed(mActivity)) {
                    BaseDialog.super.dismiss();
                }
            }
        });
    }
}