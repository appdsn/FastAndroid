package com.appdsn.commoncore.widget.smartindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.appdsn.commoncore.widget.smartindicator.adapter.IndicatorAdapter;
import com.appdsn.commoncore.widget.smartindicator.model.TabPositionInfo;
import com.appdsn.commoncore.widget.smartindicator.scrollbar.IScrollBar;
import com.appdsn.commoncore.widget.smartindicator.tabview.ITabView;
import com.appdsn.commoncore.widget.smartindicator.tabview.TextTabView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 智能tab指示器
 *
 * @Author: wangbaozhong
 * @Date: 2019/11/29 15:35
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class SmartIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private FrameLayout mRootLayout;
    protected ViewPager mViewPager;
    private IndicatorAdapter mIndicatorAdapter;
    private int mTabCount;
    private LinearLayout mTabViewLayout;
    private FrameLayout mScrollBarLayout;
    private IScrollBar mScrollBar;
    private int mScrollState;
    private float mSelectedPercent = 0.6f;
    private int mLastSelectedPosition;
    private int mTargetPosition;
    private boolean skipItem = true;
    private int mCurPosition = -1;
    private int mNextPosition = -1;
    /**
     * 提供给外部的参数配置
     */
    private OnTabClickListener mOnTabClickListener;
    private OnTabSelectedListener mOnTabSelectedListener;
    private boolean mScrollBarFront = false; // 指示器是否在title上层，默认为下层
    private boolean mIsFix;//是否数目固定
    private boolean mMeasureDouble;

    private boolean mEnablePivotScroll = true; // 启动中心点滚动
    private float mScrollPivotX = 0.5f;  // 滚动中心点 0.0f - 1.0f
    private boolean mFollowTouch = true;  // 是否手指跟随滚动
    protected boolean mSmoothScroll = true;// 是否平滑滚动
    private boolean mNeedRefresh;

    public SmartIndicator(Context context) {
        this(context, null);
    }

    public SmartIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        setHorizontalScrollBarEnabled(false);
        /*rootView:ScrollView的子view宽度只能是WRAP_CONTENT，设置其他宽度也会变成WRAP_CONTENT*/
        mRootLayout = new FrameLayout(context);
        LayoutParams rootParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(mRootLayout, rootParams);
        /*scrollbar：跟随mTabViewLayout的宽度，所以设置为MATCH_PARENT*/
        mScrollBarLayout = new FrameLayout(context);
        LayoutParams barParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRootLayout.addView(mScrollBarLayout, barParams);
        /*tabView：宽度可以自定义，默认WRAP_CONTENT*/
        mTabViewLayout = new LinearLayout(context);
        mTabViewLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams tabParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mRootLayout.addView(mTabViewLayout, tabParams);
    }

    /*滚动条覆盖方式有两种：在tabView的上面，或者后面*/
    public void setScrollBarFront(boolean isFront) {
        mScrollBarFront = isFront;
        if (mScrollBarFront) {
            mScrollBarLayout.getParent().bringChildToFront(mScrollBarLayout);
        } else {
            mScrollBarLayout.getParent().bringChildToFront(mTabViewLayout);
        }
    }

    public void setEnablePivotScroll(boolean enable) {
        mEnablePivotScroll = enable;
    }

    public void setScrollPivotX(float scrollPivotX) {
        mScrollPivotX = scrollPivotX;
    }

    /*设置是否平滑滚动ScrollBar，以及ViewPager的平滑切换，默认是开启的*/
    public void setSmoothScrollEnable(boolean isSmoothScroll) {
        this.mSmoothScroll = isSmoothScroll;
    }

    /*设置滑动过程中变成选中状态百分比，默认0.6*/
    public void setSelectedPercent(float changePercent) {
        if (changePercent > 0) {
            mSelectedPercent = changePercent;
        }
    }

    /*设置指示器的tab是否可滚动，或者是固定宽度的*/
    public void setFixEnableAsync(boolean isFix) {
        this.mIsFix = isFix;
        post(new Runnable() {
            @Override
            public void run() {
                if (mIsFix) {
                    mNeedRefresh = true;
                    int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
                    LayoutParams tabParams = new LayoutParams(width, LayoutParams.MATCH_PARENT);
                    mTabViewLayout.setLayoutParams(tabParams);
                }
            }
        });
    }

    public void setFixEnable(boolean isFix) {
        this.mIsFix = isFix;
        mMeasureDouble = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsFix && mMeasureDouble) {
            int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            LayoutParams tabParams = new LayoutParams(width, LayoutParams.MATCH_PARENT);
            mTabViewLayout.setLayoutParams(tabParams);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mMeasureDouble = false;
        }
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    public View getTabView(int position) {
        return mTabViewLayout.getChildAt(position);
    }

    public int getTabCount() {
        return mTabCount;
    }

    public IScrollBar getScrollBar() {
        return mScrollBar;
    }

    /*将指示器和ViewPager绑定，实现联动效果（当然也可以不绑定，单独使用指示器）*/
    public void bindViewPager(ViewPager viewPager) {
        bindViewPager(viewPager, 0);
    }

    public void bindViewPager(ViewPager viewPager, int position) {
        if (viewPager == null) {
            return;
        }
        if (viewPager.getAdapter() == null) {
            throw new RuntimeException("viewpager adapter can not be null");
        }
        mViewPager = viewPager;
        if (position < 0 || position >= mViewPager.getAdapter().getCount()) {
            position = 0;
        }
        mTargetPosition = position;
        mViewPager.addOnPageChangeListener(this);
        /*ViewPager数据改变时，要同步刷新指示器的数据*/
        mViewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mTargetPosition = mViewPager.getCurrentItem();
                /*如果之前初始化过IndicatorAdapter，只有数量不相等时才需要刷新指示器*/
                if (mIndicatorAdapter != null && mTabCount != mViewPager.getAdapter().getCount()) {
                    refreshIndicatorView();
                }
            }
        });
        /*如果之前初始化过IndicatorAdapter，只有数量不相等时才需要刷新指示器*/
        if (mIndicatorAdapter != null && mTabCount != mViewPager.getAdapter().getCount()) {
            refreshIndicatorView();
        }
        requestLayout();
    }

    /*设置指示器数据适配器：包括，指示器的tabView数量，以及每个自定义的tabview，滑块样式，滑动过程的监听*/
    public void setAdapter(IndicatorAdapter adapter) {
        mIndicatorAdapter = adapter;
        refreshIndicatorView();
    }

    /*刷新指示器数据*/
    public void notifyDataSetChanged() {
        refreshIndicatorView();
    }

    /*刷新指示器的数据*/
    private void refreshIndicatorView() {
        mNeedRefresh = false;
        mTabViewLayout.removeAllViews();
        mScrollBarLayout.removeAllViews();
        if (mIndicatorAdapter == null) {
            mTabCount = 0;
            return;
        }
        mIndicatorAdapter.onAttachToIndicator(getContext(), this);
        /*init TabView*/
        mTabCount = mIndicatorAdapter.getTabCount();
        //数据校验，如果绑定了ViewPager强制tab的数量和它保持一致
        if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() != mTabCount) {
            mTabCount = mViewPager.getAdapter().getCount();
        }
        if (mTabCount <= 0) {
            return;
        }
        if (mTargetPosition >= mTabCount) {
            mTargetPosition = mTabCount - 1;
        }
        mLastSelectedPosition = mTargetPosition;
        for (int i = 0; i < mTabCount; i++) {
            ITabView tab = mIndicatorAdapter.getTabView(getContext(), i, mTabViewLayout);
            if (tab == null) {//如果为空，给一个空tab（防止空指针异常）
                tab = new TextTabView();
            }
            View tabView = tab.createTabView(getContext(), i, mTabViewLayout);
            if (mTargetPosition == i) {
                tab.onSelected(tabView, i, null);
            } else {
                tab.onDeselected(tabView, i);
            }
            tabView.setTag(tab);//tab的索引位置
            tabView.setFocusable(true);
            tabView.setOnClickListener(mTabClickListener);
            LinearLayout.LayoutParams layoutParams;
            if (tabView.getLayoutParams() != null) {
                layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
            } else {
                //tab高度和指示器高度一致，宽度：固定的是平分宽度，滚动的是wrap_content
                if (mIsFix) {
                    layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT, 1);
                } else {
                    layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
                }
            }
            mTabViewLayout.addView(tabView, layoutParams);
        }
        /*init ScrollBar*/
        mScrollBar = mIndicatorAdapter.getScrollBar(getContext());
        if (mScrollBar != null && mScrollBar instanceof View) {
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mScrollBarLayout.addView((View) mScrollBar, lp);
            ((View) mScrollBar).setClickable(false);//禁用触摸事件，以防其在tabview上方时，tabview不能点击
        }
        mNeedRefresh = true;
    }

    /*选中某个tab，可以直接调用这个方法由代码设置选中，或者手指点击选中*/
    public void setCurrentTab(final int position) {
        if (position < 0 || position >= mTabCount || mLastSelectedPosition == position || mIndicatorAdapter == null) {
            return;
        }
        if (mViewPager != null) {
            if (mSmoothScroll) {
                mViewPager.setCurrentItem(position, true);
            } else {
                mViewPager.setCurrentItem(position, false);
            }
        } else {
            /*支持非ViewPager情况下使用*/
            final int originPosition = mLastSelectedPosition;
            onPageSelected(position);
            if (mSmoothScroll) {
                /*模仿平滑滚动改变TabView*/
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(150);//默认滑动块的滑动时间
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        float positionOffset = (Float) animator.getAnimatedValue();

                        int curPosition;
                        int nextPosition;
                        boolean leftToRight = false;
                        if (mLastSelectedPosition == originPosition) {
                            if (mLastSelectedPosition < position) {
                                leftToRight = true;
                                curPosition = originPosition;
                                nextPosition = position;
                            } else {
                                curPosition = position;
                                nextPosition = originPosition;
                                positionOffset = 1 - positionOffset;
                            }
                        } else {
                            if (mLastSelectedPosition < originPosition) {
                                leftToRight = true;
                                curPosition = position;
                                nextPosition = originPosition;
                                positionOffset = 1 - positionOffset;
                            } else {
                                curPosition = originPosition;
                                nextPosition = position;
                            }
                        }

                        changeTabViewAndScrollBar(curPosition, nextPosition, positionOffset, leftToRight);
                    }
                });
                animator.start();
            } else {
                onPageScrolled(position, 0, 0);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mNeedRefresh || changed) {
            mNeedRefresh = false;
            if (mScrollBar != null) {
                View curView = mTabViewLayout.getChildAt(mLastSelectedPosition);
                TabPositionInfo curTabInfo = new TabPositionInfo(curView.getLeft(), curView.getTop()
                        , curView.getRight(), curView.getBottom());
                mScrollBar.onSelected(curTabInfo);
            }

            if (mTargetPosition != mLastSelectedPosition) {
                mNeedRefresh = true;
            }
            if (mViewPager != null && mViewPager.getCurrentItem() != mTargetPosition) {
                mViewPager.setCurrentItem(mTargetPosition, false);
            } else {
                /*初始化选中的tab及ScrollBar*/
                onPageSelected(mTargetPosition);
                onPageScrolled(mTargetPosition, 0, 0);
            }
        }
    }

    /*只有手指滚动ViewPager时候才会调用,isClicked判断是否为手指滚动，还是点击tabView带来的滚动
     * 如果是点击tab带来的滚动，则不用在onPageScrolled时再滚动tab了*/
    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.i("123", "onPageScrollStateChanged" + state);
        mScrollState = state;
    }

    /*setCurrentItem后会立即调用（当然新position和当前不相同），setAdapter不会调用*/
    @Override
    public void onPageSelected(int position) {
//        Log.i("123", "onPageSelected" + position);
        mScrollState = ScrollState.SCROLL_STATE_SELECTED;
        mTargetPosition = position;
        selectAndScrollTabView(position);
    }

    /*只要每次setCurrentItem的position不同都会调用，setAdapter初始化会调用一次，所以要判断一下mScrollState*/
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
//        Log.i("123", "onPageScrolled" + position + "_" + positionOffset + "_" + mLastSelectedPosition);
//        Log.i("123", "onPageScrolled" + mCurPosition + "_" + mNextPosition );
        if (skipItem && !(position == mTargetPosition || position + 1 == mTargetPosition)) {
            return;
        }
        int curPosition = 0;
        int nextPosition = 0;
        boolean leftToRight = false;
        if (mCurPosition >= 0 && mNextPosition >= 0) {//之前已经有在变动的tab
            if (position == mCurPosition || position + 1 == mNextPosition) {
                if (mLastSelectedPosition == mCurPosition) {
                    leftToRight = true;
                }
                curPosition = mCurPosition;
                nextPosition = mNextPosition;
            } else if (position == mNextPosition && positionOffset == 0) {
                curPosition = mCurPosition;
                nextPosition = mNextPosition;
                positionOffset = 1;
                leftToRight = true;
            } else {
                if (position < mLastSelectedPosition) {
                    curPosition = position;
                    nextPosition = mLastSelectedPosition;
                    leftToRight = false;
                } else if (position > mLastSelectedPosition) {
                    curPosition = mLastSelectedPosition;
                    nextPosition = position;
                    leftToRight = true;
                } else {
                    curPosition = position;
                    nextPosition = position + 1;
                    leftToRight = true;
                }

                int cancelPosition = mCurPosition;
                if (mLastSelectedPosition == mCurPosition) {
                    cancelPosition = mNextPosition;
                }

                View cancelView = mTabViewLayout.getChildAt(cancelPosition);
                TabPositionInfo curTabInfo = new TabPositionInfo(cancelView.getLeft(), cancelView.getTop(), cancelView.getRight(), cancelView.getBottom());
                ((ITabView) (cancelView.getTag())).onScroll(cancelView, cancelPosition, 0, new RectF());
            }

        } else {//新的开始
            if (mLastSelectedPosition <= position) {
                leftToRight = true;
            }
            if (leftToRight) {
                if (positionOffset == 0) {
                    if (position != mLastSelectedPosition) {
                        curPosition = mLastSelectedPosition;
                        nextPosition = position;
                        positionOffset = 1;
                    } else {
                        if (position == 0) {//第一个
                            if (mTabCount == 1) {//只有一个数据时
                                curPosition = position;
                                nextPosition = position;
                                positionOffset = 0;
                            } else {
                                curPosition = position;
                                nextPosition = position + 1;
                                positionOffset = 0;
                            }
                        } else { //最后一个
                            curPosition = position - 1;
                            nextPosition = position;
                            positionOffset = 1;
                        }
                    }
                } else {
                    curPosition = mLastSelectedPosition;
                    nextPosition = position + 1;
                }
            } else {
                curPosition = position;
                nextPosition = mLastSelectedPosition;
            }
        }
        changeTabViewAndScrollBar(curPosition, nextPosition, positionOffset, leftToRight);
    }

    /*两个tab之间切换时的联动过渡状态,以及滑块的移动*/
    private void changeTabViewAndScrollBar(int curPosition, int nextPosition, float positionOffset, boolean leftToRight) {
        if (mIndicatorAdapter == null || mTabCount <= 0) {
            return;
        }
//        Log.i("123", "changeTabViewAndScrollBar" + curPosition + "_" + nextPosition + "_" + positionOffset + leftToRight);
        mCurPosition = curPosition;
        mNextPosition = nextPosition;
        View curView = mTabViewLayout.getChildAt(curPosition);
        View nextView = mTabViewLayout.getChildAt(nextPosition);
        RectF barRectF = null;
        if (leftToRight) {//滑块左边到右边
            if (positionOffset >= mSelectedPercent) {
                if (mLastSelectedPosition != nextPosition) {
                    if (mScrollBar != null) {
                        TabPositionInfo nextTabInfo = new TabPositionInfo(nextView.getLeft(), nextView.getTop(), nextView.getRight(), nextView.getBottom());
                        barRectF = mScrollBar.onSelected(nextTabInfo);
                    }
                    /*处理选中nextPosition，不选中当前position*/
                    ((ITabView) (curView.getTag())).onDeselected(curView, curPosition);
                    ((ITabView) (nextView.getTag())).onSelected(nextView, nextPosition, barRectF);
                    if (mOnTabSelectedListener != null) {
                        mOnTabSelectedListener.onDeselected(curView, curPosition);
                        mOnTabSelectedListener.onSelected(nextView, nextPosition);
                    }

                    mLastSelectedPosition = nextPosition;
                }
            }

            if (positionOffset == 1 || positionOffset == 0) {
                mCurPosition = -1;
                mNextPosition = -1;
            }
        } else {
            if (positionOffset <= 1 - mSelectedPercent) {
                if (mLastSelectedPosition != curPosition) {
                    if (mScrollBar != null) {
                        TabPositionInfo curTabInfo = new TabPositionInfo(curView.getLeft(), curView.getTop(), curView.getRight(), curView.getBottom());
                        barRectF = mScrollBar.onSelected(curTabInfo);
                    }
                    /*处理选中position，不选中当前nextPosition*/
                    ((ITabView) (nextView.getTag())).onDeselected(nextView, nextPosition);
                    ((ITabView) (curView.getTag())).onSelected(curView, curPosition, barRectF);

                    if (mOnTabSelectedListener != null) {
                        mOnTabSelectedListener.onDeselected(nextView, nextPosition);
                        mOnTabSelectedListener.onSelected(curView, curPosition);
                    }
                    mLastSelectedPosition = curPosition;
                }
            }

            if (positionOffset == 0) {
                mCurPosition = -1;
                mNextPosition = -1;
            }
        }

        TabPositionInfo curTabInfo = new TabPositionInfo(curView.getLeft(), curView.getTop(), curView.getRight(), curView.getBottom());
        TabPositionInfo nextTabInfo = new TabPositionInfo(nextView.getLeft(), nextView.getTop(), nextView.getRight(), nextView.getBottom());
        if (mScrollBar != null) {
            barRectF = mScrollBar.onScroll(curTabInfo, nextTabInfo, positionOffset);
        }

        ((ITabView) (curView.getTag())).onScroll(curView, curPosition, 1 - positionOffset, barRectF);
        if (curView != nextView) {
            ((ITabView) (nextView.getTag())).onScroll(nextView, nextPosition, positionOffset, barRectF);
        }

        // 手指跟随滚动
        if (mFollowTouch) {
            if (mEnablePivotScroll) {
                float scrollTo = curTabInfo.getCenterX() - getWidth() * mScrollPivotX;
                float nextScrollTo = nextTabInfo.getCenterX() - getWidth() * mScrollPivotX;
                scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
            } else {
                //如果当前项被部分遮挡，则滚动显示完全
                float scrollTo = curTabInfo.getRight() - getWidth();
                float nextScrollTo = nextTabInfo.getRight() - getWidth();
                scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
            }
        }
    }

    /*移动当前tab到中间，并设置当前tab为选中状态*/
    private void selectAndScrollTabView(int position) {
        if (mIndicatorAdapter != null && mTabCount > 0) {
            scrollTabToPivotX(position);//将选中的tab移动到中间
            for (int i = 0; i < mTabCount; i++) {
                View child = mTabViewLayout.getChildAt(i);
                boolean isSelected = (i == position);//将选中的tab设置为选中状态
                child.setSelected(isSelected);
                child.setActivated(isSelected);//解决第一个默认Selected时不起效果，可用Activated属性代替
            }
        }
    }

    private void scrollTabToPivotX(int position) {
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (mTabViewLayout.getMeasuredWidth() <= width) {
            return;//内容太少不需要滚动
        }
        if (!mFollowTouch) {
            View tabView = mTabViewLayout.getChildAt(position);
            if (mEnablePivotScroll) {
                int scrollPos = (int) ((tabView.getLeft() + tabView.getWidth() / 2) - getWidth() * mScrollPivotX);
                if (mSmoothScroll) {
                    smoothScrollTo(scrollPos, 0);
                } else {
                    scrollTo(scrollPos, 0);
                }
            } else {
                // 如果当前项被部分遮挡，则滚动显示完全
                if (getScrollX() > tabView.getLeft()) {
                    if (mSmoothScroll) {
                        smoothScrollTo(tabView.getLeft(), 0);
                    } else {
                        scrollTo(tabView.getLeft(), 0);
                    }
                } else if (getScrollX() + getWidth() < tabView.getRight()) {
                    if (mSmoothScroll) {
                        smoothScrollTo(tabView.getRight() - getWidth(), 0);
                    } else {
                        scrollTo(tabView.getRight() - getWidth(), 0);
                    }
                }
            }
        }
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = mTabViewLayout.indexOfChild(view);
            if (mOnTabClickListener != null) {//外部可以设置监听，写自己的逻辑
                if (mOnTabClickListener.onClick(view, position)) {
                    setCurrentTab(position);
                }
            } else {
                setCurrentTab(position);
            }
        }
    };

    public void performClick(int tabPosition) {
        if (tabPosition >= 0 && tabPosition < mTabViewLayout.getChildCount()) {
            View curView = mTabViewLayout.getChildAt(tabPosition);
            curView.performClick();
        }
    }

    public void setOnTabClickListener(OnTabClickListener onClickListener) {
        this.mOnTabClickListener = onClickListener;
    }

    public interface OnTabClickListener {
        boolean onClick(View tabView, int position);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }

    public interface OnTabSelectedListener {
        void onSelected(View tabView, int position);

        void onDeselected(View tabView, int position);
    }

    public interface ScrollState {
        int SCROLL_STATE_IDLE = 0;
        int SCROLL_STATE_DRAGGING = 1;
        int SCROLL_STATE_SETTLING = 2;
        int SCROLL_STATE_SELECTED = 3;
    }
}
