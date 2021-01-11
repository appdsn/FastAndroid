package com.appdsn.commoncore.widget.xrecyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appdsn.commoncore.widget.xrecyclerview.XRecyclerView;

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private int mLPadding;
    private int mRPadding;
    private Paint mPaint;

    public LinearItemDecoration(int height, int color) {
        mHeight = height;
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    public LinearItemDecoration(int height, int leftPadding, int rightPadding, int color) {
        mHeight = height;
        mLPadding = leftPadding;
        mRPadding = rightPadding;
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        XRecyclerView recyclerView = null;
        if (parent instanceof XRecyclerView) {
            recyclerView = (XRecyclerView) parent;
        }

        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final int top = child.getBottom();
            final int bottom = top + mHeight;

            int left = child.getLeft() + mLPadding;
            int right = child.getRight() - mRPadding;

            int position = parent.getChildAdapterPosition(child);
            c.save();
            if (recyclerView != null && (recyclerView.isHeader(position) || recyclerView.isFooter(position))) {
                c.drawRect(0, 0, 0, 0, mPaint);
            } else {
                c.drawRect(left, top, right, bottom, mPaint);
            }

            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        XRecyclerView recyclerView = null;
        if (parent instanceof XRecyclerView) {
            recyclerView = (XRecyclerView) parent;
        }
        int position = parent.getChildAdapterPosition(view);

        if (recyclerView != null && (recyclerView.isHeader(position) || recyclerView.isFooter(position))) {
            outRect.bottom = mHeight;
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, mHeight);
        }

    }
}
