package com.appdsn.commonbase.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.appdsn.commonbase.R;
import com.appdsn.commoncore.utils.DisplayUtils;

/**
 * Desc:切换环境弹窗
 * <p>
 * Author: AnYaBo
 * Date: 2019/10/10
 * Copyright: Copyright (c) 2016-2022
 * Company:
 * Email:anyabo@appdsn.com
 * Update Comments:
 *
 * @author anyabo
 */
public class SwitchEnvironmentDialog extends Dialog {
    /**
     * 环境选项监听
     */
    private EnvironmentOptionListener mEnvironmentOptionListener;

    /**
     * 显示弹窗
     *
     * @param context        上下文
     * @param optionListener 选项监听
     */
    public static void showDialog(Context context, EnvironmentOptionListener optionListener) {
        new SwitchEnvironmentDialog(context, optionListener).show();
    }

    /**
     * 构造方法
     *
     * @param context        上下文
     * @param optionListener 选项监听
     */
    public SwitchEnvironmentDialog(@NonNull Context context,
                                   EnvironmentOptionListener optionListener) {
        super(context, R.style.switch_environment_dialog);
        mEnvironmentOptionListener = optionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_switch_environment_dialog);
        getWindow().setLayout((int) (DisplayUtils.getScreenWidth() * 0.66f),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.test_environment_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnvironmentOptionListener != null) {
                    mEnvironmentOptionListener.doCheckTestEnvironment(SwitchEnvironmentDialog.this);
                }
            }
        });
        findViewById(R.id.product_environment_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnvironmentOptionListener != null) {
                    mEnvironmentOptionListener.doCheckProductEnvironment(SwitchEnvironmentDialog.this);
                }
            }
        });
    }

    /**
     * 环境选项监听
     */
    public interface EnvironmentOptionListener {
        /**
         * 选择测试环境
         *
         * @param dialog 切环境弹窗
         */
        void doCheckTestEnvironment(Dialog dialog);

        /**
         * 选择生产环境
         *
         * @param dialog 切环境弹窗
         */
        void doCheckProductEnvironment(Dialog dialog);
    }

}
