package com.appdsn.commoncore.widget.commondialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdsn.commoncore.R;

public class UDialog extends Dialog {
    UDialog(final UBuilder builder) {
        super(builder.context, R.style.BaseDialogTheme);
        setContentView(R.layout.common_dialog_layout);
        setCancelable(builder.cancelable);//是否可以通过返回键关闭
        setCanceledOnTouchOutside(builder.cancelable);//是否可以点击外面关闭
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        if (builder.cancelable) {
            findViewById(R.id.dia_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clear();
                }
            });
        }

        //标题
        TextView title = findViewById(R.id.dia_title);
        View lineTop = findViewById(R.id.dia_line_top);
        if (UCheck.isEmpty(builder.strTitle)) {
            title.setVisibility(View.GONE);
            lineTop.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            lineTop.setVisibility(View.VISIBLE);
            title.setText(builder.strTitle);
        }

        //图标
        ImageView icon = findViewById(R.id.dia_icon);
        if (builder.iconRes == -1) {
            icon.setVisibility(View.GONE);
        } else {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(builder.iconRes);
        }
        //通知内容
        TextView msg = findViewById(R.id.dia_msg);
        if (UCheck.isEmpty(builder.strMsg)) {
            msg.setVisibility(View.GONE);
        } else {
            msg.setText(builder.strMsg);
            msg.setVisibility(View.VISIBLE);
        }

        //取消、确认按钮:至少要显示一个
        TextView cancel = findViewById(R.id.dia_cancel);
        TextView confirm = findViewById(R.id.dia_confirm);
        View lineBottom = findViewById(R.id.dia_line_bottom);
        if (!UCheck.isEmpty(builder.strCancle) && !UCheck.isEmpty(builder.strConfirm)) {
            cancel.setVisibility(View.VISIBLE);
            lineBottom.setVisibility(View.VISIBLE);
            cancel.setText(builder.strCancle);
            confirm.setText(builder.strConfirm);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (builder.cancelClick == null) {
                        clear();
                    } else {
                        builder.cancelClick.onCancelClick(UDialog.this);
                    }
                }
            });
        } else {
            cancel.setVisibility(View.GONE);
            lineBottom.setVisibility(View.GONE);
            confirm.setText(UCheck.isNull(builder.strConfirm, "确定"));
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.confirmClick == null) {
                    clear();
                } else {
                    builder.confirmClick.onConfirmClick(UDialog.this);
                }
            }
        });
    }

    /**
     * @apiNote 可以通过返回键、点击外面关闭
     */
    public static UBuilder builder(Context context) {
        return new UBuilder(context).cancelable(true);
    }

    /**
     * @param msg 通知内容
     * @apiNote 可以通过返回键、点击外面关闭
     * 为了调用简单，集成通知内容
     */
    public static UBuilder builder(Context context, String msg) {
        return new UBuilder(context).cancelable(true).msg(msg);
    }

    /**
     * @param cancelable 是否可以通过返回键、点击外面关闭
     */
    public static UBuilder builder(Context context, boolean cancelable) {
        return new UBuilder(context).cancelable(cancelable);
    }

    /**
     * @apiNote 隐藏并释放资源
     */
    private void clear() {
        dismiss();
    }
}
