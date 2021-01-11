package com.appdsn.commonbase.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdsn.commonbase.R;
import com.appdsn.commoncore.widget.pullrefreshlayout.IRefreshView;
import com.appdsn.commoncore.widget.pullrefreshlayout.PullRefreshLayout;

public class GetPullRefreshLayout extends PullRefreshLayout {
    public GetPullRefreshLayout(Context context) {
        super(context);
    }

    private GetRefreshHeaderView headerView;

    public GetPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        GetRefreshHeaderView headerView = new GetRefreshHeaderView(context);
        setHeaderView(headerView);
        this.headerView = headerView;
    }

    public void setHeaderViewBackground(int resid) {
        if (null != headerView) {
            headerView.getRefreshView().setBackgroundResource(resid);
        }
    }

    private static class GetRefreshHeaderView implements IRefreshView {
        private AnimationDrawable animDrawable;
        private View headerView;
        private ImageView imgAnim;
        private Context mContext;
        private int mFrameCount;
        private int mCurFrameNum;
        private boolean mRunning;

        public GetRefreshHeaderView(Context context) {
            this.mContext = context;
            this.headerView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, null);
            this.imgAnim = (ImageView) this.headerView.findViewById(R.id.ivAnim);
            this.animDrawable = (AnimationDrawable) this.imgAnim.getDrawable();
            mFrameCount = animDrawable.getNumberOfFrames();
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(50.0f));
            params.gravity = Gravity.CENTER;
            this.headerView.setLayoutParams(params);
        }

        public int getStartRefreshDistance() {
            return dp2px(50.0f);
        }

        public View getRefreshView() {
            return this.headerView;
        }

        public void onStart(PullRefreshLayout pullRefreshLayout) {
            this.animDrawable.setVisible(true, true);//重置起始帧
            mCurFrameNum = 0;
            pullCount = 0;
        }

        public void onComplete(PullRefreshLayout pullRefreshLayout, boolean hasMoreData) {
            this.animDrawable.stop();
        }

        private int pullCount = 0;

        public void onPull(PullRefreshLayout pullRefreshLayout, float percent) {
            if (percent >= 1.0f) {
                if (!mRunning) {
                    mRunning = true;
                    this.animDrawable.run();
                }
            } else {
                if (mRunning) {
                    mRunning = false;
                    this.animDrawable.stop();
                    mCurFrameNum = 0;
                    pullCount = 0;
                } else {
                    pullCount++;
                    if (pullCount >= 4) {//控制节奏
                        pullCount = 0;
                        mCurFrameNum++;
                        int num = mCurFrameNum % mFrameCount;
                        this.animDrawable.selectDrawable(num);
                    }
                }
            }
        }

        public void onRefresh(PullRefreshLayout pullRefreshLayout) {
            this.animDrawable.run();
        }

        private int dp2px(float dp) {
            return (int) TypedValue.applyDimension(1, dp, this.mContext.getResources().getDisplayMetrics());
        }
    }
}
