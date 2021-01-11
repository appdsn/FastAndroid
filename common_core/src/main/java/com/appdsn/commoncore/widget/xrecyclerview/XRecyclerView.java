package com.appdsn.commoncore.widget.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdsn.commoncore.widget.xrecyclerview.loadmore.DefaultLoadView;
import com.appdsn.commoncore.widget.xrecyclerview.loadmore.ILoadView;
import com.appdsn.commoncore.widget.xrecyclerview.loadmore.LoadState;
import com.appdsn.commoncore.widget.xrecyclerview.loadmore.OnLoadListener;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Desc:添加HeaderView,FooterView,自动加载更多，优化Adapter代码，自定义ViewHolder
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:09
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class XRecyclerView extends RecyclerView {
    /*第一：headerview和footerview相关*/
    private WrapAdapter mWrapAdapter;
    private AdapterDataObserver mDataObserver;
    private View mHeaderView;
    private View mFooterView;

    /*第二：设置点击，长按事件*/
    private OnItemClickListener mOnItemClickListener;

    /*第三：自动加载更多相关*/
    private OnLoadListener mLoadListener;// 下拉刷新和加载更多的监听器
    private ILoadView mLoadView;//用于滑到底部自动加载的Footer
    private View mLoadLayout;
    private LoadState mCurState = LoadState.NORMAL;//当前的状态
    public boolean mLoadEnabled;
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;
    private int mPreLoadNumber = 1;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);// 调用基类构造方法，初始化一些配置方法
        mDataObserver = new DataObserver();
        setLayoutManager(new LinearLayoutManager(context));//默认值
        //设置Item增加、移除动画
        setItemAnimator(new DefaultItemAnimator());
