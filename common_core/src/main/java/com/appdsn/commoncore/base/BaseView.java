package com.appdsn.commoncore.base;

import android.content.Context;

/**
 * Desc:MVP模式中View接口必须实现该接口
 */
public interface BaseView {
    /**
     * @return 目前获取的是Activity实例
     */
    Context getContext();
}
