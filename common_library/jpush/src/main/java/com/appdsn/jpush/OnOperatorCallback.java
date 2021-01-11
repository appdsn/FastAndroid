package com.appdsn.jpush;

/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/12/9 11:17
 */
public interface OnOperatorCallback {
    void onSuccess(String value);

    void onFailed(int sequence, int errorCode);
}
