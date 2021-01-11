package com.appdsn.commoncore.utils.location;

/**
 * Desc:
 * <p>
 * Author: ZhangQi
 * Date: 2019/9/9
 * Copyright: Copyright (c) 2016-2020
 * ""
 * ""
 * Update Comments:
 *
 * @author zhangqi
 */
public interface OnLocationListener {
    void onLocationSuccess(LocationBean locationBean);

    void onLocationFailed(int errorCode, String errorInfo);
}
