package com.appdsn.commonbase.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdsn.commonbase.R;
import com.appdsn.commoncore.utils.DisplayUtils;

import java.util.ArrayList;

public class BottomDialog extends Dialog {
    private Builder mBuilder;

    public BottomDialog(Builder builder) {
        super(builder.context, R.style.BaseDialogTheme);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBuilder.mContentView > 0) {
            /*自定义布局*/
            setContentView(mBuilder.mContentView);
        } else {
            /*默认布局*/
            setContentView(R.layout.layout_bottom_dialog);
            initViews();
        }

        // 设置显示和隐藏动画
        getWindow().setWindowAnimations(R.style.bottom_dialog_anim);
        setCanceledOnTouchOutside(mBuilder.mCancelTouchOut);
        //改变弹窗宽度撑满屏幕
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    private void initViews() {
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        LinearLayout layoutAction = findViewById(R.id.layoutAction);
        LinearLayout layoutContent = findViewById(R.id.ll_content);
        /*填充数据*/
        tvTitle.setText(mBuilder.mTitle);
        tvCancel.setText(mBuilder.mCancelText);
        layoutContent.setBackgroundResource(mBuilder.mBgResource);

        for (int i = 0; i < mBuilder.mBtnList.size(); i++) {
            View btn = mBuilder.mBtnList.get(i);
            layoutAction.addView(btn);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuilder.mCancelListener != null) {
                    mBuilder.mCancelListener.onClick(v);
                } else {
                }
                cancel();
            }
        });
    }

    public static final class Builder {
        private Activity context;
        private boolean mCancelTouchOut = true;
        private int mContentView = -1;
        private ArrayList<View> mBtnList = new ArrayList<>();
        private int mBgResource = R.drawable.shape_bottom_dialog_bg;
        private String mTitle = "更多";
        private String mCancelText = "取消";
        private View.OnClickListener mCancelListener;

        public Builder(Activity context) {
            this.context = context;
        }

        public Builder customContentView(int contentView) {
            contentView = contentView;
            return this;
        }

        /*默认true可以取消*/
        public Builder canceledTouchOut(boolean canceled) {
            mCancelTouchOut = canceled;
            return this;
        }

        public Builder setTitle(int bgResource, String title) {
            mBgResource = bgResource;
            mTitle = title;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setCancelText(String cancelText) {
            mCancelText = cancelText;
            return this;
        }

        public Builder setCancelListener(View.OnClickListener cancelListener) {
            mCancelListener = cancelListener;
            return this;
        }

        public Builder addActionButton(String text, View.OnClickListener listener) {
            TextView btn = new TextView(context);
            btn.setText(text);
            btn.setOnClickListener(listener);
            btn.setTextSize(15);
            btn.setTextColor(0xCC191A38);
            btn.setGravity(Gravity.CENTER);
            btn.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(60));
            params.topMargin = DisplayUtils.dp2px(0.5f);
            btn.setLayoutParams(params);
            mBtnList.add(btn);
            return this;
        }

        public Builder addActionButton(View btn) {
            if (btn != null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = DisplayUtils.dp2px(0.5f);
                btn.setLayoutParams(params);
                mBtnList.add(btn);
            }
            return this;
        }

        public BottomDialog build() {
            return new BottomDialog(this);
        }
    }

}
