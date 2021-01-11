package com.appdsn.commoncore.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.ParameterizedType;

/**
 * Desc:提供MVP模式自动注入
 */
public abstract class BaseMVPFragment<V extends BaseView, T extends BasePresenter<V>> extends BaseFragment implements BaseView {
    public T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = getInstance(this, 1);
        if (mPresenter != null) {
            mPresenter.onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
