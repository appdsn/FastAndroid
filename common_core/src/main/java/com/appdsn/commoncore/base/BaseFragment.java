package com.appdsn.commoncore.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdsn.commoncore.R;
import com.appdsn.commoncore.event.BindEventBus;
import com.appdsn.commoncore.event.EventBusHelper;
import com.appdsn.commoncore.event.EventMessage;
import com.appdsn.commoncore.utils.BarUtils;
import com.appdsn.commoncore.utils.DisplayUtils;
import com.appdsn.commoncore.utils.FragmentUtils;
import com.appdsn.commoncore.widget.EmptyLayout;
import com.appdsn.commoncore.widget.RootLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * 主要功能包括：支持ViewPager惰加载,重新定义生命周期（模板模式），
 * 加入ToolBar,跳转activity，通用的UI操作。
 */
public abstract class BaseFragment extends RxFragment {
    protected Activity mActivity;//持有Activity的引用,防止onDetach后，getActivity()== null空指针闪退
    public LayoutInflater mInflater;
    private EmptyLayout mLayBody;
    private FrameLayout mTitleBar;
    private TextView mTvCenterTitle;
    private TextView mTvLeftTitle;
    private ImageView mBtnLeft;
    private LinearLayout mLayRightBtn;
    private EmptyLayout mLayEmpty;
    private RootLayout mLayRoot;

