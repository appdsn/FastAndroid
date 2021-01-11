package com.appdsn.commoncore.widget.xrecyclerview.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appdsn.commoncore.utils.log.LogUtils;

import java.util.List;

/**
 * @ClassName: ListDividerItemDecoration
 * @Author: chenzhong
 * @Description:
 */
public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};


    private List<Integer> needDrawItems;

    private List<Integer> needDrawSpaceItems;

    private int lineHeight;//横线的高度

    private int padding; //线的padding 横向取 left right ，纵向线取top bottom，值都一样

    private boolean isDrawSpace;

    private int spaceHeight;

    private int spacePadding;


    private Drawable mDivider;
    private Drawable mSpaceDrawable;

    protected boolean mShowLastDivider;

    public boolean ismShowLastDivider() {
        return mShowLastDivider;
    }

    public void setmShowLastDivider(boolean mShowLastDivider) {
        this.mShowLastDivider = mShowLastDivider;
    }

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();


    public ListDividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            LogUtils.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                    + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        setOrientation(orientation);
    }


    public ListDividerItemDecoration(int lineHeight, int padding, @ColorInt int color, int orientation) {

        this.lineHeight = lineHeight;
        this.padding = padding;

        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
        setOrientation(orientation);

    }

    /**
     * spacecColor, spaceHeight分组标题上的空隙
     */
    public ListDividerItemDecoration(int lineHeight, int padding, @ColorInt int color,
                                     int spaceHeight, @ColorInt int spacecColor, int orientation) {
        this.lineHeight = lineHeight;
        this.padding = padding;
        this.spaceHeight = spaceHeight;
        setOrientation(orientation);

        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
        if (spacecColor != 0 && spaceHeight != 0) {
            isDrawSpace = true;
            if (spacecColor != 0) {
                mSpaceDrawable = new ColorDrawable(spacecColor);
            }

        }

    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }


    public void setDividerColor(@ColorInt int color) {
        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
    }

    public void setSpaceDividerColor(@ColorInt int color) {
        if (color != 0) {
            mSpaceDrawable = new ColorDrawable(color);
        }
    }

    public void setNeedDrawItems(List<Integer> needDrawItems) {
        this.needDrawItems = needDrawItems;
    }

    public void setNeedDrawSpaceItems(List<Integer> needDrawSpaceItems) {
        this.needDrawSpaceItems = needDrawSpaceItems;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top, bottom, left, right;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            int type = parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(child));

            if (isDrawSpace) {

                if (mSpaceDrawable != null && needDrawSpaceItems != null && needDrawSpaceItems.contains(type)) {
                    left = child.getLeft() + spacePadding;
                    int width = parent.getWidth();
                    right = width - spacePadding;
                    top = child.getTop() - spaceHeight;
                    bottom = top + spaceHeight;
                    mSpaceDrawable.setBounds(left, top, right, bottom);
                    mSpaceDrawable.draw(canvas);
                }

            }
            //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                canvas.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            left = left + padding;
            right = right - padding;

            parent.getDecoratedBoundsWithMargins(child, mBounds);
            bottom = mBounds.bottom + Math.round(child.getTranslationY());
            if (lineHeight > 0) {
                top = bottom - lineHeight;

            } else {
                top = bottom - mDivider.getIntrinsicHeight();

            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top, bottom, left, right;

        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            right = mBounds.right + Math.round(child.getTranslationX());
            left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        int itemCount = parent.getAdapter().getItemCount();


        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            int type = parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view));

            boolean needDrawItem = needDrawItems != null && needDrawItems.contains(type);
            boolean needDrawSpaceItem = isDrawSpace && needDrawSpaceItems != null && needDrawSpaceItems.contains(type);

            if (needDrawItem || needDrawSpaceItem) {

                if (needDrawSpaceItem) {
                    outRect.top = spaceHeight;

                    if (!needDrawItem) {
                        return;
                    }

                } else { //needDrawItem 一定是 true
                    outRect.top = 0;

                }

                if (needDrawSpaceItem) {
                    outRect.top = spaceHeight;
                } else {
                    outRect.top = 0;

                }

                if (!mShowLastDivider && position >= itemCount - 1) {
                    // Don't set item offset for last line if mShowLastDivider = false
                    return;
                }

                if (lineHeight > 0) {
                    outRect.bottom = lineHeight;

                } else {
                    outRect.bottom = mDivider.getIntrinsicHeight();

                }


                outRect.left = 0;
                outRect.right = 0;

            } else {
                outRect.set(0, 0, 0, 0);

            }

        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
