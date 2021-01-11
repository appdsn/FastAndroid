package com.appdsn.commoncore.widget.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:支持多布局类型，侧滑菜单
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/30 10:11
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public abstract class BaseRecyclerAdapter<Model> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<Model> mDatas = new ArrayList<>();
    protected LayoutInflater mInflater;
    private SlidingMenuViewCreator mSlidingMenuCreator;
    private SpanSizeSupport<Model> mSpanSizeSupport;

    /*单布局样式*/
    public BaseRecyclerAdapter(Context context) {
        this(context, null);

    }

    /*单布局样式*/
    public BaseRecyclerAdapter(Context context, List<Model> datas) {
        this.mContext = context;
        if (datas != null) {
            this.mDatas = datas;
        }
        this.mInflater = LayoutInflater.from(mContext);
    }

    /*侧滑菜单*/
    public void setSlidingMenuCreator(SlidingMenuViewCreator slidingMenuCreator) {
        this.mSlidingMenuCreator = slidingMenuCreator;
    }

    /*span*/
    public void setSpanSizeSupport(SpanSizeSupport spanSizeSupport) {
        this.mSpanSizeSupport = spanSizeSupport;
    }

    public Model getItemData(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getItemData(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View itemView = mInflater.inflate(getLayoutResId(viewType), parent, false);
        /*侧滑菜单*/
        if (mSlidingMenuCreator != null) {
            View menuView = mSlidingMenuCreator.onCreateItemMenuView(mContext, viewType);
            if (menuView != null) {
                SlidingItemView slidingItemView = new SlidingItemView(mContext);
                slidingItemView.addContentView(itemView);
                slidingItemView.addMenuView(menuView);
                itemView = slidingItemView;
            }
        }
        holder = onCreateViewHolder(viewType, itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.itemView instanceof SlidingItemView) {
            SlidingItemView itemView = (SlidingItemView) holder.itemView;
            if (mSlidingMenuCreator != null) {
                mSlidingMenuCreator.onBindItemMenuView(itemView.getMenuView(), getItemData(position), position);
            }
        }
        convert((CommonViewHolder) holder, getItemData(position), position);
    }

    public abstract int getLayoutResId(int viewType);

    public abstract int getItemViewType(int position, Model itemData);

    public abstract void convert(CommonViewHolder holder, Model itemData, int position);

    /**
     * 也可以重写该方法，根据不同的类型，来自定义不同的Holder（必须继承CommonViewHolder），默认是CommonViewHolder
     */
    public CommonViewHolder onCreateViewHolder(int viewType, View itemView) {
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            if (mSpanSizeSupport != null) {
                params.setFullSpan(mSpanSizeSupport.isFullSpan(position, getItemData(position)));
            } else {
                params.setFullSpan(false);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView instanceof XRecyclerView) {
            XRecyclerView xRecyclerView = (XRecyclerView) recyclerView;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            final int spanCount;
            if (manager instanceof GridLayoutManager) {
                GridLayoutManager gridManager = ((GridLayoutManager) manager);
                spanCount = gridManager.getSpanCount();
            } else {
                spanCount = 0;
            }
            xRecyclerView.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSpanSizeSupport != null) {
                        int spanSize = mSpanSizeSupport.getSpanSize(position, getItemData(position), spanCount);
                        return spanSize;
                    }
                    return 1;
                }
            });
        }
    }

    public static abstract class SpanSizeSupport<Model> {
        /**
         * 结合StaggeredGridLayoutManager设置每个item是否占据整行，默认false
         */
        public boolean isFullSpan(int position, Model itemData) {
            return false;
        }

        /**
         * 结合GridLayoutManager设置每个item占据多少个单元格，默认是1，最多是spanCount
         */
        public int getSpanSize(int position, Model itemData, int allSize) {
            return 1;
        }
    }

    public interface SlidingMenuViewCreator<Model> {
        /*创建侧滑菜单View*/
        View onCreateItemMenuView(Context context, int viewType);

        /*可以给不同position的菜单项添加不同的数据，或者加点击事件等*/
        void onBindItemMenuView(View menuView, Model itemData, int position);
    }

    public void setData(List<? extends Model> datas) {
        if (datas != null) {
            if (datas != mDatas) {
                mDatas.clear();
                mDatas.addAll(datas);
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void addData(List<? extends Model> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void addData(Model data) {
        if (data != null) {
            mDatas.add(data);
            notifyDataSetChanged();
        }
    }

    public List<Model> getData() {
        return mDatas;
    }

    /**
     * 插入1个数
     */
    public void insert(int position, Model model) {
        if (position < 0) {
            position = 0;
        }

        if (position > mDatas.size()) {
            position = mDatas.size() - 1;
        }
        mDatas.add(position, model);
        notifyItemInserted(position);
    }

    /**
     * 加入1个数据
     */
    public void append(Model model) {
        mDatas.add(model);
        notifyItemInserted(mDatas.size() - 1);
    }

    /**
     * 移除1个数
     *
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除所有的数据
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }


}
