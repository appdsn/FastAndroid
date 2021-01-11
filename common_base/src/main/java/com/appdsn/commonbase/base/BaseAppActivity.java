package com.appdsn.commonbase.base;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appdsn.commonbase.R;
import com.appdsn.commonbase.statistics.PageStatisticsEvent;
import com.appdsn.commonbase.statistics.StatisticsPage;
import com.appdsn.commonbase.statistics.TalkingDataUtils;
import com.appdsn.commonbase.utils.ARouterUtils;
import com.appdsn.commoncore.base.BaseMVPActivity;
import com.appdsn.commoncore.base.BasePresenter;
import com.appdsn.commoncore.base.BaseView;
import com.appdsn.commoncore.event.EventMessage;
import com.appdsn.commoncore.utils.DisplayUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseAppActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseMVPActivity<V, P> {
    public String TAG = this.getClass().getSimpleName();
    public static boolean isBannedAlready = false;
    private LinearLayout mEmptyLayout;
    private ImageView mEmptyImg;
    private TextView mEmptyDesc;
    private ImageView mErrorImg;
    private TextView mErrorDesc;
    private ImageView mLoadingImg;
    private TextView mLoadingDesc;
    private View.OnClickListener mRetryListener;

    private PageStatisticsEvent mPageEvent;//当前页面事件
    private String mSourcePageId = "";//跳转来源页ID，用于统计
    private String mCurPageId;
    private StatisticsPage mStatisticsPage;
    private TextView mTvRightTitle;
    private Unbinder mUnBinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageEvent = getPageEvent();
        initStatisticsData(savedInstanceState);
    }

    public TextView setCenterTitleBold(TextView tvTitle) {
        tvTitle.setTypeface(Typeface.DEFAULT);
        TextPaint paint = tvTitle.getPaint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.7f);
        return tvTitle;
    }

    /**
     * 设置左边标题和颜色
     */
    public TextView setRightTitle(String title, int colorId) {
        mTvRightTitle = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = DisplayUtils.dp2px(15);
        addRightButton(mTvRightTitle, params);
        mTvRightTitle.setText(title);
        mTvRightTitle.setTextColor(getResources().getColor(colorId));
        return mTvRightTitle;
    }

    public TextView getRigght() {
        return mTvRightTitle;
    }

    public TextView setRightTitleType() {
        if (mTvRightTitle != null) {
            mTvRightTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        return mTvRightTitle;
    }

    /*初始化统计字段*/
    private void initStatisticsData(Bundle savedInstanceState) {
        if (mPageEvent != null) {
            if (TextUtils.isEmpty(mSourcePageId)) {
                StatisticsPage sourcePage = TalkingDataUtils.getTopStatisticsPage();
                if (sourcePage == null) {
                    sourcePage = TalkingDataUtils.getTopStatisticsPage();
                }
                if (sourcePage != null) {
                    mSourcePageId = sourcePage.getCurPageId();
                }
            }
            if (savedInstanceState != null) {
                mSourcePageId = savedInstanceState.getString("SourcePageId");
            }
            mCurPageId = mPageEvent.getCurPageId();
            mStatisticsPage = new StatisticsPage(mSourcePageId, mCurPageId);
        } else {
            mStatisticsPage = null;
        }
    }

    /*针对一个页面对应多种状态，可以根据条件手动设置页面事件*/
    public void setPageEvent(PageStatisticsEvent pageEvent) {
        mPageEvent = pageEvent;
        initStatisticsData(null);
    }

    /*希望自定义SourcePageId*/
    public void setSourcePage(String sourcePageId) {
        mSourcePageId = sourcePageId;
        if (mStatisticsPage != null) {
            mStatisticsPage.setSourcePageId(sourcePageId);
        }
    }

    /**
     * 1，如果当前页不需要统计，可以设置eventCode为空,
     * 2，如果当前页是个空壳页面，比如父Activity，或者父Fragment，
     * 希望子Fragment页面使用上一个页面的sourceId,可以可以return null。
     */
    public PageStatisticsEvent getPageEvent() {
        return mPageEvent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SourcePageId", mSourcePageId);
    }

    /**
     * 是否返回键是白色
     *
     * @return 默认白色 需要更改黑色，重写修改为false
     */
    protected boolean isWhiteBackIcon() {
        return false;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        mUnBinder = ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        initTitleBarView();
        setStatusBarColor(R.color.white);

        initEmptyView();
        initErrorView();
        initLoadingView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStatisticsPage != null) {
            TalkingDataUtils.setTopStatisticsPage(mStatisticsPage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPageEvent != null) {
            TalkingDataUtils.onPageStart(mPageEvent.getEventCode());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPageEvent != null) {
            TalkingDataUtils.onPageEnd(mPageEvent.getEventCode());
        }
    }

    /**
     * 初始化全局标题栏
     */
    private void initTitleBarView() {

    }

    private void initErrorView() {
        View errorView = mInflater.inflate(R.layout.common_empty_view, null);
        mErrorImg = errorView.findViewById(R.id.ivImg);
        mErrorDesc = errorView.findViewById(R.id.tvDesc);

        setErrorView(errorView);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onClick(v);
                }
            }
        });
    }

    private void initEmptyView() {
        View emptyView = mInflater.inflate(R.layout.common_empty_view, null);
        mEmptyImg = emptyView.findViewById(R.id.ivImg);
        mEmptyDesc = emptyView.findViewById(R.id.tvDesc);

        setEmptyView(emptyView);
        emptyView.setOnClickListener(mRetryListener);
    }


    public TextView setEmptyView(@DrawableRes int resId, String value) {
        mEmptyImg.setImageResource(resId);
        mEmptyDesc.setText(value);
        return mEmptyDesc;
    }

    public TextView setErrorView(@DrawableRes int resId, String value) {
        mErrorImg.setImageResource(resId);
        mErrorDesc.setText(value);
        return mErrorDesc;
    }

    public TextView setEmptyView(String value) {
        mEmptyDesc.setText(value);
        return mEmptyDesc;
    }

    public TextView setErrorView(String value) {
        mErrorDesc.setText(value);
        return mErrorDesc;
    }

    private void initLoadingView() {
        View loadingView = mInflater.inflate(R.layout.common_loading_view, null);
        setLoadingView(loadingView);
    }

    public void setLoadingView(@DrawableRes int resId, String value) {

    }

    public void setRetryListener(View.OnClickListener listener) {
        mRetryListener = listener;
    }

    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
        if (event.getData() == null) {
            return;
        }
    }

    /**
     * 含有flags通过ARouter跳转界面
     *
     * @param path   跳转地址
     * @param flags  flags
     * @param bundle bundle
     **/
    public void startActivity(String path, int[] flags, Bundle bundle) {
        startActivityForResult(path, flags, bundle, -1);
    }

    public void startActivity(String path) {
        startActivityForResult(path, null, null, -1);
    }

    public void startActivity(String path, Bundle bundle) {
        startActivityForResult(path, null, bundle, -1);
    }

    /**
     * 带返回含有Bundle通过ARouter跳转界面
     *
     * @param path        跳转地址
     * @param requestCode requestCode
     * @param bundle      bundle
     **/
    public void startActivityForResult(String path, int[] flags, Bundle bundle, int requestCode) {
        ARouterUtils.navigation(this, path, flags, bundle, requestCode);
    }

    private void setTitleBold() {
        TextPaint paint = setCenterTitle("").getPaint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.7f);
    }
}
