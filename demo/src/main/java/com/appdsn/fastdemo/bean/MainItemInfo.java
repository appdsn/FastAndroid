package com.appdsn.fastdemo.bean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @Desc: java类作用描述
 * <p>
 * @Author: wangbz
 * @CreateDate: 2019/10/14 16:40
 * @Copyright: Copyright (c) 2016-2020
 * @Company: @appdsn
 * @UpdateComments: 更新说明
 * @Version: 1.0
 */
public class MainItemInfo {
    public String title;
    public Intent intent;
    public Class<? extends Activity> destClass;
    public int spanSize = 1;

    public MainItemInfo(String title, Intent intent) {
        this.title = title;
        this.intent = intent;
    }

    public MainItemInfo(String title, Class<? extends Activity> destClass) {
        this.title = title;
        this.destClass = destClass;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void jump(Context context) {
        if (intent != null) {
            context.startActivity(intent);
        } else if (destClass != null) {
            context.startActivity(new Intent(context, destClass));
        }
    }
}
