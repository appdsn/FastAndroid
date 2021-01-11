package com.appdsn.commoncore.widget.xrecyclerview.loadmore;

import android.view.View;

public abstract class ILoadView {
    public abstract int getLayoutResId();

    public abstract void initView(View loadView);

    public abstract void onStateChanged(LoadState curState);

}