    /*fragment可见性相关*/
    private boolean mCurVisible = false;
    private boolean mSaveVisible = true;
    private boolean mSupportLazy = false; //默认不支持惰加载
    private boolean mIsLoaded = false;//是否已经惰加载过数据
    private boolean mIsFirstStart = true;
    private boolean mTurnVisible = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    /*当detach后再attach，该方法不会调用*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mLayRoot = (RootLayout) inflater.inflate(R.layout.common_fragment_base, container, false);
        initBaseView();
        // 解决点击穿透问题
        mLayRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return mLayRoot;
    }

    protected void initBaseView() {
        // 内容区
        mLayBody = (EmptyLayout) mLayRoot.findViewById(R.id.layBody);
        //TitleBar相关
        mTitleBar = (FrameLayout) mLayRoot.findViewById(R.id.titleBar);
        mTvCenterTitle = (TextView) mLayRoot.findViewById(R.id.tvCenterTitle);
        mTvLeftTitle = (TextView) mLayRoot.findViewById(R.id.tvLeftTitle);
        mBtnLeft = (ImageView) mLayRoot.findViewById(R.id.btnLeft);
        mLayRightBtn = (LinearLayout) mLayRoot.findViewById(R.id.layRightBtn);
        setContentView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusHelper.register(this);
        }
        initVariable(getArguments());//初始化变量，获取携带的数据
        initViews(mLayBody, savedInstanceState);
        onViewInit();
        setListener();
    }

    protected void onViewInit() {

    }

    /**
     * 适合自己控制Fragment显示隐藏情况，要想实现懒加载，必须先hide所有Fragment，然后再show需要的显示的fragment，
     * 如果使用该方案实现懒加载，必须按要求实现逻辑，否则loadData方法不会调用
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        handleVisible(!hidden);
    }

    /**
     * 该方法会先于onCreate和onCreateView 执行,适合ViewPager切换情况
     * 且每次都在Fragment可见或不可见时调用一次，所以需要判断是否创建View
     * 该方案与onHiddenChanged方案同时只有一个生效，不会有冲突
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        handleVisible(isVisibleToUser);
    }

    private void handleVisible(boolean isVisibleToUser) {
        if (mIsFirstStart) {
            mSaveVisible = isVisibleToUser;
        } else {
            mTurnVisible = mSaveVisible = mCurVisible = isVisibleToUser;
            onVisibleToUser(isVisibleToUser);
            dispatchToChildren(isVisibleToUser);
            /*惰性加载数据*/
            if (mSupportLazy && mCurVisible && !mIsLoaded) {
                mIsLoaded = true;
                loadData();
            }
        }
    }

    private void dispatchToChildren(boolean visible) {
        try {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (fragment instanceof BaseFragment) {
                        BaseFragment baseFragment = (BaseFragment) fragment;
                        if (visible) {
                            baseFragment.turnChildStart();
                        } else {
                            baseFragment.turnChildStop();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstStart && mSaveVisible && getParentFragment() != null) {
            if (!getParentFragment().getUserVisibleHint()) {
                mSaveVisible = false;
                mTurnVisible = true;
            }
        }

        if (mSaveVisible) {
            mCurVisible = true;
            onVisibleToUser(true);
        }

        /*惰性加载数据*/
        if (mSupportLazy) {
            if (mCurVisible && !mIsLoaded) {
                mIsLoaded = true;
                loadData();
            }
        } else if (!mIsLoaded) {
            mIsLoaded = true;
            loadData();
        }
        mIsFirstStart = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCurVisible) {
            mSaveVisible = mCurVisible;
            mCurVisible = false;
            onVisibleToUser(false);
        }
    }

    /*ViewPager切换*/
    private void turnChildStop() {
        if (mCurVisible) {
            mSaveVisible = false;
            mTurnVisible = true;
            mCurVisible = false;
            onVisibleToUser(false);
        }
    }

    /*ViewPager切换*/
    private void turnChildStart() {
        if (mTurnVisible) {
            mSaveVisible = true;
            mCurVisible = true;
            onVisibleToUser(true);
            /*惰性加载数据*/
            if (mSupportLazy && mCurVisible && !mIsLoaded) {
                mIsLoaded = true;
                loadData();
            }
        }
    }

    /*每次Fragment可见都会调用*/
    protected void onVisibleToUser(boolean visible) {
        if (visible) {
            initStatusBar();
        }
    }

    protected void initStatusBar() {

    }

    protected void setSupportLazy(boolean supportLazy) {
        mSupportLazy = supportLazy;
    }

    /***
     * 设置内容区域
     */
    private void setContentView() {
        int resId = getLayoutResId();
        mLayEmpty = mLayBody;
        try {
            if (resId > 0) {
                View rootView = mInflater.inflate(resId, mLayBody, false);
                mLayBody.addView(rootView);
                mLayBody.setContentView(rootView);

                int contentId = getResources().getIdentifier("content", "id", getActivity().getPackageName());
                if (contentId > 0) {
                    View contentView = rootView.findViewById(contentId);
                    if (contentView != null) {
                        mLayEmpty = EmptyLayout.wrap(contentView);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    protected abstract int getLayoutResId();

    protected abstract void initVariable(Bundle arguments);

    protected abstract void initViews(FrameLayout bodyView, Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void loadData();

    protected RootLayout getRootView() {
        return mLayRoot;
    }

    protected FrameLayout getBodyView() {
        return mLayBody;
    }

    /************************************************
     * **********       ToolBar相关设置***************
     ************************************************/

    /*设置toolbar覆盖在内容区上面*/
    public void setTitleBarCover(boolean cover) {
        if (cover) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayBody.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            mLayBody.setLayoutParams(params);
        }
    }

    public FrameLayout getTitleBar() {
        return mTitleBar;
    }

    public void hideTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    public void showTitleBar() {
        mTitleBar.setVisibility(View.VISIBLE);
    }

    public void setTitleBarBackground(int resId) {
        mTitleBar.setBackgroundResource(resId);
    }

    /**
     * 设置中间标题
     */
    public TextView setCenterTitle(String title) {
        mTvCenterTitle.setText(title);
        return mTvCenterTitle;
    }

    /**
     * 设置中间标题和颜色
     */
    public TextView setCenterTitle(String title, int colorId) {
        mTvCenterTitle.setText(title);
        mTvCenterTitle.setTextColor(getResources().getColor(colorId));
        return mTvCenterTitle;
    }

    /**
     * 设置左边标题
     */
    public TextView setLeftTitle(String title) {
        mTvLeftTitle.setText(title);
        return mTvLeftTitle;
    }

    /**
     * 设置左边标题和颜色
     */
    public TextView setLeftTitle(String title, int colorId) {
        mTvLeftTitle.setText(title);
        mTvLeftTitle.setTextColor(getResources().getColor(colorId));
        return mTvLeftTitle;
    }

    /*隐藏左边返回按钮*/
    public void hideLeftButton() {
        mBtnLeft.setVisibility(View.GONE);
    }

    /*设置导航条左边的按钮，一般是返回按钮*/
    public ImageView setLeftButton(int iconResId, final View.OnClickListener onClickListener) {
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setImageResource(iconResId);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
            }
        });
        return mBtnLeft;
    }

    /*设置导航条右边的按钮*/
    public ImageView addRightButton(int iconResId, View.OnClickListener onClickListener) {
        ImageView rightBtn = (ImageView) mInflater.inflate(R.layout.common_layout_right_btn, mLayRightBtn, false);
        mLayRightBtn.addView(rightBtn);
        rightBtn.setImageResource(iconResId);
        rightBtn.setOnClickListener(onClickListener);
        return rightBtn;
    }

    /*设置导航条右边的按钮*/
    public ImageView addRightButton(int iconResId, int widthDp, int heightDp, int marginRightDp) {
        ImageView rightBtn = (ImageView) mInflater.inflate(R.layout.common_layout_right_btn, mLayRightBtn, false);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rightBtn.getLayoutParams();
        layoutParams.width = DisplayUtils.dp2px(widthDp);
        layoutParams.height = DisplayUtils.dp2px(heightDp + 28);//上下padding值14
        layoutParams.rightMargin = DisplayUtils.dp2px(marginRightDp);
        rightBtn.setImageResource(iconResId);
        mLayRightBtn.addView(rightBtn);
        return rightBtn;
    }

    public View addRightButton(int layoutResId) {
        View rightBtn = mInflater.inflate(layoutResId, mLayRightBtn, false);
        mLayRightBtn.addView(rightBtn);
        return rightBtn;
    }

    /*设置导航条右边的按钮和右边距*/
    public void addRightButton(final View btnView, LinearLayout.LayoutParams layoutParams) {
        btnView.setLayoutParams(layoutParams);
        mLayRightBtn.addView(btnView);
    }

    /***************************************************************
     ***********EmptyLayout,空状态，重试，加载等View相关设置***********
     ***************************************************************/
    /*空状态下的View*/
    public void setEmptyView(View emptyView) {
        mLayEmpty.setEmptyView(emptyView);
    }

    public void showEmptyView() {
        mLayEmpty.showEmpty();
    }

    public View getEmptyView() {
        return mLayEmpty.getEmptyView();
    }

    /*无网络出错View*/
    public void setErrorView(View errorView) {
        mLayEmpty.setErrorView(errorView);
    }

    public void showErrorView() {
        mLayEmpty.showError();
    }

    public View getErrorView() {
        return mLayEmpty.getErrorView();
    }

    /*加载动画View*/
    public void setLoadingView(View loadingView) {
        mLayEmpty.setLoadingView(loadingView);
    }

    public void showLoadingView() {
        mLayEmpty.showLoading();
    }

    public View getLoadingView() {
        return mLayEmpty.getLoadingView();
    }

    /*显示隐藏的内容区域*/
    public void showContentView() {
        mLayEmpty.showContent();
    }

    /***********************************************************
     ********************沉浸式状态栏设置*************************
     ***********************************************************/
    /*设置状态栏颜色*/
    protected void setStatusBarColor(@ColorRes int colorId) {
        BarUtils.setStatusBarColor(this, colorId);
    }

    /*设置沉浸式状态栏*/
    protected void setStatusBarTranslucent() {
        setStatusBarTranslucent(false);
    }

    /*设置沉浸式状态栏*/
    protected void setStatusBarTranslucent(boolean isDark) {
        BarUtils.setStatusBarTranslucentPaddingTop(this, mTitleBar, isDark);
    }

    /************************************************************
     ********************快速启动startActivity封装*****************
     ************************************************************/
    public void startActivity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mActivity, targetActivity);
        startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> targetActivity, Bundle bundle) {
        Intent intent = new Intent(mActivity, targetActivity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    /***********************************************
     * ********          fragment管理      **********
     **********************************************/
    public void replaceFragment(Fragment fragment, int containerId) {
        FragmentUtils.replace(getChildFragmentManager(), fragment, containerId);
    }

    //添加fragment
    protected void addFragment(Fragment fragment, int containerId) {
        FragmentUtils.add(getChildFragmentManager(), fragment, containerId);
    }

    //隐藏fragment
    protected void hideFragments(Fragment... fragments) {
        FragmentUtils.hide(fragments);
    }

    //隐藏fragment
    protected void showFragments(Fragment... fragments) {
        FragmentUtils.show(fragments);
    }

    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }

    @Override
    public void onDestroyView() {
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusHelper.unregister(this);
        }
        super.onDestroyView();
    }
}
