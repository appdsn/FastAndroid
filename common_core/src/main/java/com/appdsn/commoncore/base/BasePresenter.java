package com.appdsn.commoncore.base;

import android.os.Bundle;

/**
 * Desc:所有Presenter必须继承该类
 */
public abstract class BasePresenter<V extends BaseView> {
    protected V mView;

    public void attachView(V view) {
        mView = view;
    }

    public void detachView() {
//        mView = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
    }


    protected void onDestroy() {
    }

    protected void onSaveInstanceState(Bundle outState) {
    }
}
