package com.appdsn.commoncore.widget.xrecyclerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * @ClassName: GridSpacingItemDecoration
 * @Author: chenzhong
 * @Description:
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private boolean includeEdge;
    private int[] spacings = new int[4];//[left,top,right,bottom]

    public GridSpacingItemDecoration(int spacing) {
        this(spacing, false);
    }

    public GridSpacingItemDecoration(int spacing, boolean includeEdge) {

        this(spacing, spacing, spacing, spacing, includeEdge);
    }

    public GridSpacingItemDecoration(int left_spacing, int top_spacing, int right_spacing,
                                     int bottom_spacing, boolean includeEdge) {
        spacings[0] = left_spacing;
        spacings[1] = top_spacing;
        spacings[2] = right_spacing;
        spacings[3] = bottom_spacing;
        this.includeEdge = includeEdge;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final GridLayoutManager.SpanSizeLookup lookup = layoutManager.getSpanSizeLookup();

        int spanCount = layoutManager.getSpanCount();
        int itemCount = parent.getAdapter().getItemCount();
        int lastGroupIndex = layoutManager.getSpanSizeLookup().getSpanGroupIndex(itemCount - 1, spanCount);//最后一排序号
        //判断元素是否为最后一排
        boolean isLastRow = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == lastGroupIndex;

        //获取item所占有的比重
        final int spanSize = lookup.getSpanSize(position);
        //获取每排的位置
        final int spanIndex = lookup.getSpanIndex(position, spanCount);


        if (spanSize == spanCount) { //一行一个整个item的样式，我们不需要分割线（标题类的）
            outRect.set(0, 0, 0, 0);

        } else {
            int spacing_horizontal = (spacings[0] + spacings[2]) / 2;
            int spacing_vertical = (spacings[1] + spacings[3]) / 2;

            if (includeEdge) {

                outRect.top = spacing_vertical;
                if (isLastRow) { //最后一行
                    outRect.bottom = spacing_vertical;
                }

                outRect.left = spacing_horizontal - spanIndex * spacing_horizontal / spanCount;
                outRect.right = (spanIndex + 1) * spacing_horizontal / spanCount;


            } else {
                outRect.left = spanIndex * spacing_horizontal / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing_horizontal - (spanIndex + 1) * spacing_horizontal / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

                if (!isLastRow) { //不是最后一行
                    outRect.bottom = spacing_vertical;

                }
            }

        }


    }
}
