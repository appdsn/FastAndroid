package com.appdsn.commonbase.base;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appdsn.commonbase.R;
import com.appdsn.commonbase.statistics.PageStatisticsEvent;
import com.appdsn.commonbase.statistics.StatisticsPage;
import com.appdsn.commonbase.statistics.TalkingDataUtils;
import com.appdsn.commonbase.utils.ARouterUtils;
import com.appdsn.commoncore.base.BaseMVPFragment;
import com.appdsn.commoncore.base.BasePresenter;
import com.appdsn.commoncore.base.BaseView;
import com.appdsn.commoncore.utils.DisplayUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseAppFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMVPFragment<V, P> {
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
    private Unbinder mUnBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mPageEvent = getPageEvent();
        initStatisticsData(savedInstanceState);
    }

    public void setLeftTitleMargin(View view) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(DisplayUtils.dp2px(16), 0, 0, 0);
    }

    public TextView setCenterTitleBold(TextView tvTitle) {
        tvTitle.setTypeface(Typeface.DEFAULT);
        TextPaint paint = tvTitle.getPaint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.7f);
        return tvTitle;
    }

    /**
     * 1，如果当前页不需要统计，可以设置eventCode为空,
     * 2，如果当前页是个空壳页面，比如父Activity，或者父Fragment，
     * 希望子Fragment页面使用上一个页面的sourceId,可以可以return null。
     */
    public PageStatisticsEvent getPageEvent() {
        return null;
    }

    /*希望自定义SourcePageId*/
    public void setSourcePage(String sourcePageId) {
        mSourcePageId = sourcePageId;
        if (mStatisticsPage != null) {
            mStatisticsPage.setSourcePageId(sourcePageId);
        }
    }

    /*针对一个页面对应多种状态，可以根据条件手动设置页面事件*/
    public void setPageEvent(PageStatisticsEvent pageEvent) {
        mPageEvent = pageEvent;
        initStatisticsData(null);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SourcePageId", mSourcePageId);
    }

    @Override
    protected void onVisibleToUser(boolean visible) {
        super.onVisibleToUser(visible);
        if (visible) {
            if (mStatisticsPage != null) {
                TalkingDataUtils.setTopStatisticsPage(mStatisticsPage);
            }
            if (mPageEvent != null) {
                TalkingDataUtils.onPageStart(mPageEvent.getEventCode());
            }
        } else {
            if (mPageEvent != null) {
                TalkingDataUtils.onPageEnd(mPageEvent.getEventCode());
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnBinder = ButterKnife.bind(this, getRootView());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        View emptyView = initEmptyView();
        setEmptyView(emptyView);
        View errorView = initErrorView();
        setErrorView(errorView);
        initLoadingView();
        setTitleBarBackground(R.color.white);
    }

    protected View initErrorView() {
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
        return errorView;
    }

    protected View initEmptyView() {
        View emptyView = mInflater.inflate(R.layout.common_empty_view, null);
        mEmptyImg = emptyView.findViewById(R.id.ivImg);
        mEmptyDesc = emptyView.findViewById(R.id.tvDesc);

        setEmptyView(emptyView);
        emptyView.setOnClickListener(mRetryListener);
        return emptyView;
    }

    public void setEmptyView(@DrawableRes int resId, String value) {
        mEmptyImg.setImageResource(resId);
        mEmptyDesc.setText(value);
    }

    /**
     * 设置空数据页面
     *
     * @param imageSize 图片大小，单位dp，默认145dp
     */
    public void setEmptyView(int imageSize) {
        if (imageSize < 0) {
            imageSize = DisplayUtils.dp2px(145);
        } else {
            imageSize = DisplayUtils.dp2px(imageSize);
        }
        if (mEmptyImg.getLayoutParams() != null) {
            mEmptyImg.getLayoutParams().width = imageSize;
            mEmptyImg.getLayoutParams().height = imageSize;
        }
    }

    public void setErrorView(@DrawableRes int resId, String value) {
        mErrorImg.setImageResource(resId);
        mErrorDesc.setText(value);
    }

    public void setEmptyView(String value) {
        mEmptyDesc.setText(value);
    }

    public void setErrorView(String value) {
        mErrorDesc.setText(value);
    }

    /*todo 暂时无此需求*/
    private void initLoadingView() {
        View loadingView = mInflater.inflate(R.layout.common_loading_view, null);
        setLoadingView(loadingView);
    }

    public void setLoadingView(@DrawableRes int resId, String value) {

    }

    public void setRetryListener(View.OnClickListener listener) {
        mRetryListener = listener;
    }

    /************************************************************
     ********************快速启动startActivity封装*****************
     ************************************************************/
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

}