//        setScrollListener();
    }

    private void setScrollListener() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLoadEnabled && mCurState == LoadState.NORMAL && isLastItemVisible()) {
                    startLoading();// 开始加载
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void autoLoadMore(int position) {
        if (!mLoadEnabled || mCurState != LoadState.NORMAL) {
            return;
        }
        if (position < mWrapAdapter.getItemCount() - mPreLoadNumber) {
            return;
        }
        if (null == mLoadView || mLoadListener == null) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                startLoading();
            }
        });
    }

    /*自动加载更多相关*/
    public void setAutoLoadEnabled(boolean enabled) {
        mLoadEnabled = enabled;
        if (enabled) {
            if (mLoadView == null) {
                setCustomLoadView(new DefaultLoadView());
            } else {
                setFooterView(mLoadLayout);
            }
        } else {
            removeFooterView();
        }
    }

    public void setPreLoadNumber(int preLoadNumber) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber;
        }
    }

    /*以下是HeaderView和FooterView的逻辑*/
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        if (headerView.getLayoutParams() == null) {
            mHeaderView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        requestLayout();
    }

    public void removeHeaderView() {
        mHeaderView = null;
        requestLayout();
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        if (footerView.getLayoutParams() == null) {
            mFooterView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        requestLayout();
    }

    public void removeFooterView() {
        mFooterView = null;
        requestLayout();
    }

    public void setCustomLoadView(ILoadView loadView) {
        if (loadView != null) {
            mLoadView = loadView;
            mLoadLayout = LayoutInflater.from(getContext()).inflate(mLoadView.getLayoutResId(), this, false);
            mLoadView.initView(mLoadLayout);
            mLoadLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurState == LoadState.FAILED) {
                        startLoading();
                    }
                }
            });
            // 首次进入一个界面是刷新，加载更多设置为不可用，需要隐藏footer
            onStateChanged(LoadState.NORMAL);
            setAutoLoadEnabled(true);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        boolean onItemLongClick(View itemView, int position);
    }

    //mWrapAdapter才是正真的Adapter
    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter, this);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    public boolean isHeader(int position) {
        return mWrapAdapter.getItemViewType(position) == WrapAdapter.TYPE_HEADER;
    }

    public boolean isFooter(int position) {
        return mWrapAdapter.getItemViewType(position) == WrapAdapter.TYPE_FOOTER;
    }

    private class WrapAdapter extends Adapter<ViewHolder> {
        private final static int TYPE_HEADER = -1;// 头部--支持头部增加1个headerView
        private final static int TYPE_FOOTER = -2;// 底部--支持底部增加1个footerView
        private XRecyclerView mRecyclerView;
        private Adapter mOutAdapter;

        public WrapAdapter(Adapter adapter, XRecyclerView recyclerView) {
            this.mOutAdapter = adapter;
            this.mRecyclerView = recyclerView;
        }

        public Adapter getOriginalAdapter() {
            return this.mOutAdapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                if (mHeaderView.getParent() != null) {
                    ((ViewGroup) (mHeaderView.getParent())).removeView(mHeaderView);
                }
                return new CommonViewHolder(mHeaderView);
            }
            if (viewType == TYPE_FOOTER) {
                if (mFooterView.getParent() != null) {
                    ((ViewGroup) (mFooterView.getParent())).removeView(mFooterView);
                }
                return new CommonViewHolder(mFooterView);
            }
            ViewHolder viewHolder = mOutAdapter.onCreateViewHolder(parent, viewType);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            onBindViewHolder(holder, position, null);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position, List<Object> payloads) {
            if (mOutAdapter.getItemCount() > 0) {
                mRecyclerView.autoLoadMore(position);
            }
            int type = getItemViewType(position);
            if (type == TYPE_FOOTER || type == TYPE_HEADER) {
                return;
            }

            final int realPosition = getRealPosition(position);
            if (payloads.isEmpty()) {
                mOutAdapter.onBindViewHolder(holder, realPosition);
            } else {
                mOutAdapter.onBindViewHolder(holder, realPosition, payloads);
            }

            // 如果设置了回调，则设置点击事�?
            if (mOnItemClickListener != null) {
                View itemView;
                if (holder.itemView instanceof SlidingItemView) {
                    itemView = ((SlidingItemView) holder.itemView).getContentView();
                } else {
                    itemView = holder.itemView;
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, realPosition);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemClickListener.onItemLongClick(holder.itemView,
                                realPosition);
                    }
                });
            }
        }

        public int getRealPosition(int position) {
            return mHeaderView == null ? position : position - 1;
        }

        @Override
        public int getItemCount() {
            int size = mOutAdapter.getItemCount();
            if (mFooterView != null) {
                size++;
            }
            if (mHeaderView != null) {
                size++;
            }
            return size;
        }

        @Override
        public int getItemViewType(int position) {
            int startPosition = 0;
            int endPosition = getItemCount() - 1;
            if (position == startPosition && mHeaderView != null) {
                return TYPE_HEADER;
            } else if (position == endPosition && mFooterView != null) {
                return TYPE_FOOTER;
            }
            return mOutAdapter.getItemViewType(getRealPosition(position));//重写该方法分组扩
        }

        @Override
        public long getItemId(int position) {
            return mOutAdapter.getItemId(getRealPosition(position));
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mOutAdapter.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int type = getItemViewType(position);
                        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
                            return gridManager.getSpanCount();
                        }
                        if (mSpanSizeLookup != null) {
                            return mSpanSizeLookup.getSpanSize(position);
                        }
                        return 1;
                    }
                });
            }

        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mOutAdapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;
                int type = getItemViewType(holder.getLayoutPosition());
                if (type == TYPE_HEADER || type == TYPE_FOOTER) {
                    params.setFullSpan(true);
                    return;
                }

            }
            mOutAdapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            mOutAdapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            mOutAdapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return mOutAdapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mOutAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mOutAdapter.registerAdapterDataObserver(observer);
        }

    }

    /*以下是上拉自动加载更多的逻辑*/
    private void startLoading() {
        if (null != mLoadView && mLoadListener != null) {
            onStateChanged(LoadState.LOADING);// 设置底部状态，正在加载
            mLoadListener.onLoad(this);
        }
    }

    // 加载成功后，调用改变footer状态
    public void onLoadSuccess(boolean hasMoreData) {
        if (mLoadView != null) {
            if (hasMoreData) {
                onStateChanged(LoadState.NORMAL);
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isLastItemVisible() && mCurState == LoadState.NORMAL) {
//                            startLoading();// 开始加载
//                        }
//                    }
//                }, 0);

            } else {
                onStateChanged(LoadState.NO_MORE_DATA);
            }
        }
    }

    // 加载失败后，调用改变footer状态
    public void onLoadFailed() {
        if (mLoadView != null) {
            onStateChanged(LoadState.FAILED);
        }
    }

    public DefaultLoadView getDefaultLoadView() {
        if (mLoadView instanceof DefaultLoadView) {
            return (DefaultLoadView) mLoadView;
        }
        return null;
    }

    private void onStateChanged(LoadState curState) {
        mCurState = curState;
        mLoadView.onStateChanged(curState);
    }

    // 设置刷新的监听器
    public void setOnLoadListener(OnLoadListener loadListener) {
        mLoadListener = loadListener;
    }

    /**
     * 判断最后一个child是否完全显示出来 判断listview是否滑动到底部
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        LayoutManager layoutManager = getLayoutManager();
        int lastVisibleItemPosition = getLastVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount == 0) {// 這裡主要判断数据还没有加载进来时，不可以上拉，只能下拉刷新
            return false;
        }
//        Log.i("123", lastVisibleItemPosition+"-"+visibleItemCount+"-"+totalItemCount);
        if (lastVisibleItemPosition == totalItemCount - 1) {// 当最后一项可见时
            int index = visibleItemCount - 1;
            View lastVisibleChild = this.getChildAt(index);
//			Log.i("123", lastVisibleChild.getBottom()+"-"+this.getBottom());
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= this.getBottom();// 仅试验有可能小于一个像素值，就是下线条所占据的。每个item都有一个下线条，没有上线条
            }
        }
        return false;
    }

    private int getLastVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        int lastVisibleItemPosition = -1;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();

        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastScrollPositions = new int[staggeredGridLayoutManager
                    .getSpanCount()];
            staggeredGridLayoutManager
                    .findLastVisibleItemPositions(lastScrollPositions);
            int max = Integer.MIN_VALUE;
            for (int value : lastScrollPositions) {
                if (value > max) {
                    max = value;
                }
            }
            lastVisibleItemPosition = max;
        }

        return lastVisibleItemPosition;
    }
}
