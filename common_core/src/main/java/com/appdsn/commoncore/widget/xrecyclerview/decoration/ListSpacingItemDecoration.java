package com.appdsn.commoncore.widget.xrecyclerview.decoration;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;


/**
 * author: chenzhong
 * created on: 2020/9/2 16:44
 * description:
 */
public class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private int spacing;
    private int spacingEdge; //横向recycleview 两边的spacing
    protected boolean mShowLastDivider;//垂直recyclerview 使用
    private List<Integer> needDrawItems;


    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    /**
     * 垂直recycler view 使用
     */
    public ListSpacingItemDecoration(int spacing, boolean mShowLastDivider) {

        this(spacing, 0, mShowLastDivider, VERTICAL);

    }

    /**
     * 水平 recycler view 使用
     */
    public ListSpacingItemDecoration(int spacing, int spacingEdge) {
        this(spacing, spacingEdge, false, HORIZONTAL);

    }

    public ListSpacingItemDecoration(int spacing, int spacingEdge, boolean mShowLastDivider, int orientation) {
        this.spacing = spacing;
        this.spacingEdge = spacingEdge;
        this.mShowLastDivider = mShowLastDivider;
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    public void setNeedDrawItems(List<Integer> needDrawItems) {
        this.needDrawItems = needDrawItems;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        int itemCount = parent.getAdapter().getItemCount();

        int type = parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view));

        if (mOrientation == VERTICAL) {
            if (needDrawItems != null) { //设置了 需要画分割线的list
                if (needDrawItems.contains(type)) {
                    if (!mShowLastDivider && position >= itemCount - 1) {
                        // Don't set item offset for last line if mShowLastDivider = false
                        return;
                    }

                    outRect.bottom = spacing;
                } else {
                    outRect.bottom = 0;

                }
            } else { //没有设置list,默认为需要加载
                if (!mShowLastDivider && position >= itemCount - 1) {
                    // Don't set item offset for last line if mShowLastDivider = false
                    return;
                }

                outRect.bottom = spacing;
            }
        } else {

            if (position == 0) {
                outRect.left = spacingEdge;
                outRect.right = spacing / 2;
            } else {
                outRect.left = spacing / 2;
                if (position >= itemCount - 1) {
                    outRect.right = spacingEdge;

                } else {
                    outRect.right = spacing / 2;

                }

            }

        }

    }

}
