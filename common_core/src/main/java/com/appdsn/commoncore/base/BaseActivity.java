package com.appdsn.commoncore.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 主要功能包括：重新定义生命周期（模板模式），
 * 加入ToolBar,跳转activity，通用的UI操作。
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public Context mContext;
    public LayoutInflater mInflater;
    private RootLayout mLayRoot;
    private EmptyLayout mLayBody;
    private EmptyLayout mLayEmpty;

    private FrameLayout mTitleBar;
    private TextView mTvCenterTitle;
    private TextView mTvLeftTitle;
    private ImageView mBtnLeft;
    private LinearLayout mLayRightBtn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    /* 子类使用的时候无需再次调用onCreate(),如需要加载其他方法可重写该方法 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉ActionBar
        super.setContentView(R.layout.common_activity_base);
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusHelper.register(this);
        }
        initBaseView();
        onViewCreated(mLayBody, savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
        }
    }

    protected void initBaseView() {
        //常用的对象
        mContext = this;
        mInflater = getLayoutInflater();
        /*根布局*/
        mLayRoot = (RootLayout) findViewById(R.id.layRoot);
        // 内容区
        mLayBody = (EmptyLayout) findViewById(R.id.layBody);
        //TitleBar相关
        mTitleBar = (FrameLayout) findViewById(R.id.titleBar);

        mTvCenterTitle = (TextView) findViewById(R.id.tvCenterTitle);
        mTvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        mBtnLeft = (ImageView) findViewById(R.id.btnLeft);
        mLayRightBtn = (LinearLayout) findViewById(R.id.layRightBtn);
        setContentView();
    }

    /***
     * 设置内容区域
     */
    private void setContentView() {
        mLayEmpty = mLayBody;
        try {
            int resId = getLayoutResId();
            if (resId > 0) {
                View rootView = mInflater.inflate(resId, mLayBody, false);
                mLayBody.addView(rootView);
                mLayBody.setContentView(rootView);

                /*给id是content的view包裹一个空状态EmptyLayout*/
                int contentId = getResources().getIdentifier("content", "id", getPackageName());
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

    protected void onViewCreated(View bodyView, Bundle savedInstanceState) {
        initVariable(getIntent());
        initViews((FrameLayout) bodyView, savedInstanceState);
        onViewInit();
        initStatusBar();
        setListener();
        loadData();
    }

    protected void onViewInit() {

    }

    protected abstract int getLayoutResId();

    protected abstract void initVariable(Intent intent);//包括Intent上的数据和Activity内部使用的变量

    protected abstract void initViews(FrameLayout bodyView, Bundle savedInstanceState);//初始化View控件

    protected abstract void setListener();

    protected abstract void loadData();//请求数据

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initStatusBar() {

    }

    protected RootLayout getRootView() {
        return mLayRoot;
    }

    protected FrameLayout getBodyView() {
        return mLayBody;
    }

    /************************************************
     ******************TitleBar相关设置***************
     ************************************************/
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

    /*设置toolbar覆盖在内容区上面*/
    public void setTitleBarCover(boolean cover) {
        if (cover) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayBody.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            mLayBody.setLayoutParams(params);
        }
    }

    /**
     * 设置中间标题
     */
    public TextView setCenterTitle(String title) {
        mTvCenterTitle.setText(title);
        return mTvCenterTitle;
    }

    public TextView setTitle(String title) {
        return setCenterTitle(title);
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
        layoutParams.width = widthDp > 0 ? DisplayUtils.dp2px(widthDp) : widthDp;
        layoutParams.height = heightDp > 0 ? DisplayUtils.dp2px(heightDp + 28) : heightDp;//上下padding值14
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
        if (layoutParams != null) {
            btnView.setLayoutParams(layoutParams);
        }
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

    /************************************************************
     ********************快速启动startActivity封装*****************
     ************************************************************/
    public void startActivity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        super.startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> targetActivity, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtras(bundle);
        super.startActivity(intent);
    }

    /*startActivity(Intent intent)最终也会调用这个方法，所以只需这一处判断*/
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
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

    /*******************************************************
     *********************fragment管理***********************
     *******************************************************/
    public void replaceFragment(Fragment fragment, int containerId) {
        FragmentUtils.replace(getSupportFragmentManager(), fragment, containerId);
    }

    //添加fragment
    protected void addFragment(Fragment fragment, int containerId) {
        FragmentUtils.add(getSupportFragmentManager(), fragment, containerId, "tag" + fragment.hashCode(), false, false);
    }

    //添加fragment
    protected void addFragmentStack(Fragment fragment, int containerId) {
        FragmentUtils.add(getSupportFragmentManager(), fragment, containerId, "tag" + fragment.hashCode(), true, R.anim.slide_right_enter, 0, 0, R.anim.slide_right_exit);
    }

    protected void popFragmentStack() {
        FragmentUtils.pop(getSupportFragmentManager());
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
    protected void onDestroy() {
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusHelper.unregister(this);
        }
        super.onDestroy();
    }
}
